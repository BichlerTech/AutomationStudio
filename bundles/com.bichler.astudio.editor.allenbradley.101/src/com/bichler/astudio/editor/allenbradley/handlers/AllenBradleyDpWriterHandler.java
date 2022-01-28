package com.bichler.astudio.editor.allenbradley.handlers;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDpExporter;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class AllenBradleyDpWriterHandler extends AbstractOPCUADPWriterHandler {
	public static final String ID = "com.bichler.astudio.drv.export.allenbradley";

	@Override
	protected void write(IFileSystem fs, String path, List<Object> datapoints) {
		AllenBradleyDpExporter exporter = new AllenBradleyDpExporter(fs, path);
		List<IDriverNode> dps = new ArrayList<>();
		for (Object obj : datapoints) {
			dps.add((IDriverNode) obj);
		}
		// build up model
		exporter.build(dps, ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		// write buffer to file
//    exporter.write(out, buffer);
	}
}
