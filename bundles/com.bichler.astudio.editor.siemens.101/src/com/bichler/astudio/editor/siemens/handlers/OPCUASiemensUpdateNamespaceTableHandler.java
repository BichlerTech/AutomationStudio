package com.bichler.astudio.editor.siemens.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.siemens.xml.SiemensDPEditorImporter;
import com.bichler.astudio.editor.siemens.xml.SiemensDPItem;
import com.bichler.astudio.editor.siemens.xml.SiemensDpExporter;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUASiemensUpdateNamespaceTableHandler extends AbstractOPCUAUpdateNamespaceHandler {
	public static final String ID = "com.bichler.astudio.editor.siemens.1.0.1.update.namespacetable";

	@Override
	public void onUpdateDatapoints(ASUpdateable updateable, String path) {
		// datapoints
		NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) updateable.getTrigger();
		InputStream input = null;
		try {
			String datapointsPath = new Path(path).append("datapoints.com").toOSString();
			if (!updateable.getFilesystem().isFile(datapointsPath)) {
				return;
			}
			input = updateable.getFilesystem().readFile(datapointsPath);
			SiemensDPEditorImporter importer = new SiemensDPEditorImporter();
			List<SiemensDPItem> dps = importer.loadDPs(input, trigger.getOriginNamespaceTable());
			List<IDriverNode> entries = new ArrayList<>();
			for (SiemensDPItem dp : dps) {
				SiemensEntryModelNode entry = SiemensEntryModelNode.loadFromDP(dp);
				entries.add(entry);
			}
			SiemensDpExporter exporter = new SiemensDpExporter(updateable.getFilesystem(), datapointsPath);
			exporter.updateDatapoints(entries, trigger);
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
