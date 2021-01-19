package com.bichler.astudio.opcua.opcmodeler.editor.node.models.change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.CreateFactory;

public class ModelTypDef {
	public NodeId typeId;
	public ExpandedNodeId nodeId;
	public NodeClass typeClass;
	public LocalizedText name;
	private ModelTreeDef modelTree;
	private ModelTreeDef typeTree;
	public ReferenceDescription reference;
	public boolean mapToType;

	/**
	 * Node attributes
	 * 
	 * @param attributeId
	 */
	public void buildChangedAttributeModelTree(UnsignedInteger attributeId) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		ModelTreeDef def = new ModelTreeDef();
		def.setNodeId(
				new ExpandedNodeId(nsTable.getUri(this.typeId.getNamespaceIndex()), this.typeId.getValue(), nsTable));
		// def.setNodeId(nsTable.toExpandedNodeId(this.typeId));
		List<ExpandedNodeId> already = new ArrayList<>();
		buildChangedAttributeModel(nsTable, def,
				new ExpandedNodeId(nsTable.getUri(Identifiers.RootFolder.getNamespaceIndex()),
						Identifiers.RootFolder.getValue(), nsTable),
				already);
		// buildChangedAttributeModel(nsTable, def,
		// nsTable.toExpandedNodeId(Identifiers.RootFolder), already);
		this.modelTree = def;
	}

	private void buildChangedAttributeModel(NamespaceTable nsTable, ModelTreeDef def, ExpandedNodeId nodeId,
			List<ExpandedNodeId> already) {
		if (!already.contains(nodeId)) {
			try {
				BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
				BrowseDescription description = new BrowseDescription();
				description.setBrowseDirection(BrowseDirection.Forward);
				description.setIncludeSubtypes(true);
				description.setNodeClassMask(NodeClass.ALL);
				description.setNodeId(nsTable.toNodeId(nodeId));
				description.setReferenceTypeId(Identifiers.HierarchicalReferences);
				description.setResultMask(BrowseResultMask.ALL);
				nodesToBrowse[0] = description;
				already.add(nodeId);
				BrowseResult[] result = ServerInstance.browse(nsTable.toNodeId(nodeId),
						Identifiers.HierarchicalReferences, NodeClass.ALL, BrowseResultMask.ALL,
						BrowseDirection.Forward, true);
				if (result != null && result.length > 0 && result[0].getReferences() != null
						&& result[0].getReferences().length > 0) {
					ReferenceDescription[] references = result[0].getReferences();
					for (ReferenceDescription reference : references) {
						ExpandedNodeId nodeTypeId = reference.getTypeDefinition();
						if (nodeTypeId != null && nodeTypeId.equals(def.getNodeId())) {
							ModelTreeDef treeDef = new ModelTreeDef();
							treeDef.setNodeId(reference.getNodeId());
							treeDef.setReference(reference);
							def.addChild(treeDef);
						}
						buildChangedAttributeModel(nsTable, def, reference.getNodeId(), already);
					}
				}
			} catch (ServiceResultException sre) {
				sre.printStackTrace();
			}
		}
	}

	/**
	 * Rebuilds the type structure (under type folders)
	 * 
	 * @param result
	 */
	public void buildChangedModelTree(ModelChangeInfo result) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ModelTreeDef def = new ModelTreeDef();
		def.setNodeId(
				new ExpandedNodeId(nsTable.getUri(this.typeId.getNamespaceIndex()), this.typeId.getValue(), nsTable));
		// def.setNodeId(nsTable.toExpandedNodeId(this.typeId));
		def.setNodeClass(this.typeClass);
		def.setUpdate(result);
		buildChangedModel(nsTable, def, this.typeId,
				new ExpandedNodeId(nsTable.getUri(result.getNodeId().getNamespaceIndex()),
						result.getNodeId().getValue(), nsTable),
				result);
		// buildChangedModel(nsTable, def, this.typeId,
		// nsTable.toExpandedNodeId(result.getNodeId()), result);
		// appendAdditionalTypeItems(nsTable, def, def.getNodeId(nsTable),
		// this.typeClass);
		this.modelTree = def;
	}

	/**
	 * Builds up a tree model of a type.
	 * 
	 * @param nsTable     Mapping id table
	 * @param parentDef   Parent Definition to add the children
	 * @param node2change Current new node id
	 * @param update      info
	 */
	private void buildChangedModel(NamespaceTable nsTable, ModelTreeDef parentDef, NodeId defId,
			ExpandedNodeId node2change, ModelChangeInfo update) {
		try {
			BrowseResult[] result = ServerInstance.browse(defId, Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method)),
					BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (result != null && result.length > 0 && result[0].getReferences() != null
					&& result[0].getReferences().length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					ModelTreeDef def = new ModelTreeDef();
					ExpandedNodeId refId = reference.getNodeId();
					def.setNodeId(refId);
					def.setReference(reference);
					def.setUpdate(update);
					// mark new created ones
					if (refId.equals(node2change)) {
						def.setChanged(true);
					}
					parentDef.addChild(def);
					if (!def.hasChanged()) {
						buildChangedModel(nsTable, def, def.getNodeId(nsTable), node2change, update);
					}
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		NodeId supertype = ServerInstance.getInstance().getServerInstance().getTypeTable().findSuperType(defId);
		// when the current node is an object node and no other child objects
		// node are there, then go a step back
		// when the end of the object type is reached
		if (Identifiers.BaseObjectType.equals(supertype)) {
			return;
		}
		// when a supertype is available, create its children
		else if (!NodeId.isNull(supertype)) {
			buildChangedModel(nsTable, parentDef, supertype, node2change, update);
		}
	}

	/**
	 * Builds remove model under type folder
	 * 
	 * @param nodes2remove
	 * @param nodes2remove2
	 */
	public void buildRemovedModelTree(List<ExpandedNodeId> nodes2remove) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ModelTreeDef def = new ModelTreeDef();
		def.setNodeId(
				new ExpandedNodeId(nsTable.getUri(this.typeId.getNamespaceIndex()), this.typeId.getValue(), nsTable));
		// def.setNodeId(nsTable.toExpandedNodeId(this.typeId));
		def.setNodeClass(this.typeClass);
		// node is the type
		if (nodes2remove.contains(def.getNodeId())) {
			def.setChanged(true);
		}
		List<ExpandedNodeId> preventLoop = new ArrayList<>();
		buildRemovedModel(nsTable, preventLoop, def, nodes2remove);
		this.modelTree = def;
	}

	private void buildRemovedModel(NamespaceTable nsTable, List<ExpandedNodeId> preventLoop, ModelTreeDef parentDef,
			List<ExpandedNodeId> nodes2remove) {
		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		BrowseDescription description = new BrowseDescription();
		description.setBrowseDirection(BrowseDirection.Forward);
		description.setIncludeSubtypes(true);
		description.setNodeClassMask(NodeClass.ALL);
		description.setNodeId(parentDef.getNodeId(nsTable));
		description.setReferenceTypeId(Identifiers.HierarchicalReferences);
		description.setResultMask(BrowseResultMask.ALL);
		nodesToBrowse[0] = description;
		preventLoop.add(parentDef.getNodeId());
		try {
			BrowseResult[] result = ServerInstance.browse(parentDef.getNodeId(nsTable),
					Identifiers.HierarchicalReferences, NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Forward,
					true);
			if (result != null && result.length > 0 && result[0].getReferences() != null
					&& result[0].getReferences().length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					ExpandedNodeId refId = reference.getNodeId();
					if (!preventLoop.contains(refId)) {
						ModelTreeDef def = new ModelTreeDef();
						def.setNodeId(refId);
						def.setNodeClass(reference.getNodeClass());
						def.setReference(reference);
						// mark new created ones
						if (nodes2remove.contains(refId)) {
							def.setChanged(true);
						}
						parentDef.addChild(def);
						buildRemovedModel(nsTable, preventLoop, def, nodes2remove);
					}
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Under ObjectsFolder.
	 * 
	 * Must be done from the root object node
	 * 
	 * @return
	 */
	public ModelTreeDef buildModelTree() {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ModelTreeDef def = new ModelTreeDef();
		def.setNodeId(this.nodeId);
		def.setNodeClass(this.typeClass);
		List<ExpandedNodeId> preventLoop = new ArrayList<>();
		buildModel(nsTable, preventLoop, def);
		this.modelTree = def;
		return def;
	}

	private void buildModel(NamespaceTable nsTable, List<ExpandedNodeId> preventLoop, ModelTreeDef parentDef) {
		preventLoop.add(parentDef.getNodeId());
		try {
			BrowseResult[] result = ServerInstance.browse(parentDef.getNodeId(nsTable),
					Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method)),
					BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (result != null && result.length > 0 && result[0].getReferences() != null
					&& result[0].getReferences().length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					ExpandedNodeId refId = reference.getNodeId();
					if (!preventLoop.contains(refId)) {
						ModelTreeDef def = new ModelTreeDef();
						def.setNodeId(refId);
						def.setNodeClass(reference.getNodeClass());
						def.setReference(reference);
						// mark new created ones
						parentDef.addChild(def);
						buildModel(nsTable, preventLoop, def);
					}
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Under TypesFolder
	 * 
	 * @return
	 */
	public ModelTreeDef buildTypeTree() {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ModelTreeDef def = new ModelTreeDef();
		def.setNodeId(
				new ExpandedNodeId(nsTable.getUri(this.typeId.getNamespaceIndex()), this.typeId.getValue(), nsTable));
		// def.setNodeId(nsTable.toExpandedNodeId(this.typeId));
		buildType(nsTable, def);
		this.typeTree = def;
		return def;
	}

	private void buildType(NamespaceTable nsTable, ModelTreeDef parentDef) {
		try {
			BrowseResult[] result = ServerInstance.browse(parentDef.getNodeId(nsTable),
					Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.ObjectType, NodeClass.VariableType,
							NodeClass.ReferenceType, NodeClass.DataType)),
					BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (result != null && result.length > 0 && result[0].getReferences() != null
					&& result[0].getReferences().length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					ModelTreeDef def = new ModelTreeDef();
					ExpandedNodeId refId = reference.getNodeId();
					def.setNodeId(refId);
					def.setReference(reference);
					parentDef.addChild(def);
					buildType(nsTable, def);
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	public ModelTreeDef getModelTree() {
		return this.modelTree;
	}

	public ModelTreeDef getTypeTree() {
		return this.typeTree;
	}

	/**
	 * Compares the changed model, with a model which has to update. Parent node
	 * cannot be modified, start with its children.
	 * 
	 * @param parentIds
	 * @param updateDef
	 * @param modelChangeNodes
	 * @param useNextIdFromParent
	 */
	public void doCompareModelCreate(Set<ExpandedNodeId> parentIds, ModelTypDef updateDef,
			Map<ExpandedNodeId, AddNodesResult[]> modelChangeNodes, Map<ExpandedNodeId, ExpandedNodeId> mapping,
			boolean useNextIdFromParent) {
		// get designer type mapping
		HashMap<ExpandedNodeId, List<ExpandedNodeId>> typeMapping = ServerInstance.getTypeModel().getTypeMapping();
		HashMap<ExpandedNodeId, ExpandedNodeId> objectMapping = ServerInstance.getTypeModel().getObjectMapping();
		// which nodes are infected through the model change
		Set<ExpandedNodeId> affected = new HashSet<>();
		// get model of type
		ModelTreeDef rootTypeModel = getModelTree();
		// find node which is parent of model change
		ModelTreeDef typeParentDef = findCreate(rootTypeModel);
		List<ExpandedNodeId> types = new ArrayList<>();
		// (also include first id)
		fetchSubTypes(types, typeParentDef.getNodeId());
		// if nodeclass is type and has subtypes
		for (ExpandedNodeId type : types) {
			// get all affected from the node
			fillAffectedTypeMapping(typeMapping, affected, type);
		}
		// model to update
		ModelTreeDef omTree = updateDef.getModelTree();
		// find node which is parent of model change
		ModelTreeDef parentDef = deep(omTree, affected);
		// the related node belongs to an other
		if (parentDef == null) {
			ExpandedNodeId typeIntern2 = objectMapping.get(typeParentDef.getNodeId());
			parentDef = findParentDef(typeIntern2, objectMapping, typeMapping, omTree);
		}
		if (parentDef == null) {
			// throw new NullPointerException(
			// "Parentdef is null, cannot match model");
			Logger.getLogger(getClass().getName()).log(Level.INFO, CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.modeloutofsync")
					+ " "+omTree.getNodeId()+ " from type " + typeParentDef.getNodeId());
		} else {
			// update (create nodes)
			updateModel(parentIds, typeParentDef, parentDef, modelChangeNodes, mapping, useNextIdFromParent);
		}
	}

	public ModelTypDef findParentTypeClass() {
		try {
			BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
			BrowseDescription description = new BrowseDescription();
			description.setBrowseDirection(BrowseDirection.Inverse);
			description.setIncludeSubtypes(true);
			description.setNodeClassMask(NodeClass.getMask(NodeClass.ObjectType, NodeClass.VariableType,
					NodeClass.ReferenceType, NodeClass.DataType));
			description.setNodeId(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(this.nodeId));
			description.setReferenceTypeId(Identifiers.HasSubtype);
			description.setResultMask(BrowseResultMask.ALL);
			nodesToBrowse[0] = description;
			BrowseResult[] result = ServerInstance.browse(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(this.nodeId),
					Identifiers.HasSubtype,
					NodeClass.getSet(NodeClass.getMask(NodeClass.ObjectType, NodeClass.VariableType,
							NodeClass.ReferenceType, NodeClass.DataType)),
					BrowseResultMask.ALL, BrowseDirection.Inverse, true);
			if (result != null && result.length > 0 && result[0].getReferences() != null
					&& result[0].getReferences().length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					ModelTypDef def = new ModelTypDef();
					def.nodeId = reference.getNodeId();
					def.typeClass = reference.getNodeClass();
					def.reference = reference;
					return def;
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ModelTreeDef findParentDef(ExpandedNodeId type,
			// Set<ExpandedNodeId> affected,
			HashMap<ExpandedNodeId, ExpandedNodeId> objectMapping,
			HashMap<ExpandedNodeId, List<ExpandedNodeId>> typeMapping, ModelTreeDef originModel) {
		// last type is its own type definition, must be skipped
		ExpandedNodeId isLastType = objectMapping.get(type);
		if (isLastType == null) {
			return null;
		}
		Set<ExpandedNodeId> affected = new HashSet<>();
		// get all affected from the node
		fillAffectedTypeMapping(typeMapping, affected, type);
		ModelTreeDef parentDef = deep(originModel, affected);
		if (parentDef == null) {
			ExpandedNodeId typeIntern2 = objectMapping.get(type);
			if (typeIntern2 != null) {
				parentDef = findParentDef(typeIntern2, objectMapping, typeMapping, originModel);
			}
		}
		return parentDef;
	}

	private void fillAffectedTypeMapping(HashMap<ExpandedNodeId, List<ExpandedNodeId>> typeMapping,
			Set<ExpandedNodeId> affected, ExpandedNodeId parent2insert) {
		List<ExpandedNodeId> relatedNodes = typeMapping.get(parent2insert);
		if (relatedNodes == null) {
			return;
		}
		affected.addAll(relatedNodes);
		for (ExpandedNodeId related : relatedNodes) {
			fillAffectedTypeMapping(typeMapping, affected, related);
		}
	}

	private void fetchSubTypes(List<ExpandedNodeId> types, ExpandedNodeId typeParentDef) {
		types.add(typeParentDef);
		try {
			BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
			BrowseDescription description = new BrowseDescription();
			description.setBrowseDirection(BrowseDirection.Forward);
			description.setIncludeSubtypes(true);
			description.setNodeClassMask(NodeClass.getMask(NodeClass.DataType, NodeClass.ObjectType,
					NodeClass.ReferenceType, NodeClass.VariableType));
			description.setNodeId(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(typeParentDef));
			description.setReferenceTypeId(Identifiers.HierarchicalReferences);
			description.setResultMask(BrowseResultMask.ALL);
			nodesToBrowse[0] = description;
			BrowseResult[] result = ServerInstance.browse(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(typeParentDef),
					Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.DataType, NodeClass.ObjectType,
							NodeClass.ReferenceType, NodeClass.VariableType)),
					BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (result != null && result.length > 0 && result[0].getReferences() != null
					&& result[0].getReferences().length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					fetchSubTypes(types, reference.getNodeId());
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	private ModelTreeDef deep(ModelTreeDef omTree, Set<ExpandedNodeId> affected) {
		ExpandedNodeId id = omTree.getNodeId();
		if (affected.contains(id) && !omTree.hasChanged()) {
			omTree.setChanged(true);
			return omTree;
		}
		ModelTreeDef found = null;
		for (ModelTreeDef child : omTree.getChildren()) {
			found = deep(child, affected);
			if (found != null) {
				break;
			}
		}
		return found;
	}

	private ModelTreeDef findCreate(ModelTreeDef parent) {
		ModelTreeDef[] children = parent.getChildren();
		ModelTreeDef result = null;
		for (ModelTreeDef child : children) {
			if (child.hasChanged()) {
				return parent;
			}
			result = findCreate(child);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	private ModelTreeDef findRemove(ModelTreeDef parent) {
		ModelTreeDef[] children = parent.getChildren();
		ModelTreeDef result = null;
		for (ModelTreeDef child : children) {
			if (child.hasChanged()) {
				return child;
			}
			result = findRemove(child);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	private void updateModel(Set<ExpandedNodeId> parentIds, ModelTreeDef changed, ModelTreeDef modelParent,
			Map<ExpandedNodeId, AddNodesResult[]> modelChangeNodes, Map<ExpandedNodeId, ExpandedNodeId> mapping,
			boolean useNextIdFromParent) {
		/**
		 * parent info
		 */
		ExpandedNodeId parentId = modelParent.getNodeId();
		parentIds.add(parentId);
		/**
		 * Child change info
		 */
		NodeClass c = modelParent.getTypeClass();
		ModelChangeInfo ui = changed.getUpdateInfo();
		AddNodesResult[] newly = CreateFactory.create(c, parentId, modelParent.getTypeClass(), ui.getReferenceTypeId(),
				ui, true, mapping, useNextIdFromParent, false);
		modelChangeNodes.put(parentId, newly);
	}

	public void doCompareModelRemove(List<ExpandedNodeId> objectNodes2Remove, List<ExpandedNodeId> nodes2remove,
			ModelTypDef updateDef) {
		// get designer type mapping
		HashMap<ExpandedNodeId, List<ExpandedNodeId>> typeMapping = ServerInstance.getTypeModel().getTypeMapping();
		HashMap<ExpandedNodeId, ExpandedNodeId> objectMapping = ServerInstance.getTypeModel().getObjectMapping();
		ModelTreeDef rootTypeModel = getModelTree();
		// find node which is parent of model change
		ModelTreeDef typeParentDef = findRemove(rootTypeModel);
		// object model to change
		ModelTreeDef objectTree = updateDef.getModelTree();
		for (ExpandedNodeId remove : nodes2remove) {
			List<ExpandedNodeId> types = new ArrayList<>();
			// (also include first id)
			fetchSubTypes(types, remove);
			/**
			 * fetchSubTypes(types, typeParentDef.getNodeId());
			 */
			// changed model
			// ModelTreeDef typeTree = getModelTree();
			// object model
			ModelTreeDef parentDef = null;
			for (ExpandedNodeId type : types) {
				Set<ExpandedNodeId> affected = new HashSet<>();
				fillAffectedTypeMapping(typeMapping, affected, type);
				parentDef = deep(objectTree, affected);
				if (parentDef == null) {
					ExpandedNodeId typeIntern2 = null;
					if (typeParentDef != null) {
						typeIntern2 = objectMapping.get(typeParentDef.getNodeId());
						parentDef = findParentDef(typeIntern2, objectMapping, typeMapping, objectTree);
					} else {
						System.out.println();
					}
				}
				if (parentDef == null) {
					// throw new NullPointerException(
					// "Parentdef is null, cannot match model");
					/**
					 * Model out of sync
					 */
				} else {
					updateModel(objectMapping, typeMapping, objectNodes2Remove, parentDef);
					break;
				}
			}
		}
	}

	/**
	 * remove model change
	 * 
	 * @param typeMapping
	 * @param objectMapping
	 * 
	 * @param nodes2remove
	 * @param parents2refresh
	 * @param objectTree
	 * @param parentId
	 */
	private void updateModel(HashMap<ExpandedNodeId, ExpandedNodeId> objectMapping,
			HashMap<ExpandedNodeId, List<ExpandedNodeId>> typeMapping, List<ExpandedNodeId> nodes2remove,
			ModelTreeDef objectTree) {
		/**
		 * parent info
		 */
		ExpandedNodeId nodeId = objectTree.getNodeId();
		nodes2remove.add(nodeId);
	}

	public void matchTypeModel(ModelTypDef typeDef) {
	}
}
