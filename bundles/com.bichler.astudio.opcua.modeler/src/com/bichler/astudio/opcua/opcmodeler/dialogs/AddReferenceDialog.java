package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.DeleteReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometSashForm;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelContentProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectTypeNode;

public class AddReferenceDialog extends Dialog {
	private TreeViewer referenceTypeTreeViewer = null;
	private TreeViewer targetidTreeViewer = null;
	private CheckBoxButton cb_isInverse = null;
	private Label lbl_errorMessage = null;
	private Object referenceTypeResult = null;
	private Object targetIdResult = null;
	private Node usedNode = null;
	private ReferenceNode referenceNodeToUpdate = null;
	private Form frmNewForm = null;
	private String formCaption = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"AddReferenceDialog.addref.caption");
	private NodeId sourceId;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public AddReferenceDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public AddReferenceDialog(Shell parentShell, NodeId sourceId, ReferenceNode referenceNodeToUpdate) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
		this.sourceId = sourceId;
		this.referenceNodeToUpdate = referenceNodeToUpdate;
		if (this.referenceNodeToUpdate != null) {
			this.formCaption = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"AddReferenceDialog.changeref.caption");
		}
	}

	public Node getUsedNode() {
		return usedNode;
	}

	public void setUsedNode(Node usedNode) {
		this.usedNode = usedNode;
	}

	public ReferenceNode getReferenceNodeToUpdate() {
		return referenceNodeToUpdate;
	}

	public void setReferenceNodeToUpdate(ReferenceNode referenceNodeToUpdate) {
		this.referenceNodeToUpdate = referenceNodeToUpdate;
	}

	/**
	 * set the caption of the main form
	 * 
	 * @param caption
	 */
	public void setFormCaption(String caption) {
		this.frmNewForm.setText(caption);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		/** form toolkit to create flat controls */
		FormToolkit formToolkit = new FormToolkit(Display.getDefault());
		frmNewForm = formToolkit.createForm(container);
		frmNewForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText(formCaption);
		formToolkit.decorateFormHeading(frmNewForm);
		frmNewForm.getBody().setLayout(new GridLayout(1, false));
		CometSashForm sashForm_1 = new CometSashForm(frmNewForm.getBody(), SWT.NONE);
		sashForm_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm_1.setTouchEnabled(true);
		sashForm_1.layout();
		sashForm_1.setSashWidth(4);
		sashForm_1.setFormToolkit(formToolkit);
		sashForm_1.setOrientation(SWT.VERTICAL);
		formToolkit.adapt(sashForm_1);
		formToolkit.paintBordersFor(sashForm_1);
		Composite composite_2 = new Composite(sashForm_1, SWT.NONE);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		GridLayout gl_composite_2 = new GridLayout(2, false);
		composite_2.setLayout(gl_composite_2);
		Label lbl_refType = new Label(composite_2, SWT.NONE);
		GridData gd_lbl_refType = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lbl_refType.widthHint = 170;
		lbl_refType.setLayoutData(gd_lbl_refType);
		formToolkit.adapt(lbl_refType, true, true);
		lbl_refType.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "AddReferenceDialog.lbl_refType.text"));
		referenceTypeTreeViewer = new TreeViewer(composite_2, SWT.BORDER);
		Tree tree = referenceTypeTreeViewer.getTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				clearErrorMessage();
			}
		});
		this.referenceTypeTreeViewer
				.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.ReferenceType)));
		this.referenceTypeTreeViewer.setLabelProvider(new ModelLabelProvider());
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree.heightHint = 162;
		tree.setLayoutData(gd_tree);
		formToolkit.paintBordersFor(tree);
		Composite composite_3 = new Composite(sashForm_1, SWT.NONE);
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(2, false));
		Label lbl_isInverse = new Label(composite_3, SWT.NONE);
		GridData gd_lbl_isInverse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_isInverse.widthHint = 170;
		lbl_isInverse.setLayoutData(gd_lbl_isInverse);
		lbl_isInverse.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"AddReferenceDialog.lbl_isInverse.text"));
		formToolkit.adapt(lbl_isInverse, true, true);
		cb_isInverse = new CheckBoxButton(composite_3, SWT.NONE);
		cb_isInverse.setTouchEnabled(true);
		cb_isInverse.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "AddReferenceDialog.cb_isInverse.text"));
		cb_isInverse.setLeftMargin(8);
		formToolkit.adapt(cb_isInverse);
		formToolkit.paintBordersFor(cb_isInverse);
		Label lbl_target = new Label(composite_3, SWT.NONE);
		GridData gd_lbl_target = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lbl_target.widthHint = 170;
		lbl_target.setLayoutData(gd_lbl_target);
		lbl_target.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "AddReferenceDialog.lbl_target.text"));
		formToolkit.adapt(lbl_target, true, true);
		targetidTreeViewer = new TreeViewer(composite_3, SWT.BORDER);
		Tree tree_1 = targetidTreeViewer.getTree();
		tree_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				clearErrorMessage();
			}
		});
		this.targetidTreeViewer.setContentProvider(new ModelContentProvider());
		this.targetidTreeViewer.setLabelProvider(new ModelLabelProvider());
		GridData gd_tree_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree_1.heightHint = 55;
		tree_1.setLayoutData(gd_tree_1);
		formToolkit.paintBordersFor(tree_1);
		sashForm_1.setWeights(new int[] { 135, 240 });
		lbl_errorMessage = new Label(frmNewForm.getBody(), SWT.NONE);
		lbl_errorMessage.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		GridData gd_lblNewLabel_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.heightHint = 20;
		lbl_errorMessage.setLayoutData(gd_lblNewLabel_1);
		formToolkit.adapt(lbl_errorMessage, true, true);
		lbl_errorMessage.setText("");
		lbl_errorMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		sashForm_1.hookResizeListener();
		this.setInput();
		this.setHandler();
		return container;
	}

	private void setInput() {
		BrowserModelNode node = new BrowserModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.References));
		this.referenceTypeTreeViewer.setInput(node);
		BrowserModelNode n = new BrowserModelNode(null);
		n.setNode(ServerInstance.getNode(Identifiers.RootFolder));
		this.targetidTreeViewer.setInput(n);
		this.clearErrorMessage();
		/**
		 * if referencenode to update is not null, then we edit a node
		 */
		if (this.referenceNodeToUpdate != null) {
			/**
			 * Inverse / Forward
			 */
			this.cb_isInverse.setChecked(this.referenceNodeToUpdate.getIsInverse());
			/**
			 * Target id keys from the node to root
			 */
			List<NodeId> targetKeys = null;
			List<NodeId> referenceKeys = null;
			try {
				targetKeys = this.createHierarchicalRef(ServerInstance.getInstance().getServerInstance()
						.getNamespaceUris().toNodeId(referenceNodeToUpdate.getTargetId()), Identifiers.RootFolder);
				referenceKeys = this.createHierarchicalRef(referenceNodeToUpdate.getReferenceTypeId(),
						Identifiers.References);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			/**
			 * Finds the target id
			 */
			targetidTreeViewer.expandToLevel(TreeViewer.ALL_LEVELS);
			preSelectTreeViewer(targetidTreeViewer, targetKeys);
			/**
			 * Finds reference type
			 */
			referenceTypeTreeViewer.expandToLevel(TreeViewer.ALL_LEVELS);
			preSelectTreeViewer(referenceTypeTreeViewer, referenceKeys);
		}
	}

	private TreeItem expandAndSelectTreeItem(TreeViewer viewer, TreeItem[] items, NodeId key) {
		TreeItem tItem = null;
		for (TreeItem item : items) {
			BrowserModelNode data = (BrowserModelNode) item.getData();
			NodeId nodeId = data.getNode().getNodeId();
			if (key.equals(nodeId)) {
				viewer.getTree().select(item);
				tItem = item;
			}
			item.setExpanded(false);
		}
		return tItem;
	}

	private void preSelectTreeViewer(TreeViewer viewer, List<NodeId> keys) {
		Tree tree = viewer.getTree();
		TreeItem[] items = tree.getItems();
		for (int i = keys.size(); i > 0; i--) {
			NodeId expandId = keys.get(i - 1);
			TreeItem item = expandAndSelectTreeItem(viewer, items, expandId);
			if (i > 1) {
				item.setExpanded(true);
			} else {
				item.setExpanded(false);
			}
			items = item.getItems();
		}
	}

	private TreeItem findTreeItem(TreeItem item, NodeId referenceTypeId) {
		Object data = item.getData();
		if (((BrowserModelNode) data).getNode().getNodeId().equals(referenceTypeId)) {
			return item;
		}
		return null;
	}

	private void findTreeItem(TreeViewer viewer, BrowserModelNode node, List<NodeId> keys, NodeId key,
			List<Object> treeSegments, Map<NodeId, BrowserModelNode> treeMapping) {
		Object[] contents = ((ITreeContentProvider) viewer.getContentProvider()).getChildren(node);
		for (Object content : contents) {
			// checks with my over rided equals method
			if (keys.contains(((BrowserModelNode) content).getNode().getNodeId())) {
				// found last segment
				if (key.equals(((BrowserModelNode) content).getNode().getNodeId())) {
					BrowserModelNode a = treeMapping.get(((BrowserModelNode) content).getNode().getNodeId());
					treeSegments.add(a);
					return;
				}
				// go deeper
				else {
					BrowserModelNode a = treeMapping.get(((BrowserModelNode) content).getNode().getNodeId());
					treeSegments.add(a);
					this.findTreeItem(viewer, (BrowserModelNode) content, keys, key, treeSegments, treeMapping);
				}
				return;
			}
		}
	}

	private TreeItem findTreeItem(TreeItem root, List<NodeId> keys) {
		if (root.getData() instanceof ObjectTypeNode && keys.contains(((ObjectTypeNode) root.getData()).getNodeId())) {
			keys.remove(((ObjectTypeNode) root.getData()).getNodeId());
			return root;
		} else if (root.getItemCount() > 0) {
			TreeItem it = null;
			for (TreeItem item : root.getItems()) {
				it = findTreeItem(item, keys);
				if (it != null) {
					root.setExpanded(true);
					root.getParent().layout();
					if (keys.isEmpty()) {
						/** we have visited all required keys */
						return it;
					}
				}
			}
		}
		return null;
	}

	private void setHandler() {
	}

	private ViewerFilter createViewFilter(final NodeId id) {
		return new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof BrowserModelNode) {
					Node node = ((BrowserModelNode) element).getNode();
					return node.getNodeId().equals(id);
				}
				return false;
			}
		};
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(479, 517);
	}

	/**
	 * Dialog Ok - Button pressed.
	 */
	@Override
	protected void okPressed() {
		ITreeSelection referenceTypeselection = (ITreeSelection) this.referenceTypeTreeViewer.getSelection();
		if (referenceTypeselection.isEmpty()) {
			this.lbl_errorMessage.setText("no reference type selected");
			this.lbl_errorMessage.setVisible(true);
			return;
		}
		this.referenceTypeResult = ((BrowserModelNode) referenceTypeselection.getFirstElement()).getNode();
		ITreeSelection targetIdSelection = (ITreeSelection) this.targetidTreeViewer.getSelection();
		if (targetIdSelection.isEmpty()) {
			this.lbl_errorMessage.setText("no target selected");
			this.lbl_errorMessage.setVisible(true);
			return;
		}
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		this.targetIdResult = ((BrowserModelNode) targetIdSelection.getFirstElement()).getNode();
		boolean update;
		/** Create new Reference */
		if (this.referenceNodeToUpdate == null) {
			update = false;
		}
		/** Update Reference */
		else {
			update = true;
			DeleteReferencesItem deleteRefItem = new DeleteReferencesItem();
			deleteRefItem.setDeleteBidirectional(true);
			deleteRefItem.setIsForward(!referenceNodeToUpdate.getIsInverse());
			deleteRefItem.setReferenceTypeId(referenceNodeToUpdate.getReferenceTypeId());
			deleteRefItem.setSourceNodeId(this.sourceId);
			deleteRefItem.setTargetNodeId(referenceNodeToUpdate.getTargetId());
			StatusCode[] result = null;
			try {
				result = ServerInstance.deleteReference(new DeleteReferencesItem[] { deleteRefItem });
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			if (result != null) {
				result.toString();
			}
		}
		// AddressSpace addressSpace = .getAddressSpace();
		ReferenceNode newReference = new ReferenceNode();
		newReference.setReferenceTypeId(((Node) this.referenceTypeResult).getNodeId());
		newReference.setIsInverse(this.cb_isInverse.isChecked());
		NodeClass targetNodeClass = null;
		if (this.targetIdResult != null) {
			NodeId id = ((Node) this.targetIdResult).getNodeId();
			ExpandedNodeId targetId = new ExpandedNodeId(id);
			// ExpandedNodeId targetId =
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(((Node) this.targetIdResult).getNodeId());
			newReference.setTargetId(targetId);
			targetNodeClass = ServerInstance.getNode(newReference.getTargetId()).getNodeClass();
		}
		// create service
		AddReferencesItem[] referencesToAdd = new AddReferencesItem[] {
				new AddReferencesItem(this.usedNode.getNodeId(), newReference.getReferenceTypeId(),
						!newReference.getIsInverse(), null, newReference.getTargetId(), targetNodeClass) };
		// send local service to server
		StatusCode[] result = null;
		try {
			// add new refernce
			result = ServerInstance.addReferences(referencesToAdd);
			if (!update) {
				if (result[0] != null && result[0].isGood()) {
					this.referenceNodeToUpdate = newReference;
				} else if (result[0] != null && result[0].isBad()) {
					setErrorMessage(result[0]);
				}
			}
			// edit reference
			else {
				this.referenceNodeToUpdate.setIsInverse(newReference.getIsInverse());
				this.referenceNodeToUpdate.setReferenceTypeId(newReference.getReferenceTypeId());
				this.referenceNodeToUpdate.setTargetId(newReference.getTargetId());
			}
			super.okPressed();
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears hides the error message label.
	 */
	private void clearErrorMessage() {
		this.lbl_errorMessage.setText("");
		this.lbl_errorMessage.setVisible(false);
	}

	/**
	 * Set ErrorMessages to display on a Label.
	 * 
	 * @param StatusCodes StatusCodes to display.
	 */
	private void setErrorMessage(StatusCode... statusCodes) {
		StringBuilder errorMessage = new StringBuilder();
		for (StatusCode code : statusCodes) {
			errorMessage.append(code.getName() + "\n");
		}
		this.lbl_errorMessage.setText(errorMessage.toString());
		this.lbl_errorMessage.setVisible(true);
	}

	/**
	 * Gets the ReferencesNode to update.
	 * 
	 * @return ReferenceNode
	 */
	public ReferenceNode getReference() {
		return this.referenceNodeToUpdate;
	}

	private List<NodeId> createHierarchicalRef(NodeId start, NodeId end) {
		List<NodeId> result = new ArrayList<NodeId>();
		NodeId actnode = start;
		result.add(start);
		BrowseResult[] hierachicalbrowseResult = null;
		try {
			do {
				hierachicalbrowseResult = ServerInstance.browse(actnode, Identifiers.HierarchicalReferences,
						NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Inverse, true);
				if (hierachicalbrowseResult != null && hierachicalbrowseResult[0].getReferences().length > 0
						&& hierachicalbrowseResult[0].getReferences()[0].getNodeId() != null
						&& !hierachicalbrowseResult[0].getReferences()[0].getNodeId().equals(end)) {
					actnode = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(hierachicalbrowseResult[0].getReferences()[0].getNodeId());
					result.add(actnode);
				} else {
					break;
				}
			} while (!actnode.equals(end));
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}
		return result;
	}
}
