package com.bichler.astudio.editor.calculation.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;

import com.bichler.opc.driver.calculation.CalculationDP;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.calculation.model.CalculationDPEditorImporter;
import com.bichler.astudio.editor.calculation.model.CalculationModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNamespaceHandler;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUACalculationUpdateNamespaceTableHandler extends
		AbstractOPCUAUpdateNamespaceHandler {

	public static final String ID = "command.update.opcua.namespacetable.driver.calculation";

	@Override
	public void onUpdateDatapoints(ASUpdateable updateable, String path) {
		NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) updateable
				.getTrigger();

		InputStream input = null;
		try {
			String datapointsPath = new Path(path).append("datapoints.com")
					.toOSString();

			if (!updateable.getFilesystem().isFile(datapointsPath)) {
				return;
			}
			input = updateable.getFilesystem().readFile(datapointsPath);
			CalculationDPEditorImporter importer = new CalculationDPEditorImporter();
			List<CalculationDP> dps = importer.loadDPs(
					input, trigger.getOriginNamespaceTable(), null, -1);

			List<IDriverNode> driverNodes = new ArrayList<>();

			for (CalculationDP dp : dps) {
				CalculationModelNode item = new CalculationModelNode();
				item.setDP(dp);
				driverNodes.add(item);
			}
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

	@Override
	public void onUpdateAdvancedSettings(ASUpdateable updateable, String path) {

	}

	@Override
	public void onUpdateSettings(ASUpdateable updateable, String path) {

	}
}
