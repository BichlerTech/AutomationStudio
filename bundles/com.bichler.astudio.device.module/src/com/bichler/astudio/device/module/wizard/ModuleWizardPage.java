package com.bichler.astudio.device.module.wizard;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.device.core.transfer.DeviceTargetUtil;
import com.bichler.astudio.device.module.ModuleActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.jface.viewers.ListViewer;

public class ModuleWizardPage extends WizardPage {

	private IFileSystem filesystem = null;
	private Text txtFacts;

	private File zipFile = null;
	private Text txtZipPath;

	private Button btnOpenZipDialog;
	private Button btnDocu;

	/**
	 * private int connectionType = 0; private String connectionName = ""; private
	 * int connectionTimeout = 1000; private String hostName = ""; private String
	 * userName = ""; private String password = ""; private String javaPath = "";
	 * private String javaArg = "-jar"; private String rootPath = "";
	 */

	/**
	 * Create the wizard.
	 * 
	 * @param pageOne
	 * @param isnew
	 * 
	 * @wbp.parser.constructor
	 */
	public ModuleWizardPage() {
		super("newdevice");

		setTitle(CustomString.getString(ModuleActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.zip.wizard.page.title"));
		setDescription(CustomString.getString(ModuleActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.zip.wizard.page.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label label = new Label(container, SWT.NONE);
		label.setText(CustomString.getString(ModuleActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.zip.wizard.path"));

		txtZipPath = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		// gd_txtPath.widthHint = 284;
		// gd_txtPath.heightHint = 125;

		txtZipPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnOpenZipDialog = new Button(container, SWT.NONE);
		btnOpenZipDialog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnOpenZipDialog.setText(CustomString.getString(ModuleActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.zip.wizard.addzip"));
		
		Label lblFacts = new Label(container, SWT.NONE);
		lblFacts.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblFacts.setText(CustomString.getString(ModuleActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.module.property.facts"));

		txtFacts = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);

		GridData gd_txtFacts = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtFacts.heightHint = 125;
//		gd_txtFacts.grabExcessVerticalSpace = true;
		txtFacts.setLayoutData(gd_txtFacts);

		btnDocu = new Button(container, SWT.NONE);
		btnDocu.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnDocu.setText(CustomString.getString(ModuleActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.zip.wizard.opendocu"));

		setHandler();

		// select();
	}

	private void setHandler() {
		btnDocu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (Desktop.isDesktopSupported()) {
					// TODO: OPEN DOKU
					/*
					 * String version = version2folder.get(cmbZipVersion.getText()); if (version ==
					 * null || version.isEmpty()) return; File dokufile =
					 * ModuleActivator.getDefault().getDocuFile(version); try {
					 * Desktop.getDesktop().open(dokufile); } catch (IOException e1) {
					 * Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(),
					 * e1); }
					 */
				}
			}
		});

		btnOpenZipDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
				fd.setText("Open");
				fd.setFilterPath("C:/");
				String[] filterExt = { "*.asum" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				if (selected != null) {
					txtZipPath.setText(selected);
					zipFile = new File(selected);

					if (zipFile.isFile()) {
						readModuleDescription(zipFile);
					}
				}

				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});
	}

	private void readModuleDescription(File zipFile) {
		ZipInputStream zis = null;
		ZipEntry zipEntry = null;
		StringBuffer stringbuffer = new StringBuffer();
		try {
			// open zip
			zis = new ZipInputStream(new FileInputStream(zipFile));
			// read zip entries
			zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				// find requried entry
				if (!zipEntry.getName().equals(DeviceTargetUtil.FILE_VERSION)) {
					zipEntry = zis.getNextEntry();
					continue;
				}

				try {
					// extract zip entry

					int len;
					byte[] buffer = new byte[1024];
					while ((len = zis.read(buffer)) > 0) {
						stringbuffer.append(new String(buffer));
					}
					break;
				} finally {

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zis != null) {
				try {
					zis.closeEntry();
					zis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.txtFacts.setText(stringbuffer.toString());
	}

	@Override
	public boolean isPageComplete() {
		if (this.zipFile != null && this.zipFile.exists()) {
			return true;
		}

		return false;
	}

	@Override
	public void setVisible(boolean visible) {
		/*
		 * if (visible) { setValue(); }
		 */

		super.setVisible(visible);
	}

	/*
	 * public IFileSystem getFilesystem() { return filesystem; }
	 * 
	 * public void setFilesystem(IFileSystem filesystem) { this.filesystem =
	 * filesystem; }
	 */

	/**
	 * No version needed
	 * 
	 * @return -1
	 */
	public int getZipVersionIndex() {
		return -1;
	}

	public File getZipfile() {
		return this.zipFile;
	}
}
