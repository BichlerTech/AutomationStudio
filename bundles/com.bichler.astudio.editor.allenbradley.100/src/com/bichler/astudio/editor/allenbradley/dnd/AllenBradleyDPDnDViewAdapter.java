package com.bichler.astudio.editor.allenbradley.dnd;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.allenbradley.driver.AllenBradleyDriverDragSupport;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.opcua.dnd.OPCUADropInViewAdapter;
import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;

public class AllenBradleyDPDnDViewAdapter extends OPCUADropInViewAdapter
{
  private IOPCDataPointEditPart editor;

  public AllenBradleyDPDnDViewAdapter(Viewer viewer, IOPCDataPointEditPart editor)
  {
    super(viewer);
    this.editor = editor;
  }

  @Override
  public boolean setItemValues(Widget item, int index, NodeId nodeId, String name)
  {
    AllenBradleyEntryModelNode dp = (AllenBradleyEntryModelNode) ((TableItem) item).getData();
    dp.setNodeId(nodeId);
    dp.setDisplayname(name);
    String browsepath = getBrowsePath(dp.getNodeId());
    dp.setBrowsepath(browsepath);
    ((TableViewer) getViewer()).update(dp, null);
    this.editor.setDirty(true);
    return true;
  }

  @Override
  public boolean validateDropInView(DropTargetEvent event)
  {
    // this.getSelectedObject()ns=4;i=7
    // Object datat = event.ns=5;i=2
    return true;
  }

  @Override
  protected boolean setRemoteItemValues(Object node, String data)
  {
    AllenBradleyEntryModelNode dp = (AllenBradleyEntryModelNode) node;
    String[] attributes = AllenBradleyDriverDragSupport.convertTextToAttributes(data);
    AllenBradleyDriverDragSupport.setAttributesToNode(dp, attributes);
    ((TableViewer) getViewer()).update(dp, null);
    this.editor.setDirty(true);
    return true;
  }

  @Override
  protected boolean setMoveItemValues(Object node, String data)
  {
    // TODO Auto-generated method stub
    return false;
  }
}
