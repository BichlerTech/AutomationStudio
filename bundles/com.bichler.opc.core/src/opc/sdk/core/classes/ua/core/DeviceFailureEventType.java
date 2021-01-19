package opc.sdk.core.classes.ua.core;

public class DeviceFailureEventType extends SystemEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DeviceFailureEventType;

	public DeviceFailureEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "DeviceFailureEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
