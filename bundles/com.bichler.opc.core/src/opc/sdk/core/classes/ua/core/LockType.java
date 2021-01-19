package opc.sdk.core.classes.ua.core;

public class LockType extends BaseObjectType {
	public static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.LockType;
	private opc.sdk.core.classes.ua.base.BaseMethodGen lock;
	private opc.sdk.core.classes.ua.base.BaseMethodGen unlock;

	public LockType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getLock() {
		return lock;
	}

	public void setLock(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		lock = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getUnlock() {
		return unlock;
	}

	public void setUnlock(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		unlock = value;
	}

	@Override
	public String toString() {
		return "LockType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
