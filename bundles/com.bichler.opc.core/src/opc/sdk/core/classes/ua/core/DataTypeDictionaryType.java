package opc.sdk.core.classes.ua.core;

public class DataTypeDictionaryType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DataTypeDictionaryType;
	private PropertyType namespaceUri;
	private PropertyType dataTypeVersion;

	public DataTypeDictionaryType() {
		super();
	}

	@Override
	public byte[] getValue() {
		return getVariant() != null ? (byte[]) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getNamespaceUri() {
		return namespaceUri;
	}

	public void setNamespaceUri(PropertyType value) {
		namespaceUri = value;
	}

	public PropertyType getDataTypeVersion() {
		return dataTypeVersion;
	}

	public void setDataTypeVersion(PropertyType value) {
		dataTypeVersion = value;
	}

	@Override
	public String toString() {
		return "DataTypeDictionaryType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
