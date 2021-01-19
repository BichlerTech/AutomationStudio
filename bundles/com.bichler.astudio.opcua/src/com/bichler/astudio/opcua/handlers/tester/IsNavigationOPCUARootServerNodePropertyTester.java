package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;

public class IsNavigationOPCUARootServerNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUARootServerNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver != null && receiver instanceof OPCUARootModelNode) {
			return true;
		}

		return false;
	}

}
