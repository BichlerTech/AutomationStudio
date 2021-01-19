package opc.sdk.core.classes.ua.core;

public class DataTypeEncodingType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DataTypeEncodingType;

	public DataTypeEncodingType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "DataTypeEncodingType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
