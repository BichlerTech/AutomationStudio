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

public class NewOPCUAUserRoleAuthorityAttributePage extends AbstractOPCUAUserRoleAuthorityPage {
	private Button cb_read;
	private Button cb_write;
	private TreeViewer treeViewer;
	private TreeColumn trclmn_OpcNode;
	private TreeColumn trclmn_Rechte;
	private Button cb_historyread;
	private Button cb_historywrite;

	/**
	 * Create the wizard.
	 * 
	 * @param input
	 * @param edit
	 * @param contentProvider
	 * @wbp.parser.constructor
	 */
	public NewOPCUAUserRoleAuthorityAttributePage(RootUserRoleModel input) {
		super("attribute", input);
		setTitle(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.attribute.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.attribute.description"));
	}

	public NewOPCUAUserRoleAuthorityAttributePage(RootUserRoleModel input, DBRole role) {
		super("attribute", input, role);
		setTitle(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.attribute.title"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.attribute.description"));
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
		this.cb_read = new Button(group, SWT.CHECK);
		cb_read.setBounds(0, 0, 93, 16);
		cb_read.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.attribute.read")
						+ " (r)");
		this.cb_write = new Button(group, SWT.CHECK);
		cb_write.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.userrole.attribute.write")
						+ " (w)");
		this.cb_historyread = new Button(group, SWT.CHECK);
		cb_historyread.setBounds(0, 0, 93, 16);
		cb_historyread.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.attribute.historyread") + " (hr)");
		this.cb_historywrite = new Button(group, SWT.CHECK);
		cb_historywrite.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.userrole.attribute.historywrite") + " (hw)");
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
		this.cb_read.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.Read, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_write.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.Write, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_historyread.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.HistoryRead, ((Button) e.getSource()).getSelection());
			}
		});
		this.cb_historywrite.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAuthorityRole(AuthorityRule.HistoryUpdate, ((Button) e.getSource()).getSelection());
			}
		});
	}

	@Override
	void setRoleCheckboxes(List<AbstractOPCUserModel> elements2use) {
		boolean isRead = true;
		boolean isWrite = true;
		boolean isHistoryRead = true;
		boolean isHistoryUpdate = true;
		for (AbstractOPCUserModel element : elements2use) {
			int attributes = element.getAuthority().getAuthorityRole();
			Set<AuthorityRule> set = AuthorityRule.getSet(attributes);
			if (!set.contains(AuthorityRule.Read)) {
				isRead = false;
			}
			if (!set.contains(AuthorityRule.Write)) {
				isWrite = false;
			}
			if (!set.contains(AuthorityRule.HistoryRead)) {
				isHistoryRead = false;
			}
			if (!set.contains(AuthorityRule.HistoryUpdate)) {
				isHistoryUpdate = false;
			}
			if (!isRead && !isWrite && !isHistoryRead && !isHistoryUpdate) {
				break;
			}
		}
		this.cb_read.setSelection(isRead);
		this.cb_write.setSelection(isWrite);
		this.cb_historyread.setSelection(isHistoryRead);
		this.cb_historywrite.setSelection(isHistoryUpdate);
	}

	class WizardPageLabelProvider extends UserRoleLabelProvider {
		@Override
		protected String toStringAccessLevel(Set<AuthorityRule> roles) {
			String access = "";
			// read
			if (roles.contains(AuthorityRule.Read)) {
				access += "r";
			}
			// write
			if (roles.contains(AuthorityRule.Write)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "w";
			}
			// history read
			if (roles.contains(AuthorityRule.HistoryRead)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "hr";
			}
			// history write
			if (roles.contains(AuthorityRule.HistoryUpdate)) {
				if (!access.isEmpty()) {
					access += " ";
				}
				access += "hw";
			}
			return access;
		}
	}
}
