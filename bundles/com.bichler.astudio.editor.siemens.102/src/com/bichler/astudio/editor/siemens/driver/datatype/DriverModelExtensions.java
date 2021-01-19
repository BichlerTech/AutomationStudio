package com.bichler.astudio.editor.siemens.driver.datatype;

public enum DriverModelExtensions {

	Siemens_Model(new String[] { "*.csv", "*.txt" }), Siemens_Structure("*.csv");

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

		if ("siemens".equals(drivertype)) {
			ext = Siemens_Model.getExtensions();
		} else if ("xml_da".equals(drivertype)) {

		}

		return ext;
	}

	public static String[] getDriverStructExtensions(String type) {
		String[] ext = null;

		if ("siemens".equals(type)) {
		}

		return ext;
	}
}
