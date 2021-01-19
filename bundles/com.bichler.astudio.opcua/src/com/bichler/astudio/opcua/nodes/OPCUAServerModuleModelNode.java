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
import com.bichler.astudio.opcua.OPCUAModuleRegistry;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.activator.InternationalActivator;

public class OPCUAServerModuleModelNode extends OPCUAServerModelNode {
	protected String moduleName = "";
	protected String moduleType = "";
	protected String moduleVersion = "";
	protected String editorName = "";

	public OPCUAServerModuleModelNode() {
		moduleVersion = "";
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	protected Image moduleImage = null;
	private IFileSystem targetFilesystem;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleVersion() {
		return moduleVersion;
	}

	public void setModuleVersion(String moduleVersion) {
		this.moduleVersion = moduleVersion;
	}

	public Image getModuleImage() {
		return moduleImage;
	}

	public void setModuleImage(Image moduleImage) {
		this.moduleImage = moduleImage;
	}

	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub
	}

	@Override
	public Image getLabelImage() {
		return this.getModuleImage();
	}

	@Override
	public String getLabelText() {
		if (moduleVersion.isEmpty())
			return this.getModuleName() + " (upgrade!)";
		return this.getModuleName();
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		boolean modconfig = false;
		boolean dpconfig = false;
		boolean modadvancedconfig = false;
		String modEditor = "";
		String dpEditor = "";
		String advEditor = "";
		if (nodes == null) {
			/**
			 * read available module editors from studio.config
			 */
			IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
//      String serverpath = this.getFilesystem().getRootPath();
			String moduleConfigPath = null;
			InternationalActivator activator = OPCUAModuleRegistry.modules
					.get("com.bichler.astudio.editor." + this.moduleType + "." + this.moduleVersion);
			if (activator == null) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"No installed module for module: " + this.moduleName + " found!");
				return new StudioModelNode[0];
			}
			try {
				URL url = FileLocator.find(activator.getBundle(),
						Path.ROOT.append("module").append("config").append("studio.config"), null);
				URL fileUrl = FileLocator.toFileURL(url);
				moduleConfigPath = fileUrl.getFile();
			} catch (IOException e1) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
				moduleConfigPath = new Path(store.getString(OPCUAConstants.OPCUARuntime))
						.append(store.getString(OPCUAConstants.ASOPCUARuntimePath))
						.append(store.getString(OPCUAConstants.ASOPCUAModulesFolder)).append(this.getModuleType())
						.append("config").append("studio.config").toOSString();
			}
			try (InputStream reader = this.getFilesystem().readFile(moduleConfigPath);
					BufferedReader buffer = new BufferedReader(new InputStreamReader(reader));) {
				String line = "";
				while ((line = buffer.readLine()) != null) {
					if (line.compareTo("moduleeditorid_general") == 0) {
						modconfig = true;
						modEditor = buffer.readLine();
					} else if (line.compareTo("moduleeditorid_dp") == 0) {
						dpconfig = true;
						dpEditor = buffer.readLine();
					} else if (line.compareTo("moduleeditorid_advanced") == 0) {
						modadvancedconfig = true;
						advEditor = buffer.readLine();
					}
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			nodes = new ArrayList<>();
			if (modconfig) {
				OPCUAServerModuleConfigModelNode connode = new OPCUAServerModuleConfigModelNode();
				connode.setModuleName(this.getModuleName());
				connode.setFilesystem(filesystem);
				connode.setModuleType(this.getModuleType());
				connode.setModuleVersion(this.getModuleVersion());
				connode.setServerName(this.getServerName());
				connode.setEditorName(modEditor);
				nodes.add(connode);
			}
			if (modadvancedconfig) {
				OPCUAAdvancedServerModulesModelNode advconnode = new OPCUAAdvancedServerModulesModelNode();
				advconnode.setModuleName(this.getModuleName());
				advconnode.setServerName(this.getServerName());
				advconnode.setModuleType(this.getModuleType());
				advconnode.setModuleVersion(this.getModuleVersion());
				advconnode.setFilesystem(filesystem);
				advconnode.setEditorName(advEditor);
				nodes.add(advconnode);
			}
			if (dpconfig) {
				OPCUAServerModuleDPsModelNode dpnode = new OPCUAServerModuleDPsModelNode();
				dpnode.setModuleName(this.getModuleName());
				dpnode.setServerName(this.getServerName());
				dpnode.setModuleType(this.getModuleType());
				dpnode.setModuleVersion(this.getModuleVersion());
				dpnode.setFilesystem(filesystem);
				dpnode.setEditorName(dpEditor);
				nodes.add(dpnode);
			}
			if (filesystem != null && filesystem.isconnected()) {
				IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
				String imgPath = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
						+ opcuastore.getString(OPCUAConstants.ASOPCUARuntimePath) + filesystem.getTargetFileSeparator()
						+ opcuastore.getString(OPCUAConstants.ASOPCUAModulesFolder)
						+ filesystem.getTargetFileSeparator() + getModuleType() + filesystem.getTargetFileSeparator()
						+ "image" + filesystem.getTargetFileSeparator() + "module.png";
				if (filesystem.isFile(imgPath)) {
					try (InputStream inputstream = filesystem.readFile(imgPath);) {
						this.moduleImage = new Image(Display.getCurrent(), inputstream);
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
		IPath modPath = new Path(root).append("modules").append(getModuleName());
		IPath newPath = new Path(root).append("modules").append(newName);
		try {
			getFilesystem().renameFile(modPath.toOSString(), newPath.toOSString());
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
