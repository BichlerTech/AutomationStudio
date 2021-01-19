package opc.sdk.core.classes.ua.core;

public class BaseConditionClassType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.BaseConditionClassType;

	public BaseConditionClassType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "BaseConditionClassType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
