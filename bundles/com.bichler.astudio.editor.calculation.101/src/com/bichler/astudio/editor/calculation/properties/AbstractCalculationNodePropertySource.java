package com.bichler.astudio.editor.calculation.properties;

import java.util.Deque;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public abstract class AbstractCalculationNodePropertySource extends
		AbstractCalculationPropertySource {

	static final String PROPERTY_TARGET_NAME = "target_name"; // displayname
	static final String PROPERTY_TARGET_NODEID = "target_nodeid"; // nodeid
	static final String PROPERTY_TARGET_BROWSEPATH = "target_browsepath"; // nodeid
	static final String PROPERTY_TARGET_DATATYPE = "target_datatype"; // datatype
	static final String PROPERTY_TARGET_ACCESS = "target_access"; // access
	static final String PROPERTY_TARGET_USERACCESS = "target_useraccess"; // user
	static final String PROPERTY_TARGET_VALUE = "target_value"; // value
	static final String PROPERTY_TARGET_INDEX = "target_index"; // index

	NodeId targetId;

	public AbstractCalculationNodePropertySource(NodeId targetId) {
		super();
		this.targetId = targetId;
	}

	protected IPropertyDescriptor[] getSimpleValuePropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_TARGET_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_TARGET_NODEID, "NodeId"),
				new PropertyDescriptor(PROPERTY_TARGET_BROWSEPATH, "Browsepath"),
				new PropertyDescriptor(PROPERTY_TARGET_DATATYPE, "Datatype"),
				new PropertyDescriptor(PROPERTY_TARGET_ACCESS, "Access Level"),
				new PropertyDescriptor(PROPERTY_TARGET_USERACCESS,
						"Useraccess Level"),
				new PropertyDescriptor(PROPERTY_TARGET_VALUE, "Value") };
	}

	protected IPropertyDescriptor[] getArrayValuePropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_TARGET_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_TARGET_NODEID, "NodeId"),
				new PropertyDescriptor(PROPERTY_TARGET_BROWSEPATH, "Browsepath"),
				new PropertyDescriptor(PROPERTY_TARGET_DATATYPE, "Datatype"),
				new PropertyDescriptor(PROPERTY_TARGET_ACCESS, "Access Level"),
				new PropertyDescriptor(PROPERTY_TARGET_USERACCESS,
						"Useraccess Level"),
				new PropertyDescriptor(PROPERTY_TARGET_VALUE, "Value"),
				new PropertyDescriptor(PROPERTY_TARGET_INDEX, "Value ArrayIndex") };
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if(NodeId.isNull(targetId)){
			return new IPropertyDescriptor[0];
		}
		
		
		// find node
		Node node = ServerInstance.getNode(this.targetId);
		// valueranks
		Integer valueRank = null;
		switch (node.getNodeClass()) {
		case Variable:
			valueRank = ((VariableNode) node).getValueRank();
			break;
		case VariableType:
			valueRank = ((VariableTypeNode) node).getValueRank();
			break;
		default:
			valueRank = ValueRanks.Scalar.getValue(); // -1
			break;
		}

		switch (valueRank) {
		// scalar
		case -1:
			return getSimpleValuePropertyDescriptors();
		default:
			return getArrayValuePropertyDescriptors();
		}
	}

	public Object getPropertyValue(Object id) {
		if (PROPERTY_TARGET_NAME.equals(id)) {
			return getCalculationName();
		}
		if (PROPERTY_TARGET_NODEID.equals(id)) {
			return getCalculationNodeId();
		}
		if (PROPERTY_TARGET_BROWSEPATH.equals(id)) {
			return getCalculationBrowsePath();
		}
		if (PROPERTY_TARGET_DATATYPE.equals(id)) {
			return getCalculationDataType();
		}
		if (PROPERTY_TARGET_ACCESS.equals(id)) {
			return getCalculationAccessLevel();
		}
		if (PROPERTY_TARGET_USERACCESS.equals(id)) {
			return getCalculationUserAccessLevel();
		}
		if (PROPERTY_TARGET_VALUE.equals(id)) {
			return getCalculationValue();
		}

		if (PROPERTY_TARGET_INDEX.equals(id)) {
			return getCalculationIndex();
		}

		return null;
	}

	public Object getCalculationAccessLevel() {
		Node node = ServerInstance.getInstance().getServerInstance()
				.getAddressSpaceManager().getNodeById(this.targetId);

		if (node instanceof VariableNode) {
			return AccessLevel.getSet(((VariableNode) node).getAccessLevel())
					.toString();
		}

		return "";
	}

	public abstract Object getCalculationIndex();

	public Object getCalculationName() {
		Node node = ServerInstance.getInstance().getServerInstance()
				.getAddressSpaceManager().getNodeById(this.targetId);

		if (node == null) {
			return "";
		}

		return node.getDisplayName();
	}

	public Object getCalculationNodeId() {
		if (this.targetId == null) {
			return NodeId.NULL.toString();
		}

		return this.targetId.toString();
	}
	
	protected String getCalculationBrowsePath() {
		NodeId nodeId = this.targetId;
		Deque<BrowsePathElement> browsepathelems = OPCUABrowseUtils
				.getFullBrowsePath(nodeId, ServerInstance.getInstance()
						.getServerInstance(), Identifiers.ObjectsFolder);

		String browsepath = "";
		for (BrowsePathElement element : browsepathelems) {
			if (element.getId().equals(Identifiers.ObjectsFolder)) {
				continue;
			}
			browsepath += "//" + element.getBrowsename().getName();
		}

		return browsepath;
	}

	public Object getCalculationDataType() {
		Node node = ServerInstance.getInstance().getServerInstance()
				.getAddressSpaceManager().getNodeById(this.targetId);

		NodeId datatype = NodeId.NULL;

		if (node instanceof VariableNode) {
			datatype = ((VariableNode) node).getDataType();
		}

		if (node instanceof VariableTypeNode) {
			datatype = ((VariableTypeNode) node).getDataType();
		}

		Node datatypenode = ServerInstance.getInstance().getServerInstance()
				.getAddressSpaceManager().getNodeById(datatype);

		if (datatypenode == null) {
			return "";
		}

		return datatypenode.getDisplayName();
	}

	public Object getCalculationUserAccessLevel() {
		Node node = ServerInstance.getInstance().getServerInstance()
				.getAddressSpaceManager().getNodeById(this.targetId);

		if (node instanceof VariableNode) {
			return AccessLevel.getSet(
					((VariableNode) node).getUserAccessLevel()).toString();
		}

		return "";
	}

	public Object getCalculationValue() {
		Node node = ServerInstance.getInstance().getServerInstance()
				.getAddressSpaceManager().getNodeById(this.targetId);

		if (node instanceof VariableNode) {
			return ((VariableNode) node).getValue().toString();
		}

		if (node instanceof VariableTypeNode) {
			return ((VariableTypeNode) node).getValue().toString();
		}

		return "";
	}
}
