package com.bichler.astudio.editor.siemens.model;

import java.util.ArrayList;
import java.util.List;

public class SiemensRootStartConfigurationNode {
	private List<SiemensStartConfigurationNode> children = new ArrayList<>();
	private boolean isActive = false;

	public SiemensRootStartConfigurationNode() {
	}

	public SiemensStartConfigurationNode[] getChildren() {
		return children.toArray(new SiemensStartConfigurationNode[0]);
	}

	public void addChild(SiemensStartConfigurationNode child) {
		this.children.add(child);
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void removeChild(SiemensStartConfigurationNode node) {
		this.children.remove(node);
	}
}
