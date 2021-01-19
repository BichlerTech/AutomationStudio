package opc.sdk.server.service.profile;

import opc.sdk.server.core.OPCInternalServer;

import org.opcfoundation.ua.application.ServerDiscoveryService;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.FindServersRequest;
import org.opcfoundation.ua.core.FindServersResponse;
// import org.opcfoundation.ua.core.RegisterServerRequest;
// import org.opcfoundation.ua.core.RegisterServerResponse;
import org.opcfoundation.ua.core.ServerState;
import org.opcfoundation.ua.core.ServiceFault;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class FindServerDiscovery extends ServerDiscoveryService {
	private OPCInternalServer server;

	public FindServerDiscovery(OPCInternalServer server) {
		this.server = server;
	}

	@Override
	public void onFindServers(EndpointServiceRequest<FindServersRequest, FindServersResponse> req)
			throws ServiceFaultException {
		if (this.server.getState() == ServerState.Running.getValue()) {
			try {
				this.server.getServiceManager().onFindServers(req);
			} catch (ServiceResultException e) {
				ServiceFault fault = ServiceFault.toServiceFault(e);
				req.sendFault(fault);
			}
		}
	}
	// private void onRegisterServer(EndpointServiceRequest<RegisterServerRequest,
	// RegisterServerResponse> req)
	// throws ServiceFaultException {
	// if (this.server.getState() == ServerState.Running.getValue()) {
	// try {
	// this.server.getServiceManager().onRegisterServer(req);
	// } catch (ServiceResultException e) {
	// ServiceFault fault = ServiceFault.toServiceFault(e);
	// req.sendFault(fault);
	// }
	// }
	// }
}
