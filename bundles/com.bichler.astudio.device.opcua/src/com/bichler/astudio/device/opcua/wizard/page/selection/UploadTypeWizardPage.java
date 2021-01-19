package com.bichler.astudio.device.opcua.wizard.page.selection;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.internationalization.CustomString;

public class UploadTypeWizardPage extends WizardPage
{
  private boolean flag_comboxupload;
  private Button rdo_combox;
  private Button rdo_default;

  public UploadTypeWizardPage()
  {
    super("driver");
    setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.uploadtypeselection.page.title"));
    setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.uploadtypeselection.page.description"));
  }

  @Override
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    FillLayout fl_container = new FillLayout();
    fl_container.type = SWT.VERTICAL;
    container.setLayout(fl_container);
    setControl(container);
    Group group = new Group(container, SWT.NONE);
    group.setLayout(new FillLayout(SWT.VERTICAL));
    this.rdo_combox = new Button(group, SWT.RADIO);
    rdo_combox.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.uploadtypeselection.page.datahub"));
    this.rdo_default = new Button(group, SWT.RADIO);
    rdo_default.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.uploadtypeselection.page.default"));
    setControls();
    setHandler();
  }

  private void setControls()
  {
    IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
    boolean onlyCombox = store.getBoolean(OPCUAConstants.OPCUAOnlyCombox);
    if (onlyCombox)
    {
      this.rdo_combox.setEnabled(true);
      this.rdo_default.setEnabled(false);
    }
    else
    {
      this.rdo_combox.setEnabled(true);
      this.rdo_default.setEnabled(true);
    }
  }

  private void setHandler()
  {
    this.rdo_combox.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        flag_comboxupload = ((Button) e.getSource()).getSelection();
      }
    });
    this.rdo_default.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        flag_comboxupload = !((Button) e.getSource()).getSelection();
      }
    });
    this.rdo_combox.setSelection(true);
    this.flag_comboxupload = true;
    this.rdo_combox.notifyListeners(SWT.Selection, new Event());
  }

  public boolean isComboxUpload()
  {
    return this.flag_comboxupload;
  }
}
