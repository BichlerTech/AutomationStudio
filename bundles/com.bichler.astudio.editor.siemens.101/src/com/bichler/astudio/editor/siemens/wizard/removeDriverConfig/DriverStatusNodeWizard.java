package com.bichler.astudio.editor.siemens.wizard.removeDriverConfig;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DeleteNodesItem;

import com.bichler.astudio.editor.siemens.model.SiemensNamespaceItem;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.wizard.util.OPCWizardUtil;

public class DriverStatusNodeWizard extends Wizard {
	private DriverStatusNamespacePage namespacePage;
	private NodeId startNodeId;
	private NodeId rootId;
	private List<DeleteNodesItem> nodes2delete;
	private String drvName;

	public DriverStatusNodeWizard(String driverName) {
		setWindowTitle("New Wizard");
		this.drvName = driverName;
	}

	@Override
	public void addPages() {
		this.namespacePage = new DriverStatusNamespacePage();
		addPage(this.namespacePage);
	}

	@Override
	public boolean performFinish() {
		/**
		 * NAMESPACE
		 */
		SiemensNamespaceItem namespaceItem = this.namespacePage.getNamespaceItem();
		if (namespaceItem != null && !namespaceItem.isServerEntry()) {
			// Add new namespace to the server`s namespacetable
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			nsTable.add(namespaceItem.getNamespace());
			// set dirty for the OPC UA information model
			ModelBrowserView mbv = (ModelBrowserView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView(ModelBrowserView.ID);
			mbv.setDirty(true);
		}
		try {
			ServerInstance.getInstance().getServerInstance().getMaster()
					.deleteNodes(this.nodes2delete.toArray(new DeleteNodesItem[0]), null);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		try {
			this.rootId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					.toNodeId(OPCWizardUtil.newOPCUADriverModel(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), this.startNodeId,
							this.drvName, 1));
		} catch (ServiceResultException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setStartNodeId(NodeId startNodeId) {
		this.startNodeId = startNodeId;
	}

	public void setNodes2delete(List<DeleteNodesItem> nodes2delete) {
		this.nodes2delete = nodes2delete;
	}

	public NodeId getRootNodeId() {
		return this.rootId;
	}
}
