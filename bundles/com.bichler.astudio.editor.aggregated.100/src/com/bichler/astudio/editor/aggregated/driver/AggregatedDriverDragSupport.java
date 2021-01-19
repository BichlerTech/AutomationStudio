package com.bichler.astudio.editor.aggregated.driver;

import com.bichler.astudio.editor.aggregated.clientbrowser.model.AbstractCCModel;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelDragConverter;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserConstants;

public class AggregatedDriverDragSupport implements IDriverModelDragConverter{

	@Override
	public String dragTextTransfer(Object driverNode) {
		
		AbstractCCModel node = (AbstractCCModel) driverNode;
		
		return DriverBrowserConstants.MARKER_DRAGNDROP+node.getNodeId().toString();
	}

}
