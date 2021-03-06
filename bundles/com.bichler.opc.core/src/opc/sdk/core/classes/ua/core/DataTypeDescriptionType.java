package opc.sdk.core.classes.ua.core;

public class DataTypeDescriptionType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DataTypeDescriptionType;
	private PropertyType dictionaryFragment;
	private PropertyType dataTypeVersion;

	public DataTypeDescriptionType() {
		super();
	}

	@Override
	public java.lang.String getValue() {
		return getVariant() != null ? (java.lang.String) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getDictionaryFragment() {
		return dictionaryFragment;
	}

	public void setDictionaryFragment(PropertyType value) {
		dictionaryFragment = value;
	}

	public PropertyType getDataTypeVersion() {
		return dataTypeVersion;
	}

	public void setDataTypeVersion(PropertyType value) {
		dataTypeVersion = value;
	}

	@Override
	public String toString() {
		return "DataTypeDescriptionType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
