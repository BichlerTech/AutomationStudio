package com.bichler.astudio.opcua.events;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opc.sdk.core.node.Node;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.filesystem.IFileSystem;

public class OPCUADPWriterParameter {
	public static final String PARAMETER_ID = "opcuadpwriter";
	/** filesystem */
	private IFileSystem filesystem = null;
	/** datapoint mapping from file */
	private Map<NodeId, Object> datapoints = new HashMap<>();
	/** path of datapoints file */
	private String path = "";
	private Node[] nodes;
	private String directory;
	private String filename;

	public OPCUADPWriterParameter(String path, String filename) {
		this.directory = path;
		this.filename = filename;
		this.path = Paths.get(path).toString() + "/" + this.filename;
	}

	public Map<NodeId, Object> getDatapointList() {
		return this.datapoints;
	}

	public String getDirectory() {
		return this.directory;
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

	/**
	 * Full datapoint list
	 * 
	 * @param datapoints
	 */
	public void setDatapointList(Map<NodeId, Object> datapoints) {
		this.datapoints = datapoints;
	}

	/**
	 * Nodes for devices
	 * 
	 * @param nodes
	 */
	public void setNodes(Map<Integer, List<Node>> nodes) {
		List<Node> nodeList = new ArrayList<Node>();
		for (List<Node> ns : nodes.values()) {
			nodeList.addAll(ns);
		}
		this.nodes = nodeList.toArray(new Node[0]);
	}

	public Node[] getNodes() {
		return this.nodes;
	}

}
