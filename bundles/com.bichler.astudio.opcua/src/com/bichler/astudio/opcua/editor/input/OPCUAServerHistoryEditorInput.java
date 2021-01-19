package com.bichler.astudio.opcua.editor.input;

import java.io.IOException;

import opc.sdk.core.application.ApplicationConfiguration;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.OPCUAServerHistoryModelNode;

public class OPCUAServerHistoryEditorInput extends FileStoreEditorInput implements IStorageEditorInput {

	public OPCUAServerHistoryEditorInput(IFileStore fileStore) {
		super(fileStore);
		// TODO Auto-generated constructor stub
	}

	private OPCUAServerHistoryModelNode node = null;

	private ApplicationConfiguration appConfig;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return this.node == ((OPCUAServerHistoryEditorInput) obj).node;
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

	public OPCUAServerHistoryModelNode getNode() {
		return node;
	}

	public void setNode(OPCUAServerHistoryModelNode node) {
		this.node = node;
	}

	public void init() {
		// IPreferenceStore store = ComponentsActivator.getDefault()
		// .getPreferenceStore();
		IFileSystem filesystem = getNode().getFilesystem();
		/** load xml file */
		String path = new Path(filesystem.getRootPath()).append("serverconfig").append("server.config.txt")
				.toOSString();

		if (this.node.getFilesystem().isFile(path)) {
			try {
				setAppConfig(new ApplicationConfiguration(this.node.getFilesystem().readFile(path)));
				// getAppConfig().setFilesystem(node.getFilesystem());
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

	@Override
	public IStorage getStorage() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}
}
