package opc.sdk.server.service.subscribe;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public class OPCMonitoredItemQueue {
	private int start;
	private DataValue[] values = null;
	private int end;
	private int overflow;
	private boolean discardOldest;
	private DataValue lastValue;

	public OPCMonitoredItemQueue(Boolean discardOldest) {
		this.values = null;
		this.start = -1;
		this.end = -1;
		this.overflow = -1;
		this.discardOldest = discardOldest;
	}

	/**
	 * Dequeues a value
	 * 
	 * @param value
	 * @return
	 */
	public boolean dequeue(DataValue value) {
		// check for empty queue
		if (this.start < 0) {
			return false;
		}
		DataValue dequeuedValue = this.values[this.start];
		if (dequeuedValue != null) {
			value.setValue(dequeuedValue.getValue());
			value.setServerPicoseconds(dequeuedValue.getServerPicoseconds());
			value.setServerTimestamp(dequeuedValue.getServerTimestamp());
			value.setSourcePicoseconds(dequeuedValue.getSourcePicoseconds());
			value.setSourceTimestamp(dequeuedValue.getSourceTimestamp());
			value.setStatusCode(dequeuedValue.getStatusCode());
		}
		this.values[this.start] = null;
		// set overflow bit
		if (this.overflow == this.start) {
			setOverflowBit(value);
			this.overflow = -1;
		}
		this.start++;
		// check if queue has been emptied
		if (this.start == this.end) {
			this.start = -1;
			this.end = 0;
		}
		// check for wrap around
		else if (this.start >= this.values.length) {
			this.start = 0;
		}
		return true;
	}

	/**
	 * Queues a new value.
	 * 
	 * @param value
	 */
	public void queueValue(DataValue value) {
		this.lastValue = value;
		// check for empty queue
		if (this.start < 0) {
			this.start = 0;
			this.end = 1;
			this.overflow = -1;
			this.values[this.start] = value;
			return;
		}
		int next = this.end;
		// check for wrap around
		if (next >= this.values.length) {
			next = 0;
		}
		// check if queue is full
		if (this.start == next) {
			if (!this.discardOldest) {
				// overwrite newest
				this.values[this.end - 1] = value;
				this.overflow = this.end - 1;
				return;
			}
			// removes oldest
			this.start++;
			if (this.start >= this.values.length) {
				this.start = 0;
			}
			// set overflow bit
			this.overflow = this.start;
		}
		// add value
		this.values[next] = value;
		this.end = next + 1;
		return;
	}

	private void setOverflowBit(DataValue value) {
		int queueSize = getQueueSize();
		if (value != null && queueSize <= 1) {
			StatusCode statuscode = value.getStatusCode();
			if (statuscode == null) {
				statuscode = StatusCode.BAD;
			}
			boolean isOverflow = statuscode.isOverflow();
			// remove overflow
			if (isOverflow) {
				int status = statuscode.getValueAsIntBits();
				int newStatus = status & ~StatusCode.OVERFLOW_MASK;
				StatusCode overflowStatus = new StatusCode(new UnsignedInteger(newStatus));
				value.setStatusCode(overflowStatus);
				// return;
			}
			// int status = statuscode.getValueAsIntBits();
			// int newStatus = status | StatusCode.OVERFLOW_MASK;
			// StatusCode overflowStatus = new StatusCode(new UnsignedInteger(
			// newStatus));
			// value.setStatusCode(overflowStatus);
		}
		// only set overflow when queuesize > 1
		if (value != null && queueSize > 1) {
			StatusCode statuscode = value.getStatusCode();
			if (statuscode == null) {
				statuscode = StatusCode.BAD;
			}
			boolean isOverflow = statuscode.isOverflow();
			if (isOverflow) {
				return;
			}
			int status = statuscode.getValueAsIntBits();
			int newStatus = status | StatusCode.OVERFLOW_MASK;
			StatusCode overflowStatus = new StatusCode(new UnsignedInteger(newStatus));
			value.setStatusCode(overflowStatus);
		}
	}

	/**
	 * Returns the queued value.
	 * 
	 * @return
	 */
	public DataValue getLastValue() {
		return this.lastValue;
	}

	/**
	 * Returns the queue size.
	 * 
	 * @return
	 */
	public int getQueueSize() {
		if (this.values == null) {
			return 0;
		}
		return this.values.length;
	}

	/**
	 * Sets the last queued value. TODO: CHECK IF NEEDED
	 * 
	 * @param value
	 */
	void setLastValue(DataValue value) {
		this.lastValue = value;
	}

	/**
	 * Sets or modifies the queue size and its discard oldest flag
	 * 
	 * @param queueSize
	 * @param discardOldest
	 */
	void setQueueSize(UnsignedInteger queueSize, Boolean discardOldest) {
		int length = (int) queueSize.longValue();
		if (length < 1) {
			length = 1;
		}
		// copy existing values
		List<DataValue> existingValues = new ArrayList<DataValue>();
		if (this.start >= 0) {
			// reference from a dequeued value
			DataValue dequeuedValue = new DataValue();
			// reference form a dequeued error
			while (dequeue(dequeuedValue)) {
				// new instance
				existingValues.add(dequeuedValue);
			}
		}
		// update internals
		this.values = new DataValue[length];
		this.start = -1;
		this.end = 0;
		this.overflow = -1;
		this.discardOldest = discardOldest;
		if (existingValues != null) {
			for (int ii = 0; ii < existingValues.size(); ii++) {
				queueValue(existingValues.get(ii));
			}
		}
	}
}
