package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import opc.sdk.core.node.ObjectNode;

import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ObjectNodeModelPropertySource extends BrowserModelNodeModelPropertySource {
	protected static final String PROPERTY_EVENTNOTIFIER = "p_eventnotifier";

	public ObjectNodeModelPropertySource(BrowserModelNode adapter) {
		super(adapter);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_EVENTNOTIFIER.equals(id)) {
			return ((ObjectNode) getAdapter().getNode()).getEventNotifier();
		}
		return super.getPropertyValue(id);
	}

	@Override
	protected void setDescriptors() {
		super.setDescriptors();
		this.descriptors.add(new PropertyDescriptor(PROPERTY_EVENTNOTIFIER,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.eventnotifier")));
	}
}
