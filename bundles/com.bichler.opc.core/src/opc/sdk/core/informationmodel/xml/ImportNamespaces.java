package opc.sdk.core.informationmodel.xml;

public enum ImportNamespaces {
	XMLSCHEMA("http://www.w3.org/2001/XMLSchema-instance"), TYPES("http://opcfoundation.org/UA/2008/02/Types.xsd");

	private String namespace = null;

	ImportNamespaces(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public String toString() {
		return this.namespace;
	}
}
