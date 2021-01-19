package com.bichler.astudio.opcua.log;

import java.util.ArrayList;
import java.util.List;

public class LoggingModelNode {

	private String key;
	private String value;

	private LoggingModelNode parent = null;
	private List<LoggingModelNode> children = new ArrayList<>();

	public LoggingModelNode(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public boolean addChild(LoggingModelNode child) {
		setParent(this);
		return this.children.add(child);
	}

	public LoggingModelNode[] getChildren() {
		return this.children.toArray(new LoggingModelNode[0]);
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	private void setParent(LoggingModelNode parent) {
		this.parent = parent;
	}

}
