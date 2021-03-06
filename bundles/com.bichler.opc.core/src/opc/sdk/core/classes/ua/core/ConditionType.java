package opc.sdk.core.classes.ua.core;

public class ConditionType extends BaseEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ConditionType;
	private PropertyType branchId;
	private opc.sdk.core.classes.ua.base.BaseMethodGen conditionRefresh;
	private PropertyType retain;
	private TwoStateVariableType enabledState;
	private ConditionVariableType lastSeverity;
	private PropertyType conditionClassId;
	private PropertyType conditionName;
	private opc.sdk.core.classes.ua.base.BaseMethodGen addComment;
	private PropertyType clientUserId;
	private opc.sdk.core.classes.ua.base.BaseMethodGen enable;
	private opc.sdk.core.classes.ua.base.BaseMethodGen disable;
	private ConditionVariableType quality;
	private ConditionVariableType comment;
	private PropertyType conditionClassName;

	public ConditionType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getBranchId() {
		return branchId;
	}

	public void setBranchId(PropertyType value) {
		branchId = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getConditionRefresh() {
		return conditionRefresh;
	}

	public void setConditionRefresh(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		conditionRefresh = value;
	}

	public PropertyType getRetain() {
		return retain;
	}

	public void setRetain(PropertyType value) {
		retain = value;
	}

	public TwoStateVariableType getEnabledState() {
		return enabledState;
	}

	public void setEnabledState(TwoStateVariableType value) {
		enabledState = value;
	}

	public ConditionVariableType getLastSeverity() {
		return lastSeverity;
	}

	public void setLastSeverity(ConditionVariableType value) {
		lastSeverity = value;
	}

	public PropertyType getConditionClassId() {
		return conditionClassId;
	}

	public void setConditionClassId(PropertyType value) {
		conditionClassId = value;
	}

	public PropertyType getConditionName() {
		return conditionName;
	}

	public void setConditionName(PropertyType value) {
		conditionName = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getAddComment() {
		return addComment;
	}

	public void setAddComment(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		addComment = value;
	}

	public PropertyType getClientUserId() {
		return clientUserId;
	}

	public void setClientUserId(PropertyType value) {
		clientUserId = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getEnable() {
		return enable;
	}

	public void setEnable(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		enable = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getDisable() {
		return disable;
	}

	public void setDisable(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		disable = value;
	}

	public ConditionVariableType getQuality() {
		return quality;
	}

	public void setQuality(ConditionVariableType value) {
		quality = value;
	}

	public ConditionVariableType getComment() {
		return comment;
	}

	public void setComment(ConditionVariableType value) {
		comment = value;
	}

	public PropertyType getConditionClassName() {
		return conditionClassName;
	}

	public void setConditionClassName(PropertyType value) {
		conditionClassName = value;
	}

	@Override
	public String toString() {
		return "ConditionType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
