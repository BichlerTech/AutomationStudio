package opc.sdk.core.classes.ua.core;

public class HistoryServerCapabilitiesType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.HistoryServerCapabilitiesType;
	private PropertyType insertDataCapability;
	private PropertyType accessHistoryDataCapability;
	private PropertyType updateDataCapability;
	private AggregateFunctionsType aggregateFunctions;
	private PropertyType accessHistoryEventsCapability;
	private PropertyType deleteAtTimeCapability;
	private PropertyType maxReturnValues;
	private PropertyType replaceDataCapability;
	private PropertyType deleteRawCapability;

	public HistoryServerCapabilitiesType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getInsertDataCapability() {
		return insertDataCapability;
	}

	public void setInsertDataCapability(PropertyType value) {
		insertDataCapability = value;
	}

	public PropertyType getAccessHistoryDataCapability() {
		return accessHistoryDataCapability;
	}

	public void setAccessHistoryDataCapability(PropertyType value) {
		accessHistoryDataCapability = value;
	}

	public PropertyType getUpdateDataCapability() {
		return updateDataCapability;
	}

	public void setUpdateDataCapability(PropertyType value) {
		updateDataCapability = value;
	}

	public AggregateFunctionsType getAggregateFunctions() {
		return aggregateFunctions;
	}

	public void setAggregateFunctions(AggregateFunctionsType value) {
		aggregateFunctions = value;
	}

	public PropertyType getAccessHistoryEventsCapability() {
		return accessHistoryEventsCapability;
	}

	public void setAccessHistoryEventsCapability(PropertyType value) {
		accessHistoryEventsCapability = value;
	}

	public PropertyType getDeleteAtTimeCapability() {
		return deleteAtTimeCapability;
	}

	public void setDeleteAtTimeCapability(PropertyType value) {
		deleteAtTimeCapability = value;
	}

	public PropertyType getMaxReturnValues() {
		return maxReturnValues;
	}

	public void setMaxReturnValues(PropertyType value) {
		maxReturnValues = value;
	}

	public PropertyType getReplaceDataCapability() {
		return replaceDataCapability;
	}

	public void setReplaceDataCapability(PropertyType value) {
		replaceDataCapability = value;
	}

	public PropertyType getDeleteRawCapability() {
		return deleteRawCapability;
	}

	public void setDeleteRawCapability(PropertyType value) {
		deleteRawCapability = value;
	}

	@Override
	public String toString() {
		return "HistoryServerCapabilitiesType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
