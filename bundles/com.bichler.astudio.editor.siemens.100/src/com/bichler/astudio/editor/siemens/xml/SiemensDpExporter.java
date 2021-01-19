package com.bichler.astudio.editor.siemens.xml;

import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.AbstractOPCDriverExporter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

import opc.sdk.core.node.mapper.NodeIdMapper;

public class SiemensDpExporter extends AbstractOPCDriverExporter {

	public SiemensDpExporter(IFileSystem filesystem, String path) {
		super(filesystem, path);
	}

	@Override
	protected void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping) {
		NodeId nodeId = ((SiemensEntryModelNode) item2update).getNId();
		NodeToTrigger triggerNode = ((SiemensEntryModelNode) item2update).getTrigger();
		NodeId newNodeId = NodeIdMapper.mapNamespaceIndex(nodeId, mapping);
		NodeId newTriggerId = NodeIdMapper.mapNamespaceIndex(triggerNode.nodeId, mapping);
		// dochange
		((SiemensEntryModelNode) item2update).setNodeId(newNodeId);
		triggerNode.nodeId = newTriggerId;
	}

	@Override
	protected void updateChild(IDriverNode item2update, OPCUAUpdateNodeIdEvent trigger) {
		NodeId nodeId = ((SiemensEntryModelNode) item2update).getNId();
		NodeToTrigger triggerNode = ((SiemensEntryModelNode) item2update).getTrigger();
		NodeId newId = trigger.getNewId();
		NodeId oldId = trigger.getOldId();
		// dochange
		if (oldId.equals(nodeId)) {
			((SiemensEntryModelNode) item2update).setNodeId(newId);
		}
		if (oldId.equals(triggerNode.nodeId)) {
			triggerNode.nodeId = newId;
		}
	}

	@Override
	protected StringBuffer saveChildren(IDriverNode driverNode, NamespaceTable nsTable) {
		StringBuffer buffer = new StringBuffer();
		SiemensEntryModelNode item = (SiemensEntryModelNode) driverNode;
		buffer.append("  <dp>\n");
		if (item.getNId() != null) {
			String[] idelements = item.getNId().toString().split(";");
			String id = "";
			if (idelements != null) {
				if (idelements.length == 1) {
					id = idelements[0];
				} else if (idelements.length == 2) {
					id = idelements[1];
				}
			}
			String nsuri = nsTable.getUri(item.getNId().getNamespaceIndex());
			buffer.append("    <nodeid value=\"ns=" + nsuri + ";" + id + "\" />\n");
		}
		if (item.getRootId() != null) {
			String[] idelements = item.getRootId().toString().split(";");
			String id = "";
			if (idelements != null) {
				if (idelements.length == 1) {
					id = idelements[0];
				} else if (idelements.length == 2) {
					id = idelements[1];
				}
			}
			String nsuri = nsTable.getUri(item.getRootId().getNamespaceIndex());
			buffer.append("    <rootid value=\"ns=" + nsuri + ";" + id + "\" />\n");
		}
		buffer.append("    <symbolname value=\"" + escape(item.getSymbolName()) + "\" />\n");
		buffer.append("    <isactive value=\"" + item.isActive() + "\" />\n");
		buffer.append("    <issimulate value=\"" + item.isSimulate() + "\" />\n");
		buffer.append("    <cycletime value=\"" + item.getCycletime() + "\" />\n");
		buffer.append("    <addresstype value=\"" + item.getAddressType() + "\" />\n");
		buffer.append("    <address value=\"" + item.getAddress() + "\" />\n");
		buffer.append("    <mapping value=\"" + item.getMapping().name() + "\" />\n");
		buffer.append("    <index value=\"" + item.getIndex() + "\" />\n");
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
