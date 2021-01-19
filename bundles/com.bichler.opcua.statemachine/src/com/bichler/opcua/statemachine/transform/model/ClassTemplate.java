package com.bichler.opcua.statemachine.transform.model;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectTypeNode;

public abstract class ClassTemplate {

	// UML definitions
	private Classifier classifier = null;
	// OpcUa nodeid
	private NodeId nodeId = NodeId.NULL;

	Classifier supertype;

	public ClassTemplate(Classifier classifier, NodeId nodeId) {
		this.classifier = classifier;
		this.nodeId = nodeId;

		initialize(classifier.getAttributes(), classifier.getOperations(), classifier.getAppliedStereotypes());
	}

	Node findNode(StatemachineNodesetImporter importer, Classifier classifier, List<Node> createdNodes,
			List<Node> temporary) {

		return findNode(importer, classifier.getName(), createdNodes, temporary);
	}

	Node findNode(StatemachineNodesetImporter importer, String name, List<Node> createdNodes, List<Node> temporary) {
		if (name == null) {
			return null;
		}
		// find node in OpcUa model
		Node nodeSupertype = importer.getNodeByBrowsename(name);
		// find node in already created nodes
		if (nodeSupertype == null) {
			for (Node node : createdNodes) {
				String browsename = node.getBrowseName().getName();
				if (browsename.equals(name)) {
					nodeSupertype = node;
					break;
				}
			}
		}
		// find node in temporary nodes
		if (nodeSupertype == null) {
			for (Node node : temporary) {
				String browsename = node.getBrowseName().getName();
				if (browsename.equals(name)) {
					nodeSupertype = node;
					break;
				}
			}
		}
		return nodeSupertype;
	}

	Classifier getClassifier() {
		return this.classifier;
	}

	NodeId getNodeId() {
		return this.nodeId;
	}

	Classifier generalToSupertype() {
		Classifier classifier = getClassifier();
		EList<Classifier> generals = classifier.getGenerals();
		if (generals == null) {
			return null;
		}
		if (generals.isEmpty()) {
			return null;
		}
		Classifier general = generals.get(0);
		return general;
	}

	Node createClassType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			List<Node> createdNodes, List<Node> temporary) {

		// type already defined with the statemachine type
		if (!NodeId.isNull(getNodeId())) {
			for (Node node : createdNodes) {
				if (node.getNodeId().equals(getNodeId())) {
					return null;
				}
			}
		}
		//
		Node found = findNode(importer, getClassifier(), createdNodes, temporary);
		if (found != null) {
			return null;
		}

		// create node
		Node parent = findParentType(transformer, importer, this.supertype, createdNodes, temporary);
		Node type = createType(transformer, importer, getClassifier(), parent.getNodeId());
		temporary.add(type);
		return type;
	}

	Node findParentType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Classifier classifier, List<Node> createdNodes, List<Node> temporary) {
		// find already existing node
		Node nodeSupertype = findNode(importer, classifier, createdNodes, temporary);
		// create new node
		if (nodeSupertype == null) {
			if (classifier.getGenerals().isEmpty()) {
//				Resource resource = classifier.get
//				classifier.eAdapters()
//				ClassImpl@15ef38a5
				String fragment = ((MinimalEObjectImpl)classifier).eProxyURI().fragment();
				Classifier class2obj = null;
				for (Map<String, Classifier> value : importer.getTypeClassIdMapping().values()) {					
					class2obj = value.get(fragment);
					if(class2obj != null) {
						break;
					}
				}

				if(class2obj != null) {
					nodeSupertype = findNode(importer, class2obj, createdNodes, temporary);
				}
			} else {
				Classifier general = classifier.getGenerals().get(0);
				Node parent = findParentType(transformer, importer, general, createdNodes, temporary);

				nodeSupertype = createType(transformer, importer, classifier, parent.getNodeId());
				temporary.add(nodeSupertype);
			}
		}

		return nodeSupertype;
	}

//	public void setSupertype(Classifier supertype) {
//		this.supertype = supertype;
//	}

	abstract Node createType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Classifier classifier, NodeId nodeId);

	public abstract List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix, Classifier supertype, Node parentObjType,
			Map<NodeId, NodeId> mapping);

	public abstract List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> statemachineNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix);

	public abstract void toOpcUaType(AbstractStateMachineToOpcTransformer abstractStateMachineToOpcTransformer,
			StatemachineNodesetImporter importer, List<Node> createdNodes, List<Node> temporary);

	protected abstract void initialize(EList<Property> attributes, EList<Operation> operations,
			EList<Stereotype> stereotypes);

	protected abstract Classifier getSupertype();
}
