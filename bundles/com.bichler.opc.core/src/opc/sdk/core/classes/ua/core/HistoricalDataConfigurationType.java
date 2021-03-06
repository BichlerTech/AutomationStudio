package opc.sdk.core.classes.ua.core;

public class HistoricalDataConfigurationType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.HistoricalDataConfigurationType;
	private PropertyType stepped;
	private PropertyType exceptionDeviation;
	private PropertyType maxTimeInterval;
	private AggregateConfigurationType aggregateConfiguration;
	private PropertyType minTimeInterval;
	private PropertyType exceptionDeviationFormat;
	private PropertyType definition;

	public HistoricalDataConfigurationType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getStepped() {
		return stepped;
	}

	public void setStepped(PropertyType value) {
		stepped = value;
	}

	public PropertyType getExceptionDeviation() {
		return exceptionDeviation;
	}

	public void setExceptionDeviation(PropertyType value) {
		exceptionDeviation = value;
	}

	public PropertyType getMaxTimeInterval() {
		return maxTimeInterval;
	}

	public void setMaxTimeInterval(PropertyType value) {
		maxTimeInterval = value;
	}

	public AggregateConfigurationType getAggregateConfiguration() {
		return aggregateConfiguration;
	}

	public void setAggregateConfiguration(AggregateConfigurationType value) {
		aggregateConfiguration = value;
	}

	public PropertyType getMinTimeInterval() {
		return minTimeInterval;
	}

	public void setMinTimeInterval(PropertyType value) {
		minTimeInterval = value;
	}

	public PropertyType getExceptionDeviationFormat() {
		return exceptionDeviationFormat;
	}

	public void setExceptionDeviationFormat(PropertyType value) {
		exceptionDeviationFormat = value;
	}

	public PropertyType getDefinition() {
		return definition;
	}

	public void setDefinition(PropertyType value) {
		definition = value;
	}

	@Override
	public String toString() {
		return "HistoricalDataConfigurationType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
