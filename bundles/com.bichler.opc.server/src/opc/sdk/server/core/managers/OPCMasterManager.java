package opc.sdk.server.core.managers;

import java.util.Map;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowsePath;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.CallMethodRequest;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.DeleteReferencesItem;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryReadValueId;
import org.opcfoundation.ua.core.HistoryUpdateResult;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoredItemCreateResult;
import org.opcfoundation.ua.core.MonitoredItemModifyRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NodeTypeDescription;
import org.opcfoundation.ua.core.PublishRequest;
import org.opcfoundation.ua.core.PublishResponse;
import org.opcfoundation.ua.core.QueryFirstResponse;
import org.opcfoundation.ua.core.QueryNextResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.RegisteredServer;
import org.opcfoundation.ua.core.RepublishResponse;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.SignedSoftwareCertificate;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.core.ViewDescription;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.transport.ServerSecureChannel;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

import com.bichler.opc.comdrv.IOPCMasterManager;

import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.server.service.subscribe.OPCPublishOperation;
import opc.sdk.server.service.subscribe.OPCSubscription;
import opc.sdk.ua.classes.BaseEventType;

public class OPCMasterManager implements IOPCMasterManager {
	private OPCAddressSpaceManager addressSpaceManager;
	private OPCSubscriptionManager subscriptionManager;
	private OPCSessionManager sessionManager;
	private OPCDiscoveryManager discoveryManager;

	/**
	 * Synchronizes incoming requests
	 * 
	 * @param server
	 */
	public OPCMasterManager(OPCInternalServer server) {
		this.addressSpaceManager = server.getAddressSpaceManager();
		this.subscriptionManager = server.getSubscriptionManager();
		this.sessionManager = server.getSessionManager();
		this.discoveryManager = server.getDiscoveryManager();
		server.setMaster(this);
		// set user manager
		this.addressSpaceManager.setUserAuthorityManager(server.getUserAuthentifiationManager());
		this.subscriptionManager.setUserAuthorityManager(server.getUserAuthentifiationManager());
	}

	public ActivateSessionResponse activateSession(NodeId sessionId, SignatureData clientSignature,
			SignedSoftwareCertificate[] clientSoftwareCertificates, String[] localeIds,
			ExtensionObject userIdentityToken, SignatureData userTokenSignature, ServerSecureChannel channel)
			throws ServiceResultException {
		ActivateSessionResponse response = this.sessionManager.activateSession(sessionId, clientSignature,
				clientSoftwareCertificates, localeIds, userIdentityToken, userTokenSignature, channel);
		return response;
	}

	/**
	 * 
	 * @param nodesToAdd
	 * @param Only       If False, only creates the nodes, if True, node is created
	 *                   with its type children
	 * @return
	 * @throws ServiceResultException
	 */
	public AddNodesResult[] addNodes(AddNodesItem[] nodesToAdd, Map<ExpandedNodeId, AddNodesItem> mappedNodes,
			boolean includeStructure, OPCServerSession session, boolean includeParentComponents)
			throws ServiceResultException {
		return this.addressSpaceManager.addNodes(nodesToAdd, mappedNodes, includeStructure, session,
				includeParentComponents);
	}

	/**
	 * 
	 * @param nodesToAdd
	 * @param Only       If False, only creates the nodes, if True, node is created
	 *                   with its type children
	 * @return
	 * @throws ServiceResultException
	 */
	public AddNodesResult[] addNodes(AddNodesItem[] nodesToAdd, Map<ExpandedNodeId, AddNodesItem> mappedNodes,
			boolean includeStructure, OPCServerSession session, boolean includeParentComponents,
			boolean createOptionalPlaceholder) throws ServiceResultException {
		return this.addressSpaceManager.addNodes(nodesToAdd, mappedNodes, includeStructure, session,
				includeParentComponents, createOptionalPlaceholder, true);
	}

	/**
	 * 
	 * @param nodesToAdd
	 * @param Only       If False, only creates the nodes, if True, node is created
	 *                   with its type children
	 * @return
	 * @throws ServiceResultException
	 */
	public AddNodesResult[] addNodes(AddNodesItem[] nodesToAdd, Map<ExpandedNodeId, AddNodesItem> mappedNodes,
			boolean includeStructure, OPCServerSession session, boolean includeParentComponents,
			boolean createOptionalPlaceholder, boolean addModellingRule) throws ServiceResultException {
		return this.addressSpaceManager.addNodes(nodesToAdd, mappedNodes, includeStructure, session,
				includeParentComponents, createOptionalPlaceholder, addModellingRule);
	}

	public StatusCode[] addReferences(AddReferencesItem[] referencesToAdd, OPCServerSession session)
			throws ServiceResultException {
		return this.addressSpaceManager.addReference(referencesToAdd, session);
	}

	/**
	 * incoming from service
	 */
	public BrowseResult[] browse(BrowseDescription[] nodesToBrowse, UnsignedInteger requestedMaxReferencesPerNode,
			ViewDescription view, OPCServerSession session) throws ServiceResultException {
		return this.addressSpaceManager.browse(nodesToBrowse, requestedMaxReferencesPerNode, view, session);
	}

	@Override
	public BrowseResult[] browse(BrowseDescription[] browseDescriptions, UnsignedInteger unsignedInteger, Object object,
			Object object2) throws ServiceResultException {
		if (object != null && (object instanceof ViewDescription))
			throw new ServiceResultException(StatusCodes.Bad_InvalidArgument);
		if (object2 != null && (object2 instanceof OPCServerSession))
			throw new ServiceResultException(StatusCodes.Bad_InvalidArgument);
		return this.addressSpaceManager.browse(browseDescriptions, unsignedInteger, (ViewDescription) object,
				(OPCServerSession) object2);
	}

	public BrowseResult[] browseNext(ByteString[] continuationPoints, Boolean releaseContinuationPoints,
			OPCServerSession session) throws ServiceResultException {
		return this.addressSpaceManager.browseNext(continuationPoints, releaseContinuationPoints, session);
	}

	public CallMethodResult[] call(CallMethodRequest[] methodsToCall, OPCServerSession session)
			throws ServiceResultException {
		return this.addressSpaceManager.call(methodsToCall, session);
	}

	public UnsignedInteger cancel(UnsignedInteger requestHandle, OPCServerSession session)
			throws ServiceResultException {
		return this.sessionManager.cancel(requestHandle, session);
	}

	public MonitoredItemCreateResult[] createMonitoredItems(UnsignedInteger subscriptionId,
			MonitoredItemCreateRequest[] itemsToCreate, TimestampsToReturn timestampsToReturn, OPCServerSession session)
			throws ServiceResultException {
		return this.subscriptionManager.createMonitoredItems(subscriptionId, itemsToCreate, timestampsToReturn,
				session);
	}

	public CreateSessionResponse createSession(String sessionName, String endpointUrl, String serverUri,
			ApplicationDescription clientDescription, ByteString clientCertificate, ByteString clientNonce,
			UnsignedInteger maxResponseMessageSize, Double requestedSessionTimeout, ServerSecureChannel channel)
			throws ServiceResultException {
		return this.sessionManager.createSession(sessionName, endpointUrl, serverUri, clientDescription,
				clientCertificate, clientNonce, maxResponseMessageSize, requestedSessionTimeout, channel);
	}

	public OPCSubscription createSubscription(Double requestedPublishingInterval,
			UnsignedInteger requestedLifetimeCount, UnsignedInteger requestedMaxKeepAliveCount,
			Boolean publishingEnabled, UnsignedInteger maxNotificationsPerPublish, UnsignedByte priority,
			OPCServerSession session) throws ServiceResultException {
		return this.subscriptionManager.createSubscription(requestedPublishingInterval, requestedLifetimeCount,
				requestedMaxKeepAliveCount, publishingEnabled, maxNotificationsPerPublish, priority, session);
	}

	public CloseSessionResponse closeSession(Boolean deleteSubscriptions, OPCServerSession session)
			throws ServiceResultException {
		return this.sessionManager.closeSession(deleteSubscriptions, session);
	}

	public StatusCode[] deleteMonitoredItems(UnsignedInteger subscriptionId, UnsignedInteger[] monitoredItemIds,
			OPCServerSession session) throws ServiceResultException {
		return this.subscriptionManager.deleteMonitoredItems(subscriptionId, monitoredItemIds, session);
	}

	public StatusCode[] deleteNodes(DeleteNodesItem[] nodesToDelete, OPCServerSession session)
			throws ServiceResultException {
		return this.addressSpaceManager.deleteNodes(nodesToDelete, session);
	}

	public StatusCode deleteNode(NodeId nodeToDelete, boolean deleteTargetReferences) throws ServiceResultException {
		return this.addressSpaceManager.deleteNode(nodeToDelete, deleteTargetReferences);
	}

	public StatusCode[] deleteReference(DeleteReferencesItem[] referencesToDelete, OPCServerSession session)
			throws ServiceResultException {
		return this.addressSpaceManager.deleteReferences(referencesToDelete, session);
	}

	public StatusCode[] deleteSubscriptions(UnsignedInteger[] subscriptionIds) throws ServiceResultException {
		return this.subscriptionManager.deleteSubscriptions(subscriptionIds);
	}

	public HistoryReadResult[] historyEventRead(HistoryReadValueId[] nodesToRead, ExtensionObject historyReadDetails,
			Boolean releaseContinuationPoints, TimestampsToReturn timestampsToReturn, Long[] driverStates,
			OPCServerSession session) throws ServiceResultException {
		return this.subscriptionManager.historyEventRead(nodesToRead, historyReadDetails, releaseContinuationPoints,
				timestampsToReturn, driverStates, session);
	}

	public ApplicationDescription[] findServers(String endpointUrl, String[] serverUris, String[] localeIds)
			throws ServiceResultException {
		return this.discoveryManager.findServers(endpointUrl, serverUris, localeIds);
	}

	public HistoryReadResult[] historyRead(HistoryReadValueId[] nodesToRead, ExtensionObject historyReadDetails,
			Boolean releaseContinuationPoints, TimestampsToReturn timestampsToReturn, Long[] driverStates,
			OPCServerSession session) throws ServiceResultException {
		return this.addressSpaceManager.historyRead(nodesToRead, historyReadDetails, releaseContinuationPoints,
				timestampsToReturn, driverStates, session);
	}

	public HistoryUpdateResult[] historyUpdate(ExtensionObject[] historyUpdateDetails, OPCServerSession session)
			throws ServiceResultException {
		return this.addressSpaceManager.historyUpdate(historyUpdateDetails, session);
	}

	public MonitoredItemModifyResult[] modifyMonitoredItems(UnsignedInteger subscriptionId,
			MonitoredItemModifyRequest[] itemsToModify, TimestampsToReturn timestampsToReturn, OPCServerSession session)
			throws ServiceResultException {
		return this.subscriptionManager.modifyMonitoredItems(subscriptionId, itemsToModify, timestampsToReturn,
				session);
	}

	public ModifySubscriptionResponse modifySubscription(UnsignedInteger subscriptionId,
			Double requestedPublishingInterval, UnsignedInteger requestedMaxKeepAliveCount,
			UnsignedInteger requestedLifetimeCount, UnsignedInteger maxNotificationsPerPublish, UnsignedByte priority,
			OPCServerSession session) throws ServiceResultException {
		return this.subscriptionManager.modifySubscription(subscriptionId, requestedPublishingInterval,
				requestedMaxKeepAliveCount, requestedLifetimeCount, maxNotificationsPerPublish, priority, session);
	}

	public StatusCode[] setMonitoringMode(UnsignedInteger subscriptionId, UnsignedInteger[] monitoredItemIds,
			MonitoringMode monitoringMode, OPCServerSession session) throws ServiceResultException {
		return this.subscriptionManager.setMonitoringMode(subscriptionId, monitoredItemIds, monitoringMode, session);
	}

	public void publish(EndpointServiceRequest<PublishRequest, PublishResponse> request) throws ServiceResultException {
		// wrapped publish operation for server
		OPCPublishOperation operation = new OPCPublishOperation(request);
		this.subscriptionManager.publish(operation.getRequestPublishAcknowledge(), operation, false);
	}

	public QueryFirstResponse queryFirst(NodeTypeDescription[] nodeTypes, UnsignedInteger maxDataSetsToReturn,
			UnsignedInteger maxReferencesToReturn, ViewDescription view) throws ServiceResultException {
		return null;
	}

	public QueryNextResponse queryNext(ByteString continuationPoint, Boolean releaseContinuationPoint)
			throws ServiceResultException {
		return null;
	}

	public DataValue[] read(ReadValueId[] nodesToRead, Double maxAge, TimestampsToReturn timestampsToReturn,
			Long[] driverStates, OPCServerSession session) throws ServiceResultException {
		return this.addressSpaceManager.read(nodesToRead, maxAge, timestampsToReturn, driverStates, session);
	}

	public NodeId[] registerNodes(NodeId[] nodesToRegister, OPCServerSession session) throws ServiceResultException {
		return this.addressSpaceManager.registerNodes(nodesToRegister, session);
	}

	public void registerServer(ServerSecureChannel channel, RegisteredServer serverToRegister)
			throws ServiceResultException {
		this.discoveryManager.registerServer(channel, serverToRegister);
	}

	public RepublishResponse republish(UnsignedInteger subscriptionId, UnsignedInteger retransmitSequenceNumber)
			throws ServiceResultException {
		return this.subscriptionManager.republish(subscriptionId, retransmitSequenceNumber);
	}

	public StatusCode[] setPublishingMode(UnsignedInteger[] subscriptionIds, Boolean publishingEnabled,
			OPCServerSession session) throws ServiceResultException {
		return this.subscriptionManager.setPublishingMode(subscriptionIds, publishingEnabled, session);
	}

	public SetTriggeringResponse setTriggering(UnsignedInteger subscriptionId, UnsignedInteger triggeringItemId,
			UnsignedInteger[] linksToAdd, UnsignedInteger[] linksToRemove, OPCServerSession session)
			throws ServiceResultException {
		return this.subscriptionManager.setTriggering(subscriptionId, triggeringItemId, linksToAdd, linksToRemove,
				session);
	}

	@Override
	public BrowsePathResult[] translateBrowsePathsToNodeIds(BrowsePath[] browsePaths, Object session)
			throws ServiceResultException {
		if (session == null)
			return this.addressSpaceManager.tranlsateBrowsePathsToNodeIds(browsePaths, null);
		else if (session instanceof OPCServerSession)
			return this.addressSpaceManager.tranlsateBrowsePathsToNodeIds(browsePaths, (OPCServerSession) session);
		return new BrowsePathResult[0];
	}

	public TransferSubscriptionsResponse transferSubscription(UnsignedInteger[] subscriptionIds,
			Boolean sendInitialValues, OPCServerSession session) throws ServiceResultException {
		return this.subscriptionManager.transferSubscription(subscriptionIds, sendInitialValues, session);
	}

	public void unregisterNodes(NodeId[] nodesToUnregister, OPCServerSession session) throws ServiceResultException {
		this.addressSpaceManager.unregisterNodes(nodesToUnregister, session);
	}

	public StatusCode[] write(WriteValue[] nodesToWrite, boolean skipAccessLevel, Long[] driverState,
			OPCServerSession session) throws ServiceResultException {
		return this.addressSpaceManager.write(nodesToWrite, skipAccessLevel, driverState, session);
	}

	public StatusCode[] reportEvent(NodeId[] nodeIds, BaseEventType[] eventStates, Long[] dpstates) {
		return this.addressSpaceManager.reportEvent(nodeIds, eventStates, dpstates);
	}
}
