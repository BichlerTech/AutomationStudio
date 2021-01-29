package com.bichler.astudio.device.opcua.wizard.page.selection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.options.DefaultSendOptions;
import com.bichler.astudio.device.opcua.options.OS_Startup;
import com.bichler.astudio.device.opcua.options.SendOptions;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.log.ASLogActivator;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.internationalization.CustomString;

public class StartupWizardPage extends WizardPage {
	private Text txt_path;
	private Button btn_javatarget;
	private Combo cmb_os;
	private Combo cmb_toolChain;
	private SendOptions options;

	public StartupWizardPage(IFileSystem filesystem) {
		super("platform");
		
		setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.startup.page.title"));
		setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.startup.page.description"));
		
		this.options = new DefaultSendOptions();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		Label lblOS = new Label(container, SWT.NONE);
		lblOS.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOS.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.startup.os"));
		this.cmb_os = new Combo(container, SWT.READ_ONLY);
		cmb_os.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
		boolean doCompileJar = store.getBoolean(OPCUAConstants.OPCUADoCompileJar);
		boolean doCompileAnsiC = store.getBoolean(OPCUAConstants.OPCUADoCompileAnsiC);
		boolean isToolchain = DeviceActivator.getDefault().isToolchainInstalled();
		if(!isToolchain) {
			ASLogActivator.getDefault().getLogger().log(Level.INFO, "Toolchain not found!"
//					CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
//							"com.bichler.astudio.device.opcua.handler.abstractcompile.log.error.upload")
			);
		}
		else if (doCompileAnsiC) {
			Label lblAnsiCTarget = new Label(container, SWT.NONE);
			lblAnsiCTarget.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblAnsiCTarget.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
					"com.bichler.astudio.device.opcua.handler.wizard.startup.ansic"));
			this.cmb_toolChain = new Combo(container, SWT.READ_ONLY);
			cmb_toolChain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

			fillCToolchain();
		}
		if (doCompileJar) {

			Label lblJavaTarget = new Label(container, SWT.NONE);
			lblJavaTarget.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblJavaTarget.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
					"com.bichler.astudio.device.opcua.handler.wizard.startup.java"));
			this.txt_path = new Text(container, SWT.BORDER);
			txt_path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			this.btn_javatarget = new Button(container, SWT.NONE);
			btn_javatarget.setImage(StudioImageActivator.getImage(StudioImages.ICON_SEARCH));
			btn_javatarget.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
					"upload.wizard.startup.browse.tooltip"));
			fillJavaToolchain();
			setHandlerJava();
		}
		setHandler();
		fillInput();
	}

	private void fillCToolchain() {
		// if ansi c is selected, allow toolchain selection
		URL compiler = FileLocator.find(DeviceActivator.getDefault().getBundle(),
				Path.ROOT.append("toolchain").append("compiler"), null);
		URL compiler2 = null;
		try {
			compiler2 = FileLocator.toFileURL(compiler);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		File compilerFile = new File(compiler2.getFile());
		File[] files = compilerFile.listFiles();
		
		for (File f : files) {
			this.cmb_toolChain.add(f.getName());
		}
	}

	private void fillJavaToolchain() {
		IVMInstall jre = JavaRuntime.getDefaultVMInstall();
		String path = jre.getInstallLocation().getPath();
		this.txt_path.setText(path + File.separator + "bin");
		this.options.setJavaPath(path + File.separator + "bin");
		txt_path.notifyListeners(SWT.Modify, new Event());
	}

	private void fillInput() {
		this.cmb_os.add("Windows");
		this.cmb_os.add("Linux");
		this.cmb_os.add("Mac");

		String systemOs = System.getProperty("os.name").toLowerCase();
		if (systemOs == null) {
		} else if (systemOs.startsWith("windows")) {
			this.cmb_os.select(0);
		} else if (systemOs.startsWith("linux")) {
			this.cmb_os.select(1);
		} else if (systemOs.startsWith("mac")) {
			this.cmb_os.select(2);
		}
		this.cmb_os.notifyListeners(SWT.Selection, new Event());
		this.options.setOs(OS_Startup.WINDOWS);
		this.cmb_os.setEnabled(false);
	}

	private void setHandlerJava() {
		this.txt_path.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String path = txt_path.getText();
				options.setJavaPath(path);
			}
		});
		this.btn_javatarget.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = openJavaPath();
				if (path != null) {
					txt_path.setText(path);
					txt_path.notifyListeners(SWT.Modify, new Event());
				}
			}
		});
	}
	
	private void setHandler() {
		this.cmb_os.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = ((Combo) e.getSource()).getSelectionIndex();
				switch (index) {
				case 0:
					options.setOs(OS_Startup.WINDOWS);
					break;
				case 1:
					options.setOs(OS_Startup.LINUX);
					break;
				case 2:
					options.setOs(OS_Startup.MAC);
					break;
				}
			}
		});
	}

	private String openJavaPath() {
		DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.OPEN);
		String path = dialog.open();
		return path;
	}

	public SendOptions getSendOptions() {
		return this.options;
	}

	public void doChangeOptions(/* IFileSystem filesystem, */
			boolean comboxUpload) {
		this.options.setComboxUpload(comboxUpload);
	}
}
