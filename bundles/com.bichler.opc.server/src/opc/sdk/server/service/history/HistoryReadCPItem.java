package opc.sdk.server.service.history;

import java.util.Collection;
import java.util.LinkedList;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.AggregateFilter;

/**
 * 
 * A history read continuation point item.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public class HistoryReadCPItem {
	private LinkedList<DataValue> values;
	private UnsignedInteger numValuesPerNode;
	private AggregateFilter filter;
	private byte[] continuationPoint;

	public LinkedList<DataValue> getValues() {
		return values;
	}

	public void setValues(Collection<DataValue> values) {
		if (this.values == null) {
			this.values = new LinkedList<DataValue>();
		}
		if (this.values.size() > 0) {
			this.values.clear();
		}
		this.values.addAll(values);
	}

	public UnsignedInteger getNumValuesPerNode() {
		return numValuesPerNode;
	}

	public AggregateFilter getFilter() {
		return filter;
	}

	public void setNumValuesPerNode(UnsignedInteger numValuesPerNode) {
		this.numValuesPerNode = numValuesPerNode;
	}

	public void setFilter(AggregateFilter filter) {
		this.filter = filter;
	}

	public void setContinuationPoint(byte[] continuationPoint) {
		this.continuationPoint = continuationPoint;
	}

	public byte[] getContinuationPoint() {
		return this.continuationPoint;
	}
}
