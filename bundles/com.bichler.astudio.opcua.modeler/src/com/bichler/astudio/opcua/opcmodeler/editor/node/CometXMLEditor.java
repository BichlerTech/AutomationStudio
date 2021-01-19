package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.io.InputStream;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

public class CometXMLEditor extends XMLMultiPageEditorPart implements IStorage {
	public static final String ID = "com.hbsoft.designer.editor.node.cometxmleditor";

	@Override
	public InputStream getContents() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPath getFullPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}
}
