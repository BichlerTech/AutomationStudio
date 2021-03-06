package opc.sdk.server.service.history;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import opc.sdk.core.node.Node;
import opc.sdk.ua.constants.HistoryUpdateQueryMode;

/**
 * History access interface.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public interface HistoryAccess {
	public void writehistory(Node node, DataValue value);

	public DataValue[] readHistory(Node node, DateTime startTime, DateTime endTime, UnsignedInteger maxValuesPerNode,
			boolean isReadModified, boolean timeFlowsBackward);

	/**
	 * 
	 * @param node
	 * @param value
	 * @param mode
	 * @param startTime
	 * @param endTime
	 * 
	 * @return StatusCode result of the node operation.
	 */
	public StatusCode updateHistory(Node node, DataValue value, HistoryUpdateQueryMode mode, DateTime startTime,
			DateTime endTime);
}
