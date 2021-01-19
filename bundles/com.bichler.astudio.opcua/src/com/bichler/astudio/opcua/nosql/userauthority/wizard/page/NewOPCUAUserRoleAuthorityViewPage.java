package com.bichler.astudio.opcua.nosql.userauthority.wizard.page;

import java.util.List;
import java.util.Set;

import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.node.user.DBRole;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.bichler.astudio.opcua.nosql.userauthority.wizard.OPCUAUserRoleWizard;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.AbstractOPCUserModel;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.RootUserRoleModel;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.provider.UserRoleContentProvider;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.provider.UserRoleLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class NewOPCUAUserRoleAuthorityViewPage extends AbstractOPCUAUserRoleAuthorityPage {
	private TreeViewer treeViewer;
	private TreeColumn trclmn_OpcNode;
	private TreeColumn trclmn_Rechte;
	private Button cb_browse;
	private Button cb_regNode;
	private Button cb_translateBrowsepath;

	/**
	 * Create the wizard.
	 * 
	 * @param contentProvider
	 * @wbp.parser.constructor
	 */
	public NewOPCUAUserRoleAuthorityViewPage(RootUserRoleModel input) {
		super("roles", input);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.view.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.view.description"));
	}

	public NewOPCUAUserRoleAuthorityViewPage(RootUserRoleModel input, DBRole role) {
		super("view", input, role);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.view.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.view.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		this.treeViewer = new TreeViewer(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		this.treeViewer.setContentProvider(
				new UserRoleContentProvider(((OPCUAUserRoleWizard) getWizard()).getNodeAuthorities()));
		this.treeViewer.setLabelProvider(new WizardPageLabelProvider());
		setViewer(this.treeViewer);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(false);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.trclmn_OpcNode = new TreeColumn(tree, SWT.NONE);
		trclmn_OpcNode.setAlignment(SWT.LEFT);
		trclmn_OpcNode.setWidth(250);
		trclmn_OpcNode.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.attribute.opcnode"));
		this.trclmn_Rechte = new TreeColumn(tree, SWT.NONE);
		trclmn_Rechte.setAlignment(SWT.LEFT);
		trclmn_Rechte.setWidth(100);
		trclmn_Rechte.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.attribute.rights"));
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		setToolbox(group);
		this.cb_browse = new Button(group, SWT.CHECK);
		cb_browse.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.view.browse")
						+ " (b)");
		this.cb_translateBrowsepath = new Button(group, SWT.CHECK);
		cb_translateBrowsepath.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.view.translatebrowsepath") + " (tr)");
		this.cb_regNode = new Button(group, SWT.CHECK);
		cb_regNode.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.view.registernode")
						+ " (reg)");
		createPageControl(container);
		setHandler();
		setInput();
	}

	@Override
	public boolean isPageComplete() {
		boolean isValid = true;
		return isValid;
	}

	@Override
	void setHandler() {
		super.setHandler();
		this.cb_browse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.Browse, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_translateBrowsepath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.TranslateBrowsePath, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_regNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.RegisterNode, ((Button) e.getSource()).getSelection());
			}
		});
	}

	@Override
	void setRoleCheckboxes(List<AbstractOPCUserModel> elements2use) {
		boolean isBrowse = true;
		boolean isRegisterNode = true;
		boolean isTranslateBrowsePath = true;
		for (AbstractOPCUserModel element : elements2use) {
			int attributes = element.getAuthority().getAuthorityRole();
			Set<AuthorityRule> set = AuthorityRule.getSet(attributes);
			if (!set.contains(AuthorityRule.Browse)) {
				isBrowse = false;
			}
			if (!set.contains(AuthorityRule.RegisterNode)) {
				isRegisterNode = false;
			}
			if (!set.contains(AuthorityRule.TranslateBrowsePath)) {
				isTranslateBrowsePath = false;
			}
			if (!isBrowse && !isRegisterNode && !isTranslateBrowsePath) {
				break;
			}
		}
		this.cb_browse.setSelection(isBrowse);
		this.cb_regNode.setSelection(isRegisterNode);
		this.cb_translateBrowsepath.setSelection(isTranslateBrowsePath);
	}

	class WizardPageLabelProvider extends UserRoleLabelProvider {
		@Override
		protected String toStringAccessLevel(Set<AuthorityRule> roles) {
			String access = "";
			// browse
			if (roles.contains(AuthorityRule.Browse)) {
				access += "b";
			}
			// register node
			if (roles.contains(AuthorityRule.RegisterNode)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "reg";
			}
			// translate browsepath to nodeid
			if (roles.contains(AuthorityRule.TranslateBrowsePath)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "tr";
			}
			return access;
		}
	}
}
