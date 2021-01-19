package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.EnumSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

import opc.sdk.core.enums.EventNotifiers;
import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.UAObjectNode;

public class NodeEditorObjectPart extends NodeEditorExtensionPart {
	public static final String ID = "com.hbsoft.designer.editor.node.NodeEditorObjectPart"; //$NON-NLS-1$
	private CheckBoxButton cb_sub2events = null;
	private CheckBoxButton cb_hisoryRead = null;
	private CheckBoxButton cb_historyWrite = null;
	private CometCombo cmb_modellingRule;
	private Label lbl_modellingRule;
	private Label lbl_events;
	private TreeViewer tv_objectType;
	private boolean showModellingRule = false;
	private Composite typeViewerComposite;

	public NodeEditorObjectPart() {
		super();
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		frm_mainForm.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectPart.frm_mainForm.text")
						+ getEditorInput().getName());
	}

	@Override
	protected void setInputs() {
		super.setInputs();
		UAObjectNode node = (UAObjectNode) this.getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectPart.open.info"), node);
		if (showModellingRule) {
			this.controllCreationToolkit.setComboModellingRule(cmb_modellingRule, node);
		}
		this.controllCreationToolkit.setObjectTypeTreeViewer(tv_objectType, node);
		// this.controllCreationToolkit.setObjectReferenceTable(tv_references, node);
		UnsignedByte eventNotifier = ((ObjectNode) node).getEventNotifier();
		EnumSet<EventNotifiers> set = EventNotifiers.getSet(eventNotifier.intValue());
		if (set.contains(EventNotifiers.SubscribeToEvents)) {
			this.cb_sub2events.setChecked(true);
		}
		if (set.contains(EventNotifiers.HistoryRead)) {
			this.cb_hisoryRead.setChecked(true);
		}
		if (set.contains(EventNotifiers.HistoryWrite)) {
			this.cb_historyWrite.setChecked(true);
		}
		ExpandedNodeId objectTypeId = null;
		/** get data type and variable type of that node */
		if (node instanceof ObjectNode) {
			// objectTypeId = ((ObjectNode) node).getTypeId();
			objectTypeId = node.findTarget(Identifiers.HasTypeDefinition, false);
		}
		/** get all tree items */
		this.tv_objectType.expandAll();
		TreeItem[] items = this.tv_objectType.getTree().getItems();
		TreeItem it = null;
		for (TreeItem item : items) {
			try {
				it = this.findTreeItem(item,
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(objectTypeId));
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			if (it != null) {
				this.tv_objectType.getTree().setSelection(it);
				break;
			}
		}
	}

	@Override
	protected void setHandlers() {
		super.setHandlers();
		if (showModellingRule) {
			this.controllCreationToolkit.setDirtyListener(lbl_modellingRule, cmb_modellingRule, this);
		}
		this.controllCreationToolkit.setDirtyListener(null, this.cb_hisoryRead, this);
		this.controllCreationToolkit.setDirtyListener(null, this.cb_historyWrite, this);
		this.controllCreationToolkit.setDirtyListener(null, this.cb_sub2events, this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.setFocus();
		if (this.valid()) {
			super.doSave(monitor);
			if (monitor.isCanceled()) {
				return;
			}
			UAObjectNode node = (UAObjectNode) getEditorInput().getNode().getNode();
			byte eventNotifier = 0;
			// construct the new changed values
			if (this.cb_sub2events.isChecked()) {
				eventNotifier += EventNotifiers.SubscribeToEvents.getValue();
			}
			if (this.cb_hisoryRead.isChecked()) {
				eventNotifier += EventNotifiers.HistoryRead.getValue();
			}
			if (this.cb_historyWrite.isChecked()) {
				eventNotifier += EventNotifiers.HistoryWrite.getValue();
			}
			node.setEventNotifier(new UnsignedByte(eventNotifier));
			/** will we need to save modelling rule ? */
			if (showModellingRule) {
				if (this.cmb_modellingRule.getText().compareTo(CustomString
						.getString(Activator.getDefault().RESOURCE_BUNDLE, "ModellingRule.norule.text")) == 0) {
					/** if we previously have an rule so we need to delete it */
					this.controllCreationToolkit.deleteModellingRule(node);
				} else {
					this.controllCreationToolkit.setModellingRule(node,
							NamingRuleType.valueOf(this.cmb_modellingRule.getText()));
				}
			}
			// Executable(this.cb_executeable.isChecked());
			// objectNode.setUserExecutable(this.cb_userExecuteable.isChecked());
			this.frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorObjectPart.frm_mainForm.text") + getEditorInput().getName());
			doSaveFinish();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Node node = getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectPart.close.info"),
				node);
	}

	private TreeItem findTreeItem(TreeItem root, NodeId key) {
		Object data = null;
		if (root.getData() instanceof BrowserModelNode) {
			data = ((BrowserModelNode) root.getData()).getNode();
		} else {
			data = root.getData();
		}
		if (data instanceof ObjectTypeNode && key.equals(((ObjectTypeNode) data).getNodeId())) {
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

	@Override
	protected void createExtendedSection(Composite extended) {
		Label lbl_objectType = new Label(extended, SWT.NONE);
		GridData gd_lbl_objectType = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lbl_objectType.widthHint = 220;
		lbl_objectType.setLayoutData(gd_lbl_objectType);
		lbl_objectType.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorObjectPart.lbl_objectType.text"));
		formToolkit.adapt(lbl_objectType, true, true);
		/**
		 * Changes thomas zöchbauer - Composite to disable type tree viewer
		 */
		this.typeViewerComposite = new Composite(extended, SWT.NONE);
		GridData tvgd = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		tvgd.heightHint = 200;
		typeViewerComposite.setLayoutData(tvgd);
		typeViewerComposite.setLayout(new FillLayout());
		tv_objectType = new TreeViewer(typeViewerComposite, SWT.BORDER);
		Tree tree = tv_objectType.getTree();
		// tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,
		// 1));
		formToolkit.paintBordersFor(tree);
		/** will we need it ? */
		if (!DesignerUtils.isChildOf(getEditorInput().getNode().getNode().getNodeId(), Identifiers.ObjectsFolder)) {
			showModellingRule = true;
			lbl_modellingRule = new Label(extended, SWT.NONE);
			GridData gd_lbl_modellingRule = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			gd_lbl_modellingRule.widthHint = 220;
			lbl_modellingRule.setLayoutData(gd_lbl_modellingRule);
			lbl_modellingRule.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorObjectPart.lbl_modellingRule.text")); //$NON-NLS-1$
			formToolkit.adapt(lbl_modellingRule, true, true);
			cmb_modellingRule = new CometCombo(extended, SWT.READ_ONLY);
			GridData gd_cmb_modellingRule = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_cmb_modellingRule.widthHint = 250;
			cmb_modellingRule.setLayoutData(gd_cmb_modellingRule);
			formToolkit.adapt(cmb_modellingRule);
			formToolkit.paintBordersFor(cmb_modellingRule);
		}
		lbl_events = new Label(extended, SWT.NONE);
		GridData gd_lbl_events = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_events.widthHint = 220;
		lbl_events.setLayoutData(gd_lbl_events);
		lbl_events.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectPart.lbl_events.text"));
		formToolkit.adapt(lbl_events, true, true);
		Composite grpMhcg = new Composite(extended, SWT.NONE);
		GridData gd_grpMhcg = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpMhcg.heightHint = 40;
		grpMhcg.setLayoutData(gd_grpMhcg);
		formToolkit.adapt(grpMhcg);
		formToolkit.paintBordersFor(grpMhcg);
		cb_sub2events = new CheckBoxButton(grpMhcg, SWT.NONE);
		cb_sub2events.setBounds(10, 5, 160, 30);
		cb_sub2events.setTouchEnabled(true);
		cb_sub2events.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorObjectPart.cb_sub2events.text"));
		cb_sub2events.setLeftMargin(8);
		formToolkit.paintBordersFor(cb_sub2events);
		cb_hisoryRead = new CheckBoxButton(grpMhcg, SWT.NONE);
		cb_hisoryRead.setTouchEnabled(true);
		cb_hisoryRead.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorObjectPart.cb_historyRead.text"));
		cb_hisoryRead.setLeftMargin(8);
		cb_hisoryRead.setBounds(170, 5, 160, 30);
		formToolkit.paintBordersFor(cb_hisoryRead);
		cb_historyWrite = new CheckBoxButton(grpMhcg, SWT.NONE);
		cb_historyWrite.setTouchEnabled(true);
		cb_historyWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorObjectPart.cb_historyWrite.text"));
		cb_historyWrite.setLeftMargin(8);
		cb_historyWrite.setBounds(330, 5, 160, 30);
		formToolkit.paintBordersFor(cb_historyWrite);
		lbl_writeMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_writeMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_writeMask.widthHint = 220;
		lbl_writeMask.setLayoutData(gd_lbl_writeMask);
		lbl_writeMask.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_writeMask.text"));
		formToolkit.adapt(lbl_writeMask, true, true);
		txt_writeMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_writeMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_writeMask.widthHint = 250;
		txt_writeMask.setLayoutData(gd_txt_writeMask);
		formToolkit.adapt(txt_writeMask, true, true);
		lbl_userWriteMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_userWriteMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_userWriteMask.widthHint = 220;
		lbl_userWriteMask.setLayoutData(gd_lbl_userWriteMask);
		lbl_userWriteMask.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.lbl_userWriteMask.text"));
		formToolkit.adapt(lbl_userWriteMask, true, true);
		txt_userWriteMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_userWriteMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_userWriteMask.widthHint = 250;
		txt_userWriteMask.setLayoutData(gd_txt_userWriteMask);
		formToolkit.adapt(txt_userWriteMask, true, true);
	}

	@Override
	protected void preDisableWidgets() {
		this.typeViewerComposite.setEnabled(false);
	}
}
