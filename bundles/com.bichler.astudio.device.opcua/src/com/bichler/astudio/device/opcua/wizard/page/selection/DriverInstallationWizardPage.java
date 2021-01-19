package com.bichler.astudio.device.opcua.wizard.page.selection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DriverInstallationWizardPage extends WizardPage
{
  boolean TEST_VERSION = false;
  private Table table;
  private CheckboxTableViewer tableViewer;
  private Object[] selectedDrivers = new Object[0];
  private Object[] allDrivers = new Object[0];
  private Button btnAll;
  private Button btnNone;
  private IFileSystem device;

  public DriverInstallationWizardPage()
  {
    super("driver");
    setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.title"));
    setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.description"));
  }

  @Override
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    setControl(container);
    container.setLayout(new GridLayout(1, false));
    this.tableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
    table = tableViewer.getTable();
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    table.setHeaderVisible(true);
    this.tableViewer.setContentProvider(new DriverContentProvider());
    // composite advanced buttons
    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    composite.setBounds(0, 0, 64, 64);
    // button add all
    this.btnAll = new Button(composite, SWT.NONE);
    GridData gd_btn_all = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btn_all.heightHint = 48;
    gd_btn_all.widthHint = 48;
    btnAll.setLayoutData(gd_btn_all);
    btnAll.setImage(StudioImageActivator.getImage(StudioImages.ICON_SELECT_ALL));
    btnAll.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.tooltip.selectall"));
    this.btnNone = new Button(composite, SWT.NONE);
    GridData gd_btn_none = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btn_none.widthHint = 48;
    gd_btn_none.heightHint = 48;
    btnNone.setLayoutData(gd_btn_none);
    btnNone.setImage(StudioImageActivator.getImage(StudioImages.ICON_SELECT_NONE));
    btnNone.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.tooltip.selectnone"));
    createTableViewerColumns();
    setHandler();
    initialize();
  }

  public void initialize()
  {
    this.tableViewer.setInput(OPCUADriverRegistry.drivers);
    this.tableViewer.setAllChecked(true);
    if (TEST_VERSION)
    {
      this.tableViewer.setAllChecked(false);
    }
    this.selectedDrivers = this.tableViewer.getCheckedElements();
    this.allDrivers = this.tableViewer.getCheckedElements();
    if (TEST_VERSION)
    {
      this.tableViewer.getControl().setEnabled(false);
      this.btnAll.setEnabled(false);
      this.btnNone.setEnabled(false);
    }
  }

  private void createTableViewerColumns()
  {
    TableViewerColumn tvcImgLocked = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmntvcImgLocked = tvcImgLocked.getColumn();
    tblclmntvcImgLocked.setAlignment(SWT.CENTER);
    tblclmntvcImgLocked.setWidth(50);
    tvcImgLocked.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Image getImage(Object element)
      {
        if (element instanceof InternationalActivator)
        {
          File imgPath = ((InternationalActivator) element).getFile(((InternationalActivator) element).getBundle(),
              Path.ROOT.append("driver").append("image").append("unlock.png"));
          if (imgPath == null)
            return super.getImage(element);
          try (InputStream inputstream = new FileInputStream(imgPath);)
          {
            return new Image(Display.getCurrent(), inputstream);
          }
          catch (IOException e)
          {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
          }
        }
        return super.getImage(element);
      }

      @Override
      public String getText(Object element)
      {
        return "";
      }
    });
    TableViewerColumn tvcType = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmnType = tvcType.getColumn();
    tblclmnType.setWidth(100);
    tblclmnType.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.type"));
    tvcType.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Image getImage(Object element)
      {
        if (element instanceof InternationalActivator)
        {
          File imgPath = ((InternationalActivator) element).getFile(((InternationalActivator) element).getBundle(),
              Path.ROOT.append("driver").append("image").append("driver.png"));
          try (InputStream inputstream = new FileInputStream(imgPath);)
          {
            return new Image(Display.getCurrent(), inputstream);
          }
          catch (IOException e)
          {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
          }
        }
        return super.getImage(element);
      }

      @Override
      public String getText(Object element)
      {
        if (element instanceof InternationalActivator)
        {
          String type = ((InternationalActivator) element).getBundle().getSymbolicName()
              .replace("com.bichler.astudio.editor.", "");
          int index = type.indexOf('.');
          return type.substring(0, index);
        }
        return "";
      }
    });
    TableViewerColumn tvcVersion = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmnVersoin = tvcVersion.getColumn();
    tblclmnVersoin.setWidth(100);
    tblclmnVersoin.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.version"));
    tvcVersion.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element instanceof InternationalActivator)
        {
          int index = ((InternationalActivator) element).getBundle().getVersion().toString().lastIndexOf('.');
          return ((InternationalActivator) element).getBundle().getVersion().toString().substring(0, index);
        }
        return "";
      }
    });
    TableViewerColumn tvcDate = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmnDate = tvcDate.getColumn();
    tblclmnDate.setWidth(100);
    tblclmnDate.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.date"));
    tvcDate.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element instanceof InternationalActivator)
        {
          int index = ((InternationalActivator) element).getBundle().getVersion().toString().lastIndexOf('.');
          return ((InternationalActivator) element).getBundle().getVersion().toString().substring(index + 1);
        }
        return "";
      }
    });
    TableViewerColumn tvcInfo = new TableViewerColumn(tableViewer, SWT.NONE);
    TableColumn tblclmnInfo = tvcInfo.getColumn();
    tblclmnInfo.setWidth(100);
    tblclmnInfo.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.info"));
    tvcInfo.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element instanceof InternationalActivator)
        {
          // int index = ((InternationalActivator)
          // element).getBundle().getVersion().toString().lastIndexOf('.');
          return "";
        }
        return "";
      }
    });
  }

  private boolean validateVersion(OPCUAServerDriverModelNode node)
  {
    String versions = readVersionFromTarget(node);
    return versions.contains(node.getDriverVersion());
    // tableViewer.getTable().getItems();
    // return true;
  }

  private String readVersionFromTarget(OPCUAServerDriverModelNode node)
  {
    String drivers = this.device.getRootPath() + this.device.getTargetFileSeparator() + "runtime"
        + this.device.getTargetFileSeparator() + "drivers" + this.device.getTargetFileSeparator() + node.getDriverType()
        + this.device.getTargetFileSeparator();
    StringBuilder version = new StringBuilder();
    try
    {
      String[] versions = this.device.listDirs(drivers);
      String deliminator = "";
      for (String vers : versions)
      {
        version.append(deliminator + vers);
        deliminator = " | ";
      }
    }
    catch (IOException e)
    {
      return "0";
    }
    return version.toString();
  }

  private void setHandler()
  {
    this.tableViewer.addCheckStateListener(new ICheckStateListener()
    {
      @Override
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        selectedDrivers = ((CheckboxTableViewer) event.getSource()).getCheckedElements();
      }
    });
    btnAll.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        tableViewer.setAllChecked(true);
        selectedDrivers = tableViewer.getCheckedElements();
      }
    });
    btnNone.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        tableViewer.setAllChecked(false);
        selectedDrivers = tableViewer.getCheckedElements();
      }
    });
  }


  class DriverContentProvider implements IStructuredContentProvider
  {
    @Override
    public void dispose()
    {
      // TODO Auto-generated method stub
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    @Override
    public Object[] getElements(Object inputElement)
    {
      if (inputElement instanceof SortedMap<?, ?>)
      {
        return ((SortedMap<String, InternationalActivator>) inputElement).values()
            .toArray(new InternationalActivator[0]);
      }
      return new String[0];
    }
  }

  public Object[] getSelectedDrivers()
  {
    return this.selectedDrivers;
  }

  public Object[] getAllDrivers()
  {
    return this.allDrivers;
  }

  public void setTargetFileSystem(IFileSystem device)
  {
    this.device = device;
  }
}
