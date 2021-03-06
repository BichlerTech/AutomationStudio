package opc.client.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
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
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseNextRequest;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.CallMethodRequest;
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
import org.opcfoundation.ua.core.EndpointConfiguration;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.GetEndpointsRequest;
import org.opcfoundation.ua.core.GetEndpointsResponse;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ModifyMonitoredItemsRequest;
import org.opcfoundation.ua.core.ModifyMonitoredItemsResponse;
import org.opcfoundation.ua.core.ModifySubscriptionRequest;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.NotificationMessage;
import org.opcfoundation.ua.core.PublishRequest;
import org.opcfoundation.ua.core.PublishResponse;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.RegisterNodesRequest;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.RepublishRequest;
import org.opcfoundation.ua.core.RepublishResponse;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.ServerState;
import org.opcfoundation.ua.core.ServerStatusDataType;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.SetMonitoringModeResponse;
import org.opcfoundation.ua.core.SetPublishingModeRequest;
import org.opcfoundation.ua.core.SetPublishingModeResponse;
import org.opcfoundation.ua.core.SetTriggeringRequest;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.SignedSoftwareCertificate;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.SubscriptionAcknowledgement;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.TransferSubscriptionsRequest;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.UnregisterNodesRequest;
import org.opcfoundation.ua.core.UnregisterNodesResponse;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.transport.AsyncResult;
import org.opcfoundation.ua.transport.ChannelService;
import org.opcfoundation.ua.transport.SecureChannel;
import org.opcfoundation.ua.transport.UriUtil;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityAlgorithm;
import org.opcfoundation.ua.transport.security.SecurityConstants;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.CryptoUtil;
import org.opcfoundation.ua.utils.EndpointUtil;
import org.opcfoundation.ua.utils.NumericRange;

import opc.client.application.core.ApplicationConfiguration;
import opc.client.application.listener.ConnectionListener;
import opc.client.application.listener.ReconnectListener;
import opc.client.application.listener.ServerStateListener;
import opc.client.service.node.NodeCache;
import opc.sdk.core.classes.ua.core.NamespaceMetadataType;
import opc.sdk.core.classes.ua.core.PropertyType;
import opc.sdk.core.context.StringTable;
import opc.sdk.core.context.SystemContext;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.node.AbstractNodeFactory;
import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.Node;
import opc.sdk.core.result.AsyncRequestState;
import opc.sdk.core.session.AllowNoneCertificateValidator;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.session.UserIdentityRole;
import opc.sdk.core.types.TypeTable;

/**
 * An instance of the client session. This session is used to send services to
 * an server and stores particular information.
 *
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class ClientSession extends InternalClient {
	private UnsignedInteger maxResponseMessageSize = new UnsignedInteger(1000);
	/** Session Timeout Count */
	private double sessionTimeout = -1;
	/** Table of NamespaceUris from the server */
	private NamespaceTable namespaceUris = null;
	/** Namespace Metadatas */
	private NamespaceMetadataType[] namespaceMetaDatas;
	/** Client Application Description */
	private ApplicationDescription applicationDescription = null;
	/** Client Instance Certificate to identify the application */
	private Cert clientCertificate = null;
	/** Publish Acknowledgments To Send */
	private List<SubscriptionAcknowledgement> acknowledgementsToSend = null;
	/** KeepAliveInterval of the Session */
	private long keepAliveInterval = -1;
	/** Client Application Settings */
	private ApplicationConfiguration configuration = null;
	/** Endpoint Configuration of the connected Endpoint */
	private EndpointConfiguration endpointConfiguration = null;
	/** Endpoint from the Server, which the session has been created */
	private EndpointDescription endpoint = null;
	/** Preferred Locales from the Server */
	private String[] preferredLocales = null;
	/** Client Sysetem context */
	private SystemContext systemContext = null;
	/** Server Uris available */
	private StringTable serverUris = null;
	/** Unique Identifier of the Session */
	private NodeId sessionId = null;
	/** Server Nonce */
	private byte[] serverNonce = null;
	/** Server Endpoints */
	private EndpointDescription[] serverEndpoints = null;
	/** Signed Software ceritificates */
	private SignedSoftwareCertificate[] serverSoftwareCertificates = null;
	/** Signature Data of the server */
	private SignatureData serverSignature = null;
	/** Maximum requested message size */
	private UnsignedInteger maxRequestMessageSize = null;
	/** Security Policy used by the Session */
	private String securityPolicyUri = null;
	/** User Identity of the Session */
	private UserIdentityRole identity = null;
	/** Flag if the session is reconnection */
	private boolean reconnecting = false;
	/** State of the Server */
	private ServerState serverState = null;
	/** Keep-Alive-Timer for the Session */
	private Timer keepAliveTimer = null;
	/** NodeCache to store OPC UA Nodes on the Client, to reduce network */
	private NodeCache nodeCache = null;
	/** Subscription Manager which handles all Subscriptions for a Client */
	private SubscriptionManager subscriptionManager = null;
	/** Flag to initalize the session asynchronous or synchronous */
	private boolean asyncCreation = false;
	/** Client nonce */
	private byte[] clientNonce = null;
	/** Certificate from the Server (Endpoint) */
	private Cert serverCertificate = null;
	/** interval to try reconnecting */
	private long reconnectPeriod = 5000;
	/** cache to reduce communication overhead */
	private NodeManager nodeManager = null;
	/** Reconection handler */
	private ReconnectSessionHandler reconnectHandler = null;
	/** session manager from the framework */
	private SessionManager sessionManager = null;
	/** name of the session */
	private String sessionName = null;
	/** security certificate of the session */
	private KeyPair clientApplicationInstanceCertificate = null;
	/** session state listeners */
	private List<ServerStateListener> serverStateListeners = new ArrayList<>();
	/** session connection listeners */
	private List<ConnectionListener> connectionListeners = new ArrayList<>();
	/** session reconnection listeners */
	private List<ReconnectListener> reconnectListeners = new ArrayList<>();
	private boolean asyncRequestStop = false;
	/** factory to create with multiple read-services a node */
	private AbstractNodeFactory nodeFactory = null;
	/** service executor */
	private ExecutorService publishExecutor = null;
	/** task to monitor the sessions alive state */
	private SessionKeepAliveTask keepAliveTask = null;
	private long maxMonitoredItemsPerCreate = 65535;
	private boolean readServerLimits = true;
	private long maxMonitoredItemsPerCreateDefault = 65535;
	/** flag to prevent error messages after session close */
	private boolean closeCalled = false;
	private int retransSequenceNumber = 1;
	private long maxNodesPerWrite = 65535;
	private long maxNodesPerWriteDefault = 65535;
	private long maxTranslateBrowsePath = 65535;
	private long maxTranslateBrowsePathDefault = 65535;
	private long maxRegisterNodes = 65535;
	private long maxRegisterNodesDefault = 65535;
	private long maxRead = 65535;
	private long maxReadDefault = 65535;
	private long maxNodeManagement = 65535;
	private long maxNodeManagementDefault = 65535;
	private long maxMethodCall = 65535;
	private long maxMethodCallDefault = 65535;
	private long maxHistoryUpdateEvent = 65535;
	private long maxHistoryUpdateEventDefault = 65535;
	private long maxHistoryUpdateData = 65535;
	private long maxHistoryUpdateDataDefault = 65535;
	private long maxHistoryReadEvent = 65535;
	private long maxHistoryReadEventDefault = 65535;
	private long maxHistoryReadData = 65535;
	private long maxHistoryReadDataDefault = 65535;
	private long maxNodesPerBrowse = 65535;
	private long maxNodesPerBrowseDefault = 65535;
	private boolean validateEndpoints = true;
	private long maxFutureServerTime = 0;
	private long maxPastServerTime = 0;
	private long reconnectCount = -1;
	private long reconnectCounter = -1;
	private boolean reCreateSubscriptions = true;

	/**
	 * New Instance of a client session
	 *
	 * @param ClientConfiguration                  Configuration of the client
	 *                                             framework.
	 * @param Endpoint                             Server endpoint to connect.
	 * @param EndpointConfiguration                Endpoint configuration
	 * @param ClientApplicationInstanceCertificate Security keypair.
	 * @param ProfileManager                       Framwork profilemanager
	 * @param nodeManager                          Framwork node
	 * @param SessionManager                       Framwork sessionmanager
	 * @param AsyncOperation                       Sync/Async api
	 * @param SubscriptionsPerSession              Number of maximum subscriptions
	 *                                             per session
	 */
	public ClientSession(ApplicationConfiguration clientConfiguration, EndpointDescription endpoint,
			EndpointConfiguration endpointConfiguration, KeyPair clientApplicationInstanceCertificate,
			SubscriptionManager subscriptionManager, NodeManager nodeManager, SessionManager sessionManager,
			boolean asyncOperation) {
		super();
		initialize(clientConfiguration, endpoint, endpointConfiguration, clientApplicationInstanceCertificate);
		this.subscriptionManager = subscriptionManager;
		this.nodeManager = nodeManager;
		this.sessionManager = sessionManager;
		this.asyncCreation = asyncOperation;
		this.nodeFactory = new DefaultNodeFactory();
	}

	public boolean isCloseCalled() {
		return closeCalled;
	}

	public void setCloseCalled(boolean closeCalled) {
		this.closeCalled = closeCalled;
	}

	/**
	 * Set the time (ms) to start autoreconnect. Default is 5000ms (5sec)
	 *
	 * @param Period Delay from losing the connection to reconnect
	 */
	public void setReconnectionPeriod(long period) {
		this.reconnectPeriod = period;
	}

	/**
	 * Registers a SessionConnection Listener to the session.
	 *
	 * @param Listeners ConnectionListners
	 */
	public void addConnectionListener(ConnectionListener... listeners) {
		synchronized (this.lock) {
			for (ConnectionListener l : listeners) {
				this.connectionListeners.add(l);
			}
		}
	}

	/**
	 * Registers a ServerState Listener to the session, reacting on ServerStatus
	 * Changes.
	 *
	 * @param Listeners ReconnectionListener
	 */
	public void addReconnectListener(ReconnectListener... listeners) {
		synchronized (this.lock) {
			for (ReconnectListener l : listeners) {
				this.reconnectListeners.add(l);
			}
		}
	}

	/**
	 * Registers a ServerState Listener to the session, reacting on ServerStatus
	 * Changes.
	 *
	 * @param Listener ServerStateListener
	 */
	public void addServerStateListener(ServerStateListener... listeners) {
		synchronized (this.lock) {
			for (ServerStateListener l : listeners) {
				this.serverStateListeners.add(l);
			}
		}
	}

	/**
	 * Cancel the sessions reconnecting mechanism.
	 */
	public void cancelReconnecting() {
		if (this.reconnectHandler != null) {
			synchronized (this.lock) {
				if (this.reconnectHandler != null) {
					this.reconnectHandler.cancelReconnect();
				}
			}
		}
	}

	/**
	 * Call an OPC UA servers refresh method with a subscription.
	 *
	 * @param Subscription
	 * @param SkipLogging  TRUE skips logging, otherwise FALSE.
	 * @return Result of the method.
	 * @throws ServiceResultException
	 */
	public CallMethodResult callRefresh(Subscription subscription) throws ServiceResultException {
		NodeId objectId = Identifiers.ConditionType;
		NodeId methodId = Identifiers.ConditionType_ConditionRefresh;
		List<Variant> inputArguments = new ArrayList<>();
		if (subscription != null)
			inputArguments.add(new Variant(subscription.getSubscriptionId()));
		CallMethodRequest methodToCall = new CallMethodRequest();
		methodToCall.setObjectId(objectId);
		methodToCall.setMethodId(methodId);
		methodToCall.setInputArguments(inputArguments.toArray(new Variant[inputArguments.size()]));
		CallRequest request = new CallRequest();
		request.setMethodsToCall(new CallMethodRequest[] { methodToCall });
		if (subscription != null) {
			for (MonitoredItem item : subscription.getMonitoredItems()) {
				if (item.getEventCache() != null) {
					item.getEventCache().refresh();
				}
			}
		}
		return callMethod(request).getResults()[0];
	}

	/**
	 * Returns all connection listeners.
	 *
	 * @return ConnectionListeners
	 */
	public ConnectionListener[] getConnectionListeners() {
		synchronized (this.lock) {
			return this.connectionListeners.toArray(new ConnectionListener[0]);
		}
	}

	/**
	 * Returns all reconnection listeners.
	 *
	 * @return ReconnectionListeners
	 */
	public ReconnectListener[] getReconnectListeners() {
		synchronized (this.lock) {
			return this.reconnectListeners.toArray(new ReconnectListener[0]);
		}
	}

	/**
	 * Sets the Keep Alive Interval.
	 *
	 * @param Interval KeepAliveInterval
	 */
	public void setKeepAliveInterval(long interval) {
		this.keepAliveInterval = interval;
		setAsyncStop(this.asyncRequestStop, interval * 2);
		if (this.keepAliveTask != null) {
			startKeepAliveTimer();
		}
	}

	/**
	 * Returns all server state listeners.
	 *
	 * @return ServerstateListeners
	 */
	public ServerStateListener[] getServerStateListeners() {
		synchronized (this.lock) {
			return this.serverStateListeners.toArray(new ServerStateListener[0]);
		}
	}

	/**
	 * Reconnects a session.
	 */
	public void reconnect() {
		if (this.reconnectHandler == null) {
			synchronized (this.lock) {
				if (!ServerState.Unknown.equals(this.serverState)) {
					for (ReconnectListener listener : getReconnectListeners()) {
						listener.onConnectionLost(this);
					}
					// change state
					this.serverState = ServerState.Unknown;
				}
				if (this.keepAliveTask != null) {
					this.keepAliveTask.cancel();
					this.keepAliveTask = null;
				}
				/* Unused */
				this.reconnectHandler = new ReconnectSessionHandler();
				this.reconnectHandler.beginReconnect(this);
			}
		}
	}

	/**
	 * Start the reconnecting mechanism.
	 *
	 * @param Delay  Delay to start.
	 * @param Period Intervals to try reconnecting.
	 */
	public void reconnect(long delay, long period) {
		if (this.reconnectHandler == null) {
			synchronized (this.lock) {
				if (!ServerState.Unknown.equals(this.serverState)) {
					for (ReconnectListener listener : getReconnectListeners()) {
						listener.onConnectionLost(this);
					}
					// change state
					this.serverState = ServerState.Unknown;
				}
				if (this.keepAliveTask != null) {
					this.keepAliveTask.cancel();
					this.keepAliveTask = null;
				}
				// now stop keep alive timer
				if (this.keepAliveTimer != null) {
					this.keepAliveTimer.cancel();
					this.keepAliveTimer.purge();
					this.keepAliveTimer = null;
				}
				// only start reconnecting if counter > 0
				if (reconnectCount > 0) {
					this.reconnectHandler = new ReconnectSessionHandler();
					this.reconnectHandler.beginReconnect(this, delay, period);
				} else {
					subscriptionManager.deleteSubscriptionsInternal(this);
					sessionManager.closeSessionInternally(this);
				}
			}
		}
	}

	/**
	 * Remove connection listeners from the session.
	 *
	 * @param Listeners ConnectionListeners to remove.
	 */
	public void removeConnectionListener(ConnectionListener... listeners) {
		synchronized (this.connectionListeners) {
			for (ConnectionListener l : listeners) {
				this.connectionListeners.remove(l);
			}
		}
	}

	/**
	 * Remove reconnection listeners from the session.
	 *
	 * @param Listeners ReconnectionListeners to remove.
	 */
	public void removeReconnectListener(ReconnectListener... listeners) {
		synchronized (this.lock) {
			for (ReconnectListener l : listeners) {
				this.reconnectListeners.remove(l);
			}
		}
	}

	/**
	 * Returns the maximum session timeout (ms);
	 *
	 * @return SessionTimeout
	 */
	public double getSessionTimeout() {
		return sessionTimeout;
	}

	/**
	 * Getter of the ClientNonce when creating the session.
	 *
	 * @return ClientNonce
	 */
	public byte[] getCreateSessionClientNonce() {
		return this.clientNonce;
	}

	/**
	 * Returns the servers application description.
	 *
	 * @return ApplicationDescription
	 */
	public ApplicationDescription getApplicationDescription() {
		return applicationDescription;
	}

	/**
	 * Returns the endpoint configuration.
	 *
	 * @return EndpointConfiguration
	 */
	public EndpointConfiguration getEndpointConfiguration() {
		return endpointConfiguration;
	}

	/**
	 * Returns the endpoint.
	 *
	 * @return Endpoint
	 */
	public EndpointDescription getEndpoint() {
		return endpoint;
	}

	/**
	 * Returns the sessions system context.
	 *
	 * @return SystemContext
	 */
	public SystemContext getSystemContext() {
		return systemContext;
	}

	/**
	 * Gets the NodeManager of this session
	 *
	 * @return NodeMaager
	 */
	public NodeManager getNodeManager() {
		return this.nodeManager;
	}

	/**
	 * Returns the number of maximum size of request queue.
	 *
	 * @return MaxRequestMessageSize
	 */
	public UnsignedInteger getMaxRequestMessageSize() {
		return maxRequestMessageSize;
	}

	/**
	 * Returns the current OPC UA server state.
	 *
	 * @return ServerState
	 */
	public ServerState getServerState() {
		return serverState;
	}

	/**
	 * Returns the Type Table.
	 *
	 * @return TypeTable
	 */
	public TypeTable getTypeTree() {
		return this.nodeCache.getTypeTree();
	}

	/**
	 * Returns the login identity.
	 *
	 * @return Identity
	 */
	public UserIdentityRole getIdentity() {
		return this.identity;
	}

	/**
	 * Returns the keepalive interval.
	 *
	 * @return KeepAliveInterval
	 */
	public long getKeepAliveInterval() {
		return this.keepAliveInterval;
	}

	/**
	 * Returns the client nonce.
	 *
	 * @return ClientNonce
	 */
	public byte[] getClientNonce() {
		return this.clientNonce;
	}

	/**
	 * Returns the clientside node cache.
	 *
	 * @return NodeCache
	 */
	public NodeCache getNodeCache() {
		return nodeCache;
	}

	/**
	 * Returns a timestamp of the last keep alive.
	 *
	 * @return LastKeepAliveTime
	 */
	public DateTime getLastKeepAliveTime() {
		return lastKeepAliveTime;
	}

	/**
	 * Returns the Namespace Uri Table.
	 *
	 * @return NamespaceUris
	 */
	public NamespaceTable getNamespaceUris() {
		return this.namespaceUris;
	}

	/**
	 * Returns the Namespace Metadatas.
	 * 
	 * @return NamespaceMetadatas
	 */
	public NamespaceMetadataType[] getNamespaceMetadatas() {
		return this.namespaceMetaDatas;
	}

	/**
	 * Returns the Preferred Locales.
	 *
	 * @return PreferredLocales
	 */
	public String[] getPreferredLocales() {
		return this.preferredLocales;
	}

	/**
	 * Returns the session manager.
	 *
	 * @return SessionManager
	 */
	public SessionManager getSessionManager() {
		return this.sessionManager;
	}

	/**
	 * Returns the name of the session.
	 *
	 * @return SessionName
	 */
	public String getSessionName() {
		return this.sessionName;
	}

	/**
	 * Returns the SessionId.
	 *
	 * @return SessionId
	 */
	public NodeId getSessionId() {
		return this.sessionId;
	}

	/**
	 * Returns the Server Certificate.
	 *
	 * @return ServerCertificate
	 */
	public Cert getServerCertificate() {
		return this.serverCertificate;
	}

	/**
	 * Returns the Endpoint to connect. This endpoint will be used when connecting
	 * to an UA Server.
	 *
	 * @return Endpoint
	 */
	public EndpointDescription getConnectedEndpoint() {
		return this.endpoint;
	}

	/**
	 * Returns all Server Endpoints, which are able to connect.
	 *
	 * @return ServerEndpoints
	 */
	public EndpointDescription[] getServerEndpoints() {
		return this.serverEndpoints;
	}

	/**
	 * Returns the used Security Policy.
	 *
	 * @return SecurityPolicy
	 */
	public String getSecurityPolicyUri() {
		return this.securityPolicyUri;
	}

	/**
	 * Returns the Server Signature.
	 *
	 * @return ServerCertificate
	 */
	public SignatureData getServerSignature() {
		return this.serverSignature;
	}

	/**
	 * Returns the ApplicationConfiguration.
	 *
	 * @return ApplicationConfiguration
	 */
	public ApplicationConfiguration getConfiguration() {
		return this.configuration;
	}

	/**
	 * Returns the ServerSoftwareCertificates.
	 *
	 * @return ServerSoftwareCertificates
	 */
	public SignedSoftwareCertificate[] getServerSoftwareCertificates() {
		return this.serverSoftwareCertificates;
	}

	/**
	 * Returns the Server Nonce.
	 *
	 * @return ServerNonce
	 */
	public byte[] getServerNonce() {
		return this.serverNonce;
	}

	/**
	 * Returns the Client Certificate.
	 *
	 * @return InstanceCertificate.
	 */
	public Cert getClientCertificate() {
		return this.clientCertificate;
	}

	/**
	 * Returns the UA User Identity.
	 *
	 * @return UserIdentity
	 */
	public UserIdentityToken getUserIdentityToken() {
		return this.identity.getIdentityToken();
	}

	public boolean isAsyncCreation() {
		return asyncCreation;
	}

	/**
	 * Is client in reconnecting state
	 *
	 * @return
	 */
	public boolean isReconnection() {
		return this.reconnecting;
	}

	/**
	 * Returns the Server Uri Table.
	 *
	 * @return ServerUris
	 */
	public StringTable getServerUris() {
		return this.serverUris;
	}

	/**
	 * Returns all subscription from the session.
	 *
	 * @return Subscriptions
	 */
	public Subscription[] getSubscriptions() {
		return this.subscriptionManager.getSubscriptionsPerSession(this);
	}

	/**
	 * Returns all publish acknowledgements to send.
	 *
	 * @return AcknowledgementsToSend
	 */
	public List<SubscriptionAcknowledgement> getAcknowledgementsToSend() {
		synchronized (this.lock) {
			return this.acknowledgementsToSend;
		}
	}

	/**
	 * Activates a Session on a Server
	 *
	 * @param userTokenSignature
	 * @param extensionObject
	 * @param clientSoftwareCertificates
	 * @param clientSignature
	 * @param identity2
	 * @param object
	 * @return
	 * @throws ServiceResultException BAD_UserAccessDenied
	 */
	protected ActivateSessionResponse activate(RequestHeader requestHeader, SignatureData clientSignature,
			List<SignedSoftwareCertificate> clientSoftwareCertificates, ExtensionObject userIdentityToken,
			UserIdentity identity, SignatureData userTokenSignature) throws ServiceResultException {
		ActivateSessionRequest request = new ActivateSessionRequest();
		ActivateSessionResponse response;
		request.setRequestHeader(requestHeader);
		request.setClientSignature(clientSignature);
		request.setClientSoftwareCertificates(
				clientSoftwareCertificates.toArray(new SignedSoftwareCertificate[clientSoftwareCertificates.size()]));
		request.setLocaleIds(this.preferredLocales);
		request.setUserIdentityToken(userIdentityToken);
		request.setUserTokenSignature(userTokenSignature);
		this.identity = (UserIdentityRole) identity;
		response = activateSession(request);
		endActivateSession(response);
		return response;
	}

	/**
	 * Completes an Asynchronous Publish Operation and removes the request from the
	 * Asynchronous request queue
	 *
	 * @param publishrequest
	 * @param unsignedInteger
	 * @param response
	 */
	protected void asyncRequestCompleted(UnsignedInteger requestHandle, NodeId requestTypeId) {
		synchronized (this.lock) {
			// remove the request
			AsyncRequestState state = removeRequest(requestHandle, requestTypeId);
			if (state != null) {
				// mark any old request as default (i.e. they should have
				// returned before this request)
				DateTime maxAge = state.getTimestamp();
				for (AsyncRequestState outstandingRequest : this.outstandingRequests) {
					if (outstandingRequest.getRequestTypeId().equals(requestTypeId)
							&& outstandingRequest.getTimestamp().getValue() < maxAge.getValue()) {
						outstandingRequest.setDefunct(true);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param requestHeader
	 * @param clientSignature
	 * @param clientSoftwareCertificates
	 * @param userIdentityToken
	 * @param identity
	 * @param userTokenSignature
	 * @return
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected ActivateSessionResponse beginActivateSession(RequestHeader requestHeader, SignatureData clientSignature,
			List<SignedSoftwareCertificate> clientSoftwareCertificates, ExtensionObject userIdentityToken,
			UserIdentity identity, SignatureData userTokenSignature) throws ServiceResultException {
		ActivateSessionRequest request = new ActivateSessionRequest();
		AsyncResult<ServiceResponse> result;
		request.setRequestHeader(requestHeader);
		request.setClientSignature(clientSignature);
		request.setClientSoftwareCertificates(
				clientSoftwareCertificates.toArray(new SignedSoftwareCertificate[clientSoftwareCertificates.size()]));
		request.setLocaleIds(this.preferredLocales);
		request.setUserIdentityToken(userIdentityToken);
		request.setUserTokenSignature(userTokenSignature);
		this.identity = (UserIdentityRole) identity;
		result = beginActivateSession(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), Identifiers.ActivateSessionRequest);
		ActivateSessionResponse response = (ActivateSessionResponse) waitForResponse(RequestType.ActivateSession,
				result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), Identifiers.ActivateSessionRequest);
		endActivateSession(response);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Add-Nodes Service.
	 *
	 * @param Request   {@link AddNodesRequest} with the information to create a new
	 *                  node on the server.
	 * @param RequestId Defined Id for an {@link AddNodesRequest}.
	 * @return {@link AsyncResult} of a {@link AddNodesResponse}.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected AddNodesResponse beginAddNodes(AddNodesRequest request, NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginAddNodes(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		AddNodesResponse response = (AddNodesResponse) waitForResponse(RequestType.AddNodes, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Add-Reference Service.
	 *
	 * @param Request   {@link AddReferencesRequest} with the information to create
	 *                  a new reference on the server.
	 * @param RequestId Defined Id for an {@link AddReferencesRequest}.
	 * @return {@link AsyncResult} of a {@link AddReferencesResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected AddReferencesResponse beginAddReferences(AddReferencesRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginAddReferences(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		AddReferencesResponse response = (AddReferencesResponse) waitForResponse(RequestType.AddReferences, result,
				request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Browse Service.
	 *
	 * @param Request   {@link BrowseRequest} to discover the References of a
	 *                  specified Node.
	 * @param RequestId Defined Id for an {@link BrowseRequest}.
	 * @return {@link AsyncResult} of a {@link BrowseResponset}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected BrowseResponse beginBrowse(BrowseRequest request, NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginBrowse(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		BrowseResponse response = (BrowseResponse) waitForResponse(RequestType.Browse, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Browse-Next Service.
	 *
	 * @param Request   {@link BrowseNextRequest} to continue on an existing browse.
	 * @param RequestId Defined Id for an {@link BrowseNextRequest}.
	 * @return {@link AsyncResult} of a {@link BrowseNextResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected BrowseNextResponse beginBrowseNext(BrowseNextRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginBrowseNext(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		BrowseNextResponse response = (BrowseNextResponse) waitForResponse(RequestType.BrowseNext, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Call Service.
	 *
	 * @param Request   {@link CallRequest} to call a Method on the server.
	 * @param RequestId Defined Id for an {@link CallRequest}.
	 * @return {@link AsyncResult} of a {@link CallResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected CallResponse beginCall(CallRequest request, NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginCallMethod(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		CallResponse response = (CallResponse) waitForResponse(RequestType.Call, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocationo of the Cancel Service.
	 *
	 * @param Request   {@link CancelRequest} to cancel a {@link ServiceRequest}.
	 * @param RequestId Defined Id for an {@link CallRequest}.
	 * @return {@link AsyncResult} of a {@link CancelResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected AsyncResult<ServiceResponse> beginCancelRequest(CancelRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginCancelRequest(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		CancelResponse response = (CancelResponse) waitForResponse(RequestType.Cancel, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		endCancelRequest(response);
		return result;
	}

	/**
	 * Begins an asynchronous invocation of the Close-Session Service.
	 *
	 * @param Request             {@link CloseSessionRequest} to disconnect a
	 *                            session from the server.
	 * @param DeleteSubscriptions If the value is TRUE, the Server deletes all
	 *                            Subscriptions associated with the Session. If the
	 *                            value is FALSE, the Server keeps the Subscriptions
	 *                            associated with the Session until they timeout
	 *                            based on their own lifetime.
	 * @return {@link AsyncResult} of a {@link CloseSessionResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected CloseSessionResponse beginCloseSession(boolean deleteSubscriptions, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		preCloseSession(deleteSubscriptions);
		CloseSessionRequest request = new CloseSessionRequest();
		request.setDeleteSubscriptions(deleteSubscriptions);
		AsyncResult<ServiceResponse> result = beginCloseSession(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		CloseSessionResponse response = (CloseSessionResponse) waitForResponse(RequestType.CloseSession, result,
				request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		endCloseSession();
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Create-Monitored-Item Service.
	 *
	 * @param Request   {@link CreateMonitoredItemsRequest} to create
	 *                  {@link MonitoredItem} to monitor DataChanges or Events.
	 * @param RequestId Defined Id for an {@link CreateMonitoredItemsRequest}.
	 * @return {@link AsyncResult} of a {@link CreateMonitoredItemsResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected CreateMonitoredItemsResponse beginCreateMonitoredItems(CreateMonitoredItemsRequest request,
			NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginCreateMonitoredItem(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		CreateMonitoredItemsResponse response = (CreateMonitoredItemsResponse) waitForResponse(
				RequestType.CreateMonitoredItems, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Create-Subscription Service.
	 *
	 * @param subscriptions
	 *
	 * @param subscriptionManager2
	 * @param session
	 *
	 * @param Request              {@link CreateSubscriptionRequest} to create a
	 *                             {@link Subscription} to hold
	 *                             {@link MonitoredItem}.
	 * @param RequestId            Defined Id for an
	 *                             {@link CreateSubscriptionRequest}.
	 * @return {@link AsyncResult} of a {@link CreateSubscriptionResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected CreateSubscriptionResponse beginCreateSubscription(CreateSubscriptionRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginCreateSubscription(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		CreateSubscriptionResponse response = (CreateSubscriptionResponse) waitForResponse(
				RequestType.CreateSubscription, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Monitored-Item Service.
	 *
	 * @param Request   {@link DeleteMonitoredItemsRequest} to stop monitoring an
	 *                  Event or DataChange.
	 * @param RequestId Defined Id for an {@link DeleteMonitoredItemsRequest}.
	 * @return {@link AsyncResult} of a {@link DeleteMonitoredItemsResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteMonitoredItemsResponse beginDeleteMonitoredItems(DeleteMonitoredItemsRequest request,
			NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginDeleteMonitoredItems(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		DeleteMonitoredItemsResponse response = (DeleteMonitoredItemsResponse) waitForResponse(
				RequestType.DeleteMonitoredItems, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Node Service.
	 *
	 * @param Request   {@link DeleteNodesRequest} to remove a Node from the
	 *                  server�s AddressSpace.
	 * @param RequestId Defined Id for an {@link DeleteNodesResponse}.
	 * @return {@link AsyncResult} of a {@link DeleteNodesResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteNodesResponse beginDeleteNodes(DeleteNodesRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginDeleteNodes(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		DeleteNodesResponse response = (DeleteNodesResponse) waitForResponse(RequestType.DeleteNodes, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Reference Service.
	 *
	 * @param ClientSessionKeepAlive Session used to send the Request.
	 * @param Request                {@link DeleteReferencesRequest} to remove a
	 *                               Reference from a Node of the server�s
	 *                               AddressSpace.
	 * @param RequestId              Defined Id for an
	 *                               {@link DeleteReferencesResponse}.
	 * @return {@link AsyncResult} of a {@link DeleteReferencesResponse}.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteReferencesResponse beginDeleteReferences(DeleteReferencesRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginDeleteReferences(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		DeleteReferencesResponse response = (DeleteReferencesResponse) waitForResponse(RequestType.DeleteReferences,
				result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Delete-Subscription Service.
	 *
	 * @param ClientSessionKeepAlive Session used to send the Request.
	 * @param Request                {@link DeleteSubscriptionsRequest} to remove a
	 *                               Reference from a Node of the server�s
	 *                               AddressSpace.
	 * @param RequestId              Defined Id for an
	 *                               {@link DeleteSubscriptionsRequest}.
	 * @return {@link AsyncResult} of a {@link DeleteSubscriptionsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteSubscriptionsResponse beginDeleteSubscription(DeleteSubscriptionsRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginDeleteSubscription(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		DeleteSubscriptionsResponse response = (DeleteSubscriptionsResponse) waitForResponse(
				RequestType.DeleteSubscriptions, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Read Service.
	 *
	 * @param Request   {@link ReadRequest} to read an Attribute of a Node.
	 * @param RequestId Defined Id for an {@link ReadRequest}.
	 * @return {@link AsyncResult} of a {@link ReadResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected HistoryReadResponse beginHistoryRead(HistoryReadRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginHistoryRead(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		HistoryReadResponse response = (HistoryReadResponse) result.waitForResult();
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Read Service.
	 *
	 * @param Request   {@link ReadRequest} to read an Attribute of a Node.
	 * @param RequestId Defined Id for an {@link ReadRequest}.
	 * @return {@link AsyncResult} of a {@link ReadResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected HistoryUpdateResponse beginHistoryUpdate(HistoryUpdateRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginHistoryUpdate(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		HistoryUpdateResponse response = (HistoryUpdateResponse) result.waitForResult();
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Modify-MonitoredItem Service.
	 *
	 * @param Request   {@link ModifyMonitoredItemsRequest} to modify its
	 *                  attributes.
	 * @param RequestId Defined Id for an {@link ModifyMonitoredItemsRequest}.
	 * @return {@link AsyncResult} of a {@link ModifyMonitoredItemsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected ModifyMonitoredItemsResponse beginModifyMonitoredItems(ModifyMonitoredItemsRequest request,
			NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginModifyMonitoredItems(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		ModifyMonitoredItemsResponse response = (ModifyMonitoredItemsResponse) waitForResponse(
				RequestType.ModifyMonitoredItems, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Modify-Subscription Service.
	 *
	 * @param Request   {@link ModifySubscriptionRequest} to modify its attributes.
	 * @param RequestId Defined Id for an {@link ModifySubscriptionRequest}.
	 * @return {@link AsyncResult} of a {@link ModifySubscriptionResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected ModifySubscriptionResponse beginModifySubscription(ModifySubscriptionRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginModifySubscription(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		ModifySubscriptionResponse response = (ModifySubscriptionResponse) waitForResponse(
				RequestType.ModifySubscription, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Processes a Publish Service asynchronous. It is not recommended to use this
	 * Service.
	 *
	 * First, it is used to acknowledge the receipt of NotificationMessages for one
	 * or more Subscriptions. Second, it is used to request the Server to return a
	 * NotificationMessage or a keep-alive Message. Since Publish requests are not
	 * directed to a specific Subscription, they may be used by any Subscription.
	 */
	protected void beginPublish() {
		beginPublish(false);
	}

	/**
	 * HB 14.11.2017 beginRepublish add for connection re establishment
	 */
	protected void beginRepublish() {
		this.outstandingRequests.size();
		this.outstandingPublishRequestCount();
		Subscription[] subscriptions = this.subscriptionManager.getSubscriptionsPerSession(this);
		for (Subscription subscription : subscriptions) {
			try {
				// this.last
				beginRepublish(subscription.getSubscriptionId(), new UnsignedInteger(this.retransSequenceNumber),
						Identifiers.RepublishRequest);
			} catch (ServiceResultException ex) {
				if (ex.getStatusCode().getValue().equals(StatusCodes.Bad_SubscriptionIdInvalid)) {
					try {
						if (this.reCreateSubscriptions)
							this.subscriptionManager.recreateSubscription(this, subscription);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.FINE, e.getMessage());
					}
				}
			}
			this.retransSequenceNumber++;
		}
	}

	/**
	 * Processes an asynchronous republish of a
	 *
	 * @param subscriptionId
	 * @param sequenceNumber
	 * @param requestId
	 * @return
	 * @throws ServiceResultException
	 */
	protected boolean beginRepublish(UnsignedInteger subscriptionId, UnsignedInteger sequenceNumber, NodeId requestId)
			throws ServiceResultException {
		RepublishRequest request = new RepublishRequest();
		request.setSubscriptionId(subscriptionId);
		request.setRetransmitSequenceNumber(sequenceNumber);
		AsyncResult<ServiceResponse> result = beginRepublish(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		RepublishResponse response = null;
		try {
			response = (RepublishResponse) waitForResponse(RequestType.Republish, result, request);
			asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
			processPublishResponse(RequestType.Republish, subscriptionId, null, false,
					response.getNotificationMessage(), true, false);
			return true;
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.FINE,
					"BeginRepublish failed, due to following error: " + e.getMessage());
			if (e.getStatusCode().getValue().equals(StatusCodes.Bad_MessageNotAvailable)) {
				return true;
			} else if (e.getStatusCode().getValue().equals(StatusCodes.Bad_SubscriptionIdInvalid)) {
				// if id is invalid yet, throw exception to create a new subscription
				throw e;
			}
			return false;
		}
	}

	/**
	 * Begins an asynchronous invocation of the Read Service.
	 *
	 * @param skipLogging
	 *
	 * @param Request     {@link ReadRequest} to read an Attribute of a Node.
	 * @param RequestId   Defined Id for an {@link ReadRequest}.
	 * @return {@link AsyncResult} of a {@link ReadResponse}
	 * @throws ServiceResultException
	 */
	protected ReadResponse beginRead(ReadRequest request, NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginRead(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		ReadResponse response = (ReadResponse) waitForResponse(RequestType.Read, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Register-Node Service.
	 *
	 * @param Request   {@link RegisterNodesRequest} to register Nodes on a Server,
	 *                  to increase performance accessing the Node.
	 * @param RequestId Defined Id for an {@link RegisterNodesRequest}.
	 * @return {@link AsyncResult} of a {@link RegisterNodesResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected RegisterNodesResponse beginRegisterNodes(RegisterNodesRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginRegisterNodes(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		RegisterNodesResponse response = (RegisterNodesResponse) waitForResponse(RequestType.RegisterNodes, result,
				request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Set-Monitoring-Mode Service.
	 *
	 * @param ClientSessionKeepAlive Session used to send the Request.
	 * @param Request                {@link SetMonitoringModeRequest} to set the
	 *                               monitoring mode for one or more MonitoredItems
	 *                               of a Subscription.
	 * @param RequestId              Defined Id for an
	 *                               {@link SetMonitoringModeRequest}.
	 * @return {@link AsyncResult} of a {@link RegisterNodesResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected SetMonitoringModeResponse beginSetMonitoringMode(SetMonitoringModeRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginSetMonitoringMode(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		SetMonitoringModeResponse response = (SetMonitoringModeResponse) waitForResponse(RequestType.SetMonitoringMode,
				result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Set-Publishing-Mode Service.
	 *
	 * @param Request   {@link SetPublishingModeRequest} to enable sending of
	 *                  Notifications on one or more Subscriptions.
	 * @param RequestId Defined Id for an {@link SetPublishingModeRequest}.
	 * @return {@link AsyncResult} of a {@link SetPublishingModeResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected SetPublishingModeResponse beginSetPublishingMode(SetPublishingModeRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginSetPublishingMode(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		SetPublishingModeResponse response = (SetPublishingModeResponse) waitForResponse(RequestType.SetPublishingMode,
				result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Set-Triggering Service.
	 *
	 * @param Request   {@link SetTriggeringRequest} to create triggering links
	 *                  between {@link MonitoredItem}
	 * @param RequestId Defined Id for an {@link SetTriggeringRequest}.
	 * @return {@link AsyncResult} of a {@link SetTriggeringResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected SetTriggeringResponse beginSetTriggering(SetTriggeringRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginSetTriggering(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		SetTriggeringResponse response = (SetTriggeringResponse) waitForResponse(RequestType.SetTriggering, result,
				request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Transfer-Subscription Service.
	 *
	 * @param Request   {@link TransferSubscriptionsRequest} to transfer a
	 *                  Subscription and its MonitoredItems from one Session to
	 *                  another.
	 * @param RequestId Defined Id for an {@link TransferSubscriptionsRequest}.
	 * @return {@link AsyncResult} of a {@link TransferSubscriptionsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected TransferSubscriptionsResponse beginTransferSubscriptions(TransferSubscriptionsRequest request,
			NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginTransferSubscriptions(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		TransferSubscriptionsResponse response = (TransferSubscriptionsResponse) waitForResponse(
				RequestType.TransferSubscriptions, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Translate-Browse-Paths-To-NodeId
	 * Service.
	 *
	 * @param Request   {@link TranslateBrowsePathsToNodeIdsRequest} StartingNodeIds
	 *                  and RelativePathes to translate them to NodeIds
	 * @param RequestId Defined Id for an
	 *                  {@link TranslateBrowsePathsToNodeIdsRequest} .
	 * @return {@link AsyncResult} of a
	 *         {@link TranslateBrowsePathsToNodeIdsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected TranslateBrowsePathsToNodeIdsResponse beginTranslateBrowsePathToNodeIds(
			TranslateBrowsePathsToNodeIdsRequest request, NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginTranslateBrowsePathToNodeId(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		TranslateBrowsePathsToNodeIdsResponse response = (TranslateBrowsePathsToNodeIdsResponse) waitForResponse(
				RequestType.TranslateBrowsePathsToNodeIds, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	/**
	 * Begins an asynchronous invocation of the Unregister-Node Service.
	 *
	 * @param Request   {@link UnregisterNodesRequest} to unregister a Node node on
	 *                  the server.
	 * @param RequestId Defined Id for an {@link UnregisterNodesRequest} .
	 * @return {@link AsyncResult} of a {@link UnregisterNodesResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected UnregisterNodesResponse beginUnregisterNodes(UnregisterNodesRequest request, NodeId requestId)
			throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginUnregisterNodes(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		UnregisterNodesResponse response = (UnregisterNodesResponse) waitForResponse(RequestType.UnregisterNodes,
				result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	protected WriteResponse beginWrite(WriteRequest request, NodeId requestId) throws ServiceResultException {
		if (this.reconnecting) {
			return null;
		}
		AsyncResult<ServiceResponse> result = beginWriteAsync(request);
		asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(), requestId);
		WriteResponse response = (WriteResponse) waitForResponse(RequestType.Write, result, request);
		asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), requestId);
		return response;
	}

	@Override
	protected CancelResponse cancelRequest(CancelRequest request) throws ServiceResultException {
		CancelResponse response = cancelRequest(request);
		endCancelRequest(response);
		return response;
	}

	/**
	 * deletes physical (resoureces)
	 */
	protected void clear() {
		synchronized (this.lock) {
			if (this.keepAliveTask != null) {
				this.keepAliveTask.cancel();
				this.keepAliveTask = null;
			}
			if (this.reconnectHandler != null) {
				this.reconnectHandler.cancelReconnect();
				this.reconnectHandler = null;
			}
			this.sessionManager.removeSession(this);
		}
		onClose();
	}

	/**
	 * Closes the client session
	 *
	 * @param DeleteSubscriptions If the value is TRUE, the Server deletes all
	 *                            Subscriptions associated with the Session. If the
	 *                            value is FALSE, the Server keeps the Subscriptions
	 *                            associated with the Session until they timeout
	 *                            based on their own lifetime.
	 * @return
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	@Override
	protected CloseSessionResponse closeSession(boolean deleteSubscriptions) throws ServiceResultException {
		CloseSessionResponse response = null;
		try {
			preCloseSession(deleteSubscriptions);
			CloseSessionRequest request = new CloseSessionRequest();
			request.setDeleteSubscriptions(deleteSubscriptions);
			response = closeSession(request);
		} finally {
			endCloseSession();
		}
		return response;
	}

	protected void endActivateSession(ActivateSessionResponse response) throws ServiceResultException {
		boolean isValid = true;
		if (response.getResults() != null && response.getResults().length > 0) {
			for (StatusCode code : response.getResults()) {
				if (code.isBad()) {
					isValid = false;
					break;
				}
			}
		}
		if (isValid) {
			try {
				synchronized (this.lock) {
					setSenderNonce(ByteString.asByteArray(response.getServerNonce()));
					this.reconnecting = false;
					onConnect();
					// fetch namespaces from server
					startKeepAliveTimer();
					fetchNamespaceTables();
					fetchNamespaceMetadatas();
					this.transportChannel.getMessageContext().setNamespaceTable(namespaceUris);
				}
			} catch (ServiceResultException sre) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, sre);
				closeSession(true);
			}
		} else {
			try {
				closeSession(true);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
			}
		}
	}

	protected void endCloseSession() {
		// frees any network resources
		clear();
	}

	protected void endCreateSession(CreateSessionResponse response, CreateSessionRequest request)
			throws ServiceResultException {
		this.serverNonce = ByteString.asByteArray(response.getServerNonce());
		// check server nonce
		validateCreateSessionResponse(response, request);
		this.sessionId = response.getSessionId();
		this.sessionManager.saveSession(response, this);
		setAuthentificationToken(response.getAuthenticationToken());
		// sessioncookie?
		this.sessionTimeout = response.getRevisedSessionTimeout();
		if (response.getServerNonce() == null) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Servernonce is null/empty!",
					RequestType.CreateSession.name());
		}
		byte[] serverCertificateData = ByteString.asByteArray(response.getServerCertificate());
		/**
		 * Exceptions are not able to be thrown, because of the predefined validation
		 */
		try {
			this.serverCertificate = createCertificateFromServerCertificateData(serverCertificateData);
		} catch (CertificateException | ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
		}
		this.serverEndpoints = response.getServerEndpoints();
		this.serverSoftwareCertificates = response.getServerSoftwareCertificates();
		this.serverSignature = response.getServerSignature();
		this.maxRequestMessageSize = response.getMaxRequestMessageSize();
	}

	protected void onConnect() {
		for (ConnectionListener listener : getConnectionListeners()) {
			listener.onServerConnected(this);
		}
	}

	protected void onClose() {
		synchronized (this.lock) {
			AsyncResult<SecureChannel> result = getTransportChannel().closeAsync();
			try {
				result.waitForResult();
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
			}
			for (ConnectionListener listener : getConnectionListeners()) {
				listener.onServerClose(this);
			}
			this.publishExecutor.shutdown();
		}
	}

	@Override
	protected void onError(RequestType requestType, ServiceRequest request, ServiceResponse response,
			ServiceResultException e) throws ServiceResultException {
		// client lost connection to a server, starting to reconnect
		if (e.getStatusCode() != null && (StatusCodes.Bad_ServerNotConnected.equals(e.getStatusCode().getValue())
				|| StatusCodes.Bad_Timeout.equals(e.getStatusCode().getValue())
				|| StatusCodes.Bad_NoCommunication.equals(e.getStatusCode().getValue()))) {
			ServiceResult result = new ServiceResult(e.getStatusCode());
			result.setAdditionalInfo(e.getAdditionalTextField());
			onConnectionLost(result);
		}
		// server is going to shut down -> close session
		else if (e.getStatusCode() != null && StatusCodes.Bad_Shutdown.equals(e.getStatusCode().getValue())) {
			try {
				closeSession(true);
			} catch (ServiceResultException sre) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, sre);
			}
		} else if (request instanceof PublishRequest) {
			// skip bad publish messages CTT 11.7- 006 && CTT 11.4- 001
			if (response != null && response.getResponseHeader() != null
					&& response.getResponseHeader().getServiceResult() != null
					&& response.getResponseHeader().getServiceResult().isBad()) {
				if (!this.isCloseCalled()) {
					Logger.getLogger(getClass().getName()).log(Level.FINE,
							"{0} Recieving a response with the error {1}! Message is discarded!",
							new String[] { RequestType.Publish.name(), ((PublishResponse) response).getResponseHeader()
									.getServiceResult().getDescription() });
				}
				// CTT 11.4- 002
				if (StatusCodes.Bad_NoSubscription
						.compareTo(response.getResponseHeader().getServiceResult().getValue()) == 0) {
					Subscription subscription = subscriptionManager.getSubscription(this,
							((PublishResponse) response).getSubscriptionId());
					if (subscription == null) {
						throw e;
					} else {
						if (this.reCreateSubscriptions)
							this.subscriptionManager.recreateSubscription(this, subscription);
					}
				} else {
					throw new ServiceResultException(response.getResponseHeader().getServiceResult());
				}
			}
			boolean isError = false;
			// CTT 11.4-002 establish a new subscription
			if (e.getStatusCode() != null && StatusCodes.Bad_NoSubscription.equals(e.getStatusCode().getValue())) {
				try {
					Logger.getLogger(getClass().getName()).log(Level.FINE,
							"{0} Trying to re-establish a connection to the subscription {1}, because {2}!",
							new Object[] { RequestType.Publish.name(), ((PublishResponse) response).getSubscriptionId(),
									e.getStatusCode().getDescription() });
					Subscription subscription = subscriptionManager.getSubscription(this,
							((PublishResponse) response).getSubscriptionId());
					if (subscription == null) {
						throw e;
					} else {
						if (this.reCreateSubscriptions)
							this.subscriptionManager.recreateSubscription(this, subscription);
					}
					return;
				} catch (ServiceResultException e2) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING,
							RequestType.Publish.name() + " Could not re-establish connection to the subscription "
									+ ((PublishResponse) response).getSubscriptionId() + ", because "
									+ e2.getStatusCode().getDescription() + "!",
							e2);
				}
			} else {
				if (response != null && ((PublishResponse) response).getResults() != null) {
					for (int i = 0; i < ((PublishResponse) response).getResults().length; i++) {
						StatusCode error = ((PublishResponse) response).getResults()[i];
						if (error.isGood()) {
							continue;
						}
						// reinitialize subscriptions
						if (StatusCodes.Bad_SubscriptionIdInvalid.equals(error.getValue())) {
							try {
								Subscription subscription = subscriptionManager.getSubscription(this,
										((PublishRequest) request).getSubscriptionAcknowledgements()[i]
												.getSubscriptionId());
								if (subscription == null) {
									throw new ServiceResultException(StatusCodes.Bad_NoSubscription);
								}
								if (this.reCreateSubscriptions)
									this.subscriptionManager.recreateSubscription(this, subscription);
							} catch (ServiceResultException e2) {
								Logger.getLogger(getClass().getName()).log(Level.WARNING,
										RequestType.Publish.name()
												+ " Could not re-establish connection to the subscription "
												+ ((PublishRequest) request).getSubscriptionAcknowledgements()[i]
														.getSubscriptionId()
												+ ", because " + e2.getStatusCode().getDescription() + "!",
										e2);
							}
							isError = true;
						}
						// display error to the enduser
						else if (StatusCodes.Bad_SequenceNumberUnknown.equals(error.getValue())) {
							Logger.getLogger(getClass().getName()).log(Level.FINE,
									"{0} Receiving datas with an invalid sequence number {1} in subscription {2}!",
									new Object[] { RequestType.Publish.name(),
											((PublishRequest) request).getSubscriptionAcknowledgements()[i]
													.getSequenceNumber(),
											((PublishRequest) request).getSubscriptionAcknowledgements()[i]
													.getSubscriptionId() });
						}
					}
				}
				if (isError) {
					throw new ServiceResultException(StatusCode.GOOD);
				}
				return;
			}
		}
		// log error
		Logger.getLogger(getClass().getName()).log(Level.FINE, "{0} Service error!", requestType.name());
		throw e;
	}

	/**
	 * Sends a keep alive by reading from the server
	 *
	 * @param state State of the Server, which is used to analyze the keep alive
	 *              (ServerState: Running (0) means, keep alive is successful)
	 * @throws ServiceResultException
	 */
	protected void onKeepAlive(Object state) {
		ReadRequest request = (ReadRequest) state;
		// check if session has been closed
		if (!isConnected()) {
			return;
		}
		// raise error if keep alives are not coming back.
		if (keepAliveStopped() && onKeepAliveError(new ServiceResult(StatusCodes.Bad_NoCommunication))) {
			return;
		}
		// limit the number of keep alives sent
		RequestHeader requestheader = new RequestHeader();
		requestheader.setTimeoutHint(UnsignedInteger.valueOf(this.keepAliveInterval * 2));
		requestheader.setReturnDiagnostics(UnsignedInteger.ZERO);
		request.setRequestHeader(requestheader);
		request.setTimestampsToReturn(TimestampsToReturn.Neither);
		try {
			ReadResponse response = this.nodeManager.read(this, request, null);
			onKeepAliveComplete(response);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.FINE,
					"KeepAlive failed, due to following error: " + e.getMessage());
			/**
			 * Keep Alive Failed thrown in reading server state
			 */
		}
	}

	/**
	 *
	 * @param state
	 */
	protected boolean onKeepAliveError(ServiceResult result) {
		return onSessionKeepAlive(this, result);
	}

	/**
	 * Checks if a notification has arrived. Sends a publish if it has not.
	 *
	 * @throws ServiceResultException
	 */
	protected void onKeepAliveComplete(ReadResponse response) throws ServiceResultException {
		if (response == null) {
			throw new ServiceResultException(StatusCodes.Bad_UnexpectedError);
		}
		DataValue[] values = response.getResults();
		/** validates the serverstate value */
		ServiceResult error;
		if (values != null && values.length > 0) {
			error = validateDataValue(values[0], Integer.class);
		} else {
			error = new ServiceResult(StatusCodes.Bad_UnexpectedError);
		}
		if (error.isGood()) {
			if (values == null)
				throw new NullPointerException("Datavalue is null!");
			onKeepAlive(ServerState.valueOf((Integer) values[0].getValue().getValue()));
		} else {
			onKeepAlive(ServerState.Unknown);
		}
	}

	protected boolean onSessionKeepAlive(ClientSession session, ServiceResult result) {
		// check for discarded session
		if (session != this) {
			return true;
		}
		// start reconnect sequence on communication error
		if (result != null && result.getCode().isBad()) {
			// no delay for reconnect start
			reconnect(0, this.reconnectPeriod);
			return true;
		}
		return false;
	}

	/**
	 * Callback when this session is going to reconnect.
	 *
	 */
	protected void onReconnectStart() {
		synchronized (this.lock) {
			this.reconnectCounter = this.reconnectCount;
			final ClientSession session = this;
			for (ReconnectListener listener : getReconnectListeners()) {
				listener.onReconnectStarted(session);
			}
			// should we remove subscription
			if (!reCreateSubscriptions)
				subscriptionManager.deleteSubscriptionsInternal(this);
		}
	}

	/**
	 * Callback when this session is reconnected!
	 *
	 * @param successfull
	 *
	 * @param OldSession  Maybe a different Session, when the server has crashed.
	 */
	protected void onReconnectFinish(final boolean successfull) {
		synchronized (this.lock) {
			this.reconnectHandler = null;
			final ClientSession session = this;
			for (ReconnectListener listener : getReconnectListeners()) {
				listener.onReconnectFinished(session, successfull);
			}
		}
		if (successfull) {
			// reset retransmit sequence number
			this.retransSequenceNumber = 1;
			startKeepAliveTimer();
		}
	}

	protected void onReconnectStopped() {
		synchronized (this.lock) {
			this.reconnectHandler = null;
			final ClientSession session = this;
			// we have to free all subscriptions
			deleteSubscriptionsInternal();
			for (ReconnectListener listener : getReconnectListeners()) {
				listener.onReconnectStopped(session);
			}
		}
	}

	protected void deleteSubscriptionsInternal() {
		subscriptionManager.deleteSubscriptionsInternal(this);
	}

	/**
	 * Opens a session to get all Server Endpoints without using the discovery
	 * Service.
	 *
	 * @param serverUri
	 * @param sessionName
	 * @throws ServiceResultException
	 */
	protected void open(String serverUri, ApplicationDescription clientDescription, boolean validateEndpoints)
			throws ServiceResultException {
		/** check the connection state, that the session has not been created */
		synchronized (this.lock) {
			if (isConnected()) {
				throw new ServiceResultException(StatusCodes.Bad_InvalidState, "Already connected to Server");
			}
		}
		this.validateEndpoints = validateEndpoints;
		createSession(null, clientDescription, serverUri, null, null, null, null, 0);
	}

	/**
	 * Establishes a session with the server
	 *
	 * @param sessionName
	 * @param tmpSessionTimeout
	 * @param identity
	 * @param preferredLocales
	 * @throws ServiceResultException
	 */
	protected void open(String sessionName, double sessionTimeout, boolean validateEndoints)
			throws ServiceResultException {
		double tmpSessionTimeout = sessionTimeout;
		this.validateEndpoints = validateEndoints;
		this.sessionName = sessionName;
		/** check the connection state, that the session has not been created */
		synchronized (this.lock) {
			if (isConnected()) {
				throw new ServiceResultException(StatusCodes.Bad_InvalidState, "Already connected to Server");
			}
		}
		/** Get the Certificate encoded from the Endpoint */
		byte[] certificateData = ByteString.asByteArray(this.endpoint.getServerCertificate());
		/** Check the CertificateData that it is valid */
		if (certificateData != null && certificateData.length > 0) {
			/** Not used, because it is just for validation */
			try {
				createCertificateFromServerCertificateData(certificateData);
			} catch (CertificateException e) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
				throw new ServiceResultException(StatusCodes.Bad_CertificateInvalid);
			}
		}
		// create a nonce
		this.clientNonce = ByteString
				.asByteArray(CryptoUtil.createNonce(this.configuration.getNonceLength(this.endpoint)));
		/**
		 * if (this.identity == null) { // create an anonnymous token UserIdentityToken
		 * annonymousToken = null;
		 *
		 * annonymousToken = EndpointUtil .createAnonymousIdentityToken(this.endpoint);
		 *
		 * this.identity = new UserIdentityRole(annonymousToken); }
		 */
		byte[] clientCertificateData = this.clientCertificate.getEncoded();
		ApplicationDescription clientDescription = new ApplicationDescription();
		clientDescription.setApplicationName(this.configuration.getApplicationName());
		clientDescription.setApplicationUri(this.configuration.getApplicationUri());
		clientDescription.setApplicationType(ApplicationType.Client);
		clientDescription.setProductUri(this.configuration.getProductUri());
		if (Double.compare(tmpSessionTimeout, 0.0) == 0) {
			tmpSessionTimeout = this.configuration.getDefaultSessionTimeout();
		}
		CreateSessionResponse response = createSession(null, clientDescription,
				this.endpoint.getServer().getApplicationUri(), this.endpoint.getEndpointUrl(), sessionName,
				this.clientNonce, clientCertificateData, tmpSessionTimeout);
		this.serverEndpoints = response.getServerEndpoints();
		// verify that the server returned the same instance certificate
		if (!Arrays.equals(ByteString.asByteArray(response.getServerCertificate()),
				ByteString.asByteArray(this.endpoint.getServerCertificate()))) {
			throw new ServiceResultException(StatusCodes.Bad_CertificateInvalid);
		}
	}

	/**
	 * Processes a Republish Service.
	 *
	 * This Service requests the Subscription to republish a NotificationMessage
	 * from its retransmission queue.
	 *
	 * @param subscriptionId Server assigned identifier for the Subscription to be
	 *                       republished
	 * @param sequenceNumber The sequence number of a specific NotificationMessage
	 *                       to be republished
	 * @return
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected boolean republish(UnsignedInteger subscriptionId, UnsignedInteger sequenceNumber)
			throws ServiceResultException {
		RepublishRequest request = new RepublishRequest();
		request.setSubscriptionId(subscriptionId);
		request.setRetransmitSequenceNumber(sequenceNumber);
		RepublishResponse response = republish(request);
		if (response == null) {
			return false;
		}
		processPublishResponse(RequestType.Republish, subscriptionId, null, false, response.getNotificationMessage(),
				true, false);
		return true;
	}

	protected Node[] readNodes(NodeId[] nodeIds, NodeClass[] nodeClasses, boolean asyncOperation)
			throws ServiceResultException {
		Node[] nodes = new Node[nodeIds.length];
		ReadRequest request = new ReadRequest();
		request.setMaxAge(0.0);
		request.setTimestampsToReturn(TimestampsToReturn.Neither);
		for (int i = 0; i < nodeIds.length; i++) {
			ReadValueId[] readValues = this.nodeFactory.buildReadValueIdForNodes(nodeIds[i], nodeClasses[i]);
			request.setNodesToRead(readValues);
			/** process read service */
			DataValue[] results;
			if (!asyncOperation) {
				results = read(request).getResults();
			} else {
				results = beginRead(request, Identifiers.ReadRequest).getResults();
			}
			/** create node */
			Node node = this.nodeFactory.createNode(getNamespaceUris(), nodeIds[i], nodeClasses[i], results);
			nodes[i] = node;
		}
		return nodes;
	}

	protected SignatureData sign(byte[] dataToSign) throws ServiceResultException {
		SignatureData signatureData = null;
		/** check if nothing to do */
		if (this.serverNonce == null) {
			return new SignatureData();
		}
		// nothing more to do if no encryption
		if (this.securityPolicyUri == null || this.securityPolicyUri.isEmpty()) {
			return new SignatureData();
		}
		// sign data
		if (SecurityPolicy.NONE.getPolicyUri().equals(this.securityPolicyUri)) {
			return new SignatureData();
		} else if (SecurityPolicy.BASIC128RSA15.getPolicyUri().equals(this.securityPolicyUri)
				|| SecurityPolicy.BASIC256.getPolicyUri().equals(this.securityPolicyUri)
				|| SecurityPolicy.BASIC256SHA256.getPolicyUri().equals(this.securityPolicyUri)
				|| SecurityPolicy.AES128_SHA256_RSAOAEP.getPolicyUri().equals(this.securityPolicyUri)
				|| SecurityPolicy.AES256_SHA256_RSAPSS.getPolicyUri().equals(this.securityPolicyUri)) {
			SecurityAlgorithm algorithm = SecurityPolicy.getSecurityPolicy(this.securityPolicyUri)
					.getAsymmetricSignatureAlgorithm();
			try {
				signatureData = CryptoUtil.signAsymm(this.privateKey.getPrivateKey(),
						algorithm/* SecurityAlgorithm.RsaSha1 */, dataToSign);
			} catch (ServiceResultException e1) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e1);
			}
			return signatureData;
		} else {
			throw new ServiceResultException(StatusCodes.Bad_SecurityPolicyRejected,
					"Unsupported Security Policy: " + securityPolicyUri);
		}
	}

	/**
	 * Processes the response from a publish request. Stores the Acknowledge for the
	 * next Request.
	 *
	 * @param Responseheader
	 * @param SubscriptionId
	 * @param AvailablesequenceNumbers
	 * @param MoreNotifications
	 * @param NotificationMessage
	 * @param StatusCodes
	 * @param IsRepublish
	 * @param IsMoreNotifications
	 */
	protected boolean processPublishResponse(RequestType requestType, UnsignedInteger subscriptionId,
			UnsignedInteger[] availableSequenceNumbers, Boolean moreNotifications,
			NotificationMessage notificationMessage, boolean isRepublish, boolean isMoreNotifications) {
		// send notification that the server is alive
		Subscription subscription = null;
		onKeepAlive(this.serverState);
		// collect the sequence numbers for messages to acknowledge
		synchronized (this.lock) {
			// clear out acknowledgements for unreachable subscriptions
			List<SubscriptionAcknowledgement> tmpAcknowledgementsToSend = new ArrayList<>();
			for (int i = 0; i < this.acknowledgementsToSend.size(); i++) {
				SubscriptionAcknowledgement acknowledgement = this.acknowledgementsToSend.get(i);
				// subscription not available
				if (!(acknowledgement.getSubscriptionId().equals(subscriptionId))) {
					tmpAcknowledgementsToSend.add(acknowledgement);
				} else {
					// sequencenumber
					if (availableSequenceNumbers == null
							|| Arrays.asList(availableSequenceNumbers).contains(acknowledgement.getSequenceNumber())) {
						tmpAcknowledgementsToSend.add(acknowledgement);
					}
				}
			}
			// create an acknowledgement to be sent back to the server
			if (notificationMessage.getNotificationData() != null
					&& notificationMessage.getNotificationData().length > 0) {
				Subscription s = getSubscriptionManager().getSubscription(this, subscriptionId);
				if (s != null) {
					SubscriptionAcknowledgement acknowledgement = new SubscriptionAcknowledgement();
					acknowledgement.setSequenceNumber(notificationMessage.getSequenceNumber());
					acknowledgement.setSubscriptionId(subscriptionId);
					tmpAcknowledgementsToSend.add(acknowledgement);
				}
			}
			this.acknowledgementsToSend = tmpAcknowledgementsToSend;
		}
		// ignore messages with a subscription that has been deleted
		for (Subscription s : getSubscriptions()) {
			if (subscriptionId.equals(s.getSubscriptionId())) {
				subscription = s;
				break;
			}
		}
		// store publish values
		if (subscription != null) {
			boolean continue2work = subscription.saveMessageInCache(requestType, availableSequenceNumbers,
					notificationMessage, isRepublish, isMoreNotifications, this.publishExecutor);
			// CTT 11.4 - 014
			if (moreNotifications && continue2work) {
				if (notificationMessage.getNotificationData() == null
						|| notificationMessage.getNotificationData().length == 0) {
					Logger.getLogger(getClass().getName()).log(Level.FINE,
							"{0} More notifications available for subscription {1} in message with sequence number {2}, but there were no notifications available!",
							new Object[] { requestType.name(), subscriptionId,
									notificationMessage.getSequenceNumber() });
				}
				return moreNotifications;
			}
		} else {
			Logger.getLogger(getClass().getName()).log(Level.FINE,
					"{0} Could not find a subscription with the id {1} to store the notifications!",
					new Object[] { RequestType.Publish.name(), subscriptionId });
			DeleteSubscriptionsRequest request = new DeleteSubscriptionsRequest();
			request.setSubscriptionIds(new UnsignedInteger[] { subscriptionId });
			try {
				this.subscriptionManager.beginDeleteSubscription(this, request, true);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.FINE,
						RequestType.DeleteSubscriptions.name() + " Could not delete the invalid subscription "
								+ subscriptionId + " because " + e.getStatusCode().getDescription() + "!",
						e);
			}
		}
		return moreNotifications;
	}

	/**
	 * Removes all Subscriptions from the session
	 *
	 * @throws ServiceResultException
	 */
	protected void removeAllSubscriptions() throws ServiceResultException {
		Subscription[] subscriptions = this.subscriptionManager.getSubscriptionsPerSession(this);
		removeSubscription(subscriptions);
	}

	/**
	 * Removes a subscription from the client session
	 *
	 * @param subscription
	 * @throws ServiceResultException
	 */
	protected void removeSubscription(Subscription... subscriptions) throws ServiceResultException {
		if (subscriptions == null || subscriptions.length == 0) {
			return;
		}
		UnsignedInteger[] subscriptionIds = new UnsignedInteger[subscriptions.length];
		for (int ii = 0; ii < subscriptions.length; ii++) {
			subscriptionIds[ii] = subscriptions[ii].getSubscriptionId();
		}
		DeleteSubscriptionsRequest request = new DeleteSubscriptionsRequest();
		request.setSubscriptionIds(subscriptionIds);
		this.subscriptionManager.beginDeleteSubscription(this, request, true);
	}

	/**
	 * Reconnects to the server after a network failure
	 *
	 * @param forceReconnect
	 *
	 * @throws ServiceResultException
	 */
	protected void reconnect(boolean forceReconnect) throws ServiceResultException {
		if (this.reconnecting && forceReconnect) {
			throw new ServiceResultException(StatusCodes.Bad_InvalidState);
		}
		try {
			this.reconnecting = true;
			ActivateSessionResponse response = null;
			try {
				response = reactivateSession();
				if (response != null && response.getResults() != null && response.getResults().length > 0) {
					for (StatusCode result : response.getResults()) {
						if (result.isBad()) {
							throw new ServiceResultException(result);
						}
					}
				}
			} catch (ServiceResultException e) {
				UnsignedInteger statusCodeValue = e.getStatusCode().getValue();
				if (StatusCodes.Bad_SecureChannelClosed.equals(statusCodeValue)
						|| StatusCodes.Bad_ServerNotConnected.equals(e.getStatusCode().getValue())) {
					createNewServiceChannel(this.endpoint, this.endpointConfiguration);
					throw new ServiceResultException(StatusCodes.Bad_SessionClosed);
				} else {
					throw e;
				}
			}
			// only if reactivate is allowed
			if (reCreateSubscriptions)
				reactivateSubscriptions(response);
		} finally {
			this.reconnecting = false;
		}
	}

	protected void recreate() throws ServiceResultException {
		synchronized (this.lock) {
			ActivateSessionResponse activeResponse;
			try {
				this.outstandingRequests.clear();
				open();
			} catch (Exception e) {
				throw new ServiceResultException(StatusCodes.Bad_CommunicationError);
			}
			try {
				// change the active session to the new one
				activeResponse = reactivateSession();
				if (activeResponse != null && activeResponse.getResults() != null
						&& activeResponse.getResults().length > 0) {
					for (StatusCode result : activeResponse.getResults()) {
						if (result.isBad()) {
							throw new ServiceResultException(result);
						}
					}
				}
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
				throw new ServiceResultException(StatusCodes.Bad_CommunicationError);
			}
			if (this.reCreateSubscriptions) {
				// recreate the subscriptions
				Subscription[] subscriptionsToChange = this.subscriptionManager.getSubscriptionsPerSession(this);
				if (subscriptionsToChange != null) {
					for (Subscription subscription : subscriptionsToChange) {
						this.subscriptionManager.recreateSubscription(this, subscription);
					}
					if (activeResponse != null)
						reactivateSubscriptions(activeResponse);
				}
			}
		}
	}

	/**
	 * Returns the Application Instance Certificate.
	 *
	 * @return ClientApplicationInstanceCertificate
	 */
	protected KeyPair getApplicationInstanceCertificate() {
		return this.clientApplicationInstanceCertificate;
	}

	/**
	 * Returns the Type of the service, which has been created the session.
	 *
	 * @return TRUE if the session has been created async, FALSE th session has been
	 *         created sync
	 */
	protected boolean getServiceOperation() {
		return this.asyncCreation;
	}

	protected void setApplicationDescription(ApplicationDescription applicationDescription) {
		this.applicationDescription = applicationDescription;
	}

	protected void setEndpoint(EndpointDescription endpoint) {
		this.endpoint = endpoint;
	}

	protected void setEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
		this.endpointConfiguration = endpointConfiguration;
	}

	protected void setLock(Lock lock) {
		this.lock = lock;
	}

	protected void setSessionTimeout(double sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	protected void setMaxRequestMessageSize(UnsignedInteger maxRequestMessageSize) {
		this.maxRequestMessageSize = maxRequestMessageSize;
	}

	protected void setIdentity(UserIdentityRole identity) {
		this.identity = identity;
	}

	protected void setReconnecting(boolean reconnecting) {
		this.reconnecting = reconnecting;
	}

	protected void setSystemContext(SystemContext systemContext) {
		this.systemContext = systemContext;
	}

	protected void setServerState(ServerState serverState) {
		this.serverState = serverState;
	}

	protected void setLastKeepAliveTime(DateTime lastKeepAliveTime) {
		this.lastKeepAliveTime = lastKeepAliveTime;
	}

	protected void setNodeCache(NodeCache nodeCache) {
		this.nodeCache = nodeCache;
	}

	protected void setAsyncCreation(boolean asyncCreation) {
		this.asyncCreation = asyncCreation;
	}

	protected void setNamespaceUris(NamespaceTable namespaceUris) {
		this.namespaceUris = namespaceUris;
	}

	protected void setClientCertificate(Cert clientCertificate) {
		this.clientCertificate = clientCertificate;
	}

	protected void setConfiguration(ApplicationConfiguration configuration) {
		this.configuration = configuration;
	}

	protected void setPreferredLocales(String[] preferredLocales) {
		this.preferredLocales = preferredLocales;
	}

	protected void setServerUris(StringTable serverUris) {
		this.serverUris = serverUris;
	}

	protected void setSessionId(NodeId sessionId) {
		this.sessionId = sessionId;
	}

	protected void setServerEndpoints(EndpointDescription[] serverEndpoints) {
		this.serverEndpoints = serverEndpoints;
	}

	protected void setServerSoftwareCertificates(SignedSoftwareCertificate[] serverSoftwareCertificates) {
		this.serverSoftwareCertificates = serverSoftwareCertificates;
	}

	protected void setServerSignature(SignatureData serverSignature) {
		this.serverSignature = serverSignature;
	}

	protected void setPrivateKey(PrivKey privateKey) {
		this.privateKey = privateKey;
	}

	protected void setSubscriptionManager(SubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	protected SubscriptionManager getSubscriptionManager() {
		return this.subscriptionManager;
	}

	protected void setServerCertificate(Cert serverCertificate) {
		this.serverCertificate = serverCertificate;
	}

	protected CallResponse callMethod(CallRequest request) throws ServiceResultException {
		return this.nodeManager.call(this, request, null);
	}

	/**
	 * Default values
	 */
	private void initialize() {
		this.sessionTimeout = 0;
		this.namespaceUris = new NamespaceTable();
		this.serverUris = new StringTable();
		this.applicationDescription = new ApplicationDescription();
		this.clientCertificate = null;
		this.endpoint = null;
		this.acknowledgementsToSend = new ArrayList<>();
		this.keepAliveInterval = 10000;
		this.outstandingRequests = new ArrayList<AsyncRequestState>();
		this.nodeCache = new NodeCache(this);
		this.publishExecutor = Executors.newFixedThreadPool(1);
	}

	/**
	 * Checks if the session (sessionId != null) is connected
	 *
	 * @return
	 */
	private boolean isConnected() {
		return this.keepAliveTask != null;
	}

	private Cert createCertificateFromServerCertificateData(byte[] certificateData)
			throws CertificateException, ServiceResultException {
		Cert certToValidate = null;
		if (certificateData != null) {
			X509Certificate x509CertInstance = CertificateUtils.decodeX509Certificate(certificateData);
			/** Create a Certificate */
			certToValidate = new Cert(x509CertInstance);
			/**
			 * Validate the Certificate with the ClientCertificate - Validator
			 */
			SecurityPolicy securityPolicy = SecurityPolicy.getSecurityPolicy(this.endpoint.getSecurityPolicyUri());
			if (this.configuration.getCertificateValidator() instanceof AllowNoneCertificateValidator) {
				StatusCode validCertificateError = ((AllowNoneCertificateValidator) this.configuration
						.getCertificateValidator()).validateCertificate(securityPolicy, null, certToValidate);
				if (validCertificateError != null && validCertificateError.isBad()) {
					throw new ServiceResultException(validCertificateError);
				}
			}
		}
		return certToValidate;
	}

	/**
	 * Creates a Session on a Server
	 *
	 * @param requestHeader
	 * @param clientDescription
	 * @param serverUri
	 * @param endpointUrl
	 * @param sessionName
	 * @param clientNonce
	 * @param clientCertificate
	 * @param requestedSessionTimeout
	 *
	 * @throws ServiceResultException Reasons: Certificate Invalid
	 */
	private CreateSessionResponse createSession(RequestHeader requestHeader, ApplicationDescription clientDescription,
			String serverUri, String endpointUrl, String sessionName, byte[] clientNonce, byte[] clientCertificate,
			double requestedSessionTimeout) throws ServiceResultException {
		CreateSessionRequest request = new CreateSessionRequest();
		request.setRequestHeader(requestHeader);
		request.setClientDescription(clientDescription);
		request.setServerUri(serverUri);
		request.setEndpointUrl(endpointUrl);
		request.setSessionName(sessionName);
		request.setClientNonce(ByteString.valueOf(clientNonce));
		request.setClientCertificate(ByteString.valueOf(clientCertificate));
		request.setRequestedSessionTimeout(requestedSessionTimeout);
		request.setMaxResponseMessageSize(maxResponseMessageSize);
		if (this.endpoint != null) {
			this.securityPolicyUri = this.endpoint.getSecurityPolicyUri();
		}
		if (!this.asyncCreation) {
			CreateSessionResponse response = createSession(this.endpoint, this.endpointConfiguration, request);
			endCreateSession(response, request);
			return response;
		} else {
			AsyncResult<?> result = beginCreateSession(this.endpoint, this.endpointConfiguration, request);
			asyncRequestStarted(result, request.getRequestHeader().getRequestHandle(),
					Identifiers.CreateSessionRequest);
			CreateSessionResponse response = (CreateSessionResponse) waitForResponse(RequestType.CreateSession, result,
					request);
			asyncRequestCompleted(response.getResponseHeader().getRequestHandle(), Identifiers.CreateSessionRequest);
			endCreateSession(response, request);
			return response;
		}
	}

	private void endCancelRequest(CancelResponse response) {
		if (UnsignedInteger.ZERO.equals(response.getCancelCount())) {
			Logger.getLogger(getClass().getName()).log(Level.FINE, "{0} Cancelcount is zero!",
					RequestType.Cancel.name());
		}
	}

	private void fetchNamespaceMetadatas() {
		List<NamespaceMetadataType> namespaceMetadatas = new ArrayList<>();
		// Browse namespace metadatas
		try {
			BrowseDescription nodeToBrowse = new BrowseDescription();
			nodeToBrowse.setBrowseDirection(BrowseDirection.Forward);
			nodeToBrowse.setIncludeSubtypes(true);
			nodeToBrowse.setNodeClassMask(NodeClass.Object);
			nodeToBrowse.setNodeId(Identifiers.Server_Namespaces);
			nodeToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
			nodeToBrowse.setResultMask(BrowseResultMask.ALL);
			BrowseDescription[] nodesToBrowse = { nodeToBrowse };
			BrowseRequest request = new BrowseRequest();
			request.setNodesToBrowse(nodesToBrowse);
			request.setRequestedMaxReferencesPerNode(new UnsignedInteger(100));
			BrowseResponse response = beginBrowse(request, Identifiers.BrowseRequest);
			checkBrowseResponse(response);
			for (ReferenceDescription result : response.getResults()[0].getReferences()) {
				// check namespace metadata type
				NodeId typeDef = this.namespaceUris.toNodeId(result.getTypeDefinition());
				if (!Identifiers.NamespaceMetadataType.equals(typeDef)) {
					continue;
				}
				NodeId metadata = this.namespaceUris.toNodeId(result.getNodeId());
				BrowseDescription nsmetadataToBrowse = new BrowseDescription();
				nsmetadataToBrowse.setBrowseDirection(BrowseDirection.Forward);
				nsmetadataToBrowse.setIncludeSubtypes(true);
				nsmetadataToBrowse.setNodeClassMask(NodeClass.Variable);
				nsmetadataToBrowse.setNodeId(metadata);
				nsmetadataToBrowse.setReferenceTypeId(Identifiers.HasProperty);
				nsmetadataToBrowse.setResultMask(BrowseResultMask.ALL);
				BrowseDescription[] nsmetadatasToBrowse = { nsmetadataToBrowse };
				BrowseRequest nsmetadataRequest = new BrowseRequest();
				nsmetadataRequest.setNodesToBrowse(nsmetadatasToBrowse);
				nsmetadataRequest.setRequestedMaxReferencesPerNode(new UnsignedInteger(100));
				BrowseResponse nsmetadataResponse = beginBrowse(nsmetadataRequest, Identifiers.BrowseRequest);
				checkBrowseResponse(nsmetadataResponse);
				BrowseResult nsmetadataResults = nsmetadataResponse.getResults()[0];
				Map<NodeId, String> mapping = new HashMap<>();
				List<ReadValueId> read = new ArrayList<>();
				for (ReferenceDescription reference : nsmetadataResults.getReferences()) {
					QualifiedName browsename = reference.getBrowseName();
					NodeId refId = this.namespaceUris.toNodeId(reference.getNodeId());
					ReadValueId readValue = new ReadValueId();
					readValue.setAttributeId(Attributes.Value);
					readValue.setNodeId(refId);
					mapping.put(refId, browsename.getName());
					read.add(readValue);
				}
				ReadRequest readRequest = new ReadRequest();
				readRequest.setMaxAge(0.0);
				readRequest.setNodesToRead(read.toArray(new ReadValueId[0]));
				readRequest.setTimestampsToReturn(TimestampsToReturn.Both);
				try {
					ReadResponse readResponse = beginRead(readRequest, Identifiers.ReadRequest);
					DataValue[] readResults = readResponse.getResults();
					// validate read
					if (readResults == null) {
						throw new ServiceResultException("Cannot read Server_Namespace metadatas!");
					}
					if (readResults.length != read.size()) {
						throw new ServiceResultException("Cannot read Server_Namespace metadatas!");
					}
					NamespaceMetadataType nsMetadata = new NamespaceMetadataType();
					for (int i = 0; i < read.size(); i++) {
						ReadValueId readValue = read.get(i);
						String attribute = mapping.get(readValue.getNodeId());
						DataValue value = readResults[i];
						if (value.isNull()) {
							continue;
						}
						if (value.getValue().isEmpty()) {
							continue;
						}
						try {
							switch (attribute) {
							case "NamespaceUri":
								nsMetadata.setNamespaceUri((String) value.getValue().getValue());
								break;
							case "NamespaceVersion":
								nsMetadata.setNamespaceVersion((String) value.getValue().getValue());
								break;
							case "NamespacePublicationDate":
								nsMetadata.setNamespacePublicationDate((DateTime) value.getValue().getValue());
								break;
							case "IsNamespaceSubset":
								nsMetadata.setIsNamespaceSubset((Boolean) value.getValue().getValue());
								break;
							case "StaticNodeIdTypes":
								nsMetadata.setStaticNodeIdTypes((IdType[]) value.getValue().getValue());
								break;
							case "StaticNumericNodeIdRange":
								nsMetadata.setStaticNumericNodeIdRange((NumericRange[]) value.getValue().getValue());
								break;
							case "StaticStringNodeIdPattern":
								nsMetadata.setStaticStringNodeIdPattern((String) value.getValue().getValue());
								break;
							}
						} catch (ClassCastException ce) {
							Logger.getLogger(getClass().getName()).log(Level.FINE, null, ce);
						}
					}
					namespaceMetadatas.add(nsMetadata);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
				}
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Cannot read Server_Namespace metadatas!", e);
		}
		this.namespaceMetaDatas = namespaceMetadatas.toArray(new NamespaceMetadataType[0]);
	}

	private void checkBrowseResponse(BrowseResponse response) throws ServiceResultException {
		if (response == null) {
			throw new ServiceResultException("Cannot read Server_Namespace metadatas!");
		}
		BrowseResult[] results = response.getResults();
		if (results == null) {
			throw new ServiceResultException("Cannot read Server_Namespace metadatas!");
		}
		if (results.length <= 0) {
			throw new ServiceResultException("Cannot read Server_Namespace metadatas!");
		}
		if (results[0].getReferences() == null) {
			throw new ServiceResultException("Cannot read Server_Namespace metadatas!");
		}
		if (results[0].getReferences().length <= 0) {
			throw new ServiceResultException("Cannot read Server_Namespace metadatas!");
		}
	}

	/**
	 * Updates the local copy of the server's namespace uri and server uri tables
	 *
	 * @throws ServiceResultException
	 */
	private void fetchNamespaceTables() {
		List<ReadValueId> nodesToRead = new ArrayList<>();
		// request namespace array
		ReadValueId namespaceValueId = new ReadValueId();
		namespaceValueId.setNodeId(Identifiers.Server_NamespaceArray);
		namespaceValueId.setAttributeId(Attributes.Value);
		nodesToRead.add(namespaceValueId);
		// request server array
		ReadValueId serverarrayValueId = new ReadValueId();
		serverarrayValueId.setNodeId(Identifiers.Server_ServerArray);
		serverarrayValueId.setAttributeId(Attributes.Value);
		nodesToRead.add(serverarrayValueId);
		// read from server
		ReadRequest request = new ReadRequest();
		request.setMaxAge(0.0);
		request.setNodesToRead(nodesToRead.toArray(new ReadValueId[nodesToRead.size()]));
		request.setTimestampsToReturn(TimestampsToReturn.Both);
		ReadResponse response = null;
		try {
			response = beginRead(request, Identifiers.ReadRequest);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
		}
		if (response != null) {
			// encapsulate the data values from the resonse
			DataValue[] values = response.getResults();
			ServiceResult result;
			// namespace uris
			if (values != null && values.length > 0) {
				result = validateDataValue(values[0], String[].class);
				// addes the values to the namespace table
				if (result.isGood()) {
					for (String namespaceUriValue : (String[]) values[0].getValue().getValue()) {
						this.namespaceUris.add(-1, namespaceUriValue);
					}
				}
			}
			// server array
			if (values != null && values.length > 1) {
				result = validateDataValue(values[1], String[].class);
				if (result.isGood()) {
					for (String serverArray : (String[]) values[1].getValue().getValue()) {
						this.serverUris.append(serverArray);
					}
				}
			}
		}
	}

	/**
	 * Initialize with params
	 *
	 * @param endpoint
	 *
	 * @param clientConfiguration
	 * @param endpointConfiguration
	 * @param clientApplicationInstanceCertificate
	 */
	private void initialize(ApplicationConfiguration configuration, EndpointDescription endpoint,
			EndpointConfiguration endpointConfiguration, KeyPair clientApplicationInstanceCertificate) {
		initialize();
		// save the configuration
		this.configuration = configuration;
		this.endpoint = endpoint;
		this.endpointConfiguration = endpointConfiguration;
		// update default subscriptions ?
		// check for valid certificate
		if (clientApplicationInstanceCertificate == null) {
			throw new IllegalArgumentException("NO APPLICATIONINSTANCECERT");
		}
		// set client certificate
		this.clientCertificate = clientApplicationInstanceCertificate.getCertificate();
		this.privateKey = clientApplicationInstanceCertificate.getPrivateKey();
		if (this.clientCertificate == null) {
			throw new IllegalArgumentException("NO CLIENTCERT");
			// load the application instance certificate
		}
		// check for private key
		if (clientApplicationInstanceCertificate.getPrivateKey() == null) {
			throw new IllegalArgumentException("NO PRIVATEKEY");
		}
		this.clientApplicationInstanceCertificate = clientApplicationInstanceCertificate;
		// this.clientInstance = new InternalClient(
		setAsyncStop(this.asyncRequestStop, this.keepAliveInterval * 2);
		// initialize the message context
		// set default prefered locales
		this.preferredLocales = new String[] { Locale.getDefault().getCountry() };
		// create a context to use
		this.systemContext = new SystemContext();
		this.systemContext.setNamespaceUris(this.namespaceUris);
		this.systemContext.setServerUris(this.serverUris);
		this.systemContext.setTypeTable(getTypeTree());
		this.systemContext.setPreferredLocales(null);
		this.systemContext.setSessionId(null);
		this.systemContext.setUserIdentity(null);
		if (this.clientApplicationInstanceCertificate != null) {
			try {
				String certApplicationUri = CertificateUtils
						.getApplicationUriOfCertificate(this.clientApplicationInstanceCertificate.certificate);
				this.configuration.getApplication().setApplicationUri(certApplicationUri);
			} catch (CertificateParsingException e) {
				// ignore here
			}
			this.configuration.getApplication()
					.addApplicationInstanceCertificate(this.clientApplicationInstanceCertificate);
			this.configuration.getApplication().getHttpsSettings()
					.setKeyPair(this.clientApplicationInstanceCertificate);
			this.cert = this.clientApplicationInstanceCertificate.getCertificate();
			this.privateKey = this.clientApplicationInstanceCertificate.getPrivateKey();
		}
		this.clientInstance = new Client(this.configuration.getApplication());
	}

	/**
	 * Returns true if the session is not receiving keep alives
	 *
	 * Set to true if the server does not respond for 2 times the KeepAliveInterval.
	 * Set to false is communication recovers.
	 */
	private boolean keepAliveStopped() {
		synchronized (this.lock) {
			long delta = DateTime.currentTime().getTimeInMillis() - this.lastKeepAliveTime.getTimeInMillis();
			return (this.keepAliveInterval * 2) <= delta;
		}
	}

	private void notifyOnKeepAlive(final ServerState state) {
		final ClientSession session = this;
		for (ServerStateListener listener : getServerStateListeners()) {
			listener.onStatusChange(session, state);
		}
	}

	private void notifyServerStateChange(final ServerState oldState, final ServerState newState) {
		synchronized (this.lock) {
			for (ServerStateListener listener : getServerStateListeners()) {
				listener.onServerStateChange(this, oldState, newState);
			}
		}
	}

	private void notifyServerStateShutdown() {
		synchronized (this.lock) {
			final ClientSession session = this;
			try {
				final ServerStatusDataType status = readServerStatus();
				for (ServerStateListener listener : getServerStateListeners())
					listener.onServerShutdown(session, status.getSecondsTillShutdown().longValue(),
							status.getShutdownReason());
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
				for (ServerStateListener listener : getServerStateListeners())
					listener.onServerShutdown(session, 0l, null);
			}
		}
	}

	/**
	 * Called when the server returns a keep alive response
	 *
	 * @param ServerState
	 * @param Timestamp
	 */
	private void onKeepAlive(final ServerState currentServerState) {
		// restart publishing if keep alives recovered
		synchronized (this.lock) {
			if (keepAliveStopped()) {
				// ignore if already reconnnecting
				if (this.reconnecting) {
					return;
				}
				int count;
				for (AsyncRequestState outstandingRequest : this.outstandingRequests) {
					if (Identifiers.PublishRequest.equals(outstandingRequest.getRequestTypeId())) {
						outstandingRequest.setDefunct(true);
					}
				}
				count = getSubscriptions().length;
				while (count-- > 0) {
					beginPublish();
				}
			}
			this.lastKeepAliveTime = DateTime.currentTime();
		}
		// ServerState Shutdown
		if (ServerState.Shutdown.equals(currentServerState)) {
			notifyServerStateShutdown();
		}
		if (this.serverState != currentServerState) {
			notifyOnKeepAlive(currentServerState);
			notifyServerStateChange(serverState, currentServerState);
			this.serverState = currentServerState;
		}
	}

	private void open() throws ServiceResultException {
		open(this.sessionName, this.validateEndpoints);
	}

	/**
	 * Establishes a session with the server
	 *
	 * @param sessionName
	 * @param identity
	 * @throws ServiceResultException
	 */
	protected void open(String sessionName, boolean validateEndpoints) throws ServiceResultException {
		open(sessionName, 0, validateEndpoints);
	}

	/**
	 * Checks subscriptions
	 *
	 * @param DeleteSubscriptions True deletes all subscriptions.
	 * @throws ServiceResultException
	 */
	private void preCloseSession(boolean deleteSubscriptions) throws ServiceResultException {
		// first close reconnect session handler if exists
		if (reconnectHandler != null) {
			reconnectHandler.stopReconnect();
		}
		// now stop keep alive timer
		if (this.keepAliveTimer != null) {
			this.keepAliveTimer.cancel();
			this.keepAliveTimer.purge();
			this.keepAliveTimer = null;
		}
		if (deleteSubscriptions) {
			this.removeAllSubscriptions();
		}
	}

	/**
	 * Reactivates the session (plain service)
	 *
	 * @return ActivateSessionResponse
	 *
	 * @throws ServiceResultException
	 */
	private ActivateSessionResponse reactivateSession() throws ServiceResultException {
		// stop keep alive timer
		synchronized (this.lock) {
			this.reconnecting = true;
			if (this.keepAliveTask != null) {
				this.keepAliveTask.cancel();
				this.keepAliveTask = null;
			}
		}
		ActivateSessionResponse response;
		response = this.sessionManager.activateSession(this, this.identity, null, false);
		return response;
	}

	/**
	 * Reactivates all subscriptions on the session
	 *
	 * @param Response ActivateSessionResponse
	 *
	 */
	private void reactivateSubscriptions(ActivateSessionResponse response) {
		int publishCount;
		synchronized (this.lock) {
			this.serverNonce = ByteString.asByteArray(response.getServerNonce());
		}
		publishCount = this.getSubscriptions().length;
		// refill pipelines
		for (int i = 0; i < publishCount; i++) {
			// HB 10.11.2017
			// add begin republish
			beginRepublish();
			beginPublish();
		}
	}

	private ServerStatusDataType readServerStatus() throws ServiceResultException {
		ReadRequest request = new ReadRequest();
		ReadValueId[] tmpServerState = new ReadValueId[] {
				new ReadValueId(Identifiers.Server_ServerStatus, Attributes.Value, null, null) };
		request.setNodesToRead(tmpServerState);
		ReadResponse response = this.nodeManager.read(this, request, null);
		if (response.getResults() != null && response.getResults()[0] != null) {
			DataValue status = response.getResults()[0];
			return (ServerStatusDataType) status.getValue().getValue();
		}
		throw new ServiceResultException(StatusCodes.Bad_UnexpectedError,
				"ServerStatusDataType on the Server is not Available!");
	}

	/**
	 * Removes a completed async request.
	 *
	 * @param result
	 * @param requestId
	 * @param requestTypeId
	 */
	private AsyncRequestState removeRequest(UnsignedInteger requestId, NodeId requestTypeId) {
		synchronized (this.lock) {
			ListIterator<AsyncRequestState> iterator = this.outstandingRequests.listIterator();
			while (iterator.hasNext()) {
				AsyncRequestState state = iterator.next();
				// removes error results
				if (state.getResult() != null && state.getResult().getError() != null) {
					iterator.remove();
					continue;
				}
				if (state.getRequestId().equals(requestId) && state.getRequestTypeId().equals(requestTypeId)) {
					iterator.remove();
					return state;
				}
			}
		}
		return null;
	}

	/**
	 * Starts a timer to check that the connection to the server is still available
	 */
	private void startKeepAliveTimer() {
		long tmpKeepAliveInterval = this.keepAliveInterval;
		synchronized (this.lock) {
			this.serverState = ServerState.Unknown;
			this.lastKeepAliveTime = DateTime.currentTime();
		}
		// read the servers state
		ReadRequest serverStateRequest = serverStateRequest();
		// restart publishing timer
		synchronized (this.lock) {
			if (this.keepAliveTask != null) {
				this.keepAliveTask.cancel();
				this.keepAliveTask = null;
			}
			if (this.keepAliveTimer != null) {
				this.keepAliveTimer.cancel();
				this.keepAliveTimer.purge();
				this.keepAliveTimer = null;
			}
			this.keepAliveTimer = new Timer("UA Session keepAliveTimer - " + this.getSessionName());
			// start timer
			this.keepAliveTask = new SessionKeepAliveTask(serverStateRequest);
			if (tmpKeepAliveInterval == 0) {
				tmpKeepAliveInterval = 5000;
			}
			this.keepAliveTimer.scheduleAtFixedRate(this.keepAliveTask, 0, tmpKeepAliveInterval);
		}
	}

	private void validateCreateSessionResponse(CreateSessionResponse response, CreateSessionRequest request)
			throws ServiceResultException {
		try {
			validateCreatedSessionAuthentification(response);
			validateCreatedServerNonce(ByteString.asByteArray(response.getServerNonce()), false);
			if (this.validateEndpoints)
				validateCreatedServerEndpoints(request, response);
			validateCreatedServerSoftwareCertificates(response);
			validateCreatedServerSignature(response);
			validateCreatedServerMaxMessageSize(response, request);
			validateCreatedSessionTimeout(request, response);
		} catch (ServiceResultException e) {
			// log error
			Logger.getLogger(getClass().getName()).log(Level.FINE,
					RequestType.CreateSession.name() + " " + e.getAdditionalTextField());
			// drop session
			try {
				closeSession(true);
			} catch (ServiceResultException sre) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, sre);
			}
			throw e;
		}
	}

	/**
	 * Validates the authentification token from the created sessions response.
	 *
	 * @param Response
	 *
	 * @throws ServiceResultException
	 */
	private void validateCreatedSessionAuthentification(CreateSessionResponse response) throws ServiceResultException {
		// CTT 7.1-Err018-019
		if (response.getSessionId() == null || NodeId.isNull(response.getSessionId())) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid,
					"Invalid authentification information received! SessionId is invalid!!");
		}
		// CTT 7.1-Err020
		if (response.getAuthenticationToken() == null || NodeId.isNull(response.getAuthenticationToken())) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid,
					"Invalid authentification information received! Autentification token is invalid!!");
		}
		// check for duplicate authentificatino tokens
		// CTT 7.1-Err021
		for (ClientSession session : this.sessionManager.getSessions()) {
			if (response.getAuthenticationToken().equals(session.getAuthentificationToken())) {
				throw new ServiceResultException("Authentificationtoken " + response.getAuthenticationToken().toString()
						+ " is already in use! Cannot open the session!");
			}
		}
	}

	private void validateCreatedSessionTimeout(CreateSessionRequest request, CreateSessionResponse response)
			throws ServiceResultException {
		if (request.getRequestedSessionTimeout() * 10 <= response.getRevisedSessionTimeout()) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Very long session timeout ({1})!",
					new Object[] { RequestType.CreateSession.name(), response.getRevisedSessionTimeout() });
		} else if (response.getRevisedSessionTimeout() <= this.keepAliveInterval) {
			throw new ServiceResultException(
					"Very short session timeout (" + response.getRevisedSessionTimeout() + ")!");
		}
	}

	/**
	 * Validates the server endpoints from the created sessions response.
	 *
	 * @param request
	 *
	 * @param Response
	 *
	 * @throws ServiceResultException
	 */
	private void validateCreatedServerEndpoints(CreateSessionRequest request, CreateSessionResponse response)
			throws ServiceResultException {
		// CTT 7.1-Err037
		if (response.getServerEndpoints() == null || response.getServerEndpoints().length == 0) {
			throw new ServiceResultException("Endpoints from the server are empty! Session is dropped!");
		}
		// CTT 7.1-Err038
		EndpointDescription[] endpoints2match = discoverEndpoints(request.getEndpointUrl());
		if (endpoints2match != null && endpoints2match.length > 0
				&& response.getServerEndpoints().length != endpoints2match.length) {
			throw new ServiceResultException(
					"Serverendpoints received from the create session response are different than the discovered ones!");
		}
		// CTT 7.1-Err036
		boolean isValid = true;
		for (int i = 0; i < response.getServerEndpoints().length; i++) {
			SecurityPolicy policy = null;
			for (SecurityPolicy p : SecurityPolicy.getAllSecurityPolicies()) {
				if (p.getPolicyUri().equals(response.getServerEndpoints()[i].getSecurityPolicyUri())) {
					policy = p;
					break;
				}
			}
			String scheme = null;
			try {
				scheme = new URI(response.getServerEndpoints()[i].getEndpointUrl()).getScheme();
			} catch (URISyntaxException e) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
				scheme = UriUtil.SCHEME_OPCTCP;
			}
			try {
				EndpointDescription[] serverEndpoint = EndpointUtil.select(response.getServerEndpoints(),
						response.getServerEndpoints()[i].getEndpointUrl(), scheme,
						response.getServerEndpoints()[i].getSecurityMode(), policy,
						ByteString.asByteArray(response.getServerEndpoints()[i].getServerCertificate()));
				EndpointDescription[] discoveryEndpoint = EndpointUtil.select(endpoints2match,
						response.getServerEndpoints()[i].getEndpointUrl(), scheme,
						response.getServerEndpoints()[i].getSecurityMode(), policy,
						ByteString.asByteArray(response.getServerEndpoints()[i].getServerCertificate()));
				if (serverEndpoint.length != discoveryEndpoint.length) {
					isValid = false;
					break;
				}
			} catch (NullPointerException npe) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, npe);
				throw new ServiceResultException(
						"Endpoints from the discoverd server do not match with the sessions endpoint! The endpoints which should match have invalid values! Session is dropped!");
			}
		}
		/** valid returned server endpoints */
		if (!isValid) {
			throw new ServiceResultException(
					"Endpoints from the discoverd server do not match with the sessions endpoint! Session is dropped!");
		}
	}

	private EndpointDescription[] discoverEndpoints(String endpointUrl) throws ServiceResultException {
		// Must not use encryption!
		SecureChannel channel = this.clientInstance.createSecureChannel(endpointUrl, "", SecurityMode.NONE, null);
		ChannelService chan = new ChannelService(channel);
		try {
			GetEndpointsRequest req = new GetEndpointsRequest(null, endpointUrl, new String[0], new String[0]);
			req.setRequestHeader(new RequestHeader());
			req.getRequestHeader().setTimeoutHint(UnsignedInteger.valueOf(this.clientInstance.getTimeout()));
			GetEndpointsResponse res = chan.GetEndpoints(req);
			return res.getEndpoints();
		} finally {
			channel.close();
			channel.dispose();
		}
	}

	/**
	 * Validates the max message size.
	 *
	 * @param MaxRequestMessageSize
	 *
	 * @param MaxResponseMessageSiz
	 */
	private void validateCreatedServerMaxMessageSize(CreateSessionResponse response, CreateSessionRequest request) {
		// received from response
		UnsignedInteger msResponse = response.getMaxRequestMessageSize();
		// send from request
		UnsignedInteger msRequest = request.getMaxResponseMessageSize();
		if ((msResponse != null && msRequest != null) && (msRequest.longValue() * 10 <= msResponse.longValue())) {
			// validate the value
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} MaxRequestMessageSize is too large in response {1}!",
					new String[] { RequestType.CreateSession.name(), Long.toString(msResponse.longValue()) });
		}
		if (msResponse != null && msResponse.longValue() == 0 && msRequest != null && msRequest.longValue() > 0) {
			// responded is zero, set back to requested
			response.setMaxRequestMessageSize(request.getMaxResponseMessageSize());
			msResponse = request.getMaxResponseMessageSize();
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} MaxRequestMessageSize is ZERO! It has been changed to the requested value!",
					new String[] { RequestType.CreateSession.name() });
		} else if (msResponse != null && msResponse.longValue() == 0) {
			msResponse = new UnsignedInteger(500);
			response.setMaxRequestMessageSize(msResponse);
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} MaxRequestMessageSize is ZERO! It has been changed to default 500 (bytes)!",
					new String[] { RequestType.CreateSession.name() });
		}
		if ((msResponse != null && msRequest != null) && (msResponse.compareTo(msRequest) != 0)) {
			Logger.getLogger(getClass().getName()).log(Level.FINE,
					"{0} MaxMessageSize has been changed from {1} to {2}(bytes)!",
					new String[] { RequestType.CreateSession.name(), Long.toString(msRequest.longValue()),
							Long.toString(msResponse.longValue()) });
		}
	}

	/**
	 * Validates the server nonce from the created sessions response.
	 *
	 * @param Response
	 *
	 * @throws ServiceResultException
	 */
	private void validateCreatedServerNonce(byte[] serverNonce, boolean isActivateSession)
			throws ServiceResultException {
		if (serverNonce != null) {
			// length of nonce do not match
			if (this.configuration.getNonceLength(this.endpoint) != serverNonce.length) {
				throw new ServiceResultException(StatusCodes.Bad_NonceInvalid,
						"The length of the server nonce do not match with the required client nonce!");
			}
			for (ClientSession session : this.sessionManager.getSessions()) {
				if (session != this && serverNonce == session.getServerNonce()) {
					throw new ServiceResultException(StatusCodes.Bad_NonceInvalid,
							"The server nonce of this session matches with a server nonce of a different session!");
				}
			}
		} else if (SecurityConstants.SECURITY_POLICY_URI_BINARY_NONE.equals(this.endpoint.getSecurityPolicyUri())) {
			RequestType type;
			if (isActivateSession) {
				type = RequestType.ActivateSession;
			} else {
				type = RequestType.CreateSession;
			}
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} The requested server nonce is empty, but there is no security to use the nonce!",
					new String[] { type.name() });
			return;
		} else {
			throw new ServiceResultException(StatusCodes.Bad_NonceInvalid,
					"The length of the server nonce do not match with the required client nonce!");
		}
	}

	/**
	 * Validates the user siganture from the created session.
	 *
	 * @param Response
	 *
	 * @throws ServiceResultException
	 */
	private void validateCreatedServerSignature(CreateSessionResponse response) throws ServiceResultException {
		if (!SecurityConstants.SECURITY_POLICY_URI_BINARY_NONE.equals(this.endpoint.getSecurityPolicyUri())) {
			if (response.getServerSignature() == null) {
				throw new ServiceResultException(StatusCodes.Bad_UserSignatureInvalid,
						"The user signature of this session is empty!");
			}
			if (response.getServerSignature().getSignature() == null
					|| response.getServerSignature().getSignature().getLength() == 0) {
				throw new ServiceResultException(StatusCodes.Bad_UserSignatureInvalid,
						"The user signature of this session is empty!");
			}
		}
	}

	/**
	 * Validates the server softwarecertificates returned from the create session
	 * response.
	 *
	 * @param Response
	 *
	 */
	private void validateCreatedServerSoftwareCertificates(CreateSessionResponse response) {
		if (response.getServerSoftwareCertificates() == null || response.getServerSoftwareCertificates().length <= 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} The server software certificates are empty!",
					new String[] { RequestType.CreateSession.name() });
		}
	}

	private ServiceResponse waitForResponse(RequestType requestType, AsyncResult<?> result, ServiceRequest request)
			throws ServiceResultException {
		ServiceResponse response = null;
		try {
			if (request.getRequestHeader().getTimeoutHint() != null
					&& request.getRequestHeader().getTimeoutHint().intValue() != 0) {
				response = (ServiceResponse) result
						.waitForResult(request.getRequestHeader().getTimeoutHint().longValue(), TimeUnit.MILLISECONDS);
			} else {
				response = (ServiceResponse) result.waitForResult();
			}
			validateResponse(requestType, response, request);
		} catch (ServiceResultException e) {
			if (this.lastKeepAliveTime != null || request instanceof CreateSessionRequest
					|| request instanceof ActivateSessionRequest) {
				onError(requestType, request, response, e);
			}
			throw e;
		}
		return response;
	}

	@Override
	void onConnectionLost(ServiceResult result) {
		onKeepAliveError(result);
	}

	void setKeepAliveTimer(Timer timer) {
		this.keepAliveTimer = timer;
	}

	Timer getKeepAliveTimer() {
		return this.keepAliveTimer;
	}

	/**
	 * Set the Security Policy.
	 *
	 * @param SecurityPolicyUri
	 */
	void setSecurityPolicyUri(String securityPolicyUri) {
		if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
			this.securityPolicyUri = this.endpoint.getSecurityPolicyUri();
			return;
		}
		this.securityPolicyUri = securityPolicyUri;
	}

	/**
	 * Setter of the ServerNonce when recieving an Session Request
	 *
	 * @param serverNonce
	 * @throws ServiceResultException
	 */
	void setSenderNonce(byte[] serverNonce) throws ServiceResultException {
		// check nonce
		validateCreatedServerNonce(serverNonce, true);
		this.serverNonce = serverNonce;
	}

	/**
	 * Adds an Asynchronous request to the client request queue.
	 *
	 * @param Result        Asynchronous result of the service.
	 * @param RequestId     Client-assigned Id of the Request.
	 * @param RequestTypeId Request type of the service
	 */
	@Override
	void asyncRequestStarted(AsyncResult<?> result, UnsignedInteger requestHandle, NodeId requestTypeId) {
		synchronized (this.lock) {
			// check if the request completed asynchronously
			AsyncRequestState state = removeRequest(requestHandle, requestTypeId);
			if (state == null) {
				state = new AsyncRequestState();
				state.setRequestTypeId(requestTypeId);
				state.setRequestId(requestHandle);
				state.setDefunct(false);
				state.setResult(result);
				state.setTimestamp(DateTime.currentTime());
				this.outstandingRequests.add(state);
			}
		}
	}

	void beginPublish(boolean moreNotifications) {
		List<SubscriptionAcknowledgement> tmpAcknowledgementsToSend;
		// collect the current set if acknowledgements
		synchronized (this.lock) {
			tmpAcknowledgementsToSend = new ArrayList<>(this.acknowledgementsToSend);
			// free
			this.acknowledgementsToSend.clear();
		}
		// send publish request
		PublishRequest request = new PublishRequest();
		request.setSubscriptionAcknowledgements(
				tmpAcknowledgementsToSend.toArray(new SubscriptionAcknowledgement[tmpAcknowledgementsToSend.size()]));
		try {
			AsyncResult<?> result = beginPublish(this, request, moreNotifications);
			if (result.getError() != null && result.getError().getStatusCode().isBad()) {
				throw result.getError();
			}
		} catch (ServiceResultException e) {
			try {
				onError(RequestType.Publish, request, null, e);
			} catch (ServiceResultException e2) {
				Logger.getLogger(getClass().getName()).log(Level.FINE, null, e2);
				return;
			}
		}
	}

	private static final ReadRequest serverStateRequest() {
		ReadRequest request = new ReadRequest();
		ReadValueId[] request2Read = new ReadValueId[] {
				new ReadValueId(Identifiers.Server_ServerStatus_State, Attributes.Value, null, null) };
		request.setNodesToRead(request2Read);
		return request;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// max monitored items per create
	public long getMaxMonitoredItemsPerCreate() {
		return maxMonitoredItemsPerCreate;
	}

	public void setMaxMonitoredItemsPerCreate(long maxMonitoredItemsPerCreate) {
		this.maxMonitoredItemsPerCreate = maxMonitoredItemsPerCreate;
	}

	public long getMaxMonitoredItemsPerCreateDefault() {
		return maxMonitoredItemsPerCreateDefault;
	}

	public void setMaxMonitoredItemsPerCreateDefault(long maxMonitoredItemsPerCreateDefault) {
		this.maxMonitoredItemsPerCreateDefault = maxMonitoredItemsPerCreateDefault;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// max nodes per write
	public long getMaxNodesPerWrite() {
		return this.maxNodesPerWrite;
	}

	public void setMaxNodesPerWrite(long maxNodesPerWrite) {
		this.maxNodesPerWrite = maxNodesPerWrite;
	}

	public long getMaxNodesPerWriteDefault() {
		return this.maxNodesPerWriteDefault;
	}

	public void setMaxNodesPerWriteDefault(long maxNodesPerWriteDefault) {
		this.maxNodesPerWriteDefault = maxNodesPerWriteDefault;
	}

	///////////////////////////////////////////////////////////////////////////////////////
	// max translate browsepath to nodeid
	public long getMaxTranslateBrowsePath() {
		return this.maxTranslateBrowsePath;
	}

	public void setMaxTranslateBrowsePath(long maxTranslateBrowsePath) {
		this.maxTranslateBrowsePath = maxTranslateBrowsePath;
	}

	public long getMaxTranslateBrowsePathDefault() {
		return this.maxTranslateBrowsePathDefault;
	}

	public void setMaxTranslateBrowsePathDefault(long maxTranslateBrowsePathDefault) {
		this.maxTranslateBrowsePathDefault = maxTranslateBrowsePathDefault;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// max register nodes per call
	public long getMaxRegisterNodes() {
		return this.maxRegisterNodes;
	}

	public void setMaxRegisterNodes(long maxRegisterNodes) {
		this.maxRegisterNodes = maxRegisterNodes;
	}

	public long getMaxRegisterNodesDefault() {
		return this.maxRegisterNodesDefault;
	}

	public void setMaxRegisterNodesDefault(long maxRegisterNodesDefault) {
		this.maxRegisterNodesDefault = maxRegisterNodesDefault;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// max read nodes per call
	public long getMaxRead() {
		return this.maxRead;
	}

	public void setMaxRead(long maxRead) {
		this.maxRead = maxRead;
	}

	public long getMaxReadDefault() {
		return this.maxReadDefault;
	}

	public void setMaxReadDefault(long maxReadDefault) {
		this.maxReadDefault = maxReadDefault;
	}

	///////////////////////////////////////////////////////////////////////////////////
	// max node management
	public long getMaxNodeManagement() {
		return this.maxNodeManagement;
	}

	public void setMaxNodeManagement(long maxNodeManagement) {
		this.maxNodeManagement = maxNodeManagement;
	}

	public long getMaxNodeManagementDefault() {
		return this.maxNodeManagementDefault;
	}

	public void setMaxNodeManagementDefault(long maxNodeManagementDefault) {
		this.maxNodeManagementDefault = maxNodeManagementDefault;
	}

	/////////////////////////////////////////////////////////////////////////////////
	// max method call
	public long getMaxMethodCall() {
		return this.maxMethodCall;
	}

	public void setMaxMethodCall(long maxMethodCall) {
		this.maxMethodCall = maxMethodCall;
	}

	public long getMaxMethodCallDefault() {
		return this.maxMethodCallDefault;
	}

	public void setMaxMethodCallDefault(long maxMethodCallDefault) {
		this.maxMethodCallDefault = maxMethodCallDefault;
	}

	//////////////////////////////////////////////////////////////////////////////////
	// max history update event
	public long getMaxHistoryUpdateEvent() {
		return this.maxHistoryUpdateEvent;
	}

	public void setMaxHistoryUpdateEvent(long maxHistoryUpdateEvent) {
		this.maxHistoryUpdateEvent = maxHistoryUpdateEvent;
	}

	public long getMaxHistoryUpdateEventDefault() {
		return this.maxHistoryUpdateEventDefault;
	}

	public void setMaxHistoryUpdateEventDefault(long maxHistoryUpdateEventDefault) {
		this.maxHistoryUpdateEventDefault = maxHistoryUpdateEventDefault;
	}

	////////////////////////////////////////////////////////////////////////////////
	// max history update data
	public long getMaxHistoryUpdateData() {
		return this.maxHistoryUpdateData;
	}

	public void setMaxHistoryUpdateData(long maxHistoryUpdateData) {
		this.maxHistoryUpdateData = maxHistoryUpdateData;
	}

	public long getMaxHistoryUpdateDataDefault() {
		return this.maxHistoryUpdateDataDefault;
	}

	public void setMaxHistoryUpdateDataDefault(long maxHistoryUpdateDataDefault) {
		this.maxHistoryUpdateDataDefault = maxHistoryUpdateDataDefault;
	}

	////////////////////////////////////////////////////////////////////////////////
	// max history read event
	public long getMaxHistoryReadEvent() {
		return this.maxHistoryReadEvent;
	}

	public void setMaxHistoryReadEvent(long maxHistoryReadEvent) {
		this.maxHistoryReadEvent = maxHistoryReadEvent;
	}

	public long getMaxHistoryReadEventDefault() {
		return this.maxHistoryReadEventDefault;
	}

	public void setMaxHistoryReadEventDefault(long maxHistoryReadEventDefault) {
		this.maxHistoryReadEventDefault = maxHistoryReadEventDefault;
	}

	////////////////////////////////////////////////////////////////////////////////
	// max history read data
	public long getMaxHistoryReadData() {
		return this.maxHistoryReadData;
	}

	public void setMaxHistoryReadData(long maxHistoryReadData) {
		this.maxHistoryReadData = maxHistoryReadData;
	}

	public long getMaxHistoryReadDataDefault() {
		return this.maxHistoryReadDataDefault;
	}

	public void setMaxHistoryReadDataDefault(long maxHistoryReadDataDefault) {
		this.maxHistoryReadDataDefault = maxHistoryReadDataDefault;
	}

	////////////////////////////////////////////////////////////////////////////////
	// max nodes per browse
	public long getMaxNodesPerBrowse() {
		return this.maxNodesPerBrowse;
	}

	public void setMaxNodesPerBrowse(long maxNodesPerBrowse) {
		this.maxNodesPerBrowse = maxNodesPerBrowse;
	}

	public long getMaxNodesPerBrowseDefault() {
		return this.maxNodesPerBrowseDefault;
	}

	public void setMaxNodesPerBrowseDefault(long maxNodesPerBrowseDefault) {
		this.maxNodesPerBrowseDefault = maxNodesPerBrowseDefault;
	}

	public boolean isReadServerLimits() {
		return readServerLimits;
	}

	public void setReadServerLimits(boolean readServerLimits) {
		this.readServerLimits = readServerLimits;
	}

	public long getMaxFutureServerTime() {
		return maxFutureServerTime;
	}

	public void setMaxFutureServerTime(long maxFutureServerTime) {
		this.maxFutureServerTime = maxFutureServerTime;
	}

	public long getMaxPastServerTime() {
		return maxPastServerTime;
	}

	public void setMaxPastServerTime(long maxPastServerTime) {
		this.maxPastServerTime = maxPastServerTime;
	}

	public long getReconnectCount() {
		return reconnectCount;
	}

	public void setReconnectCount(long reconnectCount) {
		this.reconnectCount = reconnectCount;
		this.reconnectCounter = reconnectCount;
	}

	public long getReconnectCounter() {
		return reconnectCounter;
	}

	public void setReconnectCounter(long reconnectCounter) {
		this.reconnectCounter = reconnectCounter;
	}

	public long decrementCounter() {
		this.reconnectCounter--;
		return reconnectCounter;
	}

	public boolean isReCreateSubscriptions() {
		return reCreateSubscriptions;
	}

	public void setReCreateSubscriptions(boolean reCreateSubscriptions) {
		this.reCreateSubscriptions = reCreateSubscriptions;
	}

	/**
	 * Called when session checks for keep alive. Sends a keep alive by reading from
	 * the server.
	 *
	 * @author Thomas Z&ouml;chbauer
	 *
	 */
	private class SessionKeepAliveTask extends TimerTask {
		private ReadRequest status = null;

		public SessionKeepAliveTask(Object status) {
			this.status = (ReadRequest) status;
		}

		@Override
		public void run() {
			onKeepAlive(this.status);
		}
	}
}
