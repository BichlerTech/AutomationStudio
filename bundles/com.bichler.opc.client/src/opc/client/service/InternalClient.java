package opc.client.service;

import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceRequest;
import org.opcfoundation.ua.builtintypes.ServiceResponse;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
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
import org.opcfoundation.ua.core.EndpointConfiguration;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ModifyMonitoredItemsRequest;
import org.opcfoundation.ua.core.ModifyMonitoredItemsResponse;
import org.opcfoundation.ua.core.ModifySubscriptionRequest;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.PublishRequest;
import org.opcfoundation.ua.core.PublishResponse;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.RegisterNodesRequest;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.RepublishRequest;
import org.opcfoundation.ua.core.RepublishResponse;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.SetMonitoringModeResponse;
import org.opcfoundation.ua.core.SetPublishingModeRequest;
import org.opcfoundation.ua.core.SetPublishingModeResponse;
import org.opcfoundation.ua.core.SetTriggeringRequest;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.SubscriptionAcknowledgement;
import org.opcfoundation.ua.core.TransferSubscriptionsRequest;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.UnregisterNodesRequest;
import org.opcfoundation.ua.core.UnregisterNodesResponse;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.transport.AsyncResult;
import org.opcfoundation.ua.transport.ResultListener;
import org.opcfoundation.ua.transport.SecureChannel;
import org.opcfoundation.ua.transport.ServiceChannel;
import org.opcfoundation.ua.transport.TransportChannelSettings;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.tcp.io.SequenceNumber;

import opc.sdk.core.enums.DiagnosticsMasks;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.result.AsyncRequestState;

/**
 * Internal client that is used by the session to send the services via a secure
 * channel.
 *
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public abstract class InternalClient {
	public static final String ID = "opc.client.service.InternalClient";
	/** lock */
	protected Object lock = new Object();
	/** OPC UA client instance */
	protected Client clientInstance = null;
	/** transport channel, which sends services to the server */
	protected SecureChannel transportChannel = null;
	/** certificate to use */
	protected Cert cert = null;
	/** private key */
	protected PrivKey privateKey = null;
	/** request handle sequence number */
	private SequenceNumber nextRequestHandle = new SequenceNumber();
	/** authentification token to use in the request header */
	private NodeId authentificationToken = null;
	private boolean asyncRequestStop = false;
	/** timeout for async operations */
	private long asynctimeOut = 60000;
	/** Outstanding Asynchronous Requests */
	List<AsyncRequestState> outstandingRequests = null;
	/** Timestamp of the last KeepAlive */
	DateTime lastKeepAliveTime = null;
	/** diagnostics mask to use */
	DiagnosticsMasks diagnosticsMask = DiagnosticsMasks.None;

	/**
	 * Internal client class. Basic session and service functionality.
	 *
	 * @param Logger Logger instance.
	 */
	public InternalClient() {
	}

	/**
	 * Getter Operation Timeout from the {@link ServiceChannel}.
	 *
	 * @return OperationTimeout
	 */
	public int getOperationTimeout() {
		return this.transportChannel.getOperationTimeout();
	}

	/**
	 * Invokes the Activate-Session Service.
	 *
	 * @param Request
	 *
	 * @return {@link ActivateSessionResponse} of the Service.
	 * @throws ServiceResultException Bad_UserAccessDenied
	 * @throws ServiceFaultException
	 */
	protected ActivateSessionResponse activateSession(ActivateSessionRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("ActivateSessionRequest is NULL");
		}
		updateRequestHeader(request, RequestType.ActivateSession);
		ActivateSessionResponse response = null;
		try {
			response = (ActivateSessionResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.ActivateSession, response, request);
			return response;
		} catch (ServiceResultException e) {
			onError(RequestType.ActivateSession, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Add-Nodes Service.
	 *
	 * @param Request {@link AddNodesRequest} with the information to create a new
	 *                node on the server.
	 * @return {@link AddNodesResponse} of the Service.
	 */
	protected AddNodesResponse addNodes(AddNodesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("AddNodesRequest");
		}
		updateRequestHeader(request, RequestType.AddNodes);
		try {
			AddNodesResponse response = (AddNodesResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.AddNodes, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.AddNodes, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Add-References Service.
	 *
	 * @param Request {@link AddReferencesRequest} with the information to create a
	 *                new reference on the server.
	 * @return {@link AddReferencesResponse} of the Service.
	 */
	protected AddReferencesResponse addReferences(AddReferencesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("AddReferencesRequest");
		}
		updateRequestHeader(request, RequestType.AddReferences);
		try {
			AddReferencesResponse response = (AddReferencesResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.AddReferences, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.AddReferences, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Browse Service.
	 *
	 * @param Request {@link BrowseRequest} to discover the References of a
	 *                specified Node.
	 * @return {@link BrowseResponse} of the Service.
	 */
	protected BrowseResponse browse(BrowseRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("BrowseRequest");
		}
		updateRequestHeader(request, RequestType.Browse);
		try {
			BrowseResponse response = (BrowseResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.Browse, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.Browse, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Browse-Next Service.
	 *
	 * @param Request {@link BrowseNextRequest} to continue on an existing browse.
	 * @return {@link BrowseNextResponse} of the Service.
	 */
	protected BrowseNextResponse browseNext(BrowseNextRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("BrowseNextRequest");
		}
		updateRequestHeader(request, RequestType.BrowseNext);
		try {
			BrowseNextResponse response = (BrowseNextResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.BrowseNext, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.BrowseNext, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Call-Method Service
	 *
	 * @param Request {@link CallRequest} to call a Method on the server.
	 * @return {@link CallResponse} of the Service.
	 */
	protected CallResponse call(CallRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CallRequest");
		}
		updateRequestHeader(request, RequestType.CreateSubscription);
		try {
			CallResponse response = (CallResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.CreateSubscription, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.CreateSubscription, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Cancel-Request Service
	 *
	 * @param Request {@link CancelRequest} to cancel a {@link ServiceRequest}.
	 * @return {@link CancelResponse} of the Service.
	 */
	protected CancelResponse cancelRequest(CancelRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CancelRequest");
		}
		updateRequestHeader(request, RequestType.Cancel);
		try {
			CancelResponse response = (CancelResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.Cancel, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.Cancel, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Close-Session Service.
	 *
	 * @param Request {@link CloseSessionRequest} to disconnect a session from the
	 *                server.
	 * @return {@link CloseSessionResponse} of the Service.
	 */
	protected CloseSessionResponse closeSession(CloseSessionRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CloseSessionRequest");
		}
		updateRequestHeader(request, RequestType.CloseSession);
		CloseSessionResponse response = null;
		try {
			response = (CloseSessionResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.CloseSession, response, request);
			// test to close channel too
			this.transportChannel.close();
		} catch (NullPointerException npe) {
			Logger.getLogger(getClass().getName()).log(Level.FINE, null, npe);
		} catch (final ServiceResultException e) {
			onError(RequestType.CloseSession, request, null, e);
			throw e;
		}
		return response;
	}

	/**
	 * Invokes the Create-Monitored-Item Service.
	 *
	 * @param Request {@link CreateMonitoredItemsRequest} to create
	 *                {@link MonitoredItem} to monitor DataChanges or Events.
	 * @return {@link CreateMonitoredItemsResponse} of the Service.
	 */
	protected CreateMonitoredItemsResponse createMonitoredItems(CreateMonitoredItemsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CreateMonitoredItemsRequest");
		}
		updateRequestHeader(request, RequestType.CreateMonitoredItems);
		try {
			CreateMonitoredItemsResponse response = (CreateMonitoredItemsResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.CreateMonitoredItems, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.CreateMonitoredItems, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Create-Subscription Service.
	 *
	 * @param Request {@link CreateSubscriptionRequest} to create a
	 *                {@link Subscription} to hold {@link MonitoredItem}.
	 * @return {@link CreateSubscriptionResponse} of the Service.
	 */
	protected CreateSubscriptionResponse createSubscription(CreateSubscriptionRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CreateSubscriptionRequest");
		}
		updateRequestHeader(request, RequestType.CreateSubscription);
		try {
			CreateSubscriptionResponse response = (CreateSubscriptionResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.CreateSubscription, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.CreateSubscription, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Create-Session Service.
	 *
	 * <!-- Wenn man die session mit einem SessionChannel macht, kann man im
	 * nodemanager den servicechannel seperat abspeichern -->
	 *
	 * @param endpoint
	 * @param endpointConfiguration
	 * @param serverCertificate
	 * @param request
	 * @param clientSession
	 * @return {@link AddNodesResponse} of the Service.
	 * @throws ServiceResultException
	 */
	protected CreateSessionResponse createSession(EndpointDescription endpoint,
			EndpointConfiguration endpointConfiguration, CreateSessionRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CreateSessionRequest");
		}
		updateRequestHeader(request, RequestType.CreateSession);
		createChannel(endpoint, endpointConfiguration, request);
		try {
			CreateSessionResponse response = (CreateSessionResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.CreateSession, response, request);
			return response;
		} catch (ServiceResultException e) {
			this.transportChannel.close();
			this.transportChannel.dispose();
			throw e;
		}
	}

	/**
	 * Invokes the Delete-Monitored-Item Service.
	 *
	 * @param Request {@link DeleteMonitoredItemsRequest} to stop monitoring an
	 *                Event or DataChange.
	 * @return {@link DeleteMonitoredItemsResponse} of the Service.
	 */
	protected DeleteMonitoredItemsResponse deleteMonitoredItems(DeleteMonitoredItemsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteMonitoredItemsRequest");
		}
		updateRequestHeader(request, RequestType.DeleteMonitoredItems);
		try {
			DeleteMonitoredItemsResponse response = (DeleteMonitoredItemsResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.DeleteMonitoredItems, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.DeleteMonitoredItems, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Delete-Nodes Service.
	 *
	 * @param Request {@link DeleteNodesRequest} to remove a Node from the
	 *                server�s AddressSpace.
	 * @return {@link DeleteNodesResponse} of the Service.
	 */
	protected DeleteNodesResponse deleteNodes(DeleteNodesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteNodesRequest");
		}
		updateRequestHeader(request, RequestType.DeleteNodes);
		try {
			DeleteNodesResponse response = (DeleteNodesResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.DeleteNodes, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.DeleteNodes, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Delete-Reference Service
	 *
	 * @param Request {@link DeleteReferencesRequest} to remove a Reference from a
	 *                Node of the server�s AddressSpace.
	 * @return {@link AddNodesResponse} of the Service.
	 */
	protected DeleteReferencesResponse deleteReferences(DeleteReferencesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteReferencesRequest");
		}
		updateRequestHeader(request, RequestType.DeleteReferences);
		try {
			DeleteReferencesResponse response = (DeleteReferencesResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.DeleteReferences, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.DeleteReferences, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Delete-Subscription Service.
	 *
	 * @param Request {@link DeleteSubscriptionsRequest} to delete a
	 *                {@link Subscription} on the server with all its containing
	 *                {@link MonitoredItem}.
	 * @return {@link DeleteSubscriptionsResponse} of the Service.
	 */
	protected DeleteSubscriptionsResponse deleteSubscription(DeleteSubscriptionsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteSubscriptionsRequest");
		}
		updateRequestHeader(request, RequestType.DeleteSubscriptions);
		try {
			DeleteSubscriptionsResponse response = (DeleteSubscriptionsResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.DeleteSubscriptions, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.DeleteSubscriptions, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Read Service.
	 *
	 * @param Request {@link ReadRequest} to read an Attribute of a Node.
	 * @return {@link ReadResponse} of the service.
	 */
	protected HistoryReadResponse historyRead(HistoryReadRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("HistoryReadRequest");
		}
		updateRequestHeader(request, RequestType.HistoryRead);
		HistoryReadResponse response;
		response = (HistoryReadResponse) this.transportChannel.serviceRequest(request);
		validateResponse(RequestType.HistoryRead, response, request);
		return response;
	}

	/**
	 * Invokes a Read Service.
	 *
	 * @param Request {@link ReadRequest} to read an Attribute of a Node.
	 * @return {@link ReadResponse} of the service.
	 */
	protected HistoryUpdateResponse historyUpdate(HistoryUpdateRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("HistoryUpdateRequest");
		}
		updateRequestHeader(request, RequestType.HistoryUpdate);
		HistoryUpdateResponse response;
		response = (HistoryUpdateResponse) this.transportChannel.serviceRequest(request);
		validateResponse(RequestType.HistoryUpdate, response, request);
		return response;
	}

	/**
	 * Invokes a Modify-Monitored-Item Service.
	 *
	 * @param Request {@link ModifyMonitoredItemsRequest} to modify its attributes.
	 * @return {@link ModifyMonitoredItemsResponse} of the Service.
	 */
	protected ModifyMonitoredItemsResponse modifyMonitoredItems(ModifyMonitoredItemsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("ModifyMonitoredItemsRequest");
		}
		updateRequestHeader(request, RequestType.ModifyMonitoredItems);
		try {
			ModifyMonitoredItemsResponse response = (ModifyMonitoredItemsResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.ModifyMonitoredItems, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.ModifyMonitoredItems, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Modify-Subscription Service
	 *
	 * @param Request {@link ModifySubscriptionRequest} to modify its attributes.
	 * @return {@link ModifySubscriptionResponse} of the Service.
	 */
	protected ModifySubscriptionResponse modifySubscription(ModifySubscriptionRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("ModifySubscriptionRequest");
		}
		updateRequestHeader(request, RequestType.ModifySubscription);
		try {
			ModifySubscriptionResponse response = (ModifySubscriptionResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.ModifySubscription, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.ModifySubscription, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Publish Service.
	 *
	 * @param Request {@link PublishRequest} to publish a {@link Subscription}.
	 * @return {@link PublishRequest} of the Service.
	 */
	protected PublishResponse publish(PublishRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("PublishRequest");
		}
		updateRequestHeader(request, RequestType.Publish);
		try {
			/**
			 * verify if we have not closed the session before
			 */
			PublishResponse response = (PublishResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.Publish, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.Publish, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Read Service.
	 *
	 * @param
	 *
	 * @param Request {@link ReadRequest} to read an Attribute of a Node.
	 * @return {@link ReadResponse} of the service.
	 */
	protected ReadResponse read(ReadRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("ReadRequest");
		}
		updateRequestHeader(request, RequestType.Read);
		try {
			ReadResponse response = (ReadResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.Read, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.Read, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Register-Node Service.
	 *
	 * @param Request {@link RegisterNodesRequest} to register Nodes on a Server, to
	 *                increase performance accessing the Node.
	 * @return {@link RegisterNodesResponse} of the service.
	 */
	protected RegisterNodesResponse registerNodes(RegisterNodesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("RegisterNodesRequest");
		}
		updateRequestHeader(request, RequestType.RegisterNodes);
		try {
			RegisterNodesResponse response = (RegisterNodesResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.RegisterNodes, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.RegisterNodes, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Republish Service.
	 *
	 * @param Request {@link RepublishRequest} to receive an older Notification
	 *                Message from an Subscription.
	 * @return {@link RepublishResponse} of the Service.
	 */
	protected RepublishResponse republish(RepublishRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("RepublishRequest");
		}
		updateRequestHeader(request, RequestType.Republish);
		try {
			RepublishResponse response = (RepublishResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.Republish, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.Republish, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Set-Monitoring-Mode Service.
	 *
	 * @param Request {@link SetMonitoringModeRequest} to set the monitoring mode
	 *                for one or more MonitoredItems of a Subscription.
	 * @return {@link SetMonitoringModeResponse} of the Service
	 */
	protected SetMonitoringModeResponse setMonitoringMode(SetMonitoringModeRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("SetMonitoringModeRequest");
		}
		updateRequestHeader(request, RequestType.SetMonitoringMode);
		try {
			SetMonitoringModeResponse response = (SetMonitoringModeResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.SetMonitoringMode, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.SetMonitoringMode, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Set-Publishing-Mode Service.
	 *
	 * @param Request {@link SetPublishingModeRequest} to enable sending of
	 *                Notifications on one or more Subscriptions.
	 * @return {@link SetPublishingModeResponse} of the Service.
	 */
	protected SetPublishingModeResponse setPublishingModegMode(SetPublishingModeRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("SetPublishingModeRequest");
		}
		updateRequestHeader(request, RequestType.SetPublishingMode);
		try {
			SetPublishingModeResponse response = (SetPublishingModeResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.SetPublishingMode, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.SetPublishingMode, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Set-Triggering Service.
	 *
	 * @param Request {@link SetTriggeringRequest} to create triggering links
	 *                between {@link MonitoredItem}
	 * @return {@link SetTriggeringResponse} of the Service
	 */
	protected SetTriggeringResponse setTriggering(SetTriggeringRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("SetTriggeringRequest");
		}
		updateRequestHeader(request, RequestType.SetTriggering);
		try {
			SetTriggeringResponse response = (SetTriggeringResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.SetTriggering, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.SetTriggering, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Transfer-Subscription Service.
	 *
	 * @param Request {@link TransferSubscriptionsRequest} to transfer a
	 *                Subscription and its MonitoredItems from one Session to
	 *                another.
	 * @return {@link TransferSubscriptionsResponse} of the Service.
	 */
	protected TransferSubscriptionsResponse transfereSubscriptions(TransferSubscriptionsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("TransferSubscriptionsRequest");
		}
		updateRequestHeader(request, RequestType.TransferSubscriptions);
		try {
			TransferSubscriptionsResponse response = (TransferSubscriptionsResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.TransferSubscriptions, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.TransferSubscriptions, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Translate-Browse-Path-To-NodeId Service.
	 *
	 * @param Request {@link TranslateBrowsePathsToNodeIdsRequest} StartingNodeIds
	 *                and RelativePathes to translate them to NodeIds.
	 * @return {@link TranslateBrowsePathsToNodeIdsResponse} of the Service.
	 */
	protected TranslateBrowsePathsToNodeIdsResponse translateBrowsePathToNodeIds(
			TranslateBrowsePathsToNodeIdsRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("TranslateBrowsePathsToNodeIdsRequest");
		}
		updateRequestHeader(request, RequestType.TranslateBrowsePathsToNodeIds);
		try {
			TranslateBrowsePathsToNodeIdsResponse response = (TranslateBrowsePathsToNodeIdsResponse) this.transportChannel
					.serviceRequest(request);
			validateResponse(RequestType.TranslateBrowsePathsToNodeIds, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.TranslateBrowsePathsToNodeIds, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes a Unregister-Node Service.
	 *
	 * @param Request {@link UnregisterNodesRequest} to unregister a Node node on
	 *                the server.
	 * @return {@link UnregisterNodesResponse} of the service.
	 */
	protected UnregisterNodesResponse unregisterNodes(UnregisterNodesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("UnregisterNodesRequest");
		}
		updateRequestHeader(request, RequestType.UnregisterNodes);
		try {
			UnregisterNodesResponse response = (UnregisterNodesResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.UnregisterNodes, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.UnregisterNodes, request, null, e);
			throw e;
		}
	}

	/**
	 * Invokes the Write Service
	 *
	 * @param Request {@link WriteRequest} to write values on Nodes.
	 * @return {@link WriteResponse} of the service.
	 */
	protected WriteResponse write(WriteRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("WriteRequest");
		}
		updateRequestHeader(request, RequestType.Write);
		try {
			WriteResponse response = (WriteResponse) this.transportChannel.serviceRequest(request);
			validateResponse(RequestType.Write, response, request);
			return response;
		} catch (final ServiceResultException e) {
			onError(RequestType.Write, request, null, e);
			throw e;
		}
	}

	/**
	 * Begins an asynchronous invocation of the Activate-Session service
	 *
	 * @param Request
	 * @return{@link AsyncResult} of a {@link ActivateSessionResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginActivateSession(ActivateSessionRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("ActivateSessionRequest");
		}
		updateRequestHeader(request, RequestType.ActivateSession);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Add-Nodes Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link AddNodesRequest} with the information to create a new
	 *                  node on the server.
	 * @param RequestId Defined Id for an {@link AddNodesRequest}.
	 * @return {@link AsyncResult} of a {@link AddNodesResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginAddNodes(AddNodesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("AddNodesRequest");
		}
		updateRequestHeader(request, RequestType.AddNodes);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Add-Reference Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link AddReferencesRequest} with the information to create
	 *                  a new reference on the server.
	 * @param RequestId Defined Id for an {@link AddReferencesRequest}.
	 * @return {@link AsyncResult} of a {@link AddReferencesResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginAddReferences(AddReferencesRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("AddReferencesRequest");
		}
		updateRequestHeader(request, RequestType.AddReferences);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Browse Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link BrowseRequest} to discover the References of a
	 *                  specified Node.
	 * @param RequestId Defined Id for an {@link BrowseRequest}.
	 * @return {@link AsyncResult} of a {@link BrowseResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginBrowse(BrowseRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("BrowseRequest");
		}
		updateRequestHeader(request, RequestType.Browse);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Browse-Next Service.
	 *
	 * @param Request {@link BrowseNextRequest} to continue on an existing browse.
	 * @return {@link AsyncResult} of a {@link BrowseNextResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginBrowseNext(BrowseNextRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("BrowseNextRequest");
		}
		updateRequestHeader(request, RequestType.BrowseNext);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Call Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link CallRequest} to call a Method on the server.
	 * @param RequestId Defined Id for an {@link CallRequest}.
	 * @return {@link AsyncResult} of a {@link CallResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginCallMethod(CallRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CallRequest");
		}
		updateRequestHeader(request, RequestType.Call);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocationo of the Cancel Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link CancelRequest} to cancel a {@link ServiceRequest}.
	 * @param RequestId Defined Id for an {@link CallRequest}.
	 * @return {@link AsyncResult} of a {@link CancelResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginCancelRequest(CancelRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CancelRequest");
		}
		updateRequestHeader(request, RequestType.Cancel);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Close-Session Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link CloseSessionRequest} to disconnect a session from the
	 *                  server.
	 * @param RequestId Defined Id for an {@link CloseSessionRequest}.
	 * @return {@link AsyncResult} of a {@link CloseSessionResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginCloseSession(CloseSessionRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CloseSessionRequest");
		}
		updateRequestHeader(request, RequestType.CloseSession);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Create-Monitored-Item Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link CreateMonitoredItemsRequest} to create
	 *                  {@link MonitoredItem} to monitor DataChanges or Events.
	 * @param RequestId Defined Id for an {@link CreateMonitoredItemsRequest}.
	 * @return {@link AsyncResult} of a {@link CreateMonitoredItemsResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginCreateMonitoredItem(CreateMonitoredItemsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CreateMonitoredItemsRequest");
		}
		updateRequestHeader(request, RequestType.CreateMonitoredItems);
		return this.transportChannel.serviceRequestAsync(request);
	}

	protected AsyncResult<ServiceResponse> beginCreateSession(EndpointDescription endpoint,
			EndpointConfiguration endpointConfiguration, CreateSessionRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CreateSession");
		}
		updateRequestHeader(request, RequestType.CreateSession);
		createChannel(endpoint, endpointConfiguration, request);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Create-Subscription Service.
	 *
	 * @param subscriptionManager
	 * @param subscriptions
	 *
	 * @param Session             Session used to send the Request.
	 * @param Request             {@link DeleteSubscriptionsRequest} to delete a
	 *                            {@link Subscription} on the server with all its
	 *                            containing {@link MonitoredItem}.
	 * @param RequestId           Defined Id for an
	 *                            {@link CreateSubscriptionRequest}.
	 * @return {@link AsyncResult} of a {@link CreateSubscriptionResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginCreateSubscription(CreateSubscriptionRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("CreateSubscriptionRequest");
		}
		updateRequestHeader(request, RequestType.CreateSubscription);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Monitored-Item Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link DeleteMonitoredItemsRequest} to stop monitoring an
	 *                  Event or DataChange.
	 * @param RequestId Defined Id for an {@link DeleteMonitoredItemsRequest}.
	 * @return {@link AsyncResult} of a {@link DeleteMonitoredItemsResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginDeleteMonitoredItems(DeleteMonitoredItemsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteMonitoredItemsRequest");
		}
		updateRequestHeader(request, RequestType.DeleteMonitoredItems);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Node Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link DeleteNodesRequest} to remove a Node from the
	 *                  server�s AddressSpace.
	 * @param RequestId Defined Id for an {@link DeleteNodesResponse}.
	 * @return {@link AsyncResult} of a {@link DeleteNodesResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginDeleteNodes(DeleteNodesRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteNodesRequest");
		}
		updateRequestHeader(request, RequestType.DeleteNodes);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Reference Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link DeleteReferencesRequest} to remove a Reference from a
	 *                  Node of the server�s AddressSpace.
	 * @param RequestId Defined Id for an {@link DeleteReferencesResponse}.
	 * @return @return {@link AsyncResult} of a {@link DeleteReferencesResponse}
	 */
	protected AsyncResult<ServiceResponse> beginDeleteReferences(DeleteReferencesRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteReferencesRequest");
		}
		updateRequestHeader(request, RequestType.DeleteReferences);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Subscription Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link DeleteSubscriptionsRequest} to delete a
	 *                  {@link Subscription} on the server with all its containing
	 *                  {@link MonitoredItem}.
	 * @param RequestId Defined Id for an {@link DeleteSubscriptionsRequest}.
	 * @return {@link AsyncResult} of a {@link DeleteSubscriptionsResponse}
	 */
	protected AsyncResult<ServiceResponse> beginDeleteSubscription(DeleteSubscriptionsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("DeleteSubscriptionsRequest");
		}
		updateRequestHeader(request, RequestType.DeleteSubscriptions);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Read Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link ReadRequest} to read an Attribute of a Node.
	 * @param RequestId Defined Id for an {@link ReadRequest}.
	 * @return {@link AsyncResult} of a {@link ReadResponse}
	 */
	protected AsyncResult<ServiceResponse> beginHistoryRead(HistoryReadRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("HistoryReadRequest");
		}
		updateRequestHeader(request, RequestType.HistoryRead);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Read Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link ReadRequest} to read an Attribute of a Node.
	 * @param RequestId Defined Id for an {@link ReadRequest}.
	 * @return {@link AsyncResult} of a {@link ReadResponse}
	 */
	protected AsyncResult<ServiceResponse> beginHistoryUpdate(HistoryUpdateRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("HistoryUpdateRequest");
		}
		updateRequestHeader(request, RequestType.HistoryUpdate);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Modify-MonitoredItem Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link ModifyMonitoredItemsRequest} to modify its
	 *                  attributes.
	 * @param RequestId Defined Id for an {@link ModifyMonitoredItemsRequest}.
	 * @return {@link AsyncResult} of a {@link ModifyMonitoredItemsResponse}
	 */
	protected AsyncResult<ServiceResponse> beginModifyMonitoredItems(ModifyMonitoredItemsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("ModifyMonitoredItemsRequest");
		}
		updateRequestHeader(request, RequestType.ModifyMonitoredItems);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Modify-Subscription Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link ModifySubscriptionRequest} to modify its attributes.
	 * @param RequestId Defined Id for an {@link ModifySubscriptionRequest}.
	 * @return {@link AsyncResult} of a {@link ModifySubscriptionResponse}
	 */
	protected AsyncResult<ServiceResponse> beginModifySubscription(ModifySubscriptionRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("ModifySubscriptionRequest");
		}
		updateRequestHeader(request, RequestType.ModifySubscription);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Publish Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link PublishRequest}
	 * @param RequestId Defined Id for an {@link PublishRequest}.
	 * @return {@link AsyncResult} of a {@link PublishRequest}
	 */
	protected AsyncResult<ServiceResponse> beginPublish(ClientSession session, PublishRequest request,
			boolean moreNotifications) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("PublishRequest");
		}
		updateRequestHeader(request, RequestType.Publish);
		AsyncResult response = this.transportChannel.serviceRequestAsync(request);
		this.asyncRequestStarted(response, request.getRequestHeader().getRequestHandle(), Identifiers.PublishRequest);
		response.setListener(new PublishResultListener(session, request, moreNotifications));
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Read Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link ReadRequest} to read an Attribute of a Node.
	 * @param RequestId Defined Id for an {@link ReadRequest}.
	 * @return {@link AsyncResult} of a {@link ReadResponse}
	 */
	protected AsyncResult<ServiceResponse> beginRead(ReadRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("ReadRequest");
		}
		updateRequestHeader(request, RequestType.Read);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Register-Node Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link RegisterNodesRequest} to register Nodes on a Server,
	 *                  to increase performance accessing the Node.
	 * @param RequestId Defined Id for an {@link RegisterNodesRequest}.
	 * @return {@link AsyncResult} of a {@link RegisterNodesResponse}
	 */
	protected AsyncResult<ServiceResponse> beginRegisterNodes(RegisterNodesRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("RegisterNodesRequest");
		}
		updateRequestHeader(request, RequestType.RegisterNodes);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Set-Monitoring-Mode Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link SetMonitoringModeRequest} to set the monitoring mode
	 *                  for one or more MonitoredItems of a Subscription.
	 * @param RequestId Defined Id for an {@link SetMonitoringModeRequest}.
	 * @return {@link AsyncResult} of a {@link RegisterNodesResponse}
	 */
	protected AsyncResult<ServiceResponse> beginSetMonitoringMode(SetMonitoringModeRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("SetMonitoringModeRequest");
		}
		updateRequestHeader(request, RequestType.SetMonitoringMode);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Set-Publishing-Mode Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link SetPublishingModeRequest} to enable sending of
	 *                  Notifications on one or more Subscriptions.
	 * @param RequestId Defined Id for an {@link SetPublishingModeRequest}.
	 * @return {@link AsyncResult} of a {@link SetPublishingModeResponse}
	 */
	protected AsyncResult<ServiceResponse> beginSetPublishingMode(SetPublishingModeRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("SetPublishingModeRequest");
		}
		updateRequestHeader(request, RequestType.SetPublishingMode);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Set-Triggering Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link SetTriggeringRequest} to create triggering links
	 *                  between {@link MonitoredItem}
	 * @param RequestId Defined Id for an {@link SetTriggeringRequest}.
	 * @return {@link AsyncResult} of a {@link SetTriggeringResponse}
	 */
	protected AsyncResult<ServiceResponse> beginSetTriggering(SetTriggeringRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("SetTriggeringRequest");
		}
		updateRequestHeader(request, RequestType.SetTriggering);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Transfer-Subscription Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link TransferSubscriptionsRequest} to transfer a
	 *                  Subscription and its MonitoredItems from one Session to
	 *                  another.
	 * @param RequestId Defined Id for an {@link TransferSubscriptionsRequest}.
	 * @return {@link AsyncResult} of a {@link TransferSubscriptionsResponse}
	 */
	protected AsyncResult<ServiceResponse> beginTransferSubscriptions(TransferSubscriptionsRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("TransferSubscriptionsRequest");
		}
		updateRequestHeader(request, RequestType.TransferSubscriptions);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Translate-Browse-Paths-To-NodeId
	 * Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link TranslateBrowsePathsToNodeIdsRequest} StartingNodeIds
	 *                  and RelativePathes to translate them to NodeIds
	 * @param RequestId Defined Id for an
	 *                  {@link TranslateBrowsePathsToNodeIdsRequest} .
	 * @return {@link AsyncResult} of a
	 *         {@link TranslateBrowsePathsToNodeIdsResponse}
	 */
	protected AsyncResult<ServiceResponse> beginTranslateBrowsePathToNodeId(
			TranslateBrowsePathsToNodeIdsRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("TranslateBrowsePathsToNodeIdsRequest");
		}
		updateRequestHeader(request, RequestType.TranslateBrowsePathsToNodeIds);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Unregister-Node Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link UnregisterNodesRequest} to unregister a Node node on
	 *                  the server.
	 * @param RequestId Defined Id for an {@link UnregisterNodesRequest} .
	 * @return {@link AsyncResult} of a {@link UnregisterNodesResponse}.
	 */
	protected AsyncResult<ServiceResponse> beginUnregisterNodes(UnregisterNodesRequest request)
			throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("UnregisterNodesRequest");
		}
		updateRequestHeader(request, RequestType.UnregisterNodes);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Begins an asynchronous invocation of the Write Service.
	 *
	 * @param Session   Session used to send the Request.
	 * @param Request   {@link WriteRequest} to write values on Nodes.
	 * @param RequestId Defined Id for an {@link WriteRequest} .
	 * @return {@link AsyncResult} of a {@link WriteResponse}
	 */
	protected AsyncResult<ServiceResponse> beginWriteAsync(WriteRequest request) throws ServiceResultException {
		if (request == null) {
			throw new IllegalArgumentException("WriteRequest");
		}
		updateRequestHeader(request, RequestType.Write);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * *************************************************************************
	 * ***** historical section
	 *********************************************************************************/
	/**
	 * Validates a DataValue returned from the server
	 *
	 * @param timestamp
	 * @param i
	 * @param class1
	 * @param values
	 */
	protected ServiceResult validateDataValue(DataValue value, Class<?> expectedType) {
		// check for null
		if (value == null) {
			return new ServiceResult(StatusCodes.Bad_UnexpectedError);
		}
		// check status code
		if (value.getStatusCode().isBad()) {
			return getResult(value.getStatusCode());
		}
		// check data type
		if (expectedType != null) {
			if (Variant.NULL.equals(value.getValue())) {
				return new ServiceResult(StatusCodes.Bad_UnexpectedError);
			}
			if (!expectedType.equals(value.getValue().getValue().getClass())) {
				return new ServiceResult(StatusCodes.Bad_UnexpectedError);
			}
		}
		return new ServiceResult(StatusCode.GOOD);
	}

	/**
	 * Validates a response returned by the server.
	 *
	 * @throws ServiceResultException
	 */
	protected void validateDiagnostics(DiagnosticInfo[] diagnostics) throws ServiceResultException {
		// returning an empty list for diagnostics info arrays is allowed
		if (diagnostics != null && diagnostics.length > 0/**
															 * && diagnostics.length != status.
															 */
		) {
			throw new ServiceResultException(new StatusCode(StatusCodes.Bad_UnexpectedError));
		}
	}

	/**
	 * Validates a response returned by the server
	 *
	 * @param requestType
	 *
	 * @param response
	 *
	 * @return FALSE if the response is GOOD, otherwise TRUE.
	 */
	protected boolean validateResponse(RequestType requestType, ServiceResponse response, ServiceRequest request)
			throws ServiceResultException {
		if (response == null || response.getResponseHeader() == null) {
			throw new IllegalArgumentException("ResponseHeader");
		}
		if (response.getResponseHeader().getServiceResult().isBad()) {
			throw new ServiceResultException(response.getResponseHeader().getServiceResult());
		}
		UnsignedInteger responseHandle = (response.getResponseHeader().getRequestHandle() == null)
				? UnsignedInteger.ZERO
				: response.getResponseHeader().getRequestHandle();
		UnsignedInteger requestHandle = (request.getRequestHeader().getRequestHandle() == null) ? UnsignedInteger.ZERO
				: request.getRequestHeader().getRequestHandle();
		if (!responseHandle.equals(requestHandle)) {
			throw new ServiceResultException(StatusCodes.Bad_UnknownResponse);
		}
		Logger.getLogger(getClass().getName()).log(Level.FINE, "{0} executed", new String[] { requestType.name() });
		return false;
	}

	/**
	 * Validates a response returned by the server.
	 *
	 * @param values
	 * @param result
	 * @throws ServiceResultException
	 */
	protected void validateResponse(DataValue[] values) throws ServiceResultException {
		if (values == null) {
			throw new ServiceResultException(new StatusCode(StatusCodes.Bad_UnexpectedError));
		}
	}

	protected void createNewServiceChannel(EndpointDescription endpoint, EndpointConfiguration endpointConfiguration)
			throws ServiceResultException {
		// close the old channel, which is closed/inactive/not existent, for
		// this session
		if (this.transportChannel != null && this.transportChannel.isOpen()) {
			this.transportChannel.close();
		}
		TransportChannelSettings settings = new TransportChannelSettings(endpoint, endpointConfiguration, this.cert,
				this.privateKey, CertificateValidator.ALLOW_ALL, getNamespaceUris());
		settings.getHttpsSettings().readFrom(clientInstance.getApplicationHttpsSettings());
		settings.getOpctcpSettings().readFrom(clientInstance.getApplication().getOpctcpSettings());
		this.transportChannel = this.clientInstance.createSecureChannel(settings);
		// this.transportChannel.getConnection().get
	}

	protected void setAuthentificationToken(NodeId authentificationToken) {
		this.authentificationToken = authentificationToken;
	}

	protected abstract void onError(RequestType requestType, ServiceRequest request, ServiceResponse response,
			ServiceResultException e) throws ServiceResultException;

	protected void setAsyncStop(boolean asyncRequestStop, long timeOut) {
		this.asyncRequestStop = asyncRequestStop;
		this.asynctimeOut = timeOut;
	}

	/**
	 * Returns the publish request count
	 *
	 * @return
	 */
	protected int outstandingPublishRequestCount() {
		int count = 0;
		synchronized (this.lock) {
			ListIterator<AsyncRequestState> iterator = this.outstandingRequests.listIterator();
			while (iterator.hasNext()) {
				try {
					AsyncRequestState result = iterator.next();
					if (result.getResult() == null || result.getResult().getResult() == null
							|| result.getResult().getError() != null) {
						iterator.remove();
						continue;
					}
					if (Identifiers.PublishRequest.equals(result.getRequestTypeId())) {
						count++;
					}
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return count;
	}

	AsyncResult<ServiceResponse> beginRepublish(RepublishRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("RepublishRequest");
		}
		updateRequestHeader(request, RequestType.Republish);
		return this.transportChannel.serviceRequestAsync(request);
	}

	/**
	 * Getter for the ServiceChannel on wich a session sends services to a server.
	 *
	 * @return ServiceChannel
	 */
	SecureChannel getTransportChannel() {
		return this.transportChannel;
	}

	/**
	 * Gets the authentification token of this session.
	 *
	 * @return AuthentificationToken
	 */
	NodeId getAuthentificationToken() {
		return this.authentificationToken;
	}

	private void createChannel(EndpointDescription endpoint, EndpointConfiguration endpointConfiguration,
			CreateSessionRequest request) throws ServiceResultException {
		try {
			if (endpoint != null) {
				createNewServiceChannel(endpoint, endpointConfiguration);
			} else {
				// DEFAULT CONNECTION
				this.transportChannel = this.clientInstance.createSecureChannel(request.getServerUri());
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			throw e;
		}
	}

	/**
	 * Converts a service response to a ServiceResult object.
	 */
	private ServiceResult getResult(StatusCode statusCode) {
		return new ServiceResult(statusCode);
	}

	/**
	 * Updates the {@link RequestHeader} for a Request.
	 *
	 * @param Request      The {@link ServiceRequest} which will be updatet with the
	 *                     header.
	 * @param RequesetType Type of the Request.
	 */
	private void updateRequestHeader(ServiceRequest request, RequestType requestType) {
		synchronized (this.lock) {
			boolean usedefault = request.getRequestHeader() == null;
			// request header
			if (request.getRequestHeader() == null) {
				request.setRequestHeader(new RequestHeader());
			}
			if (usedefault) {
				request.getRequestHeader().setReturnDiagnostics(this.diagnosticsMask.getDiagnosticMask());
			}
			// requestid
			if (request.getRequestHeader().getRequestHandle() == null
					|| request.getRequestHeader().getRequestHandle().intValue() == 0) {
				int requestId = this.nextRequestHandle.getCurrentSendSequenceNumber();
				this.nextRequestHandle.getNextSendSequencenumber();
				request.getRequestHeader().setRequestHandle(new UnsignedInteger(requestId));
			}
			// authentification token
			if (NodeId.isNull(request.getRequestHeader().getAuthenticationToken())) {
				request.getRequestHeader().setAuthenticationToken(this.authentificationToken);
				if (this.authentificationToken == null) {
					request.getRequestHeader().setAuthenticationToken(new NodeId(0, 0));
				}
			}
			// request timeout
			if (request.getRequestHeader().getTimeoutHint() == null
					|| request.getRequestHeader().getTimeoutHint().longValue() <= 0) {
				if (this.asyncRequestStop && !RequestType.Publish.equals(requestType)) {
					request.getRequestHeader().setTimeoutHint(new UnsignedInteger(this.asynctimeOut));
				} else if (!this.asyncRequestStop && !RequestType.Publish.equals(requestType)
						&& transportChannel != null && transportChannel.isOpen()) {
					request.getRequestHeader()
							.setTimeoutHint(new UnsignedInteger(this.transportChannel.getOperationTimeout() / 2));
				}
			}
			// timestamp
			request.getRequestHeader().setTimestamp(DateTime.currentTime());
			// audit entry
			createAuditLogEntry(request);
		}
	}

	private void createAuditLogEntry(ServiceRequest request) {
		request.getRequestHeader().setAuditEntryId(request.getRequestHeader().getAuditEntryId());
	}

	abstract void onConnectionLost(ServiceResult result);

	abstract CloseSessionResponse closeSession(boolean deleteSubscriptions) throws ServiceResultException;

	abstract void asyncRequestStarted(AsyncResult<?> result, UnsignedInteger requestHandle, NodeId requestTypeId);

	public abstract NamespaceTable getNamespaceUris();

	class PublishResultListener implements ResultListener<PublishResponse> {
		/** Session, from which the Request has been sent */
		private ClientSession session = null;
		private PublishRequest request = null;
		private boolean isMoreNotifications;

		/**
		 * Publish Result Listener
		 *
		 * @param session           Session that the Service has been sent
		 * @param request
		 * @param moreNotifications
		 */
		public PublishResultListener(ClientSession session, PublishRequest request, boolean moreNotifications) {
			this.session = session;
			this.request = request;
			this.isMoreNotifications = moreNotifications;
		}

		/**
		 * Called when an asynchronous Publish - Service completes.
		 *
		 * @Override
		 * @param Result {@link PublishResponse} of the completed Service.
		 */
		@Override
		public void onCompleted(PublishResponse result) {
			PublishResponse response = result;
			boolean skipMessage = false;
			boolean skipResponse = false;
			try {
				boolean isError = validateResponse(RequestType.Publish, response, request);
				if (isError) {
					throw new ServiceResultException(StatusCode.BAD);
				}
			} catch (ServiceResultException e) {
				try {
					InternalClient.this.onError(RequestType.Publish, this.request, response, e);
				} catch (ServiceResultException e1) {
					Logger.getLogger(getClass().getName()).log(Level.FINE, null, e1);
					// CTT 11.4-017 - 021
					skipMessage = true;
					if (e1.getStatusCode() != null && e1.getStatusCode().isGood()) {
						skipResponse = true;
					}
				}
			}
			// check the results with the requestedsubscriptionacknowledgements
			if (this.request.getSubscriptionAcknowledgements() != null && response.getResults() != null
					&& this.request.getSubscriptionAcknowledgements().length > 0
					&& this.request.getSubscriptionAcknowledgements().length != response.getResults().length) {
				int index = 0;
				for (SubscriptionAcknowledgement ack : this.request.getSubscriptionAcknowledgements()) {
					if (index + 1 > response.getResults().length) {
						Logger.getLogger(getClass().getName()).log(Level.FINE,
								"{0} The size of the results array does not match with the size of the requested subscription acknowledge array! Error occured on subscription {1} with the sequence number {2}!",
								new Object[] { RequestType.Publish.name(), ack.getSubscriptionId(),
										ack.getSequenceNumber() });
					}
					index++;
				}
			}
			boolean moreNotifications = false;
			if (!skipMessage) {
				moreNotifications = this.session.processPublishResponse(RequestType.Publish,
						response.getSubscriptionId(), response.getAvailableSequenceNumbers(),
						response.getMoreNotifications(), response.getNotificationMessage(), false, isMoreNotifications);
			}
			this.session.asyncRequestCompleted(response.getResponseHeader().getRequestHandle(),
					Identifiers.PublishRequest);
			if (!skipResponse) {
				int outstandingPublishRequests = outstandingPublishRequestCount();
				int maxPublishRequests = this.session.getSubscriptions().length;
				if (outstandingPublishRequests < maxPublishRequests)
					this.session.beginPublish(moreNotifications);
			}
		}

		/**
		 * Called when an Asynchronous Publish Service occurs an error.
		 *
		 * @Override
		 * @param Error Error occured on the Service operation.
		 */
		@Override
		public void onError(ServiceResultException e) {
			// dummy for further use
		}
	}
}
