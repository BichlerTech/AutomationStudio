package com.bichler.astudio.editor.aggregated.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.core.runtime.Path;

import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpExporter;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNodeIdHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUAAggregatedUpdateNodeIdHandler extends AbstractOPCUAUpdateNodeIdHandler {

	public static final String ID = "command.update.opcua.nodeid.driver.aggregated";

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

			AggregatedDpExporter exporter = new AggregatedDpExporter(updateable.getFilesystem(), datapointsPath);
			exporter.updateDatapoints(new ArrayList<IDriverNode>(), trigger,
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
