package com.bichler.opc.client.arraydimensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opc.client.application.UAClient;
import opc.client.application.UADiscoveryClient;
import opc.client.application.listener.ConnectionListener;
import opc.client.service.ClientSession;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

import org.junit.Test;
import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.EncodeableSerializer;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.encoding.IDecoder;
import org.opcfoundation.ua.encoding.IEncodeable;
import org.opcfoundation.ua.encoding.IEncoder;
import org.opcfoundation.ua.encoding.utils.AbstractSerializer;

import com.bichler.opc.client.structures.E83ProductionDatasetReadOptionsType;

public class OPCUAClient_ArrayDimensions {

	@Test
	public void testArrayDimensions(EndpointDescription endpoint) {

		UAClient client = createClient();
		if (client == null) {
			return;
		}
		ClientSession session = null;
		try {
			session = createSession(client, endpoint);
			initialize(session);

			// br(client, session);

			callTest(client, session);

			System.out.println("Taste-Drücken um zu beenden....");
			try {
				System.in.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					client.closeSession(session, true);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initialize(ClientSession session) {
		E83ProductionDatasetReadOptionsType.init(session);
		/**
		 * add E83ProductionDatasetReadOptionsType serializer
		 */

		EncodeableSerializer.getInstance()
				.addSerializer(new AbstractSerializer(E83ProductionDatasetReadOptionsType.class,
						E83ProductionDatasetReadOptionsType.BINARY, E83ProductionDatasetReadOptionsType.XML) {
					@Override
					public void calcEncodeable(IEncodeable encodeable, IEncoder calculator) throws EncodingException {
						E83ProductionDatasetReadOptionsType obj = (E83ProductionDatasetReadOptionsType) encodeable;
						calculator.putInt32("Storage", (obj == null) ? null : obj.getStorage());
						calculator.putString("Name", (obj == null) ? null : obj.getName());
					}

					@Override
					public void putEncodeable(IEncodeable encodeable, IEncoder encoder) throws EncodingException {
						E83ProductionDatasetReadOptionsType obj = (E83ProductionDatasetReadOptionsType) encodeable;
						encoder.putInt32("Storage", (obj == null) ? null : obj.getStorage());
						encoder.putString("Name", (obj == null) ? null : obj.getName());
					}

					@Override
					public IEncodeable getEncodeable(IDecoder decoder) throws DecodingException {
						E83ProductionDatasetReadOptionsType result = new E83ProductionDatasetReadOptionsType();
						result.setStorage(decoder.getInt32("Storage"));
						result.setName(decoder.getString("Name"));
						return result;
					}
				});

		NamespaceTable.getDefaultInstance().addAll(session.getNamespaceUris().toArray());
	}

	private static final NodeId METHOD_GET_FILE_FOR_READ = new NodeId(1, 116);
	private static final NodeId OBJECT_FILE_FOR_READ = new NodeId(1, 111);

	private void callTest(UAClient client, ClientSession session) throws ServiceResultException {
		List<Node> errorNodes = new ArrayList<>();
		List<NodeId> others = new ArrayList<NodeId>();
		List<NodeId> parents = new ArrayList<>();
		// browse
		recBrowse(client, session, Identifiers.RootFolder, parents, errorNodes, others);

		System.out.println("Nodecount: " + errorNodes.size());

	}

	private void recBrowse(UAClient client, ClientSession session, NodeId parent, List<NodeId> parents,
			List<Node> errorNodes, List<NodeId> others) throws ServiceResultException {
		BrowseResult references = browse(client, session, parent, BrowseDirection.Forward);
		parents.add(parent);

		for (ReferenceDescription reference : references.getReferences()) {
			ExpandedNodeId refId = reference.getNodeId();
			NodeId nodeId = session.getNamespaceUris().toNodeId(refId);

			if (parents.contains(nodeId)) {
				continue;
			}

			boolean valid = testNodeScalar(client, session, errorNodes, nodeId, reference);
			if (!valid) {
				others.add(parent);
				continue;
			}

			recBrowse(client, session, nodeId, parents, errorNodes, others);
		}
	}

	private boolean testNodeScalar(UAClient client, ClientSession session, List<Node> errorNodes, NodeId nodeId,
			ReferenceDescription reference) throws ServiceResultException {

		if (NodeId.isNull(nodeId)) {
			return false;
		}

		NodeClass hasValueAttribute = reference.getNodeClass();
		Node node = client.readNode(session, nodeId, hasValueAttribute);
		int valueRank = -1;
		UnsignedInteger[] dims = null;
		switch (hasValueAttribute) {
		case Variable:
			valueRank = ((VariableNode) node).getValueRank();
			dims = ((VariableNode) node).getArrayDimensions();
			break;
		case VariableType:
			valueRank = ((VariableTypeNode) node).getValueRank();
			dims = ((VariableTypeNode) node).getArrayDimensions();
			break;
		default:
			return true;
		}

		// scalar
		if (valueRank <= 0) {
			// null is valid
			if (dims == null) {

			}
			// length 0 is valid
			else if (dims.length == 0) {

			}
			// length > 0 is invalid
			else {
				errorNodes.add(node);
			}
		}
		return true;
	}

	private BrowseResult browse(UAClient client, ClientSession session, NodeId source, BrowseDirection direction)
			throws ServiceResultException {

		return client.browse(session, source, direction, true, BrowseResultMask.getMask(BrowseResultMask.ALL),
				Identifiers.References, BrowseResultMask.getMask(BrowseResultMask.ALL), UnsignedInteger.ZERO, null,
				false);
	}

	private ClientSession createSession(UAClient client, EndpointDescription endpoint) throws ServiceResultException {
		ClientSession session = client.createSession(endpoint, "TestCalculationClient");
		session.addConnectionListener(new ConnectionListener() {

			@Override
			public void onServerConnected(ClientSession session) {
				System.out.println("Session-Connect");
			}

			@Override
			public void onServerClose(ClientSession session) {
				System.out.print("Session-Close");
			}
		});
		// activate with user and password
		client.activateSession(session, "efactory", "eFac4Monitor", null);

		return session;
	}

	private static UAClient createClient() {
		UAClient client = new UAClient();

		File cconfig = new File("config/clientconfig.xml");
		File ccert = new File("cert/cert.der");
		File ckey = new File("cert/key.pfx");

		try {
			client.setClientConfiguration(cconfig, ccert, ckey);
			client.getApplicationConfiguration().setApplication(new Application());
			client.setDefaultKeepAliveInterval(10000);
			return client;
		} catch (ServiceResultException e) {

		} catch (NullPointerException npe) {

		}

		return null;
	}

	public EndpointDescription discover(String url) throws ServiceResultException {
		UADiscoveryClient discovery = new UAClient().createDiscoveryClient();
		EndpointDescription[] endpoints;

		endpoints = discovery.getEndpoints(false, url);
		// select proper endpoint
		return discovery.selectEndpointNone(endpoints);
	}
}
