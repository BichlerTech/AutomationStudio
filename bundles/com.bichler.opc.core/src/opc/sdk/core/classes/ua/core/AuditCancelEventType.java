package opc.sdk.core.classes.ua.core;

public class AuditCancelEventType extends AuditSessionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCancelEventType;
	private PropertyType requestHandle;

	public AuditCancelEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getRequestHandle() {
		return requestHandle;
	}

	public void setRequestHandle(PropertyType value) {
		requestHandle = value;
	}

	@Override
	public String toString() {
		return "AuditCancelEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
