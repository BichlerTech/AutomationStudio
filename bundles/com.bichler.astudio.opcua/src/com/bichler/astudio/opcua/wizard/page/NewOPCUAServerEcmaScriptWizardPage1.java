package com.bichler.astudio.opcua.wizard.page;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUAServerEcmaScriptWizardPage1 extends WizardPage {

	private IWorkbench workbench = null;
	private IStructuredSelection selection = null;
	private Text txt_scriptname;
	private Text txt_scriptinterval;
	private Combo cmb_scripttype;
	private Label label;

	private String scriptName = "";
	private int scriptType = 0;
	private int interval = 0;

	/**
	 * Create the wizard.
	 */
	public NewOPCUAServerEcmaScriptWizardPage1(IWorkbench workbench, IStructuredSelection selection) {
		this();
		this.workbench = workbench;
		this.selection = selection;
	}

	/**
	 * Create the wizard.
	 * 
	 * @wbp.parser.constructor
	 */
	public NewOPCUAServerEcmaScriptWizardPage1() {
		super("wizardPage");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.ecmascript.title"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.ecmascript.description"));
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
		lblServername.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name"));

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

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.countertree.type"));

		cmb_scripttype = new Combo(container, SWT.READ_ONLY);
		cmb_scripttype.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmb_scripttype.getSelectionIndex() == 0) {
					label.setVisible(false);
					txt_scriptinterval.setVisible(false);
					scriptType = 0;
				} else {
					label.setVisible(true);
					txt_scriptinterval.setVisible(true);
					scriptType = 1;
				}
			}
		});
		cmb_scripttype.setItems(new String[] {
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.ecmascript.single"),
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.cycletime") });
		cmb_scripttype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cmb_scripttype.select(0);

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.advanced.cycletime"));
		label.setVisible(false);

		txt_scriptinterval = new Text(container, SWT.BORDER);
		txt_scriptinterval.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				interval = Integer.parseInt(txt_scriptinterval.getText());
			}
		});
		txt_scriptinterval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txt_scriptinterval.setVisible(false);

		this.init();
	}

	private void init() {
		this.txt_scriptname.setText(this.scriptName);
		this.cmb_scripttype.select(this.scriptType);
		this.txt_scriptinterval.setText("" + this.interval);
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public int getScriptType() {
		return scriptType;
	}

	public void setScriptType(int scriptType) {
		this.scriptType = scriptType;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}
