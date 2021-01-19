package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.WriteValue;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.namespace.OPCNamesspaceWizard;

import opc.sdk.core.newApplication.NamespaceItem;
import opc.sdk.core.node.Node;

public class NamespaceHandler extends AbstractHandler {
	public static final String ID = "commands.designer.namespace";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		OPCNamesspaceWizard wizard = new OPCNamesspaceWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		int open = dialog.open();
		if (WizardDialog.OK == open) {
			updateNamespaces(wizard);
		}
		return null;
	}

	public static void updateNamespaces(OPCNamesspaceWizard wizard) {
		// remove nodes from namespace
		beforeUpdate(wizard.getNodesToRemove());
		// operate namespace change
		NamespaceItem[] namespaceTable = wizard.getNamespaceTable();
		List<String> newNamespaces = new ArrayList<>();
		List<String> oldNamespaces = new ArrayList<>();
		// initialize namespace lists
		for (NamespaceItem bo : namespaceTable) {
			newNamespaces.add(bo.getNamespace());
			oldNamespaces.add(bo.getOriginNamespace());
		}
		// initialize arrays
		String[] namespaceArrays = newNamespaces.toArray(new String[0]);
		String[] namespaceIndizes2change = oldNamespaces.toArray(new String[0]);
		String[] uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		// something has changed
		if (!Arrays.equals(namespaceArrays, uris)) {
			try {
				// write namespace array node
				WriteValue value = new WriteValue();
				value.setAttributeId(Attributes.Value);
				value.setNodeId(Identifiers.Server_NamespaceArray);
				value.setValue(new DataValue(new Variant(namespaceArrays)));

				ServerInstance.getInstance().getServerInstance().getMaster().write(new WriteValue[] { value }, true,
						null, null);
				// change server namespace indizes
				ServerInstance.doChangeNamespaceTable(namespaceTable, NamespaceTable.createFromArray(namespaceArrays),
						NamespaceTable.createFromArray(namespaceIndizes2change));
				ModelBrowserView mbv = (ModelBrowserView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView(ModelBrowserView.ID);
				mbv.refresh();
				mbv.setDirty(true);
			} catch (ServiceResultException e) {
				Logger.getLogger(NamespaceHandler.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		}
	}

	public static void beforeUpdate(List<Node[]> nodesToRemove) {
		// remove opc ua nodes from removed namespaces
		for (Node[] nodes : nodesToRemove) {
			for (Node node : nodes) {
				NodeId nodeId = node.getNodeId();
				if (ServerInstance.getNode(nodeId) == null) {
					continue;
				}
				ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().deleteNode(node.getNodeId(),
						true);
			}
		}
	}
}
