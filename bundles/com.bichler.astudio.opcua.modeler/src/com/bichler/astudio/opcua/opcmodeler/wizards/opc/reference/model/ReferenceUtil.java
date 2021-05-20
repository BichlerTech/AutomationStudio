package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

import opc.sdk.core.node.Node;

public class ReferenceUtil {
	public static ReferenceModel initializeReferenceTypeTree(NodeId rootId, NodeId referenceType, boolean includeRoot) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		Node root = ServerInstance.getNode(rootId);
		ReferenceModel model, dummy = null;
		model = new ReferenceModel(new ExpandedNodeId(root.getNodeId()), root.getDisplayName(), root.getNodeClass());
		// model = new ReferenceModel(nsTable.toExpandedNodeId(root.getNodeId()),
		// root.getDisplayName(), root.getNodeClass());
		List<ExpandedNodeId> preventStackOverflow = new ArrayList<>();
		rekModel(nsTable, referenceType, model, preventStackOverflow);
		if (includeRoot) {
			dummy = new ReferenceModel(ExpandedNodeId.NULL, LocalizedText.EMPTY, NodeClass.Unspecified);
			dummy.addChild(model);
			return dummy;
		}
		return model;
	}

	private static void rekModel(NamespaceTable nsTable, NodeId referenceType, ReferenceModel model,
			List<ExpandedNodeId> preventStackOverflow) {
		boolean containsId = preventStackOverflow.contains(model.getId());
		if (!containsId) {
			preventStackOverflow.add(model.getId());
			NodeId nodeId = model.getId(nsTable);
			try {
				BrowseResult[] results = ServerInstance.browse(nodeId, referenceType, NodeClass.ALL,
						BrowseResultMask.ALL, BrowseDirection.Forward, true);
				if (results != null && results.length > 0) {
					for (ReferenceDescription refDesc : results[0].getReferences()) {
						ReferenceModel item = new ReferenceModel(refDesc.getNodeId(), refDesc.getDisplayName(),
								refDesc.getNodeClass());
						model.addChild(item);
						rekModel(nsTable, referenceType, item, preventStackOverflow);
					}
				}
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
	}
}
