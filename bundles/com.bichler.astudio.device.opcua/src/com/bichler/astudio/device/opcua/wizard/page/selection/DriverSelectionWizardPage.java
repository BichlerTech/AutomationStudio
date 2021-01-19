package com.bichler.astudio.device.opcua.wizard.page.selection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.wizard.DeviceTargetWizard;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.driver.OPCDriverUtil;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DriverSelectionWizardPage extends WizardPage {
	boolean TEST_VERSION = false;
	private Table table;
	private CheckboxTableViewer tableViewer;
	private IFileSystem fs;
	private StudioModelNode serverNode;
	private Object[] selectedDrivers = new Object[0];
	private Object[] allDrivers = new Object[0];
	private Button btnAll;
	private Button btnNone;
	private IFileSystem device;
	private DeviceTargetWizard deviceTargetWizard;
	private boolean isValidateRequ = false;
	private Map<String, String> drivers = null;

	public CheckboxTableViewer getTableViewer() {
		return this.tableViewer;
	}

	public boolean isValidateRequ() {
		return isValidateRequ;
	}

	public void setValidateRequ(boolean isValidateRequ) {
		this.isValidateRequ = isValidateRequ;
	}

	public DriverSelectionWizardPage(DeviceTargetWizard deviceTargetWizard) {
		super("driver");
		setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.title"));
		setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.description"));
		this.deviceTargetWizard = deviceTargetWizard;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		this.tableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		this.tableViewer.setContentProvider(new DriverContentProvider());
		// composite advanced buttons
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setBounds(0, 0, 64, 64);
		// button add all
		this.btnAll = new Button(composite, SWT.NONE);
		GridData gd_btn_all = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_all.heightHint = 48;
		gd_btn_all.widthHint = 48;
		btnAll.setLayoutData(gd_btn_all);
		btnAll.setImage(StudioImageActivator.getImage(StudioImages.ICON_SELECT_ALL));
		btnAll.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.tooltip.selectall"));
		this.btnNone = new Button(composite, SWT.NONE);
		GridData gd_btn_none = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_none.widthHint = 48;
		gd_btn_none.heightHint = 48;
		btnNone.setLayoutData(gd_btn_none);
		btnNone.setImage(StudioImageActivator.getImage(StudioImages.ICON_SELECT_NONE));
		btnNone.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.tooltip.selectnone"));
		createTableViewerColumns();
		setHandler();
		// initialize();
	}

	public void initialize() {
		this.tableViewer.setInput(fs);
		this.tableViewer.setAllChecked(true);
		if (TEST_VERSION) {
			this.tableViewer.setAllChecked(false);
		}
		this.selectedDrivers = this.tableViewer.getCheckedElements();
		this.allDrivers = this.tableViewer.getCheckedElements();
		if (TEST_VERSION) {
			this.tableViewer.getControl().setEnabled(false);
			this.btnAll.setEnabled(false);
			this.btnNone.setEnabled(false);
		}
	}

	private void createTableViewerColumns() {
		TableViewerColumn tvcImg = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tvcImg.getColumn();
		tblclmnNewColumn_1.setAlignment(SWT.CENTER);
		tblclmnNewColumn_1.setWidth(50);
		tvcImg.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverImage();
				}
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				return "";
			}
		});
		TableViewerColumn tvcName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tvcName.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.name"));
		tvcName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverName();
				}
				return "";
			}
		});
		TableViewerColumn tvcType = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnType = tvcType.getColumn();
		tblclmnType.setWidth(100);
		tblclmnType.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.type"));
		tvcType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverType();
				}
				return "";
			}
		});
		TableViewerColumn tvcVersion = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnVersoin = tvcVersion.getColumn();
		tblclmnVersoin.setWidth(100);
		tblclmnVersoin.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.version"));
		tvcVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getForeground(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return ((OPCUAServerDriverModelNode) element).getDriverVersion();
				}
				return "";
			}
		});
		TableViewerColumn tvcTargetVersion = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTargetVersion = tvcTargetVersion.getColumn();
		tblclmnTargetVersion.setWidth(100);
		tblclmnTargetVersion.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.targetversion"));
		tvcTargetVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					return readVersionFromTarget((OPCUAServerDriverModelNode) element);
				}
				return "";
			}
		});
		TableViewerColumn tvcInfo = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnInfo = tvcInfo.getColumn();
		tblclmnInfo.setWidth(100);
		tblclmnInfo.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.driverselection.page.column.info"));
		tvcInfo.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					String targetversion = readVersionFromTarget((OPCUAServerDriverModelNode) element);
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!targetversion.contains(node.getDriverVersion())) {
						// de-select and de activate row
						tableViewer.setChecked(element, false);
						return "driver is not compatible with runtime";
					}
				}
				return "";
			}
		});
		tableViewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean isChecked(Object element) {
				if (element instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) element;
					if (!readVersionFromTarget((OPCUAServerDriverModelNode) element)
							.contains(node.getDriverVersion())) {
						return false;
					}
				}
				return false;
			}
		});
		tableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {

				// validate();

				if (event.getElement() instanceof OPCUAServerDriverModelNode) {
					OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) event.getElement();
					if (!validateVersion(node)) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING,
								"Driver {0} is not compatible with runtime!", node.getDriverName());
						// tableViewer.
						for (TableItem item : tableViewer.getTable().getItems()) {
							if (item.getChecked()) {
								if (!validateVersion((OPCUAServerDriverModelNode) item.getData())) {
									item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
								} else
									item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							} else {
								item.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						}
					}
				}
			}
		});
	}

	private boolean validateVersion(OPCUAServerDriverModelNode node) {
		String versions = readVersionFromTarget(node);
		return versions.contains(node.getDriverVersion());
	}

	private boolean validate() {
		setPageComplete(true);
		for (Object node : tableViewer.getCheckedElements()) {
			OPCUAServerDriverModelNode n = (OPCUAServerDriverModelNode) node;
			if (!validateVersion(n)) {
				setPageComplete(false);
				break;
			}
		}
		return true;
	}

	protected static final String HBS = "hbs";
	protected static final String COMET = "comet";
	protected static final String OPC_UA_SERVER = "opc_ua_server";

	private String getRootPath(IFileSystem targetFileSystem) {
		// HACK FOR HBS DATAHUB DEVICE
		if (targetFileSystem instanceof DataHubFileSystem) {
			return "/";
		}

		// target server workspace root
		String rootPathTarget = null;
		// check for separator
		if (targetFileSystem.getRootPath().endsWith(targetFileSystem.getTargetFileSeparator()))
			rootPathTarget = targetFileSystem.getRootPath();
		else {
			rootPathTarget = targetFileSystem.getRootPath() + targetFileSystem.getTargetFileSeparator();
		}
		return rootPathTarget;
	}

	private String getDefaultStructurePath(IFileSystem fs) {
		if (fs instanceof DataHubFileSystem) {
			return fs.getTargetFileSeparator() + HBS + fs.getTargetFileSeparator() + COMET + fs.getTargetFileSeparator()
					+ OPC_UA_SERVER + fs.getTargetFileSeparator();
		}

		return getRootPath(fs);
	}

	private String readVersionFromTarget(OPCUAServerDriverModelNode node) {
		// only validate if we are on previous page
		if (!isValidateRequ)
			return "0";
		if (drivers == null) {
			drivers = new HashMap<>();
			String drvs = getDefaultStructurePath(this.device)
					+ this.device.getTargetFileSeparator() + "runtime" + this.device.getTargetFileSeparator()
					+ "drivers" + this.device.getTargetFileSeparator(); // +
																		// node.getDriverType()
			// + this.device.getTargetFileSeparator();
			StringBuilder version = new StringBuilder();
			try {
				if (!this.device.connect()) {
					version.append("no connection");
				} else {
					String[] types = this.device.listDirs(drvs);
					for (String type : types) {
						version = new StringBuilder();
						if (type.compareTo(".") == 0 || type.compareTo("..") == 0)
							continue;
						String[] versions = this.device.listDirs(drvs + type);
						String deliminator = "";
						if (versions == null) {
							version.append("????");
						} else {
							for (String vers : versions) {
								if (vers.compareTo(".") == 0 || vers.compareTo("..") == 0)
									continue;
								version.append(deliminator + vers);
								deliminator = " | ";
							}
						}
						drivers.put(type, version.toString());
					}
					this.device.disconnect();
				}
			} catch (IOException e) {
				return "0";
			}
		}
		if (!drivers.containsKey(node.getDriverType()))
			return "????";
		return drivers.get(node.getDriverType());
	}

	private void setHandler() {
		this.tableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				selectedDrivers = ((CheckboxTableViewer) event.getSource()).getCheckedElements();
			}
		});
		btnAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(true);
				selectedDrivers = tableViewer.getCheckedElements();
			}
		});
		btnNone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(false);
				selectedDrivers = tableViewer.getCheckedElements();
			}
		});
	}

	class DriverContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IFileSystem) {
				return OPCDriverUtil.getAllDriverNamesFromOPCProject((IFileSystem) inputElement, serverNode);
			}
			return new String[0];
		}
	}

	public void setServerFileSystem(IFileSystem serverFileSystem) {
		this.fs = serverFileSystem;
	}

	public void setServerNode(StudioModelNode serverNode) {
		this.serverNode = serverNode;
	}

	public Object[] getSelectedDrivers() {
		return selectedDrivers;
	}

	public Object[] getAllDrivers() {
		return this.allDrivers;
	}

	public void setTargetFileSystem(IFileSystem device) {
		this.device = device;
	}

	public IFileSystem getTargetFileSystem() {
		return this.device;
	}

}
