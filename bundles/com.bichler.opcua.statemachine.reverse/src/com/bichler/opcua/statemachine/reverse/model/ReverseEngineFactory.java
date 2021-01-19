package com.bichler.opcua.statemachine.reverse.model;

import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.reverse.engineering.IUMLTranslation;

import opc.sdk.core.node.Node;

public class ReverseEngineFactory {

	public IUMLTranslation createNodeTranslation(String modelname, Node node, String umlId, IUMLTranslation parent)
			throws StatemachineException {
		AbstractUMLTranslation model = null;

		switch (node.getNodeClass()) {
		case ObjectType:
			model = new ObjectTypeTranslation(modelname, node.getNodeId(), umlId, parent);
			model.setName(node.getBrowseName().getName());
			break;
		case DataType:
			model = new DataTypeTranslation(modelname, node.getNodeId(), umlId, parent);
			model.setName(node.getBrowseName().getName());
			break;
		default:
			throw new StatemachineException("OPC UA node " + node.getNodeClass() + " " + node.getBrowseName().getName()
					+ "cannot reverse translated!");
		}

//		NodeId interfaceType = new NodeId(0, 17602);
//		NodeId interfaceTypeFolder = new NodeId(0, 17708);
		
//		NodeId nodeId = node.getNodeId();
//		if(nodeId.equals(interfaceType)) {
//			System.out.println("");
//		}
//		else if(nodeId.equals(interfaceTypeFolder)) {
//			System.out.println("");
//		}
		
		return model;
	}
}
