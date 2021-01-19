package com.bichler.astudio.opcua.opcmodeler.exporter;

import opc.sdk.core.node.Node;

import org.w3c.dom.Document;

/**
 * Interface of the Exporter.
 * 
 * @author Thomas Zöchbauer
 */
public interface XMLWriter {
	/**
	 * Writes a Node in a XML File.
	 * 
	 * @param Document Document to Write.
	 * @param Node     Node to export.
	 */
	void writeNode(Document document, Node node);

	/**
	 * Write Nodes in a XML File.
	 * 
	 * @param Document Document to Write.
	 * @param Nodes    Nodes to export.
	 */
	void writeNodes(Document document, Node[] nodes);
}
