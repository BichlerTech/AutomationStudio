package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class AcknowledgeableConditionType extends ConditionType {
	protected TwoStateVariableType ackedState = null;
	protected TwoStateVariableType confirmedState = null;
	protected BaseMethod acknowledgeMethod = null;
	protected BaseMethod confirmMethod = null;

	public AcknowledgeableConditionType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.AcknowledgeableConditionType;
	}

	private BaseInstance setAckedState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.ackedState == null) {
			if (replacement == null) {
				setAckedState(new TwoStateVariableType(this));
			} else {
				setAckedState((TwoStateVariableType) replacement);
			}
		}
		return this.ackedState;
	}

	private BaseInstance setConfirmedState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.confirmedState == null) {
			if (replacement == null) {
				setConfirmedState(new TwoStateVariableType(this));
			} else {
				setConfirmedState((TwoStateVariableType) replacement);
			}
		}
		return this.confirmedState;
	}

	private BaseInstance setAcknowledge(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.acknowledgeMethod == null) {
			if (replacement == null) {
				setAcknowledge(new BaseMethod(this));
			} else {
				setAcknowledge((BaseMethod) replacement);
			}
		}
		return this.acknowledgeMethod;
	}

	private BaseInstance setConfirm(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.confirmMethod == null) {
			if (replacement == null) {
				setConfirm(new BaseMethod(this));
			} else {
				setConfirm((BaseMethod) replacement);
			}
		}
		return this.confirmMethod;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.ACKEDSTATE:
			instance = setAckedState(createOrReplace, replacement);
			break;
		case BrowseNames.CONFIRMEDSTATE:
			instance = setConfirmedState(createOrReplace, replacement);
			break;
		case BrowseNames.ACKNOWLEDGE:
			instance = setAcknowledge(createOrReplace, replacement);
			break;
		case BrowseNames.CONFIRM:
			instance = setConfirm(createOrReplace, replacement);
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
		if (this.ackedState != null) {
			children.add(this.ackedState);
		}
		if (this.confirmedState != null) {
			children.add(this.confirmedState);
		}
		if (this.acknowledgeMethod != null) {
			children.add(this.acknowledgeMethod);
		}
		if (this.confirmMethod != null) {
			children.add(this.confirmMethod);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public TwoStateVariableType getAckedState() {
		return ackedState;
	}

	public TwoStateVariableType getConfirmedState() {
		return confirmedState;
	}

	public BaseMethod getAcknowledgeMethod() {
		return acknowledgeMethod;
	}

	public BaseMethod getConfirmMethod() {
		return confirmMethod;
	}

	public void setAckedState(TwoStateVariableType ackedState) {
		if (this.ackedState != ackedState) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.ackedState = ackedState;
	}

	public void setConfirmedState(TwoStateVariableType confirmedState) {
		if (this.confirmedState != confirmedState) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.confirmedState = confirmedState;
	}

	public void setAcknowledge(BaseMethod acknowledged) {
		if (this.acknowledgeMethod != acknowledged) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.acknowledgeMethod = acknowledged;
	}

	public void setConfirm(BaseMethod confirm) {
		if (this.confirmMethod != confirm) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.confirmMethod = confirm;
	}
}
