package opc.sdk.core.classes.ua.core;

public class AuditEventType extends BaseEventType {
	private static org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditEventType;
	private PropertyType actionTimeStamp;
	private PropertyType clientUserId;
	private PropertyType clientAuditEntryId;
	private PropertyType status;
	private PropertyType serverId;

	public AuditEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getActionTimeStamp() {
		return actionTimeStamp;
	}

	public void setActionTimeStamp(PropertyType value) {
		actionTimeStamp = value;
	}

	public PropertyType getClientUserId() {
		return clientUserId;
	}

	public void setClientUserId(PropertyType value) {
		clientUserId = value;
	}

	public PropertyType getClientAuditEntryId() {
		return clientAuditEntryId;
	}

	public void setClientAuditEntryId(PropertyType value) {
		clientAuditEntryId = value;
	}

	public PropertyType getStatus() {
		return status;
	}

	public void setStatus(PropertyType value) {
		status = value;
	}

	public PropertyType getServerId() {
		return serverId;
	}

	public void setServerId(PropertyType value) {
		serverId = value;
	}

	@Override
	public String toString() {
		return "AuditEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
