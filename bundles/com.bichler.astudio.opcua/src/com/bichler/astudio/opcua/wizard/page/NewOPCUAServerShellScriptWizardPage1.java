package com.bichler.astudio.opcua.wizard.page;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

public class NewOPCUAServerShellScriptWizardPage1 extends WizardPage {

	private IWorkbench workbench = null;
	private IStructuredSelection selection = null;
	private Text txt_name;
	private Combo cmb_type;

	private String scriptname = "";
	private int scripttype = 0;

	/**
	 * Create the wizard.
	 * 
	 * @wbp.parser.constructor
	 */
	public NewOPCUAServerShellScriptWizardPage1(IWorkbench workbench, IStructuredSelection selection) {
		this();
		this.workbench = workbench;
		this.selection = selection;
	}

	/**
	 * Create the wizard.
	 */
	public NewOPCUAServerShellScriptWizardPage1() {
		super("wizardPage");
		setTitle("New OPC UA Server");
		setDescription("Create a new OPC UA Data Server ");
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

		Label lbl_scriptname = new Label(container, SWT.NONE);
		lbl_scriptname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_scriptname.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name"));

		this.txt_name = new Text(container, SWT.BORDER);
		txt_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.txt_name.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				scriptname = ((Text) e.getSource()).getText();
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

		Label lbl_type = new Label(container, SWT.NONE);
		lbl_type.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_type.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.countertree.type"));

		this.cmb_type = new Combo(container, SWT.NONE);
		ShellType[] values = ShellType.values();
		String[] items = new String[values.length];

		for (int i = 0; i < values.length; i++) {
			items[i] = values[i].name();
		}

		cmb_type.setItems(items);
		cmb_type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmb_type.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = ((Combo) e.getSource()).getText();
				ShellType shell = ShellType.valueOf(type);
				// define scripttype
				scripttype = shell.getType();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		cmb_type.select(0);
	}

	public String getScriptName() {
		return this.scriptname;
	}

	public int getScriptType() {
		return this.scripttype;
	}

	enum ShellType {
		Pre(0), Post(1);

		private int type = 0;

		ShellType(int type) {
			this.type = type;
		}

		public int getType() {
			return this.type;
		}
	}
}
