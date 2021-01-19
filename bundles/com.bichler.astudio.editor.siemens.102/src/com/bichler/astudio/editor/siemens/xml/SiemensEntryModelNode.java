package com.bichler.astudio.editor.siemens.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.siemens.driver.datatype.SIEMENS_DATA_TYPE;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensEntryModelNode implements IDriverNode {
	private String version = "v.1.0.2";
	private String buildDate = "01.01.2021";
	private String license = "";
	private NodeId nodeId = null;
	private long cycletime = 1000;
	private String displayname = "";
	private String browsepath = "";
	private int arrayLength = 0;
	private String address = "";
	private String vorgabe1 = "";
	private String vorgabe2 = "";
	private SIEMENS_MAPPING_TYPE mapping = SIEMENS_MAPPING_TYPE.SCALAR;
	private NodeToTrigger trigger = null;
	private String fileDir = "";
	private boolean active = true;
	private String dataType = SiemensDataType.UNKNOWN.name();
	private String description = "";
	private float index = 0;
	private Image labelImage = null;
	private String symbolname = "";
	private List<SiemensEntryModelNode> children = null;
	private SiemensAreaCode addressType;
	private boolean valid = true;
	private SiemensEntryModelNode neighbor = null;
	private String indexNew = "";
	private String datatypeNew = "";
	private NodeId rootId;
	private boolean simulate;

	public SiemensEntryModelNode() {
		this.setChildren(new ArrayList<SiemensEntryModelNode>());
	}

	public SiemensEntryModelNode getChild(String key) {
		for (SiemensEntryModelNode node : children) {
			if (node.getSymbolName().compareTo(key) == 0) {
				return node;
			}
		}
		return null;
	}

	public static SiemensEntryModelNode loadFromDP(SiemensDPItem dp) {
		SiemensEntryModelNode node = new SiemensEntryModelNode();
		node.setActive(dp.isActive());
		node.setAddress(Integer.toString(dp.getDBAddress()));
		node.setIndex(dp.getIndex());
		node.setAddressType(dp.getAddressType());
		node.setCycletime(dp.getCycletime());
		node.setDataType(dp.getDataType());
		node.setDescription(dp.getDescription());
		node.setMapping(dp.getMapping());
		node.setRootId(dp.getRootId());
		NodeToTrigger t = new NodeToTrigger();
		t.triggerName = dp.getTriggerNode();
		node.setTrigger(t);
		node.setSimulate(dp.isSimulate());
		node.setNodeId(dp.getNodeId());
		node.setSymbolName(dp.getSymbolname());
		return node;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getVorgabe1() {
		return vorgabe1;
	}

	public void setVorgabe1(String vorgabe1) {
		this.vorgabe1 = vorgabe1;
	}

	public String getVorgabe2() {
		return vorgabe2;
	}

	public void setVorgabe2(String vorgabe2) {
		this.vorgabe2 = vorgabe2;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	/**
	 * public String getImportPath() { return importPath; }
	 * 
	 * public void setImportPath(String importPath) { this.importPath = importPath;
	 * }
	 */
	public List<SiemensEntryModelNode> getChildren() {
		return children;
	}

	public void setChildren(List<SiemensEntryModelNode> children) {
		this.children = children;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCycletime() {
		return cycletime;
	}

	public void setCycletime(long cycletime) {
		this.cycletime = cycletime;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	@Override
	public String getBrowsepath() {
		return browsepath;
	}

	public void setBrowsepath(String browsepath) {
		this.browsepath = browsepath;
	}

	public NodeToTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(NodeToTrigger trigger) {
		this.trigger = trigger;
	}

	public void setArrayRange(int[] rangeResults) {
		int min = rangeResults[0];
		int max = rangeResults[1];
		this.arrayLength = max - min + 1;
	}

	public int getArrayLength() {
		return this.arrayLength;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isSimulate() {
		return simulate;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setSimulate(boolean simulate) {
		this.simulate = simulate;
	}

	public String getSymbolName() {
		return symbolname;
	}

	public void setSymbolName(String datapoint) {
		this.symbolname = datapoint;
	}

	public Image getLabelImage() {
		return labelImage;
	}

	public void setLabelImage(Image labelImage) {
		this.labelImage = labelImage;
	}

	public float getIndex() {
		return index;
	}

	public void setIndex(float index) {
		this.index = index;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAddressType(SiemensAreaCode addressType) {
		this.addressType = addressType;
	}

	public SiemensAreaCode getAddressType() {
		return this.addressType;
	}

	public SIEMENS_MAPPING_TYPE getMapping() {
		return this.mapping;
	}

	public void setMapping(SIEMENS_MAPPING_TYPE valueRank) {
		this.mapping = valueRank;
	}

	public float calculateEndIndex() {
		SIEMENS_DATA_TYPE type = SIEMENS_DATA_TYPE.getTypeFromString(this.dataType);
		return type.getIndexLength() + this.index;
	}

	public void setNeighbor(SiemensEntryModelNode neighbor) {
		this.neighbor = neighbor;
	}

	protected SiemensEntryModelNode getNeighbor() {
		return neighbor;
	}

	public String getIndexNew() {
		return this.indexNew;
	}

	public void setIndexNew(String index) {
		this.indexNew = index;
	}

	public String getDataTypeNew() {
		return this.datatypeNew;
	}

	public void setDataTypeNew(String datatypeNew) {
		this.datatypeNew = datatypeNew;
	}

	@Override
	public String getDname() {
		return getDisplayname();
	}

	@Override
	public String getDesc() {
		return getDescription();
	}

	@Override
	public String getDtype() {
		return getDataType();
	}

	@Override
	public NodeId getNId() {
		return this.nodeId;
	}

	public void setNId(NodeId nid) {
		this.nodeId = nid;
	}

	@Override
	public boolean isValid() {
		return this.valid;
	}

	public NodeId getRootId() {
		return this.rootId;
	}

	public void setRootId(NodeId rootId) {
		this.rootId = rootId;
	}
}
