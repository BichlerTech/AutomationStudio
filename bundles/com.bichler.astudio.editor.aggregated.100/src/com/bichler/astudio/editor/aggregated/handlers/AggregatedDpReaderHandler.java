package com.bichler.astudio.editor.aggregated.handlers;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.aggregated.model.AggregatedDPEditorImporter;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPReaderHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class AggregatedDpReaderHandler extends AbstractOPCUADPReaderHandler {

	public static final String ID = "com.bichler.astudio.editor.aggregated.1.0.0.import";

	@Override
	protected void insertDatapoint(Map<NodeId, Object> datapoints, Object dp) {
		// data point entrys
		if (dp == null) {
			return;
		}
		// source opc ua node id
		NodeId source = ((AggregatedDpModelNode) dp).getDPItem().getServerNodeId();
		if (NodeId.isNull(source)) {
			return;
		}
		// add to datapoints
		datapoints.put(source, (AggregatedDpModelNode) dp);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List readDatapoints(InputStream input) {

		AggregatedDPEditorImporter importer = new AggregatedDPEditorImporter();
		// try {
		List<AggregatedDpModelNode> dps = importer.loadDPs(input,
				ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		//
		// List<Beckhoff_ModelNode> nodes = new ArrayList<>();
		//
		// for (Beckhoff_DPItem dp : dps) {
		// Beckhoff_ModelNode rdp = Beckhoff_ModelNode.loadFromDP(dp);
		// nodes.add(rdp);
		// }
		//
		// return nodes;
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

//		List<AggregatedDPItem> dps = AggregatedImporter.loadDPs(input,
//				ServerInstance.getInstance().getServerInstance().getNamespaceUris());

		return dps;
	}
}
