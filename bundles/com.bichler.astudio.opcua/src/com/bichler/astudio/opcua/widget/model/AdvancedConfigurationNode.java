package com.bichler.astudio.opcua.widget.model;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.widget.DATATYPE_MAPPING_TYPE;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class AdvancedConfigurationNode extends AbstractConfigNode {

	private boolean isActive = false;
	private boolean isGroup = false;
	private boolean isDevice = false;
	private boolean isState = false;

	private NodeId deviceId = NodeId.NULL;
	private NodeId configNodeId = NodeId.NULL;

	private int index = -1;
	private int value = -1;
	private String configNodeName = "";
	private String deviceName = "";
	private String groupName = "";
	private String addonName = "";
	private String counter = "";
	private List<AdvancedConfigurationNode> children = new ArrayList<>();
	private AbstractConfigNode parent;

	/**
	 * device data-mapping
	 */
	private NodeId addonId = NodeId.NULL;
	private NodeId groupId = NodeId.NULL;
	private NodeId meterId = NodeId.NULL;

	private String refDB = "";
	private String refStartAddress = "";
	private String addonRange = "";
	private String groupRange = "";
	private String meterName = "";
	private String model = "";
	private boolean enable = true;
	private String browsepath = "";
	private String datatype = "";
	private DATATYPE_MAPPING_TYPE mapping = DATATYPE_MAPPING_TYPE.SCALAR;
	private NodeId enableId = NodeId.NULL;
	private String enableName = "";
	private long cycletime;
	private NodeToTrigger trigger = null;

	public AdvancedConfigurationNode(AdvancedSectionType type) {
		super(type);
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public Boolean isActive() {
		return this.isActive;
	}

	public NodeId getDeviceId() {
		return this.deviceId;
	}

	public NodeId getConfigNodeId() {
		return this.configNodeId;
	}

	public Integer getIndex() {
		return this.index;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setConfigId(NodeId configId) {
		this.configNodeId = configId;
	}

	public void setDeviceId(NodeId deviceId) {
		this.deviceId = deviceId;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public String getConfigNodeName() {
		return this.configNodeName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setConfigNodeName(String configNodeName) {
		this.configNodeName = configNodeName;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String group) {
		this.groupName = group;
	}

	@Override
	public AbstractConfigNode getParent() {
		return parent;
	}

	public void addChild(AdvancedConfigurationNode child) {
		child.setParent(this);
		this.children.add(child);
	}

	protected void setParent(AbstractConfigNode parent) {
		this.parent = parent;
	}

	@Override
	public AdvancedConfigurationNode[] getChildren() {
		return this.children.toArray(new AdvancedConfigurationNode[0]);
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public boolean isDevice() {
		return isDevice;
	}

	public void setDevice(boolean isDevice) {
		this.isDevice = isDevice;
	}

	public boolean isState() {
		return isState;
	}

	public void setCounter(boolean isState) {
		this.isState = isState;
	}

	public void removeChild(AdvancedConfigurationNode node) {
		this.children.remove(node);
	}

	public NodeId getAddonId() {
		return addonId;
	}

	public void setAddonId(NodeId addonId) {
		this.addonId = addonId;
	}

	public NodeId getGroupId() {
		return groupId;
	}

	public void setGroupId(NodeId groupId) {
		this.groupId = groupId;
	}

	public String getAddonName() {
		return addonName;
	}

	public NodeId getMeterId() {
		return this.meterId;
	}

	public void setAddonName(String addonName) {
		this.addonName = addonName;
	}

	public String getGroupRange() {
		return groupRange;
	}

	public void setGroupRange(String groupRange) {
		this.groupRange = groupRange;
	}

	public String getAddonRange() {
		return addonRange;
	}

	public void setAddonRange(String addonRange) {
		this.addonRange = addonRange;
	}

	public String getRefStartAddress() {
		return refStartAddress;
	}

	public void setRefStartAddress(String refStartAddress) {
		this.refStartAddress = refStartAddress;
	}

	public String getRefDB() {
		return refDB;
	}

	public void setRefDB(String refDB) {
		this.refDB = refDB;
	}

	public void setMeterId(NodeId meterId) {
		this.meterId = meterId;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return this.model;
	}

	public String getMeterName() {
		return this.meterName;
	}

	public void setEnable(boolean isEnable) {
		this.enable = isEnable;
	}

	public boolean isEnable() {
		return this.enable;
	}

	public String getBrowsepath() {
		return this.browsepath;
	}

	public void setBrowsePath(String browsepath) {
		this.browsepath = browsepath;
	}

	public String getDataType() {
		return this.datatype;
	}

	public void setDataType(String datatype) {
		this.datatype = datatype;
	}

	public DATATYPE_MAPPING_TYPE getMapping() {
		return this.mapping;
	}

	public void setMapping(DATATYPE_MAPPING_TYPE mapping) {
		this.mapping = mapping;
	}

	public NodeId getEnableId() {
		return this.enableId;
	}

	public void setEnableId(NodeId enableId) {
		this.enableId = enableId;
	}

	public String getEnableName() {
		return this.enableName;
	}

	public void setEnableName(String enableName) {
		this.enableName = enableName;
	}

	public long getCycletime() {
		return cycletime;
	}

	public void setCycletime(long cycletime) {
		this.cycletime = cycletime;
	}

	public NodeToTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(NodeToTrigger trigger) {
		this.trigger = trigger;
	}
}
