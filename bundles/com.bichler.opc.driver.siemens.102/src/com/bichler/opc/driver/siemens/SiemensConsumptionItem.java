package com.bichler.opc.driver.siemens;

import java.util.ArrayList;
import java.util.List;

import com.bichler.opc.driver.siemens.dp.SiemensDPItem;

public class SiemensConsumptionItem {
	private int db = 0;
	private double startaddress = 0.0;
	private double length = 0.0;
	private List<SiemensDPItem> items = null;

	public SiemensConsumptionItem() {
		this.items = new ArrayList<SiemensDPItem>();
	}

	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	public double getStartaddress() {
		return startaddress;
	}

	public void setStartaddress(double startaddress) {
		this.startaddress = startaddress;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public List<SiemensDPItem> getItems() {
		return items;
	}

	public void setItems(List<SiemensDPItem> items) {
		this.items = items;
	}
}
