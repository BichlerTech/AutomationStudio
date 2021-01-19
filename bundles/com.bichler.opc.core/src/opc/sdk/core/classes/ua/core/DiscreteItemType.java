package opc.sdk.core.classes.ua.core;

public class DiscreteItemType extends DataItemType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DiscreteItemType;

	public DiscreteItemType() {
		super();
	}

	@Override
	public java.lang.Object getValue() {
		return getVariant() != null ? getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "DiscreteItemType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
