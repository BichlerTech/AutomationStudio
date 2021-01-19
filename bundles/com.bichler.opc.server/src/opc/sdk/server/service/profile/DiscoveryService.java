package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.core.DiscoveryServiceSetHandler;
import org.opcfoundation.ua.core.FindServersRequest;
import org.opcfoundation.ua.core.FindServersResponse;
import org.opcfoundation.ua.core.GetEndpointsRequest;
import org.opcfoundation.ua.core.GetEndpointsResponse;
import org.opcfoundation.ua.core.RegisterServerRequest;
import org.opcfoundation.ua.core.RegisterServerResponse;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class DiscoveryService extends OPCService implements DiscoveryServiceSetHandler {
	public DiscoveryService(OPCServiceManager serviceManager) {
		super(serviceManager);
	}

	@Override
	public void onFindServers(EndpointServiceRequest<FindServersRequest, FindServersResponse> req)
			throws ServiceFaultException {
		getServiceManager().onFindServers(req);
	}

	@Override
	public void onGetEndpoints(EndpointServiceRequest<GetEndpointsRequest, GetEndpointsResponse> req)
			throws ServiceFaultException {
		// getServiceManager().onGetEndpoints(req);
	}

	@Override
	public void onRegisterServer(EndpointServiceRequest<RegisterServerRequest, RegisterServerResponse> req)
			throws ServiceFaultException {
		getServiceManager().onRegisterServer(req);
	}
}
