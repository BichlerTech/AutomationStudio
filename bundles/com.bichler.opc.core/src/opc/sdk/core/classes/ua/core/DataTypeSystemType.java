package opc.sdk.core.classes.ua.core;

public class DataTypeSystemType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DataTypeSystemType;

	public DataTypeSystemType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "DataTypeSystemType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
