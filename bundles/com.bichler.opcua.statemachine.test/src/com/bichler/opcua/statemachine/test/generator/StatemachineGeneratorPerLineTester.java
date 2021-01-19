package com.bichler.opcua.statemachine.test.generator;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

class StatemachineGeneratorPerLineTester {

	// proper working nodeset2.xml for testing
	private BufferedReader inOriginNodeset = null;
	// generated nodeset2.xml to test
	private BufferedReader inGeneratedNodeset = null;

	@Before
	public void setup() throws IOException {
		inOriginNodeset = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("/nodeset/v001/output.xml")));
		inGeneratedNodeset = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/data.txt")));
	}

	@After
	public void releaseResources() throws IOException {

		if (inOriginNodeset != null) {
			inOriginNodeset.close();
			inOriginNodeset = null;
		}
		if (inGeneratedNodeset != null) {
			inGeneratedNodeset.close();
			inGeneratedNodeset = null;
		}
	}

	@Test
	void testStateMachineType() throws IOException {

		String pathSrc = "nodeset/v002/tester.xml";
		String pathTarget = "nodeset/v002/output.xml";

		File fileSrc = new File(pathSrc);
		File fileTarget = new File(pathTarget);

		try {
			inOriginNodeset = new BufferedReader(new InputStreamReader(new FileInputStream(fileSrc)));
			inGeneratedNodeset = new BufferedReader(new InputStreamReader(new FileInputStream(fileTarget)));

			boolean testMode = true;
			int linenumber = 1;
			String lineOrigin = "", lineGenerated = "";

			while (testMode) {
				lineOrigin = inOriginNodeset.readLine();
				lineGenerated = inGeneratedNodeset.readLine();

				// successfull line end
				if (lineOrigin == null && lineGenerated == null) {
					testMode = false;
				} else {
					assertNotSame(lineOrigin, lineGenerated, "Difference at line " + linenumber);
					linenumber++;
				}
			}
		
			assertFalse("Exit test with failure!", testMode);
		} finally {
			if (inOriginNodeset != null) {
				inOriginNodeset.close();
				inOriginNodeset = null;
			}
			if (inGeneratedNodeset != null) {
				inGeneratedNodeset.close();
				inGeneratedNodeset = null;
			}
		}
	}

}
