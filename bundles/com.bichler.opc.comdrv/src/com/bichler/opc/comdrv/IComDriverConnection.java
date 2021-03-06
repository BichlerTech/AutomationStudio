// (c) Copyright 2010 HB-Softsolution e.U.
// ALL RIGHTS RESERVED.
//
// DISCLAIMER:
// This code is provided and developed by HB-Softsolution e.U.
// Distribution of this source code underlies the Warranty
// and Liability Disclaimers which appear in the
// HB-Softsolution e.U. license agreements.
//
// Authors: hannes bichler
// Company: HB-Softsolution e.U.
// Web: www.hb-softsoluiton.com
// contact: hannes.bichler@hb-softsolution.com
package com.bichler.opc.comdrv;

import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.ua.classes.BaseEventType;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.TimestampsToReturn;

/**
 * 
 * @version v1.0.0.1
 * @author hannes
 * @since 22.09.2009
 * 
 */
public interface IComDriverConnection {
	/**
	 * write a driver value to the visu address space returns true if variable could
	 * be written otherwise false
	 * 
	 * 
	 * @param nodeId    Nodeid of data point.
	 * @param value     DataValue to write to ua.
	 * @param dpstate   State of data point to write.
	 * @param timestamp Timestamp of data point.
	 */
	// StatusCode writeFromDriver(NodeId[] nodeId, DataValue[] value,
	// long dpstate);
	/**
	 * write a driver value to the visu address space returns true if variable could
	 * be written otherwise false
	 * 
	 * @deprecated
	 * 
	 * @param nodeId    Nodeid of data point.
	 * @param value     DataValue to write to ua.
	 * @param dpstate   State of data point to write.
	 * @param timestamp Timestamp of data point.
	 */
	// StatusCode writeFromDriverInternal(NodeId nodeId, DataValue value,
	// long dpstate);
	/**
	 * Set a flag to node if a special driver is connected.
	 * 
	 * @param nodeid   Id of the node to set the flags.
	 * @param flag     Indicate if a driver is connected for to a opc ua node.
	 * @param syncRead Indicate if the node should be red async or sync.
	 * @param drvId    driver id to which the node is connected
	 * 
	 * @return True if flag can be set, otherwise false.
	 */
	boolean setDriverReadConnected(NodeId nodeid, boolean flag, byte syncRead, long drvId);

	/**
	 * Set a flag to node if a special driver is connected.
	 * 
	 * @param nodeid    Id of the node to set the flags.
	 * @param flag      Indicate if a driver is connected for to a opc ua node.
	 * @param syncWrite Indicate if the node should be written async or sync.
	 * @param drvId     driver id to which the node is connected
	 * 
	 * @return True if flag can be set, otherwise false.
	 */
	boolean setDriverWriteConnected(NodeId nodeid, boolean flag, byte syncWrite, long drvId);

	/**
	 * Write some driver values to the ua address space. returns true if variables
	 * could be written otherwise false.
	 * 
	 * @param nodeIds    Nodeids of data points.
	 * @param values     DataValues to write to ua.
	 * @param dpstates   States of data points to write.
	 * @param timestamps Timestamps of data points.
	 */
	DataValue readFromDriver(NodeId nodeIds, UnsignedInteger attributeIds, String indexRanges,
			QualifiedName dataEncodings, Long dpstates, double maxAge, TimestampsToReturn timestampToReturn);

	/**
	 * Write some driver values to the ua address space. returns true if variables
	 * could be written otherwise false.
	 * 
	 * @param nodeIds    Nodeids of data points.
	 * @param values     DataValues to write to ua.
	 * @param dpstates   States of data points to write.
	 * @param timestamps Timestamps of data points.
	 */
	DataValue[] readFromDriver(NodeId[] nodeIds, UnsignedInteger[] attributeIds, String[] indexRanges,
			QualifiedName[] dataEncodings, Long[] dpstates, double maxAge, TimestampsToReturn timestampToReturn);

	/**
	 * Fire an event from driver and write some event values to the ua address
	 * space. returns true if variables could be written otherwise false.
	 * 
	 * @param nodeIds    Nodeids of data points.
	 * @param values     DataValues to write to ua.
	 * @param dpstates   States of data points to write.
	 * @param timestamps Timestamps of data points.
	 */
	StatusCode[] fireEventFromDriver(NodeId[] nodeIds, BaseEventType[] eventStates, Long[] dpstates);

	/**
	 * Write some driver values to the ua address space. returns true if variables
	 * could be written otherwise false.
	 * 
	 * @param nodeIds    Nodeids of data points.
	 * @param values     DataValues to write to ua.
	 * @param dpstates   States of data points to write.
	 * @param timestamps Timestamps of data points.
	 */
	StatusCode[] writeFromDriver(NodeId[] nodeIds, UnsignedInteger[] attributeIds, String[] indexRanges,
			DataValue[] values, Long[] dpstates);

	/**
	 * Write some driver values to the ua address space. returns true if variables
	 * could be written otherwise false.
	 * 
	 * @param nodeIds    Nodeids of data points.
	 * @param values     DataValues to write to ua.
	 * @param dpstates   States of data points to write.
	 * @param timestamps Timestamps of data points.
	 */
	StatusCode writeFromDriver(NodeId nodeIds, UnsignedInteger attributeIds, String indexRanges, DataValue values,
			Long dpstates);
	/**
	 * Write some driver values to the ua address space. returns true if variables
	 * could be written otherwise false.
	 * 
	 * @depricated
	 * 
	 * @param nodeIds    Nodeids of data points.
	 * @param values     DataValues to write to ua.
	 * @param dpstates   States of data points to write.
	 * @param timestamps Timestamps of data points.
	 */
	// StatusCode[] writeFromDriverInternal(NodeId []nodeIds, DataValue
	// []values,
	// long []dpstates);
	/**
	 * Read an attribute from opc ua address space.
	 * 
	 * 
	 * @param nodeId NodeId of attribute to read.
	 * @param id     Attribute id to read.
	 * @return The red datavalue or null.
	 */
	// DataValue readAttribute(NodeId nodeId, UnsignedInteger attribueId);
	/**
	 * Read an internal attribute from opc ua address space.
	 * 
	 * @deprecated
	 * 
	 * @param nodeId NodeId of attribute to read.
	 * @param id     Attribute id to read.
	 * @return The red datavalue or null.
	 */
	// DataValue readInternalAttribute(NodeId nodeId, UnsignedInteger
	// attribueId);

	/**
	 * Read the value of the desired node.
	 * 
	 * @param nodeId NodeId of value to read.
	 * @return The read request result.
	 */
	// DataValue readValue(NodeId nodeId);
	/**
	 * Get a list with all children of a node from the address space.
	 * 
	 * @param nodeId Nodeid to get the node for.
	 * @return List with all children for that id.
	 */
	Node[] getChildren(NodeId nodeId);

	/**
	 * Get a node from the address space via nodeid.
	 * 
	 * @param nodeId Nodeid to get the node for.
	 * @return actual node for that id.
	 */
	Node getNode(NodeId nodeId);

	/**
	 * Get the type info of the value of a variable node, only nodeids of variable
	 * nodes are allowed.
	 * 
	 * @param nodeId Id of variable node to get the whole type info.
	 * @return Typeinfo of the value or null if it is node has no value.
	 */
	TypeInfo getTypeInfo(NodeId nodeId);
	/**
	 * Set a flag to node if a special driver is connected.
	 * 
	 * @param nodeid   Id of the node to set the flags.
	 * @param flag     Indicate if a driver is connected for to a opc ua node.
	 * @param syncRead Indicate if the node should be red async or sync.
	 * 
	 * @return True if flag can be set, otherwise false.
	 */
	// @Deprecated
	// boolean setDriverReadConnected(NodeId nodeid, boolean flag, byte
	// syncRead);

	/**
	 * Set a flag to node if a special driver is connected.
	 * 
	 * @param nodeid    Id of the node to set the flags.
	 * @param flag      Indicate if a driver is connected for to a opc ua node.
	 * @param syncWrite Indicate if the node should be written async or sync.
	 * 
	 * @return True if flag can be set, otherwise false.
	 */
	// @Deprecated
	// boolean setDriverWriteConnected(NodeId nodeid, boolean flag, byte
	// syncWrite);
	/**
	 * Add one node into internal address space.
	 * 
	 * @param node
	 * @return
	 */
	boolean addNodes(AddNodesItem[] node);

	/**
	 * Adds a node to the server address space (if a type, then all child nodes for
	 * the type are created as well)
	 * 
	 * @param nodes Information for the new nodes to create.
	 * 
	 */
	boolean loadNodes(AddNodesItem[] node);

	/**
	 * Adds a node to the server address space (if a type, then all child nodes for
	 * the type are created as well)
	 * 
	 * @param nodes                   Information for the new nodes to create.
	 * @param includeParentComponents Indicate if parent componts should be created
	 *                                too.
	 * 
	 */
	boolean loadNodes(AddNodesItem[] nodes, boolean includeParentComponents);

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
	boolean loadNodes(AddNodesItem[] nodes, boolean includeParentComponents, boolean createOptionalPlaceholder);

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
	boolean loadNodes(AddNodesItem[] nodes, boolean includeParentComponents, boolean createOptionalPlaceholder,
			boolean addModellingRule);

	/**
	 * 
	 * @param item
	 * @return
	 */
	boolean addReferences(AddReferencesItem[] item);
	/**
	 * Read a internal value from opc ua address space, no additional driver read
	 * will be called.
	 * 
	 * @deprecated
	 * 
	 * @param nodeId NodeId of attribute to read.
	 * @param id     Attribute id to read.
	 * @return The red datavalue or null.
	 */
	// DataValue readInternalValue(NodeId nodeId);

	/**
	 * Remove a node from address space with or without all its children.
	 * 
	 * @param nodeid Id of main node to delete.
	 * @return success or error code
	 */
	StatusCode[] removeNodes(NodeId[] nodeid, boolean deleteTargetReferences);

	/**
	 * Remove any nodes from address space inclusive all their children.
	 * 
	 * @param nodeid Id of main node to delete.
	 * @return success or error code
	 */
	StatusCode[] removeNodes(NodeId[] nodeid);

	/**
	 * imports a model file into existing opc ua address space
	 * 
	 * @param path
	 */
	void importModelFile(String path);

	/**
	 * Sets a flag if an OPC UA node is shown in the address space to clients.
	 * 
	 * @param nodeId
	 * @param isVisible
	 */
	void setNodeVisible(NodeId[] nodeId, boolean isVisible);

	boolean filterEventFromDriver(NodeId nodeId, BaseEventType eventState, EventFilter dpState);
}
