package opc.sdk.server.service.profile;

import opc.sdk.server.core.managers.OPCServiceManager;

import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.core.AddNodesRequest;
import org.opcfoundation.ua.core.AddNodesResponse;
import org.opcfoundation.ua.core.AddReferencesRequest;
import org.opcfoundation.ua.core.AddReferencesResponse;
import org.opcfoundation.ua.core.BrowseNextRequest;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowseRequest;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.DeleteNodesRequest;
import org.opcfoundation.ua.core.DeleteNodesResponse;
import org.opcfoundation.ua.core.DeleteReferencesRequest;
import org.opcfoundation.ua.core.DeleteReferencesResponse;
import org.opcfoundation.ua.core.NodeManagementServiceSetHandler;
import org.opcfoundation.ua.core.QueryFirstRequest;
import org.opcfoundation.ua.core.QueryFirstResponse;
import org.opcfoundation.ua.core.QueryNextRequest;
import org.opcfoundation.ua.core.QueryNextResponse;
import org.opcfoundation.ua.core.RegisterNodesRequest;
import org.opcfoundation.ua.core.RegisterNodesResponse;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.ua.core.TranslateBrowsePathsToNodeIdsResponse;
import org.opcfoundation.ua.core.UnregisterNodesRequest;
import org.opcfoundation.ua.core.UnregisterNodesResponse;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class NodeService extends OPCService implements NodeManagementServiceSetHandler {
	public NodeService(OPCServiceManager serviceManager) {
		super(serviceManager);
	}

	@Override
	public void onAddNodes(EndpointServiceRequest<AddNodesRequest, AddNodesResponse> req) throws ServiceFaultException {
		getServiceManager().onAddNodes(req);
	}

	@Override
	public void onAddReferences(EndpointServiceRequest<AddReferencesRequest, AddReferencesResponse> req)
			throws ServiceFaultException {
		getServiceManager().onAddReference(req);
	}

	@Override
	public void onDeleteNodes(EndpointServiceRequest<DeleteNodesRequest, DeleteNodesResponse> req)
			throws ServiceFaultException {
		getServiceManager().onDeleteNodes(req);
	}

	@Override
	public void onDeleteReferences(EndpointServiceRequest<DeleteReferencesRequest, DeleteReferencesResponse> req)
			throws ServiceFaultException {
		getServiceManager().onDeleteReferences(req);
	}

	@Override
	public void onBrowse(EndpointServiceRequest<BrowseRequest, BrowseResponse> req) throws ServiceFaultException {
		getServiceManager().onBrowse(req);
	}

	@Override
	public void onBrowseNext(EndpointServiceRequest<BrowseNextRequest, BrowseNextResponse> req)
			throws ServiceFaultException {
		getServiceManager().onBrowseNext(req);
	}

	@Override
	public void onTranslateBrowsePathsToNodeIds(
			EndpointServiceRequest<TranslateBrowsePathsToNodeIdsRequest, TranslateBrowsePathsToNodeIdsResponse> req)
			throws ServiceFaultException {
		getServiceManager().onTranslateBrowsePathsToNodeIds(req);
	}

	@Override
	public void onRegisterNodes(EndpointServiceRequest<RegisterNodesRequest, RegisterNodesResponse> req)
			throws ServiceFaultException {
		getServiceManager().onRegisterNodes(req);
	}

	@Override
	public void onUnregisterNodes(EndpointServiceRequest<UnregisterNodesRequest, UnregisterNodesResponse> req)
			throws ServiceFaultException {
		getServiceManager().onUnregisterNodes(req);
	}

	@Override
	public void onQueryFirst(EndpointServiceRequest<QueryFirstRequest, QueryFirstResponse> req)
			throws ServiceFaultException {
		getServiceManager().onQueryFirst(req);
	}

	@Override
	public void onQueryNext(EndpointServiceRequest<QueryNextRequest, QueryNextResponse> req)
			throws ServiceFaultException {
		getServiceManager().onQueryNext(req);
	}
}
