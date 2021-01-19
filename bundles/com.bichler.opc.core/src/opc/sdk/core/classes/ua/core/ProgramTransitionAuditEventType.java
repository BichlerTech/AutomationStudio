package opc.sdk.core.classes.ua.core;

public class ProgramTransitionAuditEventType extends AuditUpdateStateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ProgramTransitionAuditEventType;
	private FiniteTransitionVariableType transition;

	public ProgramTransitionAuditEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public FiniteTransitionVariableType getTransition() {
		return transition;
	}

	public void setTransition(FiniteTransitionVariableType value) {
		transition = value;
	}

	@Override
	public String toString() {
		return "ProgramTransitionAuditEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
