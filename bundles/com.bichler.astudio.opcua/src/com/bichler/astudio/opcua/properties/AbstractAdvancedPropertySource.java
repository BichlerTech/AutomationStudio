package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;

public abstract class AbstractAdvancedPropertySource implements IPropertySource {

	private AbstractConfigNode adapter;

	public AbstractAdvancedPropertySource(AbstractConfigNode adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// String s = (String) value;
		// if (id.equals("summary")) {
		// adapter.setSummary("SUM");
		// }
		// if (id.equals("description")) {
		// adapter.setDescription(s);
		// }
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	public AbstractConfigNode getAdapter() {
		return this.adapter;
	}
}
