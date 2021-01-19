package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.NamingRuleType;

public class CreateMethodModellingDialog extends AbstractCreateModellingDialog {
	private NamingRuleType namingRule;
	private Combo cmb_namingRule;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public CreateMethodModellingDialog(Shell parentShell) {
		super(parentShell);
		this.filtersModellingRule = new NodeClass[] { NodeClass.ObjectType };
		this.filtersModellingParent = new NodeClass[] { NodeClass.ObjectType };
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		GridData layoutData = new GridData();
		Label lblNamingRule = new Label(container, SWT.NONE);
		lblNamingRule.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.namingrule"));
		layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		layoutData.minimumWidth = 200;
		this.cmb_namingRule = new Combo(container, SWT.READ_ONLY);
		cmb_namingRule.setLayoutData(layoutData);
		/** add none */
		cmb_namingRule.add(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.none"));
		cmb_namingRule.select(0);
		/** add other namingrules */
		for (NamingRuleType nrType : NamingRuleType.values()) {
			cmb_namingRule.add(nrType.name());
			cmb_namingRule.setData(nrType.name(), nrType);
		}
		return container;
	}

	private void addModelRuleSelf() {
		String text = this.cmb_namingRule.getText();
		this.namingRule = (NamingRuleType) this.cmb_namingRule.getData(text);
	}

	@Override
	protected void okPressed() {
		addModelRuleSelf();
		super.okPressed();
	}

	public NamingRuleType getModelRuleSelf() {
		return this.namingRule;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 175);
	}

	@Override
	protected NodeClass[] getFilteredRules() {
		return this.filtersModellingRule;
	}

	@Override
	protected NodeClass[] getFilteredParent() {
		return this.filtersModellingParent;
	}
}
