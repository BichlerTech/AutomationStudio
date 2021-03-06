package opc.sdk.core.classes.ua.core;

public class AnalogItemType extends DataItemType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AnalogItemType;
	private PropertyType instrumentRange;
	private PropertyType eURange;
	private PropertyType engineeringUnits;

	public AnalogItemType() {
		super();
	}

	@Override
	public java.lang.Object getValue() {
		return getVariant() != null ? getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getInstrumentRange() {
		return instrumentRange;
	}

	public void setInstrumentRange(PropertyType value) {
		instrumentRange = value;
	}

	public PropertyType getEURange() {
		return eURange;
	}

	public void setEURange(PropertyType value) {
		eURange = value;
	}

	public PropertyType getEngineeringUnits() {
		return engineeringUnits;
	}

	public void setEngineeringUnits(PropertyType value) {
		engineeringUnits = value;
	}

	@Override
	public String toString() {
		return "AnalogItemType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
