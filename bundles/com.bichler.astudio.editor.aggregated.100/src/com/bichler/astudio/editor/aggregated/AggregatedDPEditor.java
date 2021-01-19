package com.bichler.astudio.editor.aggregated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.progress.UIJob;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.editor.aggregated.dnd.OPCUADropInAggregatedViewAdapter;
import com.bichler.astudio.editor.aggregated.dp.AggregatedDPItem;
import com.bichler.astudio.editor.aggregated.driver.AggregatedDriverUtil;
import com.bichler.astudio.editor.aggregated.model.AggregatedDPEditorExporter;
import com.bichler.astudio.editor.aggregated.model.AggregatedDPEditorImporter;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpModelNode;
import com.bichler.astudio.editor.aggregated.model.AggregatedDriverConfigurationType;
import com.bichler.astudio.editor.aggregated.wizard.NodeIdWizard;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.AbstractOPCDPDriverViewLinkEditorPart;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUARemoteNodeDialog;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.TableWidgetEnum;
import com.bichler.astudio.opcua.widget.TableWidgetUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.client.application.UAClient;
import opc.client.application.listener.ReconnectListener;
import opc.client.service.ClientSession;
import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

public class AggregatedDPEditor extends AbstractOPCDPDriverViewLinkEditorPart
{
  public final static String ID = "com.bichler.astudio.editor.aggregated.AggregatedDPEditor"; //$NON-NLS-1$
  // public static UAServerApplicationInstance aggregatedUAServer = null;
  private OPCUADropInAggregatedViewAdapter dropListener = null;
  private boolean dirty = false;
  private TableViewer tableViewer;
  private UAServerModelNode server;
  private UAServerApplicationInstance opcServer = null;
  private ISelectionChangedListener listener;
  private Object currentSelection;

  public AggregatedDPEditor()
  {
  }

  @Override
  public void addSelectionChangedListener(ISelectionChangedListener listener)
  {
    this.listener = listener;
  }

  @Override
  protected void createPages()
  {
    Composite composite = new Composite(getContainer(), SWT.NONE);
    FillLayout layout = new FillLayout();
    composite.setLayout(layout);
    this.createPage1(composite);
    int index = addPage(composite);
    setPageText(index, "edit");
  }

  /**
   * Create contents of the editor part.
   * 
   * @param parent
   */
  public void createPage1(Composite parent)
  {
    ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    Composite composite = new Composite(scrolledComposite, SWT.NONE);
    composite.setLayout(new GridLayout(3, false));
    tableViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL);
    this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        setDataPointSelection(((StructuredSelection) event.getSelection()).getFirstElement());
      }
    });
    final Table table = tableViewer.getTable();
    table.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseDoubleClick(MouseEvent event)
      {
        try
        {
          // get the selected node id
          Point p = new Point(event.x, event.y);
          ViewerCell cell = tableViewer.getCell(p);
          if (cell == null)
            return;
          int columnIndex = cell.getColumnIndex();
          if (columnIndex <= 2)
          {
            TableItem[] selection = table.getSelection();
            if (selection != null && selection.length > 0)
            {
              TableItem item = selection[0];
              Object data = item.getData();
              if (data != null && data instanceof AggregatedDpModelNode)
              {
                final AggregatedDPItem dp = ((AggregatedDpModelNode) data).getDPItem();
                NodeId nodeid = dp.getServerNodeId();
                OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
                dialog.setInternalServer(opcServer.getServerInstance());
                dialog.setSelectedNodeId(nodeid);
                // dialog.setStartNodeId(Identifiers.ObjectsFolder);
                dialog.setFormTitle(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
                    "com.bichler.astudio.editor.aggregated.dpeditor.dialog.remote.title"));
                if (dialog.open() == Dialog.OK)
                {
                  dp.setServerNodeId(dialog.getSelectedNodeId());
                  dp.setServerDisplayName(dialog.getSelectedDisplayName());
                  // fill full string to browsepath
                  String browsepath = "";
                  for (BrowsePathElement element : dialog.getBrowsePath())
                  {
                    if (element.getId().equals(Identifiers.ObjectsFolder))
                    {
                      continue;
                    }
                    browsepath += "//" + element.getBrowsename().getName();
                  }
                  dp.setServerBrowsePath(browsepath);
                  // dialog.get
                  // now update tabeviewer
                  tableViewer.refresh();
                  setDirty(true);
                }
              }
            }
          }
          else if (columnIndex > 3)
          {
            // we also neet to check the connection to the
            // underlying opc ua server
            TableItem[] selection = table.getSelection();
            if (selection != null && selection.length > 0)
            {
              TableItem item = selection[0];
              Object data = item.getData();
              if (data != null && data instanceof AggregatedDpModelNode)
              {
                AggregatedDPItem dp = ((AggregatedDpModelNode) data).getDPItem();
                NodeId nodeid = dp.getTargetNodeId();
                // open dialog with "connected" server
                boolean onlineServer = true;
                // check if server is online or not
                if (server != null && server.getDevice() != null && server.getDevice().getUaclient() != null)
                {
                  UAClient client = server.getDevice().getUaclient();
                  ClientSession session = client.getActiveSession();
                  boolean reconnecting = session.isReconnection();
                  // session is trying to reconnect to an
                  // offline server
                  if (reconnecting)
                  {
                    onlineServer = false;
                  }
                }
                else
                {
                  onlineServer = false;
                }
                // open dialog for a online server
                if (onlineServer)
                {
                  OPCUARemoteNodeDialog remoteDialog = new OPCUARemoteNodeDialog(Display.getCurrent().getActiveShell());
                  remoteDialog.setSelServerNode(server);
                  remoteDialog.setSelectedNodeId(nodeid);
                  if (server != null && server.getDevice() != null && server.getDevice().getUaServerName() != null)
                  {
                    String dialogname = "OPC UA Server";
                    if (!server.getDevice().getUaServerName().isEmpty())
                    {
                      dialogname = CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
                          "com.bichler.astudio.editor.aggregated.dpeditor.dialog.remote.nodesof") + " "
                          + server.getDevice().getUaServerName();
                    }
                    remoteDialog.setFormTitle(dialogname);
                  }
                  if (remoteDialog.open() == Dialog.OK)
                  {
                    dp.setTargetNodeId(remoteDialog.getSelectedNodeId());
                    dp.setTargetDisplayName(remoteDialog.getDisplayName());
                    // fill full string to browsepath
                    String browsepath = "";
                    for (BrowsePathElement element : remoteDialog.getBrowsePath())
                    {
                      if (element.getId().equals(Identifiers.ObjectsFolder))
                      {
                        continue;
                      }
                      browsepath += "//" + element.getBrowsename().getName();
                    }
                    dp.setTargetBrowsePath(browsepath);
                    // dialog.get
                    // now update tabeviewer
                    tableViewer.refresh();
                    setDirty(true);
                  }
                }
                else
                {
                  if (NodeId.isNull(nodeid))
                  {
                    try
                    {
                      nodeid = NodeId.parseNodeId(dp.getLoadedTargetNodeId());
                    }
                    catch (IllegalArgumentException e)
                    {
                      e.printStackTrace();
                    }
                  }
                  NodeIdWizard wizard = new NodeIdWizard(nodeid);
                  WizardDialog dialog = new WizardDialog(getEditorSite().getShell(), wizard);
                  if (Dialog.OK == dialog.open())
                  {
                    dp.setTargetNodeId(wizard.getNewNodeId());
                    dp.setTargetBrowsePath("");
                    dp.setTargetDisplayName("");
                    tableViewer.update(((AggregatedDpModelNode) data), null);
                    setDirty(true);
                  }
                }
              }
            }
          }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    });
    table.setLinesVisible(true);
    table.setHeaderVisible(true);
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
    tableViewer.setContentProvider(new ArrayContentProvider()
    {
      @Override
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }
    });
    this.dropListener = new OPCUADropInAggregatedViewAdapter(this.tableViewer, this, this.server);
    int operations = DND.DROP_COPY;
    Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
    this.tableViewer.addDropSupport(operations, transferTypes, this.dropListener);
    // Activate the tooltip support for the viewer
    ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);
    TableViewerColumn tvc_columnnr = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.row"),
        75);
    tvc_columnnr.setLabelProvider(new ColumnLabelProvider()
    {
      public Image getImage(Object element)
      {
        return null;
      }

      public String getText(Object element)
      {
        @SuppressWarnings("unchecked")
        List<IDriverNode> input = (List<IDriverNode>) tableViewer.getInput();
        int lineNumber = input.indexOf(element);
        return "" + (lineNumber + 1);
      }
    });
    TableViewerColumn tvc_sname = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.servernode"),
        125, new TableWidgetUtil().new AbstractDriverComperator()
        {
          @Override
          public Object getComparableObject(IDriverNode element)
          {
            return ((AggregatedDpModelNode) element).getDPItem().getServerDisplayName();
          }

          @Override
          public int doCompare(Object o1, Object o2)
          {
            return ((String) o1).toLowerCase().compareTo(((String) o2).toLowerCase());
          }
        });
    // new TableViewerColumn(
    // tableViewer, SWT.NONE);
    // TableColumn trclmn_sourceName = tvc_sname.getColumn();
    // trclmn_sourceName.setWidth(115);
    // trclmn_sourceName.setText("Aggregated Name");
    tvc_sname.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Color getBackground(Object element)
      {
        return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
      }

      @Override
      public String getText(Object element)
      {
        return ((AggregatedDpModelNode) element).getDPItem().getServerDisplayName();
      }
    });
    TableViewerColumn tvc_servernodeid = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.servernodeid"),
        125, new TableWidgetUtil().new AbstractDriverComperator()
        {
          @Override
          public Object getComparableObject(IDriverNode element)
          {
            return ((AggregatedDpModelNode) element).getDPItem().getServerNodeId();
          }

          @Override
          public int doCompare(Object o1, Object o2)
          {
            return ((NodeId) o1).compareTo((NodeId) o2);
          }
        });
    // TableColumn trclmn_nodeId = tvc_servernodeid.getColumn();
    // trclmn_nodeId.setWidth(115);
    // trclmn_nodeId.setText("Server NodeId");
    tvc_servernodeid.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Color getBackground(Object element)
      {
        return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
      }

      @Override
      public String getText(Object element)
      {
        if (((AggregatedDpModelNode) element).getDPItem().getServerNodeId() == null)
          return "";
        return ((AggregatedDpModelNode) element).getDPItem().getServerNodeId().toString() + "";
      }
    });
    TableViewerColumn tvc_serverbrowsepath = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.browsepath"),
        150, TableWidgetEnum.BROWSEPATH);
    // TableColumn trclmn_browsePath = tvc_serverbrowsepath.getColumn();
    // trclmn_browsePath.setWidth(144);
    // trclmn_browsePath.setText("Aggregated Browse Path");
    tvc_serverbrowsepath.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Color getBackground(Object element)
      {
        return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
      }

      @Override
      public String getText(Object element)
      {
        return ((AggregatedDpModelNode) element).getDPItem().getServerBrowsePath();
      }
    });
    TableViewerColumn tvc_separator = TableWidgetUtil.createTableColumn(tableViewer, "", 20);
    tvc_separator.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return "";
      }
    });
    TableViewerColumn tvc_targetname = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.target"),
        125, new TableWidgetUtil().new AbstractDriverComperator()
        {
          @Override
          public Object getComparableObject(IDriverNode element)
          {
            return ((AggregatedDpModelNode) element).getDPItem().getTargetDisplayName();
          }

          @Override
          public int doCompare(Object o1, Object o2)
          {
            return ((String) o1).toLowerCase().compareTo(((String) o2).toLowerCase());
          }
        });
    tvc_targetname.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getToolTipText(Object element)
      {
        if (!server.isConnected())
        {
          return CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.target.error.tooltip");
        }
        return server.getDisplayName() + " " + CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.target.connected") + "!";
      }

      @Override
      public Color getBackground(Object element)
      {
        return getBackgroundForViewerItem(element);
      }

      @Override
      public String getText(Object element)
      {
        if (element == null)
        {
          return "";
        }
        String displayname = ((AggregatedDpModelNode) element).getDPItem().getTargetDisplayName();
        if (displayname == null || displayname.isEmpty())
        {
          if (server != null)
          {
            boolean isConnected = server.isConnected();
            if (isConnected)
            {
              NodeId targetNodeId = ((AggregatedDpModelNode) element).getDPItem().getTargetNodeId();
              if (NodeId.isNull(targetNodeId))
              {
                String loadedTargetId = ((AggregatedDpModelNode) element).getDPItem().getLoadedTargetNodeId();
                if (loadedTargetId == null || loadedTargetId.isEmpty())
                {
                  return "";
                }
                try
                {
                  targetNodeId = NodeId.parseNodeId(loadedTargetId);
                }
                catch (IllegalArgumentException iae)
                {
                  return "";
                }
              }
              DataValue dv = getDevice().readAggregatedValue(targetNodeId, Attributes.DisplayName);
              if (dv != null)
              {
                Variant variant = dv.getValue();
                if (variant != null && !variant.isEmpty())
                {
                  if (variant.getValue() instanceof LocalizedText)
                  {
                    LocalizedText value = (LocalizedText) variant.getValue();
                    displayname = value.getText();
                    return displayname;
                  }
                }
              }
            }
          }
          return "";
        }
        return displayname;
      }
    });
    TableViewerColumn tvc_targetnodeid = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.targetnodeid"),
        125, new TableWidgetUtil().new AbstractDriverComperator()
        {
          @Override
          public Object getComparableObject(IDriverNode element)
          {
            NodeId targetId = ((AggregatedDpModelNode) element).getDPItem().getTargetNodeId();
            if (NodeId.isNull(targetId))
            {
              // TODO: loadedtargetid
              return NodeId.NULL;
            }
            return targetId;
          }

          @Override
          public int doCompare(Object o1, Object o2)
          {
            return ((NodeId) o1).compareTo(((NodeId) o2));
          }
        });
    tvc_targetnodeid.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getToolTipText(Object element)
      {
        if (!server.isConnected())
        {
          return CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.target.error.tooltip");
        }
        return server.getDisplayName() + " " + CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.target.connected") + "!";
      }

      @Override
      public Color getBackground(Object element)
      {
        return getBackgroundForViewerItem(element);
      }

      @Override
      public String getText(Object element)
      {
        if (((AggregatedDpModelNode) element).getDPItem().getTargetNodeId() == null)
          return ((AggregatedDpModelNode) element).getDPItem().getLoadedTargetNodeId();
        return ((AggregatedDpModelNode) element).getDPItem().getTargetNodeId().toString();
      }
    });
    TableViewerColumn tvc_targetbrowsepath = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.targetbrowsepath"),
        125, new TableWidgetUtil().new AbstractDriverComperator()
        {
          @Override
          public Object getComparableObject(IDriverNode element)
          {
            return ((AggregatedDpModelNode) element).getDPItem().getTargetBrowsePath();
          }

          @Override
          public int doCompare(Object o1, Object o2)
          {
            return ((String) o1).toLowerCase().compareTo(((String) o2).toLowerCase());
          }
        });
    tvc_targetbrowsepath.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getToolTipText(Object element)
      {
        if (!server.isConnected())
        {
          return CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.target.error.tooltip");
        }
        return server.getDisplayName() + " " + CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.target.connected") + "!";
      }

      @Override
      public Color getBackground(Object element)
      {
        return getBackgroundForViewerItem(element);
      }

      @Override
      public String getText(Object element)
      {
        return ((AggregatedDpModelNode) element).getDPItem().getTargetBrowsePath();
      }
    });
    TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.active"),
        40);
    tvc_active.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return "";
      }

      @Override
      public Image getImage(Object element)
      {
        if (((AggregatedDpModelNode) element).getDPItem().isActive())
        {
          return AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_1);
        }
        else
        {
          return AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_0);
        }
      }
    });
    tvc_active.setEditingSupport(new EditingSupport(tableViewer)
    {
      protected boolean canEdit(Object element)
      {
        return true;
      }

      protected CellEditor getCellEditor(Object element)
      {
        return new CheckboxCellEditor(tableViewer.getTable());
      }

      protected Object getValue(Object element)
      {
        return ((AggregatedDpModelNode) element).getDPItem().isActive();
      }

      protected void setValue(Object element, Object value)
      {
        setDirty(true);
        ((AggregatedDpModelNode) element).getDPItem()
            .setActive(!((AggregatedDpModelNode) element).getDPItem().isActive());
        tableViewer.refresh(element);
      }
    });
    TableViewerColumn tvc_read = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.read"),
        40);
    tvc_read.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return "";
      }

      @Override
      public Image getImage(Object element)
      {
        if (((AggregatedDpModelNode) element).getDPItem().isRead())
        {
          return AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_1);
        }
        else
        {
          return AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_0);
        }
      }
    });
    tvc_read.setEditingSupport(new EditingSupport(tableViewer)
    {
      protected boolean canEdit(Object element)
      {
        return true;
      }

      protected CellEditor getCellEditor(Object element)
      {
        return new CheckboxCellEditor(tableViewer.getTable());
      }

      protected Object getValue(Object element)
      {
        return ((AggregatedDpModelNode) element).getDPItem().isRead();
      }

      protected void setValue(Object element, Object value)
      {
        setDirty(true);
        ((AggregatedDpModelNode) element).getDPItem().setRead(!((AggregatedDpModelNode) element).getDPItem().isRead());
        tableViewer.refresh(element);
      }
    });
    TableViewerColumn tvc_write = TableWidgetUtil.createTableColumn(tableViewer,
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.write"),
        40);
    tvc_write.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return "";
      }

      @Override
      public Image getImage(Object element)
      {
        if (((AggregatedDpModelNode) element).getDPItem().isWrite())
        {
          return AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_1);
        }
        else
        {
          return AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_0);
        }
      }
    });
    tvc_write.setEditingSupport(new EditingSupport(tableViewer)
    {
      protected boolean canEdit(Object element)
      {
        return true;
      }

      protected CellEditor getCellEditor(Object element)
      {
        return new CheckboxCellEditor(tableViewer.getTable());
      }

      protected Object getValue(Object element)
      {
        return ((AggregatedDpModelNode) element).getDPItem().isWrite();
      }

      protected void setValue(Object element, Object value)
      {
        setDirty(true);
        ((AggregatedDpModelNode) element).getDPItem()
            .setWrite(!((AggregatedDpModelNode) element).getDPItem().isWrite());
        tableViewer.refresh(element);
      }
    });
    Button btn_add = new Button(composite, SWT.NONE);
    btn_add.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.dpeditor.widget.button.tooltip.adddp"));
    btn_add.addSelectionListener(new SelectionAdapter()
    {
      @SuppressWarnings("unchecked")
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        /** first get selected index of table */
        StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
        Object[] selections = sel.toArray();
        AggregatedDPItem newitem = new AggregatedDPItem();
        if (tableViewer.getInput() == null)
        {
          ArrayList<AggregatedDpModelNode> nodes = new ArrayList<>();
          newitem.setLoadedTargetNodeId("");
          newitem.setActive(true);
          newitem.setWrite(true);
          newitem.setRead(true);
          AggregatedDpModelNode node = new AggregatedDpModelNode(newitem);
          nodes.add(node);
          tableViewer.setInput(nodes);
        }
        else
        {
          int index = ((ArrayList<AggregatedDPItem>) tableViewer.getInput()).size();
          if (selections != null && selections.length > 0)
          {
            Object o = selections[selections.length - 1];
            index = ((ArrayList<AggregatedDPItem>) tableViewer.getInput()).indexOf(o);
          }
          newitem.setLoadedTargetNodeId("");
          newitem.setActive(true);
          newitem.setWrite(true);
          newitem.setRead(true);
          AggregatedDpModelNode node = new AggregatedDpModelNode(newitem);
          ((ArrayList<AggregatedDpModelNode>) tableViewer.getInput()).add(index, node);
          tableViewer.refresh();
        }
        tableViewer.setSelection(new StructuredSelection(newitem), false);
        setDirty(true);
      }
    });
    GridData gd_btn_add = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
    gd_btn_add.widthHint = 60;
    gd_btn_add.heightHint = 60;
    btn_add.setLayoutData(gd_btn_add);
    btn_add.setImage(
        CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.ADD));
    Button btn_delete = new Button(composite, SWT.NONE);
    btn_delete.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.dpeditor.widget.button.tooltip.deletedp"));
    btn_delete.addSelectionListener(new SelectionAdapter()
    {
      @SuppressWarnings("unchecked")
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
        Object[] selections = sel.toArray();
        if (selections != null)
        {
          for (Object o : selections)
          {
            ((ArrayList<AggregatedDpModelNode>) tableViewer.getInput()).remove(o);
          }
          tableViewer.remove(selections);
          tableViewer.refresh();
          setDirty(true);
        }
      }
    });
    GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
    gd_btn_delete.heightHint = 60;
    gd_btn_delete.widthHint = 60;
    btn_delete.setLayoutData(gd_btn_delete);
    btn_delete.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
        CommonImagesActivator.DELETE));
    scrolledComposite.setContent(composite);
    scrolledComposite.setMinSize(new Point(200, 200));
    fillAggregatedTable();
    getSite().setSelectionProvider(this);
  }

  @Override
  public void doSave(IProgressMonitor monitor)
  {
    @SuppressWarnings("unchecked")
    ArrayList<AggregatedDpModelNode> items = (ArrayList<AggregatedDpModelNode>) tableViewer.getInput();
    StringBuffer buffer = new StringBuffer();
    AggregatedDPEditorExporter.exportDPs(buffer, items, this.server);
    String symtable = getEditorInput().getDPConfigFile();
    if (!getEditorInput().getFilesystem().isFile(symtable))
    {
      try
      {
        getEditorInput().getFilesystem().addFile(symtable);
      }
      catch (IOException e)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
      }
    }
    if (getEditorInput().getFilesystem().isFile(symtable))
    {
      OutputStream output = null;
      try
      {
        output = getEditorInput().getFilesystem().writeFile(symtable);
        output.write(buffer.toString().getBytes());
        output.flush();
      }
      catch (IOException e)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
      }
      finally
      {
        try
        {
          if (output != null)
          {
            output.close();
          }
        }
        catch (IOException e)
        {
          Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
        }
      }
    }
    this.setDirty(false);
    OPCUAUtil.validateOPCUADriver(getEditorInput().getFilesystem(), getEditorInput().getNode());
  }

  @Override
  public void dispose()
  {
    if (this.server != null)
    {
      this.server.dispose();
    }
    // ClientBrowserUtils.closeClientConnectionView();
    // getSite().getPage().removePartListener(this.partListener);
    super.dispose();
  }

  @Override
  public void doSaveAs()
  {
  }

  @Override
  public void fillTriggerNodes(List<NodeToTrigger> nodesToTrigger)
  {
    // no trigger nodes
  }

  @Override
  public OPCUADPEditorInput getEditorInput()
  {
    return (OPCUADPEditorInput) super.getEditorInput();
  }

  @Override
  public Control getDPControl()
  {
    return this.tableViewer.getControl();
  }

  @Override
  public ISelectionProvider getDPViewer()
  {
    return this.tableViewer;
  }

  public void refreshEditor()
  {
  }

  @Override
  public void onFocusRemoteView()
  {
    AggregatedDriverUtil.openDriverView(getEditorInput().getFilesystem(), getEditorInput().getDriverConfigFile(),
        getEditorInput().getDriverPath());
  }

  @Override
  public ISelection getSelection()
  {
    if (this.currentSelection != null)
    {
      return new StructuredSelection(this.currentSelection);
    }
    return StructuredSelection.EMPTY;
  }

  @Override
  public void init(final IEditorSite site, final IEditorInput input) throws PartInitException
  {
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
    try
    {
      dialog.run(true, false, new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor)
        {
          monitor.beginTask(
              CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.editor.aggregated.dpeditor.init.monitor.opendatapoints"),
              IProgressMonitor.UNKNOWN);
          monitor.subTask(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.aggregated.dpeditor.init.monitor.subtask.init"));
          try
          {
            setSite(site);
            setInput(input);
            setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - "
                + CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
                    "com.bichler.astudio.editor.aggregated.dpeditor.init.monitor.partname"));
            // server path
            String serverpath = new Path(getEditorInput().getFilesystem().getRootPath()).toOSString();
            // information model path
            URL modelParent = new Path(serverpath).append("informationmodel").toFile().toURI().toURL();
            String serverconfigPath = new Path(serverpath).append("serverconfig/server.config.xml").toOSString();
            if (getEditorInput().getFilesystem().isFile(serverconfigPath))
            {
              opcServer = Studio_ResourceManager.getOrNewOPCUAServer(getEditorInput().getServerName(), serverconfigPath,
                  serverpath, serverpath + "/localization", modelParent);
              monitor.subTask(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.editor.aggregated.dpeditor.init.monitor.opendatapoints"));
              AggregatedEditorDevice device = loadOPCUAServerConnection();
              server = openServer(getEditorInput().getFilesystem(), device);
            }
          }
          catch (IOException e1)
          {
            e1.printStackTrace();
          }
          catch (ExecutionException e)
          {
            e.printStackTrace();
          }
          monitor.done();
        }
      });
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

  @Override
  public boolean isDirty()
  {
    return this.dirty;
  }

  @Override
  public boolean isSaveAsAllowed()
  {
    return false;
  }

  public UAServerModelNode openServer(IFileSystem filesystem, AggregatedEditorDevice device)
  {
    UAServerModelNode modelNode = new UAServerModelNode();
    modelNode.setDevice(device);
    if (device != null)
    {
      modelNode.setDisplayName(device.getUaServerName());
      modelNode.setServerUrl(device.getUaServerUri());
      String path_clientConfig = new Path(getEditorInput().getDriverPath()).append("clientconfig")
          .append("clientconfig.xml").toOSString();
      String path_clientCert = new Path(getEditorInput().getDriverPath()).append("cert").append("clientcertificate.der")
          .toOSString();
      String path_clientKey = new Path(getEditorInput().getDriverPath()).append("cert").append("clientkey.pfx")
          .toOSString();
      File configurationFile = null, certFile = null, privKeyFile = null;
      IFileSystem fs = getEditorInput().getFilesystem();
      if (fs.isFile(path_clientConfig) && fs.isFile(path_clientCert) && fs.isFile(path_clientKey))
      {
        // try {
        configurationFile = new File(path_clientConfig);
        certFile = new File(path_clientCert);
        privKeyFile = new File(path_clientKey);
        AggregatedDriverConfigurationType confType = device.getAggregatedConfigType();
        String modelpath = device.getUaInformationModel();
        device.connect2Server(filesystem, configurationFile, certFile, privKeyFile, modelpath);
        // close all input streams
        if (device.isConnected())
        {
          modelNode.setConnected(true);
        }
        // only add reconnect listener if connection to an ua server
        // is ok
        if (device.getUaclient() != null && device.getUaclient().getActiveSession() != null)
        {
          device.getUaclient().getActiveSession().addReconnectListener(new ReconnectListener()
          {
            @Override
            public void onReconnectStarted(ClientSession session)
            {
              UIJob job = new UIJob("")
              {
                @Override
                public IStatus runInUIThread(IProgressMonitor monitor)
                {
                  server.setConnected(false);
                  tableViewer.refresh();
                  server.setReconnection(true);
                  server.getDevice().getUaclient().getActiveSession().reconnect();
                  return Status.OK_STATUS;
                }
              };
              job.schedule();
            }

            @Override
            public void onReconnectFinished(ClientSession session, boolean successfull)
            {
              UIJob job = new UIJob("")
              {
                @Override
                public IStatus runInUIThread(IProgressMonitor monitor)
                {
                  // TODO Auto-generated method
                  // stub
                  server.setConnected(true);
                  if (tableViewer != null && !tableViewer.getControl().isDisposed())
                  {
                    tableViewer.refresh();
                  }
                  return Status.OK_STATUS;
                }
              };
              job.schedule();
            }

            @Override
            public void onConnectionLost(ClientSession session)
            {
              UIJob job = new UIJob("")
              {
                @Override
                public IStatus runInUIThread(IProgressMonitor monitor)
                {
                  // TODO Auto-generated method
                  // stub
                  server.setConnected(false);
                  tableViewer.refresh();
                  server.getDevice().getUaclient().getActiveSession().reconnect();
                  return Status.OK_STATUS;
                }
              };
              job.schedule();
            }

			@Override
			public void onReconnectStopped(ClientSession session) {
				// TODO Auto-generated method stub
				
			}
          });
        }
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // finally {
        // if (configurationFile != null) {
        // try {
        // configurationFile.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        // if (certFile != null) {
        // try {
        // certFile.close();
        // } catch (IOException e) {
        //
        // e.printStackTrace();
        // }
        // }
        // if (privKeyFile != null) {
        // try {
        // privKeyFile.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        // }
      }
    }
    return modelNode;
  }

  @Override
  public void removeSelectionChangedListener(ISelectionChangedListener listener)
  {
    this.listener = null;
  }

  @Override
  public void setFocus()
  {
    super.setFocus();
    this.tableViewer.getTable().setFocus();
  }

  @Override
  public void setDataPointSelection(Object element)
  {
    this.currentSelection = element;
    selectionChanged();
  }

  @Override
  public void setDirty(boolean dirty)
  {
    this.dirty = dirty;
    firePropertyChange(IEditorPart.PROP_DIRTY);
  }

  @Override
  public void setSelection(ISelection selection)
  {
  }

  @Override
  public void onNamespaceChange(NamespaceTableChangeParameter trigger)
  {
  }

  @Override
  public void refreshDatapoints()
  {
    String dpsPath = getEditorInput().getDPConfigFile();
    fill(dpsPath);
  }

  protected void openClientBrowserView()
  {
    AggregatedDriverUtil.openDriverView(getEditorInput().getFilesystem(), getEditorInput().getDriverConfigFile(),
        getEditorInput().getDriverPath());
    // ClientBrowserUtils.openClientConnectionView(server, this.modelRoot);
  }

  private Color getBackgroundForViewerItem(Object element)
  {
    if (element != null)
    {
      if (getDevice() == null)
      {
        return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
      }
      if (!getDevice().isConnected())
      {
        return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
      }
      NodeId targetId = ((AggregatedDpModelNode) element).getDPItem().getTargetNodeId();
      // Object node = getDevice().existsNode(NodeId.NULL);
      if (targetId == null)
      {
        return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
      }
    }
    return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
  }

  protected void selectionChanged()
  {
    listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
  }

  protected AggregatedEditorDevice getDevice()
  {
    if (this.server == null)
      return null;
    return (AggregatedEditorDevice) this.server.getDevice();
  }

  private void fillAggregatedTable()
  {
    /** open file to import */
    String dpsPath = getEditorInput().getDPConfigFile();
    fill(dpsPath);
  }

  private void fill(String dpsPath)
  {
    String[] ns = null;
    if (server != null && server.isConnected())
    {
      DataValue value = getDevice().readAggregatedValue(Identifiers.Server_NamespaceArray, Attributes.Value);
      if (value != null && value.getValue() != null && value.getValue().getValue() != null)
      {
        ns = (String[]) value.getValue().getValue();
      }
    }
    InputStream input = null;
    try
    {
      input = new FileInputStream(dpsPath);
      AggregatedDPEditorImporter importer = new AggregatedDPEditorImporter();
      List<AggregatedDpModelNode> dps = importer.loadDPs(input, opcServer.getServerInstance().getNamespaceUris());
      Node node = null;
      // fill all displaynames from server address space
      for (AggregatedDpModelNode item : dps)
      {
        AggregatedDPItem dp = item.getDPItem();
        node = opcServer.getServerInstance().getAddressSpaceManager().getNodeById(dp.getServerNodeId());
        if (node != null)
        {
          dp.setServerDisplayName(node.getDisplayName().getText());
        }
        // try to set target node id
        if (ns != null)
        {
          for (int i = 0; i < ns.length; i++)
          {
            String ltId = dp.getLoadedTargetNodeId();
            if (ltId == null)
            {
              continue;
            }
            // namespace uri matches
            NodeId id = null;
            try
            {
              id = NodeId.parseNodeId(ltId);
            }
            catch (IllegalArgumentException iae)
            {
            }
            // set id
            if (NodeId.isNull(id))
            {
              boolean validNs = ltId.startsWith(ns[i]);
              if (!validNs)
              {
                continue;
              }
              // get the identifier
              String substring = ltId.substring(ns[i].length(), ltId.length());
              // check if the identifier starts with ';'
              boolean delimitor = substring.startsWith(";");
              if (!delimitor)
              {
                continue;
              }
              id = NodeId.parseNodeId("ns=" + i + substring);
            }
            dp.setTargetNodeId(id);
            break;
          }
        }
      }
      tableViewer.setInput(dps);
    }
    catch (FileNotFoundException e)
    {
      // TODO we have no datapoint file so add an log entry and do nothing
      // else
    }
    finally
    {
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  private AggregatedEditorDevice loadOPCUAServerConnection()
  {
    AggregatedEditorDevice device = new AggregatedEditorDevice();
    String config = getEditorInput().getDriverConfigFile();
    IFileSystem fs = getEditorInput().getFilesystem();
    if (fs != null)
    {
      InputStream stream = null;
      try
      {
        stream = fs.readFile(config);
        device.loadOPCUAServerConnection(stream);
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (stream != null)
        {
          try
          {
            stream.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
    return device;
  }
}
