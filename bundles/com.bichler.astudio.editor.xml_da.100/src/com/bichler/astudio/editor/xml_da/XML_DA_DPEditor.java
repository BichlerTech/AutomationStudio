package com.bichler.astudio.editor.xml_da;

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

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
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

import com.bichler.astudio.editor.xml_da.dnd.XMLDaDPDndViewAdapter;
import com.bichler.astudio.editor.xml_da.driver.datatype.XML_DA_DATATYPE;
import com.bichler.astudio.editor.xml_da.xml.XMLDADPEditorImporter;
import com.bichler.astudio.editor.xml_da.xml.XMLDADPExporter;
import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;
import com.bichler.astudio.editor.xml_da.xml.XML_DA_DPItem;
import com.bichler.astudio.editor.xml_da.xml.XML_DA_MAPPING_TYPE;
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
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class XML_DA_DPEditor extends AbstractOPCTriggerNodeEditPart {
	public static final String ID = "com.bichler.astudio.editor.xml_da.XML_DA_DPEditor"; //$NON-NLS-1$
	private static Map<String, Integer> dataTypeSizes = new HashMap<>();
	private UAServerApplicationInstance opcServer = null;
	private int maxNodeId = 10;
	private ComboBoxViewerCellEditor triggerEditor;
	private boolean dirty = false;
	private Object currentSelection;
	private ISelectionChangedListener listener;
	private TableViewer tableViewer;
	private Logger logger = Logger.getLogger(getClass().getName());

	public XML_DA_DPEditor() {
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
				String line = "";
				String[] items = null;
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
					}
				}
				reader.close();
				input.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	@Override
	public OPCUADPEditorInput getEditorInput() {
		return (OPCUADPEditorInput) super.getEditorInput();
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
		tableViewer.setLabelProvider(new XML_DA_ModelLabelProvider());
		tableViewer.setContentProvider(new XML_DA_ModelContentProvider());
		int operations = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		this.tableViewer.addDropSupport(operations, transferTypes, new XMLDaDPDndViewAdapter(this.tableViewer, this));
		this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setDataPointSelection(((IStructuredSelection) event.getSelection()).getFirstElement());
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				try {
					// get the selected node id
					Point p = new Point(event.x, event.y);
					ViewerCell cell = tableViewer.getCell(p);
					if (cell == null) {
						return;
					}
					int columnIndex = cell.getColumnIndex();
					if (columnIndex <= 2) {
						TableItem[] selection = table.getSelection();
						if (selection != null && selection.length > 0) {
							TableItem item = selection[0];
							Object data = item.getData();
							if (data != null && data instanceof XMLDAModelNode) {
								XMLDAModelNode dp = (XMLDAModelNode) data;
								NodeId nodeid = dp.getNId();
								OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
								dialog.setInternalServer(opcServer.getServerInstance());
								dialog.setSelectedNodeId(nodeid);
								dialog.setFormTitle(
										CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
												"com.bichler.astudio.editor.xml_da.editor.dialog.title"));
								if (dialog.open() == Dialog.OK) {
									dp.setNodeId(dialog.getSelectedNodeId());
									dp.setDisplayname(dialog.getSelectedDisplayName());
									// fill full string to browsepath
									String browsepath = "";
									for (BrowsePathElement element : dialog.getBrowsePath()) {
										if (element.getId().equals(Identifiers.ObjectsFolder)) {
											continue;
										}
										browsepath += "//" + element.getBrowsename().getName();
									}
									dp.setBrowsepath(browsepath);
									// dialog.get
									// now update tabeviewer
									tableViewer.refresh();
									setDirty(true);
								}
							}
						}
					}
				} catch (Exception ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			}
		});
		TableViewerColumn tvc_columnnr = TableWidgetUtil.createTableColumn(tableViewer, CustomString.getString(
				XML_DA_Activator.getDefault().RESOURCE_BUNDLE, "com.bichler.astudio.editor.xml_da.editor.dp.row"), 75);
		tvc_columnnr.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				@SuppressWarnings("unchecked")
				List<IDriverNode> input = (List<IDriverNode>) tableViewer.getInput();
				int lineNumber = input.indexOf(element);
				return Integer.toString(lineNumber + 1);
			}
		});
		TableViewerColumn tvc_displayname = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.name"),
				125, TableWidgetEnum.DISPLAYNAME);
		tvc_displayname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((XMLDAModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getDisplayname();
			}
		});
		TableViewerColumn tvc_nodeid = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.nodeid"),
				100, TableWidgetEnum.NODEID);
		tvc_nodeid.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((XMLDAModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				if (((XMLDAModelNode) element).getNId() != null)
					return ((XMLDAModelNode) element).getNId().toString();
				return "";
			}
		});
		TableViewerColumn tvc_browsepath = TableWidgetUtil
				.createTableColumn(tableViewer,
						CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.xml_da.editor.dp.browsepath"),
						125, TableWidgetEnum.BROWSEPATH);
		tvc_browsepath.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}

			@Override
			public Image getImage(Object element) {
				return ((XMLDAModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getBrowsepath();
			}
		});
		TableViewerColumn tvc_valuerank = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.mapping"),
				125, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((XMLDAModelNode) element).getMapping().name();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_valuerank.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return ((XMLDAModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getMapping().name();
			}
		});
		tvc_valuerank.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				((XMLDAModelNode) element).setMapping((XML_DA_MAPPING_TYPE) value);
				tableViewer.update(element, null);
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				return ((XMLDAModelNode) element).getMapping();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(tableViewer.getTable());
				cellEditor.setLabelProvider(new LabelProvider());
				cellEditor.setContentProvider(new ArrayContentProvider());
				cellEditor.setInput(XML_DA_MAPPING_TYPE.values());
				return cellEditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tvc_symbolname = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.varname"),
				125, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((XMLDAModelNode) element).getSymbolName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_symbolname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return ((XMLDAModelNode) element).getLabelImage();
			}

			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getSymbolName();
			}
		});
		tvc_symbolname.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((XMLDAModelNode) element).getSymbolName();
			}

			protected void setValue(Object element, Object value) {
				if (((XMLDAModelNode) element).getSymbolName().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((XMLDAModelNode) element).setSymbolName(value.toString());
				tableViewer.refresh(element);
			}
		});
		TableViewerColumn tvc_itempath = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.itempath"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((XMLDAModelNode) element).getItemPath();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_itempath.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getItemPath() + "";
			}
		});
		tvc_itempath.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((XMLDAModelNode) element).getItemPath();
			}

			protected void setValue(Object element, Object value) {
				if ((((XMLDAModelNode) element).getItemPath()).compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((XMLDAModelNode) element).setItemPath(value.toString());
				tableViewer.refresh(element);
			}
		});
		TableViewerColumn tvc_itemname = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.itemname"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((XMLDAModelNode) element).getItemName();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_itemname.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getItemName();
			}
		});
		tvc_itemname.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((XMLDAModelNode) element).getItemName();
			}

			protected void setValue(Object element, Object value) {
				if ((((XMLDAModelNode) element).getItemName()).compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((XMLDAModelNode) element).setItemName(value.toString());
				tableViewer.refresh(element);
			}
		});
		TableViewerColumn tvcDatatype = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.datatype"),
				75, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((XMLDAModelNode) element).getDataType();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		final ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(tableViewer.getTable());
		editor.setContentProvider(new ArrayContentProvider());
		editor.setLabelProvider(new LabelProvider());
		List<String> v = new ArrayList<>();
		for (XML_DA_DATATYPE d : XML_DA_DATATYPE.values()) {
			v.add(d.name());
		}
		editor.setInput(v.toArray(new String[v.size()]));
		tvcDatatype.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element == null || ((XMLDAModelNode) element).getDataType() == null) {
					return "";
				}
				return ((XMLDAModelNode) element).getDataType();
			}
		});
		tvcDatatype.setEditingSupport(new EditingSupport(tableViewer) {
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
							List<String> l = new ArrayList<>();
							for (String s : (String[]) i) {
								l.add(s);
							}
							l.add(el.toUpperCase().replace(" ", ""));
							editor.getViewer().setInput(l.toArray(new String[l.size()]));
							editor.getViewer().refresh();
						}
					}
					((XMLDAModelNode) element).setDataType(el.toUpperCase().replace(" ", ""));
					tableViewer.refresh();
					setDirty(true);
					return;
				}
				((XMLDAModelNode) element).setDataType(value.toString());
				tableViewer.refresh();
				setDirty(true);
			}

			@Override
			protected Object getValue(Object element) {
				if (element == null || ((XMLDAModelNode) element).getDataType() == null) {
					return "";
				}
				return ((XMLDAModelNode) element).getDataType();
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
		TableViewerColumn tvcActive = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.active"),
				40);
		tvcActive.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (((XMLDAModelNode) element).isActive()) {
					return XML_DA_SharedImages.getImage(XML_DA_SharedImages.ICON_CHECKED_1);
				} else {
					return XML_DA_SharedImages.getImage(XML_DA_SharedImages.ICON_CHECKED_0);
				}
			}
		});
		tvcActive.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(tableViewer.getTable());
			}

			protected Object getValue(Object element) {
				return ((XMLDAModelNode) element).isActive();
			}

			protected void setValue(Object element, Object value) {
				setDirty(true);
				((XMLDAModelNode) element).setActive(!((XMLDAModelNode) element).isActive());
				// ((OmronTableItem)element).active = value.toString();
				tableViewer.refresh(element);
			}
		});
		TableViewerColumn tvc_cycletime = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.cycletime"),
				50, new TableWidgetUtil().new AbstractDriverComperator() {
					@Override
					public Object getComparableObject(IDriverNode element) {
						return ((XMLDAModelNode) element).getCycletime();
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Integer) o1).compareTo((Integer) o2);
					}
				});
		// TableColumn trclmnCycletime = tvc_cycletime.getColumn();
		// trclmnCycletime.setWidth(55);
		// trclmnCycletime.setText("cycletime");
		tvc_cycletime.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getCycletime() + "";
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
				return ((XMLDAModelNode) element).getCycletime();
			}

			protected void setValue(Object element, Object value) {
				try {
					int cycletime = Integer.parseInt(value.toString());
					if (((XMLDAModelNode) element).getCycletime() == cycletime) {
						return;
					}
					((XMLDAModelNode) element).setCycletime(cycletime);
					setDirty(true);
				} catch (NumberFormatException ex) {
					// TODO parse number format exception
				}
				tableViewer.refresh(element);
			}
		});
		TableViewerColumn tvc_trigger = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.trigger"),
				100);
		// TableColumn trclmn_historical = tvc_trigger.getColumn();
		// trclmn_historical.setWidth(90);
		// trclmn_historical.setText("trigger");
		tvc_trigger.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((XMLDAModelNode) element).getTrigger() != null) {
					// check if nodeid is null for dummy trigger event,
					// to show an empty line
					if (((XMLDAModelNode) element).getTrigger().nodeId == null)
						return "";
					return ((XMLDAModelNode) element).getTrigger().triggerName + " "
							+ ((XMLDAModelNode) element).getTrigger().nodeId.toString() + " "
							+ ((XMLDAModelNode) element).getTrigger().displayname;
				}
				return "";
			}

			@Override
			public Color getForeground(Object element) {
				NodeToTrigger obj = ((XMLDAModelNode) element).getTrigger();
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
		controlDecoration.setDescriptionText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.dp.trigger.description"));
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
					return CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.xml_da.editor.dp.trigger.description");
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
				if (((XMLDAModelNode) element).getTrigger() != null) {
					return ((XMLDAModelNode) element).getTrigger();
				}
				return "";
			}

			protected void setValue(Object element, Object value) {
				if (value == null || !(value instanceof NodeToTrigger))
					return;
				((XMLDAModelNode) element).setTrigger((NodeToTrigger) value);
				setDirty(true);
				tableViewer.update(element, null);
			}
		});
		TableViewerColumn tvc_description = TableWidgetUtil.createTableColumn(tableViewer,
				CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.editor.xml_da.editor.dp.description"),
				250);
		// TableColumn trclmnNewColumn_3 = tvc_description.getColumn();
		// trclmnNewColumn_3.setWidth(248);
		// trclmnNewColumn_3.setText("description");
		tvc_description.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((XMLDAModelNode) element).getDescription();
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
				return ((XMLDAModelNode) element).getDescription();
			}

			protected void setValue(Object element, Object value) {
				if (((XMLDAModelNode) element).getDescription().compareTo(value.toString()) == 0) {
					return;
				}
				setDirty(true);
				((XMLDAModelNode) element).setDescription(value.toString());
				tableViewer.refresh(element);
			}
		});
		Button btn_add = new Button(composite, SWT.NONE);
		btn_add.setToolTipText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.dp.adddatapoint"));
		btn_add.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				/** first get selected index of table */
				StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
				Object[] selections = sel.toArray();
				if (tableViewer.getInput() == null) {
					tableViewer.setInput(new ArrayList<XMLDAModelNode>());
				}
				int index = ((ArrayList<XMLDAModelNode>) tableViewer.getInput()).size();
				if (selections != null && selections.length > 0) {
					Object o = selections[selections.length - 1];
					index = ((ArrayList<XMLDAModelNode>) tableViewer.getInput()).indexOf(o) + 1;
				}
				maxNodeId++;
				XMLDAModelNode newitem = new XMLDAModelNode();
				newitem.setId(maxNodeId);
				// newitem.setLabelImage(SiemensSharedImages
				// .getImage(SiemensSharedImages.ICON_DATEPOINTLEAF));
				newitem.setSymbolName("unknown");
				newitem.setActive(true);
				newitem.setCycletime(1000);
				newitem.setItemPath("");
				newitem.setItemName("");
				newitem.setDescription("insert comment");
				((ArrayList<XMLDAModelNode>) tableViewer.getInput()).add(index, newitem);
				tableViewer.refresh();
				setDirty(true);
			}
		});
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btn_add.widthHint = 60;
		gd_btn_add.heightHint = 60;
		btn_add.setLayoutData(gd_btn_add);
		btn_add.setImage(XML_DA_SharedImages.getImage(XML_DA_SharedImages.ICON_ADD));
		Button btn_delete = new Button(composite, SWT.NONE);
		btn_delete.setToolTipText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.xml_da.editor.dp.deletedatapoint"));
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
				Object[] selections = sel.toArray();
				if (selections != null) {
					for (Object o : selections) {
						((ArrayList<XMLDAModelNode>) tableViewer.getInput()).remove(o);
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
		btn_delete.setImage(XML_DA_SharedImages.getImage(XML_DA_SharedImages.ICON_DELETE));
		Button btn_DPTest = new Button(composite, SWT.NONE);
		btn_DPTest.setToolTipText(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.editor.dp.tooltip.testdp"));
		btn_DPTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
				Object[] selections = sel.toArray();
				if (selections != null && selections.length > 0) {
					// first read device config
					XML_DA_DriverEditor deditor = new XML_DA_DriverEditor();
					deditor.initDevice(getEditorInput().getFilesystem(), getEditorInput().getDriverConfigFile());
					XML_DA_TestDialog tdialog = new XML_DA_TestDialog(tableViewer.getControl().getShell());
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
		btn_DPTest.setImage(XML_DA_SharedImages.getImage(XML_DA_SharedImages.ICON_TEST));
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(new Point(200, 200));
		this.loadPossibleTriggerNodes();
		this.fillTree();
		getSite().setSelectionProvider(this);
	}

	public static Map<String, Integer> getDataTypeSizes() {
		return dataTypeSizes;
	}

	public static void setDataTypeSizes(Map<String, Integer> dataTypeSizes) {
		XML_DA_DPEditor.dataTypeSizes = dataTypeSizes;
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
		List<XMLDAModelNode> items = (ArrayList<XMLDAModelNode>) tableViewer.getInput();
		List<IDriverNode> items2export = new ArrayList<>();
		items2export.addAll(items);
		// StringBuffer buffer = new StringBuffer();
		XMLDADPExporter exporter = new XMLDADPExporter(getEditorInput().getFilesystem(),
				getEditorInput().getDPConfigFile());
		exporter.build(items2export, opcServer.getServerInstance().getNamespaceUris());

//    String configPath = getEditorInput().getDPConfigFile();
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
					monitor.beginTask(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.xml_da.editor.dp.loaddp"), IProgressMonitor.UNKNOWN);
					monitor.subTask(CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.xml_da.editor.dp.initialize"));
					try {
						setSite(site);
						setInput(input);
						setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - "
								+ CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.editor.xml_da.editor.dp.dp"));
						String serverpath = getEditorInput().getFilesystem().getRootPath();
						Path sPath = new Path(serverpath);
						try {
							URL modelParent = sPath.append("informationmodel").toFile().toURI().toURL();
							IPath sconfigPath = sPath.append("serverconfig").append("server.config.xml");
							if (getEditorInput().getFilesystem().isFile(sconfigPath.toOSString())) {
								try {
									opcServer = Studio_ResourceManager.getOrNewOPCUAServer(
											getEditorInput().getServerName(), sconfigPath.toOSString(),
											sPath.toOSString(), serverpath + "/localization", modelParent);
								} catch (ExecutionException e) {
									logger.log(Level.SEVERE, e.getMessage());
								}
							}
						} catch (IOException e1) {
							logger.log(Level.SEVERE, e1.getMessage());
						}
						getEditorInput().setToolTipText("XML-DA v1.0.0 - "
								+ CustomString.getString(XML_DA_Activator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.editor.xml_da.editor.dp.tooltips"));
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			logger.log(Level.SEVERE, e.getMessage());
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
		if (triggerEditor.getViewer().getContentProvider() != null) {
			triggerEditor.setInput(getPossibleTriggerNodes());
		}
		tableViewer.refresh();
	}

	@Override
	public void setDataPointSelection(Object element) {
		this.currentSelection = element;
		selectionChanged();
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
		listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	private void fillTree() {
		/** open file to import */
		String dpsPath = getEditorInput().getDPConfigFile();
		List<XML_DA_DPItem> dps = new ArrayList<>();
		List<XMLDAModelNode> nodes = new ArrayList<>();
		if (getEditorInput().getFilesystem().isFile(dpsPath)) {
			InputStream input = null;
			try {
				input = getEditorInput().getFilesystem().readFile(dpsPath);
				NamespaceTable uris = this.opcServer.getServerInstance().getNamespaceUris();
				XMLDADPEditorImporter importer = new XMLDADPEditorImporter();
				dps = importer.loadDPs(new InputSource(new InputStreamReader(input, "UTF-8")), uris);
				XMLDAModelNode smn = null;
				String browsepath = "";
				Deque<BrowsePathElement> browsepathelems = null;
				for (XML_DA_DPItem dp : dps) {
					browsepath = "";
					smn = XMLDAModelNode.loadFromDP(dp);
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
							if (trig.nodeId != null && trig.nodeId.toString().equals(dp.getTriggerNode())) {
								smn.setTrigger(trig);
								break;
							}
						}
					}
					nodes.add(smn);
				}
				Node node = null;
				// now set displayname for each model node
				for (XMLDAModelNode n : nodes) {
					node = opcServer.getServerInstance().getAddressSpaceManager().getNodeById(n.getNId());
					if (node != null) {
						n.setDisplayname(node.getDisplayName().getText());
					}
				}
				// create a S
				tableViewer.setInput(nodes);
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, e.getMessage());
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void refreshDatapoints() {
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
		DriverBrowserUtil.openEmptyDriverModelView();
	}
}
