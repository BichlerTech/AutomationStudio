package com.bichler.opcua.statemachine.reverse.model;

import java.io.BufferedWriter;
import java.io.IOException;

import com.bichler.opcua.statemachine.reverse.IdGenerator;
import com.bichler.opcua.statemachine.reverse.engineering.IUMLTranslation;

public interface IEdgeNotation {

	String getId();

	IUMLTranslation getSource();

	IUMLTranslation getTarget();

	public void notate(BufferedWriter out, IdGenerator idGenerator, String modelName) throws IOException;
	public void notate(BufferedWriter out, IdGenerator idGenerator, String modelName, String edgeId) throws IOException;

	boolean isLocalTarget();
}
