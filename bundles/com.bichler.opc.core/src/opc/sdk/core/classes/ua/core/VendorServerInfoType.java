package opc.sdk.core.classes.ua.core;

public class VendorServerInfoType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.VendorServerInfoType;

	public VendorServerInfoType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "VendorServerInfoType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
