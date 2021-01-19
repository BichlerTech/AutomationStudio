package com.bichler.astudio.connections.enums;

public enum ConnectionType {

	Virtual_Device("Virtual Device"), SSH("DataHub");

	private String description = "";
	
	private ConnectionType(String description) {
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
}
