package com.bichler.astudio.editor.allenbradley;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.DataValue;

import com.bichler.astudio.editor.allenbradley.AllenBradleyDriverEditor.Device;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.opc.driver.ethernet_ip.ComEthernetIPDevice;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDIntItem;

public class AllenBradleyTestDialog extends TitleAreaDialog {

	private Text eipUrl;
	private Text eipPort;
	private Text eipSlot;
	private Object[] nodes;
	private Device device;

	public AllenBradleyTestDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		createEIPURL(container);
		createEIPPort(container);
		createEIPSlot(container);
		createDPs(container);
		return area;
	}

	private void createEIPURL(Composite container) {
		Label lbtURL = new Label(container, SWT.NONE);
		lbtURL.setText("URL: ");
		GridData dataUrl = new GridData();
		dataUrl.grabExcessHorizontalSpace = true;
		dataUrl.horizontalAlignment = GridData.FILL;

		eipUrl = new Text(container, SWT.BORDER);
		eipUrl.setLayoutData(dataUrl);
		eipUrl.setText(device.ip);

	}

	private void createEIPPort(Composite container) {
		Label lbtUSerName = new Label(container, SWT.NONE);
		lbtUSerName.setText("Port: ");
		GridData dataUserName = new GridData();
		dataUserName.grabExcessHorizontalSpace = true;
		dataUserName.horizontalAlignment = GridData.FILL;

		eipPort = new Text(container, SWT.BORDER);
		eipPort.setLayoutData(dataUserName);
		eipPort.setText(device.port + "");
	}

	private void createEIPSlot(Composite container) {
		Label lbtPassword = new Label(container, SWT.NONE);
		lbtPassword.setText("Slot");
		GridData dataPWD = new GridData();
		dataPWD.grabExcessHorizontalSpace = true;
		dataPWD.horizontalAlignment = GridData.FILL;
		eipSlot = new Text(container, SWT.BORDER);
		eipSlot.setLayoutData(dataPWD);
		eipSlot.setText(device.slot + "");
	}

	private void createDPs(Composite container) {
		Label lblDPs = new Label(container, SWT.NONE);
		lblDPs.setText("Datenpunkte:");
		new Label(container, SWT.NONE);
		for (Object obj : this.nodes) {
			final AllenBradleyEntryModelNode node = (AllenBradleyEntryModelNode) obj;

			Label lblItemName = new Label(container, SWT.NONE);
			lblItemName.setText("Ethernet IP Tag: ");
			GridData dataItemName = new GridData();
			dataItemName.grabExcessHorizontalSpace = true;
			dataItemName.horizontalAlignment = GridData.FILL;
			final Text txtItemName = new Text(container, SWT.BORDER);
			txtItemName.setText(node.getSymbolName());
			txtItemName.setLayoutData(dataItemName);

			Button btndp = new Button(container, SWT.NONE);
			btndp.setText("Test");
			GridData datadpGrid = new GridData();

			GridData dataValue = new GridData();
			dataValue.grabExcessHorizontalSpace = true;
			dataValue.horizontalAlignment = GridData.FILL;
			final Text txtValue = new Text(container, SWT.BORDER);
			txtValue.setLayoutData(dataValue);
			GridData dataType = new GridData();
			dataType.grabExcessHorizontalSpace = true;
			dataType.horizontalAlignment = GridData.FILL;
			new Label(container, SWT.NONE);
			final Text txtDataType = new Text(container, SWT.BORDER);
			txtDataType.setLayoutData(dataType);
			datadpGrid.widthHint = 55;
			btndp.setLayoutData(datadpGrid);
			btndp.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					ComEthernetIPDevice dev = new ComEthernetIPDevice();
					dev.setDeviceAddress(device.ip + ";" + device.port + ";" + device.slot);
					String value = "";
					// first ty to connect to ethernet ip server
					if (dev.connectRead() > 0) {
						EthernetIPDIntItem item = new EthernetIPDIntItem();
						item.setActive(node.isActive());
						item.setAddress(node.getAddress());
						item.setdpName(node.getDataType());

						DataValue dv;
						try {
							dv = dev.read(item);
							if (dv.getValue().isArray()) {
								int arraycount = dv.getValue().getArrayDimensions()[0];
								value = "[";
								for (int i = 0; i < arraycount; i++) {

								}
								value += "]";
							} else {

							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						dev.disconnect();
					}

				}
			});

		}

	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	public void setDPs(Object[] selections, Device device) {
		this.nodes = selections;
		this.device = device;
	}
}
