package com.bichler.astudio.opcua.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.utils.UtilsActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.images.SharedImages;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.dnd.OPCUADropInTriggerNodesViewAdapter;
import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;
import com.bichler.astudio.opcua.driver.IOPCDriverConfigEditPart;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.editor.input.OPCUADriverEditorInput;
import com.bichler.astudio.opcua.handlers.events.AdvancedDriverPersister;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.server.core.UAServerApplicationInstance;

public class TriggerNodesWidget {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private List<NodeToTrigger> triggernodes = new ArrayList<NodeToTrigger>();
	private Section sectionTriggernodes;
	private TableViewer tv_TriggerNodes;
	private UAServerApplicationInstance opcServer;
	private IOPCDriverConfigEditPart editor;
	private Composite composite;
	private TriggerExpansionListener handler;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TriggerNodesWidget(Composite parent, int style, UAServerApplicationInstance opcServer,
			IOPCDriverConfigEditPart editor) {
		this.composite = parent;
		this.opcServer = opcServer;
		this.editor = editor;
		createTriggernodeSection(composite);
		fillTriggernodes();
		addHandler();
	}

	public boolean saveTriggerNodes() {
		OPCUADriverEditorInput eInput = ((OPCUADriverEditorInput) ((EditorPart) editor).getEditorInput());
		IFileSystem filesystem = ((OPCUADriverEditorInput) ((EditorPart) editor).getEditorInput()).getFileSystem();
		String path = new Path(eInput.getDriverPath()).append("triggernodes.com").toOSString();
		AdvancedDriverPersister exporter = new AdvancedDriverPersister();
		exporter.exportTriggerNodes(this.opcServer.getServerInstance().getNamespaceUris(), filesystem, path,
				this.triggernodes);
		updateTriggerNodes();
		return true;
	}

	public void refresh() {
		this.tv_TriggerNodes.refresh();
	}

	public Object getInput() {
		return this.tv_TriggerNodes.getInput();
	}

	public void addExpansionListener(ExpansionAdapter expansionAdapter) {
		this.sectionTriggernodes.addExpansionListener(expansionAdapter);
	}

	public void setExpansionHandler(TriggerExpansionListener handler) {
		this.handler = handler;
	}

	private void addHandler() {
	}

	private void createTriggernodeSection(Composite composite) {
		/**
		 * Section with a client composite
		 */
		this.sectionTriggernodes = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		formToolkit.paintBordersFor(sectionTriggernodes);
		sectionTriggernodes
				.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.trigger.title"));
		sectionTriggernodes.setExpanded(true);
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_table_1.widthHint = 478;
		sectionTriggernodes.setLayoutData(gd_table_1);
		Composite sctnComposite = new Composite(sectionTriggernodes, SWT.NONE);
		formToolkit.adapt(sctnComposite);
		formToolkit.paintBordersFor(sctnComposite);
		sectionTriggernodes.setClient(sctnComposite);
		sctnComposite.setLayout(new GridLayout(3, false));
		tv_TriggerNodes = new TableViewer(sctnComposite, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table = tv_TriggerNodes.getTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem[] selection = table.getSelection();
				// check selection
				if (selection != null && selection.length > 0) {
					// check cell index
					Point p = new Point(e.x, e.y);
					ViewerCell cell = tv_TriggerNodes.getCell(p);
					if (cell == null) {
						return;
					}
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex) {
					case 3:
						break;
					default:
						TableItem item = selection[0];
						Object data = item.getData();
						if (data != null && data instanceof NodeToTrigger) {
							NodeId nodeid = ((NodeToTrigger) data).nodeId;
							OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
							dialog.setInternalServer(opcServer.getServerInstance());
							dialog.setSelectedNodeId(nodeid);
							// dialog.setStartNodeId(Identifiers.ObjectsFolder);
							dialog.setFormTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"widget.advanced.opcuanodes"));
							if (dialog.open() == Dialog.OK) {
								editor.setDirty(true);
								if (dialog.getSelectedNodeId() != null)
									((NodeToTrigger) data).nodeId = dialog.getSelectedNodeId();
								((NodeToTrigger) data).displayname = dialog.getSelectedDisplayName();
								tv_TriggerNodes.refresh();
								// updateTriggerNodes();
							}
						}
						break;
					}
				}
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 2);
		gd_table_1.widthHint = 478;
		table.setLayoutData(gd_table_1);
		tv_TriggerNodes.setContentProvider(new ArrayContentProvider());
		int operations = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		this.tv_TriggerNodes.addDropSupport(operations, transferTypes,
				new OPCUADropInTriggerNodesViewAdapter(this.tv_TriggerNodes, this.editor));
		TableViewerColumn tvc_active = TableWidgetUtil.createTableColumn(tv_TriggerNodes,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.active"), 100);
		tvc_active.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (((NodeToTrigger) element).active) {
					return UtilsActivator.getDefault().getImageRegistry().get(SharedImages.ICON_CHECKED_1);
				} else {
					return UtilsActivator.getDefault().getImageRegistry().get(SharedImages.ICON_CHECKED_0);
				}
			}
		});
		tvc_active.setEditingSupport(new EditingSupport(tv_TriggerNodes) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(tv_TriggerNodes.getTable());
			}

			protected Object getValue(Object element) {
				return ((NodeToTrigger) element).active;
			}

			protected void setValue(Object element, Object value) {
				editor.setDirty(true);
				((NodeToTrigger) element).active = !((NodeToTrigger) element).active;
				// ((OmronTableItem)element).active = value.toString();
				tv_TriggerNodes.refresh(element);
			}
		});
		TableViewerColumn tvc_nodeid = TableWidgetUtil.createTableColumn(tv_TriggerNodes,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.nodeid"), 100,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((NodeToTrigger) element).nodeId;
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((NodeId) o1).compareTo((NodeId) o2);
					}
				});
		tvc_nodeid.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((NodeToTrigger) element).nodeId != null)
					return ((NodeToTrigger) element).nodeId.toString();
				return "";
			}
		});
		TableViewerColumn tvc_name = TableWidgetUtil.createTableColumn(tv_TriggerNodes,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.displayname"), 125,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						if (((NodeToTrigger) element).displayname != null) {
							return ((NodeToTrigger) element).displayname;
							// Node node = ServerInstance
							// .getInstance()
							// .getServerInstance()
							// .getAddressSpaceManager()
							// .getNodeById(
							// ((NodeToTrigger) element).nodeId);
							//
							// if (node != null) {
							// return node.getDisplayName().getText();
							// }
						}
						return "";
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((String) o1).compareTo((String) o2);
					}
				});
		tvc_name.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (((NodeToTrigger) element).nodeId != null) {
					Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeById(((NodeToTrigger) element).nodeId);
					if (node != null) {
						return node.getDisplayName().getText();
					}
				}
				return "";
				// return ((NodeToTrigger) element).displayname;
			}
		});
		TableViewerColumn tvc_index = TableWidgetUtil.createTableColumn(tv_TriggerNodes,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.index"), 100,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((NodeToTrigger) element).index;
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return ((Integer) o1).compareTo((Integer) o2);
					}
				});
		tvc_index.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				String index = "" + ((NodeToTrigger) element).index;
				return index;
			}
		});
		tvc_index.setEditingSupport(new EditingSupport(tv_TriggerNodes) {
			protected void setValue(Object element, Object value) {
				if (value == null)
					return;
				((NodeToTrigger) element).index = (Integer) value;
				tv_TriggerNodes.update(element, null);
				editor.setDirty(true);
				// tv_TriggerNodes.refresh(element);
			}

			protected Object getValue(Object element) {
				return ((NodeToTrigger) element).index;
			}

			protected CellEditor getCellEditor(Object element) {
				ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(tv_TriggerNodes.getTable(),
						SWT.READ_ONLY);
				cellEditor.setLabelProvider(new LabelProvider());
				cellEditor.setContentProvider(new ArrayContentProvider());
				Node node = opcServer.getServerInstance().getAddressSpaceManager()
						.getNodeById(((NodeToTrigger) element).nodeId);
				if (node instanceof UAVariableNode) {
					// read arraydimensions of selected node
					List<Integer> input = new ArrayList<Integer>();
					input.add(-1);
					if (((UAVariableNode) node).getArrayDimensions() != null) {
						UnsignedInteger[] dim = ((UAVariableNode) node).getArrayDimensions();
						if (dim.length == 1) {
							for (int i = 0; i < dim[0].intValue(); i++) {
								input.add(i);
							}
						}
					}
					cellEditor.setInput(input);
					return cellEditor;
				}
				return null;
			}

			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableColumn tblclmnIndex = tvc_index.getColumn();
		tblclmnIndex.setWidth(100);
		tblclmnIndex.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.index"));
		TableViewerColumn tvc_triggername = TableWidgetUtil.createTableColumn(tv_TriggerNodes,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.trigger.triggername"), 100,
				new TableWidgetUtil().new AbstractCustomComperator() {
					@Override
					public Object getComparableObject(Object element) {
						return ((NodeToTrigger) element).triggerName;
					}

					@Override
					public int doCompare(Object o1, Object o2) {
						return o1.toString().compareTo(o2.toString());
					}
				});
		tvc_triggername.setEditingSupport(new EditingSupport(tv_TriggerNodes) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected Object getValue(Object element) {
				return ((NodeToTrigger) element).triggerName;
			}

			protected void setValue(Object element, Object value) {
				((NodeToTrigger) element).triggerName = value.toString();
				editor.setDirty(true);
				tv_TriggerNodes.refresh(element);
			}

			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tv_TriggerNodes.getTable());
				return tce;
			}
		});
		tvc_triggername.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				return ((NodeToTrigger) element).triggerName;
			}
		});
		TableColumn tblclmnTriggername = tvc_triggername.getColumn();
		tblclmnTriggername.setWidth(100);
		tblclmnTriggername.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.trigger.triggername"));
		tv_TriggerNodes.setInput(triggernodes);
		Button btn_add = new Button(sctnComposite, SWT.NONE);
		btn_add.setToolTipText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.trigger.addtrigger"));
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add.widthHint = 60;
		gd_btn_add.heightHint = 60;
		btn_add.setLayoutData(gd_btn_add);
		btn_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OPCUANodeDialog dialog = new OPCUANodeDialog(Display.getCurrent().getActiveShell());
				dialog.setInternalServer(opcServer.getServerInstance());
				dialog.setSelectedNodeId(null);
				// dialog.setStartNodeId(Identifiers.ObjectsFolder);
				dialog.setFormTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"widget.advanced.opcuanodes"));
				if (dialog.open() == Dialog.OK) {
					NodeToTrigger node = new NodeToTrigger();
					node.nodeId = dialog.getSelectedNodeId();
					node.displayname = dialog.getSelectedDisplayName();
					node.active = true;
					triggernodes.add(node);
					tv_TriggerNodes.refresh();
					// updateTriggerNodes();
					handler.compute();
					editor.setDirty(true);
				}
			}
		});
		btn_add.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.ADD));
		Button btn_delete = new Button(sctnComposite, SWT.NONE);
		btn_delete.setToolTipText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "widget.trigger.deletetrigger"));
		GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btn_delete.heightHint = 60;
		gd_btn_delete.widthHint = 60;
		btn_delete.setLayoutData(gd_btn_delete);
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sel = (StructuredSelection) tv_TriggerNodes.getSelection();
				Object[] selections = sel.toArray();
				if (selections != null) {
					for (Object o : selections) {
						triggernodes.remove(o);
					}
					tv_TriggerNodes.refresh();
					handler.compute();
					// updateTriggerNodes();
					editor.setDirty(true);
				}
			}
		});
		btn_delete.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24,
				CommonImagesActivator.DELETE));
	}

	public void fillTriggernodes() {
		this.triggernodes.clear();
		String triggerpath = new Path(
				((OPCUADriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getDriverPath())
						.append("triggernodes.com").toOSString();
		IFileSystem filesystem = ((OPCUADriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getFileSystem();
		if (!filesystem.isFile(triggerpath)) {
			return;
		}
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		List<NodeToTrigger> triggers = importer.importTriggerNodes(opcServer.getServerInstance().getNamespaceUris(),
				filesystem, triggerpath);
		this.triggernodes.addAll(triggers);
		this.tv_TriggerNodes.refresh();
	}

	private void updateTriggerNodes() {
		// now also try to update datapoint editor
		IEditorReference[] refs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		Object i = tv_TriggerNodes.getInput();
		List<NodeToTrigger> possibleTriggerNodes = new ArrayList<NodeToTrigger>();
		// now generate all possible trigger nodes to update
		if (i instanceof ArrayList<?>) {
			NodeToTrigger dummy = new NodeToTrigger();
			dummy.active = true;
			dummy.nodeId = null;
			dummy.displayname = "";
			possibleTriggerNodes.add(0, dummy);
			for (NodeToTrigger n : (List<NodeToTrigger>) i) {
				// if (n.active)
				possibleTriggerNodes.add(n);
			}
		}
		for (IEditorReference ref : refs) {
			IEditorPart editor = ref.getEditor(true);
			try {
				// if (editor.getClass() == this.editor.getClass()) {
				IEditorInput input = ref.getEditorInput();
				if (editor instanceof IOPCDataPointEditPart && input != null && input instanceof OPCUADPEditorInput) {
					if (((OPCUADPEditorInput) input).getDriverName().compareTo(
							((OPCUADriverEditorInput) ((EditorPart) this.editor).getEditorInput()).getDriverName()) == 0
							&& ((OPCUADPEditorInput) input).getServerName()
									.compareTo(((OPCUADriverEditorInput) ((EditorPart) this.editor).getEditorInput())
											.getServerName()) == 0) {
						((IOPCDataPointEditPart) editor).fillTriggerNodes(possibleTriggerNodes);
					}
				}
				// }
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

	public TableViewer getTriggerViewer() {
		return this.tv_TriggerNodes;
	}

	public interface TriggerExpansionListener {
		public void compute();
	}
}
