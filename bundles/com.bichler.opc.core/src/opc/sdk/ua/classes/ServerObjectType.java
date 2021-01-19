package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.classes.ServerCapabilitiesType;
import opc.sdk.ua.classes.ServerRedundancyType;
import opc.sdk.ua.classes.VendorServerInfoType;

import opc.sdk.ua.IOPCContext;

public class ServerObjectType extends BaseObjectType {
	protected PropertyVariableType<String[]> serverArray;
	protected PropertyVariableType<String[]> namespaceArray;
	protected ServerStatusType serverStatus;
	protected PropertyVariableType<Byte> serviceLevel;
	protected PropertyVariableType<Boolean> auditing;
	protected ServerCapabilitiesType serverCapabilities;
	protected ServerDiagnosticsType serverDiagnostics;
	protected VendorServerInfoType vendorServerInfo;
	protected ServerRedundancyType serverRedundancy;
	protected NamespacesType namespaces;
	protected BaseMethod getMonitoredItemsMethod;

	public ServerObjectType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.serverArray = new PropertyVariableType<>(this);
		this.namespaceArray = new PropertyVariableType<>(this);
		this.serverStatus = new ServerStatusType(this);
		this.serviceLevel = new PropertyVariableType<>(this);
		this.auditing = new PropertyVariableType<>(this);
		this.serverCapabilities = new ServerCapabilitiesType(this);
		this.serverDiagnostics = new ServerDiagnosticsType(this);
		this.vendorServerInfo = new VendorServerInfoType(this);
		this.serverRedundancy = new ServerRedundancyType(this);
		this.namespaces = new NamespacesType(this);
		this.getMonitoredItemsMethod = new BaseMethod(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.serverArray != null) {
			children.add(this.serverArray);
		}
		if (this.namespaceArray != null) {
			children.add(this.namespaceArray);
		}
		if (this.serverStatus != null) {
			children.add(this.serverStatus);
		}
		if (this.serviceLevel != null) {
			children.add(this.serviceLevel);
		}
		if (this.auditing != null) {
			children.add(this.auditing);
		}
		if (this.serverCapabilities != null) {
			children.add(this.serverCapabilities);
		}
		if (this.serverDiagnostics != null) {
			children.add(this.serverDiagnostics);
		}
		if (this.vendorServerInfo != null) {
			children.add(this.vendorServerInfo);
		}
		if (this.namespaces != null) {
			children.add(this.namespaces);
		}
		if (this.getMonitoredItemsMethod != null) {
			children.add(this.getMonitoredItemsMethod);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
