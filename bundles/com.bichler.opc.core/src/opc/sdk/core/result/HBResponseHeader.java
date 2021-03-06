package opc.sdk.core.result;

import opc.sdk.core.context.StringTable;

import org.opcfoundation.ua.core.ResponseHeader;

public class HBResponseHeader extends ResponseHeader {
	private StringTable responseTable = new StringTable();

	public HBResponseHeader() {
		super();
	}

	public StringTable getResponseTable() {
		return this.responseTable;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (super.equals(obj)) {
			if (getClass() != obj.getClass())
				return false;
			HBResponseHeader other = (HBResponseHeader) obj;
			return other.responseTable.equals(responseTable);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((responseTable == null) ? 0 : responseTable.hashCode());
		return result;
	}
}
