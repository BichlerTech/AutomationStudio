package com.bichler.astudio.opcua.addressspace.wizard.java.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.opcua.addressspace.wizard.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DefaultConfigurationPage extends WizardPage {

	/** instance vars */
	private String destination = "";
	private String packageName;
	private String constantsName;

	/** swt controls */
	private Text txt_package;
	private Text txt_constants;
	private Text txt_destination;

	/**
	 * Create the wizard.
	 */
	public DefaultConfigurationPage() {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.page.title"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.page.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblPfad = new Label(container, SWT.NONE);
		lblPfad.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.page.path"));

		txt_destination = new Text(container, SWT.BORDER);
		txt_destination.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_destination.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}

		});

		Button btnFolder = new Button(container, SWT.NONE);
		btnFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				destination = dialog.open();
				txt_destination.setText(destination != null ? destination : "");

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		btnFolder.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.page.folder"));

		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));

		Label lblPackage = new Label(container, SWT.NONE);
		lblPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPackage.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.page.javapackage"));

		txt_package = new Text(container, SWT.BORDER);
		txt_package.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		txt_package.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				packageName = ((Text) e.getSource()).getText();

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		Label lblConstants = new Label(container, SWT.NONE);
		lblConstants.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.page.constants"));
		lblConstants.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_constants = new Text(container, SWT.BORDER);
		txt_constants.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		txt_constants.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				constantsName = ((Text) e.getSource()).getText();

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	}

	@Override
	public boolean isPageComplete() {

		if (this.destination == null) {
			return false;
		} else if (this.packageName == null) {
			return false;
		} else if (this.constantsName == null) {
			return false;
		}

		return true;
	}

	public String getConstantName() {
		return this.constantsName;
	}

	public String getDestination() {
		return this.destination;
	}

	public String getPackageName() {
		return this.packageName;
	}

}
