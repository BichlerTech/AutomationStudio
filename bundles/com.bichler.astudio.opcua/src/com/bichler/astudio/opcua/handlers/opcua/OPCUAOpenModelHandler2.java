package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;

public class OPCUAOpenModelHandler2 extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.opcserver.open";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		// if we have an old nodeset
		FileDialog dialog = new FileDialog(window.getShell());
		dialog.setFilterExtensions(DesignerConstants.EXTENSION_INFORMATIONMODEL);
		String path = dialog.open();
		if (path != null) {
			OPCUAUtil.openModel2(window, path);

//      OPCUADriverServer opcServer = new OPCUADriverServer();
//      if (((OPCUADriverServer) ServerInstance.getInstance()).getDriverConnection() == null)
//      {
//        OPCUADriverConnection driverConnection = new OPCUADriverConnection(
//            ServerInstance.getInstance().getServerInstance());
//        ((OPCUADriverServer) ServerInstance.getInstance()).setDriverConnection(driverConnection);
//      }
//      NodesetImporter importer = new NodesetImporter();
//      importer.importNodeSet((OPCUADriverServer) ServerInstance.getInstance(), path);
		}
		return null;
	}
}
