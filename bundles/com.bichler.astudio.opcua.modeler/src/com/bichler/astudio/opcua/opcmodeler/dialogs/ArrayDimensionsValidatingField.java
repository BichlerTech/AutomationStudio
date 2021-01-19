package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.widgets.Control;

import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class ArrayDimensionsValidatingField<T> extends ValidatingField<T> {
	@Override
	public boolean isValid() {
		this.getContents();
		return true;
	}

	@Override
	public void setContents(T arg0) {
		// TODO Auto-generated method stub
		super.setContents(arg0);
	}

	public ArrayDimensionsValidatingField(Control arg0, IFieldValidator<T> arg1, ControlDecoration arg2,
			IControlContentAdapter arg3, IContentsStringConverter<T> arg4, boolean arg5) {
		super(arg0, arg1, arg2, arg3, arg4, arg5);
		// TODO Auto-generated constructor stub
	}
}
