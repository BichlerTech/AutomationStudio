package com.bichler.opcua.statemachine.addressspace.importer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

/**
 * 
 * @author andreas.schartlmueller@sequality.at
 */
public class NodeSetBean1 {
	private StatemachineNodesetImporter importer = null;
	private NodeClass nodeClass = null;
	private NodeId parentNodeId = null;
	private NodeId nodeId = null;
	private String browseName = null;
	private String displayName = null;
	private NodeId referenceType = null;
	private NodeId typeDefinition = null;
	private String description = null;
	private boolean symmetric = false;
	private NodeId hasModellingRule = null;

	private NodeSetBean1() {
	}

	public NodeSetBean1(NodeClass nodeClass, StatemachineNodesetImporter importer) {
		this.nodeClass = nodeClass;
		this.importer = importer;
	}

	public NodeClass getNodeClass() {
		return nodeClass;
	}

	public NodeId getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(NodeId parameterNodeId) {
		this.parentNodeId = parameterNodeId;
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public String getBrowseName() {
		return browseName;
	}

	public void setBrowseName(String browseName) {
		this.browseName = browseName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public NodeId getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(NodeId referenceType) {
		this.referenceType = referenceType;
	}

	public NodeId getTypeDefinition() {
		return typeDefinition;
	}

	public void setTypeDefinition(NodeId typeDefinition) {
		this.typeDefinition = typeDefinition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSymmetric() {
		return symmetric;
	}

	public void setSymmetric(boolean symetric) {
		this.symmetric = symetric;
	}

	public void setHasModellingRule(NodeId hasModellingRule) {
		this.hasModellingRule = hasModellingRule;
	}

	public NodeId getHasModellingRule() {
		return hasModellingRule;
	}

	public AddReferencesItem createTypeDescription(NodeId typeDescription, String direction,
			NamespaceTable namespaceTable) {
		AddReferencesItem item = new AddReferencesItem();
		if (direction == null)
			item.setIsForward(true);
		else
			item.setIsForward(Boolean.parseBoolean(direction));
		item.setReferenceTypeId(Identifiers.HasDescription);
		item.setSourceNodeId(this.nodeId);
		item.setTargetNodeClass(NodeClass.Variable);
		item.setTargetNodeId(new ExpandedNodeId(namespaceTable.getUri(typeDescription.getNamespaceIndex()),
				typeDescription.getValue(), namespaceTable));
		return item;
	}

	public AddReferencesItem createTypeEncoding(NodeId typeDescription, String direction,
			NamespaceTable namespaceTable) {
		AddReferencesItem item = new AddReferencesItem();
		if (direction == null)
			item.setIsForward(true);
		else
			item.setIsForward(Boolean.parseBoolean(direction));
		item.setReferenceTypeId(Identifiers.HasEncoding);
		item.setSourceNodeId(this.nodeId);
		item.setTargetNodeClass(NodeClass.Variable);
		item.setTargetNodeId(new ExpandedNodeId(namespaceTable.getUri(typeDescription.getNamespaceIndex()),
				typeDescription.getValue(), namespaceTable));
		return item;
	}

	public AddReferencesItem createAdditionalReference(NodeId targetNodeId, String referenceType, String direction,
			NamespaceTable namespaceTable, StatemachineNodesetImporter importer) {
		AddReferencesItem item = new AddReferencesItem();
		if (direction == null)
			item.setIsForward(true);
		else
			item.setIsForward(Boolean.parseBoolean(direction));
		try {
			NodeId refid = (NodeId) Identifiers.class.getDeclaredField(referenceType).get(null);
			item.setReferenceTypeId(refid);
		} catch (NoSuchFieldException nsfe) {
//			Logger.getLogger(getClass().getName()).log(Level.INFO, "No intern identifier found for " + referenceType);
			try {
				// do we have other references?
				item.setReferenceTypeId(importer.createNodeIdByXmlValue(referenceType));
			} catch (Exception ex) {
//				Logger.getLogger(getClass().getName()).log(Level.INFO, "No intern identifier found for " + referenceType);
				NodeId refNodeId = null;
				if ((refNodeId = importer.getSpezificDataType(referenceType)) == null) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found", ex);
				} else {
					try {
						if (!NodeId.isNull(refNodeId)) {
							item.setReferenceTypeId(importer.createNodeIdByXmlValue(refNodeId.toString()));
						} else {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found");
						}
					} catch (Exception ex2) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found");
					}
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found", ex);
		}
		item.setSourceNodeId(this.nodeId);
		// item.setTargetNodeClass(NodeClass.Object);

		// NamespaceTable nsTable =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		item.setTargetNodeId(new ExpandedNodeId(namespaceTable.getUri(targetNodeId.getNamespaceIndex()),
				targetNodeId.getValue(), namespaceTable));

		return item;
	}
}
