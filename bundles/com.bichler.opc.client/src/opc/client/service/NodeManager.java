package opc.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResponse;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesRequest;
import org.opcfoundation.ua.core.AddNodesResponse;
import org.opcfoundation.ua.core.AddReferencesRequest;
import org.opcfoundation.ua.core.AddReferencesResponse;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.CallResponse;
import org.opcfoundation.ua.core.DeleteNodesRequest;
import org.opcfoundation.ua.core.DeleteNodesResponse;
import org.opcfoundation.ua.core.DeleteReferencesRequest;
import org.opcfoundation.ua.core.DeleteReferencesResponse;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.RegisterNodesRequest;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.UnregisterNodesRequest;
import org.opcfoundation.ua.core.UnregisterNodesResponse;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.utils.AttributesUtil;

import opc.client.application.core.method.MethodElement;
import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.utils.Utils;

/**
 * This service manager defines services to add and delete UA addressSpace nodes
 * and references between them.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class NodeManager {
	private static final String ATINDEX = " at index: ";
	private DataAccessManager dataAccessManager;
	private MethodManager methodManager;

	public NodeManager() {
		this.dataAccessManager = new DataAccessManager();
		this.methodManager = new MethodManager();
	}

	public DataAccessManager getDataAccessManager() {
		return this.dataAccessManager;
	}

	/**
	 * Sends the {@link AddNodesRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link AddNodesRequest} with the information to create a new
	 *                node on the server.
	 * @return {@link AddNodesResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected AddNodesResponse addNodes(ClientSession session, AddNodesRequest request) throws ServiceResultException {
		return session.addNodes(request);
	}

	/**
	 * Sends the {@link AddReferencesRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link AddReferencesRequest} with the information to create a
	 *                new reference on the server.
	 * @return {@link AddReferencesResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected AddReferencesResponse addReferences(ClientSession session, AddReferencesRequest request)
			throws ServiceResultException {
		return session.addReferences(request);
	}

	/**
	 * Sends the {@link CallRequest} to the server.
	 * 
	 * @param elements2call
	 * 
	 * @param Session       Session used to send the Request.
	 * @param Request       {@link CallRequest} to call a Method on the server.
	 * @return {@link CallResponse} of the service.
	 * @throws ServiceResultException
	 */
	protected CallResponse call(ClientSession session, CallRequest request, MethodElement[] elements2call)
			throws ServiceResultException {
		validateMethodCall(session, elements2call);
		return session.call(request);
	}

	/**
	 * Sends the {@link DeleteNodesRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link DeleteNodesRequest} to remove a Node from the
	 *                server�s AddressSpace.
	 * @return {@link DeleteNodesResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteNodesResponse deleteNodes(ClientSession session, DeleteNodesRequest request)
			throws ServiceResultException {
		return session.deleteNodes(request);
	}

	/**
	 * Sends the {@link DeleteReferencesRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link DeleteReferencesRequest} to remove a Reference from a
	 *                Node of the server�s AddressSpace.
	 * @return {@link DeleteReferencesResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteReferencesResponse deleteReferences(ClientSession session, DeleteReferencesRequest request)
			throws ServiceResultException {
		return session.deleteReferences(request);
	}

	/**
	 * *************************************************************************
	 * ***** historical section
	 *********************************************************************************/
	/**
	 * Sends the {@link ReadRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link ReadRequest} to read an Attribute of a Node.
	 * @return {@link ReadResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected HistoryReadResponse historyRead(ClientSession session, HistoryReadRequest request)
			throws ServiceResultException {
		return session.historyRead(request);
	}

	/**
	 * Sends the {@link ReadRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link ReadRequest} to read an Attribute of a Node.
	 * @return {@link ReadResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected HistoryUpdateResponse historyUpdate(ClientSession session, HistoryUpdateRequest request)
			throws ServiceResultException {
		return session.historyUpdate(request);
	}

	/**
	 * Sends the {@link ReadRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link ReadRequest} to read an Attribute of a Node.
	 * @return {@link ReadResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected ReadResponse read(ClientSession session, ReadRequest request, Boolean[] isEqualResult)
			throws ServiceResultException {
		ReadResponse response = session.read(request);
		boolean isEqual = endRead(request, response, session);
		if (isEqualResult != null) {
			isEqualResult[0] = isEqual;
		}
		return response;
	}

	protected ReadResponse readDA(ClientSession session, boolean validEURange, boolean validInstrumentRange,
			boolean validEUInformation, boolean enableValueChange, ReadRequest request) throws ServiceResultException {
		Map<Integer, Entry<NodeId, List<Entry<NodeId, LocalizedText>>>> dynamicNode2DataAccessType = new HashMap<>();
		NodeId[] nodeId = new NodeId[request.getNodesToRead().length];
		for (int i = 0; i < request.getNodesToRead().length; i++) {
			nodeId[i] = request.getNodesToRead()[i].getNodeId();
		}
		StatusCode[] codes = validateDAService(session, nodeId, dynamicNode2DataAccessType);
		Boolean[] isEqualResult = new Boolean[1];
		ReadResponse response = read(session, request, isEqualResult);
		this.dataAccessManager.evaluateDARead(session, request, response, codes, enableValueChange, isEqualResult[0],
				validEURange, validInstrumentRange, validEUInformation, dynamicNode2DataAccessType);
		return response;
	}

	/**
	 * Sends the {@link RegisterNodesRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link RegisterNodesRequest} to register Nodes on a Server, to
	 *                increase performance accessing the Node.
	 * @return {@link RegisterNodesResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected RegisterNodesResponse registerNodes(ClientSession session, RegisterNodesRequest request)
			throws ServiceResultException {
		return session.registerNodes(request);
	}

	/**
	 * Sends the {@link TranslateBrowsePathsToNodeIdsRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link TranslateBrowsePathsToNodeIdsRequest} StartingNodeIds
	 *                and RelativePathes to translate them to NodeIds
	 * @return {@link TranslateBrowsePathsToNodeIdsResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected TranslateBrowsePathsToNodeIdsResponse translateBrowsePathToNodeIds(ClientSession session,
			TranslateBrowsePathsToNodeIdsRequest request) throws ServiceResultException {
		return session.translateBrowsePathToNodeIds(request);
	}

	/**
	 * Sends the {@link UnregisterNodesRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link UnregisterNodesRequest} to unregister a Node node on
	 *                the server.
	 * @return {@link UnregisterNodesResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected UnregisterNodesResponse unregisterNodes(ClientSession session, UnregisterNodesRequest request)
			throws ServiceResultException {
		return session.unregisterNodes(request);
	}

	/**
	 * Sends the {@link WriteRequest} to the server.
	 * 
	 * @param Session Session used to send the Request.
	 * @param Request {@link WriteRequest} to write values on Nodes.
	 * @return {@link WriteResponse} of the service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected WriteResponse write(ClientSession session, WriteRequest request) throws ServiceResultException {
		WriteResponse response = session.write(request);
		endWrite(response, request);
		return response;
	}

	protected WriteResponse writeDA(ClientSession session, boolean validEURange, boolean validInstrumentRange,
			boolean validEUInformation, WriteRequest request) throws ServiceResultException {
		Map<Integer, Entry<NodeId, List<Entry<NodeId, LocalizedText>>>> dynamicNode2DataAccessType = new HashMap<>();
		NodeId[] nodeId = new NodeId[request.getNodesToWrite().length];
		DataValue[] values = new DataValue[request.getNodesToWrite().length];
		for (int i = 0; i < request.getNodesToWrite().length; i++) {
			nodeId[i] = request.getNodesToWrite()[i].getNodeId();
			values[i] = request.getNodesToWrite()[i].getValue();
		}
		validateDAService(session, nodeId, dynamicNode2DataAccessType);
		this.dataAccessManager.validateDAWrite(session, nodeId, values, validEURange, validInstrumentRange,
				validEUInformation, dynamicNode2DataAccessType);
		return write(session, request);
	}

	/**
	 * Begins an Asynchronous Add Node Service. After sending the Request to the
	 * Server, a Listener is used to acknowledge, when the service finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request AddNodes Request to add Nodes on a server�s AddressSpace.
	 * @return AsyncResult of the AddNodes Service
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected AddNodesResponse beginAddNodes(ClientSession session, AddNodesRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.AddNodesRequest;
		return session.beginAddNodes(request, requestId);
	}

	/**
	 * Begins an Asynchronous Add Reference Service. After sending the Request to
	 * the Server, a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request AddReference Request to add References on a server�s
	 *                AddressSpace.
	 * @return AsyncResult of the AddNodes Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected AddReferencesResponse beginAddReferences(ClientSession session, AddReferencesRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.AddReferencesRequest;
		return session.beginAddReferences(request, requestId);
	}

	/**
	 * Begins an Asynchronous Browse Service. After sending the Request to the
	 * Server, a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request Browse Request to browse on a server�s AddressSpace.
	 * @return AsyncResult of the Browse Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	/**
	 * Begins an Asynchronous Call Service. After sending the Request to the Server,
	 * a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param elements2call
	 * 
	 * @param Session       Session which is sending the Request.
	 * @param Request       Call Request to call a method on a server.
	 * @return AsyncResult of the Call Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected CallResponse beginCallMethod(ClientSession session, CallRequest request, MethodElement[] elements2call)
			throws ServiceResultException {
		NodeId requestId = Identifiers.CallRequest;
		validateMethodCall(session, elements2call);
		return session.beginCall(request, requestId);
	}

	/**
	 * Begins an Asynchronous DeleteNode Service. After sending the Request to the
	 * Server, a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request DeleteNode Request to remove a Node on a server�s
	 *                AddressSpace.
	 * @return AsyncResult of the DeleteNode Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteNodesResponse beginDeleteNodes(ClientSession session, DeleteNodesRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.DeleteNodesRequest;
		return session.beginDeleteNodes(request, requestId);
	}

	/**
	 * Begins an Asynchronous DeleteReference Service. After sending the Request to
	 * the Server, a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request DeleteReference Request to delete a Reference on a on a Node
	 *                in a server�s AddressSpace.
	 * @return AsyncResult of the DeleteReference Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteReferencesResponse beginDeleteReferences(ClientSession session, DeleteReferencesRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.DeleteReferencesRequest;
		return session.beginDeleteReferences(request, requestId);
	}

	/**
	 * Begins an Asynchronous Read Service. After sending the Request to the Server,
	 * a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request Read Request to read an attribute of a Node.
	 * @return AsyncResult of the Read Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected HistoryReadResponse beginHistoryRead(ClientSession session, HistoryReadRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.HistoryReadRequest;
		return session.beginHistoryRead(request, requestId);
	}

	/**
	 * Begins an Asynchronous Read Service. After sending the Request to the Server,
	 * a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request Read Request to read an attribute of a Node.
	 * @return HistoryUpdateResponse of the history update Service
	 * @throws ServiceResultException
	 */
	protected HistoryUpdateResponse beginHistoryUpdate(ClientSession session, HistoryUpdateRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.HistoryUpdateRequest;
		return session.beginHistoryUpdate(request, requestId);
	}

	/**
	 * Begins an Asynchronous Read Service. After sending the Request to the Server,
	 * a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request Read Request to read an attribute of a Node.
	 * @return AsyncResult of the Read Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected ReadResponse beginRead(ClientSession session, ReadRequest request, Boolean[] isEqualResult)
			throws ServiceResultException {
		NodeId requestId = Identifiers.ReadRequest;
		ReadResponse response = session.beginRead(request, requestId);
		boolean isEqual = endRead(request, response, session);
		if (isEqualResult != null) {
			isEqualResult[0] = isEqual;
		}
		return response;
	}

	protected ReadResponse beginReadDA(ClientSession session, boolean validEURange, boolean validInstrumentRange,
			boolean validEUInformation, boolean enableValueChange, ReadRequest request) throws ServiceResultException {
		// index || <TypeId, List<Property,DisplayName>
		Map<Integer, Entry<NodeId, List<Entry<NodeId, LocalizedText>>>> dynamicNode2DataAccessType = new HashMap<>();
		NodeId[] nodeId = new NodeId[request.getNodesToRead().length];
		for (int i = 0; i < request.getNodesToRead().length; i++) {
			nodeId[i] = request.getNodesToRead()[i].getNodeId();
		}
		StatusCode[] codes = validateDAService(session, nodeId, dynamicNode2DataAccessType);
		Boolean[] isEqualResult = new Boolean[1];
		ReadResponse response = beginRead(session, request, isEqualResult);
		this.dataAccessManager.evaluateDARead(session, request, response, codes, enableValueChange, isEqualResult[0],
				validEURange, validInstrumentRange, validEUInformation, dynamicNode2DataAccessType);
		return response;
	}

	protected Node[] readNodes(ClientSession session, NodeId[] nodeIds, NodeClass[] nodeClasses, boolean asyncOperation)
			throws ServiceResultException {
		return session.readNodes(nodeIds, nodeClasses, asyncOperation);
	}

	/**
	 * Begins an Asynchronous RegisterNode Service. After sending the Request to the
	 * Server, a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request RegisterNode Request to register Nodes on a Server, to
	 *                increase performance accessing the Node.
	 * @return AsyncResult of the DeleteReference Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected RegisterNodesResponse beginRegisterNodes(ClientSession session, RegisterNodesRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.RegisterNodesRequest;
		return session.beginRegisterNodes(request, requestId);
	}

	/**
	 * Begins an Asynchronous TranslateBrowsePathsToNodeid Service. After sending
	 * the Request to the Server, a Listener is used to acknowledge, when the
	 * service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request TranslateBrowsePathsToNodeId Request StartingNodeIds and
	 *                RelativePathes to translate them to NodeIds.
	 * @return AsyncResult of the TranslateBrowsePathsToNodeid Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected TranslateBrowsePathsToNodeIdsResponse beginTranslateBrowsePathToNodeIds(ClientSession session,
			TranslateBrowsePathsToNodeIdsRequest request) throws ServiceResultException {
		NodeId requestId = Identifiers.TranslateBrowsePathsToNodeIdsRequest;
		return session.beginTranslateBrowsePathToNodeIds(request, requestId);
	}

	/**
	 * Begins an Asynchronous UnregisterNode Service. After sending the Request to
	 * the Server, a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request UnregisterNode Request to unregister a Node node on the
	 *                server.
	 * @return AsyncResult of the UnregisterNode Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected UnregisterNodesResponse beginUnregisterNodes(ClientSession session, UnregisterNodesRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.UnregisterNodesRequest;
		return session.beginUnregisterNodes(request, requestId);
	}

	/**
	 * Begins an Asynchronous Write Service. After sending the Request to the
	 * Server, a Listener is used to acknowledge, when the service is finished.
	 * 
	 * @param Session Session which is sending the Request.
	 * @param Request Write Request to write values on Nodes.
	 * @return AsyncResult of the Write Service
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected WriteResponse beginWrite(ClientSession session, WriteRequest request) throws ServiceResultException {
		NodeId requestId = Identifiers.WriteRequest;
		WriteResponse response = session.beginWrite(request, requestId);
		endWrite(response, request);
		return response;
	}

	protected WriteResponse beginWriteDA(ClientSession session, boolean validEURange, boolean validInstrumentRange,
			boolean validEUInformation, WriteRequest request) throws ServiceResultException {
		Map<Integer, Entry<NodeId, List<Entry<NodeId, LocalizedText>>>> dynamicNode2DataAccessType = new HashMap<>();
		NodeId[] nodeId = new NodeId[request.getNodesToWrite().length];
		DataValue[] values = new DataValue[request.getNodesToWrite().length];
		for (int i = 0; i < request.getNodesToWrite().length; i++) {
			nodeId[i] = request.getNodesToWrite()[i].getNodeId();
			values[i] = request.getNodesToWrite()[i].getValue();
		}
		validateDAService(session, nodeId, dynamicNode2DataAccessType);
		this.dataAccessManager.validateDAWrite(session, nodeId, values, validEURange, validInstrumentRange,
				validEUInformation, dynamicNode2DataAccessType);
		return beginWrite(session, request);
	}

	protected ReadValueId[] validateReadRequest(NodeId[] nodeIds, UnsignedInteger[] attributeIds, String[] indexRanges,
			QualifiedName[] dataEncodings) {
		if (nodeIds == null || nodeIds.length == 0) {
			return new ReadValueId[0];
		}
		if (attributeIds != null && attributeIds.length != nodeIds.length) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} The nodeIds of the read request are inconsistent with the attributeIds!",
					new String[] { RequestType.Read.name() });
		}
		List<ReadValueId> attributes2read = new ArrayList<>();
		for (int i = 0; i < nodeIds.length; i++) {
			UnsignedInteger attributeId = null;
			String indexRange = null;
			QualifiedName dataEncoding = null;
			if (attributeIds != null) {
				if (attributeIds.length > i) {
					attributeId = attributeIds[i];
				}
			} else {
				attributeId = Attributes.Value;
			}
			if (indexRanges != null) {
				try {
					indexRange = indexRanges[i];
				} catch (IndexOutOfBoundsException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			if (dataEncodings != null) {
				try {
					dataEncoding = dataEncodings[i];
				} catch (IndexOutOfBoundsException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			StatusCode error = validateReadValueId(nodeIds[i], attributeId, indexRange, dataEncoding);
			if (error.isBad()) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Error: {1}",
						new String[] { RequestType.Read.name(), error.getDescription() });
			}
			ReadValueId attributeToRead = new ReadValueId();
			attributeToRead.setNodeId(nodeIds[i]);
			if (attributeId != null && !UnsignedInteger.ZERO.equals(attributeId)) {
				attributeToRead.setAttributeId(attributeId);
			}
			if (indexRanges != null) {
				attributeToRead.setIndexRange(indexRanges[i]);
			}
			if (dataEncodings != null) {
				// EncodeType.Binary
				attributeToRead.setDataEncoding(dataEncodings[i]);
			}
			attributes2read.add(attributeToRead);
		}
		return attributes2read.toArray(new ReadValueId[0]);
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the AddNodes Service.
	 * 
	 * @param session
	 * 
	 * @param Request  {@link ReadRequest} Request, which was sent to the server.
	 * @param Session  Sender of the Service
	 * @param Response {@link ReadResponse} Response, which has been received.
	 */
	private boolean endRead(ReadRequest request, ReadResponse response, ClientSession session) {
		// validates the timestamps of the results
		if (response != null && response.getResults() != null) {
			boolean isError = false;
			StringBuilder message = new StringBuilder();
			TimestampsToReturn timestampsToReturn = request.getTimestampsToReturn();
			for (int i = 0; i < response.getResults().length; i++) {
				DataValue value = response.getResults()[i];
				/** check timestampstoreturn */
				if (timestampsToReturn == TimestampsToReturn.Both) {
					if (Utils.checkDateTimeNull(value.getServerTimestamp())
							|| Utils.checkDateTimeNull(value.getSourceTimestamp())) {
						isError = true;
					}
				} else if (timestampsToReturn == TimestampsToReturn.Server) {
					if (Utils.checkDateTimeNull(value.getServerTimestamp())
							|| !Utils.checkDateTimeNull(value.getSourceTimestamp())) {
						isError = true;
					}
				} else if (timestampsToReturn == TimestampsToReturn.Source
						&& (Utils.checkDateTimeNull(value.getSourceTimestamp())
								|| !Utils.checkDateTimeNull(value.getServerTimestamp()))) {
					isError = true;
				}
				/** create message for timestampstoreturn */
				if (isError) {
					message = new StringBuilder();
					message.append("Fail to request timestamps " + timestampsToReturn.name() + " for value "
							+ value.toString() + ATINDEX + i + " ][ ");
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} {1}",
							new Object[] { RequestType.Read.name(), message });
					// reinit
					isError = false;
				}
				/** check timestamps in future */
				if (session.getMaxFutureServerTime() >= 0) {
					if (value.getServerTimestamp() != null
							&& DateTime.currentTime().getTimeInMillis() < (value.getServerTimestamp().getTimeInMillis()
									- session.getMaxFutureServerTime())) {
						message = new StringBuilder();
						message.append(
								"ServerTimestamp is in future for value: " + value.toString() + ATINDEX + i + " ][ ");
						isError = true;
					}
					if (value.getSourceTimestamp() != null
							&& DateTime.currentTime().getTimeInMillis() < (value.getSourceTimestamp().getTimeInMillis()
									- session.getMaxFutureServerTime())) {
						message.append(
								"SourceTimestamp is in future for value: " + value.toString() + ATINDEX + i + " ][ ");
						isError = true;
					}
				}
				if (!isError && session.getMaxPastServerTime() >= 0) {
					if (value.getServerTimestamp() != null
							&& DateTime.currentTime().getTimeInMillis() > (value.getServerTimestamp().getTimeInMillis()
									+ session.getMaxPastServerTime())) {
						message = new StringBuilder();
						message.append(
								"ServerTimestamp is in future for value: " + value.toString() + ATINDEX + i + " ][ ");
						isError = true;
					}
					if (value.getSourceTimestamp() != null
							&& DateTime.currentTime().getTimeInMillis() > (value.getSourceTimestamp().getTimeInMillis()
									+ session.getMaxPastServerTime())) {
						message.append(
								"SourceTimestamp is in future for value: " + value.toString() + ATINDEX + i + " ][ ");
						isError = true;
					}
				}
				if (isError) {
					String output = new String(message);
					Logger.getLogger(getClass().getName()).log(Level.FINE, "{0} {1}",
							new String[] { RequestType.Read.name(), output });
					// reinit
					isError = false;
				}
			}
		}
		// validates same result as requested
		boolean equalRequestedMessagesThenResponded = false;
		if (request.getNodesToRead() != null && response != null && response.getResults() != null) {
			if (request.getNodesToRead().length == response.getResults().length) {
				equalRequestedMessagesThenResponded = true;
			}
		} else if (request.getNodesToRead() == null && response.getResults() == null) {
			equalRequestedMessagesThenResponded = true;
		}
		if (!equalRequestedMessagesThenResponded) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} The results of the read response mismatch with the request!",
					new String[] { RequestType.Read.name() });
			return false;
		}
		return true;
		// -------------------------------------------------
	}

	private void endWrite(WriteResponse response, WriteRequest request) throws ServiceResultException {
		// diagnostics
		boolean isErrorDiagnostics = false;
		if ((request.getRequestHeader().getReturnDiagnostics() != null
				&& UnsignedInteger.ZERO.compareTo(request.getRequestHeader().getReturnDiagnostics()) != 0)
				&& (response.getDiagnosticInfos() == null || response.getDiagnosticInfos().length == 0)) {
			// CTT 9.2-038
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} The results of the write response {1} diagnostics are empty, but they were requested by the client!",
					new Object[] { RequestType.Write.name(), response.getResponseHeader().getRequestHandle() });
		}
		// CTT 9.2-039
		if ((response.getDiagnosticInfos() != null && response.getDiagnosticInfos().length > 0
				&& request.getNodesToWrite() != null)
				&& response.getDiagnosticInfos().length != request.getNodesToWrite().length) {
			isErrorDiagnostics = true;
		}
		if (isErrorDiagnostics) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"{0} The results of the write response {1} diagnostics mismatch with the requested one! The diagnostics cannot be proccessed!",
					new Object[] { RequestType.Write, response.getResponseHeader().getRequestHandle() });
		} else {
			processDiagnostics(response.getDiagnosticInfos());
		}
		// validates same result as requested
		boolean isErrorResult = true;
		// CTT 9.2- 031-032
		if (request.getNodesToWrite() != null && response.getResults() != null) {
			if (request.getNodesToWrite().length == response.getResults().length) {
				isErrorResult = false;
			}
		} else if (request.getNodesToWrite() == null && response.getResults() == null) {
			isErrorResult = false;
		}
		if (isErrorResult) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_DataLost,
					"The results of the write response mismatch with the requested ones!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"{0} The results of the write response {1} mismatch with the requested ones!",
					new Object[] { RequestType.Write.name(), response.getResponseHeader().getRequestHandle() });
			throw sre;
		}
	}

	private void processDiagnostics(DiagnosticInfo[] diagnosticInfos) {
		if (diagnosticInfos == null || diagnosticInfos.length == 0) {
			return;
		}
	}

	private StatusCode[] validateDAService(ClientSession session, NodeId[] nodeIds2validate,
			Map<Integer, Entry<NodeId, List<Entry<NodeId, LocalizedText>>>> dynamicNode2DataAccessType) {
		if (nodeIds2validate == null || nodeIds2validate.length == 0) {
			return new StatusCode[0];
		}
		StatusCode[] codes = new StatusCode[nodeIds2validate.length];
		// check valid type per node [DA-Type DataItemType]
		for (int i = 0; i < nodeIds2validate.length; i++) {
			if (NodeId.isNull(nodeIds2validate[i])) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"{0} DataAccessRead - There is no NodeId to validate the DA-Type of the node!",
						new String[] { RequestType.Read.name() });
			}
			OPCEntry<NodeId, List<Entry<NodeId, LocalizedText>>> entry = new OPCEntry<>();
			codes[i] = this.dataAccessManager.validateType(session, nodeIds2validate[i], i, entry);
			if (codes[i].isGood()) {
				dynamicNode2DataAccessType.put(i, entry);
			}
		}
		// check for valid properties [Definition, ValuePrecision,...]
		BrowseRequest browseProperties = new BrowseRequest();
		BrowseDescription node2Browse = new BrowseDescription();
		node2Browse.setBrowseDirection(BrowseDirection.Forward);
		node2Browse.setIncludeSubtypes(true);
		node2Browse.setNodeClassMask(NodeClass.getSet(NodeClass.Variable.getValue()));
		node2Browse.setReferenceTypeId(Identifiers.HasProperty);
		node2Browse.setResultMask(BrowseResultMask.ALL);
		// browsedescription for a multiple call
		Map<Integer, BrowseDescription> index2description = new HashMap<>();
		for (int i = 0; i < nodeIds2validate.length; i++) {
			BrowseDescription description = node2Browse.clone();
			description.setNodeId(nodeIds2validate[i]);
			index2description.put(i, description);
		}
		// skip nothing to do
		if (index2description.values().isEmpty()) {
			return codes;
		}
		browseProperties.setNodesToBrowse(index2description.values().toArray(new BrowseDescription[0]));
		try {
			BrowseResponse response = session.browse(browseProperties);
			for (int i = 0; i < response.getResults().length; i++) {
				if (response.getResults()[i].getReferences() == null) {
					continue;
				}
				for (int j = 0; j < response.getResults()[i].getReferences().length; j++) {
					ReferenceDescription description = response.getResults()[i].getReferences()[j];
					// no displayname - skip validation and log warning
					if (description.getDisplayName() == null || description.getDisplayName().getText() == null) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING,
								"{0} DataAccessRead - There is no DisplayName to validate the property of node {1} {2}!",
								new Object[] { RequestType.Read.name(), description.getNodeId(),
										description.getBrowseName() });
						continue;
					}
					Entry<NodeId, List<Entry<NodeId, LocalizedText>>> entry = dynamicNode2DataAccessType.get(i);
					if (entry != null)
						this.dataAccessManager.validateProperty(session, description.getNodeId(),
								description.getDisplayName(), entry);
				}
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return codes;
		}
		return codes;
	}

	private void validateMethodCall(ClientSession session, MethodElement[] elements2call) {
		if (elements2call == null) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Missing validation when calling a method!",
					new String[] { RequestType.Call.name() });
			return;
		}
		this.methodManager.validate(session, elements2call);
	}

	private StatusCode validateReadValueId(NodeId nodeId, UnsignedInteger attributeId, String indexRange,
			QualifiedName encoding) {
		StatusCode error;
		// validate NodeId
		if (!NodeId.isNull(nodeId)) {
			error = NodeIdUtil.validate(nodeId);
			if (error.isBad()) {
				return error;
			}
		} else {
			return new StatusCode(StatusCodes.Bad_NodeIdInvalid);
		}
		// validate attributeid & value
		if (attributeId != null && !UnsignedInteger.ZERO.equals(attributeId)) {
			boolean isValid = AttributesUtil.isValid(attributeId);
			if (!isValid) {
				return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
			}
		} else {
			return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
		}
		// validate timestampstoreturn
		// validate indexrange
		if (indexRange != null && !indexRange.isEmpty()) {
			// prepared for further use
		}
		// validate data encodings
		if (!QualifiedName.isNull(encoding)) {
			// prepared for further use
		}
		return error;
	}
}
