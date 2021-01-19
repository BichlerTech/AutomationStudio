package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.ParametrizedExportModelHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.opc.ExportNamespaceModelHandler;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorDataTypePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorInput;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorMethodPart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorObjectPart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorObjectTypePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorReferenceTypePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorVariablePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorVariableTypePart;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;

public class ModelBrowserView extends ViewPart implements ISaveablePart {
	public static final String ID = "com.hbsoft.designer.views.modeldesignbrowser.modelbrowserview";
	private boolean isContextMenuRegisterd = false;
	private boolean isDirty = false;
	private TreeViewer treeViewer = null;
	private boolean isOpenAllowed = true;

	@Override
	public void createPartControl(Composite parent) {
		createView(parent);
		setHandler();
	}

	public void startView() {
		BrowserModelNode node = new BrowserModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.RootFolder));
		this.treeViewer.setInput(node);
		this.treeViewer.expandToLevel(2);
		registerTreeViewerContextMenu();
	}

	public void closeView() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				setDirty(false);
				treeViewer.setInput(null);
			}
		});
	}

	@Override
	public void setFocus() {
		this.treeViewer.getTree().setFocus();
	}

	public void refresh() {
		BrowserModelNode node = (BrowserModelNode) treeViewer.getInput();
		if (node.getNode() == null) {
			node.setNode(ServerInstance.getNode(Identifiers.RootFolder));
			this.treeViewer.setInput(node);
		}
		refresh(node);
	}

	public void refresh(NodeId nodeId) {
		BrowserModelNode node = getBrowserNode(nodeId);
		if (node == null) {
			return;
		}
		refresh(node);
	}

	public void refresh(BrowserModelNode node) {
		this.treeViewer.refresh(node, true);
		this.treeViewer.update(node, null);
	}

	public void update(BrowserModelNode node) {
		this.treeViewer.update(node, null);
	}

	public TreeViewer getTreeViewer() {
		return this.treeViewer;
	}

	/**
	 * Programmatically saving from comet studio
	 */
	public void doSave() {
		String path = Studio_ResourceManager.getInfoModellerResource();
		boolean isSuccessFull = DesignerUtils.doSaveOPCInformationModel(null, path);
		setDirty(!isSuccessFull);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (monitor != null) {
			monitor.beginTask(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.save") + "...",
					IProgressMonitor.UNKNOWN);
		}
		IWorkbenchWindow window = getSite().getPage().getWorkbenchWindow();
		String path = Studio_ResourceManager.getInfoModellerResource();
		if (path == null) {
			FileDialog dialog = new FileDialog(getSite().getShell(), SWT.OPEN);
			path = dialog.open();
		}
		// no model.xml
		final IHandlerService service = window.getService(IHandlerService.class);
		ICommandService cmdService = window.getService(ICommandService.class);
		Command command = cmdService.getCommand(ParametrizedExportModelHandler.ID);
		IParameter parameter;
		try {
			parameter = command.getParameter(ParametrizedExportModelHandler.PARAMETER_FILE);
			Parameterization[] parameterizations = new Parameterization[1];
			parameterizations[0] = new Parameterization(parameter, path);
			final ParameterizedCommand cmd = new ParameterizedCommand(command, parameterizations);
			Object result = service.executeCommand(cmd, null);
			if (result instanceof Boolean) {
				setDirty((Boolean) result);
			}
		} catch (NotDefinedException | NotEnabledException | NotHandledException | ExecutionException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		if (monitor != null) {
			monitor.done();
		}
	}

	@Override
	public void doSaveAs() {
		IWorkbenchWindow window = getSite().getWorkbenchWindow();
		final IHandlerService service = window.getService(IHandlerService.class);
		try {
			service.executeCommand(ExportNamespaceModelHandler.ID, null);
			setDirty(false);
		} catch (NotDefinedException | NotEnabledException | NotHandledException | ExecutionException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public void selectNode(NodeId selectedId) {
		BrowserModelNode root = (BrowserModelNode) treeViewer.getInput();
		Deque<BrowsePathElement> paths = OPCUABrowseUtils.getFullBrowsePath(selectedId,
				ServerInstance.getInstance().getServerInstance(), Identifiers.RootFolder);
		for (int i = 0; i < paths.size(); i++) {
			BrowsePathElement element = paths.poll();
			String displayname = element.getDisplayname().getText();
			if (i == 0 && "Root".equals(displayname)) {
				continue;
			}
			TreePath[] expElements = this.treeViewer.getExpandedTreePaths();
			TreePath look = null;
			for (TreePath path : expElements) {
				BrowserModelNode lastSegment = (BrowserModelNode) path.getLastSegment();
				String text = lastSegment.getNode().getDisplayName().getText();
				if (displayname.equals(text)) {
					look = path;
					break;
				}
			}
			this.treeViewer.expandToLevel(look, i + 1);
		}
		BrowserModelNode selection = findNode(root, selectedId);
		if (selection == null) {
			return;
		}
		this.treeViewer.setSelection(new StructuredSelection(selection));
	}

	BrowserModelNode getBrowserNode(NodeId nodeId) {
		BrowserModelNode root = (BrowserModelNode) treeViewer.getInput();
		Deque<BrowsePathElement> paths = OPCUABrowseUtils.getFullBrowsePath(nodeId,
				ServerInstance.getInstance().getServerInstance(), Identifiers.RootFolder);
		for (int i = 0; i < paths.size(); i++) {
			BrowsePathElement element = paths.poll();
			String displayname = element.getDisplayname().getText();
			if (i == 0 && "Root".equals(displayname)) {
				continue;
			}
			TreePath[] expElements = this.treeViewer.getExpandedTreePaths();
			TreePath look = null;
			for (TreePath path : expElements) {
				BrowserModelNode lastSegment = (BrowserModelNode) path.getLastSegment();
				String text = lastSegment.getNode().getDisplayName().getText();
				if (displayname.equals(text)) {
					look = path;
					break;
				}
			}
			this.treeViewer.expandToLevel(look, i + 1);
		}
		return findNode(root, nodeId);
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return this.isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return this.isDirty;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return true;
	}

	private BrowserModelNode findNode(BrowserModelNode node, NodeId selectedId) {
		NodeId nodeId = node.getNode().getNodeId();
		if (selectedId.equals(nodeId)) {
			return node;
		}
		// get element path from opc ua server
		BrowserModelNode[] children = node.getChildren();
		BrowserModelNode selection = null;
		for (BrowserModelNode child : children) {
			selection = findNode(child, selectedId);
			if (selection != null) {
				break;
			}
		}
		return selection;
	}

	private void setHandler() {
		this.treeViewer.getTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				openSelectedNodeEditor();

			}

		});
	}

	private void openSelectedNodeEditor() {
		BrowserModelNode selectedNode = (BrowserModelNode) ((IStructuredSelection) treeViewer.getSelection())
				.getFirstElement();
		if (isOpenAllowed && selectedNode != null) {
			openNodeEditor(selectedNode);
		}
	}
	
	public static void openNodeEditor(BrowserModelNode node) {
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			String editorId = "";
			switch (node.getNode().getNodeClass()) {
			case Object:
				editorId = NodeEditorObjectPart.ID;
				break;
			case ObjectType:
				editorId = NodeEditorObjectTypePart.ID;
				break;
			case DataType:
				editorId = NodeEditorDataTypePart.ID;
				break;
			case Method:
				editorId = NodeEditorMethodPart.ID;
				break;
			case ReferenceType:
				editorId = NodeEditorReferenceTypePart.ID;
				break;
			case VariableType:
				editorId = NodeEditorVariableTypePart.ID;
				break;
			case Variable:
				editorId = NodeEditorVariablePart.ID;
				break;
			default:
				return;
			}
			/**
			 * Prefents opening multipe editors of the same node
			 */
			IEditorReference[] erefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
			boolean isOpen = true;
			NodeEditorInput newEI = null;
			for (IEditorReference eref : erefs) {
				// skip other editors than nodeeditparts
				if (!(eref instanceof NodeEditorInput)) {
					continue;
				}
				NodeEditorInput nei = (NodeEditorInput) eref.getEditorInput();
				BrowserModelNode bNode = nei.getNode();
				Node x = bNode.getNode();
				Node y = node.getNode();
				if (x == y) {
					isOpen = false;
					newEI = nei;
					break;
				}
			}
			if (isOpen) {
				page.openEditor(new NodeEditorInput(node, false), editorId);
			} else {
				page.openEditor(newEI, editorId);
			}
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ModelBrowserView.ID);
		} catch (PartInitException e) {
			Logger.getLogger(ModelBrowserView.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void createView(Composite parent) {
		// create tree viewer
		this.treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		// set content provider
		this.treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.ALL)));
		// set label provider
		this.treeViewer.setLabelProvider(new ModelLabelProvider());
		// set selection provider
		getSite().setSelectionProvider(this.treeViewer);
		// Drag & Drop
		int operation = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		this.treeViewer.addDragSupport(operation, transferTypes, new OPCUAModelDragListener(this.treeViewer));
		// comparator
		this.treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
	}

	private void registerTreeViewerContextMenu() {
		if (!isContextMenuRegisterd) {
			MenuManager menuMgr = new MenuManager(
					"popup:com.hbsoft.designer.views.modeldesignbrowser.modelbrowserview");
			menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			Menu menu = menuMgr.createContextMenu(this.treeViewer.getControl());

			this.treeViewer.getControl().setMenu(menu);
			getSite().registerContextMenu(menuMgr, this.treeViewer);
		}
	}

	class OPCUAModelDragListener extends DragSourceAdapter {
		private TreeViewer viewer;

		public OPCUAModelDragListener(TreeViewer viewer) {
			this.viewer = viewer;
		}

		@Override
		public void dragStart(DragSourceEvent event) {
			// Here you do the convertion to the type which is expected.
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			BrowserModelNode firstElement = (BrowserModelNode) selection.getFirstElement();
			event.data = firstElement.getNode().getNodeId().toString();
		}

		@Override
		public void dragSetData(DragSourceEvent event) {
			// Here you do the convertion to the type which is expected.
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			BrowserModelNode firstElement = (BrowserModelNode) selection.getFirstElement();
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = firstElement.getNode().getNodeId().toString();
			}
		}
	}
}
