package opc.sdk.core.newApplication;

public class NamespaceItem {
	private String origin = "";
	private String modified = null;

	public NamespaceItem(String ns) {
		this.origin = ns;
	}

	public String getNamespace() {
		if (this.modified != null) {
			return this.modified;
		}
		return this.origin;
	}

	public void setModifiedNamespace(String namespace) {
		this.modified = namespace;
	}

	public String getOriginNamespace() {
		return this.origin;
	}
}
