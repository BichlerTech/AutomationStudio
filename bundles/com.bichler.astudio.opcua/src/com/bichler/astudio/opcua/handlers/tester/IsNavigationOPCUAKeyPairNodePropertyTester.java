package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.security.OPCUAKeyPairModelNode;

public class IsNavigationOPCUAKeyPairNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAKeyPairNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		// PROPERTY.equals(property)
		if (receiver != null && (receiver.getClass() == OPCUAKeyPairModelNode.class /*
																					 * || receiver . getClass () ==
																					 * OPCUACertificateModelNode . class
																					 */)) {
			return true;
		}

		return false;
	}

}
