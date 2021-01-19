package com.bichler.astudio.editor.siemens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.handlers.IHandlerService;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.AbstractOPCConfigDriverViewLinkEditorPart;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.editor.input.OPCUAAdvancedDriverEditorInput;
import com.bichler.astudio.opcua.events.OpenDriverModelBrowserParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.INamespaceTableChange;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.widget.AdvancedDriverConfigWidget;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

public class SiemensAdvancedDriverConfigEditor extends AbstractOPCConfigDriverViewLinkEditorPart
		implements INamespaceTableChange {
	public static final String ID = "com.bichler.astudio.opcua.editor.SiemensAdvancedDriverConfigEditor"; //$NON-NLS-1$
	private List<NodeToTrigger> possibleTriggerNodes = new ArrayList<>();
	private boolean dirty = false;
	private ScrolledComposite scrolledComposite;
	private Composite composite;
	private AdvancedDriverConfigWidget advancedDrvConfig;
	private UAServerApplicationInstance opcServer;

	public SiemensAdvancedDriverConfigEditor() {
	}

	public boolean isTriggerNodeValid(NodeToTrigger lookup) {
		// triggernode is null
		if (lookup == null) {
			return true;
		}
		if (NodeId.isNull(lookup.nodeId)) {
			return true;
		}
		for (NodeToTrigger n2t : possibleTriggerNodes) {
			if (n2t.triggerName.compareTo(lookup.triggerName) == 0) {
				if (n2t.active)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public void loadPossibleTriggerNodes() {
		String triggerpath = new Path(getEditorInput().getDriverPath()).append("triggernodes.com").toOSString();
		if (getEditorInput().getFileSystem().isFile(triggerpath)) {
			BufferedReader reader = null;
			try {
				InputStream input = getEditorInput().getFileSystem().readFile(triggerpath);
				reader = new BufferedReader(new InputStreamReader(input));
				String line = "";
				String[] items = null;
				getPossibleTriggerNodes().clear();
				NodeToTrigger dummy = new NodeToTrigger();
				dummy.displayname = "";
				dummy.active = false;
				dummy.nodeId = null;
				getPossibleTriggerNodes().add(dummy);
				while ((line = reader.readLine()) != null) {
					try {
						items = line.split("\t");
						if (items != null && items.length >= 3) {
							// we also need to get the namespace index from
							// server
							NamespaceTable uris = this.opcServer.getServerInstance().getNamespaceUris();
							String[] nitems = items[0].split(";");
							if (nitems != null && nitems.length == 2) {
								// now create node to tigger
								int nsindex = uris.getIndex(nitems[0].replace("ns=", ""));
								if (nsindex != -1) {
									NodeToTrigger n = new NodeToTrigger();
									n.nodeId = NodeId.parseNodeId("ns=" + nsindex + ";" + nitems[1]);
									Node node = this.opcServer.getServerInstance().getAddressSpaceManager()
											.getNodeById(n.nodeId);
									n.displayname = node.getDisplayName().getText();
									if (items.length > 3)
										n.triggerName = items[3];
									getPossibleTriggerNodes().add(n);
								}
							}
						}
					} catch (Exception ex) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
					}
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}
	}

	public List<NodeToTrigger> getPossibleTriggerNodes() {
		return possibleTriggerNodes;
	}

	public void setPossibleTriggerNodes(List<NodeToTrigger> possibleTriggerNodes) {
		this.possibleTriggerNodes = possibleTriggerNodes;
	}

	@Override
	public String getPartName() {
		return getEditorInput().getDriverName();
	}

	@Override
	public OPCUAAdvancedDriverEditorInput getEditorInput() {
		return (OPCUAAdvancedDriverEditorInput) super.getEditorInput();
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.loadPossibleTriggerNodes();
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setMinWidth(700);
		scrolledComposite.setMinHeight(750);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		scrolledComposite.setMinSize(new Point(300, 600));
		this.advancedDrvConfig = new AdvancedDriverConfigWidget(this.composite, SWT.NONE, this, this.opcServer,
				getEditorInput().getDriverType(), new AdvancedDriverConfigWidget().new ComputeSectionsHandler(this));
		scrolledComposite.setContent(composite);
		ExpansionAdapter adapter = new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				computeSection();
			}
		};
		this.advancedDrvConfig.setExpansionListener(adapter);
		getSite().setSelectionProvider(this.advancedDrvConfig);
		computeSection();
	}

	@Override
	public void setFocus() {
		// Set the focus
		super.setFocus();
		this.scrolledComposite.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IFileSystem filesystem = getEditorInput().getFileSystem();
		this.advancedDrvConfig.doSave(filesystem, new Path(filesystem.getRootPath()).append("drivers")
				.append(getEditorInput().getDriverName()).toOSString());
		setDirty(false);
		// now validate trigger nodes
		OPCUAUtil.validateOPCUADriver(getEditorInput().getFileSystem(), getEditorInput().getNode());
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"editor.advanced.monitor.openconfig");
					monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"editor.advanced.monitor.openconfig") + "...", IProgressMonitor.UNKNOWN);
					monitor.subTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"editor.advanced.monitor.init") + "...");
					try {
						String serverpath = getEditorInput().getServerRuntimePath();
						if (!serverpath.endsWith("/")) {
							serverpath += "/";
						}
						try {
							URL modelParent = new Path(serverpath + "informationmodel").toFile().toURI().toURL();
							if (getEditorInput().getFileSystem()
									.isFile(serverpath + "serverconfig/server.config.xml")) {
								opcServer = Studio_ResourceManager.getOrNewOPCUAServer(getEditorInput().getServerName(),
										serverpath + "serverconfig/server.config.xml", serverpath,
										serverpath + "/localization", modelParent);
							}
						} catch (IOException | ExecutionException e1) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
						}
					} finally {
						monitor.done();
					}
				}
			});
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
		}
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void computeSection() {
		Point minSize = this.advancedDrvConfig.computeSize();
		this.scrolledComposite.setMinSize(minSize);
		this.composite.layout(true);
	}

	@Override
	public void onNamespaceChange(NamespaceTableChangeParameter trigger) {
	}

	@Override
	public void refreshDatapoints() {
		// startconfig
		this.advancedDrvConfig.refresh();
	}

	@Override
	public void onFocusRemoteView() {
		ICommandService commandService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ICommandService.class);
		IHandlerService handlerService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IHandlerService.class);

		String drvCmd = "com.bichler.astudio.editor.";
		String drvVersion = getEditorInput().getNode().getDriverVersion();
		String drvType = getEditorInput().getDriverType();
		String drvCmdEnd = "opendrivermodel";

		Command openDriverModelCmd = commandService.getCommand(drvCmd + drvType + "." + drvVersion + "." + drvCmdEnd);
		IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
		String drivername = getEditorInput().getDriverName();
		String driverPath = new Path(getEditorInput().getFileSystem().getRootPath())
				.append(store.getString(OPCUAConstants.ASOPCUADriversFolder)).append(getEditorInput().getDriverName())
				.toOSString();
		String driverConfigPath = new Path(driverPath).append("driver.com").toOSString();
		OpenDriverModelBrowserParameter openDrvModelEvent = new OpenDriverModelBrowserParameter();
		openDrvModelEvent.setFilesystem(getEditorInput().getFileSystem());
		openDrvModelEvent.setDriverName(drivername);
		openDrvModelEvent.setDriverPath(driverPath);
		openDrvModelEvent.setDriverConfigPath(driverConfigPath);
		ExecutionEvent executionOpenDriverModelEvent = handlerService.createExecutionEvent(openDriverModelCmd, null);
		IEvaluationContext evalCtx = (IEvaluationContext) executionOpenDriverModelEvent.getApplicationContext();
		evalCtx.getParent().addVariable(AbstractOPCOpenDriverModelHandler.PARAMETER_ID, openDrvModelEvent);
		try {
			openDriverModelCmd.executeWithChecks(executionOpenDriverModelEvent);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}
}
