package opc.sdk.core.classes.ua.core;

public class AuditChannelEventType extends AuditSecurityEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditChannelEventType;
	private PropertyType secureChannelId;

	public AuditChannelEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getSecureChannelId() {
		return secureChannelId;
	}

	public void setSecureChannelId(PropertyType value) {
		secureChannelId = value;
	}

	@Override
	public String toString() {
		return "AuditChannelEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
