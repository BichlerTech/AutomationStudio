package opc.client.application.runtime.model.service;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NodeClass;

public class MonitoredItemCreate extends AbstractRuntimeService {
	private NodeId nodeId;
	private double interval;
	private MonitoringMode monitoringMode;
	private NodeClass nodeClass;
	private UnsignedInteger queueSize;
	private UnsignedInteger attributeId;

	public MonitoredItemCreate() {
		// constructor to generate a default monitored item create object
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public double getInterval() {
		return interval;
	}

	public MonitoringMode getMonitoringMode() {
		return monitoringMode;
	}

	public NodeClass getNodeClass() {
		return nodeClass;
	}

	public UnsignedInteger getQueueSize() {
		return queueSize;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = NodeId.parseNodeId(nodeId);
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public void setMonitoringMode(String monitoringMode) {
		this.monitoringMode = MonitoringMode.valueOf(monitoringMode);
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = NodeClass.valueOf(nodeClass);
	}

	public void setQueueSize(long queueSize) {
		this.queueSize = new UnsignedInteger(queueSize);
	}

	public UnsignedInteger getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(UnsignedInteger attributeId) {
		this.attributeId = attributeId;
	}
}
