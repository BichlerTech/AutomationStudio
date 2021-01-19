package com.bichler.astudio.opcua.addressspace.model.gen.classes.model;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import opc.sdk.core.node.VariableNode;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.UAServerApplicationInstance;

public class VariableGen extends BaseVariableGen
{
  public VariableGen(VariableNode node)
  {
    super(node);
  }

  @Override
  public VariableNode getNode()
  {
    return (VariableNode) super.getNode();
  }

  @Override
  public ExpandedNodeId prepareAsVariable(UAServerApplicationInstance server)
  {
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
