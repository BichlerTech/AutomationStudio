package opc.sdk.server.core.managers;

import opc.sdk.server.core.OPCInternalServer;

public interface IOPCManager {
	public boolean start();

	public boolean stop();

	OPCInternalServer getServer();

	public boolean isInitialized();
}
