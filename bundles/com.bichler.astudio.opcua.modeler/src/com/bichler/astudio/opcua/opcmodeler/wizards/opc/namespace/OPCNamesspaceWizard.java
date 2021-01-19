package com.bichler.astudio.opcua.opcmodeler.wizards.opc.namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DeleteNodesItem;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.CreateFactory;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.node.OPCUANodesWizard;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.newApplication.NamespaceItem;
import opc.sdk.core.node.Node;

public class OPCNamesspaceWizard extends Wizard {
	// page
	private OPCNamespaceWizardPage namespaceTablePage = null;
	// namespace items to modify
	private NamespaceItem[] namespaceTable;
	// origin namespacetable before modifying
	private String[] nsServer;
	// nodes to remove if performing finish
	private List<Node[]> nodes2remove = null;

	public OPCNamesspaceWizard() {
		setWindowTitle(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"));
		this.nsServer = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
	}

	@Override
	public void addPages() {
		this.namespaceTablePage = new OPCNamespaceWizardPage(this.nsServer);
		addPage(this.namespaceTablePage);
	}

	@Override
	public boolean performFinish() {
		this.namespaceTable = this.namespaceTablePage.getNamespaces();
		// find indizes for uris to remove
		List<Integer> uris2remove = new ArrayList<>();
		for (int i = 0; i < this.nsServer.length; i++) {
			String ns = this.nsServer[i];
			boolean exist = false;
			for (NamespaceItem item : this.namespaceTable) {
				if (ns.equals(item.getOriginNamespace())) {
					exist = true;
					break;
				}
			}
			// remove not existing nodes in namespace
			if (!exist) {
				uris2remove.add(i);
			}
		}
		// nodes
		this.nodes2remove = new ArrayList<>();
		for (Integer index : uris2remove) {
			Node[] nodes = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getAllNodes(index);
			if (nodes.length > 0) {
				this.nodes2remove.add(nodes);
			}
		}
		boolean OK = true;
		if (!this.nodes2remove.isEmpty()) {
			// wizard only to display nodes to remove
			OPCUANodesWizard wizard = new OPCUANodesWizard(this.nodes2remove);
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
			OK = dialog.open() == WizardDialog.OK;
			if (OK) {
				// remove nodes from opc ua address space
				Map<NodeId, DeleteNodesItem> items2delete = new HashMap<>();
				List<Node> nodes2delete = new ArrayList<>();
				for (Node[] nodes : this.nodes2remove) {
					for (Node node2delete : nodes) {
						DeleteNodesItem item = new DeleteNodesItem();
						item.setDeleteTargetReferences(true);
						item.setNodeId(node2delete.getNodeId());
						items2delete.put(item.getNodeId(), item);
						nodes2delete.add(node2delete);
					}
				}
				// REMOVE nodes from OPC UA server properly
				try {
					CreateFactory.remove(nodes2delete.toArray(new Node[0]), items2delete);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return OK;
	}

	public NamespaceItem[] getNamespaceTable() {
		return this.namespaceTable;
	}

	public List<Node[]> getNodesToRemove() {
		return this.nodes2remove;
	}
}
