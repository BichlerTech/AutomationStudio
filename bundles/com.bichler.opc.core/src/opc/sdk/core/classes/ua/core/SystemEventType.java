package opc.sdk.core.classes.ua.core;

public class SystemEventType extends BaseEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SystemEventType;

	public SystemEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "SystemEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
