package opc.sdk.core.classes.ua.core;

public class ServerCapabilitiesType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ServerCapabilitiesType;
	private PropertyType maxBrowseContinuationPoints;
	private PropertyType maxQueryContinuationPoints;
	private FolderType modellingRules;
	private PropertyType localeIdArray;
	private PropertyType serverProfileArray;
	private PropertyType minSupportedSampleRate;
	private AggregateFunctionsType aggregateFunctions;
	private PropertyType softwareCertificates;
	private PropertyType maxHistoryContinuationPoints;

	public ServerCapabilitiesType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getMaxBrowseContinuationPoints() {
		return maxBrowseContinuationPoints;
	}

	public void setMaxBrowseContinuationPoints(PropertyType value) {
		maxBrowseContinuationPoints = value;
	}

	public PropertyType getMaxQueryContinuationPoints() {
		return maxQueryContinuationPoints;
	}

	public void setMaxQueryContinuationPoints(PropertyType value) {
		maxQueryContinuationPoints = value;
	}

	public FolderType getModellingRules() {
		return modellingRules;
	}

	public void setModellingRules(FolderType value) {
		modellingRules = value;
	}

	public PropertyType getLocaleIdArray() {
		return localeIdArray;
	}

	public void setLocaleIdArray(PropertyType value) {
		localeIdArray = value;
	}

	public PropertyType getServerProfileArray() {
		return serverProfileArray;
	}

	public void setServerProfileArray(PropertyType value) {
		serverProfileArray = value;
	}

	public PropertyType getMinSupportedSampleRate() {
		return minSupportedSampleRate;
	}

	public void setMinSupportedSampleRate(PropertyType value) {
		minSupportedSampleRate = value;
	}

	public AggregateFunctionsType getAggregateFunctions() {
		return aggregateFunctions;
	}

	public void setAggregateFunctions(AggregateFunctionsType value) {
		aggregateFunctions = value;
	}

	public PropertyType getSoftwareCertificates() {
		return softwareCertificates;
	}

	public void setSoftwareCertificates(PropertyType value) {
		softwareCertificates = value;
	}

	public PropertyType getMaxHistoryContinuationPoints() {
		return maxHistoryContinuationPoints;
	}

	public void setMaxHistoryContinuationPoints(PropertyType value) {
		maxHistoryContinuationPoints = value;
	}

	@Override
	public String toString() {
		return "ServerCapabilitiesType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
