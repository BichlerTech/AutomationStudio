package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.InverseModelLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.PropertyOfModelContentProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;

public class SelectPropertyOfDialog extends Dialog {
	private TreeViewer propertyOfTreeViewer = null;
	private Object propertyOfResult = null;
	private ReferenceNode referenceNodeToUpdate = null;
	private Node nodeToUpdate = null;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SelectPropertyOfDialog(Shell parentShell) {
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
		PropertyOfModelContentProvider provider = new PropertyOfModelContentProvider(
				NodeClass.getMask(NodeClass.ReferenceType));
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form frmNewForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"SelectPropertyOfDialog.frmNewForm.text")); //$NON-NLS-1$
		frmNewForm.getBody().setLayout(new GridLayout(2, false));
		formToolkit.decorateFormHeading(frmNewForm);
		Label lbl_Reference = new Label(frmNewForm.getBody(), SWT.NONE);
		lbl_Reference.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lbl_Reference.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SELECTPROPERTYOFDIALOG_REFERENCE"));
		this.propertyOfTreeViewer = new TreeViewer(frmNewForm.getBody(), SWT.BORDER);
		Tree tree = propertyOfTreeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.propertyOfTreeViewer.setContentProvider(provider);
		this.propertyOfTreeViewer.setLabelProvider(new InverseModelLabelProvider());
		this.propertyOfTreeViewer.setInput(ServerInstance.getNode(Identifiers.Aggregates));
		return container;
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

	public Node getNodeToUpdate() {
		return nodeToUpdate;
	}

	public void setNodeToUpdate(Node nodeToUpdate) {
		this.nodeToUpdate = nodeToUpdate;
	}

	/**
	 * Dialog Ok - Button pressed.
	 */
	@Override
	protected void okPressed() {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		this.propertyOfResult = ((ITreeSelection) this.propertyOfTreeViewer.getSelection()).getFirstElement();
		if (this.propertyOfResult != null) {
			NodeId id = this.nodeToUpdate.getNodeId();
			ReferenceTypeNode node = (ReferenceTypeNode) this.propertyOfResult;
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

	/**
	 * Gets the ReferencesNode to update.
	 * 
	 * @return ReferenceNode
	 */
	public ReferenceNode getReference() {
		return this.referenceNodeToUpdate;
	}
}
