package opc.sdk.core.classes.ua.core;

public class AuditConditionConfirmEventType extends AuditConditionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditConditionConfirmEventType;

	public AuditConditionConfirmEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditConditionConfirmEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
