package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.pubsub.nodes.PubSubSecurityParameters;

public class PubSubReaderGroupPage extends AbstractMainWizardPage {

	private Text txt_name;
	private Text txt_securityParameters;

	public PubSubReaderGroupPage() {
		super("pubsubreadergroupWizardPage");
		setTitle("PubSubReaderGroup");
		setDescription("Properties of a PubSubReaderGroup");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(4, false));

		Label lbl_name = new Label(container, SWT.NONE);
		lbl_name.setText("Name:");

		txt_name = new Text(container, SWT.BORDER);
		txt_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_securityParameters = new Label(container, SWT.NONE);
		lbl_securityParameters.setText("Security parameters:");

		this.txt_securityParameters = new Text(container, SWT.NONE);
		txt_securityParameters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Button btn_securityParameters = new Button(container, SWT.NONE);
		btn_securityParameters.setBounds(0, 0, 90, 30);
		btn_securityParameters.setText("  ");

		setDefaultValues();
		setHandler();
	}

	/**
	 * Set default values to page controls
	 */
	private void setDefaultValues() {

	}

	/**
	 * Sets handlers to page controls
	 */
	private void setHandler() {
		this.txt_name.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});
	}

	@Override
	public boolean isPageComplete() {
		// field name
		String name = this.txt_name.getText();
		if (name.isEmpty()) {
			setErrorMessage("Name must be specified!");
			return false;
		}

		setErrorMessage(null);
		return true;
	}

	public String getElementName() {
		return this.txt_name.getText();
	}

	public PubSubSecurityParameters getSecurityParameters() {
		return null;
	}
}
