package opc.sdk.core.classes.ua.core;

public class AuditHistoryUpdateEventType extends AuditUpdateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditHistoryUpdateEventType;
	private PropertyType parameterDataTypeId;

	public AuditHistoryUpdateEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getParameterDataTypeId() {
		return parameterDataTypeId;
	}

	public void setParameterDataTypeId(PropertyType value) {
		parameterDataTypeId = value;
	}

	@Override
	public String toString() {
		return "AuditHistoryUpdateEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
