package opc.sdk.core.classes.ua.core;

public class ModellingRuleType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ModellingRuleType;
	private PropertyType namingRule;

	public ModellingRuleType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getNamingRule() {
		return namingRule;
	}

	public void setNamingRule(PropertyType value) {
		namingRule = value;
	}

	@Override
	public String toString() {
		return "ModellingRuleType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
