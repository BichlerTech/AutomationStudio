package com.bichler.astudio.opcua.opcmodeler.dialogs.namespaceprovider;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignedit.TableLabelProvider;

public class NamespacesLabelProvider extends TableLabelProvider {
	@Override
	public String getColumnText(Object element, int columnIndex) {
		String[] elements = (String[]) element;
		if (elements[columnIndex] == null) {
			return "";
		}
		return ((String[]) element)[columnIndex].toString();
	}
}
