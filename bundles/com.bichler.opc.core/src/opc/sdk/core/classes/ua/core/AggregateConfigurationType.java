package opc.sdk.core.classes.ua.core;

public class AggregateConfigurationType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AggregateConfigurationType;
	private PropertyType percentDataGood;
	private PropertyType useSlopedExtrapolation;
	private PropertyType percentDataBad;
	private PropertyType treatUncertainAsBad;

	public AggregateConfigurationType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getPercentDataGood() {
		return percentDataGood;
	}

	public void setPercentDataGood(PropertyType value) {
		percentDataGood = value;
	}

	public PropertyType getUseSlopedExtrapolation() {
		return useSlopedExtrapolation;
	}

	public void setUseSlopedExtrapolation(PropertyType value) {
		useSlopedExtrapolation = value;
	}

	public PropertyType getPercentDataBad() {
		return percentDataBad;
	}

	public void setPercentDataBad(PropertyType value) {
		percentDataBad = value;
	}

	public PropertyType getTreatUncertainAsBad() {
		return treatUncertainAsBad;
	}

	public void setTreatUncertainAsBad(PropertyType value) {
		treatUncertainAsBad = value;
	}

	@Override
	public String toString() {
		return "AggregateConfigurationType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
