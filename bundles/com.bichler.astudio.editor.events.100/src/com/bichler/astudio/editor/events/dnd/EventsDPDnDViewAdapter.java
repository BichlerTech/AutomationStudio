package com.bichler.astudio.editor.events.dnd;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.editor.events.xml.EventEntryModelNode;
import com.bichler.astudio.opcua.dnd.OPCUADropInViewAdapter;
import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;
import com.hbsoft.driver.opc.events.dp.AbstractDatapointModel;
import com.hbsoft.driver.opc.events.dp.EventDefItemModel;
import com.hbsoft.driver.opc.events.dp.EventDpItemModel;
import com.hbsoft.driver.opc.events.expression.AbstractExpressionModel;
import com.hbsoft.driver.opc.events.expression.group.GroupExpressionModel;
import com.hbsoft.driver.opc.events.expression.io.AbstractIOExpressionModel;

import opc.sdk.core.node.Node;

public class EventsDPDnDViewAdapter extends OPCUADropInViewAdapter
{
  private IOPCDataPointEditPart editor;

  public EventsDPDnDViewAdapter(Viewer viewer, IOPCDataPointEditPart editor)
  {
    super(viewer);
    this.editor = editor;
  }

  @Override
  protected boolean performDropFilter(Node node, Widget item, int index)
  {
    EventEntryModelNode model = (EventEntryModelNode) item.getData();
    switch (model.getType())
    {
    case DATAPOINT:
      return node.getNodeClass() == NodeClass.Object;
    case EVENT:
      return node.getNodeClass() == NodeClass.ObjectType;// event type
    case EXPRESSION:
      switch (index)
      {
      case 0:
        return node.getNodeClass() == NodeClass.Variable;
      }
    }
    return super.performDropFilter(node, item, index);
  }

  @Override
  public boolean setItemValues(Widget item, int index, NodeId nodeId, String name)
  {
    EventEntryModelNode data = (EventEntryModelNode) ((TreeItem) item).getData();
    if (data.getItem() instanceof AbstractDatapointModel)
    {
      AbstractDatapointModel dp = (AbstractDatapointModel) data.getItem();
      // TODO: any column
      // switch (index) {
      // case 0:
      dp.setNodeId(nodeId);
      dp.setDisplayname(name);
      // break;
      // }
    }
    else if (data.getItem() instanceof AbstractIOExpressionModel)
    {
      ((AbstractIOExpressionModel) data.getItem()).setNodeId(nodeId);
    }
    // else if (data.getItem() instanceof EventDpSyntax) {
    // EventDpSyntax dp = (EventDpSyntax) data.getItem();
    //
    // switch (index) {
    // case 0:
    // // dp.setSourceId(nodeId);
    // // dp.setDisplayname(name);
    // // // browsename
    // // String browsepath = getBrowsePath(data.getItem().getSourceId(),
    // // Identifiers.RootFolder);
    // // data.getItem().setBrowsepath(browsepath);
    // break;
    // // case 4:
    // // dp.setVariableSource(nodeId);
    // // break;
    // }
    // }
    ((TreeViewer) getViewer()).update(data, null);
    this.editor.setDirty(true);
    return true;
  }

  @Override
  protected boolean setRemoteItemValues(Object node, String data)
  {
    // EventDpSyntax dp = (EventDpSyntax) node;
    // String[] attributes = EventsDriverDragSupport
    // .convertTextToAttributes(data);
    // EventsDriverDragSupport.setAttributesToNode(dp, attributes);
    ((TreeViewer) getViewer()).update(node, null);
    this.editor.setDirty(true);
    return true;
  }

  @Override
  public boolean validateDropInView(DropTargetEvent event)
  {
//    int index = getIndexFromTableItem(getCurrentEvent());
    EventEntryModelNode node = (EventEntryModelNode) getCurrentTarget();
    if (node.getItem() instanceof EventDpItemModel)
    {
      return true;
    }
    else if (node.getItem() instanceof EventDefItemModel)
    {
      return true;
    }
    else if (node.getItem() instanceof AbstractExpressionModel)
    {
      return true;
    }
    // else if (node.getItem() instanceof EventDpSyntax) {
    // switch (index) {
    // case 0:
    // case 1:
    // return true;
    // // case 4:
    // // return true;
    // }
    // }
    return false;
  }

  @Override
  protected boolean setMoveItemValues(Object node, String data)
  {
    EventEntryModelNode fe = (EventEntryModelNode) ((IStructuredSelection) editor.getSelection()).getFirstElement();
    EventEntryModelNode feParent = fe.getParent();
    EventEntryModelNode nodeParent = ((EventEntryModelNode) node).getParent();
    int feIndex = feParent.getChildrenList().indexOf(fe);
    int nodeIndex = nodeParent.getChildrenList().indexOf(node);
    if (feIndex < 0 || nodeIndex < 0)
    {
      return false;
    }
    feParent.getChildrenList().remove(fe);
    if (((EventEntryModelNode) node).getItem() instanceof GroupExpressionModel)
    {
      ((EventEntryModelNode) node).getChildrenList().add(fe);
    }
    else
    {
      nodeParent.getChildrenList().add(nodeIndex, fe);
    }
    ((TreeViewer) getViewer()).refresh(feParent);
    ((TreeViewer) getViewer()).refresh(nodeParent);
    this.editor.setDirty(true);
    return true;
  }
}
