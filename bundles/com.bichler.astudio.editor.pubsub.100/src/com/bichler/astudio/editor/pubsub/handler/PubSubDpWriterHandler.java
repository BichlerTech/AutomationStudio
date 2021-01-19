package com.bichler.astudio.editor.pubsub.handler;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;
import com.bichler.astudio.editor.pubsub.xml.PubSubExporter;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class PubSubDpWriterHandler extends AbstractOPCUADPWriterHandler
{
  public static final String ID = "com.bichler.astudio.drv.export.pubsub";

  @Override
  protected void write(IFileSystem filesys, String path, List<Object> datapoints)
  {
    PubSubExporter exporter = new PubSubExporter(filesys, path);
    List<IDriverNode> dps = new ArrayList<>();
    for (Object event : datapoints)
    {
//      PubSubEntryModelNode item = PubSubEntryModelNode.loadFromDP((EventDpItemModel) event, PUBSUBENTRYTYPE.CONNECTION);
//      dps.add(item);
    }
    for (IDriverNode datapoint : dps)
    {
//      EventDpItemModel datapointsItem = (EventDpItemModel) ((PubSubEntryModelNode) datapoint).getItem();
//      for (AbstractEventModel eventsItem : datapointsItem.getChildren())
//      {
////        PubSubEntryModelNode item = PubSubEntryModelNode.loadFromDP(eventsItem, PUBSUBENTRYTYPE.EVENT);
////        ((PubSubEntryModelNode) datapoint).addChild(item);
////        fillExpression(item);
//      }
    }
    StringBuffer builder = exporter.build(dps, ServerInstance.getInstance().getServerInstance().getNamespaceUris());
    exporter.write(filesys, path, builder);
  }

  private void fillExpression(PubSubEntryModelNode item)
  {
//    for (AbstractEventModel expressionItem : item.getItem().getChildren())
//    {
////      PubSubEntryModelNode expression = PubSubEntryModelNode.loadFromDP(expressionItem, PUBSUBENTRYTYPE.EXPRESSION);
////      item.addChild(expression);
////      fillExpression(expression);
//    }
  }
}
