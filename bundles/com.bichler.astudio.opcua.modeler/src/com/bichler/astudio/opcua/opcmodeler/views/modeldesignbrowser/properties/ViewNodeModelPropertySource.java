package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import opc.sdk.core.node.ViewNode;

import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ViewNodeModelPropertySource extends BrowserModelNodeModelPropertySource {
	protected static final String PROPERTY_EVENTNOTIFIER = "p_eventnotifier";
	protected static final String PROPERTY_CONTAINSLOOP = "p_containsloop";

	public ViewNodeModelPropertySource(BrowserModelNode adapter) {
		super(adapter);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_EVENTNOTIFIER.equals(id)) {
			return ((ViewNode) getAdapter().getNode()).getEventNotifier();
		}
		if (PROPERTY_CONTAINSLOOP.equals(id)) {
			return ((ViewNode) getAdapter().getNode()).getContainsNoLoops();
		}
		return super.getPropertyValue(id);
	}

	@Override
	protected void setDescriptors() {
		super.setDescriptors();
		this.descriptors.add(new PropertyDescriptor(PROPERTY_EVENTNOTIFIER,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.eventnotifier")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_CONTAINSLOOP,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.containsnoloop")));
	}
}
