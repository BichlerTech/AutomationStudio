package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;

public class IsNavigationOPCUADriverNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUADriverNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerDriversModelNode) {
			return true;
		}

		return false;
	}

}
