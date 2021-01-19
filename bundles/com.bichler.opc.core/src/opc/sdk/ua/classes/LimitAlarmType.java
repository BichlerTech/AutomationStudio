package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;

public class LimitAlarmType extends AlarmConditionType {
	protected PropertyVariableType<Double> highHighLimit = null;
	protected PropertyVariableType<Double> highLimit = null;
	protected PropertyVariableType<Double> lowLimit = null;
	protected PropertyVariableType<Double> lowLowLimit = null;

	public LimitAlarmType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.LimitAlarmType;
	}

	private PropertyVariableType<Double> createHighHighLimit(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.highHighLimit == null) {
			if (replacement == null) {
				setHighHighLimit(new PropertyVariableType<Double>(this));
			} else {
				setHighHighLimit((PropertyVariableType<Double>) replacement);
			}
		}
		return this.highHighLimit;
	}

	private PropertyVariableType<Double> createHighLimit(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.highLimit == null) {
			if (replacement == null) {
				setHighLimit(new PropertyVariableType<Double>(this));
			} else {
				setHighLimit((PropertyVariableType<Double>) replacement);
			}
		}
		return this.highLimit;
	}

	private PropertyVariableType<Double> createLowLimit(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.lowLimit == null) {
			if (replacement == null) {
				setLowLimit(new PropertyVariableType<Double>(this));
			} else {
				setLowLimit((PropertyVariableType<Double>) replacement);
			}
		}
		return this.lowLimit;
	}

	private PropertyVariableType<Double> createLowLowLimit(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.lowLowLimit == null) {
			if (replacement == null) {
				setLowLowLimit(new PropertyVariableType<Double>(this));
			} else {
				setLowLowLimit((PropertyVariableType<Double>) replacement);
			}
		}
		return this.lowLowLimit;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.HIGHHIGHLIMIT:
			instance = createHighHighLimit(createOrReplace, replacement);
			break;
		case BrowseNames.HIGHLIMIT:
			instance = createHighLimit(createOrReplace, replacement);
			break;
		case BrowseNames.LOWLIMIT:
			instance = createLowLimit(createOrReplace, replacement);
			break;
		case BrowseNames.LOWLOWLIMIT:
			instance = createLowLowLimit(createOrReplace, replacement);
			break;
		default:
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.highHighLimit != null) {
			children.add(this.highHighLimit);
		}
		if (this.highLimit != null) {
			children.add(this.highLimit);
		}
		if (this.lowLimit != null) {
			children.add(this.lowLimit);
		}
		if (this.lowLowLimit != null) {
			children.add(this.lowLowLimit);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public PropertyVariableType<Double> getHighHighLimit() {
		return highHighLimit;
	}

	public PropertyVariableType<Double> getHighLimit() {
		return highLimit;
	}

	public PropertyVariableType<Double> getLowLimit() {
		return lowLimit;
	}

	public PropertyVariableType<Double> getLowLowLimit() {
		return lowLowLimit;
	}

	public void setHighHighLimit(PropertyVariableType<Double> highHighLimit) {
		this.highHighLimit = highHighLimit;
	}

	public void setHighLimit(PropertyVariableType<Double> highLimit) {
		this.highLimit = highLimit;
	}

	public void setLowLimit(PropertyVariableType<Double> lowLimit) {
		this.lowLimit = lowLimit;
	}

	public void setLowLowLimit(PropertyVariableType<Double> lowLowLimit) {
		this.lowLowLimit = lowLowLimit;
	}
}
