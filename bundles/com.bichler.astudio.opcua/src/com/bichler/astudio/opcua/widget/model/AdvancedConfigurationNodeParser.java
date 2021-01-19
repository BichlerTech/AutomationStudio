package com.bichler.astudio.opcua.widget.model;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AdvancedConfigurationNodeParser extends AbstractAdvanedParser {

	public static final String StartTag = "main";
	public static final String ConsumptionTag = "consumption";
	public static final String ValueTag = "value";
	public static final String NodeTag = "node";
	// public static final String GroupTag = "group";
	public static final String IsActiveTag = "isactive";
	public static final String IsEnable = "isenable";
	public static final String DeviceTagNS = "devicens";
	public static final String ConfigNodeIdTagNS = "confignodeidns";
	public static final String DeviceTagID = "deviceid";
	public static final String ConfigNodeIdTagID = "confignodeid";
	public static final String DeviceTagName = "devicename";
	public static final String ConfigNodeTagName = "config";
	// public static final String Name = "name";

	public static final String IndexTag = "index";
	public static final String DB = "db";
	public static final String Start = "start";
	public static final String RangeAddon = "raddon";
	public static final String RangeGroups = "rgroups";
	public static final String RangeMeter = "rmeter";
	public static final String AddonTagID = "addonid";
	public static final String AddonTagNS = "addonns";
	public static final String AddonTagName = "addonname";

	public static final String EnableTagID = "enableid";
	public static final String EnableTagNS = "enablens";
	public static final String EnableTagName = "enablename";

	public static final String GroupTagNS = "groupns";
	public static final String GroupTagID = "groupid";
	public static final String GroupTagName = "groupname";
	public static final String MeterTagNS = "meterns";
	public static final String MeterTagID = "meterid";
	public static final String MeterTagName = "metername";
	public static final String ConsumptionDB = "consumptiondb";
	public static final String ConsumptionAddress = "consumptionaddress";
	public static final String ConsumptionStructLength = "consumptionlength";
	public static final String StateDatatype = "statedatatype";
	public static final String Browsepath = "browsepath";
	public static final String Datatype = "datatype";
	public static final String Name = "name";
	public static final String Mapping = "mapping";
	public static final String CycleTime = "cycletime";
	public static final String TriggerNode = "trigger";

	// private NamespaceTable namespaceTable = null;
	private AdvancedRootConfigurationNode root;

	// private AdvancedSectionType type;

	public AdvancedConfigurationNodeParser(NamespaceTable namespaceTable, AdvancedRootConfigurationNode root,
			AdvancedSectionType type) {
		super(namespaceTable, type);
		this.root = root;
	}

	public AdvancedConfigurationNodeParser(NamespaceTable namespaceTable, AdvancedSectionType type) {
		this(namespaceTable, null, type);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (StartTag.compareTo(qName) == 0) {
			String isActive = attributes.getValue(IsActiveTag);
			String configNS = attributes.getValue(ConfigNodeIdTagNS);
			String configID = attributes.getValue(ConfigNodeIdTagID);
			String configNodeTagName = attributes.getValue(ConfigNodeTagName);
			String db = attributes.getValue(DB);
			String startConfig = attributes.getValue(Start);
			String rAddon = attributes.getValue(RangeAddon);
			String rGroup = attributes.getValue(RangeGroups);
			String metaId = attributes.getValue(RangeMeter);

			if (isActive != null && !isActive.isEmpty()) {
				this.root.setActive(Boolean.parseBoolean(isActive));
			}

			if (configNS != null && configID != null && !configNS.isEmpty() && !configID.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(configNS);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + configID);
				this.root.setRefNodeId(id);
			}

			if (configNodeTagName != null) {
				this.root.setRefNodeName(configNodeTagName);
			}

			if (startConfig != null) {
				this.root.setStartAddress(startConfig);
			}

			if (db != null) {
				this.root.setDatablock(db);
			}

			if (rAddon != null) {
				this.root.setAddonRange(rAddon);
			}

			if (rGroup != null) {
				this.root.setGroupRange(rGroup);
			}

			if (metaId != null) {
				this.root.setMetaId(metaId);
			}
		} else if (NodeTag.compareTo(qName) == 0) {
			AdvancedConfigurationNode node = new AdvancedConfigurationNode(getType());

			String active = attributes.getValue(IsActiveTag);
			String configNS = attributes.getValue(ConfigNodeIdTagNS);
			String configID = attributes.getValue(ConfigNodeIdTagID);
			String deviceNS = attributes.getValue(DeviceTagNS);
			String deviceID = attributes.getValue(DeviceTagID);
			String index = attributes.getValue(IndexTag);

			String value = attributes.getValue(ValueTag);
			String configName = attributes.getValue(ConfigNodeTagName);
			String deviceName = attributes.getValue(DeviceTagName);

			String enableNs = attributes.getValue(EnableTagNS);
			String enableId = attributes.getValue(EnableTagID);
			String enableName = attributes.getValue(EnableTagName);

			String addonNs = attributes.getValue(AddonTagNS);
			String addonId = attributes.getValue(AddonTagID);
			String addonName = attributes.getValue(AddonTagName);

			String groupNs = attributes.getValue(GroupTagNS);
			String groupId = attributes.getValue(GroupTagID);
			String groupName = attributes.getValue(GroupTagName);

			String metaNs = attributes.getValue(MeterTagNS);
			String metaId = attributes.getValue(MeterTagID);
			String metaName = attributes.getValue(MeterTagName);

			String enable = attributes.getValue(IsEnable);

			/** active */
			if (active != null && !active.isEmpty()) {
				node.setActive(Boolean.parseBoolean(active));
			}
			/** enable */
			if (enable != null && !enable.isEmpty()) {
				node.setEnable(Boolean.parseBoolean(enable));
			}

			/** enable node id */
			if (enableNs != null && enableId != null && !enableNs.isEmpty() && !enableId.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(enableNs);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + enableId);
				node.setEnableId(id);
			}

			/** config node id */
			if (configNS != null && configID != null && !configNS.isEmpty() && !configID.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(configNS);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + configID);
				node.setConfigId(id);
			}
			/** configuration node name */
			if (configName != null) {
				node.setConfigNodeName(configName);
			}

			/** device id */
			if (deviceNS != null && deviceID != null && !deviceNS.isEmpty() && !deviceID.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(deviceNS);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + deviceID);
				node.setDeviceId(id);
			}

			/** device node name */
			if (deviceName != null) {
				node.setDeviceName(deviceName);
			}

			/** addon node id */
			if (addonNs != null && addonId != null && !addonNs.isEmpty() && !addonId.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(addonNs);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + addonId);
				node.setAddonId(id);
			}

			if (enableName != null) {
				node.setEnableName(enableName);
			}

			/** addon name */
			if (addonName != null) {
				node.setAddonName(addonName);
			}

			/** group node id */
			if (groupNs != null && groupId != null && !groupNs.isEmpty() && !groupId.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(groupNs);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + groupId);
				node.setGroupId(id);
			}

			/** meta node id */
			if (metaNs != null && metaId != null && !metaNs.isEmpty() && !metaId.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(metaNs);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + metaId);
				node.setMeterId(id);
			}

			if (metaName != null) {
				node.setMeterName(metaName);
			}

			/** addon name */
			if (groupName != null) {
				node.setGroupName(groupName);
			}

			/** index */
			if (index != null && !index.isEmpty()) {
				node.setIndex(Integer.parseInt(index));
			}
			/** value */
			if (value != null && !value.isEmpty()) {
				node.setValue(Integer.parseInt(value));
			}

			this.root.addChild(node);
		}
	}

	@Override
	public AdvancedRootConfigurationNode getRoot() {
		return this.root;
	}

	@Override
	public void setRoot(AdvancedRootConfigurationNode root) {
		this.root = root;
	}
}
