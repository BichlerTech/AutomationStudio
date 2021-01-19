package com.bichler.astudio.opcua.events;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.filesystem.IFileSystem;

public class OPCUADPReaderParameter {
	public static final String PARAMETER_ID = "opcuadpreader";
	/** filesystem */
	private IFileSystem filesystem = null;
	/** datapoint mapping from file */
	private Map<NodeId, Object> datapoints = new HashMap<>();
	/** path of datapoints file */
	private String path = "";

	public OPCUADPReaderParameter(String path) {
		this.path = path;
	}

	public Map<NodeId, Object> getDatapointList() {
		return this.datapoints;
	}

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public String getPath() {
		return this.path;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
