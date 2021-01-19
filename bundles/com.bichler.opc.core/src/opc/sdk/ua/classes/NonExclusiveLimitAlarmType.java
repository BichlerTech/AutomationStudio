package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;

public class NonExclusiveLimitAlarmType extends LimitAlarmType {
	protected TwoStateVariableType highHighState = null;
	protected TwoStateVariableType highState = null;
	protected TwoStateVariableType lowState = null;
	protected TwoStateVariableType lowLowState = null;

	public NonExclusiveLimitAlarmType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.NonExclusiveLimitAlarmType;
	}

	private TwoStateVariableType createHighHighState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.highHighState == null) {
			if (replacement == null) {
				setHighHighState(new TwoStateVariableType(this));
			} else {
				setHighHighState((TwoStateVariableType) replacement);
			}
		}
		return this.highHighState;
	}

	private TwoStateVariableType createHighState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.highState == null) {
			if (replacement == null) {
				setHighState(new TwoStateVariableType(this));
			} else {
				setHighState((TwoStateVariableType) replacement);
			}
		}
		return this.highState;
	}

	private TwoStateVariableType createLowState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.lowState == null) {
			if (replacement == null) {
				setLowState(new TwoStateVariableType(this));
			} else {
				setLowState((TwoStateVariableType) replacement);
			}
		}
		return this.lowState;
	}

	private TwoStateVariableType createLowLowState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.lowLowState == null) {
			if (replacement == null) {
				setLowLowState(new TwoStateVariableType(this));
			} else {
				setLowLowState((TwoStateVariableType) replacement);
			}
		}
		return this.lowLowState;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.HIGHHIGHSTATE:
			instance = createHighHighState(createOrReplace, replacement);
			break;
		case BrowseNames.HIGHSTATE:
			instance = createHighState(createOrReplace, replacement);
			break;
		case BrowseNames.LOWSTATE:
			instance = createLowState(createOrReplace, replacement);
			break;
		case BrowseNames.LOWLOWSTATE:
			instance = createLowLowState(createOrReplace, replacement);
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
		if (this.highHighState != null) {
			children.add(this.highHighState);
		}
		if (this.highState != null) {
			children.add(this.highState);
		}
		if (this.lowState != null) {
			children.add(this.lowState);
		}
		if (this.lowLowState != null) {
			children.add(this.lowLowState);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public TwoStateVariableType getHighHighState() {
		return highHighState;
	}

	public TwoStateVariableType getHighState() {
		return highState;
	}

	public TwoStateVariableType getLowState() {
		return lowState;
	}

	public TwoStateVariableType getLowLowState() {
		return lowLowState;
	}

	public void setHighHighState(TwoStateVariableType highHighState) {
		this.highHighState = highHighState;
	}

	public void setHighState(TwoStateVariableType highState) {
		this.highState = highState;
	}

	public void setLowState(TwoStateVariableType lowState) {
		this.lowState = lowState;
	}

	public void setLowLowState(TwoStateVariableType lowLowState) {
		this.lowLowState = lowLowState;
	}
}
