package com.bichler.astudio.device.opcua.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.device.opcua.wizard.page.selection.DeviceInstallationPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.DriverInstallationWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.RuntimeInstallationWizardPage;

public class DeviceInstallWizard extends Wizard
{
  private DeviceInstallationPage pageOne;
//  private RuntimeInstallationWizardPage pageTwo;
  private boolean isInternalRuntime;
  private String externalPath;
  private DriverInstallationWizardPage pageThree;
  private Object[] selectedDrivers = new Object[0];

  public Object[] getSelectedDrivers()
  {
    return selectedDrivers;
  }

  public void setSelectedDrivers(Object[] selectedDrivers)
  {
    this.selectedDrivers = selectedDrivers;
  }

  public DeviceInstallWizard()
  {
    setWindowTitle("Installation");
  }

  @Override
  public void addPages()
  {
    this.pageOne = new DeviceInstallationPage();
    addPage(this.pageOne);
//    this.pageTwo = new RuntimeInstallationWizardPage();
//    addPage(pageTwo);
    this.pageThree = new DriverInstallationWizardPage();
    addPage(pageThree);
  }

  @Override
  public IWizardPage getNextPage(IWizardPage page)
  {
    if (page instanceof RuntimeInstallationWizardPage)
    {
      this.pageThree.initialize();
      return this.pageThree;
    }
    return super.getNextPage(page);
  }

  @Override
  public boolean performFinish()
  {
    this.isInternalRuntime = this.pageOne.isInternalRuntime();
    this.externalPath = this.pageOne.getExternalPath();
    this.selectedDrivers = this.pageThree.getSelectedDrivers();
    return true;
  }

  public boolean isInternalRuntime()
  {
    return this.isInternalRuntime;
  }

  public String getExternalPath()
  {
    return this.externalPath;
  }
}
