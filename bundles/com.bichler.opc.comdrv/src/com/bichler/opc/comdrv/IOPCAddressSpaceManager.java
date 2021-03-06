package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.NodeId;

import opc.sdk.core.node.AbstractNodeFactory;
import opc.sdk.core.node.Node;

public interface IOPCAddressSpaceManager {
	Node getNodeById(NodeId nidToTranslate);

	AbstractNodeFactory getNodeFactory();
}
