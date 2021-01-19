package opc.sdk.core.classes.ua.core;

public class ProgramDiagnosticType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ProgramDiagnosticType;
	private PropertyType lastTransitionTime;
	private PropertyType lastMethodInputArguments;
	private PropertyType lastMethodCallTime;
	private PropertyType lastMethodCall;
	private PropertyType lastMethodSessionId;
	private PropertyType createClientName;
	private PropertyType lastMethodOutputArguments;
	private PropertyType invocationCreationTime;
	private PropertyType lastMethodReturnStatus;
	private PropertyType createSessionId;

	public ProgramDiagnosticType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.ProgramDiagnosticDataType getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.core.ProgramDiagnosticDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getLastTransitionTime() {
		return lastTransitionTime;
	}

	public void setLastTransitionTime(PropertyType value) {
		lastTransitionTime = value;
	}

	public PropertyType getLastMethodInputArguments() {
		return lastMethodInputArguments;
	}

	public void setLastMethodInputArguments(PropertyType value) {
		lastMethodInputArguments = value;
	}

	public PropertyType getLastMethodCallTime() {
		return lastMethodCallTime;
	}

	public void setLastMethodCallTime(PropertyType value) {
		lastMethodCallTime = value;
	}

	public PropertyType getLastMethodCall() {
		return lastMethodCall;
	}

	public void setLastMethodCall(PropertyType value) {
		lastMethodCall = value;
	}

	public PropertyType getLastMethodSessionId() {
		return lastMethodSessionId;
	}

	public void setLastMethodSessionId(PropertyType value) {
		lastMethodSessionId = value;
	}

	public PropertyType getCreateClientName() {
		return createClientName;
	}

	public void setCreateClientName(PropertyType value) {
		createClientName = value;
	}

	public PropertyType getLastMethodOutputArguments() {
		return lastMethodOutputArguments;
	}

	public void setLastMethodOutputArguments(PropertyType value) {
		lastMethodOutputArguments = value;
	}

	public PropertyType getInvocationCreationTime() {
		return invocationCreationTime;
	}

	public void setInvocationCreationTime(PropertyType value) {
		invocationCreationTime = value;
	}

	public PropertyType getLastMethodReturnStatus() {
		return lastMethodReturnStatus;
	}

	public void setLastMethodReturnStatus(PropertyType value) {
		lastMethodReturnStatus = value;
	}

	public PropertyType getCreateSessionId() {
		return createSessionId;
	}

	public void setCreateSessionId(PropertyType value) {
		createSessionId = value;
	}

	@Override
	public String toString() {
		return "ProgramDiagnosticType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
