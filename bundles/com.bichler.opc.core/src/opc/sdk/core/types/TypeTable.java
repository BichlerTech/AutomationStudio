package opc.sdk.core.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.StatusCodes;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.mapper.NodeIdMapper;

/**
 * Typetable for the address space. - Namespace table - reference types (?) -
 * nodes (datatype, objecttype, variabletype, DataType) - encodings
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class TypeTable {
	/** lock */
	private Object lock = new Object();
	/** instance variables */
	private NamespaceTable namespaceUris = null;
	private Map<QualifiedName, TypeInfo> referenceTypes = null;
	private Map<ExpandedNodeId, TypeInfo> encoding = null;
	private Map<NodeId, TypeInfo> nodes = null;
	private Map<NodeId, TypeInfo> tmpNodes = null;
	private ComplexTypes complextypes = null;

	// complex
	/**
	 * Initializes the object with default values.
	 * 
	 * @param namespaceUris
	 */
	public TypeTable(NamespaceTable namespaceUris) {
		this.namespaceUris = namespaceUris;
		this.referenceTypes = new HashMap<>();
		// nodeid - type
		this.nodes = new HashMap<>();
		this.encoding = new HashMap<>();
		this.tmpNodes = new HashMap<>();
		this.complextypes = new ComplexTypes();
		initClasses();
	}

	private void initClasses() {
		this.complextypes.initialize();
	}

	public boolean addTypeNode(Node node) {
		// check if node is null
		if (node == null || NodeId.isNull(node.getNodeId())) {
			return false;
		}
		// find the supertype id from HasSubtype reference
		ExpandedNodeId superTypeId = null;
		for (ReferenceNode reference : node.getReferences()) {
			if (isTypeOf(reference.getReferenceTypeId(), Identifiers.HasSubtype) && reference.getIsInverse()) {
				superTypeId = reference.getTargetId();
				break;
			}
		}
		// get locak sypertype id
		NodeId localsuperTypeId = null;
		if (!ExpandedNodeId.isNull(superTypeId)) {
			try {
				localsuperTypeId = this.namespaceUris.toNodeId(superTypeId);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		synchronized (this.lock) {
			// create the TypeInfo for the new node
			TypeInfo typeInfo = this.nodes.get(node.getNodeId());
			if (typeInfo == null) {
				typeInfo = new TypeInfo();
				typeInfo.setBrowseName(node.getBrowseName());
				typeInfo.setNodeId(node.getNodeId());
				typeInfo.setDeleted(false);
				this.nodes.put(node.getNodeId(), typeInfo);
			} else {
				// fill empty
				if (typeInfo.isDeleted()) {
					typeInfo.setBrowseName(node.getBrowseName());
					typeInfo.setNodeId(node.getNodeId());
					typeInfo.setDeleted(false);
				}
			}
			// lookup the supertype
			TypeInfo superTypeInfo = null;
			if (localsuperTypeId != null) {
				// new supertype
				superTypeInfo = this.nodes.get(localsuperTypeId);
				if (superTypeInfo == null) {
					/** empty supertype info */
					superTypeInfo = new TypeInfo();
					superTypeInfo.setNodeId(localsuperTypeId);
					superTypeInfo.setDeleted(true);
					this.nodes.put(superTypeInfo.getNodeId(), superTypeInfo);
				}
				superTypeInfo.addSubType(typeInfo);
			}
			// set supertype
			typeInfo.setSuperType(superTypeInfo);
			// // remove the encodings
			if (typeInfo.getEncodings() != null) {
				for (ExpandedNodeId enc : typeInfo.getEncodings()) {
					this.encoding.remove(enc);
				}
			}
			// any new encoding
			ExpandedNodeId[] encodings = node.findTargets(Identifiers.HasEncoding, false);
			if (encodings.length > 0) {
				typeInfo.setEncodings(new ExpandedNodeId[encodings.length]);
				for (int ii = 0; ii < encodings.length; ii++) {
					typeInfo.setEncodingAt(encodings[ii], ii);
					this.encoding.put(typeInfo.getEncodings()[ii], typeInfo);
				}
			}
			// // add reference type
			if ((node.getNodeClass().getValue() & NodeClass.ReferenceType.getValue()) != 0) {
				if (QualifiedName.isNull(typeInfo.getBrowseName())) {
					this.referenceTypes.remove(typeInfo.getBrowseName());
				}
				typeInfo.setBrowseName(node.getBrowseName());
				this.referenceTypes.put(node.getBrowseName(), typeInfo);
			}
			return true;
		}
	}

	public boolean add(Node node, Node parentNode) {
		// check if node is null
		if (node == null || NodeId.isNull(node.getNodeId())) {
			return false;
		}
		// ignore non types
		if ((node.getNodeClass().getValue() & (NodeClass.ObjectType.getValue() | NodeClass.VariableType.getValue()
				| NodeClass.ReferenceType.getValue() | NodeClass.DataType.getValue())) == 0) {
			return false;
		}
		// find the supertype id from HasSubtype reference
		ExpandedNodeId superTypeId = null;
		for (ReferenceNode reference : node.getReferences()) {
			if (isTypeOf(reference.getReferenceTypeId(), Identifiers.HasSubtype) && reference.getIsInverse()) {
				superTypeId = reference.getTargetId();
				break;
			}
		}
		// get locak sypertype id
		NodeId localsuperTypeId = null;
		if (!ExpandedNodeId.isNull(superTypeId)) {
			try {
				localsuperTypeId = this.namespaceUris.toNodeId(superTypeId);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		synchronized (this.lock) {
			// create the TypeInfo for the new node
			TypeInfo typeInfo = null;
			if ((typeInfo = this.nodes.get(node.getNodeId())) == null) {
				typeInfo = new TypeInfo();
				typeInfo.setBrowseName(node.getBrowseName());
				typeInfo.setNodeId(node.getNodeId());
				typeInfo.setDeleted(false);
				this.nodes.put(node.getNodeId(), typeInfo);
			}
			// lookup the supertype
			TypeInfo superTypeInfo = null;
			if (localsuperTypeId != null) {
				// new supertype
				if ((superTypeInfo = this.nodes.get(localsuperTypeId)) == null && parentNode != null) {
					superTypeInfo = new TypeInfo();
					superTypeInfo.setBrowseName(parentNode.getBrowseName());
					superTypeInfo.setNodeId(parentNode.getNodeId());
					superTypeInfo.setDeleted(false);
					this.nodes.put(superTypeInfo.getNodeId(), superTypeInfo);
				}
				if (superTypeInfo != null)
					superTypeInfo.addSubType(typeInfo);
			}
			// set supertype
			typeInfo.setSuperType(superTypeInfo);
			// // remove the encodings
			if (typeInfo.getEncodings() != null) {
				for (ExpandedNodeId enc : typeInfo.getEncodings()) {
					this.encoding.remove(enc);
				}
			}
			// any new encoding
			ExpandedNodeId[] encodings = node.findTargets(Identifiers.HasEncoding, false);
			if (encodings.length > 0) {
				typeInfo.setEncodings(new ExpandedNodeId[encodings.length]);
				for (int ii = 0; ii < encodings.length; ii++) {
					typeInfo.setEncodingAt(encodings[ii], ii);
					this.encoding.put(typeInfo.getEncodings()[ii], typeInfo);
				}
			}
			// // add reference type
			if ((node.getNodeClass().getValue() & NodeClass.ReferenceType.getValue()) != 0) {
				if (QualifiedName.isNull(typeInfo.getBrowseName())) {
					this.referenceTypes.remove(typeInfo.getBrowseName());
				}
				typeInfo.setBrowseName(node.getBrowseName());
				this.referenceTypes.put(node.getBrowseName(), typeInfo);
			}
			return true;
		}
	}

	/**
	 * Every new node type know its parent.
	 * 
	 * @param Node       Node to add.
	 * @param ParentNode Hierachical parent of the node.
	 * @return TRUE if the node is added, otherwise false.
	 */
	public boolean addTemporaryType(Node node, Node parentNode) {
		// check if node is null
		if (node == null || NodeId.isNull(node.getNodeId())) {
			throw new IllegalArgumentException();
		}
		// ignore non types
		if ((node.getNodeClass().getValue() & (NodeClass.ObjectType.getValue() | NodeClass.VariableType.getValue()
				| NodeClass.ReferenceType.getValue() | NodeClass.DataType.getValue())) == 0) {
			return false;
		}
		// find the supertype id from HasSubtype reference
		ExpandedNodeId superTypeId = null;
		for (ReferenceNode reference : node.getReferences()) {
			if (isTypeOf(reference.getReferenceTypeId(), Identifiers.HasSubtype) && reference.getIsInverse()) {
				superTypeId = reference.getTargetId();
				break;
			}
		}
		// get locak sypertype id
		NodeId localsuperTypeId = null;
		if (!ExpandedNodeId.isNull(superTypeId)) {
			try {
				localsuperTypeId = this.namespaceUris.toNodeId(superTypeId);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		synchronized (this.lock) {
			// create the TypeInfo for the new node
			TypeInfo typeInfo = null;
			if ((typeInfo = this.tmpNodes.get(node.getNodeId())) == null) {
				typeInfo = new TypeInfo();
				typeInfo.setBrowseName(node.getBrowseName());
				typeInfo.setNodeId(node.getNodeId());
				typeInfo.setDeleted(false);
				this.tmpNodes.put(node.getNodeId(), typeInfo);
			}
			// lookup the supertype
			TypeInfo superTypeInfo = null;
			if (localsuperTypeId != null) {
				// new supertype
				if ((superTypeInfo = this.tmpNodes.get(localsuperTypeId)) == null) {
					/** empty supertype info */
					if (parentNode != null) {
						superTypeInfo = new TypeInfo();
						superTypeInfo.setBrowseName(parentNode.getBrowseName());
						superTypeInfo.setNodeId(parentNode.getNodeId());
						superTypeInfo.setDeleted(false);
						this.tmpNodes.put(superTypeInfo.getNodeId(), superTypeInfo);
					} else {
						return false;
					}
				}
				superTypeInfo.addSubType(typeInfo);
			}
			// set supertype
			typeInfo.setSuperType(superTypeInfo);
			return true;
		}
	}

	public boolean isTypeOf(ExpandedNodeId subTypeId, ExpandedNodeId superTypeId) throws ServiceResultException {
		// check for null
		if (ExpandedNodeId.isNull(subTypeId) || ExpandedNodeId.isNull(superTypeId)) {
			return false;
		}
		// check for exact match
		if (subTypeId.equals(superTypeId)) {
			return true;
		}
		NodeId startId = null;
		NodeId targetId = null;
		try {
			startId = this.namespaceUris.toNodeId(subTypeId);
			if (NodeId.isNull(startId)) {
				return false;
			}
			targetId = this.namespaceUris.toNodeId(superTypeId);
			if (NodeId.isNull(targetId)) {
				return false;
			}
		} catch (ServiceResultException sre) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, sre.getMessage(), sre);
		}
		synchronized (this.lock) {
			TypeInfo info = null;
			if ((info = this.tmpNodes.get(startId)) == null && (info = this.nodes.get(startId)) == null) {
				return false;
			}
			return info.isTypeOf(targetId);
		}
	}

	public boolean isTypeOf(NodeId subTypeId, NodeId superTypeId) {
		// check for null
		if (NodeId.isNull(subTypeId) || NodeId.isNull(superTypeId)) {
			return false;
		}
		// check for exact match
		if (subTypeId.equals(superTypeId)) {
			return true;
		}
		synchronized (this.lock) {
			TypeInfo info = null;
			if ((info = this.tmpNodes.get(subTypeId)) == null && (info = this.nodes.get(subTypeId)) == null) {
				return false;
			}
			return info.isTypeOf(superTypeId);
		}
	}

	public TypeInfo findType(NodeId typeId, BuiltinType builtInType, ValueRanks valueRank) {
		synchronized (this.lock) {
			TypeInfo typeInfo = null;
			if ((typeInfo = this.nodes.get(typeId)) == null) {
				return TypeInfo.Unknown;
			}
			return typeInfo.cloneInfo(typeId, builtInType, valueRank);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public NodeId findDataTypeId(NodeId typeId) {
		synchronized (this.lock) {
			TypeInfo typeInfo = null;
			if ((typeInfo = this.encoding.get(typeId)) == null) {
				return NodeId.NULL;
			}
			return typeInfo.getNodeId();
		}
	}

	public NodeId findSuperType(NodeId typeId) {
		if (typeId == null) {
			return NodeId.NULL;
		}
		synchronized (this.lock) {
			TypeInfo typeInfo = null;
			if ((typeInfo = this.nodes.get(typeId)) == null) {
				return NodeId.NULL;
			}
			if (typeInfo.getSuperType() != null) {
				return typeInfo.getSuperType().getNodeId();
			}
		}
		return NodeId.NULL;
	}

	public NodeId findReferenceType(QualifiedName browseName) {
		// check for empty name
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		synchronized (this.lock) {
			TypeInfo typeInfo = null;
			if ((typeInfo = this.referenceTypes.get(browseName)) == null) {
				return null;
			}
			return typeInfo.getNodeId();
		}
	}

	public QualifiedName findReferenceTypeName(NodeId referenceTypeId) {
		synchronized (this.lock) {
			TypeInfo typeInfo = null;
			if ((typeInfo = this.nodes.get(referenceTypeId)) == null) {
				return null;
			}
			return typeInfo.getBrowseName();
		}
	}

	/**
	 * Get all typenodeinfos
	 * 
	 * @return a copy of the current node type model
	 */
	public Map<NodeId, TypeInfo> getAllTypes() {
		return new HashMap<>(this.nodes);
	}

	/**
	 * Checks if the TypeId is a type instance of an Structure (Complex Type)
	 * 
	 * @param typeId
	 * @return
	 */
	public boolean isStructure(NodeId typeId) {
		return this.isTypeOf(typeId, Identifiers.Structure);
	}

	/**
	 * Checks if the TypeId is a type instance of an Structure
	 * 
	 * @param typeId
	 * @return
	 */
	public boolean isEnumeration(NodeId typeId) {
		return this.isTypeOf(typeId, Identifiers.Enumeration);
	}

	public boolean isKnown(NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			return false;
		}
		synchronized (this.lock) {
			return this.nodes.containsKey(nodeId);
		}
	}

	/**
	 * Adds a type to the table. A browse name is only required if it is a reference
	 * type.
	 * 
	 * @param NodeId      The subtype identifier
	 * @param SuperTypeId The supertype identifier
	 * @throws ServiceResultException
	 */
	public void addSubType(NodeId nodeId, NodeId superTypeId) throws ServiceResultException {
		addSubType(nodeId, superTypeId, null);
	}

	private void addSubType(NodeId subTypeId, NodeId superTypeId, QualifiedName browseName)
			throws ServiceResultException {
		synchronized (this.lock) {
			// lookup the supertype
			TypeInfo superTypeInfo = null;
			if (!NodeId.isNull(superTypeId)) {
				superTypeInfo = this.nodes.get(superTypeId);
				if (superTypeInfo == null) {
					throw new ServiceResultException(StatusCodes.Bad_NodeIdInvalid);
				}
				// create the TypeInfo
				TypeInfo typeInfo = this.nodes.get(subTypeId);
				if (typeInfo == null) {
					typeInfo = new TypeInfo();
					this.nodes.put(subTypeId, typeInfo);
				}
				// update the info
				typeInfo.setNodeId(subTypeId);
				typeInfo.setSuperType(superTypeInfo);
				typeInfo.setDeleted(false);
				// add to supertype
				superTypeInfo.addSubType(typeInfo);
				// remove the encodings
				if (typeInfo.getEncodings() != null) {
					for (ExpandedNodeId enc : typeInfo.getEncodings()) {
						this.encoding.remove(enc);
					}
				}
				// add reference type
				if (!QualifiedName.isNull(browseName)) {
					typeInfo.setBrowseName(browseName);
					this.referenceTypes.put(browseName, typeInfo);
				}
			}
		}
	}

	/**
	 * Adds a type to the typeTable. A Browsename is only required if it is a
	 * ReferenceType.
	 * 
	 * @param SubTypeId   The subtype identifier.
	 * @param SuperTypeId The supertype identifier.
	 * @param BrowseName  Name of the reference.
	 * @throws ServiceResultException
	 */
	public void addReferenceSubType(NodeId subTypeId, NodeId superTypeId, QualifiedName browseName)
			throws ServiceResultException {
		addSubType(subTypeId, superTypeId, browseName);
	}

	/**
	 * Removes a subtype
	 * 
	 * @param typeId
	 */
	public void remove(ExpandedNodeId typeId) {
		if (ExpandedNodeId.isNull(typeId) || typeId.getServerIndex().intValue() != 0) {
			return;
		}
		NodeId localId = null;
		try {
			localId = this.namespaceUris.toNodeId(typeId);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		this.remove(localId);
	}

	/**
	 * Removes a subtype
	 * 
	 * @param localId
	 */
	public void remove(NodeId localId) {
		if (NodeId.isNull(localId)) {
			return;
		}
		synchronized (this.lock) {
			// remove type
			TypeInfo typeInfo = this.nodes.get(localId);
			if (typeInfo == null) {
				return;
			}
			this.nodes.remove(localId);
			// setting the flag to deleted ensures references from subtypes are
			// not broken
			typeInfo.setDeleted(true);
			// remove from subtype list
			if (typeInfo.getSuperType() != null) {
				typeInfo.getSuperType().removeSubType(localId);
			}
			// remove encodings
			if (typeInfo.getEncodings() != null) {
				for (int i = 0; i < typeInfo.getEncodings().length; i++) {
					this.encoding.remove(typeInfo.getEncodings()[i]);
				}
			}
			// remove reference type
			if (!QualifiedName.isNull(typeInfo.getBrowseName())) {
				this.referenceTypes.remove(typeInfo.getBrowseName()).getBrowseName();
			}
		}
	}

	public boolean isEncodingFor(NodeId expectedType, Object value) {
		// no match
		if (value == null) {
			return false;
		}
		// null type matches all
		if (NodeId.isNull(expectedType)) {
			return true;
		}
		NodeId actualTypeId = TypeInfo.getDataTypeId(value);
		// value is valid if the expected datatype is same as or a supertype of
		// the actual datatype
		// for example: expected datatype of 'Integer' matches an actual
		// datatype of 'UInt32'.
		if (isTypeOf(expectedType, actualTypeId)) {
			return true;
		}
		// allow matches non-structure values where the actual datatype is a
		// supertype of the expected datatype.
		// for example: expected datatype of 'UtcTime' matches an actual
		// datatype of 'DateTime'.
		if (!Identifiers.Structure.equals(actualTypeId)) {
			return isTypeOf(expectedType, actualTypeId);
		}
		// for structure types must try to determine the subtype.
		if (value instanceof ExtensionObject) {
			ExtensionObject extensionObj = (ExtensionObject) value;
			return isEncodingFor(expectedType, extensionObj);
		}
		// every element in the array must match
		if (value instanceof ExtensionObject[]) {
			ExtensionObject[] extensionObj = (ExtensionObject[]) value;
			for (int i = 0; i < extensionObj.length; i++) {
				if (!isEncodingFor(expectedType, extensionObj[i])) {
					return false;
				}
			}
			return true;
		}
		// can only get here if the value is an unrecognized data type.
		return false;
	}

	public boolean isEncodingFor(NodeId expectedTypeId, ExtensionObject value) {
		// no match
		if (value == null) {
			return false;
		}
		return isEncodingOf(value.getTypeId(), expectedTypeId);
	}

	public boolean isEncodingOf(ExpandedNodeId encodingId, NodeId dataTypeId) {
		if (ExpandedNodeId.isNull(encodingId) || NodeId.isNull(dataTypeId)) {
			return false;
		}
		synchronized (this.lock) {
			TypeInfo typeInfo = this.encoding.get(encodingId);
			if (typeInfo == null) {
				return false;
			}
			if (typeInfo.getNodeId().equals(dataTypeId)) {
				return true;
			}
			// check if the encoding is a representation of a subtype of the
			// expected datatype id
			TypeInfo superTypeInfo = typeInfo.getSuperType();
			while (superTypeInfo != null) {
				if (!superTypeInfo.isDeleted() && dataTypeId.equals(superTypeInfo.getNodeId())) {
					return true;
				}
				superTypeInfo = superTypeInfo.getSuperType();
			}
		}
		return false;
	}

	public void removeTemporaryTypes() {
		this.tmpNodes.clear();
	}

	/**
	 * Changes the servers namespace table and its nodeid's
	 * 
	 * @param namespaceTable
	 * @param mapping
	 */
	public void doChangeNamespaceTable(NamespaceTable namespaceTable, Map<Integer, Integer> mapping) {
		Map<NodeId, TypeInfo> newNodes = new HashMap<>();
		// do mapping reference types
		for (Entry<NodeId, TypeInfo> node : this.nodes.entrySet()) {
			// map typeinfo
			TypeInfo typeinfo = node.getValue();
			NodeId newId = NodeIdMapper.mapNamespaceIndex(typeinfo.getNodeId(), mapping);
			typeinfo.setNodeId(newId);
			newNodes.put(newId, typeinfo);
		}
		// change to new nodeids
		this.nodes = newNodes;
		this.complextypes.doChangeNamespaceTable(namespaceTable, mapping);
		this.namespaceUris = namespaceTable;
	}
}
