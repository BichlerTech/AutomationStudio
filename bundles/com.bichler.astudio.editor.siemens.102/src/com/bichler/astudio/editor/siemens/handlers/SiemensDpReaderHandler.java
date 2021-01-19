package com.bichler.astudio.editor.siemens.handlers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.siemens.xml.SiemensDPEditorImporter;
import com.bichler.astudio.editor.siemens.xml.SiemensDPItem;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPReaderHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class SiemensDpReaderHandler extends AbstractOPCUADPReaderHandler {
	public static final String ID = "com.bichler.astudio.editor.siemens.1.0.2.import";

	@Override
	protected void insertDatapoint(Map<NodeId, Object> datapoints, Object dp) {
		NodeId nodeId = ((SiemensEntryModelNode) dp).getNId();
		if (NodeId.isNull(nodeId)) {
			return;
		}
		datapoints.put(nodeId, dp);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List readDatapoints(InputStream input) {
		SiemensDPEditorImporter importer = new SiemensDPEditorImporter();
		List<SiemensDPItem> dps = importer.loadDPs(input

				, ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		List<SiemensEntryModelNode> nodes = new ArrayList<>();
		for (SiemensDPItem dp : dps) {
			SiemensEntryModelNode rdp = SiemensEntryModelNode.loadFromDP(dp);
			nodes.add(rdp);
		}
		return nodes;
	}
}
