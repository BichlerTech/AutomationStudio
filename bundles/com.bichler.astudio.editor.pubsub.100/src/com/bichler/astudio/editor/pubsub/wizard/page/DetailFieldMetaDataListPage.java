package com.bichler.astudio.editor.pubsub.wizard.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataItemsTemplate;
import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperDataSetMetaData;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperFieldMetaData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

public class DetailFieldMetaDataListPage extends AbstractDetailWizardPage {

	private Button btnAdd;
	private Button btnRemove;
	private TableViewer tableViewer;

	public DetailFieldMetaDataListPage() {
		super("fieldmetadatalist");
		setTitle("FieldMetaData List");
		setDescription("Properties of FieldMetaDatas");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));

		this.tableViewer = new TableViewer(container, SWT.BORDER | SWT.V_SCROLL);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblcName = tableViewerColumn.getColumn();
		tblcName.setWidth(100);
		tblcName.setText("Name");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn_1.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText("Description");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnBuiltintype = tableViewerColumn_2.getColumn();
		tblclmnBuiltintype.setWidth(100);
		tblclmnBuiltintype.setText("BuiltinType");

		this.tableViewer.setContentProvider(new FieldArrayContentProvider());
		this.tableViewer.setLabelProvider(new FieldLabelProvider());

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		this.btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setText("Add");

		this.btnRemove = new Button(composite, SWT.NONE);
		btnRemove.setText("Remove");

		setDefaultValues();
		setHandler();
	}

	@Override
	public PubSubPublishedDataSetWizard getWizard() {
		return (PubSubPublishedDataSetWizard) super.getWizard();
	}

	protected void refreshList() {
		this.tableViewer.refresh();
	}

	public void setDefaultValues() {
		this.btnRemove.setEnabled(false);

		Object config = getWizard().pageTwo.getConfig();
		if (config instanceof PubSubPublishedDataItemsTemplate) {
			WrapperDataSetMetaData metadata = ((PubSubPublishedDataItemsTemplate) config).getMetaData();
			this.tableViewer.setInput(metadata);
		}
	}

	private void setHandler() {
		this.btnAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DetailPublishedMetadataFieldPage page = getWizard().pageThree;
				page.setField(null);
				getContainer().showPage(page);
			}

		});

		this.btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// check for selection
				if (tableViewer.getSelection().isEmpty()) {
					return;
				}

				Object config = getWizard().pageTwo.getConfig();
				if (config instanceof PubSubPublishedDataItemsTemplate) {
					WrapperDataSetMetaData metadata = ((PubSubPublishedDataItemsTemplate) config).getMetaData();
					WrapperFieldMetaData[] fields = metadata.getFields();
					Object selection = ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();

					List<WrapperFieldMetaData> newFields = new ArrayList<>();
					for (WrapperFieldMetaData field : fields) {
						if (field == selection) {
							continue;
						}
						newFields.add(field);
					}
					metadata.setFields(newFields.toArray(new WrapperFieldMetaData[0]));
					refreshList();
				}
			}

		});

		this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection.isEmpty()) {
					btnRemove.setEnabled(false);
				} else {
					btnRemove.setEnabled(true);
				}
			}
		});
	}

	class FieldArrayContentProvider extends ArrayContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof WrapperDataSetMetaData) {
				return ((WrapperDataSetMetaData) inputElement).getFields();
			}

			return super.getElements(inputElement);
		}

	}

	class FieldLabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((WrapperFieldMetaData) element).getName() != null ? ((WrapperFieldMetaData) element).getName()
						: "";
			case 1:
				return ((WrapperFieldMetaData) element).getDescription() != null
						? ((WrapperFieldMetaData) element).getDescription().getText()
						: "";
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {

		}

		@Override
		public void dispose() {

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {

		}

	}

}
