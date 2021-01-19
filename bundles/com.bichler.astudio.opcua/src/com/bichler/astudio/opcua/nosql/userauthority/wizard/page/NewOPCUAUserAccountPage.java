package com.bichler.astudio.opcua.nosql.userauthority.wizard.page;

import java.util.ArrayList;

import opc.sdk.core.node.user.DBRole;
import opc.sdk.core.node.user.DBUser;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUAUserAccountPage extends WizardPage {

	private DBRole[] selectedRoles = new DBRole[0];
	private String username = "";
	private String password = "";
	private String userDescription = "";
	private Text txt_name;
	private Text txt_password;
	// private Combo cmb_role;
	private DBRole[] roles;
	private DBUser user = null;
	private Button btn_addRoleToUser;
	private Button btn_removeRoleFromUser;
	private ListViewer lv_roles;
	private ListViewer lv_userroles;
	private Text txt_description;

	/**
	 * Create the wizard.
	 * 
	 * @param roles
	 * @wbp.parser.constructor
	 */
	public NewOPCUAUserAccountPage(DBRole[] roles) {
		super("useraccount");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.user.new"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.user.new.description"));
		this.roles = roles;
	}

	public NewOPCUAUserAccountPage(DBRole[] roles, DBUser user) {
		this(roles);
		if (user != null) {
			setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.edituser.title"));
			setDescription(
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.edituser.description"));
		}
		this.user = user;
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
		lblName.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.user"));

		txt_name = new Text(container, SWT.BORDER);
		txt_name.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblPasswort = new Label(container, SWT.NONE);
		lblPasswort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPasswort
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.password"));

		txt_password = new Text(container, SWT.BORDER);
		txt_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label separator1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description"));

		txt_description = new Text(container, SWT.BORDER);
		txt_description.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label separator2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		separator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Label lblRolle = new Label(container, SWT.NONE);
		lblRolle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblRolle.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.user.role"));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));

		this.lv_roles = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List list_roles = lv_roles.getList();
		list_roles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeButton = new Composite(composite, SWT.NONE);
		compositeButton.setLayout(new GridLayout(1, false));
		compositeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		this.btn_addRoleToUser = new Button(compositeButton, SWT.NONE);
		btn_addRoleToUser.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btn_addRoleToUser.setText("-->");
		this.btn_removeRoleFromUser = new Button(compositeButton, SWT.NONE);
		btn_removeRoleFromUser.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btn_removeRoleFromUser.setText("<--");

		this.lv_userroles = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List List_userroles = lv_userroles.getList();
		List_userroles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// this.cmb_role = new Combo(container, SWT.NONE);
		// cmb_role.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		// false,
		// 1, 1));
		// new Label(container, SWT.NONE);

		setHandler();

		fillPage();
	}

	private void fillPage() {
		// new user input
		if (this.user == null) {
			// fill all roles as defualt
			for (DBRole role : this.roles) {
				if (role.isDeleted()) {
					continue;
				}

				this.lv_roles.add(role.getRolename());
				this.lv_roles.setData(role.getRolename(), role);
			}
		} else {
			// find username/password
			this.txt_name.setText(this.user.getUsername());
			this.txt_password.setText(this.user.getPassword());
			this.txt_description.setText(this.user.getDescription());
			DBRole[] userRoles = this.user.getRoles();

			// int roleMask = this.user.getRoleMask();
			for (DBRole role : this.roles) {
				// ignore deleted roles
				if (role.isDeleted()) {
					continue;
				}
				boolean foundRole = false;
				// userroles
				for (DBRole userRole : userRoles) {
					if (userRole == role) {
						foundRole = true;
						break;
					}
				}
				// insert as userrole
				if (foundRole) {
					this.lv_userroles.add(role.getRolename());
					this.lv_userroles.setData(role.getRolename(), role);
				}
				// insert as role to select
				else {
					this.lv_roles.add(role.getRolename());
					this.lv_roles.setData(role.getRolename(), role);
				}

				// if ((roleMask & ((int) Math.scalb(1.0, role.getId()))) != 0)
				// {
				// this.lv_userroles.add(role.getRolename());
				// this.lv_userroles.setData(role.getRolename(), role);
				// } else {
				// this.lv_roles.add(role.getRolename());
				// this.lv_roles.setData(role.getRolename(), role);
				// }
			}

			// notify texts
			this.txt_name.notifyListeners(SWT.Selection, new Event());
			this.txt_password.notifyListeners(SWT.Selection, new Event());
			this.txt_description.notifyListeners(SWT.Modify, new Event());

			// ADD ROLESE TO LIST
			setSelectedRoles();
			// find role
			// String rolename = this.user.getRolename();
			// for (int i = 0; i < this.cmb_role.getItemCount(); i++) {
			// String text = this.cmb_role.getItem(i);
			//
			// if (text.equals(rolename)) {
			// this.cmb_role.select(i);
			// break;
			// }
			// }
		}
		// this.cmb_role.notifyListeners(SWT.Selection, new Event());

		this.lv_roles.getList().notifyListeners(SWT.Selection, new Event());
		this.lv_userroles.getList().notifyListeners(SWT.Selection, new Event());
	}

	private void setHandler() {
		this.txt_name.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				username = ((Text) e.getSource()).getText();
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);

			}
		});

		this.txt_password.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				password = ((Text) e.getSource()).getText();
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.txt_description.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				userDescription = ((Text) e.getSource()).getText();
			}
		});

		this.lv_roles.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();

				if (selection.isEmpty()) {
					btn_addRoleToUser.setEnabled(false);
				} else {
					btn_addRoleToUser.setEnabled(true);
				}

			}
		});

		this.lv_userroles.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();

				if (selection.isEmpty()) {
					btn_removeRoleFromUser.setEnabled(false);
				} else {
					btn_removeRoleFromUser.setEnabled(true);
				}
			}
		});

		this.btn_addRoleToUser.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) lv_roles.getSelection();
				if (selection.isEmpty()) {
					return;
				}

				Object[] objs = selection.toArray();
				// replace
				for (Object obj : objs) {
					DBRole role = (DBRole) lv_roles.getData((String) obj);
					lv_roles.remove((String) obj);
					lv_roles.setData((String) obj, null);

					lv_userroles.add((String) obj);
					lv_userroles.setData((String) obj, role);
				}

				setSelectedRoles();
			}

		});

		this.btn_removeRoleFromUser.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) lv_userroles.getSelection();
				if (selection.isEmpty()) {
					return;
				}

				Object[] objs = selection.toArray();
				// replace
				for (Object obj : objs) {
					DBRole role = (DBRole) lv_userroles.getData((String) obj);
					lv_userroles.remove((String) obj);
					lv_userroles.setData((String) obj, null);

					lv_roles.add((String) obj);
					lv_roles.setData((String) obj, role);
				}
				setSelectedRoles();
			}

		});

		// this.cmb_role.addSelectionListener(new SelectionAdapter() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// String roleName = ((Combo) e.getSource()).getText();
		// role = (DBRole) ((Combo) e.getSource()).getData(roleName);
		// boolean isComplete = isPageComplete();
		// setPageComplete(isComplete);
		// }
		//
		// });
	}

	private void setSelectedRoles() {
		String[] objs = lv_userroles.getList().getItems();

		java.util.List<DBRole> roles = new ArrayList<>();
		for (String obj : objs) {
			DBRole role = (DBRole) lv_userroles.getData(obj);
			roles.add(role);
		}

		this.selectedRoles = roles.toArray(new DBRole[0]);
	}

	@Override
	public boolean isPageComplete() {
		boolean isValid = true;

		if (username.isEmpty()) {
			isValid = false;
		} else if (password.isEmpty()) {
			isValid = false;
		}

		return isValid;
	}

	public DBRole[] getRoles() {

		return this.selectedRoles;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUserDescription() {
		return this.userDescription;
	}

}
