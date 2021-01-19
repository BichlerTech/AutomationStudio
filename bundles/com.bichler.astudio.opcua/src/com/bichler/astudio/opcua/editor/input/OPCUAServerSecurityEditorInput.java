package com.bichler.astudio.opcua.editor.input;

import java.io.IOException;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.security.OPCUAServerSecurityModelNode;

import opc.sdk.core.application.ApplicationConfiguration;

public class OPCUAServerSecurityEditorInput implements IEditorInput {

	private OPCUAServerSecurityModelNode node = null;

	private ApplicationConfiguration appConfig;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return this.node == ((OPCUAServerSecurityEditorInput) obj).node;
	}

	public OPCUAServerSecurityEditorInput() {
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return node.getServerName();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "test";
	}

	public OPCUAServerSecurityModelNode getNode() {
		return node;
	}

	public void setNode(OPCUAServerSecurityModelNode node) {
		this.node = node;
	}

	public void init() {
		IFileSystem filesystem = getNode().getFilesystem();
		/** load xml file */
		// String path = new Path(filesystem.getRootPath()).append("serverconfig")
		// .append("server.config.txt").toOSString();
		String path = new Path(filesystem.getRootPath()).append("serverconfig").append("server.config.xml")
				.toOSString();

		if (this.node.getFilesystem().isFile(path)) {
			// ServerConfigUtil.readServerConfiguration(filesystem, path);
			try {
				setAppConfig(new ApplicationConfiguration(this.node.getFilesystem().readFile(path)));
				// getAppConfig().setFilesystem(node.getFilesystem());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public ApplicationConfiguration getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(ApplicationConfiguration appConfig) {
		this.appConfig = appConfig;
	}
}
