package com.bichler.astudio.editor.allenbradley.wizard;

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
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;

import com.bichler.astudio.editor.allenbradley.AllenBradleyActivator;
import com.bichler.astudio.editor.allenbradley.AllenBradleyModelLabelProvider;
import com.bichler.astudio.editor.allenbradley.AllenBradleyModelTreeContentProvider;
import com.bichler.astudio.editor.allenbradley.AllenBradleySharedImages;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.editor.allenbradley.model.AllenBradleyArrayNode;
import com.bichler.astudio.editor.allenbradley.model.AllenBradleyNodeFactory;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class AllenBradleyImportWizardPage extends WizardPage
{
  private static final String EXT_CSV = "csv";
  public static final String EXT_L5X = "L5X";
  private static final String[] FILTER_DATAPOINT = { "allenbradley Datapoint File (*." + EXT_CSV + ")" };
  // private static final String[] FILTER_STRUCTURES = { "allenbradley Structure
  // File
  // (*."
  // + EXT_L5X + ")" };
  private static final String[] FILTER_EXT_CSV = { "*." + EXT_CSV };
  private Combo comboAddressType;
  private Text textAddress;
  private TreeViewer treeViewer;
  private IFileSystem filesystem = null;
  private AllenBradleyNodeFactory nodeFactory;
  private AbstractAllenBradleyNode input;
  private AllenBradleyDBResourceManager structManager;

  protected AllenBradleyImportWizardPage(IFileSystem filesystem, AllenBradleyDBResourceManager structManager)
  {
    super("allenbradley wizard page");
    this.filesystem = filesystem;
    this.structManager = structManager;
  }

  @Override
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    setControl(container);
    container.setLayout(new GridLayout(1, false));
    Composite composite_1 = new Composite(container, SWT.NONE);
    composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    composite_1.setLayout(new GridLayout(3, false));
    createImportSymTabButton(composite_1);
    Label lblAddressType = new Label(composite_1, SWT.NONE);
    lblAddressType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAddressType.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.addresstype"));
    this.comboAddressType = new Combo(composite_1, SWT.READ_ONLY);
    comboAddressType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    Label lblAddress = new Label(composite_1, SWT.NONE);
    lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAddress.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.address"));
    this.textAddress = new Text(composite_1, SWT.BORDER);
    textAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    createImportUDTButton(composite_1);
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
    treeViewer.setContentProvider(new AllenBradleyModelTreeContentProvider());
    TreeViewerColumn trvclmn_selected = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_active = trvclmn_selected.getColumn();
    trclmn_active.setWidth(35);
    trclmn_active.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.active"));
    trvclmn_selected.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Image getImage(Object element)
      {
        if (((AbstractAllenBradleyNode) element).isActive())
        {
          return AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_CHECKED_1);
        }
        else
        {
          return AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_CHECKED_0);
        }
      }

      @Override
      public String getText(Object element)
      {
        return "";
      }
    });
    trvclmn_selected.setEditingSupport(new EditingSupport(treeViewer)
    {
      protected boolean canEdit(Object element)
      {
        return true;
      }

      protected CellEditor getCellEditor(Object element)
      {
        return new CheckboxCellEditor(treeViewer.getTree());
      }

      protected Object getValue(Object element)
      {
        return ((AbstractAllenBradleyNode) element).isActive();
      }

      protected void setValue(Object element, Object value)
      {
        ((AbstractAllenBradleyNode) element).setActive(!((AbstractAllenBradleyNode) element).isActive());
        /** we need to set all child elements */
        // AbstractSiemensModelNode node = (AbstractSiemensModelNode)
        // element;
        // if (node.getChildren() != null &&
        // !node.getChildren().isEmpty()) {
        // // for(SiemensModelNode nnode.getChildren()
        // }
        // ((OmronTableItem)element).active = value.toString();
        treeViewer.refresh(element);
      }
    });
    TreeViewerColumn trvclmn_symbolname = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_symbolname = trvclmn_symbolname.getColumn();
    trclmn_symbolname.setWidth(167);
    trclmn_symbolname.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.symbolname"));
    trvclmn_symbolname.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Image getImage(Object element)
      {
        return ((AbstractAllenBradleyNode) element).getLabelImage();
      }

      @Override
      public String getText(Object element)
      {
        return ((AbstractAllenBradleyNode) element).getName();
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
        return ((AbstractAllenBradleyNode) element).getName();
      }

      protected void setValue(Object element, Object value)
      {
        if (((AbstractAllenBradleyNode) element).getName().compareTo(value.toString()) == 0)
        {
          return;
        }
        ((AbstractAllenBradleyNode) element).setName(value.toString());
        treeViewer.refresh(element);
      }
    });
    // TreeViewerColumn trvclmn_address = new TreeViewerColumn(treeViewer,
    // SWT.NONE);
    // TreeColumn trclmn_address = trvclmn_address.getColumn();
    // trclmn_address.setWidth(55);
    // trclmn_address.setText("index");
    // trvclmn_address.setLabelProvider(new ColumnLabelProvider() {
    // @Override
    // public String getText(Object element) {
    // return ((AbstractallenbradleyNode) element).getAddressIndex() + "";
    // }
    // });
    // trvclmn_address.setEditingSupport(new EditingSupport(treeViewer) {
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
    // return "" + ((AbstractallenbradleyNode) element).getAddressIndex();
    // }
    //
    // protected void setValue(Object element, Object value) {
    // if ((((AbstractallenbradleyNode) element).getAddressIndex() + "")
    // .compareTo(value.toString()) == 0) {
    // /** do nothing */
    // return;
    // }
    // try {
    // ((AbstractallenbradleyNode) element).setIndex((Float
    // .parseFloat(value.toString())));
    // } catch (NumberFormatException e) {
    // // TODO: handle exception
    // e.printStackTrace();
    // }
    // treeViewer.refresh(element);
    // }
    //
    // });
    TreeViewerColumn trvclmn_datatype = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_datatype = trvclmn_datatype.getColumn();
    trclmn_datatype.setWidth(82);
    trclmn_datatype.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.editor.dp.datatype"));
    trvclmn_datatype.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element == null || ((AbstractAllenBradleyNode) element).getDataType() == null)
        {
          return "";
        }
        if (element instanceof AllenBradleyArrayNode)
        {
          return ((AllenBradleyArrayNode) element).getDataType();
        }
        return ((AbstractAllenBradleyNode) element).getDataType();
      }
    });
    // trvclmn_datatype.setEditingSupport(new EditingSupport(treeViewer) {
    //
    // @Override
    // protected void initializeCellEditorValue(CellEditor cellEditor,
    // ViewerCell cell) {
    // // TODO Auto-generated method stub
    // super.initializeCellEditorValue(cellEditor, cell);
    // }
    //
    // protected boolean canEdit(Object element) {
    // return true;
    // }
    //
    // protected CellEditor getCellEditor(Object element) {
    // return editor;
    // }
    //
    // protected Object getValue(Object element) {
    //
    // if (element == null
    // || ((AbstractSiemensNode) element).getDataType() == null) {
    // return "";
    // }
    // // TODO Auto-generated method stub
    // return ((AbstractSiemensNode) element).getDataType();
    // // return ((OmronTableItem)element).tagType;
    // }
    //
    // protected void setValue(Object element, Object value) {
    // if (value == null) {
    // String el = ((CCombo) editor.getControl()).getText();
    // if (el.isEmpty()) {
    // return;
    // }
    // // does the new element is in the items
    // boolean found = false;
    // for (String item : ((CCombo) editor.getControl())
    // .getItems()) {
    // if (el.compareTo(item) == 0) {
    // found = true;
    // break;
    // }
    // }
    // // we couldn't find the item, so add it
    // if (!found) {
    // Object i = editor.getViewer().getInput();
    // if (i instanceof String[]) {
    // List<String> l = new ArrayList<String>();
    // for (String s : (String[]) i) {
    // l.add(s);
    // }
    // l.add(el.toUpperCase().replace(" ", ""));
    //
    // editor.getViewer().setInput(
    // l.toArray(new String[l.size()]));
    // editor.getViewer().refresh();
    // }
    // // now add el to input
    // }
    // ((AbstractSiemensNode) element).setDataType(el
    // .toUpperCase().replace(" ", ""));
    // treeViewer.refresh();
    // return;
    // // value = ((CCombo)editor.getControl()).getText();
    // }
    // // TODO Auto-generated method stub
    // // value.toString();
    // ((AbstractSiemensNode) element).setDataType(value.toString());
    // treeViewer.refresh();
    // }
    //
    // });
    TreeViewerColumn trvclmn_description = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmn_description = trvclmn_description.getColumn();
    trclmn_description.setWidth(192);
    trclmn_description.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.editor.dp.description"));
    trvclmn_description.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return ((AbstractAllenBradleyNode) element).getDescription();
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
        return ((AbstractAllenBradleyNode) element).getDescription();
      }

      protected void setValue(Object element, Object value)
      {
        if (((AbstractAllenBradleyNode) element).getDescription().compareTo(value.toString()) == 0)
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
        ((AbstractAllenBradleyNode) element).setDescription(value.toString());
        treeViewer.refresh(element);
      }
    });
    scrolledComposite.setContent(composite);
    scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    this.nodeFactory = new AllenBradleyNodeFactory();
    this.registerPlatformTools();
    Composite bottomParent = new Composite(container, SWT.NONE);
    bottomParent.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
    bottomParent.setLayout(new GridLayout(4, false));
    createButtonBar(bottomParent);
    // new Label(topParent, SWT.NONE);
  }

  /**
   * Creates a new button with the given id.
   * <p>
   * The <code>Dialog</code> implementation of this framework method creates a
   * standard push button, registers it for selection events including button
   * presses, and registers default buttons with its shell. The button id is
   * stored as the button's client data. If the button id is
   * <code>IDialogConstants.CANCEL_ID</code>, the new button will be accessible
   * from <code>getCancelButton()</code>. If the button id is
   * <code>IDialogConstants.OK_ID</code>, the new button will be accesible from
   * <code>getOKButton()</code>. Note that the parent's layout is assumed to be a
   * <code>GridLayout</code> and the number of columns in this layout is
   * incremented. Subclasses may override.
   * </p>
   * 
   * @param parent
   *          the parent composite
   * @param id
   *          the id of the button (see <code>IDialogConstants.*_ID</code>
   *          constants for standard dialog button ids)
   * @param label
   *          the label from the button
   * @param defaultButton
   *          <code>true</code> if the button is to be the default button, and
   *          <code>false</code> otherwise
   * 
   * @return the new button
   * 
   * @see #getCancelButton
   * @see #getOKButton()
   */
  protected Button createButton(Composite parent, int id, String label, boolean defaultButton)
  {
    // increment the number of columns in the button bar
    // ((GridLayout) parent.getLayout()).numColumns++;
    Button button = new Button(parent, SWT.PUSH);
    button.setText(label);
    button.setFont(JFaceResources.getDialogFont());
    button.setData(new Integer(id));
    if (defaultButton)
    {
      Shell shell = parent.getShell();
      if (shell != null)
      {
        shell.setDefaultButton(button);
      }
    }
    return button;
  }

  private void createButtonBar(Composite parentBottomBar)
  {
    createDeselectAllButton(parentBottomBar);
    createSelectAllButton(parentBottomBar);
  }

  private void createSelectAllButton(Composite parent)
  {
    Button selectAll = createButton(parent, 4096, CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.selectall"), true);
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
    Button deselectAll = createButton(parent, 4096, CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.deselectall"), true);
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

  private void createImportSymTabButton(Composite parent)
  {
    Button importdb = new Button(parent, SWT.PUSH);
    importdb.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.dpfile"));
    importdb.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_IMPORT));
    final Text txtImportDB = new Text(parent, SWT.BORDER);
    txtImportDB.setEditable(false);
    GridData gd_txtImportDB = new GridData(SWT.FILL, SWT.FILL, true, false);
    gd_txtImportDB.horizontalSpan = 2;
    txtImportDB.setLayoutData(gd_txtImportDB);
    importdb.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        /** open file to import */
        FileDialog dialog = new FileDialog(getShell());
        dialog.setFilterNames(FILTER_DATAPOINT);
        dialog.setFilterExtensions(FILTER_EXT_CSV);
        final String csv = dialog.open();
        if (csv == null)
        {
          /** do nothing, because the import command was canceled */
          return;
        }
        txtImportDB.setText(csv);
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .getActiveEditor();
        Job importJob = new Job("Importiere Datenbaustein")
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            monitor.beginTask(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.editor.allenbradley.wizard.import.monitor.init"), 10);
            if (filesystem.isFile(csv))
            {
              monitor.worked(2);
              InputStream symboTableFile = null;
              try
              {
                symboTableFile = filesystem.readFile(csv);
                monitor.worked(1);
                final AbstractAllenBradleyNode root = nodeFactory
                    .parseCSV(new BufferedReader(new InputStreamReader(symboTableFile)), structManager);
                monitor.worked(6);
                Display.getDefault().syncExec(new Runnable()
                {
                  @Override
                  public void run()
                  {
                    // String symbolName = root.getName();
                    // String symbolAddress = symbolName
                    // .replaceFirst(
                    // SiemensAreaCode.DB
                    // .name(), "")
                    // .trim();
                    comboAddressType.removeAll();
                    // comboAddressType.add(SiemensAreaCode.DB
                    // .name());
                    comboAddressType.select(0);
                    // textAddress.setText(symbolAddress);
                    input = root;
                    treeViewer.setInput(root);
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
              catch (Exception e)
              {
                e.printStackTrace();
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
        importJob.schedule();
      }
    });
  }

  private void createImportUDTButton(Composite parent)
  {
    Label lblAddressType = new Label(parent, SWT.NONE);
    lblAddressType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAddressType.setText("Structures");
    final Text txtImportStruct = new Text(parent, SWT.BORDER);
    txtImportStruct.setEditable(false);
    txtImportStruct.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    Button importStructUdt = new Button(parent, SWT.PUSH);
    importStructUdt.setToolTipText("import structure file");
    importStructUdt.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_GEARS));
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
        txtImportStruct.setText(directory);
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
                    // allenbradley stucture extension
                    if (EXT_L5X.equalsIgnoreCase(extension))
                    {
                      return true;
                    }
                  }
                  return false;
                }
              });
              if (files != null)
              {
                monitor.beginTask(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
                    "com.bichler.astudio.editor.allenbradley.wizard.import.monitor.init"), files.length);
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
                      for (AbstractAllenBradleyNode arn : structs)
                      {
                        structures.put(arn.getName(), arn);
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
                    catch (Exception e)
                    {
                      e.printStackTrace();
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
   * Context menu & registrate treeviewer as selectionprovider
   */
  private void registerPlatformTools()
  {
    MenuManager menuManager = new MenuManager();
    final Menu menu = menuManager.createContextMenu(treeViewer.getTree());
    // Set the MenuManager
    /**
     * As menu manager
     */
    treeViewer.getTree().setMenu(menu);
    /**
     * defined in plugin xml
     */
    IMenuService mSvc = (IMenuService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
        .getService(IMenuService.class);
    mSvc.populateContributionManager(menuManager, "allenbradleydpeditor.table.contextmenu");
    IWorkbenchPartSite site = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart()
        .getSite();
    site.registerContextMenu(menuManager, treeViewer);
    site.setSelectionProvider(treeViewer);
    // make the selection available
    // getSite().setSelectionProvider(tableViewer);
  }

  public AbstractAllenBradleyNode getInput()
  {
    return this.input;
  }
}
