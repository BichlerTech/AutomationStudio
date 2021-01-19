package opc.sdk.core.classes.ua.core;

public class SessionDiagnosticsArrayType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SessionDiagnosticsArrayType;

	public SessionDiagnosticsArrayType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.SessionDiagnosticsDataType getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.core.SessionDiagnosticsDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "SessionDiagnosticsArrayType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
