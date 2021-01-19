package com.bichler.astudio.editor.xml_da.handlers;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.xml_da.xml.XMLDADPExporter;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class XML_DA_DpWriterHandler extends AbstractOPCUADPWriterHandler {
	public static final String ID = "com.hbsoft.comet.drv.export.xmlda";

	@Override
	protected void write(IFileSystem fs, String path, List<Object> datapoints) {
		XMLDADPExporter exporter = new XMLDADPExporter(fs, path);
		List<IDriverNode> dps = new ArrayList<>();
		for (Object obj : datapoints) {
			dps.add((IDriverNode) obj);
		}
		exporter.build(dps, ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		// exporter.write(out, buffer);
	}
}
