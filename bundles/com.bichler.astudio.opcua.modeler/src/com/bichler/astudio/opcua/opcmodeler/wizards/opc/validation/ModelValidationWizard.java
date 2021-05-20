package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.AbstractContentValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model.ValidationModelContentFactory;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.page.ObjectModelSourcePage;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.page.ValidationModelPage;

import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

public class ModelValidationWizard extends Wizard {
	// selected def from the modelbrowserview
	private ModelTypDef selectedDef;
	// type definition of the mapping node structure
	private ModelTypDef typeDef;
	// root element of the mapping node structure
	private ModelTypDef elementRootDef;
	// selected type def
	private ModelTypDef selectedTypeDef = null;
	private ModelTypDef selectedRootElement = null;
	// runtime models
	private Map<ExpandedNodeId, AbstractContentValidationModel> runtimeModel = new HashMap<>();
	// runtime mapping
	private Map<ExpandedNodeId, Map<ExpandedNodeId, ExpandedNodeId>> runtimeMapping = new HashMap<>();
	private ObjectModelSourcePage objectModelMappingPage;
	private ValidationModelPage validationPage;
	private List<NodeId> currentNewNodeids = new ArrayList<>();
	private List<ValidationModel> nodes2add = new ArrayList<>();;

	public ModelValidationWizard() {
	}

	@Override
	public void addPages() {
		// map element with type
		if (typeDef == null && elementRootDef == null) {
			this.objectModelMappingPage = new ObjectModelSourcePage();
			this.objectModelMappingPage.setSelectedDef(this.selectedDef);
			addPage(this.objectModelMappingPage);
		}
		this.validationPage = new ValidationModelPage();
		addPage(this.validationPage);
	}

	@Override
	public boolean performFinish() {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		List<ValidationModel> newNodesToCreate = getNodesToAdd();
		List<AddNodesItem> nodesToAdd = new ArrayList<>();
		for (ValidationModel model : newNodesToCreate) {
			// new node id
			NodeId newNodeId = model.getNodeId();
			// new mapping id
			ExpandedNodeId mappingId = model.getMappingId();
			try {
				NodeId id = model.getParent().getNodeId();

				ReferenceDescription[] result = ValidationModelContentFactory.browse(
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(mappingId),
						BrowseDirection.Inverse, Identifiers.HierarchicalReferences);
				ReferenceDescription[] result2 = ValidationModelContentFactory.browse(
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(mappingId),
						BrowseDirection.Forward, Identifiers.HasTypeDefinition);
				Node node = ServerInstance.getNode(mappingId);
				NodeAttributes nodeAttributes = createNodeAttributes(model, node);
				AddNodesItem item = new AddNodesItem();
				item.setBrowseName(node.getBrowseName());
				item.setNodeAttributes(
						ExtensionObject.binaryEncode(nodeAttributes, EncoderContext.getDefaultInstance()));
				item.setNodeClass(model.getNodeClass());
				item.setParentNodeId(
						new ExpandedNodeId(id));
				// item.setParentNodeId(ServerInstance.getInstance()
				// .getServerInstance().getNamespaceUris()
				// .toExpandedNodeId(model.getParent().getNodeId()));
				item.setReferenceTypeId(result[0].getReferenceTypeId());
				item.setRequestedNewNodeId(new ExpandedNodeId(newNodeId));
				// item.setRequestedNewNodeId(ServerInstance.getInstance()
				// .getServerInstance().getNamespaceUris()
				// .toExpandedNodeId(newNodeId));
				if (result2.length > 0) {
					item.setTypeDefinition(result2[0].getNodeId());
				} else {
					item.setTypeDefinition(mappingId);
				}
				nodesToAdd.add(item);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		if (!nodesToAdd.isEmpty()) {
			try {
				ServerInstance.addNode(nodesToAdd.toArray(new AddNodesItem[0]), true);
				// getRuntimeMapping().put(arg0, arg1);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		Map<ExpandedNodeId, ExpandedNodeId> mapping = getRuntimeMapping();
		for (Entry<ExpandedNodeId, ExpandedNodeId> entry : mapping.entrySet()) {
			ServerInstance.getTypeModel().addModelMapping(entry.getKey(), entry.getValue());
		}
		return true;
	}

	private NodeAttributes createNodeAttributes(ValidationModel model, Node node) {
		NodeAttributes attributes = null;
		switch (node.getNodeClass()) {
		case Object:
			attributes = new ObjectAttributes();
			((ObjectAttributes) attributes).setEventNotifier(((ObjectNode) node).getEventNotifier());
			break;
		case ObjectType:
			attributes = new ObjectAttributes();
			((ObjectAttributes) attributes).setEventNotifier(UnsignedByte.ZERO);
			break;
		case Variable:
			attributes = new VariableAttributes();
			((VariableAttributes) attributes).setAccessLevel(((VariableNode) node).getAccessLevel());
			((VariableAttributes) attributes).setArrayDimensions(((VariableNode) node).getArrayDimensions());
			((VariableAttributes) attributes).setDataType(((VariableNode) node).getDataType());
			((VariableAttributes) attributes).setHistorizing(((VariableNode) node).getHistorizing());
			((VariableAttributes) attributes)
					.setMinimumSamplingInterval(((VariableNode) node).getMinimumSamplingInterval());
			((VariableAttributes) attributes).setUserAccessLevel(((VariableNode) node).getUserAccessLevel());
			((VariableAttributes) attributes).setValue(((VariableNode) node).getValue());
			((VariableAttributes) attributes).setValueRank(((VariableNode) node).getValueRank());
			break;
		case VariableType:
			attributes = new VariableAttributes();
			((VariableAttributes) attributes).setArrayDimensions(((VariableTypeNode) node).getArrayDimensions());
			((VariableAttributes) attributes).setDataType(((VariableTypeNode) node).getDataType());
			((VariableAttributes) attributes).setValue(((VariableTypeNode) node).getValue());
			((VariableAttributes) attributes).setValueRank(((VariableTypeNode) node).getValueRank());
			break;
		case Method:
			attributes = new MethodAttributes();
			((MethodAttributes) attributes).setExecutable(((MethodNode) node).getExecutable());
			((MethodAttributes) attributes).setUserExecutable(((MethodNode) node).getUserExecutable());
			break;
		}
		attributes.setDescription(node.getDescription());
		attributes.setDisplayName(node.getDisplayName());
		attributes.setUserWriteMask(node.getUserWriteMask());
		attributes.setWriteMask(node.getWriteMask());
		return attributes;
	}

	public void setSelectedDef(ModelTypDef def) {
		this.selectedDef = def;
	}

	public void setElementTypeDef(ModelTypDef def) {
		this.typeDef = def;
	}

	public void setElementRootDef(ModelTypDef def) {
		this.elementRootDef = def;
	}

	public void setObjectModelSourceTypeSelection(IStructuredSelection selection) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ValidationModel model = (ValidationModel) selection.getFirstElement();
		ModelTypDef selectedRootDef = new ModelTypDef();
		selectedRootDef.nodeId = new ExpandedNodeId(model.getNodeId());
		// selectedRootDef.nodeId = ServerInstance.getInstance()
		// .getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(model.getNodeId());
		selectedRootDef.typeClass = model.getNodeClass();
		selectedRootDef.name = model.getName();
		ReferenceDescription[] result = ValidationModelContentFactory.browse(model.getNodeId(), BrowseDirection.Forward,
				Identifiers.HasTypeDefinition);
		ModelTypDef selectedTypeDef = new ModelTypDef();
		if (result.length > 0) {
			selectedTypeDef.nodeId = result[0].getNodeId();
			selectedTypeDef.typeClass = result[0].getNodeClass();
			selectedTypeDef.name = result[0].getDisplayName();
		} else {
			selectedTypeDef.nodeId = selectedRootDef.nodeId;
			selectedTypeDef.typeClass = selectedRootDef.typeClass;
			selectedTypeDef.name = selectedRootDef.name;
		}
		this.selectedRootElement = selectedRootDef;
		this.selectedTypeDef = selectedTypeDef;
	}

	public ModelTypDef getElementTypeDef() {
		if (typeDef != null) {
			return this.typeDef;
		}
		return this.selectedTypeDef;
	}

	public ModelTypDef getRootElementTypeDef() {
		if (elementRootDef != null) {
			return this.elementRootDef;
		}
		return this.selectedRootElement;
	}

	public Map<ExpandedNodeId, AbstractContentValidationModel> getRuntimeModel() {
		return this.runtimeModel;
	}

	public Map<ExpandedNodeId, ExpandedNodeId> getRuntimeMapping() {
		Map<ExpandedNodeId, ExpandedNodeId> rtMapping = this.runtimeMapping.get(getRootElementTypeDef().nodeId);
		if (rtMapping == null) {
			rtMapping = new HashMap<>();
			this.runtimeMapping.put(getRootElementTypeDef().nodeId, rtMapping);
		}
		return rtMapping;
	}

	public List<NodeId> getCurrentNewNodeIds() {
		return this.currentNewNodeids;
	}

	public List<ValidationModel> getNodesToAdd() {
		return this.nodes2add;
	}
}
