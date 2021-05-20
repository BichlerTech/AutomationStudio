package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.NamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.namespace.OPCNamesspaceWizard;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.server.service.util.AddressSpaceUtil;

public class CreateFactory {
	enum CreateType {
		Object, Type, View, Unspecified;
	}

	public static AddNodesResult[] create(NodeClass class2create, ExpandedNodeId parentNodeId,
			NodeClass parentNodeClass, NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping, boolean useNextIdFromParent, boolean typeCreate) {
		// List<NodeId> additionalNodeIds2create = new ArrayList<>();
		AddNodesResult[] results = null;
		switch (class2create) {
		case Object:
			results = createObject(parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate, mapping,
					useNextIdFromParent, typeCreate);
			if (!isUpdate) {
				doTypeMapping(mapping);
			}
			break;
		case Variable:
			results = createVariable(parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate, mapping,
					useNextIdFromParent);
			if (!isUpdate) {
				doTypeMapping(mapping);
			}
			break;
		case Method:
			results = createMethod(parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate, mapping,
					useNextIdFromParent);
			if (!isUpdate) {
				doTypeMapping(mapping);
			}
			break;
		case ObjectType:
			results = createObjectType(parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate, mapping);
			break;
		case VariableType:
			results = createVariableType(parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate, mapping);
			break;
		case DataType:
			results = createDataType(parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate, mapping);
			break;
		case ReferenceType:
			results = createReferenceType(parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate, mapping);
			break;
		}
		return results;
	}

	public static AddNodesResult[] create(NodeClass class2create, ExpandedNodeId parentNodeId,
			NodeClass parentNodeClass, NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate,
			boolean typeCreate) {
		return create(class2create, parentNodeId, parentNodeClass, referenceTypeId, info, isUpdate,
				new HashMap<ExpandedNodeId, ExpandedNodeId>(), false, typeCreate);
	}

	/**
	 * Mappes the created object with its typ
	 * 
	 * @param treeNode
	 * @param mapping
	 */
	public static void doTypeMapping(Map<ExpandedNodeId, ExpandedNodeId> mapping) {
		for (Entry<ExpandedNodeId, ExpandedNodeId> entry : mapping.entrySet()) {
			ServerInstance.getTypeModel().addModelMapping(entry.getKey(), entry.getValue());
		}
	}

	public static void remove(DeleteNodesItem... nodes2delete) throws ServiceResultException {
		if (nodes2delete != null && nodes2delete.length > 0) {
			ServerInstance.deleteNodes(nodes2delete);
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			Set<NodeId> mapping = new HashSet<>();
			for (DeleteNodesItem item : nodes2delete) {
				mapping.add(item.getNodeId());
			}
			for (NodeId node : mapping) {
				ServerInstance.getTypeModel().removeModelMapping(
						new ExpandedNodeId(node));
				// ServerInstance.getTypeModel().removeModelMapping(nsTable.toExpandedNodeId(node));
			}
		}
	}

	public static void remove(Node[] nodes, Map<NodeId, DeleteNodesItem> nodesToDelete) throws ServiceResultException {
		DesignerUtils.doModelChangeRemove(nodes, nodesToDelete);
		remove(nodesToDelete.values().toArray(new DeleteNodesItem[0]));
	}

	public static int checkForNewNamespaceTable() {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		int index = nsTable.size();
		int open = -1;
		// we have no other namespaces then the default opc ua namespace
		if (index <= 1) {
			// open namespace dialog
			OPCNamesspaceWizard wizard = new OPCNamesspaceWizard();
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					wizard);
			open = dialog.open();
			if (WizardDialog.OK == open) {
				NamespaceHandler.updateNamespaces(wizard);
				nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
				if (nsTable.size() > 1)
					index = 1;
			}
		}
		return open;
	}

	private static AddNodesResult[] createReferenceType(ExpandedNodeId parentNodeId, NodeClass parentNodeClass,
			NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping) {

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(), EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			e.printStackTrace();
		}
		AddNodesItem nodesToAdd = new AddNodesItem();
		nodesToAdd.setBrowseName(info.getBrowseName());
		nodesToAdd.setNodeAttributes(attributes);
		nodesToAdd.setNodeClass(info.getNodeClass());
		nodesToAdd.setParentNodeId(parentNodeId);
		nodesToAdd.setReferenceTypeId(referenceTypeId);
		nodesToAdd.setRequestedNewNodeId(new ExpandedNodeId(nsTable.getUri(info.getNodeId().getNamespaceIndex()),
				info.getNodeId().getValue(), nsTable));
		// nodesToAdd.setRequestedNewNodeId(
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toExpandedNodeId(info.getNodeId()));
		nodesToAdd.setTypeDefinition(null);
		AddNodesResult[] results = null;
		try {
			ServerInstance.addNode(new AddNodesItem[] { nodesToAdd }, false);
			// results = ServerInstance.getInstance().getServerInstance()
			// .getProfileManager().createNodes(nodesToAdd);
			createReferences(info);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return results;
	}

	private static AddNodesResult[] createDataType(ExpandedNodeId parentNodeId, NodeClass parentNodeClass,
			NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping) {
		// server namespace table
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(), EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			e.printStackTrace();
		}
		List<AddNodesItem> nodes2add = new ArrayList<>();
		AddNodesItem nodesToAdd = new AddNodesItem();
		nodesToAdd.setBrowseName(info.getBrowseName());
		nodesToAdd.setNodeAttributes(attributes);
		nodesToAdd.setNodeClass(info.getNodeClass());
		nodesToAdd.setParentNodeId(parentNodeId);
		nodesToAdd.setReferenceTypeId(referenceTypeId);
		nodesToAdd.setRequestedNewNodeId(new ExpandedNodeId(info.getNodeId()));
		// nodesToAdd.setRequestedNewNodeId(
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toExpandedNodeId(info.getNodeId()));
		nodesToAdd.setTypeDefinition(null);
		nodes2add.add(nodesToAdd);
		Map<Node, Boolean> additionalNodes = info.getAdditionalCreateNodes();
		List<AddReferencesItem> additionalReferences = new ArrayList<>();
		for (Entry<Node, Boolean> entry : additionalNodes.entrySet()) {
			UAVariableNode key = (UAVariableNode) entry.getKey();
			VariableAttributes attr = new VariableAttributes();
			attr.setAccessLevel(key.getAccessLevel());
			attr.setArrayDimensions(key.getArrayDimensions());
			attr.setDataType(key.getDataType());
			attr.setDescription(key.getDescription());
			attr.setDisplayName(key.getDisplayName());
			attr.setHistorizing(key.getHistorizing());
			attr.setMinimumSamplingInterval(key.getMinimumSamplingInterval());
			attr.setUserAccessLevel(key.getUserAccessLevel());
			attr.setUserWriteMask(key.getUserWriteMask());
			attr.setValue(key.getValue());
			attr.setValueRank(key.getValueRank());
			attr.setWriteMask(key.getWriteMask());
			AddNodesItem additional2add = new AddNodesItem();
			additional2add.setBrowseName(key.getBrowseName());
			try {
				additional2add
						.setNodeAttributes(ExtensionObject.binaryEncode(attr, EncoderContext.getDefaultInstance()));
			} catch (EncodingException e) {
				e.printStackTrace();
			}
			additional2add.setNodeClass(key.getNodeClass());
			additional2add.setParentNodeId(new ExpandedNodeId(info.getNodeId()));

			additional2add.setReferenceTypeId(Identifiers.HasProperty);
			additional2add.setRequestedNewNodeId(new ExpandedNodeId(key.getNodeId()));

			additional2add
					.setTypeDefinition(new ExpandedNodeId(Identifiers.PropertyType));

			nodes2add.add(additional2add);
			additionalReferences.add(new AddReferencesItem(key.getNodeId(), Identifiers.HasModellingRule, true, null,
					new ExpandedNodeId(Identifiers.ModellingRule_Mandatory),
					NodeClass.Object));

			// TODO:
//			additionalReferences.add(new AddReferencesItem(key.getNodeId(), new NodeId(0, 50), true, null,
//					new ExpandedNodeId(nsTable.getUri(info.getNodeId().getNamespaceIndex()),
//							info.getNodeId().getValue(), nsTable),
//					NodeClass.DataType));
		}
		AddNodesResult[] results = null;
		try {
			results = ServerInstance.addNode(nodes2add.toArray(new AddNodesItem[0]), false);
			ServerInstance.addReferences(additionalReferences.toArray(new AddReferencesItem[0]));
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return results;
	}
	// private static void createDataTypeAddNodesItemStatement2(ModelChangeInfo
	// info, ExpandedNodeId parentNodeId,
	// NodeId nodeId, NodeId referenceTypeId, Map<ExpandedNodeId, AddNodesItem>
	// nodesToAdd,
	// List<AddReferencesItem> referencesToAdd, NodeClass parentNodeClass,
	// boolean isUpdate, List<NodeId> rekIds,
	// Map<ExpandedNodeId, ExpandedNodeId> mapping, boolean useNextIdFromParent)
	// {
	//
	// AddNodesItem nodeToAdd = new AddNodesItem();
	// ExpandedNodeId expNodeId =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
	// .toExpandedNodeId(nodeId);
	// nodesToAdd.put(expNodeId, nodeToAdd);
	//
	// if (isUpdate) {
	// boolean ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
	//
	// NodeId nextId2use = null;
	// // continue ids from parent node
	// if (useNextIdFromParent) {
	// try {
	// nextId2use =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
	// .toNodeId(parentNodeId);
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	// }
	// // continue ids from source node (started model change action)
	// else {
	// nextId2use = info.getNodeId();
	// }
	//
	// nodeId =
	// ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
	// .getNextNodeId(nextId2use.getNamespaceIndex(), nextId2use.getValue(),
	// nextId2use.getIdType(),
	// rekIds, ccNodeId);
	// }
	//
	// rekIds.add(nodeId);
	//
	// ExpandedNodeId newNodeId =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
	// .toExpandedNodeId(nodeId);
	//
	// nodeToAdd.setBrowseName(info.getBrowseName());
	// ExtensionObject attributes = null;
	// try {
	// attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(),
	// EncoderContext.getDefaultInstance());
	// } catch (EncodingException e) {
	// e.printStackTrace();
	// }
	// nodeToAdd.setNodeAttributes(attributes);
	// nodeToAdd.setNodeClass(NodeClass.Method);
	// nodeToAdd.setParentNodeId(parentNodeId);
	// nodeToAdd.setReferenceTypeId(referenceTypeId);
	// nodeToAdd.setRequestedNewNodeId(newNodeId);
	// nodeToAdd.setTypeDefinition(null);
	// // no update, map, new node to type node
	// if (!isUpdate) {
	// // no type for plain method
	// mapping.put(newNodeId, ExpandedNodeId.NULL);
	// } else {
	// mapping.put(newNodeId, expNodeId);
	// }
	//
	// if (info.getNamingRule() != null) {
	// NodeId ruleId = null;
	//
	// switch (info.getNamingRule()) {
	// case Constraint:
	// ruleId = Identifiers.ModellingRule_ExposesItsArray;
	// break;
	// case Mandatory:
	// ruleId = Identifiers.ModellingRule_Mandatory;
	// break;
	// case Optional:
	// ruleId = Identifiers.ModellingRule_Optional;
	// break;
	// }
	//
	// AddReferencesItem modelingRule = new AddReferencesItem();
	// modelingRule.setIsForward(true);
	// modelingRule.setReferenceTypeId(Identifiers.HasModellingRule);
	// modelingRule.setSourceNodeId(NodeIdUtil.createNodeId(newNodeId.getNamespaceIndex(),
	// newNodeId.getValue()));
	// modelingRule.setTargetNodeClass(NodeClass.Object);
	// modelingRule.setTargetNodeId(new ExpandedNodeId(ruleId));
	// referencesToAdd.add(modelingRule);
	// }
	//
	// if (info.hasRuleItself()) {
	// // forward ref (child -> parent)
	// // AddReferencesItem modelingItemForward = new AddReferencesItem();
	// // modelingItemForward.setIsForward(true);
	// // modelingItemForward.setReferenceTypeId(Identifiers.HasModelParent);
	// // try {
	// // modelingItemForward.setSourceNodeId(ServerInstance
	// // .getInstance().getServerInstance().getNamespaceUris()
	// // .toNodeId(newNodeId));
	// // } catch (ServiceResultException e) {
	// // e.printStackTrace();
	// // }
	// // modelingItemForward.setTargetNodeClass(parentNodeClass);
	// // if (!isUpdate) {
	// // modelingItemForward.setTargetNodeId(info.getParentNodeId());
	// // } else {
	// // modelingItemForward.setTargetNodeId(parentNodeId);
	// // }
	// // inverse ref (parent -> new child)
	//
	// // AddReferencesItem modelingItemInverse = new AddReferencesItem();
	// // modelingItemInverse.setIsForward(false);
	// // modelingItemInverse.setReferenceTypeId(Identifiers.HasModelParent);
	// // try {
	// // if (!isUpdate) {
	// // modelingItemInverse.setSourceNodeId(ServerInstance
	// // .getInstance().getServerInstance()
	// // .getNamespaceUris()
	// // .toNodeId(info.getParentNodeId()));
	// // } else {
	// // modelingItemInverse.setSourceNodeId(ServerInstance
	// // .getInstance().getServerInstance()
	// // .getNamespaceUris().toNodeId(parentNodeId));
	// // }
	// // } catch (ServiceResultException e) {
	// // e.printStackTrace();
	// // }
	// // modelingItemInverse.setTargetNodeClass(info.getNodeClass());
	// // modelingItemInverse.setTargetNodeId(newNodeId);
	// //
	// // referencesToAdd.add(modelingItemInverse);
	// // referencesToAdd.add(modelingItemForward);
	// }
	//
	// for (ReferenceNode additionalReferenceNode :
	// info.getAdditionalReferences()) {
	//
	// AddReferencesItem addReference = new AddReferencesItem();
	// addReference.setIsForward(!additionalReferenceNode.getIsInverse());
	// addReference.setReferenceTypeId(additionalReferenceNode.getReferenceTypeId());
	// addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
	//
	// NodeClass targetNodeClass =
	// ServerInstance.getNode(additionalReferenceNode.getTargetId()).getNodeClass();
	//
	// addReference.setSourceNodeId(info.getNodeId());
	//
	// addReference.setTargetNodeClass(targetNodeClass);
	// // addReference.setTargetServerUri(TargetServerUri);
	// referencesToAdd.add(addReference);
	// }
	//
	// /**
	// * create method argument information
	// */
	// if (info.isCreate()) {
	//// createdata
	// createMethodAddNodes(info, isUpdate, nodesToAdd, referencesToAdd,
	// info.getType(), newNodeId, null, rekIds,
	// mapping);
	// }
	// /**
	// * Paste
	// */
	// else {
	// Map<ExpandedNodeId, AddNodesItem> additional =
	// info.getAdditionalPasteNodes();
	// if (additional != null) {
	// Map<ExpandedNodeId, ExpandedNodeId> pasteIdMapping = new HashMap<>();
	//
	// // this is already should exist in current addnodes item
	// additional.remove(expNodeId);
	// pasteIdMapping.put(expNodeId, newNodeId);
	//
	// // mapping for all new nodeids
	// for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
	// ExpandedNodeId key = entry.getKey();
	//
	// AddNodesItem value = entry.getValue();
	// AddNodesItem mappedItem = new AddNodesItem();
	//
	// mappedItem.setBrowseName(value.getBrowseName());
	// mappedItem.setNodeAttributes(value.getNodeAttributes());
	// mappedItem.setNodeClass(value.getNodeClass());
	// mappedItem.setReferenceTypeId(value.getReferenceTypeId());
	// mappedItem.setTypeDefinition(value.getTypeDefinition());
	//
	// // key
	// boolean ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
	// NodeId pasteId =
	// ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
	// .getNodeFactory()
	// .getNextNodeId(key.getNamespaceIndex(), key.getValue(), key.getIdType(),
	// rekIds, ccNodeId);
	//
	// rekIds.add(pasteId);
	// ExpandedNodeId newExpId =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
	// .toExpandedNodeId(pasteId);
	//
	// pasteIdMapping.put(key, newExpId);
	// nodesToAdd.put(newExpId, mappedItem);
	// }
	// /**
	// * map addnodeitem statement
	// */
	// for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
	// ExpandedNodeId key = entry.getKey();
	// ExpandedNodeId newExpId = pasteIdMapping.get(key);
	// AddNodesItem mappedItem = nodesToAdd.get(newExpId);
	// AddNodesItem value = entry.getValue();
	//
	// mappedItem.setRequestedNewNodeId(newExpId);
	// ExpandedNodeId p = pasteIdMapping.get(value.getParentNodeId());
	// mappedItem.setParentNodeId(p);
	// }
	// /**
	// * map addreference statement
	// */
	// NamespaceTable nsTable =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris();
	// for (AddReferencesItem entry : info.getAdditionalPasteReferences()) {
	//
	// try {
	// AddReferencesItem newReference = new AddReferencesItem();
	// newReference.setIsForward(entry.getIsForward());
	// newReference.setReferenceTypeId(entry.getReferenceTypeId());
	// newReference.setTargetNodeClass(entry.getTargetNodeClass());
	//
	// NodeId sr = entry.getSourceNodeId();
	// ExpandedNodeId expSr = nsTable.toExpandedNodeId(sr);
	// ExpandedNodeId srExpId = pasteIdMapping.get(expSr);
	// newReference.setSourceNodeId(nsTable.toNodeId(srExpId));
	//
	// ExpandedNodeId tr = entry.getTargetNodeId();
	// ExpandedNodeId trExpId = pasteIdMapping.get(tr);
	// newReference.setTargetNodeId(trExpId);
	//
	// referencesToAdd.add(newReference);
	// } catch (ServiceResultException sre) {
	// sre.printStackTrace();
	// }
	// }
	// }
	// }
	// }

	/**
	 * Instantiate the new defined node
	 * 
	 * @param result
	 * @param parentNodeId
	 * @param referenceTypeId
	 * @param nodesToAdd
	 * @param referencesToAdd
	 * @param additional
	 */
	// private static void createDataTypeAddNodesItemStatement(ModelChangeInfo
	// result, ExpandedNodeId parentNodeId,
	// NodeId referenceTypeId, List<AddNodesItem> nodesToAdd,
	// List<AddReferencesItem> referencesToAdd,
	// List<NodeId> rekIds) {
	//
	// ExtensionObject attributes = null;
	// try {
	// attributes = ExtensionObject.binaryEncode(result.getNodeAttributes(),
	// EncoderContext.getDefaultInstance());
	// } catch (EncodingException e) {
	// e.printStackTrace();
	// }
	//
	// // create datatype node
	// ExpandedNodeId newNodeId =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
	// .toExpandedNodeId(result.getNodeId());
	//
	// AddNodesItem nodeToAdd = new AddNodesItem();
	// nodeToAdd.setBrowseName(result.getBrowseName());
	// nodeToAdd.setNodeAttributes(attributes);
	// nodeToAdd.setNodeClass(result.getNodeClass());
	// nodeToAdd.setParentNodeId(parentNodeId);
	// nodeToAdd.setReferenceTypeId(referenceTypeId);
	// nodeToAdd.setRequestedNewNodeId(newNodeId);
	// nodeToAdd.setTypeDefinition(null);
	//
	// nodesToAdd.add(nodeToAdd);
	//
	// for (ReferenceNode additionalReferenceNode :
	// result.getAdditionalReferences()) {
	// AddReferencesItem addReference = new AddReferencesItem();
	// addReference.setIsForward(!additionalReferenceNode.getIsInverse());
	// addReference.setReferenceTypeId(additionalReferenceNode.getReferenceTypeId());
	// addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
	//
	// NodeClass targetNodeClass =
	// ServerInstance.getNode(additionalReferenceNode.getTargetId()).getNodeClass();
	//
	// addReference.setSourceNodeId(result.getNodeId());
	//
	// addReference.setTargetNodeClass(targetNodeClass);
	// // addReference.setTargetServerUri(TargetServerUri);
	// referencesToAdd.add(addReference);
	// }
	//
	// NodeId typeId = null;
	// try {
	// typeId =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(parentNodeId);
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	//
	// // checks if the parent is the datatypefolder and not a base type
	// if (!Identifiers.DataTypesFolder.equals(typeId)) {
	// // create children of its type
	// createDataTypeAddNodes(nodesToAdd, referencesToAdd/* , addrSpace */,
	// ServerInstance.getInstance().getServerInstance().getTypeTable(), typeId,
	// newNodeId, rekIds);
	// }
	// }
	/**
	 * Recursive call to get all children
	 * 
	 * @param nodesToAdd
	 * @param referencesToAdd
	 * @param addrSpace
	 * @param nodeManager
	 * @param typeTree
	 * @param rekIds
	 * @param referenceMap
	 * @param type
	 * @param newNodeId
	 */
	// private static void createDataTypeAddNodes(List<AddNodesItem> nodesToAdd,
	// List<AddReferencesItem> referencesToAdd,
	// TypeTable typeTree, NodeId typeId, ExpandedNodeId parentNodeId,
	// List<NodeId> rekIds) {
	//
	// BrowseResult[] hierachicalbrowseResult = null;
	// try {
	// hierachicalbrowseResult = ServerInstance.browse(typeId,
	// Identifiers.HierarchicalReferences,
	// NodeClass.getSet(NodeClass.getMask(NodeClass.Object, NodeClass.Variable,
	// NodeClass.Method)),
	// BrowseResultMask.ALL, BrowseDirection.Forward, true);
	// // .getInstance()
	// // .getServerInstance()
	// // .getProfileManager()
	// // .browse(new BrowseDescription[] { new BrowseDescription(
	// // typeId, BrowseDirection.Forward,
	// // Identifiers.HierarchicalReferences, true,
	// // NodeClass.getMask(NodeClass.Object,
	// // NodeClass.Variable, NodeClass.Method),
	// // BrowseResultMask.getMask(BrowseResultMask.ALL)) },
	// // UnsignedInteger.ZERO, null);
	// } catch (ServiceResultException e1) {
	// e1.printStackTrace();
	// }
	// /**
	// * TODO: Nonhierachical references
	// */
	// BrowseResult[] nonhierachicalbrowseResult = null;
	// try {
	// nonhierachicalbrowseResult = ServerInstance.browse(typeId,
	// Identifiers.NonHierarchicalReferences,
	// NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Both, true);
	// // BrowseDirection.Both,
	// // Identifiers.NonHierarchicalReferences, true,
	// // NodeClass.getMask(NodeClass.ALL), BrowseResultMask
	// // .getMask(BrowseResultMask.ALL)
	// // UnsignedInteger.ZERO, null);
	// } catch (ServiceResultException e1) {
	// e1.printStackTrace();
	// }
	//
	// /**
	// * TODO: switch target id to new generated id
	// */
	// // only when there are browse results!
	// NodeId supertype =
	// ServerInstance.getInstance().getServerInstance().getTypeTable().findSuperType(typeId);
	// for (ReferenceDescription refDescription :
	// hierachicalbrowseResult[0].getReferences()) {
	// Node n = ServerInstance.getNode(refDescription.getNodeId());
	// NodeAttributes attributes = AddressSpaceUtil.fetchNodeAttribute(n);
	//
	// ExtensionObject encodedAttributes = null;
	// try {
	// encodedAttributes = ExtensionObject.binaryEncode(attributes,
	// EncoderContext.getDefaultInstance());
	// } catch (EncodingException e) {
	// e.printStackTrace();
	// }
	//
	// boolean ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
	// ExpandedNodeId newNodeId = new
	// ExpandedNodeId(ServerInstance.getInstance().getServerInstance()
	// .getAddressSpaceManager().getNodeFactory().getNextNodeId(parentNodeId.getNamespaceIndex(),
	// parentNodeId.getValue(), parentNodeId.getIdType(), rekIds, ccNodeId));
	//
	// // add node
	// AddNodesItem nodeToAdd = new AddNodesItem();
	// nodeToAdd.setBrowseName(refDescription.getBrowseName());
	// nodeToAdd.setNodeAttributes(encodedAttributes);
	// nodeToAdd.setNodeClass(refDescription.getNodeClass());
	// nodeToAdd.setParentNodeId(parentNodeId);
	// nodeToAdd.setReferenceTypeId(refDescription.getReferenceTypeId());
	// nodeToAdd.setRequestedNewNodeId(newNodeId);
	// nodeToAdd.setTypeDefinition(refDescription.getTypeDefinition());
	//
	// nodesToAdd.add(nodeToAdd);
	//
	// if (n.getNodeClass().equals(NodeClass.Variable)) {
	// createDataTypeAddNodes(nodesToAdd, referencesToAdd/* , addrSpace */,
	// typeTree, n.getNodeId(), newNodeId,
	// rekIds);
	// }
	// }
	//
	// if (nonhierachicalbrowseResult[0].getReferences() != null) {
	// for (ReferenceDescription refDesc :
	// nonhierachicalbrowseResult[0].getReferences()) {
	//
	// NodeId sourceId = null;
	// try {
	// sourceId =
	// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
	// .toNodeId(parentNodeId);
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	//
	// AddReferencesItem addReferenceItem = new AddReferencesItem();
	// addReferenceItem.setIsForward(refDesc.getIsForward());
	// addReferenceItem.setReferenceTypeId(refDesc.getReferenceTypeId());
	// addReferenceItem.setSourceNodeId(sourceId);
	// addReferenceItem.setTargetNodeClass(refDesc.getNodeClass());
	// addReferenceItem.setTargetNodeId(refDesc.getNodeId());
	// addReferenceItem.setTargetServerUri(refDesc.getNodeId().getNamespaceUri());
	//
	// referencesToAdd.add(addReferenceItem);
	// }
	// }
	//
	// // when the current node is an object node and no other child objects
	// // node are there, then go a step back
	// // when the end of the object type is reached
	// if (Identifiers.BaseDataType.equals(supertype)) {
	// return;
	// }
	// // when a supertype is available, create its children
	// else if (!NodeId.isNull(supertype)) {
	// createDataTypeAddNodes(nodesToAdd, referencesToAdd/* , addrSpace */,
	// typeTree, supertype, parentNodeId,
	// rekIds);
	// }
	// }
	private static AddNodesResult[] createVariableType(ExpandedNodeId parentNodeId, NodeClass parentNodeClass,
			NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping) {

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(), EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			e.printStackTrace();
		}
		AddNodesItem nodesToAdd = new AddNodesItem();
		nodesToAdd.setBrowseName(info.getBrowseName());
		nodesToAdd.setNodeAttributes(attributes);
		nodesToAdd.setNodeClass(info.getNodeClass());
		nodesToAdd.setParentNodeId(parentNodeId);
		nodesToAdd.setReferenceTypeId(referenceTypeId);
		nodesToAdd.setRequestedNewNodeId(new ExpandedNodeId(info.getNodeId()));
		// nodesToAdd.setRequestedNewNodeId(
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toExpandedNodeId(info.getNodeId()));
		nodesToAdd.setTypeDefinition(null);
		AddNodesResult[] results = null;
		try {
			results = ServerInstance.addNode(new AddNodesItem[] { nodesToAdd }, false);
			createReferences(info);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return results;
	}

	private static AddNodesResult[] createObjectType(ExpandedNodeId parentNodeId, NodeClass parentNodeClass,
			NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping) {

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(), EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			e.printStackTrace();
		}
		AddNodesItem nodesToAdd = new AddNodesItem();
		nodesToAdd.setBrowseName(info.getBrowseName());
		nodesToAdd.setNodeAttributes(attributes);
		nodesToAdd.setNodeClass(info.getNodeClass());
		nodesToAdd.setParentNodeId(parentNodeId);
		nodesToAdd.setReferenceTypeId(referenceTypeId);
		nodesToAdd.setRequestedNewNodeId(new ExpandedNodeId(info.getNodeId()));
		// nodesToAdd.setRequestedNewNodeId(
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toExpandedNodeId(info.getNodeId()));
		nodesToAdd.setTypeDefinition(null);
		AddNodesResult[] results = null;
		try {
			results = ServerInstance.addNode(new AddNodesItem[] { nodesToAdd }, false);
			createReferences(info);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return results;
	}

	private static void createReferences(ModelChangeInfo result) {
		List<AddReferencesItem> referencesToAdd = new ArrayList<AddReferencesItem>();
		for (ReferenceNode additionalReferenceNode : result.getAdditionalReferences()) {
			AddReferencesItem addReference = new AddReferencesItem();
			addReference.setIsForward(!additionalReferenceNode.getIsInverse());
			addReference.setReferenceTypeId(additionalReferenceNode.getReferenceTypeId());
			addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
			NodeClass targetNodeClass = ServerInstance.getNode(additionalReferenceNode.getTargetId()).getNodeClass();
			addReference.setSourceNodeId(result.getNodeId());
			addReference.setTargetNodeClass(targetNodeClass);
			// addReference.setTargetServerUri(TargetServerUri);
			referencesToAdd.add(addReference);
		}
		AddReferencesItem[] referencesToAddArray = referencesToAdd
				.toArray(new AddReferencesItem[referencesToAdd.size()]);
		try {
			if (referencesToAddArray != null && referencesToAddArray.length > 0) {
				ServerInstance.addReferences(referencesToAddArray);
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	private static AddNodesResult[] createMethod(ExpandedNodeId parentNodeId, NodeClass parentNodeClass,
			NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate, Map<ExpandedNodeId, ExpandedNodeId> mapping,
			boolean useNextIdFromParent) {
		Map<ExpandedNodeId, AddNodesItem> nodesToAdd = new HashMap<>();
		List<AddReferencesItem> referencesToAdd = new ArrayList<AddReferencesItem>();
		createMethodAddNodesItemStatement(info, parentNodeId, info.getNodeId(), referenceTypeId, nodesToAdd,
				referencesToAdd, parentNodeClass, isUpdate, /* rekIds, */ mapping, useNextIdFromParent);

		AddNodesResult[] results = null;
		try {
			if (!nodesToAdd.isEmpty()) {
				AddNodesItem[] nodesToAddArray = nodesToAdd.values().toArray(new AddNodesItem[nodesToAdd.size()]);
				results = ServerInstance.addNode(nodesToAddArray, false);
			}
			if (!referencesToAdd.isEmpty()) {
				AddReferencesItem[] referencesToAddArray = referencesToAdd
						.toArray(new AddReferencesItem[referencesToAdd.size()]);
				ServerInstance.addReferences(referencesToAddArray);
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(CreateFactory.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		return results;
	}

	private static AddNodesResult[] createObject(ExpandedNodeId parentNodeId, NodeClass parentNodeClass,
			NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate, Map<ExpandedNodeId, ExpandedNodeId> mapping,
			boolean useNextIdFromParent, boolean typeCreate) {
		Map<ExpandedNodeId, AddNodesItem> nodesToAdd = new HashMap<>();
		List<AddReferencesItem> referencesToAdd = new ArrayList<>();
		createObjectAddNodesItemStatement(info, parentNodeId, info.getNodeId(), referenceTypeId, nodesToAdd,
				referencesToAdd, parentNodeClass, isUpdate, mapping, useNextIdFromParent, typeCreate);
		AddNodesResult[] results = null;
		try {
			if (!nodesToAdd.isEmpty()) {
				AddNodesItem[] nodesToAddArray = nodesToAdd.values().toArray(new AddNodesItem[nodesToAdd.size()]);
				results = ServerInstance.addNode(nodesToAddArray, false);
			}
			if (!referencesToAdd.isEmpty()) {
				AddReferencesItem[] referencesToAddArray = referencesToAdd
						.toArray(new AddReferencesItem[referencesToAdd.size()]);
				ServerInstance.addReferences(referencesToAddArray);
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return results;
	}

	private static AddNodesResult[] createVariable(ExpandedNodeId parentNodeId, NodeClass parentNodeClass,
			NodeId referenceTypeId, ModelChangeInfo info, boolean isUpdate, Map<ExpandedNodeId, ExpandedNodeId> mapping,
			boolean useNextIdFromParent) {
		Map<ExpandedNodeId, AddNodesItem> nodesToAdd = new HashMap<>();
		List<AddReferencesItem> referencesToAdd = new ArrayList<>();
		createVariableAddNodesItemStatement(info, parentNodeId, info.getNodeId(), referenceTypeId, nodesToAdd,
				referencesToAdd, parentNodeClass, isUpdate, mapping, useNextIdFromParent);
		AddNodesResult[] results = null;
		try {
			if (!nodesToAdd.isEmpty()) {
				AddNodesItem[] nodesToAddArray = nodesToAdd.values().toArray(new AddNodesItem[nodesToAdd.size()]);
				results = ServerInstance.addNode(nodesToAddArray, false);
			}
			if (!referencesToAdd.isEmpty()) {
				AddReferencesItem[] referencesToAddArray = referencesToAdd
						.toArray(new AddReferencesItem[referencesToAdd.size()]);
				ServerInstance.addReferences(referencesToAddArray);
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(CreateFactory.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		return results;
	}

	private static void createMethodReferences(ModelChangeInfo result, List<AddReferencesItem> referencesToAdd) {
		for (ReferenceNode additionalReferenceNode : result.getAdditionalReferences()) {
			AddReferencesItem addReference = new AddReferencesItem();
			addReference.setIsForward(!additionalReferenceNode.getIsInverse());
			addReference.setReferenceTypeId(additionalReferenceNode.getReferenceTypeId());
			addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
			NodeClass targetNodeClass = ServerInstance.getNode(additionalReferenceNode.getTargetId()).getNodeClass();
			addReference.setSourceNodeId(result.getNodeId());
			addReference.setTargetNodeClass(targetNodeClass);
			// addReference.setTargetServerUri(TargetServerUri);
			referencesToAdd.add(addReference);
		}
		try {
			if (!referencesToAdd.isEmpty()) {
				AddReferencesItem[] referencesToAddArray = referencesToAdd
						.toArray(new AddReferencesItem[referencesToAdd.size()]);
				ServerInstance.addReferences(referencesToAddArray);
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	private static void createMethodAddNodesItemStatement(ModelChangeInfo info, ExpandedNodeId parentNodeId,
			NodeId nodeId, NodeId referenceTypeId, Map<ExpandedNodeId, AddNodesItem> nodesToAdd,
			List<AddReferencesItem> referencesToAdd, NodeClass parentNodeClass, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping, boolean useNextIdFromParent) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		AddNodesItem nodeToAdd = new AddNodesItem();
		ExpandedNodeId expNodeId = new ExpandedNodeId(nodeId);
		// ExpandedNodeId expNodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(nodeId);
		nodesToAdd.put(expNodeId, nodeToAdd);
		if (isUpdate) {
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			NodeId nextId2use = null;
			// continue ids from parent node
			if (useNextIdFromParent) {
				try {
					nextId2use = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(parentNodeId);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
			// continue ids from source node (started model change action)
			else {
				nextId2use = info.getNodeId();
			}
			nodeId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
					.getNextNodeId(nextId2use.getNamespaceIndex(), nextId2use.getValue(), nextId2use.getIdType(),
							/* rekIds, */ ccNodeId);
		}
		// rekIds.add(nodeId);
		ExpandedNodeId newNodeId = new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(),
				nsTable);
		// ExpandedNodeId newNodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(nodeId);
		nodeToAdd.setBrowseName(info.getBrowseName());
		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(), EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			e.printStackTrace();
		}
		nodeToAdd.setNodeAttributes(attributes);
		nodeToAdd.setNodeClass(NodeClass.Method);
		nodeToAdd.setParentNodeId(parentNodeId);
		nodeToAdd.setReferenceTypeId(referenceTypeId);
		nodeToAdd.setRequestedNewNodeId(newNodeId);
		nodeToAdd.setTypeDefinition(null);
		// no update, map, new node to type node
		if (!isUpdate) {
			// no type for plain method
			mapping.put(newNodeId, ExpandedNodeId.NULL);
		} else {
			mapping.put(newNodeId, expNodeId);
		}
		if (info.getNamingRule() != null) {
			NodeId ruleId = null;
			switch (info.getNamingRule()) {
			case ExposesItsArray:
				ruleId = Identifiers.ModellingRule_ExposesItsArray;
				break;
			case Mandatory:
				ruleId = Identifiers.ModellingRule_Mandatory;
				break;
			case Optional:
				ruleId = Identifiers.ModellingRule_Optional;
				break;
			case OptionalPlaceholder:
				ruleId = Identifiers.ModellingRule_OptionalPlaceholder;
				break;
			case MandatoryPlaceholder:
				ruleId = Identifiers.ModellingRule_MandatoryPlaceholder;
				break;
			}
			AddReferencesItem modelingRule = new AddReferencesItem();
			modelingRule.setIsForward(true);
			modelingRule.setReferenceTypeId(Identifiers.HasModellingRule);
			modelingRule.setSourceNodeId(NodeIdUtil.createNodeId(newNodeId.getNamespaceIndex(), newNodeId.getValue()));
			modelingRule.setTargetNodeClass(NodeClass.Object);
			modelingRule.setTargetNodeId(
					new ExpandedNodeId(ruleId));
			referencesToAdd.add(modelingRule);
		}
		if (info.hasRuleItself()) {
			// forward ref (child -> parent)
			// AddReferencesItem modelingItemForward = new AddReferencesItem();
			// modelingItemForward.setIsForward(true);
			// modelingItemForward.setReferenceTypeId(Identifiers.HasModelParent);
			// try {
			// modelingItemForward.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance().getNamespaceUris()
			// .toNodeId(newNodeId));
			// } catch (ServiceResultException e) {
			// e.printStackTrace();
			// }
			// modelingItemForward.setTargetNodeClass(parentNodeClass);
			// if (!isUpdate) {
			// modelingItemForward.setTargetNodeId(info.getParentNodeId());
			// } else {
			// modelingItemForward.setTargetNodeId(parentNodeId);
			// }
			// inverse ref (parent -> new child)
			// AddReferencesItem modelingItemInverse = new AddReferencesItem();
			// modelingItemInverse.setIsForward(false);
			// modelingItemInverse.setReferenceTypeId(Identifiers.HasModelParent);
			// try {
			// if (!isUpdate) {
			// modelingItemInverse.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance()
			// .getNamespaceUris()
			// .toNodeId(info.getParentNodeId()));
			// } else {
			// modelingItemInverse.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance()
			// .getNamespaceUris().toNodeId(parentNodeId));
			// }
			// } catch (ServiceResultException e) {
			// e.printStackTrace();
			// }
			// modelingItemInverse.setTargetNodeClass(info.getNodeClass());
			// modelingItemInverse.setTargetNodeId(newNodeId);
			//
			// referencesToAdd.add(modelingItemInverse);
			// referencesToAdd.add(modelingItemForward);
		}
		for (ReferenceNode additionalReferenceNode : info.getAdditionalReferences()) {
			AddReferencesItem addReference = new AddReferencesItem();
			addReference.setIsForward(!additionalReferenceNode.getIsInverse());
			addReference.setReferenceTypeId(additionalReferenceNode.getReferenceTypeId());
			addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
			Node node = ServerInstance.getNode(additionalReferenceNode.getTargetId());
			NodeClass targetNodeClass = NodeClass.Variable;
			if (node != null)
				targetNodeClass = node.getNodeClass();
			addReference.setSourceNodeId(info.getNodeId());
			addReference.setTargetNodeClass(targetNodeClass);
			// addReference.setTargetServerUri(TargetServerUri);
			referencesToAdd.add(addReference);
		}
		/**
		 * create method argument information
		 */
		if (info.isCreate()) {
			createMethodAddNodes(info, isUpdate, nodesToAdd, referencesToAdd, info.getType(), newNodeId, null, /*
																												 * rekIds,
																												 */
					mapping);
		}
		/**
		 * Paste
		 */
		else {
			Map<ExpandedNodeId, AddNodesItem> additional = info.getAdditionalPasteNodes();
			if (additional != null) {
				Map<ExpandedNodeId, ExpandedNodeId> pasteIdMapping = new HashMap<>();
				// this is already should exist in current addnodes item
				additional.remove(expNodeId);
				pasteIdMapping.put(expNodeId, newNodeId);
				// mapping for all new nodeids
				for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
					ExpandedNodeId key = entry.getKey();
					AddNodesItem value = entry.getValue();
					AddNodesItem mappedItem = new AddNodesItem();
					mappedItem.setBrowseName(value.getBrowseName());
					mappedItem.setNodeAttributes(value.getNodeAttributes());
					mappedItem.setNodeClass(value.getNodeClass());
					mappedItem.setReferenceTypeId(value.getReferenceTypeId());
					mappedItem.setTypeDefinition(value.getTypeDefinition());
					// key
					NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
					NodeId pasteId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeFactory()
							.getNextNodeId(key.getNamespaceIndex(), key.getValue(), key.getIdType(), ccNodeId);

					ExpandedNodeId newExpId = new ExpandedNodeId(pasteId);
					// ExpandedNodeId newExpId =
					// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					// .toExpandedNodeId(pasteId);
					pasteIdMapping.put(key, newExpId);
					nodesToAdd.put(newExpId, mappedItem);
				}
				/**
				 * map addnodeitem statement
				 */
				for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
					ExpandedNodeId key = entry.getKey();
					ExpandedNodeId newExpId = pasteIdMapping.get(key);
					AddNodesItem mappedItem = nodesToAdd.get(newExpId);
					AddNodesItem value = entry.getValue();
					mappedItem.setRequestedNewNodeId(newExpId);
					ExpandedNodeId p = pasteIdMapping.get(value.getParentNodeId());
					mappedItem.setParentNodeId(p);
				}
				/**
				 * map addreference statement
				 */
				for (AddReferencesItem entry : info.getAdditionalPasteReferences()) {
					try {
						AddReferencesItem newReference = new AddReferencesItem();
						newReference.setIsForward(entry.getIsForward());
						newReference.setReferenceTypeId(entry.getReferenceTypeId());
						newReference.setTargetNodeClass(entry.getTargetNodeClass());
						NodeId sr = entry.getSourceNodeId();
						ExpandedNodeId expSr = new ExpandedNodeId(sr);
						// ExpandedNodeId expSr = nsTable.toExpandedNodeId(sr);
						ExpandedNodeId srExpId = pasteIdMapping.get(expSr);
						newReference.setSourceNodeId(nsTable.toNodeId(srExpId));
						ExpandedNodeId tr = entry.getTargetNodeId();
						ExpandedNodeId trExpId = pasteIdMapping.get(tr);
						newReference.setTargetNodeId(trExpId);
						referencesToAdd.add(newReference);
					} catch (ServiceResultException sre) {
						sre.printStackTrace();
					}
				}
			}
		}
	}

	private static void createObjectAddNodesItemStatement(ModelChangeInfo info, ExpandedNodeId parentNodeId,
			NodeId nodeId, NodeId referenceTypeId, Map<ExpandedNodeId, AddNodesItem> nodesToAdd,
			List<AddReferencesItem> referencesToAdd, NodeClass parentNodeClass, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping, boolean useNextIdFromParent, boolean typeCreate) {

		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(), EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			e.printStackTrace();
		}
		AddNodesItem nodeToAdd = new AddNodesItem();

		NamespaceTable table = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		String nsUri = table.getUri(nodeId.getNamespaceIndex());
		ExpandedNodeId expNodeId = new ExpandedNodeId(nodeId);
		// ExpandedNodeId expNodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(nodeId);
		nodesToAdd.put(expNodeId, nodeToAdd);
		// when update use new id
		if (isUpdate) {
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			NodeId nextId2use = null;
			// continue ids from parent node
			if (useNextIdFromParent) {
				try {
					nextId2use = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(parentNodeId);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
			// continue ids from source node (started model change action)
			else {
				nextId2use = info.getNodeId();
			}
			nodeId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
					.getNextNodeId(nextId2use.getNamespaceIndex(), nextId2use.getValue(), nextId2use.getIdType(),
							ccNodeId);
		}
		ExpandedNodeId newNodeId = new ExpandedNodeId(nodeId);
		// ExpandedNodeId newNodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(nodeId);
		ExpandedNodeId newNodeType = null;
		switch (info.getNodeClass()) {
		case Method:
			newNodeType = ExpandedNodeId.NULL;
			break;
		default:
			newNodeType = new ExpandedNodeId(info.getType());
			// newNodeType =
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(info.getType());
			break;
		}
		nodeToAdd.setBrowseName(info.getBrowseName());
		nodeToAdd.setNodeAttributes(attributes);
		nodeToAdd.setNodeClass(info.getNodeClass());
		nodeToAdd.setParentNodeId(parentNodeId);
		nodeToAdd.setReferenceTypeId(referenceTypeId);
		nodeToAdd.setRequestedNewNodeId(newNodeId);
		nodeToAdd.setTypeDefinition(newNodeType);
		// no update, map, new node to type node
		if (!isUpdate) {
			mapping.put(newNodeId, newNodeType);
		} else {
			mapping.put(newNodeId, newNodeType);
		}
		if (info.getNamingRule() != null) {
			NodeId ruleId = null;
			switch (info.getNamingRule()) {
			case Mandatory:
				ruleId = Identifiers.ModellingRule_Mandatory;
				break;
			case Optional:
				ruleId = Identifiers.ModellingRule_Optional;
				break;
			case OptionalPlaceholder:
				ruleId = Identifiers.ModellingRule_OptionalPlaceholder;
				break;
			case MandatoryPlaceholder:
				ruleId = Identifiers.ModellingRule_MandatoryPlaceholder;
				break;
			case ExposesItsArray:
				ruleId = Identifiers.ModellingRule_ExposesItsArray;
				break;
			}
			AddReferencesItem modelingRule = new AddReferencesItem();
			modelingRule.setIsForward(true);
			modelingRule.setReferenceTypeId(Identifiers.HasModellingRule);
			modelingRule.setSourceNodeId(NodeIdUtil.createNodeId(newNodeId.getNamespaceIndex(), newNodeId.getValue()));
			modelingRule.setTargetNodeClass(NodeClass.Object);
			modelingRule.setTargetNodeId(
					new ExpandedNodeId(ruleId));
			referencesToAdd.add(modelingRule);
		}
		if (info.hasRuleItself()) {
			// forward ref (child -> parent)
			// AddReferencesItem modelingItemForward = new AddReferencesItem();
			// modelingItemForward.setIsForward(true);
			// modelingItemForward.setReferenceTypeId(Identifiers.HasModelParent);
			// try {
			// modelingItemForward.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance().getNamespaceUris()
			// .toNodeId(newNodeId));
			// } catch (ServiceResultException e) {
			// e.printStackTrace();
			// }
			// modelingItemForward.setTargetNodeClass(parentNodeClass);
			// if (!isUpdate) {
			// modelingItemForward.setTargetNodeId(info.getParentNodeId());
			// } else {
			// modelingItemForward.setTargetNodeId(parentNodeId);
			// }
			// inverse ref (parent -> new child)
			// AddReferencesItem modelingItemInverse = new AddReferencesItem();
			// modelingItemInverse.setIsForward(false);
			// modelingItemInverse.setReferenceTypeId(Identifiers.HasModelParent);
			// try {
			// if (!isUpdate) {
			// modelingItemInverse.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance()
			// .getNamespaceUris()
			// .toNodeId(info.getParentNodeId()));
			// } else {
			// modelingItemInverse.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance()
			// .getNamespaceUris().toNodeId(parentNodeId));
			// }
			// } catch (ServiceResultException e) {
			// e.printStackTrace();
			// }
			// modelingItemInverse.setTargetNodeClass(info.getNodeClass());
			// modelingItemInverse.setTargetNodeId(newNodeId);
			//
			// referencesToAdd.add(modelingItemInverse);
			// referencesToAdd.add(modelingItemForward);
		}
		for (ReferenceNode additionalReferenceNode : info.getAdditionalReferences()) {
			AddReferencesItem addReference = new AddReferencesItem();
			addReference.setIsForward(!additionalReferenceNode.getIsInverse());
			addReference.setReferenceTypeId(additionalReferenceNode.getReferenceTypeId());
			addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
			NodeClass targetNodeClass = ServerInstance.getNode(additionalReferenceNode.getTargetId()).getNodeClass();
			addReference.setSourceNodeId(info.getNodeId());
			addReference.setTargetNodeClass(targetNodeClass);
			// addReference.setTargetServerUri(TargetServerUri);
			referencesToAdd.add(addReference);
		}
		/**
		 * create type information
		 */
		if (info.isCreate()) {
			//
			List<ExpandedNodeId> overwritten = new ArrayList<>();
			createObjectAddNodes(info, isUpdate, nodesToAdd, referencesToAdd, info.getType(), newNodeId, null, mapping,
					overwritten, typeCreate);
			// change reference ids
			for (Entry<ExpandedNodeId, ExpandedNodeId> e : mapping.entrySet()) {
				objectReferencesToAdd(e.getValue(), referencesToAdd, e.getKey());
			}
			// remove overwritten nodes
			for (ExpandedNodeId ow : overwritten) {
				ExpandedNodeId node2remove = null;
				for (Entry<ExpandedNodeId, AddNodesItem> e : nodesToAdd.entrySet()) {
					if (e.getValue().getRequestedNewNodeId().equals(ow)) {
						node2remove = e.getKey();
						break;
					}
				}
				if (node2remove != null) {
					nodesToAdd.remove(node2remove);
				}
				List<AddReferencesItem> ref2remove = new ArrayList<>();
				for (AddReferencesItem ref : referencesToAdd) {
					if (ow.equals(ref.getTargetNodeId())) {
						ref2remove.add(ref);
					}
				}
				if (ref2remove != null) {
					referencesToAdd.removeAll(ref2remove);
				}
			}
		}
		/**
		 * Paste
		 */
		else {
			Map<ExpandedNodeId, AddNodesItem> additional = info.getAdditionalPasteNodes();
			if (additional != null) {
				Map<ExpandedNodeId, ExpandedNodeId> pasteIdMapping = new HashMap<>();
				// this is already should exist in current addnodes item
				additional.remove(expNodeId);
				pasteIdMapping.put(expNodeId, newNodeId);
				// mapping for all new nodeids
				for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
					ExpandedNodeId key = entry.getKey();
					AddNodesItem value = entry.getValue();
					AddNodesItem mappedItem = new AddNodesItem();
					mappedItem.setBrowseName(value.getBrowseName());
					mappedItem.setNodeAttributes(value.getNodeAttributes());
					mappedItem.setNodeClass(value.getNodeClass());
					mappedItem.setReferenceTypeId(value.getReferenceTypeId());
					mappedItem.setTypeDefinition(value.getTypeDefinition());
					// key
					NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
					NodeId pasteId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeFactory()
							.getNextNodeId(key.getNamespaceIndex(), key.getValue(), key.getIdType(), ccNodeId);

					ExpandedNodeId newExpId = new ExpandedNodeId(pasteId);
					// ExpandedNodeId newExpId =
					// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					// .toExpandedNodeId(pasteId);
					pasteIdMapping.put(key, newExpId);
					nodesToAdd.put(newExpId, mappedItem);
				}
				/**
				 * map addnodeitem statement
				 */
				for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
					ExpandedNodeId key = entry.getKey();
					ExpandedNodeId newExpId = pasteIdMapping.get(key);
					AddNodesItem mappedItem = nodesToAdd.get(newExpId);
					AddNodesItem value = entry.getValue();
					mappedItem.setRequestedNewNodeId(newExpId);
					ExpandedNodeId p = pasteIdMapping.get(value.getParentNodeId());
					mappedItem.setParentNodeId(p);
				}
				/**
				 * map addreference statement
				 */
				for (AddReferencesItem entry : info.getAdditionalPasteReferences()) {
					try {
						AddReferencesItem newReference = new AddReferencesItem();
						newReference.setIsForward(entry.getIsForward());
						newReference.setReferenceTypeId(entry.getReferenceTypeId());
						newReference.setTargetNodeClass(entry.getTargetNodeClass());
						NodeId sr = entry.getSourceNodeId();
						ExpandedNodeId expSr = new ExpandedNodeId(sr);
						// ExpandedNodeId expSr = nsTable.toExpandedNodeId(sr);
						ExpandedNodeId srExpId = pasteIdMapping.get(expSr);
						newReference.setSourceNodeId(table.toNodeId(srExpId));
						ExpandedNodeId tr = entry.getTargetNodeId();
						ExpandedNodeId trExpId = pasteIdMapping.get(tr);
						newReference.setTargetNodeId(trExpId);
						referencesToAdd.add(newReference);
					} catch (ServiceResultException sre) {
						sre.printStackTrace();
					}
				}
			}
		}
	}

	private static void createVariableAddNodesItemStatement(ModelChangeInfo info, ExpandedNodeId parentNodeId,
			NodeId nodeId, NodeId referenceTypeId, Map<ExpandedNodeId, AddNodesItem> nodesToAdd,
			List<AddReferencesItem> referencesToAdd, NodeClass parentNodeClass, boolean isUpdate,
			Map<ExpandedNodeId, ExpandedNodeId> mapping, boolean useNextIdFromParent) {

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(info.getNodeAttributes(), EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		ExpandedNodeId expNodeId = new ExpandedNodeId(nodeId);
		// ExpandedNodeId expNodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(nodeId);
		AddNodesItem nodeToAdd = new AddNodesItem();
		nodesToAdd.put(expNodeId, nodeToAdd);
		// when update, use new id
		if (isUpdate) {
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			NodeId nextId2use = null;
			// continue ids from parent node
			if (useNextIdFromParent) {
				try {
					nextId2use = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(parentNodeId);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
			// continue ids from source node (started model change action)
			else {
				nextId2use = info.getNodeId();
			}
			nodeId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
					.getNextNodeId(nextId2use.getNamespaceIndex(), nextId2use.getValue(), nextId2use.getIdType(),
							ccNodeId);
		}
		ExpandedNodeId newNodeId = new ExpandedNodeId(nodeId);
		// ExpandedNodeId newNodeId =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(nodeId);
		ExpandedNodeId variableType = new ExpandedNodeId(info);
		// ExpandedNodeId variableType =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(info.getType());
		nodeToAdd.setBrowseName(info.getBrowseName());
		nodeToAdd.setNodeAttributes(attributes);
		nodeToAdd.setNodeClass(info.getNodeClass());
		nodeToAdd.setParentNodeId(parentNodeId);
		nodeToAdd.setReferenceTypeId(referenceTypeId);
		nodeToAdd.setRequestedNewNodeId(newNodeId);
		nodeToAdd.setTypeDefinition(variableType);
		// no update, map, new node to type node
		if (!isUpdate) {
			mapping.put(newNodeId, variableType);
		} else {
			mapping.put(newNodeId, expNodeId);
		}
		// add namingrule reference
		if (info.getNamingRule() != null) {
			NodeId ruleId = null;
			switch (info.getNamingRule()) {
			case ExposesItsArray:
				ruleId = Identifiers.ModellingRule_ExposesItsArray;
				break;
			case Mandatory:
				ruleId = Identifiers.ModellingRule_Mandatory;
				break;
			case Optional:
				ruleId = Identifiers.ModellingRule_Optional;
				break;
			case OptionalPlaceholder:
				ruleId = Identifiers.ModellingRule_OptionalPlaceholder;
				break;
			case MandatoryPlaceholder:
				ruleId = Identifiers.ModellingRule_MandatoryPlaceholder;
				break;
			}
			AddReferencesItem modelingRule = new AddReferencesItem();
			modelingRule.setIsForward(true);
			modelingRule.setReferenceTypeId(Identifiers.HasModellingRule);
			modelingRule.setSourceNodeId(NodeIdUtil.createNodeId(newNodeId.getNamespaceIndex(), newNodeId.getValue()));
			modelingRule.setTargetNodeClass(NodeClass.Object);
			modelingRule.setTargetNodeId(
					new ExpandedNodeId(ruleId));
			referencesToAdd.add(modelingRule);
		}
		if (info.hasRuleItself()) {
			// forward ref (child -> parent)
			// AddReferencesItem modelingItemForward = new AddReferencesItem();
			// modelingItemForward.setIsForward(true);
			// modelingItemForward.setReferenceTypeId(Identifiers.HasModelParent);
			// try {
			// modelingItemForward.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance().getNamespaceUris()
			// .toNodeId(newNodeId));
			// } catch (ServiceResultException e) {
			// e.printStackTrace();
			// }
			// modelingItemForward.setTargetNodeClass(parentNodeClass);
			// if (!isUpdate) {
			// modelingItemForward.setTargetNodeId(result.getParentNodeId());
			// } else {
			// modelingItemForward.setTargetNodeId(parentNodeId);
			// }
			// // inverse ref (parent -> new child)
			//
			// AddReferencesItem modelingItemInverse = new AddReferencesItem();
			// modelingItemInverse.setIsForward(false);
			// modelingItemInverse.setReferenceTypeId(Identifiers.HasModelParent);
			// try {
			// if (!isUpdate) {
			// modelingItemInverse.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance()
			// .getNamespaceUris()
			// .toNodeId(result.getParentNodeId()));
			// } else {
			// modelingItemInverse.setSourceNodeId(ServerInstance
			// .getInstance().getServerInstance()
			// .getNamespaceUris().toNodeId(parentNodeId));
			// }
			// } catch (ServiceResultException e) {
			// e.printStackTrace();
			// }
			// modelingItemInverse.setTargetNodeClass(result.getNodeClass());
			// modelingItemInverse.setTargetNodeId(newNodeId);
			//
			// referencesToAdd.add(modelingItemInverse);
			// referencesToAdd.add(modelingItemForward);
		}
		for (ReferenceNode additionalReferenceNode : info.getAdditionalReferences()) {
			AddReferencesItem addReference = new AddReferencesItem();
			addReference.setIsForward(!additionalReferenceNode.getIsInverse());
			addReference.setReferenceTypeId(additionalReferenceNode.getReferenceTypeId());
			addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
			NodeClass targetNodeClass = ServerInstance.getNode(additionalReferenceNode.getTargetId()).getNodeClass();
			addReference.setSourceNodeId(info.getNodeId());
			addReference.setTargetNodeClass(targetNodeClass);
			// addReference.setTargetServerUri(TargetServerUri);
			referencesToAdd.add(addReference);
		}
		/**
		 * create type information
		 */
		if (info.isCreate()) {
			List<ExpandedNodeId> overwritten = new ArrayList<>();
			createVariableAddNodes(info, isUpdate, nodesToAdd, referencesToAdd, info.getType(), newNodeId, null,
					mapping, overwritten);
			for (Entry<ExpandedNodeId, ExpandedNodeId> e : mapping.entrySet()) {
				variableReferencesToAdd(e.getValue(), referencesToAdd, e.getKey());
			}
			for (ExpandedNodeId ow : overwritten) {
				ExpandedNodeId node2remove = null;
				for (Entry<ExpandedNodeId, AddNodesItem> e : nodesToAdd.entrySet()) {
					if (e.getValue().getRequestedNewNodeId().equals(ow)) {
						node2remove = e.getKey();
						break;
					}
				}
				if (node2remove != null) {
					nodesToAdd.remove(node2remove);
				}
				List<AddReferencesItem> ref2remove = new ArrayList<>();
				for (AddReferencesItem ref : referencesToAdd) {
					if (ow.equals(ref.getTargetNodeId())) {
						ref2remove.add(ref);
					}
				}
				if (ref2remove != null) {
					referencesToAdd.removeAll(ref2remove);
				}
			}
		}
		/**
		 * paste
		 */
		else {
			Map<ExpandedNodeId, AddNodesItem> additional = info.getAdditionalPasteNodes();
			if (additional != null) {
				Map<ExpandedNodeId, ExpandedNodeId> pasteIdMapping = new HashMap<>();
				// this is already should exist in current addnodes item
				additional.remove(expNodeId);
				pasteIdMapping.put(expNodeId, newNodeId);
				// mapping for all new nodeids
				for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
					ExpandedNodeId key = entry.getKey();
					AddNodesItem value = entry.getValue();
					AddNodesItem mappedItem = new AddNodesItem();
					mappedItem.setBrowseName(value.getBrowseName());
					mappedItem.setNodeAttributes(value.getNodeAttributes());
					mappedItem.setNodeClass(value.getNodeClass());
					mappedItem.setReferenceTypeId(value.getReferenceTypeId());
					mappedItem.setTypeDefinition(value.getTypeDefinition());
					// key
					NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
					NodeId pasteId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeFactory()
							.getNextNodeId(key.getNamespaceIndex(), key.getValue(), key.getIdType(), ccNodeId);
					ExpandedNodeId newExpId = new ExpandedNodeId(pasteId),
							pasteId.getValue(), nsTable);
					// ExpandedNodeId newExpId =
					// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					// .toExpandedNodeId(pasteId);
					pasteIdMapping.put(key, newExpId);
					nodesToAdd.put(newExpId, mappedItem);
				}
				/**
				 * map addnodeitem statement
				 */
				for (Entry<ExpandedNodeId, AddNodesItem> entry : additional.entrySet()) {
					ExpandedNodeId key = entry.getKey();
					ExpandedNodeId newExpId = pasteIdMapping.get(key);
					AddNodesItem mappedItem = nodesToAdd.get(newExpId);
					AddNodesItem value = entry.getValue();
					mappedItem.setRequestedNewNodeId(newExpId);
					ExpandedNodeId p = pasteIdMapping.get(value.getParentNodeId());
					mappedItem.setParentNodeId(p);
				}
				/**
				 * map addreference statement
				 */
				for (AddReferencesItem entry : info.getAdditionalPasteReferences()) {
					try {
						AddReferencesItem newReference = new AddReferencesItem();
						newReference.setIsForward(entry.getIsForward());
						newReference.setReferenceTypeId(entry.getReferenceTypeId());
						newReference.setTargetNodeClass(entry.getTargetNodeClass());
						NodeId sr = entry.getSourceNodeId();
						ExpandedNodeId expSr = new ExpandedNodeId(sr);
						// ExpandedNodeId expSr = nsTable.toExpandedNodeId(sr);
						ExpandedNodeId srExpId = pasteIdMapping.get(expSr);
						newReference.setSourceNodeId(nsTable.toNodeId(srExpId));
						ExpandedNodeId tr = entry.getTargetNodeId();
						ExpandedNodeId trExpId = pasteIdMapping.get(tr);
						newReference.setTargetNodeId(trExpId);
						referencesToAdd.add(newReference);
					} catch (ServiceResultException sre) {
						sre.printStackTrace();
					}
				}
			}
		}
	}

	private static void createMethodAddNodes(ModelChangeInfo info, boolean isUpdate,
			Map<ExpandedNodeId, AddNodesItem> addNodesItemList, List<AddReferencesItem> referencesToAdd, NodeId typeId,
			ExpandedNodeId targetNodeId, ExpandedNodeId parentNodeId, Map<ExpandedNodeId, ExpandedNodeId> mapping) {

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		// create inputarguments or outputarguments node for method
		Map<Node, Boolean> additionl = info.getAdditionalCreateNodes();
		int index = 0;
		for (Entry<Node, Boolean> entry : additionl.entrySet()) {
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			NodeId id = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
					.getNextNodeId(targetNodeId.getNamespaceIndex(), targetNodeId.getValue(), targetNodeId.getIdType(),
							ccNodeId);
			ExpandedNodeId newNodeId = new ExpandedNodeId(id);
//      referencesToAdd.get(index).setTargetNodeId(newNodeId);
//      index++;
			// ExpandedNodeId newNodeId =
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(id);
			Node n = entry.getKey();
			n.setNodeId(id);
			NodeAttributes attributes = AddressSpaceUtil.fetchNodeAttribute(n);
			ExtensionObject encodedAttributes = null;
			try {
				encodedAttributes = ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance());
			} catch (EncodingException e) {
				e.printStackTrace();
			}
			// boolean exists =
			// existsObjectIdInNodeList(info.getAdditionalCreateNodes(),
			// refDescription.getNodeId());
			// modelrule is good
			// if (exists) {
			// objectReferencesToAdd(refDescription, referencesToAdd,
			// newNodeId);
			ExpandedNodeId typeDef = new ExpandedNodeId(Identifiers.PropertyType);
			// ExpandedNodeId typeDef =
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(Identifiers.PropertyType);
			AddNodesItem nodesToAdd = new AddNodesItem();
			nodesToAdd.setBrowseName(n.getBrowseName());
			nodesToAdd.setNodeAttributes(encodedAttributes);
			nodesToAdd.setNodeClass(n.getNodeClass());
			nodesToAdd.setParentNodeId(targetNodeId);
			nodesToAdd.setReferenceTypeId(Identifiers.HasProperty);
			nodesToAdd.setRequestedNewNodeId(newNodeId);
			nodesToAdd.setTypeDefinition(typeDef);
			addNodesItemList.put(newNodeId, nodesToAdd);
			// add new node statement
			// if (!addNodesItemList.containsKey(refDescription.getNodeId())) {
			// no update, map, new node to type node
			if (!isUpdate) {
				mapping.put(newNodeId, typeDef);
			} else {
				// origin child
				mapping.put(newNodeId, typeDef);
			}
			// Modelling rule mandatory for input and output arguments
			NodeId ruleId = Identifiers.ModellingRule_Mandatory;
			AddReferencesItem modelingRule = new AddReferencesItem();
			modelingRule.setIsForward(true);
			modelingRule.setReferenceTypeId(Identifiers.HasModellingRule);
			modelingRule.setSourceNodeId(NodeIdUtil.createNodeId(newNodeId.getNamespaceIndex(), newNodeId.getValue()));
			modelingRule.setTargetNodeClass(NodeClass.Object);
			modelingRule.setTargetNodeId(
					new ExpandedNodeId(ruleId));
			referencesToAdd.add(modelingRule);
		}
	}

	private static void createObjectAddNodes(ModelChangeInfo info, boolean isUpdate,
			Map<ExpandedNodeId, AddNodesItem> addNodesItemList, List<AddReferencesItem> referencesToAdd, NodeId typeId,
			ExpandedNodeId targetNodeId, ExpandedNodeId parentNodeId, Map<ExpandedNodeId, ExpandedNodeId> mapping,
			List<ExpandedNodeId> overwritten, boolean typeCreate) {

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		/**
		 * add type related nodeds
		 */
		BrowseResult[] hierachicalForward = null;
		try {
			hierachicalForward = ServerInstance.browse(typeId, Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method)),
					BrowseResultMask.ALL, BrowseDirection.Forward, true);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}
		/**
		 * add type related references
		 */
		BrowseResult[] nonhierachicalbrowseResult = null;
		try {
			nonhierachicalbrowseResult = ServerInstance.browse(typeId, Identifiers.NonHierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method)),
					BrowseResultMask.ALL, BrowseDirection.Both, true);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}
		// only when there are browse results!
		if (nonhierachicalbrowseResult[0].getReferences() != null) {
			for (ReferenceDescription refDescription : nonhierachicalbrowseResult[0].getReferences()) {
				NodeId sourceId = null;
				try {
					sourceId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(targetNodeId);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
				AddReferencesItem addReferenceItem = new AddReferencesItem();
				addReferenceItem.setIsForward(refDescription.getIsForward());
				addReferenceItem.setReferenceTypeId(refDescription.getReferenceTypeId());
				addReferenceItem.setSourceNodeId(sourceId);
				addReferenceItem.setTargetNodeClass(refDescription.getNodeClass());
				addReferenceItem.setTargetServerUri(refDescription.getNodeId().getNamespaceUri());
				addReferenceItem.setTargetNodeId(refDescription.getNodeId());
				boolean exists = existsObjectIdInNodeList(info.getAdditionalCreateNodes(), refDescription.getNodeId());
				if (exists) {
					referencesToAdd.add(addReferenceItem);
				}
			}
		}
		// nothing more to do
		if (hierachicalForward != null && hierachicalForward[0] != null
				&& hierachicalForward[0].getReferences() != null) {
			for (ReferenceDescription refDescription : hierachicalForward[0].getReferences()) {
				Node n = ServerInstance.getNode(refDescription.getNodeId());
				NodeAttributes attributes = AddressSpaceUtil.fetchNodeAttribute(n);
				ExtensionObject encodedAttributes = null;
				try {
					encodedAttributes = ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance());
				} catch (EncodingException e) {
					e.printStackTrace();
				}
				NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
				NodeId id = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
						.getNextNodeId(targetNodeId.getNamespaceIndex(), targetNodeId.getValue(),
								targetNodeId.getIdType(), ccNodeId);

				ExpandedNodeId newNodeId = new ExpandedNodeId(id);
				// ExpandedNodeId newNodeId =
				// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				// .toExpandedNodeId(id);
				boolean exists = existsObjectIdInNodeList(info.getAdditionalCreateNodes(), refDescription.getNodeId());
				// modelrule is good
				if (exists) {
					// objectReferencesToAdd(refDescription.getNodeId(),
					// referencesToAdd, newNodeId);
					AddNodesItem nodesToAdd = new AddNodesItem();
					// if configured, to take the namespaceindex from type or not
					if (true && !typeCreate)
						nodesToAdd.setBrowseName(new QualifiedName(refDescription.getBrowseName().getNamespaceIndex(),
								refDescription.getBrowseName().getName()));
					else
						nodesToAdd.setBrowseName(new QualifiedName(info.getBrowseName().getNamespaceIndex(),
								refDescription.getBrowseName().getName()));
					nodesToAdd.setNodeAttributes(encodedAttributes);
					nodesToAdd.setNodeClass(refDescription.getNodeClass());
					nodesToAdd.setParentNodeId(targetNodeId);
					nodesToAdd.setReferenceTypeId(refDescription.getReferenceTypeId());
					nodesToAdd.setRequestedNewNodeId(newNodeId);
					nodesToAdd.setTypeDefinition(refDescription.getTypeDefinition());
					for (Entry<ExpandedNodeId, AddNodesItem> entry : addNodesItemList.entrySet()) {
						if (entry.getValue().getParentNodeId().equals(nodesToAdd.getParentNodeId())
								&& entry.getValue().getBrowseName().equals(nodesToAdd.getBrowseName())) {
							overwritten.add(newNodeId);
							break;
						}
					}
					// add new node statement
					if (!addNodesItemList.containsKey(refDescription.getNodeId())) {
						// TODO: CHECK FOR OVERWRITTEN OBJECTS/METHODS
						addNodesItemList.put(refDescription.getNodeId(), nodesToAdd);
						// no update, map, new node to type node
						if (!isUpdate) {
							mapping.put(newNodeId, refDescription.getNodeId());
						} else {
							// origin child
							mapping.put(newNodeId, refDescription.getNodeId());
						}
						if (n.getNodeClass().equals(NodeClass.Object) || n.getNodeClass().equals(NodeClass.Variable)
								|| n.getNodeClass().equals(NodeClass.Method)) {
							createObjectAddNodes(info, isUpdate, addNodesItemList, referencesToAdd, n.getNodeId(),
									newNodeId, targetNodeId, mapping, overwritten, typeCreate);
						}
					}
					// if add node already exists, add hierachical reference
					else {
						NodeId sourceId = null;
						try {
							sourceId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
									.toNodeId(targetNodeId);
						} catch (ServiceResultException e) {
							e.printStackTrace();
						}
						AddNodesItem target = addNodesItemList.get(refDescription.getNodeId());
						AddReferencesItem additionalHierachicalReferenceItem = new AddReferencesItem();
						additionalHierachicalReferenceItem.setIsForward(refDescription.getIsForward());
						additionalHierachicalReferenceItem.setReferenceTypeId(refDescription.getReferenceTypeId());
						additionalHierachicalReferenceItem.setSourceNodeId(sourceId);
						additionalHierachicalReferenceItem.setTargetNodeClass(refDescription.getNodeClass());
						additionalHierachicalReferenceItem
								.setTargetServerUri(refDescription.getNodeId().getNamespaceUri());
						additionalHierachicalReferenceItem.setTargetNodeId(target.getRequestedNewNodeId());
						referencesToAdd.add(additionalHierachicalReferenceItem);
					}
				}
			}
		}
		NodeId supertype = ServerInstance.getInstance().getServerInstance().getTypeTable().findSuperType(typeId);
		// when the current node is an object node and no other child objects
		// node are there, then go a step back
		// when the end of the object type is reached
		if (Identifiers.BaseObjectType.equals(supertype)) {
			return;
		}
		// when a supertype is available, create its children
		else if (!NodeId.isNull(supertype)) {
			createObjectAddNodes(info, isUpdate, addNodesItemList, referencesToAdd, supertype, targetNodeId, null,
					mapping, overwritten, typeCreate);
		}
	}

	private static void createVariableAddNodes(ModelChangeInfo result, boolean isUpdate,
			Map<ExpandedNodeId, AddNodesItem> nodesToAdd, List<AddReferencesItem> referencesToAdd, NodeId typeId,
			ExpandedNodeId targetNodeId, ExpandedNodeId parentNodeId, Map<ExpandedNodeId, ExpandedNodeId> mapping,
			List<ExpandedNodeId> overwritten) {
		BrowseResult[] hierachicalForward = null;
		try {
			hierachicalForward = ServerInstance.browse(typeId, Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.Variable)), BrowseResultMask.ALL,
					BrowseDirection.Forward, true);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}
		BrowseResult[] nonhierachicalbrowseResult = null;
		try {
			nonhierachicalbrowseResult = ServerInstance.browse(typeId, Identifiers.NonHierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method)),
					BrowseResultMask.ALL, BrowseDirection.Both, true);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		// only when there are browse results!
		if (nonhierachicalbrowseResult[0].getReferences() != null) {
			for (ReferenceDescription refDescription : nonhierachicalbrowseResult[0].getReferences()) {
				NodeId sourceId = null;
				try {
					sourceId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(targetNodeId);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
				AddReferencesItem addReferenceItem = new AddReferencesItem();
				addReferenceItem.setIsForward(refDescription.getIsForward());
				addReferenceItem.setReferenceTypeId(refDescription.getReferenceTypeId());
				addReferenceItem.setSourceNodeId(sourceId);
				addReferenceItem.setTargetNodeClass(refDescription.getNodeClass());
				addReferenceItem.setTargetServerUri(refDescription.getNodeId().getNamespaceUri());
				addReferenceItem.setTargetNodeId(refDescription.getNodeId());
				boolean exists = existsVariableIdInNodeList(result.getAdditionalCreateNodes(),
						refDescription.getNodeId());
				if (exists) {
					referencesToAdd.add(addReferenceItem);
				}
			}
		}
		if (hierachicalForward != null && hierachicalForward[0] != null
				&& hierachicalForward[0].getReferences() != null) {
			for (ReferenceDescription refDescription : hierachicalForward[0].getReferences()) {
				Node n = ServerInstance.getNode(refDescription.getNodeId());
				NodeAttributes attributes = AddressSpaceUtil.fetchNodeAttribute(n);
				ExtensionObject encodedAttributes = null;
				try {
					encodedAttributes = ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance());
				} catch (EncodingException e) {
					e.printStackTrace();
				}
				NodeId id = null;
				NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
				if (targetNodeId.getValue() instanceof UnsignedInteger) {
					id = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
							.getNextNodeId(targetNodeId.getNamespaceIndex(), targetNodeId.getValue(),
									targetNodeId.getIdType(), ccNodeId);
				} else {
					id = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
							.getNextNodeId(targetNodeId.getNamespaceIndex(), targetNodeId.getValue(), null, ccNodeId);
				}
				ExpandedNodeId newNodeId = new ExpandedNodeId(id);
				boolean exists = existsVariableIdInNodeList(result.getAdditionalCreateNodes(),
						refDescription.getNodeId());
				// modelrule is good
				if (exists) {
					AddNodesItem nodeToAdd = new AddNodesItem();
					nodeToAdd.setBrowseName(refDescription.getBrowseName());
					nodeToAdd.setNodeAttributes(encodedAttributes);
					nodeToAdd.setNodeClass(refDescription.getNodeClass());
					nodeToAdd.setParentNodeId(targetNodeId);
					nodeToAdd.setReferenceTypeId(refDescription.getReferenceTypeId());
					nodeToAdd.setRequestedNewNodeId(newNodeId);
					nodeToAdd.setTypeDefinition(refDescription.getTypeDefinition());
					for (Entry<ExpandedNodeId, AddNodesItem> entry : nodesToAdd.entrySet()) {
						if (entry.getValue().getParentNodeId().equals(nodeToAdd.getParentNodeId())
								&& entry.getValue().getBrowseName().equals(nodeToAdd.getBrowseName())) {
							overwritten.add(newNodeId);
							break;
						}
					}
					if (!nodesToAdd.containsKey(refDescription.getNodeId())) {
						// TODO: CHECK FOR OVERWRITTEN OBJECTS/METHODS
						nodesToAdd.put(refDescription.getNodeId(), nodeToAdd);
						// no update, map, new node to type node
						if (!isUpdate) {
							mapping.put(newNodeId, refDescription.getNodeId());
						} else {
							mapping.put(newNodeId, refDescription.getNodeId());
						}
						if (n.getNodeClass().equals(NodeClass.Variable)) {
							createVariableAddNodes(result, isUpdate, nodesToAdd, referencesToAdd, n.getNodeId(),
									newNodeId, targetNodeId, mapping, overwritten);
						}
					} else {
						NodeId sourceId = null;
						try {
							sourceId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
									.toNodeId(targetNodeId);
						} catch (ServiceResultException e) {
							e.printStackTrace();
						}
						AddNodesItem target = nodesToAdd.get(refDescription.getNodeId());
						AddReferencesItem additionalHierachicalReferenceItem = new AddReferencesItem();
						additionalHierachicalReferenceItem.setIsForward(refDescription.getIsForward());
						additionalHierachicalReferenceItem.setReferenceTypeId(refDescription.getReferenceTypeId());
						additionalHierachicalReferenceItem.setSourceNodeId(sourceId);
						additionalHierachicalReferenceItem.setTargetNodeClass(refDescription.getNodeClass());
						additionalHierachicalReferenceItem
								.setTargetServerUri(refDescription.getNodeId().getNamespaceUri());
						additionalHierachicalReferenceItem.setTargetNodeId(target.getRequestedNewNodeId());
						referencesToAdd.add(additionalHierachicalReferenceItem);
					}
				}
			}
		}
		NodeId supertype = ServerInstance.getInstance().getServerInstance().getTypeTable().findSuperType(typeId);
		// when the current node is an object node and no other child objects
		// node are there, then go a step back
		// when the end of the object type is reached
		if (Identifiers.BaseVariableType.equals(supertype)) {
			return;
		}
		// when a supertype is available, create its children
		else if (!NodeId.isNull(supertype)) {
			createVariableAddNodes(result, isUpdate, nodesToAdd, referencesToAdd, supertype, targetNodeId, null,
					mapping, overwritten);
		}
	}

	private static boolean existsObjectIdInNodeList(Map<Node, Boolean> additionalNodes, ExpandedNodeId nodeId) {
		// no modeling rule == MANDATORY
		if (additionalNodes == null) {
			return true;
		}
		for (Node node : additionalNodes.keySet()) {
			if (nodeId.equals(node.getNodeId())) {
				return additionalNodes.get(node);
			}
		}
		return true;
	}

	private static boolean existsVariableIdInNodeList(Map<Node, Boolean> additionalNodes, ExpandedNodeId nodeId) {
		// no modeling rule == MANDATORY
		if (additionalNodes == null) {
			return true;
		}
		for (Node node : additionalNodes.keySet()) {
			if (nodeId.equals(node.getNodeId())) {
				return additionalNodes.get(node);
			}
		}
		return true;
	}

	private static void objectReferencesToAdd(ExpandedNodeId id2change, List<AddReferencesItem> referencesToAdd,
			ExpandedNodeId newNodeId) {
		for (AddReferencesItem item : referencesToAdd) {
			if (item.getTargetNodeId().equals(id2change)) {
				item.setTargetNodeId(newNodeId);
			}
		}
	}

	private static void variableReferencesToAdd(ExpandedNodeId id2change, List<AddReferencesItem> referencesToAdd,
			ExpandedNodeId newNodeId) {
		for (AddReferencesItem item : referencesToAdd) {
			if (item.getTargetNodeId().equals(id2change)) {
				item.setTargetNodeId(newNodeId);
			}
		}
	}
}
