package com.bichler.astudio.editor.aggregated.clientbrowser.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.editor.aggregated.clientbrowser.model.AbstractCCModel;
import com.bichler.astudio.editor.aggregated.clientbrowser.properties.ClientPropertySource;

public class ClientPropertyAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		/**
		 * properties of opc ua navigation view
		 */
		// opc ua server root navigation node
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject instanceof AbstractCCModel) {
			return new ClientPropertySource((AbstractCCModel) adaptableObject);
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
