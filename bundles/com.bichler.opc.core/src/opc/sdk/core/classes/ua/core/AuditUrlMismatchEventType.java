package opc.sdk.core.classes.ua.core;

public class AuditUrlMismatchEventType extends AuditCreateSessionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditUrlMismatchEventType;
	private PropertyType endpointUrl;

	public AuditUrlMismatchEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(PropertyType value) {
		endpointUrl = value;
	}

	@Override
	public String toString() {
		return "AuditUrlMismatchEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
