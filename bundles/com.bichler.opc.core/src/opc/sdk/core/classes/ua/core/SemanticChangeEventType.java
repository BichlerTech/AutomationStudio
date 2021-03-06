package opc.sdk.core.classes.ua.core;

public class SemanticChangeEventType extends BaseModelChangeEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SemanticChangeEventType;
	private PropertyType changes;

	public SemanticChangeEventType() {
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
		return "SemanticChangeEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
