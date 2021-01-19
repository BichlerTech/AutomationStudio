package com.bichler.astudio.opcua.widget.model;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.opcua.widget.ConsumptionTemplate;

public class DeviceConsumption {

	private boolean isActive = false;
	private List<ConsumptionTemplate> templates = new ArrayList<>();

	public DeviceConsumption() {

	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<ConsumptionTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<ConsumptionTemplate> templates) {
		this.templates = templates;
	}

	public void addTemplate(ConsumptionTemplate state) {
		this.templates.add(state);
	}

}
