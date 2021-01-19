package opc.sdk.core.classes.ua.core;

public class BaseEventType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.BaseEventType;
	private PropertyType localTime;
	private PropertyType severity;
	private PropertyType sourceNode;
	private PropertyType time;
	private PropertyType sourceName;
	private PropertyType eventId;
	private PropertyType eventType;
	private PropertyType message;
	private PropertyType receiveTime;

	public BaseEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getLocalTime() {
		return localTime;
	}

	public void setLocalTime(PropertyType value) {
		localTime = value;
	}

	public PropertyType getSeverity() {
		return severity;
	}

	public void setSeverity(PropertyType value) {
		severity = value;
	}

	public PropertyType getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(PropertyType value) {
		sourceNode = value;
	}

	public PropertyType getTime() {
		return time;
	}

	public void setTime(PropertyType value) {
		time = value;
	}

	public PropertyType getSourceName() {
		return sourceName;
	}

	public void setSourceName(PropertyType value) {
		sourceName = value;
	}

	public PropertyType getEventId() {
		return eventId;
	}

	public void setEventId(PropertyType value) {
		eventId = value;
	}

	public PropertyType getEventType() {
		return eventType;
	}

	public void setEventType(PropertyType value) {
		eventType = value;
	}

	public PropertyType getMessage() {
		return message;
	}

	public void setMessage(PropertyType value) {
		message = value;
	}

	public PropertyType getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(PropertyType value) {
		receiveTime = value;
	}

	@Override
	public String toString() {
		return "BaseEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
