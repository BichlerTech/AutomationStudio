package com.bichler.astudio.device.wakeonlan.wizard;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.bichler.astudio.device.wakeonlan.WakeonlanActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class WakeOnLanInstallationWizardPage extends WizardPage {

	private Combo cmbWakeOnLanVersion;
	private int wakeOnLanVersionIndex;
	private IFileSystem filesystem = null;
	private Text txtFacts;
	
	private Map<String, String> version2folder = new LinkedHashMap<>();
	//private Map<String, String> mappingWakeonlanFiles = new LinkedHashMap<>();
	
	private Map<String, List<String>> versionsDesc = new HashMap<>();

	/**
	 * Create the wizard.
	 * 
	 * @param pageOne
	 * @param isnew
	 * 
	 * @wbp.parser.constructor
	 */
	public WakeOnLanInstallationWizardPage() {
		super("newdevice");

		setTitle(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.wakeonlan.page.title"));
		setDescription(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.wakeonlan.page.description"));
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
		lblVersion.setText(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.version"));

		cmbWakeOnLanVersion = new Combo(container, SWT.READ_ONLY);
		cmbWakeOnLanVersion.setItems(listWakeOnLanVersions());
		cmbWakeOnLanVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbWakeOnLanVersion.select(0);
		cmbWakeOnLanVersion.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbWakeOnLanVersion.getSelectionIndex() > 0) {
					String facts = "";
					String break_ = "";
					List<String> lines = versionsDesc.get(cmbWakeOnLanVersion.getText());
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
		lblFacts.setText(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.facts"));

		txtFacts = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);

		GridData gd_txtFacts = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtFacts.heightHint = 125;
//		gd_txtFacts.grabExcessVerticalSpace = true;
		txtFacts.setLayoutData(gd_txtFacts);

		Button btnDesc = new Button(container, SWT.NONE);
		btnDesc.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnDesc.setText(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.wakeonlan.wizard.opendocu"));
		btnDesc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Desktop.isDesktopSupported()) {
					String version = version2folder.get(cmbWakeOnLanVersion.getText());
					if (version == null || version.isEmpty())
						return;
					File dokufile = WakeonlanActivator.getDefault().getDocuFile(version);
					try {
						Desktop.getDesktop().open(dokufile);
					} catch (IOException e1) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
					}
				}
			}
		});

		setHandler();

		this.cmbWakeOnLanVersion.notifyListeners(SWT.Selection, new Event());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.cmbWakeOnLanVersion, "message");
		// select();
	}

	@Override
	public boolean isPageComplete() {

		return true;
	}

	/*
	public void setEdit() {

		setTitle(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.edithost.title"));
		setDescription(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.edithost.description"));

		fill();
	}*/

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
			this.cmbWakeOnLanVersion.select(0);
		} else {
			this.cmbWakeOnLanVersion.select(1);
		}
	}
	
	private String[] listWakeOnLanVersions() {
		List<String> versions = new ArrayList<>();
		versions.add(CustomString.getString(WakeonlanActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.wakeonlan.noupdate"));
		File wakeonlanFolder = WakeonlanActivator.getDefault().getFiles();		
		if (wakeonlanFolder.exists()) {
			for (File folder : wakeonlanFolder.listFiles()) {
				for (File f : folder.listFiles()) {	
					if (f.getName().compareTo(DeviceTargetUtil.FILE_VERSION) == 0) {
						try (FileReader fr = new FileReader(f); BufferedReader br = new BufferedReader(fr);) {
							String line = br.readLine();
							versions.add(line);
							version2folder.put(line, folder.getName());
							List<String> versionDesc = versionsDesc.get(line);
							if(versionDesc == null) {
								versionDesc = new ArrayList<String>();
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

	/*private void mappingFile(String line) {
		if(line.startsWith("#")) {
			return ;
		}
		
		String[] path = line.split(":");
		if(path.length <= 1) {
			return;
		}
		
		this.mappingWakeonlanFiles.put(path[0], path[1]);
	}*/
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
		cmbWakeOnLanVersion.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				wakeOnLanVersionIndex = cmbWakeOnLanVersion.getSelectionIndex() -1;

				if(wakeOnLanVersionIndex > 0) {
					boolean isComplete = isPageComplete();
					setPageComplete(isComplete);
				}
			}
		});

	}

	public int getWakeOnLanVersionIndex() {
		return wakeOnLanVersionIndex;
	}
	

	/*
	public Map<String, String> getFileMappingWakeonlan(){
		return this.mappingWakeonlanFiles;
	} */
}
