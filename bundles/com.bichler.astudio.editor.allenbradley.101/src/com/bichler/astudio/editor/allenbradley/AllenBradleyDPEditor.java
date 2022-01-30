package com.bichler.astudio.editor.allenbradley;

import java.io.BufferedReader;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;
import org.xml.sax.InputSource;

import com.bichler.astudio.editor.allenbradley.datatype.ALLENBRADLEY_DATA_TYPE;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyResources;
import com.bichler.astudio.editor.allenbradley.dialog.AllenBradleyValidationDialog;
import com.bichler.astudio.editor.allenbradley.dnd.AllenBradleyDPDnDViewAdapter;
import com.bichler.astudio.editor.allenbradley.driver.AllenBradleyDriverUtil;
import com.bichler.astudio.editor.allenbradley.wizard.AllenBradleyImportWizard;
import com.bichler.astudio.editor.allenbradley.xml.ALLENBRADLEY_MAPPING_TYPE;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPEditorImporter;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPItem;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDpExporter;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.AbstractOPCTriggerNodeEditPart;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.TableWidgetEnum;
import com.bichler.astudio.opcua.widget.TableWidgetUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

public class AllenBradleyDPEditor extends AbstractOPCTriggerNodeEditPart {
	public static final String ID = "com.bichler.astudio.editor.allenbradley.101.AllenBradleyDPEditor"; //$NON-NLS-1$
	private static Map<String, Integer> dataTypeSizes = new HashMap<>();
	private UAServerApplicationInstance opcServer = null;
	private TableViewer tableViewer;
	/**
	 * Button validate. Validates a selected element, if no selection then this
	 * widget is disabled.
	 */
	private Button btnValidate;
	private Object currentSelection;
	private ISelectionChangedListener listener;
	private Button btnDelete;
	private Button btnAdd;
	private Button btnImport;
	private ComboBoxViewerCellEditor editorComboBox;
	private boolean dirty = false;
	private int maxNodeId = 10;
	private ComboBoxViewerCellEditor triggerEditor;
	private TableViewerColumn tvcTrigger;
	private TableViewer tableMonitoringViewer;

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
			BufferedReader reader = null;
			try {
				InputStream input = getEditorInput().getFilesystem().readFile(triggerpath);
				reader = new BufferedReader(new InputStreamReader(input));
				String line = "";
				String[] items = null;
				getPossibleTriggerNodes().clear();
				NodeToTrigger dummy = new NodeToTrigger();
				dummy.displayname = "";
				dummy.active = true;
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
									n.active = Boolean.parseBoolean(items[1]);
									getPossibleTriggerNodes().add(n);
								}
							}
						}
					} catch (Exception ex) {
					}
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				}
			}
		}
	}

	@Override
	public OPCUADPEditorInput getEditorInput() {
		return (OPCUADPEditorInput) super.getEditorInput();
	}

	public AllenBradleyDPEditor() {
		dataTypeSizes.put("BOOL", 1);
		dataTypeSizes.put("BYTE", 10);
		dataTypeSizes.put("WORD", 20);
		dataTypeSizes.put("DWORD", 40);
		dataTypeSizes.put("CHAR", 10);
		dataTypeSizes.put("INT", 20);
		dataTypeSizes.put("DINT", 40);
		dataTypeSizes.put("REAL", 40);
		dataTypeSizes.put("S5TIME", 20);
		dataTypeSizes.put("TIME", 40);
		dataTypeSizes.put("TIMEOFDAY", 40);
		dataTypeSizes.put("DATE", 20);
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
		tableViewer.setLabelProvider(new AllenBradleyModelLabelProvider());
		tableViewer.setContentProvider(new AllenBradleyModelContentProvider());
		int operations = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		this.tableViewer.addDropSupport(operations, transferTypes,
				new AllenBradleyDPDnDViewAdapter(this.tableViewer, this));
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
							if (data != null && data instanceof AllenBradleyEntryModelNode) {
								AllenBradleyEntryModelNode dp = (AllenBradleyEntryModelNode) data;
								NodeId nodeid = dp.getNodeId();
								OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
								dialog.setInternalServer(opcServer.getServerInstance());
								dialog.setSelectedNodeId(nodeid);
								dialog.setFormTitle(
										CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
												"com.bichler.astudio.editor.allenbradley.editor.dp.dialog.opc"));
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
		TableViewerColumn tvc_row = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.row"),
				75);
		tvc_row.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				@SuppressWarnings("unchecked")
				List<AllenBradleyEntryModelNode> input = (List<AllenBradleyEntryModelNode>) tableViewer.getInput();
				int lineNumber = input.indexOf(element);
				return "" + (lineNumber + 1);
			}
		});
		TableViewerColumn tvc_displayname = TableWidgetUtil
				.createTableColumn(tableViewer,
						CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.allenbradley.editor.dp.name"),
						125, TableWidgetEnum.DISPLAYNAME);
		// TableColumn trclmn_OPCUADisplayName = tvc_displayname
		// .getColumn();
		// trclmn_OPCUADisplayName.setWidth(133);
		// trclmn_OPCUADisplayName.setText("OPC UA Display Name");
		tvc_displayname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getDisplayname();
			}
		});
		TableViewerColumn tvc_nodeid = TableWidgetUtil
				.createTableColumn(tableViewer,
						CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.allenbradley.editor.dp.nodeid"),
						90, TableWidgetEnum.NODEID);
		tvc_nodeid.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				if (((AllenBradleyEntryModelNode) element).getNodeId() != null)
					return ((AllenBradleyEntryModelNode) element).getNodeId().toString();
				return "";
			}
		});
		TableViewerColumn tvc_browsepath = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.browsepath"),
				125, TableWidgetEnum.BROWSEPATH);
		tvc_browsepath.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getBrowsepath();
			}
		});
		TableViewerColumn tvc_tag = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.tagname"),
				125, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getSymbolName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_tag.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getSymbolName();
			}
		});
		tvc_tag.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AllenBradleyEntryModelNode) element).getSymbolName();
			}

			protected void setValue(Object element, Object value) {
				if (((AllenBradleyEntryModelNode) element).getSymbolName().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((AllenBradleyEntryModelNode) element).setSymbolName(value.toString());
				tableViewer.update(element, null);
				setDirty(true);
			}
		});
		TableViewerColumn tvc_mapping = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.mapping"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getMapping().name();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_mapping.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getMapping().name();
			}
		});
		tvc_mapping.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AllenBradleyEntryModelNode) element).setMapping((ALLENBRADLEY_MAPPING_TYPE) value);
				tableViewer.update(element, null);
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return ((AllenBradleyEntryModelNode) element).getMapping();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(tableViewer.getTable(),
						SWT.READ_ONLY);
				cellEditor.setLabelProvider(new LabelProvider());
				cellEditor.setContentProvider(new ArrayContentProvider());
				cellEditor.setInput(ALLENBRADLEY_MAPPING_TYPE.values());
				return cellEditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_datatype = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.datatype"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getDataType();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		this.editorComboBox = new ComboBoxViewerCellEditor(tableViewer.getTable());
		editorComboBox.setContentProvider(new ArrayContentProvider());
		editorComboBox.setLabelProvider(new LabelProvider());
		List<String> v = new ArrayList<>();
		for (ALLENBRADLEY_DATA_TYPE d : ALLENBRADLEY_DATA_TYPE.values()) {
			v.add(d.name());
		}
		editorComboBox.setInput(v.toArray(new String[v.size()]));
		tvc_datatype.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (!(element instanceof AllenBradleyEntryModelNode)) {
					return "";
				}
				if (((AllenBradleyEntryModelNode) element).getDataType() == null) {
					return "";
				}
				return ((AllenBradleyEntryModelNode) element).getDataType();
			}
		});
		tvc_datatype.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				if (value == null) {
					String el = ((CCombo) editorComboBox.getControl()).getText();
					if (el.isEmpty()) {
						return;
					}
					boolean found = false;
					for (String item : ((CCombo) editorComboBox.getControl()).getItems()) {
						if (el.compareTo(item) == 0) {
							found = true;
							break;
						}
					}
					if (!found) {
						Object i = editorComboBox.getViewer().getInput();
						if (i instanceof String[]) {
							List<String> l = new ArrayList<String>();
							for (String s : (String[]) i) {
								l.add(s);
							}
							l.add(el.toUpperCase().replace(" ", ""));
							editorComboBox.getViewer().setInput(l.toArray(new String[l.size()]));
							editorComboBox.getViewer().refresh();
						}
					}
					((AllenBradleyEntryModelNode) element).setDataType(el.toUpperCase().replace(" ", ""));
					tableViewer.refresh();
					setDirty(true);
					return;
				}
				((AllenBradleyEntryModelNode) element).setDataType(value.toString());
				tableViewer.refresh();
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				if (element == null || ((AllenBradleyEntryModelNode) element).getDataType() == null) {
					return "";
				}
				return ((AllenBradleyEntryModelNode) element).getDataType();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return editorComboBox;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.active"),
				35);
		tvc_active.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (((AllenBradleyEntryModelNode) element).isActive()) {
					return AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_CHECKED_1);
				} else {
					return AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_CHECKED_0);
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
				return ((AllenBradleyEntryModelNode) element).isActive();
			}

			protected void setValue(Object element, Object value) {
				((AllenBradleyEntryModelNode) element).setActive(!((AllenBradleyEntryModelNode) element).isActive());
				setDirty(true);
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_cycletime = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.cycletime"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getCycletime();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Long) o1).compareTo((Long) o2);
					}
				});
		tvc_cycletime.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getCycletime() + "";
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
				return ((AllenBradleyEntryModelNode) element).getCycletime();
			}

			protected void setValue(Object element, Object value) {
				try {
					int cycletime = Integer.parseInt(value.toString());
					if (((AllenBradleyEntryModelNode) element).getCycletime() == cycletime) {
						return;
					}
					((AllenBradleyEntryModelNode) element).setCycletime(cycletime);
					setDirty(true);
				} catch (NumberFormatException ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
				}
				tableViewer.update(element, null);
			}
		});
		tvcTrigger = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.trigger"),
				100);
		// TableColumn trclmn_historical = tvc_trigger.getColumn();
		// trclmn_historical.setWidth(90);
		// trclmn_historical.setText("trigger");
		tvcTrigger.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((AllenBradleyEntryModelNode) element).getTrigger() != null) {
					// check if nodeid is null for dummy trigger event,
					// to show an empty line
					if (((AllenBradleyEntryModelNode) element).getTrigger().nodeId == null)
						return "";
					return ((AllenBradleyEntryModelNode) element).getTrigger().triggerName + " "
							+ ((AllenBradleyEntryModelNode) element).getTrigger().nodeId.toString() + " "
							+ ((AllenBradleyEntryModelNode) element).getTrigger().displayname;
				}
				return "";
			}

			@Override
			public Color getForeground(Object element) {
				NodeToTrigger obj = ((AllenBradleyEntryModelNode) element).getTrigger();
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
		controlDecoration.setDescriptionText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.dp.trigger.description"));
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
					return CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.allenbradley.editor.dp.trigger.description");
				}
			}
		});
		triggerEditor.setLabelProvider(new LabelProvider() {
			@Override
			public Image getImage(Object element) {
				NodeToTrigger obj = ((AllenBradleyEntryModelNode) element).getTrigger();
				boolean good = isTriggerNodeValid(obj);
				if (!good) {
					return FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
							.getImage();
				}
				return null;
			}

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
		tvcTrigger.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				List<NodeToTrigger> trigg = new ArrayList<NodeToTrigger>();
				// get only active triggers
				for (NodeToTrigger node : getPossibleTriggerNodes()) {
					trigg.add(node);
				}
				triggerEditor.setInput(trigg);
				return triggerEditor;
			}

			protected Object getValue(Object element) {
				if (((AllenBradleyEntryModelNode) element).getTrigger() != null) {
					return ((AllenBradleyEntryModelNode) element).getTrigger();
				}
				return "";
			}

			protected void setValue(Object element, Object value) {
				if (value == null || !(value instanceof NodeToTrigger))
					return;
				((AllenBradleyEntryModelNode) element).setTrigger((NodeToTrigger) value);
				tableViewer.update(element, null);
				setDirty(true);
			}
		});
		TableViewerColumn tvc_description = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.description"),
				250);
		tvc_description.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getDesc();
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
				return ((AllenBradleyEntryModelNode) element).getDescription();
			}

			protected void setValue(Object element, Object value) {
				if (((AllenBradleyEntryModelNode) element).getDescription().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((AllenBradleyEntryModelNode) element).setDescription(value.toString());
				tableViewer.refresh(element);
				setDirty(true);
			}
		});
		Composite menu = new Composite(composite, SWT.BORDER);
		menu.setLayout(new GridLayout(4, false));
		this.btnImport = new Button(menu, SWT.NONE);
		btnImport.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.dp.import.tooltip"));
		GridData gd_btn_import = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_import.widthHint = 60;
		gd_btn_import.heightHint = 60;
		btnImport.setLayoutData(gd_btn_import);
		btnImport.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_LOADDP));
		
		this.btnValidate = new Button(menu, SWT.NONE);
		btnValidate.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.dp.validate.tooltip"));
		GridData gd_btn_validate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_validate.widthHint = 60;
		gd_btn_validate.heightHint = 60;
		btnValidate.setLayoutData(gd_btn_validate);
		btnValidate.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_VALIDATEDP));
		this.btnAdd = new Button(menu, SWT.NONE);
		btnAdd.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.dp.add.tooltip"));
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btn_add.widthHint = 60;
		gd_btn_add.heightHint = 60;
		btnAdd.setLayoutData(gd_btn_add);
		btnAdd.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.ADD));
		this.btnDelete = new Button(menu, SWT.NONE);
		btnDelete.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.dp.delete.tooltip"));
		GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btn_delete.heightHint = 60;
		gd_btn_delete.widthHint = 60;
		btnDelete.setLayoutData(gd_btn_delete);
		btnDelete.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_DELETE));
		Button btn_DPTest = new Button(composite, SWT.NONE);
	    btn_DPTest.setToolTipText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
	        "com.bichler.astudio.editor.siemens.editor.dp.tooltip.testdp"));
	    btn_DPTest.addSelectionListener(new SelectionAdapter()
	    {
	      @Override
	      public void widgetSelected(SelectionEvent e)
	      {
	        StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
	        Object[] selections = sel.toArray();
	        if (selections != null && selections.length > 0)
	        {
	          // first read device config
	        	AllenBradleyDriverEditor deditor = new AllenBradleyDriverEditor();
	          deditor.initDevice(getEditorInput().getFilesystem(), getEditorInput().getDriverConfigFile());
	          AllenBradleyTestDialog tdialog = new AllenBradleyTestDialog(tableViewer.getControl().getShell());
	          tdialog.setDPs(selections, deditor.getDevice());
	          tdialog.create();
	          tdialog.open();
	        }
	        else
	        {
	          MessageDialog.openWarning(tableViewer.getControl().getShell(), "Siemens Test Panel",
	              "Kein knoten zum Testen ausgew�hlt");
	        }
	      }
	    });
	    GridData gd_btn_DPTest = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	    gd_btn_DPTest.heightHint = 60;
	    gd_btn_DPTest.widthHint = 60;
	    btn_DPTest.setLayoutData(gd_btn_DPTest);
	    btn_DPTest.setImage(AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_TEST));
	    
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(new Point(200, 200));
		this.loadPossibleTriggerNodes();
		this.fillTree();
		setHandler();
		getSite().setSelectionProvider(this);
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	public void createPage2(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		tableMonitoringViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		final Table table = tableMonitoringViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tableMonitoringViewer.setLabelProvider(new AllenBradleyModelLabelProvider());
		tableMonitoringViewer.setContentProvider(new AllenBradleyModelContentProvider());
		TableViewerColumn tvc_row = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.row"),
				75);
		tvc_row.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				@SuppressWarnings("unchecked")
				List<AllenBradleyEntryModelNode> input = (List<AllenBradleyEntryModelNode>) tableMonitoringViewer
						.getInput();
				int lineNumber = input.indexOf(element);
				return "" + (lineNumber + 1);
			}
		});
		TableViewerColumn tvc_displayname = TableWidgetUtil
				.createTableColumn(tableMonitoringViewer,
						CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.allenbradley.editor.dp.name"),
						125, TableWidgetEnum.DISPLAYNAME);
		tvc_displayname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getDisplayname();
			}
		});
		TableViewerColumn tvc_nodeid = TableWidgetUtil
				.createTableColumn(tableMonitoringViewer,
						CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.allenbradley.editor.dp.nodeid"),
						90, TableWidgetEnum.NODEID);
		tvc_nodeid.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				if (((AllenBradleyEntryModelNode) element).getNodeId() != null)
					return ((AllenBradleyEntryModelNode) element).getNodeId().toString();
				return "";
			}
		});
		TableViewerColumn tvc_browsepath = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.browsepath"),
				125, TableWidgetEnum.BROWSEPATH);
		tvc_browsepath.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getBrowsepath();
			}
		});
		TableViewerColumn tvc_tag = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.tagname"),
				125, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getSymbolName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_tag.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return ((AllenBradleyEntryModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getSymbolName();
			}
		});
		tvc_tag.setEditingSupport(new EditingSupport(tableMonitoringViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableMonitoringViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AllenBradleyEntryModelNode) element).getSymbolName();
			}

			protected void setValue(Object element, Object value) {
				if (((AllenBradleyEntryModelNode) element).getSymbolName().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((AllenBradleyEntryModelNode) element).setSymbolName(value.toString());
				tableMonitoringViewer.update(element, null);
				setDirty(true);
			}
		});
		TableViewerColumn tvc_mapping = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.mapping"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getMapping().name();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_mapping.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getMapping().name();
			}
		});
		tvc_mapping.setEditingSupport(new EditingSupport(tableMonitoringViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((AllenBradleyEntryModelNode) element).setMapping((ALLENBRADLEY_MAPPING_TYPE) value);
				tableMonitoringViewer.update(element, null);
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return ((AllenBradleyEntryModelNode) element).getMapping();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(tableMonitoringViewer.getTable(),
						SWT.READ_ONLY);
				cellEditor.setLabelProvider(new LabelProvider());
				cellEditor.setContentProvider(new ArrayContentProvider());
				cellEditor.setInput(ALLENBRADLEY_MAPPING_TYPE.values());
				return cellEditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_datatype = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.datatype"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getDataType();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		this.editorComboBox = new ComboBoxViewerCellEditor(tableMonitoringViewer.getTable());
		editorComboBox.setContentProvider(new ArrayContentProvider());
		editorComboBox.setLabelProvider(new LabelProvider());
		List<String> v = new ArrayList<>();
		for (ALLENBRADLEY_DATA_TYPE d : ALLENBRADLEY_DATA_TYPE.values()) {
			v.add(d.name());
		}
		editorComboBox.setInput(v.toArray(new String[v.size()]));
		tvc_datatype.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (!(element instanceof AllenBradleyEntryModelNode)) {
					return "";
				}
				if (((AllenBradleyEntryModelNode) element).getDataType() == null) {
					return "";
				}
				return ((AllenBradleyEntryModelNode) element).getDataType();
			}
		});
		TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.active"),
				35);
		tvc_active.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (((AllenBradleyEntryModelNode) element).isActive()) {
					return AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_CHECKED_1);
				} else {
					return AllenBradleySharedImages.getImage(AllenBradleySharedImages.ICON_CHECKED_0);
				}
			}
		});
		TableViewerColumn tvc_cycletime = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.cycletime"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((AllenBradleyEntryModelNode) element).getCycletime();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Long) o1).compareTo((Long) o2);
					}
				});
		tvc_cycletime.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getCycletime() + "";
			}
		});
		tvcTrigger = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.trigger"),
				100);
		// TableColumn trclmn_historical = tvc_trigger.getColumn();
		// trclmn_historical.setWidth(90);
		// trclmn_historical.setText("trigger");
		tvcTrigger.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((AllenBradleyEntryModelNode) element).getTrigger() != null) {
					// check if nodeid is null for dummy trigger event,
					// to show an empty line
					if (((AllenBradleyEntryModelNode) element).getTrigger().nodeId == null)
						return "";
					return ((AllenBradleyEntryModelNode) element).getTrigger().triggerName + " "
							+ ((AllenBradleyEntryModelNode) element).getTrigger().nodeId.toString() + " "
							+ ((AllenBradleyEntryModelNode) element).getTrigger().displayname;
				}
				return "";
			}

			@Override
			public Color getForeground(Object element) {
				NodeToTrigger obj = ((AllenBradleyEntryModelNode) element).getTrigger();
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
		triggerEditor = new ComboBoxViewerCellEditor(tableMonitoringViewer.getTable(), SWT.READ_ONLY);
		triggerEditor.setContentProvider(new ArrayContentProvider());
		final ControlDecoration controlDecoration = new ControlDecoration(triggerEditor.getControl(),
				SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.allenbradley.editor.dp.trigger.description"));
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
					return CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.allenbradley.editor.dp.trigger.description");
				}
			}
		});
		triggerEditor.setLabelProvider(new LabelProvider() {
			@Override
			public Image getImage(Object element) {
				NodeToTrigger obj = ((AllenBradleyEntryModelNode) element).getTrigger();
				boolean good = isTriggerNodeValid(obj);
				if (!good) {
					return FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
							.getImage();
				}
				return null;
			}

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
		TableViewerColumn tvc_description = TableWidgetUtil.createTableColumn(tableMonitoringViewer,
				CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.description"),
				250);
		tvc_description.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AllenBradleyEntryModelNode) element).getDesc();
			}
		});
		tvc_description.setEditingSupport(new EditingSupport(tableMonitoringViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableMonitoringViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((AllenBradleyEntryModelNode) element).getDescription();
			}

			protected void setValue(Object element, Object value) {
				if (((AllenBradleyEntryModelNode) element).getDescription().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((AllenBradleyEntryModelNode) element).setDescription(value.toString());
				tableMonitoringViewer.refresh(element);
				setDirty(true);
			}
		});
		this.fillMonitoringTree();
		getSite().setSelectionProvider(this);
	}

	public static Map<String, Integer> getDataTypeSizes() {
		return dataTypeSizes;
	}

	public static void setDataTypeSizes(Map<String, Integer> dataTypeSizes) {
		AllenBradleyDPEditor.dataTypeSizes = dataTypeSizes;
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
		List<AllenBradleyEntryModelNode> items = (List<AllenBradleyEntryModelNode>) tableViewer.getInput();
		List<IDriverNode> items2export = new ArrayList<>();
		items2export.addAll(items);
		String configPath = getEditorInput().getDPConfigFile();
		AllenBradleyDpExporter exporter = new AllenBradleyDpExporter(getEditorInput().getFilesystem(), configPath);
		exporter.build(items2export, opcServer.getServerInstance().getNamespaceUris());

//    exporter.write(getEditorInput().getFilesystem(), configPath, buffer);
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
					monitor.beginTask(
							CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.editor.allenbradley.editor.dp.monitor.init"),
							IProgressMonitor.UNKNOWN);
					monitor.subTask(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.allenbradley.editor.dp.monitor.init.subtask"));
					try {
						setSite(site);
						setInput(input);
						setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - "
								+ CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.editor.allenbradley.editor.dp.monitor.init.part"));
						String serverpath = new Path(getEditorInput().getFilesystem().getRootPath()).toOSString();
						try {
							URL modelParent = new Path(serverpath).append("informationmodel").toFile().toURI().toURL();
							String serverConfigPath = new Path(serverpath).append("serverconfig")
									.append("server.config.xml").toOSString();
							if (getEditorInput().getFilesystem().isFile(serverConfigPath)) {
								opcServer = Studio_ResourceManager.getOrNewOPCUAServer(getEditorInput().getServerName(),
										serverConfigPath, serverpath, serverpath + "/localization", modelParent);
							}
						} catch (IOException | ExecutionException e1) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
						}
						getEditorInput().setToolTipText(
								CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.editor.allenbradley.editor.dp.tooltip"));
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
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
					if (object instanceof Integer) {
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

	@Override
	public void setDataPointSelection(Object element) {
		this.currentSelection = element;
		selectionChanged();
	}

	protected void selectionChanged() {
		if (listener == null) {
			return;
		}
		listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	private void setHandler() {
		this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setDataPointSelection(((StructuredSelection) event.getSelection()).getFirstElement());
			}
		});
		this.btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AllenBradleyDBResourceManager structManager = AllenBradleyResources.getInstance()
						.getDBResourceManager(getEditorInput().getDriverName());
				AllenBradleyImportWizard wizard = new AllenBradleyImportWizard(structManager);
				wizard.setFilesystem(getEditorInput().getFilesystem());
				WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
				if (Dialog.OK == dialog.open()) {
					setDirty(true);
				}
			}
		});
		this.btnValidate.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings({ "unchecked" })
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object input = tableViewer.getInput();
				if (input == null) {
					MessageDialog.openInformation(getSite().getShell(),
							CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.editor.allenbradley.editor.dialog.validation"),
							CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.editor.allenbradley.editor.dialog.validation.error"));
					return;
				}
				AllenBradleyDBResourceManager structManager = AllenBradleyResources.getInstance()
						.getDBResourceManager(getEditorInput().getDriverName());
				AllenBradleyValidationDialog dialog = new AllenBradleyValidationDialog(getSite().getShell(),
						structManager);
				dialog.setFilesystem(getEditorInput().getFilesystem());
				Map<String, AllenBradleyEntryModelNode> inputModel = new HashMap<>();
				for (AllenBradleyEntryModelNode child : (List<AllenBradleyEntryModelNode>) input) {
					inputModel.put(child.getSymbolName(), child);
				}
				dialog.setModel(new ArrayList<AllenBradleyEntryModelNode>((List<AllenBradleyEntryModelNode>) input),
						inputModel);
				if (Dialog.OK == dialog.open()) {
					setDirty(true);
				}
			}
		});
		this.btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/** first get selected index of table */
				if (tableViewer.getInput() == null) {
					tableViewer.setInput(new ArrayList<AllenBradleyEntryModelNode>());
				}
				@SuppressWarnings("unchecked")
				List<AllenBradleyEntryModelNode> input = (List<AllenBradleyEntryModelNode>) tableViewer.getInput();
				@SuppressWarnings("unchecked")
				int index = ((List<AllenBradleyEntryModelNode>) tableViewer.getInput()).size();
				if (index > 0) {
					// last
					AllenBradleyEntryModelNode neighbor = input.get(index - 1);
					// nIndex =
					neighbor.calculateEndIndex();
				}
				AllenBradleyEntryModelNode newitem = new AllenBradleyEntryModelNode();
				newitem.setSymbolName("unknown");
				newitem.setActive(true);
				newitem.setCycletime(1000);
				newitem.setAddress("0");
				newitem.setDescription(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.allenbradley.editor.dp.comment"));
				input.add(newitem);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(newitem), false);
				setDirty(true);
			}
		});
		this.btnDelete.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
				Object[] selections = sel.toArray();
				if (selections != null) {
					for (Object o : selections) {
						((List<AllenBradleyEntryModelNode>) tableViewer.getInput()).remove(o);
					}
					tableViewer.remove(selections);
					tableViewer.refresh();
					setDirty(true);
				}
			}
		});
	}

	private void setExpression(TableItem item, NodeId nodeId, String name, String browsepath) {
		AllenBradleyEntryModelNode dp = (AllenBradleyEntryModelNode) item.getData();
		dp.setNodeId(nodeId);
		dp.setDisplayname(name);
		dp.setBrowsepath(browsepath);
		tableViewer.refresh();
		setDirty(true);
	}

	private void fillTree() {
		/** open file to import */
		String dpsPath = getEditorInput().getDPConfigFile();
		List<AllenBradleyDPItem> dps = new ArrayList<>();
		List<AllenBradleyEntryModelNode> nodes = new ArrayList<>();
		if (getEditorInput().getFilesystem().isFile(dpsPath)) {
			InputStreamReader isr = null;
			try {
				InputStream input = getEditorInput().getFilesystem().readFile(dpsPath);
				isr = new InputStreamReader(input, "UTF-8");
				InputSource is = new InputSource(isr);
				NamespaceTable uris = this.opcServer.getServerInstance().getNamespaceUris();
				AllenBradleyDPEditorImporter importer = new AllenBradleyDPEditorImporter();
				dps = importer.loadDPs(is, uris);
				AllenBradleyEntryModelNode smn = null;
				StringBuilder browsepath = new StringBuilder();
				Deque<BrowsePathElement> browsepathelems = null;
				for (AllenBradleyDPItem dp : dps) {
					browsepath = new StringBuilder();
					smn = AllenBradleyEntryModelNode.loadFromDP(dp);
					// load display name for each node
					// create browsepath from address space
					browsepathelems = OPCUABrowseUtils.getFullBrowsePath(dp.getNodeId(), opcServer.getServerInstance(),
							Identifiers.ObjectsFolder);
					for (BrowsePathElement element : browsepathelems) {
						if (element.getId().equals(Identifiers.ObjectsFolder)) {
							continue;
						}
						browsepath.append("//" + element.getBrowsename().getName());
					}
					smn.setBrowsepath(browsepath.toString());
					// now try to find the correct trigger
					if (dp.getTriggerNode() != null && !dp.getTriggerNode().isEmpty()) {
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
				for (AllenBradleyEntryModelNode n : nodes) {
					node = opcServer.getServerInstance().getAddressSpaceManager().getNodeById(n.getNodeId());
					if (node != null) {
						n.setDisplayname(node.getDisplayName().getText());
					}
				}
				List<String> inputs = new ArrayList<String>();
				Object inputItems = editorComboBox.getViewer().getInput();
				if (inputItems instanceof String[]) {
					for (String s : (String[]) inputItems) {
						inputs.add(s);
					}
				}
				for (AllenBradleyEntryModelNode node4datatype : nodes) {
					String dt = node4datatype.getDataType();
					if (!inputs.contains(dt)) {
						inputs.add(dt);
					}
				}
				editorComboBox.getViewer().setInput(inputs.toArray(new String[inputs.size()]));
				editorComboBox.getViewer().refresh();
				// create a S
				tableViewer.setInput(nodes);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			} finally {
				if (isr != null) {
					try {
						isr.close();
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}
	}

	private void fillMonitoringTree() {
		/** open file to import */
		String dpsPath = getEditorInput().getDPConfigFile();
		List<AllenBradleyDPItem> dps = new ArrayList<>();
		List<AllenBradleyEntryModelNode> nodes = new ArrayList<>();
		if (getEditorInput().getFilesystem().isFile(dpsPath)) {
			InputStreamReader isr = null;
			try {
				InputStream input = getEditorInput().getFilesystem().readFile(dpsPath);
				isr = new InputStreamReader(input, "UTF-8");
				InputSource is = new InputSource(isr);
				NamespaceTable uris = this.opcServer.getServerInstance().getNamespaceUris();
				AllenBradleyDPEditorImporter importer = new AllenBradleyDPEditorImporter();
				dps = importer.loadDPs(is, uris);
				AllenBradleyEntryModelNode smn = null;
				StringBuilder browsepath = new StringBuilder();
				Deque<BrowsePathElement> browsepathelems = null;
				for (AllenBradleyDPItem dp : dps) {
					browsepath = new StringBuilder();
					smn = AllenBradleyEntryModelNode.loadFromDP(dp);
					// load display name for each node
					// create browsepath from address space
					browsepathelems = OPCUABrowseUtils.getFullBrowsePath(dp.getNodeId(), opcServer.getServerInstance(),
							Identifiers.ObjectsFolder);
					for (BrowsePathElement element : browsepathelems) {
						if (element.getId().equals(Identifiers.ObjectsFolder)) {
							continue;
						}
						browsepath.append("//" + element.getBrowsename().getName());
					}
					smn.setBrowsepath(browsepath.toString());
					// now try to find the correct trigger
					if (dp.getTriggerNode() != null && !dp.getTriggerNode().isEmpty()) {
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
				for (AllenBradleyEntryModelNode n : nodes) {
					node = opcServer.getServerInstance().getAddressSpaceManager().getNodeById(n.getNodeId());
					if (node != null) {
						n.setDisplayname(node.getDisplayName().getText());
					}
				}
				List<String> inputs = new ArrayList<String>();
				Object inputItems = editorComboBox.getViewer().getInput();
				if (inputItems instanceof String[]) {
					for (String s : (String[]) inputItems) {
						inputs.add(s);
					}
				}
				for (AllenBradleyEntryModelNode node4datatype : nodes) {
					String dt = node4datatype.getDataType();
					if (!inputs.contains(dt)) {
						inputs.add(dt);
					}
				}
				editorComboBox.getViewer().setInput(inputs.toArray(new String[inputs.size()]));
				editorComboBox.getViewer().refresh();
				// create a S
				tableMonitoringViewer.setInput(nodes);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			} finally {
				if (isr != null) {
					try {
						isr.close();
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void refreshDatapoints() {
		fillTree();
	}

	@Override
	public Control getDPControl() {
		return this.tableViewer.getControl();
	}

	@Override
	public ISelectionProvider getDPViewer() {
		return this.tableViewer;
	}

	@Override
	public void onFocusRemoteView() {
		AllenBradleyDBResourceManager structManager = AllenBradleyResources.getInstance()
				.getDBResourceManager(getEditorInput().getDriverName());
		AllenBradleyDriverUtil.openDriverView(getEditorInput().getFilesystem(), getEditorInput().getDriverConfigFile(),
				structManager);
	}
}
