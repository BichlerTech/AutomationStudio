package opc.sdk.core.classes.ua.core;

public class AggregateFunctionType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AggregateFunctionType;

	public AggregateFunctionType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AggregateFunctionType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
