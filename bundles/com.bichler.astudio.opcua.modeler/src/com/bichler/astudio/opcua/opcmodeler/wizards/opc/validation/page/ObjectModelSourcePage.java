package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.page;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.ModelValidationWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModelContentFactory;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModelContentProvider;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModelLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ObjectModelSourcePage extends WizardPage {
	private ModelTypDef selectedDef;
	private TreeViewer treeViewer;

	/**
	 * Create the wizard.
	 */
	public ObjectModelSourcePage() {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.modelsource.title"));
		setDescription(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.modelsource.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, true));
		this.treeViewer = new TreeViewer(container, SWT.BORDER);
		treeViewer.setContentProvider(new ValidationModelContentProvider());
		treeViewer.setLabelProvider(new ValidationModelLabelProvider());
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setBounds(0, 0, 64, 64);
		setHandler();
		initWizardPage();
	}

	private void setHandler() {
		this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				((ModelValidationWizard) getWizard())
						.setObjectModelSourceTypeSelection((IStructuredSelection) event.getSelection());
			}
		});
	}

	private void initWizardPage() {
		ExpandedNodeId selectedId = this.selectedDef.nodeId;
		NodeClass nodeClass = this.selectedDef.typeClass;
		try {
			NodeId[] path = ValidationModelContentFactory.getSelectedPath(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(selectedId));
			ValidationModelContentFactory.setObjectModelSourceTreeInput(this.treeViewer, path);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	public void setSelectedDef(ModelTypDef selectedDef) {
		this.selectedDef = selectedDef;
	}
}
