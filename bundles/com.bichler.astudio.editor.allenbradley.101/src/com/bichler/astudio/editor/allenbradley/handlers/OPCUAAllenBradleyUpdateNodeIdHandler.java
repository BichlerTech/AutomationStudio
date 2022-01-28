package com.bichler.astudio.editor.allenbradley.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.xml.sax.InputSource;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPEditorImporter;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPItem;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDpExporter;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNodeIdHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUAAllenBradleyUpdateNodeIdHandler extends AbstractOPCUAUpdateNodeIdHandler
{
  public static final String ID = "command.update.opcua.nodeid.driver.allenbradley";

  @Override
  public void onUpdateDatapoints(ASUpdateable updateable, String path)
  {
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
      InputSource isrc = new InputSource(new InputStreamReader(input, "UTF-8"));
      AllenBradleyDPEditorImporter importer = new AllenBradleyDPEditorImporter();
      List<AllenBradleyDPItem> dps = importer.loadDPs(isrc,
          ServerInstance.getInstance().getServerInstance().getNamespaceUris());
      List<IDriverNode> entries = new ArrayList<>();
      for (AllenBradleyDPItem dp : dps)
      {
        AllenBradleyEntryModelNode entry = AllenBradleyEntryModelNode.loadFromDP(dp);
        entries.add(entry);
      }
      AllenBradleyDpExporter exporter = new AllenBradleyDpExporter(updateable.getFilesystem(), datapointsPath);
      exporter.updateDatapoints(entries, trigger, ServerInstance.getInstance().getServerInstance().getNamespaceUris()
          );
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
