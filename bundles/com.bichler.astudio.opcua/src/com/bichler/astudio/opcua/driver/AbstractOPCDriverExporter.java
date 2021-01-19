package com.bichler.astudio.opcua.driver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public abstract class AbstractOPCDriverExporter implements IOPCDriverExporter {

	protected IFileSystem filesystem;
	protected String path;

	public AbstractOPCDriverExporter(IFileSystem filesystem, String path) {
		this.filesystem = filesystem;
		this.path = path;
	}

	@Override
	public void updateDatapoints(List<IDriverNode> entries, OPCUAUpdateNodeIdEvent trigger, NamespaceTable nsTable) {
		for (Object entry : entries) {
			updateChild((IDriverNode) entry, trigger);
		}
		build(entries, nsTable);

//		write(filesystem, path, configuration);
	}

	@Override
	public void updateDatapoints(List<IDriverNode> entries, NamespaceTableChangeParameter trigger) {

		Map<Integer, Integer> mapping = trigger.getIndexMapping();

		for (Object entry : entries) {
			updateChild((IDriverNode) entry, mapping);
		}

		build(entries, trigger.getNamespaceTable2change());
//		write(filesystem, path, configuration);
	}

	/**
	 * Creates the configuration to export.
	 * 
	 * @param items
	 * @param nsTable
	 * @return
	 */
	public StringBuffer build(List<IDriverNode> items, NamespaceTable nsTable) {
		StringBuffer buffer = new StringBuffer();
		try (OutputStream out = filesystem.writeFile(path)) {
			// first we have to clear file
			out.write(new byte[0]);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		try (OutputStream out = filesystem.writeFile(path)) {
			// open in append mode

			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n");
			buffer.append("<DPConfiguration>\n");
			buffer.append("  <DPs>\n");

			write(out, buffer);
			for (IDriverNode item : items) {
				buffer = saveChildren(item, nsTable);
				write(out, buffer);
			}

			buffer = new StringBuffer();
			buffer.append("  </DPs>\n");
			buffer.append("</DPConfiguration>\n");
			write(out, buffer);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return buffer;
	}

	/**
	 * Writes a configuration string to a target file
	 * 
	 * @param filesystem
	 * @param path       Namespace id table
	 * @param buffer
	 */
	public void write(IFileSystem filesystem, String path, StringBuffer buffer) {

		if (!filesystem.isFile(path)) {
			try {
				filesystem.addFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (filesystem.isFile(path)) {
			OutputStream output = null;
			try {
				output = filesystem.writeFile(path, true);
				write(output, buffer);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void write(OutputStream output, StringBuffer buffer) throws IOException {
		output.write(buffer.toString().getBytes());
		output.flush();

	}

	/**
	 * Update node ids for driver node
	 * 
	 * @param item2update
	 * @param mapping
	 */
	protected abstract void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping);

	/**
	 * Update node ids for driver node
	 * 
	 * @param entry
	 * @param trigger
	 */
	protected abstract void updateChild(IDriverNode entry, OPCUAUpdateNodeIdEvent trigger);

	/**
	 * Save children in particular way
	 * 
	 * @param buffer
	 * @param driverNode
	 * @param nsTable
	 */
	protected abstract StringBuffer saveChildren(IDriverNode driverNode, NamespaceTable nsTable);

	protected String escape(String input) {
		// StringBuffer output = new StringBuffer();
		// for (int i = 0; i < input.length(); i++) {
		// if ((int) input.charAt(i) > 32 && (int) input.charAt(i) < 126) {
		// output.append(input.charAt(i));
		// }
		// }

		if (input == null) {
			return "";
		}

		return input.replace("ä", "ae").replace("Ä", "Ae").replace("ö", "oe").replace("Ö", "Oe").replace("ü", "ue")
				.replace("Ü", "Ue").replace("ß", "ss").replace("\"", "");
	}

}
