package opc.client.application.runtime.model.node;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;

public abstract class AbstractSubscriptionNode extends AbstractIdentifiedNode {
	private boolean enable = false;
	private int keepalive = -1;
	private int lifetime = -1;
	private double interval = -1;
	private List<AbstractMonitorRuntimeItem> children = new ArrayList<>();

	public AbstractSubscriptionNode() {
		super();
	}

	public void addChild(AbstractMonitorRuntimeItem node) {
		this.children.add(node);
		node.setParent(this);
	}

	public List<AbstractMonitorRuntimeItem> getChildren() {
		return this.children;
	}

	@Override
	public void load(Element element) {
		super.load(element);
		String attrEnable = element.getAttributeValue(ProjectsStoreConfiguration.ENABLE.name());
		setEnable(Boolean.parseBoolean(attrEnable));
		String attrKeepalive = element.getAttributeValue(ProjectsStoreConfiguration.KEEPALIVE.name());
		setKeepalive(Integer.parseInt(attrKeepalive));
		String attrLifetime = element.getAttributeValue(ProjectsStoreConfiguration.LIFETIME.name());
		setLifetime(Integer.parseInt(attrLifetime));
		String attrInterval = element.getAttributeValue(ProjectsStoreConfiguration.INTERVAL.name());
		setInterval(Double.parseDouble(attrInterval));
	}

	public boolean isEnable() {
		return enable;
	}

	public int getKeepalive() {
		return keepalive;
	}

	public int getLifetime() {
		return lifetime;
	}

	public double getInterval() {
		return interval;
	}

	private void setEnable(boolean enable) {
		this.enable = enable;
	}

	private void setKeepalive(int keepalive) {
		this.keepalive = keepalive;
	}

	private void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	private void setInterval(double interval) {
		this.interval = interval;
	}
}
