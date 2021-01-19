package com.bichler.astudio.opcua.editor.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.node.user.DBAuthority;
import opc.sdk.core.node.user.DBRole;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.OPCUAServerUsersModelNode;

public class OPCUAServerUsersEditorInput implements IEditorInput {

	private OPCUAServerUsersModelNode node = null;
	private ApplicationConfiguration appConfig;

	private Map<DBRole, Map<NodeId, DBAuthority>> dirtyRoleNodes;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return this.node == ((OPCUAServerUsersEditorInput) obj).node;
	}

	public OPCUAServerUsersEditorInput() {
		this.dirtyRoleNodes = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
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

	public OPCUAServerUsersModelNode getNode() {
		return node;
	}

	public void setNode(OPCUAServerUsersModelNode node) {
		this.node = node;
	}

	public void init() {
		IFileSystem filesystem = getNode().getFilesystem();
		// initialize server configuration
		String path = new Path(filesystem.getRootPath()).append("serverconfig").append("server.config.txt")
				.toOSString();
		if (this.node.getFilesystem().isFile(path)) {
			try {
				setAppConfig(new ApplicationConfiguration(this.node.getFilesystem().readFile(path)));
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		// add users directory if not existing
		IPath usrPath = new Path(filesystem.getRootPath()).append("users");
		if (!filesystem.isDir(usrPath.toOSString())) {
			try {
				filesystem.addDir(usrPath.toOSString());
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
	}

	public ApplicationConfiguration getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(ApplicationConfiguration appConfig) {
		this.appConfig = appConfig;
	}

	public void addDirtyRoleNodes(DBRole role2add, Map<NodeId, DBAuthority> nodeAuthority) {
		this.dirtyRoleNodes.put(role2add, nodeAuthority);
	}

	public void removeDirtyRoleNodes(DBRole role2remove) {
		this.dirtyRoleNodes.remove(role2remove);
	}

	public Map<NodeId, DBAuthority> getDirtyRoleNodes(DBRole role) {
		return this.dirtyRoleNodes.get(role);
	}
}
