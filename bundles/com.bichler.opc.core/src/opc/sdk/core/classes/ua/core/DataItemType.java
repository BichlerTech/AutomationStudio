package opc.sdk.core.classes.ua.core;

public class DataItemType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DataItemType;
	private PropertyType definition;
	private PropertyType valuePrecision;

	public DataItemType() {
		super();
	}

	@Override
	public java.lang.Object getValue() {
		return getVariant() != null ? getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getDefinition() {
		return definition;
	}

	public void setDefinition(PropertyType value) {
		definition = value;
	}

	public PropertyType getValuePrecision() {
		return valuePrecision;
	}

	public void setValuePrecision(PropertyType value) {
		valuePrecision = value;
	}

	@Override
	public String toString() {
		return "DataItemType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
