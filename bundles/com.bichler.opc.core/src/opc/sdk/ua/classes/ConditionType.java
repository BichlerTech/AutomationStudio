package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class ConditionType extends BaseEventType {
	protected PropertyVariableType<NodeId> conditionClassId = null;
	protected PropertyVariableType<LocalizedText> conditionClassName = null;
	protected PropertyVariableType<String> conditionName = null;
	protected PropertyVariableType<NodeId> branchId = null;
	protected PropertyVariableType<Boolean> retain = null;
	protected TwoStateVariableType enabledState = null;
	protected ConditionVariableType<StatusCode> quality = null;
	protected ConditionVariableType<UnsignedShort> lastSeverity = null;
	protected ConditionVariableType<LocalizedText> comment = null;
	protected PropertyVariableType<String> clientUserId = null;
	protected BaseMethod enableMethod = null;
	protected BaseMethod disableMethod = null;
	protected BaseMethod addCommentMethod = null;

	public ConditionType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ConditionType;
	}

	private PropertyVariableType<NodeId> createConditionClassId(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.conditionClassId == null) {
			if (replacement == null) {
				setConditionClassId(new PropertyVariableType<NodeId>(this));
			} else {
				setConditionClassId((PropertyVariableType<NodeId>) replacement);
			}
		}
		return this.conditionClassId;
	}

	private PropertyVariableType<LocalizedText> createConditionClassName(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.conditionClassName == null) {
			if (replacement == null) {
				setConditionClassName(new PropertyVariableType<LocalizedText>(this));
			} else {
				setConditionClassName((PropertyVariableType<LocalizedText>) replacement);
			}
		}
		return this.conditionClassName;
	}

	private PropertyVariableType<String> createConditionName(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.conditionName == null) {
			if (replacement == null) {
				setConditionName(new PropertyVariableType<String>(this));
			} else {
				setConditionName((PropertyVariableType<String>) replacement);
			}
		}
		return this.conditionName;
	}

	private PropertyVariableType<NodeId> createBranchId(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.branchId == null) {
			if (replacement == null) {
				setBranchId(new PropertyVariableType<NodeId>(this));
			} else {
				setBranchId((PropertyVariableType<NodeId>) replacement);
			}
		}
		return this.branchId;
	}

	private PropertyVariableType<Boolean> createRetain(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.retain == null) {
			if (replacement == null) {
				setRetain(new PropertyVariableType<Boolean>(this));
			} else {
				setRetain((PropertyVariableType<Boolean>) replacement);
			}
		}
		return this.retain;
	}

	private TwoStateVariableType createEnableState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.enabledState == null) {
			if (replacement == null) {
				setEnabledState(new TwoStateVariableType(this));
			} else {
				setEnabledState((TwoStateVariableType) replacement);
			}
		}
		return this.enabledState;
	}

	private ConditionVariableType<StatusCode> createQuality(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.quality == null) {
			if (replacement == null) {
				setQuality(new ConditionVariableType<StatusCode>(this));
			} else {
				setQuality((ConditionVariableType<StatusCode>) replacement);
			}
		}
		return this.quality;
	}

	private ConditionVariableType<UnsignedShort> createLastSeverity(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.lastSeverity == null) {
			if (replacement == null) {
				setLastSeverity(new ConditionVariableType<UnsignedShort>(this));
			} else {
				setLastSeverity((ConditionVariableType<UnsignedShort>) replacement);
			}
		}
		return this.lastSeverity;
	}

	private ConditionVariableType<LocalizedText> createComment(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.comment == null) {
			if (replacement == null) {
				setComment(new ConditionVariableType<LocalizedText>(this));
			} else {
				setComment((ConditionVariableType<LocalizedText>) replacement);
			}
		}
		return this.comment;
	}

	private PropertyVariableType<String> createClientUserId(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.clientUserId == null) {
			if (replacement == null) {
				setClientUserId(new PropertyVariableType<String>(this));
			} else {
				setClientUserId((PropertyVariableType<String>) replacement);
			}
		}
		return this.clientUserId;
	}

	private BaseMethod createEnable(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.enableMethod == null) {
			if (replacement == null) {
				setEnable(new BaseMethod(this));
			} else {
				setEnable((BaseMethod) replacement);
			}
		}
		return this.enableMethod;
	}

	private BaseMethod createDisable(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.disableMethod == null) {
			if (replacement == null) {
				setDisable(new BaseMethod(this));
			} else {
				setDisable((BaseMethod) replacement);
			}
		}
		return this.disableMethod;
	}

	private BaseMethod createAddComment(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.addCommentMethod == null) {
			if (replacement == null) {
				setAddCommentMethod(new BaseMethod(this));
			} else {
				setAddCommentMethod((BaseMethod) replacement);
			}
		}
		return this.addCommentMethod;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.CONDITIONCLASSID:
			instance = createConditionClassId(createOrReplace, replacement);
			break;
		case BrowseNames.CONDITIONCLASSNAME:
			instance = createConditionClassName(createOrReplace, replacement);
			break;
		case BrowseNames.CONDITIONNAME:
			instance = createConditionName(createOrReplace, replacement);
			break;
		case BrowseNames.BRANCHID:
			instance = createBranchId(createOrReplace, replacement);
			break;
		case BrowseNames.RETAIN:
			instance = createRetain(createOrReplace, replacement);
			break;
		case BrowseNames.ENABLESTATE:
			instance = createEnableState(createOrReplace, replacement);
			break;
		case BrowseNames.QUALITY:
			instance = createQuality(createOrReplace, replacement);
			break;
		case BrowseNames.LASTSEVERITY:
			instance = createLastSeverity(createOrReplace, replacement);
			break;
		case BrowseNames.COMMENT:
			instance = createComment(createOrReplace, replacement);
			break;
		case BrowseNames.CLIENTUSERID:
			instance = createClientUserId(createOrReplace, replacement);
			break;
		case BrowseNames.ENABLE:
			instance = createEnable(createOrReplace, replacement);
			break;
		case BrowseNames.DISABLE:
			instance = createDisable(createOrReplace, replacement);
			break;
		case BrowseNames.ADDCOMMENT:
			instance = createAddComment(createOrReplace, replacement);
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
		if (this.conditionClassId != null) {
			children.add(this.conditionClassId);
		}
		if (this.conditionClassName != null) {
			children.add(this.conditionClassName);
		}
		if (this.conditionName != null) {
			children.add(this.conditionName);
		}
		if (this.branchId != null) {
			children.add(this.branchId);
		}
		if (this.retain != null) {
			children.add(this.retain);
		}
		if (this.enabledState != null) {
			children.add(this.enabledState);
		}
		if (this.quality != null) {
			children.add(this.quality);
		}
		if (this.lastSeverity != null) {
			children.add(this.lastSeverity);
		}
		if (this.comment != null) {
			children.add(this.comment);
		}
		if (this.clientUserId != null) {
			children.add(this.clientUserId);
		}
		if (this.enableMethod != null) {
			children.add(this.enableMethod);
		}
		if (this.disableMethod != null) {
			children.add(this.disableMethod);
		}
		if (this.addCommentMethod != null) {
			children.add(this.addCommentMethod);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public PropertyVariableType<NodeId> getConditionClassId() {
		return conditionClassId;
	}

	public PropertyVariableType<LocalizedText> getConditionClassName() {
		return conditionClassName;
	}

	public PropertyVariableType<String> getConditionName() {
		return conditionName;
	}

	public PropertyVariableType<NodeId> getBranchId() {
		return branchId;
	}

	public PropertyVariableType<Boolean> getRetain() {
		return retain;
	}

	public TwoStateVariableType getEnabledState() {
		return enabledState;
	}

	public ConditionVariableType<StatusCode> getQuality() {
		return quality;
	}

	public ConditionVariableType<UnsignedShort> getLastSeverity() {
		return lastSeverity;
	}

	public ConditionVariableType<LocalizedText> getComment() {
		return comment;
	}

	public PropertyVariableType<String> getClientUserId() {
		return clientUserId;
	}

	public BaseMethod getEnableMethod() {
		return enableMethod;
	}

	public BaseMethod getDisableMethod() {
		return disableMethod;
	}

	public BaseMethod getAddCommentMethod() {
		return addCommentMethod;
	}

	public void setConditionClassId(PropertyVariableType<NodeId> conditionClassId) {
		if (this.conditionClassId != conditionClassId) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.conditionClassId = conditionClassId;
	}

	public void setConditionClassName(PropertyVariableType<LocalizedText> conditionClassName) {
		if (this.conditionClassName != conditionClassName) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.conditionClassName = conditionClassName;
	}

	public void setConditionName(PropertyVariableType<String> conditionName) {
		if (this.conditionName != conditionName) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.conditionName = conditionName;
	}

	public void setBranchId(PropertyVariableType<NodeId> branchId) {
		if (this.branchId != branchId) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.branchId = branchId;
	}

	public void setRetain(PropertyVariableType<Boolean> retain) {
		if (this.retain != retain) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.retain = retain;
	}

	public void setEnabledState(TwoStateVariableType enabledState) {
		if (this.enabledState != enabledState) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.enabledState = enabledState;
	}

	public void setQuality(ConditionVariableType<StatusCode> quality) {
		if (this.quality != quality) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.quality = quality;
	}

	public void setLastSeverity(ConditionVariableType<UnsignedShort> lastSeverity) {
		if (this.lastSeverity != lastSeverity) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.lastSeverity = lastSeverity;
	}

	public void setComment(ConditionVariableType<LocalizedText> comment) {
		if (this.comment != comment) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.comment = comment;
	}

	public void setClientUserId(PropertyVariableType<String> clientUserId) {
		if (this.clientUserId != clientUserId) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.clientUserId = clientUserId;
	}

	public void setEnable(BaseMethod enableMethod) {
		if (this.enableMethod != enableMethod) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.enableMethod = enableMethod;
	}

	public void setDisable(BaseMethod disableMethod) {
		if (this.disableMethod != disableMethod) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.disableMethod = disableMethod;
	}

	public void setAddCommentMethod(BaseMethod addCommentMethod) {
		if (this.addCommentMethod != addCommentMethod) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.addCommentMethod = addCommentMethod;
	}
}
