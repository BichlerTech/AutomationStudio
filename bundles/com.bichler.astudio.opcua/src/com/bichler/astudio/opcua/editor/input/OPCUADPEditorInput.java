package com.bichler.astudio.opcua.editor.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;

public class OPCUADPEditorInput implements IEditorInput {
	private String serverName;
	private String driverName;
	private IFileSystem filesystem;
	private String driverConfigFile;
	private String dPConfigFile;
	private String driverPath;
	private OPCUAServerDriverDPsModelNode node = null;
	private String serverRuntimePath;
	private String tooltiptext = "";

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		OPCUADPEditorInput iobj = (OPCUADPEditorInput) obj;
		return (this.getServerName() + " - " + this.getDriverName())
				.compareTo(iobj.getServerName() + " - " + iobj.getDriverName()) == 0;
	}

	public OPCUAServerDriverDPsModelNode getNode() {
		return node;
	}

	public void setNode(OPCUAServerDriverDPsModelNode node) {
		this.node = node;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	public String getDriverConfigFile() {
		return driverConfigFile;
	}

	public void setDriverConfigFile(String driverConfigFile) {
		this.driverConfigFile = driverConfigFile;
	}

	public String getDPConfigFile() {
		return dPConfigFile;
	}

	public void setDPConfigFile(String dPConfigFile) {
		this.dPConfigFile = dPConfigFile;
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
		return driverName;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		if (tooltiptext.isEmpty())
			return driverName;
		return tooltiptext;
	}

	public void setToolTipText(String tooltiptext) {
		this.tooltiptext = tooltiptext;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServerRuntimePath() {
		return serverRuntimePath;
	}

	public void setServerRuntimePath(String serverRuntimePath) {
		this.serverRuntimePath = serverRuntimePath;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

}
