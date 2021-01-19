package com.bichler.astudio.opcua.widget.model;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.astudio.opcua.widget.ConsumptionTemplate;
import com.bichler.astudio.opcua.widget.DATATYPE_MAPPING_TYPE;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class ConsumptionImportHandler extends DefaultHandler {

	private ConsumptionTemplate current;
	private NamespaceTable namespaceTable;
	private DeviceConsumption state;

	public ConsumptionImportHandler(DeviceConsumption state, NamespaceTable namespaceTable) {
		this.state = state;
		this.current = null;
		this.namespaceTable = namespaceTable;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (AdvancedConfigurationNodeParser.StartTag.equals(qName)) {
			String isactive = attributes.getValue("isactive");
			if (isactive != null && !isactive.isEmpty()) {
				state.setActive(Boolean.parseBoolean(isactive));
			}
		} else if (AdvancedConfigurationNodeParser.ConsumptionTag.equals(qName)) {
			String category = attributes.getValue(AdvancedConfigurationNodeParser.Name);
			this.current = new ConsumptionTemplate(category);

			String db = attributes.getValue(AdvancedConfigurationNodeParser.DB);
			String startaddress = attributes.getValue(AdvancedConfigurationNodeParser.ConsumptionAddress);
			String structlength = attributes.getValue(AdvancedConfigurationNodeParser.ConsumptionStructLength);
			if (db != null) {
				this.current.setDB(db);
			}
			if (startaddress != null) {
				this.current.setStartAddress(startaddress);
			}
			if (structlength != null) {
				this.current.setStructLength(structlength);
			}
		} else if (AdvancedConfigurationNodeParser.NodeTag.equals(qName)) {
			String name = attributes.getValue(AdvancedConfigurationNodeParser.ConfigNodeTagName);
			String tag_id = attributes.getValue(AdvancedConfigurationNodeParser.ConfigNodeIdTagID);
			String tag_ns = attributes.getValue(AdvancedConfigurationNodeParser.ConfigNodeIdTagNS);
			String browsepath = attributes.getValue(AdvancedConfigurationNodeParser.Browsepath);
			String device = attributes.getValue(AdvancedConfigurationNodeParser.DeviceTagName);
			String mapping = attributes.getValue(AdvancedConfigurationNodeParser.Mapping);
			String datatype = attributes.getValue(AdvancedConfigurationNodeParser.Datatype);
			String index = attributes.getValue(AdvancedConfigurationNodeParser.IndexTag);
			String enabled = attributes.getValue(AdvancedConfigurationNodeParser.IsActiveTag);
			String cycletime = attributes.getValue(AdvancedConfigurationNodeParser.CycleTime);
			String trigger = attributes.getValue(AdvancedConfigurationNodeParser.TriggerNode);
			if (index == null) {
				index = "-1";
			}

			boolean isenabled = Boolean.parseBoolean(enabled);
			long cycle = -1;
			try {
				cycle = Long.parseLong(cycletime);
			} catch (NumberFormatException ex) {

			}
			int nsIndex = this.namespaceTable.getIndex(tag_ns);

			NodeId nodeid = NodeId.NULL;
			try {
				nodeid = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + tag_id);
			} catch (IllegalArgumentException e) {
				// e.printStackTrace();
			}
			AdvancedConfigurationNode node2add = new AdvancedConfigurationNode(AdvancedSectionType.State);
			node2add.setConfigNodeName(name);
			node2add.setConfigId(nodeid);
			node2add.setBrowsePath(browsepath);
			node2add.setDeviceName(device);
			// node2add.setMapping(mapping);
			try {
				node2add.setMapping(DATATYPE_MAPPING_TYPE.valueOf(mapping));
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
			node2add.setDataType(datatype);
			node2add.setRefStartAddress(index);
			node2add.setEnable(isenabled);
			node2add.setCycletime(cycle);
			NodeToTrigger t = new NodeToTrigger();
			if (trigger == null) {
				trigger = "";
			}
			t.triggerName = trigger;
			node2add.setTrigger(t);
			this.current.addItem(node2add);
		}
	}

	@Override
	public void endElement(String uri, String localname, String qname) throws SAXException {
		if (AdvancedConfigurationNodeParser.ConsumptionTag.equals(qname)) {
			this.state.addTemplate(this.current);
		}
	}

}