package opc.server.hbserver;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.WriteValue;

import com.bichler.opc.comdrv.IComDriverConnection;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.ua.classes.BaseEventType;

public class OPCUADriverConnection implements IComDriverConnection {
	// TESTPARAMETER, Accesslevels should not be skipped!
	private static final boolean SKIPACCESSLEVEL = true;
	private Logger logger = Logger.getLogger(OPCUADriverConnection.class.getName());
	/** Operation Server */
	private OPCInternalServer server = null;

	/**
	 * A new HB Driver.
	 * 
	 * @param InternalServer Internal operation server
	 */
	public OPCUADriverConnection(OPCInternalServer internalServer) {
		this.server = internalServer;
	}

	/**
	 * Adds a node to the server's addressspace
	 * 
	 * @param Node Information for the new node to create.
	 * 
	 */
	@Override
	public boolean addNodes(AddNodesItem[] nodes) {
		AddNodesResult[] result = null;
		try {
			Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<ExpandedNodeId, AddNodesItem>();
			for (AddNodesItem item : nodes) {
				mappedNodes.put(item.getRequestedNewNodeId(), item);
			}
			result = this.server.getMaster().addNodes(nodes, mappedNodes, false, null, false);
		} catch (ServiceResultException e) {
			logger.severe("Could not create all nodes to address space!");
		}
		return result != null;
	}

	/**
	 * Adds a reference to the server's addressspace (allows to connect underlying
	 * system models to the server).
	 * 
	 * @param Item Information for the reference to add.
	 */
	@Override
	public boolean addReferences(AddReferencesItem[] item) {
		StatusCode[] result = null;
		try {
			result = this.server.getMaster().addReferences(item, null);
		} catch (ServiceResultException e) {
			logger.severe("Could not create all references to address space!");
		}
		return result != null;
	}

	/**
	 * Return children node from the nodeid parent.
	 * 
	 * @param NodeId NodeId from the parent node.
	 * @return Array of node children.
	 */
	@Override
	public Node[] getChildren(NodeId parentId) {
		/** fetch the children of a node with its nodeId */
		return this.server.getAddressSpaceManager().findChildren(parentId);
	}

	/**
	 * Returns a TypeInfo from the value attribute of the node with the NodeId
	 * parameter.
	 * 
	 * @param NodeId
	 * 
	 * @return TypeInfo, or null if we couldn't create a corresponding resultType
	 */
	@Override
	public TypeInfo getTypeInfo(NodeId nodeId) {
		DataValue resultType = readFromDriver(nodeId, Attributes.Value, null, null, null, 0.0,
				TimestampsToReturn.Neither);
		if (resultType == null) {
			return null;
		}
		return TypeInfo.construct(resultType, this.server.getTypeTable());
	}

	/**
	 * Returns a cloned node from the server.
	 * 
	 * @param NodeId
	 * 
	 *               return Node
	 */
	@Override
	public Node getNode(NodeId nodeId) {
		return this.server.getAddressSpaceManager().getNodeById(nodeId);
	}

	/**
	 * Adds a node to the server address space (if a type, then all child nodes for
	 * the type are created as well)
	 * 
	 * @param Node Information for the new node to create.
	 * 
	 */
	@Override
	public boolean loadNodes(AddNodesItem[] nodes) {
		AddNodesResult[] result = null;
		try {
			Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<>();
			for (AddNodesItem item : nodes) {
				mappedNodes.put(item.getRequestedNewNodeId(), item);
			}
			result = this.server.getMaster().addNodes(nodes, mappedNodes, true, null, true);
		} catch (ServiceResultException e) {
			logger.severe("Could not create all nodes to address space!");
		}
		return result != null;
	}

	/**
	 * Adds a node to the server address space (if a type, then all child nodes for
	 * the type are created as well)
	 * 
	 * @param nodes                   Information for the new nodes to create.
	 * @param includeParentComponents Indicate if parent componts should be created
	 *                                too.
	 * 
	 */
	@Override
	public boolean loadNodes(AddNodesItem[] nodes, boolean includeParentComponents) {
		AddNodesResult[] result = null;
		try {
			Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<>();
			for (AddNodesItem item : nodes) {
				mappedNodes.put(item.getRequestedNewNodeId(), item);
			}
			result = this.server.getMaster().addNodes(nodes, mappedNodes, true, null, includeParentComponents);
		} catch (ServiceResultException e) {
			logger.severe("Could not create all nodes to address space!");
		}
		return result != null;
	}

	/**
	 * Adds a node to the server address space (if a type, then all child nodes for
	 * the type are created as well)
	 * 
	 * @param nodes                     Information for the new nodes to create.
	 * @param includeParentComponents   Indicate if parent componts should be
	 *                                  created too.
	 * @param createOptionalPlaceholder Indicate if optional placeholder components
	 *                                  should be created too.
	 * 
	 */
	@Override
	public boolean loadNodes(AddNodesItem[] nodes, boolean includeParentComponents, boolean createOptionalPlaceholder) {
		AddNodesResult[] result = null;
		try {
			Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<>();
			for (AddNodesItem item : nodes) {
				mappedNodes.put(item.getRequestedNewNodeId(), item);
			}
			result = this.server.getMaster().addNodes(nodes, mappedNodes, true, null, includeParentComponents,
					createOptionalPlaceholder);
		} catch (ServiceResultException e) {
			logger.severe("Could not create all nodes to address space!");
		}
		return result != null;
	}

	/**
	 * Adds a node to the server address space (if a type, then all child nodes for
	 * the type are created as well)
	 * 
	 * @param nodes                     Information for the new nodes to create.
	 * @param includeParentComponents   Indicate if parent componts should be
	 *                                  created too.
	 * @param createOptionalPlaceholder Indicate if optional placeholder components
	 *                                  should be created too.
	 * @param addModellingRule          Indicate if modelling rule reference should
	 *                                  be created too.
	 * 
	 */
	@Override
	public boolean loadNodes(AddNodesItem[] nodes, boolean includeParentComponents, boolean createOptionalPlaceholder,
			boolean addModellingRule) {
		AddNodesResult[] result = null;
		try {
			Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<>();
			for (AddNodesItem item : nodes) {
				mappedNodes.put(item.getRequestedNewNodeId(), item);
			}
			result = this.server.getMaster().addNodes(nodes, mappedNodes, true, null, includeParentComponents,
					createOptionalPlaceholder, addModellingRule);
		} catch (ServiceResultException e) {
			logger.severe("Could not create all nodes to address space!");
		}
		return result != null;
	}

	/**
	 * Reads an attribute from the Node with the NodeId parameter
	 * 
	 * @param NodeId      NodeId from the node to read.
	 * @param AttributeId AttributeId from the node�s attribute to read.
	 * 
	 * @return DataValue from the read attribute.
	 */
	@Override
	public DataValue readFromDriver(NodeId nodeIds, UnsignedInteger attributeIds, String indexRanges,
			QualifiedName dataEncodings, Long dpstates, double maxAge, TimestampsToReturn timestampToReturn) {
		DataValue[] value = readFromDriver(new NodeId[] { nodeIds }, new UnsignedInteger[] { attributeIds },
				new String[] { indexRanges }, new QualifiedName[] { dataEncodings }, new Long[] { dpstates }, maxAge,
				timestampToReturn);
		if (value != null && value.length > 0) {
			return value[0];
		}
		return null;
	}

	/**
	 * Writes a value from the driver. (Skips AccessLevel)
	 * 
	 * @param NodeId
	 * @param Value
	 * @param Tagstate
	 * 
	 * @return TRUE if the value is written, otherwise FALSE.
	 */
	@Override
	public DataValue[] readFromDriver(NodeId[] nodeIds, UnsignedInteger[] attributeIds, String[] indexRanges,
			QualifiedName[] dataEncodings, Long[] dpstates, double maxAge, TimestampsToReturn timestampToReturn) {
		/** read the values to the server */
		ReadValueId[] values2read = new ReadValueId[nodeIds.length];
		for (int i = 0; i < values2read.length; i++) {
			values2read[i] = new ReadValueId();
			values2read[i].setNodeId(nodeIds[i]);
			values2read[i].setAttributeId(attributeIds[i]);
			if (indexRanges != null) {
				values2read[i].setIndexRange(indexRanges[i]);
			}
			if (dataEncodings != null) {
				values2read[i].setDataEncoding(dataEncodings[i]);
			}
		}
		DataValue[] results = null;
		try {
			results = this.server.getMaster().read(values2read, maxAge, timestampToReturn, dpstates, null);
		} catch (ServiceResultException e) {
			logger.severe("Could not read a node value from address space!");
			// e.printStackTrace();
		}
		return results;
	}

	@Override
	public StatusCode writeFromDriver(NodeId nodeIds, UnsignedInteger attributeIds, String indexRanges,
			DataValue values, Long dpstates) {
		StatusCode[] result = writeFromDriver(new NodeId[] { nodeIds }, new UnsignedInteger[] { attributeIds },
				new String[] { indexRanges }, new DataValue[] { values }, new Long[] { dpstates });
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Write values from the driver. (Skips AccessLevel)
	 * 
	 * TODO: Locales
	 * 
	 * @param NodeId
	 * @param Value
	 * @param Tagstate
	 * 
	 * @return TRUE if the value is written, otherwise FALSE.
	 */
	@Override
	public StatusCode[] writeFromDriver(NodeId[] nodeIds, UnsignedInteger[] attributeIds, String[] indexRange,
			DataValue[] values, Long[] tagstate) {
		/** write the values to the server */
		WriteValue[] values2write = new WriteValue[nodeIds.length];
		for (int i = 0; i < values2write.length; i++) {
			values2write[i] = new WriteValue();
			values2write[i].setNodeId(nodeIds[i]);
			values2write[i].setValue(values[i]);
			values2write[i].setAttributeId(attributeIds[i]);
			if (indexRange != null) {
				values2write[i].setIndexRange(indexRange[i]);
			}
		}
		StatusCode[] results = null;
		try {
			results = this.server.getMaster().write(values2write, SKIPACCESSLEVEL, tagstate, null);
		} catch (ServiceResultException e) {
			logger.severe("Could not write a node value to address space!");
		}
		return results;
	}

	/**
	 * Set the DriverConnected-Flag to a node. This flag indicates if the node
	 * writes to the server or not.
	 * 
	 * @param IsWriteToDriver
	 * 
	 * @return TRUE if the flag has been set, otherwise FALSE.
	 */
	@Override
	public boolean setDriverReadConnected(NodeId nodeId, boolean flag, byte syncRead, long drvId) {
		Node node = this.server.getAddressSpaceManager().getNodeById(nodeId);
		if (node == null) {
			return false;
		}
		switch (node.getNodeClass()) {
		case Variable:
			((UAVariableNode) node).setDriverConnected(flag);
			((UAVariableNode) node).setSyncReadMask(syncRead);
			((UAVariableNode) node).addReadDriverId(drvId);
			return true;
		default:
			return false;
		}
	}

	/**
	 * Set the DriverConnected-Flag to a node. This flag indicates if the node
	 * writes to the server or not.
	 * 
	 * @param IsWriteToDriver
	 * 
	 * @return TRUE if the flag has been set, otherwise FALSE.
	 */
	@Override
	public boolean setDriverWriteConnected(NodeId nodeId, boolean flag, byte syncWrite, long drvId) {
		Node node = server.getAddressSpaceManager().getNodeById(nodeId);
		if (node == null) {
			return false;
		}
		switch (node.getNodeClass()) {
		case Variable:
			((UAVariableNode) node).setDriverConnected(flag);
			((UAVariableNode) node).setSyncWriteMask(syncWrite);
			((UAVariableNode) node).addWriteDriverId(drvId);
			return true;
		default:
			return false;
		}
	}

	@Override
	public StatusCode[] removeNodes(NodeId[] nodeid) {
		if (nodeid == null) {
			return new StatusCode[] { new StatusCode(StatusCodes.Bad_NothingToDo) };
		}
		DeleteNodesItem[] items = new DeleteNodesItem[nodeid.length];
		for (int i = 0; i < nodeid.length; i++) {
			items[i] = new DeleteNodesItem();
			items[i].setDeleteTargetReferences(true);
			items[i].setNodeId(nodeid[i]);
		}
		try {
			return server.getMaster().deleteNodes(items, null);
		} catch (ServiceResultException e) {
			logger.severe("Could not delete a node from address space!");
		}
		return null;
	}

	@Override
	public StatusCode[] removeNodes(NodeId[] nodeid, boolean deleteTargetReferences) {
		if (nodeid == null) {
			return new StatusCode[] { new StatusCode(StatusCodes.Bad_NothingToDo) };
		}
		StatusCode[] codes = new StatusCode[nodeid.length];
		for (int i = 0; i < nodeid.length; i++) {
			try {
				codes[i] = server.getMaster().deleteNode(nodeid[i], deleteTargetReferences);
			} catch (ServiceResultException e) {
				logger.severe("Could not delete a node from address space!");
			}
		}
		return null;
	}

	@Override
	public void setNodeVisible(NodeId[] nodeIds, boolean isVisible) {
		for (NodeId nodeId : nodeIds) {
			Node node = this.server.getAddressSpaceManager().getIgnoreVisibilityNodeById(nodeId);
			if (node == null) {
				continue;
			}
			node.setVisible(isVisible);
		}
	}

	@Override
	public void importModelFile(String path) {
		this.server.importModel(path);
	}

	@Override
	public StatusCode[] fireEventFromDriver(NodeId[] nodeIds, BaseEventType[] eventStates, Long[] dpstates) {
		return this.server.getMaster().reportEvent(nodeIds, eventStates, dpstates);
	}

	@Override
	public boolean filterEventFromDriver(NodeId nodeId, BaseEventType eventState, EventFilter filter) {
		return false;
	}
}
