package com.bichler.astudio.editor.aggregated.clientbrowser.model;

import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.editor.aggregated.clientbrowser.util.OPCConnectionAdapter;

public class RootCCModel extends AbstractCCModel{

	public RootCCModel(String displayname, NodeClass nodeClass, OPCConnectionAdapter serverAdapter) {
		super(displayname, nodeClass, Identifiers.RootFolder);
		setClient(serverAdapter);
	}

	public boolean isServerActive(){
		return getClient().isServerActive();
	}
	

}
