package com.bichler.astudio.filesystem;

import java.util.HashMap;
import java.util.Map;

/**
 * this class contains all connections to HB Studio Projects,
 * this can be local or remote through ssh
 * @author applemc207da
 *
 */
public class HBStudioConnections {
	
	private Map<String, IFileSystem> connections = null;
	
	public HBStudioConnections() {
		this.setConnections(new HashMap<String, IFileSystem>());
	}

	public Map<String, IFileSystem> getConnections() {
		return connections;
	}

	public void setConnections(Map<String, IFileSystem> connections) {
		this.connections = connections;
	}
	
	public void addConnection(String name, IFileSystem system) {
		this.connections.put(name, system);
	}
	
	public void removeConnection(String hostName) {
		this.connections.remove(hostName);
	}
	
}
