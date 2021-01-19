package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.node.Node;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public abstract class AbstractCreateModellingDialog extends Dialog {
	protected NodeClass[] filtersModellingRule = {};
	protected NodeClass[] filtersModellingParent = {};

	protected AbstractCreateModellingDialog(Shell parentShell) {
		super(parentShell);
	}

	public boolean hasRuleItself(Node node, LinkedList<QualifiedName> path) {
		boolean hasRule = false;
		for (NodeClass nc : getFilteredRules()) {
			if (nc == node.getNodeClass()) {
				return true;
			}
		}
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		hasRule = findHasRule(nsTable, path, node.getNodeId(), node.getNodeClass(), node.getBrowseName());
		return hasRule;
	}

	private boolean findHasRule(NamespaceTable nsTable, LinkedList<QualifiedName> path, NodeId id, NodeClass nodeClass,
			QualifiedName targetName) {
		for (NodeClass nc : getFilteredParent()) {
			if (nc == nodeClass) {
				return true;
			}
		}
		path.addFirst(targetName);
		try {
			BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
			BrowseDescription description = new BrowseDescription();
			description.setBrowseDirection(BrowseDirection.Inverse);
			description.setIncludeSubtypes(true);
			description.setNodeClassMask(NodeClass.ALL);
			description.setNodeId(id);
			description.setReferenceTypeId(Identifiers.HierarchicalReferences);
			description.setResultMask(BrowseResultMask.ALL);
			nodesToBrowse[0] = description;
			BrowseResult[] result = ServerInstance.getInstance().getServerInstance().getMaster().browse(nodesToBrowse,
					UnsignedInteger.ZERO, null, null);
			// has a model parent class
			if (result != null && result.length > 0 && result[0].getReferences() != null
					&& result[0].getReferences().length > 0) {
				ReferenceDescription reference = result[0].getReferences()[0];
				return findHasRule(nsTable, path, nsTable.toNodeId(reference.getNodeId()), reference.getNodeClass(),
						reference.getBrowseName());
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}

	protected abstract NodeClass[] getFilteredRules();

	protected abstract NodeClass[] getFilteredParent();
}
