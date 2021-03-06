package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.core.TimeZoneDataType;

import opc.sdk.core.context.ISystemContext;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.EventSeverity;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class BaseEventType extends BaseObjectType {
	protected PropertyVariableType<byte[]> eventId = null;
	protected PropertyVariableType<NodeId> eventType = null;
	protected PropertyVariableType<NodeId> sourceNode = null;
	protected PropertyVariableType<String> sourceName = null;
	protected PropertyVariableType<DateTime> time = null;
	protected PropertyVariableType<DateTime> receiveTime = null;
	protected PropertyVariableType<TimeZoneDataType> localTime = null;
	protected PropertyVariableType<LocalizedText> message = null;
	protected PropertyVariableType<UnsignedShort> severity = null;

	/**
	 * Qualified names of event
	 */
	private static final QualifiedName eventIdName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.EVENTID);
	protected QualifiedName eventTypeName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.EVENTTYPE);
	protected QualifiedName sourceNodeName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.SOURCENODE);
	protected QualifiedName sourceNameName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.SOURCENAME);
	protected QualifiedName timeName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.TIME);
	protected QualifiedName receiveTimeName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.RECEIVETIME);
	protected QualifiedName localTimeName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.LOCALTIME);
	protected QualifiedName messageName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.MESSAGE);
	protected QualifiedName severityName = new QualifiedName(UnsignedShort.valueOf(0), BrowseNames.SEVERITY);

	public BaseEventType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.BaseEventType;
	}

	public void initialize(ISystemContext context, BaseNode source, EventSeverity severity, LocalizedText message) {
		this.eventId = new PropertyVariableType<>(this);
		// SET NEW ID
		this.eventType = new PropertyVariableType<>(this);
		this.eventType.setValue(getDefaultTypeDefinitionId());
		setTypeDefinitionId(this.eventType.getValue());
		if (source != null) {
			if (!NodeId.isNull(source.getNodeId())) {
				this.sourceNode = new PropertyVariableType<>(this);
				this.sourceNode.setValue(source.getNodeId());
			}
			if (!QualifiedName.isNull(source.getBrowseName())) {
				this.sourceName = new PropertyVariableType<>(this);
				this.sourceName.setValue(source.getBrowseName().getName());
			}
		}
		this.time = new PropertyVariableType<>(this);
		this.time.setValue(DateTime.currentTime());
		this.receiveTime = new PropertyVariableType<>(this);
		this.receiveTime.setValue(DateTime.currentTime());
		if (severity != null) {
			this.severity = new PropertyVariableType<>(this);
			this.severity.setValue(severity.severity());
		}
		if (message != null) {
			this.message = new PropertyVariableType<>(this);
			this.message.setValue(message);
		}
	}

	public boolean validateEvent(EventFilter filter) {
		for (SimpleAttributeOperand operand : filter.getSelectClauses()) {
			if (operand.getBrowsePath().length > 0) {
				if (operand.getBrowsePath()[0].equals(eventIdName))
					return true;
				if (operand.getBrowsePath()[0].equals(eventTypeName))
					return true;
				if (operand.getBrowsePath()[0].equals(sourceNodeName))
					return true;
				if (operand.getBrowsePath()[0].equals(sourceNameName))
					return true;
				if (operand.getBrowsePath()[0].equals(timeName))
					return true;
				if (operand.getBrowsePath()[0].equals(receiveTimeName))
					return true;
				if (operand.getBrowsePath()[0].equals(localTimeName))
					return true;
				if (operand.getBrowsePath()[0].equals(messageName))
					return true;
				if (operand.getBrowsePath()[0].equals(severityName))
					return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.EVENTID:
			if (createOrReplace && this.eventId == null) {
				if (replacement == null) {
					setEventId(new PropertyVariableType<byte[]>(this));
				} else {
					setEventId((PropertyVariableType<byte[]>) replacement);
				}
			}
			instance = this.eventId;
			break;
		case BrowseNames.EVENTTYPE:
			if (createOrReplace && this.eventType == null) {
				if (replacement == null) {
					setEventType(new PropertyVariableType<NodeId>(this));
				} else {
					setEventType((PropertyVariableType<NodeId>) replacement);
				}
			}
			instance = this.eventType;
			break;
		case BrowseNames.SOURCENODE:
			if (createOrReplace && this.sourceNode == null) {
				if (replacement == null) {
					setSourceNode(new PropertyVariableType<NodeId>(this));
				} else {
					setSourceNode((PropertyVariableType<NodeId>) replacement);
				}
			}
			instance = this.sourceNode;
			break;
		case BrowseNames.SOURCENAME:
			if (createOrReplace && this.sourceName == null) {
				if (replacement == null) {
					setSourceName(new PropertyVariableType<String>(this));
				} else {
					setSourceName((PropertyVariableType<String>) replacement);
				}
			}
			instance = this.sourceName;
			break;
		case BrowseNames.TIME:
			if (createOrReplace && this.time == null) {
				if (replacement == null) {
					setTime(new PropertyVariableType<DateTime>(this));
				} else {
					setTime((PropertyVariableType<DateTime>) replacement);
				}
			}
			instance = this.time;
			break;
		case BrowseNames.RECEIVETIME:
			if (createOrReplace && this.receiveTime == null) {
				if (replacement == null) {
					setReceiveTime(new PropertyVariableType<DateTime>(this));
				} else {
					setReceiveTime((PropertyVariableType<DateTime>) replacement);
				}
			}
			instance = this.receiveTime;
			break;
		case BrowseNames.LOCALTIME:
			if (createOrReplace && this.localTime == null) {
				if (replacement == null) {
					setLocalTime(new PropertyVariableType<TimeZoneDataType>(this));
				} else {
					setLocalTime((PropertyVariableType<TimeZoneDataType>) replacement);
				}
			}
			instance = this.localTime;
			break;
		case BrowseNames.MESSAGE:
			if (createOrReplace && this.message == null) {
				if (replacement == null) {
					setMessage(new PropertyVariableType<LocalizedText>(this));
				} else {
					setMessage((PropertyVariableType<LocalizedText>) replacement);
				}
			}
			instance = this.message;
			break;
		case BrowseNames.SEVERITY:
			if (createOrReplace && this.severity == null) {
				if (replacement == null) {
					setSeverity(new PropertyVariableType<UnsignedShort>(this));
				} else {
					setSeverity((PropertyVariableType<UnsignedShort>) replacement);
				}
			}
			instance = this.severity;
			break;
		default:
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.eventId != null) {
			children.add(this.eventId);
		}
		if (this.eventType != null) {
			children.add(this.eventType);
		}
		if (this.sourceNode != null) {
			children.add(this.sourceNode);
		}
		if (this.sourceName != null) {
			children.add(this.sourceName);
		}
		if (this.time != null) {
			children.add(this.time);
		}
		if (this.receiveTime != null) {
			children.add(this.receiveTime);
		}
		if (this.localTime != null) {
			children.add(this.localTime);
		}
		if (this.message != null) {
			children.add(this.message);
		}
		if (this.severity != null) {
			children.add(this.severity);
		}
		children.addAll(super.getChildren());
		return children;
	}

	/**
	 * @return the eventId
	 */
	public PropertyVariableType<byte[]> getEventId() {
		return eventId;
	}

	/**
	 * @return the eventType
	 */
	public PropertyVariableType<NodeId> getEventType() {
		return eventType;
	}

	/**
	 * @return the sourceNode
	 */
	public PropertyVariableType<NodeId> getSourceNode() {
		return sourceNode;
	}

	/**
	 * @return the sourceName
	 */
	public PropertyVariableType<String> getSourceName() {
		return sourceName;
	}

	/**
	 * @return the time
	 */
	public PropertyVariableType<DateTime> getTime() {
		return time;
	}

	/**
	 * @return the receiveTime
	 */
	public PropertyVariableType<DateTime> getReceiveTime() {
		return receiveTime;
	}

	/**
	 * @return the localTime
	 */
	public PropertyVariableType<TimeZoneDataType> getLocalTime() {
		return localTime;
	}

	/**
	 * @return the message
	 */
	public PropertyVariableType<LocalizedText> getMessage() {
		return message;
	}

	/**
	 * @return the severity
	 */
	public PropertyVariableType<UnsignedShort> getSeverity() {
		return severity;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(PropertyVariableType<byte[]> eventId) {
		if (this.eventId != eventId) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.eventId = eventId;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(PropertyVariableType<NodeId> eventType) {
		if (this.eventType != eventType) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.eventType = eventType;
	}

	public void setSourceNode(NodeId sourceNodeId) {
		PropertyVariableType<NodeId> pvt = new PropertyVariableType<NodeId>(null);
		pvt.setValue(sourceNodeId);
		setSourceNode(pvt);
	}

	/**
	 * @param sourceNode the sourceNode to set
	 */
	public void setSourceNode(PropertyVariableType<NodeId> sourceNode) {
		if (this.sourceNode != sourceNode) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sourceNode = sourceNode;
	}

	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(PropertyVariableType<String> sourceName) {
		if (this.sourceName != sourceName) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sourceName = sourceName;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(PropertyVariableType<DateTime> time) {
		if (this.time != time) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.time = time;
	}

	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(PropertyVariableType<DateTime> receiveTime) {
		if (this.receiveTime != receiveTime) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.receiveTime = receiveTime;
	}

	/**
	 * @param localTime the localTime to set
	 */
	public void setLocalTime(PropertyVariableType<TimeZoneDataType> localTime) {
		if (this.localTime != localTime) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.localTime = localTime;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(PropertyVariableType<LocalizedText> message) {
		if (this.message != message) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.message = message;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(PropertyVariableType<UnsignedShort> severity) {
		if (this.severity != severity) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.severity = severity;
	}
}
