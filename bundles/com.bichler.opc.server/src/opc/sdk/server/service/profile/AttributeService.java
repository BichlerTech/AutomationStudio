package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.core.AttributeServiceSetHandler;
import org.opcfoundation.ua.core.HistoryReadRequest;
import org.opcfoundation.ua.core.HistoryReadResponse;
import org.opcfoundation.ua.core.HistoryUpdateRequest;
import org.opcfoundation.ua.core.HistoryUpdateResponse;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.WriteRequest;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class AttributeService extends OPCService implements AttributeServiceSetHandler {
	public AttributeService(OPCServiceManager serviceManager) {
		super(serviceManager);
	}

	@Override
	public void onRead(EndpointServiceRequest<ReadRequest, ReadResponse> req) throws ServiceFaultException {
		getServiceManager().onRead(req);
	}

	@Override
	public void onHistoryRead(EndpointServiceRequest<HistoryReadRequest, HistoryReadResponse> req)
			throws ServiceFaultException {
		getServiceManager().onHistoryRead(req);
	}

	@Override
	public void onWrite(EndpointServiceRequest<WriteRequest, WriteResponse> req) throws ServiceFaultException {
		getServiceManager().onWrite(req);
	}

	@Override
	public void onHistoryUpdate(EndpointServiceRequest<HistoryUpdateRequest, HistoryUpdateResponse> req)
			throws ServiceFaultException {
		getServiceManager().onHistoryUpdate(req);
	}
}
