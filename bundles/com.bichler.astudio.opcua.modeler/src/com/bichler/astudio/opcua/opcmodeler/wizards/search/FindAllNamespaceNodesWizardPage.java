package com.bichler.astudio.opcua.opcmodeler.wizards.search;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;

public class FindAllNamespaceNodesWizardPage extends WizardPage {
	String namespace = "";
	private Combo cmbNamespace;

	/**
	 * Create the wizard.
	 */
	public FindAllNamespaceNodesWizardPage() {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.lookup.title"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.lookup.title"));
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
		Label lblNamespace = new Label(container, SWT.NONE);
		lblNamespace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamespace.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"));
		this.cmbNamespace = new Combo(container, SWT.NONE);
		cmbNamespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		setHandler();
		fill();
	}

	public String getNamespace() {
		return this.namespace;
	}

	private void fill() {
		String[] items = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		this.cmbNamespace.setItems(items);
		this.cmbNamespace.select(0);
	}

	private void setHandler() {
		this.cmbNamespace.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				namespace = cmbNamespace.getText();
			}
		});
	}
}
