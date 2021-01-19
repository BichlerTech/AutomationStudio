package com.bichler.astudio.editor.pubsub.handler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPReaderHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class PubSubDpReaderHandler extends AbstractOPCUADPReaderHandler
{
  public static final String ID = "com.hbsoft.comet.drv.import.pubsub";

  @Override
  protected void insertDatapoint(Map<NodeId, Object> datapoints, Object dp)
  {
//    NodeId nodeId = ((EventDpItemModel) dp).getNodeId();
//    if (NodeId.isNull(nodeId))
//    {
//      return;
//    }
//    datapoints.put(nodeId, dp);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected List readDatapoints(InputStream input)
  {
//    EventsImporter importer = new EventsImporter();
//    List<EventDpItemModel> dps = importer.loadDPs(input
    // new InputSource(
    // new InputStreamReader(input, "UTF-8"))
//        , ServerInstance.getInstance().getServerInstance().getNamespaceUris());
    // List<EventDpSyntax> nodes = new ArrayList<>();
    // for (SiemensDPItem dp : dps) {
    // SiemensEntryModelNode rdp = SiemensEntryModelNode
    // .loadFromDP(dp);
    // nodes.add(rdp);
    // }
//    return dps;
	  return null;
  }
}
