package opc.sdk.core.classes.ua.core;

public class AuditConditionAcknowledgeEventType extends AuditConditionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditConditionAcknowledgeEventType;

	public AuditConditionAcknowledgeEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditConditionAcknowledgeEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
