package com.bichler.astudio.opcua.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.bichler.astudio.filesystem.IFileSystem;

public class LoggingModelManager {

	enum LogProperty {
		ROOT(".rootLogger"), LOGGER(".logger."), APPENDER(".appender.");

		private String property;

		LogProperty(String name) {
			this.property = name;
		}

		public String getPropertyName() {
			return this.property;
		}
	}

	private Map<String, LoggingModelNode> logModel = new LinkedHashMap<>();
	private IFileSystem filesystem;
	private IPath path;

	public LoggingModelManager() {
	}

	public LoggingModelNode removeLogNode(LoggingModelNode node) {
		return this.logModel.remove(node.getKey());
	}

	public void initialize(IFileSystem filesystem, IPath path) {
		this.filesystem = filesystem;
		this.path = path;

		initialize(LogProperty.ROOT, filesystem, path);
		initialize(LogProperty.LOGGER, filesystem, path);
		initialize(LogProperty.APPENDER, filesystem, path);
	}

	public void doSaveEntry(IProgressMonitor monitor) {
		// maybe to calculate progress dialog
		monitor.beginTask("Speichere OPC UA Logging Einstellungen", IProgressMonitor.UNKNOWN);
		monitor.setTaskName("Speichern...");

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(filesystem.writeFile(path.toOSString())));
			for (Entry<String, LoggingModelNode> entry : this.logModel.entrySet()) {
				LoggingModelNode node = entry.getValue();
				doSaveLogger(writer, node);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Map<String, LoggingModelNode> getModel() {
		return this.logModel;
	}

	private void addLogNode(LoggingModelNode node) {
		this.logModel.put(node.getKey(), node);
	}

	private void doSaveLogger(BufferedWriter writer, LoggingModelNode node) throws IOException {
		String key = node.getKey();
		String value = node.getValue();

		writer.write(key + "=" + value);
		writer.newLine();

		LoggingModelNode[] children = node.getChildren();
		for (LoggingModelNode child : children) {
			doSaveLogger(writer, child);
		}

	}

	private void initialize(LogProperty property, IFileSystem filesystem, IPath path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(filesystem.readFile(path.toOSString())));

			LoggingModelNode lastParent = null;
			String mainKey = null;

			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] keyVal = line.split("=");

				// validate key/value pair
				if (keyVal == null || keyVal.length < 2) {
					continue;
				}

				LoggingModelNode node = null;
				// key/value property
				String key = keyVal[0];
				String value = keyVal[1];

				if (key.contains(property.getPropertyName())) {
					do {
						// no main property
						if (mainKey == null) {
							// set main property
							node = lastParent = new LoggingModelNode(key, value);
							mainKey = key;
							// add main property to manager
							addLogNode(node);
						}
						// fetch properties for main property
						else {
							// check is property for last parent
							if (key.contains(mainKey)) {
								node = new LoggingModelNode(key, value);
								// add property to parent
								lastParent.addChild(node);
							}
							// otherwise start new property
							else {
								mainKey = null;
							}
						}
						// repeat if new property has started
					} while (mainKey == null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
