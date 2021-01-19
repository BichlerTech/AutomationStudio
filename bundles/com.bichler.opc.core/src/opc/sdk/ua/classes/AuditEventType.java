package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DateTime;

import opc.sdk.ua.IOPCContext;

public class AuditEventType extends BaseEventType {
	protected PropertyVariableType<DateTime> actionTimeStamp;
	protected PropertyVariableType<Boolean> status;
	protected PropertyVariableType<String> serverId;
	protected PropertyVariableType<String> clientAuditEntryId;
	protected PropertyVariableType<String> clientUserId;

	public AuditEventType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initialize(IOPCContext context) {
		super.initialize(context);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.actionTimeStamp = new PropertyVariableType<>(this);
		this.status = new PropertyVariableType<>(this);
		this.serverId = new PropertyVariableType<>(this);
		this.clientAuditEntryId = new PropertyVariableType<>(this);
		this.clientUserId = new PropertyVariableType<>(this);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<BaseInstance>();
		if (this.actionTimeStamp != null) {
			children.add(this.actionTimeStamp);
		}
		if (this.status != null) {
			children.add(this.status);
		}
		if (this.serverId != null) {
			children.add(this.serverId);
		}
		if (this.clientAuditEntryId != null) {
			children.add(this.clientAuditEntryId);
		}
		if (this.clientUserId != null) {
			children.add(this.clientUserId);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
