package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConfigurationVersion;

public abstract class DetailVersionPage extends AbstractDetailWizardPage {
	protected Text txt_minVersion;
	protected Text txt_maxVersion;
	protected WrapperConfigurationVersion model;

	public DetailVersionPage() {
		super("versionpage");
		setTitle("Version");
		setDescription("Properties of a version");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblMinVersion = new Label(container, SWT.NONE);
		lblMinVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMinVersion.setText("Min. version:");

		txt_minVersion = new Text(container, SWT.BORDER);
		txt_minVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblMaxVersion = new Label(container, SWT.NONE);
		lblMaxVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxVersion.setText("Max. version:");

		txt_maxVersion = new Text(container, SWT.BORDER);
		txt_maxVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		createApply(container);

		setDefaultValue();
		setHandler();
	}

	abstract void setDefaultValue();

	private void setHandler() {
		btn_apply.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setMajorVersion(new UnsignedInteger(txt_maxVersion.getText()));
				model.setMinorVersion(new UnsignedInteger(txt_minVersion.getText()));
			}
			
			
		});
	}

	public WrapperConfigurationVersion getVersion() {
		return this.model;
	}
	


}
