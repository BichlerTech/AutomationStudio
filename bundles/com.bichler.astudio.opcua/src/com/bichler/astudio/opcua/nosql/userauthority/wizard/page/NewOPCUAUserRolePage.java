package com.bichler.astudio.opcua.nosql.userauthority.wizard.page;

import opc.sdk.core.node.user.DBRole;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUAUserRolePage extends WizardPage {
	private Text txt_name;
	private String roleName = "";
	private String roleDescription = "";
	private DBRole role = null;
	private Text txt_description;

	/**
	 * Create the wizard.
	 */
	public NewOPCUAUserRolePage() {
		super("roles");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.user.role"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.description"));
	}

	/**
	 * @wbp.parser.constructor
	 */
	public NewOPCUAUserRolePage(DBRole role) {
		this();
		this.role = role;
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

		Label lbl_name = new Label(container, SWT.NONE);
		lbl_name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_name.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name"));

		txt_name = new Text(container, SWT.BORDER);
		txt_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lbl_description = new Label(container, SWT.NONE);
		lbl_description.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_description.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description"));

		txt_description = new Text(container, SWT.BORDER);
		txt_description.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		setHandler();

		fillInput();
	}

	@Override
	public boolean isPageComplete() {
		boolean isValid = true;
		// only some text is valid
		if (this.roleName.isEmpty()) {
			isValid = false;
		}

		return isValid;
	}

	private void fillInput() {
		if (this.role == null) {
			return;
		}

		this.txt_name.setText(this.role.getRolename());
		this.txt_name.notifyListeners(SWT.Modify, new Event());

		this.txt_description.setText(this.role.getDescription());
		this.txt_description.notifyListeners(SWT.Modify, new Event());
	}

	private void setHandler() {
		this.txt_name.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				roleName = ((Text) e.getSource()).getText();
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.txt_description.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				roleDescription = ((Text) e.getSource()).getText();
			}
		});
	}

	public String getRoleName() {
		return this.roleName;
	}

	public String getRoleDescription() {
		return this.roleDescription;
	}

}
