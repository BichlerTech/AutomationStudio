package opc.sdk.server.service.node;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import opc.sdk.core.node.UAObjectNode;
import opc.sdk.server.service.history.HistoryManager;

/**
 * Extended ua variable node to use historizing.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public class UAServerObjectNode extends UAObjectNode {
	/**
	 * generated serial version uid
	 */
	private static final long serialVersionUID = 2650397426996676149L;
	private HistoryManager serverHistoryManager = null;

	public UAServerObjectNode() {
		super();
	}

	public UAServerObjectNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, UnsignedByte EventNotifier) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References,
				EventNotifier);
	}

	public void setHistory(HistoryManager historyManager) {
		this.serverHistoryManager = historyManager;
	}

	public void storeHistoryValue(DataValue value) {
		if (this.serverHistoryManager == null) {
			return;
		}
		this.serverHistoryManager.writehistory(this, value);
	}

	public HistoryManager getHistoryManager() {
		return this.serverHistoryManager;
	}
}
