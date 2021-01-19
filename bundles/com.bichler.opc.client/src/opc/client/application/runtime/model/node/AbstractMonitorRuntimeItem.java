package opc.client.application.runtime.model.node;

import opc.client.application.runtime.model.ProjectsStoreConfiguration;

import org.jdom.Element;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public abstract class AbstractMonitorRuntimeItem extends AbstractRuntimeItem {
	private boolean discardOldest = false;
	private double interval = -1;
	private String mode = "";
	private String nodeClass = "";
	private long queueSize = -1;
	private AbstractSubscriptionNode parent = null;

	public AbstractMonitorRuntimeItem() {
		super();
	}

	@Override
	public void load(Element element) {
		super.load(element);
		String attrDiscardOldest = element.getAttributeValue(ProjectsStoreConfiguration.DISCARDOLDEST.name());
		setDiscardOldest(Boolean.parseBoolean(attrDiscardOldest));
		String attrInterval = element.getAttributeValue(ProjectsStoreConfiguration.INTERVAL.name());
		setInterval(Double.parseDouble(attrInterval));
		String attrMode = element.getAttributeValue(ProjectsStoreConfiguration.MODE.name());
		setMode(attrMode);
		String attrNodeClass = element.getAttributeValue(ProjectsStoreConfiguration.NODECLASS.name());
		setNodeClass(attrNodeClass);
		String attrQueueSize = element.getAttributeValue(ProjectsStoreConfiguration.QUEUESIZE.name());
		setQueueSize(Long.parseLong(attrQueueSize));
	}

	public boolean isDiscardOldest() {
		return discardOldest;
	}

	public double getInterval() {
		return interval;
	}

	public String getMode() {
		return mode;
	}

	public String getNodeClass() {
		return nodeClass;
	}

	public long getQueueSize() {
		return queueSize;
	}

	private void setDiscardOldest(boolean discardOldest) {
		this.discardOldest = discardOldest;
	}

	private void setInterval(double interval) {
		this.interval = interval;
	}

	private void setMode(String mode) {
		this.mode = mode;
	}

	private void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}

	private void setQueueSize(long queueSize) {
		this.queueSize = queueSize;
	}

	void setParent(AbstractSubscriptionNode parent) {
		this.parent = parent;
	}

	public AbstractSubscriptionNode getParent() {
		return this.parent;
	}

	public abstract UnsignedInteger getAttributeId();
}
