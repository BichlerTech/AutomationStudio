package opc.sdk.core.classes.ua.core;

public class ProgramTransitionEventType extends TransitionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ProgramTransitionEventType;
	private PropertyType intermediateResult;

	public ProgramTransitionEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getIntermediateResult() {
		return intermediateResult;
	}

	public void setIntermediateResult(PropertyType value) {
		intermediateResult = value;
	}

	@Override
	public String toString() {
		return "ProgramTransitionEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
