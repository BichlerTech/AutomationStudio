package com.bichler.astudio.opcua.wizard.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUAServerDriverWizardPage extends WizardPage {
	private static final String EDITORPREFIX = "com.bichler.astudio.editor.";
	@SuppressWarnings("unused")
	private IWorkbench workbench = null;
	@SuppressWarnings("unused")
	private IStructuredSelection selection = null;
	private Combo cmbDRVType;
	private ComboViewer cmbTypeViewer;
	private IFileSystem filesystem = null;
	private ComboViewer cmbDRVVersion;
	private String drvName = "";
	private String drvType = "";
	private String drvVersion;
	private String[] existingDrivers;
	private Text lblDRVDescription;
	private List<String> insertedDriver = new ArrayList<>();

	/**
	 * Create the wizard.
	 * 
	 * @wbp.parser.constructor
	 */
	public NewOPCUAServerDriverWizardPage(IWorkbench workbench, IStructuredSelection selection) {
		this();
		this.workbench = workbench;
		this.selection = selection;
		Object element = ((TreeSelection) selection).getFirstElement();
		if (element instanceof StudioModelNode) {
			filesystem = ((StudioModelNode) element).getFilesystem();
			initAvailableDriverNames();
		}
	}

	/**
	 * Create the wizard.
	 * 
	 * @param filesystem2
	 */
	public NewOPCUAServerDriverWizardPage() {
		super("wizardPage");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.serverdriver.title"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.serverdriver.description"));
		ImageDescriptor desc = StudioImageActivator.getImageDescriptor(StudioImages.ICON_WIZARD_DRIVER_ADD);
		setImageDescriptor(desc);
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		Label lblDRVType = new Label(container, SWT.NONE);
		lblDRVType.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.drivertype"));
		cmbTypeViewer = new ComboViewer(container, SWT.READ_ONLY);
		cmbTypeViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof InternationalActivator) {
					InternationalActivator current = (InternationalActivator) element;
					String type = current.getBundle().getSymbolicName().replace(EDITORPREFIX, "");
					return type.substring(0, type.indexOf('.'));
				}
				return super.getText(element);
			}
		});
		cmbTypeViewer.addSelectionChangedListener(event -> {
			IStructuredSelection tselection = (IStructuredSelection) event.getSelection();
			String type = ((InternationalActivator) tselection.getFirstElement()).getBundle().getSymbolicName()
					.replace(EDITORPREFIX, "");
			drvType = type.substring(0, type.indexOf('.'));
			fillDRVVersions();
			boolean isComplete = isPageComplete();
			setPageComplete(isComplete);
		});
		cmbTypeViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblDRVVersion = new Label(container, SWT.NONE);
		lblDRVVersion
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.driverversion"));
		cmbDRVVersion = new ComboViewer(container, SWT.READ_ONLY);
		cmbDRVVersion.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbDRVVersion.addSelectionChangedListener(event -> {
			IStructuredSelection vselection = (IStructuredSelection) event.getSelection();
			if (vselection.isEmpty())
				drvVersion = "";
			else {
				drvVersion = cmbDRVVersion.getCombo().getText();
			}
			InternationalActivator bd = (InternationalActivator) vselection.getFirstElement();
			setDRVDescription(bd);
			boolean isComplete = isPageComplete();
			setPageComplete(isComplete);
		});
		cmbDRVVersion.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof InternationalActivator) {
					InternationalActivator current = (InternationalActivator) element;
					return getDriverVersion(current);
				}
				return super.getText(element);
			}
		});
		Label lblDRVName = new Label(container, SWT.NONE);
		lblDRVName
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.drivername"));
		Text txtDRVName = new Text(container, SWT.BORDER);
		txtDRVName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDRVName.addModifyListener(e -> {
			drvName = txtDRVName.getText();
			boolean isComplete = isPageComplete();
			setPageComplete(isComplete);
		});
		new Label(container, SWT.NONE);
		lblDRVDescription = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		lblDRVDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.fillDriverSelection();
		setPageComplete(false);
	}

	public void fillDriverSelection() {
		for (InternationalActivator bd : OPCUADriverRegistry.drivers.values()) {
			String type = bd.getBundle().getSymbolicName().replace(EDITORPREFIX, "");
			type = type.substring(0, type.indexOf('.'));
			if (!insertedDriver.contains(type)) {
				insertedDriver.add(type);
				cmbTypeViewer.add(bd);
			}
		}
	}

	public String getDrvName() {
		return drvName;
	}

	public String getDrvType() {
		return drvType;
	}

	public void setSelectionDriver(String driver) {
		cmbDRVType.setText(driver);
		cmbDRVType.notifyListeners(SWT.Selection, new Event());
	}

	@Override
	public boolean isPageComplete() {
		boolean isValid = true;
		setErrorMessage(null);
		// validate driver type
		if (this.drvType == null || this.drvType.isEmpty()) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.serverdriver.error.drivertype"));
			return false;
		}
		// validate driver version
		if (this.drvVersion == null || this.drvVersion.isEmpty()) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.serverdriver.error.driverversion"));
			return false;
		}
		// validate driver name
		if (this.drvName == null || this.drvName.isEmpty()) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.serverdriver.error.drivername"));
			return false;
		} else {
			if (this.existingDrivers != null) {
				for (String name : this.existingDrivers) {
					if (this.drvName.equals(name)) {
						isValid = false;
						setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
								"wizard.serverdriver.error.drivername.exists"));
						break;
					}
				}
			}
		}
		return isValid;
	}

	public String getDrvVersion() {
		return drvVersion;
	}

	private String getDriverVersion(InternationalActivator bundle) {
		String version = bundle.getBundle().getVersion().toString();
		version = version.substring(0, version.lastIndexOf('.'));
		return version;
	}

	private void setDRVDescription(InternationalActivator bd) {
		if (bd == null || bd.getBundle() == null)
			lblDRVDescription.setText("");
		else {
			String description = CustomString.getString(bd.RESOURCE_BUNDLE, "driver.vendor.label") + " "
					+ bd.getBundle().getHeaders().get("Bundle-Vendor") + "\n"
					+ CustomString.getString(bd.RESOURCE_BUNDLE, "driver.date.label") + " \n\n" +
					// "Vendor: "+ bd.getBundle().getHeaders().get("Bundle-Vendor") + "\n"+
					CustomString.getString(bd.RESOURCE_BUNDLE, "driver.description");
			lblDRVDescription.setText(description);
		}
	}

	private void fillDRVVersions() {
		cmbDRVVersion.refresh();

		Map<String, InternationalActivator> bundles = new HashMap<>();
		for (InternationalActivator bd : OPCUADriverRegistry.drivers.values()) {
			if (bd.getBundle().getSymbolicName().contains(drvType)) {
				String version = getDriverVersion(bd);
				bundles.put(version, bd);
			}
		}
		// get latest version
		Iterator<Entry<String, InternationalActivator>> iterator = bundles.entrySet().iterator();
		String lastVersion = null;
		while (iterator.hasNext()) {
			Entry<String, InternationalActivator> value = iterator.next();
			lastVersion = value.getKey();
		}

		if (lastVersion == null) {
			// TODO: Logger
			Logger.getLogger(getClass().getName()).log(Level.INFO, "There is driver version for driver " + drvType);
			return;
		}

		InternationalActivator latestBundle = bundles.get(lastVersion);
		cmbDRVVersion.add(latestBundle);
		// Add all driver versions
		// for (InternationalActivator bd : OPCUADriverRegistry.drivers.values()) {
		// if (bd.getBundle().getSymbolicName().contains(drvType)) {
		// cmbDRVVersion.add(bd);
		// }
		// }
	}

	private void initAvailableDriverNames() {
		// instantiate filesystem
		IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
		// find directory to match files (directories)
		String root = this.filesystem.getRootPath();
		try {
			String path = new Path(root).append(opcuastore.getString(OPCUAConstants.ASOPCUADriversFolder)).toOSString();
			// store existing projects
			String[] drivers = filesystem.listDirs(path);
			for (InternationalActivator bundle : OPCUADriverRegistry.drivers.values()) {
				// bundle.get
				bundle.getBundle().getVersion();
			}
			if (drivers != null) {
				this.existingDrivers = drivers;
			}
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}
}
