package com.bichler.astudio.opcua.driver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModuleModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModulesModelNode;

public class OPCModuleUtil {
	private Logger logger = Logger.getLogger(getClass().getName());

	public static Object[] getAllModuleNamesFromOPCProject(StudioModelNode node) {
		IFileSystem filesystem = node.getFilesystem();
		return getAllModuleNamesFromOPCProject(filesystem, node);
	}

	public static Object[] getAllModuleNamesFromOPCProject(IFileSystem filesystem, StudioModelNode node) {
		IPath rootPath = new Path(filesystem.getRootPath());
		IPath modulePath = rootPath.append("modules");
		String[] moduleFiles = null;
		if (filesystem.isDir(modulePath.toOSString())) {
			moduleFiles = modulePath.toFile().list();
		}
		if (moduleFiles != null) {
			List<OPCUAServerModuleModelNode> children = new ArrayList<>();
			findModules(node.getChildren(), children);
			return children.toArray();
		}
		return new String[0];
	}

	/**
	 * Reads module.com file and stores attributes in key/value map.
	 * 
	 * @param filesystem
	 * @param path
	 * @return
	 */
	public static Map<String, String> readModuleCom(IFileSystem filesystem, String path) {
		Map<String, String> configuration = new HashMap<>();
		if (filesystem == null) {
			return configuration;
		}
		if (!filesystem.isFile(path)) {
			return configuration;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(filesystem.readFile(path)));
			boolean isKey = false;
			String key = "";
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				if (!isKey && line.isEmpty()) {
					continue;
				}
				if (!isKey) {
					key = line;
					isKey = true;
				} else {
					configuration.put(key, line);
					isKey = false;
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(OPCModuleUtil.class.getName()).log(Level.SEVERE, ex.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					Logger.getLogger(OPCModuleUtil.class.getName()).log(Level.SEVERE, e1.getMessage());
				}
			}
		}
		return configuration;
	}

	public static void writeModuleCom(IFileSystem filesystem, String modulePath,
			Map<String, String> attributes2change) {
		if (!filesystem.isFile(modulePath)) {
			return;
		}
		StringBuilder moduleConfigString = new StringBuilder();
		BufferedReader bfr = null;
		try {
			bfr = new BufferedReader(new InputStreamReader(filesystem.readFile(modulePath)));
			String line = "";
			while ((line = bfr.readLine()) != null) {
				moduleConfigString.append(line + "\n");
				boolean attribute2change = attributes2change.containsKey(line.trim());
				if (attribute2change) {
					// skip
					bfr.readLine();
					moduleConfigString.append(attributes2change.remove(line.trim()) + "\n");
				}
			}
			// remaining entries
			for (Entry<String, String> entry : attributes2change.entrySet()) {
				moduleConfigString.append(entry.getKey() + "\n");
				moduleConfigString.append(entry.getValue() + "\n");
				moduleConfigString.append("\n");
			}
		} catch (IOException e) {
			Logger.getLogger(OPCModuleUtil.class.getName()).log(Level.SEVERE, e.getMessage());
			moduleConfigString = null;
		} finally {
			if (bfr != null) {
				try {
					bfr.close();
				} catch (IOException e) {
					Logger.getLogger(OPCModuleUtil.class.getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		if (moduleConfigString == null) {
			return;
		}
		OutputStream out = null;
		try {
			out = filesystem.writeFile(modulePath);
			out.write(moduleConfigString.toString().getBytes());
			out.flush();
		} catch (IOException e) {
			Logger.getLogger(OPCModuleUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(OPCModuleUtil.class.getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}

	private static void findModules(Object[] children, List<OPCUAServerModuleModelNode> children2fetch) {
		if (children == null) {
			return;
		}
		for (Object c : children) {
			if (c instanceof OPCUAServerModulesModelNode) {
				Object[] cc = ((OPCUAServerModulesModelNode) c).getChildren();
				for (Object child2add : cc) {
					children2fetch.add((OPCUAServerModuleModelNode) child2add);
				}
			}
		}
	}
}
