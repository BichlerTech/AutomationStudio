package com.bichler.astudio.opcua.navigation.tester;

import org.eclipse.core.expressions.PropertyTester;

public class IsDriverNodePropertyTester extends PropertyTester {

	public static final String PROPERTY_ISDRIVERNODE = "isdrivernode";

	public IsDriverNodePropertyTester() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		if (PROPERTY_ISDRIVERNODE.equals(property)) {
			return true;
		}

		return false;
	}

}
