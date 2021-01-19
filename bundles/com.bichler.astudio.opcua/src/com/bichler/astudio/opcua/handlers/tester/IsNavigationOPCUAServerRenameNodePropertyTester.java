package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;

public class IsNavigationOPCUAServerRenameNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAServerRenameNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		// PROPERTY.equals(property)
		if (receiver != null && receiver.getClass() == OPCUAServerDriverModelNode.class) {
			return true;
		}

		return false;
	}

}
