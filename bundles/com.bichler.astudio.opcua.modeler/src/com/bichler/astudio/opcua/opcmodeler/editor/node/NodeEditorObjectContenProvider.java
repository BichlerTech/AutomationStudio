package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;

import opc.sdk.core.enums.EventNotifiers;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.UAObjectNode;

public class NodeEditorObjectContenProvider extends NodeEditorContentProvider {
	private CheckBoxButton[] cb_supportEvents = null;
	// private ValidatingField<String> txt_type = null;
	// private ValidatingField<String> txt_nodeIdType = null;
	private TreeViewer treeViewer = null;

	public NodeEditorObjectContenProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

	protected void createContent(Node node, ScrolledComposite parent) {
		GridLayoutFactory.fillDefaults().applyTo(parent);
		Composite composite = new Composite(parent, SWT.SHADOW_ETCHED_IN);
		parent.setContent(composite);
		GridLayoutFactory.fillDefaults().applyTo(composite);
		Group nodeGroup = new Group(composite, SWT.NONE);
		// Base Node Content
		createBaseNodeContent(nodeGroup, node);
		// Label * SupportsEvents
		this.controllCreationToolkit.createLabel(nodeGroup,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.support.events"));
		// CheckBoxField * SupportsEvents
		this.cb_supportEvents = this.controllCreationToolkit.createCheckSupportEvents(nodeGroup);
		// Label * Base Object Type
		Label lb_baseObjectType = this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectPart.lbl_objectType.text"));
		GridDataFactory.fillDefaults().span(1, 20).applyTo(lb_baseObjectType);
		// Treeviewer * Object Type
		this.treeViewer = this.controllCreationToolkit.createObjectTypeTreeViewer(nodeGroup);
		GridDataFactory.fillDefaults().span(2, 20).align(SWT.FILL, SWT.FILL).applyTo(this.treeViewer.getControl());
		this.disableEditableSection(this.treeViewer.getTree());
		// Label * Reference
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.tblclmn_reference.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createObjectReferenceTable(composite, node,
				this.callBackEditor);
		createReferenceTableButtonSection(composite, node);
		createErrorLabel(composite);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		composite.layout();
		/** Disables the Editor if it is a default Server Node. */
		if (!isEditable(node)) {
			disableEditableSection(nodeGroup);
		}
	}

	protected void setNodeInput(Node node) {
		setBaseNodeInput(node);
		setReferenceTableNodeInput(node);
		// fill datatype combo box
		Map<String, NodeId> objectTypes = this.controllCreationToolkit.fetchObjectTypes();
		ExpandedNodeId objectTypeId = null;
		/** get data type and variable type of that node */
		if (node instanceof ObjectNode) {
			// objectTypeId = ((ObjectNode) node).getTypeId();
			objectTypeId = node.findTarget(Identifiers.HasTypeDefinition, false);
		}
		UnsignedByte eventNotifier = ((ObjectNode) node).getEventNotifier();
		EnumSet<EventNotifiers> set = EventNotifiers.getSet(eventNotifier.intValue());
		for (EventNotifiers evtNotifier : set) {
			for (int i = 0; i < this.cb_supportEvents.length; i++) {
				String data = this.cb_supportEvents[i].getText();
				EventNotifiers value = EventNotifiers.valueOf(data);
				if (evtNotifier.equals(value)) {
					this.cb_supportEvents[i].setImage(null);
				}
			}
		}
		for (CheckBoxButton b : this.cb_supportEvents) {
			b.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					callBackEditor.setDirty(true);
				}
			});
		}
		/** get all tree items */
		this.treeViewer.expandAll();
		TreeItem[] items = this.treeViewer.getTree().getItems();
		TreeItem it = null;
		for (TreeItem item : items) {
			try {
				it = this.findTreeItem(item,
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(objectTypeId));
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			if (it != null) {
				this.treeViewer.getTree().setSelection(it);
				break;
			}
		}
	}

	@Override
	public void doSave(Node node) {
		// save the default node attributes
		doSaveNodeBaseAttributes(node);
		// cast the node
		UAObjectNode objNode = (UAObjectNode) node;
		// construct the new changed values
		UnsignedByte eventNotifier = this.createEventNotifier();
		// change the object node
		objNode.setEventNotifier(eventNotifier);
	}

	/**
	 * Constructs an event notifier object node attribute.
	 * 
	 * @return browsename
	 */
	private UnsignedByte createEventNotifier() {
		List<EventNotifiers> eventNotifierList = new ArrayList<EventNotifiers>();
		boolean isNone = true;
		for (int i = 0; i < this.cb_supportEvents.length; i++) {
			CheckBoxButton eventNotifier = this.cb_supportEvents[i];
			if (eventNotifier.isChecked()) {
				String data = cb_supportEvents[i].getText();
				EventNotifiers value = EventNotifiers.valueOf(data);
				eventNotifierList.add(value);
				if (isNone) {
					isNone = false;
				}
			}
		}
		UnsignedInteger mask = EventNotifiers
				.getMask(eventNotifierList.toArray(new EventNotifiers[eventNotifierList.size()]));
		return new UnsignedByte(mask.intValue());
	}

	private TreeItem findTreeItem(TreeItem root, NodeId key) {
		if (root.getData() instanceof ObjectTypeNode && key.equals(((ObjectTypeNode) root.getData()).getNodeId())) {
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
}
