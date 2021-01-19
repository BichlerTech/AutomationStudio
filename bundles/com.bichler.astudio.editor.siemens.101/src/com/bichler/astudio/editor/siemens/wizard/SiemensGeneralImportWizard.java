package com.bichler.astudio.editor.siemens.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensDBWizardPage;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensImportTypeWizardPage;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensUDTWizardPage;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SiemensGeneralImportWizard extends Wizard {
	private IFileSystem filesystem;
	private SiemensDBResourceManager structManager;
	private SiemensImportTypeWizardPage importTypePage;
	private SiemensUDTWizardPage selectUDTPage;
	private SiemensDBWizardPage selectDBPage;
	private SiemensImportTypeWizardPage overviewPage;

	public SiemensGeneralImportWizard(SiemensDBResourceManager structManager) {
		setWindowTitle(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.title"));
		this.structManager = structManager;
	}

	@Override
	public void addPages() {
		// this.importTypePage = new SiemensImportTypeWizardPage();
		// addPage(this.importTypePage);
		//
		// this.selectUDTPage = new SiemensUDTWizardPage();
		// addPage(this.selectUDTPage);
		//
		// this.selectDBPage = new SiemensDBWizardPage();
		// addPage(this.selectDBPage);
		//
		// this.overviewPage = new SiemensImportTypeWizardPage();
		// addPage(this.overviewPage);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}
}
