package com.bichler.astudio.opcua.nosql.userauthority.wizard;

import opc.sdk.core.node.user.DBRole;
import opc.sdk.core.node.user.DBUser;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.NewOPCUAUserAccountPage;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUAUserAccountWizard extends Wizard {
	private NewOPCUAUserAccountPage pageUser;
	private DBRole[] userRoles;
	private String username;
	private String password;
	private DBRole[] roles;
	private DBUser user;
	private String description;

	public OPCUAUserAccountWizard(DBRole[] roles) {
		super();
		setWindowTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.user.new"));
		this.roles = roles;
	}

	public OPCUAUserAccountWizard(DBRole[] roles, DBUser user) {
		this(roles);
		setWindowTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.edituser.title"));
		this.user = user;
	}

	@Override
	public void addPages() {
		this.pageUser = new NewOPCUAUserAccountPage(this.roles, this.user);
		this.addPage(this.pageUser);
	}

	@Override
	public boolean performFinish() {
		this.userRoles = this.pageUser.getRoles();
		this.username = this.pageUser.getUsername();
		this.password = this.pageUser.getPassword();
		this.description = this.pageUser.getUserDescription();
		return true;
	}

	public DBRole[] getRoles() {
		return this.userRoles;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getDescription() {
		return this.description;
	}
}
