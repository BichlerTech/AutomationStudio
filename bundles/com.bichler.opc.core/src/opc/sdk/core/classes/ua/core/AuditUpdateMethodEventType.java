package opc.sdk.core.classes.ua.core;

public class AuditUpdateMethodEventType extends AuditEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditUpdateMethodEventType;
	private PropertyType inputArguments;
	private PropertyType methodId;

	public AuditUpdateMethodEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getInputArguments() {
		return inputArguments;
	}

	public void setInputArguments(PropertyType value) {
		inputArguments = value;
	}

	public PropertyType getMethodId() {
		return methodId;
	}

	public void setMethodId(PropertyType value) {
		methodId = value;
	}

	@Override
	public String toString() {
		return "AuditUpdateMethodEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
