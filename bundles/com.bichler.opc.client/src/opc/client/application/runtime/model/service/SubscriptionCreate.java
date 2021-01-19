package opc.client.application.runtime.model.service;

public class SubscriptionCreate extends AbstractRuntimeService {
	private boolean enable;
	private double interval;
	private int keepalive;
	private int lifetime;

	public SubscriptionCreate() {
		// constructor to generate a default subscription create object
	}

	public boolean isEnable() {
		return enable;
	}

	public double getInterval() {
		return interval;
	}

	public int getKeepalive() {
		return keepalive;
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public void setKeepalive(int keepalive) {
		this.keepalive = keepalive;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
}
