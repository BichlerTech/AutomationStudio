package com.bichler.astudio.opcua.nodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.activator.InternationalActivator;

public class OPCUAServerDriverModelNode extends OPCUAServerModelNode {
	protected String driverName = "";
	protected String driverType = "";
	protected String driverVersion = "";
	protected String editorName = "";

	public OPCUAServerDriverModelNode() {
		driverVersion = "";
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	protected Image driverImage = null;
	private IFileSystem targetFilesystem;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverType() {
		return driverType;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	public Image getDriverImage() {
		return driverImage;
	}

	public void setDriverImage(Image driverImage) {
		this.driverImage = driverImage;
	}

	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub
	}

	@Override
	public Image getLabelImage() {
		return this.getDriverImage();
	}

	@Override
	public String getLabelText() {
		if (driverVersion.isEmpty())
			return this.getDriverName() + " (upgrade!)";
		return this.getDriverName();
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		boolean drvconfig = false;
		boolean dpconfig = false;
		boolean drvadvancedconfig = false;
		String drvEditor = "";
		String dpEditor = "";
		String advEditor = "";
		if (nodes == null) {
			/**
			 * read available driver editors from studio.config
			 */
			IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
//      String serverpath = this.getFilesystem().getRootPath();
			String driverConfigPath = null;
			InternationalActivator activator = OPCUADriverRegistry.drivers
					.get("com.bichler.astudio.editor." + this.driverType + "." + this.driverVersion);
			if (activator == null) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"No installed module for driver: " + this.driverName + " found!");
				return new StudioModelNode[0];
			}
			try {
				URL url = FileLocator.find(activator.getBundle(),
						Path.ROOT.append("driver").append("config").append("studio.config"), null);
				URL fileUrl = FileLocator.toFileURL(url);
				driverConfigPath = fileUrl.getFile();
			} catch (IOException e1) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
				driverConfigPath = new Path(store.getString(OPCUAConstants.OPCUARuntime))
						.append(store.getString(OPCUAConstants.ASOPCUARuntimePath))
						.append(store.getString(OPCUAConstants.ASOPCUADriversFolder)).append(this.getDriverType())
						.append("config").append("studio.config").toOSString();
			}
			try (InputStream reader = this.getFilesystem().readFile(driverConfigPath);
					BufferedReader buffer = new BufferedReader(new InputStreamReader(reader));) {
				String line = "";
				while ((line = buffer.readLine()) != null) {
					if (line.compareTo("drivereditorid_general") == 0) {
						drvconfig = true;
						drvEditor = buffer.readLine();
					} else if (line.compareTo("drivereditorid_dp") == 0) {
						dpconfig = true;
						dpEditor = buffer.readLine();
					} else if (line.compareTo("drivereditorid_advanced") == 0) {
						drvadvancedconfig = true;
						advEditor = buffer.readLine();
					}
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			nodes = new ArrayList<>();
			if (drvconfig) {
				OPCUAServerDriverConfigModelNode connode = new OPCUAServerDriverConfigModelNode();
				connode.setDriverName(this.getDriverName());
				connode.setFilesystem(filesystem);
				connode.setDriverType(this.getDriverType());
				connode.setDriverVersion(this.getDriverVersion());
				connode.setServerName(this.getServerName());
				connode.setEditorName(drvEditor);
				nodes.add(connode);
			}
			if (drvadvancedconfig) {
				OPCUAAdvancedServerDriversModelNode advconnode = new OPCUAAdvancedServerDriversModelNode();
				advconnode.setDriverName(this.getDriverName());
				advconnode.setServerName(this.getServerName());
				advconnode.setDriverType(this.getDriverType());
				advconnode.setDriverVersion(this.getDriverVersion());
				advconnode.setFilesystem(filesystem);
				advconnode.setEditorName(advEditor);
				nodes.add(advconnode);
			}
			if (dpconfig) {
				OPCUAServerDriverDPsModelNode dpnode = new OPCUAServerDriverDPsModelNode();
				dpnode.setDriverName(this.getDriverName());
				dpnode.setServerName(this.getServerName());
				dpnode.setDriverType(this.getDriverType());
				dpnode.setDriverVersion(this.getDriverVersion());
				dpnode.setFilesystem(filesystem);
				dpnode.setEditorName(dpEditor);
				nodes.add(dpnode);
			}
			if (filesystem != null && filesystem.isconnected()) {
				IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
				String imgPath = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
						+ opcuastore.getString(OPCUAConstants.ASOPCUARuntimePath) + filesystem.getTargetFileSeparator()
						+ opcuastore.getString(OPCUAConstants.ASOPCUADriversFolder)
						+ filesystem.getTargetFileSeparator() + getDriverType() + filesystem.getTargetFileSeparator()
						+ "image" + filesystem.getTargetFileSeparator() + "driver.png";
				if (filesystem.isFile(imgPath)) {
					try (InputStream inputstream = filesystem.readFile(imgPath);) {
						this.driverImage = new Image(Display.getCurrent(), inputstream);
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
				}
				this.setChildren(nodes);
			}
		}
		return nodes.toArray();
	}

	@Override
	public void rename(String newName) {
		String root = getFilesystem().getRootPath();
		IPath drvPath = new Path(root).append("drivers").append(getDriverName());
		IPath newPath = new Path(root).append("drivers").append(newName);
		try {
			getFilesystem().renameFile(drvPath.toOSString(), newPath.toOSString());
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}

//	@Override
//	public void setTargetFilesystem(IFileSystem targetFilesystem) {
//		this.targetFilesystem = targetFilesystem;
//	}
//
//	@Override
//	public IFileSystem getTargetFilesystem() {
//		return this.targetFilesystem;
//	}
	
//	@Override
//	public void setFilesystem(IFileSystem filesystem) {
//		this.filesystem = targetFilesystem;
//	}
//
//	@Override
//	public IFileSystem getFilesystem() {
//		return this.filesystem;
//	}
}
