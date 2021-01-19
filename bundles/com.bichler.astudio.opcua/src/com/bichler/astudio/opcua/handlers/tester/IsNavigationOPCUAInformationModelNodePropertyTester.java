package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelNode;

public class IsNavigationOPCUAInformationModelNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAInformationModelNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		if (receiver.getClass() == OPCUAServerInfoModelNode.class) {
			return true;
		}

		return false;
	}

}
