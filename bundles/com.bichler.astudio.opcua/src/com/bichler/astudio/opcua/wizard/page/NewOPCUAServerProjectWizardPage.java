package com.bichler.astudio.opcua.wizard.page;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;
import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUAServerProjectWizardPage extends WizardPage {
	private IFileSystem filesystem = null;

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	private IWorkbench workbench = null;
	private IStructuredSelection selection = null;
	private Text txtName = null;
	private Text txtModel = null;
	private Text txtVersion = null;
	// private Combo combo = null;
	private String newServerName = "";
	private String externModel = "";
	private Button btnOpenExternalModel;
	private String newServerVersion = "";
	private String history = "";
	private String[] existingProjects = new String[0];

	public String getNewServerName() {
		return this.newServerName;
	}

	public String getExternModel() {
		return externModel;
	}

	/**
	 * Create the wizard.
	 */
	public NewOPCUAServerProjectWizardPage(IWorkbench workbench, IStructuredSelection selection) {
		//
		super("wizardPage");
		this.workbench = workbench;
		this.selection = selection;
		this.filesystem = ((OPCUARootModelNode) ((TreeSelection) selection).getFirstElement()).getFilesystem();
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.title"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.description"));
		ImageDescriptor desc = new ImageDescriptor() {
			@Override
			public ImageData getImageData() {
				return StudioImageActivator.getImage(StudioImages.ICON_WIZARD_OPC_UA_SERVER_ADD).getImageData();
			}
		};
		setImageDescriptor(desc);
		initAvailableServerNames();
	}

	/**
	 * Create the wizard.
	 * 
	 * @wbp.parser.constructor
	 */
	public NewOPCUAServerProjectWizardPage() {
		super("wizardPage");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.title"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.description"));
		ImageDescriptor desc = new ImageDescriptor() {
			@Override
			public ImageData getImageData() {
				return StudioImageActivator.getImage(StudioImages.ICON_WIZARD_OPC_UA_SERVER_ADD).getImageData();
			}
		};
		setImageDescriptor(desc);
		initAvailableServerNames();
	}

	private void initAvailableServerNames() {
		IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
		filesystem.setTargetFileSeparator(File.separator);
		// find directory to match files (directories)
		String root = this.filesystem.getRootPath();
		try {
			String path = new Path(root).append(opcuastore.getString(OPCUAConstants.OPCUARuntime))
					.append(opcuastore.getString(OPCUAConstants.ASOPCUAServersPath)).toOSString();
			// store existing projects
			String[] projects = filesystem.listDirs(path);
			if (projects != null) {
				this.existingProjects = projects;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		Label lblServername = new Label(container, SWT.NONE);
		lblServername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServername.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.servername"));
		txtName = new Text(container, SWT.BORDER);
//		gd_text.widthHint = 350;
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				newServerName = txtName.getText();
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		new Label(container, SWT.NONE);
		
		Label lblexternalModel = new Label(container, SWT.NONE);
		lblexternalModel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblexternalModel.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.externalmodel"));
		txtModel = new Text(container, SWT.BORDER);
//		gd_model.widthHint = 350;
		txtModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtModel.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				externModel = txtModel.getText();
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		btnOpenExternalModel = new Button(container, SWT.NONE);
		btnOpenExternalModel.setText("...");
		GridData gd_btnmodel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnmodel.widthHint = 50;
		btnOpenExternalModel.setLayoutData(gd_btnmodel);
		btnOpenExternalModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell());
				dialog.setFilterExtensions(DesignerConstants.EXTENSION_INFORMATIONMODEL);
				String path = dialog.open();
				if (path != null) {
					txtModel.setText(path);
				}
			}
		});

		Label lblServerversion = new Label(container, SWT.NONE);
		lblServerversion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerversion
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.version"));
		txtVersion = new Text(container, SWT.BORDER);
		GridData gd_version = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_version.widthHint = 196;
		txtVersion.setLayoutData(gd_version);
		txtVersion.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setNewServerVersion(txtVersion.getText());
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	
		new Label(container, SWT.NONE);

		
//    btnOpenExternalModel.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
//			"com.bichler.astudio.device.opcua.handler.wizard.deviceinstall.page.browse"));

		Label lblServerConfiguration = new Label(container, SWT.NONE);
		lblServerConfiguration.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblServerConfiguration
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.server.history"));
		Text historyArea = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		historyArea.setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		historyArea.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setHistory(historyArea.getText());
			}
		});
		historyArea.setText(
				"-----------------------------------------------\r\n@Version:\t\r\n@Autor:\tThomas\r\n@Datum:\tWed Jan 13 16:08:17 CET 2021\r\n@Beschreibung:  \r\n-----------------------------------------------\r\n");
		
		new Label(container, SWT.NONE);
		
		boolean isComplete = isPageComplete();
		setPageComplete(isComplete);
	}

	@Override
	public boolean isPageComplete() {
		boolean valid = true;
		setErrorMessage(null);
		if (this.newServerName == null) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.server.error.projectname"));
			return false;
		} else if (newServerName.isEmpty()) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.server.error.projectname"));
			return false;
		} else {
			for (String name : this.existingProjects) {
				if (this.newServerName.equals(name)) {
					valid = false;
					setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"wizard.server.error.projectname.exists"));
					break;
				}
			}
		}
		return valid;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getNewServerVersion() {
		return newServerVersion;
	}

	public void setNewServerVersion(String newServerVersion) {
		this.newServerVersion = newServerVersion;
	}
}
