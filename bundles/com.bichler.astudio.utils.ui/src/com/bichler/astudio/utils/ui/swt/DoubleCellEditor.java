package com.bichler.astudio.utils.ui.swt;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * class to validate integer input cells.
 * 
 * @author applemc207da
 * 
 */
public class DoubleCellEditor extends TextCellEditor {

	public DoubleCellEditor() {
		super();
		setValidator(new ICellEditorValidator() {
			public String isValid(Object object) {
				if (object instanceof Double) {
					return null;
				} else {
					String string = (String) object;
					try {
						Double.parseDouble(string);
						return null;
					} catch (NumberFormatException exception) {
						return exception.getMessage();
					}
				}
			}
		});
	}

	public DoubleCellEditor(Composite composite) {
		super(composite);
		setValidator(new ICellEditorValidator() {
			public String isValid(Object object) {
				if (object instanceof Double) {
					return null;
				} else {
					String string = (String) object;
					try {
						Double.parseDouble(string);
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
		return Double.parseDouble((String) super.doGetValue());
	}

	@Override
	public void doSetValue(Object value) {
		// if (value != null && value instanceof String) {
		// if (!((String) value).isEmpty()) {
		try {

//			int val = Integer.parseInt(value.toString());
			super.doSetValue(value.toString());
		} catch (NumberFormatException nfe) {

		}
		// }
		// }
	}

}
