package com.bichler.astudio.editor.aggregated.clientbrowser.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.editor.aggregated.clientbrowser.util.OPCConnectionAdapter;
import com.bichler.astudio.images.opcua.OPCImagesActivator;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;

public abstract class AbstractCCModel extends AbstractDriverModelViewNode
{
  // parent
  private AbstractCCModel parent = null;
  //
  private List<AbstractCCModel> children = new ArrayList<>();
  private String displayname = "";
  private NodeClass nodeClass = NodeClass.Unspecified;
  private OPCConnectionAdapter client;
  private NodeId nodeId;

  public AbstractCCModel(String displayname, NodeClass nodeClass, NodeId nodeId)
  {
    super(null);
    this.setDisplayname(displayname);
    this.setNodeClass(nodeClass);
    this.setNodeId(nodeId);
  }

  public AbstractCCModel[] getChildren()
  {
    return children.toArray(new AbstractCCModel[0]);
  }

  public void addChild(AbstractCCModel child)
  {
    child.setParent(this);
    child.setClient(client);
    this.children.add(child);
  }

  public AbstractCCModel getParent()
  {
    return parent;
  }

  private void setParent(AbstractCCModel parent)
  {
    this.parent = parent;
  }

  public String getDisplayname()
  {
    return displayname;
  }

  public void setDisplayname(String displayname)
  {
    this.displayname = displayname;
  }

  public NodeClass getNodeClass()
  {
    return nodeClass;
  }

  public void setNodeClass(NodeClass nodeClass)
  {
    this.nodeClass = nodeClass;
  }

  public OPCConnectionAdapter getClient()
  {
    return this.client;
  }

  protected void setClient(OPCConnectionAdapter serverAdapter)
  {
    this.client = serverAdapter;
  }

  public NodeId getNodeId()
  {
    return this.nodeId;
  }

  protected void setNodeId(NodeId nodeId)
  {
    this.nodeId = nodeId;
  }

  /**
   * fetch children with the client instance and use addChild for them
   * 
   * @param element
   */
  public void fetchChildren()
  {
    NodeId parentId = getNodeId();
    BrowseResult childrenResult = this.client.browse(parentId, BrowseDirection.Forward, true,
        NodeClass.getMask(NodeClass.ALL), Identifiers.HierarchicalReferences,
        BrowseResultMask.getMask(BrowseResultMask.ALL));
    if (childrenResult != null && childrenResult.getReferences() != null)
    {
      for (ReferenceDescription e : childrenResult.getReferences())
      {
        LocalizedText name = e.getDisplayName();
        NodeClass nc = e.getNodeClass();
        ExpandedNodeId nId = e.getNodeId();
        NodeId nid = NodeId.get(nId.getIdType(), nId.getNamespaceIndex(), nId.getValue());
        NodeCCModel newModel = new NodeCCModel(name.getText(), nc, nid);
        this.addChild(newModel);
      }
    }
  }

  public void prepareFetchChildren()
  {
    this.children.clear();
  }

  /**
   * Not used in aggregated
   * 
   * @Override
   */
  public Image getDecorator()
  {
    NodeClass img = getNodeClass();
    switch (img)
    {
    case DataType:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
          OPCImagesActivator.DATATYPE);
    case Method:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
          OPCImagesActivator.METHOD);
    case Object:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
          OPCImagesActivator.OBJECT);
    case ObjectType:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
          OPCImagesActivator.OBJECTTYPE);
    case ReferenceType:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
          OPCImagesActivator.REFERENCETYPE);
    case Variable:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
          OPCImagesActivator.VARIABLE);
    case VariableType:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
          OPCImagesActivator.VARIABLETYPE);
    case View:
      return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16, OPCImagesActivator.VIEW);
    default:
      break;
    }
    return null;
  }

  @Override
  public String getText()
  {
    return getDisplayname();
  }
}
