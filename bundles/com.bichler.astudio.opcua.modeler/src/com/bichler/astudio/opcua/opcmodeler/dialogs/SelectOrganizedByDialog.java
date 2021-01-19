package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.InverseModelLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.OrganizedByModelContentProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;

public class SelectOrganizedByDialog extends StatusDialog {
	/** Controls */
	private TreeViewer organizedByTreeViewer = null;
	private Object organizedByResult = null;
	private ReferenceNode referenceNodeToUpdate = null;
	private Node nodeToUpdate = null;;

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
	public SelectOrganizedByDialog(Shell parentShell) {
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
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		Label lbl_Reference = new Label(container, SWT.NONE);
		GridData gd_lbl_Reference = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_lbl_Reference.widthHint = 132;
		lbl_Reference.setLayoutData(gd_lbl_Reference);
		lbl_Reference.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SELECTORGANIZEDBYDIALOG_REFERENCE"));
		this.organizedByTreeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = organizedByTreeViewer.getTree();
		OrganizedByModelContentProvider provider = new OrganizedByModelContentProvider(
				NodeClass.getMask(NodeClass.ReferenceType));
		this.organizedByTreeViewer.setContentProvider(provider);
		this.organizedByTreeViewer.setLabelProvider(new InverseModelLabelProvider());
		this.organizedByTreeViewer.setInput(ServerInstance.getNode(Identifiers.HierarchicalReferences));
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree.widthHint = 254;
		tree.setLayoutData(gd_tree);
		return container;
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
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		this.organizedByResult = ((ITreeSelection) this.organizedByTreeViewer.getSelection()).getFirstElement();
		if (this.organizedByResult != null) {
			NodeId id = this.nodeToUpdate.getNodeId();
			ReferenceTypeNode node = (ReferenceTypeNode) this.organizedByResult;
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
	}
}
