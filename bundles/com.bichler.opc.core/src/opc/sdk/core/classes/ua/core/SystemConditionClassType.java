package opc.sdk.core.classes.ua.core;

public class SystemConditionClassType extends BaseConditionClassType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SystemConditionClassType;

	public SystemConditionClassType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "SystemConditionClassType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
