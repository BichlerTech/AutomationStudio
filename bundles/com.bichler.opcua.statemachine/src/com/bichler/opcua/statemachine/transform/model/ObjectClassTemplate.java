package com.bichler.opcua.statemachine.transform.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.Node;

public class ObjectClassTemplate extends ClassTemplate {

	public ObjectClassTemplate(Classifier classifier, NodeId nodeId) {
		super(classifier, nodeId);
	}

	@Override
	protected void initialize(EList<Property> attributes, EList<Operation> operations, EList<Stereotype> stereotypes) {

	}
	
	@Override
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix, Classifier supertype, Node parentObjType,
			Map<NodeId, NodeId> mapping) {

		return null;
	}

	@Override
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> statemachineNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix) {

		List<Node> nodes = new ArrayList<Node>();
		return nodes;
	}

	@Override
	public void toOpcUaType(AbstractStateMachineToOpcTransformer abstractStateMachineToOpcTransformer,
			StatemachineNodesetImporter importer, List<Node> createdNodes, List<Node> temporary) {

	}

	@Override
	protected Classifier getSupertype() {
		return null;
	}

	@Override
	Node createType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Classifier classifier, NodeId nodeId) {
	
		return null;
	}

}
