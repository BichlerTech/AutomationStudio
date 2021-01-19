package opc.sdk.server;

import opc.sdk.ua.IOPCOperation;
import opc.sdk.ua.IOPCSession;

public class OPCServerOperation implements IOPCOperation {
	private IOPCSession session;

	public OPCServerOperation(IOPCSession session) {
		if (session == null) {
			throw new IllegalArgumentException("Session is null!");
		}
		this.session = session;
	}

	@Override
	public IOPCSession getSession() {
		return this.session;
	}
}
