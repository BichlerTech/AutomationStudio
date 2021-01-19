package com.bichler.astudio.opcua.opcmodeler.wizards.create.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.EnumValueType;

import com.bichler.astudio.opcua.opcmodeler.dialogs.LocalizedTextInputDialog;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DataTypeEnumTypePage.EnumType;

public class DataTypeEnumPage extends WizardPage {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private DataTypeEnumTypePage enumTypePage;
	private TableViewer tv_stringValue;
	private TableViewer tv_enumTypeValue;
	// private GridData gd_table_string;
	// private GridData gd_table_enumtype;
	private Button btnAdd;
	private Button btnRemove;
	private Composite composite;
	private List<LocalizedText> inputStringValue;
	private List<EnumValueType> inputEnumTypeValue;

	/**
	 * Create the wizard.
	 */
	public DataTypeEnumPage() {
		super("wizardPage");
		setTitle("Enumeration Value");
		setDescription("Defines OPC UA enumeration values");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		// root composite
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout());
		setControl(container);
		ScrolledForm form = formToolkit.createScrolledForm(container);
		form.setExpandHorizontal(true);
		form.setExpandVertical(true);
		form.getBody().setLayout(new GridLayout(1, false));
		Section section = formToolkit.createSection(form.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText("Enumeration values");
		section.setExpanded(true);
		// section composite
		this.composite = new Composite(section, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(2, false));
		/**
		 * String value table viewer
		 */
		this.tv_stringValue = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tv_stringValue.getTable();
		table.setHeaderVisible(true);
		GridData gd_table_string = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		table.setLayoutData(gd_table_string);
		formToolkit.paintBordersFor(table);
		this.tv_stringValue.setContentProvider(new ArrayContentProvider());
		// this.tv_stringValue.setLabelProvider(new LabelProvider());
		this.inputStringValue = new ArrayList<>();
		this.tv_stringValue.setInput(this.tv_stringValue);
		// column value
		TableViewerColumn tvc_sv = new TableViewerColumn(tv_stringValue, SWT.NONE);
		TableColumn tblclmnNewColumn = tvc_sv.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Name");
		tvc_sv.setLabelProvider(new LocalizedTextColumnLabelProvider());
		/**
		 * EnumDataType section
		 */
		this.tv_enumTypeValue = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_1 = tv_enumTypeValue.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		GridData gd_table_enumtype = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_enumtype.exclude = true;
		table_1.setLayoutData(gd_table_enumtype);
		formToolkit.paintBordersFor(table_1);
		// column value
		TableViewerColumn tvc_ev = new TableViewerColumn(tv_enumTypeValue, SWT.NONE);
		TableColumn tc_ev = tvc_ev.getColumn();
		tc_ev.setWidth(168);
		tc_ev.setText("Value");
		// column name
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tv_enumTypeValue, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		// column description
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_enumTypeValue, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn_1.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText("Description");
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		formToolkit.adapt(group);
		formToolkit.paintBordersFor(group);
		group.setLayout(new GridLayout(1, false));
		this.btnAdd = new Button(group, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnAdd, true, true);
		btnAdd.setText("Add");
		this.btnRemove = new Button(group, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnRemove, true, true);
		btnRemove.setText("Remove");
		setHandler();
		setTableVisible();
	}

	protected void setTableVisible() {
		GridData gd_table_string = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData gd_table_enumtype = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		switch (this.enumTypePage.getEnumType()) {
		case StringValue:
			gd_table_string.exclude = false;
			this.tv_stringValue.getTable().setVisible(true);
			this.tv_stringValue.getTable().setLayoutData(gd_table_string);
			gd_table_enumtype.exclude = true;
			this.tv_enumTypeValue.getTable().setVisible(false);
			this.tv_enumTypeValue.getTable().setLayoutData(gd_table_enumtype);
			this.composite.layout();
			break;
		case EnumValueType:
			gd_table_string.exclude = true;
			this.tv_stringValue.getTable().setVisible(false);
			this.tv_stringValue.getTable().setLayoutData(gd_table_string);
			gd_table_enumtype.exclude = false;
			this.tv_enumTypeValue.getTable().setVisible(true);
			this.tv_enumTypeValue.getTable().setLayoutData(gd_table_enumtype);
			this.composite.layout();
			break;
		}
	}

	private void openInput(TableViewer tableViewer, List<LocalizedText> inputValues, int selectionIndex, String value) {
		InputDialog input = new InputDialog(getShell(), "Input", "Enumeration value", value, null);

		int open = input.open();
		if (open != Dialog.OK) {
			return;
		}
		LocalizedText localizedText = new LocalizedText(input.getValue(), LocalizedText.NULL_LOCALE);
		if (selectionIndex >= 0) {
			inputValues.set(selectionIndex, localizedText);
		} else {
			inputValues.add(localizedText);
		}
		// refresh viewer
		tableViewer.setInput(inputValues);
	}

	private void setHandler() {
		if (this.tv_stringValue != null) {
			this.tv_stringValue.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					int selectionIndex = tv_stringValue.getTable().getSelectionIndex();
					LocalizedText selection = (LocalizedText) ((StructuredSelection) event.getSelection()).getFirstElement();

					openInput(tv_stringValue, inputStringValue, selectionIndex, selection.getText());
				}
			});
		}

		if (this.tv_enumTypeValue != null) {

		}

		this.btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// open input dialog
				EnumType enumType = enumTypePage.getEnumType();
				switch (enumType) {
				case StringValue:
					openInput(tv_stringValue, inputStringValue, -1, null);
					break;
				case EnumValueType:
					break;
				default:
					break;
				}
			}
		});
		this.btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selection = null;
				boolean confirm = MessageDialog.openConfirm(getShell(), "Delete", "Confirm delete");
				if (!confirm) {
					return;
				}
				EnumType enumType = enumTypePage.getEnumType();
				switch (enumType) {
				case StringValue:
					selection = ((IStructuredSelection) tv_stringValue.getSelection()).getFirstElement();
					inputStringValue.remove(selection);
					// refresh viewer
					tv_stringValue.setInput(inputStringValue);
					break;
				case EnumValueType:
					selection = ((IStructuredSelection) tv_enumTypeValue.getSelection()).getFirstElement();
					tv_enumTypeValue.remove(selection);
					tv_enumTypeValue.refresh();
					break;
				default:
					break;
				}
			}
		});
	}

	public Object[] getEnumerationValues() {
		switch (this.enumTypePage.getEnumType()) {
		case EnumValueType:
			return this.inputEnumTypeValue.toArray(new EnumValueType[0]);
		case StringValue:
			return this.inputStringValue.toArray(new LocalizedText[0]);
		default:
			return new Object[0];
		}
	}

	public void setEnumTypePage(DataTypeEnumTypePage enumTypePage) {
		this.enumTypePage = enumTypePage;
	}

	class LocalizedTextColumnLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof LocalizedText) {
				return ((LocalizedText) element).getText();
			}
			return super.getText(element);
		}
	}
}
