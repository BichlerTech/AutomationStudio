package opc.sdk.core.classes.ua.core;

public class EventQueueOverflowEventType extends BaseEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.EventQueueOverflowEventType;

	public EventQueueOverflowEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "EventQueueOverflowEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
