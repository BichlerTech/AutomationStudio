package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.core.ActivateSessionRequest;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.CancelRequest;
import org.opcfoundation.ua.core.CancelResponse;
import org.opcfoundation.ua.core.CloseSessionRequest;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.CreateSessionRequest;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.opcfoundation.ua.core.SessionServiceSetHandler;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class SessionService extends OPCService implements SessionServiceSetHandler {
	public SessionService(OPCServiceManager serviceManager) {
		super(serviceManager);
	}

	@Override
	public void onCreateSession(EndpointServiceRequest<CreateSessionRequest, CreateSessionResponse> req)
			throws ServiceFaultException {
		getServiceManager().onCreateSession(req);
	}

	@Override
	public void onActivateSession(EndpointServiceRequest<ActivateSessionRequest, ActivateSessionResponse> req)
			throws ServiceFaultException {
		getServiceManager().onActivateSession(req);
	}

	@Override
	public void onCloseSession(EndpointServiceRequest<CloseSessionRequest, CloseSessionResponse> req)
			throws ServiceFaultException {
		getServiceManager().onCloseSession(req);
	}

	@Override
	public void onCancel(EndpointServiceRequest<CancelRequest, CancelResponse> req) throws ServiceFaultException {
		getServiceManager().onCancle(req);
	}
}
