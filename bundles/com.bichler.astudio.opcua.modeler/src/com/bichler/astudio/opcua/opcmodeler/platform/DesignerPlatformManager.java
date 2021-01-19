package com.bichler.astudio.opcua.opcmodeler.platform;

public class DesignerPlatformManager {
	private static DesignerPlatformManager instance = null;

	public static DesignerPlatformManager getInstance() {
		if (instance == null) {
			instance = new DesignerPlatformManager();
		}
		return instance;
	}

	private DesignerModifcationStore modificationStore;

	protected DesignerPlatformManager() {
		this.modificationStore = new DesignerModifcationStore();
	}

	public void modifyNodeId() {
	}
}
