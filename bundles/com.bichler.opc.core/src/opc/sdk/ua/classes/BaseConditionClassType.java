package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class BaseConditionClassType extends BaseObjectType {
	public BaseConditionClassType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initialize(IOPCContext context) {
		super.initialize(context);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.BaseConditionClassType;
	}
}
