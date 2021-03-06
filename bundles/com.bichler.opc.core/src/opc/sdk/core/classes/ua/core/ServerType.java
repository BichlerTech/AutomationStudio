package opc.sdk.core.classes.ua.core;

public class ServerType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ServerType;
	private PropertyType serverArray;
	private VendorServerInfoType vendorServerInfo;
	private PropertyType namespaceArray;
	private ServerCapabilitiesType serverCapabilities;
	private ServerDiagnosticsType serverDiagnostics;
	private PropertyType auditing;
	private ServerRedundancyType serverRedundancy;
	private ServerStatusType serverStatus;
	private PropertyType serviceLevel;

	public ServerType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getServerArray() {
		return serverArray;
	}

	public void setServerArray(PropertyType value) {
		serverArray = value;
	}

	public VendorServerInfoType getVendorServerInfo() {
		return vendorServerInfo;
	}

	public void setVendorServerInfo(VendorServerInfoType value) {
		vendorServerInfo = value;
	}

	public PropertyType getNamespaceArray() {
		return namespaceArray;
	}

	public void setNamespaceArray(PropertyType value) {
		namespaceArray = value;
	}

	public ServerCapabilitiesType getServerCapabilities() {
		return serverCapabilities;
	}

	public void setServerCapabilities(ServerCapabilitiesType value) {
		serverCapabilities = value;
	}

	public ServerDiagnosticsType getServerDiagnostics() {
		return serverDiagnostics;
	}

	public void setServerDiagnostics(ServerDiagnosticsType value) {
		serverDiagnostics = value;
	}

	public PropertyType getAuditing() {
		return auditing;
	}

	public void setAuditing(PropertyType value) {
		auditing = value;
	}

	public ServerRedundancyType getServerRedundancy() {
		return serverRedundancy;
	}

	public void setServerRedundancy(ServerRedundancyType value) {
		serverRedundancy = value;
	}

	public ServerStatusType getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(ServerStatusType value) {
		serverStatus = value;
	}

	public PropertyType getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(PropertyType value) {
		serviceLevel = value;
	}

	@Override
	public String toString() {
		return "ServerType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
