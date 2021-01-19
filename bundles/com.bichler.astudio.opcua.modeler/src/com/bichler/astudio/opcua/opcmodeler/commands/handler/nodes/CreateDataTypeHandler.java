package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;
import com.bichler.astudio.opcua.datadictionary.modeler.model.StructuredDataTypeManager;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.CreateDataTypeWizard;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;

public class CreateDataTypeHandler extends AbstractHandler {
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

		CreateDataTypeWizard wizard = new CreateDataTypeWizard(selectedNode);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		// CreateDataTypeDialog dialog2 = new CreateDataTypeDialog(shell,
		// selectedNode);
		int open = dialog.open();
		if (open == Dialog.OK) {
			// dialog2.open();
			final DialogResult result = wizard.getDialogResult();
			// get the result from the dialog
			// final DialogResult result = dialog2.getResult();
			final ModelChangeInfo info = new ModelChangeInfo(true);
			info.setNodeId(result.getNodeResult().getNodeId());
			info.setType(result.getType());
			info.setBrowseName(result.getNodeResult().getBrowseName());
			info.setNodeClass(result.getNodeResult().getNodeClass());
			info.setNodeAttributes(result.getNodeAttributes());
			info.setAdditionalNodesMap(result.getAdditionalNodes());
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
								"opc.message.extenddatatype") + "...", IProgressMonitor.UNKNOWN);
						try {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									create((BrowserModelNode) selection.getFirstElement(), info);
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
		AddNodesResult[] addNodeResult = CreateFactory.create(NodeClass.DataType, parentNodeId, info.getNodeClass(),
				referenceTypeId, info, false, true);

		if (addNodeResult.length == 0) {
			return;
		}

		NodeId nodeId = addNodeResult[0].getAddedNodeId();
		Node node = ServerInstance.getNode(nodeId);

		boolean isStructure = ServerInstance.getInstance().getServerInstance().getTypeTable().isTypeOf(nodeId,
				Identifiers.Structure);
		if (isStructure) {
			// initialize structure
			initializeStructure(node);
		}
		boolean isEnumeration = ServerInstance.getInstance().getServerInstance().getTypeTable().isEnumeration(nodeId);
		if (isEnumeration) {
			initializeEnumeration(node);
		}
	}

	private void initializeEnumeration(Node node) {
		StructuredDataTypeManager structureManager = new StructuredDataTypeManager(
				ServerInstance.getInstance().getServerInstance());
		boolean checked = structureManager.initOpcEnumerationStructure((DataTypeNode) node);
		// structured datatype
		if (!checked) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Warning",
					"No binary schema definition for node " + node.getBrowseName().getName() + " has been created!");
			return;
		}

		DefinitionBean definition = new DefinitionBean();
		definition.setDefinitionName(node.getBrowseName().getName());

		VariableNode enumValues = structureManager.findNodeEnumerationStrings(node.getNodeId());
		// enumeration values
		Variant value = enumValues.getValue();
		LocalizedText[] enumStrings = new LocalizedText[0];
		if (!value.isEmpty()) {
			enumStrings = (LocalizedText[]) value.getValue();	
		}
		// localized text values
		for (int i = 0; i < enumStrings.length; i++) {
			LocalizedText enumString = enumStrings[i];
			DefinitionFieldBean field = new DefinitionFieldBean();
			field.setName(enumString.getText());
			field.setValue(i);
			definition.addField(field);
		}

		/* ByteString byteString = */structureManager.writeOpcEnumDictionary((DataTypeNode) node, definition);

		Studio_ResourceManager.DATATYPE_DEFINITIONS.put(node.getNodeId(), definition.clone());
	}

	private void initializeStructure(Node node) {
		StructuredDataTypeManager structureManager = new StructuredDataTypeManager(
				ServerInstance.getInstance().getServerInstance());

		boolean checked = structureManager.initOpcBinaryStructure((DataTypeNode) node);

		// structured datatype
		if (!checked) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Warning",
					"No binary schema definition for node " + node.getBrowseName().getName() + " has been created!");
			return;
		}
		DefinitionBean definition = new DefinitionBean();
		definition.setDefinitionName(node.getBrowseName().getName());

		/* ByteString byteString = */structureManager.writeOpcDataDictionary((DataTypeNode) node, definition);

		Studio_ResourceManager.DATATYPE_DEFINITIONS.put(node.getNodeId(), definition.clone());
	}
}
