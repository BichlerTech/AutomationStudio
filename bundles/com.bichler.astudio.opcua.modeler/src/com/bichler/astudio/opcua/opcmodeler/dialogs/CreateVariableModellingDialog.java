package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.constants.ImageConstants;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.CreateNodeUtil;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.ITreeViewerChildren;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.CreateModellingLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.CreateVariableModellingContentProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;

public class CreateVariableModellingDialog extends AbstractCreateModellingDialog implements ITreeViewerChildren {
	private NodeId parentNodeId = Identifiers.HAConfiguration;
	private TreeViewer treeViewer = null;
	private Image imgChecked = null;
	private Image imgUnChecked = null;
	private Image img1Checked = null;
	private List<NodeId> inherit = null;
	private List<Node> children = null;
	private Map<Node, Boolean> mapping = null;
	private Tree tree;
	private boolean hasRuleItself = false;
	private boolean hasRules = false;
	private Combo cmb_namingRule;
	private NamingRuleType namingRule;

	@Override
	protected void okPressed() {
		/** now get all selected nodes to use */
		/** first we need to expand the whole treeviewer */
		children = new ArrayList<Node>();
		mapping = new HashMap<Node, Boolean>();
		// add additional nodes
		if (this.hasRules) {
			treeViewer.expandAll();
			this.addNodes(children, treeViewer.getTree().getItems());
		}
		// add a reference as NamingRule
		if (this.hasRuleItself) {
			this.addModelRuleSelf();
		}
		super.okPressed();
	}

	private void addModelRuleSelf() {
		String text = this.cmb_namingRule.getText();
		this.namingRule = (NamingRuleType) this.cmb_namingRule.getData(text);
	}

	private void addNodes(List<Node> nodes, TreeItem[] items) {
		Node node = null;
		for (TreeItem item : items) {
			node = (Node) item.getData();
			if (item.getImage().equals(imgChecked) || item.getImage().equals(img1Checked)) {
				this.eliminateModRule(node);
				nodes.add(node);
				mapping.put(node, true);
				addNodes(nodes, item.getItems());
			} else {
				mapping.put(node, false);
			}
		}
	}

	/**
	 * eliminate modeling rules references
	 * 
	 * @param node
	 */
	private void eliminateModRule(Node node) {
		List<ReferenceNode> refs = new ArrayList<ReferenceNode>();
		for (ReferenceNode ref : node.getReferences()) {
			if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
				if (ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)
						|| ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
					continue;
				}
			}
			refs.add(ref);
		}
		node.setReferences(refs.toArray(new ReferenceNode[refs.size()]));
	}

	public NodeId getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(NodeId parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public CreateVariableModellingDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.TITLE);
		this.imgChecked = DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.CHECKED_1_IMG);
		this.imgUnChecked = DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.CHECKED_0_IMG);
		this.img1Checked = DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.CHECKED_1_GRAY_IMG);
		filtersModellingRule = new NodeClass[] { NodeClass.ObjectType, NodeClass.VariableType, NodeClass.DataType,
				NodeClass.ReferenceType };
		filtersModellingParent = new NodeClass[] { NodeClass.ObjectType };
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		/**
		 * HasRuleItself - Section
		 */
		if (this.hasRuleItself) {
			createHasRuleItselfSection(container);
		}
		/**
		 * HasRules - Section
		 */
		if (this.hasRules) {
			createHasRulesDialogSection(container);
		}
		return container;
	}

	private void createHasRuleItselfSection(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		composite.setLayout(new GridLayout(2, false));
		GridData layoutData = new GridData();
		Label lbl_namingType = new Label(composite, SWT.NONE);
		lbl_namingType.setLayoutData(layoutData);
		lbl_namingType.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.namingrule"));
		layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		layoutData.minimumWidth = 200;
		this.cmb_namingRule = new Combo(composite, SWT.READ_ONLY);
		cmb_namingRule.setLayoutData(layoutData);
		/** add none */
		cmb_namingRule.add(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.none"));
		cmb_namingRule.select(0);
		/** add other namingrules */
		for (NamingRuleType nrType : NamingRuleType.values()) {
			cmb_namingRule.add(nrType.name());
			cmb_namingRule.setData(nrType.name(), nrType);
		}
	}

	/**
	 * HasRules - Section
	 * 
	 * @param container
	 */
	private void createHasRulesDialogSection(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new FillLayout());
		treeViewer = new TreeViewer(composite, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				for (TreeItem item : treeViewer.getTree().getSelection()) {
					if (item.getImage() != null) {
						if ((e.x > item.getImageBounds(0).x)
								&& (e.x < (item.getImageBounds(0).x + item.getImage().getBounds().width))) {
							if ((e.y > item.getImageBounds(0).y)
									&& (e.y < (item.getImageBounds(0).y + item.getImage().getBounds().height))) {
								setChecked(item);
							}
						}
					}
				}
			}
		});
		CreateModellingLabelProvider labelProvider = new CreateModellingLabelProvider();
		labelProvider.setImgChecked(imgChecked);
		labelProvider.setImgUnChecked(imgUnChecked);
		labelProvider.setImg1Checked(img1Checked);
		treeViewer.setLabelProvider(labelProvider);
		inherit = DesignerUtils.getInheritance(Identifiers.BaseVariableType, this.parentNodeId);
		treeViewer.setContentProvider(new CreateVariableModellingContentProvider(this.inherit));
		treeViewer.setInput(inherit);
	}

	private void setChecked(TreeItem item) {
		if (item.getImage().equals(imgUnChecked)) {
			item.setImage(imgChecked);
		} else if (item.getImage().equals(imgChecked)) {
			item.setImage(imgUnChecked);
			// item.getItems()[0].setBackground(color);
			// item.setExpanded(false);
		}
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
		if (!hasRuleItself && hasRules) {
			return new Point(542, 487);
		}
		return super.getInitialSize();
	}

	public Map<Node, Boolean> getModelRuleChildren() {
		return this.mapping;
	}

	@Override
	public boolean hasTreeViewerChildren(NodeId result, UnsignedInteger filter) {
		List<NodeId> inheritated = DesignerUtils.getInheritance(Identifiers.BaseObjectType, result);
		Object[] children = CreateNodeUtil.getNodeChildrenForTreeViewer(inheritated, filter);
		return children.length > 0;
	}

	public void setRuleItself(boolean hasRuleItself) {
		this.hasRuleItself = hasRuleItself;
	}

	public void setHasRules(boolean hasRules) {
		this.hasRules = hasRules;
	}

	public NamingRuleType getModelRuleSelf() {
		return this.namingRule;
	}

	@Override
	protected NodeClass[] getFilteredRules() {
		return this.filtersModellingRule;
	}

	@Override
	protected NodeClass[] getFilteredParent() {
		return this.filtersModellingParent;
	}
}
