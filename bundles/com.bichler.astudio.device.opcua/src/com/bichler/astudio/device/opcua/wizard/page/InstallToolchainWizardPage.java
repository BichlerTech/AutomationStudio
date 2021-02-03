package com.bichler.astudio.device.opcua.wizard.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class InstallToolchainWizardPage extends WizardPage {
	private Text txtPath;
	private Button btnPath;

	private boolean flagToolchainZip = false;

	private String path = null;

	public InstallToolchainWizardPage() {
		super("installtoolchainWizardPage");
		setTitle("Install toolchain");
		setDescription("Select toolchain file");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblPath = new Label(container, SWT.NONE);
		lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPath.setText("Path");

		txtPath = new Text(container, SWT.BORDER);
		txtPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnPath = new Button(container, SWT.NONE);
		btnPath.setText("...");

		setHandler();
	}

	@Override
	public boolean isPageComplete() {
		if (this.path == null) {
			setErrorMessage("Select a toolchain.zip file!");
			return false;
		}

		if (!this.flagToolchainZip) {
			setErrorMessage("Selected toolchain.zip is corrupt!");
			return false;
		}

		setErrorMessage(null);

		return true;
	}

	public String getPath() {
		return this.path;
	}

	private void setIsToolchainZip() {
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
		try {
			progressDialog.run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.setTaskName("Check toolchain file"+"...");
					
					File zipFile = new File(getPath());
					ZipInputStream zis = null;
					try {
						zis = new ZipInputStream(new FileInputStream(zipFile));
						ZipEntry zipEntry = zis.getNextEntry();
						while (zipEntry != null) {
							String name = zipEntry.getName();

							if ("toolchain".equals(name)) {
								flagToolchainZip = true;
								break;
							}
							flagToolchainZip = false;
							zipEntry = zis.getNextEntry();
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (zis != null) {
							try {
								zis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
					progressDialog.close();
				}
			});
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
//		progressDialog.open();
	}

	private void setHandler() {
		this.btnPath.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.zip" });
				String path = dialog.open();
				if (path != null) {
					txtPath.setText(path);
					setPath(path);
				}
				setIsToolchainZip();

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}

		});
	}

	private void setPath(String path) {
		this.path = path;
	}

}
