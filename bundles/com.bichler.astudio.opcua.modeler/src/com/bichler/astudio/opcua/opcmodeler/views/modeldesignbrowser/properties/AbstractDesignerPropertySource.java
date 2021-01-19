package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public abstract class AbstractDesignerPropertySource implements IPropertySource {
	protected static final String PROPERTY_BROWSENAME = "p_brname";
	protected static final String PROPERTY_DISPLAYNAME = "p_dname";
	protected static final String PROPERTY_NODEID = "p_nodeid";
	protected static final String PROPERTY_NODECLASS = "p_nodeclass";
	protected static final String PROPERTY_WRITEMASK = "p_writemask";
	protected static final String PROPERTY_USERWRITEMASK = "p_userwritemask";
	private BrowserModelNode adapter;
	protected List<IPropertyDescriptor> descriptors = new ArrayList<>();

	public AbstractDesignerPropertySource(BrowserModelNode adapter) {
		this.adapter = adapter;
		setDescriptors();
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
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	public BrowserModelNode getAdapter() {
		return this.adapter;
	}

	protected abstract void setDescriptors();
}
