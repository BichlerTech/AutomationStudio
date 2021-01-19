package com.bichler.astudio.editor.pubsub.wizard.page;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.pubsub.wizard.PubSubConnectionWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConnectionAddress;

public class DetailConnectionAddressPage extends AbstractDetailWizardPage {
	private Text txt_url;
	private Text txt_interface;

	private WrapperConnectionAddress model = null;

	public DetailConnectionAddressPage() {
		super("addresspage");
		setTitle("Address");
		setDescription("Properties of a address");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblNetworkaddressInterface = new Label(container, SWT.NONE);
		lblNetworkaddressInterface.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNetworkaddressInterface.setText("Networkaddress interface:");

		txt_interface = new Text(container, SWT.BORDER);
		txt_interface.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNetworkaddressUrl = new Label(container, SWT.NONE);
		lblNetworkaddressUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNetworkaddressUrl.setText("Networkaddress URL:");

		txt_url = new Text(container, SWT.BORDER);
		txt_url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		createApply(container);

		setDefaultValues();
		setHandler();
	}

	public WrapperConnectionAddress getAddress() {
		/*
		 * if (this.model != null) { return new Variant(this.model); }
		 */
		return this.model;
	}

	@Override
	public PubSubConnectionWizard getWizard() {
		return (PubSubConnectionWizard) super.getWizard();
	}

	private String getURI() throws URISyntaxException {
		if (this.txt_url.getText().isEmpty()) {
			return null;
		}

		return new URI(this.txt_url.getText()).toString();
	}

	private void setHandler() {
		btn_apply.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String addressUri = getURI();
					String networkInterface = (txt_interface.getText().isEmpty() ? null : txt_interface.getText());
					model = new WrapperConnectionAddress();
					model.setNetworkInterface(networkInterface);
					model.setUrl(addressUri);
					// model = new NetworkAddressUrlDataType(networkInterface, addressUri);
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}

		});
	}

	private void setDefaultValues() {
		if (getWizard().getElement().getAddress() != null) {
			WrapperConnectionAddress nadt = getWizard().getElement().getAddress();
			// network interface
			this.txt_interface.setText((nadt.getNetworkInterface() == null) ? "" : nadt.getNetworkInterface());
			this.txt_url.setText((nadt.getUrl() == null) ? "" : nadt.getUrl());
			this.model = nadt.clone();
		}
	}
}
