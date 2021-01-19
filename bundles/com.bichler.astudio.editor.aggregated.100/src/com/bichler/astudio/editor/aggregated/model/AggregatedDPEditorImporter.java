package com.bichler.astudio.editor.aggregated.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.editor.aggregated.dp.AggregatedDPItem;

public class AggregatedDPEditorImporter
{
  public AggregatedDPEditorImporter()
  {
  }

  public List<AggregatedDpModelNode> loadDPs(InputStream stream, NamespaceTable uris)
  {
    List<AggregatedDpModelNode> dps = new ArrayList<AggregatedDpModelNode>();
    if (stream != null)
    {
      BufferedReader reader;
      try
      {
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line = "";
        while ((line = reader.readLine()) != null)
        {
          // ignore comments
          if (line.startsWith("#"))
          {
            continue;
          }
          String[] items = line.split("\t");
          if (items != null && items.length >= 2)
          {
            // now try to wrap the ns to ns index
            String[] ns = items[0].split(";");
            NodeId id = NodeId.NULL;
            if (ns != null && ns.length >= 2)
            {
              int index = uris.getIndex(ns[0]);
              if (index >= 0)
              {
                id = NodeId.parseNodeId("ns=" + index + ";" + ns[1]);
              }
            }
            try
            {
              AggregatedDPItem dp = new AggregatedDPItem();
              dp.setServerNodeId(id);
              if (!NodeId.isNull(id))
              {
                dp.setServerBrowsePath(items[1]);
              }
              if (items.length >= 8)
              {
                dp.setTargetDisplayName(items[2]);
                dp.setLoadedTargetNodeId(items[3]);
                dp.setTargetBrowsePath(items[4]);
                dp.setActive(Boolean.parseBoolean(items[5]));
                dp.setRead(Boolean.parseBoolean(items[6]));
                dp.setWrite(Boolean.parseBoolean(items[7]));
              }
              AggregatedDpModelNode item = new AggregatedDpModelNode(dp);
              dps.add(item);
            }
            catch (NumberFormatException ex)
            {
              ex.printStackTrace();
            }
          }
        }
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return dps;
  }
}
