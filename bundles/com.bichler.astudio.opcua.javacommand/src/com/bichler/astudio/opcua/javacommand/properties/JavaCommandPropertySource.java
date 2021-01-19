package com.bichler.astudio.opcua.javacommand.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.opcua.javacommand.preferences.JavaCommandPreferenceConstants;
import com.bichler.astudio.opcua.javacommand.preferences.JavaCommandPreferenceManager;

public class JavaCommandPropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "p_cmdname";
	private static final String PROPERTY_DESCRIPTION = "p_cmddesc";
	
	private Preferences adaptable;

	public JavaCommandPropertySource(String adaptable) {
		this.adaptable = JavaCommandPreferenceManager.getCommand(adaptable);
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_NAME, "Skriptname"),
				new PropertyDescriptor(PROPERTY_DESCRIPTION, "Beschreibung") };
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_NAME.equals(id)) {
			return this.adaptable.name();
		}
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return this.adaptable
					.get(JavaCommandPreferenceConstants.PREFERENCE_COMMAND_DESCRIPTION,
							"");
		}

		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {

	}

}
