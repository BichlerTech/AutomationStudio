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

public class NewOPCUAUserRoleAuthorityMonitoringPage extends AbstractOPCUAUserRoleAuthorityPage {
	private Button cb_events;
	private Button cb_datachange;
	private TreeViewer treeViewer;
	private TreeColumn trclmn_OpcNode;
	private TreeColumn trclmn_Rechte;

	/**
	 * Create the wizard.
	 * 
	 * @param contentProvider
	 * @wbp.parser.constructor
	 */
	public NewOPCUAUserRoleAuthorityMonitoringPage(RootUserRoleModel input) {
		super("monitoring", input);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.monitor.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.monitor.description"));
	}

	public NewOPCUAUserRoleAuthorityMonitoringPage(RootUserRoleModel input, DBRole role) {
		super("monitoring", input, role);
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.monitor.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.monitor.description"));
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
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setHeaderVisible(true);
		tree.setLinesVisible(false);
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
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		group.setLayout(new GridLayout(2, false));
		setToolbox(group);
		this.cb_datachange = new Button(group, SWT.CHECK);
		cb_datachange.setBounds(0, 0, 93, 16);
		cb_datachange.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.monitor.datachange") + " (dc)");
		this.cb_events = new Button(group, SWT.CHECK);
		cb_events.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.monitor.event")
						+ " (e)");
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
		this.cb_events.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.Events, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_datachange.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.Datachange, ((Button) e.getSource()).getSelection());
			}
		});
	}

	@Override
	void setRoleCheckboxes(List<AbstractOPCUserModel> elements2use) {
		boolean isEvent = true;
		boolean isDatachange = true;
		for (AbstractOPCUserModel element : elements2use) {
			int attributes = element.getAuthority().getAuthorityRole();
			Set<AuthorityRule> set = AuthorityRule.getSet(attributes);
			if (!set.contains(AuthorityRule.Events)) {
				isEvent = false;
			}
			if (!set.contains(AuthorityRule.Datachange)) {
				isDatachange = false;
			}
			if (!isDatachange && !isEvent) {
				break;
			}
		}
		this.cb_events.setSelection(isEvent);
		this.cb_datachange.setSelection(isDatachange);
	}

	class WizardPageLabelProvider extends UserRoleLabelProvider {
		@Override
		protected String toStringAccessLevel(Set<AuthorityRule> roles) {
			String access = "";
			// events
			if (roles.contains(AuthorityRule.Events)) {
				access += "e";
			}
			// data change
			if (roles.contains(AuthorityRule.Datachange)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "dc";
			}
			return access;
		}
	}
}
