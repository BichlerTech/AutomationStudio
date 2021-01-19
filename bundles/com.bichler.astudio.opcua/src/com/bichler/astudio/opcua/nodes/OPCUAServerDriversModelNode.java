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
import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerDriversModelNode extends OPCUAServerModelNode {
	private String driverName = "";
	private String driverType = "";

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

	@Override
	public void nodeDBLClicked() {
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_OPCUADRIVERS);
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
		OPCUAServerDriverModelNode node = null;
		if (filesystem instanceof SimpleFileSystem) {
			nodes = new ArrayList<>();
			IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
			String drivers = new Path(filesystem.getRootPath())
					.append(opcuastore.getString(OPCUAConstants.ASOPCUADriversFolder)).toOSString();
			try {
				if (filesystem.isDir(drivers)) {
					Logger.getLogger(getClass().getName()).log(Level.INFO, "load configured drivers from: " + drivers);
					String[] drvs = filesystem.listDirs(drivers);
					for (String drv : drvs) {
						if (drv.startsWith(".") || drv.startsWith("..")) {
							continue;
						}
						String config = new Path(drivers).append(drv)
								.append(opcuastore.getString(OPCUAConstants.ASOPCUADriverConfigFile)).toOSString();
						/** read properties from config */
						if (filesystem.isFile(config)) {
							node = new OPCUAServerDriverModelNode();
							node.setServerName(serverName);
							node.setDriverName(drv);
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
									} else if (line.compareTo("drivertype") == 0) {
										node.setDriverType(reader.readLine());
									} else if (line.compareTo("driverversion") == 0) {
										node.setDriverVersion(reader.readLine());
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
							 * we need minimum driver version 1.0.0
							 */
							String version = node.getDriverVersion();
							if (version.isEmpty())
								version = "1.0.0";
							Bundle bundle = OPCUADriverRegistry.drivers
									.get("com.bichler.astudio.editor." + node.getDriverType() + "." + version)
									.getBundle();
							URL url = FileLocator.find(bundle,
									Path.ROOT.append("driver").append("image").append("driver.png"), null);
							if (url == null) {
								Logger.getLogger(getClass().getName()).log(Level.WARNING,
										"No url for bundle: com.bichler.astudio.editor." + node.getDriverType() + "."
												+ version + " found!");
								continue;
							}
							URL fileURL = FileLocator.toFileURL(url);
							String imgPath = fileURL.getFile();
							// find driver image
							if (filesystem.isFile(imgPath)) {
								InputStream imstream = filesystem.readFile(imgPath);
								Image img = new Image(Display.getCurrent(), imstream);
								node.setDriverImage(img);
								imstream.close();
							}
							nodes.add(node);

						} else {
							Logger.getLogger(getClass().getName()).log(Level.WARNING, "No config file for driver: "
									+ drv + " found. We please see folder: " + drivers + "/" + drv);
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
}
