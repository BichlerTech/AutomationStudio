package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import java.io.File;

import opc.sdk.core.language.LanguageItem;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.language.LanguageCSVReader;

public class ImportLanguageHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.importlanguage";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(new String[] { "*.csv" });
		String path = fileDialog.open();
		if (path != null) {
			File file = new File(path);
			if (file != null && file.exists()) {
				LanguageCSVReader parser = new LanguageCSVReader();
				LanguageItem[] items2add = parser.read(file);
				ServerInstance.getInstance().getServerInstance().importLanguage(items2add);
			}
		}
		return null;
	}
}
