package com.bichler.astudio.opcua.opcmodeler.wizards.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

public class SearchWizardPage extends WizardPage {
	private Combo cmb_namespace;
	private Combo cmb_type;
	private Text txt_id;
	private Button btn_search;
	private TableViewer tableViewerAttributes;
	private TreeViewer tableViewerNodes;

	/**
	 * Create the wizard.
	 */
	public SearchWizardPage() {
		super("searchnodewizardpage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.node.lookup.title"));
		setDescription(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.node.lookup.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);
		setControl(container);
		container.setLayout(new GridLayout(4, false));
		this.tableViewerNodes = new TreeViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tableNodes_1 = this.tableViewerNodes.getTree();
		tableNodes_1.setHeaderVisible(true);
		tableNodes_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));
		TreeViewerColumn tableViewerColumn = new TreeViewerColumn(tableViewerNodes, SWT.NONE);
		TreeColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(170);
		tblclmnName.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_references.text"));
		this.tableViewerNodes.setContentProvider(new NodesContentProvider());
		this.tableViewerNodes.setLabelProvider(new NodesLabelProvider());
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		Label lbl_namespace = new Label(group, SWT.NONE);
		lbl_namespace.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"));
		this.cmb_namespace = new Combo(group, SWT.NONE);
		cmb_namespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblId = new Label(group, SWT.NONE);
		lblId.setSize(10, 15);
		lblId.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.id"));
		this.cmb_type = new Combo(group, SWT.NONE);
		cmb_type.setSize(91, 23);
		this.txt_id = new Text(group, SWT.BORDER);
		txt_id.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txt_id.setSize(497, 21);
		this.txt_id.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changeNodeIdText(((Text) e.getSource()).getText());
				validatePage();
			}
		});
		this.cmb_type.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeNodeIdType();
				validatePage();
			}
		});
		this.cmb_namespace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		this.btn_search = new Button(group, SWT.NONE);
		GridData gd_btn_search = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1);
		gd_btn_search.widthHint = 32;
		gd_btn_search.heightHint = 32;
		btn_search.setLayoutData(gd_btn_search);
		this.btn_search.setToolTipText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.lookup"));
		this.btn_search.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
				CommonImagesActivator.LOG));
		this.btn_search.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		this.tableViewerAttributes = new TableViewer(container, SWT.BORDER | SWT.V_SCROLL);
		this.tableViewerAttributes.setContentProvider(new NodeTableContentProvider());
		tableViewerAttributes.setLabelProvider(new NodeListLabelProvider());
		Table tableAttributes = tableViewerAttributes.getTable();
		tableAttributes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		TableColumn tblclmnAttribute = new TableColumn(tableAttributes, SWT.NONE);
		tblclmnAttribute.setWidth(150);
		tblclmnAttribute.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.attributes"));
		TableColumn tblclmnWert = new TableColumn(tableAttributes, SWT.NONE);
		tblclmnWert.setWidth(300);
		tblclmnWert.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_value.text"));
		new Label(container, SWT.NONE);
		fillPage();
		validatePage();
	}

	protected void changeNodeIdText(String text) {
		IdType type = (IdType) this.cmb_type.getData(this.cmb_type.getText());
		// remember id
		this.txt_id.setData(type.name(), text);
	}

	protected void changeNodeIdType() {
		String type = this.cmb_type.getText();
		String value = (String) this.txt_id.getData(type);
		this.txt_id.setText(value);
	}

	protected void search() {
		IdType type = (IdType) this.cmb_type.getData(this.cmb_type.getText());
		Integer namespaceIndex = (Integer) this.cmb_namespace.getData(this.cmb_namespace.getText());
		String text = this.txt_id.getText();
		if (text != null && !text.isEmpty()) {
			Object value2look = null;
			// id to look for
			NodeId id2look = null;
			try {
				switch (type) {
				case Guid:
					value2look = UUID.fromString(text);
					id2look = NodeId.get(type, namespaceIndex, value2look);
					break;
				case Numeric:
					value2look = UnsignedInteger.parseUnsignedInteger(text);
					id2look = NodeId.get(type, namespaceIndex, value2look);
					break;
				case Opaque:
					setErrorMessage("Opaque wird noch nicht unterstützt!");
					break;
				case String:
					value2look = text;
					id2look = NodeId.get(type, namespaceIndex, value2look);
					break;
				}
				// remember id
				this.txt_id.setData(type.name(), text);
				Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(id2look);
				if (node != null) {
					this.tableViewerAttributes.setInput(node);
					this.tableViewerNodes.setInput(new Node[] { node });
				}
			} catch (IllegalArgumentException e1) {
				setErrorMessage(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeId.invalid.error"));
			}
		} else {
			Node[] nodes = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getAllNodes(namespaceIndex);
			this.tableViewerNodes.setInput(nodes);
		}
	}

	private void fillPage() {
		// fill namespace index
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		String[] namespaces = nsTable.toArray();
		for (String ns : namespaces) {
			this.cmb_namespace.add(ns);
			this.cmb_namespace.setData(ns, nsTable.getIndex(ns));
		}
		this.cmb_namespace.select(0);
		// fill id types
		IdType[] idTypes = IdType.values();
		for (IdType type : idTypes) {
			String typename = type.name();
			this.cmb_type.add(typename);
			this.cmb_type.setData(typename, type);
		}
		this.cmb_type.select(0);
	}

	/**
	 * Enable/Disable the search button.
	 * 
	 * @param IsVisible
	 */
	private void setSearchEnable(boolean isEnable) {
		this.btn_search.setEnabled(isEnable);
	}

	/**
	 * Enable disables controls
	 */
	private void validatePage() {
		String namespace = this.cmb_namespace.getText();
		String type = this.cmb_type.getText();
		String id = this.txt_id.getText();
		// check for error
		if (namespace.isEmpty()) {
			setErrorMessage(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.select.namespace"));
			setSearchEnable(false);
		} else if (type.isEmpty()) {
			setErrorMessage(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.select.idtype"));
			setSearchEnable(false);
		} else if (id.isEmpty()) {
			setErrorMessage(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.select.identifikation"));
			// setSearchEnable(false);
		} else {
			// remove error message
			setErrorMessage(null);
			setSearchEnable(true);
		}
	}

	class SearchTableEntry {
		private String tag = "";
		private String value = "";

		SearchTableEntry(String tag, String value) {
			this.tag = tag;
			this.value = value;
		}

		public String getTag() {
			return tag;
		}

		public String getValue() {
			return value;
		}
	}

	class NodeTableContentProvider extends ArrayContentProvider {
		public NodeTableContentProvider() {
			super();
		}

		@Override
		public Object[] getElements(Object inputElement) {
			List<SearchTableEntry> entries = new ArrayList<>();
			// node
			Node base = (Node) inputElement;
			// attributes
			NodeClass nodeClass = base.getNodeClass();
			SearchTableEntry entry = new SearchTableEntry(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.nodeclass"),
					nodeClass.toString());
			entries.add(entry);
			QualifiedName browsename = base.getBrowseName();
			entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.lbl_browseName.text"), browsename.toString());
			entries.add(entry);
			LocalizedText description = base.getDescription();
			entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.lbl_description.text"), description.toString());
			entries.add(entry);
			LocalizedText displayname = base.getDisplayName();
			entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.lbl_displayName.text"), displayname.toString());
			entries.add(entry);
			NodeId nodeId = base.getNodeId();
			entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.lbl_nodeId.text"), nodeId.toString());
			entries.add(entry);
			UnsignedInteger userWriteMask = base.getUserWriteMask();
			entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.lbl_userWriteMask.text"), userWriteMask.toString());
			entries.add(entry);
			UnsignedInteger writeMask = base.getWriteMask();
			entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.lbl_writeMask.text"), writeMask.toString());
			entries.add(entry);
			switch (nodeClass) {
			case Object: {
				UnsignedByte eventNotifier = ((ObjectNode) base).getEventNotifier();
				entry = new SearchTableEntry(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.eventnotifier"),
						eventNotifier.toString());
				entries.add(entry);
			}
				break;
			case Variable: {
				UnsignedByte accessLevel = ((VariableNode) base).getAccessLevel();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"CreateVariableDialog.lbl_accessLevel.text"), accessLevel.toString());
				entries.add(entry);
				UnsignedInteger[] arrayDimension = ((VariableNode) base).getArrayDimensions();
				entry = new SearchTableEntry(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"NodeEditorVariableTypePart.lbl_arrayDimensions.text"),
						Arrays.deepToString(arrayDimension));
				entries.add(entry);
				NodeId dataType = ((VariableNode) base).getDataType();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorVariableTypePart.lbl_dataType.text"), dataType.toString());
				entries.add(entry);
				Boolean historizing = ((VariableNode) base).getHistorizing();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"CreateVariableDialog.btn_historizing.text"), historizing.toString());
				entries.add(entry);
				Double minimumSamplingInterval = ((VariableNode) base).getMinimumSamplingInterval();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"CreateVariableDialog.lbl_minSamplInt.text"), minimumSamplingInterval.toString());
				entries.add(entry);
				UnsignedByte userAccessLevel = ((VariableNode) base).getUserAccessLevel();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorVariablePart.lbl_userAccessLevel.text"), userAccessLevel.toString());
				entries.add(entry);
				Variant value = ((VariableNode) base).getValue();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorVariableTypePart.lbl_value.text"), value.toString());
				entries.add(entry);
				Integer valueRank = ((VariableNode) base).getValueRank();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorVariablePart.lbl_valueRank.text"), valueRank.toString());
				entries.add(entry);
			}
				break;
			case Method: {
				Boolean executeable = ((MethodNode) base).getExecutable();
				entry = new SearchTableEntry(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.method.executeable"),
						executeable.toString());
				entries.add(entry);
				Boolean userExecuteable = ((MethodNode) base).getUserExecutable();
				entry = new SearchTableEntry(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.method.userexecuteable"),
						userExecuteable.toString());
				entries.add(entry);
			}
				break;
			case ObjectType: {
				Boolean isAbstract = ((ObjectTypeNode) base).getIsAbstract();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorDataTypePart.lbl_isAbstract.text"), isAbstract.toString());
				entries.add(entry);
			}
				break;
			case VariableType: {
				UnsignedInteger[] arrayDimension = ((VariableTypeNode) base).getArrayDimensions();
				entry = new SearchTableEntry(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"NodeEditorVariableTypePart.lbl_arrayDimensions.text"),
						Arrays.deepToString(arrayDimension));
				entries.add(entry);
				NodeId dataType = ((VariableTypeNode) base).getDataType();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorVariableTypePart.lbl_dataType.text"), dataType.toString());
				entries.add(entry);
				Boolean historizing = ((VariableTypeNode) base).getIsAbstract();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"CreateVariableDialog.btn_historizing.text"), historizing.toString());
				entries.add(entry);
				Variant value = ((VariableTypeNode) base).getValue();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"CreateVariableDialog.lbl_value.text"), value.toString());
				entries.add(entry);
				Integer valueRank = ((VariableTypeNode) base).getValueRank();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"CreateVariableDialog.lbl_valueRank.text"), valueRank.toString());
				entries.add(entry);
			}
				break;
			case DataType: {
				Boolean isAbstract = ((DataTypeNode) base).getIsAbstract();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorDataTypePart.lbl_isAbstract.text"), isAbstract.toString());
				entries.add(entry);
			}
				break;
			case ReferenceType: {
				LocalizedText inverseName = ((ReferenceTypeNode) base).getInverseName();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorReferenceTypePart.lbl_inverseName.text"), inverseName.toString());
				entries.add(entry);
				Boolean isAbstract = ((ReferenceTypeNode) base).getIsAbstract();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorVariableTypePart.lbl_isAbstract.text"), isAbstract.toString());
				entries.add(entry);
				Boolean symmetric = ((ReferenceTypeNode) base).getSymmetric();
				entry = new SearchTableEntry(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"NodeEditorReferenceTypePart.lbl_symmetric.text"), symmetric.toString());
				entries.add(entry);
			}
				break;
			}
			return entries.toArray();
		}
	}

	class NodesLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Node) {
				return ((Node) element).getDisplayName().getText();
			} else if (element instanceof ReferenceNode) {
				return ((ReferenceNode) element).getReferenceTypeId() + " - " + ((ReferenceNode) element).getIsInverse()
						+ " - " + ((ReferenceNode) element).getTargetId().toString().replaceAll("%3A", ":")
								.replaceAll("%2F", "/");
			}
			return "";
		}
	}

	class NodesContentProvider implements ITreeContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object input) {
			return getChildren(input);
		}

		@Override
		public Object[] getChildren(Object element) {
			if (element instanceof Object[]) {
				return (Object[]) element;
			} else if (element instanceof Node) {
				return ((Node) element).getReferences();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	class NodeListLabelProvider extends LabelProvider implements ITableLabelProvider {
		public NodeListLabelProvider() {
			super();
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((SearchTableEntry) element).getTag();
			default:
				return ((SearchTableEntry) element).getValue();
			}
		}
	}
}
