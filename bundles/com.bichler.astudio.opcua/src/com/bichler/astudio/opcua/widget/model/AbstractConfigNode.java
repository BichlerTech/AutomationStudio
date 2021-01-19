package com.bichler.astudio.opcua.widget.model;

public abstract class AbstractConfigNode {

	private AdvancedSectionType type = AdvancedSectionType.Undefined;
//	private String mapping = "";

	public AbstractConfigNode(AdvancedSectionType type) {
		this.type = type;
	}

	public abstract AbstractConfigNode getParent();

	public abstract AdvancedConfigurationNode[] getChildren();
	// public void moveChild(IConfigNode child, int indizes2move);

	public AdvancedSectionType getType() {
		return this.type;
	}

//	public String getMapping(){
//		return this.mapping;
//	}
//	
//	public void setMapping(String mapping){
//		this.mapping = mapping;
//	}
}
