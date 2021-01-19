package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.core.CreateSubscriptionRequest;
import org.opcfoundation.ua.core.CreateSubscriptionResponse;
import org.opcfoundation.ua.core.DeleteSubscriptionsRequest;
import org.opcfoundation.ua.core.DeleteSubscriptionsResponse;
import org.opcfoundation.ua.core.ModifySubscriptionRequest;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.PublishRequest;
import org.opcfoundation.ua.core.PublishResponse;
import org.opcfoundation.ua.core.RepublishRequest;
import org.opcfoundation.ua.core.RepublishResponse;
import org.opcfoundation.ua.core.SetPublishingModeRequest;
import org.opcfoundation.ua.core.SetPublishingModeResponse;
import org.opcfoundation.ua.core.SubscriptionServiceSetHandler;
import org.opcfoundation.ua.core.TransferSubscriptionsRequest;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class SubscriptionService extends OPCService implements SubscriptionServiceSetHandler {
	public SubscriptionService(OPCServiceManager serviceManager) {
		super(serviceManager);
	}

	@Override
	public void onCreateSubscription(EndpointServiceRequest<CreateSubscriptionRequest, CreateSubscriptionResponse> req)
			throws ServiceFaultException {
		getServiceManager().onCreateSubscription(req);
	}

	@Override
	public void onModifySubscription(EndpointServiceRequest<ModifySubscriptionRequest, ModifySubscriptionResponse> req)
			throws ServiceFaultException {
		getServiceManager().onModifySubscription(req);
	}

	@Override
	public void onSetPublishingMode(EndpointServiceRequest<SetPublishingModeRequest, SetPublishingModeResponse> req)
			throws ServiceFaultException {
		getServiceManager().onSetPublishingMode(req);
	}

	@Override
	public void onPublish(EndpointServiceRequest<PublishRequest, PublishResponse> req) throws ServiceFaultException {
		getServiceManager().onPublish(req);
	}

	@Override
	public void onRepublish(EndpointServiceRequest<RepublishRequest, RepublishResponse> req)
			throws ServiceFaultException {
		getServiceManager().onRepublish(req);
	}

	@Override
	public void onTransferSubscriptions(
			EndpointServiceRequest<TransferSubscriptionsRequest, TransferSubscriptionsResponse> req)
			throws ServiceFaultException {
		getServiceManager().onTransferSubscription(req);
	}

	@Override
	public void onDeleteSubscriptions(
			EndpointServiceRequest<DeleteSubscriptionsRequest, DeleteSubscriptionsResponse> req)
			throws ServiceFaultException {
		getServiceManager().onDeleteSubscription(req);
	}
}
