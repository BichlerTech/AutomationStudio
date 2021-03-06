package opc.sdk.server.core.managers;

import org.opcfoundation.ua.common.NamespaceTable;

import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.OPCInternalServer;

public abstract class OPCNamespaceManager implements IOPCManager {
	/** internal opc server instance */
	private OPCInternalServer server;

	public OPCNamespaceManager(OPCInternalServer server) {
		this.server = server;
	}

	public OPCInternalServer getServer() {
		return this.server;
	}

	/**
	 * Returns the server's namespace table.
	 * 
	 * @return namespace table
	 */
	NamespaceTable getNamespaceUris() {
		return this.server.getNamespaceUris();
	}

	/**
	 * Returns the server's type table.
	 * 
	 * @return type table
	 */
	TypeTable getTypeTable() {
		return this.server.getTypeTable();
	}
}
