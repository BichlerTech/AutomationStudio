package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import java.io.File;

import opc.sdk.core.language.LanguageItem;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.language.LanguageCSVWriter;

public class ParametrizedExportLanguageHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.extern.exportlanguage";
	public static final String PARAMETER_FILE = "com.xcontrol.modeler.opc.extern.exportlang.files";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String param = event.getParameter(PARAMETER_FILE);
		if (param != null) {
			File file = new File(param);
			if (file != null && file.exists()) {
				LanguageItem[] items = ServerInstance.getInstance().getServerInstance().getLanguageManager()
						.getLanguageInformation();
				LanguageCSVWriter parser = new LanguageCSVWriter();
				parser.write(file, items);
			}
		}
		return null;
	}
}
