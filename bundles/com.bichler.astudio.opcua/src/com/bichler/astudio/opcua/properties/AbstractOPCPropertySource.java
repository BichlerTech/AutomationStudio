package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public abstract class AbstractOPCPropertySource implements IPropertySource {

	private StudioModelNode adapter;

	public AbstractOPCPropertySource(StudioModelNode adapter) {
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

	public StudioModelNode getAdapter() {
		return this.adapter;
	}
}