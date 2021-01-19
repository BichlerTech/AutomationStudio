package com.bichler.astudio.components.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

import com.bichler.astudio.utils.constants.StudioConstants;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.components.ui.ComponentsUIActivator;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class General_Path_Selection extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "com.bichler.astudio";

	private Text txt_StudioFolder;
	private Text txt_JavaCommand;
	private Text txt_BinCommand;
	private Text txt_JavaJarCommand;
	private Label label;
	private Button btn_CometStudioFolder;
	private Label lblConfigFolder;
	private Text txt_ConfigFolder;
	private Label lblJavaCommand;
	private Label lblJarCommand;
	private Label lblBinCommand;

	/**
	 * Create the preference page.
	 */
	public General_Path_Selection() {
	}

	/**
	 * Create contents of the preference page.
	 * 
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		// initializeDefaultPreferences();

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, false));

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,
				"Preferences.Label.StudioFolder"));

		txt_StudioFolder = new Text(container, SWT.BORDER);
		GridData gd_txt_StudioFolder = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txt_StudioFolder.widthHint = 150;
		txt_StudioFolder.setLayoutData(gd_txt_StudioFolder);

		btn_CometStudioFolder = new Button(container, SWT.NONE);
		btn_CometStudioFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String name = txt_StudioFolder.getText();

				DirectoryDialog dialog = new DirectoryDialog(getShell());
				dialog.setFilterPath(name);
				name = dialog.open();
				if (name != null && !name.isEmpty()) {
					txt_StudioFolder.setText(name);
				}
			}
		});
		btn_CometStudioFolder.setText("...");

		lblConfigFolder = new Label(container, SWT.NONE);
		lblConfigFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfigFolder.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,
				"Preferences.Label.ConfigFolder"));

		txt_ConfigFolder = new Text(container, SWT.BORDER);
		txt_ConfigFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		label.setAlignment(SWT.CENTER);
		new Label(container, SWT.NONE);

		lblJavaCommand = new Label(container, SWT.NONE);
		lblJavaCommand.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "Preferences.Label.JavaCommand"));
		lblJavaCommand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_JavaCommand = new Text(container, SWT.BORDER);
		txt_JavaCommand.setText("Java");
		txt_JavaCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		lblJarCommand = new Label(container, SWT.NONE);
		lblJarCommand.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "Preferences.Label.JarCommand"));
		lblJarCommand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_JavaJarCommand = new Text(container, SWT.BORDER);
		txt_JavaJarCommand.setText("-jar");
		txt_JavaJarCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		label.setAlignment(SWT.CENTER);
		new Label(container, SWT.NONE);
		
		lblBinCommand = new Label(container, SWT.NONE);
		lblBinCommand.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "Preferences.Label.BinCommand"));
		lblBinCommand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_BinCommand = new Text(container, SWT.BORDER);
		txt_BinCommand.setText("-jar");
		txt_BinCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		
		this.initializeValues();

		return container;
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {

	}

	private void initializeValues() {
		IPreferenceStore store = ComponentsUIActivator.getDefault().getPreferenceStore();
		this.txt_StudioFolder.setText(store.getString(StudioConstants.StudioFolder));
		this.txt_ConfigFolder.setText(store.getString(StudioConstants.ConfigFolder));
		this.txt_JavaCommand.setText(store.getString(StudioConstants.JavaCommand));
		this.txt_JavaJarCommand.setText(store.getString(StudioConstants.JavaJarCommand));
		this.txt_BinCommand.setText(store.getString(StudioConstants.BinCommand));
	}

	@Override
	protected void performApply() {
		IPreferenceStore store = ComponentsUIActivator.getDefault().getPreferenceStore();
		store.setValue(StudioConstants.StudioFolder, this.txt_StudioFolder.getText());
		store.setValue(StudioConstants.ConfigFolder, this.txt_ConfigFolder.getText());
		store.setValue(StudioConstants.JavaCommand, this.txt_JavaCommand.getText());
		store.setValue(StudioConstants.JavaJarCommand, this.txt_JavaJarCommand.getText());
		store.setValue(StudioConstants.BinCommand, this.txt_BinCommand.getText());
	}

	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}

}
