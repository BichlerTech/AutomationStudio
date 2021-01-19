package com.bichler.astudio.editor.allenbradley.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Path;
import org.xml.sax.InputSource;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPEditorImporter;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPItem;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDpExporter;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUAAllenBradleyUpdateNamespaceTableHandler extends AbstractOPCUAUpdateNamespaceHandler {
	public static final String ID = "command.update.opcua.namespacetable.driver.allenbradley";

	@Override
	public void onUpdateDatapoints(ASUpdateable updateable, String path) {
		NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) updateable.getTrigger();
		String datapointsPath = new Path(path).append("datapoints.com").toOSString();
		if (!updateable.getFilesystem().isFile(datapointsPath)) {
			return;
		}

		try (InputStream input = updateable.getFilesystem().readFile(datapointsPath);) {

			InputSource isrc = new InputSource(new InputStreamReader(input, "UTF-8"));
			AllenBradleyDPEditorImporter importer = new AllenBradleyDPEditorImporter();
			List<AllenBradleyDPItem> dps = importer.loadDPs(isrc, trigger.getOriginNamespaceTable());
			List<IDriverNode> entries = new ArrayList<>();
			for (AllenBradleyDPItem dp : dps) {
				AllenBradleyEntryModelNode entry = AllenBradleyEntryModelNode.loadFromDP(dp);
				entries.add(entry);
			}
			AllenBradleyDpExporter exporter = new AllenBradleyDpExporter(updateable.getFilesystem(), datapointsPath);
			exporter.updateDatapoints(entries, trigger);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
