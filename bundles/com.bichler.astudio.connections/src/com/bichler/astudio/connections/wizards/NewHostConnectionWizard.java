package com.bichler.astudio.connections.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.filesystem.IFileSystem;

public class NewHostConnectionWizard extends Wizard implements INewWizard {

	public static final String copyright = "(c) Bichler Technologies GmbH - 2018.";

	private IFileSystem filesystem = null;

	// the workbench instance
	protected IWorkbench workbench;

	private HostConnectionWizardPage newPage1 = null;

	// workbench selection when the wizard was started
	protected IStructuredSelection selection;

//	private ICometFileSystem localFileSystem;

	@Override
	public void addPages() {
		newPage1 = new HostConnectionWizardPage(workbench, selection);
		addPage(newPage1);
	}

	public NewHostConnectionWizard() {
		this.setWindowTitle("Automation Studio");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		filesystem = newPage1.getFilesystem();
		return true;
	}

	public void setEdit() {
		this.newPage1.setEdit();
	}

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setTargetFilesystem(IFileSystem filesystem) {
		if (newPage1 != null) {
			newPage1.setFilesystem(filesystem);
		}
		this.filesystem = filesystem;
	}

//	public void setLocalFileSystem(ICometFileSystem localfileSystem) {
//		this.localFileSystem = localfileSystem;
//	}
}
