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
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;


public class OPCUAPubSubUpdateNamespaceTableHandler extends AbstractOPCUAUpdateNamespaceHandler
{
  public static final String ID = "command.update.opcua.namespacetable.driver.pubsub";

  @Override
  public void onUpdateDatapoints(ASUpdateable updateable, String path)
  {
    // datapoints
    NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) updateable.getTrigger();
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
//      PubSubImporter importer = new EventsImporter();
//      List<EventDpItemModel> dps = importer.loadDPs(input, trigger.getOriginNamespaceTable());
//      List<IDriverNode> entries = new ArrayList<>();
//      for (EventDpItemModel dp : dps)
//      {
//        PubSubEntryModelNode entry = PubSubEntryModelNode.loadFromDP(dp, PUBSUBENTRYTYPE.CONNECTION);
//        entries.add(entry);
//      }
      PubSubExporter exporter = new PubSubExporter(updateable.getFilesystem(), datapointsPath);
//      exporter.updateDatapoints(entries, trigger);
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
