package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerConfigModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;

public class IsNavigationOPCUAServerConfigNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAServerConfigNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerConfigModelNode) {
			return true;
		}

		return false;
	}

}