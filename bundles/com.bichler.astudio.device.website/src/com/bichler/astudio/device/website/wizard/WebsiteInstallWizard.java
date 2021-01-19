package com.bichler.astudio.device.website.wizard;

import java.io.File;

import org.eclipse.jface.wizard.IWizardPage;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.website.WebsiteActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class WebsiteInstallWizard extends AbstractInstallWizard
{
  private WebsiteInstallationWizardPage pageOne;

  public WebsiteInstallWizard()
  {
    setWindowTitle(CustomString.getString(WebsiteActivator.getDefault().RESOURCE_BUNDLE,
			"com.bichler.astudio.device.website.wizard.title"));
  }

  @Override
  public void addPages()
  {
    this.pageOne = new WebsiteInstallationWizardPage();
    addPage(pageOne);
  }

  @Override
  public IWizardPage getNextPage(IWizardPage page)
  {
    return super.getNextPage(page);
  }


   @Override
   protected int getVersionIndex() {
       return this.pageOne.getWebVersionIndex();
   }
	
   @Override
   protected File getRootFolderToUpload() {
	   return WebsiteActivator.getDefault().getFiles();
   }
}
