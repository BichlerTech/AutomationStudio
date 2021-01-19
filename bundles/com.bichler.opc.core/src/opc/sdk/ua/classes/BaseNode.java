package opc.sdk.ua.classes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.DecodingException;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.classes.ua.NodeStateFactory;
import opc.sdk.core.classes.ua.NodeTable;
import opc.sdk.core.context.INodeIdFactory;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.node.ViewNode;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.NodeStateChangeMasks;

/**
 * The base class for custom nodes.
 * 
 * @author Arbeit
 * 
 */
public abstract class BaseNode {
	private Object handle = null;
	private String symbolicName = null;
	private NodeId nodeId = null;
	private NodeClass nodeClass = null;
	private QualifiedName browseName = null;
	private LocalizedText displayName = null;
	private LocalizedText description = null;
	private List<BaseInstance> children = null;
	private List<ReferenceNode> references = null;
	private AttributeWriteMask userWriteMask = null;
	private AttributeWriteMask writeMask = null;
	private NodeId typeDefinitionId = null;
	private NodeStateChangeMasks changeMasks = null;

	public BaseNode(NodeClass nodeClass) {
		this.nodeClass = nodeClass;
		this.changeMasks = NodeStateChangeMasks.NONE;
	}

	/**
	 * Adds a child to the node.
	 * 
	 * @param replacement
	 */
	public void addChild(BaseInstance child) {
		if (child.getParent() != this) {
			child.setParent(this);
			if (NodeId.isNull(child.getReferenceTypeId())) {
				child.setReferenceTypeId(Identifiers.HasComponent);
			}
		}
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		this.children.add(child);
		setChangeMask(NodeStateChangeMasks.CHILDREN);
	}

	/**
	 * Adds a reference.
	 * 
	 * @param referenceTypeId
	 * @param isInverse
	 * @param targetId
	 */
	public void addReference(NodeId referenceTypeId, Boolean isInverse, ExpandedNodeId targetId) {
		if (NodeId.isNull(referenceTypeId))
			throw new IllegalArgumentException("referenceTypeId");
		if (ExpandedNodeId.isNull(targetId))
			throw new IllegalArgumentException("targetId");
		if (this.references == null) {
			this.references = new ArrayList<>();
		}
		this.references.add(new ReferenceNode(referenceTypeId, isInverse, targetId));
		setChangeMask(NodeStateChangeMasks.REFERENCES);
	}

	/**
	 * Recursively assigns NodeIds to the node and its children.
	 * 
	 * @param context
	 * @param mappingTable
	 */
	public void assignNodeIds(INodeIdFactory nodeIdFactory, Map<NodeId, NodeId> mappingTable) {
		List<BaseInstance> tmpChildren = getChildren();
		assignNodeIds(nodeIdFactory, tmpChildren, mappingTable);
	}

	/**
	 * Clears the change masks.
	 * 
	 * @param context
	 * @param includeChildren
	 */
	public void clearChangeMasks(boolean includeChildren) {
		if (includeChildren) {
			List<BaseInstance> tmpChildren = getChildren();
			if (tmpChildren != null)
				for (int i = 0; i < tmpChildren.size(); i++) {
					tmpChildren.get(i).clearChangeMasks(true);
				}
		}
	}

	public void create(IOPCContext context, NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			throw new IllegalArgumentException("NodeId_Null not allowed!");
		}
		this.nodeId = nodeId;
		initializeModel(context, this);
		clearChangeMasks(true);
	}

	private void initializeModel(IOPCContext context, BaseNode nodeState) {
		// nodeState.initialize(context);
		nodeState.initializeChildren(context);
		Class<?> baseClass = nodeState.getClass();
		List<Field> fields = new ArrayList<>();
		while (true) {
			if (baseClass == BaseObjectType.class || baseClass == BaseVariableType.class
					|| baseClass == BaseMethod.class) {
				break;
			}
			Field[] fields2add = baseClass.getDeclaredFields();
			for (Field field : fields2add) {
				int modifier = field.getModifiers();
				// IMPORTANT
				if (Modifier.isProtected(modifier)) {
					fields.add(field);
				}
			}
			baseClass = baseClass.getSuperclass();
		}
		ReferenceDescription[] refs = context.browse(nodeState.getNodeId(), BrowseDirection.Forward);
		// fill children
		List<BaseInstance> childrenNodes = nodeState.getChildren();
		for (int i = 0; i < childrenNodes.size(); i++) {
			BaseInstance child = childrenNodes.get(i);
			Field field = fields.get(i);
			NodeId nid = NodeId.NULL;
			for (ReferenceDescription desc : refs) {
				QualifiedName refName = desc.getBrowseName();
				String fieldName = field.getName();
				if (fieldName.equalsIgnoreCase(refName.getName())) {
					nid = context.expandedNodeIdToNodeId(desc.getNodeId());
					break;
				}
			}
			child.setNodeId(nid);
			initializeModel(context, child);
		}
	}

	/**
	 * Creates a node with default values and assigns new node ids to it and all
	 * children.
	 */
	public void create(ISystemContext context, NodeId nodeId, QualifiedName browseName, LocalizedText displayName,
			boolean assignNodeIds) {
		// initialize(null);
		// Call OnBeforeCreate on all children.
		/** callOnBeforeCreate(context); */
		// override node id.
		if (!NodeId.isNull(nodeId)) {
			this.nodeId = nodeId;
		}
		// set defaults for names.
		if (!QualifiedName.isNull(browseName)) {
			this.symbolicName = browseName.getName();
			this.browseName = browseName;
			this.displayName = new LocalizedText(this.browseName.getName(), "");
		}
		// override display name.
		if (displayName != null) {
			this.displayName = displayName;
		}
		// get all children.
		List<BaseInstance> tmpChildren = getChildren();
		if (assignNodeIds) {
			// Call CallOnAssignNodeIds on all children.
			callOnBeforeAssignNodeIds(tmpChildren);
			// assign the node ids.
			Map<NodeId, NodeId> mappingTable = new HashMap<>();
			assignNodeIds(context.getNodeIdFactory(), tmpChildren, mappingTable);
			// update the reference targets.
			updateReferenceTargets(context, tmpChildren, mappingTable);
		}
		// Call OnAfterCreate on all children.
		/** callOnAfterCreate(context, children); */
		clearChangeMasks(true);
	}

	/**
	 * Finds or creates the child with the specified browse name.
	 * 
	 * @param context
	 * @param browseName
	 * @return
	 */
	public BaseInstance createChild(QualifiedName browseName) {
		if (browseName == null || QualifiedName.isNull(browseName)) {
			return null;
		}
		return findChild(browseName, true, null);
	}

	public BaseInstance findChild(QualifiedName browseName) {
		return findChild(browseName, false, null);
	}

	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (browseName == null) {
			return null;
		}
		if (this.children != null) {
			for (int ii = 0; ii < children.size(); ii++) {
				BaseInstance child = children.get(ii);
				if (browseName.equals(child.getBrowseName())) {
					if (createOrReplace && replacement != null) {
						child = replacement;
						BaseInstance element = child;
						children.add(ii, element);
					}
					return child;
				}
			}
		}
		if (createOrReplace && replacement != null) {
			addChild(replacement);
		}
		return null;
	}

	/**
	 * Populates a list with the children that belong to the node.
	 * 
	 * @return children
	 */
	public List<BaseInstance> getChildren() {
		if (this.children != null) {
			List<BaseInstance> tmpChildren = new ArrayList<>();
			for (int i = 0; i < this.children.size(); i++) {
				tmpChildren.add(this.children.get(i));
			}
			return tmpChildren;
		}
		return new ArrayList<>();
	}

	/**
	 * Populates a list with the non-child related references that belong to the
	 * node.
	 * 
	 * @return references
	 */
	public List<ReferenceNode> getReferences() {
		List<ReferenceNode> refs = new ArrayList<>();
		if (this.references != null) {
			for (ReferenceNode rn : this.references) {
				refs.add(rn);
			}
		}
		return refs;
	}

	/**
	 * Adds a child from the node.
	 * 
	 * @param instance
	 */
	public void removeChild(BaseInstance child) {
		if (this.children != null) {
			for (int i = 0; i < this.children.size(); i++) {
				if (this.children.get(i) == child) {
					child.setParent(null);
					this.children.remove(i);
					setChangeMask(NodeStateChangeMasks.CHILDREN);
					return;
				}
			}
		}
	}

	/**
	 * Removes a reference.
	 * 
	 * @param ReferenceTypeId Type of the reference.
	 * @param IsInverse       Reference is inverse.
	 * @param TargetId        Target of the reference.
	 * @return TRUE if the reference has deleted, otherwise FALSE.
	 */
	public boolean removeReference(NodeId referenceTypeId, boolean isInverse, ExpandedNodeId targetId) {
		if (NodeId.isNull(referenceTypeId)) {
			throw new IllegalArgumentException("ReferenceTypeId RemoveReference");
		}
		if (ExpandedNodeId.isNull(targetId)) {
			throw new IllegalArgumentException("TargetId RemoveReference");
		}
		if (this.references == null || this.references.isEmpty()) {
			return false;
		}
		ReferenceNode refernceToDelete = new ReferenceNode(referenceTypeId, isInverse, targetId);
		for (ReferenceNode reference : this.references) {
			if (reference.equals(refernceToDelete)) {
				refernceToDelete = reference;
				break;
			}
		}
		boolean removed = this.references.remove(refernceToDelete);
		if (removed) {
			setChangeMask(NodeStateChangeMasks.REFERENCES);
		}
		return removed;
	}

	/**
	 * When overridden in a derived class, iinitializes the instance with the
	 * default values.
	 * 
	 * @param context
	 */
	protected abstract void initialize(IOPCContext context);

	/**
	 * When overridden in a derived class, initializes the any option children
	 * defined for the instance.
	 * 
	 * @param context
	 */
	protected abstract void initializeChildren(IOPCContext context);

	/**
	 * Recursively assigns NodeIds to the node and its children.
	 * 
	 * @param nodeIdFactory
	 * 
	 * @param context
	 * @param children
	 * @param mappingTable
	 */
	private void assignNodeIds(INodeIdFactory nodeIdFactory, List<BaseInstance> children,
			Map<NodeId, NodeId> mappingTable) {
		if (nodeIdFactory == null) {
			return;
		}
		// update id for instance.
		NodeId oldId = getNodeId();
		NodeId newId = null;
		if (!NodeId.isNull(oldId)) {
			NodeId storedId = mappingTable.get(oldId);
			mappingTable.remove(oldId);
			mappingTable.put(newId, storedId);
		}
		setNodeId(newId);
		// update id for children.
		if (children != null)
			for (int ii = 0; ii < children.size(); ii++) {
				children.get(ii).assignNodeIds(nodeIdFactory, mappingTable);
			}
	}

	/**
	 * Recusivesly calls OnAfterCreate for the node and its children.
	 * 
	 * @param context
	 * @param tmpChildren
	 */
	protected void callOnAfterCreate(ISystemContext context, List<BaseInstance> dhildren) {
		List<BaseInstance> tmpChildren = children;
		if (tmpChildren == null) {
			tmpChildren = getChildren();
		}
		for (int ii = 0; ii < tmpChildren.size(); ii++) {
			tmpChildren.get(ii).callOnAfterCreate(context, null);
		}
		onAfterCreate(context, this);
	}

	/**
	 * Recusivesly calls OnBeforeCreate for the node and its children.
	 * 
	 * @param context
	 * @param tmpParamChildren
	 */
	protected void callOnBeforeAssignNodeIds(List<BaseInstance> paramChildren) {
		List<BaseInstance> tmpParamChildren = paramChildren;
		onBeforeAssignNodeIds();
		if (tmpParamChildren == null) {
			tmpParamChildren = getChildren();
		}
		if (tmpParamChildren != null)
			for (int ii = 0; ii < tmpParamChildren.size(); ii++) {
				tmpParamChildren.get(ii).callOnBeforeAssignNodeIds(null);
			}
	}

	/**
	 * Recusivesly calls OnBeforeCreate for the node and its children.
	 * 
	 * @param context
	 */
	protected void callOnBeforeCreate(ISystemContext context) {
		onBeforeCreate(context, this);
		List<BaseInstance> tmpChildren = getChildren();
		for (int ii = 0; ii < tmpChildren.size(); ii++) {
			tmpChildren.get(ii).callOnBeforeCreate(context);
		}
	}

	/**
	 * Exports a copy of the node to a node table.
	 * 
	 * @param Context
	 * @param Node
	 */
	protected void export(ISystemContext context, Node node) {
		node.setNodeId(this.nodeId);
		node.setNodeClass(this.nodeClass);
		node.setBrowseName(this.browseName);
		node.setDisplayName(this.displayName);
		node.setDescription(this.description);
		if (this.writeMask == null) {
			node.setWriteMask(UnsignedInteger.ZERO);
		} else {
			node.setWriteMask(UnsignedInteger.getFromBits(this.writeMask.getValue()));
		}
		if (this.userWriteMask == null) {
			node.setUserWriteMask(UnsignedInteger.ZERO);
		} else {
			node.setUserWriteMask(UnsignedInteger.getFromBits(this.userWriteMask.getValue()));
		}
		if (node.getReferences() == null) {
			node.setReferences(new ReferenceNode[0]);
		}
	}

	/**
	 * Finds the child by a path constructed from the symbolic names.
	 * 
	 * @param Context
	 * @param ComponentPath
	 * 
	 * @return the matching child. Nulli fthe child was not found.
	 */
	public BaseNode findChildBySymbolicName(String symbolicPath) {
		// check for null
		if (symbolicPath == null || symbolicPath.isEmpty()) {
			return null;
		}
		// strip out leading slashes
		int start = 0;
		while (start < symbolicPath.length()) {
			if (symbolicPath.charAt(start) != '/') {
				break;
			}
			start++;
		}
		// check if nothing left in path
		if (start >= symbolicPath.length()) {
			return null;
		}
		// find next slash
		int end = start + 1;
		while (end < symbolicPath.length()) {
			if (symbolicPath.charAt(end) == '/') {
				break;
			}
			end++;
		}
		// extract the symbolic name for the top level
		String sName = symbolicPath;
		if (start > 0 || end < symbolicPath.length()) {
			sName = symbolicPath.substring(start, end - start);
		}
		// find the top level chidl
		List<BaseInstance> tmpChildren = getChildren();
		for (int i = 0; i < tmpChildren.size(); i++) {
			BaseInstance child = tmpChildren.get(i);
			if (child.getSymbolicName().equals(sName)) {
				// check if additional path elements remain
				if (end < symbolicPath.length() - 1) {
					return child.findChildBySymbolicName(symbolicPath.substring(end + 1));
				}
				return child;
			}
		}
		return null;
	}

	/**
	 * Initializes the instance with the default values.
	 * 
	 * @param context
	 * @param source
	 */
	protected void initialize(ISystemContext context, BaseNode source) {
		this.handle = source.getHandle();
		this.symbolicName = source.getSymbolicName();
		this.nodeId = source.getNodeId();
		this.nodeClass = source.getNodeClass();
		this.browseName = source.getBrowseName();
		this.displayName = source.getDisplayName();
		this.description = source.getDescription();
		this.writeMask = source.getWriteMask();
		this.children = null;
		this.references = null;
		this.changeMasks = NodeStateChangeMasks.NONE;
		// set the initialization flags.
		List<BaseInstance> tmpChildren = source.getChildren();
		for (int ii = 0; ii < tmpChildren.size(); ii++) {
			BaseInstance sourceChild = tmpChildren.get(ii);
			BaseInstance child = createChild(sourceChild.getBrowseName());
			if (child == null) {
				try {
					child = (BaseInstance) sourceChild.clone();
				} catch (CloneNotSupportedException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				addChild(child);
			}
			if (child == null)
				throw new NullPointerException();
			child.initialize(context, sourceChild);
		}
		List<ReferenceNode> tmpcReferences = source.getReferences();
		for (int ii = 0; ii < tmpcReferences.size(); ii++) {
			ReferenceNode reference = tmpcReferences.get(ii);
			addReference(reference.getReferenceTypeId(), reference.getIsInverse(), reference.getTargetId());
		}
	}

	/**
	 * Returns true if the reference exists.
	 * 
	 * @param ReferenceTypeId The type of the reference.
	 * @param IsInverse       The direction of the reference.
	 * @param TargetId        The target of the reference.
	 * @return TRUE if the reference exists, otherwise FALSE.
	 */
	public boolean referenceExists(NodeId referenceTypeId, Boolean isInverse, ExpandedNodeId targetId) {
		if (this.references == null || NodeId.isNull(referenceTypeId) || ExpandedNodeId.isNull(targetId)) {
			return false;
		}
		ReferenceNode referenceToCheck = new ReferenceNode();
		for (ReferenceNode reference : this.references) {
			if (reference.equals(referenceToCheck)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the object from a binary stream.
	 * 
	 * @param Context
	 * @param Decoder
	 * @param AttributesToLoad
	 * @throws DecodingException
	 */
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		if (attributesToLoad.contains(AttributesToSave.NodeClass)) {
			this.nodeClass = decoder.getEnumeration(null, NodeClass.class);
		}
		if (attributesToLoad.contains(AttributesToSave.SymbolicName)) {
			this.symbolicName = decoder.getString(null);
		}
		if (attributesToLoad.contains(AttributesToSave.SymbolicName)) {
			this.symbolicName = decoder.getString(null);
		}
		if (attributesToLoad.contains(AttributesToSave.BrowseName)) {
			this.browseName = decoder.getQualifiedName(null);
		}
		if (attributesToLoad.contains(AttributesToSave.NodeId)) {
			this.nodeId = decoder.getNodeId(null);
		}
		if (attributesToLoad.contains(AttributesToSave.DisplayName)) {
			this.displayName = decoder.getLocalizedText(null);
		}
		if (this.displayName == null || LocalizedText.EMPTY.equals(this.displayName) && this.browseName != null) {
			this.displayName = new LocalizedText(this.browseName.getName(), "");
		}
		if (attributesToLoad.contains(AttributesToSave.Description)) {
			this.description = decoder.getLocalizedText(null);
		}
		if (attributesToLoad.contains(AttributesToSave.WriteMask)) {
			this.writeMask = decoder.getEnumeration(null, AttributeWriteMask.class);
		}
		if (attributesToLoad.contains(AttributesToSave.UserWriteMask)) {
			this.userWriteMask = decoder.getEnumeration(null, AttributeWriteMask.class);
		}
	}

	/**
	 * Sets the specified bits in the change masks (ORs with the current bits).
	 * 
	 * @param changeMasks
	 */
	public void updateChangeMasks(NodeStateChangeMasks changeMasks) {
	}

	/**
	 * Loads the children from a binary stream.
	 * 
	 * @param context
	 * @param decoder
	 * @throws ServiceResultException
	 */
	public void updateChildren(ISystemContext context, BinaryImporterDecoder decoder) throws ServiceResultException {
		int count = decoder.getInt32(null);
		for (int i = 0; i < count; i++) {
			updateChild(context, decoder);
		}
	}

	/**
	 * Reads attributes for the next child found in the stream.
	 * 
	 * The child is created if it does not already exist. Recursively updates any
	 * children of the child.
	 * 
	 * @param context
	 * @param decoder
	 * @return
	 * @throws ServiceResultException
	 */
	private BaseInstance updateChild(ISystemContext context, BinaryImporterDecoder decoder)
			throws ServiceResultException {
		EnumSet<AttributesToSave> attributesToLoad = AttributesToSave.getSet(decoder.getUInt32(null).intValue());
		NodeClass tmpNodeClass;
		String tmpSymbolicName = null;
		QualifiedName tmpBrowseName = null;
		tmpNodeClass = decoder.getEnumeration(null, NodeClass.class);
		attributesToLoad.remove(AttributesToSave.NodeClass);
		if (attributesToLoad.contains(AttributesToSave.SymbolicName)) {
			tmpSymbolicName = decoder.getString(null);
			attributesToLoad.remove(AttributesToSave.SymbolicName);
		}
		if (attributesToLoad.contains(AttributesToSave.BrowseName)) {
			tmpBrowseName = decoder.getQualifiedName(null);
			attributesToLoad.remove(AttributesToSave.BrowseName);
		}
		if ((tmpSymbolicName == null || tmpSymbolicName.isEmpty()) && !QualifiedName.isNull(tmpBrowseName)) {
			if (tmpBrowseName == null)
				throw new NullPointerException("BrowseName is null!");
			tmpSymbolicName = tmpBrowseName.getName();
		}
		// check for the children defined by the type
		BaseInstance child = createChild(tmpBrowseName);
		if (child != null) {
			child.setSymbolicName(tmpSymbolicName);
			child.setBrowseName(tmpBrowseName);
			// update attributes
			child.update(context, decoder, attributesToLoad);
			// update any references
			child.updateReferences(decoder);
			// update any children
			child.updateChildren(context, decoder);
			return child;
		}
		// handle unknown child
		child = (BaseInstance) updateUnknownChild(context, decoder, this, attributesToLoad, tmpNodeClass,
				tmpSymbolicName, tmpBrowseName);
		// add the child
		if (child != null) {
			child.setBrowseName(tmpBrowseName);
			addChild(child);
		}
		return child;
	}

	public void updateReferences(BinaryImporterDecoder decoder) throws DecodingException {
		int count = decoder.getInt32(null);
		for (int i = 0; i < count; i++) {
			NodeId referenceTypeId = decoder.getNodeId(null);
			boolean isInverse = decoder.getBoolean(null);
			ExpandedNodeId targetId = decoder.getExpandedNodeId(null);
			if (this.references == null) {
				this.references = new ArrayList<>();
			}
			this.references.add(new ReferenceNode(referenceTypeId, isInverse, targetId));
		}
	}

	/**
	 * Recursively updates the targets of references.
	 * 
	 * @param context
	 * @param children
	 * @param mappingTable
	 */
	private void updateReferenceTargets(ISystemContext context, List<BaseInstance> children,
			Map<NodeId, NodeId> mappingTable) {
		NamespaceTable nsTable = context.getNamespaceUris();
		// check if there are references to update.
		if (this.references != null) {
			List<ReferenceNode> referencesToAdd = new ArrayList<>();
			List<ReferenceNode> referencesToRemove = new ArrayList<>();
			for (ReferenceNode reference : this.references) {
				// check for absolute id.
				NodeId oldId = null;
				try {
					oldId = context.getNamespaceUris().toNodeId(reference.getTargetId());
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				if (oldId == null) {
					continue;
				}
				// look up new node id.
				NodeId newId = mappingTable.get(oldId);
				if (newId != null) {
					referencesToRemove.add(reference);
					referencesToAdd.add(new ReferenceNode(reference.getReferenceTypeId(), reference.getIsInverse(),
							new ExpandedNodeId(nsTable.getUri(newId.getNamespaceIndex()), newId.getValue(), nsTable)));
				}
			}
			// remove old references.
			for (int ii = 0; ii < referencesToRemove.size(); ii++) {
				if (references.remove(referencesToRemove.get(ii))) {
					setChangeMask(NodeStateChangeMasks.REFERENCES);
				}
			}
			// add new references.
			for (int ii = 0; ii < referencesToAdd.size(); ii++) {
				references.add(referencesToAdd.get(ii));
				setChangeMask(NodeStateChangeMasks.REFERENCES);
			}
		}
		if (children != null)
			// recursively update targets for children.
			for (int ii = 0; ii < children.size(); ii++) {
				children.get(ii).updateReferenceTargets(context, mappingTable);
			}
	}

	/**
	 * Recursively updates the targets of references.
	 */
	protected void updateReferenceTargets(ISystemContext context, Map<NodeId, NodeId> mappingTable) {
		List<BaseInstance> tmpChildren = getChildren();
		updateReferenceTargets(context, tmpChildren, mappingTable);
	}

	/**
	 * Get the root node if the node is part of an instance hierarchy
	 * 
	 * @return root node
	 */
	public BaseNode getHierarchyRoot() {
		// only instance nodes can be part of a hierarchy.
		BaseInstance instance = null;
		if (this instanceof BaseInstance) {
			instance = (BaseInstance) this;
		}
		if (instance == null || instance.getParent() == null) {
			return this;
		}
		// find the root
		BaseNode root = instance.getParent();
		while (root != null) {
			if (root instanceof BaseInstance) {
				instance = (BaseInstance) root;
			}
			if (instance.getParent() == null) {
				return root;
			}
			root = instance.getParent();
		}
		return root;
	}

	public AttributeWriteMask getWriteMask() {
		return this.writeMask;
	}

	public LocalizedText getDescription() {
		return this.description;
	}

	public LocalizedText getDisplayName() {
		return this.displayName;
	}

	public QualifiedName getBrowseName() {
		return this.browseName;
	}

	public NodeClass getNodeClass() {
		return this.nodeClass;
	}

	public NodeId getNodeId() {
		return this.nodeId;
	}

	public String getSymbolicName() {
		return this.symbolicName;
	}

	public Object getHandle() {
		return this.handle;
	}

	public NodeStateChangeMasks getNodeStateChangeMask() {
		return this.changeMasks;
	}

	public void setBrowseName(QualifiedName browseName) {
		if (this.browseName != browseName) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.browseName = browseName;
	}

	public void setDescription(LocalizedText description) {
		if (this.description != description) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.description = description;
	}

	/**
	 * An arbitrary handle associated with the node
	 * 
	 * @param Handle Handle for the Node
	 */
	public void setHandle(Object handle) {
		this.handle = handle;
	}

	public void setDisplayName(LocalizedText displayName) {
		if (this.displayName != displayName) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.displayName = displayName;
	}

	public void setNodeId(NodeId nodeId) {
		if (this.nodeId != nodeId) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.nodeId = nodeId;
	}

	protected void setChangeMask(NodeStateChangeMasks changeMask) {
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public void setTypeDefinitionId(NodeId typeDefId) {
		if (typeDefinitionId != typeDefId) {
			setChangeMask(NodeStateChangeMasks.REFERENCES);
		}
		typeDefinitionId = typeDefId;
	}

	public NodeId getTypeDefinitionId() {
		return this.typeDefinitionId;
	}

	public void setUserWriteMask(AttributeWriteMask userWriteMask) {
		if (this.userWriteMask != userWriteMask) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.userWriteMask = userWriteMask;
	}

	public void setWriteMask(AttributeWriteMask value) {
		if (this.writeMask != value) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.writeMask = value;
	}

	/**
	 * Called before the ids are assigned to the node and its children.
	 */
	// defined by the sub-class.
	protected void onBeforeAssignNodeIds() {
	}

	/**
	 * <summary> Called after a node is created.
	 */
	// defined by the sub-class.
	protected void onAfterCreate(ISystemContext context, BaseNode node) {
	}

	/**
	 * Called before a node is created.
	 */
	// defined by the sub-class.
	protected void onBeforeCreate(ISystemContext context, BaseNode node) {
	}

	/**
	 * Exports a copy of the node to a node table.
	 * 
	 * @param Context
	 * @param NodesToExport
	 */
	public void export(ISystemContext context, NodeTable table) {
		Node node = null;
		switch (getNodeClass()) {
		case Object:
			node = new ObjectNode();
			break;
		case ObjectType:
			node = new ObjectTypeNode();
			break;
		case Variable:
			node = new VariableNode();
			break;
		case VariableType:
			node = new VariableTypeNode();
			break;
		case Method:
			node = new MethodNode();
			break;
		case DataType:
			node = new DataTypeNode();
			break;
		case ReferenceType:
			node = new ReferenceTypeNode();
			break;
		case View:
			node = new ViewNode();
			break;
		default:
			node = new Node();
			break;
		}
		export(context, node);
		List<ReferenceNode> tmpReferences = getReferences();
		List<ReferenceNode> refsList = Arrays.asList(node.getReferences());
		refsList = new ArrayList<>(refsList);
		for (int i = 0; i < tmpReferences.size(); i++) {
			refsList.add(tmpReferences.get(i));
		}
		node.setReferences(refsList.toArray(new ReferenceNode[0]));
		table.attach(node);
		List<BaseInstance> tmpChildren = getChildren();
		for (int i = 0; i < tmpChildren.size(); i++) {
			tmpChildren.get(i).export(context, table);
		}
	}

	public static BaseNode loadNode(ISystemContext context, BinaryImporterDecoder decoder)
			throws ServiceResultException {
		UnsignedInteger attributes = decoder.getUInt32(null);
		EnumSet<AttributesToSave> attributesToLoad = AttributesToSave.getSet(attributes.intValue());
		NodeClass nodeClass;
		String symbolicName = null;
		QualifiedName browseName = null;
		nodeClass = decoder.getEnumeration(null, NodeClass.class);
		attributesToLoad.remove(AttributesToSave.NodeClass);
		if (attributesToLoad.contains(AttributesToSave.SymbolicName)) {
			symbolicName = decoder.getString(null);
			attributesToLoad.remove(AttributesToSave.SymbolicName);
		}
		if (attributesToLoad.contains(AttributesToSave.BrowseName)) {
			browseName = decoder.getQualifiedName(null);
			attributesToLoad.remove(AttributesToSave.BrowseName);
		}
		if ((symbolicName == null || symbolicName.isEmpty()) && !QualifiedName.isNull(browseName)) {
			if (browseName == null) {
				throw new NullPointerException("BrowseName is null");
			}
			symbolicName = browseName.getName();
		}
		return loadUnknownNode(context, decoder, attributesToLoad, nodeClass, symbolicName, browseName);
	}

	/**
	 * Reads an unknown node from a stream.
	 * 
	 * @param context
	 * 
	 * @param decoder
	 * @param attributesToLoad
	 * @param nodeClass2
	 * @param symbolicName2
	 * @param browseName
	 * @return
	 * @throws ServiceResultException
	 */
	private static BaseNode loadUnknownNode(ISystemContext context, BinaryImporterDecoder decoder,
			EnumSet<AttributesToSave> attributesToLoad, NodeClass nodeClass, String symbolicName,
			QualifiedName browseName) throws ServiceResultException {
		// create the appropriate node
		switch (nodeClass) {
		case Variable:
		case Object:
		case Method:
			return updateUnknownChild(context, decoder, null, attributesToLoad, nodeClass, symbolicName, browseName);
		default:
			break;
		}
		// get the node factory
		NodeStateFactory factory = context.getNodeStateFactory();
		if (factory == null) {
			factory = new NodeStateFactory();
		}
		// create the appropriate node
		BaseNode child = factory.createInstance(context, null, nodeClass, null, null);
		if (child == null) {
			throw new ServiceResultException(StatusCodes.Bad_DecodingError);
		}
		// update symbolic name
		child.setSymbolicName(symbolicName);
		child.setBrowseName(browseName);
		// update attributes
		child.update(context, decoder, attributesToLoad);
		// update any references
		child.updateReferences(decoder);
		// update any children
		child.updateChildren(context, decoder);
		return child;
	}

	/**
	 * Updates a child which is not defined by the type definition.
	 * 
	 * @param context
	 * 
	 * @param decoder
	 * @param object
	 * @param attributesToLoad
	 * @param nodeClass2
	 * @param symbolicName2
	 * @param browseName
	 * @return
	 * @throws ServiceResultException
	 */
	private static BaseNode updateUnknownChild(ISystemContext context, BinaryImporterDecoder decoder, BaseNode parent,
			EnumSet<AttributesToSave> attributesToLoad, NodeClass nodeClass, String symbolicName,
			QualifiedName browseName) throws ServiceResultException {
		NodeId nodeId = null;
		LocalizedText displayName = null;
		LocalizedText description = null;
		AttributeWriteMask writeMask = AttributeWriteMask.None;
		AttributeWriteMask userWriteMask = AttributeWriteMask.None;
		NodeId referenceTypeId = null;
		NodeId typedefinition = null;
		if (attributesToLoad.contains(AttributesToSave.NodeId)) {
			nodeId = decoder.getNodeId(null);
			attributesToLoad.remove(AttributesToSave.NodeId);
		}
		if (attributesToLoad.contains(AttributesToSave.DisplayName)) {
			displayName = decoder.getLocalizedText(null);
			attributesToLoad.remove(AttributesToSave.DisplayName);
		}
		if ((displayName == null || displayName.getText().isEmpty()) && !QualifiedName.isNull(browseName)) {
			displayName = new LocalizedText(browseName.getName(), "");
		}
		if (attributesToLoad.contains(AttributesToSave.Description)) {
			description = decoder.getLocalizedText(null);
			attributesToLoad.remove(AttributesToSave.Description);
		}
		if (attributesToLoad.contains(AttributesToSave.WriteMask)) {
			writeMask = decoder.getEnumeration(null, AttributeWriteMask.class);
			attributesToLoad.remove(AttributesToSave.WriteMask);
		}
		if (attributesToLoad.contains(AttributesToSave.UserWriteMask)) {
			userWriteMask = decoder.getEnumeration(null, AttributeWriteMask.class);
			attributesToLoad.remove(AttributesToSave.UserWriteMask);
		}
		if (attributesToLoad.contains(AttributesToSave.ReferenceTypeId)) {
			referenceTypeId = decoder.getNodeId(null);
			attributesToLoad.remove(AttributesToSave.ReferenceTypeId);
		}
		if (attributesToLoad.contains(AttributesToSave.TypeDefinitionId)) {
			typedefinition = decoder.getNodeId(null);
			attributesToLoad.remove(AttributesToSave.TypeDefinitionId);
		}
		NodeStateFactory factory = context.getNodeStateFactory();
		if (factory == null) {
			factory = new NodeStateFactory();
		}
		// create the appropriate node
		BaseInstance child = (BaseInstance) factory.createInstance(context, parent, nodeClass, referenceTypeId,
				typedefinition);
		if (child == null) {
			throw new ServiceResultException(StatusCodes.Bad_DecodingError);
		}
		// initialize the child from the stream
		child.setSymbolicName(symbolicName);
		child.setNodeId(nodeId);
		child.setBrowseName(browseName);
		child.setDisplayName(displayName);
		child.setDescription(description);
		child.setWriteMask(writeMask);
		child.setUserWriteMask(userWriteMask);
		child.setReferenceTypeId(referenceTypeId);
		child.setTypeDefinitionId(typedefinition);
		// update attributes
		child.update(context, decoder, attributesToLoad);
		// update any references
		child.updateReferences(decoder);
		// update any children
		child.updateChildren(context, decoder);
		return child;
	}

	protected DateTime dateToOpc(long timestamp) {
		DateTime time = DateTime.fromMillis(timestamp);
		return time;
	}
}
