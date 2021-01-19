package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.dialogs.CreateReferenceTypeDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;

public class CreateReferenceTypeHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		// get the selection (parent node)
		final TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event)
				.getSelectionService().getSelection(ModelBrowserView.ID);
		Node selectedNode = ((BrowserModelNode) selection.getFirstElement()).getNode();

		int firstNamespaceIndex = CreateFactory.checkForNewNamespaceTable();
		if (firstNamespaceIndex == WizardDialog.CANCEL) {
			return null;
		}

		// if (NodeClass.ReferenceType.equals(selectedNode.getNodeClass())) {
		CreateReferenceTypeDialog dialog = new CreateReferenceTypeDialog(shell, selectedNode);
		int open = dialog.open();
		if (open == Dialog.OK) {
			// get the result from the dialog
			final DialogResult result = dialog.getResult();
			final ModelChangeInfo info = new ModelChangeInfo(true);
			info.setNodeId(result.getNodeResult().getNodeId());
			info.setType(result.getType());
			info.setBrowseName(result.getNodeResult().getBrowseName());
			info.setNodeClass(result.getNodeResult().getNodeClass());
			info.setNodeAttributes(result.getNodeAttributes());
			// info.setAdditionalNodesMap(additionalNodes);
			// info.setHasRuleItself(hasRuleItself);
			// info.setNamingRuleTyp(selfRule);
			info.setParentNodeId(result.getParentNodeId());
			info.setReferenceTypeId(result.getReferenceTypeId());
			info.setAdditionalRefernces(result.getAdditionalReferences());
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
			try {
				progressDialog.run(true, false, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"opc.message.extendreftype") + "...", IProgressMonitor.UNKNOWN);
						try {
							// creates the AddNodesItem and insert it to the address space
							create((BrowserModelNode) selection.getFirstElement(), info);
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									// refresh
									DesignerUtils.refreshBrowserNode((BrowserModelNode) selection.getFirstElement(),
											true);
									// information model change
									MCPreferenceStoreUtil.setHasInformationModelChanged(shell, result.getNodeResult());
								}
							});
						} finally {
							monitor.done();
						}
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void create(BrowserModelNode treeSelection, ModelChangeInfo info) {
		ExpandedNodeId parentNodeId = info.getParentNodeId();
		NodeId referenceTypeId = info.getReferenceTypeId();
		CreateFactory.create(NodeClass.ReferenceType, parentNodeId, info.getNodeClass(), referenceTypeId, info, false,
				true);
	}
}
