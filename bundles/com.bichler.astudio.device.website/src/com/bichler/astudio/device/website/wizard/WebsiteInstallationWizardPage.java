package com.bichler.astudio.device.website.wizard;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.device.core.transfer.DeviceTargetUtil;
import com.bichler.astudio.device.website.WebsiteActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class WebsiteInstallationWizardPage extends WizardPage {

	private Combo cmbWebsiteVersion;
	private int websiteVersionIndex;
	private IFileSystem filesystem = null;
	private Text txtFacts;
	private Map<String, String> version2folder = new HashMap<String, String>();

	private Map<String, List<String>> versionsDesc = new HashMap<>();

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
	public WebsiteInstallationWizardPage() {
		super("newdevice");

		setTitle(CustomString.getString(WebsiteActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.website.page.title"));
		setDescription(CustomString.getString(WebsiteActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.website.page.description"));
	}

	private String[] listWebsiteVersions() {
		List<String> versions = new ArrayList<>();

		List<String> versionDesc = null;
		versions.add(CustomString.getString(WebsiteActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.website.noupdate"));
		File webfolder = WebsiteActivator.getDefault().getFiles();
		if (webfolder.exists()) {
			for (File folder : webfolder.listFiles()) {
				for (File f : folder.listFiles()) {
					if (f.getName().compareTo(DeviceTargetUtil.FILE_VERSION) == 0) {
						try (FileReader fr = new FileReader(f); BufferedReader br = new BufferedReader(fr);) {
							String line = br.readLine();
							versions.add(line);
							version2folder.put(line, folder.getName());
							versionDesc = versionsDesc.get(line);
							if (versionDesc == null) {
								versionDesc = new ArrayList<>();
								versionsDesc.put(line, versionDesc);
							}
							// read all lines
							while ((line = br.readLine()) != null) {
								versionDesc.add(line);
							}
						} catch (Exception e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
			}
		}

		return versions.toArray(new String[0]);
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

		Label lblVersion = new Label(container, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVersion.setText(CustomString.getString(WebsiteActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.version"));

		cmbWebsiteVersion = new Combo(container, SWT.READ_ONLY);
		cmbWebsiteVersion.setItems(listWebsiteVersions());
		cmbWebsiteVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbWebsiteVersion.select(0);
		cmbWebsiteVersion.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbWebsiteVersion.getSelectionIndex() > 0) {
					String facts = "";
					String break_ = "";
					List<String> lines = versionsDesc.get(cmbWebsiteVersion.getText());
					for (String fact : lines) {
						facts += break_ + fact;
						break_ = "\n";
					}
					txtFacts.setText(facts);
				} else {
					txtFacts.setText("");
				}
			}
		});

		new Label(container, SWT.NONE);

		Label lblFacts = new Label(container, SWT.NONE);
		lblFacts.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblFacts.setText(CustomString.getString(WebsiteActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.facts"));

		txtFacts = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);

		GridData gd_txtFacts = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtFacts.heightHint = 125;
//		gd_txtFacts.grabExcessVerticalSpace = true;
		txtFacts.setLayoutData(gd_txtFacts);

		Button btnDesc = new Button(container, SWT.NONE);
		btnDesc.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		btnDesc.setText(CustomString.getString(WebsiteActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.website.wizard.opendocu"));
		btnDesc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Desktop.isDesktopSupported()) {
					String version = version2folder.get(cmbWebsiteVersion.getText());
					if (version == null || version.isEmpty())
						return;
					File dokufile = WebsiteActivator.getDefault().getDocuFile(version);
					try {
						Desktop.getDesktop().open(dokufile);
					} catch (IOException e1) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
					}
				}
			}
		});

		setHandler();

		this.cmbWebsiteVersion.notifyListeners(SWT.Selection, new Event());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.cmbWebsiteVersion, "message");
		// select();
	}

	@Override
	public boolean isPageComplete() {

		return true;
	}


	@Override
	public void setVisible(boolean visible) {
		/*if (visible) {
			setValue();
		}*/

		super.setVisible(visible);
	}

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	private void fill() {
		if (filesystem instanceof SimpleFileSystem) {
			this.cmbWebsiteVersion.select(0);
		} else {
			this.cmbWebsiteVersion.select(1);
		}

	}

	/*
	private void setValue() {
		this.filesystem = new SimpleFileSystem();

		BroadcastMessage e1 = null;

		if (e1 == null) {
			return;
		}

		this.filesystem = new DataHubFileSystem();
		this.filesystem.setConnectionName(e1.getConnectionName());
		// this.filesystem.setHMIRootPath(path);
		this.filesystem.setHostName(e1.getHost());
		// this.filesystem.setJavaArg(value);
		this.filesystem.setUser(e1.getUsername());
		this.filesystem.setPassword(e1.getPassword());
		this.filesystem.setTargetFileSeparator(e1.getFileSeparator());
		this.filesystem.setTimeOut(Integer.parseInt(e1.getConnectionTimeout()));
		this.filesystem.setRootPath(e1.getRootpath());

		fill();
	}*/

	private void setHandler() {
		cmbWebsiteVersion.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				// index 0 is empty selection
				websiteVersionIndex = cmbWebsiteVersion.getSelectionIndex() -1;
				if(websiteVersionIndex > 0) {
					boolean isComplete = isPageComplete();
					setPageComplete(isComplete);
				}
			}
		});
	}

	public int getWebVersionIndex() {
		return websiteVersionIndex;
	}

}
