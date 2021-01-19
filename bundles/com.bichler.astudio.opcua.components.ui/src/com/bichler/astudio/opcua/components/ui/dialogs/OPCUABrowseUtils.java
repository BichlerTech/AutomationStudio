package com.bichler.astudio.opcua.components.ui.dialogs;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.client.application.UAClient;
import opc.sdk.core.node.Node;
import opc.sdk.server.core.OPCInternalServer;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.opcua.components.ui.BrowsePathElement;

public class OPCUABrowseUtils
{
  /**
   * get the whole browsepath from root to the required node, browse will only be
   * performed on local address space, no connection to client required
   * 
   * @param id
   * @param opc
   * @param endnode
   * @return
   */
  public static Deque<BrowsePathElement> getFullBrowsePath(NodeId id, OPCInternalServer opc, NodeId endnode)
  {
    Deque<BrowsePathElement> queue = new ArrayDeque<BrowsePathElement>();
    if (id == null)
    {
      return queue;
    }
    BrowsePathElement element = null;
    NodeId baseTypeDef = null;
    Node value = opc.getAddressSpaceManager().getNodeById(id);
    if (value == null)
    {
      // we couldn't find the selected node
      return queue;
    }
    element = new BrowsePathElement();
    if (value != null)
    {
      element.setDisplayname(value.getDisplayName());
      element.setBrowsename(value.getBrowseName());
    }
    element.setId(id);
    queue.add(element);
    while (!id.equals(endnode))
    {
      BrowseDescription[] nodesToBrowse = {
          new BrowseDescription(id, BrowseDirection.Inverse, Identifiers.HierarchicalReferences, true,
              NodeClass.getMask(NodeClass.ALL), BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values()))) };
      try
      {
        BrowseResult references = opc.getMaster().browse(nodesToBrowse, UnsignedInteger.ZERO, null, null)[0];
        if (references != null && references.getReferences() != null && references.getReferences().length > 0)
        {
          if (references.getReferences()[0] != null)
          {
            baseTypeDef = opc.getNamespaceUris().toNodeId(references.getReferences()[0].getNodeId());
            Node node = opc.getAddressSpaceManager().getNodeById(baseTypeDef);
            if(node == null) {
            	Logger.getLogger(OPCUABrowseUtils.class.getName()).log(Level.SEVERE, "Can not find Node for nodeid: " + baseTypeDef);
            }
            element = new BrowsePathElement();
            element.setDisplayname(node.getDisplayName());
            element.setBrowsename(node.getBrowseName());
            element.setId(baseTypeDef);
            queue.addFirst(element);
            id = baseTypeDef;
          }
        }
        // no more references
        else
        {
          break;
        }
      }
      catch (ServiceResultException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return queue;
  }

  /**
   * get the whole browsepath from root to the required node, browse will only be
   * performed on local address space, no connection to client required
   * 
   * @param id
   * @param opc
   * @param endnode
   * @return
   */
  public static Deque<BrowsePathElement> getFullBrowsePath(NodeId id, UAClient opc, NodeId endnode)
  {
    Deque<BrowsePathElement> queue = new ArrayDeque<BrowsePathElement>();
    if (id == null)
    {
      return queue;
    }
    BrowsePathElement element = null;
    NodeId baseTypeDef = null;
    LocalizedText displayname = null;
    QualifiedName qualifiedName = null;
    try
    {
      DataValue value = opc.read1(opc.getActiveSession(), id, Attributes.DisplayName, null, null, 0.0,
          TimestampsToReturn.Both);
      if (value != null && value.getValue() != null && value.getValue().getValue() != null)
      {
        displayname = (LocalizedText) value.getValue().getValue();
      }
      value = opc.read1(opc.getActiveSession(), id, Attributes.BrowseName, null, null, 0.0, TimestampsToReturn.Both);
      if (value != null && value.getValue() != null && value.getValue().getValue() != null)
      {
        qualifiedName = (QualifiedName) value.getValue().getValue();
      }
    }
    catch (ServiceResultException e)
    {
      e.printStackTrace();
      return queue;
    }
    element = new BrowsePathElement();
    element.setDisplayname(displayname);
    element.setBrowsename(qualifiedName);
    element.setId(id);
    queue.add(element);
    boolean references2browse = true;
    while (!id.equals(endnode) && references2browse)
    {
      /**
       * BrowseDescription[] nodesToBrowse = { new BrowseDescription( id,
       * BrowseDirection.Inverse, Identifiers.HierarchicalReferences, true,
       * NodeClass.getMask(NodeClass.ALL), BrowseResultMask.getMask(Arrays
       * .asList(BrowseResultMask.values()))) };
       */
      try
      {
        BrowseResult references = opc.browse(opc.getActiveSession(), id, BrowseDirection.Inverse, true,
            NodeClass.getMask(NodeClass.ALL), Identifiers.HierarchicalReferences,
            BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values())), UnsignedInteger.ZERO, null, false);
        if (references != null && references.getReferences() != null && references.getReferences().length > 0
            && references.getReferences()[0] != null)
        {
          element = new BrowsePathElement();
          element.setDisplayname(references.getReferences()[0].getDisplayName());
          element.setBrowsename(references.getReferences()[0].getBrowseName());
          baseTypeDef = NodeId.get(references.getReferences()[0].getNodeId().getIdType(),
              references.getReferences()[0].getNodeId().getNamespaceIndex(),
              references.getReferences()[0].getNodeId().getValue());
          element.setId(baseTypeDef);
          queue.addFirst(element);
          id = baseTypeDef;
        }
        else
        {
          references2browse = false;
        }
      }
      catch (ServiceResultException e)
      {
        e.printStackTrace();
      }
    }
    return queue;
  }
}
