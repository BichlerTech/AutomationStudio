package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

public abstract class OPCService {
	private OPCServiceManager serviceManager = null;

	public OPCService(OPCServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	protected OPCServiceManager getServiceManager() {
		return this.serviceManager;
	}
}
