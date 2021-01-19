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

public class NewOPCUAUserRoleAuthorityMethodPage extends AbstractOPCUAUserRoleAuthorityPage {
	private Button cb_call;
	private TreeViewer treeViewer;
	private TreeColumn trclmn_OpcNode;
	private TreeColumn trclmn_Rechte;

	/**
	 * Create the wizard.
	 * 
	 * @param input
	 * @param edit
	 */
	public NewOPCUAUserRoleAuthorityMethodPage(RootUserRoleModel input) {
		super("method", input);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.method.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.method.description"));
	}

	public NewOPCUAUserRoleAuthorityMethodPage(RootUserRoleModel input, DBRole role) {
		super("method", input, role);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.method.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.method.description"));
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
		this.cb_call = new Button(group, SWT.CHECK);
		cb_call.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.method.executeable") + " (exec)");
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
		this.cb_call.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.Call, ((Button) e.getSource()).getSelection());
			}
		});
	}

	@Override
	void setRoleCheckboxes(List<AbstractOPCUserModel> elements2use) {
		boolean isCall = true;
		for (AbstractOPCUserModel element : elements2use) {
			int attributes = element.getAuthority().getAuthorityRole();
			Set<AuthorityRule> set = AuthorityRule.getSet(attributes);
			if (!set.contains(AuthorityRule.Call)) {
				isCall = false;
			}
			if (!isCall) {
				break;
			}
		}
		this.cb_call.setSelection(isCall);
	}

	class WizardPageLabelProvider extends UserRoleLabelProvider {
		@Override
		protected String toStringAccessLevel(Set<AuthorityRule> roles) {
			String access = "";
			// ausführbar
			if (roles.contains(AuthorityRule.Call)) {
				access += "exec";
			}
			return access;
		}
	}
}
