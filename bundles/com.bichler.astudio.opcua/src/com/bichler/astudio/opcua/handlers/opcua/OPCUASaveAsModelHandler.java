package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;

public class OPCUASaveAsModelHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcserver.saveas";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.SAVE);
		dialog.setFilterExtensions(DesignerConstants.EXTENSION_INFORMATIONMODEL);
		String path = dialog.open();

		if (path != null) {
			return OPCUAUtil.saveModel(window, path);

//			OPCUAUtil.saveAsModel(window, path);
		}
		return null;
	}

}
