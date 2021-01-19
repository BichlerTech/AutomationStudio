package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.security.OPCUACertificateModelNode;
import com.bichler.astudio.opcua.nodes.security.OPCUAKeyPairModelNode;

public class IsNavigationOPCUACertificateNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUACertificateNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		// PROPERTY.equals(property)
		if (receiver != null
				&& (receiver.getClass() == OPCUACertificateModelNode.class /*
																			 * || receiver . getClass () ==
																			 * OPCUACertificateModelNode . class
																			 */)) {
			return true;
		}

		return false;
	}

}
