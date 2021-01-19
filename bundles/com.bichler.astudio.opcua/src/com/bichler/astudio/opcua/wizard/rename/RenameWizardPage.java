package com.bichler.astudio.opcua.wizard.rename;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class RenameWizardPage extends WizardPage {
	private Text txtName;
	private String rename = "";
	private String name = "";

	/**
	 * Create the wizard.
	 */
	public RenameWizardPage(String name) {
		super("wizardPage");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.rename.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.rename.title"));
		this.name = name;
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
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name"));
		this.txtName = new Text(container, SWT.BORDER);
		this.txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.txtName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				rename = ((Text) e.getSource()).getText();
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		setInput();
	}

	private void setInput() {
		this.txtName.setText(this.name);
	}

	public String getRename() {
		return this.rename;
	}

	@Override
	public boolean isPageComplete() {
		if (this.name.equals(this.rename)) {
			setErrorMessage(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"wizard.rename.error.samename"));
			return false;
		} else if (this.rename.contains(" ")) {
			setErrorMessage(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.rename.error.space"));
			return false;
		}
		setErrorMessage(null);
//    setMessage("all right", INFORMATION);
		return true;
	}
}
