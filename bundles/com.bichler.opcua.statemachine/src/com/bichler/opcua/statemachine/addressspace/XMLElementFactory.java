package com.bichler.opcua.statemachine.addressspace;

import com.bichler.opcua.statemachine.addressspace.elements.BaseXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UADataTypeXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAMethodXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAObjectTypeXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAObjectXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAReferenceTypeXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAVariableTypeXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAVariableXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAViewXMLGen;

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
