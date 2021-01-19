package opc.sdk.core.classes.ua.core;

public class SessionSecurityDiagnosticsArrayType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SessionSecurityDiagnosticsArrayType;

	public SessionSecurityDiagnosticsArrayType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.SessionSecurityDiagnosticsDataType getValue() {
		return getVariant() != null
				? (org.opcfoundation.ua.core.SessionSecurityDiagnosticsDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "SessionSecurityDiagnosticsArrayType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
