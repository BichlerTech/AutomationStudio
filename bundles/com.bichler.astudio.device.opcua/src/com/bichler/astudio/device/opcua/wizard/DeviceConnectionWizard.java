package com.bichler.astudio.device.opcua.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.wizard.page.connection.DeviceConnectionWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.connection.ScanForDeviceDNSWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.connection.ScanForDeviceWizardPage;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DeviceConnectionWizard extends Wizard implements INewWizard
{
  public static final String copyright = "(c) Bichler Technologies GmbH - 2018.";
  private IFileSystem filesystem = null;
  // the workbench instance
  protected IWorkbench workbench;
  private ScanForDeviceWizardPage pageOne = null;
  private ScanForDeviceDNSWizardPage pageTwo = null;
  private DeviceConnectionWizardPage pageThree = null;
  // workbench selection when the wizard was started
  protected IStructuredSelection selection;

  private boolean isnew = false;
  private boolean isscan = false;

  public DeviceConnectionWizard(boolean isnew, boolean showScanDevicePage)
  {
    this.isnew = isnew;
    this.isscan = showScanDevicePage;
    setWindowTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.deviceconnection.wizard.title"));
  }

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
    this.workbench = workbench;
    this.selection = selection;
  }

  @Override
  public void addPages()
  {
    if (isnew && isscan)
    {
      pageOne = new ScanForDeviceWizardPage();
      addPage(pageOne);
      pageTwo = new ScanForDeviceDNSWizardPage();
      addPage(pageTwo);
    }
    pageThree = new DeviceConnectionWizardPage(isnew, isscan, pageOne/* , pageTwo */);
    addPage(pageThree);
  }

  @Override
  public boolean performFinish()
  {
    filesystem = pageThree.getFilesystem();
    return true;
  }

  public void setEdit()
  {
    this.pageThree.setEdit();
  }

  public IFileSystem getFilesystem()
  {
    return filesystem;
  }

  public void setTargetFilesystem(IFileSystem filesystem)
  {
    if (pageThree != null)
    {
      pageThree.setFilesystem(filesystem);
    }
    this.filesystem = filesystem;
  }

//  public void setLocalFileSystem(IFileSystem localfileSystem)
//  {
//    this.localFileSystem = localfileSystem;
//  }
}
