package com.bichler.astudio.device.opcua.tester;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.expressions.PropertyTester;

public class ScriptSimulationPropertyTester extends PropertyTester {

	public ScriptSimulationPropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
	  Logger.getLogger(getClass().getName()).log(Level.INFO, "No license for Script Simulation (TestCenter Plugin) found!");
	  return true;
	}

}
