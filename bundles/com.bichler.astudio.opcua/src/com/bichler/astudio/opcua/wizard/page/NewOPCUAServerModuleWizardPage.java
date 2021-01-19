package com.bichler.astudio.opcua.wizard.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.bichler.astudio.opcua.OPCUAModuleRegistry;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUAServerModuleWizardPage extends WizardPage {
	private static final String EDITORPREFIX = "com.bichler.astudio.editor.";
	@SuppressWarnings("unused")
	private IWorkbench workbench = null;
	@SuppressWarnings("unused")
	private IStructuredSelection selection = null;
	private Combo cmbMODType;
	private ComboViewer cmbTypeViewer;
	private IFileSystem filesystem = null;
	private ComboViewer cmbMODVersion;
	private String modName = "";
	private String modType = "";
	private String modVersion;
	private String[] existingModules;
	private Text lblMODDescription;
	private List<String> insertedModule = new ArrayList<>();

	/**
	 * Create the wizard.
	 * 
	 * @wbp.parser.constructor
	 */
	public NewOPCUAServerModuleWizardPage(IWorkbench workbench, IStructuredSelection selection) {
		this();
		this.workbench = workbench;
		this.selection = selection;
		Object element = ((TreeSelection) selection).getFirstElement();
		if (element instanceof StudioModelNode) {
			filesystem = ((StudioModelNode) element).getFilesystem();
			initAvailableModuleNames();
		}
	}

	/**
	 * Create the wizard.
	 * 
	 * @param filesystem2
	 */
	public NewOPCUAServerModuleWizardPage() {
		super("wizardPage");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.servermodule.title"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.servermodule.description"));
		ImageDescriptor desc = StudioImageActivator.getImageDescriptor(StudioImages.ICON_WIZARD_DRIVER_ADD);
		setImageDescriptor(desc);
	}

	private void initAvailableModuleNames() {
		// instantiate filesystem
		IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
		// find directory to match files (directories)
		String root = this.filesystem.getRootPath();
		try {
			String path = new Path(root).append(opcuastore.getString(OPCUAConstants.ASOPCUAModulesFolder)).toOSString();
			// store existing projects
			String[] modules = filesystem.listDirs(path);
			for (InternationalActivator bundle : OPCUAModuleRegistry.modules.values()) {
				// bundle.get
				bundle.getBundle().getVersion();
			}
			if (modules != null) {
				this.existingModules = modules;
			}
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
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
		container.setLayout(new GridLayout(2, false));
		Label lblMODType = new Label(container, SWT.NONE);
		lblMODType.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.moduletype"));
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
			modType = type.substring(0, type.indexOf('.'));
			fillMODVersions();
			boolean isComplete = isPageComplete();
			setPageComplete(isComplete);
		});
		cmbTypeViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblMODVersion = new Label(container, SWT.NONE);
		lblMODVersion
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.moduleversion"));
		cmbMODVersion = new ComboViewer(container, SWT.READ_ONLY);
		cmbMODVersion.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbMODVersion.addSelectionChangedListener(event -> {
			IStructuredSelection vselection = (IStructuredSelection) event.getSelection();
			if (vselection.isEmpty())
				modVersion = "";
			else {
				modVersion = cmbMODVersion.getCombo().getText();
			}
			InternationalActivator bd = (InternationalActivator) vselection.getFirstElement();
			setMODDescription(bd);
			boolean isComplete = isPageComplete();
			setPageComplete(isComplete);
		});
		cmbMODVersion.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof InternationalActivator) {
					InternationalActivator current = (InternationalActivator) element;
					String version = current.getBundle().getVersion().toString();
					version = version.substring(0, version.lastIndexOf('.'));
					return version;
				}
				return super.getText(element);
			}
		});
		Label lblMODName = new Label(container, SWT.NONE);
		lblMODName
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.modulename"));
		Text txtMODName = new Text(container, SWT.BORDER);
		txtMODName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtMODName.addModifyListener(e -> {
			modName = txtMODName.getText();
			boolean isComplete = isPageComplete();
			setPageComplete(isComplete);
		});
		new Label(container, SWT.NONE);
		lblMODDescription = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		lblMODDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.fillModuleSelection();
		setPageComplete(false);
	}

	private void setMODDescription(InternationalActivator bd) {
		if (bd == null || bd.getBundle() == null)
			lblMODDescription.setText("");
		else {
			String description = CustomString.getString(bd.RESOURCE_BUNDLE, "module.vendor.label") + " "
					+ bd.getBundle().getHeaders().get("Bundle-Vendor") + "\n"
					+ CustomString.getString(bd.RESOURCE_BUNDLE, "module.date.label") + " \n\n" +
					// "Vendor: "+ bd.getBundle().getHeaders().get("Bundle-Vendor") + "\n"+
					CustomString.getString(bd.RESOURCE_BUNDLE, "module.description");
			lblMODDescription.setText(description);
		}
	}

	private void fillMODVersions() {
		cmbMODVersion.refresh();
		for (InternationalActivator bd : OPCUAModuleRegistry.modules.values()) {
			if (bd.getBundle().getSymbolicName().contains(modType)) {
				cmbMODVersion.add(bd);
			}
		}
	}

	public void fillModuleSelection() {
		for (InternationalActivator bd : OPCUAModuleRegistry.modules.values()) {
			String type = bd.getBundle().getSymbolicName().replace(EDITORPREFIX, "");
			type = type.substring(0, type.indexOf('.'));
			if (!insertedModule.contains(type)) {
				insertedModule.add(type);
				cmbTypeViewer.add(bd);
			}
		}
	}

	public String getDrvName() {
		return modName;
	}

	public String getDrvType() {
		return modType;
	}

	public void setSelectionModule(String module) {
		cmbMODType.setText(module);
		cmbMODType.notifyListeners(SWT.Selection, new Event());
	}

	@Override
	public boolean isPageComplete() {
		boolean isValid = true;
		setErrorMessage(null);
		// validate module type
		if (this.modType == null || this.modType.isEmpty()) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.servermodule.error.moduletype"));
			return false;
		}
		// validate module version
		if (this.modVersion == null || this.modVersion.isEmpty()) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.servermodule.error.moduleversion"));
			return false;
		}
		// validate module name
		if (this.modName == null || this.modName.isEmpty()) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.servermodule.error.modulename"));
			return false;
		} else {
			if (this.existingModules != null) {
				for (String name : this.existingModules) {
					if (this.modName.equals(name)) {
						isValid = false;
						setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
								"wizard.servermodule.error.modulename.exists"));
						break;
					}
				}
			}
		}
		return isValid;
	}

	public String getDrvVersion() {
		return modVersion;
	}
}
