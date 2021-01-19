package com.bichler.astudio.device.firmware.wizard;

import java.io.File;

import org.eclipse.jface.wizard.IWizardPage;


import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.firmware.FirmwareActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class FirmwareInstallWizard extends AbstractInstallWizard
{
  private OverviewInstallationWizardPage pageOne;

  public FirmwareInstallWizard()
  {
    setWindowTitle(CustomString.getString(FirmwareActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.device.firmware.wizard.title"));
  }

  @Override
  public void addPages()
  {
    this.pageOne = new OverviewInstallationWizardPage();
    addPage(pageOne);
  }

  @Override
  public IWizardPage getNextPage(IWizardPage page)
  {
    return super.getNextPage(page);
  }
 

  @Override
  protected int getVersionIndex() {
	return this.pageOne.getFramworkVersionIndex();
  }

  @Override
  protected File getRootFolderToUpload() {
	return FirmwareActivator.getDefault().getFirmwareFile();
  }
}
