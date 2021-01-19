package com.bichler.opcua.statemachine.test.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.test.Activator;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;
import com.bichler.opcua.statemachine.transform.PluginStateMachineToOpcTransformer;

class PluginStatemachineTransformationResultTester {

	@Before
	public void setup() {

	}

	@After
	public void releaseResources() {

	}

	@Test
	void testStateMachineType() {
		File folderTestcases = Activator.getDefault().getFolderTestcaseTransformation();
		File[] testcases = folderTestcases.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File file, String name) {
				if (name.startsWith(".") || name.endsWith("_")) {
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
		String testcasename = testcase.getName().substring(testcase.getName().lastIndexOf("."));

		File[] model = testcase.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(testcasename + ".uml");
			}
		});

		File result = new File(testcase, "result.xml");
		if (result.exists()) {
			result.delete();
		}

		System.out.println("TestNr: " + testcasename + "");

		try {
			result.createNewFile();

			File resourceFile = model[0];
			File output = new File(result.getPath());
			AbstractStateMachineToOpcTransformer transformer = new PluginStateMachineToOpcTransformer(true);
			transformer.transform(resourceFile, output);
		} catch (StatemachineException | IOException e) {
			result.delete();
			Logger.getLogger(getClass().getName()).log(Level.INFO, testcasename + " result " + e.getMessage());
		}
	}

}
