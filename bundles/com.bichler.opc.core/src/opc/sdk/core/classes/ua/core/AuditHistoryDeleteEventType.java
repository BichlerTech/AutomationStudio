package opc.sdk.core.classes.ua.core;

public class AuditHistoryDeleteEventType extends AuditHistoryUpdateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditHistoryDeleteEventType;
	private PropertyType updatedNode;

	public AuditHistoryDeleteEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getUpdatedNode() {
		return updatedNode;
	}

	public void setUpdatedNode(PropertyType value) {
		updatedNode = value;
	}

	@Override
	public String toString() {
		return "AuditHistoryDeleteEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
