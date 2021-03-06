package opc.sdk.core.classes.ua.core;

public class SubscriptionDiagnosticsArrayType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SubscriptionDiagnosticsArrayType;

	public SubscriptionDiagnosticsArrayType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.SubscriptionDiagnosticsDataType getValue() {
		return getVariant() != null
				? (org.opcfoundation.ua.core.SubscriptionDiagnosticsDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "SubscriptionDiagnosticsArrayType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
