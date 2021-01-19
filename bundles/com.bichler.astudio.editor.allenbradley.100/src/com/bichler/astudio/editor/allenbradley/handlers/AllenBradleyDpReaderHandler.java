package com.bichler.astudio.editor.allenbradley.handlers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.xml.sax.InputSource;

import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPEditorImporter;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPItem;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPReaderHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class AllenBradleyDpReaderHandler extends AbstractOPCUADPReaderHandler
{
  public static final String ID = "com.bichler.astudio.drv.import.allenbradley";

  @Override
  protected void insertDatapoint(Map<NodeId, Object> datapoints, Object dp)
  {
    NodeId nodeId = ((AllenBradleyEntryModelNode) dp).getNodeId();
    if (NodeId.isNull(nodeId))
    {
      return;
    }
    datapoints.put(nodeId, dp);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected List readDatapoints(InputStream input)
  {
    AllenBradleyDPEditorImporter importer = new AllenBradleyDPEditorImporter();
    try
    {
      List<AllenBradleyDPItem> dps = importer.loadDPs(new InputSource(new InputStreamReader(input, "UTF-8")),
          ServerInstance.getInstance().getServerInstance().getNamespaceUris());
      List<AllenBradleyEntryModelNode> nodes = new ArrayList<>();
      for (AllenBradleyDPItem dp : dps)
      {
        AllenBradleyEntryModelNode rdp = AllenBradleyEntryModelNode.loadFromDP(dp);
        nodes.add(rdp);
      }
      return nodes;
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }
}
