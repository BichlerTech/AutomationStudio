package com.bichler.astudio.editor.pubsub.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * top level element for pub sub tree model
 */
public class PubSubModel extends PubSubEntryModelNode {

	private String version = "0.0.1";
	private List<PubSubConnection> connections = new ArrayList<PubSubConnection>();
	private List<PubSubPublishedDataSet> publishedDS = new ArrayList<PubSubPublishedDataSet>();
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<PubSubConnection> getConnections() {
		return connections;
	}

	public void setConnections(List<PubSubConnection> connections) {
		this.connections = connections;
	}

	public List<PubSubPublishedDataSet> getPublishedDS() {
		return publishedDS;
	}

	public void setPublishedDS(List<PubSubPublishedDataSet> publishedDS) {
		this.publishedDS = publishedDS;
	}
}
