package com.bichler.astudio.editor.siemens.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Path;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.siemens.xml.SiemensDPEditorImporter;
import com.bichler.astudio.editor.siemens.xml.SiemensDPItem;
import com.bichler.astudio.editor.siemens.xml.SiemensDpExporter;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNodeIdHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUASiemensUpdateNodeIdHandler extends AbstractOPCUAUpdateNodeIdHandler {
	public static final String ID = "command.update.opcua.nodeid.driver.siemens";

	@Override
	public void onUpdateDatapoints(ASUpdateable updateable, String path) {
		// datapoints
		OPCUAUpdateNodeIdEvent trigger = (OPCUAUpdateNodeIdEvent) updateable.getTrigger();
		String datapointsPath = new Path(path).append("datapoints.com").toOSString();
		if (!updateable.getFilesystem().isFile(datapointsPath)) {
			return;
		}
		try (InputStream input = updateable.getFilesystem().readFile(datapointsPath);) {

			SiemensDPEditorImporter importer = new SiemensDPEditorImporter();
			List<SiemensDPItem> dps = importer.loadDPs(input,
					ServerInstance.getInstance().getServerInstance().getNamespaceUris());
			List<IDriverNode> entries = new ArrayList<>();
			for (SiemensDPItem dp : dps) {
				SiemensEntryModelNode entry = SiemensEntryModelNode.loadFromDP(dp);
				entries.add(entry);
			}
			SiemensDpExporter exporter = new SiemensDpExporter(updateable.getFilesystem(), datapointsPath);
			exporter.updateDatapoints(entries, trigger,
					ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
