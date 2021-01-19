package com.bichler.astudio.editor.events.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.events.xml.EVENTENTRYTYPE;
import com.bichler.astudio.editor.events.xml.EventEntryModelNode;
import com.bichler.astudio.editor.events.xml.EventsExporter;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.hbsoft.driver.opc.events.dp.EventDpItemModel;
import com.hbsoft.driver.opc.events.model.EventsImporter;

public class OPCUAEventsUpdateNamespaceTableHandler extends AbstractOPCUAUpdateNamespaceHandler
{
  public static final String ID = "command.update.opcua.namespacetable.driver.events";

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
      EventsImporter importer = new EventsImporter();
      List<EventDpItemModel> dps = importer.loadDPs(input, trigger.getOriginNamespaceTable());
      List<IDriverNode> entries = new ArrayList<>();
      for (EventDpItemModel dp : dps)
      {
        EventEntryModelNode entry = EventEntryModelNode.loadFromDP(dp, EVENTENTRYTYPE.DATAPOINT);
        entries.add(entry);
      }
      EventsExporter exporter = new EventsExporter(updateable.getFilesystem(), datapointsPath);
      exporter.updateDatapoints(entries, trigger);
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
