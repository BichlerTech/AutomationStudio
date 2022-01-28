package com.bichler.astudio.editor.allenbradley.dialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.editor.allenbradley.AllenBradleyDPEditor;
import com.bichler.astudio.editor.allenbradley.AllenBradleyModelLabelProvider;
import com.bichler.astudio.editor.allenbradley.AllenBradleySharedImages;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.editor.allenbradley.model.AllenBradleyNodeFactory;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.filesystem.IFileSystem;

public class AllenBradleyValidationDialog extends Dialog
{
  // private DecimalFormat format = new DecimalFormat("0.0");
  private String importPath = "";
  // private String symbolName = "";
  // private String symbolAddress = "";
  private IFileSystem filesystem = null;


  // private int maxNodeId = 0;
  class SiemensImportDialogContentProvider implements ITreeContentProvider
  {
    @Override
    public void dispose()
    {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    @Override
    public Object[] getElements(Object inputElement)
    {
      return getChildren(inputElement);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object[] getChildren(Object parentElement)
    {
      if (parentElement instanceof List)
      {
        return ((List) parentElement).toArray();
      }
      if (parentElement instanceof AbstractAllenBradleyNode)
      {
        return ((AbstractAllenBradleyNode) parentElement).getMembers();
      }
      return new Object[0];
    }

    @Override
    public Object getParent(Object element)
    {
      return null;
    }

    @Override
    public boolean hasChildren(Object element)
    {
      return this.getChildren(element).length > 0;
    }
  }

  /**
   */
  @Override
  protected void okPressed()
  {
    TableViewer tv = null;
    IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    if (activeEditor instanceof AllenBradleyDPEditor)
    {
      tv = ((AllenBradleyDPEditor) activeEditor).getTableViewer();
    }
    else
    {
      return;
    }
    boolean yes = MessageDialog.openQuestion(getShell(), "Import", "Wollen Sie den Index der Datenpunkte verändern!");
    if (!yes)
    {
      return;
    }
    @SuppressWarnings("unchecked")
    List<AllenBradleyEntryModelNode> editorModel = (List<AllenBradleyEntryModelNode>) tv.getInput();
    for (AllenBradleyEntryModelNode semn : editorModel)
    {
      if (semn.getIndexNew() != null && !semn.getIndexNew().isEmpty())
      {
        try
        {
          // semn.setIndex(Float.parseFloat(semn.getIndexNew()));
          semn.setIndexNew("");
        }
        catch (NumberFormatException nfe)
        {
          nfe.printStackTrace();
        }
      }
      if (semn.getDataTypeNew() != null && !semn.getDataTypeNew().isEmpty())
      {
        semn.setDataType(semn.getDataTypeNew());
        semn.setDataTypeNew("");
      }
    }
    tv.refresh(true);
    super.okPressed();
  }
  // private void addChildren(List<AbstractSiemensNode> nodes,
  // AbstractSiemensNode item) {
  // AbstractSiemensNode newItem = null;
  //
  // if(item instanceof SiemensStructNode){
  // newItem = new Siemens
  // }
  //
  // if (item.isActive()) {
  //
  // newItem.setId(++maxNodeId);
  // newItem.setActive(true);
  // newItem.setAddress(item.getAddress());
  // newItem.setAddressType(item.getAddressType());
  // newItem.setDataType(item.getDataType());
  // newItem.setDescription(item.getDescription());
  // newItem.setSymbolName(item.getSymbolName());
  // newItem.setLabelImage(item.getLabelImage());
  //
  // nodes.add(newItem);
  //
  // /** */
  // for (AbstractSiemensNode node : item.getChildren()) {
  // this.addChildren(newItem.getChildren(), node);
  // }
  // }
  // }
  private TreeViewer treeViewer;
  private static final String[] FILTER_NAME = { "Siemens Datapoints Files (*.txt)",
      "Siemens Datapoints Files (*.csv)" };
  private static final String[] FILTER_EXTS = { "*.txt", "*.csv" };
  private Text textAddress;
  private Combo comboAddressType;
  private AllenBradleyNodeFactory nodeFactory;
  private Map<String, AllenBradleyEntryModelNode> inputModel;
  private List<AllenBradleyEntryModelNode> listModel;
  private AllenBradleyDBResourceManager structManager;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public AllenBradleyValidationDialog(Shell parentShell, AllenBradleyDBResourceManager structManager)
  {
    super(parentShell);
    setShellStyle(SWT.BORDER | SWT.MIN | SWT.MAX | SWT.RESIZE);
    this.structManager = structManager;
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
    container.setLayout(new GridLayout(1, false));
    Composite composite_1 = new Composite(container, SWT.NONE);
    composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    composite_1.setLayout(new GridLayout(2, false));
    Label lblAddressType = new Label(composite_1, SWT.NONE);
    lblAddressType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAddressType.setText("address type");
    this.comboAddressType = new Combo(composite_1, SWT.READ_ONLY);
    comboAddressType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    Label lblAddress = new Label(composite_1, SWT.NONE);
    lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAddress.setText("address");
    this.textAddress = new Text(composite_1, SWT.BORDER);
    textAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    Composite composite = new Composite(scrolledComposite, SWT.NONE);
    composite.setLayout(new FillLayout(SWT.VERTICAL));
    treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
    Tree tree = treeViewer.getTree();
    tree.setLinesVisible(true);
    tree.setHeaderVisible(true);
    treeViewer.setLabelProvider(new AllenBradleyModelLabelProvider());
    treeViewer.setContentProvider(new SiemensImportDialogContentProvider());
    // TreeViewerColumn trvclmn_address_old = new
    // TreeViewerColumn(treeViewer,
    // SWT.NONE);
    // TreeColumn trclmn_address = trvclmn_address_old.getColumn();
    // trclmn_address.setWidth(100);
    // trclmn_address.setText("index alt");
    // trvclmn_address_old.setLabelProvider(new ColumnLabelProvider() {
    // @Override
    // public String getText(Object element) {
    // return "" + ((allenbradleyEntryModelNode) element).getIndex();
    // }
    // });
    // trvclmn_address_old.setEditingSupport(new EditingSupport(treeViewer)
    // {
    //
    // protected boolean canEdit(Object element) {
    // return true;
    // }
    //
    // protected CellEditor getCellEditor(Object element) {
    // return new TextCellEditor(treeViewer.getTree());
    // }
    //
    // protected Object getValue(Object element) {
    // return "" + ((allenbradleyEntryModelNode) element).getIndex();
    // }
    //
    // protected void setValue(Object element, Object value) {
    // if ((((allenbradleyEntryModelNode) element).getIndex() + "")
    // .compareTo(value.toString()) == 0) {
    // /** do nothing */
    // return;
    // }
    // try {
    // ((allenbradleyEntryModelNode) element).setIndex((Float
    // .parseFloat(value.toString())));
    // } catch (NumberFormatException e) {
    // // TODO: handle exception
    // e.printStackTrace();
    // }
    // treeViewer.refresh(element);
    // }
    //
    // });
    TreeViewerColumn trvclmn_address_new = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_address_new = trvclmn_address_new.getColumn();
    trclmn_address_new.setWidth(103);
    trclmn_address_new.setText("index neu");
    trvclmn_address_new.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return ((AllenBradleyEntryModelNode) element).getIndexNew();
      }
    });
    trvclmn_address_new.setEditingSupport(new EditingSupport(treeViewer)
    {
      protected boolean canEdit(Object element)
      {
        return true;
      }

      protected CellEditor getCellEditor(Object element)
      {
        return new TextCellEditor(treeViewer.getTree());
      }

      protected Object getValue(Object element)
      {
        return "" + ((AllenBradleyEntryModelNode) element).getIndexNew();
      }

      protected void setValue(Object element, Object value)
      {
        if ((((AllenBradleyEntryModelNode) element).getIndexNew() + "").compareTo(value.toString()) == 0)
        {
          /** do nothing */
          return;
        }
        try
        {
          ((AllenBradleyEntryModelNode) element).setIndexNew("" + (Float.parseFloat(value.toString())));
        }
        catch (NumberFormatException e)
        {
          // TODO: handle exception
          e.printStackTrace();
        }
        treeViewer.refresh(element);
      }
    });
    TreeViewerColumn trvclmn_symbolname = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_symbolname = trvclmn_symbolname.getColumn();
    trclmn_symbolname.setWidth(167);
    trclmn_symbolname.setText("symbol name");
    trvclmn_symbolname.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Image getImage(Object element)
      {
        return ((AllenBradleyEntryModelNode) element).getLabelImage();
      }

      @Override
      public String getText(Object element)
      {
        return ((AllenBradleyEntryModelNode) element).getSymbolName();
      }
    });
    trvclmn_symbolname.setEditingSupport(new EditingSupport(treeViewer)
    {
      protected boolean canEdit(Object element)
      {
        return true;
      }

      protected CellEditor getCellEditor(Object element)
      {
        return new TextCellEditor(treeViewer.getTree());
      }

      protected Object getValue(Object element)
      {
        return ((AllenBradleyEntryModelNode) element).getSymbolName();
      }

      protected void setValue(Object element, Object value)
      {
        if (((AllenBradleyEntryModelNode) element).getSymbolName().compareTo(value.toString()) == 0)
        {
          return;
        }
        ((AllenBradleyEntryModelNode) element).setSymbolName(value.toString());
        treeViewer.refresh(element);
      }
    });
    TreeViewerColumn trvclmn_datatype = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_datatype = trvclmn_datatype.getColumn();
    trclmn_datatype.setWidth(82);
    trclmn_datatype.setText("datatype_alt");
    trvclmn_datatype.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element == null || ((AllenBradleyEntryModelNode) element).getDataType() == null)
        {
          return "";
        }
        return ((AllenBradleyEntryModelNode) element).getDataType();
      }
    });
    TreeViewerColumn trvclmn_datatypeNew = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmnDatatypenew = trvclmn_datatypeNew.getColumn();
    trclmnDatatypenew.setWidth(92);
    trclmnDatatypenew.setText("datatype_neu");
    trvclmn_datatypeNew.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element == null || ((AllenBradleyEntryModelNode) element).getDataTypeNew() == null)
        {
          return "";
        }
        return ((AllenBradleyEntryModelNode) element).getDataTypeNew();
      }
    });
    TreeViewerColumn trvclmn_description = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_description = trvclmn_description.getColumn();
    trclmn_description.setWidth(192);
    trclmn_description.setText("description");
    trvclmn_description.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return ((AllenBradleyEntryModelNode) element).getDname();
      }
    });
    trvclmn_description.setEditingSupport(new EditingSupport(treeViewer)
    {
      protected boolean canEdit(Object element)
      {
        return true;
      }

      protected CellEditor getCellEditor(Object element)
      {
        return new TextCellEditor(treeViewer.getTree());
      }

      protected Object getValue(Object element)
      {
        return ((AllenBradleyEntryModelNode) element).getDname();
      }

      protected void setValue(Object element, Object value)
      {
        if (((AllenBradleyEntryModelNode) element).getDname().compareTo(value.toString()) == 0)
        {
          /** try to parse it to float */
          return;
        }
        try
        {
          Float.parseFloat(value.toString());
        }
        catch (NumberFormatException ex)
        {
          treeViewer.refresh(element);
          return;
        }
        ((AllenBradleyEntryModelNode) element).setDescription(value.toString());
        treeViewer.refresh(element);
      }
    });
    this.nodeFactory = new AllenBradleyNodeFactory();
    fillTableInput();
    scrolledComposite.setContent(composite);
    scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    // this.registerPlatformTools();
    return container;
  }

  private void fillTableInput()
  {
    if (this.listModel != null)
    {
      // List<SiemensEntryModelNode> model =new ArrayList<>( );
      this.treeViewer.setInput(this.listModel);
    }
  }

  /**
   * Context menu & registrate treeviewer as selectionprovider
   */
  // private void registerPlatformTools() {
  //
  // MenuManager menuManager = new MenuManager();
  // final Menu menu = menuManager.createContextMenu(treeViewer.getTree());
  // // Set the MenuManager
  //
  // /**
  // * As menu manager
  // */
  // treeViewer.getTree().setMenu(menu);
  //
  // /**
  // * defined in plugin xml
  // */
  //
  // IMenuService mSvc = (IMenuService) PlatformUI.getWorkbench()
  // .getActiveWorkbenchWindow().getService(IMenuService.class);
  //
  // mSvc.populateContributionManager(menuManager,
  // "siemensdpeditor.table.contextmenu");
  //
  // IWorkbenchPartSite site = PlatformUI.getWorkbench()
  // .getActiveWorkbenchWindow().getActivePage().getActivePart()
  // .getSite();
  //
  // site.registerContextMenu(menuManager, treeViewer);
  // site.setSelectionProvider(treeViewer);
  // }
  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createSelectAllButton(parent);
    createDeselectAllButton(parent);
    createImportSymTabButton(parent);
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  private void createSelectAllButton(Composite parent)
  {
    Button selectAll = createButton(parent, 4096, "Select All", true);
    selectAll.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
        Object input = treeViewer.getInput();
        if (input == null)
        {
          return;
        }
        if (input instanceof AbstractAllenBradleyNode)
        {
          ((AbstractAllenBradleyNode) input).setActiveAll(true);
        }
        treeViewer.refresh();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
      }
    });
  }

  private void createDeselectAllButton(Composite parent)
  {
    Button deselectAll = createButton(parent, 4096, "Deselect All", true);
    deselectAll.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
        Object input = treeViewer.getInput();
        if (input == null)
        {
          return;
        }
        if (input instanceof AbstractAllenBradleyNode)
        {
          ((AbstractAllenBradleyNode) input).setActiveAll(false);
        }
        treeViewer.refresh();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
      }
    });
  }

  private void fillValidationInput(AbstractAllenBradleyNode root)
  {
    // String symbolName = root.getName();
    // String symbolAddress = symbolName.replaceFirst(
    // SiemensAreaCode.DB.name(), "").trim();
    comboAddressType.removeAll();
    // comboAddressType.add(SiemensAreaCode.DB.name());
    comboAddressType.select(0);
    // textAddress.setText(symbolAddress);
    this.treeViewer.refresh(true);
  }

  private void validateModelWithDB(AbstractAllenBradleyNode[] children)
  {
    // fill newindex value mapped with symbolname
    if (children == null)
    {
      return;
    }
    for (AbstractAllenBradleyNode c : children)
    {
      AllenBradleyEntryModelNode dbsn = this.inputModel.get(c.getName());
      if (dbsn != null)
      {
        dbsn.setDataTypeNew(c.getDataType());
      }
      if (c.getMembers() != null && c.getMembers().length > 0)
      {
        validateModelWithDB(c.getMembers());
      }
    }
  }

  private void createImportSymTabButton(Composite parent)
  {
    Button importdb = createButton(parent, 2048, "", true);
    importdb.setToolTipText("import dp file");
    importdb.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_IMPORT));
    importdb.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        /** open file to import */
        FileDialog dialog = new FileDialog(getShell());
        dialog.setFilterNames(FILTER_NAME);
        dialog.setFilterExtensions(FILTER_EXTS);
        final String csv = dialog.open();
        if (csv == null)
        {
          /** do nothing, because the import command was canceled */
          return;
        }
        Job importJob = new Job("Importiere Datenbaustein")
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            monitor.beginTask("Importiere...", 10);
            if (filesystem.isFile(csv))
            {
              monitor.worked(2);
              InputStream symboTableFile = null;
              try
              {
                symboTableFile = filesystem.readFile(csv);
                monitor.worked(1);
                final AbstractAllenBradleyNode validationRoot = nodeFactory
                    .parseCSV(new BufferedReader(new InputStreamReader(symboTableFile)), structManager);
                validateModelWithDB(validationRoot.getMembers());
                monitor.worked(6);
                Display.getDefault().syncExec(new Runnable()
                {
                  @Override
                  public void run()
                  {
                    fillValidationInput(validationRoot);
                  }
                });
              }
              catch (FileNotFoundException e1)
              {
                e1.printStackTrace();
              }
              catch (IOException e2)
              {
                e2.printStackTrace();
              }
              finally
              {
                if (symboTableFile != null)
                {
                  try
                  {
                    symboTableFile.close();
                  }
                  catch (IOException e1)
                  {
                    e1.printStackTrace();
                  }
                }
              }
            }
            monitor.worked(1);
            monitor.done();
            return Status.OK_STATUS;
          }
        };
        importJob.setUser(true);
        importJob.setSystem(false);
        importJob.schedule();
      }
    });
    Button importStructUdt = createButton(parent, 4098, "", true);
    importStructUdt.setToolTipText("import structure file");
    importStructUdt.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_IMPORT));
    importStructUdt.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        final String directory = dialog.open();
        if (directory == null || directory.isEmpty())
        {
          /** do nothing, because the import command was canceled */
          return;
        }
        Job importJob = new Job("Importiere Strukturen")
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            if (filesystem.isDir(directory))
            {
              File directoryFile = new File(directory);
              String[] files = directoryFile.list(new FilenameFilter()
              {
                @Override
                public boolean accept(File file, String filename)
                {
                  if (filename != null)
                  {
                    int index = filename.lastIndexOf(".");
                    String extension = filename.substring(index + 1);
                    if ("csv".equalsIgnoreCase(extension))
                    {
                      return true;
                    }
                  }
                  return false;
                }
              });
              if (files != null)
              {
                monitor.beginTask("Importiere...", files.length);
                // cache structures
                Map<String, AbstractAllenBradleyNode> structures = new HashMap<String, AbstractAllenBradleyNode>();
                for (String file : files)
                {
                  String csv = directory + File.separator + file;
                  if (filesystem.isFile(csv))
                  {
                    InputStream symboTableFile = null;
                    try
                    {
                      symboTableFile = filesystem.readFile(csv);
                      List<AbstractAllenBradleyNode> structs = nodeFactory.parseL5X(symboTableFile, structManager);
                      for (AbstractAllenBradleyNode struct : structs)
                      {
                        structures.put(struct.getName(), struct);
                      }
                      monitor.worked(1);
                    }
                    catch (FileNotFoundException e1)
                    {
                      e1.printStackTrace();
                    }
                    catch (IOException e2)
                    {
                      e2.printStackTrace();
                    }
                    finally
                    {
                      if (symboTableFile != null)
                      {
                        try
                        {
                          symboTableFile.close();
                        }
                        catch (IOException e1)
                        {
                          e1.printStackTrace();
                        }
                      }
                    }
                  }
                }
                structManager.addStructures(structures);
              }
            }
            monitor.done();
            return Status.OK_STATUS;
          }
        };
        importJob.setUser(true);
        importJob.schedule();
      }
    });
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize()
  {
    return new Point(670, 401);
  }

  public String getImportPath()
  {
    return importPath;
  }

  public void setImportPath(String importPath)
  {
    this.importPath = importPath;
  }

  public String removeEscapes(String builder)
  {
    return builder.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
  }

  public IFileSystem getFilesystem()
  {
    return filesystem;
  }

  public void setFilesystem(IFileSystem filesystem)
  {
    this.filesystem = filesystem;
  }

  public void setModel(List<AllenBradleyEntryModelNode> arrayList, Map<String, AllenBradleyEntryModelNode> inputModel)
  {
    this.listModel = arrayList;
    this.inputModel = inputModel;
  }
}
