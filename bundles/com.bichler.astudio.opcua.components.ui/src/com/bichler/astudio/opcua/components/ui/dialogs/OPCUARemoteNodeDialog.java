package com.bichler.astudio.opcua.components.ui.dialogs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUARemoteBrowseContentProvider;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUARemoteBrowserLabelProvider;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUARemoteBrowserProvider;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerContentProvider;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerLabelProvider;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;

public class OPCUARemoteNodeDialog extends Dialog
{
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Table uaservertable;
  private Tree table_1;
  private String formTitle = "";
  private String selectedServer = "";
  private NodeId selectedNodeId = null;
  private String displayName = "";
  private Deque<BrowsePathElement> browsePath = new ArrayDeque<BrowsePathElement>();
  private List<UAServerModelNode> servers = null;
  private UAServerModelNode selServerNode;
  private TreeViewer tableViewer_1;
  private Button btn_OK;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public OPCUARemoteNodeDialog(Shell parentShell)
  {
    super(parentShell);
    setShellStyle(SWT.CLOSE | SWT.RESIZE);
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    Form frmNewForm = formToolkit.createForm(container);
    formToolkit.paintBordersFor(frmNewForm);
    frmNewForm.setText(formTitle);
    formToolkit.decorateFormHeading(frmNewForm);
    frmNewForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
    SashForm sashForm = new SashForm(frmNewForm.getBody(), SWT.NONE);
    formToolkit.adapt(sashForm);
    formToolkit.paintBordersFor(sashForm);
    TableViewer tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
    uaservertable = tableViewer.getTable();
    formToolkit.paintBordersFor(uaservertable);
    tableViewer.setContentProvider(new UAServerContentProvider());
    tableViewer.setLabelProvider(new UAServerLabelProvider());
    tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        Object element = ((StructuredSelection) event.getSelection()).getFirstElement();
        if (element == null)
        {
          return;
        }
        if (!(element instanceof UAServerModelNode))
        {
          return;
        }
        /** cast element to node */
        selServerNode = (UAServerModelNode) element;
        OPCUARemoteBrowseContentProvider content = new OPCUARemoteBrowseContentProvider(selServerNode.getDisplayName(),
            null);
        selectedServer = selServerNode.getDisplayName();
        selectedNodeId = null;
        browsePath.clear();
        content.setServer(selServerNode);
        // TODO Auto-generated method stub
        tableViewer_1.setContentProvider(content);
        OPCUARemoteBrowserLabelProvider label = new OPCUARemoteBrowserLabelProvider(tableViewer_1);
        label.setServer(selServerNode);
        tableViewer_1.setLabelProvider(label);
        OPCUARemoteBrowserProvider input = new OPCUARemoteBrowserProvider(selServerNode);
        tableViewer_1.setInput(input.getBrowser());
      }
    });
    tableViewer_1 = new TreeViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
    table_1 = tableViewer_1.getTree();
    formToolkit.paintBordersFor(table_1);
    sashForm.setWeights(new int[] { 1, 1 });
    if (selServerNode == null && servers.size() > 0)
    {
      selServerNode = servers.get(0);
      selectedServer = selServerNode.getDisplayName();
    }
    if (selServerNode != null)
    {
      OPCUARemoteBrowseContentProvider content = new OPCUARemoteBrowseContentProvider(selServerNode.getDisplayName(),
          null);
      content.setServer(selServerNode);
      tableViewer_1.setContentProvider(content);
      OPCUARemoteBrowserLabelProvider label = new OPCUARemoteBrowserLabelProvider(tableViewer_1);
      label.setServer(selServerNode);
      tableViewer_1.setLabelProvider(label);
      OPCUARemoteBrowserProvider input = new OPCUARemoteBrowserProvider(selServerNode);
      tableViewer_1.setInput(input.getBrowser());
      tableViewer_1.addSelectionChangedListener(new ISelectionChangedListener()
      {
        @Override
        public void selectionChanged(SelectionChangedEvent event)
        {
          // set the selected node to return
          Object element = ((StructuredSelection) event.getSelection()).getFirstElement();
          if (element == null)
          {
            return;
          }
          if (!(element instanceof OPCRemoteTreeViewerItem))
          {
            return;
          }
          OPCRemoteTreeViewerItem item = (OPCRemoteTreeViewerItem) element;
          selectedNodeId = item.getNodeId();
          // get the full browsepath from server objects to
          // the
          // selected element
          browsePath = OPCUABrowseUtils.getFullBrowsePath(selectedNodeId, selServerNode.getDevice().getUaclient(),
              Identifiers.ObjectsFolder);
          displayName = item.getDisplayName();
          if (btn_OK != null)
            btn_OK.setEnabled(true);
          // browsePath =
          // Comet_Utils.getFullBrowsePath(selectedNodeId,
          // inter,
          // Identifiers.ObjectsFolder);
        }
      });
    }
    if (servers == null)
    {
      tableViewer.getTable().setVisible(false);
    }
    else
    {
      tableViewer.setInput(servers);
    }
    this.fillTree();
    return container;
  }

  private void fillTree()
  {
    if (selServerNode != null && selectedNodeId != null)
    {
      browsePath = OPCUABrowseUtils.getFullBrowsePath(selectedNodeId, selServerNode.getDevice().getUaclient(),
          Identifiers.ObjectsFolder);
      // open tree
      TreeItem[] items = tableViewer_1.getTree().getItems();
      if (items != null)
      {
        for (BrowsePathElement element : browsePath)
        {
          if (element.getId().equals(Identifiers.ObjectsFolder))
            continue;
          for (TreeItem item : items)
          {
            if (item.getData() instanceof OPCRemoteTreeViewerItem)
            {
              OPCRemoteTreeViewerItem it = (OPCRemoteTreeViewerItem) item.getData();
              if (it.getNodeId().equals(element.getId()))
              {
                item.setExpanded(true);
                tableViewer_1.refresh();
                items = item.getItems();
                StructuredSelection selection = new StructuredSelection(it);
                tableViewer_1.setSelection(selection);
                break;
              }
            }
          }
        }
      }
    }
  }

  public UAServerModelNode getSelServerNode()
  {
    return selServerNode;
  }

  public void setSelServerNode(UAServerModelNode selServerNode)
  {
    this.selServerNode = selServerNode;
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    btn_OK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    if (selServerNode != null)
    {
      if (selServerNode.isConnected())
      {
        btn_OK.setEnabled(true);
      }
      else
      {
        btn_OK.setEnabled(false);
      }
    }
    else
      btn_OK.setEnabled(false);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize()
  {
    return new Point(450, 300);
  }

  public NodeId getSelectedNodeId()
  {
    return selectedNodeId;
  }

  public void setSelectedNodeId(NodeId selectedNodeId)
  {
    this.selectedNodeId = selectedNodeId;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  public Deque<BrowsePathElement> getBrowsePath()
  {
    return browsePath;
  }

  public void setBrowsePath(Deque<BrowsePathElement> browsePath)
  {
    this.browsePath = browsePath;
  }

  public String getFormTitle()
  {
    return formTitle;
  }

  public void setFormTitle(String formTitle)
  {
    this.formTitle = formTitle;
  }

  public String getSelectedServer()
  {
    return selectedServer;
  }

  public void setSelectedServer(String selectedServer)
  {
    this.selectedServer = selectedServer;
  }

  public List<UAServerModelNode> getServers()
  {
    return servers;
  }

  public void setServers(List<UAServerModelNode> servers)
  {
    this.servers = servers;
  }
}
