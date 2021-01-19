package com.bichler.astudio.editor.aggregated.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Path;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpExporter;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUAAggregatedUpdateNamespaceTableHandler extends AbstractOPCUAUpdateNamespaceHandler {

	public static final String ID = "command.update.opcua.namespacetable.driver.aggregated";

	@Override
	public void onUpdateDatapoints(ASUpdateable updateable, String path) {
		NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) updateable.getTrigger();

		InputStream input = null;
		try {
			String datapointsPath = new Path(path).append("datapoints.com").toOSString();

			if (!updateable.getFilesystem().isFile(datapointsPath)) {
				return;
			}
			input = updateable.getFilesystem().readFile(datapointsPath);

			AggregatedDpExporter exporter = new AggregatedDpExporter(updateable.getFilesystem(), datapointsPath);
			exporter.updateDatapoints(new ArrayList<IDriverNode>(), trigger);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}

	@Override
	public void onUpdateAdvancedSettings(ASUpdateable updateable, String path) {
		// nothing to update
	}

	@Override
	public void onUpdateSettings(ASUpdateable updateable, String path) {
		// nothing to update
	}

}
