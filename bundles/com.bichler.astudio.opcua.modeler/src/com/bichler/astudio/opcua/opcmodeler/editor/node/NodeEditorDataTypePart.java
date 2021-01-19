package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Identifiers;
import org.xml.sax.SAXException;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;
import com.bichler.astudio.opcua.datadictionary.modeler.model.DataDictionaryHelper;
import com.bichler.astudio.opcua.datadictionary.modeler.model.StructuredDataTypeManager;
import com.bichler.astudio.opcua.datadictionary.modeler.model.StructuredDatatypeNodeGenerator;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.types.TypeTable;

public class NodeEditorDataTypePart extends NodeEditorExtensionPart {
	public static final String ID = "com.hbsoft.designer.editor.node.NodeEditorDataTypePart"; //$NON-NLS-1$

//	private static final String TAGNAME_TYPEDICTIONARY = "TypeDictionary";
//	private static final String ATTRIBUTE_NAME = "Name";

	private Label lbl_isAbstract = null;
	private CheckBoxButton cb_isAbstract = null;
	private TabFolder datatypeStruct = null;
	private TabItem datatypeItem1 = null;
	private TabItem datatypeItem2 = null;
	private TableViewer tv_struct = null;
	private Table tb_struct;
	// the node where the struct definition is located
	private Node descParentNode = null;
	private Button btnStructureAdd;
	private Button btnStructureDelete;
	private TextViewer tv_plain;
	private Map<NodeId, String> allDatatypes = null;

	public NodeEditorDataTypePart() {
		super();
		allDatatypes = DesignerUtils.fetchAllDatatypesSortByValue();
		allDatatypes.put(NodeId.NULL, "");
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorDataTypePart.frm_mainForm.text") + getEditorInput().getName());
	}

	// TIPP: getEditorInput überschreiben und ein konkretisiertes
	// EditorInput-Objekt zurückliefern
	@Override
	public NodeEditorInput getEditorInput() {
		return super.getEditorInput();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.setFocus();
		if (this.valid()) {
			super.doSave(monitor);
			if (monitor.isCanceled()) {
				return;
			}
			Node node = getEditorInput().getNode().getNode();
			// cast the node
			UADataTypeNode dataTypeNode = (UADataTypeNode) node;
			// construct the new changed values
			dataTypeNode.setIsAbstract(this.cb_isAbstract.isChecked());
			this.frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorDataTypePart.frm_mainForm.text") + getEditorInput().getName());

			TypeTable tt = ServerInstance.getInstance().getServerInstance().getTypeTable();
			// fill structure table
			boolean validSave = true;
			if (tt.isStructure(node.getNodeId())) {
				DefinitionBean definition = (DefinitionBean) tv_struct.getInput();
				// write plain (Data Dictionary)
				validSave = doSaveStructure(definition);
			} else if (tt.isEnumeration(node.getNodeId())) {
				DefinitionBean definition = (DefinitionBean) tv_struct.getInput();
				// write plain (Data Dictionary)
				validSave = doSaveStructure(definition);
				// set variable EnumStrings
				List<LocalizedText> enumValues = new ArrayList<>();
				for (DefinitionFieldBean field : definition.getFields()) {
					LocalizedText value = new LocalizedText(field.getName(), LocalizedText.NULL_LOCALE);
					enumValues.add(value);
				}
				StructuredDataTypeManager structuredManager = new StructuredDataTypeManager(
						ServerInstance.getInstance().getServerInstance());
				VariableNode enumStrings = structuredManager.findNodeEnumerationStrings(node.getNodeId());
				enumStrings.setValue(new Variant(enumValues.toArray(new LocalizedText[0])));
			}

			if (validSave) {
				doSaveFinish();
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Node node = getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.close.info"),
				node);
	}

	protected void createExtendedSection(Composite extended) {
		GridLayout gl_extended = new GridLayout(2, false);
		gl_extended.horizontalSpacing = 10;
		extended.setLayout(gl_extended);

		GridData gd_lbl_references = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_references.widthHint = 120;

		TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
		NodeId datatypeId = this.getEditorInput().getNode().getNode().getNodeId();
		// is the datatype complex (structure)
		if (typeTable.isEnumeration(datatypeId) || typeTable.isStructure(datatypeId)) {
			datatypeStruct = new TabFolder(extended, SWT.BORDER | SWT.FULL_SELECTION);

			GridData tvgd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2);
			tvgd.heightHint = 175;
			datatypeStruct.setLayoutData(tvgd);

			// tabitem Structure
			datatypeItem1 = new TabItem(datatypeStruct, SWT.NULL);
			datatypeItem1.setText("Structure");

			tv_struct = new TableViewer(datatypeStruct, SWT.BORDER | SWT.FULL_SELECTION);
			tv_struct.setContentProvider(new ArrayContentProvider() {

				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof DefinitionBean) {
						return ((DefinitionBean) inputElement).getFields();
					}
					return super.getElements(inputElement);
				}

			});

			tb_struct = tv_struct.getTable();
			tb_struct.setHeaderVisible(true);
			tb_struct.setLinesVisible(true);

			createTableStructColumns();
			createTableStructContextMenu();

			formToolkit.paintBordersFor(tb_struct);
			datatypeItem1.setControl(tb_struct);
			datatypeItem1.setControl(tv_struct.getControl());

			// tabitem Plain
			datatypeItem2 = new TabItem(datatypeStruct, SWT.NULL);
			datatypeItem2.setText("Plain");

			this.tv_plain = new TextViewer(datatypeStruct, SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.READ_ONLY);
			datatypeItem2.setControl(tv_plain.getControl());

			Composite cmpTable = new Composite(extended, SWT.NONE);
			cmpTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			cmpTable.setLayout(new GridLayout(2, false));

			this.btnStructureAdd = new Button(cmpTable, SWT.NONE);
			btnStructureAdd.setText("Add");
			btnStructureAdd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

			this.btnStructureDelete = new Button(cmpTable, SWT.NONE);
			btnStructureDelete.setText("Delete");
			btnStructureDelete.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		}

		lbl_isAbstract = new Label(extended, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 220;
		lbl_isAbstract.setLayoutData(gd_lblNewLabel);
		lbl_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorDataTypePart.lbl_isAbstract.text"));
		cb_isAbstract = new CheckBoxButton(extended, SWT.NONE);
		GridData gd_checkBoxButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_checkBoxButton.widthHint = 250;
		cb_isAbstract.setLayoutData(gd_checkBoxButton);
		// cb_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
		// "NodeEditorDataTypePart.cb_isAbstract.text"));
		lbl_writeMask = new Label(extended, SWT.NONE);
		lbl_writeMask.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lbl_writeMask.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_writeMask.text"));
		txt_writeMask = new Text(extended, SWT.BORDER);
		txt_writeMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lbl_userWriteMask = new Label(extended, SWT.NONE);
		lbl_userWriteMask.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lbl_userWriteMask.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.lbl_userWriteMask.text"));
		txt_userWriteMask = new Text(extended, SWT.BORDER);
		txt_userWriteMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	/**
	 * Fill dynamic context menu
	 *
	 * @param contextMenu
	 */
	protected void fillContextMenu(IMenuManager contextMenu) {
		contextMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		Action actDelete = new Action("Delete") {

			@Override
			public void run() {
				deleteStructTableItem();
			}
		};

		ImageDescriptor deleteImage = CommonImagesActivator.getDefault().getImageRegistry().getDescriptor(
				new Path(CommonImagesActivator.IMG_16).append(CommonImagesActivator.DELETE).toOSString());
		actDelete.setImageDescriptor(deleteImage);

		contextMenu.add(actDelete);
	}

	protected void setInputs() {
		super.setInputs();
		Node node = this.getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.open.info"),
				node);
		this.cb_isAbstract.setChecked(((DataTypeNode) node).getIsAbstract());

		TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
		NodeId datatypeId = this.getEditorInput().getNode().getNode().getNodeId();
		// fill structure table
		if (typeTable.isEnumeration(datatypeId) || typeTable.isStructure(datatypeId)) {
			String valuePlain = "";
			DefinitionBean definition = Studio_ResourceManager.DATATYPE_DEFINITIONS
					.get(this.getEditorInput().getNode().getNode().getNodeId());
			if (definition == null) {
				definition = new DefinitionBean();
				definition.setDefinitionName(this.getEditorInput().getNode().getNode().getBrowseName().getName());
			}

			definition = definition.clone();

			if (typeTable.isStructure(datatypeId)) {
				DataDictionaryHelper ddHelper = new DataDictionaryHelper(
						ServerInstance.getInstance().getServerInstance());
				ByteString byteString = ddHelper.findByteStringFromNamespaceDataTypeId(datatypeId);
				try {
					org.w3c.dom.Node datatypeNode = ddHelper.findNodeFromByteString(byteString,
							node.getBrowseName().getName());
					if (datatypeNode != null) {
						valuePlain = ddHelper.nodeToString(datatypeNode);
					}
				} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			} else if (typeTable.isEnumeration(datatypeId)) {
				StructuredDatatypeNodeGenerator structuredManager = new StructuredDatatypeNodeGenerator(
						ServerInstance.getInstance().getServerInstance());
				ExpandedNodeId binarySchema = structuredManager.findBinarySchemaId(datatypeId.getNamespaceIndex());
				UAVariableNode schemaNode = (UAVariableNode) ServerInstance.getNode(binarySchema);
				try {
					ByteString byteString = (ByteString) schemaNode.getValue().getValue();
					if (byteString == null) {
						byteString = ByteString.EMPTY;
					}
					DataDictionaryHelper ddHelper = new DataDictionaryHelper(
							ServerInstance.getInstance().getServerInstance());
					org.w3c.dom.Node datatypeNode = ddHelper.findNodeFromByteString(byteString,
							node.getBrowseName().getName());
					if (datatypeNode != null) {
						valuePlain = ddHelper.nodeToString(datatypeNode);
					}
				} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
			tv_struct.setInput(definition);
			Document documentPlain = new Document(valuePlain);
			tv_plain.setDocument(documentPlain);
		}
	}

	@Override
	protected void preDisableWidgets() {

	}

	@Override
	protected void setHandlers() {
		super.setHandlers();
		this.controllCreationToolkit.setDirtyListener(lbl_isAbstract, this.cb_isAbstract, this);

		if (this.btnStructureAdd != null) {
			this.btnStructureAdd.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					addStructTableItem();
				}

			});
		}
		if (btnStructureDelete != null) {
			this.btnStructureDelete.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					deleteStructTableItem();
				}

			});
			this.btnStructureDelete.setEnabled(false);
		}

		if (tv_struct != null) {
			tv_struct.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (event.getSelection().isEmpty()) {
						btnStructureDelete.setEnabled(false);
					} else {
						btnStructureDelete.setEnabled(true);
					}
				}
			});
		}

	}

	boolean doSaveStructure(DefinitionBean definition) {
		Node node = getEditorInput().getNode().getNode();
		DefinitionBean store = Studio_ResourceManager.DATATYPE_DEFINITIONS.get(node.getNodeId());

		if (store != null && store.equals(definition)) {
			return true;
		}

		StructuredDataTypeManager structureManager = new StructuredDataTypeManager(
				ServerInstance.getInstance().getServerInstance());

		boolean isStructure = ServerInstance.getInstance().getServerInstance().getTypeTable()
				.isStructure(node.getNodeId());
		boolean isEnumeration = ServerInstance.getInstance().getServerInstance().getTypeTable()
				.isEnumeration(node.getNodeId());
		boolean checked = true;
		
		if (isStructure) {
			checked = structureManager.initOpcBinaryStructure((DataTypeNode) node);
		} else if (isEnumeration) {
			checked = structureManager.initOpcEnumerationStructure((DataTypeNode) node);
		}
		// structured datatype
		if (!checked) {
			MessageDialog.openInformation(getSite().getShell(), "Warning",
					"No binary schema definition for node " + getEditorInput().getName() + " has been created!");
			return false;
		}

		ByteString byteString = structureManager
				.writeOpcDataDictionary((DataTypeNode) getEditorInput().getNode().getNode(), definition);
		// refresh plain view
		DataDictionaryHelper ddHelper = new DataDictionaryHelper(ServerInstance.getInstance().getServerInstance());
		try {
			org.w3c.dom.Node datatypeNode = ddHelper.findNodeFromByteString(byteString,
					getEditorInput().getNode().getNode().getBrowseName().getName());
			String valuePlain = ddHelper.nodeToString(datatypeNode);
			Document document = new Document(valuePlain);
			tv_plain.setDocument(document);
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Cannot refresh DataDictionary plain view!");
		}

		Studio_ResourceManager.DATATYPE_DEFINITIONS.put(getEditorInput().getNode().getNode().getNodeId(),
				definition.clone());
		return true;
	}

	private void createTableStructContextMenu() {
		MenuManager contextMenu = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});

		Menu menu = contextMenu.createContextMenu(tb_struct);
		tb_struct.setMenu(menu);
	}

	private void createTableStructColumns() {
		TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
		NodeId datatypeId = this.getEditorInput().getNode().getNode().getNodeId();

		TableViewerColumn col1 = new TableViewerColumn(tv_struct, SWT.NONE);
		col1.getColumn().setWidth(200);
		col1.getColumn().setText("Name");
		col1.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DefinitionFieldBean p = (DefinitionFieldBean) element;
				return p.getName();
			}
		});
		col1.setEditingSupport(new EditingSupport(tv_struct) {

			@Override
			protected void setValue(Object element, Object value) {
				if (((DefinitionFieldBean) element).getName().equals(value)) {
					return;
				}

				((DefinitionFieldBean) element).setName((String) value);
				tv_struct.update(element, null);
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return ((DefinitionFieldBean) element).getName();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tv_struct.getTable());
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		if (typeTable.isStructure(datatypeId)) {
			TableViewerColumn colDataType = new TableViewerColumn(tv_struct, SWT.NONE);
			colDataType.getColumn().setWidth(200);
			colDataType.getColumn().setText("DataType");
			colDataType.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					DefinitionFieldBean p = (DefinitionFieldBean) element;

					NodeId datatype = p.getDatatype();
					if (!NodeId.isNull(datatype)) {
						Node node = ServerInstance.getNode(datatype);
						if (node != null) {
							return node.getBrowseName().getName();
						}
					}

					return "";
				}
			});
			colDataType.setEditingSupport(new EditingSupport(tv_struct) {

				@Override
				protected void setValue(Object element, Object value) {
					if (((DefinitionFieldBean) element).getDatatype() != null
							&& ((DefinitionFieldBean) element).getDatatype().equals(value)) {
						return;
					}

					if (((DefinitionFieldBean) element).getDatatype() == null && value == null) {
						return;
					}

					if (value instanceof NodeId && ((NodeId) value).isNullNodeId()) {
						value = null;
					}

					((DefinitionFieldBean) element).setDatatype((NodeId) value);
					tv_struct.update(element, null);
					setDirty(true);
				}

				@Override
				protected Object getValue(Object element) {
					return ((DefinitionFieldBean) element).getDatatype();
				}

				@Override
				protected CellEditor getCellEditor(Object element) {
					ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(tv_struct.getTable(), SWT.READ_ONLY);
					editor.setContentProvider(new ArrayContentProvider());
					editor.setLabelProvider(new LabelProvider() {

						@Override
						public String getText(Object element) {
							NodeId datatype = (NodeId) element;
							if (!NodeId.isNull(datatype)) {
								String name = allDatatypes.get(datatype);
								if (name != null) {
									return name;
								}
							}

							return "";
						}

					});

					List<NodeId> datatypes = new ArrayList<>();
					datatypes.addAll(allDatatypes.keySet());

					editor.setInput(datatypes.toArray(new NodeId[0]));
					return editor;
				}

				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
			});

			TableViewerColumn colValueRanks = new TableViewerColumn(tv_struct, SWT.NONE);
			colValueRanks.getColumn().setWidth(200);
			colValueRanks.getColumn().setText("ValueRank");
			colValueRanks.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					DefinitionFieldBean p = (DefinitionFieldBean) element;
					Integer valueRank = (p.getValueRank() != null) ? p.getValueRank() : -1;
					return ValueRanks.getValueRanks(valueRank).name();
//					return (p.getValueRank() != null) ? p.getValueRank().toString() : "";
				}
			});
			colValueRanks.setEditingSupport(new EditingSupport(tv_struct) {

				@Override
				protected void setValue(Object element, Object value) {
					Integer intVal = null;
					try {
						if (value instanceof ValueRanks && value != ValueRanks.Scalar) {
							intVal = ((ValueRanks) value).getValue();
						}
					} catch (NumberFormatException nfe) {
					}

					if (intVal != null && ((DefinitionFieldBean) element).getValueRank() == intVal) {
						return;
					}
					if (intVal != null) {
						((DefinitionFieldBean) element).setValueRank(intVal);
					} else {
						((DefinitionFieldBean) element).setValueRank(null);
					}
					tv_struct.update(element, null);
					setDirty(true);
				}

				@Override
				protected Object getValue(Object element) {
					Integer valueRank = ((DefinitionFieldBean) element).getValueRank();
					if (valueRank == null) {
						return ValueRanks.Scalar;
					}
					return ValueRanks.getValueRanks(valueRank);
				}

				@Override
				protected CellEditor getCellEditor(Object element) {
					ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(tv_struct.getTable(), SWT.READ_ONLY);
					editor.setContentProvider(new ArrayContentProvider());
					editor.setLabelProvider(new LabelProvider() {

						@Override
						public String getText(Object element) {
							ValueRanks valueRank = (ValueRanks) element;
							return valueRank.name();
						}

					});
					editor.setInput(ValueRanks.values());
					return editor;
				}

				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
			});
		} else if (typeTable.isEnumeration(datatypeId)) {
			TableViewerColumn col4 = new TableViewerColumn(tv_struct, SWT.NONE);
			col4.getColumn().setWidth(200);
			col4.getColumn().setText("Value");
			col4.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					DefinitionFieldBean p = (DefinitionFieldBean) element;
					return (p.getValue() != null) ? p.getValue().toString() : "";
				}
			});

//			col4.setEditingSupport(new EditingSupport(tv_struct) {
//
//				@Override
//				protected void setValue(Object element, Object value) {
//					if (((DefinitionFieldBean) element).getValue() == value) {
//						return;
//					}
//
//					((DefinitionFieldBean) element).setValue((Integer) value);
//					tv_struct.update(element, null);
//				}
//
//				@Override
//				protected Object getValue(Object element) {
//					return ((DefinitionFieldBean) element).getValue();
//				}
//
//				@Override
//				protected CellEditor getCellEditor(Object element) {
//					return new IntegerCellEditor(tv_struct.getTable());
//				}
//
//				@Override
//				protected boolean canEdit(Object element) {
//					return true;
//				}
//			});
		}
	}

	private void addStructTableItem() {
		boolean isEnumeration = ServerInstance.getInstance().getServerInstance().getTypeTable()
				.isTypeOf(getEditorInput().getNode().getNode().getNodeId(), Identifiers.Enumeration);

		DefinitionFieldBean newField = new DefinitionFieldBean();
		// new name
		newField.setName("<new>");
		// value ordinal() for enumeration
		if (isEnumeration) {
			newField.setValue(((DefinitionBean) tv_struct.getInput()).getFields().length);
		}
		((DefinitionBean) tv_struct.getInput()).addField(newField);

		tv_struct.refresh();
		setDirty(true);
	}

	private void deleteStructTableItem() {
		DefinitionFieldBean selection = (DefinitionFieldBean) tv_struct.getStructuredSelection().getFirstElement();
		if (selection == null) {
			return;
		}

		boolean isEnumeration = ServerInstance.getInstance().getServerInstance().getTypeTable()
				.isTypeOf(getEditorInput().getNode().getNode().getNodeId(), Identifiers.Enumeration);

		((DefinitionBean) tv_struct.getInput()).removeField(selection);
		// revalidate ordinal() values for enumeration
		if (isEnumeration) {
			for (int i = 0; i < ((DefinitionBean) tv_struct.getInput()).getFields().length; i++) {
				DefinitionFieldBean field = ((DefinitionBean) tv_struct.getInput()).getFields()[i];
				field.setValue(i);
			}
		}

		tv_struct.refresh();
		setDirty(true);
	}

//	private String nodeListToString(NodeList nodes) throws TransformerException {
//		DOMSource source = new DOMSource();
//		StringWriter writer = new StringWriter();
//		StreamResult result = new StreamResult(writer);
//		Transformer transformer = TransformerFactory.newInstance().newTransformer();
//		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//
//		for (int i = 0; i < nodes.getLength(); ++i) {
//			source.setNode(nodes.item(i));
//			transformer.transform(source, result);
//		}
//
//		return writer.toString();
//	}
}
