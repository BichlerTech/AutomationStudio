package com.bichler.astudio.editor.events.handler;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.events.xml.EVENTENTRYTYPE;
import com.bichler.astudio.editor.events.xml.EventEntryModelNode;
import com.bichler.astudio.editor.events.xml.EventsExporter;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.hbsoft.driver.opc.events.dp.AbstractEventModel;
import com.hbsoft.driver.opc.events.dp.EventDpItemModel;

public class EventsDpWriterHandler extends AbstractOPCUADPWriterHandler
{
  public static final String ID = "com.bichler.astudio.drv.export.events";

  @Override
  protected void write(IFileSystem filesys, String path, List<Object> datapoints)
  {
    EventsExporter exporter = new EventsExporter(filesys, path);
    List<IDriverNode> dps = new ArrayList<>();
    for (Object event : datapoints)
    {
      EventEntryModelNode item = EventEntryModelNode.loadFromDP((EventDpItemModel) event, EVENTENTRYTYPE.DATAPOINT);
      dps.add(item);
    }
    for (IDriverNode datapoint : dps)
    {
      EventDpItemModel datapointsItem = (EventDpItemModel) ((EventEntryModelNode) datapoint).getItem();
      for (AbstractEventModel eventsItem : datapointsItem.getChildren())
      {
        EventEntryModelNode item = EventEntryModelNode.loadFromDP(eventsItem, EVENTENTRYTYPE.EVENT);
        ((EventEntryModelNode) datapoint).addChild(item);
        fillExpression(item);
      }
    }
    StringBuffer builder = exporter.build(dps, ServerInstance.getInstance().getServerInstance().getNamespaceUris());
    exporter.write(filesys, path, builder);
  }

  private void fillExpression(EventEntryModelNode item)
  {
    for (AbstractEventModel expressionItem : item.getItem().getChildren())
    {
      EventEntryModelNode expression = EventEntryModelNode.loadFromDP(expressionItem, EVENTENTRYTYPE.EXPRESSION);
      item.addChild(expression);
      fillExpression(expression);
    }
  }
}
