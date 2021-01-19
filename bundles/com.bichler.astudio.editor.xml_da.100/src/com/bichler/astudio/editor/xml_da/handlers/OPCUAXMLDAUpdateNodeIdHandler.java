package com.bichler.astudio.editor.xml_da.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.xml.sax.InputSource;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.xml_da.xml.XMLDADPEditorImporter;
import com.bichler.astudio.editor.xml_da.xml.XMLDADPExporter;
import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;
import com.bichler.astudio.editor.xml_da.xml.XML_DA_DPItem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNodeIdHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUAXMLDAUpdateNodeIdHandler extends AbstractOPCUAUpdateNodeIdHandler {
	public static final String ID = "command.update.opcua.nodeid.driver.xml_da";

	@Override
	public void onUpdateDatapoints(ASUpdateable updateable, String path) {
		OPCUAUpdateNodeIdEvent trigger = (OPCUAUpdateNodeIdEvent) updateable.getTrigger();
		InputStream input = null;
		try {
			String datapointsPath = new Path(path).append("datapoints.com").toOSString();
			if (!updateable.getFilesystem().isFile(datapointsPath)) {
				return;
			}
			input = updateable.getFilesystem().readFile(datapointsPath);
			XMLDADPEditorImporter importer = new XMLDADPEditorImporter();
			List<XML_DA_DPItem> dps = importer.loadDPs(new InputSource(new InputStreamReader(input, "UTF-8")),
					ServerInstance.getInstance().getServerInstance().getNamespaceUris());
			List<IDriverNode> entries = new ArrayList<>();
			for (XML_DA_DPItem item : dps) {
				XMLDAModelNode entry = XMLDAModelNode.loadFromDP(item);
				entries.add(entry);
			}
			XMLDADPExporter exporter = new XMLDADPExporter(updateable.getFilesystem(), datapointsPath);
			exporter.updateDatapoints(entries, trigger,
					ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
