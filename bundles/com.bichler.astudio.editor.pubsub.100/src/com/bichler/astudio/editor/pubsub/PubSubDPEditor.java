package com.bichler.astudio.editor.pubsub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.bichler.astudio.editor.pubsub.dnd.PubSubDPDnDViewAdapter;
import com.bichler.astudio.editor.pubsub.nodes.PubSubConnection;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetField;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetWriter;
import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.nodes.PubSubModel;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSet;
import com.bichler.astudio.editor.pubsub.nodes.PubSubReaderGroup;
import com.bichler.astudio.editor.pubsub.nodes.PubSubWriterGroup;
import com.bichler.astudio.editor.pubsub.wizard.AbstractPubSubWizard;
import com.bichler.astudio.editor.pubsub.wizard.PubSubConnectionWizard;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetFieldWizard;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetWriterWizard;
import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;
import com.bichler.astudio.editor.pubsub.wizard.PubSubReaderGroupWizard;
import com.bichler.astudio.editor.pubsub.wizard.PubSubWriterGroupWizard;
import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;
import com.bichler.astudio.images.opcua.OPCImagesActivator;
import com.bichler.astudio.opcua.AbstractOPCTriggerNodeEditPart;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.editor.input.OPCUAModuleDPEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.nodes.OPCUAServerModuleDPsModelNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import opc.sdk.server.core.UAServerApplicationInstance;

public class PubSubDPEditor extends AbstractOPCTriggerNodeEditPart {
	public static final String ID = "com.bichler.astudio.editor.pubsub.1.0.0.PubSubDPEditor";
	private ISelectionChangedListener listener;
	private Object currentSelection;
	private boolean dirty;
	private TreeViewer treeViewer;

	/*
	 * public TreeViewer getTreeViewer() { return treeViewer; }
	 * 
	 * public void setTreeViewer(TreeViewer treeViewer) { this.treeViewer =
	 * treeViewer; }
	 */

	private UAServerApplicationInstance opcServer;

	public PubSubDPEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// GsonBuilder builder = new GsonBuilder();
		// builder.registerTypeHierarchyAdapter(NetworkAddressUrlDataType.class, new
		// NetworkURLAdapter());

		Gson GSON = new GsonBuilder().setPrettyPrinting().create();

		// Gson GSON = new
		// GsonBuilder().registerTypeAdapterFactory(PubSubOPCUATypeAdapter.FACTORY).setPrettyPrinting().create();
		String configPath = getEditorInput().getDPConfigFile();

		File extensionConfig = new File(configPath);
		try (FileWriter writer = new FileWriter(extensionConfig, false)) {
			treeViewer.getInput();
			GSON.toJson(treeViewer.getInput(), writer);
		} catch (IOException | JsonIOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Failed to store config to path '" + configPath + "'", e);
		}
		setDirty(false);
		OPCUAUtil.validateOPCUAModule(getEditorInput().getFilesystem(), getEditorInput().getNode());
	}

	@Override
	public void doSaveAs() {
		// we only save the editor input with the default name
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					monitor.beginTask("Laden der PubSub Configuration ...", IProgressMonitor.UNKNOWN);
					monitor.subTask("Initialisiere ...");

					setSite(site);
					setInput(input);
					setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName()
							+ " - Datenpunkte");
					String configPath = getEditorInput().getDPConfigFile();
					File configFile = new File(configPath);
					if (!configFile.exists() || !configFile.isFile()) {
						return;
					}
					try (FileReader reader = new FileReader(configFile);) {

						String serverpath = new Path(getEditorInput().getFilesystem().getRootPath()).toOSString();
						try {
							URL modelParent = new Path(serverpath).append("informationmodel").toFile().toURI().toURL();
							String serverConfigPath = new Path(serverpath).append("serverconfig")
									.append("server.config.xml").toOSString();
							if (getEditorInput().getFilesystem().isFile(serverConfigPath)) {
								opcServer = Studio_ResourceManager.getOrNewOPCUAServer(getEditorInput().getServerName(),
										serverConfigPath, serverpath, serverpath + "/localization", modelParent);
							}
						} catch (IOException | ExecutionException e1) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
						}
						getEditorInput().setToolTipText("Events - Connect Events with Model Nodes");
					} catch (IOException iox) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, iox.getMessage(), iox);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	protected void createPages() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		this.createPage1(composite);
		int index = addPage(composite);
		setPageText(index, "edit");
	}

	public void createPage1(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		createTreeViewer(parent);
		createMenu(parent);
		fillTree();
		this.treeViewer.expandAll();
		// registerContextMenu();
		setHandler();
		getSite().setSelectionProvider(this.treeViewer);
	}

	@Override
	public void setFocus() {
		this.treeViewer.getTree().setFocus();
	}

	@Override
	public OPCUAModuleDPEditorInput getEditorInput() {
		return (OPCUAModuleDPEditorInput) super.getEditorInput();
	}

	@Override
	public void fillTriggerNodes(List<NodeToTrigger> nodesToTrigger) {
		// TODO Auto-generated method stub
	}

	@Override
	public Control getDPControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISelectionProvider getDPViewer() {
		// TODO Auto-generated method stub
		return null;
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
	public void refreshDatapoints() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFocusRemoteView() {
		DriverBrowserUtil.openEmptyDriverModelView();
	}

	@Override
	public void setDataPointSelection(Object element) {
		this.currentSelection = element;
		if (this.currentSelection != null) {
			selectionChanged();
		}
	}

	protected PubSubEntryModelNode getParentOf(PubSubEntryModelNode element) {
		PubSubModel input = (PubSubModel) this.treeViewer.getInput();

		switch (element.getType()) {
		case CONNECTION:
		case PUBLISHEDDATASET:
			break;
		case DATASETFIELD:
			for (PubSubPublishedDataSet dataSet : input.getPublishedDS()) {
				for (PubSubDataSetField child : dataSet.getChildren()) {
					if (child == element) {
						return dataSet;
					}
				}
			}
			break;
		case DATASETWRITER:
			for (PubSubConnection connection : input.getConnections()) {
				for (PubSubWriterGroup writerGroup : connection.getChildren()) {
					for (PubSubDataSetWriter child : writerGroup.getChildren()) {
						if (child == element) {
							return writerGroup;

						}
					}
				}
			}
			break;
		case WRITERGROUP:
			for (PubSubConnection connection : input.getConnections()) {
				for (PubSubWriterGroup child : connection.getChildren()) {
					if (child == element) {
						return connection;
					}
				}
			}
			break;
		default:
			break;
		}

		return null;
	}

	protected void selectionChanged() {
		if (this.listener == null) {
			return;
		}
		listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	private void createTreeViewer(Composite parent) {
		this.treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.NONE);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TreeViewerColumn tvc_value = createTreeColumn("Connection", 150);
		createTreeColumn("Name", 150);
		TreeViewerColumn tvc_message = createTreeColumn("Message", 150);

		TreeViewerColumn tvc_severity = createTreeColumn("Severity", 100);
		this.treeViewer.setContentProvider(new EventDPTreeContentProvider());
		this.treeViewer.setLabelProvider(new PubSubDPTreeLabelProvider());
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		DragSource dragSource = new DragSource(this.treeViewer.getTree(), operations);
		dragSource.setTransfer(transferTypes);
		dragSource.addDragListener(new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = treeViewer.getTree().getSelection();
				if (selection.length > 0 /*
											 * && selection[0].getItemCount() == 0
											 */) {
					event.doit = true;
				} else {
					event.doit = false;
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				PubSubEntryModelNode fe = (PubSubEntryModelNode) ((IStructuredSelection) treeViewer.getSelection())
						.getFirstElement();
				event.data = "%move%" + fe.toString(); // "test";//dragSourceItem[0].getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE) {
					// dragSourceItem[0].dispose();
					// dragSourceItem[0] = null;
				}
			}
		});
		this.treeViewer.addDropSupport(operations, transferTypes, new PubSubDPDnDViewAdapter(this.treeViewer, this));

		this.treeViewer.getTree().addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				if (treeViewer.getTree().getItem(new Point(e.x, e.y)) == null)
					treeViewer.setSelection(StructuredSelection.EMPTY);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		this.registerContextMenu();
	}

	private TreeViewerColumn createTreeColumn(String name, int width) {
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn tc_event = treeViewerColumn.getColumn();
		tc_event.setWidth(width);
		tc_event.setText(name);
		return treeViewerColumn;
	}

	private void createMenu(Composite parent) {
		Composite cmp_menu = new Composite(parent, SWT.NONE);
		cmp_menu.setLayout(new RowLayout(SWT.HORIZONTAL));
	}

	private void fillTree() {
		// List<PubSubEntryModelNode> nodes = new ArrayList<>();
		// this.treeViewer.setInput(nodes);
		// ----
		/** open file to import */
		String dpsPath = getEditorInput().getDPConfigFile();
		// List<Events_DPItem> dps = new ArrayList<>();
		if (getEditorInput().getFilesystem().isFile(dpsPath)) {
			// InputStreamReader isr = null;
			// InputStream input = null;
			String configPath = getEditorInput().getDPConfigFile();
			File configFile = new File(configPath);
			if (!configFile.exists() || !configFile.isFile()) {
				return;
			}
			try (FileReader reader = new FileReader(configFile);) {

				Gson GSON = new GsonBuilder().setPrettyPrinting().create();

				PubSubModel input = GSON.fromJson(reader, PubSubModel.class);
				treeViewer.setInput(input);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	private void openDataPointWizard(PubSubEntryModelNode node) {
		AbstractPubSubWizard wizard = null;
		PUBSUBENTRYTYPE type = node.getType();
		switch (type) {
		case CONNECTION:
			wizard = new PubSubConnectionWizard((PubSubConnection) node);
			break;
		case DATASETFIELD:
			wizard = new PubSubDataSetFieldWizard((PubSubDataSetField) node);
			break;
		case DATASETWRITER:
			wizard = new PubSubDataSetWriterWizard((PubSubDataSetWriter) node);
			break;
		case PUBLISHEDDATASET:
			wizard = new PubSubPublishedDataSetWizard((PubSubPublishedDataSet) node);
			break;
		case READERGROUP:
			wizard = new PubSubReaderGroupWizard((PubSubReaderGroup) node);
			break;
		case WRITERGROUP:
			wizard = new PubSubWriterGroupWizard((PubSubWriterGroup) node);
			break;
		}

		WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
		int open = dialog.open();
		if (WizardDialog.OK == open) {
			if (wizard.isDirty()) {
				setDirty(true);
				treeViewer.update(node, null);
			}
		}
	}

	private void setHandler() {
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object element = ((IStructuredSelection) event.getSelection()).getFirstElement();
				openDataPointWizard((PubSubEntryModelNode) element);
			}
		});

		this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object element = ((StructuredSelection) event.getSelection()).getFirstElement();
				setDataPointSelection(element);
			}
		});
	}

	private void registerContextMenu() {
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(this.treeViewer.getControl());
		this.treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.treeViewer);
	}

	public void addPubSubConnection() {
		if (this.treeViewer.getInput() == null) {
			PubSubModel model = new PubSubModel();
			this.treeViewer.setInput(model);
		}
		PubSubModel input = (PubSubModel) this.treeViewer.getInput();
		PubSubConnection newItem = new PubSubConnection();
		input.getConnections().add(newItem);
		this.treeViewer.refresh(input);
		this.treeViewer.setSelection(new StructuredSelection(newItem), false);
		setDirty(true);
	}

	public void addPubSubPublishedDataSet() {
		if (this.treeViewer.getInput() == null) {
			this.treeViewer.setInput(new ArrayList<PubSubEntryModelNode>());
		}

		PubSubModel input = (PubSubModel) this.treeViewer.getInput();

		PubSubPublishedDataSet newItem = new PubSubPublishedDataSet();
		input.getPublishedDS().add(newItem);
		this.treeViewer.refresh(input);
		this.treeViewer.setSelection(new StructuredSelection(newItem), false);
		setDirty(true);
	}

	public void addPubSubWriterGroup() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		Object element = selection.getFirstElement();
		if (element instanceof PubSubConnection) {
			((PubSubConnection) element).getChildren().add(new PubSubWriterGroup());
		}

		this.treeViewer.refresh(element);
		// this.treeViewer.setSelection(new StructuredSelection(eventNode), false);
		setDirty(true);
	}

	public void addPubSubDataSetWriter() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		Object element = selection.getFirstElement();

		if (element instanceof PubSubWriterGroup) {
			((PubSubWriterGroup) element).addChild(new PubSubDataSetWriter());
		}

		this.treeViewer.refresh(element);
		// this.treeViewer.setSelection(new StructuredSelection(eventNode), false);
		setDirty(true);
	}

	public void addPubSubDataSetField() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		Object element = selection.getFirstElement();

		if (element instanceof PubSubPublishedDataSet) {
			((PubSubPublishedDataSet) element).getChildren().add(new PubSubDataSetField());
		}

		this.treeViewer.refresh(element);
		// this.treeViewer.setSelection(new StructuredSelection(eventNode), false);
		setDirty(true);
	}

	public void removePubSubNode() {
		PubSubModel input = (PubSubModel) this.treeViewer.getInput();

		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		PubSubEntryModelNode element = (PubSubEntryModelNode) selection.getFirstElement();

		switch (element.getType()) {
		case CONNECTION:
			input.getConnections().remove(element);
			this.treeViewer.refresh(input);
			break;
		case PUBLISHEDDATASET:
			input.getPublishedDS().remove(element);
			this.treeViewer.refresh(input);
			break;
		case DATASETFIELD:
			PubSubPublishedDataSet dsParent = (PubSubPublishedDataSet) getParentOf(element);
			if (dsParent != null) {
				dsParent.getChildren().remove(element);
				this.treeViewer.refresh(dsParent);
			}
			break;
		case DATASETWRITER:
			PubSubWriterGroup wgParent = (PubSubWriterGroup) getParentOf(element);
			if (wgParent != null) {
				wgParent.getChildren().remove(element);
				this.treeViewer.refresh(wgParent);
			}
			break;
		case WRITERGROUP:
			PubSubConnection conParent = (PubSubConnection) getParentOf(element);
			if (conParent != null) {
				conParent.getChildren().remove(element);
				this.treeViewer.refresh(conParent);
			}
			break;
		default:
			break;
		}
		setDirty(true);
	}

	void removeEvent() {
		StructuredSelection sel = (StructuredSelection) this.treeViewer.getSelection();
		Object[] selections = sel.toArray();
		if (selections != null) {
			for (Object o : selections) {
				@SuppressWarnings("unchecked")
				List<PubSubEntryModelNode> input = (List<PubSubEntryModelNode>) this.treeViewer.getInput();
				remove(input, (PubSubEntryModelNode) o);
				// ((List<EventEntryModelNode>) this.treeViewer.getInput())
				// .remove(o);
			}
			// this.treeViewer.remove(selections);
			this.treeViewer.refresh();
			setDirty(true);
		}
	}

	private void remove(List<PubSubEntryModelNode> siblings, PubSubEntryModelNode obj2remove) {
		boolean removed = siblings.remove(obj2remove);
		if (removed) {
			return;
		}
		for (PubSubEntryModelNode node : siblings) {
			// remove(node.getChildrenList(), obj2remove);
		}
	}

	class EventDPTreeContentProvider implements ITreeContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof PubSubModel) {
				List<PubSubEntryModelNode> children = new ArrayList<PubSubEntryModelNode>();
				children.addAll(((PubSubModel) parentElement).getConnections());
				children.addAll(((PubSubModel) parentElement).getPublishedDS());
				return children.toArray();
			} else if (parentElement instanceof List<?>) {
				return ((List<?>) parentElement).toArray();
			} else if (parentElement instanceof PubSubConnection) {
				return ((PubSubConnection) parentElement).getChildren().toArray();
			} else if (parentElement instanceof PubSubWriterGroup) {
				return ((PubSubWriterGroup) parentElement).getChildren().toArray();
			} else if (parentElement instanceof PubSubDataSetWriter) {
				return new Object[0];
			} else if (parentElement instanceof PubSubPublishedDataSet) {
				return ((PubSubPublishedDataSet) parentElement).getChildren().toArray();
			} else if (parentElement instanceof PubSubDataSetField) {
				return new Object[0];
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	class PubSubDPTreeLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				// AbstractEventModel item = ((PubSubEntryModelNode) element).getItem();
				if (element instanceof PubSubConnection) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.OBJECT);
				} else if (element instanceof PubSubPublishedDataSet) {

					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.OBJECT);
				} else if (element instanceof PubSubDataSetField) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.EVENT);
				} else if (element instanceof PubSubDataSetWriter) {

					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.EVENT);
				} else if (element instanceof PubSubWriterGroup) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.BOOK);
				} else if (element instanceof PubSubReaderGroup) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.BOOK);

				}
			}
			return super.getImage(element);
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// AbstractEventModel item = ((PubSubEntryModelNode) element).getItem();
			if (element instanceof PubSubEntryModelNode) {
				switch (columnIndex) {
				case 0:
					return ((PubSubEntryModelNode) element).getType().name();
				case 1:
					switch (((PubSubEntryModelNode) element).getType()) {
					case CONNECTION:
						return ((PubSubConnection) element).getName();
					case DATASETFIELD:
						if (((PubSubDataSetField) element).getField() != null) {
							return ((PubSubDataSetField) element).getField().getFieldNameAlias();
						}
						return "<undefined>";
					case DATASETWRITER:
						return ((PubSubDataSetWriter) element).getName();
					case PUBLISHEDDATASET:
						return ((PubSubPublishedDataSet) element).getName();
					case READERGROUP:
						return ((PubSubReaderGroup) element).getName();
					case WRITERGROUP:
						return ((PubSubWriterGroup) element).getName();
					default:
						return "";
					}
				default:
					return null;
				}
			}
			return "";
		}
	}

}
