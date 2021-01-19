package opc.sdk.core.classes.ua.core;

public class RefreshEndEventType extends SystemEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.RefreshEndEventType;

	public RefreshEndEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "RefreshEndEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
