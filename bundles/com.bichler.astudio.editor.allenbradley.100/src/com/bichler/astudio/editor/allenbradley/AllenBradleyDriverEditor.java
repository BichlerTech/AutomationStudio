package com.bichler.astudio.editor.allenbradley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
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

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyResources;
import com.bichler.astudio.editor.allenbradley.driver.AllenBradleyDriverUtil;
import com.bichler.astudio.editor.allenbradley.model.AllenBradleyRootStartConfigurationNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.AbstractOPCDriverConfigSelectionEditorPart;
import com.bichler.astudio.opcua.editor.input.OPCUADriverEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.TriggerNodesWidget;
import com.bichler.astudio.opcua.widget.TriggerNodesWidget.TriggerExpansionListener;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.utils.ui.swt.NumericText;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

import opc.sdk.core.node.Node;

public class AllenBradleyDriverEditor extends AbstractOPCDriverConfigSelectionEditorPart {
	public static final String ID = "com.bichler.astudio.editor.allenbradley.AllenBradleyDriverEditor";
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * @author hannes
	 * 
	 */
	class Device {
		String ip = "localhost";
		int port = 102;
		int slot = 0;
		int reconnecttimeout = 1000;
		boolean scanCyclic = true;
		int scanCyclicInterval = 1000;
		List<NodeId> driverstatusnodes = new ArrayList<>();
		public boolean driverstatusflag = false;
	}

	public Device getDevice() {
		return device;
	}

	public class StartConfigurationNode {
		boolean isActive = true;
		String name = "";
		NodeId nodeId = NodeId.NULL;
		int index = -1;
		int value = -1;
	}

	private String drvConfigString = "";
	private Device device = null;
	private int maxNodeId = 10;
	private boolean dirty = false;
	private Text txt_IP;
	private Text txt_Port;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Composite composite;
	private ScrolledComposite scrolledComposite;
	private Form form;
	private Combo cmb_Slot;
	private NumericText txt_ReconnectTime;
	private NumericText txt_scanCyclic;
	private CheckBoxButton cb_scanCyclic;
	private TriggerNodesWidget tnWidget;

	public AllenBradleyDriverEditor() {
		device = new Device();
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
		form.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.title"));
		formToolkit.decorateFormHeading(form);
		form.getBody().setLayout(new FillLayout());
		this.scrolledComposite = new ScrolledComposite(form.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		this.composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(3, false));
		createOPCDriverModelSection(this.composite);
		createDriverConfigSection(composite);
		createTriggerNodesSection(composite);
		scrolledComposite.setContent(composite);
		this.fillControls();
		this.addHandlers();
		setExpandHandlers();
		computeSection();
		getSite().setSelectionProvider(this);
	}

	public class TriggerNodesExpansionHandler implements TriggerExpansionListener {
		@Override
		public void compute() {
			computeSection();
		}
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
		String address = this.txt_IP.getText() + ";" + this.txt_Port.getText() + ";"
		/* + this.cmb_Rack.getText() + ";" */ + this.cmb_Slot.getText();
		String scanCyclic = this.cb_scanCyclic.isChecked() + ";" + this.txt_scanCyclic.getText();
		IFileSystem fs = getEditorInput().getFileSystem();
		String config = getEditorInput().getDriverConfigPath();
		try {
			if (!fs.isFile(config)) {
				fs.addFile(config);
			}
			if (fs.isFile(config)) {
				OutputStream output = null;
				try {
					if (this.drvConfigString.contains("%address%")) {
						tmpoutput = this.drvConfigString.replace("%address%", address);
					} else {
						tmpoutput = this.drvConfigString + "\ndeviceaddress\n" + address + "\n";
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
						tmpoutput = tmpoutput.replace("%drvstatusflag%", "" + isDriverStatusModel());
					} else {
						tmpoutput = tmpoutput + "\n\ndrvstatusflag\n" + isDriverStatusModel() + "\n";
					}
					if (tmpoutput.contains("%driverstatus%")) {
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
						tmpoutput = tmpoutput.replace("%driverstatus%", builder.toString());
					} else {
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
						if (!this.device.driverstatusnodes.isEmpty()) {
							tmpoutput = tmpoutput + "\ndriverstatus\n" + builder.toString() + "\n";
						}
					}
					output = fs.writeFile(config);
					output.write(tmpoutput.getBytes());
					output.flush();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							logger.log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
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
							if (items.length >= 1) {
								device.ip = items[0];
								device.port = Integer.parseInt(items[1]);
							}
							drvConfigString += "%address%\n";
						}
					} else if (line.compareTo("reconnecttimeout") == 0) {
						line = bfr.readLine();
						if (line != null) {
							try {
								device.reconnecttimeout = Integer.parseInt(line);
							} catch (NumberFormatException ex) {
								logger.log(Level.SEVERE, ex.getMessage(), ex);
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
								logger.log(Level.SEVERE, ex.getMessage(), ex);
							}
							drvConfigString += "%scancyclic%\n";
						}
					} else if (line.compareTo("drvstatusflag") == 0) {
						line = bfr.readLine();
						if (line != null) {
							device.driverstatusflag = Boolean.parseBoolean(line);
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
								logger.log(Level.SEVERE, e.getMessage(), e);
							}
						}
						drvConfigString += "%driverstatus%\n";
					}
				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			} finally {
				if (bfr != null) {
					try {
						bfr.close();
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				}
			}
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - "
				+ CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.driver.driver"));
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					monitor.beginTask(
							CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.editor.allenbradley.editor.driver.monitor.task"),
							IProgressMonitor.UNKNOWN);
					monitor.subTask(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.allenbradley.editor.driver.monitor.initialize"));
					IFileSystem fs = getEditorInput().getFileSystem();
					String config = getEditorInput().getDriverConfigPath();

					initDevice(fs, config);

					monitor.done();
				}
			});
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
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

	class StartConfigContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof AllenBradleyRootStartConfigurationNode) {
				return ((AllenBradleyRootStartConfigurationNode) inputElement).getChildren();
			}
			return new Object[0];
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
	public OPCUADriverEditorInput getEditorInput() {
		return (OPCUADriverEditorInput) super.getEditorInput();
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		this.listener = null;
	}

	@Override
	public void setSelection(ISelection selection) {
	}

	@Override
	public void refreshDatapoints() {
		this.tnWidget.fillTriggernodes();
	}

	@Override
	public void onFocusRemoteView() {
		AllenBradleyDBResourceManager structManager = AllenBradleyResources.getInstance()
				.getDBResourceManager(getEditorInput().getDriverName());
		AllenBradleyDriverUtil.openDriverView(getEditorInput().getFileSystem(), getEditorInput().getDriverConfigPath(),
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
	public boolean isTriggerNodeValid(NodeToTrigger obj) {
		return false;
	}

	@Override
	public List<NodeToTrigger> getPossibleTriggerNodes() {
		return new ArrayList<>();
	}

	@Override
	public void setPossibleTriggerNodes(List<NodeToTrigger> possibleTriggerNodes) {
	}

	public int getMaxNodeId() {
		return maxNodeId;
	}

	public void setMaxNodeId(int maxNodeId) {
		this.maxNodeId = maxNodeId;
	}

	private void addHandlers() {
		cmb_Slot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				device.slot = Integer.parseInt(cmb_Slot.getText());
				setDirty(true);
			}
		});
		txt_IP.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_Port.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_ReconnectTime.addModifyListener(new ModifyListener() {
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
		this.tnWidget.getTriggerViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setSelectionBO(((StructuredSelection) event.getSelection()).getFirstElement());
			}
		});
		super.addHandler(getEditorInput().getDriverName(), device.driverstatusnodes);
	}

	private void createDriverConfigSection(Composite composite) {
		Section sctnNewSection = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection
				.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE, "ab.drv.settings"));
		sctnNewSection.setExpanded(true);
		Composite sectionParent = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(sectionParent);
		sectionParent.setLayout(new GridLayout(3, false));
		sctnNewSection.setClient(sectionParent);
		Label lblIp = new Label(sectionParent, SWT.NONE);
		lblIp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblIp.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.ip"));
		lblIp.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.ip.tooltip"));
		lblIp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_IP = new Text(sectionParent, SWT.BORDER);
		txt_IP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblPort = new Label(sectionParent, SWT.NONE);
		lblPort.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblPort.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.port"));
		lblPort.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.port.tooltip"));
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_Port = new NumericText(sectionParent, SWT.BORDER);
		txt_Port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblSlot = new Label(sectionParent, SWT.NONE);
		lblSlot.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblSlot.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSlot.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.slot"));
		lblSlot.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.slot.tooltip"));
		cmb_Slot = new Combo(sectionParent, SWT.READ_ONLY);
		cmb_Slot.setItems(new String[] { "0", "1", "2", "3", "4", "5" });
		cmb_Slot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblTimeout = new Label(sectionParent, SWT.NONE);
		lblTimeout.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblTimeout.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.timeout"));
		lblTimeout.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.timeout.tooltip"));
		lblTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_ReconnectTime = new NumericText(sectionParent, SWT.BORDER);
		txt_ReconnectTime.setText("0");
		txt_ReconnectTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblScanCyclic = new Label(sectionParent, SWT.NONE);
		lblScanCyclic.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblScanCyclic.setText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.scancyclic"));
		lblScanCyclic.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.driver.scancyclic.tooltip"));
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

	private void createTriggerNodesSection(Composite composite) {
		this.tnWidget = new TriggerNodesWidget(composite, SWT.NONE, ServerInstance.getInstance(), this);
	}

	private void fillControls() {
		txt_IP.setText(device.ip);
		txt_Port.setText("" + device.port);
		//
		cmb_Slot.setText(device.slot + "");
		//
		txt_ReconnectTime.setText(device.reconnecttimeout + "");
		//
		txt_scanCyclic.setText(device.scanCyclicInterval + "");
		//
		cb_scanCyclic.setChecked(device.scanCyclic);
		fillDriverStatus(device.driverstatusflag, device.driverstatusnodes);
	}

	private void setExpandHandlers() {
		this.tnWidget.setExpansionHandler(new TriggerNodesExpansionHandler());
	}
}
