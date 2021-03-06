package com.bichler.opc.driver.base.handler;

import com.bichler.opc.driver.base.BaseResourceManager;

public class AbstractBaseHandler {
	private BaseResourceManager manager = null;
	private long driverId;

	public AbstractBaseHandler() {
	}

	public long getDriverId() {
		return this.driverId;
	}

	public BaseResourceManager getManager() {
		return manager;
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public void setManager(BaseResourceManager manager) {
		this.manager = manager;
	}
}
