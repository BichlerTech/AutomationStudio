package com.bichler.astudio.view.drivermodel.browser.listener;

public interface IDriverModelDragConverter {


	/**
	 * Converts a driver model node for drag support.
	 * 
	 * @param driverNode
	 * @return text for drag support
	 */
	public String dragTextTransfer(Object driverNode);
}
