package opc.sdk.server.core.managers;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ServiceRequest;
import org.opcfoundation.ua.builtintypes.ServiceResponse;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionRequest;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AddNodesRequest;
import org.opcfoundation.ua.core.AddNodesResponse;
import org.opcfoundation.ua.core.AddReferencesRequest;
import org.opcfoundation.ua.core.AddReferencesResponse;
import org.opcfoundation.ua.core.BrowseNextRequest;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
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
import org.opcfoundation.ua.core.GetEndpointsRequest;
import org.opcfoundation.ua.core.GetEndpointsResponse;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.ModifyMonitoredItemsRequest;
import org.opcfoundation.ua.core.ModifyMonitoredItemsResponse;
import org.opcfoundation.ua.core.ModifySubscriptionRequest;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
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
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.ResponseHeader;
import org.opcfoundation.ua.core.ServerState;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.SetMonitoringModeResponse;
import org.opcfoundation.ua.core.SetPublishingModeRequest;
import org.opcfoundation.ua.core.SetPublishingModeResponse;
import org.opcfoundation.ua.core.SetTriggeringRequest;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.StatusCodes;
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
import org.opcfoundation.ua.utils.ObjectUtils;

import com.bichler.opc.comdrv.ComDRVManager;

import opc.sdk.core.enums.RequestType;
import opc.sdk.core.result.HBResponseHeader;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.profile.AttributeService;
import opc.sdk.server.service.profile.FindServerDiscovery;
import opc.sdk.server.service.profile.MethodService;
import opc.sdk.server.service.profile.MonitoringService;
import opc.sdk.server.service.profile.NodeService;
import opc.sdk.server.service.profile.SessionService;
import opc.sdk.server.service.profile.SubscriptionService;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.server.service.util.ServiceUtil;

public abstract class OPCServiceManager implements IOPCManager {
	private OPCInternalServer server = null;
	/** master manager handels incoming services */
	protected OPCMasterManager masterManager = null;
	private boolean hasInitialized;

	public OPCServiceManager(OPCInternalServer server) {
		this.server = server;
		this.masterManager = new OPCMasterManager(server);
		initServices();
	}

	/**
	 * Initialize OPC UA services
	 */
	private void initServices() {
		// attribute service
		AttributeService attribute = new AttributeService(this);
		this.server.addServiceHandler(attribute);
		// find server
		FindServerDiscovery findServer = new FindServerDiscovery(this.server);
		this.server.addServiceHandler(findServer);
		// discovery
		// method service
		MethodService method = new MethodService(this);
		this.server.addServiceHandler(method);
		// monitoring service
		MonitoringService monitoring = new MonitoringService(this);
		this.server.addServiceHandler(monitoring);
		// node service
		NodeService node = new NodeService(this);
		this.server.addServiceHandler(node);
		// session service
		SessionService session = new SessionService(this);
		this.server.addServiceHandler(session);
		// subscription service
		SubscriptionService subscription = new SubscriptionService(this);
		this.server.addServiceHandler(subscription);
	}

	public void onErrorRequest(RequestType requestType,
			EndpointServiceRequest<? extends ServiceRequest, ? extends ServiceResponse> req, ServiceResultException e) {
		/** server diagnostics */
		if (ServiceUtil.isSecurityError(e.getStatusCode())) {
		}
		// unsecure
		// send service error
		ServiceResponse response;
		switch (requestType) {
		case ActivateSession:
			response = new ActivateSessionResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<ActivateSessionRequest, ActivateSessionResponse>) req)
					.sendResponse((ActivateSessionResponse) response);
			break;
		case AddNodes:
			response = new AddNodesResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<AddNodesRequest, AddNodesResponse>) req).sendResponse((AddNodesResponse) response);
			break;
		case AddReferences:
			response = new AddReferencesResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<AddReferencesRequest, AddReferencesResponse>) req)
					.sendResponse((AddReferencesResponse) response);
			break;
		case Browse:
			response = new BrowseResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<BrowseRequest, BrowseResponse>) req).sendResponse((BrowseResponse) response);
			break;
		case BrowseNext:
			response = new BrowseNextResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<BrowseNextRequest, BrowseNextResponse>) req)
					.sendResponse((BrowseNextResponse) response);
			break;
		case Call:
			response = new CallResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<CallRequest, CallResponse>) req).sendResponse((CallResponse) response);
			break;
		case Cancel:
			response = new CancelResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<CancelRequest, CancelResponse>) req).sendResponse((CancelResponse) response);
			break;
		case CloseSession:
			response = new CloseSessionResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<CloseSessionRequest, CloseSessionResponse>) req)
					.sendResponse((CloseSessionResponse) response);
			break;
		case CreateMonitoredItems:
			response = new CreateMonitoredItemsResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<CreateMonitoredItemsRequest, CreateMonitoredItemsResponse>) req)
					.sendResponse((CreateMonitoredItemsResponse) response);
			break;
		case CreateSession:
			response = new CreateSessionResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<CreateSessionRequest, CreateSessionResponse>) req)
					.sendResponse((CreateSessionResponse) response);
			break;
		case CreateSubscription:
			response = new CreateSubscriptionResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<CreateSubscriptionRequest, CreateSubscriptionResponse>) req)
					.sendResponse((CreateSubscriptionResponse) response);
			break;
		case DeleteMonitoredItems:
			response = new DeleteMonitoredItemsResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<DeleteMonitoredItemsRequest, DeleteMonitoredItemsResponse>) req)
					.sendResponse((DeleteMonitoredItemsResponse) response);
			break;
		case DeleteNodes:
			response = new DeleteNodesResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<DeleteNodesRequest, DeleteNodesResponse>) req)
					.sendResponse((DeleteNodesResponse) response);
			break;
		case DeleteReferences:
			response = new DeleteReferencesResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<DeleteReferencesRequest, DeleteReferencesResponse>) req)
					.sendResponse((DeleteReferencesResponse) response);
			break;
		case DeleteSubscriptions:
			response = new DeleteSubscriptionsResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<DeleteSubscriptionsRequest, DeleteSubscriptionsResponse>) req)
					.sendResponse((DeleteSubscriptionsResponse) response);
			break;
		case FindServers:
			response = new FindServersResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<FindServersRequest, FindServersResponse>) req)
					.sendResponse((FindServersResponse) response);
			break;
		case GetEndpoints:
			response = new GetEndpointsResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<GetEndpointsRequest, GetEndpointsResponse>) req)
					.sendResponse((GetEndpointsResponse) response);
			break;
		case HistoryRead:
			response = new HistoryReadResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<HistoryReadRequest, HistoryReadResponse>) req)
					.sendResponse((HistoryReadResponse) response);
			break;
		case HistoryUpdate:
			response = new HistoryUpdateResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<HistoryUpdateRequest, HistoryUpdateResponse>) req)
					.sendResponse((HistoryUpdateResponse) response);
			break;
		case ModifyMonitoredItems:
			response = new ModifyMonitoredItemsResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<ModifyMonitoredItemsRequest, ModifyMonitoredItemsResponse>) req)
					.sendResponse((ModifyMonitoredItemsResponse) response);
			break;
		case ModifySubscription:
			response = new ModifySubscriptionResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<ModifySubscriptionRequest, ModifySubscriptionResponse>) req)
					.sendResponse((ModifySubscriptionResponse) response);
			break;
		case Publish:
			response = new PublishResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<PublishRequest, PublishResponse>) req).sendResponse((PublishResponse) response);
			break;
		case QueryFirst:
			response = new QueryFirstResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<QueryFirstRequest, QueryFirstResponse>) req)
					.sendResponse((QueryFirstResponse) response);
			break;
		case QueryNext:
			response = new QueryNextResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<QueryNextRequest, QueryNextResponse>) req)
					.sendResponse((QueryNextResponse) response);
			break;
		case Read:
			response = new ReadResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<ReadRequest, ReadResponse>) req).sendResponse((ReadResponse) response);
			break;
		case RegisterNodes:
			response = new RegisterNodesResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<RegisterNodesRequest, RegisterNodesResponse>) req)
					.sendResponse((RegisterNodesResponse) response);
			break;
		case RegisterServer:
			response = new RegisterServerResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<RegisterServerRequest, RegisterServerResponse>) req)
					.sendResponse((RegisterServerResponse) response);
			break;
		case Republish:
			response = new RepublishResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<RepublishRequest, RepublishResponse>) req)
					.sendResponse((RepublishResponse) response);
			break;
		case SetMonitoringMode:
			response = new SetMonitoringModeResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<SetMonitoringModeRequest, SetMonitoringModeResponse>) req)
					.sendResponse((SetMonitoringModeResponse) response);
			break;
		case SetPublishingMode:
			response = new SetPublishingModeResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<SetPublishingModeRequest, SetPublishingModeResponse>) req)
					.sendResponse((SetPublishingModeResponse) response);
			break;
		case SetTriggering:
			response = new SetTriggeringResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<SetTriggeringRequest, SetTriggeringResponse>) req)
					.sendResponse((SetTriggeringResponse) response);
			break;
		case TransferSubscriptions:
			response = new TransferSubscriptionsResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<TransferSubscriptionsRequest, TransferSubscriptionsResponse>) req)
					.sendResponse((TransferSubscriptionsResponse) response);
			break;
		case TranslateBrowsePathsToNodeIds:
			response = new TranslateBrowsePathsToNodeIdsResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<TranslateBrowsePathsToNodeIdsRequest, TranslateBrowsePathsToNodeIdsResponse>) req)
					.sendResponse((TranslateBrowsePathsToNodeIdsResponse) response);
			break;
		case UnregisterNodes:
			response = new UnregisterNodesResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<UnregisterNodesRequest, UnregisterNodesResponse>) req)
					.sendResponse((UnregisterNodesResponse) response);
			break;
		case Write:
			response = new WriteResponse();
			doResponseHeader(response, req.getRequest(), e);
			((EndpointServiceRequest<WriteRequest, WriteResponse>) req).sendResponse((WriteResponse) response);
			break;
		case Unknown:
		default:
			req.sendException(e);
			break;
		}
	}

	public OPCServerSession onReceiveRequest(RequestType requestType,
			EndpointServiceRequest<? extends ServiceRequest, ? extends ServiceResponse> req)
			throws ServiceResultException {
		ServiceRequest request = req.getRequest();
		int secureChannelId = req.getChannel().getSecureChannelId();
		OPCServerSession serverSession = validateRequest(requestType, request, secureChannelId);
		if (requestType == RequestType.CreateSession) {
			//
			ComDRVManager.getDRVManager().createSession(serverSession);
		}
		return serverSession;
	}

	public void doResponseHeader(ServiceResponse response, ServiceRequest request, ServiceResultException e) {
		ResponseHeader responseHeader = new HBResponseHeader();
		if (e != null) {
			responseHeader.setServiceResult(e.getStatusCode());
		} else {
			responseHeader.setServiceResult(StatusCode.GOOD);
		}
		responseHeader.setRequestHandle(request.getRequestHeader().getRequestHandle());
		responseHeader.setTimestamp(DateTime.currentTime());
		responseHeader.setStringTable(new String[0]);
		response.setResponseHeader(responseHeader);
	}

	protected void doResponseHeader(ServiceResponse response, ServiceRequest request) {
		doResponseHeader(response, request, null);
	}

	private OPCServerSession validateRequest(RequestType requestType, ServiceRequest request, int secureChannelId)
			throws ServiceResultException {
		if (this.server.getState() != ServerState.Running.getValue()) {
			throw new ServiceResultException(StatusCodes.Bad_ServerHalted);
		}
		RequestHeader requestHeader = request.getRequestHeader();
		if (requestHeader == null) {
			throw new ServiceResultException(StatusCodes.Bad_RequestHeaderInvalid);
		}
		return this.server.getSessionManager().validateRequest(requestType, requestHeader, secureChannelId);
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

	@Override
	public OPCInternalServer getServer() {
		return this.server;
	}

	public void onActivateSession(EndpointServiceRequest<ActivateSessionRequest, ActivateSessionResponse> req)
			throws ServiceFaultException {
		try {
			onReceiveRequest(RequestType.ActivateSession, req);
			ActivateSessionResponse response = activateSession(req.getRequest(), req.getChannel());
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.ActivateSession, req, e);
		}
	}

	public void onAddNodes(EndpointServiceRequest<AddNodesRequest, AddNodesResponse> req) throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.AddNodes, req);
			AddNodesResponse response = addNode(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.AddNodes, req, e);
		}
	}

	public void onAddReference(EndpointServiceRequest<AddReferencesRequest, AddReferencesResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.AddReferences, req);
			AddReferencesResponse response = addReferences(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.AddReferences, req, e);
		}
	}

	public void onBrowse(EndpointServiceRequest<BrowseRequest, BrowseResponse> req) throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.Browse, req);
			BrowseResponse response = browse(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.Browse, req, e);
		}
	}

	public void onBrowseNext(EndpointServiceRequest<BrowseNextRequest, BrowseNextResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.BrowseNext, req);
			BrowseNextResponse response = browseNext(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.BrowseNext, req, e);
		}
	}

	public void onCall(EndpointServiceRequest<CallRequest, CallResponse> req) throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.Call, req);
			CallResponse response = call(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.Call, req, e);
		}
	}

	public void onCancle(EndpointServiceRequest<CancelRequest, CancelResponse> req) throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.Cancel, req);
			CancelResponse response = cancle(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			server.logService(RequestType.Cancel, ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.Cancel, req, e);
		}
	}

	public void onCreateMonitoredItems(
			EndpointServiceRequest<CreateMonitoredItemsRequest, CreateMonitoredItemsResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.CreateMonitoredItems, req);
			CreateMonitoredItemsResponse response = createMonitoredItem(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.CreateMonitoredItems,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.CreateMonitoredItems, req, e);
		}
	}

	public void onCreateSession(EndpointServiceRequest<CreateSessionRequest, CreateSessionResponse> req)
			throws ServiceFaultException {
		try {
			onReceiveRequest(RequestType.CreateSession, req);
			CreateSessionResponse response = createSession(req.getRequest(), req.getChannel());
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.CreateSession,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.CreateSession, req, e);
		}
	}

	public void onCreateSubscription(EndpointServiceRequest<CreateSubscriptionRequest, CreateSubscriptionResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.CreateSubscription, req);
			CreateSubscriptionResponse response = createSubscription(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.CreateSubscription,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.CreateSubscription, req, e);
		}
	}

	public void onCloseSession(EndpointServiceRequest<CloseSessionRequest, CloseSessionResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.CloseSession, req);
			CloseSessionResponse response = closeSession(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.CloseSession,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.CloseSession, req, e);
		}
	}

	public void onDeleteMonitoredItems(
			EndpointServiceRequest<DeleteMonitoredItemsRequest, DeleteMonitoredItemsResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.DeleteMonitoredItems, req);
			DeleteMonitoredItemsResponse response = deleteMonitoredItems(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.DeleteMonitoredItems,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.DeleteMonitoredItems, req, e);
		}
	}

	public void onDeleteNodes(EndpointServiceRequest<DeleteNodesRequest, DeleteNodesResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.DeleteNodes, req);
			DeleteNodesResponse response = deleteNodes(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.DeleteNodes,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.DeleteNodes, req, e);
		}
	}

	public void onDeleteReferences(EndpointServiceRequest<DeleteReferencesRequest, DeleteReferencesResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.DeleteReferences, req);
			DeleteReferencesResponse response = deleteReference(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.DeleteReferences,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.DeleteReferences, req, e);
		}
	}

	public void onDeleteSubscription(
			EndpointServiceRequest<DeleteSubscriptionsRequest, DeleteSubscriptionsResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.DeleteSubscriptions, req);
			DeleteSubscriptionsResponse response = deleteSubscription(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.DeleteSubscriptions,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.DeleteSubscriptions, req, e);
		}
	}

	public void onFindServers(EndpointServiceRequest<FindServersRequest, FindServersResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.FindServers, req);
			FindServersResponse response = findServers(req.getRequest());
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			server.logService(RequestType.FindServers, ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.FindServers, req, e);
		}
	}

	// public void onGetEndpoints(EndpointServiceRequest<GetEndpointsRequest,
	// GetEndpointsResponse> req)
	// throws ServiceFaultException {
	// try {
	// OPCServerSession clientSession =
	// onReceiveRequest(RequestType.GetEndpoints, req);
	// GetEndpointsResponse response = getEndpoints(req.getRequest());
	// doResponseHeader(response, req.getRequest());
	// req.sendResponse(response);
	// server.logService(RequestType.GetEndpoints,
	// ObjectUtils.printFieldsDeep(response));
	// } catch (ServiceResultException e) {
	// onErrorRequest(RequestType.GetEndpoints, req, e);
	// }
	// }
	public void onHistoryRead(EndpointServiceRequest<HistoryReadRequest, HistoryReadResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.HistoryRead, req);
			HistoryReadResponse response = historyRead(req.getRequest(), null, clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			server.logService(RequestType.HistoryRead, ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.HistoryRead, req, e);
		}
	}

	public void onHistoryUpdate(EndpointServiceRequest<HistoryUpdateRequest, HistoryUpdateResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.HistoryUpdate, req);
			HistoryUpdateResponse response = historyUpdate(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			server.logService(RequestType.HistoryUpdate, ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.HistoryUpdate, req, e);
		}
	}

	public void onModifyMonitoredItems(
			EndpointServiceRequest<ModifyMonitoredItemsRequest, ModifyMonitoredItemsResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.ModifyMonitoredItems, req);
			ModifyMonitoredItemsResponse response = modifyMonitoredItems(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.ModifyMonitoredItems,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.ModifyMonitoredItems, req, e);
		}
	}

	public void onModifySubscription(EndpointServiceRequest<ModifySubscriptionRequest, ModifySubscriptionResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.ModifySubscription, req);
			ModifySubscriptionResponse response = modifySubscription(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.ModifySubscription,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.ModifySubscription, req, e);
		}
	}

	public void onSetMonitoringMode(EndpointServiceRequest<SetMonitoringModeRequest, SetMonitoringModeResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.SetMonitoringMode, req);
			SetMonitoringModeResponse response = setMonitoringMode(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.SetMonitoringMode,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.SetMonitoringMode, req, e);
		}
	}

	public void onPublish(EndpointServiceRequest<PublishRequest, PublishResponse> req) throws ServiceFaultException {
		try {
			onReceiveRequest(RequestType.Publish, req);
			publish(req);
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.Publish, req, e);
		}
	}

	public void onQueryFirst(EndpointServiceRequest<QueryFirstRequest, QueryFirstResponse> req)
			throws ServiceFaultException {
		try {
			onReceiveRequest(RequestType.QueryFirst, req);
			QueryFirstResponse response = queryFirst(req.getRequest());
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.QueryFirst,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.QueryFirst, req, e);
		}
	}

	public void onQueryNext(EndpointServiceRequest<QueryNextRequest, QueryNextResponse> req)
			throws ServiceFaultException {
		try {
			onReceiveRequest(RequestType.QueryNext, req);
			QueryNextResponse response = queryNext(req.getRequest());
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.QueryNext,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.QueryNext, req, e);
		}
	}

	public void onRead(EndpointServiceRequest<ReadRequest, ReadResponse> req) throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.Read, req);
			ReadResponse response = read(req.getRequest(), null, clientSession);
			// serverSession.getLocaleIds()
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.Read,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.Read, req, e);
		}
	}

	public void onRegisterNodes(EndpointServiceRequest<RegisterNodesRequest, RegisterNodesResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.RegisterNodes, req);
			RegisterNodesResponse response = registerNodes(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			server.logService(RequestType.RegisterNodes, ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.RegisterNodes, req, e);
		}
	}

	public void onRegisterServer(EndpointServiceRequest<RegisterServerRequest, RegisterServerResponse> req)
			throws ServiceFaultException {
		try {
			onReceiveRequest(RequestType.RegisterServer, req);
			RegisterServerResponse response = registerServer(req.getChannel(), req.getRequest());
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			server.logService(RequestType.RegisterServer, ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.RegisterServer, req, e);
		}
	}

	public void onRepublish(EndpointServiceRequest<RepublishRequest, RepublishResponse> req)
			throws ServiceFaultException {
		try {
			onReceiveRequest(RequestType.Republish, req);
			RepublishResponse response = republish(req.getRequest());
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.Republish,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.Republish, req, e);
		}
	}

	public void onSetPublishingMode(EndpointServiceRequest<SetPublishingModeRequest, SetPublishingModeResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.SetPublishingMode, req);
			SetPublishingModeResponse response = setPublishingMode(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.SetPublishingMode,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.SetPublishingMode, req, e);
		}
	}

	public void onSetTriggering(EndpointServiceRequest<SetTriggeringRequest, SetTriggeringResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.SetTriggering, req);
			SetTriggeringResponse response = setTriggering(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			server.logService(RequestType.SetTriggering, ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.SetTriggering, req, e);
		}
	}

	public void onTranslateBrowsePathsToNodeIds(
			EndpointServiceRequest<TranslateBrowsePathsToNodeIdsRequest, TranslateBrowsePathsToNodeIdsResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.TranslateBrowsePathsToNodeIds, req);
			TranslateBrowsePathsToNodeIdsResponse response = translateBrowsePathsToNodeIds(req.getRequest(),
					clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.TranslateBrowsePathsToNodeIds,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.TranslateBrowsePathsToNodeIds, req, e);
		}
	}

	public void onTransferSubscription(
			EndpointServiceRequest<TransferSubscriptionsRequest, TransferSubscriptionsResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.TransferSubscriptions, req);
			TransferSubscriptionsResponse response = transferSubscription(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest(),
					new ServiceResultException(StatusCodes.Bad_ServiceUnsupported));
			req.sendResponse(response);
			// server.logService(RequestType.TransferSubscriptions,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.TransferSubscriptions, req, e);
		}
	}

	public void onUnregisterNodes(EndpointServiceRequest<UnregisterNodesRequest, UnregisterNodesResponse> req)
			throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.UnregisterNodes, req);
			UnregisterNodesResponse response = unregisterNodes(req.getRequest(), clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.UnregisterNodes,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.UnregisterNodes, req, e);
		}
	}

	public void onWrite(EndpointServiceRequest<WriteRequest, WriteResponse> req) throws ServiceFaultException {
		try {
			OPCServerSession clientSession = onReceiveRequest(RequestType.Write, req);
			WriteResponse response = write(req.getRequest(), false, null, clientSession);
			doResponseHeader(response, req.getRequest());
			req.sendResponse(response);
			// server.logService(RequestType.Write,
			// ObjectUtils.printFieldsDeep(response));
		} catch (ServiceResultException e) {
			onErrorRequest(RequestType.Write, req, e);
		}
	}

	public abstract ActivateSessionResponse activateSession(ActivateSessionRequest request, ServerSecureChannel channel)
			throws ServiceResultException;

	public abstract AddNodesResponse addNode(AddNodesRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract AddReferencesResponse addReferences(AddReferencesRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract BrowseResponse browse(BrowseRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract BrowseNextResponse browseNext(BrowseNextRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract CallResponse call(CallRequest request, OPCServerSession session) throws ServiceResultException;

	public abstract CancelResponse cancle(CancelRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract CreateMonitoredItemsResponse createMonitoredItem(CreateMonitoredItemsRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract CreateSessionResponse createSession(CreateSessionRequest request, ServerSecureChannel channel)
			throws ServiceResultException;

	public abstract CreateSubscriptionResponse createSubscription(CreateSubscriptionRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract CloseSessionResponse closeSession(CloseSessionRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract DeleteMonitoredItemsResponse deleteMonitoredItems(DeleteMonitoredItemsRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract DeleteNodesResponse deleteNodes(DeleteNodesRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract DeleteReferencesResponse deleteReference(DeleteReferencesRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract DeleteSubscriptionsResponse deleteSubscription(DeleteSubscriptionsRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract FindServersResponse findServers(FindServersRequest request) throws ServiceResultException;

	public abstract HistoryReadResponse historyRead(HistoryReadRequest request, Long[] driverState,
			OPCServerSession session) throws ServiceResultException;

	public abstract HistoryUpdateResponse historyUpdate(HistoryUpdateRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract ModifyMonitoredItemsResponse modifyMonitoredItems(ModifyMonitoredItemsRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract ModifySubscriptionResponse modifySubscription(ModifySubscriptionRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract SetMonitoringModeResponse setMonitoringMode(SetMonitoringModeRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract QueryFirstResponse queryFirst(QueryFirstRequest request) throws ServiceResultException;

	public abstract QueryNextResponse queryNext(QueryNextRequest request) throws ServiceResultException;

	public abstract void publish(EndpointServiceRequest<PublishRequest, PublishResponse> request)
			throws ServiceResultException;

	public abstract ReadResponse read(ReadRequest request, Long[] driverState, OPCServerSession session)
			throws ServiceResultException;

	public abstract RegisterNodesResponse registerNodes(RegisterNodesRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract RegisterServerResponse registerServer(ServerSecureChannel channel, RegisterServerRequest request)
			throws ServiceResultException;

	public abstract RepublishResponse republish(RepublishRequest request) throws ServiceResultException;

	public abstract SetPublishingModeResponse setPublishingMode(SetPublishingModeRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract SetTriggeringResponse setTriggering(SetTriggeringRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract TranslateBrowsePathsToNodeIdsResponse translateBrowsePathsToNodeIds(
			TranslateBrowsePathsToNodeIdsRequest request, OPCServerSession session) throws ServiceResultException;

	public abstract TransferSubscriptionsResponse transferSubscription(TransferSubscriptionsRequest request,
			OPCServerSession session) throws ServiceResultException;

	public abstract UnregisterNodesResponse unregisterNodes(UnregisterNodesRequest request, OPCServerSession session)
			throws ServiceResultException;

	public abstract WriteResponse write(WriteRequest request, boolean skipAccessLevel, Long[] driverState,
			OPCServerSession session) throws ServiceResultException;
}
