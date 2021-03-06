package opc.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesRequest;
import org.opcfoundation.ua.core.AddNodesResponse;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.AddReferencesRequest;
import org.opcfoundation.ua.core.AddReferencesResponse;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseNextRequest;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowsePath;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.CallMethodRequest;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.CallResponse;
import org.opcfoundation.ua.core.CancelRequest;
import org.opcfoundation.ua.core.CancelResponse;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.CreateMonitoredItemsRequest;
import org.opcfoundation.ua.core.CreateSubscriptionRequest;
import org.opcfoundation.ua.core.DeleteMonitoredItemsRequest;
import org.opcfoundation.ua.core.DeleteMonitoredItemsResponse;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.DeleteNodesRequest;
import org.opcfoundation.ua.core.DeleteNodesResponse;
import org.opcfoundation.ua.core.DeleteReferencesItem;
import org.opcfoundation.ua.core.DeleteReferencesRequest;
import org.opcfoundation.ua.core.DeleteReferencesResponse;
import org.opcfoundation.ua.core.DeleteSubscriptionsRequest;
import org.opcfoundation.ua.core.DeleteSubscriptionsResponse;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.HistoryReadDetails;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryReadValueId;
import org.opcfoundation.ua.core.HistoryUpdateDetails;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.HistoryUpdateResult;
import org.opcfoundation.ua.core.ModifyMonitoredItemsRequest;
import org.opcfoundation.ua.core.ModifyMonitoredItemsResponse;
import org.opcfoundation.ua.core.ModifySubscriptionRequest;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.RegisterNodesRequest;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.SetMonitoringModeResponse;
import org.opcfoundation.ua.core.SetPublishingModeRequest;
import org.opcfoundation.ua.core.SetPublishingModeResponse;
import org.opcfoundation.ua.core.SetTriggeringRequest;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.TransferResult;
import org.opcfoundation.ua.core.TransferSubscriptionsRequest;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.UnregisterNodesRequest;
import org.opcfoundation.ua.core.UnregisterNodesResponse;
import org.opcfoundation.ua.core.ViewDescription;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.transport.tcp.io.SequenceNumber;
import org.opcfoundation.ua.utils.NumericRange;

import opc.client.application.core.ApplicationConfiguration;
import opc.client.application.core.method.MethodElement;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.node.Node;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.utils.Utils;

/**
 * The profile manager is the top layer to send services from a UAClient
 * application to an OPC server.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class ProfileManager {
	private static final String SESSSIONNOTALIVE = "Session is not Alive!";
	private static final String ZEROONE = "{0} {1}";
	/** manager to administrate session services */
	private SessionManager sessionManager = null;
	/** manager to administrate subscription services */
	private SubscriptionManager subscriptionManager = null;
	/** manager to administrate node services */
	private NodeManager nodeManager = null;
	/** client handle for monitored items */
	private SequenceNumber clientHandleForMonitoredItems = new SequenceNumber();

	/**
	 * Constructor of the profile manager
	 * 
	 * @param Logger Logging handler
	 */
	public ProfileManager() {
		this.nodeManager = new NodeManager();
		this.subscriptionManager = new SubscriptionManager();
		this.sessionManager = new SessionManager(this.subscriptionManager, this.nodeManager);
	}

	/**
	 * Disposes all OPC UA client resources
	 */
	public void dispose() {
		this.sessionManager.dispose();
	}

	/**
	 * Activates a created Session with a Activate-Session Service.
	 * 
	 * @param preferredLocales Preferred locales to use.
	 * @param Session          Created session to activate or impersonate.
	 * @param PreferredLocales Preferred locales to use.
	 * @param SkipLogging      If the value is TRUE, the logging mechanism is
	 *                         skipped.<br>
	 *                         If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation   If the value is TRUE, the Service will send
	 *                         asynchronous over the stack.<br>
	 *                         If the value is FALSE, the Service will send
	 *                         synchronous over the stack.
	 * @return TRUE if the Session has been activated successfull, otherwise FALSE.
	 * @throws ServiceResultException Session
	 */
	public ActivateSessionResponse activateSession(ClientSession session, UserIdentity identity,
			String[] preferredLocales, boolean asyncOperation) throws ServiceResultException {
		if (!verifySession(session)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		ActivateSessionResponse response;
		if (!asyncOperation) {
			response = this.sessionManager.activateSession(session, identity, preferredLocales, false);
		} else {
			response = this.sessionManager.activateSession(session, identity, preferredLocales, true);
		}
		this.sessionManager.changeActiveSession(session);
		return response;
	}

	/**
	 * This service is used to add one or more Nodes into a servers AddressSpace
	 * hierarchy.
	 * 
	 * @param Session         Session to use.
	 * @param BrowseNames     The browse name of the Node to add.
	 * @param NodeAttributes  The Attributes that are specific to the NodeClass,
	 *                        this is an extensible parameter.
	 * @param NodeClasses     NodeClass of the Node to add.
	 * @param ParentNodeIds   ExpandedNodeId of the parent Node for the Reference.
	 * @param ReferenceTypes  NodeId of the hierachical ReferenceType to use for the
	 *                        Reference from the parent Node to the new Node.
	 * @param NewNodeIds      Client requested ExpandedNodeId, the ServerIndex in
	 *                        the Id shall be 0.
	 * @param TypeDefinitions NodeId of the TypeDefinitionNode for the Node to add,
	 *                        shall be NULL for all NodeClasses other than Object
	 *                        and Variable.
	 * @param SkipLogging     If the value is TRUE, the logging mechanism is
	 *                        skipped.<br>
	 *                        If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation  If the value is TRUE, the Service will send
	 *                        asynchronous over the stack.<br>
	 *                        If the value is FALSE, the Service will send
	 *                        synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the {@link AddNodesResponse} .
	 *         TRUE if the {@link StatusCode} was GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public AddNodesResult[] addNodes(ClientSession session, QualifiedName[] browseNames,
			ExtensionObject[] nodeAttributes, NodeClass[] nodeClasses, ExpandedNodeId[] parentNodeIds,
			NodeId[] referenceTypes, ExpandedNodeId[] newNodeIds, ExpandedNodeId[] typeDefinitions,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, "No valid Session !");
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (tmpSession.getMaxNodeManagement() <= 0)
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		// divide add nodes request in any if the limits exceeds
		int count = 0;
		AddNodesItem item = null;
		List<AddNodesItem> nodesToAdd = new ArrayList<>();
		AddNodesResponse response = null;
		AddNodesResult[] result = new AddNodesResult[newNodeIds.length];
		for (int i = 0; i < newNodeIds.length; i++) {
			item = new AddNodesItem();
			item.setBrowseName(browseNames[i]);
			item.setNodeAttributes(nodeAttributes[i]);
			item.setNodeClass(nodeClasses[i]);
			item.setParentNodeId(parentNodeIds[i]);
			item.setReferenceTypeId(referenceTypes[i]);
			item.setRequestedNewNodeId(newNodeIds[i]);
			item.setTypeDefinition(typeDefinitions[i]);
			nodesToAdd.add(item);
			count++;
			if (count % tmpSession.getMaxNodeManagement() == 0 || count == newNodeIds.length) {
				AddNodesRequest request = new AddNodesRequest();
				request.setNodesToAdd(nodesToAdd.toArray(new AddNodesItem[0]));
				if (!asyncOperation) {
					response = this.nodeManager.addNodes(tmpSession, request);
				} else {
					response = this.nodeManager.beginAddNodes(tmpSession, request);
				}
				if (response != null) {
					AddNodesResult[] res = response.getResults();
					int index = res.length;
					for (AddNodesResult r : res) {
						result[count - index] = r;
						index++;
					}
				}
				nodesToAdd.clear();
			}
		}
		return result;
	}

	/**
	 * This Service is used to add one or more References to one or more Nodes.
	 * 
	 * @param Session          Session to use.
	 * @param SourceNodeId     NodeId of the Node to which the Reference is to be
	 *                         added.
	 * @param TargetNodeId     Expanded NodeId of the TargetNode.
	 * @param RreferenceTypeId NodeId of the ReferenceType that defines the
	 *                         Reference.
	 * @param TargetNodeClass  NodeClass of the TargetNode.
	 * @param IsForward        If the value is TRUE, the server creates a forward
	 *                         Reference. <br>
	 *                         If the value is FALSE, the server creates an inverse
	 *                         Reference.
	 * @param TargetServerUri  URI of the remote Server. If this parameter is not
	 *                         null, it uses the ServerIndex from the TargetNodeId.
	 * @param SkipLogging      If the value is TRUE, the logging mechanism is
	 *                         skipped.<br>
	 *                         If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation   If the value is TRUE, the Service will send
	 *                         asynchronous over the stack.<br>
	 *                         If the value is FALSE, the Service will send
	 *                         synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link AddReferencesResponse} . TRUE if the {@link StatusCode} was
	 *         GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public StatusCode[] addReference(ClientSession session, NodeId[] sourceNodeId, ExpandedNodeId[] targetNodeId,
			NodeId[] referenceTypeId, NodeClass[] targetNodeClass, Boolean[] isForward, String[] targetServerUri,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		AddReferencesItem[] referencesToAdd = new AddReferencesItem[sourceNodeId.length];
		for (int i = 0; i < sourceNodeId.length; i++) {
			referencesToAdd[i] = new AddReferencesItem();
			referencesToAdd[i].setSourceNodeId(sourceNodeId[i]);
			referencesToAdd[i].setTargetNodeId(targetNodeId[i]);
			referencesToAdd[i].setReferenceTypeId(referenceTypeId[i]);
			referencesToAdd[i].setTargetNodeClass(targetNodeClass[i]);
			referencesToAdd[i].setIsForward(isForward[i]);
			referencesToAdd[i].setTargetServerUri(targetServerUri[i]);
		}
		AddReferencesRequest request = new AddReferencesRequest();
		request.setReferencesToAdd(referencesToAdd);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		AddReferencesResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.addReferences(tmpSession, request);
		} else {
			response = this.nodeManager.beginAddReferences(tmpSession, request);
		}
		return response.getResults();
	}

	/**
	 * Send a bunch of browse request at once to the server
	 * 
	 * @param Session          Session to use.
	 * @param NodesToBrowse    NodeId of the Node to be browse.
	 * @param BrowseDirection  An enumeration that specifies the direction of
	 *                         References to follow. It has the following values,
	 *                         Forward, Inverse, Both.
	 * @param IncludeSubtypese Indicates whether subtypes of the ReferenceType
	 *                         should be included in the browse. If TRUE, then
	 *                         instances of referenceTypeId and all of its subtypes
	 *                         are returned.
	 * @param NodeClassMask    Specifies the NodeClasses of the TargetNodes. Only
	 *                         TargetNodes with the selected NodeClasses are
	 *                         returned. If set to zero, then all NodeClasses are
	 *                         returned.
	 * @param ReferenceTypeId  NodeId of the ReferenceType for the Reference.
	 * @param ResultMask       Specifies the fields in the ReferenceDescription
	 *                         structure that should be returned.0...ReferenceType
	 *                         1...IsForward 2...NodeClass 3...BrowseName
	 *                         4...DisplayName 5...TypeDefinition
	 * @param MaxReferences    Indicates the maximum number of references to return
	 *                         for each starting Node specified in the request. The
	 *                         value 0 indicates that the Client is imposing no
	 *                         limitation.
	 * @param View             Description of the View to browse. An empty
	 *                         ViewDescription value indicates the entire
	 *                         AddressSpace. Use of the empty ViewDescription value
	 *                         causes all References of the nodeToBrowse to be
	 *                         returned. Use of any other View causes only the
	 *                         References of the nodeToBrowse that are defined for
	 *                         that View to be returned.
	 * @param SkipLogging      If the value is TRUE, the logging mechanism is
	 *                         skipped.<br>
	 *                         If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation   If the value is TRUE, the Service will send
	 *                         asynchronous over the stack.<br>
	 *                         If the value is FALSE, the Service will send
	 *                         synchronous over the stack.
	 * @return BrowseResponse OPC UA Server
	 * @throws ServiceResultException
	 */
	public BrowseResponse browse(ClientSession session, NodeId[] nodesToBrowse, BrowseDirection browseDirection,
			boolean includeSubtypes, UnsignedInteger nodeClasses, NodeId referenceTypeId, UnsignedInteger resultMask,
			UnsignedInteger maxReferences, ViewDescription view, boolean asyncOperation, boolean validateResults)
			throws ServiceResultException {
		BrowseResponse response = null;
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		// max browse should not be 0
		if (tmpSession.getMaxNodesPerBrowse() <= 0)
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		int count = 0;
		List<BrowseDescription> browseDescriptions = new ArrayList<>(nodesToBrowse.length);
		for (NodeId id : nodesToBrowse) {
			BrowseDescription browseDescription = new BrowseDescription();
			browseDescription.setNodeId(nodesToBrowse[count]);
			browseDescription.setBrowseDirection(browseDirection);
			browseDescription.setIncludeSubtypes(includeSubtypes);
			browseDescription.setNodeClassMask(nodeClasses);
			browseDescription.setReferenceTypeId(referenceTypeId);
			browseDescription.setResultMask(resultMask);
			browseDescriptions.add(browseDescription);
			count++;
			if (count % tmpSession.getMaxNodesPerBrowse() == 0 || count == nodesToBrowse.length) {
				BrowseRequest request = new BrowseRequest();
				request.setNodesToBrowse(browseDescriptions.toArray(new BrowseDescription[browseDescriptions.size()]));
				request.setRequestedMaxReferencesPerNode(maxReferences);
				request.setView(view);
				Browser browser = new Browser(tmpSession);
				BrowseResponse tmpResp = browser.browse(request, asyncOperation, validateResults);
				if (response == null)
					response = tmpResp;
				else {
					List<BrowseResult> results = new ArrayList<>();
					results.addAll(Arrays.asList(response.getResults()));
					results.addAll(Arrays.asList(tmpResp.getResults()));
					response.setResults(results.toArray(new BrowseResult[0]));
				}
				// clear descriptions for next call
				browseDescriptions.clear();
			}
		}
		return response;
	}

	/**
	 * Send a bunch of browse request at once to the server
	 * 
	 * @param Session         Session to use.
	 * @param NodesToBrowse   Array of NodeIds of the Node to be browse.
	 * @param BrowseDirection Array of enumeration that specifies the direction of
	 *                        References to follow. It has the following values,
	 *                        Forward, Inverse, Both.
	 * @param IncludeSubtypes Array which indicates whether subtypes of the
	 *                        ReferenceType should be included in the browse. If
	 *                        TRUE, then instances of referenceTypeId and all of its
	 *                        subtypes are returned.
	 * @param NodeClassMask   Array which specifies the NodeClasses of the
	 *                        TargetNodes. Only TargetNodes with the selected
	 *                        NodeClasses are returned. If set to zero, then all
	 *                        NodeClasses are returned.
	 * @param ReferenceTypeId Array of NodeIds of the ReferenceType for the
	 *                        References.
	 * @param ResultMask      Array which Specifies the fields in the
	 *                        ReferenceDescription structure that should be
	 *                        returned.0...ReferenceType 1...IsForward 2...NodeClass
	 *                        3...BrowseName 4...DisplayName 5...TypeDefinition
	 * @param MaxReferences   Indicates the maximum number of references to return
	 *                        for each starting Node specified in the request. The
	 *                        value 0 indicates that the Client is imposing no
	 *                        limitation.
	 * @param View            Description of the View to browse. An empty
	 *                        ViewDescription value indicates the entire
	 *                        AddressSpace. Use of the empty ViewDescription value
	 *                        causes all References of the nodeToBrowse to be
	 *                        returned. Use of any other View causes only the
	 *                        References of the nodeToBrowse that are defined for
	 *                        that View to be returned.
	 * @param SkipLogging     If the value is TRUE, the logging mechanism is
	 *                        skipped.<br>
	 *                        If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation  If the value is TRUE, the Service will send
	 *                        asynchronous over the stack.<br>
	 *                        If the value is FALSE, the Service will send
	 *                        synchronous over the stack.
	 * @return BrowseResponse OPC UA Server
	 * @throws ServiceResultException
	 */
	public BrowseResponse browse(ClientSession session, NodeId[] nodesToBrowse, BrowseDirection[] browseDirection,
			boolean[] includeSubtypes, UnsignedInteger[] nodeClasses, NodeId[] referenceTypeId,
			UnsignedInteger[] resultMask, UnsignedInteger maxReferences, ViewDescription view, boolean asyncOperation,
			boolean validateResults) throws ServiceResultException {
		BrowseResponse response = null;
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		// max browse should not be 0
		if (tmpSession.getMaxNodesPerBrowse() <= 0)
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		List<BrowseDescription> browseDescriptions = new ArrayList<>(nodesToBrowse.length);
		int count = 0;
		for (NodeId id : nodesToBrowse) {
			BrowseDescription browseDescription = new BrowseDescription();
			browseDescription.setNodeId(nodesToBrowse[count]);
			browseDescription.setBrowseDirection(browseDirection[count]);
			browseDescription.setIncludeSubtypes(includeSubtypes[count]);
			browseDescription.setNodeClassMask(nodeClasses[count]);
			browseDescription.setReferenceTypeId(referenceTypeId[count]);
			browseDescription.setResultMask(resultMask[count]);
			browseDescriptions.add(browseDescription);
			count++;
			if (count % tmpSession.getMaxNodesPerBrowse() == 0 || count == nodesToBrowse.length) {
				BrowseRequest request = new BrowseRequest();
				request.setNodesToBrowse(browseDescriptions.toArray(new BrowseDescription[browseDescriptions.size()]));
				request.setRequestedMaxReferencesPerNode(maxReferences);
				request.setView(view);
				Browser browser = new Browser(tmpSession);
				BrowseResponse tmpResp = browser.browse(request, asyncOperation, validateResults);
				if (response == null)
					response = tmpResp;
				else {
					List<BrowseResult> results = new ArrayList<>();
					results.addAll(Arrays.asList(response.getResults()));
					results.addAll(Arrays.asList(tmpResp.getResults()));
					response.setResults(results.toArray(new BrowseResult[0]));
				}
				// clear descriptions for next call
				browseDescriptions.clear();
			}
		}
		return response;
	}

	/**
	 * @param Session                   Session to use.
	 * @param ContinuationPoints        Server-defined opaque values that represent
	 *                                  continuation points. The value for a
	 *                                  continuation point was returned to the
	 *                                  Client in a previous Browse or BrowseNext
	 *                                  response. These values are used to identify
	 *                                  the previously processed Browse or
	 *                                  BrowseNext request that is being continued
	 *                                  and the point in the result set from which
	 *                                  the browse response is to continue. Clients
	 *                                  may mix continuation points from different
	 *                                  Browse or BrowseNext responses.
	 * @param ReleaseContinuationPoints If the value is TRUE, passed
	 *                                  continuationPoints shall be reset to free
	 *                                  resources in the Server. If the value is
	 *                                  FALSE, passed continuationPoints shall be
	 *                                  used to get the next set of browse
	 *                                  information.
	 * @param SkipLogging               If the value is TRUE, the logging mechanism
	 *                                  is skipped.<br>
	 *                                  If the value is FALSE, the logging mechanism
	 *                                  is used.
	 * @param AsyncOperation            If the value is TRUE, the Service will send
	 *                                  asynchronous over the stack.<br>
	 *                                  If the value is FALSE, the Service will send
	 *                                  synchronous over the stack.
	 * @return BrowseNextResponse OPC UA server response
	 * @throws ServiceResultException
	 */
	public BrowseNextResponse browseNext(ClientSession session, ByteString[] continuationPoints,
			boolean releaseContinuationPoints, boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		BrowseNextRequest request = new BrowseNextRequest();
		request.setContinuationPoints(continuationPoints);
		request.setReleaseContinuationPoints(releaseContinuationPoints);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		Browser browser = new Browser(tmpSession);
		if (!asyncOperation) {
			return browser.browseNext(request);
		} else {
			return browser.beginBrowseNext(request);
		}
	}

	/**
	 * This Service is used to call (invoke) a Method.
	 * 
	 * @param InputArguments Array of input argument values. An empty list indicates
	 *                       that there are no input arguments. The size and order
	 *                       of this list matches the size and order of the input
	 *                       arguments defined by the input InputArguments Property
	 *                       of the Method. The name, a description and the data
	 *                       type of each argument are defined by the Argument
	 *                       structure in each element of the method�s
	 *                       InputArguments Property.
	 * @param MethodId       NodeIds of the Methods to invoke.
	 * @param ObjectId       The NodeIds shall be that of the Objects or ObjectTypes
	 *                       that are the sources of a HasComponent Reference (or
	 *                       subtype of HasComponent Reference) to this Method.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return {@link CallResponse}
	 * @throws ServiceResultException
	 */
	public CallResponse call(ClientSession session, Variant[][] inputArguments, NodeId[] objectId, NodeId[] methodId,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		CallMethodRequest[] callMethodRequests = new CallMethodRequest[methodId.length];
		MethodElement[] elements2call = new MethodElement[methodId.length];
		for (int i = 0; i < methodId.length; i++) {
			callMethodRequests[i] = new CallMethodRequest();
			callMethodRequests[i].setInputArguments(inputArguments[i]);
			callMethodRequests[i].setMethodId(methodId[i]);
			callMethodRequests[i].setObjectId(objectId[i]);
			elements2call[i] = new MethodElement(methodId[i], inputArguments[i]);
		}
		CallRequest request = new CallRequest();
		request.setMethodsToCall(callMethodRequests);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (!asyncOperation) {
			return this.nodeManager.call(tmpSession, request, elements2call);
		} else {
			return this.nodeManager.beginCallMethod(tmpSession, request, elements2call);
		}
	}

	/**
	 * 
	 * @param RequestHandle
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return
	 * @throws ServiceResultException
	 */
	public CancelResponse cancelRequest(ClientSession session, UnsignedInteger requestHandle, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		CancelRequest request = new CancelRequest();
		request.setRequestHandle(requestHandle);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (!asyncOperation) {
			return this.sessionManager.cancelRequest(tmpSession, request);
		} else {
			return (CancelResponse) this.sessionManager.asyncCancelRequest(tmpSession, request).waitForResult();
		}
	}

	/**
	 * Closes a session.
	 * 
	 * @param Session             Session to close.
	 * @param DeleteSubscriptions If the value is TRUE, the Server deletes all
	 *                            Subscriptions associated with the Session. If the
	 *                            value is FALSE, the Server keeps the Subscriptions
	 *                            associated with the Session until they timeout
	 *                            based on their own lifetime.
	 * @param SkipLogging         If the value is TRUE, the logging mechanism is
	 *                            skipped.<br>
	 *                            If the value is FALSE, the logging mechanism is
	 *                            used.
	 * @param AsyncOperation      If the value is TRUE, the Service will send
	 *                            asynchronous over the stack.<br>
	 *                            If the value is FALSE, the Service will send
	 *                            synchronous over the stack.
	 * @return {@link CloseSessionResponse}
	 * @throws ServiceResultException
	 */
	public CloseSessionResponse closeSession(ClientSession session, boolean deleteSubscriptions, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (!asyncOperation) {
			return this.sessionManager.closeSession(tmpSession, deleteSubscriptions);
		} else {
			return this.sessionManager.asyncCloseSession(tmpSession, deleteSubscriptions);
		}
	}

	/**
	 * This Service is used to create and add one or more MonitoredItems to a
	 * Subscription. MonitoredItems are defined to subscribe to Data Changes and
	 * Events.
	 * 
	 * @param Session           Session to use.
	 * @param SubscriptionId    The Server-assigned identifier for the Subscription
	 *                          that will report Notifications for this
	 *                          MonitoredItem.
	 * @param NodesToMonitor    Identifies an item in the AddressSpace to monitor.
	 *                          To monitor for Events, the attributeId element of
	 *                          the ReadValueId structure is the id of the
	 *                          EventNotifier Attribute.
	 * @param SamplingInterval  A requested monitoring parameter. The interval that
	 *                          defines the fastest rate at which the
	 *                          MonitoredItem(s) should be accessed and evaluated.
	 *                          This interval is defined in milliseconds.
	 * @param Filter            A requested monitoring parameter. A filter used by
	 *                          the Server to determine if the MonitoredItem should
	 *                          generate a Notification. If not used, this parameter
	 *                          is null.
	 * @param QueueSize         A requested monitoring parameter. The requested size
	 *                          of the MonitoredItem queue.
	 * @param DiscardOldest     If the value is TRUE, the oldest (first)
	 *                          Notification in the queue is discarded. The new
	 *                          Notification is added to the end of the queue. If
	 *                          the value is FALSE, the new Notification is
	 *                          discarded. The queue is unchanged.
	 * @param MonitoringMode    The monitoring mode parameter is used to enable and
	 *                          disable the sampling of a MonitoredItem, and also to
	 *                          provide for independently enabling and disabling the
	 *                          reporting of Notifications.
	 * @param TimestampToReturn The timestamp of a value to be transmitted for each
	 *                          MonitoredItem.
	 * @param SkipLogging       If the value is TRUE, the logging mechanism is
	 *                          skipped.<br>
	 *                          If the value is FALSE, the logging mechanism is
	 *                          used.
	 * @param AsyncOperation    If the value is TRUE, the Service will send
	 *                          asynchronous over the stack.<br>
	 *                          If the value is FALSE, the Service will send
	 *                          synchronous over the stack.
	 * @return Created MonitoredItems with the given requested parameters.
	 * @throws ServiceResultException
	 */
	public MonitoredItem[] createMonitoredItem(ClientSession session, UnsignedInteger subscriptionId,
			ReadValueId[] nodesToMonitor, double samplingInterval, ExtensionObject filter, UnsignedInteger queueSize,
			boolean discardOldest, MonitoringMode monitoringMode, TimestampsToReturn timestampsToReturn,
			boolean asyncOperation, String[] key) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (nodesToMonitor == null || nodesToMonitor.length == 0) {
			throw new IllegalArgumentException("NodesToMonitor");
		}
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		List<MonitoredItemCreateRequest> monitoredItemsToCreate = new ArrayList<>(nodesToMonitor.length);
		for (ReadValueId readValue : nodesToMonitor) {
			UnsignedInteger clientHandle = new UnsignedInteger(
					this.clientHandleForMonitoredItems.getCurrentSendSequenceNumber());
			this.clientHandleForMonitoredItems.getNextSendSequencenumber();
			MonitoredItemCreateRequest createRequest = new MonitoredItemCreateRequest();
			createRequest.setItemToMonitor(readValue);
			createRequest.setMonitoringMode(monitoringMode);
			createRequest.setRequestedParameters(
					new MonitoringParameters(clientHandle, samplingInterval, filter, queueSize, discardOldest));
			monitoredItemsToCreate.add(createRequest);
		}
		CreateMonitoredItemsRequest request = new CreateMonitoredItemsRequest();
		request.setItemsToCreate(
				monitoredItemsToCreate.toArray(new MonitoredItemCreateRequest[monitoredItemsToCreate.size()]));
		request.setSubscriptionId(subscriptionId);
		request.setTimestampsToReturn(timestampsToReturn);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (!asyncOperation) {
			return this.subscriptionManager.createMonitoredItems(tmpSession, request, key);
		} else {
			return this.subscriptionManager.beginCreateMonitoredItems(tmpSession, request, key);
		}
	}

	/**
	 * This Service is used to create a Session.
	 * 
	 * @param Endpoint                             An OPC Instance of a network
	 *                                             address that the Client used to
	 *                                             access the Session Endpoint.
	 * @param ClientConfiguration                  UA Client application settings.
	 * @param ClientApplicationInstanceCertificate Instance application certificate.
	 * @param SkipLogging                          If the value is TRUE, the logging
	 *                                             mechanism is skipped.<br>
	 *                                             If the value is FALSE, the
	 *                                             logging mechanism is used.
	 * @param AsyncOperation                       If the value is TRUE, the Service
	 *                                             will send asynchronous over the
	 *                                             stack.<br>
	 *                                             If the value is FALSE, the
	 *                                             Service will send synchronous
	 *                                             over the stack.
	 * @return Created {@link ClientSession}
	 * @throws ServiceResultException
	 */
	public ClientSession createSession(EndpointDescription endpoint, String sessionName,
			ApplicationConfiguration clientConfiguration, boolean asyncOperation, long keepAliveInterval)
			throws ServiceResultException {
		return createSession(endpoint, sessionName, clientConfiguration, asyncOperation, true, keepAliveInterval);
	}

	/**
	 * This Service is used to create a Session.
	 * 
	 * @param Endpoint                             An OPC Instance of a network
	 *                                             address that the Client used to
	 *                                             access the Session Endpoint.
	 * @param ClientConfiguration                  UA Client application settings.
	 * @param ClientApplicationInstanceCertificate Instance application certificate.
	 * @param SkipLogging                          If the value is TRUE, the logging
	 *                                             mechanism is skipped.<br>
	 *                                             If the value is FALSE, the
	 *                                             logging mechanism is used.
	 * @param AsyncOperation                       If the value is TRUE, the Service
	 *                                             will send asynchronous over the
	 *                                             stack.<br>
	 *                                             If the value is FALSE, the
	 *                                             Service will send synchronous
	 *                                             over the stack.
	 * @return Created {@link ClientSession}
	 * @throws ServiceResultException
	 */
	public ClientSession createSession(EndpointDescription endpoint, String sessionName,
			ApplicationConfiguration clientConfiguration, boolean asyncOperation, boolean validateEndpoints,
			long keepAliveInterval) throws ServiceResultException {
		if (endpoint == null) {
			ServiceResult result = new ServiceResult(StatusCodes.Bad_ServerUriInvalid);
			throw new ServiceResultException(result.getCode(),
					"The Endpoint is NULL, no connection can be established!");
		}
		return this.sessionManager.createSession(endpoint, sessionName, clientConfiguration, asyncOperation,
				validateEndpoints, keepAliveInterval);
	}

	/**
	 * This Service is used to create a Subscription.
	 * 
	 * @param Session                   Session to use.
	 * @param MaxNotificationPerPublish The maximum number of notifications that the
	 *                                  Client wishes to receive in a single Publish
	 *                                  response. A value of 0 indicates that there
	 *                                  is no limit.
	 * @param Priority                  The relative priority of the Subscription.
	 *                                  When more than one Subscription needs to
	 *                                  send Notifications, the Server should
	 *                                  dequeue a Publish request to the
	 *                                  Subscription with the highest priority
	 *                                  number.
	 * @param PublishingEnabled         If the value is TRUE, publishing is enabled
	 *                                  for the Subscription. If the value is FALSE,
	 *                                  publishing is disabled for the Subscription.
	 * @param LifetimeCount             The lifetime count shall be a mimimum of
	 *                                  three times the keep keep-alive count. When
	 *                                  the publishing timer has expired this number
	 *                                  of times without a Publish request being
	 *                                  available to send a NotificationMessage,
	 *                                  then the Subscription shall be deleted by
	 *                                  the Server
	 * @param KeepAliveCount            When the publishing timer has expired this
	 *                                  number of times without requiring any
	 *                                  NotificationMessage to be sent, the
	 *                                  Subscription sends a keep-alive Message to
	 *                                  the Client. The value 0 is invalid.
	 * @param PublishingInterval        This interval defines the cyclic rate that
	 *                                  the Subscription is being requested to
	 *                                  return Notifications to the Client. This
	 *                                  interval is expressed in milliseconds.
	 * @param SkipLogging               If the value is TRUE, the logging mechanism
	 *                                  is skipped.<br>
	 *                                  If the value is FALSE, the logging mechanism
	 *                                  is used.
	 * @param AsyncOperation            If the value is TRUE, the Service will send
	 *                                  asynchronous over the stack.<br>
	 *                                  If the value is FALSE, the Service will send
	 *                                  synchronous over the stack.
	 * @return Created {@link Subscription} with the given requested parameters.
	 * @throws ServiceResultException
	 */
	public Subscription createSubscription(ClientSession session, UnsignedInteger maxNotificationPerPublish,
			UnsignedByte priority, Boolean publishingEnabled, UnsignedInteger lifetimeCount,
			UnsignedInteger maxKeepAliveCount, Double publishingInterval, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		CreateSubscriptionRequest request = new CreateSubscriptionRequest();
		request.setMaxNotificationsPerPublish(maxNotificationPerPublish);
		request.setPriority(priority);
		request.setPublishingEnabled(publishingEnabled);
		request.setRequestedLifetimeCount(lifetimeCount);
		request.setRequestedMaxKeepAliveCount(maxKeepAliveCount);
		request.setRequestedPublishingInterval(publishingInterval);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (publishingInterval <= 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Changed publishing interval to 1000ms!",
					new String[] { RequestType.CreateSubscription.name() });
			request.setRequestedPublishingInterval(1000.0);
		}
		if (lifetimeCount.longValue() < maxKeepAliveCount.longValue() * 3) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} Invalid lifetime count, changed to 3x keepalive count!",
					new String[] { RequestType.CreateSubscription.name() });
			request.setRequestedLifetimeCount(new UnsignedInteger(maxKeepAliveCount.longValue() * 3));
		}
		if (!asyncOperation) {
			return this.subscriptionManager.createSubscription(tmpSession, request);
		} else {
			return this.subscriptionManager.beginCreateSubscription(tmpSession, request);
		}
	}

	/**
	 * This Service is used to remove one or more MonitoredItems of a Subscription.
	 * 
	 * @param Session          Session to use.
	 * @param SubscriptionId   Server-assigned identifier for the Subscription that
	 *                         contains the MonitoredItems to be deleted.
	 * @param MonitoredItemIds Server-assigned Ids of the MonitoredItem to be
	 *                         deleted.
	 * @param SkipLogging      If the value is TRUE, the logging mechanism is
	 *                         skipped.<br>
	 *                         If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation   If the value is TRUE, the Service will send
	 *                         asynchronous over the stack.<br>
	 *                         If the value is FALSE, the Service will send
	 *                         synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link DeleteMonitoredItemsResponse} . TRUE if the {@link StatusCode}
	 *         was GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public StatusCode[] deleteMonitoredItems(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger[] monitoredItemIds, boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		DeleteMonitoredItemsRequest request = new DeleteMonitoredItemsRequest();
		request.setSubscriptionId(subscriptionId);
		request.setMonitoredItemIds(monitoredItemIds);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		DeleteMonitoredItemsResponse response;
		if (!asyncOperation) {
			response = this.subscriptionManager.deleteMonitoredItems(tmpSession, request);
		} else {
			response = this.subscriptionManager.beginDeleteMonitoredItems(tmpSession, request);
		}
		return response.getResults();
	}

	/**
	 * This Service is used to delete one or more Nodes from a servers AddressSpace.
	 * 
	 * @param Session                Session to use.
	 * @param NodeIds                NodeIds of the Nodes to delete
	 * @param DeleteTargetReferences If the value is TRUE, the server deletes
	 *                               References in TargetNodes that references the
	 *                               Node to delete.<br>
	 *                               If the value is FALSE, the server deletes only
	 *                               the References for which the Node to delete is
	 *                               the source.
	 * @param AsyncOperation         If the value is TRUE, the Service will send
	 *                               asynchronous over the stack.<br>
	 *                               If the value is FALSE, the Service will send
	 *                               synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the {@link DeleteNodesResponse}
	 *         . TRUE if the {@link StatusCode} was GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public DeleteNodesResponse deleteNodes(ClientSession session, NodeId[] nodeIds, Boolean[] deleteTargetReferences,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		DeleteNodesItem[] nodesToDelete = new DeleteNodesItem[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			nodesToDelete[i] = new DeleteNodesItem();
			nodesToDelete[i].setNodeId(nodeIds[i]);
			nodesToDelete[i].setDeleteTargetReferences(deleteTargetReferences[i]);
		}
		DeleteNodesRequest request = new DeleteNodesRequest();
		request.setNodesToDelete(nodesToDelete);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		DeleteNodesResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.deleteNodes(tmpSession, request);
		} else {
			response = this.nodeManager.beginDeleteNodes(tmpSession, request);
		}
		return response;
	}

	/**
	 * This Service is used to delete one or more References of a Node.
	 * 
	 * @param Session             Session to use.
	 * @param SourceNodeIds       NodeId of the Node that contains the Reference to
	 *                            delete.
	 * @param TargetNodeIds       NodeId of the TargetNode of the Reference.
	 * @param ReferenceTypeIds    NodeId of the ReferenceType that defines the
	 *                            Reference to delete.
	 * @param IsForward           If the value is TRUE, the Server deletes a forward
	 *                            Reference If the value is FALSE, the Server
	 *                            deletes an inverse Reference.
	 * @param DeleteBidirectional If the value is TRUE, the server delete the
	 *                            specified Reference and the opposite Reference
	 *                            from the Target Node If the value is FALSE, the
	 *                            server delete only the specified Reference.
	 * @param SkipLogging         If the value is TRUE, the logging mechanism is
	 *                            skipped.<br>
	 *                            If the value is FALSE, the logging mechanism is
	 *                            used.
	 * @param AsyncOperation      If the value is TRUE, the Service will send
	 *                            asynchronous over the stack.<br>
	 *                            If the value is FALSE, the Service will send
	 *                            synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link DeleteReferencesResponse} . TRUE if the {@link StatusCode} was
	 *         GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public DeleteReferencesResponse deleteReferences(ClientSession session, NodeId[] sourceNodeIds,
			ExpandedNodeId[] targetNodeIds, NodeId[] referenceTypeIds, Boolean[] isForward,
			Boolean[] deleteBidirectional, boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		DeleteReferencesItem[] referencesToDelete = new DeleteReferencesItem[sourceNodeIds.length];
		for (int i = 0; i < sourceNodeIds.length; i++) {
			referencesToDelete[i] = new DeleteReferencesItem();
			referencesToDelete[i].setDeleteBidirectional(deleteBidirectional[i]);
			referencesToDelete[i].setIsForward(isForward[i]);
			referencesToDelete[i].setReferenceTypeId(referenceTypeIds[i]);
			referencesToDelete[i].setSourceNodeId(sourceNodeIds[i]);
			referencesToDelete[i].setTargetNodeId(targetNodeIds[i]);
		}
		DeleteReferencesRequest request = new DeleteReferencesRequest();
		request.setReferencesToDelete(referencesToDelete);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		DeleteReferencesResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.deleteReferences(tmpSession, request);
		} else {
			response = this.nodeManager.beginDeleteReferences(tmpSession, request);
		}
		return response;
	}

	/**
	 * This Service is invoked to delete one or more Subscriptions that has created.
	 * 
	 * @param Session         Session to use.
	 * @param SubscriptionIds NodeId of the server-assigned identifier for the
	 *                        Subscription.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link DeleteSubscriptionsResponse} . TRUE if the {@link StatusCode}
	 *         was GOOD, othwise FALSE.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @throws ServiceResultException
	 */
	public StatusCode[] deleteSubscriptions(ClientSession session, UnsignedInteger[] subscriptionIds,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		DeleteSubscriptionsRequest request = new DeleteSubscriptionsRequest();
		request.setSubscriptionIds(subscriptionIds);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (subscriptionIds == null || subscriptionIds.length == 0) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_NoSubscription,
					"Invalid parameter for subscriptions to remove!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ZEROONE,
					new String[] { RequestType.DeleteSubscriptions.name(), sre.getAdditionalTextField() });
			throw sre;
		}
		DeleteSubscriptionsResponse response;
		if (!asyncOperation) {
			response = this.subscriptionManager.deleteSubscription(tmpSession, request, true);
		} else {
			response = this.subscriptionManager.beginDeleteSubscription(tmpSession, request, true);
		}
		return response.getResults();
	}

	/**
	 * This Service is used to get all EndpointDescriptions from the Server, without
	 * using the Discovery Service.
	 * 
	 * @param ServerUri                            ServerUri to discover.
	 * @param ClientApplicationConfiguration       Configuration.
	 * @param ClientApplicationInstanceCertificate Certificate.
	 * @param SkipLogging                          If the value is TRUE, the logging
	 *                                             mechanism is skipped.<br>
	 *                                             If the value is FALSE, the
	 *                                             logging mechanism is used.
	 * @param AsyncOperation                       If the value is TRUE, the Service
	 *                                             will send asynchronous over the
	 *                                             stack.<br>
	 *                                             If the value is FALSE, the
	 *                                             Service will send synchronous
	 *                                             over the stack.
	 * @return discovered EndpointDescriptions.
	 * 
	 * @throws ServiceResultException
	 */
	public EndpointDescription[] getServerEndpoints(String serverUri,
			ApplicationConfiguration clientApplicationConfiguration, boolean asyncOperation)
			throws ServiceResultException {
		return getServerEndpoints(serverUri, clientApplicationConfiguration, asyncOperation, true);
	}

	/**
	 * This Service is used to get all EndpointDescriptions from the Server, without
	 * using the Discovery Service.
	 * 
	 * @param ServerUri                            ServerUri to discover.
	 * @param ClientApplicationConfiguration       Configuration.
	 * @param ClientApplicationInstanceCertificate Certificate.
	 * @param SkipLogging                          If the value is TRUE, the logging
	 *                                             mechanism is skipped.<br>
	 *                                             If the value is FALSE, the
	 *                                             logging mechanism is used.
	 * @param AsyncOperation                       If the value is TRUE, the Service
	 *                                             will send asynchronous over the
	 *                                             stack.<br>
	 *                                             If the value is FALSE, the
	 *                                             Service will send synchronous
	 *                                             over the stack.
	 * @return discovered EndpointDescriptions.
	 * 
	 * @throws ServiceResultException
	 */
	public EndpointDescription[] getServerEndpoints(String serverUri,
			ApplicationConfiguration clientApplicationConfiguration, boolean asyncOperation, boolean validateEndpoints)
			throws ServiceResultException {
		return this.sessionManager.getEndpoints(serverUri, clientApplicationConfiguration, asyncOperation,
				validateEndpoints);
	}

	/**
	 * Creates an history archive to read/update/delete historical values.
	 * 
	 * @param Session Session to use.
	 * @return HistoryArchive to send history requests.
	 * 
	 * @throws ServiceResultException
	 */
	public HistoryArchive historyArchive(ClientSession session) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		return new HistoryArchive(tmpSession);
	}

	/**
	 * Sending an history read service. (Same like the read in a HistoryArchive)
	 * 
	 * @param Session             Session to use.
	 * @param NodeIds             Ids from the nodes to read.
	 * @param ContinuationPoint   Historical continuation point to continue.
	 * @param IndexRanges         IndexRange of the value e.g.: ("1:2").
	 * @param DataEncodings       Qualified data encoding.
	 * @param ReleaseContinuation Weather to release the continuation point to
	 *                            continue.
	 * @param TimestampToReturn   Timestamps to return in the values.
	 * @param Details             History read details.
	 * @param SkipLogging         If the value is TRUE, the logging mechanism is
	 *                            skipped.<br>
	 *                            If the value is FALSE, the logging mechanism is
	 *                            used.
	 * @param AsyncOperation      If the value is TRUE, the Service will send
	 *                            asynchronous over the stack.<br>
	 *                            If the value is FALSE, the Service will send
	 *                            synchronous over the stack.
	 * @return history read results.
	 * @throws ServiceResultException
	 */
	public HistoryReadResult[] historyRead(ClientSession session, NodeId[] nodeIds, byte[][] continuationPoint,
			String[] indexRanges, QualifiedName[] dataEncodings, boolean releaseContinuation,
			TimestampsToReturn timestampToReturn, HistoryReadDetails details, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		HistoryReadResponse response;
		HistoryReadRequest request = new HistoryReadRequest();
		HistoryReadValueId[] attributesToRead = new HistoryReadValueId[nodeIds.length];
		try {
			for (int i = 0; i < nodeIds.length; i++) {
				HistoryReadValueId attributeToRead = new HistoryReadValueId();
				attributeToRead.setNodeId(nodeIds[i]);
				if (indexRanges != null) {
					attributeToRead.setIndexRange(indexRanges[i]);
					attributeToRead.setParsedIndexRange(NumericRange.parse(indexRanges[i]));
				}
				if (dataEncodings != null) {
					attributeToRead.setDataEncoding(dataEncodings[i]);
				}
				if (continuationPoint != null) {
					attributeToRead.setContinuationPoint(ByteString.valueOf(continuationPoint[i]));
				}
				attributesToRead[i] = attributeToRead;
			}
			request.setNodesToRead(attributesToRead);
			request.setTimestampsToReturn(timestampToReturn);
			request.setReleaseContinuationPoints(releaseContinuation);
			request.setHistoryReadDetails(ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()));
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (!asyncOperation) {
			response = this.nodeManager.historyRead(tmpSession, request);
		} else {
			response = this.nodeManager.beginHistoryRead(tmpSession, request);
		}
		return response.getResults();
	}

	/**
	 * Sending an history update service. (Same like the updates in a
	 * HistoryArchive)
	 * 
	 * @param Session        Session to use.
	 * @param NodeIds        Ids from the nodes to update.
	 * @param Details        Update details.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return history update results.
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult[] historyUpdate(ClientSession session, NodeId[] nodeIds, HistoryUpdateDetails[] details,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		HistoryUpdateResponse response;
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		ExtensionObject[] attributesToUpdate = new ExtensionObject[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			try {
				attributesToUpdate[i] = ExtensionObject.binaryEncode(details[i], EncoderContext.getDefaultInstance());
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
		request.setHistoryUpdateDetails(attributesToUpdate);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (!asyncOperation) {
			response = this.nodeManager.historyUpdate(tmpSession, request);
		} else {
			response = this.nodeManager.beginHistoryUpdate(tmpSession, request);
		}
		return response.getResults();
	}

	/**
	 * This Service is used to modify MonitoredItems of a Subscription.
	 * 
	 * @param Session           Session to use.
	 * @param SubscriptionId    The Server-assigned identifier for the Subscription
	 *                          that will report Notifications for this
	 *                          MonitoredItem.
	 * @param MonitoredItemIds  Server-assigned Id for the MonitoredItem;
	 * @param ClientHandles     Client-supplied id of the MonitoredItem.
	 * @param DiscardOldest     If the value is TRUE, the oldest (first)
	 *                          Notification in the queue is discarded. The new
	 *                          Notification is added to the end of the queue. If
	 *                          the value is FALSE, the new Notification is
	 *                          discarded. The queue is unchanged.
	 * @param Filter            A requested monitoring parameter. A filter used by
	 *                          the Server to determine if the MonitoredItem should
	 *                          generate a Notification. If not used, this parameter
	 *                          is null.
	 * @param QueueSize         A requested monitoring parameter. The requested size
	 *                          of the MonitoredItem queue.
	 * @param SamplingInterval  A requested monitoring parameter. The interval that
	 *                          defines the fastest rate at which the
	 *                          MonitoredItem(s) should be accessed and evaluated.
	 *                          This interval is defined in milliseconds.
	 * @param TimestampToReturn The timestamp Attributes to be transmitted for each
	 *                          MonitoredItem.
	 * @param AsyncOperation    If the value is TRUE, the Service will send
	 *                          asynchronous over the stack.<br>
	 *                          If the value is FALSE, the Service will send
	 *                          synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link ModifyMonitoredItemsResponse} . TRUE if the {@link StatusCode}
	 *         was GOOD, othwise FALSE.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @throws ServiceResultException
	 */
	public MonitoredItemModifyResult[] modifyMonitoredItems(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger[] monitoredItemIds, UnsignedInteger[] clientHandles, Boolean[] discardOldest,
			ExtensionObject[] filter, UnsignedInteger[] queueSize, Double[] samplingInterval,
			TimestampsToReturn timestampToReturn, boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		MonitoredItemModifyRequest[] itemsToModify = new MonitoredItemModifyRequest[monitoredItemIds.length];
		for (int ii = 0; ii < monitoredItemIds.length; ii++) {
			MonitoredItemModifyRequest itemToModify = new MonitoredItemModifyRequest();
			MonitoringParameters parameters = new MonitoringParameters();
			parameters.setClientHandle(clientHandles[ii]);
			parameters.setDiscardOldest(discardOldest[ii]);
			parameters.setFilter(filter[ii]);
			parameters.setQueueSize(queueSize[ii]);
			parameters.setSamplingInterval(samplingInterval[ii]);
			itemToModify.setRequestedParameters(parameters);
			itemToModify.setMonitoredItemId(monitoredItemIds[ii]);
			itemsToModify[ii] = itemToModify;
		}
		ModifyMonitoredItemsRequest request = new ModifyMonitoredItemsRequest();
		request.setSubscriptionId(subscriptionId);
		request.setTimestampsToReturn(timestampToReturn);
		request.setItemsToModify(itemsToModify);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		ModifyMonitoredItemsResponse response;
		if (!asyncOperation) {
			response = this.subscriptionManager.modifyMonitoredItems(tmpSession, request);
		} else {
			response = this.subscriptionManager.beginModifyMonitoredItems(tmpSession, request);
		}
		return response.getResults();
	}

	/**
	 * This Service is used to modify a Subscription.
	 * 
	 * @param Session                     Session to use.
	 * @param SubscriptionId              Server-assigned identifier for the
	 *                                    Subscription
	 * @param RequestedPublishingInterval This interval defines the cyclic rate that
	 *                                    the Subscription is being requested to
	 *                                    return Notifications to the Client. This
	 *                                    interval is expressed in milliseconds.
	 * @param MaxNotificationsPerPublish  The maximum number of notifications that
	 *                                    the Client wishes to receive in a single
	 *                                    Publish response. A value of 0 indicates
	 *                                    that there is no limit.
	 * @param RequestedMaxKeepAliveCount  When the publishing timer has expired this
	 *                                    number of times without requiring any
	 *                                    NotificationMessage to be sent, the
	 *                                    Subscription sends a keep-alive Message to
	 *                                    the Client.
	 * @param Priority                    Indicates the relative priority of the
	 *                                    Subscription. When more than one
	 *                                    Subscription needs to send Notifications,
	 *                                    the Server should dequeue a Publish
	 *                                    request to the Subscription with the
	 *                                    highest priority number.
	 * @param RequestedLifetimeCount      The lifetime count shall be a mimimum of
	 *                                    three times the keep keep-alive count.
	 *                                    When the publishing timer has expired this
	 *                                    number of times without a Publish request
	 *                                    being available to send a
	 *                                    NotificationMessage, then the Subscription
	 *                                    shall be deleted by the Server.
	 * @param asyncOperation              If the value is TRUE, the Service will
	 *                                    send asynchronous over the stack.<br>
	 *                                    If the value is FALSE, the Service will
	 *                                    send synchronous over the stack.
	 * @return Boolean from the StatusCode of the
	 *         {@link ModifySubscriptionResponse}. TRUE if the {@link StatusCode}
	 *         was GOOD, othwise FALSE.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return Response of the service.
	 * @throws ServiceResultException
	 */
	public ModifySubscriptionResponse modifySubscription(ClientSession session, UnsignedInteger subscriptionId,
			Double requestedPublishingInterval, UnsignedInteger maxNotificationsPerPublish,
			UnsignedInteger requestedMaxKeepAliveCount, UnsignedByte priority, UnsignedInteger requestedLifetimeCount,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		ModifySubscriptionRequest request = new ModifySubscriptionRequest();
		request.setSubscriptionId(subscriptionId);
		request.setRequestedPublishingInterval(requestedPublishingInterval);
		request.setRequestedMaxKeepAliveCount(requestedMaxKeepAliveCount);
		request.setRequestedLifetimeCount(requestedLifetimeCount);
		request.setRequestedMaxKeepAliveCount(requestedMaxKeepAliveCount);
		request.setPriority(priority);
		request.setMaxNotificationsPerPublish(maxNotificationsPerPublish);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (requestedPublishingInterval <= 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Changed publishing interval to 1000ms!",
					new String[] { RequestType.ModifySubscription.name() });
			request.setRequestedPublishingInterval(1000.0);
		}
		if (requestedLifetimeCount.longValue() <= requestedMaxKeepAliveCount.longValue() * 3) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} Invalid lifetime count for subscription {1}, changed to 3x keepalive count {2}!",
					new Object[] { RequestType.ModifySubscription.name(), request.getSubscriptionId(),
							Long.toString(requestedMaxKeepAliveCount.longValue() * 3) });
			request.setRequestedLifetimeCount(new UnsignedInteger(requestedMaxKeepAliveCount.longValue() * 3));
		}
		ModifySubscriptionResponse response;
		if (!asyncOperation) {
			response = this.subscriptionManager.modifySubscriptions(tmpSession, request);
		} else {
			response = this.subscriptionManager.beginModifySubscription(tmpSession, request);
		}
		return response;
	}

	/**
	 * This Service is used to read one or more Attributes of one or more Nodes.
	 * 
	 * @param Session           Session to use.
	 * @param NodeId            NodeId of the Node to read.
	 * @param AttributeId       Id of the Attribute to read from the Node.
	 * @param IndexRange        IndexRange of the value e.g.: ("1:2")
	 * @param DataEncoding      Qualified data encoding
	 * @param MaxAge            The maxAge parameter is used to direct the Server to
	 *                          access the value from the underlying data source,
	 *                          such as a device, if its copy of the data is older
	 *                          than that which the maxAge specifies.
	 * @param TimestampToReturn Timestamp to be returned for each requested Variable
	 *                          Value Attribute.
	 * @param SkipLogging       If the value is TRUE, the logging mechanism is
	 *                          skipped.<br>
	 *                          If the value is FALSE, the logging mechanism is
	 *                          used.
	 * @param AsyncOperation    If the value is TRUE, the Service will send
	 *                          asynchronous over the stack.<br>
	 *                          If the value is FALSE, the Service will send
	 *                          synchronous over the stack.
	 * @return DataValues from the {@link ReadResponse}.
	 * @throws ServiceResultException
	 */
	public DataValue[] read1(ClientSession session, NodeId[] nodeIds, UnsignedInteger[] attributeIds,
			String[] indexRanges, QualifiedName[] dataEncodings, Double maxAge, TimestampsToReturn timestampToReturn,
			boolean asyncOperation) throws ServiceResultException {
		ReadResponse response = read2(session, nodeIds, attributeIds, indexRanges, dataEncodings, maxAge,
				timestampToReturn, asyncOperation);
		if (response == null) {
			return new DataValue[0];
		}
		return response.getResults();
	}

	/**
	 * This Service is used to read one or more Attributes of one or more Nodes.
	 * 
	 * @param NodeId            NodeId of the Node to read.
	 * @param AttributeId       Id of the Attribute to read from the Node.
	 * @param IndexRange        IndexRange of the value e.g.: ("1:2")
	 * @param DataEncoding      Qualified data encoding
	 * @param MaxAge            The maxAge parameter is used to direct the Server to
	 *                          access the value from the underlying data source,
	 *                          such as a device, if its copy of the data is older
	 *                          than that which the maxAge specifies.
	 * @param TimestampToReturn Timestamp to be returned for each requested Variable
	 *                          Value Attribute.
	 * @param SkipLogging       If the value is TRUE, the logging mechanism is
	 *                          skipped.<br>
	 *                          If the value is FALSE, the logging mechanism is
	 *                          used.
	 * @param AsyncOperation    If the value is TRUE, the Service will send
	 *                          asynchronous over the stack.<br>
	 *                          If the value is FALSE, the Service will send
	 *                          synchronous over the stack.
	 * @return DataValues from the {@link ReadResponse}.
	 * @throws ServiceResultException
	 */
	public ReadResponse read2(ClientSession session, NodeId[] nodeIds, UnsignedInteger[] attributeIds,
			String[] indexRanges, QualifiedName[] dataEncodings, Double maxAge, TimestampsToReturn timestampToReturn,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		if (maxAge < 0 || maxAge > Integer.MAX_VALUE) {
			throw new ServiceResultException(StatusCodes.Bad_MaxAgeInvalid);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		// max monitored item per create should not be 0
		if (tmpSession.getMaxRead() <= 0)
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		ReadValueId[] attributesToRead;
		// only read max packages if it is limited
		ReadResponse response = null;
		if (nodeIds.length < tmpSession.getMaxRead()) {
			ReadRequest request = new ReadRequest();
			request.setMaxAge(maxAge);
			request.setTimestampsToReturn(timestampToReturn);
			attributesToRead = this.nodeManager.validateReadRequest(nodeIds, attributeIds, indexRanges, dataEncodings);
			request.setNodesToRead(attributesToRead);
			if (!asyncOperation) {
				response = this.nodeManager.read(tmpSession, request, null);
			} else {
				response = this.nodeManager.beginRead(tmpSession, request, null);
			}
		} else {
			List<NodeId> nids = new ArrayList<>();
			List<UnsignedInteger> aids = new ArrayList<>();
			List<String> iranges = new ArrayList<>();
			List<QualifiedName> encodings = new ArrayList<>();
			// now repack elements
			int count = 0;
			for (NodeId id : nodeIds) {
				nids.add(nodeIds[count]);
				aids.add(attributeIds[count]);
				iranges.add(indexRanges[count]);
				encodings.add(dataEncodings[count]);
				count++;
				if (count % tmpSession.getMaxRead() == 0 || count == nodeIds.length) {
					ReadRequest request = new ReadRequest();
					ReadResponse tmpReponse = null;
					request.setMaxAge(maxAge);
					request.setTimestampsToReturn(timestampToReturn);
					attributesToRead = this.nodeManager.validateReadRequest(nids.toArray(new NodeId[0]),
							aids.toArray(new UnsignedInteger[0]), iranges.toArray(new String[0]),
							encodings.toArray(new QualifiedName[0]));
					request.setNodesToRead(attributesToRead);
					if (!asyncOperation) {
						tmpReponse = this.nodeManager.read(tmpSession, request, null);
					} else {
						tmpReponse = this.nodeManager.beginRead(tmpSession, request, null);
					}
					if (response == null)
						response = tmpReponse;
					else {
						List<DataValue> dvs = new ArrayList<>();
						dvs.addAll(Arrays.asList(response.getResults()));
						dvs.addAll(Arrays.asList(tmpReponse.getResults()));
						response.setResults(dvs.toArray(new DataValue[0]));
					}
					nids.clear();
					aids.clear();
					iranges.clear();
					encodings.clear();
				}
			}
		}
		return response;
	}

	/**
	 * This Service is used to read one or more Attributes of one or more Nodes.
	 * 
	 * @param Session              Session to use.
	 * @param NodeIds              NodeIds from the Nodes to read.
	 * @param MaxAge               The maxAge parameter is used to direct the Server
	 *                             to access the value from the underlying data
	 *                             source, such as a device, if its copy of the data
	 *                             is older than that which the maxAge specifies.
	 * @param TimestampToReturn    Timestamp to be returned for each requested
	 *                             Variable Value Attribute.
	 * @param EnableValueChange    Change the value with its underlying one. (e.g.
	 *                             TwoStateItem - int 0 for the 0 indexed enum
	 *                             value)
	 * @param ValidEURange         TRUE only if valid EURange.
	 * @param ValidInstrumentRange TRUE only if valid InstrumentRange
	 * @param ValidEUInformation   TRUE only if valid EUInformation.
	 * @param SkipLogging          If the value is TRUE, the logging mechanism is
	 *                             skipped.<br>
	 *                             If the value is FALSE, the logging mechanism is
	 *                             used.
	 * @param AsyncOperation       If the value is TRUE, the Service will send
	 *                             asynchronous over the stack.<br>
	 *                             If the value is FALSE, the Service will send
	 *                             synchronous over the stack.
	 * @return Response of the service.
	 * @throws ServiceResultException
	 */
	public ReadResponse readDA(ClientSession session, NodeId[] nodeIds, double maxAge,
			TimestampsToReturn timestampToReturn, boolean enableValueChange, boolean validEURange,
			boolean validInstrumentRange, boolean validEUInformation, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (!verifySession(tmpSession)) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.Read.name(),
					"Verify the session failed!");
			throw sre;
		}
		if (maxAge < 0 || maxAge > Integer.MAX_VALUE || ((Double) maxAge).compareTo(Double.NaN) == 0) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_MaxAgeInvalid);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} Defined maxage invalid!",
					new String[] { RequestType.Read.name() });
			throw sre;
		}
		ReadValueId[] attributesToRead;
		ReadRequest request = new ReadRequest();
		request.setMaxAge(maxAge);
		request.setTimestampsToReturn(timestampToReturn);
		attributesToRead = this.nodeManager.validateReadRequest(nodeIds, null, null, null);
		request.setNodesToRead(attributesToRead);
		ReadResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.readDA(tmpSession, validEURange, validInstrumentRange, validEUInformation,
					enableValueChange, request);
		} else {
			response = this.nodeManager.beginReadDA(tmpSession, validEURange, validInstrumentRange, validEUInformation,
					enableValueChange, request);
		}
		return response;
	}

	/**
	 * Reads a UA Node with its attributes from the server.
	 * 
	 * @param Session        Session to use.
	 * @param NodeIds        Ids from the nodes.
	 * @param NodeClasses    Type of the nodes.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return ua nodes
	 * @throws ServiceResultException
	 */
	public Node[] readNodes(ClientSession session, NodeId[] nodeIds, NodeClass[] nodeClasses, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		return this.nodeManager.readNodes(tmpSession, nodeIds, nodeClasses, asyncOperation);
	}

	/**
	 * Force to reconnect a session to its server.
	 * 
	 * @param Session Forced session to reconnect.
	 * @throws ServiceResultException
	 */
	public void reconnect(ClientSession session) throws ServiceResultException {
		this.sessionManager.reconnect(session);
	}

	/**
	 * The RegisterNodes Service can be used by Clients to register the Nodes that
	 * they know they will access repeatedly (e.g. Write, Call). It allows Servers
	 * to set up anything needed so that the access operations will be more
	 * efficient.
	 * 
	 * @param NodesToRegister NodeId to register that the client has retrieved
	 *                        through browsing, querying or in some other manner.
	 * @param SkipLogging     If the value is TRUE, the logging mechanism is
	 *                        skipped.<br>
	 *                        If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation  If the value is TRUE, the Service will send
	 *                        asynchronous over the stack.<br>
	 *                        If the value is FALSE, the Service will send
	 *                        synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link RegisterNodesResponse} . TRUE if the {@link StatusCode} was
	 *         GOOD, othwise FALSE.
	 * 
	 * @throws ServiceResultException
	 */
	public RegisterNodesResponse registerNodes(ClientSession session, NodeId[] nodesToRegister, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		RegisterNodesRequest request = new RegisterNodesRequest();
		request.setNodesToRegister(nodesToRegister);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (nodesToRegister == null || nodesToRegister.length == 0) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_NodeIdInvalid,
					"Invalid parameter for nodes to register!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ZEROONE,
					new String[] { RequestType.RegisterNodes.name(), sre.getAdditionalTextField() });
			throw sre;
		}
		RegisterNodesResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.registerNodes(tmpSession, request);
		} else {
			response = this.nodeManager.beginRegisterNodes(tmpSession, request);
		}
		return response;
	}

	/**
	 * This Service is used to set the monitoring mode for one or more
	 * MonitoredItems of a Subscription.
	 * 
	 * @param Session
	 * @param SubscriptionId   The Server-assigned identifier for the Subscription
	 *                         used to qualify the monitoredItemIds
	 * @param MonitoredItemIds Server-assigned Id for the MonitoredItem.
	 * @param MonitoringMode   The monitoring mode to be set for the MonitoredItems.
	 * @param SkipLogging      If the value is TRUE, the logging mechanism is
	 *                         skipped.<br>
	 *                         If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation   If the value is TRUE, the Service will send
	 *                         asynchronous over the stack.<br>
	 *                         If the value is FALSE, the Service will send
	 *                         synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link SetMonitoringModeResponse} . TRUE if the {@link StatusCode}
	 *         was GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public StatusCode[] setMonitoringMode(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger[] monitoredItemIds, MonitoringMode monitoringMode, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		SetMonitoringModeRequest request = new SetMonitoringModeRequest();
		request.setSubscriptionId(subscriptionId);
		request.setMonitoredItemIds(monitoredItemIds);
		request.setMonitoringMode(monitoringMode);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		SetMonitoringModeResponse response;
		if (!asyncOperation) {
			response = this.subscriptionManager.setMonitoringMode(tmpSession, request);
		} else {
			response = this.subscriptionManager.beginSetMonitoringMode(tmpSession, request);
		}
		return response.getResults();
	}

	/**
	 * This Service is used to enable sending of Notifications on one or more
	 * Subscriptions.
	 * 
	 * @param Session           Session to use.
	 * @param SubscriptionIds   Server-assigned Ids for the Subscription to enable
	 *                          or disable.
	 * @param PublishingEnabled If the value is TRUE, publishing of
	 *                          NotificationMessages is enabled for the
	 *                          Subscription. If the value is FALSE, publishing of
	 *                          NotificationMessages is disabled for the
	 *                          Subscription.
	 * @param SkipLogging       If the value is TRUE, the logging mechanism is
	 *                          skipped.<br>
	 *                          If the value is FALSE, the logging mechanism is
	 *                          used.
	 * @param AsyncOperation    If the value is TRUE, the Service will send
	 *                          asynchronous over the stack.<br>
	 *                          If the value is FALSE, the Service will send
	 *                          synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link SetPublishingModeResponse} . TRUE if the {@link StatusCode}
	 *         was GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public StatusCode[] setPublishingMode(ClientSession session, UnsignedInteger[] subscriptionIds,
			Boolean publishingEnabled, boolean asynchOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		SetPublishingModeRequest request = new SetPublishingModeRequest();
		request.setSubscriptionIds(subscriptionIds);
		request.setPublishingEnabled(publishingEnabled);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (subscriptionIds == null || subscriptionIds.length == 0 || UnsignedInteger.ZERO.equals(subscriptionIds[0])) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid,
					"Invalid id for subscription!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ZEROONE,
					new String[] { RequestType.SetPublishingMode.name(), sre.getAdditionalTextField() });
			throw sre;
		}
		SetPublishingModeResponse response;
		if (!asynchOperation) {
			response = this.subscriptionManager.setPublishingMode(tmpSession, request);
		} else {
			response = this.subscriptionManager.beginSetPublishing(tmpSession, request);
		}
		return response.getResults();
	}

	public StatusCode[] setTriggering(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger triggeringItemId, UnsignedInteger[] linksToAdd, UnsignedInteger[] linksToRemove,
			boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		SetTriggeringRequest request = new SetTriggeringRequest();
		request.setSubscriptionId(subscriptionId);
		request.setTriggeringItemId(triggeringItemId);
		request.setLinksToAdd(linksToAdd);
		request.setLinksToRemove(linksToRemove);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		SetTriggeringResponse response;
		if (!asyncOperation) {
			response = this.subscriptionManager.setTriggering(tmpSession, request);
		} else {
			response = this.subscriptionManager.beginSetTriggering(tmpSession, request);
		}
		return response.getAddResults();
	}

	/**
	 * This Service is used to transfer a Subscription and its MonitoredItems from
	 * one Session to another.
	 * 
	 * @param Session           Session to use.
	 * @param SubscriptionIds   Id for the Subscription to be transferred to the new
	 *                          Client.
	 * @param SendInitialValues If the value is TRUE the first Publish response
	 *                          after the TransferSubscriptions call shall contain
	 *                          the current values of all Monitored Items in the
	 *                          Subscription where the Monitoring Mode is set to
	 *                          Reporting. If the value is FALSE, the first Publish
	 *                          response after the TransferSubscriptions call shall
	 *                          contain only the value changes since the last
	 *                          Publish response was sent.
	 * @param SkipLogging       If the value is TRUE, the logging mechanism is
	 *                          skipped.<br>
	 *                          If the value is FALSE, the logging mechanism is
	 *                          used.
	 * @param AsyncOperation    If the value is TRUE, the Service will send
	 *                          asynchronous over the stack.<br>
	 *                          If the value is FALSE, the Service will send
	 *                          synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link TransferSubscriptionsResponse} . TRUE if the
	 *         {@link StatusCode} was GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public TransferResult[] transfereSubscription(ClientSession session, NodeId targetSessionId,
			UnsignedInteger[] subscriptionIds, Boolean sendInitialValues, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		TransferSubscriptionsRequest request = new TransferSubscriptionsRequest();
		request.setSendInitialValues(sendInitialValues);
		request.setSubscriptionIds(subscriptionIds);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		TransferSubscriptionsResponse response;
		if (!asyncOperation) {
			response = this.subscriptionManager.transfereSubscriptions(tmpSession, request, targetSessionId);
		} else {
			response = this.subscriptionManager.beginTransferSubscriptions(tmpSession, request, targetSessionId);
		}
		return response.getResults();
	}

	/**
	 * This Service is used to request that the Server translates one or more browse
	 * paths to NodeIds.
	 * 
	 * @param Session        Session to use.
	 * @param IsInverse      Direction to browse.
	 * @param StartNodeId    NodeId of the starting Node for the browse path.
	 * @param RelativePaths  The path to follow from the startingNode. The last
	 *                       element in the relativePath shall always have a
	 *                       targetName specified.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return ExpandedNodeIds from the Translated-Browse-Paths of the
	 *         {@link TranslateBrowsePathsToNodeIdsResponse}.
	 * @throws ServiceResultException
	 */
	public BrowsePathResult[] translateBrowsePathToNodeIds(ClientSession session, NodeId[] startNodeId,
			QualifiedName[][] relativePaths, Boolean[] isInverse, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		// build the list of browse paths to follow by parsing the relative
		// paths.
		if ((relativePaths == null || relativePaths.length == 0) || (startNodeId == null || startNodeId.length == 0)) {
			return new BrowsePathResult[0];
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		BrowsePath[] browsePaths = new BrowsePath[startNodeId.length];
		for (int j = 0; j < startNodeId.length; j++) {
			BrowsePath browsePath = new BrowsePath();
			browsePath.setStartingNode(startNodeId[j]);
			browsePath.setRelativePath(Utils.parseRelativePath(relativePaths[j], tmpSession.getTypeTree(),
					tmpSession.getNamespaceUris(), tmpSession.getNamespaceUris(), isInverse[j]));
			browsePaths[j] = browsePath;
		}
		TranslateBrowsePathsToNodeIdsRequest request = new TranslateBrowsePathsToNodeIdsRequest();
		request.setBrowsePaths(browsePaths);
		TranslateBrowsePathsToNodeIdsResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.translateBrowsePathToNodeIds(tmpSession, request);
		} else {
			response = this.nodeManager.beginTranslateBrowsePathToNodeIds(tmpSession, request);
		}
		return response.getResults();
	}

	/**
	 * This Service is used to unregister NodeIds that have been obtained via the
	 * Register-Node Service.
	 * 
	 * @param Session        Session to use.
	 * @param NodeIds        NodeIds that have been obtained via the RegisterNode
	 *                       service.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the
	 *         {@link UnregisterNodesResponse} . TRUE if the {@link StatusCode} was
	 *         GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public Boolean[] unregisterNodes(ClientSession session, NodeId[] nodesToUnregister, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		UnregisterNodesRequest request = new UnregisterNodesRequest();
		request.setNodesToUnregister(nodesToUnregister);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		if (nodesToUnregister == null || nodesToUnregister.length == 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Invalid parameter for nodes to register!",
					new String[] { RequestType.UnregisterNodes.name() });
			throw new ServiceResultException(StatusCodes.Bad_NodeIdInvalid);
		}
		UnregisterNodesResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.unregisterNodes(tmpSession, request);
		} else {
			response = this.nodeManager.beginUnregisterNodes(tmpSession, request);
		}
		Boolean[] isUnregisterd = new Boolean[nodesToUnregister.length];
		for (int i = 0; i < nodesToUnregister.length; i++) {
			isUnregisterd[i] = response != null;
		}
		return isUnregisterd;
	}

	/**
	 * This Service is used to write values to one or more Attributes of one or more
	 * Nodes.
	 * 
	 * @param Session        Session to use.
	 * @param NodeToWrite    NodeId of the Node that contains the Attributes.
	 * @param AttributeId    Id of the attribute to write.
	 * @param IndexRange     This parameter is used to identify a single element of
	 *                       an array, or a single range of indexes for arrays. The
	 *                       first element is identified by index 0. NULL Parameter
	 *                       ignores a IndexBased-Write.
	 * @param Value          The Node�s Attribute value.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return Boolean Array from the StatusCodes of the {@link WriteResponse} .
	 *         TRUE if the {@link StatusCode} was GOOD, othwise FALSE.
	 * @throws ServiceResultException
	 */
	public StatusCode[] write1(ClientSession session, NodeId[] nodesToWrite, UnsignedInteger[] attributeId,
			String[] indexRange, DataValue[] value, boolean asyncOperation) throws ServiceResultException {
		if (!verifySession(session)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		WriteResponse response = write2(session, nodesToWrite, attributeId, indexRange, value, asyncOperation);
		return response.getResults();
	}

	/**
	 * This Service is used to write values to one or more Attributes of one or more
	 * Nodes.
	 * 
	 * @param Session        Session to use.
	 * @param NodeToWrite    NodeId of the Node that contains the Attributes.
	 * @param AttributeId    Id of the attribute to write.
	 * @param IndexRange     This parameter is used to identify a single element of
	 *                       an array, or a single range of indexes for arrays. The
	 *                       first element is identified by index 0. NULL Parameter
	 *                       ignores a IndexBased-Write.
	 * @param Value          The Node�s Attribute value.
	 * @param SkipLogging    If the value is TRUE, the logging mechanism is
	 *                       skipped.<br>
	 *                       If the value is FALSE, the logging mechanism is used.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return Response from the StatusCodes of the {@link WriteResponse} .
	 * @throws ServiceResultException
	 */
	public WriteResponse write2(ClientSession session, NodeId[] nodesToWrite, UnsignedInteger[] attributeId,
			String[] indexRange, DataValue[] value, boolean asyncOperation) throws ServiceResultException {
		ClientSession tmpSession = session;
		WriteResponse response = null;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		// max monitored item per create should not be 0
		if (tmpSession.getMaxNodesPerWrite() <= 0)
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		List<WriteValue> attributesToWrite = new ArrayList<>();
		if (tmpSession.getMaxNodesPerWrite() > nodesToWrite.length) {
			for (int ii = 0; ii < nodesToWrite.length; ii++) {
				WriteValue writeValue = new WriteValue();
				writeValue.setNodeId(nodesToWrite[ii]);
				writeValue.setAttributeId(attributeId[ii]);
				writeValue.setIndexRange(indexRange[ii]);
				writeValue.setValue(value[ii]);
				attributesToWrite.add(writeValue);
			}
			WriteRequest request = new WriteRequest();
			request.setNodesToWrite(attributesToWrite.toArray(new WriteValue[attributesToWrite.size()]));
			if (!asyncOperation) {
				response = this.nodeManager.write(tmpSession, request);
			} else {
				response = this.nodeManager.beginWrite(tmpSession, request);
			}
		} else {
			int count = 0;
			WriteResponse tmpResponse = null;
			for (NodeId nid : nodesToWrite) {
				WriteValue writeValue = new WriteValue();
				writeValue.setNodeId(nodesToWrite[count]);
				writeValue.setAttributeId(attributeId[count]);
				writeValue.setIndexRange(indexRange[count]);
				writeValue.setValue(value[count]);
				attributesToWrite.add(writeValue);
				count++;
				if (count % tmpSession.getMaxNodesPerWrite() == 0 || count == nodesToWrite.length) {
					WriteRequest request = new WriteRequest();
					request.setNodesToWrite(attributesToWrite.toArray(new WriteValue[attributesToWrite.size()]));
					if (!asyncOperation) {
						tmpResponse = this.nodeManager.write(tmpSession, request);
					} else {
						tmpResponse = this.nodeManager.beginWrite(tmpSession, request);
					}
					if (response == null)
						response = tmpResponse;
					else {
						List<StatusCode> status = new ArrayList<>();
						status.addAll(Arrays.asList(response.getResults()));
						status.addAll(Arrays.asList(tmpResponse.getResults()));
						response.setResults(status.toArray(new StatusCode[0]));
					}
					attributesToWrite.clear();
				}
			}
		}
		return response;
	}

	/**
	 * This Service is used to write values to one or more Attributes of one or more
	 * Nodes.
	 * 
	 * @param Session              Session to use.
	 * @param NodesToWrite         Id of the attribute to write. NodeId of the Node
	 *                             that contains the Attributes.
	 * @param Values               The Node�s Attribute value.
	 * @param ValidEURange         Only if valid EURange.
	 * @param ValidInstrumentRange Only if valid InsturmentRange.
	 * @param ValidEUInformation   Only if valid EUInformation.
	 * @param SkipLogging          If the value is TRUE, the logging mechanism is
	 *                             skipped.<br>
	 *                             If the value is FALSE, the logging mechanism is
	 *                             used.
	 * @param AsyncOperation       If the value is TRUE, the Service will send
	 *                             asynchronous over the stack.<br>
	 *                             If the value is FALSE, the Service will send
	 *                             synchronous over the stack.
	 * @return Response of the service.
	 * @throws ServiceResultException
	 */
	public WriteResponse writeDA(ClientSession session, NodeId[] nodesToWrite, DataValue[] values, boolean validEURange,
			boolean validInstrumentRange, boolean validEUInformation, boolean asyncOperation)
			throws ServiceResultException {
		ClientSession tmpSession = session;
		if (!verifySession(tmpSession)) {
			throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid, SESSSIONNOTALIVE);
		}
		WriteValue[] attributesToWrite = new WriteValue[nodesToWrite.length];
		for (int i = 0; i < nodesToWrite.length; i++) {
			WriteValue writeValue = new WriteValue();
			writeValue.setNodeId(nodesToWrite[i]);
			writeValue.setAttributeId(Attributes.Value);
			writeValue.setValue(values[i]);
			attributesToWrite[i] = writeValue;
		}
		WriteRequest request = new WriteRequest();
		request.setNodesToWrite(attributesToWrite);
		if (tmpSession == null) {
			tmpSession = this.sessionManager.getActiveSession();
		}
		WriteResponse response;
		if (!asyncOperation) {
			response = this.nodeManager.writeDA(tmpSession, validEURange, validInstrumentRange, validEUInformation,
					request);
		} else {
			response = this.nodeManager.beginWriteDA(tmpSession, validEURange, validInstrumentRange, validEUInformation,
					request);
		}
		return response;
	}

	/**
	 * Get the SessionManager.
	 * 
	 * @return {@link SessionManager} Client-Side Session-Manager.
	 */
	public SessionManager getSessionManager() {
		return this.sessionManager;
	}

	/**
	 * Get the SubscriptionManager.
	 * 
	 * @return {@link SubscriptionManager} Client-Side Subscription-Manager.
	 */
	public SubscriptionManager getSubscriptionManager() {
		return this.subscriptionManager;
	}

	/**
	 * Verifies a Session. <br>
	 * 1) Is the session alive.
	 * 
	 * @param Session Session to verify.
	 * @return TRUE if successful, otherwise FALSE.
	 * @throws ServiceResultException
	 */
	private boolean verifySession(ClientSession session) throws ServiceResultException {
		// session is null and there is no active session
		if (session == null && this.sessionManager.getActiveSession() != null) {
			return true;
		} else if (session == null && this.sessionManager.getActiveSession() == null) {
			throw new ServiceResultException(StatusCodes.Bad_UnexpectedError);
		} else if (session != null && !this.sessionManager.sessionExist(session)) {
			return false;
		}
		return true;
	}
}
