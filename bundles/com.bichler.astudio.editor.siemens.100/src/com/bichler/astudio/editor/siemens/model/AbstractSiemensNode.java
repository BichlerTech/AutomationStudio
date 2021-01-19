package com.bichler.astudio.editor.siemens.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;

public abstract class AbstractSiemensNode extends AbstractDriverModelViewNode {
	private boolean active = true;
	private String dataType = SiemensDataType.UNKNOWN.name();
	private String description = "";
	private float index = 0;
	private Image labelImage = null;
	private String name = "";
	private String symbolName = "";
	private AbstractSiemensNode parent = null;
	List<AbstractSiemensNode> children = new ArrayList<>();
	private String address = "";
	private boolean isStructure = false;
	private boolean defaultIndex = true;
	private String initValue = "";
	// private Float nextIndex = 0f;

	protected AbstractSiemensNode(SiemensDBResourceManager structureManager) {
		super(structureManager);
	}

	public boolean isDefaultIndex() {
		return this.defaultIndex;
	}

	public void setInitValue(String value) {
		this.initValue = value;
	}

	public String getInitValue() {
		return this.initValue;
	}

	@Override
	public String getText() {
		return getName();
	}

	// public TreeViewer getViewer() {
	// return this.viewer;
	// }
	@Override
	public SiemensDBResourceManager getStructureManager() {
		return (SiemensDBResourceManager) super.getStructureManager();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getLabelImage() {
		return labelImage;
	}

	public void setLabelImage(Image labelImage) {
		this.labelImage = labelImage;
	}

	// public float getIndex() {
	// return this.index;
	// }
	public float getIndex() {
		return this.index;
	}

	public float getAddressIndex() {
		float i = this.index;
		if (getParent() != null) {
			// parent is an array
			if (getParent() instanceof SiemensArrayNode) {
				if (this.getClass() != SiemensStructNode.class) {
					int childIndex = ((SiemensArrayNode) getParent()).getChildIndex(this);
					i *= childIndex;
				}
				// parent is a struct node
				else {
					int childIndex = ((SiemensArrayNode) getParent()).getChildIndex(this);
					i = childIndex * ((SiemensStructNode) this).getStructLength();
				}
			} else if (getParent() instanceof SiemensStructNode) {
				// prevent root start node
				if (!((SiemensStructNode) getParent()).isRoot() && ((SiemensStructNode) getParent()).isArrayEntry()) {
					if (this.getClass() == SiemensArrayNode.class) {
						return getParent().getAddressIndex() + i;
					}
					// int childIndex = ((SiemensStructNode) getParent())
					// .getChildIndex(this);
					// if (childIndex != -1) {
					// // i *= childIndex;
					// }
				}
			}
			i += getParent().getAddressIndex();
		}
		return i;
	}

	public float getAddressIndex2() {
		float i = this.index;
		if (getParent() != null) {
			// parent is an array
			if (getParent() instanceof SiemensArrayNode) {
				if (this.getClass() != SiemensStructNode.class) {
					int childIndex = ((SiemensArrayNode) getParent()).getChildIndex(this);
					String datatype = getDataType();
					if ("BOOL".equalsIgnoreCase(datatype)) {
						int rest = childIndex / 8;
						int a = childIndex % 8;
						i = rest + (a * i);
					} else {
						i *= childIndex;
					}
				}
				// parent is a struct node
				else {
					int childIndex = ((SiemensArrayNode) getParent()).getChildIndex(this);
					i = childIndex * ((SiemensStructNode) this).getStructLength();
				}
			} else if (getParent() instanceof SiemensStructNode) {
				// prevent root start node
				if (!((SiemensStructNode) getParent()).isRoot() && ((SiemensStructNode) getParent()).isArrayEntry()) {
					if (this.getClass() == SiemensArrayNode.class) {
						return getParent().getAddressIndex() + i;
					}
					// int childIndex = ((SiemensStructNode) getParent())
					// .getChildIndex(this);
					// if (childIndex != -1) {
					// // i *= childIndex;
					// }
				}
			}
			i += getParent().getAddressIndex2();
		}
		return i;
	}

	public void setIndex(float index) {
		this.index = index;
		this.defaultIndex = false;
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

	public void addChild(AbstractSiemensNode child) {
		child.setParent(this);
		this.children.add(child);
	}

	protected void setParent(AbstractSiemensNode parent) {
		this.parent = parent;
	}

	public AbstractSiemensNode[] getChildren() {
		return this.children.toArray(new AbstractSiemensNode[0]);
	}

	public AbstractSiemensNode getParent() {
		return this.parent;
	}

	public abstract AbstractSiemensNode cloneNode(boolean calcIndex, boolean includeChildren);

	public void setActiveAll(boolean active) {
		setActive(active);
		for (AbstractSiemensNode child : getChildren()) {
			child.setActiveAll(active);
		}
	}

	/**
	 * Clone nodes to treeviewer, include the address index
	 * 
	 * @param collection
	 * @return
	 */
	public AbstractSiemensNode fillActiveAll(List<AbstractSiemensNode> collection,
			Map<String, AbstractSiemensNode> mapping) {
		AbstractSiemensNode cloned = this.cloneNode(true, false);
		if (isActive()) {
			collection.add(cloned);
			mapping.put(getSymbolName(), cloned);
		}
		for (AbstractSiemensNode child : getChildren()) {
			child.fillActiveAll(collection, mapping);
		}
		return cloned;
	}

	// public String getAddress() {
	// return address;
	// }
	//
	// public void setAddress(String address) {
	// this.address = address;
	// }
	//
	// public int getId() {
	// return id;
	// }
	//
	// public void setId(int id) {
	// this.id = id;
	// }
	public static AbstractSiemensNode loadFromDP(SiemensDPItem dp) {
		SiemensEntryModelNode node = new SiemensEntryModelNode();
		node.setActive(dp.isActive());
		// node.setAddress(dp.getAddress());
		node.setIndex(dp.getIndex());
		// node.setAddressType(dp.getAddressType());
		node.setCycletime(dp.getCycletime());
		node.setDataType(dp.getDataType().name());
		node.setDescription(dp.getDescription());
		// node.setDataType(dp.getDataType().name());
		node.setDescription(dp.getDescription());
		// node.setTrigger(dp.getTriggerNode());
		// node.setHistorical(dp.isHistorical());
		// node.setId(dp.getId());
		node.setNodeId(dp.getNodeId());
		node.setSymbolName(dp.getSymbolname());
		return null;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// public String getIndexNew() {
	// return index_new;
	// }
	//
	// public void setIndexNew(String index_new) {
	// this.index_new = index_new;
	// }
	public String getSymbolName() {
		return symbolName;
	}

	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	public abstract int getChildIndex(AbstractSiemensNode child);

	public void initializeAddress() {
		String symbolName = getName();
		String symbolAddress = symbolName.replaceFirst("DB", "").trim();
		initAddress(symbolAddress);
	}

	private void initAddress(String symbolAddress) {
		for (AbstractSiemensNode child : getChildren()) {
			child.setAddress(symbolAddress);
			child.initAddress(symbolAddress);
		}
	}

	public void setStructure(boolean isStructure) {
		this.isStructure = isStructure;
	}

	public boolean isStructure() {
		return this.isStructure;
	}

	public void setDefaultIndex(boolean defaultIndex) {
		this.defaultIndex = defaultIndex;
	}
	// public void setNextIndex(Float nextIndex) {
	// this.nextIndex = nextIndex;
	// }
	// public void setAddressType(SiemensAreaCode addressType) {
	// this.addressType = addressType;
	// }
	//
	// public SiemensAreaCode getAddressType() {
	// return this.addressType;
	// }
}
