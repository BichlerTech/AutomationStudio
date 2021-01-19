package com.bichler.astudio.opcua.widget.model;

import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.helpers.DefaultHandler;

public abstract class AbstractAdvanedParser extends DefaultHandler {

	private NamespaceTable namespaceTable = null;
	private AdvancedSectionType type;

	public AbstractAdvanedParser(NamespaceTable namespaceTable, AdvancedSectionType type) {
		this.namespaceTable = namespaceTable;
		this.type = type;
	}

	public NamespaceTable getNamespaceTable() {
		return this.namespaceTable;
	}

	public AdvancedSectionType getType() {
		return this.type;
	}

	public abstract AdvancedRootConfigurationNode getRoot();

	public abstract void setRoot(AdvancedRootConfigurationNode root);
}
