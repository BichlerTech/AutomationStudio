package opc.sdk.core.classes.ua.core;

public class MaintenanceConditionClassType extends BaseConditionClassType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.MaintenanceConditionClassType;

	public MaintenanceConditionClassType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "MaintenanceConditionClassType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
