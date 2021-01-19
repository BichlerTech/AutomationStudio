package opc.sdk.core.classes.ua.core;

public class AuditDeleteNodesEventType extends AuditNodeManagementEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditDeleteNodesEventType;
	private PropertyType nodesToDelete;

	public AuditDeleteNodesEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getNodesToDelete() {
		return nodesToDelete;
	}

	public void setNodesToDelete(PropertyType value) {
		nodesToDelete = value;
	}

	@Override
	public String toString() {
		return "AuditDeleteNodesEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
