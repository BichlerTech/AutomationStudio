package com.bichler.astudio.opcua.wizard.page;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;

public class RenameOPCUAServerEcmaScriptWizardPage1 extends WizardPage {

	private IWorkbench workbench = null;
	private IStructuredSelection selection = null;
	private Text txt_scriptname;
	private Label label;

	private String scriptName = "";

	/**
	 * Create the wizard.
	 */
	public RenameOPCUAServerEcmaScriptWizardPage1(IWorkbench workbench, IStructuredSelection selection) {
		this();
		this.workbench = workbench;
		this.selection = selection;
	}

	/**
	 * Create the wizard.
	 * 
	 * @wbp.parser.constructor
	 */
	public RenameOPCUAServerEcmaScriptWizardPage1() {
		super("wizardPage");
		setTitle("Rename OPC UA Server Ecma Script");
		setDescription("Rename OPC UA Server Ecma Script File");
		ImageDescriptor desc = new ImageDescriptor() {

			@Override
			public ImageData getImageData() {
				return StudioImageActivator.getImage(StudioImages.ICON_WIZARD_SCRIPT_ADD).getImageData();
			}
		};
		setImageDescriptor(desc);
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

		Label lblServername = new Label(container, SWT.NONE);
		lblServername.setAlignment(SWT.RIGHT);
		GridData gd_lblServername = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblServername.widthHint = 116;
		lblServername.setLayoutData(gd_lblServername);
		lblServername.setText("Script Name:");

		txt_scriptname = new Text(container, SWT.BORDER);
		txt_scriptname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				scriptName = txt_scriptname.getText();
			}
		});
		GridData gd_txt_scriptname = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txt_scriptname.widthHint = 257;
		txt_scriptname.setLayoutData(gd_txt_scriptname);
		new Label(container, SWT.NONE);

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Interval:");
		label.setVisible(false);

		this.init();
	}

	private void init() {
		this.txt_scriptname.setText(this.scriptName);
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
}
