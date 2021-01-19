package opc.sdk.core.classes.ua.core;

public class BaseObjectType extends opc.sdk.core.classes.ua.base.BaseObjectTypeGen {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.BaseObjectType;

	public BaseObjectType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "BaseObjectType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
