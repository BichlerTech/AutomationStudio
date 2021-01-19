package com.bichler.astudio.opcua.opcmodeler.wizards.create.page;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;

import org.eclipse.swt.layout.FillLayout;

public class DetailReferenceTypePage extends WizardPage {
	private DesignerFormToolkit controllCreationToolkit = null;
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private CometCombo combo_executeable;
	private CometCombo combo_userExecuteable;

	/**
	 * Create the wizard.
	 */
	public DetailReferenceTypePage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		this.controllCreationToolkit = new DesignerFormToolkit();
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout());
		setControl(container);
		ScrolledForm form = formToolkit.createScrolledForm(container);
		form.setExpandHorizontal(true);
		form.setExpandVertical(true);
		form.getBody().setLayout(new GridLayout(1, false));
		createSection(form.getBody());
	}

	private void createSection(Composite container) {
		Section section = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText("Methoden Attribute");
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(1, false));
		// Label * Executeable
		this.controllCreationToolkit.createLabel(composite, "Executeable: ");
		// Combo * Executeable
		this.combo_executeable = this.controllCreationToolkit.createComboBoolean(composite);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.combo_executeable);
		// Label * UserExecuteable
		this.controllCreationToolkit.createLabel(composite, "UserExecuteable: ");
		this.combo_userExecuteable = this.controllCreationToolkit.createComboBoolean(composite);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.combo_userExecuteable);
	}

	@Override
	public boolean isPageComplete() {
		return true;
	}

	public boolean getExecuteable() {
		return Boolean.parseBoolean(this.combo_executeable.getText());
	}

	public boolean getUserExecuteable() {
		return Boolean.parseBoolean(this.combo_userExecuteable.getText());
	}
}
