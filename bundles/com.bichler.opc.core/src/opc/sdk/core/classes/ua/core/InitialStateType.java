package opc.sdk.core.classes.ua.core;

public class InitialStateType extends StateType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.InitialStateType;

	public InitialStateType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "InitialStateType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
