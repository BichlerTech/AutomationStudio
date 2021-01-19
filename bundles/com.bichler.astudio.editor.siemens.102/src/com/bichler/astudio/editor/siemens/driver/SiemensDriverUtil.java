package com.bichler.astudio.editor.siemens.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bichler.astudio.editor.siemens.SiemensModelLabelProvider;
import com.bichler.astudio.editor.siemens.SiemensModelTreeContentProvider;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.model.SiemensNodeFactory;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.enums.DriverConfigProperties;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelListener;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class SiemensDriverUtil {
	public static void openDriverView(final IFileSystem filesystem, String driverConfig,
			final SiemensDBResourceManager structureManager) {
		SiemensModelLabelProvider labelProvider = new SiemensModelLabelProvider();
		SiemensModelTreeContentProvider contentProvider = new SiemensModelTreeContentProvider();
		Map<DriverConfigProperties, IDriverModelListener> listeners = new LinkedHashMap<>();
		// listener for structure
		listeners.put(DriverConfigProperties.pathdriverstruct, new IDriverModelListener() {
			@Override
			public void loadModel(String path) {
				if (!filesystem.isDir(path)) {
					return;
				}
				File directory = new File(path);
				String[] files = directory.list(new FilenameFilter() {
					@Override
					public boolean accept(File file, String filename) {
						if (filename != null) {
							int index = filename.lastIndexOf('.');
							String extension = filename.substring(index + 1);
							if ("csv".equalsIgnoreCase(extension)) {
								return true;
							}
						}
						return false;
					}
				});
				Map<String, AbstractSiemensNode> structures = new HashMap<>();
				for (String file : files) {
					String csv = directory + File.separator + file;
					if (filesystem.isFile(csv)) {
						InputStream symboTableFile = null;
						try {
							symboTableFile = filesystem.readFile(csv);
							AbstractSiemensNode struct = new SiemensNodeFactory().parseCSV(
									new BufferedReader(new InputStreamReader(symboTableFile)), false, structureManager);
							structures.put(struct.getName(), struct);
						} catch (Exception e1) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
						} finally {
							if (symboTableFile != null) {
								try {
									symboTableFile.close();
								} catch (IOException e1) {
									Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
								}
							}
						}
					}
				}
				structureManager.addStructures(structures);
			}
		});
		// listener for csv
		listeners.put(DriverConfigProperties.pathdrivermodel, new IDriverModelListener() {
			@Override
			public void loadModel(String path) {
				InputStream symboTableFile;
				if (!filesystem.isFile(path)) {
					return;
				}
				try {
					symboTableFile = filesystem.readFile(path);
					AbstractSiemensNode root = new SiemensNodeFactory().parseCSV(
							new BufferedReader(new InputStreamReader(symboTableFile)), true, structureManager);
					// set <address> field of nodes
					root.initializeAddress();
					DriverBrowserUtil.updateDriverModelView(root);
				} catch (IOException e1) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
				}
			}
		});
		DriverBrowserUtil.openDriverModelView(contentProvider, labelProvider, filesystem, driverConfig, listeners,
				new SiemensDriverDragSupport(), structureManager);
	}
}
