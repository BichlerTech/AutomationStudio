package opc.sdk.server.core.managers;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionRequest;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesRequest;
import org.opcfoundation.ua.core.AddNodesResponse;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesRequest;
import org.opcfoundation.ua.core.AddReferencesResponse;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.BrowseNextRequest;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.CallResponse;
import org.opcfoundation.ua.core.CancelRequest;
import org.opcfoundation.ua.core.CancelResponse;
import org.opcfoundation.ua.core.CloseSessionRequest;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.CreateMonitoredItemsRequest;
import org.opcfoundation.ua.core.CreateMonitoredItemsResponse;
import org.opcfoundation.ua.core.CreateSessionRequest;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.opcfoundation.ua.core.CreateSubscriptionRequest;
import org.opcfoundation.ua.core.CreateSubscriptionResponse;
import org.opcfoundation.ua.core.DeleteMonitoredItemsRequest;
import org.opcfoundation.ua.core.DeleteMonitoredItemsResponse;
import org.opcfoundation.ua.core.DeleteNodesRequest;
import org.opcfoundation.ua.core.DeleteNodesResponse;
import org.opcfoundation.ua.core.DeleteReferencesRequest;
import org.opcfoundation.ua.core.DeleteReferencesResponse;
import org.opcfoundation.ua.core.DeleteSubscriptionsRequest;
import org.opcfoundation.ua.core.DeleteSubscriptionsResponse;
import org.opcfoundation.ua.core.FindServersRequest;
import org.opcfoundation.ua.core.FindServersResponse;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.HistoryUpdateResult;
import org.opcfoundation.ua.core.ModifyMonitoredItemsRequest;
import org.opcfoundation.ua.core.ModifyMonitoredItemsResponse;
import org.opcfoundation.ua.core.ModifySubscriptionRequest;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.MonitoredItemCreateResult;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.PublishRequest;
import org.opcfoundation.ua.core.PublishResponse;
import org.opcfoundation.ua.core.QueryFirstRequest;
import org.opcfoundation.ua.core.QueryFirstResponse;
import org.opcfoundation.ua.core.QueryNextRequest;
import org.opcfoundation.ua.core.QueryNextResponse;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.RegisterNodesRequest;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.RegisterServerRequest;
import org.opcfoundation.ua.core.RegisterServerResponse;
import org.opcfoundation.ua.core.RepublishRequest;
import org.opcfoundation.ua.core.RepublishResponse;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.SetMonitoringModeResponse;
import org.opcfoundation.ua.core.SetPublishingModeRequest;
import org.opcfoundation.ua.core.SetPublishingModeResponse;
import org.opcfoundation.ua.core.SetTriggeringRequest;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.TransferSubscriptionsRequest;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.UnregisterNodesRequest;
import org.opcfoundation.ua.core.UnregisterNodesResponse;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.transport.ServerSecureChannel;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.server.service.subscribe.OPCSubscription;

public class OPCProfileManager extends OPCServiceManager {
	public OPCProfileManager(OPCInternalServer server) {
		super(server);
	}

	@Override
	public ActivateSessionResponse activateSession(ActivateSessionRequest request, ServerSecureChannel channel)
			throws ServiceResultException {
		return this.masterManager.activateSession(request.getRequestHeader().getAuthenticationToken(),
				request.getClientSignature(), request.getClientSoftwareCertificates(), request.getLocaleIds(),
				request.getUserIdentityToken(), request.getUserTokenSignature(), channel);
	}

	@Override
	public AddNodesResponse addNode(AddNodesRequest request, OPCServerSession session) throws ServiceResultException {
		Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<ExpandedNodeId, AddNodesItem>();
		for (AddNodesItem item : request.getNodesToAdd()) {
			mappedNodes.put(item.getRequestedNewNodeId(), item);
		}
		AddNodesResult[] results = this.masterManager.addNodes(request.getNodesToAdd(), mappedNodes, true, session,
				false);
		AddNodesResponse response = new AddNodesResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public AddReferencesResponse addReferences(AddReferencesRequest request, OPCServerSession session)
			throws ServiceResultException {
		StatusCode[] results = this.masterManager.addReferences(request.getReferencesToAdd(), session);
		AddReferencesResponse response = new AddReferencesResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public BrowseResponse browse(BrowseRequest request, OPCServerSession session) throws ServiceResultException {
		BrowseResult[] results = this.masterManager.browse(request.getNodesToBrowse(),
				request.getRequestedMaxReferencesPerNode(), request.getView(), session);
		BrowseResponse response = new BrowseResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public BrowseNextResponse browseNext(BrowseNextRequest request, OPCServerSession session)
			throws ServiceResultException {
		BrowseResult[] results = this.masterManager.browseNext(request.getContinuationPoints(),
				request.getReleaseContinuationPoints(), session);
		BrowseNextResponse response = new BrowseNextResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public CallResponse call(CallRequest request, OPCServerSession session) throws ServiceResultException {
		CallMethodResult[] results = this.masterManager.call(request.getMethodsToCall(), session);
		CallResponse response = new CallResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public CancelResponse cancle(CancelRequest request, OPCServerSession session) throws ServiceResultException {
		UnsignedInteger cancelCount = this.masterManager.cancel(request.getRequestHandle(), session);
		CancelResponse response = new CancelResponse();
		response.setCancelCount(cancelCount);
		return response;
	}

	@Override
	public CreateMonitoredItemsResponse createMonitoredItem(CreateMonitoredItemsRequest request,
			OPCServerSession session) throws ServiceResultException {
		MonitoredItemCreateResult[] results = this.masterManager.createMonitoredItems(request.getSubscriptionId(),
				request.getItemsToCreate(), request.getTimestampsToReturn(), session);
		CreateMonitoredItemsResponse response = new CreateMonitoredItemsResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public CreateSessionResponse createSession(CreateSessionRequest request, ServerSecureChannel channel)
			throws ServiceResultException {
		CreateSessionResponse response = this.masterManager.createSession(request.getSessionName(),
				request.getEndpointUrl(), request.getServerUri(), request.getClientDescription(),
				request.getClientCertificate(), request.getClientNonce(), request.getMaxResponseMessageSize(),
				request.getRequestedSessionTimeout(), channel);
		return response;
	}

	@Override
	public CreateSubscriptionResponse createSubscription(CreateSubscriptionRequest request, OPCServerSession session)
			throws ServiceResultException {
		OPCSubscription opcSubscription = this.masterManager.createSubscription(
				request.getRequestedPublishingInterval(), request.getRequestedLifetimeCount(),
				request.getRequestedMaxKeepAliveCount(), request.getPublishingEnabled(),
				request.getMaxNotificationsPerPublish(), request.getPriority(), session);
		CreateSubscriptionResponse response = new CreateSubscriptionResponse();
		response.setRevisedLifetimeCount(opcSubscription.getMaxLifeTimeCount());
		response.setRevisedMaxKeepAliveCount(opcSubscription.getMaxKeepAliveCount());
		response.setRevisedPublishingInterval(opcSubscription.getPublishingInterval());
		response.setSubscriptionId(opcSubscription.getSubscriptionId());
		return response;
	}

	@Override
	public CloseSessionResponse closeSession(CloseSessionRequest request, OPCServerSession session)
			throws ServiceResultException {
		CloseSessionResponse response = this.masterManager.closeSession(request.getDeleteSubscriptions(), session);
		return response;
	}

	@Override
	public DeleteMonitoredItemsResponse deleteMonitoredItems(DeleteMonitoredItemsRequest request,
			OPCServerSession session) throws ServiceResultException {
		StatusCode[] results = this.masterManager.deleteMonitoredItems(request.getSubscriptionId(),
				request.getMonitoredItemIds(), session);
		DeleteMonitoredItemsResponse response = new DeleteMonitoredItemsResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public DeleteNodesResponse deleteNodes(DeleteNodesRequest request, OPCServerSession session)
			throws ServiceResultException {
		StatusCode[] results = this.masterManager.deleteNodes(request.getNodesToDelete(), session);
		DeleteNodesResponse response = new DeleteNodesResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public DeleteReferencesResponse deleteReference(DeleteReferencesRequest request, OPCServerSession session)
			throws ServiceResultException {
		StatusCode[] results = this.masterManager.deleteReference(request.getReferencesToDelete(), session);
		DeleteReferencesResponse response = new DeleteReferencesResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public DeleteSubscriptionsResponse deleteSubscription(DeleteSubscriptionsRequest request, OPCServerSession session)
			throws ServiceResultException {
		StatusCode[] results = this.masterManager.deleteSubscriptions(request.getSubscriptionIds());
		DeleteSubscriptionsResponse response = new DeleteSubscriptionsResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public FindServersResponse findServers(FindServersRequest request) throws ServiceResultException {
		ApplicationDescription[] results = this.masterManager.findServers(request.getEndpointUrl(),
				request.getServerUris(), request.getLocaleIds());
		FindServersResponse response = new FindServersResponse();
		response.setServers(results);
		return response;
	}

	@Override
	public HistoryReadResponse historyRead(HistoryReadRequest request, Long[] driverState, OPCServerSession session)
			throws ServiceResultException {
		HistoryReadResult[] results = this.masterManager.historyRead(request.getNodesToRead(),
				request.getHistoryReadDetails(), request.getReleaseContinuationPoints(),
				request.getTimestampsToReturn(), driverState, session);
		HistoryReadResponse response = new HistoryReadResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public HistoryUpdateResponse historyUpdate(HistoryUpdateRequest request, OPCServerSession session)
			throws ServiceResultException {
		HistoryUpdateResult[] results = this.masterManager.historyUpdate(request.getHistoryUpdateDetails(), session);
		HistoryUpdateResponse response = new HistoryUpdateResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public ModifyMonitoredItemsResponse modifyMonitoredItems(ModifyMonitoredItemsRequest request,
			OPCServerSession session) throws ServiceResultException {
		MonitoredItemModifyResult[] results = this.masterManager.modifyMonitoredItems(request.getSubscriptionId(),
				request.getItemsToModify(), request.getTimestampsToReturn(), session);
		ModifyMonitoredItemsResponse response = new ModifyMonitoredItemsResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public ModifySubscriptionResponse modifySubscription(ModifySubscriptionRequest request, OPCServerSession session)
			throws ServiceResultException {
		return this.masterManager.modifySubscription(request.getSubscriptionId(),
				request.getRequestedPublishingInterval(), request.getRequestedMaxKeepAliveCount(),
				request.getRequestedLifetimeCount(), request.getMaxNotificationsPerPublish(), request.getPriority(),
				session);
	}

	public void publish(EndpointServiceRequest<PublishRequest, PublishResponse> request) throws ServiceResultException {
		this.masterManager.publish(request);
	}

	@Override
	public QueryFirstResponse queryFirst(QueryFirstRequest request) throws ServiceResultException {
		return this.masterManager.queryFirst(request.getNodeTypes(), request.getMaxDataSetsToReturn(),
				request.getMaxReferencesToReturn(), request.getView());
	}

	@Override
	public QueryNextResponse queryNext(QueryNextRequest request) throws ServiceResultException {
		return this.masterManager.queryNext(request.getContinuationPoint(), request.getReleaseContinuationPoint());
	}

	@Override
	public ReadResponse read(ReadRequest request, Long[] driverState, OPCServerSession session)
			throws ServiceResultException {
		DataValue[] values = this.masterManager.read(request.getNodesToRead(), request.getMaxAge(),
				request.getTimestampsToReturn(), driverState, session);
		ReadResponse response = new ReadResponse();
		response.setResults(values);
		return response;
	}

	@Override
	public RegisterNodesResponse registerNodes(RegisterNodesRequest request, OPCServerSession session)
			throws ServiceResultException {
		NodeId[] results = this.masterManager.registerNodes(request.getNodesToRegister(), session);
		RegisterNodesResponse response = new RegisterNodesResponse();
		response.setRegisteredNodeIds(results);
		return response;
	}

	@Override
	public RegisterServerResponse registerServer(ServerSecureChannel channel, RegisterServerRequest request)
			throws ServiceResultException {
		this.masterManager.registerServer(channel, request.getServer());
		RegisterServerResponse response = new RegisterServerResponse();
		return response;
	}

	@Override
	public RepublishResponse republish(RepublishRequest request) throws ServiceResultException {
		return this.masterManager.republish(request.getSubscriptionId(), request.getRetransmitSequenceNumber());
	}

	@Override
	public SetMonitoringModeResponse setMonitoringMode(SetMonitoringModeRequest request, OPCServerSession session)
			throws ServiceResultException {
		StatusCode[] results = this.masterManager.setMonitoringMode(request.getSubscriptionId(),
				request.getMonitoredItemIds(), request.getMonitoringMode(), session);
		SetMonitoringModeResponse response = new SetMonitoringModeResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public SetPublishingModeResponse setPublishingMode(SetPublishingModeRequest request, OPCServerSession session)
			throws ServiceResultException {
		StatusCode[] results = this.masterManager.setPublishingMode(request.getSubscriptionIds(),
				request.getPublishingEnabled(), session);
		SetPublishingModeResponse response = new SetPublishingModeResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public SetTriggeringResponse setTriggering(SetTriggeringRequest request, OPCServerSession session)
			throws ServiceResultException {
		return this.masterManager.setTriggering(request.getSubscriptionId(), request.getTriggeringItemId(),
				request.getLinksToAdd(), request.getLinksToRemove(), session);
	}

	@Override
	public TranslateBrowsePathsToNodeIdsResponse translateBrowsePathsToNodeIds(
			TranslateBrowsePathsToNodeIdsRequest request, OPCServerSession session) throws ServiceResultException {
		BrowsePathResult[] results = this.masterManager.translateBrowsePathsToNodeIds(request.getBrowsePaths(),
				session);
		TranslateBrowsePathsToNodeIdsResponse response = new TranslateBrowsePathsToNodeIdsResponse();
		response.setResults(results);
		return response;
	}

	@Override
	public TransferSubscriptionsResponse transferSubscription(TransferSubscriptionsRequest request,
			OPCServerSession session) throws ServiceResultException {
		return this.masterManager.transferSubscription(request.getSubscriptionIds(), request.getSendInitialValues(),
				session);
	}

	@Override
	public UnregisterNodesResponse unregisterNodes(UnregisterNodesRequest request, OPCServerSession session)
			throws ServiceResultException {
		this.masterManager.unregisterNodes(request.getNodesToUnregister(), session);
		UnregisterNodesResponse response = new UnregisterNodesResponse();
		return response;
	}

	@Override
	public WriteResponse write(WriteRequest request, boolean skipAccessLevel, Long[] driverState,
			OPCServerSession session) throws ServiceResultException {
		StatusCode[] results = this.masterManager.write(request.getNodesToWrite(), skipAccessLevel, driverState,
				session);
		WriteResponse response = new WriteResponse();
		response.setResults(results);
		return response;
	}

	boolean hasInitialized = false;

	@Override
	public boolean isInitialized() {
		return this.hasInitialized;
	}
}
