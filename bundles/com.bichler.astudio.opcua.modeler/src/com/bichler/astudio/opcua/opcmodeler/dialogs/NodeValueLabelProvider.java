package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class NodeValueLabelProvider extends LabelProvider implements ITableLabelProvider {
	private int lineCount = 0;

	public NodeValueLabelProvider() {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == 0) {
			return "" + this.lineCount++;
		}
		return element.toString();
	}

	public void resetLineCount() {
		this.lineCount = 0;
	}
}
