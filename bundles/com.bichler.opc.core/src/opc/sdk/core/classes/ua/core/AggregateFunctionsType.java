package opc.sdk.core.classes.ua.core;

public class AggregateFunctionsType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AggregateFunctionsType;

	public AggregateFunctionsType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AggregateFunctionsType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
