package com.bichler.astudio.opcua.editor.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverConfigModelNode;

public class OPCUADriverEditorInput implements IEditorInput {
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		OPCUADriverEditorInput iobj = (OPCUADriverEditorInput) obj;
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
	private OPCUAServerDriverConfigModelNode node = null;
	private String tooltiptext;

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

	public void setToolTipText(String tooltiptext) {
		this.tooltiptext = tooltiptext;
	}

	@Override
	public String getToolTipText() {
		return this.tooltiptext;
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

	public OPCUAServerDriverConfigModelNode getNode() {
		return node;
	}

	public void setNode(OPCUAServerDriverConfigModelNode node) {
		this.node = node;
	}
}
