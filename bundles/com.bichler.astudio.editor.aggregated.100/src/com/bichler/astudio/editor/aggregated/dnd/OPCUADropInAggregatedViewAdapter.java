package com.bichler.astudio.editor.aggregated.dnd;

import java.lang.reflect.InvocationTargetException;
import java.util.Deque;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;
import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.editor.aggregated.AggregatedActivator;
import com.bichler.astudio.editor.aggregated.AggregatedEditorDevice;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpModelNode;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserConstants;

public class OPCUADropInAggregatedViewAdapter extends ViewerDropAdapter
{
  private UAServerModelNode server = null;
  private AggregatedEditorDevice client = null;
  private IOPCDataPointEditPart editor = null;

  public OPCUADropInAggregatedViewAdapter(Viewer viewer, IOPCDataPointEditPart editor, UAServerModelNode server)
  {
    super(viewer);
    this.editor = editor;
    this.server = server;
    if (this.server != null)
      this.client = (AggregatedEditorDevice) this.server.getDevice();
  }


  abstract class AggregatedRunnableWithProgress implements IRunnableWithProgress
  {
    boolean result = false;

    public boolean getResult()
    {
      return this.result;
    }

    public void setResult(boolean result)
    {
      this.result = result;
    }
  }

  @Override
  public boolean performDrop(final Object data)
  {
    if (data instanceof String)
    {
      AggregatedRunnableWithProgress runnable = new AggregatedRunnableWithProgress()
      {
        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          monitor.beginTask(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.aggregated.drivereditor.monitor.dnd.task"), IProgressMonitor.UNKNOWN);
          monitor.subTask(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.aggregated.drivereditor.monitor.dnd.task.sub"));
          boolean isRemote = ((String) data).startsWith(DriverBrowserConstants.MARKER_DRAGNDROP);
          // information use for drop
          NodeId nodeId = null;
          String name = "";
          if (!isRemote)
          {
            nodeId = NodeId.parseNodeId((String) data);
            DataValue[] result = null;
            try
            {
              // opc ua read service
              ReadValueId[] nodesToRead = new ReadValueId[1];
              nodesToRead[0] = new ReadValueId();
              nodesToRead[0].setAttributeId(Attributes.DisplayName);
              nodesToRead[0].setNodeId(nodeId);
              result = ServerInstance.getInstance().getServerInstance().getMaster().read(nodesToRead, 0.0,
                  TimestampsToReturn.Both, null, null);
              if (result != null && result.length > 0 && result[0] != null)
              {
                name = ((LocalizedText) result[0].getValue().getValue()).getText();
              }
            }
            catch (ServiceResultException e)
            {
              e.printStackTrace();
            }
          }
          else
          {
            String replaced = ((String) data).replaceFirst(DriverBrowserConstants.MARKER_DRAGNDROP, "");
            nodeId = NodeId.parseNodeId(replaced);
            DataValue result = client.readAggregatedValue(nodeId, Attributes.DisplayName);
            if (result != null && result.getValue() != null && !result.getValue().isEmpty())
            {
              name = ((LocalizedText) result.getValue().getValue()).getText();
            }
          }
          if (NodeId.isNull(nodeId))
          {
            result = false;
            return;
          }
          // find table item to drop into
          DropTargetEvent event = getCurrentEvent();
          Widget src = event.item;
          // drop to no table item
          if (src == null)
          {
            result = false;
            return;
          }
          AggregatedDpModelNode dp = (AggregatedDpModelNode) src.getData();
          // local server
          if (!isRemote)
          {
            dp.getDPItem().setServerNodeId(nodeId);
            dp.getDPItem().setServerDisplayName(name);
            // fill full string to browsepath
            String browsepath = "";
            Deque<BrowsePathElement> path = OPCUABrowseUtils.getFullBrowsePath(nodeId,
                ServerInstance.getInstance().getServerInstance(), Identifiers.ObjectsFolder);
            for (BrowsePathElement element : path)
            {
              if (element.getId().equals(Identifiers.ObjectsFolder))
              {
                continue;
              }
              browsepath += "//" + element.getBrowsename().getName();
            }
            dp.getDPItem().setServerBrowsePath(browsepath);
            ((ColumnViewer) getViewer()).update(dp, null);
            editor.setDirty(true);
            result = true;
          }
          // remote server
          else
          {
            dp.getDPItem().setTargetNodeId(nodeId);
            dp.getDPItem().setTargetDisplayName(name);
            // fill full string to browsepath
            String browsepath = "";
            Deque<BrowsePathElement> path = client.getFullBrowsePath(nodeId, client, Identifiers.ObjectsFolder);
            for (BrowsePathElement element : path)
            {
              if (element.getId().equals(Identifiers.ObjectsFolder))
              {
                continue;
              }
              browsepath += "//" + element.getBrowsename().getName();
            }
            // browsepath += "//Objects//";
            dp.getDPItem().setTargetBrowsePath(browsepath);
            ((ColumnViewer) getViewer()).update(dp, null);
            editor.setDirty(true);
            result = true;
          }
          monitor.done();
        }
      };
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(getViewer().getControl().getShell());
      try
      {
        dialog.run(false, false, runnable);
        return runnable.getResult();
      }
      catch (InvocationTargetException e)
      {
        e.printStackTrace();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    return false;
  }

  @Override
  public void dragEnter(DropTargetEvent event)
  {
    event.detail = DND.DROP_COPY;
    super.dragEnter(event);
  }

  @Override
  public boolean validateDrop(Object data, int operation, TransferData transferType)
  {
    // DropTargetEvent event = getCurrentEvent();
    //
    // int index = getIndexFromTableItem(event);
    // if (index < 0) {
    // return false;
    // }
    return true;
    // Widget item = event.item;
    //
    // if (data instanceof String) {
    // NodeId nodeId = NodeId.parseNodeId((String) data);
    //
    // ReadValueId[] nodesToRead = new ReadValueId[1];
    // nodesToRead[0] = new ReadValueId();
    // nodesToRead[0].setAttributeId(Attributes.DisplayName);
    // nodesToRead[0].setNodeId(nodeId);
    // String name = "";
    // // try {
    // // DataValue[] result = ServerInstance.getInstance()
    // // .getServerInstance().getProfileManager()
    // // .read(nodesToRead, 0.0, TimestampsToReturn.Both);
    // //
    // // if (result != null && result.length > 0 && result[0] != null) {
    // // name = ((LocalizedText) result[0].getValue().getValue())
    // // .getText();
    // // }
    // //
    // // } catch (ServiceResultException e) {
    // // e.printStackTrace();
    // // }
    //
    // if (item != null) {
    // return setItemValues(item, index, nodeId, name);
    // }
    // }
    //
    // else if(data instanceof AggregatedDP){
    //
    // System.out.println();
    // }
    //
    // return false;
  }

  public boolean setItemValues(Widget item, int index, NodeId nodeId, String name)
  {
    // if (item.getData() instanceof AbstractCCModel) {
    // AbstractCCModel node = (AbstractCCModel) item
    // .getData();
    // if (node != null) {
    // boolean isDirty = false;
    // switch (index) {
    // case 1:
    // node.setDeviceId(nodeId);
    // node.setDeviceName(name);
    // ((ColumnViewer) getViewer()).update(node, null);
    // isDirty = true;
    // break;
    // case 2:
    // node.setConfigId(nodeId);
    // node.setConfigNodeName(name);
    // ((ColumnViewer) getViewer()).update(node, null);
    // isDirty = true;
    // break;
    // }
    // ((ColumnViewer) getViewer()).update(node, null);
    // if(isDirty){
    // this.editor.setDirty(true);
    // }
    // // this.editor.setDirty(true);
    // return true;
    // }
    // }
    return false;
  }

  int getIndexFromTableItem(DropTargetEvent event)
  {
    Viewer viewer2use = getViewer();
    if (viewer2use instanceof ColumnViewer)
    {
      Point location = getViewer().getControl().toControl(event.x, event.y);
      ViewerCell cell = ((ColumnViewer) viewer2use).getCell(location);
      if (cell != null)
      {
        return cell.getColumnIndex();
      }
    }
    return -1;
  }
}
