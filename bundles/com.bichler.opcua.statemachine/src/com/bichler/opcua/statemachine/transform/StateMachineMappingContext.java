package com.bichler.opcua.statemachine.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Vertex;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.utils.BijectionMap;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.Node;

public class StateMachineMappingContext {

	private BijectionMap<ExpandedNodeId, Vertex> states = new BijectionMap<>();
	private BijectionMap<ExpandedNodeId, Transition> transitions = new BijectionMap<>();
	private BijectionMap<ExpandedNodeId, CallEvent> callEvents = new BijectionMap<>();
	private BijectionMap<ExpandedNodeId, Classifier> effectEvents = new BijectionMap<>();

	private Map<Transition, List<Trigger>> triggers = new HashMap<>();
	private Map<Transition, Classifier> effects = new HashMap<>();

	public StateMachineMappingContext() {
	}

	public void mapNamedElement(ExpandedNodeId nodeId, NamedElement element) {
		if (element instanceof Pseudostate) {
			this.states.map(nodeId, (Vertex) element);
		} else if (element instanceof State) {
			this.states.map(nodeId, (Vertex) element);
		} else if (element instanceof Transition) {
			this.transitions.map(nodeId, (Transition) element);
		} else if (element instanceof CallEvent) {
			this.callEvents.map(nodeId, (CallEvent) element);
		} else if (element instanceof Classifier) {
			this.effectEvents.map(nodeId, (Classifier) element);
		}
	}

	public void createTransitionReferences(StatemachineNodesetImporter importer, List<Node> nodes, Node typeDef) {
		Map<ExpandedNodeId, Node> nodeIdsToNode = new HashMap<>();
		for (Node node : nodes) {
			nodeIdsToNode.put(importer.getNamespaceTable().toExpandedNodeId(node.getNodeId()), node);
		}

		for (Entry<ExpandedNodeId, Transition> entry : this.transitions.getEntries()) {
			// transition
			ExpandedNodeId transitionId = entry.getKey();
			Transition transition = entry.getValue();
			// source
			Vertex sourceState = transition.getSource();
			ExpandedNodeId sourceId = this.states.getLeft(sourceState);
			// target
			Vertex targetState = transition.getTarget();
			ExpandedNodeId targetId = this.states.getLeft(targetState);
			// OPC UA nodes
			Node transitionNode = nodeIdsToNode.get(transitionId);
			Node sourceNode = nodeIdsToNode.get(sourceId);
			Node targetNode = nodeIdsToNode.get(targetId);
			// transition -> source
			ReferenceNode fromState = new ReferenceNode();
			fromState.setIsInverse(false);
			fromState.setReferenceTypeId(Identifiers.FromState);
			fromState.setTargetId(sourceId);
			addReferenceToNode(transitionNode, fromState);
			// source -> transition
			ReferenceNode fromStateInverse = new ReferenceNode();
			fromStateInverse.setIsInverse(true);
			fromStateInverse.setReferenceTypeId(Identifiers.FromState);
			fromStateInverse.setTargetId(transitionId);
			addReferenceToNode(sourceNode, fromStateInverse);
			// transition -> target
			ReferenceNode toState = new ReferenceNode();
			toState.setIsInverse(false);
			toState.setReferenceTypeId(Identifiers.ToState);
			toState.setTargetId(targetId);
			addReferenceToNode(transitionNode, toState);
			// target -> transition
			ReferenceNode toStateInverse = new ReferenceNode();
			toStateInverse.setIsInverse(true);
			toStateInverse.setReferenceTypeId(Identifiers.ToState);
			toStateInverse.setTargetId(transitionId);
			addReferenceToNode(targetNode, toStateInverse);
			// callevents and triggers
			List<Trigger> trigger = this.triggers.get(transition);
			if (trigger != null) {
				// has cause reference
				for (Trigger call : trigger) {
					if (!(call.getEvent() instanceof CallEvent)) {
						continue;
					}

					ExpandedNodeId methodId = this.callEvents.getLeft((CallEvent) call.getEvent());
					// skip not defined call event for trigger
					if (ExpandedNodeId.isNull(methodId)) {
						continue;
					}

					Node methodNode = nodeIdsToNode.get(methodId);

					ReferenceNode hasCause = new ReferenceNode();
					hasCause.setIsInverse(false);
					hasCause.setReferenceTypeId(Identifiers.HasCause);
					hasCause.setTargetId(methodId);
					addReferenceToNode(transitionNode, hasCause);

					ReferenceNode hasCauseInverse = new ReferenceNode();
					hasCauseInverse.setIsInverse(true);
					hasCauseInverse.setReferenceTypeId(Identifiers.HasCause);
					hasCauseInverse.setTargetId(transitionId);
					addReferenceToNode(methodNode, hasCauseInverse);
				}
			}

			// has effect reference
			Classifier effect = this.effects.get(transition);
			if (effect != null) {
				ExpandedNodeId effectEventId = this.effectEvents.getLeft(effect);
				Node eventNode = nodeIdsToNode.get(effectEventId);
				// transition -> event
				ReferenceNode hasEffect = new ReferenceNode();
				hasEffect.setIsInverse(false);
				hasEffect.setReferenceTypeId(Identifiers.HasEffect);
				hasEffect.setTargetId(effectEventId);
				addReferenceToNode(transitionNode, hasEffect);
				// event -> transition
				ReferenceNode hasEffectInverse = new ReferenceNode();
				hasEffectInverse.setIsInverse(true);
				hasEffectInverse.setReferenceTypeId(Identifiers.HasEffect);
				hasEffectInverse.setTargetId(transitionId);
				addReferenceToNode(eventNode, hasEffectInverse);

				// statemachine type -> event
				ReferenceNode generatesEvent = new ReferenceNode();
				generatesEvent.setIsInverse(false);
				generatesEvent.setReferenceTypeId(Identifiers.GeneratesEvent);
				generatesEvent.setTargetId(effectEventId);
				addReferenceToNode(typeDef, generatesEvent);
				// event -> statemachine type
				ReferenceNode generatesEventInverse = new ReferenceNode();
				generatesEventInverse.setIsInverse(true);
				generatesEventInverse.setReferenceTypeId(Identifiers.GeneratesEvent);
				generatesEventInverse.setTargetId(importer.getNamespaceTable().toExpandedNodeId(typeDef.getNodeId()));
				addReferenceToNode(eventNode, generatesEventInverse);
			}
		}
	}

	public void setTriggerMappings(Map<Transition, List<Trigger>> triggers) {
		this.triggers = triggers;
	}

	public void setEffectMappings(Map<Transition, Classifier> effects) {
		this.effects = effects;
	}
	
	private void addReferenceToNode(Node node, ReferenceNode reference) {
		ReferenceNode[] references = node.getReferences();
		if (references == null) {
			references = new ReferenceNode[0];
		}

		List<ReferenceNode> referenceNodes = new ArrayList<>();
		for (ReferenceNode refNode : references) {
			referenceNodes.add(refNode);
		}

		boolean contains = false;
		for (ReferenceNode refNode : referenceNodes) {
			if (refNode.getIsInverse() == reference.getIsInverse()
					&& NodeId.equals(refNode.getReferenceTypeId(), reference.getReferenceTypeId())
					&& refNode.getTargetId().equals(reference.getTargetId())) {
				contains = true;
				break;
			}
		}

		if (!contains) {
			referenceNodes.add(reference);
		}
		node.setReferences(referenceNodes.toArray(new ReferenceNode[0]));
	}
}
