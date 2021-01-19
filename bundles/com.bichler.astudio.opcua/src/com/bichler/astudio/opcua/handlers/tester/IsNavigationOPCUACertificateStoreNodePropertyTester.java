package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.security.AbstractOPCUACertificateStoreModelNode;
import com.bichler.astudio.opcua.nodes.security.AbstractOPCUAServerCertificateStoreModelNode;
import com.bichler.astudio.opcua.nodes.security.OPCUAKeyPairModelNode;

public class IsNavigationOPCUACertificateStoreNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUACertificateStoreNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		// PROPERTY.equals(property)
		if (receiver != null && (receiver instanceof AbstractOPCUACertificateStoreModelNode)) {
			return true;
		}

		return false;
	}

}
