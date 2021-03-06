package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class TwoStateVariableType extends StateVariableType {
	protected PropertyVariableType<DateTime> transitionTime = null;
	protected PropertyVariableType<DateTime> effectiveTransitionTime = null;
	protected PropertyVariableType<LocalizedText> trueState = null;
	protected PropertyVariableType<LocalizedText> falseState = null;

	public TwoStateVariableType(BaseNode parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.StateVariableState#getDefaultTypeDefinitionId(org.
	 * opcfoundation.ua.common.NamespaceTable)
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.TwoStateVariableType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.StateVariableState#getDefaultDataTypeId(org.
	 * opcfoundation .ua.common.NamespaceTable)
	 */
	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.LocalizedText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.StateVariableState#getDefaultValueRank()
	 */
	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.Scalar.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opc.sdk.ua.classes.NodeState#findChild(opc.sdk.core.context.ISystemContext ,
	 * org.opcfoundation.ua.builtintypes.QualifiedName, boolean,
	 * opc.sdk.ua.classes.BaseInstanceState)
	 */
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.TRANSITIONTIME:
			if (createOrReplace) {
				if (replacement == null) {
					setTransitionTime(new PropertyVariableType<DateTime>(this));
				} else {
					setTransitionTime((PropertyVariableType<DateTime>) replacement);
				}
			}
			instance = this.transitionTime;
			break;
		case BrowseNames.EFFECTIVETRANSITIONTIME:
			if (createOrReplace) {
				if (replacement == null) {
					setEffectiveTransitionTime(new PropertyVariableType<DateTime>(this));
				} else {
					setEffectiveTransitionTime((PropertyVariableType<DateTime>) replacement);
				}
			}
			instance = this.effectiveTransitionTime;
			break;
		case BrowseNames.TRUESTATE:
			if (createOrReplace) {
				if (replacement == null) {
					setTrueState(new PropertyVariableType<LocalizedText>(this));
				} else {
					setTrueState((PropertyVariableType<LocalizedText>) replacement);
				}
			}
			instance = this.trueState;
			break;
		case BrowseNames.FALSESTATE:
			if (createOrReplace) {
				if (replacement == null) {
					setFalseState(new PropertyVariableType<LocalizedText>(this));
				} else {
					setFalseState((PropertyVariableType<LocalizedText>) replacement);
				}
			}
			instance = this.falseState;
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.NodeState#getChildren()
	 */
	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.transitionTime != null) {
			children.add(this.transitionTime);
		}
		if (this.effectiveTransitionTime != null) {
			children.add(this.effectiveTransitionTime);
		}
		if (this.trueState != null) {
			children.add(this.trueState);
		}
		if (this.falseState != null) {
			children.add(this.falseState);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public PropertyVariableType<DateTime> getTransitionTime() {
		return transitionTime;
	}

	public PropertyVariableType<DateTime> getEffectiveTransitionTime() {
		return effectiveTransitionTime;
	}

	public PropertyVariableType<LocalizedText> getTrueState() {
		return trueState;
	}

	public PropertyVariableType<LocalizedText> getFalseState() {
		return falseState;
	}

	public void setTransitionTime(PropertyVariableType<DateTime> transitionTime) {
		if (this.transitionTime != transitionTime) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.transitionTime = transitionTime;
	}

	public void setEffectiveTransitionTime(PropertyVariableType<DateTime> effectiveTransitionTime) {
		if (this.effectiveTransitionTime != effectiveTransitionTime) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.effectiveTransitionTime = effectiveTransitionTime;
	}

	public void setTrueState(PropertyVariableType<LocalizedText> trueState) {
		if (this.trueState != trueState) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.trueState = trueState;
	}

	public void setFalseState(PropertyVariableType<LocalizedText> falseState) {
		if (this.falseState != falseState) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.falseState = falseState;
	}
}
