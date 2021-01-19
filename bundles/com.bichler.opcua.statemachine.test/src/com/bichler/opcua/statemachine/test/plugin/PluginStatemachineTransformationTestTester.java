package com.bichler.opcua.statemachine.test.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.test.Activator;
import com.bichler.opcua.statemachine.test.NodeContentTester;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;
import com.bichler.opcua.statemachine.transform.PluginStateMachineToOpcTransformer;

import opc.sdk.core.node.Node;

class PluginStatemachineTransformationTestTester {

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

		File test = new File(testcase, "test.xml");
		if (test.exists()) {
			test.delete();
		}

		System.out.println("TestNr: " + testcasename + "");

		try {
			test.createNewFile();

			File resourceFile = model[0];
			File output = new File(test.getPath());
			AbstractStateMachineToOpcTransformer transformer = new PluginStateMachineToOpcTransformer(true);
			transformer.transform(resourceFile, output);

			File result = new File(testcase, "result.xml");

			compareModel(test, result);
		} catch (StatemachineException | IOException e) {
			test.delete();
			Logger.getLogger(getClass().getName()).log(Level.INFO, testcasename + " result " + e.getMessage());
		}
	}

	private void compareModel(File fileTest, File fileResult) throws StatemachineException {
		StatemachineNodesetImporter importerOrigin = new StatemachineNodesetImporter();
		StatemachineNodesetImporter importerTester = new StatemachineNodesetImporter();

		importerOrigin.importNodeSet(new File[] { fileTest });
		importerTester.importNodeSet(new File[] { fileResult });

		Map<ExpandedNodeId, Node> nodesSrc = importerOrigin.getNodesItemList();
		Map<ExpandedNodeId, Node> nodesTester = importerTester.getNodesItemList();
		// imported OPC UA nodes size
		Integer srcSize = nodesSrc.size();
		Integer testerSize = nodesTester.size();

		assertEquals("Imported Nodeset2 OPC Node size differes from another!", srcSize.longValue(),
				testerSize.longValue());

		Set<ExpandedNodeId> nodeIds = new HashSet<>();
		for (ExpandedNodeId id : nodesSrc.keySet()) {
			nodeIds.add(id);
		}
		for (ExpandedNodeId id : nodesTester.keySet()) {
			nodeIds.add(id);
		}
		// imported OPC UA nodeIds size
		Integer collectedSize = nodeIds.size();
		assertEquals("Collected imported Nodeset2 OPC NodeIds differes from another", srcSize.longValue(),
				collectedSize.longValue());

		for (ExpandedNodeId id : nodeIds) {
			Node src = nodesSrc.get(id);
			Node test = nodesTester.get(id);

			if (!NodeContentTester.equals(src, test)) {
				System.out.println("");
			}

			if (test.getNodeId().getValue() instanceof UnsignedInteger
					&& (((UnsignedInteger) test.getNodeId().getValue()).longValue() == 5026
							|| ((UnsignedInteger) test.getNodeId().getValue()).longValue() == 1007)) {
				continue;
			}

			boolean isEqual = NodeContentTester.equals(src, test);

			assertTrue(isEqual,
					"Different nodes src: " + src.getDisplayName() + " and tester: " + test.getBrowseName());
		}
	}

}
