package com.bichler.astudio.device.opcua.tester;

import org.eclipse.core.expressions.PropertyTester;

public class TestCenterPropertyTester extends PropertyTester {

	public TestCenterPropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
	  System.out.println("No license for Testcenter plugin found!");
	  return true;
	}

}
