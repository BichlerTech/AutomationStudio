package com.bichler.astudio.editor.siemens.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.wizard.ExportType;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SiemensImportTypeWizardPage extends WizardPage {
	private IFileSystem filesystem;
	private SiemensDBResourceManager structManager;
	private ExportType type;
	private SiemensImportWizardPage2 importPage;

	/**
	 * Create the wizard.
	 * 
	 * @param structManager
	 * @param filesystem
	 */
	public SiemensImportTypeWizardPage(IFileSystem filesystem, SiemensDBResourceManager structManager) {
		super("wizardPage");
		setTitle(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.wizard.type.title"));
		setDescription(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.wizard.type.description"));
		this.filesystem = filesystem;
		this.structManager = structManager;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FillLayout(SWT.VERTICAL));
		Button rdo_default = new Button(container, SWT.RADIO);
		rdo_default.setText(
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.wizard.type.default"));
		rdo_default.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				type = ExportType.Default;
				importPage.setExportType(type);
			}
		});
		Button rdo_tia = new Button(container, SWT.RADIO);
		rdo_tia.setText(
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.wizard.type.tia"));
		rdo_tia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				type = ExportType.Tia;
				importPage.setExportType(type);
			}
		});
	}

	public ExportType getType() {
		return this.type;
	}

	public void setImportPage(SiemensImportWizardPage2 page) {
		this.importPage = page;
	}
}
