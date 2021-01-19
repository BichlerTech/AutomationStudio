package com.bichler.astudio.editor.siemens.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.editor.siemens.properties.SiemensPropertySource;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;

public class SiemensPropertyAdapter implements IAdapterFactory {
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		// rockwell datapoint node
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == SiemensEntryModelNode.class) {
			return new SiemensPropertySource((SiemensEntryModelNode) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}
}
