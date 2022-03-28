package com.bichler.astudio.editor.calculation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.AbstractOPCDPDriverViewLinkEditorPart;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.driver.calculation.CalculationDP;
import com.bichler.astudio.editor.calculation.model.CalculationDPEditorImporter;
import com.bichler.astudio.editor.calculation.model.CalculationModelNode;

import opc.sdk.server.core.UAServerApplicationInstance;
import opc.server.hbserver.OPCUADriverConnection;

public class CalculationDPEditor extends AbstractOPCDPDriverViewLinkEditorPart
{
  public final static String ID = "com.bichler.astudio.editor.calculation.CalculationDPEditor"; //$NON-NLS-1$
  // private TableViewer tableViewer = null;
  private List<CalculationModelNode> calcitems = new ArrayList<>();
  private List<CalculationComposite> composits = new ArrayList<>();

  @Override
  public OPCUADPEditorInput getEditorInput()
  {
    return (OPCUADPEditorInput) super.getEditorInput();
  }
  private boolean dirty = false;
  private UAServerApplicationInstance opcServer;
  private Composite composite_1;
  private ScrolledComposite scrolledComposite_1;
  private Button btn_add;
  private Button btn_delete;
  private ScrolledComposite scrolledComposite;

  public CalculationDPEditor()
  {
    // load the internal server addressspace
    // opcServer = new OPC_DriverServer();
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
    this.scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    Composite composite = new Composite(scrolledComposite, SWT.NONE);
    composite.setLayout(new GridLayout(3, false));
    scrolledComposite_1 = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
    scrolledComposite_1.setExpandHorizontal(true);
    scrolledComposite_1.setExpandVertical(true);
    composite_1 = new Composite(scrolledComposite_1, SWT.NONE);
    composite_1.setLayout(new GridLayout(1, false));
    btn_add = new Button(composite, SWT.NONE);
    btn_add.setToolTipText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.editor.dp.add"));
    GridData gd_btn_add = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
    gd_btn_add.widthHint = 60;
    gd_btn_add.heightHint = 60;
    btn_add.setLayoutData(gd_btn_add);
    btn_add.setImage(CalculationSharedImages.getImage(CalculationSharedImages.ICON_ADD));
    btn_delete = new Button(composite, SWT.NONE);
    btn_delete.setToolTipText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.editor.dp.delete"));
    GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
    gd_btn_delete.heightHint = 60;
    gd_btn_delete.widthHint = 60;
    btn_delete.setLayoutData(gd_btn_delete);
    btn_delete.setImage(CalculationSharedImages.getImage(CalculationSharedImages.ICON_DELETE));
    new Label(composite, SWT.NONE);
    scrolledComposite.setContent(composite);
    scrolledComposite.setMinSize(new Point(200, 200));
    this.fillTree();
    setHandler();
    setDirty(false);
    getSite().setSelectionProvider(this);
  }

  private void setHandler()
  {
    btn_add.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        CalculationModelNode dp = new CalculationModelNode();
        calcitems.add(dp);
        refillTree();
        setDirty(true);
      }
    });
    btn_delete.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DeleteCalculationItem dialog = new DeleteCalculationItem(getSite().getShell(), calcitems);
        if (dialog.open() == Dialog.OK)
        {
          calcitems.remove(dialog.getIndex());
          // TODO update all composites
          refillTree();
        }
        setDirty(true);
      }
    });
  }

  @Override
  public void setFocus()
  {
    // Set the focus
    super.setFocus();
    this.scrolledComposite.setFocus();
  }

  private void refillTree()
  {
    // clears all previews created composits
    composits.clear();
    composite_1 = new Composite(scrolledComposite_1, SWT.NONE);
    composite_1.setLayout(new GridLayout(1, false));
    for (CalculationModelNode item : this.calcitems)
    {
      addCalculationItem(item);
    }
    scrolledComposite_1.setContent(composite_1);
    scrolledComposite_1.setMinSize(composite_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    composite_1.layout();
  }

  private void addCalculationItem(CalculationModelNode item)
  {
    CalculationComposite composite_2 = new CalculationComposite(composite_1, SWT.BORDER, this, item, this.opcServer);
    composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    composits.add(composite_2);
  }

  private void addCalculationDP(CalculationDP dp)
  {
    // recalculate milliseconds
    long newTimeout = dp.getTimeout() / 1000000;
    dp.setTimeout(newTimeout);
    CalculationModelNode item = new CalculationModelNode();
    item.setDP(dp);
    this.calcitems.add(item);
    addCalculationItem(item);
  }

  private void fillTree()
  {
    /** open file to import */
    String dpsPath = getEditorInput().getDPConfigFile();
    InputStream is = null;
    OPCUADriverConnection dr = new OPCUADriverConnection(opcServer.getServerInstance());
    // opcServer.setDriverConnection(dr);
    ComDRVManager.getDRVManager().getResourceManager().setDriverConnection(dr);
    // clears all previews created composits
    this.composits.clear();
    this.calcitems.clear();
    if (getEditorInput().getFilesystem().isFile(dpsPath))
    {
      try
      {
        is = new FileInputStream(dpsPath);
        CalculationDPEditorImporter importer = new CalculationDPEditorImporter();
        List<CalculationDP> calcDps = importer.loadDPs(is, opcServer.getServerInstance().getNamespaceUris(), null, -1);
        for (CalculationDP item : calcDps)
        {
          addCalculationDP(item);
        }
        scrolledComposite_1.setContent(composite_1);
        scrolledComposite_1.setMinSize(composite_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (is != null)
        {
          try
          {
            is.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @Override
  public void doSave(IProgressMonitor monitor)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    buffer.append("<CalculationConfiguration>\n");
    buffer.append("  <calculations>\n");
    for (CalculationComposite comp : this.composits)
    {
      buffer.append(comp.toString());
    }
    buffer.append("  </calculations>\n");
    buffer.append("</CalculationConfiguration>\n");
    OutputStream output = null;
    try
    {
      String symtable = getEditorInput().getDPConfigFile();
      if (!getEditorInput().getFilesystem().isFile(symtable))
      {
        getEditorInput().getFilesystem().addFile(symtable);
      }
      if (getEditorInput().getFilesystem().isFile(symtable))
      {
        output = getEditorInput().getFilesystem().writeFile(symtable);
        output.write(buffer.toString().getBytes());
        output.flush();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (output != null)
      {
        try
        {
          output.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    this.setDirty(false);
    OPCUAUtil.validateOPCUADriver(getEditorInput().getFilesystem(), getEditorInput().getNode());
  }

  @Override
  public void doSaveAs()
  {
    // Do the Save As operation
  }

  @Override
  public void init(final IEditorSite site, final IEditorInput input) throws PartInitException
  {
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
    try
    {
      dialog.run(true, true, new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor)
        {
          monitor.beginTask(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.calculation.editor.dp.opendps"), IProgressMonitor.UNKNOWN);
          try
          {
            setSite(site);
            setInput(input);
            setPartName(getEditorInput().getServerName() + " - "
                + CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
                    "com.bichler.astudio.editor.calculation.editor.dp.add"));
            try
            {
              String serverpath = new Path(getEditorInput().getFilesystem().getRootPath()).toOSString();
              String serverConfigPath = new Path(serverpath).append("serverconfig").append("server.config.xml")
                  .toOSString();
              URL modelParent = new Path(serverpath).append("informationmodel").toFile().toURI().toURL();
              if (getEditorInput().getFilesystem().isFile(serverConfigPath))
              {
                opcServer = Studio_ResourceManager.getOrNewOPCUAServer(getEditorInput().getServerName(),
                    serverConfigPath, serverpath, new Path(serverpath).append("localization").toOSString(),
                    modelParent);
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
          }
          finally
          {
            monitor.done();
          }
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
    return dirty;
  }

  public void setDirty(boolean dirty)
  {
    this.dirty = dirty;
    firePropertyChange(IEditorPart.PROP_DIRTY);
  }

  @Override
  public boolean isSaveAsAllowed()
  {
    return false;
  }

  public String removeEscapes(String builder)
  {
    return builder.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
  }


  /**
   * class to validate integer input cells.
   * 
   * @author applemc207da
   * 
   */
  public class IntegerCellEditor extends TextCellEditor
  {
    public IntegerCellEditor(Composite composite)
    {
      super(composite);
      setValidator(new ICellEditorValidator()
      {
        public String isValid(Object object)
        {
          if (object instanceof Integer)
          {
            return null;
          }
          else
          {
            String string = (String) object;
            try
            {
              Integer.parseInt(string);
              return null;
            }
            catch (NumberFormatException exception)
            {
              return exception.getMessage();
            }
          }
        }
      });
    }

    @Override
    public Object doGetValue()
    {
      return Integer.parseInt((String) super.doGetValue());
    }

    @Override
    public void doSetValue(Object value)
    {
      super.doSetValue(value.toString());
    }
  }
  /**
   * Selection change
   */
  ISelectionChangedListener listener = null;
  private Object currentSelection;
  private Object propertyPage;

  @Override
  public void addSelectionChangedListener(ISelectionChangedListener listener)
  {
    this.listener = listener;
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
  public void removeSelectionChangedListener(ISelectionChangedListener listener)
  {
    listener = null;
  }

  @Override
  public void setSelection(ISelection selection)
  {
  }

  @Override
  public <T> T getAdapter(Class<T> adapter)
  {
    if (adapter == IPropertySheetPage.class)
    {
      if (propertyPage == null)
      {
        propertyPage = new PropertySheetPage();
      }
      return (T) propertyPage;
    }
    return super.getAdapter(adapter);
  }

  public void setDataPointSelection(Object obj)
  {
    this.currentSelection = obj;
    selectionChanged();
  }

  protected void selectionChanged()
  {
    listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
  }

  @Override
  public void onNamespaceChange(NamespaceTableChangeParameter trigger)
  {
    //
  }

  @Override
  public void refreshDatapoints()
  {
    composite_1.dispose();
    composite_1 = new Composite(scrolledComposite_1, SWT.NONE);
    composite_1.setLayout(new GridLayout(1, false));
    fillTree();
  }

  @Override
  public void fillTriggerNodes(List<NodeToTrigger> nodesToTrigger)
  {
    // no trigger nodes
  }

  @Override
  public Control getDPControl()
  {
    return this.composite_1;
  }

  @Override
  public ISelectionProvider getDPViewer()
  {
    return this;
  }

  @Override
  public void onFocusRemoteView()
  {
    DriverBrowserUtil.openEmptyDriverModelView();
  }

  public void refreshPropertyPage()
  {
    if (this.propertyPage == null)
    {
      return;
    }
    ((PropertySheetPage) this.propertyPage).refresh();
  }
}
