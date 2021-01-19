package com.bichler.astudio.opcua.widget;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;

public class ConsumptionTemplate {

	private String category = "";
	private String db = "";
	private String startaddress = "";
	private String structLength = "";
	private String browsename = "";
	private List<AbstractConfigNode> items = new ArrayList<>();

	public ConsumptionTemplate(String category) {
		this.category = category;
	}

	public AbstractConfigNode[] getItems() {
		return items.toArray(new AbstractConfigNode[0]);
	}

	public void removeItem(AbstractConfigNode item) {
		this.items.remove(item);

	}

	public void addItem(AbstractConfigNode item) {
		this.items.add(item);
	}

	public String getBrowsename() {
		return this.browsename;
	}

	public String getCategory() {
		return this.category;
	}

	public String getDB() {
		return this.db;
	}

	public String getStartAddress() {
		return this.startaddress;
	}

	public void setBrowsename(String browsename) {
		this.browsename = browsename;
	}

	public void setDB(String db) {
		this.db = db;
	}

	public void setStartAddress(String startaddress) {
		this.startaddress = startaddress;
	}

	public String getStructLength() {
		return structLength;
	}

	public void setStructLength(String structLength) {
		this.structLength = structLength;
	}

}
