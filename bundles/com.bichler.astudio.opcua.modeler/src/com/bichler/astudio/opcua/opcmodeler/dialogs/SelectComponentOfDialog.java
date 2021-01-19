package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ComponentOfModelContentProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.InverseModelLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;

public class SelectComponentOfDialog extends Dialog {
	/** Controls */
	private TreeViewer componentOfTreeViewer = null;
	private Object componentOfResult = null;
	private ReferenceNode referenceNodeToUpdate = null;

	public ReferenceNode getReferenceNodeToUpdate() {
		return referenceNodeToUpdate;
	}

	public void setReferenceNodeToUpdate(ReferenceNode referenceNodeToUpdate) {
		this.referenceNodeToUpdate = referenceNodeToUpdate;
	}

	private Node nodeToUpdate = null;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Label lbl_errorMessage = null;

	public Node getNodeToUpdate() {
		return nodeToUpdate;
	}

	public void setNodeToUpdate(Node nodeToUpdate) {
		this.nodeToUpdate = nodeToUpdate;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SelectComponentOfDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.TITLE | SWT.PRIMARY_MODAL);
		this.referenceNodeToUpdate = new ReferenceNode();
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite container = (Composite) super.createDialogArea(parent);
		ComponentOfModelContentProvider provider = new ComponentOfModelContentProvider(
				NodeClass.getMask(NodeClass.ReferenceType));
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form frmNewForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SelectComponentOfDialog.caption")); //$NON-NLS-1$
		frmNewForm.getBody().setLayout(new GridLayout(2, false));
		formToolkit.decorateFormHeading(frmNewForm);
		Label lbl_reference = new Label(frmNewForm.getBody(), SWT.NONE);
		GridData gd_lbl_reference = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lbl_reference.widthHint = 124;
		lbl_reference.setLayoutData(gd_lbl_reference);
		lbl_reference.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"SelectComponentOfDialog.lbl_reference.text"));
		this.componentOfTreeViewer = new TreeViewer(frmNewForm.getBody(), SWT.BORDER);
		Tree tree = componentOfTreeViewer.getTree();
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearErrorMessage();
			}
		});
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lbl_errorMessage = new Label(frmNewForm.getBody(), SWT.NONE);
		GridData gd_lbl_errorMessage = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_lbl_errorMessage.heightHint = 20;
		lbl_errorMessage.setLayoutData(gd_lbl_errorMessage);
		formToolkit.adapt(lbl_errorMessage, true, true);
		lbl_errorMessage.setText("text"); //$NON-NLS-1$
		lbl_errorMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		this.componentOfTreeViewer.setContentProvider(provider);
		this.componentOfTreeViewer.setLabelProvider(new InverseModelLabelProvider());
		this.componentOfTreeViewer.setInput(ServerInstance.getNode(Identifiers.Aggregates));
		this.setInputs();
		return container;
	}

	private void setInputs() {
		/** nodeid of reference to find */
		NodeId id = this.referenceNodeToUpdate.getReferenceTypeId();
		/** get all tree items */
		this.componentOfTreeViewer.expandAll();
		TreeItem[] items = this.componentOfTreeViewer.getTree().getItems();
		TreeItem it = null;
		for (TreeItem item : items) {
			it = this.findTreeItem(item, id);
			if (it != null) {
				// this.variableType = (ReferenceTypeNode)it.getData();
				this.componentOfTreeViewer.getTree().setSelection(it);
				break;
			}
		}
		this.clearErrorMessage();
	}

	private TreeItem findTreeItem(TreeItem root, NodeId key) {
		if (root.getData() instanceof ReferenceTypeNode
				&& ((ReferenceTypeNode) root.getData()).getNodeId().equals(key)) {
			return root;
		} else if (root.getItemCount() > 0) {
			TreeItem it = null;
			for (TreeItem item : root.getItems()) {
				it = findTreeItem(item, key);
				if (it != null) {
					return it;
				}
			}
		}
		return null;
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
		return new Point(364, 365);
	}

	/**
	 * Gets the ReferencesNode to update.
	 * 
	 * @return ReferenceNode
	 */
	public ReferenceNode getReference() {
		return this.referenceNodeToUpdate;
	}

	/**
	 * Dialog Ok - Button pressed.
	 */
	@Override
	protected void okPressed() {
		ITreeSelection selection = (ITreeSelection) this.componentOfTreeViewer.getSelection();
		if (selection.isEmpty()) {
			this.lbl_errorMessage.setText(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SelectComponentOfDialog.error"));
			this.lbl_errorMessage.setVisible(true);
			return;
		}
		this.componentOfResult = selection.getFirstElement();
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		if (this.componentOfResult != null) {
			NodeId id = this.nodeToUpdate.getNodeId();
			ReferenceTypeNode node = (ReferenceTypeNode) this.componentOfResult;
			this.referenceNodeToUpdate.setReferenceTypeId(node.getNodeId());
			this.referenceNodeToUpdate.setIsInverse(true);
			this.referenceNodeToUpdate
					.setTargetId(new ExpandedNodeId(nsTable.getUri(id.getNamespaceIndex()), id.getValue(), nsTable));
			// this.referenceNodeToUpdate.setTargetId(
			// NamespaceTable.getDefaultInstance().toExpandedNodeId(this.nodeToUpdate.getNodeId()));
			super.okPressed();
		}
		/** Update Reference */
		else {
			// update = true;
		}
		super.okPressed();
	}

	/**
	 * Clears hides the error message label.
	 */
	private void clearErrorMessage() {
		this.lbl_errorMessage.setText("");
		this.lbl_errorMessage.setVisible(false);
	}
}
