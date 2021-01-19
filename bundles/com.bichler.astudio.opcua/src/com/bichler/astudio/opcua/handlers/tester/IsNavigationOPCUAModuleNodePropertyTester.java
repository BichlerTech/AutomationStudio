package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerModulesModelNode;

public class IsNavigationOPCUAModuleNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAModuleNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerModulesModelNode) {
			return true;
		}

		return false;
	}

}
