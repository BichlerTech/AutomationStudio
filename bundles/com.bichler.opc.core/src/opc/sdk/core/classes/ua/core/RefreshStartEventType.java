package opc.sdk.core.classes.ua.core;

public class RefreshStartEventType extends SystemEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.RefreshStartEventType;

	public RefreshStartEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "RefreshStartEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
