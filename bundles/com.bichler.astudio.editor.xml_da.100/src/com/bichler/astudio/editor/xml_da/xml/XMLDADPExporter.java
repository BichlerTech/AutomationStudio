package com.bichler.astudio.editor.xml_da.xml;

import java.util.Map;

import opc.sdk.core.node.mapper.NodeIdMapper;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.AbstractOPCDriverExporter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class XMLDADPExporter extends AbstractOPCDriverExporter
{
  public XMLDADPExporter(IFileSystem filesystem, String path) {
		super(filesystem, path);
	}

@Override
  protected void updateChild(IDriverNode item2update, OPCUAUpdateNodeIdEvent trigger)
  {
    NodeId nodeId = ((XMLDAModelNode) item2update).getNId();
    NodeToTrigger triggerNode = ((XMLDAModelNode) item2update).getTrigger();
    NodeId newId = trigger.getNewId();
    NodeId oldId = trigger.getOldId();
    // dochange
    if (oldId.equals(nodeId))
    {
      ((XMLDAModelNode) item2update).setNodeId(newId);
    }
    if (oldId.equals(triggerNode.nodeId))
    {
      triggerNode.nodeId = newId;
    }
  }

  @Override
  protected void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping)
  {
    NodeId nodeId = ((XMLDAModelNode) item2update).getNId();
    NodeToTrigger triggerNode = ((XMLDAModelNode) item2update).getTrigger();
    NodeId newNodeId = NodeIdMapper.mapNamespaceIndex(nodeId, mapping);
    NodeId newTriggerId = NodeIdMapper.mapNamespaceIndex(triggerNode.nodeId, mapping);
    // dochange
    ((XMLDAModelNode) item2update).setNodeId(newNodeId);
    triggerNode.nodeId = newTriggerId;
  }

  @Override
  protected StringBuffer saveChildren(IDriverNode driverNode, NamespaceTable nsTable)
  {
	  StringBuffer buffer = new StringBuffer();
    XMLDAModelNode item = (XMLDAModelNode) driverNode;
    buffer.append("  <dp>\n");
    if (item.getNId() != null)
    {
      String[] idelements = item.getNId().toString().split(";");
      String id = "";
      if (idelements != null)
      {
        if (idelements.length == 1)
        {
          id = idelements[0];
        }
        else if (idelements.length == 2)
        {
          id = idelements[1];
        }
      }
      String nsuri = nsTable.getUri(item.getNId().getNamespaceIndex());
      buffer.append("    <nodeid value=\"ns=" + nsuri + ";" + id + "\" />\n");
    }
    buffer.append("    <symbolname value=\"" + item.getSymbolName() + "\" />\n");
    buffer.append("    <isactive value=\"" + item.isActive() + "\" />\n");
    buffer.append("    <cycletime value=\"" + item.getCycletime() + "\" />\n");
    buffer.append("    <itempath value=\"" + item.getItemPath() + "\" />\n");
    buffer.append("    <itemname value=\"" + item.getItemName() + "\" />\n");
    buffer.append("    <mapping value=\"" + item.getMapping() + "\" />\n");
    buffer.append("    <datatype value=\"" + item.getDataType() + "\" />\n");
    // now generate the trigger node id
    if (item.getTrigger() != null && item.getTrigger().nodeId != null)
    {
      String[] idelements = item.getTrigger().nodeId.toString().split(";");
      String id = "";
      if (idelements != null)
      {
        if (idelements.length == 1)
        {
          id = idelements[0];
        }
        else if (idelements.length == 2)
        {
          id = idelements[1];
        }
      }
      String nsuri = nsTable.getUri(item.getTrigger().nodeId.getNamespaceIndex());
      buffer.append("    <trigger ns=\"" + nsuri + "\" value=\"" + id + "\" />\n");
    }
    buffer.append("    <description value=\"" + item.getDescription() + "\" />\n");
    buffer.append("  </dp>\n");
    
    return buffer;
  }
}
