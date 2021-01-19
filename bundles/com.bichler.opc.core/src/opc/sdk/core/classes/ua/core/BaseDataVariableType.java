package opc.sdk.core.classes.ua.core;

public class BaseDataVariableType extends BaseVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.BaseDataVariableType;

	public BaseDataVariableType() {
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
		return "BaseDataVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
