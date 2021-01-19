package com.bichler.astudio.opcua.components.ui.dialogs;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.TimestampsToReturn;

public class OPCUaDataChangeParameters {

	private NodeId nodeId = null;
	private MonitoringParameters parameters = null;
	private String indexRange = null;
	private QualifiedName dataEncoding = null;
	private TimestampsToReturn timestampToReturn = null;
	private MonitoringMode monitoringMode = MonitoringMode.Disabled;

	public OPCUaDataChangeParameters() {
	}

	public MonitoringParameters getParameters() {
		return parameters;
	}

	public void setParameters(MonitoringParameters parameters) {
		this.parameters = parameters;
	}

	public String getIndexRange() {
		return indexRange;
	}

	public void setIndexRange(String indexRange) {
		this.indexRange = indexRange;
	}

	public QualifiedName getDataEncoding() {
		return dataEncoding;
	}

	public void setDataEncoding(QualifiedName dataEncoding) {
		this.dataEncoding = dataEncoding;
	}

	public void setTimestampToReturn(TimestampsToReturn timestampToReturn) {
		this.timestampToReturn = timestampToReturn;
	}

	public TimestampsToReturn getTimestampToReturn() {
		return this.timestampToReturn;
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public MonitoringMode getMonitoringMode() {
		return monitoringMode;
	}

	public void setMonitoringMode(MonitoringMode monitoringMode) {
		this.monitoringMode = monitoringMode;
	}

}
