package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ValueDialog extends Dialog {
	private List values = null;
	private Variant value = null;
	// private boolean areValuesSelected = false;
	private Button btn_moveUP = null;
	private Button btn_moveDown = null;
	private Button btn_add = null;
	private Button btn_remove = null;
	private DesignerFormToolkit controllCreationToolkit = null;

	public ValueDialog(Shell parentShell, Variant value) {
		super(parentShell);
		this.value = value;
		this.controllCreationToolkit = new DesignerFormToolkit();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"));
		this.values = new List(composite, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
		this.values.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selection = values.getSelection();
				if (selection.length > 0) {
					setValuesSelected(true);
				} else {
					setValuesSelected(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		this.values.setBounds(10, 10, 100, 100);
		if (this.value.getValue() instanceof String[]) {
			this.values.setItems((String[]) this.value.getValue());
		}
		GridDataFactory.fillDefaults().span(1, 3).applyTo(this.values);
		btn_moveUP = this.controllCreationToolkit.createButtonPush(composite, "+");
		btn_moveUP.setSize(16, 16);
		btn_moveUP.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selection = values.getSelection();
				int count = values.getSelectionCount();
				int index = -1;
				String value = null;
				if (count > 1) {
					int[] indizes = values.getSelectionIndices();
					for (int i = 0; i < selection.length; i++) {
						index = indizes[i];
						value = selection[i];
					}
				} else {
					index = values.getSelectionIndex();
					value = values.getSelection()[0];
				}
				// move up
				if (index > 0) {
					int newIndex = index - 1;
					String itemToChange = values.getItem(newIndex);
					values.setItem(newIndex, value);
					values.setItem(index, itemToChange);
					values.setSelection(newIndex);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		btn_moveDown = this.controllCreationToolkit.createButtonPush(composite, "-");
		btn_moveDown.setSize(16, 16);
		btn_moveDown.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selection = values.getSelection();
				int count = values.getSelectionCount();
				int index = -1;
				String value = null;
				if (count > 1) {
					int[] indizes = values.getSelectionIndices();
					for (int i = 0; i < selection.length; i++) {
						index = indizes[i];
						value = selection[i];
					}
				} else {
					index = values.getSelectionIndex();
					value = values.getSelection()[0];
				}
				// move down
				if (index < (values.getItemCount() - 1)) {
					int newIndex = index + 1;
					String itemToChange = values.getItem(newIndex);
					values.setItem(newIndex, value);
					values.setItem(index, itemToChange);
					values.setSelection(newIndex);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		btn_add = this.controllCreationToolkit.createButtonPush(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.add"));
		btn_add.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog dialog = new InputDialog(composite.getShell(),
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.addvalue"),
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.addvalue"), null, null);
				int open = dialog.open();
				if (open == Dialog.OK) {
					String value = dialog.getValue();
					values.add(value);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		btn_remove = this.controllCreationToolkit.createButtonPush(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "command.delete"));
		btn_remove.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selection = values.getSelection();
				for (String s : selection) {
					values.remove(s);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		// no value is selected after initalization of the dialog
		setValuesSelected(false);
		return composite;
	}

	@Override
	protected void okPressed() {
		if (this.value.isArray() && this.value.getCompositeClass() == String.class) {
			this.value = new Variant(this.values.getItems());
		}
		super.okPressed();
	}

	private void setValuesSelected(boolean areValuesSelected) {
		// enable defined buttons
		if (areValuesSelected) {
			btn_moveUP.setEnabled(areValuesSelected);
			btn_moveDown.setEnabled(areValuesSelected);
			btn_remove.setEnabled(areValuesSelected);
			btn_add.setEnabled(areValuesSelected);
		}
		// disable
		else {
			btn_moveUP.setEnabled(areValuesSelected);
			btn_moveDown.setEnabled(areValuesSelected);
			btn_remove.setEnabled(areValuesSelected);
		}
		// this.areValuesSelected = areValuesSelected;
	}

	public Object getValue() {
		return this.value;
	}
}
