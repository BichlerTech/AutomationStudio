package opc.sdk.core.classes.ua.core;

public class AuditConditionShelvingEventType extends AuditConditionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditConditionShelvingEventType;

	public AuditConditionShelvingEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditConditionShelvingEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
