package opc.sdk.core.classes.ua.core;

public class RefreshRequiredEventType extends SystemEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.RefreshRequiredEventType;

	public RefreshRequiredEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "RefreshRequiredEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
