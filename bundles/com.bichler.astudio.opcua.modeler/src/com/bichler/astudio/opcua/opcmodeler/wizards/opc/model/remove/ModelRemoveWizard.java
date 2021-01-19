package com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.remove;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DeleteNodesItem;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.CreateFactory;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ModelRemoveWizard extends Wizard {
	private ModelRemoveElementPage pageOne;
	private ModelTypDef typeDef;
	private List<ExpandedNodeId> nodes2remove;
	private ModelTypDef[] input;

	public ModelRemoveWizard() {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.synchronization"));
	}

	@Override
	public void addPages() {
		this.pageOne = new ModelRemoveElementPage();
		this.pageOne.setTypeDef(this.typeDef);
		this.pageOne.setInput(this.input);
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		final Object changes = this.pageOne.getChanges();
		if (changes == null) {
			return true;
		}
		ProgressMonitorDialog progressDilog = new ProgressMonitorDialog(getShell());
		try {
			progressDilog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.modelchange.update")
									+ "...",
							IProgressMonitor.UNKNOWN);
					List<ExpandedNodeId> objectNodes2remove = new ArrayList<>();
					for (ModelTypDef updateDef : ((ModelTypDef[]) changes)) {
						// parent <=> typedef
						// ModelTypDef updateDef = (ModelTypDef) ((TableItem)
						// change)
						// .getData();
						updateDef.buildModelTree();
						typeDef.doCompareModelRemove(objectNodes2remove, nodes2remove, updateDef);
						// refresh.add(updateDef);
					}
					try {
						remove(objectNodes2remove);
					} catch (ServiceResultException e) {
						e.printStackTrace();
					}
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							ModelBrowserView part = DesignerUtils.refreshBrowserAll();
							part.setDirty(true);
						}
					});
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Job job = new Job("Change OPC UA informationmodell"){
		//
		// @Override
		// protected IStatus run(IProgressMonitor monitor) {
		// monitor.beginTask("Update OPC UA nodes...",
		// IProgressMonitor.UNKNOWN);
		// List<ExpandedNodeId> objectNodes2remove = new ArrayList<>();
		// for (ModelTypDef updateDef : ((ModelTypDef[]) changes)) {
		// // parent <=> typedef
		// // ModelTypDef updateDef = (ModelTypDef) ((TableItem)
		// // change)
		// // .getData();
		// updateDef.buildModelTree();
		// typeDef.doCompareModelRemove(objectNodes2remove, nodes2remove,
		// updateDef);
		// // refresh.add(updateDef);
		//
		// }
		// remove(objectNodes2remove);
		// Display.getDefault().syncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		// ModelBrowserView part = DesignerUtils.refreshBrowserAll();
		// part.setDirty(true);
		// }
		//
		// });
		// return Status.OK_STATUS;
		// }
		//
		// };
		//
		// job.setUser(true);
		// job.schedule();
		// eliminieren & parent finden (browsermodelnode)
		// refreshen
		return true;
	}

	private void remove(List<ExpandedNodeId> nodes2remove) throws ServiceResultException {
		List<DeleteNodesItem> delete = new ArrayList<>();
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		for (ExpandedNodeId node2remove : nodes2remove) {
			Node node = ServerInstance.getNode(node2remove);
			if (node == null) {
				continue;
			}
			try {
				List<DeleteNodesItem> nodesToDelete = new ArrayList<>();
				List<NodeId> preventLoop = new ArrayList<>();
				DesignerUtils.findAllNodesToDelete(preventLoop, nodesToDelete,
						ServerInstance.getInstance().getServerInstance().getTypeTable(), nsTable.toNodeId(node2remove));
				delete.addAll(nodesToDelete);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		CreateFactory.remove(delete.toArray(new DeleteNodesItem[0]));
	}

	public void setInput(ModelTypDef[] input) {
		this.input = input;
	}

	public void setTypeDef(ModelTypDef ptd) {
		this.typeDef = ptd;
	}

	public void setNodesToRemove(List<ExpandedNodeId> nodes2remove) {
		this.nodes2remove = nodes2remove;
	}
}
