package com.bichler.astudio.editor.pubsub.xml;

import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.editor.pubsub.nodes.PubSubConnection;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetField;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetWriter;
import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSet;
import com.bichler.astudio.editor.pubsub.nodes.PubSubWriterGroup;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.AbstractOPCDriverExporter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

import opc.sdk.core.node.mapper.NodeIdMapper;

public class PubSubExporter extends AbstractOPCDriverExporter {
	
	public PubSubExporter(IFileSystem filesystem, String path) {
		super(filesystem, path);
	}

	@Override
	protected void updateChild(IDriverNode entry, OPCUAUpdateNodeIdEvent trigger) {
		// Auto-generated method stub
	}

	@Override
	protected void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping) {
//		NodeId nodeId = ((EventDpItemModel) ((PubSubEntryModelNode) item2update).getItem()).getNodeId();
//		NodeId newNodeId = NodeIdMapper.mapNamespaceIndex(nodeId, mapping);
//		// dochange
//		((EventDpItemModel) ((PubSubEntryModelNode) item2update).getItem()).setNodeId(newNodeId);
//		EventDpItemModel parent = (EventDpItemModel) ((PubSubEntryModelNode) item2update).getItem();
//		for (int i = 0; i < parent.getChildren().length; i++) {
//			NodeId eventTypeId = NodeId.NULL;
//			NodeIdMapper.mapNamespaceIndex(eventTypeId, mapping);
//		}
	}

	private String getNsUriFromId(NamespaceTable nsTable, NodeId nodeId) {
		if(nsTable == null || nodeId == null)
			return "";
		return nsTable.getUri(nodeId.getNamespaceIndex());
	}

	private String getValueFromId(NodeId nodeId) {
		if(nodeId == null)
			return "";
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
		return new StringBuffer();
//		return saveChild((PubSubEntryModelNode) driverNode, nsTable);
	}

//	private StringBuffer saveChild(PubSubEntryModelNode driverNode, NamespaceTable nsTable) {
//		StringBuffer builder = new StringBuffer();
//		
////		PubSubEntryModelNode[] children = driverNode.getChildren();
////		// datapoint <dp>
//		if (driverNode instanceof PubSubConnection) {
//			PubSubConnection conn = (PubSubConnection)driverNode;
//			builder.append("  <conn>\n");
//			String nsuri = getNsUriFromId(nsTable, driverNode.getNId());
//			String id = getValueFromId( driverNode.getNId());
//			builder.append("    <nodeid value=\"ns=" + nsuri + ";" + id + "\" />\n");
//			builder.append("    <name value=\"" + conn.getName() + "\" />\n");
//			builder.append("    <enabled value=\"" +  conn.getEnabled() + "\" />\n");
//			builder.append("    <publisherIdType value=\"" +  (conn.getPublisherIdType() != null ? conn.getPublisherIdType().name() : "") + "\" />\n");
//			builder.append("    <publisherId value=\"" +  conn.getPublisherId() + "\" />\n");
//			builder.append("    <transportProfileUri value=\"" +  conn.getTransportProfileUri() + "\" />\n");
//			builder.append("    <connectionPropertiesSize value=\"" +  conn.getConnectionPropertiesSize() + "\" />\n");
//			builder.append("    <address value=\"" +  conn.getAddress() + "\" />\n");
//			builder.append("    <connectionProperties value=\"" +  conn.getConnectionProperties() + "\" />\n");
//			builder.append("    <connectionTransportSettings value=\"" +  conn.getConnectionTransportSettings() + "\" />\n");
//			builder.append("    <connchildren>\n");
//			for (PubSubEntryModelNode child : conn.getChildrenList()) {
//				builder.append(saveChild(child, nsTable));
//			}
//			builder.append("    </connchildren>\n");
//			builder.append("   </conn>\n");
//		}
//		else if(driverNode instanceof PubSubWriterGroup) {
//			PubSubWriterGroup wGroup = (PubSubWriterGroup)driverNode;
//			builder.append("  <wgroup>\n");
//			builder.append("    <name value=\"" + wGroup.getName() + "\" />\n");
//			builder.append("    <enabled value=\"" +  wGroup.getEnabled() + "\" />\n");
//			
//			builder.append("    <writerGroupId value=\"" +  wGroup.getWriterGroupId() + "\" />\n");
//			builder.append("    <publishingInterval value=\"" +  wGroup.getPublishingInterval() + "\" />\n");
//			builder.append("    <keepAliveTime value=\"" +  wGroup.getKeepAliveTime() + "\" />\n");
//			builder.append("    <priority value=\"" +  wGroup.getPriority() + "\" />\n");
//			builder.append("    <securityMode value=\"" +  wGroup.getSecurityMode() + "\" />\n");
//			builder.append("    <transportSettings value=\"" +  wGroup.getTransportSettings() + "\" />\n");
//			builder.append("    <messageSettings value=\"" +  wGroup.getMessageSettings() + "\" />\n");
//			builder.append("    <groupPropertiesSize value=\"" +  wGroup.getGroupPropertiesSize() + "\" />\n");
//			
//			builder.append("    <groupProperties value=\"" +  wGroup.getGroupProperties() + "\" />\n");
//			builder.append("    <encodingMimeType value=\"" +  wGroup.getEncodingMimeType() + "\" />\n");
//			
//			builder.append("    <maxEncapsulatedDataSetMessageCount value=\"" +  wGroup.getMaxEncapsulatedDataSetMessageCount() + "\" />\n");
//			builder.append("    <configurationFrozen value=\"" +  wGroup.getConfigurationFrozen() + "\" />\n");
//			builder.append("    <rtLevel value=\"" +  wGroup.getRtLevel() + "\" />\n");
//			
//			builder.append("    <wgroupchildren>\n");
//			for (PubSubEntryModelNode child : wGroup.getChildrenList()) {
//				builder.append(saveChild(child, nsTable));
//			}
//			builder.append("    </wgroupchildren>\n");
//			builder.append("  </wgroup>\n");
//		}
//		else if(driverNode instanceof PubSubDataSetWriter) {
//			PubSubDataSetWriter writer = (PubSubDataSetWriter)driverNode;
//			builder.append("  <writer>\n");
//			builder.append("    <name value=\"" +  writer.getName() + "\" />\n");
//			
//			builder.append("    <dataSetWriterId value=\"" +  writer.getDataSetWriterId() + "\" />\n");
//			builder.append("    <dataSetFieldContentMask value=\"" +  writer.getDataSetFieldContentMask() + "\" />\n");
//			builder.append("    <keyFrameCount value=\"" +  writer.getKeyFrameCount() + "\" />\n");
//			builder.append("    <messageSettings value=\"" +  writer.getMessageSettings() + "\" />\n");
//			
//			builder.append("    <transportSettings value=\"" +  writer.getTransportSettings() + "\" />\n");
//			builder.append("    <dataSetName value=\"" +  writer.getDataSetName() + "\" />\n");
//			
//			builder.append("    <dataSetWriterPropertiesSize value=\"" +  writer.getDataSetWriterPropertiesSize() + "\" />\n");
//			builder.append("    <dataSetName value=\"" +  writer.getDataSetName() + "\" />\n");
//			
//			builder.append("   </writer>\n");
//		}
//		else if(driverNode instanceof PubSubPublishedDataSet) {
//			PubSubPublishedDataSet pdataset = (PubSubPublishedDataSet)driverNode;
//			builder.append("  <pdataset>\n");
//			builder.append("    <name value=\"" +  pdataset.getName() + "\" />\n");
//			builder.append("    <publishedDataSetType value=\"" +  pdataset.getPublishedDataSetType() + "\" />\n");
//			builder.append("    <config value=\"" +  pdataset.getConfig() + "\" />\n");
//			builder.append("    <configurationFrozen value=\"" +  pdataset.getConfigurationFrozen() + "\" />\n");
//			
//			builder.append("    <pdatasetchildren>\n");
//			for (PubSubEntryModelNode child : pdataset.getChildrenList()) {
//				builder.append(saveChild(child, nsTable));
//			}
//			builder.append("    </pdatasetchildren>\n");
//			builder.append("   </pdataset>\n");
//		}
//		
//		else if(driverNode instanceof PubSubDataSetField) {
//			PubSubDataSetField pdfield = (PubSubDataSetField)driverNode;
//			builder.append("  <pdfield>\n");
//			builder.append("    <dataSetFieldType value=\"" +  pdfield.getDataSetFieldType() + "\" />\n");
//			builder.append("    <field value=\"" +  pdfield.getField() + "\" />\n");
//			builder.append("    <configurationFrozen value=\"" +  pdfield.getConfigurationFrozen() + "\" />\n");
//			builder.append("   </pdfield>\n");
//		}
//		return builder;
//	}

}
