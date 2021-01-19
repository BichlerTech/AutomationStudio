package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.ReferenceRule;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;

public class DeleteReferenceWizardPage extends WizardPage {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	// private Table table_source;
	// private Table table_target;
	private TableViewer tv_source;
	private TableViewer tv_target;
	private BrowserModelNode source;
	private ReferenceNode reference2edit;
	private List<ReferenceRule> sourceRules;
	private List<ReferenceRule> targetRules;
	private Object targetref;
	private ReferenceNode target;
	private boolean deleteable;
	// We use icons
	// private static final Image CHECKED =
	// Activator.getImageDescriptor("icons/default_icons/add.png").createImage();
	// private static final Image UNCHECKED =
	// Activator.getImageDescriptor("icons/default_icons/edit.png").createImage();

	/**
	 * Create the wizard.
	 * 
	 * @param pageTwo
	 */
	public DeleteReferenceWizardPage() {
		super("DeleteReferenceWizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_references.text"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.deleteref.description"));
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
		// tables
		createTable(container);
		// table columns
		createTableColumns();
		setHandler();
		// input
		setInput();
	}

	private void setHandler() {
	}

	private void createTable(Composite container) {
		ScrolledComposite scrolledContainer = new ScrolledComposite(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledContainer.setExpandHorizontal(true);
		scrolledContainer.setExpandVertical(true);
		Composite composite = new Composite(scrolledContainer, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		Label lbl_outgoing = new Label(composite, SWT.NONE);
		formToolkit.adapt(lbl_outgoing, true, true);
		lbl_outgoing.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.outref"));
		// Table reference source
		this.tv_source = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_source = tv_source.getTable();
		table_source.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_source.setHeaderVisible(true);
		formToolkit.paintBordersFor(table_source);
		this.tv_source.setContentProvider(new ArrayContentProvider());
		Label lblEingehendeReferenzen = new Label(composite, SWT.NONE);
		formToolkit.adapt(lblEingehendeReferenzen, true, true);
		lblEingehendeReferenzen.setText("form.inref");
		// Table reference target
		this.tv_target = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_target = tv_target.getTable();
		table_target.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_target.setHeaderVisible(true);
		formToolkit.paintBordersFor(table_target);
		this.tv_target.setContentProvider(new ArrayContentProvider());
		scrolledContainer.setContent(composite);
		scrolledContainer.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void createTableColumns() {
		// source
		TableViewerColumn tvc_s_source = new TableViewerColumn(tv_source, SWT.NONE);
		TableColumn tc_s_source = tvc_s_source.getColumn();
		tc_s_source.setWidth(100);
		tc_s_source.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.source"));
		addLP_source(true, true, tvc_s_source);
		// column source reference, reference type
		TableViewerColumn tvc_s_refType = new TableViewerColumn(tv_source, SWT.NONE);
		TableColumn tc_s_refType = tvc_s_refType.getColumn();
		tc_s_refType.setWidth(116);
		tc_s_refType.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.addreftype.title"));
		addLP_refType(true, tv_source, tvc_s_refType);
		// column source reference direction
		TableViewerColumn tvc_s_direction = new TableViewerColumn(tv_source, SWT.NONE);
		TableColumn tc_s_direction = tvc_s_direction.getColumn();
		tc_s_direction.setWidth(146);
		tc_s_direction.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.isinverse"));
		addLP_direction(true, tv_source, tvc_s_direction);
		// column source reference target node
		TableViewerColumn tvc_s_target = new TableViewerColumn(tv_source, SWT.NONE);
		TableColumn tc_s_target = tvc_s_target.getColumn();
		tc_s_target.setWidth(168);
		tc_s_target.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "AddReferenceDialog.lbl_target.text"));
		addLP_target(true, tv_source, tvc_s_target);
		TableViewerColumn tvc_t_source = new TableViewerColumn(tv_target, SWT.NONE);
		TableColumn tc_t_source = tvc_t_source.getColumn();
		tc_t_source.setWidth(100);
		tc_t_source.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.source"));
		addLP_source(false, false, tvc_t_source);
		TableViewerColumn tvc_t_refType = new TableViewerColumn(tv_target, SWT.NONE);
		TableColumn tc_t_refType = tvc_t_refType.getColumn();
		tc_t_refType.setWidth(116);
		tc_t_refType.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.addreftype.title"));
		addLP_refType(false, tv_target, tvc_t_refType);
		TableViewerColumn tvc_t_direction = new TableViewerColumn(tv_target, SWT.NONE);
		TableColumn tc_t_direction = tvc_t_direction.getColumn();
		tc_t_direction.setWidth(146);
		tc_t_direction.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.isinverse"));
		addLP_direction(false, tv_target, tvc_t_direction);
		TableViewerColumn tvc_t_target = new TableViewerColumn(tv_target, SWT.NONE);
		TableColumn tc_t_target = tvc_t_target.getColumn();
		tc_t_target.setWidth(168);
		tc_t_target.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "AddReferenceDialog.lbl_target.text"));
		addLP_target(false, tv_target, tvc_t_target);
	}

	private void addLP_source(boolean sourceViewer, final boolean isSource, TableViewerColumn column) {
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				if (isSource) {
					NodeId id = source.getNode().getNodeId();
					Node node = ServerInstance.getNode(id);
					return node.getDisplayName().toString();
				} else {
					ExpandedNodeId id = reference2edit.getTargetId();
					Node node = ServerInstance.getNode(id);
					return node.getDisplayName().toString();
				}
			}
		});
	}

	private void addLP_refType(boolean sourceViewer, TableViewer viewer, TableViewerColumn column) {
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				NodeId refId = ((ReferenceNode) element).getReferenceTypeId();
				Boolean isInverse = ((ReferenceNode) element).getIsInverse();
				ReferenceTypeNode refIdNode = (ReferenceTypeNode) ServerInstance.getNode(refId);
				if (refIdNode != null) {
					Boolean isSymetric = refIdNode.getSymmetric();
					if (!isSymetric && isInverse) {
						LocalizedText inverseName = refIdNode.getInverseName();
						return inverseName.toString();
					}
					LocalizedText refName = refIdNode.getDisplayName();
					return refName.toString();
				}
				return refId.toString();
			}
		});
		// if (sourceViewer) {
		// column.setEditingSupport(new ReferenceTypeEditingSupport(viewer));
		// }
	}

	private void addLP_direction(boolean sourceViewer, TableViewer viewer, TableViewerColumn column) {
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				// boolean isInverse = ((ReferenceNode) element).getIsInverse();
				// if (isInverse) {
				// return CHECKED;
				// }
				// return UNCHECKED;
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				Boolean isInverse = ((ReferenceNode) element).getIsInverse();
				return isInverse.toString();
			}
		});
		// if (sourceViewer) {
		// column.setEditingSupport(new IsInverseEditingSupport(viewer));
		// }
	}

	private void addLP_target(boolean sourceViewer, TableViewer viewer, TableViewerColumn column) {
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				ExpandedNodeId target = ((ReferenceNode) element).getTargetId();
				Node targetIdNode = ServerInstance.getNode(target);
				if (targetIdNode != null) {
					return targetIdNode.getDisplayName().toString();
				}
				return target.toString();
			}
		});
		// if (sourceViewer) {
		// column.setEditingSupport(new TargetEditingSupport(viewer));
		// }
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
			// this.setErrorMessage("Cannot delete hierachical reference");
			this.setErrorMessage(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.nohierachical"));
			this.deleteable = false;
			break;
		case Typedef:
			// this.setErrorMessage("Cannot delete typedefinition reference");
			this.setErrorMessage(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.notype"));
			this.deleteable = false;
			break;
		case ModellParent:
			this.setErrorMessage(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.parent"));
			this.setErrorMessage(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.modelingrule"));
			this.deleteable = false;
			break;
		default:
			this.deleteable = true;
			break;
		}
		this.tv_source.setInput(new ReferenceNode[] { this.reference2edit });
		if (target != null) {
			this.tv_target.setInput(new ReferenceNode[] { target });
		}
		setPageComplete(this.deleteable);
	}

	public boolean isDeleteable() {
		return this.deleteable;
	}

	public void setSourceNode(BrowserModelNode source) {
		this.source = source;
	}

	public void setSourceReference(ReferenceNode refNode2Edit) {
		this.reference2edit = refNode2Edit;
	}

	@Override
	public void dispose() {
		// CHECKED.dispose();
		// UNCHECKED.dispose();
		super.dispose();
	}

	public ReferenceNode getSourceReferenceNode() {
		return ((ReferenceNode[]) this.tv_source.getInput())[0];
	}

	public ReferenceNode getTargetReferenceNode() {
		if (this.tv_target.getInput() != null) {
			return ((ReferenceNode[]) this.tv_target.getInput())[0];
		}
		return null;
	}

	public void setSourceRules(List<ReferenceRule> sourceRules) {
		this.sourceRules = sourceRules;
	}

	public void setTargetRules(List<ReferenceRule> targetRules) {
		this.targetRules = targetRules;
	}

	public void setTargetReference(ReferenceNode targetRefNode) {
		this.target = targetRefNode;
	}
}
