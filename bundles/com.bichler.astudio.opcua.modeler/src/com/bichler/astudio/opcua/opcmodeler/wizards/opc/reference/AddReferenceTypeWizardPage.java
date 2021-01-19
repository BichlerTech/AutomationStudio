package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceContentProvider;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

public class AddReferenceTypeWizardPage extends WizardPage {
	private TreeViewer treeViewer;
	private ReferenceModel type;
	private Button btn_isInverse;
	private boolean isInverse;

	/**
	 * Create the wizard.
	 */
	public AddReferenceTypeWizardPage() {
		super("AddReferenceTypeWizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.addreftype.title"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.addref.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		this.treeViewer = new TreeViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeViewer.setContentProvider(new ReferenceContentProvider());
		treeViewer.setLabelProvider(new ReferenceLabelProvider());
		this.btn_isInverse = new Button(container, SWT.CHECK);
		btn_isInverse.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.tblclmn_isinverse.text"));
		ReferenceModel model = ReferenceUtil.initializeReferenceTypeTree(Identifiers.References, Identifiers.HasSubtype,
				false);
		treeViewer.setInput(model);
		setHandler();
		setPageComplete(false);
	}

	private void setHandler() {
		this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				type = (ReferenceModel) ((StructuredSelection) event.getSelection()).getFirstElement();
				/**
				 * default wizard page check
				 */
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		this.btn_isInverse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isInverse = btn_isInverse.getSelection();
			}
		});
	}

	public boolean getIsInverse() {
		return this.isInverse;
	}

	public ReferenceModel getType() {
		return this.type;
	}

	@Override
	public boolean isPageComplete() {
		if (type == null) {
			return false;
		}
		return true;
	}
}
