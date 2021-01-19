package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class IsNavigationOPCUAServerNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAServerNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerModelNode) {
			return true;
		}

		return false;
	}

}
