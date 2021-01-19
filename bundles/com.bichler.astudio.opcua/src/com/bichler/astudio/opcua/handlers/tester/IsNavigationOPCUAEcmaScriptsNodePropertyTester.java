package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaIntervalScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaSingleScriptsModelNode;

public class IsNavigationOPCUAEcmaScriptsNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAEcmaScriptsNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerEcmaSingleScriptsModelNode
				|| receiver instanceof OPCUAServerEcmaIntervalScriptsModelNode) {
			return true;
		}

		return false;
	}

}
