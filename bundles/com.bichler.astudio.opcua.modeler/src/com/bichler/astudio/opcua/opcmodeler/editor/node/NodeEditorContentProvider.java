package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.DeleteReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.AddReferenceDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.WriteMaskDialog;
import com.bichler.astudio.opcua.opcmodeler.preferences.ShowDialogPreferencePage;
import com.richclientgui.toolbox.validation.ValidatingField;

public abstract class NodeEditorContentProvider {
	/**
	 * variables for general use
	 */
	protected Map<String, String> usedNodeIds = null;
	protected NodeEditorPart callBackEditor = null;
	protected DesignerFormToolkit controllCreationToolkit = null;
	/**
	 * SWT Controls to display the ua node attributes
	 */
	private ValidatingField<Integer> txt_namespaceBrowseName;
	private ValidatingField<String> txt_browseName;
	private ValidatingField<String> txt_description;
	private ValidatingField<String> txt_displayName;
	private ValidatingField<String> txt_nodeId;
	private ValidatingField<UnsignedInteger> txt_writeMask;
	private ValidatingField<UnsignedInteger> txt_userWriteMask;
	// private TreeViewer typeTreeViewer = null;
	protected TableViewer tableViewer = null;
	protected Label errorLabel = null;
	private ValidatingField<Locale> combo_localeDescription = null;
	private ValidatingField<Locale> combo_localeDisplayname = null;
	private Button btn_addReference = null;
	private Button btn_removeReference = null;

	/**
	 * Content factory for a NodeEditorPart. Creates and Saves the content for a
	 * node.
	 * 
	 * @param nodeEditorPart on which the content factory depends.
	 */
	public NodeEditorContentProvider(NodeEditorPart nodeEditorPart) {
		this.callBackEditor = nodeEditorPart;
		this.usedNodeIds = new HashMap<String, String>();
		this.controllCreationToolkit = new DesignerFormToolkit();
	}

	protected abstract void createContent(Node node, ScrolledComposite scrolledComposite);

	protected abstract void setNodeInput(Node node);

	/**
	 * This is a detailed save method for a NodeEditorPart doSave() method. Saves
	 * the changes of a node.
	 * 
	 * @param node to save
	 */
	public abstract void doSave(Node node);

	/**
	 * Creates the content for a NodeEditorPart depends on the node.
	 * 
	 * @param node   to create the content for the editor, depends on its class,
	 *               because of a node attributes.
	 * 
	 * @param parent on which the content will be created.
	 */
	public static NodeEditorContentProvider createContent(Node node, Composite parent, NodeEditorPart nodeEditorPart) {
		if (node == null) {
			return null;
		}
		// ExpandedNodeId selectedParent =
		// node.findTarget(Identifiers.Organizes, true);
		NodeEditorContentProvider provider = null;
		// Scrolled Composite
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setAlwaysShowScrollBars(true);
		switch (node.getNodeClass()) {
		case Object:
			provider = new NodeEditorObjectContenProvider(nodeEditorPart);
			break;
		case ObjectType:
			provider = new NodeEditorObjectTypeContentProvider(nodeEditorPart);
			break;
		case Variable:
			provider = new NodeEditorVariableContentProvider(nodeEditorPart);
			break;
		case VariableType:
			provider = new NodeEditorVariableTypeContentProvider(nodeEditorPart);
			break;
		case DataType:
			provider = new NodeEditorDataTypeContentProvider(nodeEditorPart);
			break;
		case Method:
			provider = new NodeEditorMethodContentProvider(nodeEditorPart);
			break;
		case ReferenceType:
			provider = new NodeEditorReferenceTypeContentProvider(nodeEditorPart);
			break;
		case View:
			provider = new NodeEditorViewContentProvider(nodeEditorPart);
			break;
		default:
			provider = new NodeEditorUnspecifiedContentProvider(nodeEditorPart);
			break;
		}
		provider.createContent(node, scrolledComposite);
		provider.setNodeInput(node);
		return provider;
	}

	/**
	 * Prints the StatusCode Errors on the Error Label.
	 * 
	 * @param StatusCodes StatusCodes to print.F
	 */
	public void setError(StatusCode... statusCodes) {
		StringBuilder errorMessage = new StringBuilder();
		for (StatusCode code : statusCodes) {
			errorMessage.append(code.getDescription() + "\n");
		}
		this.errorLabel.setText(errorMessage.toString());
		this.errorLabel.setVisible(true);
	}

	/**
	 * Checks if the NodeEditorContent is Editable.
	 * 
	 * @param Node InputNode which filles the EditorContent.
	 * 
	 * @return TRUE if the Node is not a default UA Server Node, otherwise FALSE. Or
	 *         in the preferences the flag edit opc internals is set to return
	 *         always TRUE
	 */
	protected boolean isEditable(Node node) {
		// is edit internal elements allowed?
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		if (preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_OPCUA_EDIT_INTERNAL)) {
			return true;
		}
		int nsServerIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(NamespaceTable.OPCUA_NAMESPACE);
		if (node.getNodeId().getNamespaceIndex() == nsServerIndex) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the current content is valid (input of the controls to design a
	 * model)
	 * 
	 * @return true if the input is valid or false
	 */
	public boolean valid() {
		// check all text fields
		if (this.txt_namespaceBrowseName != null) {
			if (!this.txt_namespaceBrowseName.isValid()) {
				return false;
			}
		}
		if (this.txt_browseName != null) {
			if (!this.txt_browseName.isValid()) {
				return false;
			}
		}
		if (this.txt_description != null) {
			if (!this.txt_description.isValid()) {
				return false;
			}
		}
		if (this.txt_displayName != null) {
			if (!this.txt_displayName.isValid()) {
				return false;
			}
		}
		if (this.txt_nodeId != null) {
			if (!this.txt_nodeId.isValid()) {
				return false;
			}
		}
		if (this.txt_writeMask != null) {
			if (!this.txt_writeMask.isValid()) {
				return false;
			}
		}
		if (this.txt_userWriteMask != null) {
			if (!this.txt_userWriteMask.isValid()) {
				return false;
			}
		}
		return true;
	}

	/**************************************************************************
	 * 
	 * creates the whole method panel content
	 * 
	 *************************************************************************/
	private CometCombo combo_nodeId;

	protected void createBaseNodeContent(final Composite parent, final Node node) {
		parent.setLayout(new GridLayout(3, false));
		// Label * Browsename
		Label lb_browseName = this.controllCreationToolkit.createLabel(parent, "Browsename");
		lb_browseName.setLayoutData(this.controllCreationToolkit.gridData3);
		// Text * NamespaceIndex
		this.txt_namespaceBrowseName = this.controllCreationToolkit.createTextInt32(parent, 0);
		this.txt_namespaceBrowseName.getControl().setLayoutData(this.controllCreationToolkit.gridData1);
		// Text * Browsename
		this.txt_browseName = this.controllCreationToolkit.createTextBrowseName(parent);
		this.txt_browseName.getControl().setLayoutData(this.controllCreationToolkit.gridData2);
		// Label * Description
		Label lb_description = this.controllCreationToolkit.createLabel(parent, "Description");
		lb_description.setLayoutData(this.controllCreationToolkit.gridData3);
		// Combo * Description
		this.combo_localeDescription = this.controllCreationToolkit.createComboLocale(parent, node.getDescription());
		this.combo_localeDescription.getControl().setLayoutData(this.controllCreationToolkit.gridData1);
		// Text * Description
		this.txt_description = this.controllCreationToolkit.createTextString(parent);
		GridDataFactory.fillDefaults().span(1, 1).applyTo(this.txt_description.getControl());
		this.txt_description.getControl().setLayoutData(this.controllCreationToolkit.gridData2);
		// Label * Displayname
		Label lb_displayName = this.controllCreationToolkit.createLabel(parent, "Displayname");
		lb_displayName.setLayoutData(this.controllCreationToolkit.gridData3);
		// Combo * Displayname
		this.combo_localeDisplayname = this.controllCreationToolkit.createComboLocale(parent, node.getDisplayName());
		this.combo_localeDisplayname.getControl().setLayoutData(this.controllCreationToolkit.gridData1);
		// Text * Displayname
		this.txt_displayName = this.controllCreationToolkit.createTextString(parent);
		this.txt_displayName.getControl().setLayoutData(this.controllCreationToolkit.gridData2);
		// Label * NodeId
		Label lb_nodeId = this.controllCreationToolkit.createLabel(parent, "NodeId");
		lb_nodeId.setLayoutData(this.controllCreationToolkit.gridData3);
		// Combo * NodeId
		this.combo_nodeId = this.controllCreationToolkit.createComboNodeId(parent,
				node.getNodeId().getNamespaceIndex());
		this.combo_nodeId.setLayoutData(this.controllCreationToolkit.gridData1);
		this.combo_nodeId.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// txt_nodeId.setContents("");
				String index = combo_nodeId.getText();
				String content = usedNodeIds.get(index);
				if (content != null) {
					txt_nodeId.setContents(content);
				} else {
					txt_nodeId.setContents("");
				}
				txt_nodeId.validate();
			}
		});
		// Text * NodeId
		// this.txt_nodeId =
		// this.controllCreationToolkit.createTextNodeId(parent,
		// this.combo_nodeId, this.usedNodeIds);
		this.txt_nodeId.getControl().setLayoutData(this.controllCreationToolkit.gridData2);
		// Label * WriteMask
		Label lb_writeMask = this.controllCreationToolkit.createLabel(parent, "WriteMask");
		lb_writeMask.setLayoutData(this.controllCreationToolkit.gridData3);
		// Text * WriteMask
		this.txt_writeMask = this.controllCreationToolkit.createTextUnsignedInteger(parent, new UnsignedInteger());
		((Text) this.txt_writeMask.getControl()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});
		((Text) this.txt_writeMask.getControl()).addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(parent.getShell(), node.getNodeClass(),
						txt_writeMask.getContents());
				int open = writemaskDialog.open();
				if (open == Dialog.OK) {
					UnsignedInteger writeMask = AttributeWriteMask.getMask(writemaskDialog.getWritemask());
					txt_writeMask.setContents(writeMask);
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.txt_writeMask.getControl());
		// Label * UserWriteMask
		Label lb_userwritemask = this.controllCreationToolkit.createLabel(parent, "UserWriteMask");
		lb_userwritemask.setLayoutData(this.controllCreationToolkit.gridData3);
		// Text * UserWriteMask
		this.txt_userWriteMask = this.controllCreationToolkit.createTextUnsignedInteger(parent, new UnsignedInteger());
		((Text) this.txt_userWriteMask.getControl()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});
		((Text) this.txt_userWriteMask.getControl()).addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(parent.getShell(), node.getNodeClass(),
						txt_userWriteMask.getContents());
				int open = writemaskDialog.open();
				if (open == Dialog.OK) {
					UnsignedInteger writeMask = AttributeWriteMask.getMask(writemaskDialog.getWritemask());
					txt_userWriteMask.setContents(writeMask);
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.txt_userWriteMask.getControl());
		parent.layout();
	}

	/**
	 * Set the input for a OPC UA Node (Default node attributes). This includes the
	 * Browsename, Description, Displayname, NodeId, WriteMask, UserWritemask.
	 * 
	 * @param node
	 */
	protected void setBaseNodeInput(Node node) {
		// base values
		String browseName = node.getBrowseName().getName();
		String localeId_description = node.getDescription().getLocaleId();
		String description = node.getDescription().getText();
		String localeId_displayname = node.getDisplayName().getLocaleId();
		String displayname = node.getDisplayName().getText();
		String nodeIdValue = node.getNodeId().getValue().toString();
		String writeMask = node.getWriteMask().toString();
		String userWriteMask = node.getUserWriteMask().toString();
		browseName = browseName == null ? "" : browseName;
		description = description == null ? "" : description;
		displayname = displayname == null ? "" : displayname;
		nodeIdValue = nodeIdValue == null ? "" : nodeIdValue;
		writeMask = writeMask == null ? "" : writeMask;
		userWriteMask = userWriteMask == null ? "" : userWriteMask;
		Locale locale_displayname = fetchLocaleFromId(localeId_displayname);
		Locale locale_description = fetchLocaleFromId(localeId_description);
		this.combo_nodeId.select(node.getNodeId().getNamespaceIndex());
		if (nodeIdValue != null && !nodeIdValue.isEmpty()) {
			this.usedNodeIds.put(this.combo_nodeId.getText(), nodeIdValue);
		}
		this.txt_namespaceBrowseName.setContents(node.getBrowseName().getNamespaceIndex());
		this.txt_browseName.setContents(browseName);
		this.txt_description.setContents(description);
		this.txt_displayName.setContents(displayname);
		this.txt_nodeId.setContents(nodeIdValue);
		this.txt_writeMask.setContents(node.getWriteMask());
		this.txt_userWriteMask.setContents(node.getUserWriteMask());
		this.combo_localeDescription.setContents(locale_description);
		this.combo_localeDisplayname.setContents(locale_displayname);
		((Text) this.txt_namespaceBrowseName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Text) this.txt_browseName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Text) this.txt_description.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Text) this.txt_displayName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Text) this.txt_nodeId.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Text) this.txt_writeMask.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Text) this.txt_userWriteMask.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Combo) this.combo_localeDescription.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Combo) this.combo_localeDisplayname.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
	}

	/**
	 * private void setNodeTypeInput(Node node) { NodeId nodeType = null; try {
	 * nodeType = ServerInstance .getInstance() .getServerInstance()
	 * .getNamespaceUris() .toNodeId( node.findTarget(Identifiers.HasTypeDefinition,
	 * false)); } catch (ServiceResultException e1) { e1.printStackTrace(); }
	 * 
	 * String typeText = ServerInstance.getInstance().getServerInstance()
	 * .getAddressSpace().getNode(nodeType).getDisplayName().getText();
	 * 
	 * //this.txt_nodeIdType.setContents(nodeType.toString());
	 * //this.txt_type.setContents(typeText);
	 * 
	 * /* NodeId nodeType = null; try { nodeType = ServerInstance .getInstance()
	 * .getServerInstance() .getNamespaceUris() .toNodeId(
	 * node.findTarget(Identifiers.HasTypeDefinition, false)); // Select the type
	 * from the treeviewer
	 * 
	 * selectTreeViewerType(nodeType); } catch (ServiceResultException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 */
	/*
	 * private void selectTreeViewerType(NodeId nodeType) {
	 * this.typeTreeViewer.expandAll();
	 * 
	 * TreeItem item = findTreeItem(this.typeTreeViewer.getTree().getItems(),
	 * nodeType); this.typeTreeViewer.getTree().select(item);
	 * 
	 * TreeSelection selection = (TreeSelection) this.typeTreeViewer
	 * .getSelection();
	 * 
	 * this.typeTreeViewer.collapseAll();
	 * this.typeTreeViewer.setSelection(selection);
	 * 
	 * this.typeTreeViewer .addSelectionChangedListener(new
	 * ISelectionChangedListener() {
	 * 
	 * @Override public void selectionChanged(SelectionChangedEvent event) {
	 * callBackEditor.setDirty(true);
	 * 
	 * } }); }
	 */
	protected void setReferenceTableNodeInput(Node node) {
		this.tableViewer.setInput(node.getReferences());
	}

	/**
	 * Saves the base node attributes
	 * 
	 * @param node
	 */
	protected void doSaveNodeBaseAttributes(Node node) {
		QualifiedName browseName = createBrowseName();
		LocalizedText description = createDescription(node);
		LocalizedText displayName = createDisplayName(node);
		NodeId nodeId = createNodeId(node);
		ReferenceNode[] references = createReferenceNodes(node);
		UnsignedInteger userWriteMask = createUserWriteMask();
		UnsignedInteger writeMask = createWriteMask();
		// change nodeId
		boolean hasChanged = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
				.changeNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris(), node, nodeId);
		if (hasChanged) {
			this.usedNodeIds.put(this.combo_nodeId.getText(), nodeId.getValue().toString());
		}
		// change node attributes
		node.setBrowseName(browseName);
		node.setDescription(description);
		node.setDisplayName(displayName);
		node.setReferences(references);
		node.setUserWriteMask(userWriteMask);
		node.setWriteMask(writeMask);
	}

	/**
	 * Saves something unspecified
	 * 
	 * @param node
	 */
	// private void doSaveUnspecified(Node node) {
	// // System.out.println("DoSaveUnspecified");
	// }
	/**
	 * Constructs a browsename attribute.
	 * 
	 * @return browsename
	 */
	private QualifiedName createBrowseName() {
		// get the index from the textbox browsename
		int namespaceIndex = this.txt_namespaceBrowseName.getContents();
		// get the text from the textbox browsename
		String name = this.txt_browseName.getContents();
		// constructs a new browsename from the controls
		QualifiedName browseName = new QualifiedName(namespaceIndex, name);
		return browseName;
	}

	/**
	 * Constructs a description attribute.
	 * 
	 * @param node
	 * 
	 * @return description
	 */
	private LocalizedText createDescription(Node node) {
		String text = this.txt_description.getContents();
		Locale locale = this.combo_localeDescription.getContents();
		LocalizedText description = new LocalizedText(text, locale);
		return description;
	}

	/**
	 * Constructs a displayname attribute.
	 * 
	 * @return displayname
	 */
	private LocalizedText createDisplayName(Node node) {
		String text = this.txt_displayName.getContents();
		Locale locale = this.combo_localeDisplayname.getContents();
		LocalizedText displayname = new LocalizedText(text, locale);
		return displayname;
	}

	/**
	 * Constructs a nodeid attribute. TODO: CHANGE NODEID
	 * 
	 * @param node
	 * 
	 * @return nodeid
	 */
	private NodeId createNodeId(Node node) {
		Object identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.combo_nodeId.getText());
		NodeId nodeId = NodeIdUtil.createNodeId(index, identifier);
		return nodeId;
	}

	/**
	 * Constructs a reference attribute. TODO: CHANGEREFERENCES
	 * 
	 * @param node
	 * 
	 * @return references
	 */
	private ReferenceNode[] createReferenceNodes(Node node) {
		return node.getReferences();
	}

	/**
	 * Constructs an userwritemask attribute.
	 * 
	 * @return userwritemask
	 */
	private UnsignedInteger createUserWriteMask() {
		UnsignedInteger userWriteMask = new UnsignedInteger(this.txt_userWriteMask.getContents());
		return userWriteMask;
	}

	/**
	 * Constructs a writemask attribute.
	 * 
	 * @return writemask
	 */
	private UnsignedInteger createWriteMask() {
		UnsignedInteger writeMask = new UnsignedInteger(this.txt_writeMask.getContents());
		return writeMask;
	}

	protected void createErrorLabel(Composite composite) {
		this.errorLabel = new Label(composite, SWT.NONE);
		this.errorLabel.setForeground(new Color(null, 255, 0, 0));
		this.errorLabel.setVisible(false);
		GridDataFactory.fillDefaults().span(3, 1).applyTo(this.errorLabel);
	}

	private Locale fetchLocaleFromId(String localeId) {
		if (localeId != null && !localeId.isEmpty()) {
			for (Locale loc : Locale.getAvailableLocales()) {
				if (localeId.equals(loc.toString())) {
					return loc;
				}
			}
		}
		return null;
	}

	/**
	 * Recursive Method to find the TreeItem for the Nodes Type.
	 * 
	 * @param Items    Items to get the type from the Node.
	 * @param NodeType Type to find.
	 * @return TreeItem with the NodeTypeData.
	 */
	/*
	 * private TreeItem findTreeItem(TreeItem[] items, NodeId nodeType) { for
	 * (TreeItem item : items) { NodeId treeItemTypeData = ((Node)
	 * item.getData()).getNodeId(); if (!nodeType.equals(treeItemTypeData)) {
	 * TreeItem foundItem = findTreeItem(item.getItems(), nodeType); if (foundItem
	 * != null) { return foundItem; } } else { return item; } } return null; }
	 */
	/**
	 * Creates the ReferenceTable Button section (Group).
	 * 
	 * @param Composite Composite to use.
	 */
	protected void createReferenceTableButtonSection(final Composite composite, final Node node) {
		Group referenceButtonGroup = new Group(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(referenceButtonGroup);
		this.btn_addReference = new Button(referenceButtonGroup, SWT.PUSH);
		this.btn_addReference.setText("Add Reference");
		this.btn_addReference.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddReferenceDialog addReferenceDialog = new AddReferenceDialog(composite.getShell());
				addReferenceDialog.setUsedNode(callBackEditor.getEditorInput().getNode().getNode());
				// , callBackEditor.getEditorInput()
				// .getNode());
				// addReferenceDialog.setReferenceNodeToUpdate(callBackEditor.getEditorInput()
				// .getNode());
				int open = addReferenceDialog.open();
				if (open == Dialog.OK) {
					// ReferenceNode newReference = addReferenceDialog
					// .getReference();
					// adds the reference to the table display
					// tableViewer.add(newReference);
					tableViewer.refresh();
					if (callBackEditor != null) {
						callBackEditor.setDirty(true);
					}
				}
			}
		});
		this.btn_removeReference = new Button(referenceButtonGroup, SWT.PUSH);
		this.btn_removeReference.setText("Remove Reference");
		this.btn_removeReference.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReferenceNode referenceToDelete = (ReferenceNode) ((IStructuredSelection) tableViewer.getSelection())
						.getFirstElement();
				if (referenceToDelete != null) {
					// check if element[0] is not the deleted one
					boolean allowToDelete = checkDeleteAllowed(referenceToDelete);
					if (allowToDelete) {
						// removes the reference from the model
						Node nodeToModify = callBackEditor.getEditorInput().getNode().getNode();
						// used local address space
						// AddressSpace addressSpace = ServerInstance
						// .getInstance().getServerInstance()
						// .getAddressSpace();
						// create service
						DeleteReferencesItem[] referencesToDelete = new DeleteReferencesItem[] {
								new DeleteReferencesItem(nodeToModify.getNodeId(),
										((ReferenceNode) referenceToDelete).getReferenceTypeId(),
										!((ReferenceNode) referenceToDelete).getIsInverse(),
										((ReferenceNode) referenceToDelete).getTargetId(), true) };
						// send local service to server
						try {
							// StatusCode[] result =
							ServerInstance.deleteReference(referencesToDelete);
						} catch (ServiceResultException e1) {
							e1.printStackTrace();
						}
						// removes the reference from the table display
						tableViewer.remove(referenceToDelete);
						if (callBackEditor != null) {
							callBackEditor.setDirty(true);
						}
					} else {
						setError(new StatusCode(StatusCodes.Bad_NoDeleteRights));
					}
				}
			}

			/**
			 * NICHT ZU LÖSCHEN<br>
			 * -TYPEDEFINITION Reference<br>
			 * -ModelParent Reference <br>
			 * -References von ModelingParent
			 * 
			 * @param referenceToDelete
			 * 
			 * @return TRUE if the Reference is deleteable otherwise FALSE.
			 */
			private boolean checkDeleteAllowed(ReferenceNode referenceToDelete) {
				boolean isTypeDefReference = ServerInstance.getInstance().getServerInstance().getTypeTable()
						.isTypeOf(referenceToDelete.getReferenceTypeId(), Identifiers.HasTypeDefinition);
				if (isTypeDefReference) {
					return false;
				}
				// boolean isModelParentRefeference = ServerInstance
				// .getInstance()
				// .getServerInstance()
				// .getTypeTree()
				// .isTypeOf(referenceToDelete.getReferenceTypeId(),
				// Identifiers.HasModelParent);
				//
				// if (isModelParentRefeference) {
				// return false;
				// }
				if (referenceToDelete.getIsInverse()) {
					// ExpandedNodeId[] referencesForward = node.findTargets(
					// Identifiers.HasModelParent, false);
					// for (ExpandedNodeId id : referencesForward) {
					// if (referenceToDelete.getTargetId().equals(id)) {
					// return false;
					// }
					// }
				} else {
					// ExpandedNodeId[] referencesInverse = node.findTargets(
					// Identifiers.HasModelParent, true);
					// for (ExpandedNodeId id : referencesInverse) {
					// if (referenceToDelete.getTargetId().equals(id)) {
					// return false;
					// }
					// }
				}
				return true;
			}
		});
		GridData gridData = new GridData();
		this.btn_addReference.setLayoutData(gridData);
		this.btn_removeReference.setLayoutData(gridData);
		referenceButtonGroup.layout();
	}

	/**
	 * Disables the editable Widgets.
	 * 
	 * @param WidgetToDisable Widgets to Disable
	 */
	protected void disableEditableSection(Composite... widgetToDisable) {
		for (Composite widget : widgetToDisable) {
			widget.setEnabled(false);
		}
		if (this.btn_addReference != null) {
			this.btn_addReference.setEnabled(false);
		}
		if (this.btn_removeReference != null) {
			this.btn_removeReference.setEnabled(false);
		}
	}

	protected void refreshReferenceTable() {
		tableViewer.refresh();
	}
}
