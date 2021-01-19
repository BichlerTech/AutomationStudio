package com.bichler.astudio.opcua.addressspace.xml.exporter.factory;

import java.io.BufferedWriter;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

import opc.sdk.core.node.Node;

public interface IModelFactory {

	boolean export(BufferedWriter out, List<Node> nodes, NamespaceTable namespaces,
			Map<String, List<String>> nsRequiredTable, IServerTypeModel typeModel);

}
