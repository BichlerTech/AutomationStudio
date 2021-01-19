package com.bichler.astudio.opcua.nodes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.opcua.OPCUAModuleRegistry;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerModulesModelNode extends OPCUAServerModelNode {
	private String moduleName = "";
	private String moduleType = "";

	@Override
	public void nodeDBLClicked() {
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_OPCUAMODULES);
	}

	@Override
	public String getLabelText() {
		return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				this.getClass().getSimpleName() + ".LabelText");
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = new ArrayList<>();
		String serverName = this.getServerName();
		OPCUAServerModuleModelNode node = null;
		if (filesystem instanceof SimpleFileSystem) {
			nodes = new ArrayList<>();
			IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
			String modules = new Path(filesystem.getRootPath())
					.append(opcuastore.getString(OPCUAConstants.ASOPCUAModulesFolder)).toOSString();
			try {
				if (filesystem.isDir(modules)) {
					Logger.getLogger(getClass().getName()).log(Level.INFO, "load configured modules from: " + modules);
					String[] mods = filesystem.listDirs(modules);
					for (String mod : mods) {
						if (mod.startsWith(".") || mod.startsWith("..")) {
							continue;
						}
						String config = new Path(modules).append(mod)
								.append(opcuastore.getString(OPCUAConstants.ASOPCUAModuleConfigFile)).toOSString();
						/** read properties from config */
						if (filesystem.isFile(config)) {
							node = new OPCUAServerModuleModelNode();
							node.setServerName(serverName);
							node.setModuleName(mod);
							node.setFilesystem(filesystem);
							node.setParent(this);
							BufferedReader reader = null;
							try {
								InputStream stream = filesystem.readFile(config);
								InputStreamReader inputstream = new InputStreamReader(stream);
								reader = new BufferedReader(inputstream);
								String line = "";
								while ((line = reader.readLine()) != null) {
									if (line.startsWith("#") || line.isEmpty()) {
										continue;
									} else if (line.compareTo("moduletype") == 0) {
										node.setModuleType(reader.readLine());
									} else if (line.compareTo("moduleversion") == 0) {
										node.setModuleVersion(reader.readLine());
									}
								}
							} catch (FileNotFoundException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
							} finally {
								if (reader != null) {
									try {
										reader.close();
									} catch (IOException e) {
										Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
									}
								}
							}
							/**
							 * we need minimum module version 1.0.0
							 */
							String version = node.getModuleVersion();
							if (version.isEmpty())
								version = "1.0.0";
							Bundle bundle = OPCUAModuleRegistry.modules
									.get("com.bichler.astudio.editor." + node.getModuleType() + "." + version)
									.getBundle();
							URL url = FileLocator.find(bundle,
									Path.ROOT.append("module").append("image").append("module.png"), null);
							if (url == null) {
								Logger.getLogger(getClass().getName()).log(Level.WARNING,
										"No url for bundle: com.bichler.astudio.editor." + node.getModuleType() + "."
												+ version + " found!");
								continue;
							}
							URL fileURL = FileLocator.toFileURL(url);
							String imgPath = fileURL.getFile();
							// find driver image
							if (filesystem.isFile(imgPath)) {
								InputStream imstream = filesystem.readFile(imgPath);
								Image img = new Image(Display.getCurrent(), imstream);
								node.setModuleImage(img);
								imstream.close();
							}
							nodes.add(node);
						} else {
							Logger.getLogger(getClass().getName()).log(Level.WARNING, "No config file for module: "
									+ mod + " found. Please see folder: " + modules + "/" + mod);
						}
					}
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			this.setChildren(nodes);
		}
		return nodes.toArray();
	}

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
}
