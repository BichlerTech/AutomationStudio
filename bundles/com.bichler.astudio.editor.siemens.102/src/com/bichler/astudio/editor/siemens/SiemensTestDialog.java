package com.bichler.astudio.editor.siemens;

import java.util.List;

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

import com.bichler.astudio.editor.siemens.SiemensDriverEditor.Device;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ComStatusUtils;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.SiemensTCPISODevice;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;
import com.bichler.opc.driver.siemens.communication.SiemensErrorCode;
import com.bichler.opc.driver.siemens.communication.SiemensPDUType;
import com.bichler.opc.driver.siemens.communication.SiemensReadDataPart;
import com.bichler.opc.driver.siemens.communication.SiemensReadParamPart;
import com.bichler.opc.driver.siemens.communication.SiemensReadResponseDataPart;
import com.bichler.opc.driver.siemens.communication.SiemensTPDUHeader;
import com.bichler.opc.driver.siemens.dp.SiemensBooleanItem;
import com.bichler.opc.driver.siemens.dp.SiemensByteItem;
import com.bichler.opc.driver.siemens.dp.SiemensCharItem;
import com.bichler.opc.driver.siemens.dp.SiemensDIntItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPPackages;
import com.bichler.opc.driver.siemens.dp.SiemensDWordItem;
import com.bichler.opc.driver.siemens.dp.SiemensDateAndTimeItem;
import com.bichler.opc.driver.siemens.dp.SiemensDateItem;
import com.bichler.opc.driver.siemens.dp.SiemensIntItem;
import com.bichler.opc.driver.siemens.dp.SiemensLRealItem;
import com.bichler.opc.driver.siemens.dp.SiemensRealItem;
import com.bichler.opc.driver.siemens.dp.SiemensStringCharItem;
import com.bichler.opc.driver.siemens.dp.SiemensStringItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeOfDayItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimerItem;
import com.bichler.opc.driver.siemens.dp.SiemensWordItem;
import com.bichler.opc.driver.siemens.transform.SiemensTransformFactory;

public class SiemensTestDialog extends TitleAreaDialog {
	public void setTxtIP(Text txtIP) {
		this.txtIP = txtIP;
	}

	public void setTxtPort(Text txtPort) {
		this.txtPort = txtPort;
	}

	public void setTxtRack(Text txtRack) {
		this.txtRack = txtRack;
	}

	public void setTxtSlot(Text txtSlot) {
		this.txtSlot = txtSlot;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	private Text txtIP;
	private Text txtPort;
	private Text txtRack;
	private Text txtSlot;
	private String ip;
	private String port;
	private String rack;
	private String slot;
	private Object[] nodes;
	private Device device;

	public SiemensTestDialog(Shell parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		createIP(container);
		createPort(container);
		createRack(container);
		createSlot(container);
		createDPs(container);
		return area;
	}

	private void createIP(Composite container) {
		Label lbtIP = new Label(container, SWT.NONE);
		lbtIP.setText("IP: ");
		GridData dataIP = new GridData();
		dataIP.grabExcessHorizontalSpace = true;
		dataIP.horizontalAlignment = GridData.FILL;
		txtIP = new Text(container, SWT.BORDER);
		txtIP.setLayoutData(dataIP);
		txtIP.setText(device.ip);
		Button btndp = new Button(container, SWT.NONE);
		btndp.setText("Verbinden");
		GridData datadpGrid = new GridData();
		// datadpGrid.grabExcessHorizontalSpace = true;
		// datadpGrid.horizontalAlignment = GridData.FILL;
		datadpGrid.widthHint = 55;
		btndp.setLayoutData(datadpGrid);
		btndp.addSelectionListener(new SelectionAdapter() {

		});
	}

	private void createPort(Composite container) {
		Label lbtport = new Label(container, SWT.NONE);
		lbtport.setText("Port: ");
		GridData dataPort = new GridData();
		dataPort.grabExcessHorizontalSpace = true;
		dataPort.horizontalAlignment = GridData.FILL;
		txtPort = new Text(container, SWT.BORDER);
		txtPort.setLayoutData(dataPort);
		txtPort.setText(device.port + "");
		new Label(container, SWT.NONE);
	}

	private void createRack(Composite container) {
		Label lbtRack = new Label(container, SWT.NONE);
		lbtRack.setText("Rack: ");
		GridData dataRack = new GridData();
		dataRack.grabExcessHorizontalSpace = true;
		dataRack.horizontalAlignment = GridData.FILL;
		txtRack = new Text(container, SWT.BORDER);
		txtRack.setLayoutData(dataRack);
		txtRack.setText(device.rack + "");
		new Label(container, SWT.NONE);
	}

	private void createSlot(Composite container) {
		Label lbtSlot = new Label(container, SWT.NONE);
		lbtSlot.setText("Slot: ");
		GridData dataSlot = new GridData();
		dataSlot.grabExcessHorizontalSpace = true;
		dataSlot.horizontalAlignment = GridData.FILL;
		txtSlot = new Text(container, SWT.BORDER);
		txtSlot.setLayoutData(dataSlot);
		txtSlot.setText(device.slot + "");
		new Label(container, SWT.NONE);
	}

	private void createDPs(Composite container) {
		for (Object obj : this.nodes) {
			final SiemensEntryModelNode node = (SiemensEntryModelNode) obj;
			Label lblDP = null;
			lblDP = new Label(container, SWT.NONE);
			lblDP.setText("Datenpunkt: " + node.getAddressType() + node.getAddress() + " " + node.getIndex() + " "
					+ node.getDataType());
			GridData datadp = new GridData();
			datadp.grabExcessHorizontalSpace = true;
			datadp.horizontalAlignment = GridData.FILL;
			final Text txtdp = new Text(container, SWT.BORDER);
			txtdp.setLayoutData(datadp);
			Button btndp = new Button(container, SWT.NONE);
			btndp.setText("Test");
			GridData datadpGrid = new GridData();
			// datadpGrid.grabExcessHorizontalSpace = true;
			// datadpGrid.horizontalAlignment = GridData.FILL;
			datadpGrid.widthHint = 55;
			btndp.setLayoutData(datadpGrid);
			btndp.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					SiemensTCPISODevice dev = new SiemensTCPISODevice();
					dev.setDeviceAddress(device.ip);
					dev.setRack(device.rack);
					dev.setSlot(device.slot);
					dev.setPort(device.port);
					dev.setUtils(new ComStatusUtils());
					if (dev.connect() != ComCommunicationStates.OPEN) {
						txtdp.setText("Connection not possible!!!!");
						return;
					}
					SiemensDPPackages pack = new SiemensDPPackages();
					SiemensDPItem actNode = null;
					int arraylength = 1;
					SiemensDataType dt = null;
					try {
						dt = SiemensDataType.valueOf(node.getDataType());
					} catch (IllegalArgumentException ex) {
						// now it is possible to have an array
						String datatype = node.getDataType();
						if (datatype.contains("[") && datatype.contains("]")) {
							// now extract array length
							String d = datatype.substring(0, datatype.indexOf("[")).trim();
							String arraycount = datatype.substring(datatype.indexOf("["));
							arraycount = arraycount.replace("[", "").replaceAll("]", "").trim();
							try {
								// try to parse int to integer
								arraylength = Integer.parseInt(arraycount);
								dt = SiemensDataType.valueOf(d.toUpperCase());
							} catch (NumberFormatException nex) {
							}
						}
					}
					switch (dt) {
					case BOOL:
						actNode = new SiemensBooleanItem();
						break;
					case BYTE:
						actNode = new SiemensByteItem();
						break;
					case CHAR:
						actNode = new SiemensCharItem();
						break;
					case DINT:
						actNode = new SiemensDIntItem();
						break;
					case DWORD:
						actNode = new SiemensDWordItem();
						break;
					case DATE:
						actNode = new SiemensDateItem();
						break;
					case DATE_AND_TIME:
						actNode = new SiemensDateAndTimeItem();
						break;
					case INT:
						actNode = new SiemensIntItem();
						break;
					case REAL:
						actNode = new SiemensRealItem();
					case LREAL:
						actNode = new SiemensLRealItem();
						break;
					case STRINGCHAR:
						actNode = new SiemensStringCharItem();
						break;
					case STRING:
						actNode = new SiemensStringItem();
						break;
					case TIME:
						actNode = new SiemensTimeItem();
						break;
					case TIME_OF_DAY:
						actNode = new SiemensTimeOfDayItem();
						break;
					case TIMER:
						actNode = new SiemensTimerItem();
						break;
					case WORD:
						actNode = new SiemensWordItem();
						break;
					default:
						txtdp.setText("No valid Siemens Datatype found! ");
						return;
					}
					// item.setActive(true);
					actNode.setAddress(node.getAddress());
					actNode.setAddressType(node.getAddressType());
					actNode.setArraylength(arraylength);
					actNode.setIndex(node.getIndex());
					actNode.setMaxReadByteCount(220);
					actNode.setMaxWriteByteCount(200);
					// item.setDataKind(SiemensDataKind.REAL);
					// item.setDataType(SiemensDataType.REAL);
					pack.addDP(actNode);
					pack.createReadMessage(dev);
					SiemensTransformFactory factory = new SiemensTransformFactory();
					factory.createTransform(actNode, Double.class);
					List<SiemensTPDUHeader> tpdulist = dev.readPackage(pack);
					List<SiemensDPItem> dps = pack.getDps();
					//
					if (tpdulist == null || tpdulist.size() == 0) {
						txtdp.setText("Unknown Error!!!!");
					} else {
						SiemensTPDUHeader header = tpdulist.get(0);
						if (header == null || header.getPdu() == null) {
							txtdp.setText("Unknown Header Error!!!!");
						} else {
							if (header.getPdu().getType() != SiemensPDUType.RESPONSE) {
								txtdp.setText("Wrong Message Type - Error!!!!");
							} else {
								if (header.getPdu().getErrorcode() != 0) {
									txtdp.setText("Error code from message: " + SiemensErrorCode
											.strerror(SiemensErrorCode.fromValue(header.getPdu().getErrorcode())));
								} else {
									if (header.getPdu().getParam() instanceof SiemensReadParamPart) {
										// get all dataparts
										// from response
										// int index = 0;
										for (SiemensReadDataPart d : ((SiemensReadParamPart) header.getPdu().getParam())
												.getDataParts()) {
											// is it a response?
											if (d instanceof SiemensReadResponseDataPart) {
												// now Verify error Code
												SiemensErrorCode errorcode = SiemensErrorCode.fromValue(
														((SiemensReadResponseDataPart) d).getDataErrorCode());
												if (errorcode != SiemensErrorCode.NO_ERROR) {
													txtdp.setText(SiemensErrorCode.strerror(errorcode));
												} else {
													ComByteMessage message = new ComByteMessage();
													message.addBuffer(((SiemensReadResponseDataPart) d).getData());
													try {
														String content = "";
														String sep = "";
														if (arraylength > 1) {
															content = "[";
														}
														for (int i = 0; i < arraylength; i++) {
															content += sep
																	+ actNode.getTransform().transToIntern(message);
															sep = ", ";
														}
														if (arraylength > 1) {
															content += "]";
														}
														txtdp.setText(content);
													} catch (ValueOutOfRangeException e1) {
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
												}
												//
											}
										}
									}
								}
							}
						}
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
