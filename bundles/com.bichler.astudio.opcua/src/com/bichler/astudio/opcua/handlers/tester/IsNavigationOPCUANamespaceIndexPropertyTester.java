package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelsNode;

public class IsNavigationOPCUANamespaceIndexPropertyTester extends PropertyTester {

	public IsNavigationOPCUANamespaceIndexPropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		if (receiver.getClass() == OPCUAServerInfoModelsNode.class) {
			return true;
		}

		return false;
	}

}
