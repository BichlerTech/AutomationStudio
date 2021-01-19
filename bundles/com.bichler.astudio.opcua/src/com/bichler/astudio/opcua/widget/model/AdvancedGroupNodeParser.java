package com.bichler.astudio.opcua.widget.model;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AdvancedGroupNodeParser extends AbstractAdvanedParser {

	public static final String RootTag = "groups";

	public static final String NameTag = "name";
	public static final String ValueTag = "value";
	public static final String NodeTag = "node";
	public static final String GroupTag = "group";
	public static final String DeviceTag = "device";
	public static final String IsActiveTag = "isactive";
	public static final String DeviceTagNS = "devicens";
	public static final String ConfigNodeIdTagNS = "confignodeidns";
	public static final String DeviceTagID = "deviceid";
	public static final String ConfigNodeIdTagID = "confignodeid";
	public static final String DeviceTagName = "devicename";
	public static final String ConfigNodeTagName = "config";

	public static final String IndexTag = "index";
	public static final String CounterTag = "counter";

	private AdvancedRootConfigurationNode root;
	private Object current = null;

	public AdvancedGroupNodeParser(NamespaceTable namespaceTable, AdvancedRootConfigurationNode root,
			AdvancedSectionType type) {
		super(namespaceTable, type);
		this.root = root;
	}

	public AdvancedGroupNodeParser(NamespaceTable namespaceTable, AdvancedSectionType type) {
		this(namespaceTable, null, type);
	}

	private void setCurrent(Object node) {
		if (node instanceof AdvancedConfigurationNode) {
			this.current = ((AdvancedConfigurationNode) node);
		} else if (node instanceof AdvancedRootConfigurationNode) {
			this.current = node;
		}
	}

	private void addChild(AdvancedConfigurationNode node) {
		if (this.current instanceof AdvancedConfigurationNode) {
			((AdvancedConfigurationNode) this.current).addChild(node);
		} else if (this.current instanceof AdvancedRootConfigurationNode) {
			((AdvancedRootConfigurationNode) this.current).addChild(node);
		}
	}

	private void closeTag() {
		if (this.current instanceof AdvancedConfigurationNode) {
			this.current = ((AdvancedConfigurationNode) this.current).getParent();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (GroupTag.compareTo(qName) == 0) {
			closeTag();
		} else if (DeviceTag.compareTo(qName) == 0) {
			closeTag();
		}

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (RootTag.compareTo(qName) == 0) {
			String isActive = attributes.getValue(IsActiveTag);
			String configNS = attributes.getValue(ConfigNodeIdTagNS);
			String configID = attributes.getValue(ConfigNodeIdTagID);
			String configNodeTagName = attributes.getValue(ConfigNodeTagName);

			setCurrent(this.root);

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

		} else if (GroupTag.compareTo(qName) == 0) {
			String groupName = attributes.getValue(NameTag);

			AdvancedConfigurationNode node = new AdvancedConfigurationNode(getType());

			if (groupName != null) {
				node.setGroup(true);
				node.setGroupName(groupName);
			}
			addChild(node);
			setCurrent(node);
		} else if (DeviceTag.compareTo(qName) == 0) {
			String devName = attributes.getValue(NameTag);

			AdvancedConfigurationNode node = new AdvancedConfigurationNode(getType());

			if (devName != null) {
				node.setDevice(true);
				node.setDeviceName(devName);
			}
			addChild(node);
			setCurrent(node);
		} else if (CounterTag.compareTo(qName) == 0) {
			String counterName = attributes.getValue(NameTag);
			String configNS = attributes.getValue(ConfigNodeIdTagNS);
			String configID = attributes.getValue(ConfigNodeIdTagID);

			AdvancedConfigurationNode node = new AdvancedConfigurationNode(getType());
			node.setCounter(true);
			if (counterName != null) {
				node.setCounter(counterName);
			}

			if (configNS != null && configID != null && !configNS.isEmpty() && !configID.isEmpty()) {
				int nsIndex = getNamespaceTable().getIndex(configNS);
				NodeId id = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + configID);
				node.setDeviceId(id);
			}
			addChild(node);
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
