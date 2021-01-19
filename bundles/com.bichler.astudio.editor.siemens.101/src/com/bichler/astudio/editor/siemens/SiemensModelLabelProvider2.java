package com.bichler.astudio.editor.siemens;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;
import com.bichler.astudio.view.drivermodel.handler.util.IDriverModelViewLabelProvider;

public class SiemensModelLabelProvider2 extends LabelProvider
		implements ITableLabelProvider, IDriverModelViewLabelProvider {
	/**
	 * Returns the image for the item
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof AbstractDriverModelViewNode) {
			return ((AbstractDriverModelViewNode) element).getDecorator();
		}
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return ((SiemensEntryModelNode) element).getSymbolName();
		case 1:
			return ((SiemensEntryModelNode) element).getDataType();
		case 2:
			return ((SiemensEntryModelNode) element).getVorgabe1();
		case 3:
			return ((SiemensEntryModelNode) element).getVorgabe2();
		case 4:
			return ((SiemensEntryModelNode) element).getDescription();
		default:
			break;
		}
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getText(Object element) {
		if (element instanceof AbstractSiemensNode) {
			return ((AbstractSiemensNode) element).getSymbolName();
		}
		return super.getText(element);
	}
}
