package opc.sdk.core.classes.ua.core;

public class ProcessConditionClassType extends BaseConditionClassType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ProcessConditionClassType;

	public ProcessConditionClassType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "ProcessConditionClassType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
