package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowsePath;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowseResult;

public interface IOPCMasterManager {
	BrowsePathResult[] translateBrowsePathsToNodeIds(BrowsePath[] browsePaths, Object object)
			throws ServiceResultException;

	BrowseResult[] browse(BrowseDescription[] browseDescriptions, UnsignedInteger unsignedInteger, Object object,
			Object object2) throws ServiceResultException;
}
