package com.bichler.astudio.editor.siemens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensResources;
import com.bichler.astudio.editor.siemens.driver.SiemensDriverUtil;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.AbstractOPCDriverConfigSelectionEditorPart;
import com.bichler.astudio.opcua.editor.input.OPCUADriverEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.TriggerNodesWidget;
import com.bichler.astudio.opcua.widget.TriggerNodesWidget.TriggerExpansionListener;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.utils.ui.swt.NumericText;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

import opc.sdk.core.node.Node;

public class SiemensDriverEditor extends AbstractOPCDriverConfigSelectionEditorPart {
	public final static String ID = "com.bichler.astudio.editor.siemens.101.SiemensDriverEditor"; //$NON-NLS-1$
	private static final String MAX_BYTE_COUNT = "max_bytecount";

	enum CommunicationProtocolType {
		ISOTCPS7_300, ISOTCPS7_1500;
	}

	/**
	 * 
	 * @author hannes
	 * 
	 */
	class Device {
		CommunicationProtocolType protocoltype = CommunicationProtocolType.ISOTCPS7_300;
		String ip = "localhost";
		int port = 102;
		int rack = 0;
		int slot = 0;
		int reconnecttimeout = 1000;
		int maxByteCount = 220;
		// int maxScalarDPCount = -1;
		// int maxArrayDPCount = -1;
		boolean scanCyclic = true;
		int scanCyclicInterval = 1000;
		boolean driverstatusflag = false;
		List<NodeId> driverstatusnodes = new ArrayList<>();
	}

	public class StartConfigurationNode {
		boolean isActive = true;
		String name = "";
		NodeId nodeId = NodeId.NULL;
		int index = -1;
		int value = -1;
	}

	private Map<String, CommunicationProtocolType> communicationTypeIDs = new HashMap<>();
	private Map<CommunicationProtocolType, String> communicationTypeVals = new HashMap<>();
	private String drvConfigString = "";
	private Device device = null;

	public Device getDevice() {
		return device;
	}

	private int maxNodeId = 10;
	private boolean dirty = false;
	private Text txt_IP;
	private Text txt_Port;
	// private UAServerApplicationInstance opcServer = null;
	// private Combo cmb_TargetRackSlot = null;
	private Combo cmb_Rack;
	private Combo cmb_Slot;
	private Combo cmb_CommunicationType;
	private Text txt_ReconnectTime;
	// private TableViewer tv_StartconfigNodes;
	// private CheckBoxButton cb_Active;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Composite composite;
	private ScrolledComposite scrolledComposite;
	private Form form;
	// private Section sectionStartConfig;
	// private Section sectionTriggernodes;
	private TriggerNodesWidget tnWidget;
	private NumericText txt_maxByteCount;
	private CheckBoxButton cb_scanCyclic;
	private NumericText txt_scanCyclic;

	public SiemensDriverEditor() {
		device = new Device();
		communicationTypeIDs.put("ISOTCPS7_1500", CommunicationProtocolType.ISOTCPS7_1500);
		communicationTypeVals.put(CommunicationProtocolType.ISOTCPS7_1500, "ISOTCPS7_1500");
		communicationTypeIDs.put("ISOTCPS7_300", CommunicationProtocolType.ISOTCPS7_300);
		communicationTypeVals.put(CommunicationProtocolType.ISOTCPS7_300, "ISOTCPS7_300");
	}

	public void computeSection() {
		Point minSize = this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.scrolledComposite.setMinSize(minSize);
		this.composite.layout(true);
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.form = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(form);
		form.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.title"));
		formToolkit.decorateFormHeading(form);
		form.getBody().setLayout(new FillLayout());
		this.scrolledComposite = new ScrolledComposite(form.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		this.composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(3, false));
		createOPCDriverModelSection(composite);
		createDriverConfigSection(composite);
		createTriggerNodesSection(composite);
		scrolledComposite.setContent(composite);
		this.fillControls();
		this.addHandlers();
		setExpandHandlers();
		computeSection();
		getSite().setSelectionProvider(this);
	}

	@Override
	public void setFocus() {
		// Set the focus
		super.setFocus();
		this.form.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		String tmpoutput = "";
		String address = this.communicationTypeIDs.get(this.cmb_CommunicationType.getText()) + ";"
				+ this.txt_IP.getText() + ";" + this.txt_Port.getText() + ";" + this.cmb_Rack.getText() + ";"
				+ this.cmb_Slot.getText();
		String maxDevCount = MAX_BYTE_COUNT + "=" + this.txt_maxByteCount.getText();
		String scanCyclic = this.cb_scanCyclic.isChecked() + ";" + this.txt_scanCyclic.getText();
		IFileSystem fs = getEditorInput().getFileSystem();
		String config = getEditorInput().getDriverConfigPath();
		try {
			if (!fs.isFile(config)) {
				fs.addFile(config);
			}
			if (fs.isFile(config)) {
				// if(file.exists()) {
				// OutputStream output = null;
				try (OutputStream output = fs.writeFile(config);) {
					if (this.drvConfigString.contains("%address%")) {
						tmpoutput = this.drvConfigString.replace("%address%", address);
					} else {
						tmpoutput = this.drvConfigString + "\ndeviceaddress\n" + address + "\n";
					}
					if (tmpoutput.contains("%drvproperties%")) {
						tmpoutput = tmpoutput.replace("%drvproperties%", maxDevCount);
					} else {
						tmpoutput = tmpoutput + "\ndrvproperties\n" + maxDevCount + "\n";
					}
					if (tmpoutput.contains("%reconnecttimeout%")) {
						tmpoutput = tmpoutput.replace("%reconnecttimeout%", this.txt_ReconnectTime.getText());
					} else {
						tmpoutput = tmpoutput + "\nreconnecttimeout\n" + this.txt_ReconnectTime.getText() + "\n";
					}
					if (tmpoutput.contains("%scancyclic%")) {
						tmpoutput = tmpoutput.replace("%scancyclic%", scanCyclic);
					} else {
						tmpoutput = tmpoutput + "\nscancyclic\n" + scanCyclic + "\n";
					}
					if (tmpoutput.contains("%drvstatusflag%")) {
						tmpoutput = tmpoutput.replace("%drvstatusflag%", "active=" + isDriverStatusModel());
					} else {
						tmpoutput = tmpoutput + "\ndrvstatusflag\n" + "active=" + isDriverStatusModel() + "\n";
					}
					if (tmpoutput.contains("%driverstatus%")) {
						StringBuilder builder = new StringBuilder();
						for (NodeId driverstatusNode : this.device.driverstatusnodes) {
							Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
									.getNodeById(driverstatusNode);
							String name = node.getBrowseName().getName();
							String uri = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
									.getUri(driverstatusNode.getNamespaceIndex());
							String value = driverstatusNode.toString();
							if (value.contains(";")) {
								value = value.split(";")[1];
								value = ";" + value;
							}
							builder.append(name + "=" + uri + value + "\n");
						}
						tmpoutput = tmpoutput.replace("%driverstatus%", builder.toString());
					} else {
						StringBuilder builder = new StringBuilder();
						for (NodeId driverstatusNode : this.device.driverstatusnodes) {
							Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
									.getNodeById(driverstatusNode);
							if (node == null) {
								continue;
							}
							String name = node.getBrowseName().getName();
							String uri = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
									.getUri(driverstatusNode.getNamespaceIndex());
							String value = driverstatusNode.toString();
							if (value.contains(";")) {
								value = value.split(";")[1];
								value = ";" + value;
							}
							builder.append(name + "=" + uri + value + "\n");
						}
						if (!builder.toString().isEmpty()) {
							tmpoutput = tmpoutput + "\ndriverstatus\n" + builder.toString();
						}
					}
					output.write(tmpoutput.getBytes());
					output.flush();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}

			}
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		this.tnWidget.saveTriggerNodes();
		this.setDirty(false);
		// now validate trigger nodes
		OPCUAUtil.validateOPCUADriver(getEditorInput().getFileSystem(), getEditorInput().getNode());
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - "
				+ CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.driver.title.driver"));
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					monitor.beginTask(
							CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.editor.siemens.editor.driver.config"),
							IProgressMonitor.UNKNOWN);
					monitor.subTask(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.siemens.editor.dp.initialize"));
					IFileSystem fs = getEditorInput().getFileSystem();
					String config = getEditorInput().getDriverConfigPath();
					initDevice(fs, config);
					monitor.done();
				}
			});
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void initDevice(IFileSystem fs, String config) {
		if (fs.isFile(config)) {
			BufferedReader bfr = null;
			try {
				bfr = new BufferedReader(new InputStreamReader(fs.readFile(config)));
				String line = "";
				while ((line = bfr.readLine()) != null) {
					drvConfigString += line + "\n";
					if (line.compareTo("deviceaddress") == 0) {
						line = bfr.readLine();
						if (line != null) {
							// get all configured address parts
							String[] items = line.split(";");
							if (items.length >= 5) {
								// we have all entries
								device.protocoltype = CommunicationProtocolType.valueOf(items[0]);
								device.ip = items[1];
								try {
									device.port = Integer.parseInt(items[2]);
								} catch (NumberFormatException ex) {
									device.port = 0;
								}
								device.rack = Integer.parseInt(items[3]);
								device.slot = Integer.parseInt(items[4]);
							}
							drvConfigString += "%address%\n";
						}
					} else if (line.compareTo("drvproperties") == 0) {
						line = bfr.readLine();
						if (line != null) {
							String[] items = line.split("=");
							// key/value pair
							if (items.length > 1) {
								int parsed = 220;
								try {
									parsed = Integer.parseInt(items[1]);
								} catch (NumberFormatException nfe) {
									// nfe.printStackTrace();
								}
								device.maxByteCount = parsed;
							}
							drvConfigString += "%drvproperties%\n";
						}
					} else if (line.compareTo("reconnecttimeout") == 0) {
						line = bfr.readLine();
						if (line != null) {
							try {
								device.reconnecttimeout = Integer.parseInt(line);
							} catch (NumberFormatException ex) {
								// TODO log exception
							}
							drvConfigString += "%reconnecttimeout%\n";
						}
					} else if (line.compareTo("scancyclic") == 0) {
						String cyclic = bfr.readLine();
						String[] cyclicLine = cyclic.split(";");
						if (cyclicLine.length > 1) {
							try {
								device.scanCyclic = Boolean.parseBoolean(cyclicLine[0]);
								device.scanCyclicInterval = Integer.parseInt(cyclicLine[1]);
							} catch (NumberFormatException ex) {
								// TODO logg exception
							}
							drvConfigString += "%scancyclic%\n";
						}
					} else if (line.compareTo("drvstatusflag") == 0) {
						line = bfr.readLine();
						if (line != null) {
							device.driverstatusflag = Boolean.parseBoolean(line.trim());
						}
						drvConfigString += "%drvstatusflag%\n";
					} else if (line.compareTo("driverstatus") == 0) {
						String driverstatus = null;
						device.driverstatusnodes = new ArrayList<>();
						while ((driverstatus = bfr.readLine()) != null) {
							if (driverstatus.trim().isEmpty()) {
								break;
							}
							int start = driverstatus.indexOf("=");
							String nodeid = driverstatus.substring(start + 1);
							String[] nodeid2parse = nodeid.split(";");
							NodeId parsedId = NodeId.NULL;
							try {
								if (nodeid2parse.length <= 0) {
									parsedId = NodeId.parseNodeId(nodeid);
								} else {
									String ns = nodeid2parse[0];
									String idValue = nodeid2parse[1];
									int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
											.getIndex(ns);
									parsedId = NodeId.parseNodeId("ns=" + index + ";" + idValue);
								}
								device.driverstatusnodes.add(parsedId);
							} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
							}
						}
						drvConfigString += "%driverstatus%\n";
					}
				}
			} catch (IOException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			} finally {
				if (bfr != null) {
					try {
						bfr.close();
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
				}
			}
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public String removeEscapes(String builder) {
		return builder.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
	}

	public int getMaxNodeId() {
		return maxNodeId;
	}

	public void setMaxNodeId(int maxNodeId) {
		this.maxNodeId = maxNodeId;
	}

	@Override
	public OPCUADriverEditorInput getEditorInput() {
		return (OPCUADriverEditorInput) super.getEditorInput();
	}

	@Override
	public void onNamespaceChange(NamespaceTableChangeParameter trigger) {
		// TODO Auto-generated method stub
	}

	@Override
	public void refreshDatapoints() {
		this.tnWidget.fillTriggernodes();
	}

	@Override
	public void onFocusRemoteView() {
		String drivername = getEditorInput().getDriverName();
		SiemensDBResourceManager structManager = SiemensResources.getInstance().getDBResourceManager(drivername);
		SiemensDriverUtil.openDriverView(getEditorInput().getFileSystem(), getEditorInput().getDriverConfigPath(),
				structManager);
	}

	@Override
	public void onDisposeRemoteView() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IEditorReference[] references = page.getEditorReferences();
				if (references == null || references.length == 0) {
					DriverBrowserUtil.openEmptyDriverModelView();
				}
			}
		}
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public ISelection getSelection() {
		if (this.currentSelection != null) {
			return new StructuredSelection(this.currentSelection);
		}
		return StructuredSelection.EMPTY;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		this.listener = null;
	}

	@Override
	public void setSelection(ISelection selection) {
	}

	@Override
	public boolean isTriggerNodeValid(NodeToTrigger obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NodeToTrigger> getPossibleTriggerNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPossibleTriggerNodes(List<NodeToTrigger> possibleTriggerNodes) {
		// TODO Auto-generated method stub
	}

	private void addHandlers() {
		txt_IP.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// device.ip = txt_IP.getText();
				setDirty(true);
			}
		});
		txt_Port.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_ReconnectTime.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_maxByteCount.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_scanCyclic.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		cb_scanCyclic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
		});
		cmb_Slot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				device.slot = Integer.parseInt(cmb_Slot.getText());
				setDirty(true);
			}
		});
		cmb_Rack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				device.rack = Integer.parseInt(cmb_Rack.getText());
				setDirty(true);
			}
		});
		cmb_CommunicationType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				device.protocoltype = CommunicationProtocolType.valueOf(cmb_CommunicationType.getText());
				setDirty(true);
			}
		});
		this.tnWidget.getTriggerViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setSelectionBO(((StructuredSelection) event.getSelection()).getFirstElement());
			}
		});
		super.addHandler(getEditorInput().getDriverName(), device.driverstatusnodes);
	}

	private void createTriggerNodesSection(Composite composite) {
		this.tnWidget = new TriggerNodesWidget(composite, SWT.NONE, ServerInstance.getInstance(), this);
	}

	private void createDriverConfigSection(Composite composite) {
		Section sctnNewSection = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.editor.driver.settings"));
		sctnNewSection.setExpanded(true);
		Composite sectionParent = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(sectionParent);
		sectionParent.setLayout(new GridLayout(3, false));
		sctnNewSection.setClient(sectionParent);
		Label lblUebertragung = new Label(sectionParent, SWT.NONE);
		lblUebertragung.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblUebertragung.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUebertragung.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.uebertragung"));
		cmb_CommunicationType = new Combo(sectionParent, SWT.READ_ONLY);
		cmb_CommunicationType.setItems(
				this.communicationTypeIDs.keySet().toArray(new String[this.communicationTypeIDs.keySet().size()]));
		cmb_CommunicationType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmb_CommunicationType.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.protocol"));
		cmb_CommunicationType.select(0);
		cmb_CommunicationType.notifyListeners(SWT.Selection, new Event());
		Label lblIp = new Label(sectionParent, SWT.NONE);
		lblIp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblIp.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.ip"));
		lblIp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_IP = new Text(sectionParent, SWT.BORDER);
		txt_IP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblPort = new Label(sectionParent, SWT.NONE);
		lblPort.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblPort.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.port"));
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_Port = new NumericText(sectionParent, SWT.BORDER);
		txt_Port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblRack = new Label(sectionParent, SWT.NONE);
		lblRack.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblRack.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRack.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.rack"));
		cmb_Rack = new Combo(sectionParent, SWT.READ_ONLY);
		cmb_Rack.setItems(new String[] { "0", "1", "2", "3", "4", "5" });
		cmb_Rack.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblSlot = new Label(sectionParent, SWT.NONE);
		lblSlot.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblSlot.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSlot.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.slot"));
		cmb_Slot = new Combo(sectionParent, SWT.READ_ONLY);
		cmb_Slot.setItems(new String[] { "0", "1", "2", "3", "4", "5" });
		cmb_Slot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblTimeout = new Label(sectionParent, SWT.NONE);
		lblTimeout.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblTimeout.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.timeout"));
		lblTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_ReconnectTime = new NumericText(sectionParent, SWT.BORDER);
		txt_ReconnectTime.setText("0");
		txt_ReconnectTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblMaxBytes = new Label(sectionParent, SWT.NONE);
		lblMaxBytes.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMaxBytes.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.maxbytelength"));
		lblMaxBytes.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_maxByteCount = new NumericText(sectionParent, SWT.BORDER);
		txt_maxByteCount.setText("-1");
		txt_maxByteCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblScanCyclic = new Label(sectionParent, SWT.NONE);
		lblScanCyclic.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblScanCyclic.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.driver.scancyclic"));
		lblScanCyclic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_scanCyclic = new NumericText(sectionParent, SWT.BORDER);
		txt_scanCyclic.setText("-1");
		txt_scanCyclic.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cb_scanCyclic = new CheckBoxButton(sectionParent, SWT.CHECK);
		GridData gd_cbt_scanCyclic = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cbt_scanCyclic.widthHint = 39;
		cb_scanCyclic.setLayoutData(gd_cbt_scanCyclic);
		cb_scanCyclic.setAlignment(SWT.LEFT);
		cb_scanCyclic.setBackground(SWTResourceManager.getColor(255, 255, 255));
	}

	private void fillControls() {
		try {
			cmb_CommunicationType.setText(device.protocoltype.name());
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		txt_IP.setText(device.ip);
		txt_Port.setText(Integer.toString(device.port));
		cmb_Rack.setText(Integer.toString(device.rack));
		cmb_Slot.setText(Integer.toString(device.slot));
		txt_ReconnectTime.setText(Integer.toString(device.reconnecttimeout));
		txt_maxByteCount.setText(Integer.toString(device.maxByteCount));
		txt_scanCyclic.setText(Integer.toString(device.scanCyclicInterval));
		cb_scanCyclic.setChecked(device.scanCyclic);
		fillDriverStatus(device.driverstatusflag, device.driverstatusnodes);
	}

	private void setExpandHandlers() {
		this.tnWidget.setExpansionHandler(new TriggerNodesExpansionHandler());
	}

	public class TriggerNodesExpansionHandler implements TriggerExpansionListener {
		@Override
		public void compute() {
			computeSection();
		}
	}
}
