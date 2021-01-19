package com.bichler.astudio.opcua.editor.input;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.OPCUAServerLoggingModelNode;
import com.bichler.astudio.opcua.wizard.util.OPCWizardUtil;

public class OPCUAServerLoggingEditorInput implements IEditorInput {

	private OPCUAServerLoggingModelNode node = null;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return this.node == ((OPCUAServerLoggingEditorInput) obj).node;
	}

	public OPCUAServerLoggingEditorInput() {

	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return node.getServerName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Logging Eigenschaften";
	}

	public OPCUAServerLoggingModelNode getNode() {
		return this.node;
	}

	public void setNode(OPCUAServerLoggingModelNode node) {
		this.node = node;
	}

	public void init() {
		IFileSystem filesystem = getNode().getFilesystem();
		// initialize server configuration
		IPath path = new Path(filesystem.getRootPath());
		String logPath = path.append("log.properties").toOSString();

		// add empty log.properties file if no exist
		if (!this.node.getFilesystem().isFile(logPath)) {
			try {
				OPCWizardUtil.createSectionProjectLogging(node.getFilesystem(), path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
