package com.bichler.astudio.editor.siemens.driver;

import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelDragConverter;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserConstants;

public class SiemensDriverDragSupport implements IDriverModelDragConverter {
	@Override
	public String dragTextTransfer(Object driverNode) {
		AbstractSiemensNode node = (AbstractSiemensNode) driverNode;
		return DriverBrowserConstants.MARKER_DRAGNDROP + convertAttributesToString(node);
	}

	public static String[] convertTextToAttributes(String data) {
		return data.split("%d%");
	}

	public static String convertAttributesToString(AbstractSiemensNode node) {
		String symbolname = node.getSymbolName();
		String datatype = node.getDataType();
		String address = node.getAddress();
		float index = node.getAddressIndex();
		boolean active = node.isActive();
		return symbolname + "%d%" + datatype + "%d%" + address + "%d%" + index + "%d%" + active;
	}

	public static void setAttributesToNode(SiemensEntryModelNode dp, String[] attributes) {
		dp.setSymbolName(attributes[0]);
		dp.setDataType(attributes[1]);
		dp.setAddress(attributes[2]);
		dp.setIndex(Float.parseFloat(attributes[3]));
		dp.setActive(Boolean.parseBoolean(attributes[4]));
	}
}
