package com.bichler.astudio.opcua.editor.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.OPCUAAdvancedServerDriversModelNode;

public class OPCUAAdvancedDriverEditorInput implements IEditorInput {

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		OPCUAAdvancedDriverEditorInput iobj = (OPCUAAdvancedDriverEditorInput) obj;
		return (this.getServerName() + " - " + this.getDriverName())
				.compareTo(iobj.getServerName() + " - " + iobj.getDriverName()) == 0;
	}

	private IFileSystem fileSystem;
	private String dPConfigFile;

	private String serverName;
	private String driverName;
	private String driverConfigPath;
	private String serverRuntimePath;
	private String driverPath;
	private String driverType;
	private OPCUAAdvancedServerDriversModelNode node;

	public String getServerRuntimePath() {
		return serverRuntimePath;
	}

	public void setServerRuntimePath(String serverRuntimePath) {
		this.serverRuntimePath = serverRuntimePath;
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
		return "";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "";
	}

	public IFileSystem getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(IFileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	public String getDPConfigFile() {
		return dPConfigFile;
	}

	public void setDPConfigFile(String dPConfigFile) {
		this.dPConfigFile = dPConfigFile;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDriverConfigPath() {
		return driverConfigPath;
	}

	public void setDriverConfigPath(String driverConfigPath) {
		this.driverConfigPath = driverConfigPath;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public String getDriverType() {
		return this.driverType;
	}

	public void setNode(OPCUAAdvancedServerDriversModelNode node) {
		this.node = node;
	}

	public OPCUAAdvancedServerDriversModelNode getNode() {
		return this.node;
	}
}
