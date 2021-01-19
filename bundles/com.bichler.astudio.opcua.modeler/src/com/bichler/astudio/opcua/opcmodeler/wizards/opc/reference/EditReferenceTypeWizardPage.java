package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.ReferenceRule;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceContentProvider;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceModel;
import com.bichler.astudio.utils.internationalization.CustomString;

public class EditReferenceTypeWizardPage extends WizardPage {
	private TreeViewer treeViewer;
	private NodeId currentReferenceType;
	private ReferenceModel selectedElement;
	private ReferenceModel model;
	private ReferenceModel usedModel;
	private ReferenceModel initReferenceType;
	private List<ReferenceRule> sourceRules;
	private List<ReferenceRule> targetRules;

	/**
	 * Create the wizard.
	 * 
	 * @param pageOne
	 */
	public EditReferenceTypeWizardPage() {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.addreftype.title"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.addreftype.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.treeViewer = new TreeViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		this.treeViewer.setContentProvider(new ReferenceContentProvider());
		this.treeViewer.setLabelProvider(new ReferenceLabelProvider());
		setInput();
		manualRefresh();
		setHandler();
	}

	private void setInput() {
		ReferenceRule rule = ReferenceRule.None;
		if (this.sourceRules.contains(ReferenceRule.Hierachy) || this.targetRules.contains(ReferenceRule.Hierachy)) {
			rule = ReferenceRule.Hierachy;
		} else if (this.sourceRules.contains(ReferenceRule.Typedef)
				|| this.targetRules.contains(ReferenceRule.Typedef)) {
			rule = ReferenceRule.Typedef;
		} else if (this.sourceRules.contains(ReferenceRule.ModellParent)
				|| this.targetRules.contains(ReferenceRule.ModellParent)) {
			rule = ReferenceRule.ModellParent;
		}
		switch (rule) {
		case Hierachy:
			this.usedModel = this.model.find(ServerInstance.getInstance().getServerInstance().getNamespaceUris(),
					Identifiers.HierarchicalReferences);
			break;
		case Typedef:
			this.usedModel = this.model.find(ServerInstance.getInstance().getServerInstance().getNamespaceUris(),
					Identifiers.HasTypeDefinition);
			break;
		case ModellParent:
			// this.usedModel = this.model.find(ServerInstance.getInstance()
			// .getServerInstance().getNamespaceUris(),
			// Identifiers.HasModelParent);
			break;
		default:
			this.usedModel = this.model;
			break;
		}
		this.treeViewer.setInput(this.usedModel);
		manualRefresh();
	}

	protected void manualRefresh() {
		this.treeViewer.collapseAll();
		if (this.currentReferenceType != null) {
			ReferenceModel element = usedModel.find(ServerInstance.getInstance().getServerInstance().getNamespaceUris(),
					this.currentReferenceType);
			select(element);
		}
	}

	private void setHandler() {
		this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection) event.getSelection();
				selectedElement = (ReferenceModel) selection.getFirstElement();
				/**
				 * default wizard page check
				 */
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	}

	private void select(ReferenceModel element) {
		LinkedList<ReferenceModel> hierachy = new LinkedList<>();
		buildSelectHierachy(hierachy, element);
		Tree tree = this.treeViewer.getTree();
		// rek select
		rekSelect(hierachy, element, tree.getItems());
	}

	private void rekSelect(LinkedList<ReferenceModel> hierachy, ReferenceModel element, TreeItem[] items) {
		TreeItem found = null;
		for (TreeItem item : items) {
			ReferenceModel data = (ReferenceModel) item.getData();
			if (hierachy.contains(data)) {
				item.setExpanded(true);
				this.treeViewer.refresh();
				if (element == data) {
					this.initReferenceType = data;
					this.selectedElement = data;
					found = item;
					break;
				}
				TreeItem[] t = item.getItems();
				rekSelect(hierachy, element, t);
			}
		}
		if (found != null) {
			this.treeViewer.getTree().select(found);
		}
	}

	private void buildSelectHierachy(LinkedList<ReferenceModel> hierachy, ReferenceModel element) {
		hierachy.addFirst(element);
		if (element.getParent() != null) {
			buildSelectHierachy(hierachy, element.getParent());
		}
	}

	public void setReferenceType(NodeId value) {
		this.currentReferenceType = value;
	}

	protected void setModel(ReferenceModel model) {
		this.model = model;
	}

	public ReferenceModel getNewReferenceType() {
		if (this.initReferenceType != this.selectedElement) {
			return this.selectedElement;
		}
		return null;
	}

	@Override
	public boolean isPageComplete() {
		if (selectedElement == null) {
			return false;
		}
		if (selectedElement == initReferenceType) {
			return false;
		}
		return true;
	}

	public void setSourceRules(List<ReferenceRule> sourceRules) {
		this.sourceRules = sourceRules;
	}

	public void setTargetRules(List<ReferenceRule> targetRules) {
		this.targetRules = targetRules;
	}
}
