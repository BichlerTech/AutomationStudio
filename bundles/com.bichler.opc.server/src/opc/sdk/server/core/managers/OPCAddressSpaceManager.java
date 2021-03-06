package opc.sdk.server.core.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowsePath;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowsePathTarget;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.CallMethodRequest;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.DeleteReferencesItem;
import org.opcfoundation.ua.core.HistoryReadDetails;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryReadValueId;
import org.opcfoundation.ua.core.HistoryUpdateDetails;
import org.opcfoundation.ua.core.HistoryUpdateResult;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.RelativePath;
import org.opcfoundation.ua.core.RelativePathElement;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.core.ViewAttributes;
import org.opcfoundation.ua.core.ViewDescription;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;
import org.opcfoundation.ua.utils.NumericRange;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.IOPCAddressSpaceManager;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.AbstractNodeFactory;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.node.ViewNode;
import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.subscription.IMonitoredItem;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.OPCServerOperation;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.addressspace.OPCAddressSpace;
import opc.sdk.server.service.history.HistoryManager;
import opc.sdk.server.service.node.OPCServerNodeFactory;
import opc.sdk.server.service.node.UAServerObjectNode;
import opc.sdk.server.service.node.UAServerVariableNode;
import opc.sdk.server.service.opc.browse.BrowseContinuationPoint;
import opc.sdk.server.service.opc.browse.OPCBrowseSet;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.server.service.util.AddressSpaceUtil;
import opc.sdk.server.service.util.UUIDUtil;
import opc.sdk.ua.IOPCOperation;
import opc.sdk.ua.classes.BaseEventType;
import opc.sdk.ua.constants.BrowseNames;

public class OPCAddressSpaceManager extends OPCNamespaceManager implements IOPCAddressSpaceManager {
	/** address space lock */
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
	/** user authority manager */
	private OPCUserAuthentificationManager userAuthentification = null;
	/** address spaces grouped by namespace index */
	private Map<Integer, OPCAddressSpace> addressSpace = new HashMap<>();
	/** server node factory */
	private DefaultNodeFactory nodeFactory = null;
	/** threadpool for external tasks */
	private ExecutorService publishExecutor = Executors.newSingleThreadExecutor();
	private ExecutorService historyExecutor = Executors.newSingleThreadExecutor();
	boolean hasInitialized = false;

	/** Address space util */
	public OPCAddressSpaceManager(OPCInternalServer server) {
		super(server);
		this.nodeFactory = new OPCServerNodeFactory(this, server.getHistoryManager());
	}

	public void addNodes(NamespaceTable externNsTable, Node[] nodes) {
		String[] addIfNew = externNsTable.toArray();
		NamespaceTable serverNs = getNamespaceUris();
		for (String nsUri : addIfNew) {
			int index = serverNs.getIndex(nsUri);
			if (index < 0) {
				serverNs.add(nsUri);
			}
		}
		for (Node node : nodes) {
			NodeId nodeId = node.getNodeId();
			// change nodeid from node to server id
			NodeId nodeId2 = binaryChangeNodeId(externNsTable, nodeId);
			node.setNodeId(nodeId2);
			// change reference nodeids to server ids
			ReferenceNode[] references = node.getReferences();
			if (references != null) {
				try {
					for (ReferenceNode reference : references) {
						NodeId refId = reference.getReferenceTypeId();
						NodeId refId2 = binaryChangeNodeId(externNsTable, refId);
						ExpandedNodeId targetId = reference.getTargetId();
						ExpandedNodeId targetId2 = binaryChangeNodeId(externNsTable, targetId);
						reference.setReferenceTypeId(refId2);
						reference.setTargetId(targetId2);
					}
				} catch (ServiceResultException sre) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, sre);
				}
			}
			NodeClass nodeClass = node.getNodeClass();
			// change datatype nodeids to server ids
			NodeId dataType;
			NodeId dataType2;
			switch (nodeClass) {
			case Variable:
				dataType = ((UAVariableNode) node).getDataType();
				dataType2 = binaryChangeNodeId(externNsTable, dataType);
				((UAVariableNode) node).setDataType(dataType2);
				// HBS 19.01.2017 add correction of nodeid of argument datatype
				if (((UAVariableNode) node).getValue() != null
						&& ((UAVariableNode) node).getValue().getCompositeClass() == Argument.class) {
					if (((UAVariableNode) node).getValue().isArray()) {
						for (Argument arg : (Argument[]) ((UAVariableNode) node).getValue().getValue()) {
							dataType = arg.getDataType();
							dataType2 = binaryChangeNodeId(externNsTable, dataType);
							arg.setDataType(dataType2);
						}
					} else {
						Argument arg = (Argument) ((UAVariableNode) node).getValue().getValue();
						dataType = arg.getDataType();
						dataType2 = binaryChangeNodeId(externNsTable, dataType);
						arg.setDataType(dataType2);
					}
				}
				break;
			case VariableType:
				dataType = ((UAVariableTypeNode) node).getDataType();
				dataType2 = binaryChangeNodeId(externNsTable, dataType);
				((UAVariableTypeNode) node).setDataType(dataType2);
				break;
			default:
				break;
			}
			// add node to address space
			addNodeToAddressSpace(node.getNodeId(), node);
			// add node to typetable
			switch (nodeClass) {
			case DataType:
			case ObjectType:
			case ReferenceType:
			case VariableType:
				getTypeTable().addTypeNode(node);
				break;
			default:
				break;
			}
		}
		try {
			getServer().writeNamespaceTable();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}

	public StatusCode[] deleteNodes(DeleteNodesItem[] nodesToDelete, OPCServerSession session)
			throws ServiceResultException {
		if (nodesToDelete == null || nodesToDelete.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		this.lock.writeLock().lock();
		try {
			List<StatusCode> results = new ArrayList<>();
			for (int i = 0; i < nodesToDelete.length; i++) {
				DeleteNodesItem nodeToDelete = nodesToDelete[i];
				Node node = getNodeById(nodeToDelete.getNodeId());
				// node does not exist
				if (node == null) {
					results.add(new StatusCode(StatusCodes.Bad_NodeIdInvalid));
					continue;
				}
				// check permission
				ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.DeleteNode,
						session, node);
				if (error != null && error.isBad()) {
					results.add(error.getCode());
					continue;
				}
				StatusCode result = deleteNode(nodeToDelete.getNodeId(), nodeToDelete.getDeleteTargetReferences());
				results.add(result);
				getServer().logService(RequestType.DeleteNodes,
						"Node " + node.getNodeId() + " " + node.getBrowseName() + " is removed from addressspace");
			}
			return results.toArray(new StatusCode[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	public StatusCode deleteNode(NodeId nodeId, boolean deleteTargetReferences) {
		Node node = removeNode(nodeId);
		if (node == null)
			return new StatusCode(StatusCodes.Bad_NodeIdUnknown);
		for (ReferenceNode refNode : node.getReferences()) {
			/**
			 * Removes all References that are targeting to the Node
			 */
			if (deleteTargetReferences) {
				Node deleteTargetReferenceNode = getNodeById(refNode.getTargetId());
				try {
					ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(node.getNodeId());
					deleteTargetReferenceNode.deleteReferences(eNid);
				} catch (NullPointerException npe) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
				}
			}
			/**
			 * Removes all References that are direct away from the Node to delete
			 */
			else {
				node.deleteReferences(refNode.getTargetId());
			}
		}
		return StatusCode.GOOD;
	}

	public NodeId[] getAllNodeIds(int namespaceIndex) {
		this.lock.readLock().lock();
		try {
			OPCAddressSpace addressSpace = this.addressSpace.get(namespaceIndex);
			if (addressSpace == null) {
				return new NodeId[0];
			}
			return addressSpace.getNodeIds();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	public AbstractNodeFactory getNodeFactory() {
		return this.nodeFactory;
	}

	@Override
	public boolean start() {
		this.hasInitialized = true;
		return this.hasInitialized;
	}

	@Override
	public boolean stop() {
		this.hasInitialized = false;
		return this.hasInitialized;
	}

	public Map<Integer, Integer> changeNamespaceTable(NamespaceTable originNamespaceTable,
			NamespaceTable newMappingTable) {
		// new namespaces to change
		String[] newNamespaces = originNamespaceTable.toArray();
		// mapping for current server node ids
		List<Integer> serverIndizesMapping = new ArrayList<>();
		// server nstable mapping
		for (String newNs : newNamespaces) {
			if (newNs == null) {
				continue;
			}
			Integer index = getNamespaceUris().getIndex(newNs);
			// add to mapping
			serverIndizesMapping.add(index);
		}
		// copy collection of address space
		Map<Integer, OPCAddressSpace> oldSpaces = new HashMap<>(this.addressSpace);
		// new address spaces
		Map<Integer, OPCAddressSpace> newSpaces = new LinkedHashMap<>();
		Map<Integer, Integer> nsMapping = new HashMap<>();
		// create new address space
		for (int i = 0; i < serverIndizesMapping.size(); i++) {
			int mapping = serverIndizesMapping.get(i);
			// create new address space for added element
			if (mapping < 0) {
				newSpaces.put(i, new OPCAddressSpace());
			}
			// map from old to new space
			else {
				OPCAddressSpace space = oldSpaces.get(mapping);
				newSpaces.put(i, space);
				nsMapping.put(mapping, i);
			}
		}
		// map all nodeids in address space
		for (OPCAddressSpace space : newSpaces.values()) {
			if (space == null) {
				continue;
			}
			space.mapNamespaceIndex(getNamespaceUris(), nsMapping, newMappingTable);
		}
		this.addressSpace = newSpaces;
		return nsMapping;
	}

	/**
	 * Changes the nodeId of a node. Changes also the references related to the
	 * changed NodeId Node. (SYNCHRoNIZED)
	 * 
	 * 
	 * @param SourceNode Selected node to change its NodeId.
	 * @param NewNodeId  New NodeId for the sourceNode.
	 * @return TRUE if the NodeId has changed, otherwise FALSE
	 */
	public boolean changeNodeId(NamespaceTable nsTable, Node sourceNode, NodeId newNodeId) {
		NodeId oldNodeId = null;
		this.lock.writeLock().lock();
		try {
			/**
			 * newNodeId is not available in the AddressSpace, and the node is able to
			 * change its NodeId
			 */
			if (getNodeById(newNodeId) == null) {
				oldNodeId = sourceNode.getNodeId();
				// remove node from address space
				removeNode(sourceNode.getNodeId());
				// change attributes
				sourceNode.setNodeId(newNodeId);
				// add node to address space with new node id
				addNodeToAddressSpaceOnly(sourceNode.getNodeId(), sourceNode);
				// TODO: update internal node attributes (e.g.: datatypeid in
				// variablenode)
			}
			/**
			 * newNodeId is available in the AddressSpace and the node is not able to change
			 * its NodeId
			 */
			else {
				return false;
			}
			/**
			 * change ReferenceNodes, because their NodeIds has to be changed too
			 */
			ReferenceNode[] references = sourceNode.getReferences();
			for (ReferenceNode sourceReferenceNode : references) {
				/** get the target node of the source reference */
				Node targetNode = getNodeById(sourceReferenceNode.getTargetId());
				for (ReferenceNode targetReferenceNode : targetNode.getReferences()) {
					try {
						NodeId target = nsTable.toNodeId(targetReferenceNode.getTargetId());
						/**
						 * when the target reference references the source node
						 */
						if (!NodeId.isNull(target) && target.equals(oldNodeId)) {
							/**
							 * change the reference to the new source nodes nodeid
							 */
							ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(newNodeId);
							targetReferenceNode.setTargetId(eNid);
						}
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
				}
			}
			return true;
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	/**
	 * Returns all hierachical children for an opc ua node
	 * 
	 * @param nodeId
	 * @return
	 */
	public Node[] findChildren(NodeId nodeId) {
		// ua node
		Node parentNode = getNodeById(nodeId);
		if (parentNode == null) {
			return new Node[0];
		}
		// init its children
		List<Node> children = new ArrayList<Node>();
		for (ReferenceNode refNode : parentNode.getReferences()) {
			// checks the hierachical reference
			boolean isHierachical = getTypeTable().isTypeOf(refNode.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			// check that the direction is forward
			if (isHierachical && !refNode.getIsInverse()) {
				children.add(getNodeById(refNode.getTargetId()));
			}
		}
		return children.toArray(new Node[0]);
	}

	public ExpandedNodeId findParentInTypeHierachie(NodeId child) throws ServiceResultException {
		// child node
		Node childNode = getNodeById(child);
		if (childNode == null) {
			return ExpandedNodeId.NULL;
		}
		// no parent for UATypeNodes
		NodeClass childNodeClass = childNode.getNodeClass();
		switch (childNodeClass) {
		case ObjectType:
		case VariableType:
		case DataType:
		case ReferenceType:
			return ExpandedNodeId.NULL;
		}
		if (childNode.getReferences() == null) {
			return ExpandedNodeId.NULL;
		}
		// Name of childnode to look for
		String childName = childNode.getBrowseName().getName();
		for (ReferenceNode reference : childNode.getReferences()) {
			if (!reference.getIsInverse()) {
				continue;
			}
			// look for hierachical refernce
			NodeId refTypeId = reference.getReferenceTypeId();
			boolean isHierachical = getTypeTable().isTypeOf(refTypeId, Identifiers.HierarchicalReferences);
			if (!isHierachical) {
				continue;
			}
			// find parent
			NodeId parentId = null;
			try {
				parentId = getNamespaceUris().toNodeId(reference.getTargetId());
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				continue;
			}
			Node parentNode = getNodeById(parentId);
			if (parentNode == null) {
				continue;
			}
			// UATypeNode is a parent
			NodeClass parentNodeClass = parentNode.getNodeClass();
			switch (parentNodeClass) {
			case ObjectType:
			case VariableType:
			case DataType:
			case ReferenceType:
			case Method:
				return reference.getTargetId();
			}
			// look for parent type definition
			if (parentNode.getReferences() == null) {
				continue;
			}
			NodeId typeDefId = null;
			for (ReferenceNode parentReference : parentNode.getReferences()) {
				boolean isTypeDef = getTypeTable().isTypeOf(parentReference.getReferenceTypeId(),
						Identifiers.HasTypeDefinition);
				if (isTypeDef) {
					typeDefId = getNamespaceUris().toNodeId(parentReference.getTargetId());
					break;
				}
			}
			if (typeDefId == null) {
				continue;
			}
			Node typeDef = getNodeById(typeDefId);
			if (typeDef == null) {
				continue;
			}
			// check for Optional or MandatoryPlaceholder
			boolean isFolderType = getTypeTable().isTypeOf(typeDefId, Identifiers.FolderType);
			if (isFolderType) {
				for (ReferenceNode modellingRule : childNode.getReferences()) {
					if (!getTypeTable().isTypeOf(modellingRule.getReferenceTypeId(), Identifiers.HasModellingRule)) {
						continue;
					}
					NodeId modellingRuleId = getNamespaceUris().toNodeId(modellingRule.getTargetId());
					if (getTypeTable().isTypeOf(modellingRuleId, Identifiers.ModellingRule_MandatoryPlaceholder)
							|| getTypeTable().isTypeOf(modellingRuleId,
									Identifiers.ModellingRule_OptionalPlaceholder)) {
						return reference.getTargetId();
					}
				}
			}
			// check for datatypedictionarytype
			boolean isDataTypeDictionaryType = getTypeTable().isTypeOf(typeDefId, Identifiers.DataTypeDictionaryType);
			if (isDataTypeDictionaryType) {
				return reference.getTargetId();
			}
			NodeId superTypeId = NodeId.NULL;
			switch (typeDef.getNodeClass()) {
			case ObjectType:
				superTypeId = Identifiers.BaseObjectType;
				break;
			case VariableType:
				superTypeId = Identifiers.BaseVariableType;
				break;
			case DataType:
				superTypeId = Identifiers.BaseDataType;
				break;
			}
			// match supertypes
			while (!NodeId.equals(typeDefId, superTypeId)) {
				if (typeDef.getReferences() == null) {
					continue;
				}
				for (ReferenceNode typeReference : typeDef.getReferences()) {
					if (typeReference.getIsInverse()) {
						continue;
					}
					if (!getTypeTable().isTypeOf(typeReference.getReferenceTypeId(),
							Identifiers.HierarchicalReferences)) {
						continue;
					}
					Node typeChild = getNodeById(typeReference.getTargetId());
					if (typeChild == null) {
						continue;
					}
					String typeName = typeChild.getBrowseName().getName();
					if (childName.equals(typeName)) {
						return reference.getTargetId();
					}
				}
				// find supertype reference
				for (ReferenceNode typeReference : typeDef.getReferences()) {
					if (!typeReference.getIsInverse()) {
						continue;
					}
					if (!getTypeTable().isTypeOf(typeReference.getReferenceTypeId(), Identifiers.HasSubtype)) {
						continue;
					}
					typeDefId = getNamespaceUris().toNodeId(typeReference.getTargetId());
					typeDef = getNodeById(typeDefId);
				}
			}
		}
		return ExpandedNodeId.NULL;
	}

	/**
	 * parent element should not be RootFolder
	 * 
	 * @param child
	 * @param parent
	 * @return
	 */
	public boolean isParentInHierarchie(NodeId child, NodeId parent) {
		Node childNode = getNodeById(child);
		if (childNode == null) {
			return false;
		}
		ExpandedNodeId parentNodeId = findTarget(childNode);
		if (parentNodeId == null) {
			return false;
		}
		Node parentNode = getNodeById(parentNodeId);
		if (parentNode == null) {
			return false;
		}
		while (!parentNode.getNodeId().equals(Identifiers.RootFolder)) {
			if (parentNode.getNodeId().equals(parent)) {
				return true;
			}
			parentNodeId = findTarget(parentNode); // parentNode.findTarget(Identifiers.HasComponent,
													// true);
			if (parentNodeId == null) {
				return false;
			}
			parentNode = getNodeById(parentNodeId);
			if (parentNode == null) {
				return false;
			}
		}
		return false;
	}

	private ExpandedNodeId findTarget(Node node) {
		ExpandedNodeId parentNodeId = node.findTarget(Identifiers.HasComponent, true);
		if (parentNodeId == null) {
			parentNodeId = node.findTarget(Identifiers.HasProperty, true);
		}
		return parentNodeId;
	}

	public Node[] getAllNodes() {
		this.lock.readLock().lock();
		try {
			List<Node> nodes = new ArrayList<>();
			Collection<OPCAddressSpace> addressSpaces = this.addressSpace.values();
			for (OPCAddressSpace addressSpace : addressSpaces) {
				// there are no opc ua nodes in an addressspace for a given
				// namespace index
				if (addressSpace == null) {
					// skip;
					continue;
				}
				addressSpace.fillNodes(nodes);
			}
			return nodes.toArray(new Node[0]);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	public Node[] getAllNodes(int namespaceIndex) {
		this.lock.readLock().lock();
		try {
			OPCAddressSpace addrSpace = this.addressSpace.get(namespaceIndex);
			if (addrSpace == null) {
				return new Node[0];
			}
			return addrSpace.getNodes();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	public Node getNodeById(ExpandedNodeId expNodeId) {
		try {
			NodeId nodeId = getNamespaceUris().toNodeId(expNodeId);
			return getNodeById(nodeId);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * Ignores visible state of the opc ua node. Used to set visibility of nodes
	 * 
	 * @param nodeId
	 * @return
	 */
	public Node getIgnoreVisibilityNodeById(NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			return null;
		}
		this.lock.readLock().lock();
		try {
			OPCAddressSpace space = this.addressSpace.get(nodeId.getNamespaceIndex());
			if (space == null) {
				return null;
			}
			return space.getNodeById(nodeId);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	public Node getNodeById(NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			return null;
		}
		this.lock.readLock().lock();
		try {
			OPCAddressSpace space = this.addressSpace.get(nodeId.getNamespaceIndex());
			if (space == null) {
				return null;
			}
			Node node = space.getNodeById(nodeId);
			if (node == null || !node.isVisible()) {
				return null;
			}
			return node;
		} finally {
			this.lock.readLock().unlock();
		}
	}

	protected AddNodesResult[] addNodes(AddNodesItem[] nodesToAdd, Map<ExpandedNodeId, AddNodesItem> mappedNodes,
			boolean includeStructure, OPCServerSession session, boolean includeParentComponents) {
		this.lock.writeLock().lock();
		// caching
		Map<Node, Node> tmpCache = new HashMap<>();
		Map<NodeId, Node> tmpMapping = new HashMap<>();
		Entry<Node, Node> entry = null;
		try {
			// results
			List<AddNodesResult> results = new ArrayList<AddNodesResult>();
			// Fetch the hierachical parent for the current AddNodesItem and
			// store them in a TemporaryAddressSpace
			for (AddNodesItem nodeToAdd : nodesToAdd) {
				// decodes a node from a AddNodesItem
				Node node = AddressSpaceUtil.decodeNode(getNamespaceUris(), nodeToAdd, this.nodeFactory);
				Node parentNode = null;
				// Fetch the parent from the address space or the AddNodesItem
				// arguments. ServerBaseNodes do not need a parent
				if (nodeToAdd.getParentNodeId() != null) {
					try {
						parentNode = addGetNode(getNamespaceUris().toNodeId(nodeToAdd.getParentNodeId()), tmpMapping);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
					// check if parent exists in the temporary address space
					if (parentNode == null) {
						parentNode = addGetParent(nodeToAdd, mappedNodes);
					}
				}
				// fill cache
				tmpCache.put(node, parentNode);
				tmpMapping.put(node.getNodeId(), node);
				/**
				 * !
				 */
				// add temporary types
				getTypeTable().addTemporaryType(node, parentNode);
			}
			boolean isFinished = false;
			boolean isAdded = true;
			// Remove the UA Nodes from the TemporaryAddressSpace and add them
			// to the AddressSpace
			while (!isFinished && isAdded) {
				isAdded = false;
				Set<Entry<Node, Node>> entrySet = new HashSet<Map.Entry<Node, Node>>(tmpCache.entrySet());
				for (Entry<Node, Node> ent : entrySet) {
					entry = ent;
					// parent is available for the node to add
					if (entry.getValue() != null) {
						// insert node to the address space
						boolean isInsertable = addInsert(entry, results, session);
						if (isInsertable) {
							// node has been inserted
							tmpCache.remove(entry.getKey());
							tmpMapping.remove(entry.getKey().getNodeId());
							/**
							 * !
							 */
							// add type to server type table
							getTypeTable().add(entry.getKey(), entry.getValue());
							isAdded = true;
							if (includeStructure) {
								addCreateChildStructure(entry.getKey(), tmpMapping, includeStructure, session,
										includeParentComponents);
							}
						} else
							isFinished = false;
					} else {
						// UA Definded Nodes are allowed to inserted without a
						// parent
						// if (AddressSpaceUtil.isServerBaseNode(entry.getKey())) {
						boolean isInsertAble = addInsert(entry, results, session);
						if (isInsertAble) {
							tmpCache.remove(entry.getKey());
							tmpMapping.remove(entry.getKey().getNodeId());
							/**
							 * !
							 */
							// add type to server type table
							getTypeTable().add(entry.getKey(), entry.getValue());
							isAdded = true;
							if (includeStructure) {
								try {
									addCreateChildStructure(entry.getKey(), tmpMapping, includeStructure, session,
											includeParentComponents);
								} catch (NullPointerException ex) {
									Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
								}
							}
						} else {
							isFinished = false;
						}
						// }
						// cannot insert node with no parent
						// else {
						// tmpCache.remove(entry.getKey());
						// tmpMapping.remove(entry.getKey().getNodeId());
						// results.add(new AddNodesResult(new
						// StatusCode(StatusCodes.Bad_ParentNodeIdInvalid),
						// entry.getKey().getNodeId()));
						// }
					}
				}
				if (tmpCache.isEmpty()) {
					isFinished = true;
				}
			}
			// removes temporary types
			getTypeTable().removeTemporaryTypes();
			return results.toArray(new AddNodesResult[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected AddNodesResult[] addNodes(AddNodesItem[] nodesToAdd, Map<ExpandedNodeId, AddNodesItem> mappedNodes,
			boolean includeStructure, OPCServerSession session, boolean includeParentComponents,
			boolean createOptionalPlaceholder, boolean addModellingRule) {
		this.lock.writeLock().lock();
		// caching
		Map<Node, Node> tmpCache = new HashMap<>();
		Map<NodeId, Node> tmpMapping = new HashMap<>();
		Entry<Node, Node> entry = null;
		try {
			// results
			List<AddNodesResult> results = new ArrayList<AddNodesResult>();
			// Fetch the hierachical parent for the current AddNodesItem and
			// store them in a TemporaryAddressSpace
			for (AddNodesItem nodeToAdd : nodesToAdd) {
				// decodes a node from a AddNodesItem
				Node node = AddressSpaceUtil.decodeNode(getNamespaceUris(), nodeToAdd, this.nodeFactory);
				Node parentNode = null;
				// Fetch the parent from the address space or the AddNodesItem
				// arguments. ServerBaseNodes do not need a parent
				if (nodeToAdd.getParentNodeId() != null) {
					try {
						parentNode = addGetNode(getNamespaceUris().toNodeId(nodeToAdd.getParentNodeId()), tmpMapping);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
					// check if parent exists in the temporary address space
					if (parentNode == null) {
						parentNode = addGetParent(nodeToAdd, mappedNodes);
					}
				}
				// fill cache
				tmpCache.put(node, parentNode);
				tmpMapping.put(node.getNodeId(), node);
				/**
				 * !
				 */
				// add temporary types
				getTypeTable().addTemporaryType(node, parentNode);
			}
			boolean isFinished = false;
			boolean isAdded = true;
			// Remove the UA Nodes from the TemporaryAddressSpace and add them
			// to the AddressSpace
			while (!isFinished && isAdded) {
				isAdded = false;
				Set<Entry<Node, Node>> entrySet = new HashSet<Map.Entry<Node, Node>>(tmpCache.entrySet());
				for (Entry<Node, Node> ent : entrySet) {
					entry = ent;
					// parent is available for the node to add
					if (entry.getValue() != null) {
						// insert node to the address space
						boolean isInsertable = addInsert(entry, results, session);
						if (isInsertable) {
							// node has been inserted
							tmpCache.remove(entry.getKey());
							tmpMapping.remove(entry.getKey().getNodeId());
							/**
							 * !
							 */
							// add type to server type table
							getTypeTable().add(entry.getKey(), entry.getValue());
							isAdded = true;
							if (includeStructure) {
								addCreateChildStructure(entry.getKey(), tmpMapping, includeStructure, session,
										includeParentComponents, createOptionalPlaceholder, addModellingRule);
							}
						} else
							isFinished = false;
					} else {
						// UA Definded Nodes are allowed to inserted without a
						// parent
						if (AddressSpaceUtil.isServerBaseNode(entry.getKey())) {
							boolean isInsertAble = addInsert(entry, results, session);
							if (isInsertAble) {
								tmpCache.remove(entry.getKey());
								tmpMapping.remove(entry.getKey().getNodeId());
								/**
								 * !
								 */
								// add type to server type table
								getTypeTable().add(entry.getKey(), entry.getValue());
								isAdded = true;
								if (!includeStructure) {
									addCreateChildStructure(entry.getKey(), tmpMapping, includeStructure, session,
											includeParentComponents, createOptionalPlaceholder, addModellingRule);
								}
							} else
								isFinished = false;
						}
						// cannot insert node with no parent
						else {
							tmpCache.remove(entry.getKey());
							tmpMapping.remove(entry.getKey().getNodeId());
							try {
								results.add(new AddNodesResult(new StatusCode(StatusCodes.Bad_ParentNodeIdInvalid),
										entry.getKey().getNodeId()));
							} catch (NullPointerException ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
								return new AddNodesResult[0];
							}
						}
					}
				}
				if (tmpCache.isEmpty()) {
					isFinished = true;
				}
			}
			// removes temporary types
			getTypeTable().removeTemporaryTypes();
			return results.toArray(new AddNodesResult[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected StatusCode[] addReference(AddReferencesItem[] referencesToAdd, OPCServerSession session) {
		this.lock.writeLock().lock();
		try {
			List<StatusCode> results = new ArrayList<>();
			for (AddReferencesItem referenceToAdd : referencesToAdd) {
				// Get the ReferenceNode of the Reference to add
				ReferenceNode referenceNode = this.nodeFactory.createReferenceNode(referenceToAdd.getReferenceTypeId(),
						!referenceToAdd.getIsForward(), referenceToAdd.getTargetNodeId());
				// Get the SourceNode of the Reference to add
				Node sourceNode = getNodeById(referenceToAdd.getSourceNodeId());
				// check reference
				StatusCode result = addValidateAddReference(sourceNode, referenceNode);
				if (result != null && result.isBad()) {
					results.add(result);
					continue;
				}
				// check user authority
				result = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.AddReference, null, sourceNode)
						.getCode();
				if (result.isBad()) {
					results.add(result);
					continue;
				}
				/**
				 * check if the type of the reference is not symmetric to add bot reference from
				 * source -> target & target -> source
				 */
				ReferenceTypeNode referenceTypeNode = (ReferenceTypeNode) getNodeById(
						referenceNode.getReferenceTypeId());
				NodeId referenceType = referenceNode.getReferenceTypeId();
				boolean isHierachical = getTypeTable().isTypeOf(referenceType, Identifiers.HierarchicalReferences);
				/** add a hierachical inverse reference */
				if ((isHierachical || (referenceTypeNode != null && !referenceTypeNode.getSymmetric()))
						&& sourceNode != null) {
					Node targetNode = getNodeById(referenceNode.getTargetId());
					ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(sourceNode.getNodeId());
					ReferenceNode inverseReference = new ReferenceNode(referenceNode.getReferenceTypeId(),
							!referenceNode.getIsInverse(), eNid);
					// ReferenceNode inverseReference = new
					// ReferenceNode(referenceNode.getReferenceTypeId(),
					// !referenceNode.getIsInverse(),
					result = addValidateAddReference(targetNode, inverseReference);
					if (result != null && result.isBad()) {
						results.add(result);
						continue;
					}
					createNewReferenceOnNode(targetNode, inverseReference);
				}
				/** add reference */
				StatusCode status = createNewReferenceOnNode(sourceNode, referenceNode);
				results.add(status);
			}
			return results.toArray(new StatusCode[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected BrowseResult[] browse(BrowseDescription[] nodesToBrowse, UnsignedInteger maxReferencesPerNode,
			ViewDescription view, OPCServerSession session) throws ServiceResultException {
		if (nodesToBrowse == null || nodesToBrowse.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		this.lock.readLock().lock();
		try {
			List<BrowseResult> results = new ArrayList<>();
			/** server address space */
			TypeTable typeTree = getServer().getTypeTable();
			for (int i = 0; i < nodesToBrowse.length; i++) {
				BrowseDescription description = nodesToBrowse[i];
				Node node = getNodeById(description.getNodeId());
				BrowseResult result = new BrowseResult();
				results.add(result);
				// valid node
				if (node == null) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
					continue;
				}
				// check user authority for the node to browse itself
				ServiceResult code = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.Browse, session,
						node);
				if (code.isBad()) {
					// skip
					continue;
				}
				// / valid refererence id
				NodeId referenceId = description.getReferenceTypeId();
				if (NodeId.isNull(referenceId)) {
					// continue;
					referenceId = Identifiers.References;
				} else if (!typeTree.isKnown(referenceId)) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_ReferenceTypeIdInvalid));
					continue;
				}
				// valid browse direction
				BrowseDirection direction = description.getBrowseDirection();
				if (direction == null) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_BrowseDirectionInvalid));
					continue;
				} else if (direction.getValue() < BrowseDirection.Forward.getValue()
						|| direction.getValue() > BrowseDirection.Both.getValue()) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_BrowseDirectionInvalid));
					continue;
				}
				BrowseContinuationPoint cp = new BrowseContinuationPoint();
				cp.setViewDescription(view);
				cp.setNodeToBrowse(node);
				cp.setMaxResultsToReturn(maxReferencesPerNode);
				cp.setBrowseDirection(description.getBrowseDirection());
				cp.setReferenceTypeId(description.getReferenceTypeId());
				cp.setIncludeSubtypes(description.getIncludeSubtypes());
				cp.setNodeClassMask(description.getNodeClassMask());
				cp.setResultMask(BrowseResultMask.getSet(description.getResultMask()));
				cp.setIndex(0);
				cp.setData(null);
				cp.setSession(session);
				cp.setServerTypeTable(getServer().getTypeTable());
				// fetches references from address space
				List<ReferenceDescription> references = new ArrayList<>();
				StatusCode error = cp.fetchReferences(this, references);
				getServer().logService(RequestType.Browse,
						error + " " + node.getNodeId() + " found " + references.size() + " browsed nodes");
				if (error.isBad()) {
					result.setStatusCode(error);
					continue;
				}
				if (!cp.isReleased()) {
					cp.setReferenceTypeId(Identifiers.References);
					cp.setIncludeSubtypes(true);
					result.setContinuationPoint(ByteString.valueOf(UUIDUtil.UUIdAsByteArray(cp.getId())));
				}
				result.setStatusCode(error);
				result.setReferences(references.toArray(new ReferenceDescription[0]));
			}
			/** sorts the result */
			return results.toArray(new BrowseResult[0]);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	protected BrowseResult[] browseNext(ByteString[] continuationPoints, Boolean releaseContinuationPoints,
			OPCServerSession session) throws ServiceResultException {
		if (continuationPoints == null || continuationPoints.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		List<BrowseResult> results = new ArrayList<>();
		this.lock.readLock().lock();
		try {
			for (int i = 0; i < continuationPoints.length; i++) {
				// restore/remove
				BrowseContinuationPoint cp = session.restoreContinuationPoint(continuationPoints[i]);
				// initialize result.
				BrowseResult result = new BrowseResult();
				results.add(result);
				// check if continuation point has expired.
				if (cp == null) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_ContinuationPointInvalid));
					continue;
				}
				Node node = cp.getNodeToBrowse();
				// check user authority
				StatusCode error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.Browse, session, node)
						.getCode();
				if (error.isBad()) {
					result.setStatusCode(error);
					continue;
				}
				// check if simply releasing the continuation point.
				if (releaseContinuationPoints) {
					if (cp != null) {
						cp.dispose();
						result.setStatusCode(StatusCode.GOOD);
					}
					continue;
				}
				error = browseNext(session, cp, result);
				// check for error
				result.setStatusCode(error);
				// TODO: diagnostics
				// check for continuation points
				if (!cp.isReleased()) {
					result.setStatusCode(StatusCode.GOOD);
					result.setContinuationPoint(ByteString.valueOf(UUIDUtil.UUIdAsByteArray(cp.getId())));
					continue;
				}
			}
		} finally {
			this.lock.readLock().unlock();
		}
		return results.toArray(new BrowseResult[0]);
	}

	protected CallMethodResult[] call(CallMethodRequest[] methodsToCall, OPCServerSession session)
			throws ServiceResultException {
		if (methodsToCall == null || methodsToCall.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		this.lock.writeLock().lock();
		try {
			List<CallMethodResult> results = new ArrayList<>();
			for (CallMethodRequest methodToCall : methodsToCall) {
				NodeId objectId = methodToCall.getObjectId();
				Node node = getNodeById(objectId);
				ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.Call, session,
						node);
				if (error != null && error.isBad()) {
					CallMethodResult result = new CallMethodResult();
					result.setStatusCode(error.getCode());
					results.add(result);
					continue;
				}
				IOPCOperation operation = new OPCServerOperation(session);
				CallMethodResult result = ComDRVManager.getDRVManager().callMethod(operation,
						methodToCall.getObjectId(), methodToCall.getMethodId(), methodToCall.getInputArguments());
				results.add(result);
				getServer().logService(RequestType.Call, node.getNodeId() + " method has been called");
			}
			return results.toArray(new CallMethodResult[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected StatusCode[] deleteReferences(DeleteReferencesItem[] referencesToDelete, OPCServerSession session) {
		this.lock.writeLock().lock();
		try {
			List<StatusCode> results = new ArrayList<>();
			for (int i = 0; i < referencesToDelete.length; i++) {
				Node sourceNode = getNodeById(referencesToDelete[i].getSourceNodeId());
				ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.DeleteReference,
						session, sourceNode);
				if (error != null && error.isBad()) {
					results.add(error.getCode());
					continue;
				}
				Node targetNode = getNodeById(referencesToDelete[i].getTargetNodeId());
				StatusCode result = deleteReference(sourceNode, targetNode, referencesToDelete[i].getReferenceTypeId(),
						referencesToDelete[i].getIsForward(), referencesToDelete[i].getDeleteBidirectional());
				results.add(result);
			}
			return results.toArray(new StatusCode[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected HistoryReadResult[] historyRead(HistoryReadValueId[] nodesToRead, ExtensionObject historyReadDetails,
			Boolean releaseContinuationPoints, TimestampsToReturn timestampsToReturn, Long[] driverStates,
			OPCServerSession session) {
		this.lock.readLock().lock();
		try {
			List<HistoryReadResult> results = new ArrayList<>();
			HistoryReadDetails details = null;
			try {
				if (historyReadDetails == null) {
					// TODO do something default
				} else {
					details = historyReadDetails.decode(EncoderContext.getDefaultInstance());
				}
			} catch (DecodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			for (HistoryReadValueId nodeToRead : nodesToRead) {
				HistoryReadResult result = null;
				NodeId nodeId = nodeToRead.getNodeId();
				Node node = getNodeById(nodeId);
				ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.HistoryRead,
						session, node);
				if (error != null && error.isBad()) {
					result = new HistoryReadResult();
					result.setStatusCode(error.getCode());
					results.add(result);
					continue;
				}
				if (result == null) {
					result = new HistoryReadResult();
					HistoryManager manager = null;
					if (node.getNodeClass() == NodeClass.Variable)
						manager = ((UAServerVariableNode) node).getHistoryManager();
					else if (node.getNodeClass() == NodeClass.Object)
						manager = ((UAServerObjectNode) node).getHistoryManager();
					if (manager == null) {
						result.setStatusCode(new StatusCode(StatusCodes.Bad_NodeClassInvalid));
						results.add(result);
						continue;
					}
					error = manager.historyRead(session, details, nodeToRead, releaseContinuationPoints,
							timestampsToReturn, node, result, driverStates);
					// set result code
					result.setStatusCode(error.getCode());
				}
				results.add(result);
			}
			return results.toArray(new HistoryReadResult[0]);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	protected HistoryUpdateResult[] historyUpdate(ExtensionObject[] historyUpdateDetails, OPCServerSession session) {
		this.lock.writeLock().lock();
		try {
			List<HistoryUpdateResult> results = new ArrayList<>();
			for (ExtensionObject nodeToUpdate : historyUpdateDetails) {
				HistoryUpdateResult result = new HistoryUpdateResult();
				HistoryUpdateDetails detail = null;
				try {
					detail = nodeToUpdate.decode(EncoderContext.getDefaultInstance());
					NodeId nodeId = detail.getNodeId();
					Node node = getNodeById(nodeId);
					ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.HistoryUpdate,
							session, node);
					if (error != null && error.isBad()) {
						result.setStatusCode(error.getCode());
						results.add(result);
						continue;
					}
					if (node.getNodeClass() != NodeClass.Variable) {
						result.setStatusCode(new StatusCode(StatusCodes.Bad_NodeClassInvalid));
						results.add(result);
						continue;
					}
					HistoryManager manager = ((UAServerVariableNode) node).getHistoryManager();
					error = manager.historyUpdate(detail, node, result);
					// set result code
					result.setStatusCode(error.getCode());
					results.add(result);
				} catch (DecodingException e) {
					// cannot decode
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			return results.toArray(new HistoryUpdateResult[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected DataValue[] read(ReadValueId[] nodesToRead, Double maxAge, TimestampsToReturn timestampsToReturn,
			Long[] driverStates, OPCServerSession session) throws ServiceResultException {
		/** Nothing to do */
		if (nodesToRead == null || nodesToRead.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		/** invalid maxage */
		if (maxAge == null || maxAge < 0) {
			throw new ServiceResultException(StatusCodes.Bad_MaxAgeInvalid);
		}
		/** invalid timestamps */
		if (timestampsToReturn == null || timestampsToReturn.getValue() < TimestampsToReturn.Source.getValue()
				|| timestampsToReturn.getValue() > TimestampsToReturn.Neither.getValue()) {
			throw new ServiceResultException(StatusCodes.Bad_TimestampsToReturnInvalid);
		}
		this.lock.readLock().lock();
		try {
			List<DataValue> results = new ArrayList<>();
			for (int i = 0; i < nodesToRead.length; i++) {
				// driver states to read values from underlying system
				long driverState = -1;
				if (driverStates != null && driverStates.length > i && driverStates[i] != null) {
					driverState = driverStates[i];
				}
				// reads a value with statuscode and timestamps
				DataValue value = read(nodesToRead[i], maxAge, DateTime.currentTime(), driverState, session);
				// apply timestamps
				if (TimestampsToReturn.Server != timestampsToReturn && TimestampsToReturn.Both != timestampsToReturn) {
					value.setServerTimestamp(DateTime.MIN_VALUE);
					value.setServerPicoseconds(UnsignedShort.MIN_VALUE);
				}
				if (TimestampsToReturn.Source != timestampsToReturn && TimestampsToReturn.Both != timestampsToReturn) {
					value.setSourceTimestamp(DateTime.MIN_VALUE);
					value.setSourcePicoseconds(UnsignedShort.MIN_VALUE);
				}
				results.add(value);
				getServer().logService(RequestType.Read,
						"Read node " + nodesToRead[i].getNodeId() + " attribute " + nodesToRead[i].getAttributeId()
								+ " value "
								+ (!(results.get(i) != null && !results.get(i).isNull()) ? results.get(i).getValue()
										: "[null]"));
			}
			return results.toArray(new DataValue[0]);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	protected NodeId[] registerNodes(NodeId[] nodesToRegister, OPCServerSession session) throws ServiceResultException {
		if (nodesToRegister == null || nodesToRegister.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		this.lock.writeLock().lock();
		try {
			List<NodeId> results = new ArrayList<>();
			for (NodeId nodeToRegister : nodesToRegister) {
				Node node = getNodeById(nodeToRegister);
				ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.RegisterNode,
						session, node);
				if (error != null && error.isBad()) {
					results.add(NodeId.NULL);
					continue;
				}
				results.add(nodeToRegister);
			}
			return results.toArray(new NodeId[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected BrowsePathResult[] tranlsateBrowsePathsToNodeIds(BrowsePath[] browsePaths, OPCServerSession session)
			throws ServiceResultException {
		if (browsePaths == null || browsePaths.length <= 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		this.lock.readLock().lock();
		try {
			BrowsePathResult[] results = new BrowsePathResult[browsePaths.length];
			for (int i = 0; i < browsePaths.length; i++) {
				BrowsePath browsePath = browsePaths[i];
				BrowsePathResult result = translateBrowsePathsToNodeId(browsePath, session);
				results[i] = result;
				StatusCode error = result.getStatusCode();
				if (error.isGood()) {
					// check for no match
					if (result.getTargets().length == 0) {
						result.setStatusCode(new StatusCode(StatusCodes.Bad_NoMatch));
					}
				}
				getServer().logService(RequestType.TranslateBrowsePathsToNodeIds,
						"Browsepath " + result.getStatusCode() + " " + Arrays.toString(result.getTargets()));
			}
			return results;
		} finally {
			this.lock.readLock().unlock();
		}
	}

	protected void unregisterNodes(NodeId[] nodesToUnregister, OPCServerSession session) throws ServiceResultException {
		if (nodesToUnregister == null || nodesToUnregister.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		this.lock.writeLock().lock();
		try {
			for (NodeId nodeToRegister : nodesToUnregister) {
				Node node = getNodeById(nodeToRegister);
				ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.RegisterNode,
						session, node);
				if (error != null && error.isBad()) {
					continue;
				}
				// TODO: Unregister
			}
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected StatusCode[] write(WriteValue[] nodesToWrite, boolean skipAccessLevel, Long[] tagstate,
			OPCServerSession session) throws ServiceResultException {
		if (nodesToWrite == null) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		this.lock.writeLock().lock();
		try {
			List<StatusCode> results = new ArrayList<>();
			for (int i = 0; i < nodesToWrite.length; i++) {
				/** service */
				// define no driver state
				long state = -1;
				// otherwise use driverstate
				if (tagstate != null) {
					state = tagstate[i];
				}
				StatusCode error = write(nodesToWrite[i], skipAccessLevel, state, session);
				results.add(error);
				getServer().logService(RequestType.Write,
						"Write node " + nodesToWrite[i].getNodeId() + " attribute " + nodesToWrite[i].getAttributeId()
								+ " value "
								+ ((nodesToWrite[i].getValue() != null && !nodesToWrite[i].getValue().isNull())
										? nodesToWrite[i].getValue().getValue()
										: "[null]"));
			}
			// updates monitored items related with this node
			return results.toArray(new StatusCode[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	private StatusCode addNode(Node nodeToAdd, Node parentNode, OPCServerSession session) {
		// parent node authority
		try {
			ServiceResult result = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.AddNode, session,
					parentNode);
			if (result.isBad()) {
				return result.getCode();
			}
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		/**
		 * Do not replace existing nodes. It do nothing if the nodeid of the nodeToAdd
		 * exists in the Address Space
		 */
		Node node = getNodeById(nodeToAdd.getNodeId());
		if (node != null) {
			return new StatusCode(StatusCodes.Bad_NothingToDo);
		}
		/** Get the parent of the node to add */
		Node addr_ParentNode = null;
		if (!(AddressSpaceUtil.isServerBaseNode(nodeToAdd) && parentNode == null)) {
			if (parentNode != null) {
				addr_ParentNode = getNodeById(parentNode.getNodeId());
			}
		}
		/**
		 * Node has parent and node is not a OPC Defined Default Node (NamespaceIndex of
		 * http://opcfoundation.org/UA/). Create a Reference between the new added node
		 * and the existing parent node and add it to the address space collection
		 */
		if (addr_ParentNode != null) {
			StatusCode addNodeStatus = createNewReferencesOnAddedNode(nodeToAdd, addr_ParentNode, getTypeTable(),
					getNamespaceUris());
			if (StatusCode.GOOD.equals(addNodeStatus)) {
				addNodeToAddressSpace(nodeToAdd.getNodeId(), nodeToAdd);
			}
			return addNodeStatus;
		}
		/**
		 * Node is a UA Defined Node with no parent (e.g.: Root)
		 */
		else if (parentNode == null && addr_ParentNode == null/**
																 * && AddressSpaceUtil.isServerBaseNode(nodeToAdd)
																 */
		) {
			StatusCode addNodeStatus = createNewReferencesOnAddedNode(nodeToAdd, parentNode, getTypeTable(),
					getNamespaceUris());
			if (StatusCode.GOOD.equals(addNodeStatus)) {
				addNodeToAddressSpace(nodeToAdd.getNodeId(), nodeToAdd);
			}
			return addNodeStatus;
		}
		/**
		 * No reason to add the node
		 */
		else {
			return null;
		}
	}

	private void addNodeToAddressSpaceOnly(NodeId nodeId, Node node) {
		int namespaceIndex = nodeId.getNamespaceIndex();
		OPCAddressSpace nsSpace = this.addressSpace.get(namespaceIndex);
		if (nsSpace == null) {
			nsSpace = new OPCAddressSpace();
			this.addressSpace.put(namespaceIndex, nsSpace);
		}
		// add node to address space
		nsSpace.addNode(nodeId, node);
	}

	private void addNodeToAddressSpace(NodeId nodeId, Node node) {
		addNodeToAddressSpaceOnly(nodeId, node);
		this.nodeFactory.asLastIfPossibleId(nodeId);
		// add an inverse hierachical reference if there is no available
		ReferenceNode[] references = node.getReferences();
		if (references != null) {
			for (ReferenceNode refNodes : references) {
				NodeId refTypeId = refNodes.getReferenceTypeId();
				// find first hierachcal address
				if (getServer().getTypeTable().isTypeOf(refTypeId, Identifiers.HierarchicalReferences)) {
					ExpandedNodeId target = refNodes.getTargetId();
					Node hierachicalTarget = getServer().getAddressSpaceManager().getNodeById(target);
					if (hierachicalTarget != null) {
						ExpandedNodeId newTargetId = this.getServer().getNamespaceUris().toExpandedNodeId(nodeId);
						ReferenceNode maybeNewTarget = new ReferenceNode(refNodes.getReferenceTypeId(),
								!refNodes.getIsInverse(), newTargetId);
						hierachicalTarget.ensureReferenceExists(maybeNewTarget);
						break;
					}
				}
			}
		}
		getServer().logService(RequestType.AddNodes,
				nodeId + " " + node.getBrowseName() + " has been added to the addressspace!");
	}

	/**
	 * Creates the child node. First creates the node with an addnode service,
	 * second adds the references from the child node to compare with the model.
	 * 
	 * @param child     Copy of the child node to add. This method creates the new
	 *                  node via the add nodes service.
	 * @param parent    Real node parent.
	 * @param arrayList
	 * 
	 */
	private void addCreateChild(Node child, Node parent, boolean includeStructure, OPCServerSession session,
			boolean includeParentComponents) {
		ExpandedNodeId type = child.findTarget(Identifiers.HasTypeDefinition, false);
		// uses the first hierachical reference
		NodeId referenceTypeId = null;
		for (ReferenceNode referenceNode : child.getReferences()) {
			boolean isHierachical = getTypeTable().isTypeOf(referenceNode.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			// is hierachical and is inverse reference to parent
			if (isHierachical && referenceNode.getIsInverse()) {
				referenceTypeId = referenceNode.getReferenceTypeId();
				break;
			}
		}
		// new nodeid
		ExpandedNodeId newExpandedNodeId = null;
		NodeId newNodeId = null;
		// do new nodeid
		if (newExpandedNodeId == null) {
			int index = parent.getNodeId().getNamespaceIndex();
			newNodeId = this.nodeFactory.getNextNodeId(index, null, parent.getNodeId().getIdType(), NodeIdMode.APPEND);
			newExpandedNodeId = this.getServer().getNamespaceUris().toExpandedNodeId(newNodeId);
		}
		// parent nodeid
		ExpandedNodeId parentNodeId = this.getServer().getNamespaceUris().toExpandedNodeId(parent.getNodeId());
		/** Add nodes statement */
		AddNodesItem node2add = new AddNodesItem();
		node2add.setBrowseName(child.getBrowseName());
		node2add.setNodeClass(child.getNodeClass());
		node2add.setParentNodeId(parentNodeId);
		node2add.setTypeDefinition(type);
		node2add.setRequestedNewNodeId(newExpandedNodeId);
		node2add.setReferenceTypeId(referenceTypeId);
		NodeAttributes attributes = null;
		switch (child.getNodeClass()) {
		case Object:
			attributes = new ObjectAttributes();
			((ObjectAttributes) attributes).setDescription(((ObjectNode) child).getDescription());
			((ObjectAttributes) attributes).setDisplayName(((ObjectNode) child).getDisplayName());
			((ObjectAttributes) attributes).setEventNotifier(((ObjectNode) child).getEventNotifier());
			((ObjectAttributes) attributes).setUserWriteMask(((ObjectNode) child).getUserWriteMask());
			((ObjectAttributes) attributes).setWriteMask(((ObjectNode) child).getWriteMask());
			break;
		case Variable:
			attributes = new VariableAttributes();
			((VariableAttributes) attributes).setAccessLevel(((VariableNode) child).getAccessLevel());
			((VariableAttributes) attributes).setArrayDimensions(((VariableNode) child).getArrayDimensions());
			((VariableAttributes) attributes).setDataType(((VariableNode) child).getDataType());
			((VariableAttributes) attributes).setDescription(((VariableNode) child).getDescription());
			((VariableAttributes) attributes).setDisplayName(((VariableNode) child).getDisplayName());
			((VariableAttributes) attributes).setHistorizing(((VariableNode) child).getHistorizing());
			((VariableAttributes) attributes)
					.setMinimumSamplingInterval(((VariableNode) child).getMinimumSamplingInterval());
			((VariableAttributes) attributes).setUserAccessLevel(((VariableNode) child).getUserAccessLevel());
			((VariableAttributes) attributes).setUserWriteMask(((VariableNode) child).getUserWriteMask());
			((VariableAttributes) attributes).setValue(((VariableNode) child).getValue());
			((VariableAttributes) attributes).setValueRank(((VariableNode) child).getValueRank());
			((VariableAttributes) attributes).setWriteMask(((VariableNode) child).getWriteMask());
			break;
		case Method:
			attributes = new MethodAttributes();
			((MethodAttributes) attributes).setDescription(((MethodNode) child).getDescription());
			((MethodAttributes) attributes).setDisplayName(((MethodNode) child).getDisplayName());
			((MethodAttributes) attributes).setExecutable(((MethodNode) child).getExecutable());
			((MethodAttributes) attributes).setUserExecutable(((MethodNode) child).getUserExecutable());
			((MethodAttributes) attributes).setUserWriteMask(((MethodNode) child).getUserWriteMask());
			((MethodAttributes) attributes).setWriteMask(((MethodNode) child).getWriteMask());
			break;
		case View:
			attributes = new ViewAttributes();
			((ViewAttributes) attributes).setContainsNoLoops(((ViewNode) child).getContainsNoLoops());
			((ViewAttributes) attributes).setDescription(((ViewNode) child).getDescription());
			((ViewAttributes) attributes).setDisplayName(((ViewNode) child).getDisplayName());
			((ViewAttributes) attributes).setEventNotifier(((ViewNode) child).getEventNotifier());
			((ViewAttributes) attributes).setUserWriteMask(((ViewNode) child).getUserWriteMask());
			((ViewAttributes) attributes).setWriteMask(((ViewNode) child).getWriteMask());
			break;
		case ObjectType:
			attributes = new ObjectTypeAttributes();
			((ObjectTypeAttributes) attributes).setDescription(((ObjectTypeNode) child).getDescription());
			((ObjectTypeAttributes) attributes).setDisplayName(((ObjectTypeNode) child).getDisplayName());
			((ObjectTypeAttributes) attributes).setIsAbstract(((ObjectTypeNode) child).getIsAbstract());
			((ObjectTypeAttributes) attributes).setUserWriteMask(((ObjectTypeNode) child).getUserWriteMask());
			((ObjectTypeAttributes) attributes).setWriteMask(((ObjectTypeNode) child).getWriteMask());
			break;
		case VariableType:
			attributes = new VariableTypeAttributes();
			((VariableTypeAttributes) attributes).setArrayDimensions(((VariableTypeNode) child).getArrayDimensions());
			((VariableTypeAttributes) attributes).setDataType(((VariableTypeNode) child).getDataType());
			((VariableTypeAttributes) attributes).setDescription(((VariableTypeNode) child).getDescription());
			((VariableTypeAttributes) attributes).setDisplayName(((VariableTypeNode) child).getDisplayName());
			((VariableTypeAttributes) attributes).setIsAbstract(((VariableTypeNode) child).getIsAbstract());
			((VariableTypeAttributes) attributes).setUserWriteMask(((VariableTypeNode) child).getUserWriteMask());
			((VariableTypeAttributes) attributes).setValue(((VariableTypeNode) child).getValue());
			((VariableTypeAttributes) attributes).setValueRank(((VariableTypeNode) child).getValueRank());
			((VariableTypeAttributes) attributes).setWriteMask(((VariableTypeNode) child).getWriteMask());
			break;
		case DataType:
			attributes = new DataTypeAttributes();
			((DataTypeAttributes) attributes).setDescription(((DataTypeNode) child).getDescription());
			((DataTypeAttributes) attributes).setDisplayName(((DataTypeNode) child).getDisplayName());
			((DataTypeAttributes) attributes).setIsAbstract(((DataTypeNode) child).getIsAbstract());
			((DataTypeAttributes) attributes).setUserWriteMask(((DataTypeNode) child).getUserWriteMask());
			((DataTypeAttributes) attributes).setWriteMask(((DataTypeNode) child).getWriteMask());
			break;
		case ReferenceType:
			attributes = new ReferenceTypeAttributes();
			((ReferenceTypeAttributes) attributes).setDescription(((ReferenceTypeNode) child).getDescription());
			((ReferenceTypeAttributes) attributes).setDisplayName(((ReferenceTypeNode) child).getDisplayName());
			((ReferenceTypeAttributes) attributes).setInverseName(((ReferenceTypeNode) child).getInverseName());
			((ReferenceTypeAttributes) attributes).setIsAbstract(((ReferenceTypeNode) child).getIsAbstract());
			((ReferenceTypeAttributes) attributes).setSymmetric(((ReferenceTypeNode) child).getSymmetric());
			((ReferenceTypeAttributes) attributes).setUserWriteMask(((ReferenceTypeNode) child).getUserWriteMask());
			((ReferenceTypeAttributes) attributes).setWriteMask(((ReferenceTypeNode) child).getWriteMask());
			((ReferenceTypeNode) child).getBrowseName();
			break;
		default:
			attributes = new NodeAttributes();
		}
		try {
			node2add.setNodeAttributes(ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance()));
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		Map<ExpandedNodeId, AddNodesItem> addnodesmap = new HashMap<>();
		addnodesmap.put(node2add.getRequestedNewNodeId(), node2add);
		// add the new node
		// AddNodesResult[] result =
		addNodes(new AddNodesItem[] { node2add }, addnodesmap, includeStructure, session, includeParentComponents);
		List<AddReferencesItem> references2add = new ArrayList<>();
		// get all references from the node to copy
		for (int i = 0; i < child.getReferences().length; i++) {
			ReferenceNode referenceNode = child.getReferences()[i];
			AddReferencesItem reference2add = new AddReferencesItem();
			// direction
			reference2add.setIsForward(!referenceNode.getIsInverse());
			// reference type
			reference2add.setReferenceTypeId(referenceNode.getReferenceTypeId());
			// source node
			reference2add.setSourceNodeId(newNodeId);
			// target node
			reference2add.setTargetNodeClass(parent.getNodeClass());
			// skips invalid references that does not belong to the type node
			boolean skip = false;
			// change hierachical parent to new parent
			if (referenceNode.getIsInverse()) {
				reference2add.setTargetNodeId(parentNodeId);
			}
			// Use just the valid references
			else { // get target node
				Node targetNode = getNodeById(referenceNode.getTargetId());
				// get the inverse parent reference
				for (ReferenceNode targetNodeReferences : targetNode.getReferences()) {
					boolean isHierachical = getTypeTable().isTypeOf(targetNodeReferences.getReferenceTypeId(),
							Identifiers.HierarchicalReferences);
					if (isHierachical && targetNodeReferences.getIsInverse()) {
						ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(child.getNodeId());
						if (targetNodeReferences.getTargetId().equals(eNid)) {
							skip = true;
						}
						break;
					}
				}
				reference2add.setTargetNodeId(referenceNode.getTargetId());
			}
			// skips the hierarchical model references that points to its model
			// children
			if (!skip) {
				references2add.add(reference2add);
			}
		}
		if (!references2add.isEmpty()) {
			addReference(references2add.toArray(new AddReferencesItem[0]), session);
		}
	}

	/**
	 * Creates the child node. First creates the node with an addnode service,
	 * second adds the references from the child node to compare with the model.
	 * 
	 * @param child     Copy of the child node to add. This method creates the new
	 *                  node via the add nodes service.
	 * @param parent    Real node parent.
	 * @param arrayList
	 * 
	 */
	private void addCreateChild(Node child, Node parent, boolean includeStructure, OPCServerSession session,
			boolean includeParentComponents, boolean createOptionalPlaceholder, boolean addModellingRule) {
		ExpandedNodeId type = child.findTarget(Identifiers.HasTypeDefinition, false);
		// uses the first hierarchical reference
		NodeId referenceTypeId = null;
		for (ReferenceNode referenceNode : child.getReferences()) {
			boolean isHierachical = getTypeTable().isTypeOf(referenceNode.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			// is hierachical and is inverse reference to parent
			if (isHierachical && referenceNode.getIsInverse()) {
				referenceTypeId = referenceNode.getReferenceTypeId();
				break;
			}
		}
		// new nodeid
		ExpandedNodeId newExpandedNodeId = null;
		NodeId newNodeId = null;
		// do new nodeid
		if (newExpandedNodeId == null) {
			int index = parent.getNodeId().getNamespaceIndex();
			newNodeId = this.nodeFactory.getNextNodeId(index, null, parent.getNodeId().getIdType(), NodeIdMode.APPEND);
			newExpandedNodeId = this.getServer().getNamespaceUris().toExpandedNodeId(newNodeId);
		}
		// parent nodeid
		ExpandedNodeId parentNodeId = this.getServer().getNamespaceUris().toExpandedNodeId(parent.getNodeId());
		/** Add nodes statement */
		AddNodesItem node2add = new AddNodesItem();
		node2add.setBrowseName(child.getBrowseName());
		node2add.setNodeClass(child.getNodeClass());
		node2add.setParentNodeId(parentNodeId);
		node2add.setTypeDefinition(type);
		node2add.setRequestedNewNodeId(newExpandedNodeId);
		node2add.setReferenceTypeId(referenceTypeId);
		NodeAttributes attributes = null;
		switch (child.getNodeClass()) {
		case Object:
			attributes = new ObjectAttributes();
			((ObjectAttributes) attributes).setDescription(((ObjectNode) child).getDescription());
			((ObjectAttributes) attributes).setDisplayName(((ObjectNode) child).getDisplayName());
			((ObjectAttributes) attributes).setEventNotifier(((ObjectNode) child).getEventNotifier());
			((ObjectAttributes) attributes).setUserWriteMask(((ObjectNode) child).getUserWriteMask());
			((ObjectAttributes) attributes).setWriteMask(((ObjectNode) child).getWriteMask());
			break;
		case Variable:
			attributes = new VariableAttributes();
			((VariableAttributes) attributes).setAccessLevel(((VariableNode) child).getAccessLevel());
			((VariableAttributes) attributes).setArrayDimensions(((VariableNode) child).getArrayDimensions());
			((VariableAttributes) attributes).setDataType(((VariableNode) child).getDataType());
			((VariableAttributes) attributes).setDescription(((VariableNode) child).getDescription());
			((VariableAttributes) attributes).setDisplayName(((VariableNode) child).getDisplayName());
			((VariableAttributes) attributes).setHistorizing(((VariableNode) child).getHistorizing());
			((VariableAttributes) attributes)
					.setMinimumSamplingInterval(((VariableNode) child).getMinimumSamplingInterval());
			((VariableAttributes) attributes).setUserAccessLevel(((VariableNode) child).getUserAccessLevel());
			((VariableAttributes) attributes).setUserWriteMask(((VariableNode) child).getUserWriteMask());
			((VariableAttributes) attributes).setValue(((VariableNode) child).getValue());
			((VariableAttributes) attributes).setValueRank(((VariableNode) child).getValueRank());
			((VariableAttributes) attributes).setWriteMask(((VariableNode) child).getWriteMask());
			break;
		case Method:
			attributes = new MethodAttributes();
			((MethodAttributes) attributes).setDescription(((MethodNode) child).getDescription());
			((MethodAttributes) attributes).setDisplayName(((MethodNode) child).getDisplayName());
			((MethodAttributes) attributes).setExecutable(((MethodNode) child).getExecutable());
			((MethodAttributes) attributes).setUserExecutable(((MethodNode) child).getUserExecutable());
			((MethodAttributes) attributes).setUserWriteMask(((MethodNode) child).getUserWriteMask());
			((MethodAttributes) attributes).setWriteMask(((MethodNode) child).getWriteMask());
			break;
		case View:
			attributes = new ViewAttributes();
			((ViewAttributes) attributes).setContainsNoLoops(((ViewNode) child).getContainsNoLoops());
			((ViewAttributes) attributes).setDescription(((ViewNode) child).getDescription());
			((ViewAttributes) attributes).setDisplayName(((ViewNode) child).getDisplayName());
			((ViewAttributes) attributes).setEventNotifier(((ViewNode) child).getEventNotifier());
			((ViewAttributes) attributes).setUserWriteMask(((ViewNode) child).getUserWriteMask());
			((ViewAttributes) attributes).setWriteMask(((ViewNode) child).getWriteMask());
			break;
		case ObjectType:
			attributes = new ObjectTypeAttributes();
			((ObjectTypeAttributes) attributes).setDescription(((ObjectTypeNode) child).getDescription());
			((ObjectTypeAttributes) attributes).setDisplayName(((ObjectTypeNode) child).getDisplayName());
			((ObjectTypeAttributes) attributes).setIsAbstract(((ObjectTypeNode) child).getIsAbstract());
			((ObjectTypeAttributes) attributes).setUserWriteMask(((ObjectTypeNode) child).getUserWriteMask());
			((ObjectTypeAttributes) attributes).setWriteMask(((ObjectTypeNode) child).getWriteMask());
			break;
		case VariableType:
			attributes = new VariableTypeAttributes();
			((VariableTypeAttributes) attributes).setArrayDimensions(((VariableTypeNode) child).getArrayDimensions());
			((VariableTypeAttributes) attributes).setDataType(((VariableTypeNode) child).getDataType());
			((VariableTypeAttributes) attributes).setDescription(((VariableTypeNode) child).getDescription());
			((VariableTypeAttributes) attributes).setDisplayName(((VariableTypeNode) child).getDisplayName());
			((VariableTypeAttributes) attributes).setIsAbstract(((VariableTypeNode) child).getIsAbstract());
			((VariableTypeAttributes) attributes).setUserWriteMask(((VariableTypeNode) child).getUserWriteMask());
			((VariableTypeAttributes) attributes).setValue(((VariableTypeNode) child).getValue());
			((VariableTypeAttributes) attributes).setValueRank(((VariableTypeNode) child).getValueRank());
			((VariableTypeAttributes) attributes).setWriteMask(((VariableTypeNode) child).getWriteMask());
			break;
		case DataType:
			attributes = new DataTypeAttributes();
			((DataTypeAttributes) attributes).setDescription(((DataTypeNode) child).getDescription());
			((DataTypeAttributes) attributes).setDisplayName(((DataTypeNode) child).getDisplayName());
			((DataTypeAttributes) attributes).setIsAbstract(((DataTypeNode) child).getIsAbstract());
			((DataTypeAttributes) attributes).setUserWriteMask(((DataTypeNode) child).getUserWriteMask());
			((DataTypeAttributes) attributes).setWriteMask(((DataTypeNode) child).getWriteMask());
			break;
		case ReferenceType:
			attributes = new ReferenceTypeAttributes();
			((ReferenceTypeAttributes) attributes).setDescription(((ReferenceTypeNode) child).getDescription());
			((ReferenceTypeAttributes) attributes).setDisplayName(((ReferenceTypeNode) child).getDisplayName());
			((ReferenceTypeAttributes) attributes).setInverseName(((ReferenceTypeNode) child).getInverseName());
			((ReferenceTypeAttributes) attributes).setIsAbstract(((ReferenceTypeNode) child).getIsAbstract());
			((ReferenceTypeAttributes) attributes).setSymmetric(((ReferenceTypeNode) child).getSymmetric());
			((ReferenceTypeAttributes) attributes).setUserWriteMask(((ReferenceTypeNode) child).getUserWriteMask());
			((ReferenceTypeAttributes) attributes).setWriteMask(((ReferenceTypeNode) child).getWriteMask());
			((ReferenceTypeNode) child).getBrowseName();
			break;
		default:
			attributes = new NodeAttributes();
		}
		try {
			node2add.setNodeAttributes(ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance()));
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		Map<ExpandedNodeId, AddNodesItem> addnodesmap = new HashMap<>();
		addnodesmap.put(node2add.getRequestedNewNodeId(), node2add);
		// add the new node
		// AddNodesResult[] result =
		AddNodesResult[] res = addNodes(new AddNodesItem[] { node2add }, addnodesmap, includeStructure, session,
				includeParentComponents, createOptionalPlaceholder, addModellingRule);
		List<AddReferencesItem> references2add = new ArrayList<>();
		// get all references from the node to copy
		for (int i = 0; i < child.getReferences().length; i++) {
			ReferenceNode referenceNode = child.getReferences()[i];
			if (addModellingRule == false
					&& referenceNode.getReferenceTypeId().compareTo(Identifiers.HasModellingRule) == 0) {
				continue;
			}
			AddReferencesItem reference2add = new AddReferencesItem();
			// direction
			reference2add.setIsForward(!referenceNode.getIsInverse());
			// reference type
			reference2add.setReferenceTypeId(referenceNode.getReferenceTypeId());
			// source node
			reference2add.setSourceNodeId(newNodeId);
			// target node
			reference2add.setTargetNodeClass(parent.getNodeClass());
			// skips invalid references that does not belong to the type node
			boolean skip = false;
			// change hierachical parent to new parent
			if (referenceNode.getIsInverse()) {
				reference2add.setTargetNodeId(parentNodeId);
			}
			// Use just the valid references
			else { // get target node
				Node targetNode = getNodeById(referenceNode.getTargetId());
				// get the inverse parent reference
				for (ReferenceNode targetNodeReferences : targetNode.getReferences()) {
					boolean isHierachical = getTypeTable().isTypeOf(targetNodeReferences.getReferenceTypeId(),
							Identifiers.HierarchicalReferences);
					if (isHierachical && targetNodeReferences.getIsInverse()) {
						ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(child.getNodeId());
						if (targetNodeReferences.getTargetId().equals(eNid)) {
							skip = true;
						}
						break;
					}
				}
				reference2add.setTargetNodeId(referenceNode.getTargetId());
			}
			// skips the hierachical model references that points to its model
			// children
			if (!skip) {
				references2add.add(reference2add);
			}
		}
		if (!references2add.isEmpty()) {
			addReference(references2add.toArray(new AddReferencesItem[0]), session);
		}
	}

	/**
	 * 
	 * @param parentNode
	 * @param mapping
	 * @param session
	 */
	private void addCreateChildStructure(Node parentNode, Map<NodeId, Node> mapping, boolean includeStructure,
			OPCServerSession session, boolean includeParentComponents) {
		// is sturcture (has more children to add
		boolean isComplex = addCheckComplexNodeStructure(parentNode);
		if (isComplex) {
			// find type of node
			NodeId type = null;
			for (ReferenceNode node : parentNode.getReferences()) {
				if (getTypeTable().isTypeOf(node.getReferenceTypeId(), Identifiers.HasTypeDefinition)) {
					ExpandedNodeId exptype = node.getTargetId();
					try {
						type = getNamespaceUris().toNodeId(exptype);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
					break;
				}
			}
			// if we have a method node, so we have no typedefinition
			if (NodeId.isNull(type) && parentNode.getNodeClass() != NodeClass.Method) {
				return;
			}
			// get the type node
			Node typeNode = addGetNode(type, mapping);
			// add children
			if (typeNode != null || parentNode.getNodeClass() == NodeClass.Method) {
				Node[] typeChildren = null;
				if (parentNode.getNodeClass() == NodeClass.Method) {
					typeChildren = findMethodChildrenOfType(parentNode);
				} else {
					if (typeNode != null)
						typeChildren = addFindNodeChildren(typeNode.getNodeId(), mapping, includeParentComponents)
								.toArray(new Node[0]); // NOSONAR
				}
				for (Node node : typeChildren) {
					switch (node.getNodeClass()) {
					case Method:
						addCreateChild(node, parentNode, includeStructure, session, includeParentComponents);
						break;
					case Object:
					case Variable:
					case View:
						/**
						 * delete function to prevent bug of create nodes in type model
						 */
						addCreateChild(node, parentNode, includeStructure, session, includeParentComponents);
						// /**
						// * also create structure of each node
						// */
						// if(includeStructure) {
						// Map<NodeId, Node> map = new HashMap<>();
						// map.put(node.getNodeId(), node);
						// addCreateChildStructure(node, map, includeStructure, session);
						// }
						break;
					default:
						break;
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param parentNode
	 * @param mapping
	 * @param session
	 */
	private void addCreateChildStructure(Node parentNode, Map<NodeId, Node> mapping, boolean includeStructure,
			OPCServerSession session, boolean includeParentComponents, boolean createOptionalPlaceholder,
			boolean addModellingRule) {
		// is sturcture (has more children to add
		boolean isComplex = addCheckComplexNodeStructure(parentNode);
		if (isComplex) {
			// find type of node
			NodeId type = null;
			for (ReferenceNode node : parentNode.getReferences()) {
				if (getTypeTable().isTypeOf(node.getReferenceTypeId(), Identifiers.HasTypeDefinition)) {
					ExpandedNodeId exptype = node.getTargetId();
					try {
						type = getNamespaceUris().toNodeId(exptype);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
					break;
				}
			}
			// if we have a method node, so we have no typedefinition
			if (NodeId.isNull(type) && parentNode.getNodeClass() != NodeClass.Method) {
				return;
			}
			// get the type node
			Node typeNode = addGetNode(type, mapping);
			// add children
			if (typeNode != null || parentNode.getNodeClass() == NodeClass.Method) {
				Node[] typeChildren = null;
				if (parentNode.getNodeClass() == NodeClass.Method) {
					typeChildren = findMethodChildrenOfType(parentNode);
				} else if (typeNode != null) {
					typeChildren = addFindNodeChildren(typeNode.getNodeId(), mapping, includeParentComponents,
							createOptionalPlaceholder).toArray(new Node[0]); // NOSONAR
				}
				if (typeChildren != null) {
					for (Node node : typeChildren) {
						switch (node.getNodeClass()) {
						case Method:
							/**
							 * delete function to prevent bug of create nodes in type model
							 */
							addCreateChild(node, parentNode, includeStructure, session, includeParentComponents,
									createOptionalPlaceholder, addModellingRule);
							break;
						case Object:
						case Variable:
						case View:
							/**
							 * delete function to prevent bug of create nodes in type model
							 */
							addCreateChild(node, parentNode, includeStructure, session, includeParentComponents,
									createOptionalPlaceholder, addModellingRule);
							// /**
							// * also create structure of each node
							// */
							// if(includeStructure) {
							// Map<NodeId, Node> map = new HashMap<>();
							// map.put(node.getNodeId(), node);
							// addCreateChildStructure(node, map, includeStructure, session);
							// }
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * call recursively all parent types to identify each childnode until
	 * objectsfoldertype was reached
	 * 
	 * @param nodeId
	 * @param mapping
	 * @param includeParentComponents
	 * @return
	 */
	private List<Node> addFindNodeChildren(NodeId nodeId, Map<NodeId, Node> mapping, boolean includeParentComponents) {
		// ua node
		Node parentNode = addGetNode(nodeId, mapping);
		if (parentNode == null) {
			return new ArrayList<>(); // Node[0];
		}
		// init its children
		List<Node> children = new ArrayList<Node>();
		for (ReferenceNode refNode : parentNode.getReferences()) {
			// checks the hierachical reference
			boolean isHierachical = getTypeTable().isTypeOf(refNode.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			// check that the direction is forward and it is not an type node
			if (isHierachical) {
				if (!refNode.getIsInverse()) {
					children.add(addGetNode(refNode.getTargetId(), mapping));
				} else
					try {
						if (includeParentComponents
								&& getTypeTable().isTypeOf(refNode.getReferenceTypeId(), Identifiers.HasSubtype)
								&& !getNamespaceUris().toNodeId(refNode.getTargetId())
										.equals(Identifiers.BaseObjectType)) {
							try {
								children.addAll(addFindNodeChildren(getNamespaceUris().toNodeId(refNode.getTargetId()),
										mapping, includeParentComponents));
							} catch (ServiceResultException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
							}
						}
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
			}
		}
		return children;
	}

	/**
	 * call recursively all parent types to identify each childnode until
	 * objectsfoldertype was reached
	 * 
	 * @param nodeId
	 * @param mapping
	 * @param includeParentComponents
	 * @return
	 */
	private List<Node> addFindNodeChildren(NodeId nodeId, Map<NodeId, Node> mapping, boolean includeParentComponents,
			boolean createOptionalPlaceholder) {
		// ua node
		Node parentNode = addGetNode(nodeId, mapping);
		if (parentNode == null) {
			return new ArrayList<>(); // Node[0];
		}
		// init its children
		List<Node> children = new ArrayList<Node>();
		for (ReferenceNode refNode : parentNode.getReferences()) {
			if (!createOptionalPlaceholder && !refNode.getIsInverse()) {
				// check if the node is not optional placeholder
				try {
					Node nodeToTest = addGetNode(getNamespaceUris().toNodeId(refNode.getTargetId()), mapping);
					if (nodeToTest != null) {
						ExpandedNodeId[] rules = nodeToTest.findTargets(Identifiers.HasModellingRule, false);
						if (rules != null && rules.length > 0) {
							ExpandedNodeId eNid = this.getServer().getNamespaceUris()
									.toExpandedNodeId(Identifiers.ModellingRule_OptionalPlaceholder);
							if (rules[0].compareTo(eNid) == 0)
								continue;
						}
					}
					// try to find modellingrule
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			// checks the hierachical reference
			boolean isHierachical = getTypeTable().isTypeOf(refNode.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			// check that the direction is forward and it is not an type node
			if (isHierachical) {
				if (!refNode.getIsInverse()) {
					children.add(addGetNode(refNode.getTargetId(), mapping));
				} else
					try {
						if (includeParentComponents
								&& getTypeTable().isTypeOf(refNode.getReferenceTypeId(), Identifiers.HasSubtype)
								&& !getNamespaceUris().toNodeId(refNode.getTargetId())
										.equals(Identifiers.BaseObjectType)) {
							try {
								children.addAll(addFindNodeChildren(getNamespaceUris().toNodeId(refNode.getTargetId()),
										mapping, includeParentComponents, createOptionalPlaceholder));
							} catch (ServiceResultException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
							}
						}
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
			}
		}
		return children;
	}

	private Node addGetNode(ExpandedNodeId expNodeId, Map<NodeId, Node> mapping) {
		try {
			NodeId nodeId = getNamespaceUris().toNodeId(expNodeId);
			return addGetNode(nodeId, mapping);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return null;
	}

	private Node addGetNode(NodeId nodeId, Map<NodeId, Node> mapping) {
		if (NodeId.isNull(nodeId)) {
			return null;
		}
		// local address space node
		Node node = getNodeById(nodeId);
		if (node == null) {
			// temporary
			node = mapping.get(nodeId);
			// driver node
			if (node == null) {
				node = ComDRVManager.getDRVManager().getNodeFromUnderlyingSystem(nodeId);
			}
		}
		return node;
	}

	/**
	 * new addGetParent function with performed search via map
	 * 
	 * @param nodeToAdd
	 * @param queuedNodesToAdd
	 * @return
	 */
	private Node addGetParent(AddNodesItem nodeToAdd, Map<ExpandedNodeId, AddNodesItem> queuedNodesToAdd) {
		AddNodesItem item = queuedNodesToAdd.get(nodeToAdd.getParentNodeId());
		if (item != null) {
			return AddressSpaceUtil.decodeNode(getNamespaceUris(), item, this.nodeFactory);
		}
		return null;
	}

	/**
	 * Checks a node if it has more children to create.
	 * 
	 * @param node      Node ot check.
	 * @param NodeToAdd Description to add the node.
	 * @return TRUE if the node has more children to create, otherwise FALSE.
	 */
	private boolean addCheckComplexNodeStructure(Node nodeToAdd) {
		ExpandedNodeId type = null;
		for (ReferenceNode node : nodeToAdd.getReferences()) {
			if (getTypeTable().isTypeOf(node.getReferenceTypeId(), Identifiers.HasTypeDefinition)) {
				type = node.getTargetId();
				break;
			}
		}
		if (!ExpandedNodeId.isNull(type) || nodeToAdd.getNodeClass() == NodeClass.Method) {
			NodeClass nodeClass = nodeToAdd.getNodeClass();
			try {
				NodeId typeId = getNamespaceUris().toNodeId(type);
				boolean isType = false;
				switch (nodeClass) {
				case Variable:
					// skip simple variable nodes
					if (!(Identifiers.BaseVariableType.equals(typeId)
							|| Identifiers.BaseDataVariableType.equals(typeId))) {
						ExpandedNodeId eNid = this.getServer().getNamespaceUris()
								.toExpandedNodeId(Identifiers.BaseVariableType);
						isType = getTypeTable().isTypeOf(type, eNid);
						// isType = getTypeTable().isTypeOf(type,
						// getNamespaceUris().toExpandedNodeId(Identifiers.BaseVariableType));
					}
					break;
				case Object:
					if (!(Identifiers.BaseObjectType.equals(typeId))) {
						ExpandedNodeId eNid = this.getServer().getNamespaceUris()
								.toExpandedNodeId(Identifiers.BaseObjectType);
						isType = getTypeTable().isTypeOf(type, eNid);
						// isType = getTypeTable().isTypeOf(type,
					}
					break;
				case Method:
					// check complex node depending on parent type
					// Node[] refs = findMethodChildrenOfType(nodeToAdd);
					Node[] refs = findMethodChildrenOfType(nodeToAdd);
					if (refs != null && refs.length > 0)
						isType = true;
					break;
				default:
					break;
				}
				return isType;
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
		return false;
	}

	private Node[] findMethodChildrenOfType(Node nodeToCheck) {
		List<Node> refs = new ArrayList<>();
		for (ReferenceNode node : nodeToCheck.getReferences()) {
			if (node.getIsInverse()) {
				// parent found
				NodeId nid;
				try {
					if (node.getTargetId() != null) {
						nid = getNamespaceUris().toNodeId(node.getTargetId());
						Node parentNode = getNodeById(nid);
						for (ReferenceNode n : parentNode.getReferences()) {
							if (getTypeTable().isTypeOf(n.getReferenceTypeId(), Identifiers.HasTypeDefinition)) {
								Node parentTypeNode = getNodeById(n.getTargetId());
								// type definition found
								// run till we find
								while (parentTypeNode.getNodeId().compareTo(Identifiers.ObjectTypesFolder) != 0) {
									// Node parent = getNodeById(n.getTargetId());
									ReferenceNode[] refNodes = parentTypeNode.getReferences();
									for (ReferenceNode rnode : refNodes) {
										if (!rnode.getIsInverse()) {
											Node cnode = getNodeById(rnode.getTargetId());
											if (cnode.getNodeClass() == NodeClass.Method
													&& cnode.getBrowseName().equals(nodeToCheck.getBrowseName())) {
												// we found the correct methode
												ReferenceNode[] references = cnode.getReferences();
												// now find only input and output arguments
												for (ReferenceNode ref : references) {
													if (!ref.getIsInverse()) {
														Node refnode = getNodeById(ref.getTargetId());
														if (refnode.getNodeClass() == NodeClass.Variable && (refnode
																.getBrowseName()
																.equals(new QualifiedName(BrowseNames.INPUTARGUMENTS))
																|| refnode.getBrowseName().equals(new QualifiedName(
																		BrowseNames.OUTPUTARGUMENTS)))) {
															// add reference ref
															refs.add(refnode);
														}
													}
												}
												return refs.toArray(new Node[0]);
											}
										} else {
											if (rnode.getReferenceTypeId().compareTo(Identifiers.HasSubtype) == 0) {
												parentTypeNode = getNodeById(rnode.getTargetId());
											}
										}
									}
								}
								break;
							}
						}
					}
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				// node.getTargetId()
				// type = node.getTargetId();
				break;
			}
		}
		return refs.toArray(new Node[0]);
	}

	private boolean addInsert(Entry<Node, Node> entry, List<AddNodesResult> results, OPCServerSession session) {
		StatusCode statusCode = addNode(entry.getKey(), entry.getValue(), session);
		if (statusCode != null) {
			// update last nodeid to DefaultNodeFactory
			this.nodeFactory.asLastIfPossibleId(entry.getKey().getNodeId());
			AddNodesResult result = new AddNodesResult();
			result.setAddedNodeId(entry.getKey().getNodeId());
			result.setStatusCode(statusCode);
			results.add(result);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Create a Reference between two Nodes.
	 * 
	 * @param justValidate
	 * 
	 * @param AddressSpace  Server�s AddressSpace.F
	 * @param SourceNodeId  NodeId from the SourceNode.
	 * @param ReferenceNode New Reference between two Nodes.
	 * @return StatusCode from the AddReferenceService.
	 */
	private StatusCode addValidateAddReference(Node sourceNode, ReferenceNode referenceNode) {
		// Check if the SourceNode exists
		if (sourceNode == null) {
			return new StatusCode(StatusCodes.Bad_SourceNodeIdInvalid);
		}
		// Check if the TargetNodeId is valid
		if (referenceNode.getTargetId() == null || getNodeById(referenceNode.getTargetId()) == null) {
			return new StatusCode(StatusCodes.Bad_TargetNodeIdInvalid);
		}
		if (NodeClass.getMask(getNodeById(referenceNode.getTargetId()).getNodeClass()).equals(UnsignedInteger.ZERO)) {
			return new StatusCode(StatusCodes.Bad_NodeClassInvalid);
		}
		// Check for duplicate Reference
		if (addValidateDupplicateReference(sourceNode, referenceNode))
			return new StatusCode(StatusCodes.Bad_DuplicateReferenceNotAllowed);
		// Check for Self Referencing
		ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(sourceNode.getNodeId());
		if (referenceNode.getTargetId().equals(eNid)) {
			// if
			// (referenceNode.getTargetId().equals(getNamespaceUris().toExpandedNodeId(sourceNode.getNodeId())))
			// {
			return new StatusCode(StatusCodes.Bad_InvalidSelfReference);
		}
		// Check for valid Reference Type
		ReferenceTypeNode type = (ReferenceTypeNode) getNodeById(referenceNode.getReferenceTypeId());
		if (type == null) {
			return new StatusCode(StatusCodes.Bad_ReferenceTypeIdInvalid);
		}
		// An abstract type is not allowed
		if (type.getIsAbstract() == true) {
			return new StatusCode(StatusCodes.Bad_ReferenceNotAllowed);
		}
		// do not add references from Mandatory and Optional ObjectNode to
		// target
		// NodeId nodeId = sourceNode.getNodeId();
		// if (NodeId.isNull(nodeId) && (NodeId.get(IdType.Numeric, 0, new
		// UnsignedInteger(78)).equals(nodeId))
		// || NodeId.get(IdType.Numeric, 0, new
		// UnsignedInteger(80)).equals(nodeId)) {
		// return StatusCode.BAD;
		// }
		// TODO: BAD_REFERENCE_LOCAL_ONLY && BAD_USER_ACCESS_DENIED
		return StatusCode.GOOD;
	}

	/**
	 * Checks if a Reference exits in the Node argument.
	 * 
	 * @param Node          Node to check.
	 * @param ReferenceNode Refernece to find in the Node.
	 * @return TRUE if the Node contains the Reference, otherwise FALSE.
	 */
	private boolean addValidateDupplicateReference(Node node, ReferenceNode referenceNode) {
		for (ReferenceNode reference : node.getReferences()) {
			if (referenceNode.getTargetId().equals(reference.getTargetId())
					&& referenceNode.getReferenceTypeId().equals(reference.getReferenceTypeId())) {
				return true;
			}
		}
		return false;
	}

	private NodeId binaryChangeNodeId(NamespaceTable externNsTable, NodeId nodeId) {
		NamespaceTable nsTable = getNamespaceUris();
		String uri = externNsTable.getUri(nodeId.getNamespaceIndex());
		// find index for server table
		int index = nsTable.getIndex(uri);
		if (index < 0) {
			index = nsTable.add(uri);
		}
		// change nodeid
		return NodeIdUtil.createNodeId(index, nodeId.getValue());
	}

	private ExpandedNodeId binaryChangeNodeId(NamespaceTable externNsTable, ExpandedNodeId targetId)
			throws ServiceResultException {
		NamespaceTable nsTable = getNamespaceUris();
		NodeId target = externNsTable.toNodeId(targetId);
		String uri = externNsTable.getUri(target.getNamespaceIndex());
		// find index for server table
		int index = nsTable.getIndex(uri);
		if (index < 0) {
			index = nsTable.add(uri);
		}
		// change nodeid
		NodeId target2 = NodeIdUtil.createNodeId(index, target.getValue());
		return this.getServer().getNamespaceUris().toExpandedNodeId(target2);
		// return enid;
	}

	private StatusCode browseNext(OPCServerSession session, BrowseContinuationPoint cp, BrowseResult result)
			throws ServiceResultException {
		List<ReferenceDescription> references = new ArrayList<ReferenceDescription>();
		StatusCode error = cp.fetchReferences(this, references);
		result.setReferences(references.toArray(new ReferenceDescription[0]));
		getServer().logService(RequestType.BrowseNext,
				error + " " + cp.getNodeToBrowse().getNodeId() + " found " + references.size() + " browsed nodes");
		return error;
	}

	private StatusCode createNewReferencesOnAddedNode(Node nodeToAdd, Node parentNode, TypeTable typeTree,
			NamespaceTable namespaceTable) {
		if (parentNode != null) {
			ReferenceNode[] checkedNode = nodeToAdd.getReferences();
			NodeId refTypeId = null;
			// inverse parent
			try {
				for (ReferenceNode refNode : checkedNode) {
					NodeId inverseParent = namespaceTable.toNodeId(refNode.getTargetId());
					if (refNode.getIsInverse() && parentNode.getNodeId().equals(inverseParent)) {
						refTypeId = refNode.getReferenceTypeId();
					}
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(nodeToAdd.getNodeId());
			ReferenceNode addedReference = new ReferenceNode(refTypeId, false, eNid);
			return createNewReferenceOnNode(parentNode, addedReference);
		} else if (parentNode == null && AddressSpaceUtil.isServerBaseNode(nodeToAdd)) {
			return StatusCode.GOOD;
		}
		return StatusCode.GOOD;
	}

	private StatusCode createNewReferenceOnNode(Node node, ReferenceNode referenceNode) {
		ReferenceNode[] references = node.getReferences();
		List<ReferenceNode> referenceNodeList = new ArrayList<ReferenceNode>();
		for (ReferenceNode refNode : references) {
			referenceNodeList.add(refNode);
		}
		referenceNodeList.add(referenceNode);
		references = referenceNodeList.toArray(new ReferenceNode[referenceNodeList.size()]);
		node.setReferences(references);
		getServer().logService(RequestType.AddReferences,
				referenceNode.getReferenceTypeId() + " - " + referenceNode.getIsInverse() + " - "
						+ referenceNode.getTargetId() + " reference is added to node " + node.getNodeId());
		return StatusCode.GOOD;
	}

	private DataValue read(ReadValueId readValueId, Double maxAge, DateTime timestamp, long driverState,
			OPCServerSession session) {
		// initialize datavalue
		DataValue value = new DataValue();
		// validate request
		ServiceResult error = opc.sdk.core.utils.ReadValueId.validate(readValueId);
		if (error != null && error.isBad()) {
			value.setStatusCode(error.getCode());
			return value;
		}
		Node node = getNodeById(readValueId.getNodeId());
		// check if the node is in the address space
		if (node == null || NodeId.isNull(node.getNodeId())) {
			value.setStatusCode(StatusCodes.Bad_NodeIdUnknown);
			return value;
		}
		ServiceResult result = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.Read, session, node);
		if (result.isBad()) {
			value.setStatusCode(result.getCode());
			return value;
		}
		// read attribute
		readAttribute(node, readValueId.getAttributeId(), readValueId.getIndexRange(), readValueId.getDataEncoding(),
				value, driverState, session);
		/** max age to check */
		if (value.getServerTimestamp() != null && DateTime.MIN_VALUE.compareTo(value.getServerTimestamp()) != 0
				&& maxAge > 0) {
			if (timestamp.getTimeInMillis() + maxAge < value.getServerTimestamp().getTimeInMillis()) {
				value.setStatusCode(new StatusCode(StatusCodes.Bad_MaxAgeInvalid));
			}
		}
		return value;
	}

	/**
	 * Reads an attribute value from the node. TODO: DRIVEROPERATIONS
	 * 
	 * @param handle
	 * 
	 * @param typeTable
	 * @param addressSpace
	 * 
	 * @param maxAge
	 * 
	 * @param Context      Context of a read operation.
	 * @param AttributeId  AttributeId from the Node to read.
	 * @param IndexRange   Index range from the node�s value to read.
	 * @param DataEncoding DataEncoding from the value.
	 * @return value that has been read.
	 */
	private void readAttribute(Node handle, UnsignedInteger attributeId, String indexRange, QualifiedName dataEncoding,
			DataValue value, long driverState, OPCServerSession session) {
		// read VALUE attribute
		if (Attributes.Value.compareTo(attributeId) == 0) {
			// read permitted by server
			if (handle.getNodeClass() == NodeClass.Variable) {
				// fetch node
				UAVariableNode node = (UAVariableNode) handle;
				// namespace index <= 2 should not write or read to server
				int nsIndex = -1;
				if (!NodeId.isNull(node.getNodeId())) {
					nsIndex = node.getNodeId().getNamespaceIndex();
				}
				if (nsIndex > 0) {
					// register node on driver interface
					if (node.getSyncReadMask() == ComDRVManager.READNOTINIT) {
						ComDRVManager.getDRVManager().prepareRead(node.getNodeId());
					}
					// sync read value from driver
					if (node.getSyncReadMask() != ComDRVManager.NOREAD) {
						// read from underlying system sync
						if (node.getSyncReadMask() == ComDRVManager.SYNCREAD) {
							DataValue valueFromDriver = ComDRVManager.getDRVManager().syncReadValue(node.getNodeId(),
									node.getReadDriverIds(), driverState);
							// update opc ua address space with underlying value
							if (valueFromDriver != null && !valueFromDriver.isNull()) {
								if (handle instanceof UAServerVariableNode)
									((UAServerVariableNode) handle).writeValue(Attributes.Value, valueFromDriver);
								else
									handle.write(Attributes.Value, valueFromDriver.getValue().getValue());
								// basic attribute read
								readValueAttribute(handle, attributeId, indexRange, dataEncoding, value,
										(session != null) ? session.getLocaleIds() : null);
								value.setStatusCode(valueFromDriver.getStatusCode());
								return;
							}
						}
						// // read from underlying system async
						else if (node.getSyncReadMask() == ComDRVManager.ASYNCREAD) {
							StatusCode writeResult = ComDRVManager.getDRVManager().asyncReadValue(node.getNodeId(),
									node.getReadDriverIds(), driverState);
							// statuscode from async read
							if (writeResult != null) {
								value.setStatusCode(writeResult);
								// exit
								return;
							}
						}
					}
				}
				// basic attribute read
				readValueAttribute(handle, attributeId, indexRange, dataEncoding, value,
						(session != null) ? session.getLocaleIds() : null);
			} else {
				value.setStatusCode(StatusCodes.Bad_AttributeIdInvalid);
				value.setValue(Variant.NULL);
			}
		} else {
			// read NON-VALUE attribute
			readNonValueAttribute(handle, attributeId, indexRange, dataEncoding, value,
					(session != null) ? session.getLocaleIds() : null);
		}
	}

	/**
	 * Reads a Non-Value-Attribute.
	 * 
	 * @param maxAge
	 * @param dataEncoding
	 * @param indexRange
	 * @param error2
	 * @param typeTable
	 * @param addressSpace
	 * 
	 * @param Context      Context of a read operation.
	 * @param AttributeId  AttributeId that is not VALUE (13)
	 * @return value that has been read.
	 */
	private void readNonValueAttribute(Node handle, UnsignedInteger attributeId, String indexRange,
			QualifiedName dataEncoding, DataValue result, String[] locales) {
		/** check if the node supports the value attribute */
		if (!handle.supportsAttribute(attributeId)) {
			result.setStatusCode(new StatusCode(StatusCodes.Bad_AttributeIdInvalid));
			return;
		}
		// read value and set status
		ServiceResult status = handle.readAttributeValue(attributeId, result, locales);
		if (status.isBad()) {
			result.setStatusCode(status.getCode());
		}
	}

	/**
	 * Reads the Value-Attribute.
	 * 
	 * @param typeTable
	 * @param addressSpace
	 * @param locales
	 * 
	 * @param maxAge
	 * 
	 * @param Context      Context of a read operation.
	 * @param AttributeId  AttributeId is VALUE (13)
	 * @param IndexRange   Index Range from the value to read.
	 * @param DataEncoding DataEncoding from the value to read
	 * @return value that has been read.
	 */
	private void readValueAttribute(Node node, UnsignedInteger attributeId, String indexRange,
			QualifiedName dataEncoding, DataValue value, String[] locales) {
		// check if the node supports the value attribute
		if (!node.supportsAttribute(attributeId)) {
			value.setStatusCode(StatusCodes.Bad_AttributeIdInvalid);
			return;
		}
		// ensure the access level for the variable
		if (node instanceof VariableNode) {
			if ((((VariableNode) node).getAccessLevel().intValue() & AccessLevel.CurrentRead.getValue()) == 0) {
				value.setStatusCode(StatusCodes.Bad_NotReadable);
				return;
			}
			if ((((VariableNode) node).getUserAccessLevel().intValue() & AccessLevel.CurrentRead.getValue()) == 0) {
				value.setStatusCode(StatusCodes.Bad_NotReadable);
				return;
			}
		}
		/** check if read is an enumeration */
		if (getTypeTable().isEnumeration(((VariableNode) node).getDataType())) {
			// read enum
			node.readAttributeValue(attributeId, value, locales);
		}
		// index range and data encoding
		StatusCode error = OPCValidationFramework.validateIndexRangeAndDataEncoding(node, indexRange, dataEncoding);
		if (error != null && error.isBad()) {
			value.setStatusCode(error);
			return;
		}
		error = node.readAttributeValue(Attributes.Value, value, locales).getCode();
		if (error != null && error.isBad()) {
			value.setStatusCode(error);
			return;
		}
		error = OPCValidationFramework.applyIndexRangeAndDataEncoding(node, getTypeTable(), indexRange, dataEncoding,
				value);
		if (error != null && error.isBad()) {
			value.setStatusCode(error);
			return;
		}
	}

	public StatusCode[] reportEvent(NodeId[] nodeIds, BaseEventType[] eventStates, Long[] dpstates) {
		this.lock.writeLock().lock();
		try {
			List<StatusCode> results = new ArrayList<StatusCode>();
			for (int i = 0; i < eventStates.length; i++) {
				BaseEventType event = eventStates[i];
				if (event.getSourceNode() == null) {
					results.add(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
					continue;
				}
				NodeId sourceId = event.getSourceNode().getValue();
				Node node = null;
				if ((node = getNodeById(sourceId)) == null) {
					results.add(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
					continue;
				}
				getServer().getSubscriptionManager().reportEvent(node.getMonitoredItems(), node, event);
			}
			return results.toArray(new StatusCode[0]);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	/**
	 * Writes a set of values. Each node manager should only process NodeIds that it
	 * recognizes. If it processes a value it must set the Processed flag in the
	 * WriteValue structure.
	 * 
	 * @param tagstate
	 * 
	 * @param errors
	 * @param diagnostics
	 * 
	 * @param Context         Operation Context.
	 * @param NodeToWrite     Node and their Attribute to write.
	 * @param SkipAccessLevel TRUE skips the AccessLevel on the Node to write.
	 * @param WriteToDriver   The operation writes to the driver.
	 * @return Result for the Node to write.
	 */
	private StatusCode write(WriteValue nodeToWrite, boolean skipAccessLevel, long tagstate, OPCServerSession session) {
		// validate basic
		ServiceResult validation = WriteValue.validate(nodeToWrite);
		if (validation != null) {
			return validation.getCode();
		}
		// index range is not supported
		if (nodeToWrite.getAttributeId().compareTo(Attributes.Value) != 0) {
			if (nodeToWrite.getIndexRange() != null && !nodeToWrite.getIndexRange().isEmpty()) {
				return new StatusCode(StatusCodes.Bad_WriteNotSupported);
			}
		}
		// node handle
		Node node = getNodeById(nodeToWrite.getNodeId());
		/** check if the node exists in the address space */
		if (node == null) {
			return new StatusCode(StatusCodes.Bad_NodeIdUnknown);
		}
		ServiceResult result = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.Write, session, node);
		if (result.isBad()) {
			return result.getCode();
		}
		// old value
		return writeAttribute(node, nodeToWrite.getAttributeId(), nodeToWrite.getParsedIndexRange(),
				nodeToWrite.getIndexRange(), nodeToWrite.getValue(), skipAccessLevel, tagstate,
				(session != null) ? session.getLocaleIds() : null);
	}

	private StatusCode updateDriver(Node node, NodeId nodeId, DataValue value, String indexRange, DataValue oldValue,
			long tagstate) {
		StatusCode driverResult = StatusCode.GOOD;
		if (node.getNodeClass() == NodeClass.Variable) {
			int ns = -1;
			if (!NodeId.isNull(node.getNodeId())) {
				ns = node.getNodeId().getNamespaceIndex();
			}
			if (ns > 0) {
				// custom nodes are checked if they are used by the driver
				if (((UAVariableNode) node).getSyncWriteMask() == ComDRVManager.WRITENOTINIT) {
					ComDRVManager.getDRVManager().prepareWrite(((UAVariableNode) node).getNodeId());
				}
				StatusCode code = value.getStatusCode();
				// send write to driver
				if (((UAVariableNode) node).getSyncWriteMask() != ComDRVManager.NOWRITE && code.isGood()) {
					if (((UAVariableNode) node).getSyncWriteMask() == ComDRVManager.SYNCWRITE) {
						driverResult = ComDRVManager.getDRVManager().syncWriteValue(nodeId, value, oldValue, indexRange,
								((UAVariableNode) node).getWriteDriverIds(), tagstate);
						((UAVariableNode) node).setValueStatus(driverResult);
					} else if (((UAVariableNode) node).getSyncWriteMask() == ComDRVManager.ASYNCWRITE) {
						driverResult = ComDRVManager.getDRVManager().asyncWriteValue(nodeId, value, oldValue,
								indexRange, ((UAVariableNode) node).getWriteDriverIds(), tagstate);
						((UAVariableNode) node).setValueStatus(driverResult);
					}
				}
			}
		}
		return driverResult;
	}

	/**
	 * Write a value to an attribute of a Node.
	 * 
	 * @param context          The context to write on a Node.
	 * @param attributeId      Attribute to write.
	 * @param ParsedIndexRange The ParsedIndexRange to write.
	 * @param indexRange       The IndexRange to write.
	 * @param oldValue
	 * @param tagstate
	 * @param locales
	 * @param namespaceUris
	 * @param typeTable
	 * @param Value            Value to write.
	 * @param SkipAccessLevel  TRUE skips the UserAccessLevel to write on the Node,
	 *                         otherwise FALSE.
	 * @return ServiceResult from the Write service.
	 */
	private StatusCode writeAttribute(Node node, final UnsignedInteger attributeId, NumericRange parsedIndexRange,
			String indexRange, DataValue value2write, boolean skipAccessLevel, long tagstate, String[] locales) {
		/** check for bad parameter */
		if (value2write == null) {
			return new StatusCode(StatusCodes.Bad_StructureMissing);
		}
		// read current value
		DataValue destinationValue = new DataValue();
		node.readAttributeValue(attributeId, destinationValue, locales);
		DataValue lastValue = new DataValue();
		node.readAttributeValue(attributeId, lastValue, locales);
		StatusCode error = null;
		if (Attributes.Value.compareTo(attributeId) == 0) {
			// write value to address space node
			error = writeValueAttribute(node, parsedIndexRange, value2write, destinationValue, lastValue,
					skipAccessLevel);
			if (error.isGood()) {
				// write value to underlying system
				if (node instanceof UAVariableNode) {
					error = updateDriver(node, node.getNodeId(), value2write, indexRange, destinationValue, tagstate);
				}
			}
		} else {
			// cannot use index range for non-value attributes
			if (!parsedIndexRange.isEmpty()) {
				return new StatusCode(StatusCodes.Bad_IndexRangeInvalid);
			}
			error = writeNonValueAttribute(node, attributeId, value2write, tagstate);
		}
		DataValue value = new DataValue();
		node.readAttributeValue(attributeId, value, null);
		MonitoredItemsUpdate update = new MonitoredItemsUpdate(node.getMonitoredItems(), attributeId,
				value/* , lastValue */);
		this.publishExecutor.execute(update);
		return error;
	}

	/**
	 * Write a value for any Non-Value Node Attribute.
	 * 
	 * @param Context     The Context to write.
	 * @param AttributeId The Attribute to write.
	 * @param Value       The value to write.
	 * @return ServiceResult from the write operation.
	 */
	private StatusCode writeNonValueAttribute(Node node, UnsignedInteger attributeId, DataValue value, Long tagstate) {
		try {
			switch (attributeId.intValue()) {
			case 1/* NodeId */:
				NodeId nodeId = null;
				if (value.getValue().getValue() instanceof NodeId) {
					nodeId = (NodeId) value.getValue().getValue();
				}
				return onWriteNodeId(node, nodeId, tagstate);
			case 2 /* NodeClass */:
				NodeClass nodeClass = null;
				if (value.getValue().getValue() instanceof NodeClass) {
					nodeClass = (NodeClass) value.getValue().getValue();
				} else if (value.getValue().getValue() instanceof Short) {
					nodeClass = NodeClass.valueOf(((Short) value.getValue().getValue()).intValue());
				}
				return onWriteNodeClass(node, nodeClass, tagstate);
			case 3 /* BrowseName */:
				QualifiedName browseName = null;
				if (value.getValue().getValue() instanceof QualifiedName) {
					browseName = (QualifiedName) value.getValue().getValue();
				} else if (value.getValue().getValue() instanceof String) {
					browseName = new QualifiedName((String) value.getValue().getValue());
				}
				return onWriteBrowseName(node, browseName, tagstate);
			case 4 /* DisplayName */:
				LocalizedText displayName = null;
				if (value.getValue().getValue() instanceof LocalizedText) {
					displayName = (LocalizedText) value.getValue().getValue();
				}
				return onWriteDisplayName(node, displayName, tagstate);
			case 5 /* Description */:
				LocalizedText description = null;
				if (value.getValue().getValue() instanceof LocalizedText) {
					description = (LocalizedText) value.getValue().getValue();
				}
				return onWriteDescription(node, description, tagstate);
			case 6 /* WriteMask */:
				UnsignedInteger writeMask = null;
				if (value.getValue().getValue() instanceof UnsignedInteger)
					writeMask = (UnsignedInteger) value.getValue().getValue();
				else if (value.getValue().getValue() instanceof Integer)
					writeMask = new UnsignedInteger((Integer) value.getValue().getValue());
				else if (value.getValue().getValue() instanceof Short)
					writeMask = new UnsignedInteger((Short) value.getValue().getValue());
				return onWriteWriteMask(node, writeMask, tagstate);
			case 7 /* UserWriteMask */:
				UnsignedInteger userWriteMask = null;
				if (value.getValue().getValue() instanceof UnsignedInteger)
					userWriteMask = (UnsignedInteger) value.getValue().getValue();
				else if (value.getValue().getValue() instanceof Integer)
					userWriteMask = new UnsignedInteger((Integer) value.getValue().getValue());
				return onWriteUserWriteMask(node, userWriteMask, tagstate);
			case 8 /* IsAbstract */:
				Boolean isAbstract = null;
				if (value.getValue().getValue() instanceof Boolean)
					isAbstract = (Boolean) value.getValue().getValue();
				return onWriteIsAbstract(node, isAbstract, tagstate);
			case 9 /* Symmetric */:
				Boolean symmetric = null;
				if (value.getValue().getValue() instanceof Boolean)
					symmetric = (Boolean) value.getValue().getValue();
				return onWriteSymmetric(node, symmetric, tagstate);
			case 10 /* InverseName */:
				LocalizedText inverseName = null;
				if (value.getValue().getValue() instanceof LocalizedText)
					inverseName = (LocalizedText) value.getValue().getValue();
				return onWriteInverseName(node, inverseName, tagstate);
			case 11 /* ContainsNoLoops */:
				Boolean containsNoLoops = null;
				if (value.getValue().getValue() instanceof Boolean)
					containsNoLoops = (Boolean) value.getValue().getValue();
				return onWriteContainsNoLoops(node, containsNoLoops, tagstate);
			case 12 /* EventNotifier */:
				UnsignedByte eventNotifier = null;
				if (value.getValue().getValue() instanceof UnsignedByte)
					eventNotifier = (UnsignedByte) value.getValue().getValue();
				return onWriteEventNotifier(node, eventNotifier, tagstate);
			/* case 13 Value : */
			case 14 /* DataType */:
				NodeId dataType = null;
				if (value.getValue().getValue() instanceof NodeId)
					dataType = (NodeId) value.getValue().getValue();
				else if (value.getValue().getValue() instanceof Short)
					dataType = new NodeId(0, ((Short) value.getValue().getValue()).intValue());
				return onWriteDataType(node, dataType, tagstate);
			case 15 /* ValueRank */:
				Integer valueRank = null;
				if (value.getValue().getValue() instanceof Integer)
					valueRank = (Integer) value.getValue().getValue();
				return onWriteValueRank(node, valueRank, tagstate);
			case 16 /* ArrayDimensions */:
				UnsignedInteger[] arrayDimensions = null;
				if (value.getValue().getValue() instanceof UnsignedInteger[])
					arrayDimensions = (UnsignedInteger[]) value.getValue().getValue();
				else if (value.getValue().getValue() instanceof Short)
					arrayDimensions = new UnsignedInteger[((Short) value.getValue().getValue()).intValue()];
				return onWriteArrayDimension(node, arrayDimensions, tagstate);
			case 17 /* AccessLevel */:
				UnsignedByte accessLevel = null;
				if (value.getValue().getValue() instanceof UnsignedByte)
					accessLevel = (UnsignedByte) value.getValue().getValue();
				else if (value.getValue().getValue() instanceof UnsignedInteger)
					accessLevel = UnsignedByte.valueOf(((UnsignedInteger) value.getValue().getValue()).intValue());
				return onWriteAccessLevel(node, accessLevel, tagstate);
			case 18 /* UserAccessLevel */:
				UnsignedByte userAccessLevel = null;
				if (value.getValue().getValue() instanceof UnsignedByte)
					userAccessLevel = (UnsignedByte) value.getValue().getValue();
				return onWriteUserAccessLevel(node, userAccessLevel, tagstate);
			case 19 /* MinimumSamplingInterval */:
				Double minimumSamplingInterval = null;
				if (value.getValue().getValue() instanceof Double)
					minimumSamplingInterval = (Double) value.getValue().getValue();
				return onWriteMinimumSamplingInterval(node, minimumSamplingInterval, tagstate);
			case 20 /* Historizing */:
				Boolean historizing = null;
				if (value.getValue().getValue() instanceof Boolean)
					historizing = (Boolean) value.getValue().getValue();
				return onWriteHistorizing(node, historizing, tagstate);
			case 21 /* Executeable */:
				Boolean executeable = null;
				if (value.getValue().getValue() instanceof Boolean)
					executeable = (Boolean) value.getValue().getValue();
				return onWriteExecuteable(node, executeable, tagstate);
			case 22 /* UserExecuteable */:
				Boolean userExecuteable = null;
				if (value.getValue().getValue() instanceof Boolean)
					userExecuteable = (Boolean) value.getValue().getValue();
				return onWriteUserExecuteable(node, userExecuteable, tagstate);
			default:
				return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
			}
			// exception when an invalid write occures
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			return new StatusCode(StatusCodes.Bad_TypeMismatch);
		}
	}

	private Node removeNode(NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			return null;
		}
		OPCAddressSpace space = this.addressSpace.get(nodeId.getNamespaceIndex());
		if (space == null) {
			return null;
		}
		return space.removeNodeById(nodeId);
	}

	private StatusCode deleteReference(Node sourceNode, Node targetNode, NodeId referenceTypeId, Boolean isForward,
			Boolean deleteBidirectional) {
		/**
		 * Delete reference on source node
		 */
		// sourceNode
		ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(targetNode.getNodeId());
		sourceNode.deleteReferences(referenceTypeId, isForward, eNid);
		if (deleteBidirectional) {
			/**
			 * Delete reference on target node! IsForward parameter is inverse!
			 */
			// targetNode.deleteReferences(referenceTypeId, !isForward,
			eNid = this.getServer().getNamespaceUris().toExpandedNodeId(sourceNode.getNodeId());
			targetNode.deleteReferences(referenceTypeId, !isForward, eNid);
		}
		return StatusCode.GOOD;
	}

	private BrowsePathResult translateBrowsePathsToNodeId(BrowsePath browsePath, OPCServerSession session) {
		BrowsePathResult result = new BrowsePathResult();
		result.setStatusCode(StatusCode.GOOD);
		Node sourceHandle = getNodeById(browsePath.getStartingNode());
		if (sourceHandle == null) {
			result.setStatusCode(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
			return result;
		}
		ServiceResult error = this.userAuthentification.checkAuthorityFromNode(AuthorityRule.TranslateBrowsePath,
				session, sourceHandle);
		if (error != null && error.isBad()) {
			result.setStatusCode(error.getCode());
			return result;
		}
		// check the relative path
		RelativePath relativePath = browsePath.getRelativePath();
		if (relativePath.getElements() == null) {
			result.setStatusCode(new StatusCode(StatusCodes.Bad_NothingToDo));
			return result;
		}
		for (int i = 0; i < relativePath.getElements().length; i++) {
			RelativePathElement element = relativePath.getElements()[i];
			if (element == null || QualifiedName.isNull(element.getTargetName())) {
				result.setStatusCode(new StatusCode(StatusCodes.Bad_BrowseNameInvalid));
				return result;
			}
		}
		List<BrowsePathTarget> targets = new ArrayList<BrowsePathTarget>();
		// translate path
		translateBrowsePath(sourceHandle, relativePath, targets, 0);
		result.setTargets(targets.toArray(new BrowsePathTarget[targets.size()]));
		return result;
	}

	/**
	 * Recursively processes the elements in the RelativePath starting at the
	 * specified index.
	 * 
	 * @param Context      Operation Context.
	 * @param SourceHandle SourceHandle from the StartingNode.
	 * @param RelativePath The path to follow from the startingNode.
	 * @param Targets      Targets from the followed Paths.
	 * @param Index        Index from the followed relative Path.
	 */
	private void translateBrowsePath(Node source, RelativePath relativePath, List<BrowsePathTarget> targets,
			int index) {
		// check for end of list
		if (index < 0 || index >= relativePath.getElements().length) {
			return;
		}
		/** follow the next hop */
		RelativePathElement element = relativePath.getElements()[index];
		List<ExpandedNodeId> targetIds = new ArrayList<>();
		List<NodeId> externalTargetIds = new ArrayList<>();
		translateBrowsePath(source, element, targetIds, externalTargetIds);
		for (int ii = 0; ii < externalTargetIds.size(); ii++) {
			ReferenceDescription description = new ReferenceDescription();
			updateReferenceDescription(externalTargetIds.get(ii), NodeClass.getSet(NodeClass.Unspecified.getValue()),
					BrowseResultMask.getSet(BrowseResultMask.BrowseName.getValue()), description);
			/** add to list if target name matches */
			if (description.getBrowseName().equals(element.getTargetName())) {
				boolean found = false;
				for (int jj = 0; jj < targetIds.size(); jj++) {
					if (targetIds.get(jj).equals(externalTargetIds.get(ii))) {
						found = true;
						break;
					}
				}
				if (!found) {
					ExpandedNodeId eNid = this.getServer().getNamespaceUris()
							.toExpandedNodeId(externalTargetIds.get(ii));
					targetIds.add(eNid);
				}
			}
		}
		/** check if done after a final hop */
		if (index == (relativePath.getElements().length - 1)) {
			for (int ii = 0; ii < targetIds.size(); ii++) {
				BrowsePathTarget target = new BrowsePathTarget();
				target.setTargetId(targetIds.get(ii));
				target.setRemainingPathIndex(UnsignedInteger.MAX_VALUE);
				targets.add(target);
			}
			return;
		}
		/** process next hops */
		for (int ii = 0; ii < targetIds.size(); ii++) {
			ExpandedNodeId targetId = targetIds.get(ii);
			// check for external reference
			if (!targetId.isLocal()) {
				BrowsePathTarget target = new BrowsePathTarget();
				target.setTargetId(targetId);
				target.setRemainingPathIndex(new UnsignedInteger(index + 1));
				targets.add(target);
				continue;
			}
			/** check for valid start node */
			source = getNodeById(targetId);
			if (source == null) {
				continue;
			}
			/** recusively follow hops */
			translateBrowsePath(source, relativePath, targets, index + 1);
		}
	}

	/**
	 * Returns the target of the specified browse path fragment
	 * 
	 * @param Context           Operation Context.
	 * @param SourceHandle      NodeHandle from the StartingNode.
	 * @param RelativePath      RelativePathElement from the translated browse path.
	 * @param TargetIds         Translated TargetIds from the service to return.
	 * @param ExternalTargetIds External TargetIds from an other server.
	 */
	private void translateBrowsePath(Node source, RelativePathElement relativePath, List<ExpandedNodeId> targetIds,
			List<NodeId> externalTargetIds) {
		if (source == null) {
			return;
		}
		OPCBrowseSet browser = new OPCBrowseSet(relativePath.getReferenceTypeId(), relativePath.getIncludeSubtypes(),
				(relativePath.getIsInverse()) ? BrowseDirection.Inverse : BrowseDirection.Forward, source,
				getTypeTable(), null);
		// get list of references that relative path
		// NodeBrowser browser = createBrowser(null,
		// relativePath.getReferenceTypeId(),
		// relativePath.getIncludeSubtypes(),
		// (relativePath.getIsInverse()) ? BrowseDirection.Inverse
		// : BrowseDirection.Forward, source, null,
		// this.server.getTypeTree());
		try {
			// check the browser names
			for (ReferenceNode reference = browser.next(); reference != null; reference = browser.next()) {
				// ignore unknown external reference
				// if (reference.getTargetId().isAbsolute()) {
				// continue;
				// }
				Node target = getNodeById(reference.getTargetId());
				if (target == null) {
					// ExpandedNodeId targetId = reference.getTargetId();
					// TODO: what can be done if the node will not be found?
					continue;
				}
				// check browse name
				if (target.getBrowseName().equals(relativePath.getTargetName())) {
					targetIds.add(reference.getTargetId());
				}
			}
		} finally {
			// it should not be needed, but it is also not wrong
			browser = null;
		}
	}

	/**
	 * Updates the reference description with the node attributes.
	 * 
	 * @param Context       Operation Context.
	 * @param TargetId      TargetId
	 * @param NodeClassMask
	 * @param ResultMask
	 * @param Description   Description to update.
	 * @return TRUE for an successful update, otherwise FALSE.
	 */
	private boolean updateReferenceDescription(NodeId targetId, EnumSet<NodeClass> nodeClassMask,
			EnumSet<BrowseResultMask> resultMask, ReferenceDescription description) {
		if (targetId == null) {
			throw new IllegalArgumentException("TargetId");
		}
		if (description == null) {
			throw new IllegalArgumentException("Description");
		}
		// find node manager that owns the node
		Node node = getNodeById(targetId);
		if (node == null) {
			return false;
		}
		// check nodeclass filter
		if (!nodeClassMask.contains(NodeClass.Unspecified) && nodeClassMask.contains(node.getNodeClass())) {
			return false;
		}
		// update atttributes
		ExpandedNodeId eNid = this.getServer().getNamespaceUris().toExpandedNodeId(node.getNodeId());
		description.setNodeId(eNid);
		ExpandedNodeId hasTypeDefinition = node.findTarget(Identifiers.HasTypeDefinition, false);
		if (resultMask.contains(BrowseResultMask.NodeClass)) {
			description.setNodeClass(node.getNodeClass());
		} else {
			description.setNodeClass(NodeClass.valueOf(0));
		}
		if (resultMask.contains(BrowseResultMask.BrowseName)) {
			description.setBrowseName(node.getBrowseName());
		} else {
			description.setBrowseName(null);
		}
		if (resultMask.contains(BrowseResultMask.DisplayName)) {
			description.setDisplayName(node.getDisplayName());
		} else {
			description.setDisplayName(null);
		}
		if (resultMask.contains(BrowseResultMask.TypeDefinition)) {
			description.setTypeDefinition(hasTypeDefinition);
		} else {
			description.setTypeDefinition(null);
		}
		return true;
	}

	/**
	 * Write the UserAccessLevel attribute.
	 * 
	 * @param Context         Write context.
	 * @param UserAccessLevel UnsignedByte to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteUserAccessLevel(Node node, UnsignedByte userAccessLevel, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.UserAccessLevel.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.UserAccessLevel.getValue()) == 0
				&& tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.UserAccessLevel, userAccessLevel).getCode();
	}

	/**
	 * Write the AccessLevel attribute.
	 * 
	 * @param Context     Write context.
	 * @param AccessLevel UnsignedByte to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteAccessLevel(Node node, UnsignedByte accessLevel, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.AccessLevel.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.AccessLevel.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.AccessLevel, accessLevel).getCode();
	}

	/**
	 * Write the UserExecuteable attribute.
	 * 
	 * @param Context         Write context.
	 * @param UserExecuteable Boolean to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteUserExecuteable(Node node, Boolean userExecuteable, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.UserExecutable.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.UserExecutable.getValue()) == 0
				&& tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.UserExecutable, userExecuteable).getCode();
	}

	/**
	 * Write the Executeable attribute.
	 * 
	 * @param Context     Write context.
	 * @param Executeable Boolean to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteExecuteable(Node node, Boolean executeable, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.Executable.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.Executable.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.Executable, executeable).getCode();
	}

	/**
	 * Write the Historizing attribute.
	 * 
	 * @param Context     Write context.
	 * @param Historizing Boolean to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteHistorizing(Node node, Boolean historizing, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.Historizing.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.Historizing.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.Historizing, historizing).getCode();
	}

	/**
	 * Write the MinimumSamplingInterval attribute.
	 * 
	 * @param Context                 Write context.
	 * @param MinimumSamplingInterval Double to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteMinimumSamplingInterval(Node node, Double minimumSamplingInterval, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.MinimumSamplingInterval.getValue()) == 0
				&& tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.MinimumSamplingInterval.getValue()) == 0
				&& tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.MinimumSamplingInterval, minimumSamplingInterval).getCode();
	}

	/**
	 * Write the ArrayDimension attribute.
	 * 
	 * @param Context         Write context.
	 * @param ArrayDimensions UnsignedInteger[] to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteArrayDimension(Node node, UnsignedInteger[] arrayDimensions, Long tagstate) {
		if ((node.getWriteMask().longValue() & AttributeWriteMask.ArrayDimensions.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.ArrayDimensions.getValue()) == 0
				&& tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.ArrayDimensions, arrayDimensions).getCode();
	}

	/**
	 * Write the ValueRank attribute.
	 * 
	 * @param Context   Write context.
	 * @param ValueRank Integer to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteValueRank(Node node, Integer valueRank, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.ValueRank.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.ValueRank.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.ValueRank, valueRank).getCode();
	}

	/**
	 * Write the DataType attribute.
	 * 
	 * @param Context  Write context.
	 * @param DataType NodeId to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteDataType(Node node, NodeId dataType, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.DataType.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.DataType.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.DataType, dataType).getCode();
	}

	/**
	 * Write the EventNotifier attribute.
	 * 
	 * @param Context       Write context.
	 * @param EventNotifier UnsignedByte to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteEventNotifier(Node node, UnsignedByte eventNotifier, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.EventNotifier.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.EventNotifier.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.EventNotifier, eventNotifier).getCode();
	}

	/**
	 * Write the ContainsNoLoops attribute.
	 * 
	 * @param Context         Write context.
	 * @param ContainsNoLoops Boolean to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteContainsNoLoops(Node node, Boolean containsNoLoops, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.ContainsNoLoops.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.ContainsNoLoops.getValue()) == 0
				&& tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.ContainsNoLoops, containsNoLoops).getCode();
	}

	/**
	 * Write the InverseName attribute.
	 * 
	 * @param Context     Write context.
	 * @param InverseName LocalizedText to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteInverseName(Node node, LocalizedText inverseName, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.InverseName.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.InverseName.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.InverseName, inverseName).getCode();
	}

	/**
	 * Write the Symmetric attribute.
	 * 
	 * @param Context   Write context.
	 * @param Symmetric Symmetric to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteSymmetric(Node node, Boolean symmetric, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.Symmetric.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.Symmetric.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.Symmetric, symmetric).getCode();
	}

	/**
	 * Write the IsAbstract attribute.
	 * 
	 * @param Context    Write context.
	 * @param IsAbstract Boolean to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteIsAbstract(Node node, Boolean isAbstract, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.IsAbstract.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.IsAbstract.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.IsAbstract, isAbstract).getCode();
	}

	/**
	 * Write the UserWriteMask attribute.
	 * 
	 * @param Context       Write context.
	 * @param UserWriteMask UnsignedInteger to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteUserWriteMask(Node node, UnsignedInteger userWriteMask, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.UserWriteMask.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.UserWriteMask.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.UserWriteMask, userWriteMask).getCode();
	}

	/**
	 * Write the WriteMask attribute.
	 * 
	 * @param Context   Write context.
	 * @param WriteMask UnsignedInteger to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteWriteMask(Node node, UnsignedInteger writeMask, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.WriteMask.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.WriteMask.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.WriteMask, writeMask).getCode();
	}

	/**
	 * Write the Description attribute.
	 * 
	 * @param Context     Write context.
	 * @param Description LocalizedText to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteDescription(Node node, LocalizedText description, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.Description.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.Description.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.Description, description).getCode();
	}

	/**
	 * Write the DisplayName attribute.
	 * 
	 * @param Context     Write context.
	 * @param DisplayName LocalizedText to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteDisplayName(Node node, LocalizedText displayName, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.DisplayName.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.DisplayName.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.DisplayName, displayName).getCode();
	}

	/**
	 * Write the BrowseName attribute.
	 * 
	 * @param Context    Write context.
	 * @param BrowseName QualifiedName to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteBrowseName(Node node, QualifiedName browseName, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.BrowseName.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.BrowseName.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.BrowseName, browseName).getCode();
	}

	/**
	 * Write the NodeClass attribute.
	 * 
	 * @param Context   Write context.
	 * @param NodeClass NodeClass to write.
	 * @return StatusCode result.
	 */
	private StatusCode onWriteNodeClass(Node node, NodeClass nodeClass, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.NodeClass.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.NodeClass.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.NodeClass, nodeClass).getCode();
	}

	/**
	 * Write the NodeId attribute.
	 * 
	 * @param node
	 * 
	 * @param Context Write context.
	 * @param NodeId  The node id to write
	 * @return StatusCode result.
	 */
	private StatusCode onWriteNodeId(Node node, NodeId nodeId, Long tagstate) {
		if ((node.getWriteMask().intValue() & AttributeWriteMask.NodeId.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_NotWritable);
		}
		if ((node.getUserWriteMask().intValue() & AttributeWriteMask.NodeId.getValue()) == 0 && tagstate != -1) {
			return new StatusCode(StatusCodes.Bad_UserAccessDenied);
		}
		return node.write(Attributes.NodeId, nodeId).getCode();
	}

	/**
	 * Writes a value attribute.
	 * 
	 * @param lastValue
	 * 
	 * @param typeTable
	 * @param namespaceUris
	 * 
	 * @param Context           The Context to write.
	 * @param IndexRange        IndexRange from the value to write.
	 * @param Value             The value to write.
	 * @param BypassAccessLevel Skips the AccessLevel from the Node to write.
	 * @return ServiceResult from the write operation.
	 */
	private StatusCode writeValueAttribute(final Node node, NumericRange indexRange, final DataValue value,
			DataValue destinationValue, DataValue lastValue, boolean bypassAccessLevel) {
		// check the access level for the variable node
		if (!bypassAccessLevel && node instanceof VariableNode) {
			EnumSet<AccessLevel> accessLevels = AccessLevel
					.getSet(new UnsignedInteger(((VariableNode) node).getAccessLevel()));
			if (!accessLevels.contains(AccessLevel.CurrentWrite)) {
				return new StatusCode(StatusCodes.Bad_NotWritable);
			}
			EnumSet<AccessLevel> userAccessLevels = AccessLevel
					.getSet(new UnsignedInteger(((VariableNode) node).getUserAccessLevel()));
			if (!userAccessLevels.contains(AccessLevel.CurrentWrite)) {
				return new StatusCode(StatusCodes.Bad_NotWritable);
			}
		}
		// ensure a valid datatype
		TypeInfo typeInfo = null;
		if (node instanceof VariableNode) {
			typeInfo = TypeInfo.isInstanceOfDataType(value, ((VariableNode) node).getDataType(),
					((VariableNode) node).getValueRank(), getNamespaceUris(), getTypeTable());
		} else if (node instanceof VariableTypeNode) {
			typeInfo = TypeInfo.isInstanceOfDataType(value, ((VariableTypeNode) node).getDataType(),
					((VariableTypeNode) node).getValueRank(), getNamespaceUris(), getTypeTable());
		}
		if (typeInfo == null || TypeInfo.Unknown == typeInfo) {
			return new StatusCode(StatusCodes.Bad_TypeMismatch);
		}
		// clone array to change its references. (Simple values do have their
		// own references)
		if (lastValue != null && !lastValue.isNull() && lastValue.getValue().isArray()) {
			Object array1 = lastValue.getValue().getValue();
			Object array2 = null;
			if (typeInfo.getBuiltInsType() == BuiltinType.ByteString) {
				// Class<?> componentType =
				// MultiDimensionArrayUtils.getComponentType(array1.getClass());
				// int dims[] = lastValue.getValue().getArrayDimensions();
				// Object cloned = Array.newInstance(componentType, dims);
				// System.arraycopy(array1, 0, cloned, 0, dims.length);
				/** TODO: byte[...] Array copy */
				array2 = array1;
			} else {
				array2 = MultiDimensionArrayUtils.clone(array1);
			}
			lastValue.setValue(new Variant(array2));
		}
		// ensure a valid index range
		if (indexRange != null && !indexRange.isEmpty()) {
			// copy array values to remember last value
			ServiceResult sr = updateIndexRangeValue(indexRange, value, destinationValue, getTypeTable());
			// bad index range
			if (sr != null && sr.isBad()) {
				return sr.getCode();
			}
		}
		StatusCode result = null;
		if (node instanceof UAVariableNode) {
			result = ((UAVariableNode) node).writeValue(Attributes.Value, value).getCode();
			// stores historizing values
			if (((UAServerVariableNode) node).getHistorizing()) {
				HistoryItemsUpdate ha = new HistoryItemsUpdate((UAVariableNode) node);
				this.historyExecutor.execute(ha);
			}
		} else if (node instanceof UAVariableTypeNode) {
			result = ((UAVariableTypeNode) node).writeValue(Attributes.Value, value).getCode();
		} else {
			result = node.write(Attributes.Value, value.getValue().getValue()).getCode();
		}
		return result;
	}

	class HistoryItemsUpdate implements Runnable {
		private UAVariableNode node;
		private DataValue datavalue;

		public HistoryItemsUpdate(UAVariableNode node) {
			this.node = node;
			DataValue value = new DataValue();
			this.node.readAttributeValue(Attributes.Value, value, null);
			this.datavalue = value;
		}

		@Override
		public void run() {
			((UAServerVariableNode) this.node).storeHistoryValue(this.datavalue);
		}
	}

	/**
	 * Bunch monitored updates if waiting for lock
	 * 
	 * @author Thomas Z�chbauer
	 * 
	 */
	class MonitoredItemsUpdate implements Runnable {
		private IMonitoredItem[] items;
		private UnsignedInteger attributeId;
		// private DataValue lastValue;
		private DataValue value;

		public MonitoredItemsUpdate(IMonitoredItem[] items, UnsignedInteger attributeId,
				DataValue value/*
								 * , DataValue lastValue
								 */) {
			this.items = items;
			this.attributeId = attributeId;
			this.value = value;
			// this.lastValue = lastValue;
		}

		@Override
		public void run() {
			getServer().getSubscriptionManager().updateMonitoredItems(items, attributeId, value/* , lastValue */);
		}
	}

	/**
	 * Updates a value with an index range for a write service
	 * 
	 * @param indexRange index range for src values
	 * @param srcValue   Values to write with the index range
	 * @param dstValue   Value to write into node
	 * @param typeTree
	 * @return
	 */
	private ServiceResult updateIndexRangeValue(NumericRange indexR, DataValue srcValue, DataValue dstValue,
			TypeTable typeTree) {
		opc.sdk.core.utils.NumericRange indexRange = (opc.sdk.core.utils.NumericRange) indexR;
		if (srcValue == null || srcValue.isNull() || srcValue.getValue().getValue() == null) {
			return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
		}
		TypeInfo srcTypeInfo = TypeInfo.construct(srcValue, typeTree);
		TypeInfo dstTypeInfo = TypeInfo.construct(dstValue, typeTree);
		// check for string or subset of strings (valid scalars to write an
		// index)
		if (dstTypeInfo.getValueRank() == ValueRanks.Scalar.getValue()) {
			if (indexRange.getBegin() == -1 && indexRange.getEnd() == -1) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
			}
			// check for subset of string
			if (dstTypeInfo.getBuiltInsType() == BuiltinType.String) {
				String srcString = null;
				if (srcTypeInfo.getBuiltInsType() == BuiltinType.String) {
					srcString = (String) srcValue.getValue().getValue();
				} else if (srcTypeInfo.getBuiltInsType() == BuiltinType.ByteString) {
					byte[] byteString = (byte[]) srcValue.getValue().getValue();
					srcString = new String(byteString);
				}
				char[] dstString = ((String) dstValue.getValue().getValue()).toCharArray();
				if ((srcString == null || srcString.isEmpty()) || srcString.length() != indexRange.count()) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
				}
				if (indexRange.getBegin() >= dstString.length
						|| (indexRange.getEnd() > 0 && indexRange.getEnd() >= dstString.length)) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
				}
				for (int i = 0; i < srcString.length(); i++) {
					dstString[indexRange.getBegin() + i] = srcString.charAt(i);
				}
				srcValue.setValue(new Variant(new String(dstString)));
				return null;
			}
			// update elements within a bytestring
			else if (dstTypeInfo.getBuiltInsType() == BuiltinType.ByteString) {
				byte[] srcString = (byte[]) srcValue.getValue().getValue();
				byte[] dstString = (byte[]) dstValue.getValue().getValue();
				if (srcString == null || srcString.length != indexRange.count()) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
				}
				if (indexRange.getBegin() >= dstString.length
						|| (indexRange.getEnd() > 0 && indexRange.getEnd() >= dstString.length)) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
				}
				for (int i = 0; i < srcString.length; i++) {
					dstString[indexRange.getBegin() + i] = srcString[i];
				}
				srcValue.setValue(new Variant(dstString));
				return null;
			}
			// not supported
			return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
		}
		// check invalid array target
		if (dstValue == null || dstValue.getValue() == null || dstValue.getValue().getValue() == null) {
			return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
		}
		Object srcArray = srcValue.getValue().getValue();
		Object dstArray = dstValue.getValue().getValue();
		int srcDim = MultiDimensionArrayUtils.getDimension(srcArray);
		// handle matrix
		if (srcDim > 1) {
			int dims = MultiDimensionArrayUtils.getDimension(srcValue.getValue().getValue());
			if (dims != srcDim) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
			}
		}
		if (srcTypeInfo.getBuiltInsType() != dstTypeInfo.getBuiltInsType()
				&& dstTypeInfo.getBuiltInsType() != BuiltinType.Variant) {
			return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
		}
		if (srcTypeInfo.getValueRank() != dstTypeInfo.getValueRank()) {
			return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
		}
		srcDim = srcValue.getValue().getDimension();
		// handle one dimension
		if (srcDim == 1) {
			if (dstTypeInfo.getValueRank() > 1) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
			}
			// reversed methods
			if (indexRange.getBegin() >= ((Object[]) dstArray).length
					|| ((indexRange.getEnd() > 0 && indexRange.getEnd() >= ((Object[]) dstArray).length))) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
			}
			// reversed methods
			if (((Object[]) srcArray).length != indexRange.count()) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
			}
			for (int i = 0; i < ((Object[]) srcArray).length; i++) {
				((Object[]) dstArray)[indexRange.getBegin() + i] = ((Object[]) srcArray)[i];
			}
			srcValue.setValue(new Variant(dstArray));
			return null;
		}
		NumericRange finalRange = null;
		// check for matching dimensions
		if (indexRange.getSubranges() != null && indexRange.getSubranges().length > srcTypeInfo.getValueRank()) {
			if (srcTypeInfo.getBuiltInsType() == BuiltinType.ByteString
					|| srcTypeInfo.getBuiltInsType() == BuiltinType.String) {
				if (indexRange.getSubranges().length == srcTypeInfo.getValueRank() + 1) {
					finalRange = indexRange.getSubranges()[indexRange.getSubranges().length - 1];
				}
			}
			if (finalRange == null) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
			}
		}
		// getting the arrays being copied
		int srcCount = 1;
		int[] dimensions = new int[srcTypeInfo.getValueRank()];
		for (int i = 0; i < dimensions.length; i++) {
			if (indexRange.getSubranges().length < i && indexRange.getSubranges()[i].count() != MultiDimensionArrayUtils
					.getLength(srcValue.getValue().getArrayDimensions())) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
			}
			dimensions[i] = MultiDimensionArrayUtils.getLength(srcValue.getValue().getArrayDimensions());
			srcCount *= dimensions[i];
		}
		// check that the index range fails with the target array
		int[] dstIndexes = new int[dimensions.length];
		for (int i = 0; i < srcCount; i++) {
			int divisor = srcCount;
			for (int j = 0; j < dimensions.length; j++) {
				divisor /= dimensions[i];
				int index = (i / divisor) % dimensions[j];
				int start = 0;
				if (indexRange.getSubranges().length > j) {
					start = indexRange.getSubranges()[j].getBegin();
				}
				if (start + index >= MultiDimensionArrayUtils.getLength(dstValue.getValue().getArrayDimensions())) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
				}
				dstIndexes[j] = start + index;
			}
			if (finalRange == null) {
				continue;
			}
			int last = finalRange.getBegin();
			if (finalRange.getEnd() > 0) {
				last = finalRange.getEnd();
			}
			// set element
			Object element = null;
			// subset of string
			if (dstTypeInfo.getBuiltInsType() == BuiltinType.String) {
				String str = (String) element;
				if (str == null || last >= str.length()) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
				}
			}
			// subset of bytestring
			else if (dstTypeInfo.getBuiltInsType() == BuiltinType.ByteString) {
				byte[] str = (byte[]) element;
				if (str == null || last >= str.length) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
				}
			}
		}
		// copy data
		int[] srcIndexes = new int[dimensions.length];
		for (int i = 0; i < srcCount; i++) {
			int divisor = srcCount;
			for (int j = 0; j < dimensions.length; j++) {
				divisor /= dimensions[j];
				int index = (i / divisor) & dimensions[j];
				int start = 0;
				if (indexRange.getSubranges().length > j) {
					start = indexRange.getSubranges()[j].getBegin();
				}
				if (start + index >= MultiDimensionArrayUtils.getArrayLengths(dstArray)[j]) {
					return new ServiceResult(StatusCodes.Bad_IndexRangeNoData);
				}
				srcIndexes[j] = index;
				dstIndexes[j] = start + index;
			}
			// get the element to copy
			Object element1 = MultiDimensionArrayUtils.muxArray(srcArray, srcIndexes);
			if (finalRange == null) {
				// setvalue on dstarray with element 1 on dstindexes
				continue;
			}
			Object element2 = MultiDimensionArrayUtils.muxArray(dstArray, dstIndexes);
			// update elements within a string
			if (dstTypeInfo.getBuiltInsType() == BuiltinType.String) {
				String srcString = (String) element1;
				char[] dstString = ((String) element2).toCharArray();
				if (srcString != null) {
					for (int j = 0; j < srcString.length(); j++) {
						dstString[finalRange.getBegin() + j] = srcString.charAt(j);
					}
				}
				// dstArray.setValue(new String(dstString), dstIndexes);
			}
			// update elemens within a byte string
			else if (dstTypeInfo.getBuiltInsType() == BuiltinType.ByteString) {
				byte[] srcString = (byte[]) element1;
				byte[] dstString = (byte[]) element2;
				if (srcString != null) {
					for (int j = 0; j < srcString.length; j++) {
						dstString[finalRange.getBegin() + j] = srcString[j];
					}
				}
			}
		}
		srcValue.setValue(new Variant(dstArray));
		return null;
	}

	protected void setUserAuthorityManager(OPCUserAuthentificationManager userAuthentifiationManager) {
		this.userAuthentification = userAuthentifiationManager;
	}

	public OPCUserAuthentificationManager getUserAuthentificationManager() {
		return this.userAuthentification;
	}

	@Override
	public boolean isInitialized() {
		return this.hasInitialized;
	}
}
