package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPostShellScriptModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPostShellScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPreShellScriptModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerPreShellScriptsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerShellScriptsModelNode;

public class IsNavigationOPCUAShellScriptNodePropertyTester extends PropertyTester {

	public IsNavigationOPCUAShellScriptNodePropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		// PROPERTY.equals(property)

		if (receiver instanceof OPCUAServerPreShellScriptsModelNode
				|| receiver instanceof OPCUAServerPostShellScriptsModelNode) {
			return true;
		}

		return false;
	}

}
