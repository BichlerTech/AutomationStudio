package com.bichler.opcua.statemachine.reverse.engineering;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opcua.statemachine.reverse.IdGenerator;
import com.bichler.opcua.statemachine.reverse.model.IEdgeNotation;

public interface IUMLTranslation {

	public String getId();

	public String getModelname();
	
	public String getPackageName();
	
	public NodeId getNodeId();

	IUMLTranslation getParent();

	IEdgeNotation createEdge(IdGenerator idGenerator);
	
	public String getEdgeElementId();

	public void setEdgeElementId(String edgeId);

	public String getNotationId();

	public void setNotationId(String id);
	
	public void setPackageName(String packageName);

	/**
	 * Translates to UML model
	 * 
	 * @param out
	 * @param idGenerator
	 * @throws IOException
	 */
	public void translate(BufferedWriter out, IdGenerator idGenerator) throws IOException;

	public IEdgeNotation notate(BufferedWriter out, IdGenerator idGenerator) throws IOException;

	
}
