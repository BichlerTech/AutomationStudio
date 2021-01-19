package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import opc.sdk.core.node.Node;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public abstract class AbstractReferenceWizard extends Wizard {
	protected ReferenceNode refNode2Edit;
	protected BrowserModelNode source;

	/**
	 * Finds the target reference or NULL if there is no reference.
	 * 
	 * @return
	 */
	ReferenceNode findTargetReference() {
		ExpandedNodeId targetId = this.refNode2Edit.getTargetId();
		NodeId refId = this.refNode2Edit.getReferenceTypeId();
		NodeId sourceId = this.source.getNode().getNodeId();
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		Node targetNode = ServerInstance.getNode(targetId);
		
		for (ReferenceNode refNode : targetNode.getReferences()) {
			ExpandedNodeId sourceExpId = refNode.getTargetId();
			try {
				NodeId id = nsTable.toNodeId(sourceExpId);
				if (sourceId.equals(id) && refId.equals(refNode.getReferenceTypeId())) {
					return refNode;
				}
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void setSourceNode(BrowserModelNode node) {
		this.source = node;
	}

	public void setReferenceToEdit(ReferenceNode element) throws ServiceResultException {
		this.refNode2Edit = element;
		
		ExpandedNodeId targetId = this.refNode2Edit.getTargetId();
		Node targetNode = ServerInstance.getNode(targetId);
		if(targetNode == null) {
			throw new ServiceResultException(StatusCodes.Bad_TargetNodeIdInvalid);
		}
	}
}
