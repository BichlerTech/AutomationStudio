package com.bichler.astudio.editor.xml_da;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.AbstractOPCDriverConfigSelectionEditorPart;
import com.bichler.astudio.opcua.editor.input.OPCUADriverEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.TriggerNodesWidget;
import com.bichler.astudio.opcua.widget.TriggerNodesWidget.TriggerExpansionListener;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.NumericText;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

import opc.sdk.core.node.Node;

public class XML_DA_DriverEditor extends AbstractOPCDriverConfigSelectionEditorPart {
	private static final String MAX_DP_COUNT = "max_dp_count";
	private static final String MAX_SCALAR_DP_COUNT = "max_scalar_dp_count";
	private static final String MAX_ARRAY_DP_COUNT = "max_array_dp_count";
	public final static String ID = "com.hbsoft.comet.editor.xml_da.XML_DA_DriverEditor"; //$NON-NLS-1$
	private String drvConfigString = "";
	private boolean dirty = false;
	private Text txt_url;
	private Text txt_username;
	private Text txt_password;
//  private Table table;
//  private List<NodeToTrigger> triggernodes = new ArrayList<NodeToTrigger>();
//  private TableViewer tv_StartconfigNodes;
//  private CheckBoxButton cb_Active;
	private NumericText txt_timeout;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Form form;
	private ScrolledComposite scrolledComposite;
	private Composite composite;
//  private Section sectionStartConfig;
//  private Section sectionTriggerNodes;
	private NumericText txt_maxDPs;
	private NumericText txt_maxArray;
	private NumericText txt_maxScalar;
	private TriggerNodesWidget tnWidget;
	private Device device = null;
	private int maxNodeId = 10;

	/**
	 * 
	 * @author hannes
	 * 
	 */
	class Device {
		String url = "http://192.168.1.100";
		String username = "simo";
		String password = "simo";
		public String timeout = "10000";
		int maxDPCount = 100;
		int maxScalarDPCount = 100;
		int maxArrayDPCount = 100;
		List<NodeId> driverstatusnodes = new ArrayList<>();
		public boolean driverstatusflag = false;
	}

	public XML_DA_DriverEditor() {
		device = new Device();
		
	}

	public Device getDevice() {
		return device;
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
		form.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.title"));
		formToolkit.decorateFormHeading(form);
		form.getBody().setLayout(new FillLayout());
		this.scrolledComposite = new ScrolledComposite(this.form.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		this.composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(3, false));
		createOPCDriverModelSection(this.composite);
		createConfigurationSection(this.composite);
		createTriggernodesSection(this.composite);
		scrolledComposite.setContent(composite);
		this.fillControls();
		setExpandHandler();
		computeSection();
		getSite().setSelectionProvider(this);
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - Treiber");
		getEditorInput().setToolTipText(
				getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - Treiber v1.0.0");
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					monitor.beginTask(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.xml_da.editor.dp.loaddp"), IProgressMonitor.UNKNOWN);
					monitor.subTask(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.xml_da.editor.dp.initialize"));
					IFileSystem fs = getEditorInput().getFileSystem();
					String config = getEditorInput().getDriverConfigPath();

					initDevice(fs, config);

					String serverpath = getEditorInput().getServerRuntimePath();
					if (!serverpath.endsWith("/")) {
						serverpath += "/";
					}
					monitor.done();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void initDevice(IFileSystem fs, String config) {
		if (fs.isFile(config)) {
			try {
				BufferedReader bfr = new BufferedReader(new InputStreamReader(fs.readFile(config)));
				String line = "";
				while ((line = bfr.readLine()) != null) {
					drvConfigString += line + "\n";
					if (line.compareTo("deviceaddress") == 0) {
						line = bfr.readLine();
						if (line != null) {
							// get all configured address parts
							String[] items = line.split(";");
							if (items.length >= 4) {
								// we have all entries
								device.url = items[0];
								device.username = items[1];
								device.password = items[2];
								device.timeout = items[3];
							}
							drvConfigString += "%address%\n";
						}
					} else if (line.compareTo("drvproperties") == 0) {
						line = bfr.readLine();
						if (line != null) {
							// get all configured address parts
							String[] items = line.split(";");
							if (items.length >= 3) {
								// we have all entries
								try {
									device.maxDPCount = Integer.parseInt(items[0].replace("max_dp_count", ""));
								} catch (NumberFormatException nfe) {
									nfe.printStackTrace();
								}
								try {
									device.maxScalarDPCount = Integer
											.parseInt(items[1].replace("max_scalar_dp_count", ""));
								} catch (NumberFormatException nfe) {
									nfe.printStackTrace();
								}
								try {
									device.maxArrayDPCount = Integer
											.parseInt(items[2].replace("max_array_dp_count", ""));
								} catch (NumberFormatException nfe) {
									nfe.printStackTrace();
								}
							}
							drvConfigString += "%drvproperties%\n";
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
							} catch (ArrayIndexOutOfBoundsException e) {
								// e.printStackTrace();
							} catch (IllegalArgumentException e) {
							}
						}
						drvConfigString += "%driverstatus%\n";
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
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
	public void setFocus() {
		// Set the focus
		super.setFocus();
		this.form.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		String tmpoutput = "";
		String address = this.txt_url.getText() + ";" + this.txt_username.getText() + ";" + this.txt_password.getText()
				+ ";" + this.txt_timeout.getText();
		String maxDevCount = MAX_DP_COUNT + this.txt_maxDPs.getText() + ";" + MAX_SCALAR_DP_COUNT
				+ this.txt_maxScalar.getText() + ";" + MAX_ARRAY_DP_COUNT + this.txt_maxArray.getText();
		IFileSystem fs = getEditorInput().getFileSystem();
		String config = getEditorInput().getDriverConfigPath();
		try {
			if (!fs.isFile(config)) {
				fs.addFile(config);
			}
			if (fs.isFile(config)) {

				try (OutputStream output = fs.writeFile(config);) {
					if (this.drvConfigString.contains("%address%")) {
						tmpoutput = this.drvConfigString.replace("%address%", address);
					} else {
						tmpoutput = this.drvConfigString + "\ndeviceaddress\n" + address + "\n";
					}
					if (tmpoutput.contains("%drvproperties%")) {
						tmpoutput = tmpoutput.replace("%drvproperties%", maxDevCount);
					} else {
						tmpoutput = tmpoutput + "\ndrvproperties\n" + maxDevCount;
					}
					if (tmpoutput.contains("%drvstatusflag%")) {
						tmpoutput = tmpoutput.replace("%drvstatusflag%", "" + isDriverStatusModel());
					} else {
						tmpoutput = tmpoutput + "\ndrvstatusflag\n" + isDriverStatusModel() + "\n";
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
						if (!this.device.driverstatusnodes.isEmpty()) {
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
							tmpoutput = tmpoutput + "\ndriverstatus\n" + builder.toString() + "\n";
						}
					}

					output.write(tmpoutput.getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.tnWidget.saveTriggerNodes();
		this.setDirty(false);
		// now validate trigger nodes
		OPCUAUtil.validateOPCUADriver(getEditorInput().getFileSystem(), getEditorInput().getNode());
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
		// TODO Auto-generated method stub
		return (OPCUADriverEditorInput) super.getEditorInput();
	}

	public class TriggerNodesExpansionHandler implements TriggerExpansionListener {
		@Override
		public void compute() {
			computeSection();
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
		// TODO Auto-generated method stub
	}

	@Override
	public void refreshDatapoints() {
		this.tnWidget.fillTriggernodes();
	}

	@Override
	public void onFocusRemoteView() {
		DriverBrowserUtil.openEmptyDriverModelView();
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
		txt_url.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// device.ip = txt_url.getText();
				setDirty(true);
			}
		});
		txt_username.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_password.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_timeout.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_maxDPs.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_maxScalar.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		txt_maxArray.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
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

	private void createTriggernodesSection(Composite composite2) {
		/**
		 * Section with a client composite
		 */
		this.tnWidget = new TriggerNodesWidget(composite, SWT.NONE, ServerInstance.getInstance(), this);
	}

	private void createConfigurationSection(Composite composite) {
		// final FormToolkit formToolkit = new
		// FormToolkit(Display.getDefault());
		Section sctnNewSection = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.settings"));
		sctnNewSection.setExpanded(true);
		Composite sectionParent = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(sectionParent);
		sectionParent.setLayout(new GridLayout(3, false));
		sctnNewSection.setClient(sectionParent);
		Label lblUrl = new Label(sectionParent, SWT.NONE);
		lblUrl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblUrl.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.url"));
		lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_url = new Text(sectionParent, SWT.BORDER);
		txt_url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblPort = new Label(sectionParent, SWT.NONE);
		lblPort.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblPort.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.user"));
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_username = new Text(sectionParent, SWT.BORDER);
		txt_username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblRack = new Label(sectionParent, SWT.NONE);
		lblRack.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblRack.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRack.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.password"));
		txt_password = new Text(sectionParent, SWT.BORDER);
		txt_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblTimeout = new Label(sectionParent, SWT.NONE);
		lblTimeout.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblTimeout.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.timeout"));
		lblTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_timeout = new NumericText(sectionParent, SWT.BORDER);
		txt_timeout.setText("10000");
		txt_timeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblMaxDPs = new Label(sectionParent, SWT.NONE);
		lblMaxDPs.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMaxDPs.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.maxdp"));
		lblMaxDPs.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_maxDPs = new NumericText(sectionParent, SWT.BORDER);
		txt_maxDPs.setText("-1");
		txt_maxDPs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblMaxScalarDPs = new Label(sectionParent, SWT.NONE);
		lblMaxScalarDPs.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMaxScalarDPs.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.maxscalardp"));
		lblMaxScalarDPs.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_maxScalar = new NumericText(sectionParent, SWT.BORDER);
		txt_maxScalar.setText("-1");
		txt_maxScalar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblMaxArray = new Label(sectionParent, SWT.NONE);
		lblMaxArray.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMaxArray.setText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.driver.maxfielddp"));
		lblMaxArray.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_maxArray = new NumericText(sectionParent, SWT.BORDER);
		txt_maxArray.setText("-1");
		txt_maxArray.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	}

	private void fillControls() {
		// now update all controls:
		// 1 load configuration from driver.com file
		// 2 add configuration text to each ui control
		// IFileSystem fs = getEditorInput().getFileSystem();
		// String config = getEditorInput().getDriverConfigPath();
		//
		// try {
		// if (!fs.isFile(config)) {
		// fs.addFile(config);
		// }
		//
		// // check if driver config file exists and it is a file
		// if (fs.isFile(config)) {
		// BufferedReader reader = null;
		// try {
		// InputStream input = fs.readFile(config);
		// reader = new BufferedReader(new InputStreamReader(input));
		// String line = "";
		// while ((line = reader.readLine()) != null) {
		// if (line.startsWith("#"))
		// continue;
		//
		// /**
		// * we reached "deviceaddress" section, so load device
		// * string and load properties for the defined
		// * communication type
		// */
		// if (line.compareTo("deviceaddress") == 0) {
		// String address = reader.readLine();
		// if (address == null) {
		// /**
		// * we couldn't find any device address line so
		// * show dialog and log message TODO log message
		// */
		// // MessageDialog dialog =
		// new MessageDialog(getSite().getShell(),
		// CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
		// "com.bichler.astudio.editor.xml_da.editor.driver.message.drvconfig"),
		// new Display().getSystemImage(SWT.ICON_ERROR),
		// CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
		// "com.bichler.astudio.editor.xml_da.editor.driver.message.drvconfig.description"),
		// 0, new String[] { "OK" }, 0);
		// break;
		// }
		// String[] addressitems = address.split(";");
		// // we need to check protocoltype
		// if (addressitems != null && addressitems.length >= 4) {
		//
		// this.device.url = addressitems[0];
		// this.device.username = addressitems[1];
		// this.device.password = addressitems[2];
		// this.device.timeout = addressitems[3];
		// }
		//
		// continue;
		// } else if (line.compareTo("drvproperties") == 0) {
		// line = reader.readLine();
		// if (line != null) {
		// // get all configured address parts
		// String[] items = line.split(";");
		//
		// if (items.length >= 3) {
		// // we have all entries
		//
		// // max_dp_count-1;max_scalar_dp_count-1;max_array_count_count1
		//
		// device.maxDPCount = Integer.parseInt(items[0].replace("max_dp_count",
		// ""));
		// device.maxScalarDPCount = Integer
		// .parseInt(items[1].replace("max_scalar_dp_count", ""));
		// device.maxArrayDPCount = Integer
		// .parseInt(items[2].replace("max_array_dp_count", ""));
		//
		// }
		// }
		// }
		//
		// if (line.compareTo("drvstatusflag") == 0) {
		// String driverstatusflag = reader.readLine();
		// try {
		// device.driverstatusflag = Boolean.parseBoolean(driverstatusflag);
		// } catch (NumberFormatException nfe) {
		// nfe.printStackTrace();
		// }
		// }
		//
		// if (line.compareTo("driverstatus") == 0) {
		// String driverstatus = null;
		// device.driverstatusnodes = new ArrayList<>();
		//
		// while ((driverstatus = reader.readLine()) != null) {
		// if (driverstatus.trim().isEmpty()) {
		// continue;
		// }
		//
		// int start = driverstatus.indexOf("=");
		// String nodeid = driverstatus.substring(start + 1);
		// String[] nodeid2parse = nodeid.split(";");
		// NodeId parsedId = NodeId.NULL;
		// try {
		// if (nodeid2parse.length == 1) {
		// parsedId = NodeId.parseNodeId(nodeid);
		// } else if (nodeid2parse.length == 2) {
		// String ns = nodeid2parse[0];
		// String idValue = nodeid2parse[1];
		// int index =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
		// .getIndex(ns);
		//
		// parsedId = NodeId.parseNodeId("ns=" + index + ";" + idValue);
		// }
		//
		// // check if node exists
		// Node node = ServerInstance.getInstance().getServerInstance()
		// .getAddressSpaceManager().getNodeById(parsedId);
		// if (node != null) {
		// device.driverstatusnodes.add(parsedId);
		// }
		// } catch (IllegalArgumentException e) {
		// // e.printStackTrace();
		// }
		// }
		// }
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (reader != null) {
		// try {
		// reader.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
		// } catch (IOException ex) {
		// ex.printStackTrace();
		// }
		txt_url.setText(device.url);
		txt_username.setText(device.username);
		txt_password.setText(device.password);
		txt_timeout.setText(device.timeout);
		txt_maxDPs.setText("" + device.maxDPCount);
		txt_maxScalar.setText("" + device.maxScalarDPCount);
		txt_maxArray.setText("" + device.maxArrayDPCount);
		fillDriverStatus(device.driverstatusflag, device.driverstatusnodes);
		// fillStartConfigurationNodes();
		// fillTriggernodes();
		this.addHandlers();
	}

	private void setExpandHandler() {
		this.tnWidget.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				computeSection();
			}
		});
		this.tnWidget.setExpansionHandler(new TriggerNodesExpansionHandler());
	}
}
