package opc.sdk.core.classes.ua.core;

public class BaseVariableType extends opc.sdk.core.classes.ua.base.BaseVariableTypeGen {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.BaseVariableType;

	public BaseVariableType() {
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
		return "BaseVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
