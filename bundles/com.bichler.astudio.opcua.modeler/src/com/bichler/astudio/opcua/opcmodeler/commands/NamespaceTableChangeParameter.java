package com.bichler.astudio.opcua.opcmodeler.commands;

import java.util.Map;

import org.opcfoundation.ua.common.NamespaceTable;

public class NamespaceTableChangeParameter {
	public static final String PARAMETER_ID = "opcuaupdatenamespacetable";
	private Map<Integer, Integer> nsIndexMapping;
	private Map<String, String> nsMapping;
	/** original table before changing */
	private NamespaceTable originTable;
	/** namespace table after changing */
	private NamespaceTable namespaceTable2change;

	public NamespaceTableChangeParameter() {
	}

	public void setIndexMapping(Map<Integer, Integer> mapping) {
		this.nsIndexMapping = mapping;
	}

	public Map<Integer, Integer> getIndexMapping() {
		return this.nsIndexMapping;
	}

	public void setMapping(Map<String, String> mapping) {
		this.nsMapping = mapping;
	}

	public Map<String, String> getMapping() {
		return this.nsMapping;
	}

	public NamespaceTable getOriginNamespaceTable() {
		return originTable;
	}

	public void setOriginTable(NamespaceTable originTable) {
		this.originTable = originTable;
	}

	public NamespaceTable getNamespaceTable2change() {
		return namespaceTable2change;
	}

	public void setNamespaceTable2change(NamespaceTable namespaceTable2change) {
		this.namespaceTable2change = namespaceTable2change;
	}
}
