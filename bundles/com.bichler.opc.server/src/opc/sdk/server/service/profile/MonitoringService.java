package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.core.CreateMonitoredItemsRequest;
import org.opcfoundation.ua.core.CreateMonitoredItemsResponse;
import org.opcfoundation.ua.core.DeleteMonitoredItemsRequest;
import org.opcfoundation.ua.core.DeleteMonitoredItemsResponse;
import org.opcfoundation.ua.core.ModifyMonitoredItemsRequest;
import org.opcfoundation.ua.core.ModifyMonitoredItemsResponse;
import org.opcfoundation.ua.core.MonitoredItemServiceSetHandler;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.SetMonitoringModeResponse;
import org.opcfoundation.ua.core.SetTriggeringRequest;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class MonitoringService extends OPCService implements MonitoredItemServiceSetHandler {
	public MonitoringService(OPCServiceManager serviceManager) {
		super(serviceManager);
	}

	@Override
	public void onCreateMonitoredItems(
			EndpointServiceRequest<CreateMonitoredItemsRequest, CreateMonitoredItemsResponse> req)
			throws ServiceFaultException {
		getServiceManager().onCreateMonitoredItems(req);
	}

	@Override
	public void onModifyMonitoredItems(
			EndpointServiceRequest<ModifyMonitoredItemsRequest, ModifyMonitoredItemsResponse> req)
			throws ServiceFaultException {
		getServiceManager().onModifyMonitoredItems(req);
	}

	@Override
	public void onSetMonitoringMode(EndpointServiceRequest<SetMonitoringModeRequest, SetMonitoringModeResponse> req)
			throws ServiceFaultException {
		getServiceManager().onSetMonitoringMode(req);
	}

	@Override
	public void onSetTriggering(EndpointServiceRequest<SetTriggeringRequest, SetTriggeringResponse> req)
			throws ServiceFaultException {
		getServiceManager().onSetTriggering(req);
	}

	@Override
	public void onDeleteMonitoredItems(
			EndpointServiceRequest<DeleteMonitoredItemsRequest, DeleteMonitoredItemsResponse> req)
			throws ServiceFaultException {
		getServiceManager().onDeleteMonitoredItems(req);
	}
}
