package com.bichler.astudio.opcua.opcmodeler.views.namespacebrowser.properties;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.opcmodeler.Activator;

import opc.sdk.core.node.Node;

public class NamespacePropertySource implements IPropertySource {
	protected static final String PROPERTY_BROWSENAME = "p_brname";
	protected static final String PROPERTY_BROWSEPATH = "p_browsepath";
	protected static final String PROPERTY_DISPLAYNAME = "p_dname";
	protected static final String PROPERTY_NODEID = "p_nodeid";
	protected static final String PROPERTY_NODECLASS = "p_nodeclass";
	protected static final String PROPERTY_WRITEMASK = "p_writemask";
	protected static final String PROPERTY_USERWRITEMASK = "p_userwritemask";
	private Node node = null;
	protected List<IPropertyDescriptor> descriptors = new ArrayList<>();

	public NamespacePropertySource(Node node) {
		this.node = node;
		setDescriptors();
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	protected void setDescriptors() {
		this.descriptors.add(new PropertyDescriptor(PROPERTY_BROWSEPATH,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.browsepath")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_BROWSENAME,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CREATEVARIABLEDIALOG_")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_DISPLAYNAME, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_displayName.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_NODECLASS,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "OmronTcpTypePart.lbl_varType.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_NODEID,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.id")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_USERWRITEMASK, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_userWriteMask.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_WRITEMASK, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_writeMask.text")));
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

	protected Node getNode() {
		return this.node;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return this.descriptors.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_BROWSEPATH.equals(id)) {
			Deque<BrowsePathElement> path = OPCUABrowseUtils.getFullBrowsePath(getNode().getNodeId(),
					ServerInstance.getInstance().getServerInstance(), Identifiers.RootFolder);
			path.removeFirst();
			String browsepath = "";
			for (BrowsePathElement element : path) {
				browsepath += "/" + element.getDisplayname().getText();
			}
			return browsepath;
		}
		if (PROPERTY_BROWSENAME.equals(id)) {
			return getNode().getBrowseName().toString();
		}
		if (PROPERTY_DISPLAYNAME.equals(id)) {
			return getNode().getDisplayName().toString();
		}
		if (PROPERTY_NODECLASS.equals(id)) {
			return getNode().getNodeClass().name();
		}
		if (PROPERTY_NODEID.equals(id)) {
			return getNode().getNodeId();
		}
		if (PROPERTY_USERWRITEMASK.equals(id)) {
			return getNode().getUserWriteMask();
		}
		if (PROPERTY_WRITEMASK.equals(id)) {
			return getNode().getWriteMask();
		}
		return null;
	}
}
