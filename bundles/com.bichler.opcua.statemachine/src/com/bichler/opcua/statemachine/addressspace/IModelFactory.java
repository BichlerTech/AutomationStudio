package com.bichler.opcua.statemachine.addressspace;

import java.io.BufferedWriter;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.Node;

public interface IModelFactory {

	boolean export(BufferedWriter out, StatemachineNodesetImporter importer, List<Node> nodes, NamespaceTable namespaces,
			Map<String, List<String>> nsRequiredTable);

}
