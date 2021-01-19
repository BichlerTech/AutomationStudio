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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.bichler.astudio.opcua.nosql.userauthority.wizard.OPCUAUserRoleWizard;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.AbstractOPCUserModel;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.RootUserRoleModel;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.provider.UserRoleContentProvider;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.provider.UserRoleLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class NewOPCUAUserRoleAuthorityNodemanagementPage extends AbstractOPCUAUserRoleAuthorityPage {
	private Button cb_addNode;
	private Button cb_delNode;
	private Button cb_addRef;
	private Button cb_delRef;
	private TreeColumn trclmn_OpcNode;
	private TreeColumn trclmn_Rechte;
	private TreeViewer treeViewer;

	/**
	 * Create the wizard.
	 * 
	 * @param contentProvider
	 * @wbp.parser.constructor
	 */
	public NewOPCUAUserRoleAuthorityNodemanagementPage(RootUserRoleModel input) {
		super("nodemanagement", input);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.node.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.node.description"));
	}

	public NewOPCUAUserRoleAuthorityNodemanagementPage(RootUserRoleModel input, DBRole role) {
		super("nodemanagement", input, role);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.node.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.node.description"));
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
				"wizard.userrole.attribute.rigths"));
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		setToolbox(group);
		this.cb_addNode = new Button(group, SWT.CHECK);
		cb_addNode.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.node.addnode")
						+ " (an)");
		this.cb_delNode = new Button(group, SWT.CHECK);
		cb_delNode.setBounds(0, 0, 93, 16);
		cb_delNode.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.node.deletenode")
						+ " (rn)");
		this.cb_addRef = new Button(group, SWT.CHECK);
		cb_addRef.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.node.addreference")
						+ " (ar)");
		this.cb_delRef = new Button(group, SWT.CHECK);
		cb_delRef.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.node.deletereference") + " (rr)");
		createPageControl(container);
		new Label(container, SWT.NONE);
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
		this.cb_addNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.AddNode, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_delNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.DeleteNode, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_addRef.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.AddReference, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_delRef.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.DeleteReference, ((Button) e.getSource()).getSelection());
			}
		});
	}

	@Override
	void setRoleCheckboxes(List<AbstractOPCUserModel> elements2use) {
		boolean isAddNode = true;
		boolean isAddReference = true;
		boolean isDeleteNode = true;
		boolean isDeleteReference = true;
		for (AbstractOPCUserModel element : elements2use) {
			int attributes = element.getAuthority().getAuthorityRole();
			Set<AuthorityRule> set = AuthorityRule.getSet(attributes);
			if (!set.contains(AuthorityRule.AddNode)) {
				isAddNode = false;
			}
			if (!set.contains(AuthorityRule.AddReference)) {
				isAddReference = false;
			}
			if (!set.contains(AuthorityRule.DeleteNode)) {
				isDeleteNode = false;
			}
			if (!set.contains(AuthorityRule.DeleteReference)) {
				isDeleteReference = false;
			}
			if (!isAddNode && !isAddReference && !isDeleteNode && !isDeleteReference) {
				break;
			}
		}
		this.cb_addNode.setSelection(isAddNode);
		this.cb_addRef.setSelection(isAddReference);
		this.cb_delNode.setSelection(isDeleteNode);
		this.cb_delRef.setSelection(isDeleteReference);
	}

	class WizardPageLabelProvider extends UserRoleLabelProvider {
		@Override
		protected String toStringAccessLevel(Set<AuthorityRule> roles) {
			String access = "";
			// add node
			if (roles.contains(AuthorityRule.AddNode)) {
				access += "an";
			}
			// remove node
			if (roles.contains(AuthorityRule.DeleteNode)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "rn";
			}
			// add node
			if (roles.contains(AuthorityRule.AddReference)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "ar";
			}
			// remove node
			if (roles.contains(AuthorityRule.DeleteReference)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "rr";
			}
			return access;
		}
	}
}
