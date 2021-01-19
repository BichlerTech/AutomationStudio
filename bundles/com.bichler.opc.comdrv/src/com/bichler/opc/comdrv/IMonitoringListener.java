package com.bichler.opc.comdrv;

import opc.sdk.core.node.Node;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyRequest;

public interface IMonitoringListener {
	/**
	 * Register a new node for the notification service of the underlying system.
	 * 
	 * @param node         The node to register for the service.
	 * @param itemToCreate Create request for a node with additional information.
	 * 
	 * @return GOOD if node could be registered, otherwise the corresponding code.
	 *         if we return StatusCodes.Bad_NodeIdUnknown then the rest of the
	 *         caller will be processed
	 */
	StatusCode registerEvent(Node node, MonitoredItemCreateRequest itemToCreate);

	/**
	 * Change an already registered node for the notification service of the
	 * underlying system.
	 * 
	 * @param node         The node to change for the service.
	 * @param itemToChange Change request for a node with additional information.
	 * 
	 * @return GOOD if node could be changed, otherwise the corresponding code. if
	 *         we return StatusCodes.Bad_NodeIdUnknown then the rest of the caller
	 *         will be processed
	 */
	StatusCode changeEvent(Node node, MonitoredItemModifyRequest itemToChange);

	/**
	 * Register a new node for the notification service of the underlying system.
	 * 
	 * @param node         The node to register for the service.
	 * @param itemToCreate Create request for a node with additional information.
	 * 
	 * @return GOOD if node could be registered, otherwise the corresponding code.
	 *         if we return StatusCodes.Bad_NodeIdUnknown then the rest of the
	 *         caller will be processed
	 */
	StatusCode registerNotification(Node node, MonitoredItemCreateRequest itemToCreate);

	/**
	 * Change a registered node for the notification service of the underlying
	 * system.
	 * 
	 * @param node         The node to change for the service.
	 * @param itemToChange Modify request for a node with additional information.
	 * 
	 * @return GOOD if node could be changed, otherwise the corresponding code. if
	 *         we return StatusCodes.Bad_NodeIdUnknown then the rest of the caller
	 *         will be processed
	 */
	StatusCode changeNotification(Node node, MonitoredItemModifyRequest itemToChange);

	/**
	 * UnRegister an existing node from the notification service of the underlying
	 * system.
	 * 
	 * @param nodeId The nodeid to unregister a node from the service.
	 * 
	 * @return GOOD if node could be unregistered, otherwise the corresponding code.
	 *         if we return StatusCodes.Bad_NodeIdUnknown then the rest of the
	 *         caller will be processed
	 */
	StatusCode unregisterEvent(NodeId nodeId);

	/**
	 * UnRegister an existing node from the notification service of the underlying
	 * system.
	 * 
	 * @param nodeId The nodeid to unregister a node from the service.
	 * 
	 * @return GOOD if node could be unregistered, otherwise the corresponding code.
	 *         if we return StatusCodes.Bad_NodeIdUnknown then the rest of the
	 *         caller will be processed
	 */
	StatusCode unregisterNotification(NodeId nodeId);

	/**
	 * Modify a already registered node with the new parameters.
	 * 
	 * @param nodeId       The nodeid to modify the notification for a node.
	 * @param itemToModify Modify request for a node with additional information.
	 * 
	 * @return GOOD if notification for a node could be modified, otherwise the
	 *         corresponding code. if we return StatusCodes.Bad_NodeIdUnknown then
	 *         the rest of the caller will be processed
	 */
	StatusCode modifyNotification(NodeId nodeId, MonitoredItemModifyRequest itemToModify);
}
