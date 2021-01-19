package com.bichler.astudio.editor.siemens.datenbaustein;

import java.util.HashMap;
import java.util.Map;

public class SiemensResources {
	private Map<String, SiemensDBResourceManager> dbResourceManagers = new HashMap<>();
	private static SiemensResources instance = null;

	public static SiemensResources getInstance() {
		if (instance == null) {
			instance = new SiemensResources();
		}
		return instance;
	}

	public SiemensDBResourceManager getDBResourceManager(String drivername) {
		return new SiemensDBResourceManager();
	}

	public Map<String, SiemensDBResourceManager> getDbResourceManagers() {
		return dbResourceManagers;
	}

	public void setDbResourceManagers(Map<String, SiemensDBResourceManager> dbResourceManagers) {
		this.dbResourceManagers = dbResourceManagers;
	}
}
