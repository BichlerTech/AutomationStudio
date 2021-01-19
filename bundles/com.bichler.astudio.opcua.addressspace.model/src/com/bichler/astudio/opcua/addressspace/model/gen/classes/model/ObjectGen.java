package com.bichler.astudio.opcua.addressspace.model.gen.classes.model;

import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

public class ObjectGen extends BaseObjectGen
{
  public ObjectGen(ObjectNode node)
  {
    super(node);
  }

  @Override
  public ObjectNode getNode()
  {
    return (ObjectNode) super.getNode();
  }

  @Override
  public ExpandedNodeId prepareAsVariable(UAServerApplicationInstance server)
  {
    // find object type (serious)
    // find variable type (serious)
    ExpandedNodeId type = ExpandedNodeId.NULL;
    ReferenceNode[] references = getNode().getReferences();
    if (references != null)
    {
      TypeTable typeTable = server.getServerInstance().getTypeTable();
      for (ReferenceNode ref : references)
      {
        NodeId refTypeId = ref.getReferenceTypeId();
        boolean isInverse = ref.getIsInverse();
        boolean isTypeOf = typeTable.isTypeOf(refTypeId, Identifiers.HasTypeDefinition);
        if (isTypeOf && !isInverse)
        {
          type = ref.getTargetId();
          break;
        }
      }
    }
    return type;
  }
}
