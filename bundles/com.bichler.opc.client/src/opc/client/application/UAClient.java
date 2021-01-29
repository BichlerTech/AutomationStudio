package opc.client.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.JDOMException;
import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.CancelResponse;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.DeleteNodesResponse;
import org.opcfoundation.ua.core.DeleteReferencesResponse;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.HistoryReadDetails;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryUpdateDetails;
import org.opcfoundation.ua.core.HistoryUpdateResult;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.TransferResult;
import org.opcfoundation.ua.core.ViewDescription;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.StackUtils;
import org.opcfoundation.ua.utils.TimerUtil;

import opc.client.application.core.ApplicationConfiguration;
import opc.client.service.ClientSession;
import opc.client.service.HistoryArchive;
import opc.client.service.MonitoredItem;
import opc.client.service.ProfileManager;
import opc.client.service.SessionManager;
import opc.client.service.Subscription;
import opc.client.service.SubscriptionManager;
import opc.client.util.AlarmConditionUtil;
import opc.sdk.core.application.AbstractApplication;
import opc.sdk.core.node.Node;
import opc.sdk.core.session.AllowNoneCertificateValidator;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.session.UserIdentityRole;
import opc.sdk.core.utils.DefaultSubscriptionSettings;

/**
 * The core instance to operate with an OPC UA - Server. This client instance is
 * used to create sessions on opc ua servers to send services.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public class UAClient implements AbstractApplication {
	public static final String ID = "opc.client.application.UAClient";
	/** default sdk name */
	public static final String OPC_CLIENT_SDK_NAME = "OPCUA BTech Client SDK";
	/** current sdk version */
	public static final String OPC_CLIENT_SDK_VERSION = "1.0.3";
	/** client configuration */
	private ApplicationConfiguration clientApplicationConfiguration = null;
	/** OPC UA profile manager */
	private ProfileManager profileManager = null;
	/** language locales */
	private List<Locale> locales = new ArrayList<>();
	/** sending OPC UA services in a synchronous or asynchronous way */
	private boolean asyncApi = false;
	private long futureServerTimeout = 0;
	private long defaultKeepAliveInterval;

	/**
	 * OPC UA Client instance sending OPC UA services to OPC UA server applications.
	 */
	public UAClient() {
		super();
	}

	/**
	 * Creates a OPC UA discovery client.
	 * 
	 * @return a DiscoveryClient to find OPC UA servers.
	 */
	public UADiscoveryClient createDiscoveryClient() {
		UADiscoveryClient uadc = new UADiscoveryClient();
		uadc.addLocales(locales.toArray(new Locale[0]));
		return uadc;
	}

	/**
	 * Creates a OPC UA discovery client.
	 * 
	 * @return a DiscoveryClient to find OPC UA servers.
	 */
	public UADiscoveryClient createDiscoveryClient(Application application) {
		UADiscoveryClient uadc = new UADiscoveryClient(application);
		uadc.addLocales(locales.toArray(new Locale[0]));
		return uadc;
	}

	/**
	 * Close application and remove all resources used within the application.
	 */
	public void close() {
		if (this.profileManager != null) {
			this.profileManager.dispose();
		}
	}

	/**
	 * Adds a language locale to the application.
	 * 
	 * @param Locale Application language locale.
	 */
	public void addLocale(Locale locale) {
		if (locale == null) {
			return;
		}
		locales.add(locale);
	}

	/**
	 * Processes an anonymous Active-Session Service.
	 * 
	 * This service is used by the client to submit its identity of the user
	 * associated with the Session and it shall be issued by the client before it
	 * issues any other Service request after a Create-Session Service.
	 * 
	 * OPC UA Clients can change the identity of a user associated with a session by
	 * calling the Activate-Session Service (impersonate).
	 * 
	 * @param Session          Created session to activate or impersonate.
	 * @param PreferredLocales Preferred language locales to use.
	 * @return Response of the service.
	 * 
	 * @throws ServiceResultException
	 */
	public ActivateSessionResponse activateSession(ClientSession session, String[] preferredLocales)
			throws ServiceResultException {
		UserIdentity identity = new UserIdentityRole();
		ActivateSessionResponse response = this.profileManager.activateSession(session, identity, preferredLocales,
				this.asyncApi);
		if (response != null && response.getResponseHeader() != null
				&& StatusCode.GOOD.equals(response.getResponseHeader().getServiceResult())) {
			// try to read limits from server
			this.readServerLimits(session);
		}
		return response;
	}

	/**
	 * read all server limits
	 * 
	 * @param session
	 * @throws ServiceResultException
	 */
	public void readServerLimits(ClientSession session) throws ServiceResultException {
		if (session.isReadServerLimits()) {
			DataValue[] dv = read1(session, new NodeId[] {
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerRead,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite,
					Identifiers.Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall },
					new UnsignedInteger[] { Attributes.Value, Attributes.Value, Attributes.Value, Attributes.Value,
							Attributes.Value, Attributes.Value, Attributes.Value, Attributes.Value, Attributes.Value,
							Attributes.Value, Attributes.Value, Attributes.Value },
					new String[] { null, null, null, null, null, null, null, null, null, null, null, null },
					new QualifiedName[] { null, null, null, null, null, null, null, null, null, null, null, null }, 0.0,
					TimestampsToReturn.Both);
			if (dv != null && dv.length >= 12) {
				// validate browse limits
				if (dv[0] != null && StatusCode.GOOD.equals(dv[0].getStatusCode()) && dv[0].getValue() != null
						&& dv[0].getValue().getValue() != null && dv[0].getValue().longValue() > 0) {
					session.setMaxNodesPerBrowse(dv[0].getValue().longValue());
				} else {
					// set default value
					session.setMaxNodesPerBrowse(session.getMaxNodesPerBrowseDefault());
				}
				// validate history read data limits
				if (dv[1] != null && StatusCode.GOOD.equals(dv[1].getStatusCode()) && dv[1].getValue() != null
						&& dv[1].getValue().getValue() != null && dv[1].getValue().longValue() > 0) {
					// set default value
					session.setMaxHistoryReadData(dv[1].getValue().longValue());
				} else {
					session.setMaxHistoryReadData(session.getMaxHistoryReadDataDefault());
				}
				// validate history read event limits
				if (dv[2] != null && StatusCode.GOOD.equals(dv[2].getStatusCode()) && dv[2].getValue() != null
						&& dv[2].getValue().getValue() != null && dv[2].getValue().longValue() > 0) {
					// set default value
					session.setMaxHistoryReadEvent(dv[2].getValue().longValue());
				} else {
					session.setMaxHistoryReadEvent(session.getMaxHistoryReadEventDefault());
				}
				// validate history update data limits
				if (dv[3] != null && StatusCode.GOOD.equals(dv[3].getStatusCode()) && dv[3].getValue() != null
						&& dv[3].getValue().getValue() != null && dv[3].getValue().longValue() > 0) {
					// set default value
					session.setMaxHistoryUpdateData(dv[3].getValue().longValue());
				} else {
					session.setMaxHistoryUpdateData(session.getMaxHistoryUpdateDataDefault());
				}
				// validate history update event limits
				if (dv[4] != null && StatusCode.GOOD.equals(dv[4].getStatusCode()) && dv[4].getValue() != null
						&& dv[4].getValue().getValue() != null && dv[4].getValue().longValue() > 0) {
					// set default value
					session.setMaxHistoryUpdateEvent(dv[4].getValue().longValue());
				} else {
					session.setMaxHistoryUpdateEvent(session.getMaxHistoryUpdateEventDefault());
				}
				// validate method call limits
				if (dv[5] != null && StatusCode.GOOD.equals(dv[5].getStatusCode()) && dv[5].getValue() != null
						&& dv[5].getValue().getValue() != null && dv[5].getValue().longValue() > 0) {
					// set default value
					session.setMaxMethodCall(dv[5].getValue().longValue());
				} else {
					session.setMaxMethodCall(session.getMaxMethodCallDefault());
				}
				// validate node management limits
				if (dv[6] != null && StatusCode.GOOD.equals(dv[6].getStatusCode()) && dv[6].getValue() != null
						&& dv[6].getValue().getValue() != null && dv[6].getValue().longValue() > 0) {
					// set default value
					session.setMaxNodeManagement(dv[6].getValue().longValue());
				} else {
					session.setMaxNodeManagement(session.getMaxNodeManagementDefault());
				}
				// validate read limits
				if (dv[7] != null && StatusCode.GOOD.equals(dv[7].getStatusCode()) && dv[7].getValue() != null
						&& dv[7].getValue().getValue() != null && dv[7].getValue().longValue() > 0) {
					// set default value
					session.setMaxRead(dv[7].getValue().longValue());
				} else {
					session.setMaxRead(session.getMaxReadDefault());
				}
				// validate register nodes limits
				if (dv[8] != null && StatusCode.GOOD.equals(dv[8].getStatusCode()) && dv[8].getValue() != null
						&& dv[8].getValue().getValue() != null && dv[8].getValue().longValue() > 0) {
					// set default value
					session.setMaxRegisterNodes(dv[8].getValue().longValue());
				} else {
					session.setMaxRegisterNodes(session.getMaxRegisterNodesDefault());
				}
				// validate translate browse path to nodeid limits
				if (dv[9] != null && StatusCode.GOOD.equals(dv[9].getStatusCode()) && dv[9].getValue() != null
						&& dv[9].getValue().getValue() != null && dv[9].getValue().longValue() > 0) {
					// set default value
					session.setMaxTranslateBrowsePath(dv[9].getValue().longValue());
				} else {
					session.setMaxTranslateBrowsePath(session.getMaxTranslateBrowsePathDefault());
				}
				// validate nodes per write limits
				if (dv[10] != null && StatusCode.GOOD.equals(dv[10].getStatusCode()) && dv[10].getValue() != null
						&& dv[10].getValue().getValue() != null && dv[10].getValue().longValue() > 0) {
					// set default value
					session.setMaxNodesPerWrite(dv[10].getValue().longValue());
				} else {
					session.setMaxNodesPerWrite(session.getMaxNodesPerWriteDefault());
				}
				// validate monitored item limits
				if (dv[11] != null && StatusCode.GOOD.equals(dv[11].getStatusCode()) && dv[11].getValue() != null
						&& dv[11].getValue().getValue() != null && dv[11].getValue().longValue() > 0) {
					// set default value
					session.setMaxMonitoredItemsPerCreate(dv[11].getValue().longValue());
				} else {
					session.setMaxMonitoredItemsPerCreate(session.getMaxMonitoredItemsPerCreateDefault());
				}
			} else {
				// set all default values
				session.setMaxNodesPerBrowse(session.getMaxNodesPerBrowseDefault());
				session.setMaxHistoryReadData(session.getMaxHistoryReadDataDefault());
				session.setMaxHistoryReadEvent(session.getMaxHistoryReadEventDefault());
				session.setMaxHistoryUpdateData(session.getMaxHistoryUpdateDataDefault());
				session.setMaxHistoryUpdateEvent(session.getMaxHistoryUpdateEventDefault());
				session.setMaxMethodCall(session.getMaxMethodCallDefault());
				session.setMaxNodeManagement(session.getMaxNodeManagementDefault());
				session.setMaxRead(session.getMaxReadDefault());
				session.setMaxRegisterNodes(session.getMaxRegisterNodesDefault());
				session.setMaxTranslateBrowsePath(session.getMaxTranslateBrowsePathDefault());
				session.setMaxNodesPerWrite(session.getMaxNodesPerWriteDefault());
				session.setMaxMonitoredItemsPerCreate(session.getMaxMonitoredItemsPerCreateDefault());
			}
		} else {
			// set default value
			session.setMaxNodesPerBrowse(session.getMaxNodesPerBrowseDefault());
			session.setMaxHistoryReadData(session.getMaxHistoryReadDataDefault());
			session.setMaxHistoryReadEvent(session.getMaxHistoryReadEventDefault());
			session.setMaxHistoryUpdateData(session.getMaxHistoryUpdateDataDefault());
			session.setMaxHistoryUpdateEvent(session.getMaxHistoryUpdateEventDefault());
			session.setMaxMethodCall(session.getMaxMethodCallDefault());
			session.setMaxNodeManagement(session.getMaxNodeManagementDefault());
			session.setMaxRead(session.getMaxReadDefault());
			session.setMaxRegisterNodes(session.getMaxRegisterNodesDefault());
			session.setMaxTranslateBrowsePath(session.getMaxTranslateBrowsePathDefault());
			session.setMaxNodesPerWrite(session.getMaxNodesPerWriteDefault());
			session.setMaxMonitoredItemsPerCreate(session.getMaxMonitoredItemsPerCreateDefault());
		}
	}

	/**
	 * Processes an username/password Active-Session Service.
	 * 
	 * This service is used by the client to submit its identity of the user
	 * associated with the Session and it shall be issued by the client before it
	 * issues any other Service request after a Create-Session Service.
	 * 
	 * Clients can change the identity of a user associated with a session by
	 * calling the Activate-Session Service (impersonate).
	 * 
	 * @param Session  Created session to activate or impersonate.
	 * @param Username Username to activate the session.
	 * @param Password Password to activate the session.
	 * @return Response of the Service.
	 * 
	 * @throws ServiceResultException
	 */
	public ActivateSessionResponse activateSession(ClientSession session, String username, String password,
			String[] preferredLocales) throws ServiceResultException {
		UserIdentity identity = new UserIdentityRole(username, password);
		session.setCloseCalled(false);
		return this.profileManager.activateSession(session, identity, preferredLocales, this.asyncApi);
	}

	/**
	 * Processes an username/password Active-Session Service.
	 * 
	 * This service is used by the client to submit its identity of the user
	 * associated with the Session and it shall be issued by the client before it
	 * issues any other Service request after a Create-Session Service.
	 * 
	 * Clients can change the identity of a user associated with a session by
	 * calling the Activate-Session Service (impersonate).
	 * 
	 * @param Session        Created session to activate or impersonate.
	 * @param Certificate    Publis certificate to activate the session.
	 * @param AsyncOperation If the value is TRUE, the Service will send
	 *                       asynchronous over the stack.<br>
	 *                       If the value is FALSE, the Service will send
	 *                       synchronous over the stack.
	 * @return Response of the Service.
	 * 
	 * @throws ServiceResultException
	 */
	public ActivateSessionResponse activateSession(ClientSession session, KeyPair tokenCertificate,
			String[] preferredLocales) throws ServiceResultException {
		UserIdentity identity = new UserIdentityRole(tokenCertificate);
		session.setCloseCalled(false);
		return this.profileManager.activateSession(session, identity, preferredLocales, this.asyncApi);
	}

	/**
	 * Processes an Add-Node Service.
	 * 
	 * This service is used to add one Node into a servers AddressSpace hierarchy.
	 * Using this service, each Node is added as the TargetNode of a
	 * HierarchicalReference to ensure that the AddressSpace is fully connected and
	 * that the Node is added as a child within the AddressSpace hierarchy.
	 * 
	 * @param Session        Session which is used to send the service, or NULL for
	 *                       the current active session.
	 * @param BrowseName     The browse name of the Node to add.
	 * @param NodeAttributes The Attributes that are specific to the NodeClass, this
	 *                       is an extensible parameter.
	 * @param NodeClass      NodeClass of the Node to add.
	 * @param ParentNodeId   ExpandedNodeId of the parent Node for the Reference.
	 * @param ReferenceType  NodeId of the hierachical ReferenceType to use for the
	 *                       Reference from the parent Node to the new Node.
	 * @param NewNodeId      Client requested ExpandedNodeId, the ServerIndex in the
	 *                       Id shall be 0.
	 * @param TypeDefinition NodeId of the TypeDefinitionNode for the Node to add,
	 *                       shall be NULL for all NodeClasses other than Object and
	 *                       Variable.
	 * @return Result of the AddNode-Service or null.
	 * 
	 * @throws ServiceResultException
	 */
	public AddNodesResult addNode(ClientSession session, QualifiedName browseName, ExtensionObject nodeAttributes,
			NodeClass nodeClass, ExpandedNodeId parentNodeId, NodeId referenceType, ExpandedNodeId newNodeId,
			ExpandedNodeId typeDefinition) throws ServiceResultException {
		AddNodesResult[] result = this.profileManager.addNodes(session, new QualifiedName[] { browseName },
				new ExtensionObject[] { nodeAttributes }, new NodeClass[] { nodeClass },
				new ExpandedNodeId[] { parentNodeId }, new NodeId[] { referenceType },
				new ExpandedNodeId[] { newNodeId }, new ExpandedNodeId[] { typeDefinition }, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		} else {
			throw new ServiceResultException("no AddNodesResult found!");
		}
	}

	/**
	 * Processes an Add-Node Service.
	 * 
	 * This service is used to more Nodes into a servers AddressSpace hierarchy.
	 * Using this service, each Node is added as the TargetNode of a
	 * HierarchicalReference to ensure that the AddressSpace is fully connected and
	 * that the Node is added as a child within the AddressSpace hierarchy.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param BrowseNames     The browse name of the Node to add.
	 * @param NodeAttributes  The Attributes that are specific to the NodeClass,
	 *                        this is an extensible parameter.
	 * @param NodeClasses     NodeClass of the Node to add.
	 * @param ParentNodeIds   ExpandedNodeId of the parent Node for the Reference
	 * @param ReferenceTypes  NodeId of the hierachical ReferenceType to use for the
	 *                        Reference from the parent Node to the new Node.
	 * @param NewNodeIds      Client requested ExpandedNodeId, the ServerIndex in
	 *                        the Id shall be 0.
	 * @param TypeDefinitions NodeId of the TypeDefinitionNode for the Node to add,
	 *                        shall be NULL for all NodeClasses other than Object
	 *                        and Variable.
	 * @return Results of the AddNode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public AddNodesResult[] addNode(ClientSession session, QualifiedName[] browseNames,
			ExtensionObject[] nodeAttributes, NodeClass[] nodeClasses, ExpandedNodeId[] parentNodeIds,
			NodeId[] referenceTypes, ExpandedNodeId[] newNodeids, ExpandedNodeId[] typeDefinitions)
			throws ServiceResultException {
		return this.profileManager.addNodes(session, browseNames, nodeAttributes, nodeClasses, parentNodeIds,
				referenceTypes, newNodeids, typeDefinitions, this.asyncApi);
	}

	/**
	 * Processes an Add-Reference Service.
	 * 
	 * This Service is used to add one References to one Node.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param SourceNodeId    NodeId of the Node to which the Reference is to be
	 *                        added.
	 * @param TargetNodeId    Expanded NodeId of the TargetNode.
	 * @param ReferenceTypeId NodeId of the ReferenceType that defines the
	 *                        Reference.
	 * @param TargetNodeClass NodeClass of the TargetNode.
	 * @param IsForward       If the value is TRUE, the server creates a forward
	 *                        Reference. If the value is FALSE, the server creates
	 *                        an inverse Reference.
	 * @param TargetServerUri URI of the remote Server. If this parameter is not
	 *                        null, it uses the ServerIndex from the TargetNodeId.
	 * @return StatusCode of the AddReference-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode addReference(ClientSession session, NodeId sourceNodeId, ExpandedNodeId targetNodeId,
			NodeId referenceTypeId, NodeClass targetNodeClass, Boolean isForward, String targetServerUri)
			throws ServiceResultException {
		StatusCode[] result = this.profileManager.addReference(session, new NodeId[] { sourceNodeId },
				new ExpandedNodeId[] { targetNodeId }, new NodeId[] { referenceTypeId },
				new NodeClass[] { targetNodeClass }, new Boolean[] { isForward }, new String[] { targetServerUri },
				this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		throw new ServiceResultException("no addReferenceResult found!");
	}

	/**
	 * Processes an Add-Reference Service .
	 * 
	 * This service is used to more References to one or more Nodes.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SourceNodeIds     NodeId of the Node to which the Reference is to be
	 *                          added.
	 * @param TargetNodeIds     Expanded NodeId of the TargetNode.
	 * @param ReferenceTypeIds  NodeId of the ReferenceType that defines the
	 *                          Reference.
	 * @param TargetNodeClasses NodeClass of the TargetNode.
	 * @param IsForwards        If the value is TRUE, the server creates a forward
	 *                          Reference. If the value is FALSE, the server creates
	 *                          an inverse Reference.
	 * @param TargetServerUris  URI of the remote Server. If this parameter is not
	 *                          null, it uses the ServerIndex from the TargetNodeId.
	 * @return StatusCode of the AddReference-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] addReference(ClientSession session, NodeId[] sourceNodeIds, ExpandedNodeId[] targetNodeIds,
			NodeId[] referenceTypeIds, NodeClass[] targetNodeClasses, Boolean[] isForwards, String[] targetServerUris)
			throws ServiceResultException {
		return this.profileManager.addReference(session, sourceNodeIds, targetNodeIds, referenceTypeIds,
				targetNodeClasses, isForwards, targetServerUris, this.asyncApi);
	}

	/**
	 * Processes a Browse Service.
	 * 
	 * This Service is used to discover the References of a specified Node. The
	 * browse can be further limited by the use of a View. This Browse Service also
	 * supports a primitive filtering capability.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param NodeToBrowse    NodeId of the Node to be browse.
	 * @param BrowseDirection An enumeration that specifies the direction of
	 *                        References to follow. It has the following values,
	 *                        Forward, Inverse, Both.
	 * @param IncludeSubtype  Indicates whether subtypes of the ReferenceType should
	 *                        be included in the browse. If TRUE, then instances of
	 *                        referenceTypeId and all of its subtypes are returned.
	 * @param NodeClassMask   Specifies the NodeClasses of the TargetNodes. Only
	 *                        TargetNodes with the selected NodeClasses are
	 *                        returned. If set to zero, then all NodeClasses are
	 *                        returned.
	 * @param ReferenceTypeId NodeId of the ReferenceType for the Reference.
	 * @param ResultMask      Specifies the fields in the ReferenceDescription
	 *                        structure that should be returned.0...ReferenceType
	 *                        1...IsForward 2...NodeClass 3...BrowseName
	 *                        4...DisplayName 5...TypeDefinition
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
	 * @return References returned from the Browse-Service.
	 * 
	 * @throws ServiceResultExceptionon
	 */
	public BrowseResult browse(ClientSession session, NodeId nodeToBrowse, BrowseDirection browseDirection,
			boolean includeSubtype, UnsignedInteger nodeClassMask, NodeId referenceTypeId, UnsignedInteger resultMask,
			UnsignedInteger maxReferences, ViewDescription view) throws ServiceResultException {
		return this.browse(session, nodeToBrowse, browseDirection, includeSubtype, nodeClassMask, referenceTypeId,
				resultMask, maxReferences, view, true);
	}

	/**
	 * Processes a Browse Service.
	 * 
	 * This Service is used to discover the References of a specified Node. The
	 * browse can be further limited by the use of a View. This Browse Service also
	 * supports a primitive filtering capability.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param NodeToBrowse    NodeId of the Node to be browse.
	 * @param BrowseDirection An enumeration that specifies the direction of
	 *                        References to follow. It has the following values,
	 *                        Forward, Inverse, Both.
	 * @param IncludeSubtype  Indicates whether subtypes of the ReferenceType should
	 *                        be included in the browse. If TRUE, then instances of
	 *                        referenceTypeId and all of its subtypes are returned.
	 * @param NodeClassMask   Specifies the NodeClasses of the TargetNodes. Only
	 *                        TargetNodes with the selected NodeClasses are
	 *                        returned. If set to zero, then all NodeClasses are
	 *                        returned.
	 * @param ReferenceTypeId NodeId of the ReferenceType for the Reference.
	 * @param ResultMask      Specifies the fields in the ReferenceDescription
	 *                        structure that should be returned.0...ReferenceType
	 *                        1...IsForward 2...NodeClass 3...BrowseName
	 *                        4...DisplayName 5...TypeDefinition
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
	 * @param ValidateResults Flag if results should be validated, if flag is true
	 *                        any additional read requests will be made. For higher
	 *                        performance, disable validation.
	 * @return References returned from the Browse-Service.
	 * 
	 * @throws ServiceResultExceptionon
	 */
	public BrowseResult browse(ClientSession session, NodeId nodeToBrowse, BrowseDirection browseDirection,
			boolean includeSubtype, UnsignedInteger nodeClassMask, NodeId referenceTypeId, UnsignedInteger resultMask,
			UnsignedInteger maxReferences, ViewDescription view, boolean validateResults)
			throws ServiceResultException {
		BrowseResult[] result = this.profileManager
				.browse(session, new NodeId[] { nodeToBrowse }, browseDirection, includeSubtype, nodeClassMask,
						referenceTypeId, resultMask, maxReferences, view, this.asyncApi, validateResults)
				.getResults();
		if (result != null && result.length > 0) {
			return result[0];
		}
		throw new ServiceResultException("no browse result found!");
	}

	/**
	 * Processes a Browse Service.
	 * 
	 * This Service is used to discover the References of a specified Node. The
	 * browse can be further limited by the use of a View. This Browse Service also
	 * supports a primitive filtering capability.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param NodesToBrowse   Array of NodeId of the Nodes to browse.
	 * @param BrowseDirection An enumeration that specifies the direction of
	 *                        References to follow. It has the following values,
	 *                        Forward, Inverse, Both.
	 * @param IncludeSubtype  Indicates whether subtypes of the ReferenceType should
	 *                        be included in the browse. If TRUE, then instances of
	 *                        referenceTypeId and all of its subtypes are returned.
	 * @param NodeClassMask   Specifies the NodeClasses of the TargetNodes. Only
	 *                        TargetNodes with the selected NodeClasses are
	 *                        returned. If set to zero, then all NodeClasses are
	 *                        returned.
	 * @param ReferenceTypeId NodeId of the ReferenceType for the Reference.
	 * @param ResultMask      Specifies the fields in the ReferenceDescription
	 *                        structure that should be returned.0...ReferenceType
	 *                        1...IsForward 2...NodeClass 3...BrowseName
	 *                        4...DisplayName 5...TypeDefinition
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
	 * @return References returned from the Browse-Service.
	 * 
	 * @throws ServiceResultExceptionon
	 */
	public BrowseResult[] browse(ClientSession session, NodeId[] nodesToBrowse, BrowseDirection browseDirection,
			boolean includeSubtype, UnsignedInteger nodeClassMask, NodeId referenceTypeId, UnsignedInteger resultMask,
			UnsignedInteger maxReferences, ViewDescription view, boolean validateResults)
			throws ServiceResultException {
		BrowseResult[] result = this.profileManager.browse(session, nodesToBrowse, browseDirection, includeSubtype,
				nodeClassMask, referenceTypeId, resultMask, maxReferences, view, this.asyncApi, validateResults)
				.getResults();
		if (result != null && result.length > 0) {
			return result;
		}
		throw new ServiceResultException("no browse result found!");
	}

	/**
	 * Processes a Browse Service.
	 * 
	 * This Service is used to discover the References of specified Nodes. The
	 * browse can be further limited by the use of a View. This Browse Service also
	 * supports a primitive filtering capability.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param NodeToBrowse    Array of NodeId of the Nodes to browse.
	 * @param BrowseDirection Array of enumeration that specifies the direction of
	 *                        References to follow. It has the following values,
	 *                        Forward, Inverse, Both.
	 * @param IncludeSubtype  Array which indicates whether subtypes of the
	 *                        ReferenceType should be included in the browse. If
	 *                        TRUE, then instances of referenceTypeId and all of its
	 *                        subtypes are returned.
	 * @param NodeClassMask   Array specifies the NodeClasses of the TargetNodes.
	 *                        Only TargetNodes with the selected NodeClasses are
	 *                        returned. If set to zero, then all NodeClasses are
	 *                        returned.
	 * @param ReferenceTypeId Array of NodeIds of the ReferenceType for the
	 *                        Reference.
	 * @param ResultMask      Specifies the fields in the ReferenceDescription
	 *                        structure that should be returned.0...ReferenceType
	 *                        1...IsForward 2...NodeClass 3...BrowseName
	 *                        4...DisplayName 5...TypeDefinition
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
	 * @return References returned from the Browse-Service.
	 * 
	 * @throws ServiceResultExceptionon
	 */
	public BrowseResult[] browse(ClientSession session, NodeId[] nodesToBrowse, BrowseDirection[] browseDirections,
			boolean[] includeSubtypes, UnsignedInteger[] nodeClassMasks, NodeId[] referenceTypeIds,
			UnsignedInteger[] resultMasks, UnsignedInteger maxReferences, ViewDescription view, boolean validateResults)
			throws ServiceResultException {
		BrowseResult[] result = this.profileManager.browse(session, nodesToBrowse, browseDirections, includeSubtypes,
				nodeClassMasks, referenceTypeIds, resultMasks, maxReferences, view, this.asyncApi, validateResults)
				.getResults();
		if (result != null && result.length > 0) {
			return result;
		}
		throw new ServiceResultException("no browse result found!");
	}

	/**
	 * Processes a Browse-Next Service.
	 * 
	 * This Service is used to request the next set of Browse or BrowseNext response
	 * information.
	 * 
	 * @param Session                   Session which is used to send the service,
	 *                                  or NULL for the current active session.
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
	 * @return References of the BrowseNext-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public BrowseResult[] browseNext(ClientSession session, ByteString[] continuationPoints,
			boolean releaseContinuationPoints) throws ServiceResultException {
		return this.profileManager.browseNext(session, continuationPoints, releaseContinuationPoints, this.asyncApi)
				.getResults();
	}

	/**
	 * Processes a Call Service.
	 * 
	 * This Service is used to call a Methods. This Service provides for passing
	 * input and output arguments to/from a method. These arguments are defined by
	 * Properties of the method.
	 * 
	 * @param Session        Session which is used to send the service, or NULL for
	 *                       the current active session.
	 * @param ObjectId       The NodeId shall be that of the Object or ObjectType
	 *                       that is the source of a HasComponent Reference (or
	 *                       subtype of HasComponent Reference) to this Method.
	 * @param MethodId       NodeId of the Method to invoke.
	 * @param InputArguments Array of input argument values. An empty list indicates
	 *                       that there are no input arguments. The size and order
	 *                       of this list matches the size and order of the input
	 *                       arguments defined by the input InputArguments Property
	 *                       of the Method. The name, a description and the data
	 *                       type of each argument are defined by the Argument
	 *                       structure in each element of the method�s
	 *                       InputArguments Property.
	 * @return Result from a call of a method.
	 * 
	 * @throws ServiceResultException
	 */
	public CallMethodResult callMethod(ClientSession session, NodeId objectId, NodeId methodId,
			Variant[] inputArguments) throws ServiceResultException {
		Variant[][] inputArgumentesVariant = new Variant[1][inputArguments.length];
		for (int i = 0; i < inputArguments.length; i++) {
			inputArgumentesVariant[0][i] = inputArguments[i];
		}
		CallMethodResult[] result = this.profileManager.call(session, inputArgumentesVariant, new NodeId[] { objectId },
				new NodeId[] { methodId }, this.asyncApi).getResults();
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a Call Service.
	 * 
	 * This Service is used to call (invoke) a list of Methods. This Service
	 * provides for passing input and output arguments to/from a method. These
	 * arguments are defined by Properties of the method.
	 * 
	 * @param Session        Session which is used to send the service, or NULL for
	 *                       the current active session.
	 * @param ObjectIds      The NodeIds shall be that of the Objects or ObjectTypes
	 *                       that are the sources of a HasComponent Reference (or
	 *                       subtype of HasComponent Reference) to this Method.
	 * @param MethodIds      NodeIds of the Methods to invoke.
	 * @param InputArguments Array of input argument values. An empty list indicates
	 *                       that there are no input arguments. The size and order
	 *                       of this list matches the size and order of the input
	 *                       arguments defined by the input InputArguments Property
	 *                       of the Method. The name, a description and the data
	 *                       type of each argument are defined by the Argument
	 *                       structure in each element of the method�s
	 *                       InputArguments Property.
	 * @return Results from the call of methods.
	 * 
	 * @throws ServiceResultException
	 */
	public CallMethodResult[] callMethod(ClientSession session, NodeId[] objectIds, NodeId[] methodIds,
			Variant[][] inputArguments) throws ServiceResultException {
		return this.profileManager.call(session, inputArguments, objectIds, methodIds, this.asyncApi).getResults();
	}

	/**
	 * Processes a Cancel-Service.
	 * 
	 * This service cancels an unfinished request on the server.F
	 * 
	 * @param Session       Session which is used to send the service, or NULL for
	 *                      the current active session.
	 * @param RequestHandle Identifier of the request to cancel.
	 * @return Response of the Cancel-Service
	 * 
	 * @throws ServiceResultException
	 */
	public CancelResponse cancelRequest(ClientSession session, UnsignedInteger requestHandle)
			throws ServiceResultException {
		return this.profileManager.cancelRequest(session, requestHandle, this.asyncApi);
	}

	/**
	 * Processes a Close-Session Service.
	 * 
	 * This Service is used to close an existing session on a server.
	 * 
	 * @param Session             Session which is used to send the service, or NULL
	 *                            for the current active session.
	 * @param DeleteSubscriptions If the value is TRUE, the Server deletes all
	 *                            Subscriptions associated with the Session. If the
	 *                            value is FALSE, the Server keeps the Subscriptions
	 *                            associated with the Session until they timeout
	 *                            based on their own lifetime.
	 * @return Response of the Close-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public CloseSessionResponse closeSession(ClientSession session, boolean deleteSubscriptions)
			throws ServiceResultException {
		session.setCloseCalled(true);
		return this.profileManager.closeSession(session, deleteSubscriptions, this.asyncApi);
	}

	/**
	 * Processes a Create-Monitored-Item Service.
	 * 
	 * This Service is used to create and add one MonitoredItem to a Subscription.
	 * MonitoredItem is defined to subscribe to Data Changes or Events. Each
	 * MonitoredItem identifies the item to be monitored and the Subscription to use
	 * to send Notifications. Calling the CreateMonitoredItems Service repetitively
	 * to add a small number of MonitoredItems each time may adversely affect the
	 * performance of the Server.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SubscriptionId    The Server-assigned identifier for the Subscription
	 *                          that will report Notifications for this
	 *                          MonitoredItem.
	 * @param NodeIds           NodeId of the Node to monitor.
	 * @param AttributeIds      Id of the Node�s Attribute to monitor (Value,
	 *                          EventNotifier)
	 * @param IndexRanges       IndexRange of an Array value e.g.: ("1:2")
	 * @param DataEncodings     Qualified data encoding
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
	 * @return Monitored Item, if the Service was successful.
	 * 
	 * @throws ServiceResultException
	 */
	public MonitoredItem createMonitoredItem(ClientSession session, UnsignedInteger subscriptionId, NodeId nodeId,
			UnsignedInteger attributeId, String indexRange, QualifiedName dataEncoding, double samplingInterval,
			ExtensionObject filter, UnsignedInteger queueSize, boolean discardOldest, MonitoringMode monitoringMode,
			TimestampsToReturn timestampToReturn, String key) throws ServiceResultException {
		ReadValueId nodeToRead = new ReadValueId();
		nodeToRead.setAttributeId(attributeId);
		nodeToRead.setDataEncoding(dataEncoding);
		nodeToRead.setIndexRange(indexRange);
		nodeToRead.setNodeId(nodeId);
		MonitoredItem[] item = this.profileManager.createMonitoredItem(session, subscriptionId,
				new ReadValueId[] { nodeToRead }, samplingInterval, filter, queueSize, discardOldest, monitoringMode,
				timestampToReturn, this.asyncApi, new String[] { key });
		if (item != null && item.length > 0) {
			return item[0];
		}
		return null;
	}

	/**
	 * Processes a Create-Monitored-Item Service.
	 * 
	 * This Service is used to create and more MonitoredItems to a Subscription.
	 * MonitoredItems are defined to subscribe to Data Changes and Events.Each
	 * MonitoredItem identifies the item to be monitored and the Subscription to use
	 * to send Notifications. Calling the CreateMonitoredItems Service repetitively
	 * to add a small number of MonitoredItems each time may adversely affect the
	 * performance of the Server.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SubscriptionId    The Server-assigned identifier for the Subscription
	 *                          that will report Notifications for this
	 *                          MonitoredItem.
	 * @param NodeIds           NodeId of the Node to monitor.
	 * @param AttributeIds      Id of the Node�s Attribute to monitor (Value,
	 *                          EventNotifier)
	 * @param IndexRanges       IndexRange of an Array value e.g.: ("1:2")
	 * @param DataEncodings     Qualified data encoding
	 * @param SamplingInterval  A requested monitoring parameter. The interval that
	 *                          defines the fastest rate at which the
	 *                          MonitoredItem(s) should be accessed and evaluated.
	 *                          This interval is defined in milliseconds.
	 * @param Filter            A requested monitoring parameter. A filter used by
	 *                          the Server to determine if the MonitoredItem should
	 *                          generate a Notification. If not used, this parameter
	 *                          is null.
	 * @param Queuesize         A requested monitoring parameter. The requested size
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
	 * @return Monitored Items, if the Service was successful.
	 * 
	 * @throws ServiceResultException
	 */
	public MonitoredItem[] createMonitoredItem(ClientSession session, UnsignedInteger subscriptionId, NodeId[] nodeIds,
			UnsignedInteger[] attributeIds, String[] indexRanges, QualifiedName[] dataEncodings,
			double samplingInterval, ExtensionObject filter, UnsignedInteger queueSize, boolean discardOldest,
			MonitoringMode monitoringMode, TimestampsToReturn timestampToReturn, String[] key)
			throws ServiceResultException {
		if (nodeIds.length != attributeIds.length) {
			throw new IllegalArgumentException("invalid");
		}
		// max monitored item per create should not be 0
		if (session.getMaxMonitoredItemsPerCreate() <= 0)
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		List<ReadValueId> nodesToMonitor = new ArrayList<>();
		List<MonitoredItem> monitoredItems = new ArrayList<>();
		for (int ii = 0; ii < nodeIds.length; ii++) {
			ReadValueId readValue = new ReadValueId();
			readValue.setNodeId(nodeIds[ii]);
			readValue.setAttributeId(attributeIds[ii]);
			readValue.setDataEncoding(QualifiedName.NULL);
			if (dataEncodings != null) {
				readValue.setDataEncoding(dataEncodings[ii]);
			}
			if (indexRanges != null) {
				readValue.setIndexRange(indexRanges[ii]);
			}
			nodesToMonitor.add(readValue);
			if ((ii + 1) % session.getMaxMonitoredItemsPerCreate() == 0 || ii + 1 == nodeIds.length) {
				monitoredItems.addAll(Arrays.asList(this.profileManager.createMonitoredItem(session, subscriptionId,
						nodesToMonitor.toArray(new ReadValueId[nodesToMonitor.size()]), samplingInterval, filter,
						queueSize, discardOldest, monitoringMode, timestampToReturn, this.asyncApi, key)));
				nodesToMonitor.clear();
			}
		}
		return monitoredItems.toArray(new MonitoredItem[monitoredItems.size()]);
	}

	/**
	 * Processes a Create-Session Service.
	 * 
	 * This Service is used by an OPC UA Client to create a Session and the Server
	 * returns two values which uniquely identify the Session. The first value is
	 * the sessionId which is used to identify the Session in the audit logs and in
	 * the Server address space. The second is the authenticationToken which is used
	 * to associate an incoming request with a Session.
	 * 
	 * The Session created with this Service shall not be used until the Client
	 * calls the ActivateSession Service
	 * 
	 * @param Endpoint    An OPC Instance of a network address that the Client used
	 *                    to access the Session Endpoint.
	 * @param Sessionname Name for the Session.
	 * @return Created Session, which must be activated to send services.
	 * 
	 * @throws ServiceResultException
	 */
	public ClientSession createSession(EndpointDescription endpoint, String sessionName) throws ServiceResultException {
		return createSession(endpoint, sessionName, true);
	}

	/**
	 * Processes a Create-Session Service.
	 * 
	 * This Service is used by an OPC UA Client to create a Session and the Server
	 * returns two values which uniquely identify the Session. The first value is
	 * the sessionId which is used to identify the Session in the audit logs and in
	 * the Server address space. The second is the authenticationToken which is used
	 * to associate an incoming request with a Session.
	 * 
	 * The Session created with this Service shall not be used until the Client
	 * calls the ActivateSession Service
	 * 
	 * @param Endpoint    An OPC Instance of a network address that the Client used
	 *                    to access the Session Endpoint.
	 * @param Sessionname Name for the Session.
	 * @return Created Session, which must be activated to send services.
	 * 
	 * @throws ServiceResultException
	 */
	public ClientSession createSession(EndpointDescription endpoint, String sessionName, boolean validateEndpoints)
			throws ServiceResultException {
		if (sessionName == null) {
			// generates a default sessionname
			return this.profileManager.createSession(endpoint, SessionManager.DEFAULTSESSIONNAME,
					this.clientApplicationConfiguration, this.asyncApi, this.defaultKeepAliveInterval);
		} else {
			return this.profileManager.createSession(endpoint, sessionName, this.clientApplicationConfiguration,
					this.asyncApi, validateEndpoints, this.defaultKeepAliveInterval);
		}
	}

	/**
	 * Processes a Create-Subscription Service.<br>
	 * <br>
	 * 
	 * This Service is used to create a Subscription with server default settings.
	 * Subscriptions monitor a set of MonitoredItems for Notifications and return
	 * them to the Client in response to Publish requests. Creates a default
	 * subscription with the default parameters used from the server, but publishing
	 * is enabled. <br>
	 * <br>
	 * Default Subscription: PublishingInterval:100ms, MaxNotificationsPerPublish:
	 * 1000, Priority: 0, Lifetime: 100, KeepAlive: 5, Publishing: enabled
	 * 
	 * @param Session Session which is used to send the service, or NULL for the
	 *                current active session.
	 * @return Default Subscription, if the Service was successful.
	 * 
	 * @throws ServiceResultException
	 */
	public Subscription createSubscription(ClientSession session) throws ServiceResultException {
		return this.profileManager.createSubscription(session, DefaultSubscriptionSettings.maxNotificationsPerPublsih,
				DefaultSubscriptionSettings.priority, DefaultSubscriptionSettings.publishingEnabled,
				DefaultSubscriptionSettings.lifetimeCount, DefaultSubscriptionSettings.maxKeepaliveCount,
				DefaultSubscriptionSettings.publishingInterval, this.asyncApi);
	}

	/**
	 * Processes a Create-Subscription Service.
	 * 
	 * This Service is used to create a Subscription. Subscriptions monitor a set of
	 * MonitoredItems for Notifications and return them to the Client in response to
	 * Publish requests. Creates a default subscription with the default parameters
	 * used from the server, but publishing is enabled.
	 * 
	 * @param Session                   Session which is used to send the service,
	 *                                  or NULL for the current active session.
	 * @param PublishingInterval        This interval defines the cyclic rate that
	 *                                  the Subscription is being requested to
	 *                                  return Notifications to the Client. This
	 *                                  interval is expressed in milliseconds.
	 * @param PublishingEnabled         If the value is TRUE, publishing is enabled
	 *                                  for the Subscription. If the value is FALSE,
	 *                                  publishing is disabled for the Subscription.
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
	 * 
	 * @return Subscription, if the Service was successful.
	 * 
	 * @throws ServiceResultException
	 */
	public Subscription createSubscription(ClientSession session, Double publishingInterval, Boolean publishingEnabled,
			Integer maxNotificationPerPublish, Integer priority, Integer maxLifetimeCount, Integer maxKeepAliveCount)
			throws ServiceResultException {
		UnsignedInteger maxNotificationPerPublishCopy = maxNotificationPerPublish != null
				? new UnsignedInteger(maxNotificationPerPublish)
				: null;
		UnsignedByte priorityCopy = priority != null ? new UnsignedByte(priority) : null;
		UnsignedInteger maxLifetimeCountCopy = maxLifetimeCount != null ? new UnsignedInteger(maxLifetimeCount) : null;
		UnsignedInteger maxKeepAliveCountCopy = maxKeepAliveCount != null ? new UnsignedInteger(maxKeepAliveCount)
				: null;
		return this.profileManager.createSubscription(session, maxNotificationPerPublishCopy, priorityCopy,
				publishingEnabled, maxLifetimeCountCopy, maxKeepAliveCountCopy, publishingInterval, this.asyncApi);
	}

	/**
	 * Processes a Delete-Monitored-Item Service.
	 * 
	 * This Service is used to remove one MonitoredItem of a Subscription. When a
	 * MonitoredItem is deleted, its triggered item links are also deleted.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param SubscriptionId  Server-assigned identifier for the Subscription that
	 *                        contains the MonitoredItems to be deleted.
	 * @param MonitoredItemId Server-assigned id of the MonitoredItem to be deleted.
	 * @return StatusCode of the DeleteMonitoredItem-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode deleteMonitoredItem(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger monitoredItemId) throws ServiceResultException {
		StatusCode[] result = this.profileManager.deleteMonitoredItems(session, subscriptionId,
				new UnsignedInteger[] { monitoredItemId }, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a Delete-Monitored-Item Service.
	 * 
	 * This Service is used to remove more MonitoredItems of a Subscription. When a
	 * MonitoredItem is deleted, its triggered item links are also deleted.
	 * 
	 * @param Session          Session which is used to send the service, or NULL
	 *                         for the current active session.
	 * @param SubscriptionId   Server-assigned identifier for the Subscription that
	 *                         contains the MonitoredItems to be deleted.
	 * @param MonitoredItemIds Server-assigned Ids of the MonitoredItems to be
	 *                         deleted.
	 * @return StatusCodes of the DeleteMonitoredItem-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] deleteMonitoredItem(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger[] monitoredItemIds) throws ServiceResultException {
		return this.profileManager.deleteMonitoredItems(session, subscriptionId, monitoredItemIds, this.asyncApi);
	}

	/**
	 * Processes a Delete-Node Service.
	 * 
	 * This Service is used to delete one Node from a servers AddressSpace. When any
	 * of the Nodes deleted by an invocation of this service is the TargetNode of a
	 * Reference, then those References are left unresolved based on the
	 * deleteTargetReferences parameter.
	 * 
	 * @param Session                Session which is used to send the service, or
	 *                               NULL for the current active session.
	 * @param NodeToDelete           NodeId of the Node to delete
	 * @param DeleteTargetReferences If the value is TRUE, the server deletes
	 *                               References in TargetNodes that references the
	 *                               Node to delete.<br>
	 *                               If the value is FALSE, the server deletes only
	 *                               the References for which the Node to delete is
	 *                               the source.
	 * @return Response of the DeleteNode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public DeleteNodesResponse deleteNode(ClientSession session, NodeId nodeToDelete, Boolean deleteTargetReferences)
			throws ServiceResultException {
		return this.profileManager.deleteNodes(session, new NodeId[] { nodeToDelete },
				new Boolean[] { deleteTargetReferences }, this.asyncApi);
	}

	/**
	 * Processes a Delete-Node Service.
	 * 
	 * This Service is used to delete more Nodes from a servers AddressSpace. When
	 * any of the Nodes deleted by an invocation of this service is the TargetNode
	 * of a Reference, then those References are left unresolved based on the
	 * deleteTargetReferences parameter.
	 * 
	 * @param Session                Session which is used to send the service, or
	 *                               NULL for the current active session.
	 * @param NodesToDelete          NodeId of the Nodes to delete
	 * @param DeleteTargetReferences If the value is TRUE, the server deletes
	 *                               References in TargetNodes that references the
	 *                               Node to delete.<br>
	 *                               If the value is FALSE, the server deletes only
	 *                               the References for which the Node to delete is
	 *                               the source.
	 * @return Response of the DeleteNode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public DeleteNodesResponse deleteNode(ClientSession session, NodeId[] nodesToDelete,
			Boolean[] deleteTargetReferences) throws ServiceResultException {
		return this.profileManager.deleteNodes(session, nodesToDelete, deleteTargetReferences, this.asyncApi);
	}

	/**
	 * Processes a Delete-Reference Service.
	 * 
	 * This Service is used to delete one Reference of a Node.
	 * 
	 * @param Session             Session which is used to send the service, or NULL
	 *                            for the current active session.
	 * @param SourceNodeId        NodeId of the Node that contains the Reference to
	 *                            delete.
	 * @param TargetNodeId        NodeId of the TargetNode of the Reference.
	 * @param ReferenceTypeId     NodeId of the ReferenceType that defines the
	 *                            Reference to delete.
	 * @param IsForward           If the value is TRUE, the Server deletes a forward
	 *                            Reference If the value is FALSE, the Server
	 *                            deletes an inverse Reference.
	 * @param DeleteBidirectional If the value is TRUE, the server delete the
	 *                            specified Reference and the opposite Reference
	 *                            from the Target Node If the value is FALSE, the
	 *                            server delete only the specified Reference
	 * @return Response of the DeleteReference-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public DeleteReferencesResponse deleteReferences(ClientSession session, NodeId sourceNodeId,
			ExpandedNodeId targetNodeId, NodeId referenceTypeId, Boolean isForward, Boolean deleteBidirectional)
			throws ServiceResultException {
		return this.profileManager.deleteReferences(session, new NodeId[] { sourceNodeId },
				new ExpandedNodeId[] { targetNodeId }, new NodeId[] { referenceTypeId }, new Boolean[] { isForward },
				new Boolean[] { deleteBidirectional }, this.asyncApi);
	}

	/**
	 * Processes a Delete-Reference Service.
	 * 
	 * This Service is used to deletemore References of a Node.
	 * 
	 * @param Session             Session which is used to send the service, or NULL
	 *                            for the current active session.
	 * @param SourceNodeId        NodeId of the Node that contains the Reference to
	 *                            delete.
	 * @param TargetNodeId        NodeId of the TargetNode of the Reference.
	 * @param ReferenceTypeId     NodeId of the ReferenceType that defines the
	 *                            Reference to delete.
	 * @param IsForward           If the value is TRUE, the Server deletes a forward
	 *                            Reference If the value is FALSE, the Server
	 *                            deletes an inverse Reference.
	 * @param DeleteBidirectional If the value is TRUE, the server delete the
	 *                            specified Reference and the opposite Reference
	 *                            from the Target Node If the value is FALSE, the
	 *                            server delete only the specified Reference
	 * @return Response of the DeleteReference-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public DeleteReferencesResponse deleteReferences(ClientSession session, NodeId[] sourceNodeIds,
			ExpandedNodeId[] targetNodeids, NodeId[] referenceTypeIds, Boolean[] isForward,
			Boolean[] deleteBidirectional/*
											 * , Boolean[] deleteTargetReferences
											 */) throws ServiceResultException {
		return this.profileManager.deleteReferences(session, sourceNodeIds, targetNodeids, referenceTypeIds, isForward,
				deleteBidirectional, this.asyncApi);
	}

	/**
	 * Processes a Delete-Subscription Service.
	 * 
	 * This Service is invoked to delete one Subscription that has created.
	 * Successful completion of this Service causes all MonitoredItems that use the
	 * Subscription to be deleted.
	 * 
	 * @param Session        Session which is used to send the service, or NULL for
	 *                       the current active session.
	 * @param SubscriptionId NodeId of the server-assigned identifier for the
	 *                       Subscription.
	 * @return StatusCode of the DeleteSubscription-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode deleteSubscription(ClientSession session, UnsignedInteger subscriptionId)
			throws ServiceResultException {
		StatusCode[] result = this.profileManager.deleteSubscriptions(session, new UnsignedInteger[] { subscriptionId },
				this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a Delete-Subscription Service.
	 * 
	 * This Service is invoked to delete more Subscriptions that has created.
	 * Successful completion of this Service causes all MonitoredItems that use the
	 * Subscription to be deleted.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param SubscriptionIds NodeIds of the server-assigned identifiers for the
	 *                        Subscriptions.
	 * @return StatusCodes of the DeleteSubscription-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] deleteSubscription(ClientSession session, UnsignedInteger[] subscriptionIds)
			throws ServiceResultException {
		return this.profileManager.deleteSubscriptions(session, subscriptionIds, this.asyncApi);
	}

	/**
	 * Returns the Server {@link EndpointDescriptions} without using the Discovery
	 * Service. Use after the clientconfig has been set!
	 * 
	 * @param ServerUri Server Uri of the Server to get the Endpoints.
	 * @return EndpointDescriptions from the server with the parameter serverUri, or
	 *         NULL.
	 * 
	 * @throws ServiceResultException
	 */
	public EndpointDescription[] getServerEndpoints(String serverUri) throws ServiceResultException {
		return this.profileManager.getServerEndpoints(serverUri, this.clientApplicationConfiguration, this.asyncApi);
	}

	/**
	 * Processes a HistoryRead-Service.
	 * 
	 * This Service is used to read historical values or events from nodes.
	 * 
	 * @param Session             Session which is used to send the service, or NULL
	 *                            for the current active session.
	 * @param NodeIds             The nodeId of the Nodes whose historical values
	 *                            are to be read.
	 * @param ContinuationPoint   The continuationPoint parameter in the HistoryRead
	 *                            is used to mark a point from which to continue the
	 *                            read if not all values could be returned in one
	 *                            response.
	 * @param IndexRanges         This parameter is used to identify a single
	 *                            element of an array, or a single range of indexes
	 *                            for arrays.
	 * @param DataEncodings       A QualifiedName that specifies the data encoding
	 *                            to be returned for the Value to be read.
	 * @param ReleaseContinuation TRUE passed continuationPoints shall be reset to
	 *                            free resources in the Server. <br>
	 *                            FALSE passed continuationPoints shall be used to
	 *                            get the next set of historical information. <br>
	 *                            A Client shall always use the continuation point
	 *                            returned by a HistoryRead response to free the
	 *                            resources for the continuation point in the
	 *                            Server. If the Client does not want to get the
	 *                            next set of historical information, HistoryRead
	 *                            shall be called with this parameter set to TRUE.
	 * @param TimestampToReturn   Specifies the timestamps to be returned for each
	 *                            requested Variable Value Attribute.
	 * @param HistoryReadDetails  The details define the types of history reads that
	 *                            can be performed.
	 * @return Result from the HistoricalRead-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public HistoryReadResult historyRead(ClientSession session, NodeId nodeId, byte[] continuationPoint,
			String indexRange, QualifiedName dataEncoding, boolean releaseContinuation,
			TimestampsToReturn timestampToReturn, HistoryReadDetails details) throws ServiceResultException {
		HistoryReadResult[] result = this.profileManager.historyRead(session, new NodeId[] { nodeId },
				new byte[][] { continuationPoint }, new String[] { indexRange }, new QualifiedName[] { dataEncoding },
				releaseContinuation, timestampToReturn, details, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a HistoryRead-Service.
	 * 
	 * This Service is used to read historical values or events from nodes.
	 * 
	 * @param Session             Session which is used to send the service, or NULL
	 *                            for the current active session.
	 * @param NodeIds             The nodeId of the Nodes whose historical values
	 *                            are to be read.
	 * @param ContinuationPoint   The continuationPoint parameter in the HistoryRead
	 *                            is used to mark a point from which to continue the
	 *                            read if not all values could be returned in one
	 *                            response.
	 * @param IndexRanges         This parameter is used to identify a single
	 *                            element of an array, or a single range of indexes
	 *                            for arrays.
	 * @param DataEncodings       A QualifiedName that specifies the data encoding
	 *                            to be returned for the Value to be read.
	 * @param ReleaseContinuation TRUE passed continuationPoints shall be reset to
	 *                            free resources in the Server. <br>
	 *                            FALSE passed continuationPoints shall be used to
	 *                            get the next set of historical information. <br>
	 *                            A Client shall always use the continuation point
	 *                            returned by a HistoryRead response to free the
	 *                            resources for the continuation point in the
	 *                            Server. If the Client does not want to get the
	 *                            next set of historical information, HistoryRead
	 *                            shall be called with this parameter set to TRUE.
	 * @param TimestampToReturn   Specifies the timestamps to be returned for each
	 *                            requested Variable Value Attribute.
	 * @param HistoryReadDetails  The details define the types of history reads that
	 *                            can be performed.
	 * @return Results from the HistoricalRead-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public HistoryReadResult[] historyRead(ClientSession session, NodeId[] nodeIds, byte[][] continuationPoint,
			String[] indexRanges, QualifiedName[] dataEncodings, boolean releaseContinuation,
			TimestampsToReturn timestampToReturn, HistoryReadDetails historyReadDetails) throws ServiceResultException {
		return this.profileManager.historyRead(session, nodeIds, continuationPoint, indexRanges, dataEncodings,
				releaseContinuation, timestampToReturn, historyReadDetails, this.asyncApi);
	}

	/**
	 * Processes a HistoryUpdate-Service.
	 * 
	 * This Service is used to update historical values or Events of Nodes. Valid
	 * actions are Insert, Replace or Delete.
	 * 
	 * @param Session              Session which is used to send the service, or
	 *                             NULL for the current active session.
	 * @param NodeId               The nodeId of the Nodes whose historical values
	 *                             are to be updated.
	 * @param HistoryUpdateDetails The details defined for this update.
	 * @return Result from the HistoryUpdate-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult historyUpdate(ClientSession session, NodeId nodeId,
			HistoryUpdateDetails historyUpdateDetails) throws ServiceResultException {
		HistoryUpdateResult[] result = this.historyUpdate(session, new NodeId[] { nodeId },
				new HistoryUpdateDetails[] { historyUpdateDetails });
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a HistoryUpdate-Service.
	 * 
	 * This Service is used to update historical values or Events of Nodes. Valid
	 * actions are Insert, Replace or Delete.
	 * 
	 * @param Session              Session which is used to send the service, or
	 *                             NULL for the current active session.
	 * @param NodeId               The nodeId of the Nodes whose historical values
	 *                             are to be updated.
	 * @param HistoryUpdateDetails The details defined for this update.
	 * @return Result from the HistoryUpdate-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult[] historyUpdate(ClientSession session, NodeId[] nodeIds, HistoryUpdateDetails[] details)
			throws ServiceResultException {
		return this.profileManager.historyUpdate(session, nodeIds, details, this.asyncApi);
	}

	/**
	 * Creates a history archive object from a session to read or update history
	 * data
	 * 
	 * @param Session Session to be used or NULL if the current active session
	 *                should be taken.
	 * @return HistoryArchive to use history services.
	 * @throws ServiceResultException
	 */
	public HistoryArchive historyArchive(ClientSession session) throws ServiceResultException {
		return this.profileManager.historyArchive(session);
	}

	/**
	 * Processes a Modify-Monitored-Item Service.
	 * 
	 * This Service is used to modify one MonitoredItem of a Subscription.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SubscriptionId    The Server-assigned identifier for the Subscription
	 *                          that will report Notifications for this
	 *                          MonitoredItem.
	 * @param MonitoredItemId   Server-assigned Id for the MonitoredItem;
	 * @param ClientHandle      Client-supplied id of the MonitoredItem.
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
	 * @return Result of the ModifyMonitoredItem-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public MonitoredItemModifyResult modifyMonitoredItem(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger monitoredItemId, UnsignedInteger clientHandle, Boolean discardOldest,
			ExtensionObject filter, UnsignedInteger queueSize, Double samplingInterval,
			TimestampsToReturn timestampToReturn) throws ServiceResultException {
		MonitoredItemModifyResult[] result = this.profileManager.modifyMonitoredItems(session, subscriptionId,
				new UnsignedInteger[] { monitoredItemId }, new UnsignedInteger[] { clientHandle },
				new Boolean[] { discardOldest }, new ExtensionObject[] { filter }, new UnsignedInteger[] { queueSize },
				new Double[] { samplingInterval }, timestampToReturn, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		throw new ServiceResultException("no modify monitored item result found!");
	}

	/**
	 * Processes a Modify-Monitored-Item Service.
	 * 
	 * This Service is used to modify more MonitoredItems of a Subscription.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SubscriptionId    The Server-assigned identifier for the Subscription
	 *                          that will report Notifications for this
	 *                          MonitoredItem.
	 * @param MonitoredItemId   Server-assigned Id for the MonitoredItem;
	 * @param ClientHandle      Client-supplied id of the MonitoredItem.
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
	 *                          MonitoredItem
	 * @return Results of the ModifyMonitoredItem-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public MonitoredItemModifyResult[] modifyMonitoredItem(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger[] monitoredItemIds, UnsignedInteger[] clientHandles, Boolean[] discardOldest,
			ExtensionObject[] filters, UnsignedInteger[] queueSize, Double[] samplingInterval,
			TimestampsToReturn timestampToReturn) throws ServiceResultException {
		return this.profileManager.modifyMonitoredItems(session, subscriptionId, monitoredItemIds, clientHandles,
				discardOldest, filters, queueSize, samplingInterval, timestampToReturn, this.asyncApi);
	}

	/**
	 * Processes a Modify-Subscription Service.
	 * 
	 * This Service is used to modify a Subscription.
	 * 
	 * @param Session                    Session which is used to send the service,
	 *                                   or NULL for the current active session.
	 * @param SubscriptionId             Server-assigned identifier for the
	 *                                   Subscription
	 * @param PublishingInterval         This interval defines the cyclic rate that
	 *                                   the Subscription is being requested to
	 *                                   return Notifications to the Client. This
	 *                                   interval is expressed in milliseconds.
	 * @param MaxNotificationsPerPublish The maximum number of notifications that
	 *                                   the Client wishes to receive in a single
	 *                                   Publish response. A value of 0 indicates
	 *                                   that there is no limit.
	 * @param MaxKeepAliveCount          When the publishing timer has expired this
	 *                                   number of times without requiring any
	 *                                   NotificationMessage to be sent, the
	 *                                   Subscription sends a keep-alive Message to
	 *                                   the Client.
	 * @param Priority                   Indicates the relative priority of the
	 *                                   Subscription. When more than one
	 *                                   Subscription needs to send Notifications,
	 *                                   the Server should dequeue a Publish request
	 *                                   to the Subscription with the highest
	 *                                   priority number.
	 * @param MaxLifetimeCount           The lifetime count shall be a mimimum of
	 *                                   three times the keep keep-alive count. When
	 *                                   the publishing timer has expired this
	 *                                   number of times without a Publish request
	 *                                   being available to send a
	 *                                   NotificationMessage, then the Subscription
	 *                                   shall be deleted by the Server.
	 * @return Result from the ModifySubscription-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public ModifySubscriptionResponse modifySubscription(ClientSession session, UnsignedInteger subscriptionId,
			Double publishingInterval, UnsignedInteger maxNotificationsPerPublish, UnsignedInteger maxKeepAliveCount,
			UnsignedByte priority, UnsignedInteger maxLifetimeCount) throws ServiceResultException {
		return this.profileManager.modifySubscription(session, subscriptionId, publishingInterval,
				maxNotificationsPerPublish, maxKeepAliveCount, priority, maxLifetimeCount, this.asyncApi);
	}

	/**
	 * Processes a Read Service.
	 * 
	 * This Service is used to read one Attribute of one Node. For constructed
	 * Attribute values whose elements are indexed, such as an array, this Service
	 * allows Clients to read the entire set of indexed values as a composite, to
	 * read individual elements or to read ranges of elements of the composite.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param NodeToRead        NodeId of the Node to read.
	 * @param AttributeId       Id of the Attribute to read from the Node.
	 * @param IndexRange        IndexRange of the value e.g.: ("1:2")
	 * @param DataEncoding      Qualified data encoding
	 * @param MaxAge            The maxAge parameter is used to direct the Server to
	 *                          access the value from the underlying data source,
	 *                          such as a device, if its copy of the data is older
	 *                          than that which the maxAge specifies.
	 * @param TimestampToReturn Timestamp to be returned for each requested Variable
	 *                          Value Attribute.
	 * @return DataValue of the ReadService.
	 * 
	 * @throws ServiceResultException
	 */
	public DataValue read1(ClientSession session, NodeId nodeToRead, UnsignedInteger attributeId, String indexRange,
			QualifiedName dataEncoding, double maxAge, TimestampsToReturn timestampToReturn)
			throws ServiceResultException {
		DataValue[] result = this.profileManager.read1(session, new NodeId[] { nodeToRead },
				new UnsignedInteger[] { attributeId }, new String[] { indexRange },
				new QualifiedName[] { dataEncoding }, maxAge, timestampToReturn, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a Read Service.
	 * 
	 * This Service is used to read one or more DataAccess values of one or more
	 * Node. For constructed Attribute values whose elements are indexed, such as an
	 * array, this Service allows Clients to read the entire set of indexed values
	 * as a composite, to read individual elements or to read ranges of elements of
	 * the composite.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param NodeToRead        NodeId of the Node to read.
	 * @param AttributeId       Id of the Attribute to read from the Node.
	 * @param IndexRange        IndexRange of the value e.g.: ("1:2")
	 * @param DataEncoding      Qualified data encoding
	 * @param MaxAge            The maxAge parameter is used to direct the Server to
	 *                          access the value from the underlying data source,
	 *                          such as a device, if its copy of the data is older
	 *                          than that which the maxAge specifies.
	 * @param TimestampToReturn Timestamp to be returned for each requested Variable
	 *                          Value Attribute.
	 * 
	 * @return DataValue of the ReadService.
	 * @throws ServiceResultException
	 */
	public DataValue[] readDA(ClientSession session, NodeId[] nodeToRead, boolean enableValueChange,
			boolean validEURange, boolean validInstrumentRange, boolean validEUInformation, double maxAge,
			TimestampsToReturn timestampToReturn) throws ServiceResultException {
		return this.profileManager.readDA(session, nodeToRead, maxAge, timestampToReturn, enableValueChange,
				validEURange, validInstrumentRange, validEUInformation, this.asyncApi).getResults();
	}

	/**
	 * Processes a Read Service.
	 * 
	 * This Service is used to read one or more Attributes of one or more Nodes. For
	 * constructed Attribute values whose elements are indexed, such as an array,
	 * this Service allows Clients to read the entire set of indexed values as a
	 * composite, to read individual elements or to read ranges of elements of the
	 * composite.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param NodesToRead       NodeId of the Node to read.
	 * @param AttributeIds      Id of the Attribute to read from the Node.
	 * @param IndexRanges       IndexRange of the value e.g.: ("1:2")
	 * @param DataEncodings     Qualified data encoding
	 * @param MaxAge            The maxAge parameter is used to direct the Server to
	 *                          access the value from the underlying data source,
	 *                          such as a device, if its copy of the data is older
	 *                          than that which the maxAge specifies.
	 * @param TimestampToReturn Timestamp to be returned for each requested Variable
	 *                          Value Attribute.
	 * @return DataValues of the Read Service.
	 * 
	 * @throws ServiceResultException
	 */
	public DataValue[] read1(ClientSession session, NodeId[] nodesToRead, UnsignedInteger[] attributeIds,
			String[] indexRanges, QualifiedName[] dataEncodings, double maxAge, TimestampsToReturn timestampToReturn)
			throws ServiceResultException {
		return this.profileManager.read1(session, nodesToRead, attributeIds, indexRanges, dataEncodings, maxAge,
				timestampToReturn, this.asyncApi);
	}

	/**
	 * Reads one�Node with its Attributes.
	 * 
	 * @param Session Session which is used to send the service, or NULL for the
	 *                current active session.
	 * @param NodeId  NodeId of the node.
	 * @return Read node or NULL.
	 * 
	 * @throws ServiceResultException
	 */
	public Node readNode(ClientSession session, NodeId nodeId, NodeClass nodeClass) throws ServiceResultException {
		Node[] node = this.readNode(session, new NodeId[] { nodeId }, new NodeClass[] { nodeClass });
		if (node != null && node.length > 0) {
			return node[0];
		}
		return null;
	}

	/**
	 * Read one or more Nodes with their Attributes.
	 * 
	 * @param Session Session which is used to send the service, or NULL for the
	 *                current active session.
	 * @param NodeIds NodeIds of the nodes.
	 * @return Read nodes or NULL.
	 * 
	 * @throws ServiceResultException
	 */
	public Node[] readNode(ClientSession session, NodeId[] nodeIds, NodeClass[] nodeClasses)
			throws ServiceResultException {
		return this.profileManager.readNodes(session, nodeIds, nodeClasses, this.asyncApi);
	}

	/**
	 * Processes a Register-Node Service.
	 * 
	 * The RegisterNodes Service can be used by Clients to register the Nodes that
	 * they know they will access repeatedly (e.g. Write, Call). It allows Servers
	 * to set up anything needed so that the access operations will be more
	 * efficient. Registered NodeIds are only guaranteed to be valid within the
	 * current Session. Clients shall unregister unneeded Ids immediately to free up
	 * resources.
	 * 
	 * @param Session        Session which is used to send the service, or NULL for
	 *                       the current active session.
	 * @param NodeToRegister NodeId to register that the client has retrieved
	 *                       through browsing, querying or in some other manner.
	 * @return Response of the RegisterNode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public RegisterNodesResponse registerNode(ClientSession session, NodeId nodeToRegister)
			throws ServiceResultException {
		return this.profileManager.registerNodes(session, new NodeId[] { nodeToRegister }, this.asyncApi);
	}

	/**
	 * Processes a Register-Node Service.
	 * 
	 * The RegisterNodes Service can be used by Clients to register the Nodes that
	 * they know they will access repeatedly (e.g. Write, Call). It allows Servers
	 * to set up anything needed so that the access operations will be more
	 * efficient. Registered NodeIds are only guaranteed to be valid within the
	 * current Session. Clients shall unregister unneeded Ids immediately to free up
	 * resources.
	 * 
	 * @param Session        Session which is used to send the service, or NULL for
	 *                       the current active session.
	 * @param NodeToRegister NodeIds to register that the client has retrieved
	 *                       through browsing, querying or in some other manner.
	 * @return Response of the RegisterNode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public RegisterNodesResponse registerNode(ClientSession session, NodeId[] nodesToRegister)
			throws ServiceResultException {
		return this.profileManager.registerNodes(session, nodesToRegister, this.asyncApi);
	}

	/**
	 * Processes a SetMonitoringMode-Service.
	 * 
	 * This Service is used to set the monitoring mode for one MonitoredItem of a
	 * Subscription.
	 * 
	 * @param Session         Session which is used to send the service, or NULL for
	 *                        the current active session.
	 * @param SubscriptionId  The Server-assigned identifier for the Subscription
	 *                        used to qualify the monitoredItemIds.
	 * @param MonitoredItemId Server-assigned Id for the MonitoredItem.
	 * @param MonitoringMode  The monitoring mode to be set for the MonitoredItems.
	 * @return StatusCode of the SetMonitoringMode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode setMonitoringMode(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger monitoredItemId, MonitoringMode monitoringMode) throws ServiceResultException {
		StatusCode[] result = this.profileManager.setMonitoringMode(session, subscriptionId,
				new UnsignedInteger[] { monitoredItemId }, monitoringMode, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a SetMonitoringMode Service.
	 * 
	 * This Service is used to set the monitoring mode for one or more
	 * MonitoredItems of a Subscription.
	 * 
	 * @param Session          Session which is used to send the service, or NULL
	 *                         for the current active session.
	 * @param SubscriptionId   The Server-assigned identifier for the Subscription
	 *                         used to qualify the monitoredItemIds
	 * @param MonitoredItemIds Server-assigned Ids for the MonitoredItems.
	 * @param MonitoringMode   The monitoring mode to be set for the MonitoredItems.
	 * @return StatusCodes of the SetMonitoringMode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] setMonitoringMode(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger[] monitoredItemIds, MonitoringMode monitoringMode) throws ServiceResultException {
		return this.profileManager.setMonitoringMode(session, subscriptionId, monitoredItemIds, monitoringMode,
				this.asyncApi);
	}

	/**
	 * Processes a SetPublishingMode-Service.
	 * 
	 * This Service is used to enable sending of Notifications on one Subscriptions.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SubscriptionId    Server-assigned Ids for the Subscription to enable
	 *                          or disable.
	 * @param PublishingEnabled If the value is TRUE, publishing of
	 *                          NotificationMessages is enabled for the
	 *                          Subscription. If the value is FALSE, publishing of
	 *                          NotificationMessages is disabled for the
	 *                          Subscription.
	 * @return StatusCode of the SetPublishingMode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode setPublishingMode(ClientSession session, UnsignedInteger subscriptionId,
			boolean publishingEnabled) throws ServiceResultException {
		StatusCode[] result = this.profileManager.setPublishingMode(session, new UnsignedInteger[] { subscriptionId },
				publishingEnabled, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a SetPublishingMode-Service.
	 * 
	 * This Service is used to enable sending of Notifications on one or more
	 * Subscriptions
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SubscriptionIds   Server-assigned Ids for the Subscriptions to enable
	 *                          or disable
	 * @param PublishingEnabled If the value is TRUE, publishing of
	 *                          NotificationMessages is enabled for the
	 *                          Subscription. If the value is FALSE, publishing of
	 *                          NotificationMessages is disabled for the
	 *                          Subscription.
	 * @return StatusCodes of the SetPublishingMode-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] setPublishingMode(ClientSession session, UnsignedInteger[] subscriptionId,
			boolean publishingEnabled) throws ServiceResultException {
		return this.profileManager.setPublishingMode(session, subscriptionId, publishingEnabled, this.asyncApi);
	}

	/**
	 * Processes a SetTriggering-Service.
	 * 
	 * This Service is used to create and remove triggering links for a triggering
	 * item. The triggering item and the items to report shall belong to the same
	 * Subscription. Each triggering link links a triggering item to an item to
	 * report. Each link is represented by the MonitoredItem id for the item to
	 * report.
	 * 
	 * @param Session          Session which is used to send the service, or NULL
	 *                         for the current active session.
	 * @param SubscriptionId   Server-assigned identifier for the Subscription that
	 *                         contains the triggering item and the items to report
	 * @param TriggeringItemId Server-assigned id for the MonitoredItem used as the
	 *                         triggering item.
	 * @param LinksToAdd       Server-assigned ids of the items to report that are
	 *                         to be added as triggering links.
	 * @param LinksToRemove    Server-assigned ids of the items to report for the
	 *                         triggering links to be deleted.
	 * 
	 * @return StatusCodes of the AddLink SetTriggering-Service
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] setTriggering(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger triggeringItemId, UnsignedInteger[] linksToAdd, UnsignedInteger[] linksToRemove)
			throws ServiceResultException {
		return this.profileManager.setTriggering(session, subscriptionId, triggeringItemId, linksToAdd, linksToRemove,
				this.asyncApi);
	}

	/**
	 * Processes a SetTriggering-Service.
	 * 
	 * This Service is used to create triggering links for a triggering item. The
	 * triggering item and the items to report shall belong to the same
	 * Subscription. Each triggering link links a triggering item to an item to
	 * report. Each link is represented by the MonitoredItem id for the item to
	 * report.
	 * 
	 * @param Session          Session which is used to send the service, or NULL
	 *                         for the current active session.
	 * @param SubscriptionId   Server-assigned identifier for the Subscription that
	 *                         contains the triggering item and the items to report
	 * @param TriggeringItemId Server-assigned id for the MonitoredItem used as the
	 *                         triggering item.
	 * @param LinksToAdd       Server-assigned ids of the items to report that are
	 *                         to be added as triggering links.
	 * 
	 * @return StatusCodes of the AddLink SetTriggering-Service
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] setTriggeringAddLink(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger triggeringItemId, UnsignedInteger... linksToAdd) throws ServiceResultException {
		return this.profileManager.setTriggering(session, subscriptionId, triggeringItemId, linksToAdd,
				new UnsignedInteger[0], this.asyncApi);
	}

	/**
	 * Processes a SetTriggering-Service.
	 * 
	 * This Service is used to delete triggering links for a triggering item. The
	 * triggering item and the items to report shall belong to the same
	 * Subscription. Each triggering link links a triggering item to an item to
	 * report. Each link is represented by the MonitoredItem id for the item to
	 * report.
	 * 
	 * @param Session          Session which is used to send the service, or NULL
	 *                         for the current active session.
	 * @param SubscriptionId   Server-assigned identifier for the Subscription that
	 *                         contains the triggering item and the items to report
	 * @param TriggeringItemId Server-assigned id for the MonitoredItem used as the
	 *                         triggering item.
	 * @param LinksToRemove    Server-assigned ids of the items to report for the
	 *                         triggering links to be deleted.
	 * 
	 * @return StatusCodes of the RemoveLink SetTriggering-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] setTriggeringRemoveLink(ClientSession session, UnsignedInteger subscriptionId,
			UnsignedInteger triggeringItemId, UnsignedInteger... linksToRemove) throws ServiceResultException {
		return this.profileManager.setTriggering(session, subscriptionId, triggeringItemId, new UnsignedInteger[0],
				linksToRemove, this.asyncApi);
	}

	/**
	 * Processes a TransferSubscription-Service.
	 * 
	 * This Service is used to transfer a Subscription and its MonitoredItems from
	 * one Session to another. For example, a Client may need to reopen a Session
	 * and then transfer its Subscriptions to that Session. It may also be used by
	 * one Client to take over a Subscription from another Client by transferring
	 * the Subscription to its Session.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param TargetSessionId   Session on which the Subscription will be
	 *                          transfered.
	 * @param SubscriptionId    Id for the Subscription to be transferred to the new
	 *                          Client.
	 * @param SendInitialValues If the value is TRUE the first Publish response
	 *                          after the TransferSubscriptions call shall contain
	 *                          the current values of all Monitored Items in the
	 *                          Subscription where the Monitoring Mode is set to
	 *                          Reporting. If the value is FALSE, the first Publish
	 *                          response after the TransferSubscriptions call shall
	 *                          contain only the value changes since the last
	 *                          Publish response was sent.
	 * @return Result of the TransfereSubscription-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public TransferResult transfereSubscription(ClientSession session, NodeId targetSessionId,
			UnsignedInteger subscriptionId, boolean sendInitialValues) throws ServiceResultException {
		TransferResult[] result = this.profileManager.transfereSubscription(session, targetSessionId,
				new UnsignedInteger[] { subscriptionId }, sendInitialValues, this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a Transfer-Subscription Service.
	 * 
	 * This Service is used to transfer a Subscription and its MonitoredItems from
	 * one Session to another. For example, a Client may need to reopen a Session
	 * and then transfer its Subscriptions to that Session. It may also be used by
	 * one Client to take over a Subscription from another Client by transferring
	 * the Subscription to its Session.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param SubscriptionIds   Ids for the Subscriptions to be transferred to the
	 *                          new Client
	 * @param SendInitialValues If the value is TRUE the first Publish response
	 *                          after the TransferSubscriptions call shall contain
	 *                          the current values of all Monitored Items in the
	 *                          Subscription where the Monitoring Mode is set to
	 *                          Reporting. If the value is FALSE, the first Publish
	 *                          response after the TransferSubscriptions call shall
	 *                          contain only the value changes since the last
	 *                          Publish response was sent.
	 * @return Results of the TransfereSubscription-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public TransferResult[] transfereSubscription(ClientSession session, NodeId targetSessionId,
			UnsignedInteger[] subscriptionIds, boolean sendInitialValues) throws ServiceResultException {
		return this.profileManager.transfereSubscription(session, targetSessionId, subscriptionIds, sendInitialValues,
				this.asyncApi);
	}

	/**
	 * Processes a Translate-Browsepath-To-NodeId Service.
	 * 
	 * This Service is used to request that the Server translates one browse path to
	 * NodeIds. Each browse path is constructed of a starting Node and a
	 * RelativePath. The specified starting Node identifies the Node from which the
	 * RelativePath is based. The RelativePath contains a sequence of ReferenceTypes
	 * and BrowseNames.
	 * 
	 * @param Session       Session which is used to send the service, or NULL for
	 *                      the current active session.
	 * @param StartNodeId   NodeId of the starting Node for the browse path.
	 * @param RelativePaths The path to follow from the startingNode. The last
	 *                      element in the relativePath shall always have a
	 *                      targetName specified.
	 * @param IsInverse     Direction to browse.
	 * @return Results of the TranslateBrowsePathsToNodeIds Service.
	 * 
	 * @throws ServiceResultException
	 */
	public BrowsePathResult translateBrowsePathToNodeIds(ClientSession session, NodeId startNodeId,
			QualifiedName[] relativePaths, Boolean isInverse) throws ServiceResultException {
		NodeId[] id = { startNodeId };
		QualifiedName[][] path = { relativePaths };
		Boolean[] inverse = { isInverse };
		BrowsePathResult[] result = this.profileManager.translateBrowsePathToNodeIds(session, id, path, inverse,
				this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a Translate-Browsepath-To-NodeId Service.
	 * 
	 * This Service is used to request that the Server translates one or more browse
	 * paths to NodeIds. Each browse path is constructed of a starting Node and a
	 * RelativePath. The specified starting Node identifies the Node from which the
	 * RelativePath is based. The RelativePath contains a sequence of ReferenceTypes
	 * and BrowseNames.
	 * 
	 * @param Session       Session which is used to send the service, or NULL for
	 *                      the current active session.
	 * @param StartNodeIds  NodeIds of the starting Nodes for the browse path.
	 * @param RelativePaths The path to follow from the startingNode. The last
	 *                      element in the relativePath shall always have a
	 *                      targetName specified.
	 * @param IsInverse     Direction to browse.
	 * @return Results of the TranslateBrowsePathsToNodeIds Service.
	 * 
	 * @throws ServiceResultException
	 */
	public BrowsePathResult[] translateBrowsePathToNodeIds(ClientSession session, NodeId[] startNodeIds,
			QualifiedName[][] relativePaths, Boolean[] isInverse) throws ServiceResultException {
		return this.profileManager.translateBrowsePathToNodeIds(session, startNodeIds, relativePaths, isInverse,
				this.asyncApi);
	}

	/**
	 * Processes a Unregister-Node Service.
	 * 
	 * This Service is used to unregister NodeIds that have been obtained via the
	 * Register-Node Service.
	 * 
	 * @param Session          Session which is used to send the service, or NULL
	 *                         for the current active session.
	 * @param NodeToUnregister NodeId that have been obtained via the RegisterNode
	 *                         service.
	 * @return TRUE if the UnRegister-Node Service was successful, otherwise FALSE.
	 * 
	 * @throws ServiceResultException
	 */
	public Boolean unregisterNode(ClientSession session, NodeId nodeToUnregister) throws ServiceResultException {
		Boolean[] result = this.profileManager.unregisterNodes(session, new NodeId[] { nodeToUnregister },
				this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return false;
	}

	/**
	 * Processes a Unregister-Node Service.
	 * 
	 * This Service is used to unregister NodeIds that have been obtained via the
	 * Register-Node Service.
	 * 
	 * @param Session           Session which is used to send the service, or NULL
	 *                          for the current active session.
	 * @param NodesToUnregister NodeIds that have been obtained via the RegisterNode
	 *                          service
	 * @return TRUE if the RegisterNode Service was successful, otherwise FALSE.
	 * 
	 * @throws ServiceResultException
	 */
	public Boolean[] unregisterNode(ClientSession session, NodeId[] nodesToUnregister) throws ServiceResultException {
		return this.profileManager.unregisterNodes(session, nodesToUnregister, this.asyncApi);
	}

	/**
	 * Processes a Write Service.
	 * 
	 * This Service is used to write values to one Attributes of one Node. For
	 * constructed Attribute values whose elements are indexed, such as an array,
	 * this Service allows Clients to write the entire set of indexed values as a
	 * composite, to write individual elements or to write ranges of elements of the
	 * composite.
	 * 
	 * @param Session     Session which is used to send the service, or NULL for the
	 *                    current active session.
	 * @param NodeToWrite NodeId of the Node that contains the Attributes.
	 * @param AttributeId Id of the attribute to write.
	 * @param IndexRange  This parameter is used to identify a single element of an
	 *                    array, or a single range of indexes for arrays. The first
	 *                    element is identified by index 0.NULL Parameter ignores a
	 *                    IndexBased-Write.
	 * @param Value       The Node�s Attribute value.
	 * 
	 * @return StatusCode of the service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode write1(ClientSession session, NodeId nodeToWrite, UnsignedInteger attributeId, String indexRange,
			DataValue value) throws ServiceResultException {
		StatusCode[] result = this.profileManager.write1(session, new NodeId[] { nodeToWrite },
				new UnsignedInteger[] { attributeId }, new String[] { indexRange }, new DataValue[] { value },
				this.asyncApi);
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/**
	 * Processes a Write Service.
	 * 
	 * This Service is used to write values to one Attributes of one Node. For
	 * constructed Attribute values whose elements are indexed, such as an array,
	 * this Service allows Clients to write the entire set of indexed values as a
	 * composite, to write individual elements or to write ranges of elements of the
	 * composite.
	 * 
	 * @param Session     Session which is used to send the service, or NULL for the
	 *                    current active session.
	 * @param NodeToWrite NodeId of the Node that contains the Attributes.
	 * @param AttributeId Id of the attribute to write.
	 * @param IndexRange  This parameter is used to identify a single element of an
	 *                    array, or a single range of indexes for arrays. The first
	 *                    element is identified by index 0.NULL Parameter ignores a
	 *                    IndexBased-Write.
	 * @param Value       The Node�s Attribute value.
	 * 
	 * @return WriteResponse of the Write-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public WriteResponse write2(ClientSession session, NodeId nodeToWrite, UnsignedInteger attributeId,
			String indexRange, DataValue value) throws ServiceResultException {
		return this.profileManager.write2(session, new NodeId[] { nodeToWrite }, new UnsignedInteger[] { attributeId },
				new String[] { indexRange }, new DataValue[] { value }, this.asyncApi);
	}

	/**
	 * Processes a Write Service.
	 * 
	 * This Service is used to write values to one or more Attributes of one or more
	 * Nodes. For constructed Attribute values whose elements are indexed, such as
	 * an array, this Service allows Clients to write the entire set of indexed
	 * values as a composite, to write individual elements or to write ranges of
	 * elements of the composite.
	 * 
	 * @param Session      Session which is used to send the service, or NULL for
	 *                     the current active session.
	 * @param NodesToWrite NodeIds of the Nodes that contain the Attributes.
	 * @param AttributeIds Ids of the attributes to write.
	 * @param IndexRanges  This parameter is used to identify a single element of an
	 *                     array, or a single range of indexes for arrays. The first
	 *                     element is identified by index 0.
	 * @param Values       The Value to write.
	 * @return result of the service.
	 * 
	 * @throws ServiceResultException
	 */
	public StatusCode[] write1(ClientSession session, NodeId[] nodesToWrite, UnsignedInteger[] attributeIds,
			String[] indexRanges, DataValue[] values) throws ServiceResultException {
		return this.profileManager.write1(session, nodesToWrite, attributeIds, indexRanges, values, this.asyncApi);
	}

	/**
	 * Processes a Write Service.
	 * 
	 * This Service is used to write values to one or more Attributes of one or more
	 * Nodes. For constructed Attribute values whose elements are indexed, such as
	 * an array, this Service allows Clients to write the entire set of indexed
	 * values as a composite, to write individual elements or to write ranges of
	 * elements of the composite.
	 * 
	 * @param Session      Session which is used to send the service, or NULL for
	 *                     the current active session.
	 * @param NodesToWrite NodeIds of the Nodes that contain the Attributes.
	 * @param AttributeIds Ids of the attributes to write.
	 * @param IndexRanges  This parameter is used to identify a single element of an
	 *                     array, or a single range of indexes for arrays. The first
	 *                     element is identified by index 0.
	 * @param Values       The Value to write.
	 * @return WriteResponse of the Write-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public WriteResponse write2(ClientSession session, NodeId[] nodesToWrite, UnsignedInteger[] attributeIds,
			String[] indexRanges, DataValue[] values) throws ServiceResultException {
		return this.profileManager.write2(session, nodesToWrite, attributeIds, indexRanges, values, this.asyncApi);
	}

	/**
	 * Processes a Write Service.
	 * 
	 * This Service is used to write a dataaccess values to one Attributes of one
	 * Node. For constructed Attribute values whose elements are indexed, such as an
	 * array, this Service allows Clients to write the entire set of indexed values
	 * as a composite, to write individual elements or to write ranges of elements
	 * of the composite.
	 * 
	 * @param Session      Session which is used to send the service, or NULL for
	 *                     the current active session.
	 * @param NodesToWrite NodeIds of the Nodes that contain the Attributes.
	 * @param AttributeIds Ids of the attributes to write.
	 * @param IndexRanges  This parameter is used to identify a single element of an
	 *                     array, or a single range of indexes for arrays. The first
	 *                     element is identified by index 0.
	 * @param Values       The Value to write.
	 * @return WriteResponse of the Write-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public WriteResponse writeDA(ClientSession session, NodeId nodesToWrite, DataValue value, boolean validEURange,
			boolean validInstrumentRange, boolean validEUInformation) throws ServiceResultException {
		return writeDA(session, new NodeId[] { nodesToWrite }, new DataValue[] { value }, validEURange,
				validInstrumentRange, validEUInformation);
	}

	/**
	 * Processes a Write Service.
	 * 
	 * This Service is used to write a dataaccess values to one or more Attributes
	 * of one or more Nodes. For constructed Attribute values whose elements are
	 * indexed, such as an array, this Service allows Clients to write the entire
	 * set of indexed values as a composite, to write individual elements or to
	 * write ranges of elements of the composite.
	 * 
	 * @param Session      Session which is used to send the service, or NULL for
	 *                     the current active session.
	 * @param NodesToWrite NodeIds of the Nodes that contain the Attributes.
	 * @param AttributeIds Ids of the attributes to write.
	 * @param IndexRanges  This parameter is used to identify a single element of an
	 *                     array, or a single range of indexes for arrays. The first
	 *                     element is identified by index 0.
	 * @param Values       The Value to write.
	 * @return WriteResponse of the Write-Service.
	 * 
	 * @throws ServiceResultException
	 */
	public WriteResponse writeDA(ClientSession session, NodeId[] nodesToWrite, DataValue[] values, boolean validEURange,
			boolean validInstrumentRange, boolean validEUInformation) throws ServiceResultException {
		return this.profileManager.writeDA(session, nodesToWrite, values, validEURange, validInstrumentRange,
				validEUInformation, this.asyncApi);
	}

	/**
	 * Changes the current active session to use inside the framework.
	 * 
	 * @param Session Set the default used session. This session is used every time,
	 *                when a service is sent with the session parameter NULL.
	 */
	public void changeActiveSession(ClientSession session) {
		this.profileManager.getSessionManager().changeActiveSession(session);
	}

	/**
	 * Returns the active session of the client framework. Changing an active
	 * session is done with the changeActiveSession() method.
	 * 
	 * @return Active session of this client instance.
	 */
	public ClientSession getActiveSession() {
		return this.profileManager.getSessionManager().getActiveSession();
	}

	/**
	 * Validator for the Certificates.
	 * 
	 * @param CertValidator Validator for the certificates.
	 */
	public void setCertificationValidator(AllowNoneCertificateValidator certValidator) {
		this.clientApplicationConfiguration.setCertificateValidator(certValidator);
	}

	/**
	 * Gets the client configuration.
	 * 
	 * @return the client application configuration.
	 */
	public ApplicationConfiguration getApplicationConfiguration() {
		return this.clientApplicationConfiguration;
	}

	/**
	 * Gets the client configuration.
	 * 
	 * @return the client application configuration.
	 */
	public void setApplicationConfiguration(ApplicationConfiguration config) {
		this.clientApplicationConfiguration = config;
	}

	/**
	 * Configurate a client application from a ClientConfiguration and a
	 * ConfigurationFile with its defualt location.
	 * 
	 * Default absolute locations: /appConfig/clientconfig.xml,
	 * /Cert/ClientCertificate.der, /Cert/ClientKey.pfx
	 * 
	 * @throws IOException
	 * @throws ServiceResultException
	 */
	public void setClientConfiguration() throws ServiceResultException {
		File configFile;
		try {
			// create client configuration
			configFile = new File(ClientConfiguration.CONFIGURATION_FILE_PATH + File.pathSeparator
					+ ClientConfiguration.CONFIGURATION_FILE);
			this.clientApplicationConfiguration = new ApplicationConfiguration(new FileInputStream(configFile));
		} catch (FileNotFoundException | JDOMException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		}
		File certFile = new File(
				ClientConfiguration.getCertificatePath() + File.pathSeparator + ClientConfiguration.CERTIFICATE_NAME);
		File keyFile = new File(
				ClientConfiguration.getCertificatePath() + File.pathSeparator + ClientConfiguration.PRIVATE_KEY_NAME);
		setClientConfiguration(configFile, certFile, keyFile);
	}

	/**
	 * Configurate a client application from a ClientConfiguration and a
	 * ConfigurationFile.
	 * 
	 * @param Config  Client configuration file.
	 * @param Cert    Client public certificate file.
	 * @param PrivKey Client private key file.
	 * @throws ServiceResultException
	 * @throws JDOMException
	 */
	public void setClientConfiguration(File config, File cert, File privKey) throws ServiceResultException {
		// no applicationconfiguration available
		try {
			if (this.clientApplicationConfiguration == null) {
				setApplicationConfiguration(config);
			}
			String password = this.clientApplicationConfiguration.getSecurityConfiguration().getPassword();
			// Certificate & Private Key
			Cert clientCertificate = null;
			PrivKey clientPrivateKey = null;
			KeyPair clientApplicationInstanceCertificate = null;
			try {
				// Import client certificate and privatekey
				clientCertificate = Cert.load(cert);
				clientPrivateKey = PrivKey.load(privKey.toURI().toURL().openStream(), password);
				clientApplicationInstanceCertificate = new KeyPair(clientCertificate, clientPrivateKey);
			} catch (Exception e1) {
				// create new client certificate and privatekey
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
				try {
					String hostName = InetAddress.getLocalHost().getHostName();
					clientApplicationInstanceCertificate = CertificateUtils.createApplicationInstanceCertificate(
							this.clientApplicationConfiguration.getSecurityConfiguration().getCommonName(),
							this.clientApplicationConfiguration.getSecurityConfiguration().getOrganisation(),
							this.clientApplicationConfiguration.getApplicationUri(), 365, hostName);
					// we need to change the self signed certificate
					clientApplicationInstanceCertificate.save(cert, privKey, password);
				} catch (Exception e2) {
					Logger.getLogger(AlarmConditionUtil.class.getName()).log(Level.SEVERE, e2.getMessage(), e2);
					throw new ServiceResultException(StatusCodes.Bad_CertificateInvalid,
							"The ClientCertificate is invalid! Could not create a valid Self-Signed-Certificate! "
									+ e2.getMessage());
				}
			}
			// store keypair
			this.clientApplicationConfiguration.setSecurityConfig(clientApplicationInstanceCertificate);
			// profile manager
			this.profileManager = new ProfileManager();
		} catch (IOException e) {
			Logger.getLogger(AlarmConditionUtil.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private void setApplicationConfiguration(File config) throws IOException, ServiceResultException {
		try {
			// create client configuration
			this.clientApplicationConfiguration = new ApplicationConfiguration(config.toURI().toURL().openStream());
		} catch (JDOMException e) {
			Logger.getLogger(AlarmConditionUtil.class.getName()).log(Level.SEVERE, null, e);
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError,
					"The Application configuration has invalid content! " + e.getMessage());
		}
	}

	/**
	 * Sets weather the sync or async api should used to send services over the
	 * stack.
	 * 
	 * @param AsyncApi TRUE uses the async api, FALSE uses the sync api.
	 * 
	 */
	public void setAsyncApi(boolean asyncApi) {
		this.asyncApi = asyncApi;
	}

	/**
	 * Gets the session manager.
	 * 
	 * @return SessionManager.
	 */
	public SessionManager getSessionManager() {
		return this.profileManager.getSessionManager();
	}

	/**
	 * Gets the subscription manager.
	 * 
	 * @return SubscriptionManager.
	 */
	public SubscriptionManager getSubscriptionManager() {
		return this.profileManager.getSubscriptionManager();
	}

	public void setProfileManager(ProfileManager profileManager) {
		this.profileManager = profileManager;
	}

	public long getFutureServerTimeout() {
		return futureServerTimeout;
	}

	public void setFutureServerTimeout(long futureServerTimeout) {
		this.futureServerTimeout = futureServerTimeout;
	}

	public void setDefaultKeepAliveInterval(long keepAlive) {
		this.defaultKeepAliveInterval = keepAlive;
	}

	public long tgetDefaultKeepAliveInterval() {
		return this.defaultKeepAliveInterval;
	}
}
