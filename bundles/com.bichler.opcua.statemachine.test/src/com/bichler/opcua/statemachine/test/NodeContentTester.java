package com.bichler.opcua.statemachine.test;

import java.util.Arrays;

import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

public class NodeContentTester {

	/**
	 * Compare OPC UA nodes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if the nodes have the same attributes otherwise false.
	 */
	public static boolean equals(Node n1, Node n2) {
		if (n1.getNodeClass() != n2.getNodeClass()) {
			return false;
		}

		if (!equalsBasicNode(n1, n2)) {
			return false;
		}

		NodeClass nodeClass = n1.getNodeClass();
		switch (nodeClass) {
		case Object:
			return equalsObject((ObjectNode) n1, (ObjectNode) n2);
		case ObjectType:
			return equalsObjectType((ObjectTypeNode) n1, (ObjectTypeNode) n2);
		case Variable:
			return equalsVariable((VariableNode) n1, (VariableNode) n2);
		case VariableType:
			return equalsVariableType((VariableTypeNode) n1, (VariableTypeNode) n2);
		case DataType:
			return equalsDataType((DataTypeNode) n1, (DataTypeNode) n2);
		case Method:
			return equalsMethod((MethodNode) n1, (MethodNode) n2);
		case ReferenceType:
			return equalsReferenceType((ReferenceTypeNode) n1, (ReferenceTypeNode) n2);
		case View:
		case Unspecified:
		default:
			return false;
		}
	}

	/**
	 * Compare OPC UA basic node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if basic node attributes are the same, otherwise false.
	 */
	private static boolean equalsBasicNode(Node n1, Node n2) {
		// browse name
		if (!n1.getBrowseName().getName().equals(n2.getBrowseName().getName())) {
			return false;
		}
		// description
		if (!n1.getDescription().equals(n2.getDescription())) {
			return false;
		}
		// display name
		if (!n1.getDisplayName().equals(n2.getDisplayName())) {
			return false;
		}
		// nodeid
		if (!n1.getNodeId().equals(n2.getNodeId())) {
			return false;
		}
		// writemask
		if (!n1.getWriteMask().equals(n2.getWriteMask())) {
			return false;
		}
		// user writemask
		if (!n1.getUserWriteMask().equals(n2.getUserWriteMask())) {
			return false;
		}
		if (!equalsReferenceNodes(n1.getReferences(), n2.getReferences())) {
			return false;
		}

		return true;
	}

	/**
	 * Compare OPC UA datatype node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if datatype node attributes are the same, otherwise false.
	 */
	private static boolean equalsDataType(DataTypeNode n1, DataTypeNode n2) {
		if (!n1.getIsAbstract().equals(n2.getIsAbstract())) {
			return false;
		}
		return true;
	}

	/**
	 * Compare OPC UA method node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if datatype node attributes are the same, otherwise false.
	 */
	private static boolean equalsMethod(MethodNode n1, MethodNode n2) {
		if (!n1.getExecutable().equals(n2.getExecutable())) {
			return false;
		}
		if (!n1.getUserExecutable().equals(n2.getUserExecutable())) {
			return false;
		}
		return true;
	}

	/**
	 * Compare OPC UA referencetype node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if referencetype node attributes are the same, otherwise false.
	 */
	private static boolean equalsReferenceType(ReferenceTypeNode n1, ReferenceTypeNode n2) {
		if (!n1.getInverseName().equals(n2.getInverseName())) {
			return false;
		}
		if (!n1.getIsAbstract().equals(n2.getIsAbstract())) {
			return false;
		}
		if (!n1.getSymmetric().equals(n2.getSymmetric())) {
			return false;
		}
		return true;
	}

	/**
	 * Compare OPC UA object node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if object node attributes are the same, otherwise false.
	 */
	private static boolean equalsObject(ObjectNode n1, ObjectNode n2) {
		if (!n1.getEventNotifier().equals(n2.getEventNotifier())) {
			return false;
		}
		return true;
	}

	/**
	 * Compare OPC UA objecttype node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if objecttype node attributes are the same, otherwise false.
	 */
	private static boolean equalsObjectType(ObjectTypeNode n1, ObjectTypeNode n2) {
		if (!n1.getIsAbstract().equals(n2.getIsAbstract())) {
			return false;
		}
		return true;
	}

	/**
	 * Compare references of an OPC UA node.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if references are the same, othewise false.
	 */
	private static boolean equalsReferenceNodes(ReferenceNode[] ref1, ReferenceNode[] ref2) {
		for (ReferenceNode ref : ref1) {
			boolean found = false;

			for (ReferenceNode test : ref2) {
				if (ref.equals(test)) {
					found = true;
					break;
				}
			}

			if (!found) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Compare OPC UA variable node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if variable node attributes are the same, otherwise false.
	 */
	private static boolean equalsVariable(VariableNode n1, VariableNode n2) {
		if (n1.getArrayDimensions() == null && n2.getArrayDimensions() == null) {
			// ok
		} else if (n1.getArrayDimensions() == null && n2.getArrayDimensions() != null) {
			return false;
		} else if (!Arrays.equals(n1.getArrayDimensions(), n2.getArrayDimensions())) {
			return false;
		}		
		if (!n1.getAccessLevel().equals(n2.getAccessLevel())) {
			return false;
		}
		if (!n1.getDataType().equals(n2.getDataType())) {
			return false;
		}
		if (!n1.getHistorizing().equals(n2.getHistorizing())) {
			return false;
		}
		if (!n1.getMinimumSamplingInterval().equals(n2.getMinimumSamplingInterval())) {
			return false;
		}
		if (!n1.getUserAccessLevel().equals(n2.getUserAccessLevel())) {
			return false;
		}
		if (!n1.getValue().equals(n2.getValue())) {
			return false;
		}
		if (!n1.getValueRank().equals(n2.getValueRank())) {
			return false;
		}
		return true;
	}

	/**
	 * Compare OPC UA variabletype node attributes.
	 * 
	 * @param n1 Correct node to test with
	 * @param n2 Generated node to test
	 * 
	 * @return true if variabletype node attributes are the same, otherwise false.
	 */
	private static boolean equalsVariableType(VariableTypeNode n1, VariableTypeNode n2) {
		if (!n1.getArrayDimensions().equals(n2.getArrayDimensions())) {
			return false;
		}
		if (!n1.getDataType().equals(n2.getDataType())) {
			return false;
		}
		if (!n1.getValue().equals(n2.getValue())) {
			return false;
		}
		if (!n1.getValueRank().equals(n2.getValueRank())) {
			return false;
		}
		if (!n1.getIsAbstract().equals(n2.getIsAbstract())) {
			return false;
		}
		return true;
	}

}
