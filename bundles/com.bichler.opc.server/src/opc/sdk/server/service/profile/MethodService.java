package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.CallResponse;
import org.opcfoundation.ua.core.MethodServiceSetHandler;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class MethodService extends OPCService implements MethodServiceSetHandler {
	public MethodService(OPCServiceManager serviceManager) {
		super(serviceManager);
	}

	@Override
	public void onCall(EndpointServiceRequest<CallRequest, CallResponse> req) throws ServiceFaultException {
		getServiceManager().onCall(req);
	}
}
