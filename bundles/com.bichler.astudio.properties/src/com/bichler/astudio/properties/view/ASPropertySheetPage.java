package com.bichler.astudio.properties.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetSorter;

public class ASPropertySheetPage extends PropertySheetPage {

	@Override
	public void setSorter(PropertySheetSorter sorter) {
		super.setSorter(sorter);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		ASPropertySheetEntry root = new ASPropertySheetEntry();

		this.setRootEntry(root);
	}
}
