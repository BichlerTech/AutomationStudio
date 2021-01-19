package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.dialogs.CreateMethodModellingDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.CreateMethodWizard;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;

public class CreateMethodHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		// get the selection (parent node)
		final TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event)
				.getSelectionService().getSelection(ModelBrowserView.ID);

		int firstNamespaceIndex = CreateFactory.checkForNewNamespaceTable();
		if (firstNamespaceIndex == WizardDialog.CANCEL) {
			return null;
		}

		Node selectedNode = ((BrowserModelNode) selection.getFirstElement()).getNode();
		CreateMethodWizard wizard = new CreateMethodWizard(selectedNode);
		WizardDialog wizarddialog = new WizardDialog(shell, wizard);
		// CreateMethodDialog dialog = new CreateMethodDialog(shell,
		// selectedNode);
		LinkedList<QualifiedName> path = new LinkedList<>();
		CreateMethodModellingDialog modellingDialog = new CreateMethodModellingDialog(shell);
		boolean hasRuleItself = modellingDialog.hasRuleItself(selectedNode, path);
		int ok = wizarddialog.open();
		if (ok == WizardDialog.OK) {
			// get the result from the dialog
			DialogResult result = wizard.getDialogResult();
			// int open = dialog.open();
			// if (open == Dialog.OK) {
			// DialogResult result = dialog.getResult();
			if (!hasRuleItself || (hasRuleItself && modellingDialog.open() == Dialog.OK)) {
				NamingRuleType selfRule = modellingDialog.getModelRuleSelf();
				result.setHasRuleItself(hasRuleItself);
				result.setNamingRuleTyp(selfRule);
				// creates the AddNodesItem and insert it to the address space
				final ModelChangeInfo info = new ModelChangeInfo(true);
				info.setNodeId(result.getNodeResult().getNodeId());
				info.setType(result.getType());
				info.setBrowseName(result.getNodeResult().getBrowseName());
				info.setNodeClass(result.getNodeResult().getNodeClass());
				info.setNodeAttributes(result.getNodeAttributes());
				// info.setHasRuleItself(hasRuleItself);
				info.setNamingRuleTyp(selfRule);
				info.setParentNodeId(result.getParentNodeId());
				info.setReferenceTypeId(result.getReferenceTypeId());
				info.setAdditionalNodesMap(result.getAdditionalNodes());
				info.setAdditionalRefernces(result.getAdditionalReferences());
				info.setHasRuleItself(hasRuleItself);
				info.setNamingRuleTyp(selfRule);
				
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
				try {
					progressDialog.run(true, false, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException {
							monitor.beginTask(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
									"opc.message.extendmethod") + "...", IProgressMonitor.UNKNOWN);
							create((BrowserModelNode) selection.getFirstElement(), info);
							try {
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										// refresh
										DesignerUtils.refreshBrowserNode((BrowserModelNode) selection.getFirstElement(),
												true);
										// information model
										// change
										MCPreferenceStoreUtil.setHasInformationModelChanged(shell,
												info.getNodeId().getNamespaceIndex());
										// ask modelchange
										DesignerUtils.askModelChange((BrowserModelNode) selection.getFirstElement(),
												info);
									}
								});
							} finally {
								monitor.done();
							}
						}
					});
				} catch (InvocationTargetException | InterruptedException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		/**
		 * TODO: NEW - REDESIGNED
		 * 
		 * CreateMethodWizard wizard = new CreateMethodWizard(selectedNode);
		 * WizardDialog wizardDialog = new WizardDialog(
		 * HandlerUtil.getActiveShell(event), wizard);
		 * 
		 * int OK = wizardDialog.open(); if (OK == WizardDialog.OK) {
		 * System.out.println(); }
		 */
		/*
		 * } else { Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
		 * "Parent is not an Object or ObjectType!", null); ErrorDialog.openError(shell,
		 * "Error", "Not able to create a new MethodNode here!", status); }
		 */
		return null;
	}

	private void create(BrowserModelNode treeSelection, ModelChangeInfo result) {
		ExpandedNodeId parentNodeId = result.getParentNodeId();
		NodeId referenceTypeId = result.getReferenceTypeId();
		CreateFactory.create(NodeClass.Method, parentNodeId, result.getNodeClass(), referenceTypeId, result, false,
				false);
	}
}
