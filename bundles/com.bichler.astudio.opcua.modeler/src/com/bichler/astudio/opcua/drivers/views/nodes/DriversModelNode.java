package com.bichler.astudio.opcua.drivers.views.nodes;

import org.osgi.framework.Bundle;

public class DriversModelNode {
	private Bundle bundle = null;
	private int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
}
