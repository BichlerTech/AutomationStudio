package com.bichler.astudio.editor.aggregated.clientbrowser.util;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;

public interface OPCConnectionAdapter {

	public boolean isServerActive();

	public BrowseResult browse(NodeId parentId,
			BrowseDirection forward, boolean includeSubtyes, UnsignedInteger nodeClass,
			NodeId referenceId, UnsignedInteger resultMask);
}
