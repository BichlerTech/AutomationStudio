package com.bichler.astudio.opcua.drivers.views.nodes;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignedit.TableLabelProvider;

public class DriversNodeLabelProvider extends TableLabelProvider {
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return super.getColumnImage(element, columnIndex);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof DriversModelNode) {
			switch (columnIndex) {
			case 0:
				return ((DriversModelNode) element).getId() + "";
			case 1:
				return ((DriversModelNode) element).getBundle().getSymbolicName();
			case 2:
				return ((DriversModelNode) element).getBundle().getVersion().toString();
			}
		}
		return super.getColumnText(element, columnIndex);
	}

	/**
	 * constructor, create label images
	 */
	public DriversNodeLabelProvider() {
	}
}
