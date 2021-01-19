package com.bichler.astudio.opcua.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;

public class OPCUA_Path_Selection extends PreferencePage implements IWorkbenchPreferencePage {
	public static final String ID = "com.bichler.astudio.opcua";
	private Text txt_OPCUARuntimeFolder;
	private Text txt_DriversFolder;
	private Text txt_DriversConfigFile;
	private Text txt_ModulesFolder;
	private Text txt_ModulesConfigFile;
	private Label lblOpcUaServers;
	private Label lblOpcUaDrivers;
	private Label lblOpcUaDrivers_1;
	private Text txt_OPCUAServersFolder;
	private Label lblConnectionsFile;
	private Text txt_connectionsFile;
	private Label lblOpcUaRuntime;
	private Text txt_OpcUaRuntime;
	private Label lblOpcUaSHA;
	private Button btn_OpcUaSHA;
	private Button btnFilePath_1;
	private Button btnFilePath_2;
	private Button btnFilePath_3;
	private Button btnFilePath_4;
	private Button btnFilePath_5;
	private Button btnFilePath_6;
	private Button btnFilePath_7;
	private Label lblonlyDataHUB;
	private Label lblDoCompileJar;
	private Label lblDoCompileAnisC;
	private Label lblDoDeleteSources;
	private Button btn_onlyDataHUB;
	private Button btn_doCompileJar;
	private Button btn_doCompileAnsiC;
	private Button btn_DoDeleteSources;
	private Label lblOpcUaModules;
	private Label lblOpcUaModules_1;

	/**
	 * Create the preference page.
	 */
	public OPCUA_Path_Selection() {
	}

	/**
	 * Create contents of the preference page.
	 * 
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		// initializeDefaultPreferences();
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, false));
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("OPC UA Runtime Folder:");
		txt_OPCUARuntimeFolder = new Text(container, SWT.BORDER);
		txt_OPCUARuntimeFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnFilePath_1 = new Button(container, SWT.NONE);
		btnFilePath_1.setText("\u2026");
		lblOpcUaServers = new Label(container, SWT.NONE);
		lblOpcUaServers.setText("OPC UA Servers Folder:");
		lblOpcUaServers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_OPCUAServersFolder = new Text(container, SWT.BORDER);
		txt_OPCUAServersFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnFilePath_2 = new Button(container, SWT.NONE);
		btnFilePath_2.setText("\u2026");
		lblOpcUaDrivers = new Label(container, SWT.NONE);
		lblOpcUaDrivers.setText("OPC UA Drivers Folder:");
		lblOpcUaDrivers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_DriversFolder = new Text(container, SWT.BORDER);
		txt_DriversFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnFilePath_3 = new Button(container, SWT.NONE);
		btnFilePath_3.setText("\u2026");
		lblOpcUaDrivers_1 = new Label(container, SWT.NONE);
		lblOpcUaDrivers_1.setText("OPC UA Drivers File:");
		lblOpcUaDrivers_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_DriversConfigFile = new Text(container, SWT.BORDER);
		txt_DriversConfigFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnFilePath_4 = new Button(container, SWT.NONE);
		btnFilePath_4.setText("\u2026");
		
		lblOpcUaModules = new Label(container, SWT.NONE);
		lblOpcUaModules.setText("OPC UA Modules Folder:");
		lblOpcUaModules.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_ModulesFolder = new Text(container, SWT.BORDER);
		txt_ModulesFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnFilePath_6 = new Button(container, SWT.NONE);
		btnFilePath_6.setText("\u2026");
		
		lblOpcUaModules_1 = new Label(container, SWT.NONE);
		lblOpcUaModules_1.setText("OPC UA Modules File:");
		lblOpcUaModules_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_ModulesConfigFile = new Text(container, SWT.BORDER);
		txt_ModulesConfigFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnFilePath_7 = new Button(container, SWT.NONE);
		btnFilePath_7.setText("\u2026");
		
		lblConnectionsFile = new Label(container, SWT.NONE);
		lblConnectionsFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionsFile.setText("Connections File:");
		txt_connectionsFile = new Text(container, SWT.BORDER);
		txt_connectionsFile.setText("");
		txt_connectionsFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		lblOpcUaRuntime = new Label(container, SWT.NONE);
		lblOpcUaRuntime.setText("OPC UA Runtime:");
		lblOpcUaRuntime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txt_OpcUaRuntime = new Text(container, SWT.BORDER);
		txt_OpcUaRuntime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnFilePath_5 = new Button(container, SWT.NONE);
		btnFilePath_5.setText("\u2026");
		lblOpcUaSHA = new Label(container, SWT.NONE);
		lblOpcUaSHA.setText("HASH Datei erzeugen:");
		lblOpcUaSHA.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btn_OpcUaSHA = new Button(container, SWT.CHECK);
		// btn_OpcUaSHA.setText("\u2026");
		new Label(container, SWT.NONE);
		lblonlyDataHUB = new Label(container, SWT.NONE);
		lblonlyDataHUB.setText("Only dataHUB upload:");
		lblonlyDataHUB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btn_onlyDataHUB = new Button(container, SWT.CHECK);
		// btn_onlyDataHUB.setText("Only dataHUB upload");
		new Label(container, SWT.NONE);
		lblDoCompileJar = new Label(container, SWT.NONE);
		lblDoCompileJar.setText("generiertes Modell kompilieren: (jar) ");
		lblDoCompileJar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btn_doCompileJar = new Button(container, SWT.CHECK);

		new Label(container, SWT.NONE);
		lblDoCompileAnisC = new Label(container, SWT.NONE);
		lblDoCompileAnisC.setText("generiertes Modell kompilieren: (anci c)");
		lblDoCompileAnisC.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btn_doCompileAnsiC = new Button(container, SWT.CHECK);

		new Label(container, SWT.NONE);
		lblDoDeleteSources = new Label(container, SWT.NONE);
		lblDoDeleteSources.setText("generierte Sourcedateien löschen");
		lblDoDeleteSources.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btn_DoDeleteSources = new Button(container, SWT.CHECK);

		// btn_onlyDataHUB.setText("Only dataHUB upload");
		new Label(container, SWT.NONE);
		setHandler();
		this.initializeValues();
		return container;
	}

	private void setHandler() {
		this.btnFilePath_1.addSelectionListener(new PathSelectionListener(this.txt_OPCUARuntimeFolder));
		this.btnFilePath_2.addSelectionListener(new PathSelectionListener(this.txt_OPCUAServersFolder));
		this.btnFilePath_3.addSelectionListener(new PathSelectionListener(this.txt_DriversFolder));
		this.btnFilePath_4.addSelectionListener(new PathSelectionListener(this.txt_DriversConfigFile));
		this.btnFilePath_5.addSelectionListener(new PathSelectionListener(this.txt_OpcUaRuntime));
		this.btnFilePath_6.addSelectionListener(new PathSelectionListener(this.txt_ModulesFolder));
		this.btnFilePath_7.addSelectionListener(new PathSelectionListener(this.txt_ModulesConfigFile));
	}

	class PathSelectionListener extends SelectionAdapter {
		private Text txt2display;

		public PathSelectionListener(Text textToDisplay) {
			super();
			this.txt2display = textToDisplay;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			DirectoryDialog fd = new DirectoryDialog(getShell());
			fd.setMessage("Auswahl des OPC UA Laufzeitverzeichnis");
			fd.setText("OPC UA Laufzeit");
			String path = fd.open();
			if (path != null) {
				txt2display.setText(path);
			}
		}
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(OPCUAActivator.getDefault().getPreferenceStore());
		// setDescription("HB Studio Designer");
	}

	private void initializeValues() {
		IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
		this.txt_DriversFolder.setText(store.getString(OPCUAConstants.ASOPCUADriversFolder));
		this.txt_DriversConfigFile.setText(store.getString(OPCUAConstants.ASOPCUADriverConfigFile));
		
		this.txt_ModulesFolder.setText(store.getString(OPCUAConstants.ASOPCUAModulesFolder));
		this.txt_ModulesConfigFile.setText(store.getString(OPCUAConstants.ASOPCUAModuleConfigFile));
		
		this.txt_OPCUAServersFolder.setText(store.getString(OPCUAConstants.ASOPCUAServersPath));
		this.txt_OPCUARuntimeFolder.setText(store.getString(OPCUAConstants.ASOPCUARuntimePath));
		this.txt_connectionsFile.setText(store.getString(OPCUAConstants.OPCUAConnectionsFile));
		this.txt_OpcUaRuntime.setText(store.getString(OPCUAConstants.OPCUARuntime));
		boolean onlyCombox = store.getBoolean(OPCUAConstants.OPCUAOnlyCombox);
		this.btn_onlyDataHUB.setSelection(onlyCombox);
		boolean allowSha = store.getBoolean(OPCUAConstants.OPCUASHA);
		this.btn_OpcUaSHA.setSelection(allowSha);
		boolean doCompileJar = store.getBoolean(OPCUAConstants.OPCUADoCompileJar);
		this.btn_doCompileJar.setSelection(doCompileJar);
		boolean doCompileAnsiC = store.getBoolean(OPCUAConstants.OPCUADoCompileAnsiC);
		this.btn_doCompileAnsiC.setSelection(doCompileAnsiC);
		boolean doDelSources = store.getBoolean(OPCUAConstants.OPCUADoDeleteSources);
		this.btn_DoDeleteSources.setSelection(doDelSources);
	}

	@Override
	protected void performApply() {
		IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
		store.setValue(OPCUAConstants.ASOPCUADriversFolder, this.txt_DriversFolder.getText());
		store.setValue(OPCUAConstants.ASOPCUADriverConfigFile, this.txt_DriversConfigFile.getText());
		store.setValue(OPCUAConstants.ASOPCUAModulesFolder, this.txt_ModulesFolder.getText());
		store.setValue(OPCUAConstants.ASOPCUAModuleConfigFile, this.txt_ModulesConfigFile.getText());
		store.setValue(OPCUAConstants.ASOPCUAServersPath, this.txt_OPCUAServersFolder.getText());
		store.setValue(OPCUAConstants.ASOPCUARuntimePath, this.txt_OPCUARuntimeFolder.getText());
		store.setValue(OPCUAConstants.OPCUAConnectionsFile, this.txt_connectionsFile.getText());
		store.setValue(OPCUAConstants.OPCUARuntime, this.txt_OpcUaRuntime.getText());
		store.setValue(OPCUAConstants.OPCUASHA, this.btn_OpcUaSHA.getSelection());
		store.setValue(OPCUAConstants.OPCUAOnlyCombox, this.btn_onlyDataHUB.getSelection());
		store.setValue(OPCUAConstants.OPCUADoCompileJar, this.btn_doCompileJar.getSelection());
		store.setValue(OPCUAConstants.OPCUADoCompileAnsiC, this.btn_doCompileAnsiC.getSelection());
		store.setValue(OPCUAConstants.OPCUADoDeleteSources, this.btn_DoDeleteSources.getSelection());
	}

	@Override
	public boolean performOk() {
		performApply();
		NavigationView view = (NavigationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(NavigationView.ID);
		if (view != null) {
			view.refresh(OPCUARootModelNode.class, true);
		}
		// view.refresh(node);
		return super.performOk();
	}
}
