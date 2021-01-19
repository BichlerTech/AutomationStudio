package com.bichler.astudio.opcua.addressspace.xml.exporter.factory;

import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.BaseXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UADataTypeXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAMethodXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAObjectTypeXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAObjectXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAReferenceTypeXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAVariableTypeXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAVariableXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAViewXMLGen;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.ViewNode;

public class XMLElementFactory {

	public static BaseXMLGen factor(Node node) {
		BaseXMLGen element = null;
				
		if (node instanceof UAObjectNode) {
			element = new UAObjectXMLGen((UAObjectNode) node);
		} else if (node instanceof UAVariableNode) {
			element = new UAVariableXMLGen((UAVariableNode) node);
		} else if (node instanceof UAObjectTypeNode) {
			element = new UAObjectTypeXMLGen((UAObjectTypeNode) node);
		} else if (node instanceof UAVariableTypeNode) {
			element = new UAVariableTypeXMLGen((UAVariableTypeNode) node);
		} else if (node instanceof UADataTypeNode) {
			element = new UADataTypeXMLGen((UADataTypeNode) node);
		} else if (node instanceof UAMethodNode) {
			element = new UAMethodXMLGen((UAMethodNode) node);
		} else if (node instanceof UAReferenceTypeNode) {
			element = new UAReferenceTypeXMLGen((UAReferenceTypeNode) node);
		} else if (node instanceof ViewNode) {
			element = new UAViewXMLGen((ViewNode) node);
		}

		return element;
	}
}
