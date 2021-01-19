package com.bichler.opcua.statemachine.test.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatemachineTransformationTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		Path transformationDirectory = Paths.get("testcaseUML","transformation");
		
		File resource = transformationDirectory.toFile();
		boolean exist = resource.exists();
		System.out.println();
		
		
		fail("Not yet implemented");
	}

}
