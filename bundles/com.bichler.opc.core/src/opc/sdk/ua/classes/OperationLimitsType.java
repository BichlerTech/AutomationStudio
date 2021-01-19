package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class OperationLimitsType extends BaseObjectType {
	protected PropertyVariableType<UnsignedInteger> maxNodesPerRead;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerHistoryReadData;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerHistoryReadEvents;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerWrite;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerHistoryUpdateData;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerHistoryUpdateEvents;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerMethodCall;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerBrowse;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerRegisterNodes;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerTranslateBrowsePathsToNodeIds;
	protected PropertyVariableType<UnsignedInteger> maxNodesPerNodeManagement;
	protected PropertyVariableType<UnsignedInteger> maxMonitoredItemsPerCall;

	public OperationLimitsType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.maxNodesPerRead = new PropertyVariableType<>(this);
		this.maxNodesPerHistoryReadData = new PropertyVariableType<>(this);
		this.maxNodesPerHistoryReadEvents = new PropertyVariableType<>(this);
		this.maxNodesPerWrite = new PropertyVariableType<>(this);
		this.maxNodesPerHistoryUpdateData = new PropertyVariableType<>(this);
		this.maxNodesPerHistoryUpdateEvents = new PropertyVariableType<>(this);
		this.maxNodesPerMethodCall = new PropertyVariableType<>(this);
		this.maxNodesPerBrowse = new PropertyVariableType<>(this);
		this.maxNodesPerRegisterNodes = new PropertyVariableType<>(this);
		this.maxNodesPerTranslateBrowsePathsToNodeIds = new PropertyVariableType<>(this);
		this.maxNodesPerNodeManagement = new PropertyVariableType<>(this);
		this.maxMonitoredItemsPerCall = new PropertyVariableType<>(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerCapabilitiesType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.maxNodesPerRead != null) {
			children.add(this.maxNodesPerRead);
		}
		if (this.maxNodesPerHistoryReadData != null) {
			children.add(this.maxNodesPerHistoryReadData);
		}
		if (this.maxNodesPerHistoryReadEvents != null) {
			children.add(this.maxNodesPerHistoryReadEvents);
		}
		if (this.maxNodesPerWrite != null) {
			children.add(this.maxNodesPerWrite);
		}
		if (this.maxNodesPerHistoryUpdateData != null) {
			children.add(this.maxNodesPerHistoryUpdateData);
		}
		if (this.maxNodesPerHistoryUpdateEvents != null) {
			children.add(this.maxNodesPerHistoryUpdateEvents);
		}
		if (this.maxNodesPerMethodCall != null) {
			children.add(this.maxNodesPerMethodCall);
		}
		if (this.maxNodesPerBrowse != null) {
			children.add(this.maxNodesPerBrowse);
		}
		if (this.maxNodesPerRegisterNodes != null) {
			children.add(this.maxNodesPerRegisterNodes);
		}
		if (this.maxNodesPerTranslateBrowsePathsToNodeIds != null) {
			children.add(this.maxNodesPerTranslateBrowsePathsToNodeIds);
		}
		if (this.maxNodesPerNodeManagement != null) {
			children.add(this.maxNodesPerNodeManagement);
		}
		if (this.maxMonitoredItemsPerCall != null) {
			children.add(this.maxMonitoredItemsPerCall);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
