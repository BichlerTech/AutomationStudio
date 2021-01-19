package com.bichler.astudio.opcua.javacommand.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.opcua.javacommand.preferences.JavaCommandPreferenceConstants;

public class CommandSkriptWizardPage extends WizardPage {
	private Text textSkript = null;
	private Text textName;
	private Text textDescription;

	private String name = "";
	private String description = "";
	private String skript = "";
	private Preferences edit = null;

	/**
	 * Create the wizard.
	 */
	public CommandSkriptWizardPage() {
		super("addcommand");
		setTitle("Neues OPC UA Server Startskript");
		setDescription("Eingabe des neuen Startskripts");
	}

	public CommandSkriptWizardPage(Preferences edit) {
		super("addcommand");
		setTitle("Bearbeite OPC UA Server Startskript");
		setDescription("Eingabe des neuen Startskripts");
		this.edit = edit;
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

		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name:");

		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setText("Beschreibung:");

		textDescription = new Text(container, SWT.BORDER);
		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		this.textSkript = new Text(container, SWT.BORDER | SWT.MULTI);
		textSkript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				2, 1));

		setHandler();

		fillEdit();
	}

	private void fillEdit() {
		if (this.edit != null) {
			this.textName.setText(this.edit.name());
			this.textDescription
					.setText(this.edit
							.get(JavaCommandPreferenceConstants.PREFERENCE_COMMAND_DESCRIPTION,
									""));
			this.textSkript.setText(this.edit.get(
					JavaCommandPreferenceConstants.PREFERENCE_COMMAND_COMMAND,
					""));
		}

	}

	private void setHandler() {
		this.textName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				name = textName.getText();
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.textDescription.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				description = textDescription.getText();
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.textSkript.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				skript = textSkript.getText();
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});
	}

	public String getDescription() {
		return this.description;
	}

	public String getSkriptName() {
		return this.name;
	}

	public String getSkript() {
		return this.skript;
	}

}
