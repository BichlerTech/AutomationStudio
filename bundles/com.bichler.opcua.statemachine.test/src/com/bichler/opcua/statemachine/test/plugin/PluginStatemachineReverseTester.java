package com.bichler.opcua.statemachine.test.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opcua.statemachine.BaseStatemachineActivator;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.reverse.engineering.IReverseTransformationContext;
import com.bichler.opcua.statemachine.reverse.engineering.PluginReverseStatemachineEngine;
import com.bichler.opcua.statemachine.reverse.engineering.AbstractInternalReverseEngine;
import com.bichler.opcua.statemachine.test.Activator;

class PluginStatemachineReverseTester {

	@Before
	public void setup() {

	}

	@After
	public void releaseResources() {

	}

	@Test
	void testStateMachineType() {	
//		fail("Not yet implemented 8");
		File folderTestcases = Activator.getDefault().getFolderTestcaseReverse();
		File[] testcases = folderTestcases.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File file, String name) {
				if (name.startsWith(".") || name.endsWith(".xml")) {
					return false;
				}
				return true;
			}
		});

		for (int i = 0; i < testcases.length; i++) {
			File testcase = testcases[i];
			startTestCase(testcase);
		}
	}

	private void startTestCase(File testcase) {
		String testcasename = testcase.getName().substring(testcase.getName().lastIndexOf(".")+1);

		// OPC UA Base UML model
		File[] sourceUMLFiles = BaseStatemachineActivator.getDefault().getUMLResourceDefaultOpcUaClassFiles();
		if (sourceUMLFiles == null) {
			throw new IllegalArgumentException("No UML file with a default OPC UA types model");
		}
		// Create reverse enginee
		AbstractInternalReverseEngine engine;
		try {
			engine = new PluginReverseStatemachineEngine();
			// set static uml ids mapping
			Map<String, Map<NodeId, String>> mapping = engine.fetchExternalIdsFromUML(sourceUMLFiles);
			engine.addExternalIdSet(mapping);
			engine.loadUMLResources(sourceUMLFiles);

			IReverseTransformationContext context = engine.createTransformationContext(testcase.getAbsolutePath());
			File[] nodesetFiles = testcase.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".xml");
				}
			});
			System.out.println("TestNr: " + testcasename + "");			
			// import OPC UA nodeset
			engine.importOpcUaNodeset(nodesetFiles);
			// create namespace table from imported nodeset models
			String[] namespacesToExport = engine.generateNamespacesToExport();
			// transform
			engine.transformTypesModel(context, testcasename, namespacesToExport);
			// add base model for OPC UA
			engine.createBaseModel(context);
		} catch (StatemachineException e) {
			e.printStackTrace();
		}
	}

}
