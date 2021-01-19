package com.bichler.astudio.device.opcua.wizard.page.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.server.core.UAServerApplicationInstance;

public class NamespaceModelWizardPage extends WizardPage
{
	private static class ContentProvider extends ArrayContentProvider
    {
  
    }
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public Image getColumnImage(Object element, int columnIndex)
    {
      return null;
    }

    public String getColumnText(Object element, int columnIndex)
    {
      return element.toString();
    }
  }


  
  
  private CheckboxTableViewer tableViewer;
  private String servername;
  private Button btn_none;
  private Button btn_all;
  private Button btn_exportFullNSTable;
  private Object[] selectedNamespaces = new Object[0];
  private Object[] allNamespaces = new Object[0];
  private boolean fullNsExport = false;  

  public NamespaceModelWizardPage(String servername)
  {
    super("informationmodel");
    this.servername = servername;
    setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.namespaceselection.page.title"));
    setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.namespaceselection.page.description"));
  }

  @Override
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    setControl(container);
    container.setLayout(new GridLayout(1, false));
    // checkbox table viewer
    this.tableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
    Table table = tableViewer.getTable();
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    table.setBounds(0, 0, 85, 85);
    this.tableViewer.setLabelProvider(new TableLabelProvider());
    this.tableViewer.setContentProvider(new ContentProvider());
    
    // composite advanced buttons
    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    composite.setBounds(0, 0, 64, 64);
    
    this.btn_exportFullNSTable = new Button(composite, SWT.CHECK);
    btn_exportFullNSTable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
    btn_exportFullNSTable.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.device.opcua.handler.wizard.exportFullNSTable"));
    
    this.btn_none = new Button(composite, SWT.NONE);
    GridData gd_btn_none = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btn_none.heightHint = 48;
    gd_btn_none.widthHint = 48;
    btn_none.setLayoutData(gd_btn_none);
    btn_none.setImage(StudioImageActivator.getImage(StudioImages.ICON_SELECT_NONE));
    btn_none.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.tooltip.selectnone"));
    // button add all
    this.btn_all = new Button(composite, SWT.NONE);
    GridData gd_btn_all = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btn_all.widthHint = 48;
    gd_btn_all.heightHint = 48;
    btn_all.setImage(StudioImageActivator.getImage(StudioImages.ICON_SELECT_ALL));
    btn_all.setLayoutData(gd_btn_all);
    btn_all.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.tooltip.selectall"));
    // swt handlers
    setHandler();
    // table input and initialization
    setInput();
  }

  private void setInput()
  {
    UAServerApplicationInstance server = Studio_ResourceManager.getOPCUAServerInstance(this.servername);
    List<String> nsTable = new ArrayList<String>(Arrays.asList(server.getServerInstance().getNamespaceUris().toArray()));
    // remove opc intern namespace if we generate ansi c server
    IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
	boolean doCompileAnsiC = store.getBoolean(OPCUAConstants.OPCUADoCompileAnsiC);
	if (doCompileAnsiC)
	{
		// TODO set configurable
//		nsTable.remove(0);
	}
    this.tableViewer.setInput(nsTable.toArray());
    this.tableViewer.setAllChecked(true);
    setCheckedElements();
    setAllNamespaceElements(this.tableViewer.getCheckedElements());
  }

  private void setHandler()
  {
    // remember checked elements
    this.tableViewer.addCheckStateListener(new ICheckStateListener()
    {
      @Override
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        setCheckedElements();
      }
    });
    
    btn_exportFullNSTable.addSelectionListener(new SelectionAdapter() {
    	 @Override
         public void widgetSelected(SelectionEvent e)
         {
           fullNsExport = btn_exportFullNSTable.getSelection();
         }
    });
    
    btn_all.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        tableViewer.setAllChecked(true);
        setCheckedElements();
      }
    });
    btn_none.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        tableViewer.setAllChecked(false);
        setCheckedElements();
      }
    });
  }

  private void setCheckedElements()
  {
    selectedNamespaces = tableViewer.getCheckedElements();
  }

  private void setAllNamespaceElements(Object[] objects)
  {
    this.allNamespaces = objects;
  }

  public Object[] getCheckedElements()
  {
    return this.selectedNamespaces;
  }
  
  public boolean isFullNsExport() {
	  return this.fullNsExport;
  }

  public Object[] getAllNamespaceElements()
  {
    return this.allNamespaces;
  }
}
