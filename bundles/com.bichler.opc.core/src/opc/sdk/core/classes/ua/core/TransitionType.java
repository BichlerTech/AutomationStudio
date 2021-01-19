package opc.sdk.core.classes.ua.core;

public class TransitionType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.TransitionType;

	public TransitionType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "TransitionType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
