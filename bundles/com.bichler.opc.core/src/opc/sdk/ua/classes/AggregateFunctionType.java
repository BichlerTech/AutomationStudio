package opc.sdk.ua.classes;

import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class AggregateFunctionType extends BaseObjectType {
	public AggregateFunctionType(BaseNode parent) {
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
		return Identifiers.AggregateFunctionType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		return super.getChildren();
	}
}
