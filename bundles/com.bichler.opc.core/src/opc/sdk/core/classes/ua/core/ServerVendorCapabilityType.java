package opc.sdk.core.classes.ua.core;

public class ServerVendorCapabilityType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ServerVendorCapabilityType;

	public ServerVendorCapabilityType() {
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
		return "ServerVendorCapabilityType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
