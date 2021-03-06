package opc.sdk.core.context;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.transport.tcp.io.SequenceNumber;

/**
 * String table for a session.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class StringTable {
	private List<String> strings = null;
	private int instanceId = -1;
	private boolean shared = false;
	private static SequenceNumber sglobalInstanceCount = new SequenceNumber();
	private Object lock = new Object();

	/**
	 * Creates an empty collection.
	 * 
	 * @param shared
	 */
	public StringTable() {
		this.strings = new ArrayList<String>();
		this.setInstanceId(sglobalInstanceCount.getCurrentSendSequenceNumber());
		StringTable.sglobalInstanceCount.getNextSendSequencenumber();
	}

	/**
	 * Creates an empty collection which is marked as shared.
	 * 
	 * @param shared
	 */
	public StringTable(boolean shared) {
		this.strings = new ArrayList<>();
		this.shared = shared;
		this.setInstanceId(sglobalInstanceCount.getCurrentSendSequenceNumber());
		StringTable.sglobalInstanceCount.getNextSendSequencenumber();
	}

	/**
	 * Copies a list of strings.
	 */
	public StringTable(List<String> strings) {
		update(this.strings);
		this.setInstanceId(sglobalInstanceCount.getCurrentSendSequenceNumber());
		StringTable.sglobalInstanceCount.getNextSendSequencenumber();
	}

	/**
	 * Adds a string to the end of the table
	 */
	public void append(String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("Value");
		}
		synchronized (this.lock) {
			this.strings.add(value);
		}
	}

	/**
	 * Updates the table of namespace uris.
	 * 
	 * @param mStrings
	 */
	public void update(List<String> strings) {
		if (strings == null)
			throw new IllegalArgumentException("strings");
		synchronized (this.lock) {
			this.strings = new ArrayList<>(strings);
			if (this.shared) {
				for (int ii = 0; ii < this.strings.size(); ii++) {
					// Utils.Trace("WARNING: Adding '{0}' to shared StringTable #{1}.",
					// m_strings.get(ii), m_instanceId);
				}
			}
		}
	}

	public List<String> getStrings() {
		return this.strings;
	}

	public String[] toArray() {
		return this.strings.toArray(new String[this.strings.size()]);
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
}
