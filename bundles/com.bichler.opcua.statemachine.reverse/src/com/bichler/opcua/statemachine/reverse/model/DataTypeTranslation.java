package com.bichler.opcua.statemachine.reverse.model;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opcua.statemachine.reverse.engineering.IUMLTranslation;

public class DataTypeTranslation extends AbstractUMLTranslation {

	public DataTypeTranslation(String model, NodeId nodeId, String id, IUMLTranslation parent) {
		super(model, nodeId, id, parent);
	}

	@Override
	public String getShapeType() {
		return "DataType";
	}		
}
