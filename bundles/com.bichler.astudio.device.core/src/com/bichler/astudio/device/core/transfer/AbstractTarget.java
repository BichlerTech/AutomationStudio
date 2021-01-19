package com.bichler.astudio.device.core.transfer;

import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public abstract class AbstractTarget implements ITarget{

	protected String execution = null;
	
	public AbstractTarget(String execution) {
		if(execution == null) {
			throw new IllegalArgumentException(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
			        "com.bichler.astudio.device.core.error.notargetexecution"));
		}
		this.execution = execution;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getExecutionString() {
		return this.execution;
	}
}
