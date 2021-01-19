package opc.sdk.server.service.history;

import java.util.UUID;

import org.opcfoundation.ua.builtintypes.DateTime;

/**
 * A history continuation point to continue reading history values.
 * 
 * @author Thomas Z&ouml;chbauer
 *
 */
public class HistoryContinuationPoint {
	private UUID id = null;
	private HistoryReadCPItem value = null;
	private DateTime timestamp = null;

	public HistoryContinuationPoint() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public HistoryReadCPItem getValue() {
		return value;
	}

	public void setValue(HistoryReadCPItem value) {
		this.value = value;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
}
