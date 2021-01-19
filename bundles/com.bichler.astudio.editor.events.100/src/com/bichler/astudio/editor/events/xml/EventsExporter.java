package com.bichler.astudio.editor.events.xml;

import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.AbstractOPCDriverExporter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.hbsoft.driver.opc.events.dp.AbstractEventModel;
import com.hbsoft.driver.opc.events.dp.EventDefItemModel;
import com.hbsoft.driver.opc.events.dp.EventDpItemModel;
import com.hbsoft.driver.opc.events.expression.AbstractExpressionModel;
import com.hbsoft.driver.opc.events.expression.group.GroupExpressionModel;

import opc.sdk.core.node.mapper.NodeIdMapper;

public class EventsExporter extends AbstractOPCDriverExporter {
	
	public EventsExporter(IFileSystem filesystem, String path) {
		super(filesystem, path);
	}

	@Override
	protected void updateChild(IDriverNode entry, OPCUAUpdateNodeIdEvent trigger) {
		// Auto-generated method stub
	}

	@Override
	protected void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping) {
		NodeId nodeId = ((EventDpItemModel) ((EventEntryModelNode) item2update).getItem()).getNodeId();
		NodeId newNodeId = NodeIdMapper.mapNamespaceIndex(nodeId, mapping);
		// dochange
		((EventDpItemModel) ((EventEntryModelNode) item2update).getItem()).setNodeId(newNodeId);
		EventDpItemModel parent = (EventDpItemModel) ((EventEntryModelNode) item2update).getItem();
		for (int i = 0; i < parent.getChildren().length; i++) {
			NodeId eventTypeId = NodeId.NULL;
			NodeIdMapper.mapNamespaceIndex(eventTypeId, mapping);
		}
	}

	private String getNsUriFromId(NamespaceTable nsTable, NodeId nodeId) {
		return nsTable.getUri(nodeId.getNamespaceIndex());
	}

	private String getValueFromId(NodeId nodeId) {
		String[] idelements = nodeId.toString().split(";");
		String id = "";
		if (idelements != null) {
			if (idelements.length == 1) {
				id = idelements[0];
			} else if (idelements.length == 2) {
				id = idelements[1];
			}
		}
		return id;
	}

	@Override
	protected StringBuffer saveChildren(IDriverNode driverNode, NamespaceTable nsTable) {
		return saveChild((EventEntryModelNode) driverNode, nsTable);
	}

	private StringBuffer saveChild(EventEntryModelNode driverNode, NamespaceTable nsTable) {
		StringBuffer builder = new StringBuffer();
		AbstractEventModel dp = driverNode.getItem();
		EventEntryModelNode[] children = driverNode.getChildren();
		// datapoint <dp>
		if (dp instanceof EventDpItemModel) {
			builder.append("  <dp>\n");
			String nsuri = getNsUriFromId(nsTable, ((EventDpItemModel) dp).getNodeId());
			String id = getValueFromId(((EventDpItemModel) dp).getNodeId());
			builder.append("    <nodeid value=\"ns=" + nsuri + ";" + id + "\" />\n");
			builder.append("    <name value=\"" + ((EventDpItemModel) dp).getDisplayname() + "\" />\n");
			for (EventEntryModelNode child : children) {
				builder.append(saveChild(child, nsTable));
			}
			builder.append("   </dp>\n");
		}
		// event <event>
		else if (dp instanceof EventDefItemModel) {
			builder.append("  <event>\n");
			String nsuriEvent = getNsUriFromId(nsTable, ((EventDefItemModel) dp).getNodeId());
			String idEvent = getValueFromId(((EventDefItemModel) dp).getNodeId());
			builder.append("    <nodeid value=\"ns=" + nsuriEvent + ";" + idEvent + "\" />\n");
			builder.append("    <name value=\"" + ((EventDefItemModel) dp).getDisplayname() + "\" />\n");
			builder.append("    <message value=\"" + ((EventDefItemModel) dp).getMessage() + "\" />\n");
			builder.append("    <severity value=\"" + ((EventDefItemModel) dp).getSeverity() + "\" />\n");
			for (EventEntryModelNode child : children) {
				builder.append(saveChild(child, nsTable));
			}
			builder.append("   </event>\n");
		} else if (dp instanceof AbstractExpressionModel) {
			builder.append("      <expression type=\"" + ((AbstractExpressionModel) dp).getType() + "\" >\n");
			builder.append(((AbstractExpressionModel) dp).toXMLFormat(nsTable));
			if (dp instanceof GroupExpressionModel) {
				for (EventEntryModelNode child : children) {
					builder.append(saveChild(child, nsTable));
				}
			}
			builder.append("      </expression>\n");
		}
		return builder;
	}

}
