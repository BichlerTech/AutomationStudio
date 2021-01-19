package com.bichler.astudio.editor.events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import opc.sdk.server.core.UAServerApplicationInstance;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.editor.events.dnd.EventsDPDnDViewAdapter;
import com.bichler.astudio.editor.events.xml.EVENTENTRYTYPE;
import com.bichler.astudio.editor.events.xml.EventEntryModelNode;
import com.bichler.astudio.editor.events.xml.EventsExporter;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.opcua.OPCImagesActivator;
import com.bichler.astudio.opcua.AbstractOPCTriggerNodeEditPart;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;
import com.hbsoft.driver.opc.events.dp.AbstractDatapointModel;
import com.hbsoft.driver.opc.events.dp.AbstractEventModel;
import com.hbsoft.driver.opc.events.dp.EventDefItemModel;
import com.hbsoft.driver.opc.events.dp.EventDpItemModel;
import com.hbsoft.driver.opc.events.expression.AbstractExpressionModel;
import com.hbsoft.driver.opc.events.expression.group.GroupExpressionModel;
import com.hbsoft.driver.opc.events.expression.io.AbstractIOExpressionModel;
import com.hbsoft.driver.opc.events.expression.io.AbstractValueOperatorModel;
import com.hbsoft.driver.opc.events.expression.io.ValueExpressionModel;
import com.hbsoft.driver.opc.events.expression.operator.AbstractFunctionModel;
import com.hbsoft.driver.opc.events.expression.operator.AbstractLinkOperatorModel;
import com.hbsoft.driver.opc.events.model.EventsImporter;
import com.hbsoft.driver.opc.events.model.ExpressionModelFactory;
import com.hbsoft.driver.opc.events.model.ExpressionType;
import com.hbsoft.driver.opc.events.model.ExpressionType.ExpressionConstants;

public class EventsDPEditor extends AbstractOPCTriggerNodeEditPart {
	public static final String ID = "com.bichler.astudio.editor.events.EventsDPEditor";
	private ISelectionChangedListener listener;
	private Object currentSelection;
	private boolean dirty;
	private TreeViewer treeViewer;
	private UAServerApplicationInstance opcServer;
	private Button btnAddEventRoot;
	private Button btnAddEvent;
	private Button btnRemove;
	private Button btnAddEventExpression;

	public EventsDPEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		@SuppressWarnings("unchecked")
		List<EventEntryModelNode> items = (List<EventEntryModelNode>) this.treeViewer.getInput();
		List<IDriverNode> items2export = new ArrayList<>();
		items2export.addAll(items);
		String configPath = getEditorInput().getDPConfigFile();
		EventsExporter exporter = new EventsExporter(getEditorInput().getFilesystem(), configPath);
		StringBuffer builder = exporter.build(items2export, opcServer.getServerInstance().getNamespaceUris());

		exporter.write(getEditorInput().getFilesystem(), configPath, builder);
		setDirty(false);
		OPCUAUtil.validateOPCUADriver(getEditorInput().getFilesystem(), getEditorInput().getNode());
	}

	@Override
	public void doSaveAs() {
		// we only save the editor input with the default name
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					monitor.beginTask("Laden der Events ...", IProgressMonitor.UNKNOWN);
					monitor.subTask("Initialisiere ...");
					try {
						setSite(site);
						setInput(input);
						setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName()
								+ " - Datenpunkte");
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
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
						}
						getEditorInput().setToolTipText("Events - Connect Events with Model Nodes");
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
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
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

	public void createPage1(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		createTreeViewer(parent);
		createMenu(parent);
		fillTree();
		this.treeViewer.expandAll();
		registerContextMenu();
		setHandler();
		getSite().setSelectionProvider(this.treeViewer);
	}

	@Override
	public void setFocus() {
		this.treeViewer.getTree().setFocus();
	}

	@Override
	public OPCUADPEditorInput getEditorInput() {
		return (OPCUADPEditorInput) super.getEditorInput();
	}

	@Override
	public void fillTriggerNodes(List<NodeToTrigger> nodesToTrigger) {
		// TODO Auto-generated method stub
	}

	@Override
	public Control getDPControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISelectionProvider getDPViewer() {
		// TODO Auto-generated method stub
		return null;
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
	public void refreshDatapoints() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFocusRemoteView() {
		DriverBrowserUtil.openEmptyDriverModelView();
	}

	@Override
	public void setDataPointSelection(Object element) {
		this.currentSelection = element;
		if (this.currentSelection != null) {
			if (this.currentSelection instanceof EventEntryModelNode) {
				EVENTENTRYTYPE type = ((EventEntryModelNode) this.currentSelection).getType();
				switch (type) {
				case DATAPOINT:
					this.btnAddEvent.setEnabled(true);
					break;
				case EVENT:
					this.btnAddEvent.setEnabled(true);
					this.btnAddEventExpression.setEnabled(true);
					break;
				default:
					break;
				}
			} else {
				this.btnAddEventExpression.setEnabled(false);
			}
			selectionChanged();
		}
	}

	protected void selectionChanged() {
		if (this.listener == null) {
			return;
		}
		listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	private void createTreeViewer(Composite parent) {
		this.treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TreeViewerColumn tvc_value = createTreeColumn("Value", 150);
		createTreeColumn("Name", 150);
		TreeViewerColumn tvc_message = createTreeColumn("Message", 150);
		// image
		// createTreeColumn("Type", 75);
		// TreeViewerColumn tvc_operator = createTreeColumn("Operator", 100);
		// TreeViewerColumn tvc_value = createTreeColumn("Value", 150);
		// LOWEST = 1
		// HIGHEST = 1000
		TreeViewerColumn tvc_severity = createTreeColumn("Severity", 100);
		// createTreeColumn("Variable", 150);
		// tvc_operator.setEditingSupport(new EditingSupport(this.treeViewer) {
		//
		// // ComboBoxCellEditor cellEditor =
		//
		// @Override
		// protected void setValue(Object element, Object value) {
		// AbstractEventModel item = ((EventEntryModelNode) element).getItem();
		//
		// // if (item instanceof EventDpSyntax) {
		// // ExpressionOperator operand =
		// // ExpressionOperator.values()[((Integer) value)];
		// // ((EventDpSyntax) ((EventEntryModelNode) element).getItem())
		// // .setOperation(operand.name());
		// // } else if (item instanceof EventDpExpression) {
		// // ExpressionType operationvalue =
		// // ExpressionType.values()[((Integer) value)];
		// // // ((EventDpExpression) ((EventEntryModelNode) element)
		// // // .getItem()).setType(operationvalue);
		// // }
		//
		// treeViewer.update(element, null);
		// setDirty(true);
		// }
		//
		// @Override
		// protected Object getValue(Object element) {
		// AbstractEventModel item = ((EventEntryModelNode) element).getItem();
		//
		// // if (item instanceof EventDpSyntax) {
		// // String operation = ((EventDpSyntax) item)
		// // .getOperationType();
		// //
		// // ExpressionOperator o = ExpressionOperator.onChange;
		// // try {
		// // o = ExpressionOperator.valueOf(operation);
		// // } catch (IllegalArgumentException e) {
		// // e.printStackTrace();
		// // }
		// // return o.ordinal();
		// // }
		// // else if (item instanceof EventDpExpression) {
		// // ExpressionType operation = ((EventDpExpression) item)
		// // .getType();
		// // return operation.ordinal();
		// // }
		//
		// return null;
		// }
		//
		// @Override
		// protected CellEditor getCellEditor(Object element) {
		// AbstractEventModel item = ((EventEntryModelNode) element).getItem();
		//
		// // if (item instanceof EventDpSyntax) {
		// // return new ComboBoxCellEditor(((TreeViewer) treeViewer)
		// // .getTree(), ExpressionOperator.getSyntaxValues(),
		// // SWT.READ_ONLY);
		// // } else if (item instanceof EventDpExpression) {
		// // return new ComboBoxCellEditor(((TreeViewer) treeViewer)
		// // .getTree(), ExpressionType.getValues(),
		// // SWT.READ_ONLY);
		// // }
		//
		// return null;
		// }
		//
		// @Override
		// protected boolean canEdit(Object element) {
		// if (element instanceof EventEntryModelNode) {
		// switch (((EventEntryModelNode) element).getType()) {
		// case Expression:
		// return true;
		// }
		// // }
		// // && (((EventEntryModelNode) element).getType() ==
		// // EventEntryType.Syntax || ((EventEntryModelNode) element)
		// // .getType() == EventEntryType.Expression)) {
		// // return true;
		// }
		// return false;
		// }
		// });
		//
		// tvc_value.setEditingSupport(new EditingSupport(this.treeViewer) {
		//
		// @Override
		// protected void setValue(Object element, Object value) {
		// AbstractEventModel item = ((EventEntryModelNode) element).getItem();
		//
		// // if (item instanceof EventDpSyntax) {
		// // int number = 0;
		// // try {
		// // number = Integer.parseInt((String) value);
		// // } catch (NumberFormatException nfe) {
		// // nfe.printStackTrace();
		// // }
		// //
		// // DataValue datavalue = new DataValue(new Variant(number));
		// // ((EventDpSyntax) item).setValue(datavalue);
		// // }
		//
		// // else if (item instanceof EventDpExpression) {
		// // ((EventDpExpression) item).setValue(ExpressionValue
		// // .values()[(Integer) value]);
		// // }
		// treeViewer.update(element, null);
		// setDirty(true);
		// }
		//
		// @Override
		// protected Object getValue(Object element) {
		// AbstractEventModel item = ((EventEntryModelNode) element).getItem();
		//
		// // if (item instanceof EventDpSyntax) {
		// // DataValue value = ((EventDpSyntax) item).getValue();
		// // if (value == null) {
		// // return "";
		// // }
		// //
		// // Variant variant = value.getValue();
		// // if (variant.isEmpty()) {
		// // return "";
		// // }
		// //
		// // return value.getValue().toString();
		// // }
		//
		// // else if (item instanceof EventDpExpression) {
		// // return ((EventDpExpression) item).getValue().ordinal();
		// // }
		//
		// return null;
		// }
		//
		// @Override
		// protected CellEditor getCellEditor(Object element) {
		// AbstractEventModel item = ((EventEntryModelNode) element).getItem();
		// // if (item instanceof EventDpSyntax) {
		// // return new TextCellEditor(treeViewer.getTree());
		// // }
		//
		// // else if (item instanceof EventDpExpression) {
		// // return new ComboBoxCellEditor(((TreeViewer) treeViewer)
		// // .getTree(), ExpressionValue.getValues(),
		// // SWT.READ_ONLY);
		// // }
		//
		// return null;
		// }
		//
		// @Override
		// protected boolean canEdit(Object element) {
		//
		// if (element instanceof EventEntryModelNode) {
		// switch (((EventEntryModelNode) element).getType()) {
		// case Expression:
		// return true;
		// }
		//
		// // && ((EventEntryModelNode) element).getType() ==
		// // EventEntryType.Syntax) {
		// // return true;
		// }
		//
		// return false;
		// }
		// });
		tvc_value.setEditingSupport(new EditingSupport(this.treeViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof ValueExpressionModel) {
					Object objValue = null;
					if ("true".equalsIgnoreCase((String) value)) {
						objValue = Boolean.parseBoolean((String) value);
					} else if ("false".equalsIgnoreCase((String) value)) {
						objValue = Boolean.parseBoolean((String) value);
					} else {
						try {
							objValue = Float.parseFloat((String) value);
						} catch (NumberFormatException e2) {
						}
					}
					((ValueExpressionModel) item).setVariant(new Variant(objValue));
				}
				setDirty(true);
				treeViewer.update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof ValueExpressionModel) {
					return ((ValueExpressionModel) item).getVariant().toString(false);
				}
				return null;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(treeViewer.getTree());
			}

			@Override
			protected boolean canEdit(Object element) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof ValueExpressionModel) {
					return true;
				}
				return false;
			}
		});
		tvc_message.setEditingSupport(new EditingSupport(this.treeViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof EventDefItemModel) {
					((EventDefItemModel) item).setMessage((String) value);
					setDirty(true);
					treeViewer.update(element, null);
				}
			}

			@Override
			protected Object getValue(Object element) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof EventDefItemModel) {
					return ((EventDefItemModel) item).getMessage();
				}
				return null;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(treeViewer.getTree());
			}

			@Override
			protected boolean canEdit(Object element) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if ((item instanceof EventDefItemModel) && item.getParent() != null) {
					return true;
				}
				return false;
			}
		});
		tvc_severity.setEditingSupport(new EditingSupport(this.treeViewer) {
			@Override
			protected void setValue(Object element, Object value) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof EventDefItemModel) {
					((EventDefItemModel) item).setSeverity(Integer.parseInt(((String) value)));
					setDirty(true);
					treeViewer.update(element, null);
				}
			}

			@Override
			protected Object getValue(Object element) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof EventDefItemModel) {
					return "" + ((EventDefItemModel) item).getSeverity();
				}
				return null;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(treeViewer.getTree());
			}

			@Override
			protected boolean canEdit(Object element) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if ((item instanceof EventDefItemModel) && item.getParent() != null) {
					return true;
				}
				return false;
			}
		});
		this.treeViewer.setContentProvider(new EventDPTreeContentProvider());
		this.treeViewer.setLabelProvider(new EventDPTreeLabelProvider());
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		DragSource dragSource = new DragSource(this.treeViewer.getTree(), operations);
		dragSource.setTransfer(transferTypes);
		dragSource.addDragListener(new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = treeViewer.getTree().getSelection();
				if (selection.length > 0 /*
											 * && selection[0].getItemCount() == 0
											 */) {
					event.doit = true;
				} else {
					event.doit = false;
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				EventEntryModelNode fe = (EventEntryModelNode) ((IStructuredSelection) treeViewer.getSelection())
						.getFirstElement();
				event.data = "%move%" + fe.toString(); // "test";//dragSourceItem[0].getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE) {
					// dragSourceItem[0].dispose();
					// dragSourceItem[0] = null;
				}
			}
		});
		this.treeViewer.addDropSupport(operations, transferTypes, new EventsDPDnDViewAdapter(this.treeViewer, this));
	}

	private TreeViewerColumn createTreeColumn(String name, int width) {
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn tc_event = treeViewerColumn.getColumn();
		tc_event.setWidth(width);
		tc_event.setText(name);
		return treeViewerColumn;
	}

	private void createMenu(Composite parent) {
		Composite cmp_menu = new Composite(parent, SWT.NONE);
		cmp_menu.setLayout(new RowLayout(SWT.HORIZONTAL));
		this.btnAddEventRoot = new Button(cmp_menu, SWT.NONE);
		btnAddEventRoot.setLayoutData(new RowData(48, 48));
		btnAddEventRoot.setText("Add Root");
		this.btnAddEvent = new Button(cmp_menu, SWT.NONE);
		btnAddEvent.setLayoutData(new RowData(48, 48));
		btnAddEvent.setText("Add Event");
		this.btnAddEventExpression = new Button(cmp_menu, SWT.NONE);
		btnAddEventExpression.setLayoutData(new RowData(48, 48));
		btnAddEventExpression.setText("Add Expression");
		this.btnRemove = new Button(cmp_menu, SWT.NONE);
		btnRemove.setLayoutData(new RowData(48, 48));
		btnRemove.setText("Remove");
	}

	private void fillTree() {
		List<EventEntryModelNode> nodes = new ArrayList<>();
		// this.treeViewer.setInput(nodes);
		// ----
		/** open file to import */
		String dpsPath = getEditorInput().getDPConfigFile();
		// List<Events_DPItem> dps = new ArrayList<>();
		if (getEditorInput().getFilesystem().isFile(dpsPath)) {
			// InputStreamReader isr = null;
			InputStream input = null;
			try {
				input = getEditorInput().getFilesystem().readFile(dpsPath);
				// isr = new InputStreamReader(input, "UTF-8");
				// InputSource is = new InputSource(isr);
				NamespaceTable uris = this.opcServer.getServerInstance().getNamespaceUris();
				EventsImporter importer = new EventsImporter();
				List<EventDpItemModel> events = importer.loadDPs(input, uris);
				for (EventDpItemModel event : events) {
					EventEntryModelNode item = EventEntryModelNode.loadFromDP(event, EVENTENTRYTYPE.DATAPOINT);
					nodes.add(item);
				}
				// now set displayname for each model node
				for (EventEntryModelNode datapoint : nodes) {
					EventDpItemModel datapointItem = (EventDpItemModel) datapoint.getItem();
					for (AbstractEventModel eventItem : datapointItem.getChildren()) {
						EventEntryModelNode item = EventEntryModelNode.loadFromDP(eventItem, EVENTENTRYTYPE.EVENT);
						datapoint.addChild(item);
						fillExpression(eventItem, item);
					}
				}
				this.treeViewer.setInput(nodes);
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

	private void fillExpression(AbstractEventModel parent, EventEntryModelNode item) {
		for (AbstractEventModel child : parent.getChildren()) {
			EventEntryModelNode expression = EventEntryModelNode.loadFromDP(child, EVENTENTRYTYPE.EXPRESSION);
			item.addChild(expression);
			if (child.getChildren().length > 0) {
				fillExpression(child, expression);
			}
		}
	}

	private void setHandler() {
		this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object element = ((StructuredSelection) event.getSelection()).getFirstElement();
				setDataPointSelection(element);
			}
		});
		this.treeViewer.getTree().addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// doChangeReference(currentSelection);
			}
		});
		this.btnAddEventRoot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addEventRoot();
			}
		});
		this.btnAddEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Object element = selection.getFirstElement();
				EventEntryModelNode parent = null;
				if (element instanceof EventEntryModelNode) {
					AbstractEventModel item = ((EventEntryModelNode) element).getItem();
					if (item instanceof EventDpItemModel) {
						parent = (EventEntryModelNode) element;
					} else if (item instanceof EventDefItemModel) {
						parent = ((EventEntryModelNode) element).getParent();
					}
				}
				if (parent == null) {
					return;
				}
				addEvent(parent);
			}
		});
		this.btnAddEventExpression.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Object element = selection.getFirstElement();
				EventEntryModelNode parent = null;
				if (element instanceof EventEntryModelNode) {
					AbstractEventModel item = ((EventEntryModelNode) element).getItem();
					if (item instanceof AbstractExpressionModel) {
						parent = ((EventEntryModelNode) element).getParent();
					} else if (item instanceof EventDpItemModel) {
						parent = (EventEntryModelNode) element;
					}
				}
				if (parent == null) {
					return;
				}
				// addEventExpression(parent);
			}
		});
		this.btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeEvent();
			}
		});
	}

	protected void doChangeReference(/* SelectionEvent event, */Object element) {
		// AbstractEventModel item = ((EventEntryModelNode) element).getItem();
		// if (!(item instanceof EventDpSyntax)) {
		// return;
		// }
		// Point location = this.treeViewer.getControl().toControl(event.x,
		// event.y);
		// ViewerCell cell = ((ColumnViewer) this.treeViewer).getCell(location);
		//
		// int index = -1;
		//
		// if (cell != null) {
		// index = cell.getColumnIndex();
		// }
		//
		// if (index <= 0) {
		// return;
		// }
		// ExpressionValue ref = ((EventDpSyntax) item).getReference();
		// switch (ref) {
		// case Beginn:
		// ref = EventReference.And;
		// break;
		// case And:
		// ref = EventReference.Or;
		// break;
		// case Or:
		// ref = EventReference.Beginn;
		// break;
		// }
		// ((EventDpSyntax) item).setReference(ref);
		setDirty(true);
		treeViewer.update(element, null);
	}

	private void registerContextMenu() {
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(this.treeViewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			private TreeMenuOrganizer menuOrganizer = new TreeMenuOrganizer(treeViewer);

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				MenuManager add = new MenuManager("Add", null);
				List<Object> actions2add = this.menuOrganizer.createMenu();
				for (Object action : actions2add) {
					if (action instanceof IAction) {
						add.add((IAction) action);
					} else if (action instanceof IContributionItem) {
						add.add((IContributionItem) action);
					}
				}
				// add actions
				manager.add(add);
				// remove action
				ISelection selection = treeViewer.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				EventEntryModelNode object = (EventEntryModelNode) ((IStructuredSelection) selection).getFirstElement();
				if (object.getItem() instanceof AbstractEventModel) {
					manager.add(this.menuOrganizer.createRemoveAction());
				}
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		this.treeViewer.getControl().setMenu(menu);
	}

	// private void addEventSyntax(EventEntryModelNode parent) {
	//
	// EventDpSyntax syntax = new EventDpSyntax();
	// EventEntryModelNode syntaxNode = EventEntryModelNode.loadFromDP(syntax,
	// EventEntryType.Expression);
	//
	// parent.addChild(syntaxNode);
	//
	// this.treeViewer.refresh(parent);
	// this.treeViewer
	// .setSelection(new StructuredSelection(syntaxNode), false);
	// setDirty(true);
	// }
	void addEventRoot() {
		if (this.treeViewer.getInput() == null) {
			this.treeViewer.setInput(new ArrayList<EventEntryModelNode>());
		}
		@SuppressWarnings("unchecked")
		List<EventEntryModelNode> input = (List<EventEntryModelNode>) this.treeViewer.getInput();
		EventDpItemModel event = new EventDpItemModel();
		EventEntryModelNode newItem = EventEntryModelNode.loadFromDP(event, EVENTENTRYTYPE.DATAPOINT);
		// newitem.setNeighbor(neighbor);
		// newitem.setId(maxNodeId);
		// newitem.setLabelImage(SiemensSharedImages
		// .getImage(SiemensSharedImages.ICON_DATEPOINTLEAF));
		// newitem.setSymbolName("unknown");
		// newitem.setActive(true);
		// newitem.setCycletime(1000);
		// newitem.setAddressType(SiemensAreaCode.DB);
		// newitem.setAddress("0");
		// newitem.setIndex(nIndex);
		// newitem.setDescription("insert comment");
		input.add(newItem);
		this.treeViewer.refresh(input);
		this.treeViewer.setSelection(new StructuredSelection(newItem), false);
		setDirty(true);
	}

	void addEvent(EventEntryModelNode parent) {
		EventDefItemModel eventItem = new EventDefItemModel();
		EventEntryModelNode eventNode = EventEntryModelNode.loadFromDP(eventItem, EVENTENTRYTYPE.EVENT);
		EventEntryModelNode refresh = null;
		switch (parent.getType()) {
		case DATAPOINT:
			parent.addChild(eventNode);
			refresh = parent;
			break;
		case EVENT:
			parent.getParent().addChild(eventNode);
			refresh = parent.getParent();
			break;
		default:
			break;
		}
		this.treeViewer.refresh(refresh);
		this.treeViewer.setSelection(new StructuredSelection(eventNode), false);
		setDirty(true);
	}

	void removeEvent() {
		StructuredSelection sel = (StructuredSelection) this.treeViewer.getSelection();
		Object[] selections = sel.toArray();
		if (selections != null) {
			for (Object o : selections) {
				@SuppressWarnings("unchecked")
				List<EventEntryModelNode> input = (List<EventEntryModelNode>) this.treeViewer.getInput();
				remove(input, (EventEntryModelNode) o);
				// ((List<EventEntryModelNode>) this.treeViewer.getInput())
				// .remove(o);
			}
			// this.treeViewer.remove(selections);
			this.treeViewer.refresh();
			setDirty(true);
		}
	}

	private void remove(List<EventEntryModelNode> siblings, EventEntryModelNode obj2remove) {
		boolean removed = siblings.remove(obj2remove);
		if (removed) {
			return;
		}
		for (EventEntryModelNode node : siblings) {
			remove(node.getChildrenList(), obj2remove);
		}
	}

	class EventDPTreeContentProvider implements ITreeContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof List<?>) {
				return ((List<?>) parentElement).toArray();
			} else if (parentElement instanceof EventEntryModelNode) {
				return ((EventEntryModelNode) parentElement).getChildren();
			}
			return null;
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

	class EventDPTreeLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				AbstractEventModel item = ((EventEntryModelNode) element).getItem();
				if (item instanceof EventDefItemModel) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.EVENT);
				} else if (item instanceof EventDpItemModel) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.OBJECT);
				} else if (item instanceof AbstractIOExpressionModel) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.VARIABLE);
				} else if (item instanceof AbstractValueOperatorModel) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.BOOK);
				} else if (item instanceof AbstractFunctionModel) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.BOOK);
				} else if (item instanceof AbstractLinkOperatorModel) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.BOOK);
				} else if (item instanceof GroupExpressionModel) {
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.BOOK);
				}
			}
			return super.getImage(element);
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			AbstractEventModel item = ((EventEntryModelNode) element).getItem();
			if (item instanceof AbstractDatapointModel) {
				switch (columnIndex) {
				case 0:
					return ((AbstractDatapointModel) item).getNodeId().toString();
				case 1:
					return ((AbstractDatapointModel) item).getDisplayname();
				default:
					if (item instanceof EventDefItemModel) {
						switch (columnIndex) {
						// case 2:
						// return ""
						// + ((EventDefItemModel) item).getSeverity();
						case 2:
							return ((EventDefItemModel) item).getMessage();
						case 3:
							return "" + ((EventDefItemModel) item).getSeverity();
						}
					}
					return null;
				}
			}
			if (item instanceof AbstractExpressionModel) {
				switch (columnIndex) {
				case 1:
					return ((AbstractExpressionModel) item).getType().name();
				case 0:
					return ((AbstractExpressionModel) item).getStringVariant();
				default:
					return "";
				}
			}
			return "error";
		}
	}

	class TreeMenuOrganizer {
		private TreeViewer viewer;

		public TreeMenuOrganizer(TreeViewer viewer) {
			this.viewer = viewer;
		}

		List<Object> createMenu() {
			// event root action
			List<Object> menuActions = new ArrayList<>();
			Action addEventRootAction = new Action() {
				@Override
				public void run() {
					addEventRoot();
				}
			};
			addEventRootAction.setText("Add Root");
			ISelection selection = treeViewer.getSelection();
			if (selection.isEmpty()) {
				menuActions.add(addEventRootAction);
			} else if (treeViewer.getSelection() instanceof IStructuredSelection) {
				final EventEntryModelNode object = (EventEntryModelNode) ((IStructuredSelection) selection)
						.getFirstElement();
				Action addEventAction = new Action() {
					@Override
					public void run() {
						addEvent(object);
					}
				};
				addEventAction.setText("Add Event");
				// event
				if (object.getItem() instanceof EventDpItemModel) {
					menuActions.add(addEventRootAction);
					menuActions.add(addEventAction);
				}
				// event
				else if (object.getItem() instanceof EventDefItemModel) {
					menuActions.add(addEventAction);
					// menuActions.add(new Separator());
					List<Object> actions = createMenuForExpression(object);
					menuActions.addAll(actions);
				}
				// expressions
				else if (object.getItem() instanceof AbstractExpressionModel) {
					List<Object> actions = createMenuForExpression(object);
					menuActions.addAll(actions);
				}
			}
			return menuActions;
		}

		Action createRemoveAction() {
			Action removeAction = new Action() {
				@Override
				public void run() {
					removeEvent();
				}
			};
			removeAction.setText("Remove");
			return removeAction;
		}

		List<Object> createDefaultActions(EventEntryModelNode parent) {
			List<Object> menuActions = new ArrayList<>();
			for (ExpressionType value : ExpressionType.getExpressions(ExpressionConstants.LEVEL_DEFAULT)) {
				switch (value) {
				case Undefinded:
					break;
				default:
					Action action = createExpressionAction(parent, value);
					menuActions.add(action);
					break;
				}
			}
			return menuActions;
		}

		Object createFunctionActions(EventEntryModelNode parent) {
			MenuManager subMenu = new MenuManager("Functions", null);
			for (ExpressionType value : ExpressionType.getExpressions(ExpressionConstants.LEVEL_FUNCTIONS)) {
				switch (value) {
				case Undefinded:
					break;
				default:
					Action action = createExpressionAction(parent, value);
					subMenu.add(action);
					break;
				}
			}
			return subMenu;
		}

		// private void addEventExpression(EventEntryModelNode parent) {
		// EventDpExpression syntax = new EventDpExpression();
		// EventEntryModelNode syntaxNode = EventEntryModelNode.loadFromDP(
		// syntax, EventEntryType.Expression);
		//
		// parent.addChild(syntaxNode);
		//
		// this.viewer.refresh(parent);
		// this.viewer
		// .setSelection(new StructuredSelection(syntaxNode), false);
		// setDirty(true);
		// }
		private List<Object> createMenuForExpression(EventEntryModelNode parent) {
			List<Object> menuActions = new ArrayList<>();
			if (parent.getItem() instanceof EventDefItemModel) {
				List<Object> allActions = createDefaultActions(parent);
				menuActions.addAll(allActions);
				Object functions = createFunctionActions(parent);
				menuActions.add(functions);
			} else if (parent.getItem() instanceof AbstractExpressionModel) {
				switch (((AbstractExpressionModel) parent.getItem()).getType()) {
				case And:
					break;
				case Or:
					break;
				case Group:
					List<Object> allActions = createDefaultActions(parent);
					menuActions.addAll(allActions);
					Object functions = createFunctionActions(parent);
					menuActions.add(functions);
					break;
				case Input:
					break;
				case Output:
					break;
				case Value:
					break;
				default:
					break;
				}
			}
			return menuActions;
		}

		private Action createExpressionAction(final EventEntryModelNode parent, final ExpressionType expression) {
			Action expressionAction = new Action() {
				@Override
				public void run() {
					AbstractEventModel newExpression = ExpressionModelFactory.instance.create(expression, true);
					EventEntryModelNode child = EventEntryModelNode.loadFromDP(newExpression,
							EVENTENTRYTYPE.EXPRESSION);
					parent.addChild(child);
					fillExpression(newExpression, child);
					viewer.refresh(parent);
					viewer.setSelection(new StructuredSelection(newExpression), false);
					setDirty(true);
				}
			};
			expressionAction.setText(expression.name());
			return expressionAction;
		}
	}
}
