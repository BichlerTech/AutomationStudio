package com.bichler.astudio.editor.pubsub.dnd;

import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelDragConverter;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserConstants;

public class PubSubDriverDragSupport implements IDriverModelDragConverter
{
  @Override
  public String dragTextTransfer(Object driverNode)
  {
//    EventDpItemModel node = (EventDpItemModel) driverNode;
//    return DriverBrowserConstants.MARKER_DRAGNDROP + convertAttributesToString(node);
	  return "";
  }

  public static String[] convertTextToAttributes(String data)
  {
    return data.split("%d%");
  }

  public static String convertAttributesToString(/*EventDpItemModel node*/)
  {
    // String symbolname = node.getSymbolName();
    // String datatype = node.getDataType();
    // String address = node.getAddress();
    // float index = node.getAddressIndex();
    // boolean active = node.isActive();
    // return symbolname + "%d%" + datatype + "%d%" + address + "%d%" +
    // index
    // + "%d%" + active;
    return "";
  }

  public static void setAttributesToNode(/*EventDpItemModel dp,*/ String[] attributes)
  {
    // dp.setSymbolName(attributes[0]);
    // dp.setDataType(attributes[1]);
    // dp.setAddress(attributes[2]);
    // dp.setIndex(Float.parseFloat(attributes[3]));
    // dp.setActive(Boolean.parseBoolean(attributes[4]));
  }
}
