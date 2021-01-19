package opc.sdk.core.classes.ua.core;

public class BaseModelChangeEventType extends BaseEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.BaseModelChangeEventType;

	public BaseModelChangeEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "BaseModelChangeEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
