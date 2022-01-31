package com.bichler.astudio.editor.calculation.handlers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.driver.calculation.CalculationDP;
import com.bichler.opc.driver.calculation.targets.CalculationTarget;
import com.bichler.astudio.editor.calculation.model.CalculationDPEditorImporter;
import com.bichler.astudio.editor.calculation.model.CalculationModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPReaderHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class CalculationDpReaderHandler extends AbstractOPCUADPReaderHandler {

	public static final String ID = "com.bichler.astudio.editor.calculation.1.0.1.import";

	@Override
	protected void insertDatapoint(Map<NodeId, Object> datapoints, Object dp) {
		CalculationTarget target = ((CalculationModelNode) dp).getDp().getTarget();
		if (target != null) {
			// source opc ua node id
			NodeId nodeId = target.getTargetId();
			if (NodeId.isNull(nodeId)) {
				return;
			}

			datapoints.put(nodeId, dp);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List readDatapoints(InputStream input) {
//		List<CalculationDP> dps = CalculationImporter.loadDPs(input,
//				ServerInstance.getInstance().getServerInstance().getNamespaceUris(), null, -1);
		CalculationDPEditorImporter importer = new CalculationDPEditorImporter();
		List<CalculationDP> dps2 = importer.loadDPs(input,
				ServerInstance.getInstance().getServerInstance().getNamespaceUris(), null, -1);

		List<CalculationModelNode> nodes = new ArrayList<>();
		for (CalculationDP dp : dps2) {
			CalculationModelNode rdp = new CalculationModelNode();
			rdp.setDP(dp);
			nodes.add(rdp);
		}

		return nodes;
	}

}
