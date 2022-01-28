package com.bichler.astudio.editor.allenbradley.driver;

import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelDragConverter;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserConstants;

public class AllenBradleyDriverDragSupport implements IDriverModelDragConverter
{
  @Override
  public String dragTextTransfer(Object driverNode)
  {
    AbstractAllenBradleyNode node = (AbstractAllenBradleyNode) driverNode;
    String data = convertAttributesToString(node);
    return DriverBrowserConstants.MARKER_DRAGNDROP + data;
  }

  public static String[] convertTextToAttributes(String data)
  {
    return data.split("%d%");
  }

  public static String convertAttributesToString(AbstractAllenBradleyNode node)
  {
    String name = node.getName();
    String type = node.getDataType();
    boolean active = node.isActive();
    return name + "%d%" + type + "%d%" + active;
  }

  public static void setAttributesToNode(AllenBradleyEntryModelNode dp, String[] attributes)
  {
    dp.setSymbolName(attributes[0]);
    dp.setDataType(attributes[1]);
    dp.setActive(Boolean.parseBoolean(attributes[2]));
  }
}
