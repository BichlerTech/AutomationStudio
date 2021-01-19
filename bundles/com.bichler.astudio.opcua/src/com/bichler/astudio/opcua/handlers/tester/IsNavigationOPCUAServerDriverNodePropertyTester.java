package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;

public class IsNavigationOPCUAServerDriverNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAServerDriverNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerDriverModelNode) {
			return true;
		}

		return false;
	}

}
