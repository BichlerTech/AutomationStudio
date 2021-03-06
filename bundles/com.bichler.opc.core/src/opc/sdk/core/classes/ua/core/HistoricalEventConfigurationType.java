package opc.sdk.core.classes.ua.core;

public class HistoricalEventConfigurationType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.HistoricalEventConfigurationType;

	public HistoricalEventConfigurationType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "HistoricalEventConfigurationType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
