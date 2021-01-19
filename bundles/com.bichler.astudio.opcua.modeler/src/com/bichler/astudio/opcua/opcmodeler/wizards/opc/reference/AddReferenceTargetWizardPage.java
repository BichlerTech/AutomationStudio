package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridData;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceContentProvider;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

public class AddReferenceTargetWizardPage extends WizardPage {
	private TreeViewer treeViewer;
	private ReferenceModel target;

	/**
	 * Create the wizard.
	 */
	public AddReferenceTargetWizardPage() {
		super("AddReferenceTargetWizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.addref.title"));
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
		this.treeViewer = new TreeViewer(container, SWT.BORDER);
		this.treeViewer.setContentProvider(new ReferenceContentProvider());
		this.treeViewer.setLabelProvider(new ReferenceLabelProvider());
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setBounds(0, 0, 85, 85);
		ReferenceModel model = ReferenceUtil.initializeReferenceTypeTree(Identifiers.RootFolder,
				Identifiers.HierarchicalReferences, false);
		this.treeViewer.setInput(model);
		setHandler();
	}

	private void setHandler() {
		this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				target = (ReferenceModel) ((StructuredSelection) event.getSelection()).getFirstElement();
				/**
				 * default wizard page check
				 */
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	}

	public ReferenceModel getTarget() {
		return this.target;
	}

	@Override
	public boolean isPageComplete() {
		if (this.target == null) {
			return false;
		}
		return true;
	}
}
