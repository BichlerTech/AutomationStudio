package opc.sdk.core.classes.ua.core;

public class AuditDeleteReferencesEventType extends AuditNodeManagementEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditDeleteReferencesEventType;
	private PropertyType referencesToDelete;

	public AuditDeleteReferencesEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getReferencesToDelete() {
		return referencesToDelete;
	}

	public void setReferencesToDelete(PropertyType value) {
		referencesToDelete = value;
	}

	@Override
	public String toString() {
		return "AuditDeleteReferencesEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
