package opc.sdk.core.classes.ua.core;

public class AuditAddNodesEventType extends BaseEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditAddNodesEventType;
	private PropertyType nodesToAdd;

	public AuditAddNodesEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getNodesToAdd() {
		return nodesToAdd;
	}

	public void setNodesToAdd(PropertyType value) {
		nodesToAdd = value;
	}

	@Override
	public String toString() {
		return "AuditAddNodesEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
