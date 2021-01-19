package opc.client.service;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AggregateConfiguration;
import org.opcfoundation.ua.core.DeleteAtTimeDetails;
import org.opcfoundation.ua.core.DeleteRawModifiedDetails;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryReadValueId;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.HistoryUpdateResult;
import org.opcfoundation.ua.core.PerformUpdateType;
import org.opcfoundation.ua.core.ReadAtTimeDetails;
import org.opcfoundation.ua.core.ReadProcessedDetails;
import org.opcfoundation.ua.core.ReadRawModifiedDetails;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.UpdateDataDetails;
import org.opcfoundation.ua.encoding.EncoderContext;

/**
 * A history archive item to read and update history data.
 * 
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class HistoryArchive {
	private ClientSession session;
	private boolean releaseContinuationPoints;
	private TimestampsToReturn timestamp2return;

	public HistoryArchive(ClientSession session) {
		this.session = session;
		this.releaseContinuationPoints = false;
		this.timestamp2return = TimestampsToReturn.Both;
	}

	/**
	 * Delete history values at a given timestamp from one node.
	 * 
	 * @param NodeId    Source NodeId from the history values.
	 * @param Timestamp Timstamp of values to remove.
	 * @return Results
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult deleteHistory(NodeId nodeId, DateTime[] timestamp) throws ServiceResultException {
		// check valid nodeid
		if (NodeId.isNull(nodeId)) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		DeleteAtTimeDetails details = new DeleteAtTimeDetails();
		details.setNodeId(nodeId);
		details.setReqTimes(timestamp);
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(
				new ExtensionObject[] { ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()) });
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults()[0];
	}

	/**
	 * Delete history values at a given timestamp from one or more node.
	 * 
	 * @param NodeId    Source NodeIds from the history values.
	 * @param Timestamp Timstamps of values to remove.
	 * @return Results
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult[] deleteHistory(NodeId[] nodeIds, DateTime[][] timestamps)
			throws ServiceResultException {
		// check valid nodeid
		if (nodeIds == null || nodeIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		ExtensionObject[] details = new ExtensionObject[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			DeleteAtTimeDetails detail = new DeleteAtTimeDetails();
			detail.setNodeId(nodeIds[i]);
			detail.setReqTimes(timestamps[i]);
			details[i] = ExtensionObject.binaryEncode(detail, EncoderContext.getDefaultInstance());
		}
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(details);
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults();
	}

	/**
	 * Delete history values within a range of time from one node.
	 * 
	 * @param NodeId Source NodeId from the history values.
	 * @param Start  Timestamp to start reading values.
	 * @param End    Timestamp to end reading values.
	 * @return Results
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult deleteHistory(NodeId nodeId, DateTime start, DateTime end)
			throws ServiceResultException {
		// check valid nodeid
		if (NodeId.isNull(nodeId)) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		DeleteRawModifiedDetails details = new DeleteRawModifiedDetails();
		details.setStartTime(start);
		details.setEndTime(end);
		details.setIsDeleteModified(false);
		details.setNodeId(nodeId);
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(
				new ExtensionObject[] { ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()) });
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults()[0];
	}

	/**
	 * Delete history values within a range of time from one or more nodes.
	 * 
	 * @param NodeId Source NodeIds from the history values.
	 * @param Start  Timestamp to start reading values.
	 * @param End    Timestamp to end reading values.
	 * @return Results
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult[] deleteHistory(NodeId[] nodeIds, DateTime start, DateTime end)
			throws ServiceResultException {
		// check valid nodeid
		if (nodeIds == null || nodeIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		ExtensionObject[] details = new ExtensionObject[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			DeleteRawModifiedDetails detail = new DeleteRawModifiedDetails();
			detail.setStartTime(start);
			detail.setEndTime(end);
			detail.setIsDeleteModified(false);
			detail.setNodeId(nodeIds[i]);
			details[i] = ExtensionObject.binaryEncode(detail, EncoderContext.getDefaultInstance());
		}
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(details);
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults();
	}

	/**
	 * Read history values at a given timestamp from one node.
	 * 
	 * @param NodeId    Source NodeId from the history values.
	 * @param Timestamp Timestamp of the value to read.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryReadResult readHistoryAtTime(NodeId nodeId, DateTime timestamp) throws ServiceResultException {
		// check valid nodeid
		if (NodeId.isNull(nodeId)) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		HistoryReadRequest request = new HistoryReadRequest();
		HistoryReadValueId node2read = new HistoryReadValueId();
		node2read.setNodeId(nodeId);
		request.setNodesToRead(new HistoryReadValueId[] { node2read });
		request.setReleaseContinuationPoints(this.releaseContinuationPoints);
		request.setTimestampsToReturn(this.timestamp2return);
		ReadAtTimeDetails details = new ReadAtTimeDetails();
		details.setReqTimes(new DateTime[] { timestamp });
		request.setHistoryReadDetails(ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()));
		HistoryReadResponse response = sendHistoryReadRequest(request);
		return response.getResults()[0];
	}

	/**
	 * Read history values at a given timestamp from one or more nodes.
	 * 
	 * @param NodeId    Source NodeId from the history values.
	 * @param Timestamp Timestamp of values to read.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryReadResult[] readHistoryAtTime(NodeId[] nodeId, DateTime[] timestamp) throws ServiceResultException {
		// check valid nodeid
		if (nodeId == null || nodeId.length == 0 || timestamp == null || timestamp.length != nodeId.length) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		HistoryReadRequest request = new HistoryReadRequest();
		HistoryReadValueId[] nodes2read = new HistoryReadValueId[nodeId.length];
		for (int i = 0; i < nodeId.length; i++) {
			nodes2read[i] = new HistoryReadValueId();
			nodes2read[i].setNodeId(nodeId[i]);
		}
		request.setNodesToRead(nodes2read);
		request.setReleaseContinuationPoints(this.releaseContinuationPoints);
		request.setTimestampsToReturn(this.timestamp2return);
		ReadAtTimeDetails details = new ReadAtTimeDetails();
		details.setReqTimes(timestamp);
		request.setHistoryReadDetails(ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()));
		HistoryReadResponse response = sendHistoryReadRequest(request);
		return response.getResults();
	}

	/**
	 * Read history values within a range of time from one node.
	 * 
	 * @param NodeId    Source NodeId from the history values.
	 * @param Start     Timestamp to start reading values.
	 * @param End       Timestamp to end reading values.
	 * @param MaxValues Maximum number of datavalues to return.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryReadResult readHistoryRaw(NodeId nodeId, DateTime start, DateTime end, int maxValues)
			throws ServiceResultException {
		// check valid nodeid
		if (nodeId == null) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		HistoryReadRequest request = new HistoryReadRequest();
		HistoryReadValueId node2read = new HistoryReadValueId();
		node2read.setNodeId(nodeId);
		request.setNodesToRead(new HistoryReadValueId[] { node2read });
		request.setReleaseContinuationPoints(this.releaseContinuationPoints);
		request.setTimestampsToReturn(this.timestamp2return);
		ReadRawModifiedDetails details = new ReadRawModifiedDetails();
		details.setStartTime(start);
		details.setEndTime(end);
		details.setIsReadModified(false);
		details.setNumValuesPerNode(new UnsignedInteger(maxValues));
		details.setReturnBounds(false);
		request.setHistoryReadDetails(ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()));
		HistoryReadResponse response = sendHistoryReadRequest(request);
		return response.getResults()[0];
	}

	/**
	 * ead history values within a range of time from one or more nodes.
	 * 
	 * @param NodeIds   Sources NodeIds from the history values.
	 * @param Start     Timestamp to start reading values.
	 * @param End       Timestamp to end reading values.
	 * @param MaxValues Maximum number of datavalues to return.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryReadResult[] readHistoryRaw(NodeId[] nodeIds, DateTime start, DateTime end, int maxValues)
			throws ServiceResultException {
		// check valid nodeid
		if (nodeIds == null || nodeIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		HistoryReadRequest request = new HistoryReadRequest();
		List<HistoryReadValueId> nodes2read = new ArrayList<>();
		for (NodeId nodeId : nodeIds) {
			HistoryReadValueId node2read = new HistoryReadValueId();
			node2read.setNodeId(nodeId);
			nodes2read.add(node2read);
		}
		request.setNodesToRead(nodes2read.toArray(new HistoryReadValueId[0]));
		request.setReleaseContinuationPoints(this.releaseContinuationPoints);
		request.setTimestampsToReturn(this.timestamp2return);
		ReadRawModifiedDetails details = new ReadRawModifiedDetails();
		details.setStartTime(start);
		details.setEndTime(end);
		details.setIsReadModified(false);
		details.setNumValuesPerNode(new UnsignedInteger(maxValues));
		details.setReturnBounds(false);
		request.setHistoryReadDetails(ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()));
		HistoryReadResponse response = sendHistoryReadRequest(request);
		return response.getResults();
	}

	/**
	 * Read calculated history values within a range of time from one node.
	 * 
	 * @param NodeId                 Sources NodeId from the history values.
	 * @param Start                  Timestamp to start reading values.
	 * @param End                    Timestamp to end reading values.
	 * @param AggregateConfiguration Configuration of the calculated values.
	 * @param AggregateType          Type of the aggregated configuration.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryReadResult[] readHistoryProcessed(NodeId nodeId, DateTime start, DateTime end,
			AggregateConfiguration aggregateConfiguration, NodeId[] aggregateType) throws ServiceResultException {
		// check valid nodeid
		if (NodeId.isNull(nodeId)) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		HistoryReadRequest request = new HistoryReadRequest();
		HistoryReadValueId node2read = new HistoryReadValueId();
		node2read.setNodeId(nodeId);
		request.setNodesToRead(new HistoryReadValueId[] { node2read });
		request.setReleaseContinuationPoints(this.releaseContinuationPoints);
		request.setTimestampsToReturn(this.timestamp2return);
		ReadProcessedDetails details = new ReadProcessedDetails();
		details.setAggregateConfiguration(aggregateConfiguration);
		details.setAggregateType(aggregateType);
		details.setStartTime(start);
		details.setEndTime(end);
		request.setHistoryReadDetails(ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()));
		HistoryReadResponse response = sendHistoryReadRequest(request);
		return response.getResults();
	}

	/**
	 * Read calculated history values within a range of time from one or more nodes.
	 * 
	 * @param NodeIds                Sources NodeIds from the history values.
	 * @param Start                  Timestamp to start reading values.
	 * @param End                    Timestamp to end reading values.
	 * @param AggregateConfiguration Configuration of the calculated values.
	 * @param AggregateType          Type of the aggregated configuration.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryReadResult[] readHistoryProcessed(NodeId[] nodeIds, DateTime start, DateTime end,
			AggregateConfiguration aggregateConfiguration, NodeId[] aggregateType) throws ServiceResultException {
		// check valid nodeid
		if (nodeIds == null || nodeIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		HistoryReadRequest request = new HistoryReadRequest();
		List<HistoryReadValueId> nodes2read = new ArrayList<>();
		for (NodeId nodeId : nodeIds) {
			HistoryReadValueId node2read = new HistoryReadValueId();
			node2read.setNodeId(nodeId);
			nodes2read.add(node2read);
		}
		request.setNodesToRead(nodes2read.toArray(new HistoryReadValueId[0]));
		request.setReleaseContinuationPoints(this.releaseContinuationPoints);
		request.setTimestampsToReturn(this.timestamp2return);
		ReadProcessedDetails details = new ReadProcessedDetails();
		details.setAggregateConfiguration(aggregateConfiguration);
		details.setAggregateType(aggregateType);
		details.setStartTime(start);
		details.setEndTime(end);
		request.setHistoryReadDetails(ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()));
		HistoryReadResponse response = sendHistoryReadRequest(request);
		return response.getResults();
	}

	/**
	 * Updates an existing historical value from one node.
	 * 
	 * @param NodeId Sources NodeId from the history values.
	 * @param Value  Historical values to update.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult updateHistory(NodeId nodeId, DataValue[] value) throws ServiceResultException {
		// check valid nodeid
		if (NodeId.isNull(nodeId)) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		UpdateDataDetails detail = new UpdateDataDetails();
		detail.setPerformInsertReplace(PerformUpdateType.Replace);
		detail.setNodeId(nodeId);
		detail.setUpdateValues(value);
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(
				new ExtensionObject[] { ExtensionObject.binaryEncode(detail, EncoderContext.getDefaultInstance()) });
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults()[0];
	}

	/**
	 * Updates an existing historical value from one or more nodes.
	 * 
	 * @param NodeIds Sources NodeIds from the history values.
	 * @param Values  Historical values to update.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult[] updateHistory(NodeId[] nodeIds, DataValue[][] values) throws ServiceResultException {
		// check valid nodeid
		if (nodeIds == null || nodeIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		ExtensionObject[] details = new ExtensionObject[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			UpdateDataDetails detail = new UpdateDataDetails();
			detail.setPerformInsertReplace(PerformUpdateType.Replace);
			detail.setNodeId(nodeIds[i]);
			detail.setUpdateValues(values[i]);
			details[i] = ExtensionObject.binaryEncode(detail, EncoderContext.getDefaultInstance());
		}
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(details);
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults();
	}

	/**
	 * Inserts historical values from one node.
	 * 
	 * @param NodeId Source NodeId from the history values.
	 * @param Values Historical values to insert.
	 * @return Result
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult writeHistory(NodeId nodeId, DataValue[] values) throws ServiceResultException {
		// check valid nodeid
		if (NodeId.isNull(nodeId)) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		UpdateDataDetails details = new UpdateDataDetails();
		details.setPerformInsertReplace(PerformUpdateType.Insert);
		details.setNodeId(nodeId);
		details.setUpdateValues(values);
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(
				new ExtensionObject[] { ExtensionObject.binaryEncode(details, EncoderContext.getDefaultInstance()) });
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults()[0];
	}

	/**
	 * Insert historical values from one or more nodes.
	 * 
	 * @param NodeIds Source NodeIds from the history values.
	 * @param Values  Historical values to insert.
	 * @return Result
	 * 
	 * @throws ServiceResultException
	 */
	public HistoryUpdateResult[] writeHistory(NodeId[] nodeIds, DataValue[][] values) throws ServiceResultException {
		// check valid nodeid
		if (nodeIds == null || nodeIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		ExtensionObject[] details = new ExtensionObject[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			UpdateDataDetails detail = new UpdateDataDetails();
			detail.setPerformInsertReplace(PerformUpdateType.Insert);
			detail.setNodeId(nodeIds[i]);
			detail.setUpdateValues(values[i]);
			details[i] = ExtensionObject.binaryEncode(detail, EncoderContext.getDefaultInstance());
		}
		HistoryUpdateRequest request = new HistoryUpdateRequest();
		request.setHistoryUpdateDetails(details);
		HistoryUpdateResponse response = sendHistoryUpdateRequest(request);
		return response.getResults();
	}

	/**
	 * Sets Flag for releasing historical continuationpoints.
	 * 
	 * @param ReleaseContinuationPoints Flag weather to realease a historical
	 *                                  continuation point or not.
	 */
	public void setReleaseContinuationPoints(boolean releaseContinuationPoints) {
		this.releaseContinuationPoints = releaseContinuationPoints;
	}

	/**
	 * Sets the Timestamp mark for returned values. (None, Both, Source, Server)
	 * 
	 * @param timestamp2return
	 */
	public void setTimestampToReturn(TimestampsToReturn timestamp2return) {
		this.timestamp2return = timestamp2return;
	}

	/**
	 * Sends a history read request to an OPC UA server.
	 * 
	 * @param request
	 * @return
	 * @throws ServiceResultException
	 */
	private HistoryReadResponse sendHistoryReadRequest(HistoryReadRequest request) throws ServiceResultException {
		return this.session.historyRead(request);
	}

	/**
	 * Sends a history update request to an OPC UA server.
	 * 
	 * @param request
	 * @return
	 * @throws ServiceResultException
	 */
	private HistoryUpdateResponse sendHistoryUpdateRequest(HistoryUpdateRequest request) throws ServiceResultException {
		return this.session.historyUpdate(request);
	}
}
