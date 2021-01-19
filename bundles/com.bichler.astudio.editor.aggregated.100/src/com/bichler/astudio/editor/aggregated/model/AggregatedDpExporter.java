package com.bichler.astudio.editor.aggregated.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.AbstractOPCDriverExporter;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class AggregatedDpExporter extends AbstractOPCDriverExporter {

	public AggregatedDpExporter(IFileSystem filesystem, String path) {
		super(filesystem, path);
	}

	@Override
	public void updateDatapoints(List<IDriverNode> entries, NamespaceTableChangeParameter trigger) {

		Map<String, String> mapping = trigger.getMapping();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		if (filesystem.isFile(path)) {
			try {
				reader = new BufferedReader(new InputStreamReader(filesystem.readFile(path)));
				String line = "";

				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#")) {
						continue;
					}
					// line columns
					String[] columns = line.split("\t");
					// split source nodeid column[0]
					if (columns != null && columns.length >= 2) {
						String[] splitted = columns[0].split(";");

						if (splitted == null || splitted.length < 2) {
							continue;
						}

						String nsText = splitted[0];
						String nsId = splitted[1];

						String mapped = mapping.get(nsText);
						if (mapped == null) {
							mapped = "";
							nsId = "";
						}

						columns[0] = mapped + ";" + nsId;
					}

					// save line
					for (String s : columns) {
						buffer.append(s);
						buffer.append("\t");
					}
					buffer.append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			OutputStream output = null;
			try {
				output = filesystem.writeFile(path);
				output.write(buffer.toString().getBytes());
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (output != null) {
						output.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void updateDatapoints(List<IDriverNode> entries, OPCUAUpdateNodeIdEvent trigger, NamespaceTable nsTable) {

		NodeId newId = trigger.getNewId();
		NodeId oldId = trigger.getOldId();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			if (filesystem.isFile(path)) {
				reader = new BufferedReader(new InputStreamReader(filesystem.readFile(path)));
				String line = "";

				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#")) {
						continue;
					}
					// line columns
					String[] columns = line.split("\t");
					// split source nodeid column[0]
					if (columns != null && columns.length >= 2) {
						String[] splitted = columns[0].split(";");

						if (splitted == null || splitted.length < 2) {
							continue;
						}

						String nsText = splitted[0];
						int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.getIndex(nsText);
						String nsId = splitted[1];

						NodeId nodeid = NodeId.parseNodeId("ns=" + index + ";" + nsId);

						if (oldId.equals(nodeid)) {
							String mapped = nsTable.getUri(newId.getNamespaceIndex());
							String idValue = "";
							String[] id = newId.toString().split(";");

							if (id.length <= 1) {
								idValue = id[0];
							} else {
								idValue = id[1];
							}

							columns[0] = mapped + ";" + idValue;
						}

						// String mapped = mapping.get(nsText);
						// if (mapped == null) {
						// mapped = "";
						// nsId = "";
						// }

						// columns[0] = mapped + ";" + nsId;
					}

					// save line
					for (String s : columns) {
						buffer.append(s);
						buffer.append("\t");
					}
					buffer.append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		OutputStream output = null;
		try {
			output = filesystem.writeFile(path);
			output.write(buffer.toString().getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateChild(IDriverNode entry, OPCUAUpdateNodeIdEvent trigger) {
		// TODO Auto-generated method stub

	}

	@Override
	protected StringBuffer saveChildren(IDriverNode driverNode, NamespaceTable nsTable) {
		// TODO Auto-generated method stub
		return null;
	}
}
