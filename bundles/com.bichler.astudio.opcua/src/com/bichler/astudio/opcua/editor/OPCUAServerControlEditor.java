package com.bichler.astudio.opcua.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.editor.input.OPCUAServerControlEditorInput;
import com.bichler.astudio.opcua.editor.input.OPCUAServerControlStartItem;
import com.bichler.astudio.opcua.editor.input.OPCUAServerControlStopItem;
import com.bichler.astudio.opcua.editor.providers.OPCUAServerControlLabelProvider;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.utils.constants.StudioConstants;
import com.bichler.astudio.opcua.IOPCPerspectiveEditor;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.constants.OPCUAConstants;

public class OPCUAServerControlEditor extends EditorPart implements ISaveablePart2, IOPCPerspectiveEditor {
	// private static class ContentProvider implements
	// IStructuredContentProvider {
	// public Object[] getElements(Object inputElement) {
	// return new Object[0];
	// }
	//
	// public void dispose() {
	// }
	//
	// public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	// {
	// }
	// }
	@Override
	public String getPartName() {
		// TODO Auto-generated method stub
		return getEditorInput().getNode().getServerName();
	}

	@Override
	public OPCUAServerControlEditorInput getEditorInput() {
		// TODO Auto-generated method stub
		return (OPCUAServerControlEditorInput) super.getEditorInput();
	}

	public static final String ID = "com.bichler.astudio.editors.opcuaservercontroleditor"; //$NON-NLS-1$
	private int linecounter = 0;
	private Table table;
	private TableViewer tableViewer;
	private boolean dirty = false;
	private CLabel btn_Start;
	// private Label lblNewLabel_1;
	private CLabel btn_Stop;
	private Label lbl_State;
	private ScrolledComposite scrolledComposite;
	private Composite composite;
	private BufferedReader stdreader = null;

	public OPCUAServerControlEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		btn_Start = new CLabel(composite, SWT.NONE);
		GridData gd_btn_Start = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btn_Start.widthHint = 170;
		gd_btn_Start.heightHint = 65;
		btn_Start.setLayoutData(gd_btn_Start);
		btn_Start.setText("");
		lbl_State = new Label(composite, SWT.NONE);
		GridData gd_lbl_State = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_lbl_State.widthHint = 170;
		gd_lbl_State.heightHint = 65;
		lbl_State.setLayoutData(gd_lbl_State);
		lbl_State.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_STOPPED));
		btn_Stop = new CLabel(composite, SWT.NONE);
		GridData gd_btn_Stop = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_btn_Stop.heightHint = 65;
		gd_btn_Stop.widthHint = 170;
		btn_Stop.setLayoutData(gd_btn_Stop);
		btn_Stop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				btn_Stop.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_STOP_SELECTED));
			}

			@Override
			public void mouseUp(MouseEvent e) {
				OPCUAServerModelNode node = getEditorInput().getNode();
				tableViewer.add("");
				OPCUAServerControlStopItem item = new OPCUAServerControlStopItem();
				item.setItem("*************************** " + node.getServerName()
						+ " stopped via Automation Studio *******************************");
				tableViewer.add(item);
				tableViewer.add("");
				btn_Start.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_START));
				lbl_State.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_STOPPED));
				btn_Stop.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_STOP_DISABLED));
				btn_Start.setEnabled(true);
				btn_Stop.setEnabled(false);
				getEditorInput().getFilesystem().stopProcess();
				setDirty(false);
				stdreader = null;
			}
		});
		btn_Stop.setEnabled(false);
		btn_Start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
				OPCUAServerModelNode node = getEditorInput().getNode();
				IFileSystem filesystem = getEditorInput().getNode().getFilesystem();
				String workingDirPath = getEditorInput().getFilesystem().getRootPath()
						+ filesystem.getTargetFileSeparator() + store.getString(OPCUAConstants.ASOPCUAServersPath)
						+ filesystem.getTargetFileSeparator() + node.getServerName()
						+ filesystem.getTargetFileSeparator();
				String serverJarPath = getEditorInput().getFilesystem().getRootPath()
						+ filesystem.getTargetFileSeparator() + store.getString(OPCUAConstants.ASOPCUARuntimePath)
						+ filesystem.getTargetFileSeparator() + Studio_ResourceManager.OPCUASERVERJARNAME;
				if (!getEditorInput().getFilesystem().isDir(workingDirPath)) {
					OPCUAServerControlStopItem item = new OPCUAServerControlStopItem();
					item.setItem("************ " + node.getServerName()
							+ " can not be started working directory not exists ****************");
					tableViewer.add(item);
					return;
				}
				if (!getEditorInput().getFilesystem().isFile(serverJarPath)) {
					OPCUAServerControlStopItem item = new OPCUAServerControlStopItem();
					item.setItem("************ " + node.getServerName()
							+ " can not be started server runtime not exists ****************");
					tableViewer.add(item);
					return;
				}
				/** now starting the server should be possible */
				tableViewer.add("");
				OPCUAServerControlStartItem item = new OPCUAServerControlStartItem();
				item.setItem("*************************** " + node.getServerName()
						+ " started via Automation Studio *******************************");
				tableViewer.add(item);
				tableViewer.add("");
				getEditorInput().getFilesystem().startProcess(workingDirPath,
						store.getString(StudioConstants.JavaCommand), store.getString(StudioConstants.JavaJarCommand),
						serverJarPath);
				stdreader = new BufferedReader(
						new InputStreamReader(getEditorInput().getFilesystem().getActualInputStream()));
				btn_Start.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_START_DISABLED));
				lbl_State.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_RUNNING));
				btn_Stop.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_STOP));
				btn_Start.setEnabled(false);
				btn_Stop.setEnabled(true);
				setDirty(true);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				btn_Start.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_START_SELECTED));
				// btn_Start.setImage(ResourceManager.getPluginImage(
				// "com.hbsoft.comet_studio_v0.0.2",
				// "icons/buttons/start_selected.png"));
			}
		});
		tableViewer.setLabelProvider(new OPCUAServerControlLabelProvider());
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		btn_Start.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_START));
		btn_Stop.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_STOP_DISABLED));
		if (getEditorInput().getFilesystem().connectToProcess("OPC_Server")) {
			OPCUAServerModelNode node = getEditorInput().getNode();
			OPCUAServerControlStartItem item = new OPCUAServerControlStartItem();
			item.setItem("*************************** " + node.getServerName()
					+ " connected to already running server *******************************");
			tableViewer.add(item);
			tableViewer.add("");
			btn_Start.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_START_DISABLED));
			lbl_State.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_RUNNING));
			btn_Stop.setImage(StudioImageActivator.getImage(StudioImages.ICON_BUTTON_SERVER_STOP));
			btn_Start.setEnabled(false);
			btn_Stop.setEnabled(true);
		}
		Thread stdthread = new Thread() {
			public void run() {
				Display display = getSite().getShell().getDisplay();
				if (display == null) {
					throw new SWTException(SWT.ERROR_THREAD_INVALID_ACCESS);
				}
				String line = "";
				try {
					while (true) {
						if (stdreader != null) {
							try {
								while (stdreader != null && (line = stdreader.readLine()) != null) {
									final String li = line;
									if (!display.isDisposed()) {
										display.asyncExec(new Runnable() {
											public void run() {
												linecounter++;
												if (linecounter > 1000) {
													if (!tableViewer.getControl().isDisposed()) {
														tableViewer.refresh();
													}
													linecounter = 1;
												}
												if (!tableViewer.getControl().isDisposed()) {
													tableViewer.add(li);
												}
											}
										});
									}
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							sleep(100);
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		stdthread.start();
	}

	@Override
	public void setFocus() {
		// Set the focus
		this.scrolledComposite.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
		monitor.setCanceled(true);
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
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
	public int promptToSaveOnClose() {
		MessageDialog dialog = new MessageDialog(getEditorSite().getShell(), "Stop OPC UA Server", null,
				"To close the editor, you must stop the server previously", MessageDialog.INFORMATION,
				new String[] { IDialogConstants.OK_LABEL }, 0);
		dialog.open();
		// TODO Auto-generated method stub
		return ISaveablePart2.CANCEL;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
}
