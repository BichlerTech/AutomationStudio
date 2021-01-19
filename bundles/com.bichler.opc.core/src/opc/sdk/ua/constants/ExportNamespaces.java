package opc.sdk.ua.constants;

/**
 * Enum of the namespaces to write in the exported file
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum ExportNamespaces {
	XMLSCHEMA("http://www.w3.org/2001/XMLSchema-instance"), TYPES("http://opcfoundation.org/UA/2008/02/Types.xsd");

	private String namespace = null;

	ExportNamespaces(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public String toString() {
		return this.namespace;
	}
}
