package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.EventFieldList;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.encoding.DecodingException;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.core.node.Node;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public abstract class BaseInstance extends BaseNode {
	private BaseNode parent = null;
	private NodeId referenceTypeId = null;
	private NodeId modellingRuleId = null;

	/**
	 * Initializes the instance with its defalt attribute values.
	 */
	public BaseInstance(NodeClass nodeClass, BaseNode parent) {
		super(nodeClass);
		this.parent = parent;
	}

	/**
	 * Exports a copy of the node to a node table.
	 */
	@Override
	protected void export(ISystemContext context, Node node) {
		super.export(context, node);
		NamespaceTable nsTable = context.getNamespaceUris();
		List<ReferenceNode> refsList = Arrays.asList();
		refsList = new ArrayList<>(refsList);
		if (this.parent != null) {
			NodeId tmpReferenceTypeId = this.referenceTypeId;
			if (NodeId.isNull(tmpReferenceTypeId)) {
				tmpReferenceTypeId = Identifiers.HasComponent;
			}
			refsList.add(new ReferenceNode(tmpReferenceTypeId, true,
					new ExpandedNodeId(nsTable.getUri(this.parent.getNodeId().getNamespaceIndex()),
							this.parent.getNodeId().getValue(), nsTable)));
			// refsList.add(new ReferenceNode(Identifiers.HasModelParent, false,
		}
		if (!NodeId.isNull(getTypeDefinitionId())) {
			refsList.add(new ReferenceNode(Identifiers.HasTypeDefinition, false,
					new ExpandedNodeId(nsTable.getUri(getTypeDefinitionId().getNamespaceIndex()),
							getTypeDefinitionId().getValue(), nsTable)));
		}
		if (!NodeId.isNull(this.modellingRuleId)) {
			refsList.add(new ReferenceNode(Identifiers.HasModellingRule, false,
					new ExpandedNodeId(nsTable.getUri(this.modellingRuleId.getNamespaceIndex()),
							this.modellingRuleId.getValue(), nsTable)));
		}
	}

	/**
	 * Initializes the instance from another instance.
	 */
	@Override
	protected void initialize(ISystemContext context, BaseNode source) {
		BaseInstance instance = (BaseInstance) source;
		if (instance != null) {
			this.referenceTypeId = instance.referenceTypeId;
			setTypeDefinitionId(instance.getTypeDefinitionId());
			this.modellingRuleId = instance.modellingRuleId;
		}
		super.initialize(context, source);
	}

	@Override
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		super.update(context, decoder, attributesToLoad);
		if (attributesToLoad.contains(AttributesToSave.ReferenceTypeId)) {
			setReferenceTypeId(decoder.getNodeId(null));
		}
		if (attributesToLoad.contains(AttributesToSave.TypeDefinitionId)) {
			setTypeDefinitionId(decoder.getNodeId(null));
		}
		if (attributesToLoad.contains(AttributesToSave.ModellingRuleId)) {
			setModellingRuleId(decoder.getNodeId(null));
		}
	}

	/**
	 * Initializes the instance from an event notification.
	 * 
	 * This method creates components based on the browse paths in the event field
	 * and sets the NodeId or Value based on values in the event notification.
	 * 
	 * @param Context       The context.
	 * @param SelectClauses The fields selected for the event notification.
	 * @param Notificiation The event notification.
	 */
	public void update(SimpleAttributeOperand[] selectClauses, EventFieldList notificiation) {
		for (int i = 0; i < selectClauses.length; i++) {
			SimpleAttributeOperand field = selectClauses[i];
			Object value = notificiation.getEventFields()[i].getValue();
			// check if value is provided
			if (value == null) {
				continue;
			}
			// extract NodeId for the event
			if (field.getBrowsePath().length == 0 && Attributes.NodeId.equals(field.getAttributeId())) {
				this.setNodeId((NodeId) value);
			}
			// extract TypeDefinition for the event
			if (field.getBrowsePath().length == 1 && Attributes.Value.equals(field.getAttributeId())
					&& BrowseNames.EVENTTYPE.equalsIgnoreCase(field.getBrowsePath()[0].getName())) {
				setTypeDefinitionId((NodeId) value);
				continue;
			}
			// save value for the node child
			BaseNode tmpParent = this;
			for (int j = 0; j < field.getBrowsePath().length; j++) {
				// find a predefined child identified by the browsename
				BaseInstance child = tmpParent.createChild(field.getBrowsePath()[j]);
				// create placeholder for unknown children
				if (child == null) {
					if (Attributes.Value.equals(field.getAttributeId())) {
						child = new BaseDataVariableType<Object>(tmpParent);
					} else {
						child = new BaseObjectType(tmpParent);
					}
					tmpParent.addChild(child);
				}
				// ensure the browse name is set
				if (QualifiedName.isNull(child.getBrowseName())) {
					child.setBrowseName(field.getBrowsePath()[j]);
				}
				// ensure the displayname is set
				if (child.getDisplayName() == null || LocalizedText.EMPTY.equals(child.getDisplayName())) {
					child.setDisplayName(new LocalizedText(child.getBrowseName().getName(), Locale.getDefault()));
				}
				// process next element in path
				if (j < field.getBrowsePath().length - 1) {
					tmpParent = child;
					continue;
				}
				// save the variable value
				if (Attributes.Value.equals(field.getAttributeId())) {
					if (child instanceof BaseVariableType) {
						BaseVariableType<?> variable = (BaseVariableType<?>) child;
						if (variable != null && Attributes.Value.equals(field.getAttributeId())) {
							variable.setWrappedValue(notificiation.getEventFields()[i]);
						}
					}
					break;
				}
				// save the nodeid
				child.setNodeId((NodeId) value);
			}
		}
	}

	public void setParent(BaseNode parent) {
		this.parent = parent;
	}

	public BaseNode getParent() {
		return this.parent;
	}

	public NodeId getReferenceTypeId() {
		return this.referenceTypeId;
	}

	public void setReferenceTypeId(NodeId value) {
		if (this.referenceTypeId != value) {
			setChangeMask(NodeStateChangeMasks.REFERENCES);
		}
		this.referenceTypeId = value;
	}

	public NodeId getModellingRuleId() {
		return modellingRuleId;
	}

	public void setModellingRuleId(NodeId modellingRuleId) {
		if (this.modellingRuleId != modellingRuleId) {
			setChangeMask(NodeStateChangeMasks.REFERENCES);
		}
		this.modellingRuleId = modellingRuleId;
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 */
	protected abstract NodeId getDefaultTypeDefinitionId();
}
