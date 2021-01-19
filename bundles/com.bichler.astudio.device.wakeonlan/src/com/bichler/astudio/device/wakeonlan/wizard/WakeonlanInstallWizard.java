package com.bichler.astudio.device.wakeonlan.wizard;

import java.io.File;

import org.eclipse.jface.wizard.IWizardPage;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.wakeonlan.WakeonlanActivator;
import com.bichler.astudio.utils.internationalization.CustomString;


public class WakeonlanInstallWizard extends AbstractInstallWizard
{
  private WakeOnLanInstallationWizardPage pageOne;

  public WakeonlanInstallWizard()
  {
    setWindowTitle(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
			"com.bichler.astudio.device.wakeonlan.wizard.title"));
  }

  @Override
  public void addPages()
  {
    this.pageOne = new WakeOnLanInstallationWizardPage();
    addPage(pageOne);
  }

  @Override
  public IWizardPage getNextPage(IWizardPage page)
  {
    return super.getNextPage(page);
  }

  @Override
  protected int getVersionIndex() {
	  return this.pageOne.getWakeOnLanVersionIndex();
  }

@Override
protected File getRootFolderToUpload() {
	return  WakeonlanActivator.getDefault().getFiles();
}
  
}
