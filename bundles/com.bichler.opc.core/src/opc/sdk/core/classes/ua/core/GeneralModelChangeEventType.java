package opc.sdk.core.classes.ua.core;

public class GeneralModelChangeEventType extends BaseModelChangeEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.GeneralModelChangeEventType;
	private PropertyType changes;

	public GeneralModelChangeEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getChanges() {
		return changes;
	}

	public void setChanges(PropertyType value) {
		changes = value;
	}

	@Override
	public String toString() {
		return "GeneralModelChangeEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
