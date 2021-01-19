package com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.change;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;

public class ModelChangeElementIdPage extends WizardPage {
	private Button rdoNext;
	private Button rdoCurrent;
	protected boolean useNextIdFromParent;

	/**
	 * Create the wizard.
	 */
	public ModelChangeElementIdPage() {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.informationmodel"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.informationmodel.id"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FillLayout(SWT.VERTICAL));
		this.rdoNext = new Button(container, SWT.RADIO);
		rdoNext.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.informationmodel.parent"));
		this.rdoCurrent = new Button(container, SWT.RADIO);
		rdoCurrent.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.informationmodel.current"));
		setHandler();
		setInput();
	}

	private void setInput() {
		this.rdoNext.setSelection(true);
		this.rdoNext.notifyListeners(SWT.Selection, new Event());
	}

	private void setHandler() {
		this.rdoNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useNextIdFromParent = true;
			}
		});
		this.rdoCurrent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useNextIdFromParent = false;
			}
		});
	}

	public boolean getUseNextIdFromParent() {
		return this.useNextIdFromParent;
	}
}
