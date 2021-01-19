package com.bichler.astudio.editor.xml_da.dnd;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.xml_da.XML_DA_DPEditor;
import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;
import com.bichler.astudio.opcua.dnd.OPCUADropInViewAdapter;
import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;

public class XMLDaDPDndViewAdapter extends OPCUADropInViewAdapter
{
  private IOPCDataPointEditPart editor;

  public XMLDaDPDndViewAdapter(Viewer viewer, IOPCDataPointEditPart editor)
  {
    super(viewer);
    this.editor = editor;
  }

  @Override
  public boolean setItemValues(Widget item, int index, NodeId nodeId, String name)
  {
    XMLDAModelNode dp = (XMLDAModelNode) ((TableItem) item).getData();
    dp.setNodeId(nodeId);
    dp.setDisplayname(name);
    String browsepath = getBrowsePath(dp.getNId());
    dp.setBrowsepath(browsepath);
    ((TableViewer) getViewer()).update(dp, null);
    this.editor.setDirty(true);
    return true;
  }

  @Override
  protected boolean setRemoteItemValues(Object node, String data)
  {
    XMLDAModelNode dp = (XMLDAModelNode) node;
    ((TableViewer) getViewer()).update(dp, null);
    this.editor.setDirty(true);
    return true;
  }

  @Override
  public boolean validateDropInView(DropTargetEvent event)
  {
    return true;
  }

  @Override
  protected boolean setMoveItemValues(Object node, String data)
  {
    // TODO Auto-generated method stub
    return false;
  }
}
