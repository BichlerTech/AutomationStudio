/* ========================================================================
 * Copyright (c) 2005-2015 The OPC Foundation, Inc. All rights reserved.
 *
 * OPC Foundation MIT License 1.00
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * The complete license agreement can be found here:
 * http://opcfoundation.org/License/MIT/1.00/
 * ======================================================================*/

package org.opcfoundation.ua.core;

import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;
import org.opcfoundation.ua.common.ServiceFaultException;

public interface NodeManagementServiceSetHandler {

	void onAddNodes(EndpointServiceRequest<AddNodesRequest, AddNodesResponse> req) throws ServiceFaultException;

	void onAddReferences(EndpointServiceRequest<AddReferencesRequest, AddReferencesResponse> req)
			throws ServiceFaultException;

	void onDeleteNodes(EndpointServiceRequest<DeleteNodesRequest, DeleteNodesResponse> req)
			throws ServiceFaultException;

	void onDeleteReferences(EndpointServiceRequest<DeleteReferencesRequest, DeleteReferencesResponse> req)
			throws ServiceFaultException;

	void onBrowse(EndpointServiceRequest<BrowseRequest, BrowseResponse> req) throws ServiceFaultException;

	void onBrowseNext(EndpointServiceRequest<BrowseNextRequest, BrowseNextResponse> req) throws ServiceFaultException;

	void onTranslateBrowsePathsToNodeIds(
			EndpointServiceRequest<TranslateBrowsePathsToNodeIdsRequest, TranslateBrowsePathsToNodeIdsResponse> req)
			throws ServiceFaultException;

	void onRegisterNodes(EndpointServiceRequest<RegisterNodesRequest, RegisterNodesResponse> req)
			throws ServiceFaultException;

	void onUnregisterNodes(EndpointServiceRequest<UnregisterNodesRequest, UnregisterNodesResponse> req)
			throws ServiceFaultException;

	void onQueryFirst(EndpointServiceRequest<QueryFirstRequest, QueryFirstResponse> req) throws ServiceFaultException;

	void onQueryNext(EndpointServiceRequest<QueryNextRequest, QueryNextResponse> req) throws ServiceFaultException;

}
