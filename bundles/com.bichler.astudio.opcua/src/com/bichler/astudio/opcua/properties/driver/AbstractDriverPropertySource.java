package com.bichler.astudio.opcua.properties.driver;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;

public abstract class AbstractDriverPropertySource implements IPropertySource {

	protected static final String PROPERTY_DATATYPE = "p_datatype";
	protected static final String PROPERTY_DESCRIPTION = "p_desc";
	protected static final String PROPERTY_DISPLAYNAME = "p_displayname";
	protected static final String PROPERTY_NODEID = "p_nid";
	protected static final String PROPERTY_ACCESSLEVEL = "p_access";
	protected static final String PROPERTY_USERACCESSLEVEL = "p_useraccess";

	private IDriverNode adapter;

	public AbstractDriverPropertySource() {

	}

	public AbstractDriverPropertySource(IDriverNode adapter) {
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

	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	public IDriverNode getAdapter() {
		return adapter;
	}

	public List<IPropertyDescriptor> getDescriptorList() {
		List<IPropertyDescriptor> descriptors = new ArrayList<>();

		descriptors.add(new PropertyDescriptor(PROPERTY_DISPLAYNAME,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.displayname")));
		descriptors.add(new PropertyDescriptor(PROPERTY_NODEID,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.nodeid")));
		descriptors.add(new PropertyDescriptor(PROPERTY_DESCRIPTION,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description")));
		descriptors.add(new PropertyDescriptor(PROPERTY_DATATYPE,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.opcuadatatype")));
		descriptors.add(new PropertyDescriptor(PROPERTY_ACCESSLEVEL,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.accesslevel")));
		descriptors.add(new PropertyDescriptor(PROPERTY_USERACCESSLEVEL,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.useraccesslevel")));
		return descriptors;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return getDescriptorList().toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DISPLAYNAME.equals(id)) {
			return ((IDriverNode) getAdapter()).getDname();
		}
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return ((IDriverNode) getAdapter()).getDesc();
		}
		if (PROPERTY_DATATYPE.equals(id)) {
			return getDataTypeText(((IDriverNode) getAdapter()).getNId());
		}
		if (PROPERTY_NODEID.equals(id)) {
			return ((IDriverNode) getAdapter()).getNId();
		}

		if (PROPERTY_ACCESSLEVEL.equals(id)) {

			return getAccessLevel(((IDriverNode) getAdapter()).getNId());
		}
		if (PROPERTY_USERACCESSLEVEL.equals(id)) {
			return getUserAccessLevel(((IDriverNode) getAdapter()).getNId());
		}

		return null;
	}

	protected Object getDataTypeText(NodeId nodeId) {
		Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(nodeId);

		NodeId datatype = NodeId.NULL;

		if (node instanceof VariableNode) {
			datatype = ((VariableNode) node).getDataType();
		}

		if (node instanceof VariableTypeNode) {
			datatype = ((VariableTypeNode) node).getDataType();
		}

		Node datatypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
				.getNodeById(datatype);

		if (datatypenode == null) {
			return "";
		}

		return datatypenode.getDisplayName();
	}

	protected UnsignedByte getAccessLevel(NodeId nodeId) {
		Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(nodeId);

		UnsignedByte access = null;
		if (node instanceof VariableNode) {
			access = ((VariableNode) node).getAccessLevel();
		}
		if (access != null) {
			return access;
		}

		return UnsignedByte.ZERO;
	}

	protected Object getUserAccessLevel(NodeId nodeId) {
		Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(nodeId);

		UnsignedByte access = null;
		if (node instanceof VariableNode) {
			access = ((VariableNode) node).getUserAccessLevel();
		}
		if (access != null) {
			return access;
		}

		return UnsignedByte.ZERO;
	}

}
