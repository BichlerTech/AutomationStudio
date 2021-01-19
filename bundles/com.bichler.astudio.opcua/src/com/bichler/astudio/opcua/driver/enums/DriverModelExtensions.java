package com.bichler.astudio.opcua.driver.enums;

public enum DriverModelExtensions {

	Rockwell_Model("*.csv"), Rockwell_Structure("*.L5X"), Siemens_Model(new String[] { "*.csv", "*.txt" }),
	Siemens_Structure("*.csv");

	DriverModelExtensions(String extension) {
		this.extensions = new String[] { extension };
	}

	DriverModelExtensions(String[] extensions) {
		this.extensions = extensions;
	}

	private String[] extensions;

	public String[] getExtensions() {
		return extensions;
	}

	public static String[] getDriverModelExtensions(String drivertype) {
		String[] ext = null;

		if ("aggregation".equals(drivertype)) {
		} else if ("calculation".equals(drivertype)) {
		} else if ("rockwell".equals(drivertype)) {
			ext = Rockwell_Model.getExtensions();
		} else if ("siemens".equals(drivertype)) {
			ext = Siemens_Model.getExtensions();
		} else if ("xml_da".equals(drivertype)) {

		}

		return ext;
	}

	public static String[] getDriverStructExtensions(String type) {
		String[] ext = null;

		if ("aggregation".equals(type)) {
		} else if ("calculation".equals(type)) {
		} else if ("rockwell".equals(type)) {
			ext = Rockwell_Structure.getExtensions();
		} else if ("siemens".equals(type)) {
		} else if ("xml_da".equals(type)) {
		}

		return ext;
	}
}
