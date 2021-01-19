package com.bichler.astudio.opcua.opcmodeler.wizards.create.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class DataTypeEnumTypePage extends WizardPage {
	public enum EnumType {
		None, StringValue, EnumValueType;
	}

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Button btnStringValue;
	private Button btnEnumerationValue;
	private EnumType enumType = EnumType.StringValue;
	private DataTypeEnumPage enumValuePage;

	/**
	 * Create the wizard.
	 */
	public DataTypeEnumTypePage() {
		super("wizardPage");
		setTitle("Enumeration Type");
		setDescription("Defines a enumeration type");
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
		Section section = formToolkit.createSection(form.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText("Enumeration type selection");
		section.setExpanded(true);
		Composite composite = new Composite(section, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		this.btnStringValue = new Button(composite, SWT.RADIO);
		btnStringValue.setSelection(true);
		formToolkit.adapt(btnStringValue, true, true);
		btnStringValue.setText("String Value");
		this.btnEnumerationValue = new Button(composite, SWT.RADIO);
		formToolkit.adapt(btnEnumerationValue, true, true);
		btnEnumerationValue.setText("Enumeration Value");
		setHandler();
	}

	private void setHandler() {
		this.btnStringValue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enumType = EnumType.StringValue;
				enumValuePage.setTableVisible();
			}
		});
		this.btnEnumerationValue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enumType = EnumType.EnumValueType;
				enumValuePage.setTableVisible();
			}
		});
	}

	public EnumType getEnumType() {
		return this.enumType;
	}

	public void setEnumValuePage(DataTypeEnumPage enumValuePage) {
		this.enumValuePage = enumValuePage;
	}
}
