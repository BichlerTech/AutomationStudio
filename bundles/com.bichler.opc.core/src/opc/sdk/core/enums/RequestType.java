package opc.sdk.core.enums;

/**
 * Enum of OPC UA request types.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum RequestType {
	/**
	 * The request type is not known.
	 */
	Unknown, FindServers, GetEndpoints, CreateSession, ActivateSession, CloseSession, Cancel, Read, HistoryRead, Write,
	HistoryUpdate, Call, CreateMonitoredItems, ModifyMonitoredItems, SetMonitoringMode, SetTriggering,
	DeleteMonitoredItems, CreateSubscription, ModifySubscription, SetPublishingMode, Publish, Republish,
	TransferSubscriptions, DeleteSubscriptions, AddNodes, AddReferences, DeleteNodes, DeleteReferences, Browse,
	BrowseNext, TranslateBrowsePathsToNodeIds, QueryFirst, QueryNext, RegisterNodes, UnregisterNodes, RegisterServer;
}
