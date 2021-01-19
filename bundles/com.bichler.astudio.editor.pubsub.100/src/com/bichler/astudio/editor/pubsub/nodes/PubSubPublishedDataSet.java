package com.bichler.astudio.editor.pubsub.nodes;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;

public class PubSubPublishedDataSet extends PubSubEntryModelNode {

	private String name;
	private PubSubPublishedDataSetType publishedDataSetType;
	private Object config;
//    union {
//        /* The UA_PUBSUB_DATASET_PUBLISHEDITEMS has currently no additional members
//         * and thus no dedicated config structure.*/
//        UA_PublishedDataItemsTemplateConfig itemsTemplate;
//        UA_PublishedEventConfig event;
//        UA_PublishedEventTemplateConfig eventTemplate;
//    } config;
	/* This flag is 'read only' and is set internally based on the PubSub state. */
	private Boolean configurationFrozen;

	private List<PubSubDataSetField> children = null;

	public PubSubPublishedDataSet() {
		super();
		this.children = new ArrayList<PubSubDataSetField>();
		this.type = PUBSUBENTRYTYPE.PUBLISHEDDATASET;
	}

	public List<PubSubDataSetField> getChildren() {
		return this.children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PubSubPublishedDataSetType getPublishedDataSetType() {
		return publishedDataSetType;
	}

	public void setPublishedDataSetType(PubSubPublishedDataSetType publishedDataSetType) {
		this.publishedDataSetType = publishedDataSetType;
	}

	public Object getConfig() {
		return config;
	}

	public void setConfig(Object config) {
		this.config = config;
	}

	public Boolean getConfigurationFrozen() {
		return configurationFrozen;
	}

	public void setConfigurationFrozen(Boolean configurationFrozen) {
		this.configurationFrozen = configurationFrozen;
	}
}
