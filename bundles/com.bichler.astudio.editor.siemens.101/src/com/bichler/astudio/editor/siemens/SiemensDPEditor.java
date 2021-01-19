package com.bichler.astudio.editor.siemens;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensResources;
import com.bichler.astudio.editor.siemens.dialog.SiemensValidationDialog;
import com.bichler.astudio.editor.siemens.dnd.SiemensDPDnDViewAdapter;
import com.bichler.astudio.editor.siemens.driver.SiemensDriverUtil;
import com.bichler.astudio.editor.siemens.driver.datatype.SIEMENS_DATA_TYPE;
import com.bichler.astudio.editor.siemens.wizard.SiemensImportWizard2;
import com.bichler.astudio.editor.siemens.xml.SIEMENS_MAPPING_TYPE;
import com.bichler.astudio.editor.siemens.xml.SiemensDPEditorImporter;
import com.bichler.astudio.editor.siemens.xml.SiemensDPItem;
import com.bichler.astudio.editor.siemens.xml.SiemensDpExporter;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
// import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.AbstractOPCTriggerNodeEditPart;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.TableWidgetEnum;
import com.bichler.astudio.opcua.widget.TableWidgetUtil;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

public class SiemensDPEditor extends AbstractOPCTriggerNodeEditPart {
	public final static String ID = "com.bichler.astudio.editor.siemens.101.SiemensDPEditor"; //$NON-NLS-1$
	private ComboBoxViewerCellEditor triggerEditor;
	private UAServerApplicationInstance opcServer = null;
	private int maxNodeId = 10;
	private boolean dirty = false;
	private TableViewer tableViewer;
	/**
	 * Button validate. Validates a selected element, if no selection then this
	 * widget is disabled.
	 */
	private Button btn_validate;
	private Object currentSelection;
	private ISelectionChangedListener listener = null;

	public int getMaxNodeId() {
		return maxNodeId;
	}

	public void setMaxNodeId(int maxNodeId) {
		this.maxNodeId = maxNodeId;
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void loadPossibleTriggerNodes() {
		String triggerpath = new Path(getEditorInput().getDriverPath()).append("triggernodes.com").toOSString();
		if (getEditorInput().getFilesystem().isFile(triggerpath)) {
			try {
				InputStream input = getEditorInput().getFilesystem().readFile(triggerpath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				String line;
				String[] items;
				getPossibleTriggerNodes().clear();
				NodeToTrigger dummy = new NodeToTrigger();
				dummy.displayname = "";
				dummy.active = false;
				dummy.nodeId = null;
				getPossibleTriggerNodes().add(dummy);
				while ((line = reader.readLine()) != null) {
					try {
						items = line.split("\t");
						if (items != null && items.length >= 3) {
							// we also need to get the namespace index from
							// server
							NamespaceTable uris = this.opcServer.getServerInstance().getNamespaceUris();
							String[] nitems = items[0].split(";");
							if (nitems != null && nitems.length == 2) {
								// now create node to tigger
								int nsindex = uris.getIndex(nitems[0].replace("ns=", ""));
								if (nsindex != -1) {
									NodeToTrigger n = new NodeToTrigger();
									n.nodeId = NodeId.parseNodeId("ns=" + nsindex + ";" + nitems[1]);
									Node node = this.opcServer.getServerInstance().getAddressSpaceManager()
											.getNodeById(n.nodeId);
									n.displayname = node.getDisplayName().getText();
									if (items.length > 3)
										n.triggerName = items[3];
									getPossibleTriggerNodes().add(n);
								}
							}
						}
					} catch (Exception ex) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
					}
				}
				reader.close();
				input.close();
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	@Override
	public OPCUADPEditorInput getEditorInput() {
		return (OPCUADPEditorInput) super.getEditorInput();
	}

	public SiemensDPEditor() {
		// opcServer = new OPC_DriverServer();
	}

	@Override
	protected void createPages() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		this.createPage1(composite);
		int index = addPage(composite);
		setPageText(index, "edit");
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	public void createPage1(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL);
		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tableViewer.setLabelProvider(new SiemensModelLabelProvider());
		tableViewer.setContentProvider(new SiemensModelContentProvider());
		int operations = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		this.tableViewer.addDropSupport(operations, transferTypes, new SiemensDPDnDViewAdapter(this.tableViewer, this));
		this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setDataPointSelection(((StructuredSelection) event.getSelection()).getFirstElement());
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				try {
					// get the selected node id
					Point p = new Point(event.x, event.y);
					ViewerCell cell = tableViewer.getCell(p);
					int columnIndex = cell.getColumnIndex();
					if (columnIndex <= 2) {
						TableItem[] selection = table.getSelection();
						if (selection != null && selection.length > 0) {
							TableItem item = selection[0];
							Object data = item.getData();
							if (data != null && data instanceof SiemensEntryModelNode) {
								SiemensEntryModelNode dp = (SiemensEntryModelNode) data;
								NodeId nodeid = dp.getNId();
								OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
								dialog.setInternalServer(opcServer.getServerInstance());
								dialog.setSelectedNodeId(nodeid);
								dialog.setFormTitle(
										CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
												"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.title"));
								if (dialog.open() == Dialog.OK) {
									// fill full string to browsepath
									String browsepath = "";
									for (BrowsePathElement element : dialog.getBrowsePath()) {
										if (element.getId().equals(Identifiers.ObjectsFolder)) {
											continue;
										}
										browsepath += "//" + element.getBrowsename().getName();
									}
									setExpression(item, dialog.getSelectedNodeId(), dialog.getSelectedDisplayName(),
											browsepath);
								}
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		TableViewerColumn tvc_columnnr = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.row"),
				75);
		tvc_columnnr.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				@SuppressWarnings("unchecked")
				List<IDriverNode> input = (List<IDriverNode>) tableViewer.getInput();
				int lineNumber = input.indexOf(element);
				return "" + (lineNumber + 1);
			}
		});
		TableViewerColumn tvc_displayname = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.displayname"),
				150, TableWidgetEnum.DISPLAYNAME);
		tvc_displayname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((SiemensEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getDisplayname();
			}
		});
		TableViewerColumn tvc_nodeid = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.nodeid"),
				100, TableWidgetEnum.NODEID);
		// TableColumn trclmn_OpcUaNodeid = tvc_nodeid.getColumn();
		// trclmn_OpcUaNodeid.setWidth(87);
		// trclmn_OpcUaNodeid.setText("OPC UA Nodeid");
		tvc_nodeid.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((SiemensEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				if (((SiemensEntryModelNode) element).getNId() != null)
					return ((SiemensEntryModelNode) element).getNId().toString();
				return "";
			}
		});
		TableViewerColumn tvc_browsepath = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.browsepath"),
				150, TableWidgetEnum.BROWSEPATH);
		tvc_browsepath.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((SiemensEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getBrowsepath();
			}
		});
		TableViewerColumn tvc_mapping = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.mapping"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((SiemensEntryModelNode) element).getMapping().name();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_mapping.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getMapping().name();
			}
		});
		tvc_mapping.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((SiemensEntryModelNode) element).setMapping((SIEMENS_MAPPING_TYPE) value);
				tableViewer.update(element, null);
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).getMapping();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(tableViewer.getTable(),
						SWT.READ_ONLY);
				cellEditor.setLabelProvider(new LabelProvider());
				cellEditor.setContentProvider(new ArrayContentProvider());
				cellEditor.setInput(SIEMENS_MAPPING_TYPE.values());
				return cellEditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_varname = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.siemensvarname"),
				125, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((SiemensEntryModelNode) element).getSymbolName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TableColumn trclmn_DisplayName = tvc_varname.getColumn();
		// trclmn_DisplayName.setWidth(114);
		// trclmn_DisplayName.setText("Siemens Variable Name");
		tvc_varname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return ((SiemensEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getSymbolName();
			}
		});
		tvc_varname.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).getSymbolName();
			}

			protected void setValue(Object element, Object value) {
				if (((SiemensEntryModelNode) element).getSymbolName().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((SiemensEntryModelNode) element).setSymbolName(value.toString());
				tableViewer.update(element, null);
				setDirty(true);
			}
		});
		TableViewerColumn tvc_addresstype = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.addresstype"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((SiemensEntryModelNode) element).getAddressType().name();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TableColumn trclmn_addresstype = tvc_address.getColumn();
		// trclmn_addresstype.setWidth(77);
		// trclmn_addresstype.setText("address type");
		tvc_addresstype.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getAddressType().name();
				// element).getAddressType().name();
			}
		});
		tvc_addresstype.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(tableViewer.getTable(), SWT.READ_ONLY);
				editor.setContentProvider(new ArrayContentProvider());
				editor.setLabelProvider(new LabelProvider());
				editor.setInput(SiemensAreaCode.values());
				// editor.setInput(AREA_TYPE.values());
				return editor;
			}

			protected Object getValue(Object element) {
				// SiemensAreaCode data = SiemensAreaCode.UNKNOWN;
				return ((SiemensEntryModelNode) element).getAddressType();
				//
				// if (element instanceof SiemensEntryModelNode) {
				// try {
				// data = ((SiemensEntryModelNode) element)
				// .getAddressType();
				// // .valueOf(((ModbusDP) element).getType());
				// } catch (Exception ex) {
				// data = SiemensAreaCode.UNKNOWN;
				// }
				// }
				// return data;
				// return ((OmronTableItem)element).tagType;
			}

			protected void setValue(Object element, Object value) {
				if (((SiemensEntryModelNode) element).getAddressType() == ((SiemensAreaCode) value)) {
					return;
				}
				((SiemensEntryModelNode) element).setAddressType((SiemensAreaCode) value);
				setDirty(true);
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_address = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.address"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((SiemensEntryModelNode) element).getAddress();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TableColumn trclmnNewColumn_1 = tvc_address.getColumn();
		// trclmnNewColumn_1.setWidth(52);
		// trclmnNewColumn_1.setText("address");
		tvc_address.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getAddress() + "";
			}
		});
		tvc_address.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).getAddress() + "";
			}

			protected void setValue(Object element, Object value) {
				if ((((SiemensEntryModelNode) element).getAddress() + "").compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				try {
					((SiemensEntryModelNode) element).setAddress(value.toString());
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
				tableViewer.update(element, null);
				setDirty(true);
			}
		});
		TableViewerColumn tvc_index = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.index"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((SiemensEntryModelNode) element).getIndex();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Float) o1).compareTo((Float) o2);
					}
				});
		// TableColumn tblclmnIndex = tvc_index.getColumn();
		// tblclmnIndex.setWidth(52);
		// tblclmnIndex.setText("index");
		tvc_index.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// float nIndex = ((SiemensEntryModelNode)
				// element).getNEW_INDEX();
				String sIndex = ((SiemensEntryModelNode) element).getIndex() + "";
				// float cIndex =
				// ((SiemensEntryModelNode)element).calculateIndex();
				// float cIndex =
				// ((SiemensEntryModelNode)element).getStartIndex();
				return sIndex;
			}
		});
		tvc_index.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).getIndex() + "";
			}

			protected void setValue(Object element, Object value) {
				if ((((SiemensEntryModelNode) element).getIndex() + "").compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				try {
					((SiemensEntryModelNode) element).setIndex((Float.parseFloat(value.toString())));
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
				tableViewer.update(element, null);
				setDirty(true);
			}
		});
		TableViewerColumn tvc_datatype = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.datatype"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((SiemensEntryModelNode) element).getDataType();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		// TableColumn trclmnNewColumn_2 = tvc_datatype.getColumn();
		// trclmnNewColumn_2.setWidth(66);
		// trclmnNewColumn_2.setText("data type");
		final ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(tableViewer.getTable());
		editor.setContentProvider(new ArrayContentProvider());
		editor.setLabelProvider(new LabelProvider());
		List<String> v = new ArrayList<String>();
		for (SIEMENS_DATA_TYPE d : SIEMENS_DATA_TYPE.values()) {
			v.add(d.name());
		}
		editor.setInput(v.toArray(new String[v.size()]));
		tvc_datatype.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (!(element instanceof SiemensEntryModelNode)) {
					return "";
				}
				if (((SiemensEntryModelNode) element).getDataType() == null) {
					return "";
				}
				return ((SiemensEntryModelNode) element).getDataType();
			}
		});
		tvc_datatype.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				if (value == null) {
					String el = ((CCombo) editor.getControl()).getText();
					if (el.isEmpty()) {
						return;
					}
					boolean found = false;
					for (String item : ((CCombo) editor.getControl()).getItems()) {
						if (el.compareTo(item) == 0) {
							found = true;
							break;
						}
					}
					if (!found) {
						Object i = editor.getViewer().getInput();
						if (i instanceof String[]) {
							List<String> l = new ArrayList<String>();
							for (String s : (String[]) i) {
								l.add(s);
							}
							l.add(el.toUpperCase().replace(" ", ""));
							editor.getViewer().setInput(l.toArray(new String[l.size()]));
							editor.getViewer().refresh();
						}
					}
					((SiemensEntryModelNode) element).setDataType(el.toUpperCase().replace(" ", ""));
					tableViewer.refresh();
					setDirty(true);
					return;
				}
				// TODO Auto-generated method stub
				// value.toString();
				((SiemensEntryModelNode) element).setDataType(value.toString());
				tableViewer.refresh();
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				if (element == null || ((SiemensEntryModelNode) element).getDataType() == null) {
					return "";
				}
				// TODO Auto-generated method stub
				return ((SiemensEntryModelNode) element).getDataType();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.active"),
				35);
		// TableColumn trclmn_active = tvc_active.getColumn();
		// trclmn_active.setWidth(35);
		// trclmn_active.setText("active");
		tvc_active.setLabelProvider(new ColumnLabelProvider() {
			// @Override
			// public Color getBackground(Object element) {
			// return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			// }
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (((SiemensEntryModelNode) element).isActive()) {
					return SiemensSharedImages.getImage(SiemensSharedImages.ICON_CHECKED_1);
				} else {
					return SiemensSharedImages.getImage(SiemensSharedImages.ICON_CHECKED_0);
				}
			}
		});
		tvc_active.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).isActive();
			}

			protected void setValue(Object element, Object value) {
				((SiemensEntryModelNode) element).setActive(!((SiemensEntryModelNode) element).isActive());
				// ((OmronTableItem)element).active = value.toString();
				setDirty(true);
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_cycletime = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.cycletime"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((SiemensEntryModelNode) element).getCycletime();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Long) o1).compareTo((Long) o2);
					}
				});
		// TableColumn trclmnCycletime = tvc_cycletime.getColumn();
		// trclmnCycletime.setWidth(55);
		// trclmnCycletime.setText("cycletime");
		tvc_cycletime.setLabelProvider(new ColumnLabelProvider() {
			// @Override
			// public Color getBackground(Object element) {
			// return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			// }
			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getCycletime() + "";
			}
		});
		tvc_cycletime.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new IntegerCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).getCycletime();
			}

			protected void setValue(Object element, Object value) {
				try {
					int cycletime = Integer.parseInt(value.toString());
					if (((SiemensEntryModelNode) element).getCycletime() == cycletime) {
						return;
					}
					((SiemensEntryModelNode) element).setCycletime(cycletime);
					setDirty(true);
				} catch (NumberFormatException ex) {
					// TODO parse number format exception
				}
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_trigger = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.trigger"),
				80);
		// TableColumn trclmn_historical = tvc_trigger.getColumn();
		// trclmn_historical.setWidth(90);
		// trclmn_historical.setText("trigger");
		tvc_trigger.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((SiemensEntryModelNode) element).getTrigger() != null) {
					// check if nodeid is null for dummy trigger event,
					// to show an empty line
					if (((SiemensEntryModelNode) element).getTrigger().nodeId == null)
						return "";
					return ((SiemensEntryModelNode) element).getTrigger().triggerName + " "
							+ ((SiemensEntryModelNode) element).getTrigger().nodeId.toString() + " "
							+ ((SiemensEntryModelNode) element).getTrigger().displayname;
				}
				return "";
			}

			@Override
			public Color getForeground(Object element) {
				NodeToTrigger obj = ((SiemensEntryModelNode) element).getTrigger();
				boolean good = isTriggerNodeValid(obj);
				if (!good) {
					Display display = Display.getCurrent();
					Color red = display.getSystemColor(SWT.COLOR_RED);
					return red;
				}
				return null;
			}
		});
		// create a new Trigger editor viewer
		triggerEditor = new ComboBoxViewerCellEditor(tableViewer.getTable(), SWT.READ_ONLY);
		triggerEditor.setContentProvider(new ArrayContentProvider());
		final ControlDecoration controlDecoration = new ControlDecoration(triggerEditor.getControl(),
				SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.trigger.description"));
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		triggerEditor.setValidator(new ICellEditorValidator() {
			@Override
			public String isValid(Object element) {
				if (!(element instanceof NodeToTrigger)) {
					return null;
				}
				boolean good = isTriggerNodeValid((NodeToTrigger) element);
				if (good) {
					controlDecoration.hide();
					return null;
				} else {
					controlDecoration.show();
					return CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.trigger.description");
				}
			}
		});
		triggerEditor.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (((NodeToTrigger) element) != null) {
					// check if nodeid is null for dummy trigger event,
					// to show an empty line
					if (((NodeToTrigger) element).nodeId == null)
						return "";
					return ((NodeToTrigger) element).triggerName + " " + ((NodeToTrigger) element).nodeId.toString()
							+ " " + ((NodeToTrigger) element).displayname;
				}
				return super.getText(element);
			}
		});
		tvc_trigger.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				triggerEditor.setInput(getPossibleTriggerNodes());
				return triggerEditor;
			}

			protected Object getValue(Object element) {
				if (((SiemensEntryModelNode) element).getTrigger() != null) {
					return ((SiemensEntryModelNode) element).getTrigger();
				}
				return "";
			}

			protected void setValue(Object element, Object value) {
				if (value == null || !(value instanceof NodeToTrigger))
					return;
				((SiemensEntryModelNode) element).setTrigger((NodeToTrigger) value);
				setDirty(true);
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_simulate = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.simulate"),
				80);
		// TableColumn trclmn_active = tvc_active.getColumn();
		// trclmn_active.setWidth(35);
		// trclmn_active.setText("active");
		tvc_simulate.setLabelProvider(new ColumnLabelProvider() {
			// @Override
			// public Color getBackground(Object element) {
			// return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			// }
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (((SiemensEntryModelNode) element).isSimulate()) {
					return SiemensSharedImages.getImage(SiemensSharedImages.ICON_CHECKED_1);
				} else {
					return SiemensSharedImages.getImage(SiemensSharedImages.ICON_CHECKED_0);
				}
			}
		});
		tvc_simulate.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).isSimulate();
			}

			protected void setValue(Object element, Object value) {
				((SiemensEntryModelNode) element).setSimulate(!((SiemensEntryModelNode) element).isSimulate());
				// ((OmronTableItem)element).active = value.toString();
				setDirty(true);
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_description = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.dialog.node.description"),
				250);
		// TableColumn trclmnNewColumn_3 = tvc_description.getColumn();
		// trclmnNewColumn_3.setWidth(248);
		// trclmnNewColumn_3.setText("description");
		tvc_description.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((SiemensEntryModelNode) element).getDescription();
			}
		});
		tvc_description.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((SiemensEntryModelNode) element).getDescription();
			}

			protected void setValue(Object element, Object value) {
				if (((SiemensEntryModelNode) element).getDescription().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((SiemensEntryModelNode) element).setDescription(value.toString());
				tableViewer.refresh(element);
				setDirty(true);
			}
		});
		Composite menu = new Composite(composite, SWT.BORDER);
		menu.setLayout(new GridLayout(6, false));
		Button btn_import2 = new Button(menu, SWT.NONE);
		GridData gd_btn_import2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_import2.widthHint = 60;
		gd_btn_import2.heightHint = 60;
		btn_import2.setLayoutData(gd_btn_import2);
		btn_import2.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.import.dp"));
		btn_import2.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_IMPORT));
		// SiemensSharedImages.getImage(SiemensSharedImages.ICON_IMPORT));
		btn_import2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SiemensDBResourceManager structManager = SiemensResources.getInstance()
						.getDBResourceManager(getEditorInput().getDriverName());
				SiemensImportWizard2 wizard = new SiemensImportWizard2(structManager);
				wizard.setFilesystem(getEditorInput().getFilesystem());
				WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
				if (WizardDialog.OK == dialog.open()) {
					setDirty(true);
				}
			}
		});
		this.btn_validate = new Button(menu, SWT.NONE);
		btn_validate.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.import.validate"));
		GridData gd_btn_validate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_validate.widthHint = 60;
		gd_btn_validate.heightHint = 60;
		btn_validate.setLayoutData(gd_btn_validate);
		btn_validate.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object input = tableViewer.getInput();
				if (input == null) {
					MessageDialog.openInformation(getSite().getShell(),
							CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.editor.siemens.editor.dp.import.validierung"),
							CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.editor.siemens.editor.dp.import.validierung.description"));
					return;
				}
				SiemensDBResourceManager structManager = SiemensResources.getInstance()
						.getDBResourceManager(getEditorInput().getDriverName());
				SiemensValidationDialog dialog = new SiemensValidationDialog(getSite().getShell(), structManager);
				dialog.setFilesystem(getEditorInput().getFilesystem());
				Map<String, SiemensEntryModelNode> inputModel = new HashMap<>();
				for (SiemensEntryModelNode child : (List<SiemensEntryModelNode>) input) {
					inputModel.put(child.getSymbolName(), child);
				}
				dialog.setModel(new ArrayList((List<SiemensEntryModelNode>) input), inputModel);
				if (Dialog.OK == dialog.open()) {
					setDirty(true);
				}
			}
		});
		btn_validate.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_VALIDATEDP));
		// SiemensSharedImages.getImage(SiemensSharedImages.ICON_VALIDATEDP));
		Button btn_add = new Button(menu, SWT.NONE);
		btn_add.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.tooltip.adddp"));
		btn_add.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				/** first get selected index of table */
				// StructuredSelection sel = (StructuredSelection) tableViewer
				// .getSelection();
				// Object[] selections = sel.toArray();
				if (tableViewer.getInput() == null) {
					tableViewer.setInput(new ArrayList<SiemensEntryModelNode>());
				}
				List<SiemensEntryModelNode> input = (List<SiemensEntryModelNode>) tableViewer.getInput();
				int index = ((List<SiemensEntryModelNode>) tableViewer.getInput()).size();
				float nIndex = 0;
				if (index > 0) {
					// last
					SiemensEntryModelNode neighbor = input.get(index - 1);
					nIndex = neighbor.calculateEndIndex();
				}
				// maxNodeId++;
				SiemensEntryModelNode newitem = new SiemensEntryModelNode();
				newitem.setSymbolName("unknown");
				newitem.setActive(true);
				newitem.setCycletime(1000);
				newitem.setAddressType(SiemensAreaCode.DB);
				newitem.setAddress("0");
				newitem.setIndex(nIndex);
				newitem.setDescription(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.siemens.editor.dp.insertcomment"));
				input.add(newitem);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(newitem), false);
				setDirty(true);
			}
		});
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btn_add.widthHint = 60;
		gd_btn_add.heightHint = 60;
		btn_add.setLayoutData(gd_btn_add);
		btn_add.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_ADD));
		Button btn_delete = new Button(menu, SWT.NONE);
		btn_delete.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.tooltip.deletedp"));
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
				Object[] selections = sel.toArray();
				if (selections != null) {
					for (Object o : selections) {
						((List<SiemensEntryModelNode>) tableViewer.getInput()).remove(o);
					}
					tableViewer.remove(selections);
					tableViewer.refresh();
					setDirty(true);
				}
			}
		});
		GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btn_delete.heightHint = 60;
		gd_btn_delete.widthHint = 60;
		btn_delete.setLayoutData(gd_btn_delete);
		btn_delete.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_DELETE));
		Button btn_DPTest = new Button(menu, SWT.NONE);
		btn_DPTest.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.tooltip.testdp"));
		btn_DPTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
				Object[] selections = sel.toArray();
				if (selections != null && selections.length > 0) {
					// first read device config
					SiemensDriverEditor deditor = new SiemensDriverEditor();
					deditor.initDevice(getEditorInput().getFilesystem(), getEditorInput().getDriverConfigFile());
					SiemensTestDialog tdialog = new SiemensTestDialog(tableViewer.getControl().getShell());
					tdialog.setDPs(selections, deditor.getDevice());
					tdialog.create();
					tdialog.open();
				} else {
					MessageDialog.openWarning(tableViewer.getControl().getShell(), "Siemens Test Panel",
							"Kein knoten zum Testen ausgewählt");
				}
			}
		});
		GridData gd_btn_DPTest = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btn_DPTest.heightHint = 60;
		gd_btn_DPTest.widthHint = 60;
		btn_DPTest.setLayoutData(gd_btn_DPTest);
		btn_DPTest.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_TEST));
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(new Point(200, 200));
		this.loadPossibleTriggerNodes();
		this.fillTree(editor);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		getSite().setSelectionProvider(this);
	}

	@Override
	public void setFocus() {
		// Set the focus
		super.setFocus();
		this.tableViewer.getTable().setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		@SuppressWarnings("unchecked")
		List<SiemensEntryModelNode> items = (List<SiemensEntryModelNode>) tableViewer.getInput();
		List<IDriverNode> items2export = new ArrayList<>();
		items2export.addAll(items);
		// StringBuffer buffer = new StringBuffer();
		String configPath = getEditorInput().getDPConfigFile();
		SiemensDpExporter exporter = new SiemensDpExporter(getEditorInput().getFilesystem(), configPath);
		exporter.build(items2export, opcServer.getServerInstance().getNamespaceUris());
		this.setDirty(false);
		OPCUAUtil.validateOPCUADriver(getEditorInput().getFilesystem(), getEditorInput().getNode());
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					monitor.beginTask(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.siemens.editor.dp.loaddp"), IProgressMonitor.UNKNOWN);
					monitor.subTask(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.siemens.editor.dp.initialize"));
					try {
						setSite(site);
						setInput(input);
						setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - "
								+ CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.editor.siemens.editor.dp.dp"));
						String serverpath = new Path(getEditorInput().getFilesystem().getRootPath()).toOSString();
						try {
							URL modelParent = new Path(serverpath).append("informationmodel").toFile().toURI().toURL();
							String serverConfigPath = new Path(serverpath).append("serverconfig")
									.append("server.config.xml").toOSString();
							if (getEditorInput().getFilesystem().isFile(serverConfigPath)) {
								opcServer = Studio_ResourceManager.getOrNewOPCUAServer(getEditorInput().getServerName(),
										serverConfigPath, serverpath, serverpath + "/localization", modelParent);
							}
						} catch (IOException e1) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
						} catch (ExecutionException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
						}
						getEditorInput()
								.setToolTipText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.editor.siemens.editor.dp.tooltip"));
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public String removeEscapes(String builder) {
		return builder.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
	}

	/**
	 * class to validate integer input cells.
	 * 
	 * @author applemc207da
	 * 
	 */
	public class IntegerCellEditor extends TextCellEditor {
		public IntegerCellEditor(Composite composite) {
			super(composite);
			setValidator(new ICellEditorValidator() {
				public String isValid(Object object) {
					if (object instanceof Long) {
						return null;
					} else {
						String string = (String) object;
						try {
							Integer.parseInt(string);
							return null;
						} catch (NumberFormatException exception) {
							return exception.getMessage();
						}
					}
				}
			});
		}

		@Override
		public Object doGetValue() {
			return Integer.parseInt((String) super.doGetValue());
		}

		@Override
		public void doSetValue(Object value) {
			super.doSetValue(value.toString());
		}
	}

	@Override
	public void fillTriggerNodes(List<NodeToTrigger> possibleTriggerNodes) {
		setPossibleTriggerNodes(possibleTriggerNodes);
		OPCUAUtil.validateOPCUADriver(getEditorInput().getFilesystem(), getEditorInput().getNode());
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public ISelection getSelection() {
		if (this.currentSelection != null) {
			return new StructuredSelection(this.currentSelection);
		}
		return StructuredSelection.EMPTY;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		this.listener = null;
	}

	@Override
	public void setSelection(ISelection selection) {
	}

	protected void selectionChanged() {
		if (listener == null) {
			return;
		}
		listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	@Override
	public void setDataPointSelection(Object element) {
		this.currentSelection = element;
		selectionChanged();
	}

	private void fillTree(ComboBoxViewerCellEditor editor) {
		/** open file to import */
		String dpsPath = getEditorInput().getDPConfigFile();
		List<SiemensDPItem> dps = new ArrayList<SiemensDPItem>();
		List<SiemensEntryModelNode> nodes = new ArrayList<SiemensEntryModelNode>();
		if (getEditorInput().getFilesystem().isFile(dpsPath)) {
			// InputStreamReader isr = null;
			InputStream input = null;
			try {
				input = getEditorInput().getFilesystem().readFile(dpsPath);
				// isr = new InputStreamReader(input, "UTF-8");
				// InputSource is = new InputSource(isr);
				NamespaceTable uris = this.opcServer.getServerInstance().getNamespaceUris();
				SiemensDPEditorImporter importer = new SiemensDPEditorImporter();
				dps = importer.loadDPs(input, uris);
				SiemensEntryModelNode smn = null;
				String browsepath = "";
				Deque<BrowsePathElement> browsepathelems = null;
				for (SiemensDPItem dp : dps) {
					browsepath = "";
					smn = SiemensEntryModelNode.loadFromDP(dp);
					// load display name for each node
					// create browsepath from address space
					browsepathelems = OPCUABrowseUtils.getFullBrowsePath(dp.getNodeId(), opcServer.getServerInstance(),
							Identifiers.ObjectsFolder);
					for (BrowsePathElement element : browsepathelems) {
						if (element.getId().equals(Identifiers.ObjectsFolder)) {
							continue;
						}
						browsepath += "//" + element.getBrowsename().getName();
					}
					smn.setBrowsepath(browsepath);
					// now try to find the correct trigger
					if (dp.getTriggerNode() != null) {
						for (NodeToTrigger trig : getPossibleTriggerNodes()) {
							if (trig == null)
								continue;
							if (trig.triggerName != null && trig.triggerName.equals(dp.getTriggerNode())) {
								smn.setTrigger(trig);
								break;
							}
						}
					}
					nodes.add(smn);
				}
				Node node = null;
				// now set displayname for each model node
				for (SiemensEntryModelNode n : nodes) {
					node = opcServer.getServerInstance().getAddressSpaceManager().getNodeById(n.getNId());
					if (node != null) {
						n.setDisplayname(node.getDisplayName().getText());
					}
				}
				List<String> inputs = new ArrayList<String>();
				Object inputItems = editor.getViewer().getInput();
				if (inputItems instanceof String[]) {
					for (String s : (String[]) inputItems) {
						inputs.add(s);
					}
				}
				for (SiemensEntryModelNode node4datatype : nodes) {
					String dt = node4datatype.getDataType();
					if (!inputs.contains(dt)) {
						inputs.add(dt);
					}
				}
				editor.getViewer().setInput(inputs.toArray(new String[inputs.size()]));
				editor.getViewer().refresh();
				// create a S
				tableViewer.setInput(nodes);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void setExpression(TableItem item, NodeId nodeId, String name, String browsepath) {
		SiemensEntryModelNode dp = (SiemensEntryModelNode) item.getData();
		dp.setNodeId(nodeId);
		dp.setDisplayname(name);
		dp.setBrowsepath(browsepath);
		tableViewer.refresh();
		setDirty(true);
	}

	@Override
	public void refreshDatapoints() {
		// TODO Auto-generated method stub
	}

	@Override
	public Control getDPControl() {
		return this.tableViewer.getControl();
	}

	@Override
	public ISelectionProvider getDPViewer() {
		return this;
	}

	@Override
	public void onFocusRemoteView() {
		SiemensDBResourceManager structManager = SiemensResources.getInstance()
				.getDBResourceManager(getEditorInput().getDriverName());
		SiemensDriverUtil.openDriverView(getEditorInput().getFilesystem(), getEditorInput().getDriverConfigFile(),
				structManager);
	}
}
