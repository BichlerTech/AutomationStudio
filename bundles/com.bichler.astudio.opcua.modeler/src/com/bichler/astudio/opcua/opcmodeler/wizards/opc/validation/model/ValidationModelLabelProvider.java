package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ValidationModelLabelProvider extends LabelProvider {
	@Override
	public Image getImage(Object element) {
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ValidationModel) {
			return ((ValidationModel) element).getName().toString();
		}
		return super.getText(element);
	}
}
