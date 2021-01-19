package com.bichler.astudio.editor.xml_da.handlers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.xml.sax.InputSource;

import com.bichler.astudio.editor.xml_da.xml.XMLDADPEditorImporter;
import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;
import com.bichler.astudio.editor.xml_da.xml.XML_DA_DPItem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPReaderHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class XML_DA_DpReaderHandler extends AbstractOPCUADPReaderHandler
{
  public static final String ID = "com.hbsoft.comet.drv.import.xmlda";

  @Override
  protected void insertDatapoint(Map<NodeId, Object> datapoints, Object dp)
  {
    NodeId nodeId = ((XMLDAModelNode) dp).getNId();
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
    XMLDADPEditorImporter importer = new XMLDADPEditorImporter();
    try
    {
      List<XML_DA_DPItem> dps = importer.loadDPs(new InputSource(new InputStreamReader(input, "UTF-8")),
          ServerInstance.getInstance().getServerInstance().getNamespaceUris());
      List<XMLDAModelNode> nodes = new ArrayList<>();
      for (XML_DA_DPItem dp : dps)
      {
        XMLDAModelNode rdp = XMLDAModelNode.loadFromDP(dp);
        nodes.add(rdp);
      }
      return nodes;
    }
    catch (UnsupportedEncodingException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
    }
    return new ArrayList<>();
  }
}
