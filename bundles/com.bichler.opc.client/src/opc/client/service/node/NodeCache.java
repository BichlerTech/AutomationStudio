package opc.client.service.node;

import opc.client.service.ClientSession;
import opc.sdk.core.types.TypeTable;

/**
 * A node cache which contains the types of the address space.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class NodeCache {
	private TypeTable typeTree = null;

	public NodeCache(ClientSession session) {
		if (session == null) {
			throw new IllegalArgumentException("Session");
		}
		this.typeTree = new TypeTable(session.getNamespaceUris());
	}

	public TypeTable getTypeTree() {
		return this.typeTree;
	}
}
