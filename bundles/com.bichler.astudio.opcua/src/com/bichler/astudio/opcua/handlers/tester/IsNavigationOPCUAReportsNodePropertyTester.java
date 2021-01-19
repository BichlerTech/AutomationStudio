package com.bichler.astudio.opcua.handlers.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.bichler.astudio.opcua.nodes.OPCUAReportModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaScriptModelNode;

public class IsNavigationOPCUAReportsNodePropertyTester extends PropertyTester {
	public IsNavigationOPCUAReportsNodePropertyTester() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof OPCUAReportModelNode) {
			return true;
		}
		return false;
	}
}
