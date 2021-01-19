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
public class IntegerCellEditor extends TextCellEditor {

	public IntegerCellEditor() {
		super();
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
		// if (value != null && value instanceof String) {
		// if (!((String) value).isEmpty()) {
		if (value == null) {
			return ;
		}
		try {

//			int val = Integer.parseInt(value.toString());
			super.doSetValue(value.toString());
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		// }
		// }
	}

}
