package opc.sdk.core.classes.ua.core;

public class AuditAddReferencesEventType extends AuditNodeManagementEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditAddReferencesEventType;
	private PropertyType referencesToAdd;

	public AuditAddReferencesEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getReferencesToAdd() {
		return referencesToAdd;
	}

	public void setReferencesToAdd(PropertyType value) {
		referencesToAdd = value;
	}

	@Override
	public String toString() {
		return "AuditAddReferencesEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
