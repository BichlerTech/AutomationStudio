package com.bichler.astudio.editor.allenbradley.xml;

import java.util.Map;

import opc.sdk.core.node.mapper.NodeIdMapper;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.AbstractOPCDriverExporter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class AllenBradleyDpExporter extends AbstractOPCDriverExporter {

	public AllenBradleyDpExporter(IFileSystem filesystem, String path) {
		super(filesystem, path);
	}

	@Override
	protected void updateChild(IDriverNode item2update, OPCUAUpdateNodeIdEvent trigger) {
		NodeId nodeId = ((AllenBradleyEntryModelNode) item2update).getNodeId();
		NodeToTrigger triggerNode = ((AllenBradleyEntryModelNode) item2update).getTrigger();

		NodeId newId = trigger.getNewId();
		NodeId oldId = trigger.getOldId();

		// dochange
		if (oldId.equals(nodeId)) {
			((AllenBradleyEntryModelNode) item2update).setNodeId(newId);
		}
		if (oldId.equals(triggerNode.nodeId)) {
			triggerNode.nodeId = newId;
		}

	}

	@Override
	protected void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping) {
		NodeId nodeId = ((AllenBradleyEntryModelNode) item2update).getNodeId();
		NodeToTrigger triggerNode = ((AllenBradleyEntryModelNode) item2update).getTrigger();

		NodeId newNodeId = NodeIdMapper.mapNamespaceIndex(nodeId, mapping);
		NodeId newTriggerId = NodeIdMapper.mapNamespaceIndex(triggerNode.nodeId, mapping);
		// dochange
		((AllenBradleyEntryModelNode) item2update).setNodeId(newNodeId);
		triggerNode.nodeId = newTriggerId;

	}

	@Override
	protected StringBuffer saveChildren(IDriverNode driverNode, NamespaceTable nsTable) {

		StringBuffer buffer = new StringBuffer();
		AllenBradleyEntryModelNode item = (AllenBradleyEntryModelNode) driverNode;
		buffer.append("  <dp>\n");
		if (item.getNodeId() != null) {
			String[] idelements = item.getNodeId().toString().split(";");
			String id = "";
			if (idelements != null) {
				if (idelements.length == 1) {
					id = idelements[0];
				} else if (idelements.length == 2) {

					id = idelements[1];
				}
			}
			String nsuri = nsTable.getUri(item.getNodeId().getNamespaceIndex());
			buffer.append("    <nodeid value=\"ns=" + nsuri + ";" + id + "\" />\n");
		}
		buffer.append("    <symbolname value=\"" + escape(item.getSymbolName()) + "\" />\n");
		buffer.append("    <isactive value=\"" + item.isActive() + "\" />\n");
		buffer.append("    <cycletime value=\"" + item.getCycletime() + "\" />\n");
		buffer.append("    <index value=\"" + item.getIndex() + "\" />\n");
		// buffer.append(" <address value=\"" + item.getAddress() + "\" />\n");
		buffer.append("    <mapping value=\"" + item.getMapping() + "\" />\n");
		// buffer.append(" <index value=\"" + item.getAddress() + "\" />\n");
		buffer.append("    <datatype value=\"" + item.getDataType() + "\" />\n");

		// now generate the trigger name
		if (item.getTrigger() != null && item.getTrigger().triggerName != null
				&& !item.getTrigger().triggerName.isEmpty()) {
			buffer.append("    <trigger value=\"" + item.getTrigger().triggerName + "\" />\n");
		}

		buffer.append("    <description value=\"" + escape(item.getDesc()) + "\" />\n");
		buffer.append("  </dp>\n");
		
		return buffer;
	}
}
