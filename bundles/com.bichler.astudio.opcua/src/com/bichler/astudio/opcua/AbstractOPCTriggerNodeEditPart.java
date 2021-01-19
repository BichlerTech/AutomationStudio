package com.bichler.astudio.opcua;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public abstract class AbstractOPCTriggerNodeEditPart extends AbstractOPCDPDriverViewLinkEditorPart {

	private List<NodeToTrigger> possibleTriggerNodes = new ArrayList<NodeToTrigger>();

	public AbstractOPCTriggerNodeEditPart() {

	}

	public boolean isTriggerNodeValid(NodeToTrigger lookup) {
		// triggernode is null
		if (lookup == null) {
			return true;
		}

		if (NodeId.isNull(lookup.nodeId)) {
			return true;
		}

		for (NodeToTrigger n2t : possibleTriggerNodes) {
			if (n2t.triggerName.compareTo(lookup.triggerName) == 0) {
				if (n2t.active)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public List<NodeToTrigger> getPossibleTriggerNodes() {
		return possibleTriggerNodes;
	}

	public void setPossibleTriggerNodes(List<NodeToTrigger> possibleTriggerNodes) {
		this.possibleTriggerNodes = possibleTriggerNodes;
	}

	@Override
	public void onNamespaceChange(NamespaceTableChangeParameter trigger) {

	}

}
