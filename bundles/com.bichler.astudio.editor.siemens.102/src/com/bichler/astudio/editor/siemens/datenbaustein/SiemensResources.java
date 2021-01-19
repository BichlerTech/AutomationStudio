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

	// private void addDBResourceManager(String drivername,
	// SiemensDBResourceManager structManager) {
	// this.dbResourceManagers.put(drivername, structManager);
	// }
	public SiemensDBResourceManager getDBResourceManager(String drivername) {
		// SiemensDBResourceManager manager = this.dbResourceManagers
		// .get(drivername);
		// if (manager == null) {
		SiemensDBResourceManager manager = new SiemensDBResourceManager();
		// addDBResourceManager(drivername, manager);
		// }
		return manager;
	}
}
