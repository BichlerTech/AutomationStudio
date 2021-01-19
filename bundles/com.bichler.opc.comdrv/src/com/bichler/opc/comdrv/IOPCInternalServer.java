package com.bichler.opc.comdrv;

import org.opcfoundation.ua.common.NamespaceTable;

import opc.sdk.core.types.TypeTable;

public interface IOPCInternalServer {
	IOPCAddressSpaceManager getAddressSpaceManager();

	IOPCMasterManager getMaster();

	NamespaceTable getNamespaceUris();

	void setUserConfiguration(String absolutePath);

	ComDRV getUserAuthentifiationManager();

	TypeTable getTypeTable();
}
