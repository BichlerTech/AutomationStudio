package com.bichler.astudio.device.opcua.tester;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.expressions.PropertyTester;

public class ServerSimulationPropertyTester extends PropertyTester {

	public ServerSimulationPropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
	  Logger.getLogger(getClass().getName()).log(Level.INFO,"No license for server simulation(TestCenter Plugin) found!");
	  return true;
	}

}
