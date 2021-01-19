package com.bichler.astudio.editor.pubsub.wizard.page;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.opcfoundation.ua.builtintypes.QualifiedName;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;
import com.bichler.astudio.images.opcua.OPCImagesActivator;

public abstract class DetailPropertiesPage extends AbstractDetailWizardPage {

	protected List<WrapperKeyValuePair> model = null;
	private Button btn_add;
	private TableViewer tableViewer;

	DetailPropertiesPage() {
		super("propertiespage");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		createTableViewer(container);

		createAdd(container);

		createApply(container);
		GridData gd = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 3,1);
		btn_apply.setLayoutData(gd);
		
		setDefaultValues();
		setHandler();
	}

	private void createAdd(Composite container) {
		new Label(container, SWT.NONE);

		this.btn_add = new Button(container, SWT.CENTER);
		GridData gdAdd = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 3, 1);
		gdAdd.widthHint = 125;
		btn_add.setLayoutData(gdAdd);
		btn_add.setText("Add");
	}

	private void createTableViewer(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		composite.setLayout(new TableColumnLayout());

		this.tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer.setContentProvider(new ArrayContentProvider());

		createTableViewerColumns(tableViewer);
	}

	private void createTableViewerColumns(TableViewer viewer) {
		TableColumnLayout layout = (TableColumnLayout) ((Composite) viewer.getControl().getParent()).getLayout();

		TableViewerColumn col1 = new TableViewerColumn(viewer, SWT.NONE);
		col1.getColumn().setText("Key");
		layout.setColumnData(viewer.getTable().getColumn(0), new ColumnWeightData(1));
		col1.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WrapperKeyValuePair p = (WrapperKeyValuePair) element;
				return (p.getKey() == null) ? "" : p.getKey().toString();
			}
		});
		col1.setEditingSupport(new EditingSupport(col1.getViewer()) {
			
			@Override
			protected void setValue(Object element, Object value) {
				if(value == null) {
					((WrapperKeyValuePair) element).setKey(null);
				}
				else if(((String) value).isEmpty()) {
					((WrapperKeyValuePair) element).setKey(null);
				}
				else {
					((WrapperKeyValuePair) element).setKey(new QualifiedName((String) value));
				}
				
				getViewer().update(element, null);
			}
			
			@Override
			protected Object getValue(Object element) {
				if(((WrapperKeyValuePair) element).getKey() == null){
					return "";
				}
				
				return ((WrapperKeyValuePair) element).getKey().getName();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				
				return new TextCellEditor(viewer.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		TableViewerColumn col2 = new TableViewerColumn(viewer, SWT.NONE);
		col2.getColumn().setText("Value");
		layout.setColumnData(viewer.getTable().getColumn(1), new ColumnWeightData(1));
		col2.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WrapperKeyValuePair p = (WrapperKeyValuePair) element;
				return (p.getValue() == null) ? "" : p.getValue().toString();
			}
		});

		TableViewerColumn col3 = new TableViewerColumn(viewer, SWT.NONE);
		col3.getColumn().setText("");
		layout.setColumnData(viewer.getTable().getColumn(2), new ColumnPixelData(20));
		
		col3.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.OBJECT);
			}

		});

	}

	private void setDefaultValues() {
		this.model = WrapperKeyValuePair.clone(getKeyValuePair());
		this.tableViewer.setInput(this.model);
		/*for (WrapperKeyValuePair property : this.model) {
			addKeyValuePair(property);
		}*/
	}
	
	protected void setModelValue(WrapperKeyValuePair[] values) {
		this.model = WrapperKeyValuePair.clone(values);
		this.tableViewer.setInput(this.model);
	}

	private void setHandler() {
		btn_apply.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model = WrapperKeyValuePair.clone(model.toArray(new WrapperKeyValuePair[0]));
				tableViewer.setInput(model);
			}

		});

		btn_add.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				WrapperKeyValuePair element = new WrapperKeyValuePair();
				model.add(element);
				tableViewer.add(element);
				
			}

		});
	}

	abstract WrapperKeyValuePair[] getKeyValuePair();

	public WrapperKeyValuePair[] getProperties() {
		return this.model.toArray(new WrapperKeyValuePair[0]);
	}
}
