package com.bichler.opcua.statemachine.test.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.test.NodeContentTester;

import opc.sdk.core.node.Node;

class StatemachineGeneratorObjectModelTester {

	@Before
	public void setup() {

	}

	@After
	public void releaseResources() {

	}

	@Test
	void testStateMachineType() throws Exception {
		String pathSrc = "nodeset/v003/types.xml";
		String pathTarget = "nodeset/v003/types_as2.xml";

		File fileSrc = new File(pathSrc);
		File fileTarget = new File(pathTarget);

		StatemachineNodesetImporter importerOrigin = new StatemachineNodesetImporter();
		StatemachineNodesetImporter importerTester = new StatemachineNodesetImporter();

		importerOrigin.importNodeSet(new File[] { fileSrc });
		importerTester.importNodeSet(new File[] { fileTarget });

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
