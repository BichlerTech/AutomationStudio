package com.bichler.astudio.editor.siemens.handlers;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.siemens.xml.SiemensDpExporter;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class SiemensDpWriterHandler extends AbstractOPCUADPWriterHandler {
	public static final String ID = "com.bichler.astudio.drv.export.siemens";

	@Override
	protected void write(IFileSystem fs, String path, List<Object> datapoints) {
		SiemensDpExporter exporter = new SiemensDpExporter(fs, path);
		List<IDriverNode> dps = new ArrayList<>();
		for (Object obj : datapoints) {
			dps.add((IDriverNode) obj);
		}
		exporter.build(dps, ServerInstance.getInstance().getServerInstance().getNamespaceUris());

	}
}
