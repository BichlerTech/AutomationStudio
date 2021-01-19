package com.bichler.astudio.editor.pubsub.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;
import com.bichler.astudio.editor.pubsub.xml.PubSubExporter;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNodeIdHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUAPubSubUpdateNodeIdHandler extends AbstractOPCUAUpdateNodeIdHandler
{
  public static final String ID = "command.update.opcua.nodeid.driver.pubsub";

  @Override
  public void onUpdateDatapoints(ASUpdateable updateable, String path)
  {
    // datapoints
    OPCUAUpdateNodeIdEvent trigger = (OPCUAUpdateNodeIdEvent) updateable.getTrigger();
    InputStream input = null;
    try
    {
      String datapointsPath = new Path(path).append("datapoints.com").toOSString();
      if (!updateable.getFilesystem().isFile(datapointsPath))
      {
        return;
      }
      input = updateable.getFilesystem().readFile(datapointsPath);
      // InputSource isrc = new InputSource(new InputStreamReader(input,
      // "UTF-8"));
//      EventsImporter importer = new EventsImporter();
//      List<EventDpItemModel> dps = importer.loadDPs(input,
//          ServerInstance.getInstance().getServerInstance().getNamespaceUris());
//      List<IDriverNode> entries = new ArrayList<>();
//      for (EventDpItemModel dp : dps)
//      {
//        PubSubEntryModelNode entry = PubSubEntryModelNode.loadFromDP(dp, PUBSUBENTRYTYPE.CONNECTION);
//        entries.add(entry);
//      }
      PubSubExporter exporter = new PubSubExporter(updateable.getFilesystem(), datapointsPath);
//      exporter.updateDatapoints(entries, trigger, ServerInstance.getInstance().getServerInstance().getNamespaceUris());
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}
