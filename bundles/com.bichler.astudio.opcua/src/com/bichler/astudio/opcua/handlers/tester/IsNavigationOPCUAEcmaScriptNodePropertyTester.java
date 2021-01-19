package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaScriptModelNode;

public class IsNavigationOPCUAEcmaScriptNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAEcmaScriptNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerEcmaScriptModelNode) {
			return true;
		}

		return false;
	}

}
