package com.bichler.astudio.opcua.opcmodeler.wizards.create.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;

public class MethodArgumentPage extends WizardPage {
	class MethodTreeItem {
		// private boolean isInit;
		private Argument argument = null;

		public MethodTreeItem() {
			// this.isInit = false;
			this.argument = new Argument();
			// init argument
			this.argument.setName("Arg");
			this.argument.setArrayDimensions(new UnsignedInteger[0]);
			this.argument.setDataType(BuiltinType.Null.getBuildinTypeId());
			this.argument.setDescription(LocalizedText.EMPTY);
			this.argument.setValueRank(ValueRanks.Scalar.getValue());
		}

		public MethodTreeItem(Argument argument) {
			this.argument = argument;
		}

		public String getName() {
			return this.argument.getName();
		}

		// public NodeId getType() {
		// return this.argument.getTypeId();
		// }
		public NodeId getDataType() {
			return this.argument.getDataType();
		}

		public Integer getValueRank() {
			return this.argument.getValueRank();
		}

		public String getArrayDimensions() {
			UnsignedInteger[] arrayDim = this.argument.getArrayDimensions();
			String arrayDims = "";
			if (arrayDim != null && arrayDim.length > 0) {
				arrayDims = MultiDimensionArrayUtils.toString(arrayDim);
			}
			return arrayDims;
		}

		public LocalizedText getDescription() {
			return this.argument.getDescription();
		}

		public void setName(String value) {
			this.argument.setName(value);
		}

		public void setDataType(NodeId value) {
			this.argument.setDataType(value);
		}

		public void setValueRank(ValueRanks value) {
			this.argument.setValueRank(value.getValue());
		}

		public void setDescription(String value) {
			this.argument.setDescription(new LocalizedText(value, Locale.getDefault()));
		}

		public void setArrayDimension(String value) {
			if (value != null) {
				try {
					UnsignedInteger[] dim = new UnsignedInteger[0];
					String val = value.replace("[", "").replace("]", "");
					if (!val.isEmpty()) {
						String[] indizes = val.split(",");
						dim = new UnsignedInteger[indizes.length];
						for (int i = 0; i < indizes.length; i++) {
							// if(!indizes[i].replace("[", "").replace("]", "").isEmpty())
							dim[i] = UnsignedInteger.parseUnsignedInteger(indizes[i]);
						}
					}
					this.argument.setArrayDimensions(dim);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}
		}
	}

	private static class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private static class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			String value = "...";
			switch (columnIndex) {
			case 0:
				String name = ((MethodTreeItem) element).getName();
				if (name == null) {
					break;
				}
				value = name;
				break;
			case 1:
				NodeId dataType = ((MethodTreeItem) element).getDataType();
				Node datatypeNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(dataType);
				// skip
				if (datatypeNode == null) {
					break;
				}
				value = datatypeNode.getBrowseName().toString();
				break;
			case 2:
				Integer valueRank = ((MethodTreeItem) element).getValueRank();
				if (valueRank == null) {
					break;
				}
				value = ValueRanks.getValueRanks(valueRank).toString();
				break;
			case 3:
				value = ((MethodTreeItem) element).getArrayDimensions();
				if (value == null) {
					break;
				}
				break;
			case 4:
				LocalizedText description = ((MethodTreeItem) element).getDescription();
				if (description == null) {
					break;
				}
				value = description.getText();
				break;
			}
			return value;
		}
	}

	class AddItemListener extends SelectionAdapter {
		private TableViewer viewer;
		private List<MethodTreeItem> arguments;

		public AddItemListener(TableViewer viewer, List<MethodTreeItem> arguments) {
			this.viewer = viewer;
			this.arguments = arguments;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			MethodTreeItem item = new MethodTreeItem();
			this.viewer.add(item);
			this.arguments.add(item);
		}
	}

	class RemoveItemListener extends SelectionAdapter {
		private TableViewer viewer;
		private List<MethodTreeItem> arguments;

		public RemoveItemListener(TableViewer viewer, List<MethodTreeItem> arguments) {
			this.viewer = viewer;
			this.arguments = arguments;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			boolean remove = MessageDialog.openConfirm(getShell(),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.delete"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.method.delete.arg"));
			if (remove) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				MethodTreeItem element = (MethodTreeItem) selection.getFirstElement();
				this.viewer.remove(element);
				this.arguments.remove(element);
			}
		}
	}

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private List<MethodTreeItem> inputElements = new ArrayList<>();
	private List<MethodTreeItem> outputElements = new ArrayList<>();

	/**
	 * Create the wizard.
	 */
	public MethodArgumentPage() {
		super("methodargumentpage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.method.attribute"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.method.arg.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		setControl(container);
		ScrolledForm form = formToolkit.createScrolledForm(container);
		form.setExpandHorizontal(true);
		form.setExpandVertical(true);
		form.getBody().setLayout(new GridLayout(1, false));
		createSection(form.getBody());
	}

	private void createSection(Composite container) {
		createInputSection(container);
		createOutputSection(container);
	}

	private void createInputSection(Composite container) {
		Section sctnInput = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		sctnInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(sctnInput);
		sctnInput.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.method.arg.input"));
		sctnInput.setExpanded(true);
		Composite scomp = formToolkit.createComposite(sctnInput, SWT.NONE);
		formToolkit.paintBordersFor(scomp);
		sctnInput.setClient(scomp);
		scomp.setLayout(new GridLayout(2, false));
		TableViewer tableViewer = new TableViewer(scomp, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		createTable(tableViewer);
		Composite composite = new Composite(scomp, SWT.NONE);
		composite.setBounds(0, 0, 64, 64);
		composite.setLayout(new GridLayout(1, false));
		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdd.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.add"));
		btnAdd.addSelectionListener(new AddItemListener(tableViewer, this.inputElements));
		Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemove.setBounds(0, 0, 90, 30);
		btnRemove.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.delete"));
		btnRemove.addSelectionListener(new RemoveItemListener(tableViewer, this.inputElements));
		fillSection(tableViewer, this.inputElements);
	}

	private void createOutputSection(Composite container) {
		Section sctnOutput = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		sctnOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(sctnOutput);
		sctnOutput.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.method.arg.output"));
		sctnOutput.setExpanded(true);
		Composite scomp = formToolkit.createComposite(sctnOutput, SWT.NONE);
		formToolkit.paintBordersFor(scomp);
		sctnOutput.setClient(scomp);
		scomp.setLayout(new GridLayout(2, false));
		TableViewer tableViewer = new TableViewer(scomp, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		createTable(tableViewer);
		Composite composite = new Composite(scomp, SWT.NONE);
		composite.setBounds(0, 0, 64, 64);
		composite.setLayout(new GridLayout(1, false));
		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdd.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.add"));
		btnAdd.addSelectionListener(new AddItemListener(tableViewer, this.outputElements));
		Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemove.setBounds(0, 0, 90, 30);
		btnRemove.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.delete"));
		btnRemove.addSelectionListener(new RemoveItemListener(tableViewer, this.outputElements));
		fillSection(tableViewer, this.outputElements);
	}

	private void fillSection(TableViewer tableViewer, List<MethodTreeItem> elements) {
		for (MethodTreeItem element : elements) {
			tableViewer.add(element);
		}
	}

	private void createTable(TableViewer tableViewer) {
		tableViewer.getTable().setHeaderVisible(true);
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tc_name = tableViewerColumn.getColumn();
		tc_name.setWidth(100);
		tc_name.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.name"));
		tableViewerColumn.setEditingSupport(new EditingSupport(tableViewer) {
			// private TextCellEditor editor = null;
			@Override
			protected void setValue(Object element, Object value) {
				((MethodTreeItem) element).setName((String) value);
				getViewer().update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				return ((MethodTreeItem) element).getName();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// if (editor == null) {
				TextCellEditor editor = new TextCellEditor((Composite) getViewer().getControl());
				// }
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tc_datatype = tableViewerColumn_1.getColumn();
		tc_datatype.setWidth(100);
		tc_datatype.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "OmronTcpTypePart.lbl_varType.text"));
		tableViewerColumn_1.setEditingSupport(new EditingSupport(tableViewer) {
			// private ComboBoxViewerCellEditor editor;
			@Override
			protected void setValue(Object element, Object value) {
				if (value == null) {
					return;
				}
				((MethodTreeItem) element).setDataType((NodeId) ((Object[]) value)[0]);
				getViewer().update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				return ((MethodTreeItem) element).getDataType();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// if (this.editor == null) {
				ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl());
				editor.setContentProvider(new IStructuredContentProvider() {
					@Override
					public Object[] getElements(Object inputElement) {
						ArrayList<Object> elements = new ArrayList<Object>();
						HashMap<NodeId, String> map = ((HashMap<NodeId, String>) inputElement);
						for (NodeId nid : map.keySet()) {
							elements.add(new Object[] { nid, map.get(nid) });
						}
						return elements.toArray();
						// return null;
					}
				});
				editor.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						// TODO Auto-generated method stub
						return super.getText(((Object[]) element)[1]);
					}
				});
				// set all data types from opc ua
				editor.setInput(DesignerUtils.fetchAllDatatypes());
				// editor.setInput(BuiltinType.values());
				// }
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tc_valueRank = tableViewerColumn_2.getColumn();
		tc_valueRank.setWidth(100);
		tc_valueRank.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariableTypePart.lbl_valueRank.text"));
		tableViewerColumn_2.setEditingSupport(new EditingSupport(tableViewer) {
			// private ComboBoxViewerCellEditor editor;
			@Override
			protected void setValue(Object element, Object value) {
				if (value == null) {
					return;
				}
				((MethodTreeItem) element).setValueRank((ValueRanks) value);
				getViewer().update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				int valueRank = ((MethodTreeItem) element).getValueRank();
				return ValueRanks.getValueRanks(valueRank);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// if (this.editor == null) {
				ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl());
				editor.setContentProvider(new ArrayContentProvider());
				editor.setInput(ValueRanks.values());
				// }
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tc_arrayDim = tableViewerColumn_3.getColumn();
		tc_arrayDim.setWidth(100);
		tc_arrayDim.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.dimension"));
		tableViewerColumn_3.setEditingSupport(new EditingSupport(tableViewer) {
			private TextCellEditor editor;

			@Override
			protected void setValue(Object element, Object value) {
				((MethodTreeItem) element).setArrayDimension((String) value);
				getViewer().update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				return ((MethodTreeItem) element).getArrayDimensions();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (this.editor == null) {
					this.editor = new TextCellEditor((Composite) getViewer().getControl());
				}
				return this.editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tc_description = tableViewerColumn_4.getColumn();
		tc_description.setWidth(120);
		tc_description.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_description.text"));
		tableViewerColumn_4.setEditingSupport(new EditingSupport(tableViewer) {
			// private TextCellEditor editor;
			@Override
			protected void setValue(Object element, Object value) {
				((MethodTreeItem) element).setDescription((String) value);
				getViewer().update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				return ((MethodTreeItem) element).getDescription().getText();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// if (this.editor == null) {
				TextCellEditor editor = new TextCellEditor((Composite) getViewer().getControl());
				// }
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
	}

	@Override
	public boolean isPageComplete() {
		return true;
	}

	public Argument[] getInputArguments() {
		List<Argument> args = new ArrayList<>();
		for (MethodTreeItem item : this.inputElements) {
			args.add(item.argument);
		}
		return args.toArray(new Argument[0]);
	}

	public Argument[] getOutputArguments() {
		List<Argument> args = new ArrayList<>();
		for (MethodTreeItem item : this.outputElements) {
			args.add(item.argument);
		}
		return args.toArray(new Argument[0]);
	}

	public void setInputArguments(Argument... args) {
		List<MethodTreeItem> items = new ArrayList<>();
		for (Argument arg : args) {
			MethodTreeItem newItem = new MethodTreeItem(arg);
			items.add(newItem);
		}
		this.inputElements = items;
	}

	public void setOutputArguments(Argument... args) {
		List<MethodTreeItem> items = new ArrayList<>();
		for (Argument arg : args) {
			MethodTreeItem newItem = new MethodTreeItem(arg);
			items.add(newItem);
		}
		this.outputElements = items;
	}
}
