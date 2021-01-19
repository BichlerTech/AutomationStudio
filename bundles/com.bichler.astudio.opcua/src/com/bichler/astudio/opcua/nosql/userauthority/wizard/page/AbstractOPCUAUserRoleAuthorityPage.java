package com.bichler.astudio.opcua.nosql.userauthority.wizard.page;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.node.user.DBRole;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.AbstractOPCUserModel;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.RootUserRoleModel;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public abstract class AbstractOPCUAUserRoleAuthorityPage extends WizardPage {

	/**
	 * Root of the treeviewer input. Same in every wizard page
	 */
	private RootUserRoleModel input = null;

	/**
	 * Flag, which includes children, when a role is set
	 */
	private TreeViewer viewer;
	private Group toolbox;
	private Button cb_includeChildren;

	protected AbstractOPCUAUserRoleAuthorityPage(String pageName, RootUserRoleModel input) {
		super(pageName);
		this.input = input;
	}

	protected AbstractOPCUAUserRoleAuthorityPage(String pageName, RootUserRoleModel input, DBRole edit) {
		this(pageName, input);
	}

	/**
	 * Saves the current expansion state from the wizard page tree viewer
	 */
	public void saveExpansionState() {
		TreePath[] expanded = viewer.getExpandedTreePaths();
		Object root = viewer.getInput();
		((RootUserRoleModel) root).setExpandedElements(expanded);
	}

	/**
	 * Sets the current expansion state to the wizard page tree viewer.
	 */
	public void updateExpansionState() {
		RootUserRoleModel input = (RootUserRoleModel) this.viewer.getInput();
		Object[] elements = input.getExpandedElements();
		this.viewer.setExpandedTreePaths((TreePath[]) elements);
	}

	protected void createPageControl(Composite parent) {
		this.cb_includeChildren = new Button(parent, SWT.CHECK);
		cb_includeChildren.setBounds(0, 0, 93, 16);
		cb_includeChildren
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.includechildren"));
	}

	/**
	 * Performs a treeitem selection in a role wizard page. Enable/Disable the role
	 * toolboxes.
	 * 
	 * @param event
	 */
	protected void performSelection(SelectionEvent event) {
		StructuredSelection selection = (StructuredSelection) this.viewer.getSelection();
		Object[] elements = selection.toArray();

		// add elements
		List<AbstractOPCUserModel> elements2use = new ArrayList<>();
		for (Object element : elements) {
			// check if element is allowed
			if (!isElementAllowedToUse((AbstractOPCUserModel) element)) {
				continue;
			}

			elements2use.add((AbstractOPCUserModel) element);
		}

		// disable toolbox if no element to use is selected
		if (elements2use.isEmpty()) {
			setEnabledToolbox(false);
			return;
		}

		setEnabledToolbox(true);
		setRoleCheckboxes(elements2use);
		setIncludeChildrenCheckbox(elements2use);
	}

	protected void setAuthorityRole(AuthorityRule role, boolean isSet) {
		IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
		Object[] items = selection.toArray();

		List<AbstractOPCUserModel> update = new ArrayList<>();
		for (Object item : items) {
			update.addAll(
					((AbstractOPCUserModel) item).updateAuthority(role, isSet, this.cb_includeChildren.getSelection()));
		}

		this.viewer.update(update.toArray(), null);
	}

	abstract void setRoleCheckboxes(List<AbstractOPCUserModel> elements2use);

	boolean isElementAllowedToUse(AbstractOPCUserModel element) {
		return true;
	}

	void setViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

	void setToolbox(Group toolbox) {
		this.toolbox = toolbox;
	}

	void setHandler() {
		this.viewer.getTree().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				performSelection(e);
			}

		});
	}

	void setInput() {
		this.viewer.setInput(this.input);
	}

	private void setEnabledToolbox(boolean enabled) {
		this.toolbox.setEnabled(enabled);
	}

	private void setIncludeChildrenCheckbox(List<AbstractOPCUserModel> elements2use) {

		boolean isIncludeChildren = true;

		for (AbstractOPCUserModel element : elements2use) {
			boolean includeChildren = element.isIncludeChildren();

			if (!includeChildren) {
				isIncludeChildren = false;
				break;
			}
		}

		this.cb_includeChildren.setSelection(isIncludeChildren);
	}

	// protected boolean isIncludeChildren() {
	// return this.includeChildren;
	// }

}
