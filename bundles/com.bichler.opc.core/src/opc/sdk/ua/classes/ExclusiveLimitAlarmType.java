package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;

public class ExclusiveLimitAlarmType extends LimitAlarmType {
	protected ExclusiveLimitStateMachineType limitState = null;

	public ExclusiveLimitAlarmType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ExclusiveLimitAlarmType;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		if (BrowseNames.LIMITSTATE.equalsIgnoreCase(browseName.getName())) {
			if (createOrReplace) {
				if (replacement == null) {
					setLimitState(new ExclusiveLimitStateMachineType(this));
				} else {
					setLimitState((ExclusiveLimitStateMachineType) replacement);
				}
			}
			instance = this.limitState;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.limitState != null) {
			children.add(this.limitState);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public ExclusiveLimitStateMachineType getLimitState() {
		return limitState;
	}

	public void setLimitState(ExclusiveLimitStateMachineType limitState) {
		this.limitState = limitState;
	}
}
