package com.bichler.astudio.opcua.opcmodeler.dialogs.utils;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;

public interface ITreeViewerChildren {
	public boolean hasTreeViewerChildren(NodeId result, UnsignedInteger filter);
}
