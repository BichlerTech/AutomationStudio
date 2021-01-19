package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ServerState;
import org.opcfoundation.ua.core.ServerStatusDataType;

import opc.sdk.ua.IOPCContext;

public class ServerStatusType extends BaseDataVariableType<ServerStatusDataType> {
	protected BaseDataVariableType<DateTime> startTime;
	protected BaseDataVariableType<DateTime> currentTime;
	protected BaseDataVariableType<ServerState> state;
	protected BuildInfoType buildInfo;
	protected BaseDataVariableType<UnsignedInteger> secondsTillShutdown;
	protected BaseDataVariableType<LocalizedText> shutdownReason;

	public ServerStatusType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.startTime = new BaseDataVariableType<>(this);
		this.currentTime = new BaseDataVariableType<>(this);
		this.state = new BaseDataVariableType<>(this);
		this.buildInfo = new BuildInfoType(this);
		this.secondsTillShutdown = new BaseDataVariableType<>(this);
		this.shutdownReason = new BaseDataVariableType<>(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerStatusType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.startTime != null) {
			children.add(this.startTime);
		}
		if (this.currentTime != null) {
			children.add(this.currentTime);
		}
		if (this.state != null) {
			children.add(this.state);
		}
		if (this.buildInfo != null) {
			children.add(this.buildInfo);
		}
		if (this.secondsTillShutdown != null) {
			children.add(this.secondsTillShutdown);
		}
		if (this.shutdownReason != null) {
			children.add(this.shutdownReason);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
