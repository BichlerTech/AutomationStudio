package com.bichler.astudio.opcua.nosql.userauthority.wizard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.node.user.DBAuthority;
import opc.sdk.core.node.user.DBRole;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.AbstractOPCUserModel;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.RootUserRoleModel;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.NewOPCUAUserRoleAuthorityAttributePage;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.NewOPCUAUserRoleAuthorityMethodPage;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.NewOPCUAUserRoleAuthorityMonitoringPage;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.NewOPCUAUserRoleAuthorityNodemanagementPage;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.NewOPCUAUserRoleAuthorityViewPage;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.NewOPCUAUserRolePage;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.addressspace.model.nosql.userauthority.NoSqlUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAUserRoleWizard extends Wizard {
	private String roleName = "";
	private String roleDescription = "";
	private NewOPCUAUserRolePage pageRole;
	private NewOPCUAUserRoleAuthorityAttributePage pageAttribute;
	private NewOPCUAUserRoleAuthorityMethodPage pageMethod;
	private NewOPCUAUserRoleAuthorityMonitoringPage pageMonitoring;
	private NewOPCUAUserRoleAuthorityNodemanagementPage pageNodemanagement;
	private NewOPCUAUserRoleAuthorityViewPage pageView;
	private RootUserRoleModel input;
	private Map<NodeId, DBAuthority> roleAuthorities = new HashMap<>();
	private DBRole edit = null;
	private Connection connection;

	/**
	 * New role
	 */
	public OPCUAUserRoleWizard() {
		super();
		setWindowTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.newrole.title"));
	}

	/**
	 * Edit role
	 * 
	 * @param role
	 * @param connection
	 */
	public OPCUAUserRoleWizard(DBRole role, Connection connection, Map<NodeId, DBAuthority> unsavedAuthorities) {
		this();
		setWindowTitle(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.edituserrole.title"));
		this.edit = role;
		this.connection = connection;
		if (unsavedAuthorities == null) {
			try {
				this.roleAuthorities = NoSqlUtil.readOPCNodesFromRoles(this.connection,
						ServerInstance.getInstance().getServerInstance().getNamespaceUris(), this.edit);
			} catch (SQLException e) {
				e.printStackTrace();
				this.roleAuthorities = new HashMap<>();
			}
		} else {
			this.roleAuthorities.putAll(unsavedAuthorities);
		}
	}

	@Override
	public void addPages() {
		initTreeInput();
		// general page
		this.pageRole = new NewOPCUAUserRolePage(this.edit);
		addPage(this.pageRole);
		// authority pages
		this.pageAttribute = new NewOPCUAUserRoleAuthorityAttributePage(this.input, this.edit);
		addPage(this.pageAttribute);
		this.pageMethod = new NewOPCUAUserRoleAuthorityMethodPage(this.input, this.edit);
		addPage(this.pageMethod);
		this.pageMonitoring = new NewOPCUAUserRoleAuthorityMonitoringPage(this.input, this.edit);
		addPage(this.pageMonitoring);
		this.pageNodemanagement = new NewOPCUAUserRoleAuthorityNodemanagementPage(this.input, this.edit);
		addPage(this.pageNodemanagement);
		this.pageView = new NewOPCUAUserRoleAuthorityViewPage(this.input, this.edit);
		addPage(this.pageView);
	}

	@Override
	public boolean performFinish() {
		this.roleName = this.pageRole.getRoleName();
		this.roleDescription = this.pageRole.getRoleDescription();
		doCreateTemporaryRole(this.roleName, this.input);
		return true;
	}

	public Map<NodeId, DBAuthority> getNodeAuthorities() {
		return this.roleAuthorities;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public String getRoleDescription() {
		return this.roleDescription;
	}

	/**
	 * Initialize the opc ua structure for all wizard pages which it shows. TODO:
	 * should be on startup of the wizard with progress
	 */
	private void initTreeInput() {
		this.input = new RootUserRoleModel("root", NodeClass.Object, ServerInstance.getInstance().getServerInstance());
	}

	/**
	 * Creates a database role for an opc ua server
	 * 
	 * @param input
	 * @param roleName
	 */
	private void doCreateTemporaryRole(String roleName, RootUserRoleModel input) {
		// store all nodes to remember because of restriction
		// prevent endless loop
		Set<NodeId> rekStop = new HashSet<>();
		rekTemporaryRole(input.getChildren(), rekStop);
	}

	private void rekTemporaryRole(AbstractOPCUserModel[] children, Set<NodeId> rekStop) {
		for (AbstractOPCUserModel child : children) {
			// Prevent endless loop
			if (rekStop.contains(child.getNodeId())) {
				continue;
			}
			rekStop.add(child.getNodeId());
			// include node to authorities if the node has a restricted access
			DBAuthority authority = child.getAuthority();
			if (authority.getAuthorityRole() < AuthorityRule.getMask(AuthorityRule.ALL)) {
				this.roleAuthorities.put(child.getNodeId(), authority);
			}
			// fetch children if they are included and not added
			boolean isIncludeChildren = child.isIncludeChildren();
			if (isIncludeChildren) {
				child.fetchChildren(true);
			}
			rekTemporaryRole(child.getChildren(), rekStop);
		}
	}
}
