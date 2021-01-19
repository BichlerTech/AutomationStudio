package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.utils.ReferenceRule;

public class ReferenceNodeValidation {
	private static void isMainHierachicalReference(List<ReferenceRule> rules, ReferenceModel model, Node node,
			ReferenceNode element) {
		ReferenceNode[] references = node.getReferences();
		for (ReferenceNode reference : references) {
			ReferenceModel modelType = model.find(ServerInstance.getInstance().getServerInstance().getNamespaceUris(),
					reference.getReferenceTypeId());
			boolean isHierachical = isTypeOf(modelType, Identifiers.HierarchicalReferences);
			/**
			 * is first hierachical inverse
			 */
			if (isHierachical && reference.getIsInverse()) {
				if (reference == element) {
					rules.add(ReferenceRule.Hierachy);
				}
				return;
			}
		}
	}

	private static void isMainTypedefReference(List<ReferenceRule> rules, ReferenceModel model, Node node,
			ReferenceNode element) {
		ReferenceNode[] references = node.getReferences();
		for (ReferenceNode reference : references) {
			ReferenceModel modelType = model.find(ServerInstance.getInstance().getServerInstance().getNamespaceUris(),
					reference.getReferenceTypeId());
			boolean isTypeDef = isTypeOf(modelType, Identifiers.HasTypeDefinition);
			/**
			 * is first hierachical inverse
			 */
			if (isTypeDef && !reference.getIsInverse()) {
				if (reference == element) {
					rules.add(ReferenceRule.Typedef);
				}
				return;
			}
		}
	}

	public static void isNamingRule(List<ReferenceRule> rules, ReferenceModel model, Node node, ReferenceNode element) {
		ReferenceNode[] references = node.getReferences();
		for (ReferenceNode reference : references) {
			ReferenceModel modelType = model.find(ServerInstance.getInstance().getServerInstance().getNamespaceUris(),
					reference.getReferenceTypeId());
			boolean isTypeDef = isTypeOf(modelType, Identifiers.HasModellingRule);
			/**
			 * is first hierachical inverse
			 */
			if (isTypeDef && !reference.getIsInverse()) {
				if (reference == element) {
					rules.add(ReferenceRule.ModellingRule);
				}
				return;
			}
		}
	}

	public static List<ReferenceRule> validate(ReferenceModel model, Node node, ReferenceNode element) {
		List<ReferenceRule> rules = new ArrayList<>();
		if (element != null) {
			isMainHierachicalReference(rules, model, node, element);
			isMainTypedefReference(rules, model, node, element);
			// isMainModelParentRule(rules, model, node, element);
			isNamingRule(rules, model, node, element);
		}
		return rules;
	}

	private static boolean isTypeOf(ReferenceModel model, NodeId type) {
		NodeId nodeId = model.getId(ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		if (type.equals(nodeId)) {
			return true;
		}
		ReferenceModel parent = model.getParent();
		if (parent != null) {
			return isTypeOf(parent, type);
		}
		return false;
	}
}
