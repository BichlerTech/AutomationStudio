package opc.sdk.core.classes.ua.core;

public class ServerStatusType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ServerStatusType;
	private BaseDataVariableType startTime;
	private BaseDataVariableType state;
	private BaseDataVariableType currentTime;
	private BaseDataVariableType secondsTillShutdown;
	private BuildInfoType buildInfo;
	private BaseDataVariableType shutdownReason;

	public ServerStatusType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.ServerStatusDataType getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.core.ServerStatusDataType) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public BaseDataVariableType getStartTime() {
		return startTime;
	}

	public void setStartTime(BaseDataVariableType value) {
		startTime = value;
	}

	public BaseDataVariableType getState() {
		return state;
	}

	public void setState(BaseDataVariableType value) {
		state = value;
	}

	public BaseDataVariableType getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(BaseDataVariableType value) {
		currentTime = value;
	}

	public BaseDataVariableType getSecondsTillShutdown() {
		return secondsTillShutdown;
	}

	public void setSecondsTillShutdown(BaseDataVariableType value) {
		secondsTillShutdown = value;
	}

	public BuildInfoType getBuildInfo() {
		return buildInfo;
	}

	public void setBuildInfo(BuildInfoType value) {
		buildInfo = value;
	}

	public BaseDataVariableType getShutdownReason() {
		return shutdownReason;
	}

	public void setShutdownReason(BaseDataVariableType value) {
		shutdownReason = value;
	}

	@Override
	public String toString() {
		return "ServerStatusType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
