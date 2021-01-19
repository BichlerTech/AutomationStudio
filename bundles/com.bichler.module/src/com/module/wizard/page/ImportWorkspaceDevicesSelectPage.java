package com.module.wizard.page;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ImportWorkspaceDevicesSelectPage extends WizardPage{

	private Text txtFilePath;
	private Button btnOpenFileDialog;

	private File file = null;
	private boolean removeExistingDevices = false;
	private Button checkRemoveDevices;
	
	public ImportWorkspaceDevicesSelectPage() {
		super("importworkspacedeviceselect");
		
		setTitle("Select an Automation Studio workspace");
		setDescription("Import devices from an Automation Studio workspace");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label label = new Label(container, SWT.NONE);
		label.setText("File");

		txtFilePath = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		// gd_txtPath.widthHint = 284;
		// gd_txtPath.heightHint = 125;

		txtFilePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnOpenFileDialog = new Button(container, SWT.NONE);
		btnOpenFileDialog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnOpenFileDialog.setText("Open");
		
		this.checkRemoveDevices = new Button(container, SWT.CHECK);
		checkRemoveDevices.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		checkRemoveDevices.setText("Remove existing devices");
		
		setHandler();
	}

	private void setHandler() {
		btnOpenFileDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog fd = new DirectoryDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
				fd.setText("Open");
				fd.setFilterPath("C:/");
				
				String selected = fd.open();
				if (selected != null) {
					txtFilePath.setText(selected);
					file = new File(selected);
				}

				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});
		
		checkRemoveDevices.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					removeExistingDevices = checkRemoveDevices.getSelection();
			}
		});
	}

	@Override
	public boolean isPageComplete() {
		if (this.file != null && this.file.exists()) {
			return true;
		}

		return false;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public boolean isRemoveExistingDevices() {
		return this.removeExistingDevices;
	}
}
