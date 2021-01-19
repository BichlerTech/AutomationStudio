package com.bichler.astudio.editor.pubsub.nodes;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperDataSetMetaData;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariable;

public class PubSubPublishedDataItemsTemplate implements IConfig {
	private WrapperDataSetMetaData metaData = null;
	private WrapperPublishedVariable[] variablesToAdd = null;

	public PubSubPublishedDataItemsTemplate() {
	}
	
	public void init() {
		this.metaData = new WrapperDataSetMetaData();
		this.metaData.init();
		this.variablesToAdd = new WrapperPublishedVariable[0];
	}
	
	public WrapperDataSetMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(WrapperDataSetMetaData metaData) {
		this.metaData = metaData;
	}

	public WrapperPublishedVariable[] getVariablesToAdd() {
		return variablesToAdd;
	}

	public void setVariablesToAdd(WrapperPublishedVariable[] variablesToAdd) {
		this.variablesToAdd = variablesToAdd;
	}

	public PubSubPublishedDataItemsTemplate clone() {
		PubSubPublishedDataItemsTemplate obj = new PubSubPublishedDataItemsTemplate();
		obj.metaData = metaData.clone();

		if (this.variablesToAdd != null) {
			List<WrapperPublishedVariable> variables2add = new ArrayList<>();
			for (WrapperPublishedVariable vta : this.variablesToAdd) {
				variables2add.add(vta.clone());
			}
			obj.variablesToAdd = variables2add.toArray(new WrapperPublishedVariable[0]);
		}
		return obj;
	}

}
