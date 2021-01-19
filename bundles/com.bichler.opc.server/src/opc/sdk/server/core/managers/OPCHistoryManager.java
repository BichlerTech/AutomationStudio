package opc.sdk.server.core.managers;

import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.history.HistoryManager;

public class OPCHistoryManager implements IOPCManager {
	private OPCInternalServer server;
	private HistoryManager history;

	public OPCHistoryManager(OPCInternalServer server) {
		this.server = server;
	}

	@Override
	public boolean start() {
		this.hasInitialized = true;
		return true;
	}

	@Override
	public boolean stop() {
		this.hasInitialized = false;
		return this.hasInitialized;
	}

	public HistoryManager getHistoryConnection(int namespaceIndex) {
		return null;
	}

	@Override
	public OPCInternalServer getServer() {
		return this.server;
	}

	public HistoryManager getHistory() {
		return history;
	}

	public void setHistory(HistoryManager history) {
		this.history = history;
	}

	boolean hasInitialized = false;

	@Override
	public boolean isInitialized() {
		return this.hasInitialized;
	}
}
