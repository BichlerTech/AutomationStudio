package com.bichler.astudio.editor.aggregated.model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.opc.comdrv.OPCUADevice;

public class AggregatedDPEditorExporter {

	public static void exportDPs(StringBuffer buffer,
			List<AggregatedDpModelNode> items, UAServerModelNode server) {
		String[] ns = null;

		NamespaceTable uris = ServerInstance.getInstance().getServerInstance()
				.getNamespaceUris();
		try {
			if (server != null && server.getDevice() != null && server.getDevice().getUaclient() != null) {
				DataValue value = server
						.getDevice()
						.getUaclient()
						.read1(server.getDevice().getUaclient()
								.getActiveSession(),
								Identifiers.Server_NamespaceArray,
								Attributes.Value, null, null, 0.0,
								TimestampsToReturn.Both);

				if (value != null && value.getValue() != null
						&& value.getValue().getValue() != null) {
					ns = (String[]) value.getValue().getValue();
				}
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(AggregatedDPEditorExporter.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		buffer.append("#ns;id	browsepath	targetdisplayname	#ns;id	targetbrowsepath	active	read	write\n");
		for (AggregatedDpModelNode dp : items) {
			// generate nodeid value
			if (!NodeId.isNull(dp.getDPItem().getServerNodeId())) {
				
		
			String[] idelements = dp.getDPItem().getServerNodeId().toString()
					.split(";");
			String id = "";
			if (idelements != null) {
				if (idelements.length == 1) {
					id = idelements[0];
				} else if (idelements.length == 2) {

					id = idelements[1];
				}
			}

			buffer.append(uris.getUri(dp.getDPItem().getServerNodeId()
					.getNamespaceIndex())
					+ ";"
					+ id
					+ "\t"
					+ dp.getDPItem().getServerBrowsePath()
					+ "\t");
			}
			else{
				buffer.append("\t"	+ "\t");
			}
			// target id
			String targetid = "";
			if (dp.getDPItem().getTargetNodeId() == null) {
				targetid = dp.getDPItem().getLoadedTargetNodeId();
			} else {
				String[] targetidelements = dp.getDPItem().getTargetNodeId()
						.toString().split(";");

				if (targetidelements != null) {
					if (targetidelements.length == 1) {
						targetid = targetidelements[0];
					} else if (targetidelements.length == 2) {

						targetid = targetidelements[1];
					}
				}
			}
			// wrapp namespace
			if (ns != null && ns.length > 0
					&& dp.getDPItem().getTargetNodeId() != null) {
				String namespace = ns[dp.getDPItem().getTargetNodeId()
						.getNamespaceIndex()];
				targetid = namespace + ";" + targetid;
			} else {
				if (!NodeId.isNull(dp.getDPItem().getTargetNodeId())) {
					targetid = dp.getDPItem().getTargetNodeId().toString();
				}
			}

			if (targetid == null) {
				targetid = "";
			}

			buffer.append(dp.getDPItem().getTargetDisplayName() + "\t"
					+ targetid + "\t" + dp.getDPItem().getTargetBrowsePath()
					+ "\t" + dp.getDPItem().isActive() + "\t"
					+ dp.getDPItem().isRead() + "\t" + dp.getDPItem().isWrite()
					+ "\n");
		}
	}
}
