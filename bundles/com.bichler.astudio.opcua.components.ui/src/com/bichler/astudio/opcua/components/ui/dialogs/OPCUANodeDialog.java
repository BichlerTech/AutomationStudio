package com.bichler.astudio.opcua.components.ui.dialogs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import opc.sdk.server.core.OPCInternalServer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;import org.eclipse.swt.internal.ole.win32.IDataObject;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUABrowseContentProvider;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUABrowserLabelProvider;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUABrowserProvider;

public class OPCUANodeDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Form dialogForm;
	private TreeViewer tableViewer_1;

	private OPCInternalServer internalServer;
	private String formTitle = "";
	private NodeId selectedNodeId = null;
	private String displayName = "";
	private Deque<BrowsePathElement> browsePath = new ArrayDeque<BrowsePathElement>();

	private NodeId startId = Identifiers.ObjectsFolder;	
	
	private Set<NodeClass> nodeClassFilter = new HashSet<>();
	
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public OPCUANodeDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		this.dialogForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(dialogForm);
		dialogForm.setText(formTitle);
		formToolkit.decorateFormHeading(dialogForm);

		dialogForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

		tableViewer_1 = new TreeViewer(dialogForm.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer_1.setSorter(new ViewerSorter());
		Tree table_1 = tableViewer_1.getTree();
		formToolkit.paintBordersFor(table_1);

		// UAServerApplicationInstance server = new
		// UAServerApplicationInstance();
		// server.getServerInstance();
		// InternalServer server = internalServer;

		OPCUABrowseContentProvider content = new OPCUABrowseContentProvider();
		content.setServer(internalServer);
		tableViewer_1.setContentProvider(content);

		OPCUABrowserLabelProvider label = new OPCUABrowserLabelProvider(tableViewer_1);
		label.setServer(internalServer);
		tableViewer_1.setLabelProvider(label);

		OPCUABrowserProvider input = new OPCUABrowserProvider(internalServer,  this.startId);
		tableViewer_1.setInput(input.getBrowser());

		tableViewer_1.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// set the selected node to return
				Object element = ((StructuredSelection) event.getSelection()).getFirstElement();

				if (!(element instanceof OPCTreeViewerItem)) {
					return;
				}

				OPCTreeViewerItem item = (OPCTreeViewerItem) element;

				selectedNodeId = item.getNodeId();

				// get the full browsepath from server objects to the
				// selected element
				browsePath = OPCUABrowseUtils.getFullBrowsePath(selectedNodeId, internalServer, startId);
				displayName = item.getDisplayName();

				selectionChange(item);
			}
		});

		fillTree();

		return container;
	}

	protected void selectionChange(OPCTreeViewerItem item) {
		Button btnOK = getButton(IDialogConstants.OK_ID);
		if(btnOK == null) {
			return ;
		}
		
		if(item == null) {
			btnOK.setEnabled(false);
			return ;
		}
		
		if(this.nodeClassFilter.isEmpty()) {
			btnOK.setEnabled(true);
			return ;
		}
		
		NodeClass selectedNodeClass = item.getNodeClass();		
		if(this.nodeClassFilter.contains(selectedNodeClass)) {
			btnOK.setEnabled(true);
		}
		else {
			btnOK.setEnabled(false);
		}
	}

	private void fillTree() {
		browsePath = OPCUABrowseUtils.getFullBrowsePath(selectedNodeId, internalServer, startId);

		// open tree
		TreeItem[] items = tableViewer_1.getTree().getItems();

		if (items != null) {
			for (BrowsePathElement element : browsePath) {
				if (element.getId().equals(startId))
					continue;

				for (TreeItem item : items) {
					if (item.getData() instanceof OPCTreeViewerItem) {
						OPCTreeViewerItem it = (OPCTreeViewerItem) item.getData();
						if (it.getNodeId().equals(element.getId())) {
							item.setExpanded(true);
							tableViewer_1.refresh();
							items = item.getItems();
							StructuredSelection selection = new StructuredSelection(it);
							tableViewer_1.setSelection(selection);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public NodeId getSelectedNodeId() {
		return selectedNodeId;
	}

	public void setSelectedNodeId(NodeId selectedNodeId) {
		this.selectedNodeId = selectedNodeId;
	}

	public String getSelectedDisplayName() {
		return displayName;
	}

	public void setSelectedDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Deque<BrowsePathElement> getBrowsePath() {
		return browsePath;
	}

	public void setBrowsePath(Deque<BrowsePathElement> browsePath) {
		this.browsePath = browsePath;
	}

	public String getFormTitle() {
		return formTitle;
	}
	
	public OPCInternalServer getInternalServer() {
		return internalServer;
	}

	public void setInternalServer(OPCInternalServer internalServer) {
		this.internalServer = internalServer;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public void setStartId(NodeId startId) {
		this.startId = startId;
	}

	public void setNodeClassFilter(NodeClass...nodeClasses) {
		if(nodeClasses == null) {
			this.nodeClassFilter = new HashSet<>();
			return;
		}
		
		for(NodeClass nodeClass : nodeClasses) {
			this.nodeClassFilter.add(nodeClass);
		}
	}
	
	protected TreeViewer getTreeViewer() {
		return this.tableViewer_1;
	}

	protected Composite getFormBody() {
		return this.dialogForm.getBody();
	}
	
	public static String toBrowsePath(Deque<BrowsePathElement> browsePathElements) {
		 String browsepath = "";
         for (BrowsePathElement element : browsePathElements)
         {
           if (element.getId().equals(Identifiers.ObjectsFolder))
           {
             continue;
           }
           browsepath += "//" + element.getBrowsename().getName();
         }
         return browsepath;
	}
}
