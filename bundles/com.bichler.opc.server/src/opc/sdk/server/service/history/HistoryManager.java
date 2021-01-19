package opc.sdk.server.service.history;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.DeleteAtTimeDetails;
import org.opcfoundation.ua.core.DeleteEventDetails;
import org.opcfoundation.ua.core.DeleteRawModifiedDetails;
import org.opcfoundation.ua.core.HistoryData;
import org.opcfoundation.ua.core.HistoryReadDetails;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryReadValueId;
import org.opcfoundation.ua.core.HistoryUpdateDetails;
import org.opcfoundation.ua.core.HistoryUpdateResult;
import org.opcfoundation.ua.core.ReadAtTimeDetails;
import org.opcfoundation.ua.core.ReadEventDetails;
import org.opcfoundation.ua.core.ReadProcessedDetails;
import org.opcfoundation.ua.core.ReadRawModifiedDetails;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.UpdateDataDetails;
import org.opcfoundation.ua.core.UpdateEventDetails;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.opc.comdrv.ComDRVManager;

import opc.sdk.core.node.Node;
import opc.sdk.core.utils.ValueUtil;
import opc.sdk.server.core.OPCServer;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.server.service.util.UUIDUtil;
import opc.sdk.ua.constants.HistoryUpdateQueryMode;

/**
 * History manager. Prepares for history services to the server. The
 * implementation has be done in the sub class.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public class HistoryManager implements HistoryAccess {
	protected OPCServer internalServer = null;
	private String database;

	public HistoryManager(String database) {
		this.database = database;
	}

	public String getDatabase() {
		return this.database;
	}

	public OPCServer getInternalServer() {
		return internalServer;
	}

	public void setInternalServer(OPCServer internalServer) {
		this.internalServer = internalServer;
	}

	public ServiceResult historyRead(OPCServerSession session, HistoryReadDetails details,
			HistoryReadValueId nodeToRead, Boolean releaseContinuationPoints, TimestampsToReturn timestampToReturn,
			Node handle, HistoryReadResult result, Long[] driverStates) {
		ServiceResult error = null;
		if (details instanceof ReadAtTimeDetails) {
			ReadAtTimeDetails readAtTimeDetails = (ReadAtTimeDetails) details;
			error = historyReadAtTime(session, readAtTimeDetails, nodeToRead, handle, result);
		} else if (details instanceof ReadEventDetails) {
			ReadEventDetails eventDetails = (ReadEventDetails) details;
			error = historyReadEvent(session, eventDetails, nodeToRead, handle, result, driverStates);
		} else if (details instanceof ReadProcessedDetails) {
			ReadProcessedDetails processedDetails = (ReadProcessedDetails) details;
			error = historyReadProcessed(session, processedDetails, nodeToRead, handle, result);
		} else if (details instanceof ReadRawModifiedDetails) {
			ReadRawModifiedDetails rawModiefiedDetails = (ReadRawModifiedDetails) details;
			error = historyReadRawModified(session, rawModiefiedDetails, nodeToRead, handle, result);
		}
		return error;
	}

	protected Object defaultValueWrapper(NodeId dataType, Object value, Class<?> reflactory) {
		try {
			return ValueUtil.createBuiltinValue(dataType, reflactory, value);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	private ServiceResult historyReadAtTime(OPCServerSession session, ReadAtTimeDetails details,
			HistoryReadValueId nodeToRead, Node handle, HistoryReadResult result) {
		HistoryReadCPItem historyRequest = null;
		// LOAD AN CONTINUATIONPOINT
		if (nodeToRead.getContinuationPoint() != null) {
			historyRequest = loadHistoryContinuationPoint(session,
					ByteString.asByteArray(nodeToRead.getContinuationPoint()));
			if (historyRequest == null) {
				return new ServiceResult(StatusCodes.Bad_ContinuationPointInvalid);
			}
		}
		// PROCESS A HISTORY READ
		else {
			historyRequest = createHistoryRequest(details, handle, nodeToRead);
		}
		// process values in result
		HistoryData data = new HistoryData();
		List<DataValue> valueToShift = new ArrayList<>();
		if (historyRequest != null) {
			while (historyRequest.getNumValuesPerNode().longValue() == 0
					|| valueToShift.size() < historyRequest.getNumValuesPerNode().longValue()) {
				if (historyRequest.getValues() == null || historyRequest.getValues().isEmpty()) {
					break;
				}
				DataValue value = historyRequest.getValues().removeFirst();
				valueToShift.add(value);
			}
		}
		// add data to history data
		data.setDataValues(valueToShift.toArray(new DataValue[0]));
		try {
			result.setHistoryData(ExtensionObject.binaryEncode(data, EncoderContext.getDefaultInstance()));
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		// check if a continuation point is requeued
		if (historyRequest.getValues() != null && !historyRequest.getValues().isEmpty()) {
			ByteString continuationPoint = ByteString.valueOf(saveHistoryContinuationPoint(session, historyRequest));
			result.setContinuationPoint(continuationPoint);
		} else {
			if (valueToShift.isEmpty()) {
				return new ServiceResult(StatusCodes.Good_NoData);
			}
		}
		return new ServiceResult(StatusCode.GOOD);
	}

	private ServiceResult historyReadEvent(OPCServerSession session, ReadEventDetails eventDetails,
			HistoryReadValueId nodeToRead, Node handle, HistoryReadResult result, Long[] driverStates) {
		int i = 0;
		StatusCode status = new StatusCode(StatusCodes.Bad_ServiceUnsupported);
		// driver states to read history values from underlying system
		long driverState = -1;
		if (driverStates != null && driverStates.length > i && driverStates[i] != null) {
			driverState = driverStates[i];
		}
		i++;
		// read from underlying system sync
		if (handle.getSyncHistReadMask() == ComDRVManager.HISTORYSYNCREAD) {
			status = ComDRVManager.getDRVManager().syncHistReadEventValue(handle.getNodeId(), eventDetails,
					handle.getReadDriverIds(), result, driverState);
		}
		return new ServiceResult(status);
	}

	private ServiceResult historyReadProcessed(OPCServerSession session, ReadProcessedDetails details,
			HistoryReadValueId nodeToRead, Node handle, HistoryReadResult result) {
		HistoryReadCPItem historyRequest;
		// LOAD AN CONTINUATIONPOINT
		if (nodeToRead.getContinuationPoint() != null) {
			historyRequest = loadHistoryContinuationPoint(session,
					ByteString.asByteArray(nodeToRead.getContinuationPoint()));
			if (historyRequest == null) {
				return new ServiceResult(StatusCodes.Bad_ContinuationPointInvalid);
			}
		}
		// PROCESS A HISTORY READ
		else {
			historyRequest = createHistoryRequest(details, handle, nodeToRead);
		}
		// process values in result
		HistoryData data = new HistoryData();
		List<DataValue> valueToShift = new ArrayList<>();
		if (historyRequest != null && historyRequest.getNumValuesPerNode() != null) {
			while (historyRequest.getNumValuesPerNode().longValue() == 0
					|| valueToShift.size() < historyRequest.getNumValuesPerNode().longValue()) {
				if (historyRequest.getValues() == null || historyRequest.getValues().isEmpty()) {
					break;
				}
				DataValue value = historyRequest.getValues().removeFirst();
				valueToShift.add(value);
			}
		}
		// add data to history data
		data.setDataValues(valueToShift.toArray(new DataValue[0]));
		try {
			result.setHistoryData(ExtensionObject.binaryEncode(data, EncoderContext.getDefaultInstance()));
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		// check if a continuation point is requeued
		if (historyRequest != null && historyRequest.getValues() != null && !historyRequest.getValues().isEmpty()) {
			ByteString continuationPoint = ByteString.valueOf(saveHistoryContinuationPoint(session, historyRequest));
			result.setContinuationPoint(continuationPoint);
		} else {
			if (valueToShift.isEmpty()) {
				return new ServiceResult(StatusCodes.Good_NoData);
			}
		}
		return new ServiceResult(StatusCode.GOOD);
	}

	private ServiceResult historyReadRawModified(OPCServerSession session, ReadRawModifiedDetails details,
			HistoryReadValueId nodeToRead, Node handle, HistoryReadResult result) {
		HistoryReadCPItem historyRequest;
		// LOAD AN CONTINUATIONPOINT
		if (nodeToRead.getContinuationPoint() != null) {
			historyRequest = loadHistoryContinuationPoint(session,
					ByteString.asByteArray(nodeToRead.getContinuationPoint()));
			if (historyRequest == null) {
				return new ServiceResult(StatusCodes.Bad_ContinuationPointInvalid);
			}
		}
		// PROCESS A HISTORY READ
		else {
			historyRequest = createHistoryRequest(details, handle, nodeToRead);
		}
		// process values in result
		HistoryData data = new HistoryData();
		List<DataValue> valueToShift = new ArrayList<>();
		while (historyRequest.getNumValuesPerNode().longValue() == 0
				|| valueToShift.size() < historyRequest.getNumValuesPerNode().longValue()) {
			if (historyRequest.getValues() == null || historyRequest.getValues().isEmpty()) {
				break;
			}
			DataValue value = historyRequest.getValues().removeFirst();
			valueToShift.add(value);
		}
		// add data to history data
		data.setDataValues(valueToShift.toArray(new DataValue[0]));
		try {
			result.setHistoryData(ExtensionObject.binaryEncode(data, EncoderContext.getDefaultInstance()));
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		// check if a continuation point is requeued
		if (historyRequest.getValues() != null && !historyRequest.getValues().isEmpty()) {
			// only set if both end time and start time is set
			if (DateTime.MIN_VALUE.compareTo(details.getStartTime()) != 0
					&& DateTime.MAX_VALUE.compareTo(details.getEndTime()) != 0) {
				ByteString continuationPoint = ByteString
						.valueOf(saveHistoryContinuationPoint(session, historyRequest));
				result.setContinuationPoint(continuationPoint);
			}
		} else {
			if (valueToShift.isEmpty()) {
				return new ServiceResult(StatusCodes.Good_NoData);
			}
		}
		return new ServiceResult(StatusCode.GOOD);
	}

	private HistoryReadCPItem createHistoryRequest(ReadAtTimeDetails details, Node handle,
			HistoryReadValueId nodeToRead) {
		List<DataValue> values = new ArrayList<>();
		for (DateTime reqTimes : details.getReqTimes()) {
			DataValue[] historyValue = readHistory(handle, reqTimes, reqTimes, UnsignedInteger.ONE, false, false);
			if (historyValue == null || historyValue.length == 0) {
				continue;
			}
			for (DataValue value : historyValue) {
				values.add(value);
			}
		}
		HistoryReadCPItem historyRequest = new HistoryReadCPItem();
		historyRequest.setValues(values);
		historyRequest.setNumValuesPerNode(UnsignedInteger.ZERO);
		historyRequest.setFilter(null);
		return historyRequest;
	}

	private HistoryReadCPItem createHistoryRequest(ReadRawModifiedDetails details, Node handle,
			HistoryReadValueId nodeToRead) {
		if (details.getStartTime() == null) {
			details.setStartTime(DateTime.MIN_VALUE);
		}
		if (details.getEndTime() == null) {
			details.setEndTime(DateTime.MIN_VALUE);
		}
		boolean sizeLimit = DateTime.MIN_VALUE.compareTo(details.getStartTime()) == 0
				|| DateTime.MIN_VALUE.compareTo(details.getEndTime()) == 0;
		boolean returnBounds = details.getReturnBounds() && !details.getIsReadModified();
		boolean timeFlowsBackward = DateTime.MIN_VALUE.compareTo(details.getStartTime()) == 0
				|| (DateTime.MIN_VALUE.compareTo(details.getEndTime()) != 0
						&& details.getEndTime().compareTo(details.getStartTime()) < 0);
		LinkedList<DataValue> values = new LinkedList<>();
		DataValue[] historyValue = readHistory(handle, details.getStartTime(), details.getEndTime(),
				details.getNumValuesPerNode(), details.getIsReadModified(), timeFlowsBackward);
		// store values
		if (historyValue != null && historyValue.length > 0) {
			int start = -1;
			int end = -1;
			int i = timeFlowsBackward ? historyValue.length - 1 : 0;
			while (i >= 0 && i < historyValue.length) {
				// TODO: VALUE VOM GELESENEN DATENSATZ TIMESTAMP
				try {
					DateTime timestamp = historyValue[i].getSourceTimestamp();
					if (timestamp == null) {
						continue;
					}
					if (values.isEmpty()) {
						if (timeFlowsBackward) {
							if ((DateTime.MIN_VALUE.compareTo(details.getStartTime()) != 0
									&& timestamp.compareTo(details.getStartTime()) >= 0)
									|| (DateTime.MIN_VALUE.compareTo(details.getStartTime()) == 0
											&& timestamp.compareTo(details.getEndTime()) >= 0)) {
								start = i;
								if (timestamp.compareTo(details.getStartTime()) > 0) {
									continue;
								}
							}
						} else {
							if (timestamp.compareTo(details.getStartTime()) <= 0) {
								start = i;
								if (timestamp.compareTo(details.getStartTime()) < 0) {
									continue;
								}
							}
						}
					}
					// check if absolute max values specified
					if (sizeLimit && details.getNumValuesPerNode().longValue() > 0
							&& details.getNumValuesPerNode().longValue() < values.size()) {
						break;
					}
					// check for end bound
					if (DateTime.MIN_VALUE.compareTo(details.getEndTime()) != 0
							&& timestamp.compareTo(details.getEndTime()) >= 0) {
						if (timeFlowsBackward) {
							if (timestamp.compareTo(details.getEndTime()) <= 0) {
								end = i;
								break;
							}
						} else {
							if (timestamp.compareTo(details.getEndTime()) >= 0) {
								end = i;
								break;
							}
						}
					}
					// check if the start bound needs to be returned
					if (returnBounds && values.isEmpty() && start != i
							&& DateTime.MIN_VALUE.compareTo(details.getStartTime()) != 0) {
						// start bound
						if (start == -1) {
							values.add(new DataValue(Variant.NULL, new StatusCode(StatusCodes.Bad_NotFound),
									details.getStartTime(), details.getStartTime()));
						} else {
							values.addLast(historyValue[i]);
						}
						// check if absolute max values specified
						if (sizeLimit) {
							if (details.getNumValuesPerNode().longValue() > 0
									&& details.getNumValuesPerNode().longValue() < values.size()) {
								break;
							}
						}
					}
					// add value
					values.addLast(historyValue[i]);
					if (details.getIsReadModified()) {
						// TODO
					}
				} finally {
					if (timeFlowsBackward) {
						i--;
					} else {
						i++;
					}
				}
			}
			// add end bound
			while (returnBounds && DateTime.MIN_VALUE.compareTo(details.getEndTime()) != 0) {
				if (values.isEmpty()) {
					// add start bound
					if (start == -1) {
						values.add(new DataValue(Variant.NULL, new StatusCode(StatusCodes.Bad_NotFound),
								details.getStartTime(), details.getStartTime()));
					} else {
						values.addLast(historyValue[i]);
					}
				}
				// check if absolute max values specified
				if (sizeLimit) {
					if (details.getNumValuesPerNode().longValue() > 0
							&& details.getNumValuesPerNode().longValue() < values.size()) {
						break;
					}
				}
				// add end bound.
				if (end == -1) {
					values.addLast(new DataValue(Variant.NULL, new StatusCode(StatusCodes.Bad_NotFound),
							details.getEndTime(), details.getEndTime()));
				} else {
					values.addLast(historyValue[i]);
				}
				break;
			}
		}
		HistoryReadCPItem historyRequest = new HistoryReadCPItem();
		historyRequest.setValues(values);
		historyRequest.setNumValuesPerNode(details.getNumValuesPerNode());
		historyRequest.setFilter(null);
		// modificationInfos ?
		return historyRequest;
	}

	private HistoryReadCPItem createHistoryRequest(ReadProcessedDetails details, Node handle,
			HistoryReadValueId nodeToRead) {
		return null;
	}

	/**
	 * Loads a continuation point with an id.
	 * 
	 * @param session
	 * 
	 * @param Context
	 * @param Id      byte[] id of the continuation point.
	 * @return
	 */
	private HistoryReadCPItem loadHistoryContinuationPoint(OPCServerSession session, byte[] id) {
		return session.restoreHistoryContinuationPoint(id);
	}

	private byte[] saveHistoryContinuationPoint(OPCServerSession session, HistoryReadCPItem historyRead) {
		UUID uuid = UUID.randomUUID();
		byte[] id = UUIDUtil.UUIdAsByteArray(uuid);
		session.saveHistoryContinuationPoint(uuid, historyRead);
		historyRead.setContinuationPoint(id);
		return historyRead.getContinuationPoint();
	}

	public ServiceResult historyUpdate(HistoryUpdateDetails detail, Node handle, HistoryUpdateResult result) {
		// handle update data request
		if (detail instanceof UpdateDataDetails) {
			HistoryUpdateQueryMode mode = HistoryUpdateQueryMode
					.valueOf(((UpdateDataDetails) detail).getPerformInsertReplace().getValue());
			historyUpdateData((UpdateDataDetails) detail, mode, handle, result);
		} else if (detail instanceof UpdateEventDetails) {
			HistoryUpdateQueryMode mode = HistoryUpdateQueryMode
					.valueOf(((UpdateEventDetails) detail).getPerformInsertReplace().getValue());
			historyUpdateEvent((UpdateEventDetails) detail, mode, handle, result);
		} else if (detail instanceof DeleteRawModifiedDetails) {
			HistoryUpdateQueryMode mode = HistoryUpdateQueryMode.DELETE;
			historyDeleteRawModified((DeleteRawModifiedDetails) detail, mode, handle, result);
		} else if (detail instanceof DeleteAtTimeDetails) {
			HistoryUpdateQueryMode mode = HistoryUpdateQueryMode.DELETE;
			historyDeleteAtTimeDetails((DeleteAtTimeDetails) detail, mode, handle, result);
		} else if (detail instanceof DeleteEventDetails) {
			HistoryUpdateQueryMode mode = HistoryUpdateQueryMode.DELETE;
			historyDeleteEventDetails((DeleteEventDetails) detail, mode, handle, result);
		}
		return new ServiceResult(StatusCode.GOOD);
	}

	private void historyUpdateData(UpdateDataDetails details, HistoryUpdateQueryMode mode, Node handle,
			HistoryUpdateResult result) {
		if (details == null) {
			return;
		}
		List<StatusCode> operationResults = new ArrayList<>();
		for (int i = 0; i < details.getUpdateValues().length; i++) {
			DataValue data2update = details.getUpdateValues()[i];
			StatusCode error = updateHistory(handle, data2update, mode, DateTime.MIN_VALUE, DateTime.MIN_VALUE);
			operationResults.add(error);
		}
		result.setOperationResults(operationResults.toArray(new StatusCode[0]));
	}

	private void historyUpdateEvent(UpdateEventDetails details, HistoryUpdateQueryMode mode, Node handle,
			HistoryUpdateResult result) {
		result.setStatusCode(new StatusCode(StatusCodes.Bad_HistoryOperationUnsupported));
	}

	private void historyDeleteRawModified(DeleteRawModifiedDetails details, HistoryUpdateQueryMode mode, Node handle,
			HistoryUpdateResult result) {
		DateTime startTime = details.getStartTime();
		DateTime endTime = details.getEndTime();
		List<StatusCode> operationResults = new ArrayList<>();
		StatusCode status = updateHistory(handle, null, mode, startTime, endTime);
		operationResults.add(status);
		result.setOperationResults(operationResults.toArray(new StatusCode[0]));
	}

	private void historyDeleteAtTimeDetails(DeleteAtTimeDetails details, HistoryUpdateQueryMode mode, Node handle,
			HistoryUpdateResult result) {
		List<StatusCode> operationResults = new ArrayList<>();
		for (DateTime data2delete : details.getReqTimes()) {
			StatusCode status = updateHistory(handle, null, mode, data2delete, data2delete);
			operationResults.add(status);
		}
		result.setOperationResults(operationResults.toArray(new StatusCode[0]));
	}

	private void historyDeleteEventDetails(DeleteEventDetails details, HistoryUpdateQueryMode mode, Node handle,
			HistoryUpdateResult result) {
		result.setStatusCode(new StatusCode(StatusCodes.Bad_HistoryOperationUnsupported));
	}

	@Override
	public void writehistory(Node node, DataValue value) {
		// not implemented
	}

	@Override
	public DataValue[] readHistory(Node node, DateTime startTime, DateTime endTime, UnsignedInteger maxValuesPerNode,
			boolean isReadModified, boolean timeFlowsBackward) {
		return new DataValue[0];
	}

	@Override
	public StatusCode updateHistory(Node node, DataValue value, HistoryUpdateQueryMode mode, DateTime startTime,
			DateTime endTime) {
		return new StatusCode(StatusCodes.Bad_NotSupported);
	}
}
