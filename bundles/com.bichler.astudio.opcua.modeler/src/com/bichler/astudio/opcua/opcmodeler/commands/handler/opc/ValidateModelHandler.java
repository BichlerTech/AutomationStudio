package com.bichler.astudio.opcua.opcmodeler.commands.handler.opc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTreeDef;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.ModelValidationWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModelContentFactory;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

public class ValidateModelHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		IStructuredSelection treeSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		BrowserModelNode selection = (BrowserModelNode) treeSelection.getFirstElement();
		NodeId selectedId = selection.getNode().getNodeId();
		boolean isUnderTypeModel = isSelectedNodeInTypeTree(selection);
		ModelTypDef selectedDef = new ModelTypDef();
		selectedDef.nodeId = new ExpandedNodeId(nsTable.getUri(selectedId.getNamespaceIndex()), selectedId.getValue(),
				nsTable);
		// selectedDef.nodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(selectedId);
		selectedDef.typeClass = selection.getNode().getNodeClass();
		selectedDef.name = selection.getNode().getDisplayName();
		ModelTypDef elementTypDef = findNodeType(selection, selectedDef);
		// find parent of type
		// ModelTypDef elementTypDef2 = findNodeType(selectedDef,
		// selectedDef.nodeId, selectedDef.typeClass);
		// find parent of selected element
		ModelTypDef elementRootDef = findNodeObjectParent(selectedDef, elementTypDef);
		boolean isValid = validate(elementRootDef, elementTypDef);
		if (isValid) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.validation"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.validation.success"));
		} else {
			boolean isYes = MessageDialog.openQuestion(HandlerUtil.getActiveShell(event),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.validation"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.validation.fail"));
			if (isYes) {
				Shell shell = HandlerUtil.getActiveShell(event);
				ModelValidationWizard wizard = new ModelValidationWizard();
				wizard.setSelectedDef(selectedDef);
				wizard.setElementTypeDef(elementTypDef);
				wizard.setElementRootDef(elementRootDef);
				WizardDialog dialog = new WizardDialog(shell, wizard);
				if (Dialog.OK == dialog.open()) {
					/**
					 * List<ValidationModel> nodesToAdd = wizard.getNodesToAdd();
					 * Map<ExpandedNodeId, ExpandedNodeId> runtimeMapping =
					 * wizard.getRuntimeMapping(); // update model
					 * doExecuteFinish(HandlerUtil.getActiveShell(event), runtimeMapping,
					 * nodesToAdd);
					 */
					// refresh
					DesignerUtils.refreshBrowserNode(selection, true);
				}
			}
		}
		return isValid;
	}

	private NodeAttributes createNodeAttributes(ValidationModel model, Node node) {
		NodeAttributes attributes = null;
		switch (node.getNodeClass()) {
		case Object:
			attributes = new ObjectAttributes();
			((ObjectAttributes) attributes).setEventNotifier(((ObjectNode) node).getEventNotifier());
			break;
		case ObjectType:
			attributes = new ObjectAttributes();
			((ObjectAttributes) attributes).setEventNotifier(UnsignedByte.ZERO);
			break;
		case Variable:
			attributes = new VariableAttributes();
			((VariableAttributes) attributes).setAccessLevel(((VariableNode) node).getAccessLevel());
			((VariableAttributes) attributes).setArrayDimensions(((VariableNode) node).getArrayDimensions());
			((VariableAttributes) attributes).setDataType(((VariableNode) node).getDataType());
			((VariableAttributes) attributes).setHistorizing(((VariableNode) node).getHistorizing());
			((VariableAttributes) attributes)
					.setMinimumSamplingInterval(((VariableNode) node).getMinimumSamplingInterval());
			((VariableAttributes) attributes).setUserAccessLevel(((VariableNode) node).getUserAccessLevel());
			((VariableAttributes) attributes).setValue(((VariableNode) node).getValue());
			((VariableAttributes) attributes).setValueRank(((VariableNode) node).getValueRank());
			break;
		case VariableType:
			attributes = new VariableAttributes();
			((VariableAttributes) attributes).setArrayDimensions(((VariableTypeNode) node).getArrayDimensions());
			((VariableAttributes) attributes).setDataType(((VariableTypeNode) node).getDataType());
			((VariableAttributes) attributes).setValue(((VariableTypeNode) node).getValue());
			((VariableAttributes) attributes).setValueRank(((VariableTypeNode) node).getValueRank());
			break;
		case Method:
			attributes = new MethodAttributes();
			((MethodAttributes) attributes).setExecutable(((MethodNode) node).getExecutable());
			((MethodAttributes) attributes).setUserExecutable(((MethodNode) node).getUserExecutable());
			break;
		}
		attributes.setDescription(node.getDescription());
		attributes.setDisplayName(node.getDisplayName());
		attributes.setUserWriteMask(node.getUserWriteMask());
		attributes.setWriteMask(node.getWriteMask());
		return attributes;
	}

	private void doExecuteFinish(Shell shell, final Map<ExpandedNodeId, ExpandedNodeId> mapping,
			final List<ValidationModel> newNodesToCreate) {
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.updatemodel")
								+ "...",
						IProgressMonitor.UNKNOWN);
				try {
					List<AddNodesItem> nodesToAdd = new ArrayList<>();
					NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
					for (ValidationModel model : newNodesToCreate) {
						// new node id
						NodeId newNodeId = model.getNodeId();
						// new mapping id
						ExpandedNodeId mappingId = model.getMappingId();
						NodeId parentId = model.getParent().getNodeId();
						try {
							ReferenceDescription[] result = ValidationModelContentFactory.browse(ServerInstance
									.getInstance().getServerInstance().getNamespaceUris().toNodeId(mappingId),
									BrowseDirection.Inverse, Identifiers.HierarchicalReferences);
							ReferenceDescription[] result2 = ValidationModelContentFactory.browse(ServerInstance
									.getInstance().getServerInstance().getNamespaceUris().toNodeId(mappingId),
									BrowseDirection.Forward, Identifiers.HasTypeDefinition);
							Node node = ServerInstance.getNode(mappingId);
							NodeAttributes nodeAttributes = createNodeAttributes(model, node);
							AddNodesItem item = new AddNodesItem();
							item.setBrowseName(node.getBrowseName());
							item.setNodeAttributes(
									ExtensionObject.binaryEncode(nodeAttributes, EncoderContext.getDefaultInstance()));
							item.setNodeClass(model.getNodeClass());

							item.setParentNodeId(new ExpandedNodeId(nsTable.getUri(parentId.getNamespaceIndex()),
									parentId.getValue(), nsTable));
							// item.setParentNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							// .toExpandedNodeId(model.getParent().getNodeId()));
							item.setReferenceTypeId(result[0].getReferenceTypeId());
							item.setRequestedNewNodeId(new ExpandedNodeId(nsTable.getUri(newNodeId.getNamespaceIndex()),
									newNodeId.getValue(), nsTable));
							// item.setRequestedNewNodeId(ServerInstance.getInstance().getServerInstance()
							// .getNamespaceUris().toExpandedNodeId(newNodeId));
							if (result2.length > 0) {
								item.setTypeDefinition(result2[0].getNodeId());
							} else {
								item.setTypeDefinition(mappingId);
							}
							nodesToAdd.add(item);
						} catch (ServiceResultException e) {
							e.printStackTrace();
						}
					}
					if (!nodesToAdd.isEmpty()) {
						try {
							ServerInstance.addNode(nodesToAdd.toArray(new AddNodesItem[0]), true);
							// getRuntimeMapping().put(arg0, arg1);
						} catch (ServiceResultException e) {
							e.printStackTrace();
						}
					}
					for (Entry<ExpandedNodeId, ExpandedNodeId> entry : mapping.entrySet()) {
						ServerInstance.getTypeModel().addModelMapping(entry.getKey(), entry.getValue());
					}
				} finally {
					monitor.done();
				}
			}
		};
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		try {
			dialog.run(true, true, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void fillSelectionParentTree(BrowserModelNode node, List<ExpandedNodeId> ids) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		NodeId nodeId = node.getNode().getNodeId();
		ExpandedNodeId expNodeId = new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(),
				nsTable);
		// ExpandedNodeId expNodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(nodeId);
		ids.add(expNodeId);
		BrowserModelNode parent = node.getParent();
		if (parent != null) {
			fillSelectionParentTree(parent, ids);
		}
	}

	private boolean isSelectedNodeInTypeTree(BrowserModelNode selection) {
		NodeClass nodeClass = selection.getNode().getNodeClass();
		switch (nodeClass) {
		case DataType:
		case ReferenceType:
		case ObjectType:
		case VariableType:
			return true;
		case Method:
			break;
		case Object:
			break;
		case Unspecified:
			break;
		case Variable:
			break;
		case View:
			break;
		default:
			break;
		}
		BrowserModelNode parent = selection.getParent();
		boolean isType = false;
		if (parent != null) {
			isType = isSelectedNodeInTypeTree(parent);
		}
		return isType;
	}

	/**
	 * Validates the selected node with the TYPEMAP
	 * 
	 * @param ElementId     Id of the selected element
	 * @param ElementTypDef TypDef of the Type from the selected element
	 * @return
	 */
	protected boolean validate(ModelTypDef element, ModelTypDef elementModelType) {
		if (element == null) {
			return false;
		}
		element.buildModelTree();
		ModelTreeDef elementModelDef = element.getModelTree();
		boolean isValid = validateModel(elementModelDef, elementModelType);
		return isValid;
	}

	private boolean validateModel(ModelTreeDef elementDef, ModelTypDef elementModelType) {
		ModelTreeDef typeObjects = elementModelType.buildModelTree();
		List<ExpandedNodeId> childrenIds = typeObjects.getAllChildrenIds();
		List<ExpandedNodeId> objectChildrenIds = elementDef.getAllChildrenIds();
		// all children objects from the given typeclass
		for (ExpandedNodeId childId : childrenIds) {
			List<ExpandedNodeId> related = ServerInstance.getTypeModel().getObjectsFromType(childId);
			if (related == null) {
				return false;
			}
			boolean isValid = false;
			// if there is an relation
			for (ExpandedNodeId relatedId : related) {
				if (objectChildrenIds.contains(relatedId)) {
					isValid = true;
					break;
				}
			}
			if (!isValid) {
				return isValid;
			}
		}
		ModelTypDef parent = elementModelType.findParentTypeClass();
		if (parent != null) {
			return validateModel(elementDef, parent);
		}
		return true;
	}

	private ModelTypDef compareNodeObjectParent(List<ExpandedNodeId> objects, ModelTypDef def) {
		ExpandedNodeId defId = def.nodeId;
		ModelTypDef found = null;
		if (objects.contains(defId)) {
			found = def;
		} else {
			try {
				BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
				BrowseDescription description = new BrowseDescription();
				description.setBrowseDirection(BrowseDirection.Inverse);
				description.setIncludeSubtypes(true);
				description.setNodeClassMask(NodeClass.ALL);
				description
						.setNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(defId));
				description.setReferenceTypeId(Identifiers.HierarchicalReferences);
				description.setResultMask(BrowseResultMask.ALL);
				nodesToBrowse[0] = description;
				BrowseResult[] result = ServerInstance.browse(
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(defId),
						Identifiers.HierarchicalReferences, NodeClass.ALL, BrowseResultMask.ALL,
						BrowseDirection.Inverse, true);
				if (result != null && result.length > 0 && result[0].getReferences() != null) {
					for (ReferenceDescription reference : result[0].getReferences()) {
						ModelTypDef mtd = new ModelTypDef();
						mtd.nodeId = reference.getNodeId();
						mtd.name = reference.getDisplayName();
						mtd.typeClass = reference.getNodeClass();
						found = compareNodeObjectParent(objects, mtd);
						if (found != null) {
							break;
						}
					}
				}
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		return found;
	}

	/**
	 * Finds the ROOT NODE of the selected object
	 * 
	 * @param selectedDef
	 * @param elementTypDef
	 * @return
	 */
	private ModelTypDef findNodeObjectParent(ModelTypDef selectedDef, ModelTypDef elementTypDef) {
		if (elementTypDef == null) {
			return null;
		}
		ExpandedNodeId typeId = elementTypDef.nodeId;
		List<ExpandedNodeId> objects = ServerInstance.getTypeModel().getObjectsFromType(typeId);
		if (objects == null) {
			return null;
		}
		return compareNodeObjectParent(objects, selectedDef);
	}

	protected ModelTypDef findNodeType(BrowserModelNode selection, ModelTypDef selectedDef) {
		ExpandedNodeId typeNodeId = null;
		// if (!isUnderTypeModel) {
		// get related node in opcua type tree
		ExpandedNodeId objMap1 = ServerInstance.getTypeModel().getObjectMapping().get(selectedDef.nodeId);
		// error
		if (objMap1 == null) {
			return null;
		}
		typeNodeId = objMap1;
		// } else {
		// typeNodeId = selectedDef.nodeId;
		// }
		// is related node a type node
		Node node = ServerInstance.getNode(typeNodeId);
		if (node == null) {
			return null;
		}
		switch (node.getNodeClass()) {
		case ObjectType:
		case VariableType:
		case ReferenceType:
		case DataType:
			ModelTypDef def = new ModelTypDef();
			def.nodeId = typeNodeId;
			def.typeClass = node.getNodeClass();
			def.name = node.getDisplayName();
			return def;
		}
		// find type which belongs to the selected node
		ModelTypDef elementTypDef = find(typeNodeId);
		// map to type which is the selected node
		if (elementTypDef != null) {
			List<ExpandedNodeId> selectionParentTree = new ArrayList<>();
			fillSelectionParentTree(selection, selectionParentTree);
			try {
				ModelTypDef def = new ModelTypDef();
				def.typeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(elementTypDef.nodeId);
				def.typeClass = elementTypDef.typeClass;
				def.name = elementTypDef.name;
				ModelTreeDef typeTree = def.buildTypeTree();
				List<ExpandedNodeId> typeIds = typeTree.getAllChildrenIds();
				// add selected parent type
				typeIds.add(elementTypDef.nodeId);
				elementTypDef = compareNodeType2(selectionParentTree, typeIds);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		return elementTypDef;
	}

	private ModelTypDef compareNodeType2(List<ExpandedNodeId> selection, List<ExpandedNodeId> typeIds) {
		boolean found = false;
		ExpandedNodeId rootModelNodeId = null;
		ExpandedNodeId rootModelNodeTypeId = null;
		for (ExpandedNodeId typeId : typeIds) {
			List<ExpandedNodeId> relatedObjects = ServerInstance.getTypeModel().getObjectsFromType(typeId);
			if (relatedObjects != null) {
				for (ExpandedNodeId id : selection) {
					if (relatedObjects.contains(id)) {
						rootModelNodeId = id;
						rootModelNodeTypeId = typeId;
						found = true;
						break;
					}
				}
			}
			if (found) {
				break;
			}
		}
		if (!found) {
			return null;
		}
		Node node = ServerInstance.getNode(rootModelNodeTypeId);
		ModelTypDef foundType = new ModelTypDef();
		foundType.nodeId = rootModelNodeTypeId;
		foundType.typeClass = node.getNodeClass();
		foundType.name = node.getDisplayName();
		return foundType;
	}

	private ModelTypDef find(ExpandedNodeId nodeId) {
		ModelTypDef parentDef = null;
		try {
			BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
			BrowseDescription description = new BrowseDescription();
			description.setBrowseDirection(BrowseDirection.Inverse);
			description.setIncludeSubtypes(true);
			description.setNodeClassMask(NodeClass.ALL);
			description.setNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(nodeId));
			description.setReferenceTypeId(Identifiers.HierarchicalReferences);
			description.setResultMask(BrowseResultMask.ALL);
			nodesToBrowse[0] = description;
			BrowseResult[] result = ServerInstance.browse(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(nodeId),
					Identifiers.HierarchicalReferences, NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Inverse,
					true);
			if (result != null && result.length > 0 && result[0].getReferences() != null) {
				for (ReferenceDescription refDesc : result[0].getReferences()) {
					switch (refDesc.getNodeClass()) {
					case DataType:
					case ObjectType:
					case ReferenceType:
					case VariableType:
						parentDef = new ModelTypDef();
						parentDef.nodeId = refDesc.getNodeId();
						parentDef.typeClass = refDesc.getNodeClass();
						parentDef.name = refDesc.getDisplayName();
						return parentDef;
					case Method:
						break;
					case Object:
						break;
					case Unspecified:
						break;
					case Variable:
						break;
					case View:
						break;
					default:
						break;
					}
					parentDef = find(refDesc.getNodeId());
					if (parentDef != null) {
						break;
					}
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return parentDef;
	}
}
