package com.bichler.astudio.opcua.javacommand.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.opcua.javacommand.properties.JavaCommandPropertySource;
import com.bichler.astudio.opcua.javacommand.view.JavaCommandView;

public class JavaCommandAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {

		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == String.class) {

			if (!(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActivePart() instanceof JavaCommandView)) {
				return null;
			}

			return new JavaCommandPropertySource((String) adaptableObject);
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
