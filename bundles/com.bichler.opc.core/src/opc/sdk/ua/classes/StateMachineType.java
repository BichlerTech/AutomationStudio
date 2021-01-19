package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class StateMachineType extends BaseObjectType {
	protected StateVariableType currentState = null;
	protected TransitionVariableType lastTransition = null;

	public StateMachineType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.currentState = new StateVariableType(this);
		this.lastTransition = new TransitionVariableType(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.StateMachineType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.currentState != null) {
			children.add(this.currentState);
		}
		if (this.lastTransition != null) {
			children.add(this.lastTransition);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
