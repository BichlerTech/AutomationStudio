package com.bichler.opc.client.gffr;

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

public class OPCUAClient_GetFileForRead {

	@Test
	public void testFileRead(EndpointDescription endpoint) {

		UAClient client = createClient();
		if (client == null) {
			return;
		}
		ClientSession session = null;
		try {
			session = createSession(client, endpoint);
			initialize(session);

			// br(client, session);

			callFileTest(client, session);

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

	private void callFileTest(UAClient client, ClientSession session) throws ServiceResultException {
		E83ProductionDatasetReadOptionsType pdro = new E83ProductionDatasetReadOptionsType();
		pdro.setStorage(1);
		pdro.setName("standard");
		Variant input = new Variant(pdro);

		// call file for read
		CallMethodResult outputArgs = client.callMethod(session, OBJECT_FILE_FOR_READ, METHOD_GET_FILE_FOR_READ,
				new Variant[] { input });
		Variant[] outArgs = outputArgs.getOutputArguments();

		// out filehandle
		NodeId fileNodeId = (NodeId) outArgs[0].getValue();
		// out int server handle
		UnsignedInteger handle = (UnsignedInteger) outArgs[1].getValue();
		// out comp state machine
		NodeId compStateMachine = (NodeId) outArgs[2].getValue();

		waitForFile(client, session, compStateMachine, false);

		// file handle object
		Node fileObject = client.readNode(session, fileNodeId, NodeClass.Object);

		// method read()
		NodeId methodRead = translateBrowsePath(client, session, fileObject.getNodeId(), "Read");
		
		Variant fHandle = new Variant(handle), length = new Variant(1024);
		// call read file
		CallMethodResult outRead = client.callMethod(session, fileNodeId, methodRead,
				new Variant[] { fHandle, length });
		// file data
		Variant[] data = outRead.getOutputArguments();

		// method write()
		NodeId methodWrite = translateBrowsePath(client, session, fileObject.getNodeId(), "Write");
		// method write()
		NodeId methodClose = translateBrowsePath(client, session, fileObject.getNodeId(), "Close");
		// close file
		CallMethodResult outclose = client.callMethod(session, fileNodeId, methodClose, new Variant[] { fHandle });
	}

	private void waitForFile(UAClient client, ClientSession session, NodeId compStateMachine, boolean active)
			throws ServiceResultException {
		
		if (active) {
			// if there is a asynchronous file read
			if (!NodeId.isNull(compStateMachine)) {
				// statemachine object
				Node fileStateMachine = client.readNode(session, compStateMachine, NodeClass.Object);
				// statemachine currentstate
				NodeId currentState = translateBrowsePath(client, session, compStateMachine, "CurrentState");

				while (true) {
					DataValue state = client.read1(session, currentState, Attributes.Value, null, null, 0.0,
							TimestampsToReturn.Neither);
					LocalizedText value = (LocalizedText) state.getValue().getValue();

					if ("ReadTransfer".equalsIgnoreCase(value.getText())) {
						break;
					}
					System.out.println("waiting for read transfer");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	private void br(UAClient client, ClientSession session) throws ServiceResultException {
		// find input arguments

		List<QualifiedName> browsePath = new ArrayList<>();
		browsePath.add(new QualifiedName(0, "InputArguments"));
		BrowsePathResult inputArguments = client.translateBrowsePathToNodeIds(session, new NodeId(1, 116),
				browsePath.toArray(new QualifiedName[0]), false);

		// input argument datatype
		NodeId gffr_ia = new NodeId(1, 117);
		// read value
		DataValue value = client.read1(session, gffr_ia, Attributes.Value, null, null, 0.0, TimestampsToReturn.Both);
		// arguments
		Variant variant = value.getValue();
		Argument[] arguments = (Argument[]) variant.getValue();
		// argument datatype
		NodeId datatype = arguments[0].getDataType();
		// datatype node
		Node nodeOptions = client.readNode(session, datatype, NodeClass.DataType);
		// browse
		BrowseResult references = browse(client, session, nodeOptions.getNodeId());
		ReferenceDescription binaryEncoding = references.getReferences()[0];
		// encoding
		ExpandedNodeId encoding = binaryEncoding.getNodeId();
		Node encodingNode = client.readNode(session, session.getNamespaceUris().toNodeId(encoding), NodeClass.Variable);
		references = browse(client, session, encodingNode.getNodeId());
		// binary datadictionary node
		ExpandedNodeId binary = references.getReferences()[1].getNodeId();
		Node binaryNode = client.readNode(session, session.getNamespaceUris().toNodeId(binary), NodeClass.Variable);
		references = browse(client, session, binaryNode.getNodeId());
		// default encoding euromap
		ExpandedNodeId encodingEuromap = references.getReferences()[0].getNodeId();
		Node encodingEuromapNode = client.readNode(session, session.getNamespaceUris().toNodeId(encodingEuromap),
				NodeClass.Variable);

		ExpandedNodeId encodingTarget = references.getReferences()[1].getNodeId();
		Node encodingTargetNode = client.readNode(session, session.getNamespaceUris().toNodeId(encodingTarget),
				NodeClass.Variable);

		System.out.println();
	}

	private BrowseResult browse(UAClient client, ClientSession session, NodeId source) throws ServiceResultException {
		return client.browse(session, source, BrowseDirection.Both, true,
				BrowseResultMask.getMask(BrowseResultMask.ALL), Identifiers.References,
				BrowseResultMask.getMask(BrowseResultMask.ALL), UnsignedInteger.ZERO, null, false);
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
			// InputStream configurationFile = new FileInputStream(cconfig);
			// InputStream certFile = new FileInputStream(ccert);
			// InputStream privKeyFile = new FileInputStream(ckey);
			// File certificateStore = null;

			// certificateStore = new File(txt_certStore.getText());
			// if (configurationFile == null || certFile == null || privKeyFile == null) {
			// TODO: Generate default
			// throw new NullPointerException();
			// }

			client.setClientConfiguration(cconfig, ccert, ckey);
			client.getApplicationConfiguration().setApplication(new Application());
			client.setDefaultKeepAliveInterval(10000);
			return client;
		} catch (ServiceResultException e) {

		} catch (NullPointerException npe) {

		}

		return null;
	}

	private NodeId translateBrowsePath(UAClient client, ClientSession session, NodeId startNodeId, String... names)
			throws ServiceResultException {
		List<QualifiedName> path = new ArrayList<>();
		for (String s : names) {
			path.add(new QualifiedName(s));
		}

		BrowsePathResult result = client.translateBrowsePathToNodeIds(session, startNodeId,
				path.toArray(new QualifiedName[0]), false);
		return session.getNamespaceUris().toNodeId(result.getTargets()[0].getTargetId());
	}

	public EndpointDescription discover(String url) throws ServiceResultException {
		UADiscoveryClient discovery = new UAClient().createDiscoveryClient();
		EndpointDescription[] endpoints;

		endpoints = discovery.getEndpoints(false, url);
		// select proper endpoint
		return discovery.selectEndpointNone(endpoints);
	}
}
