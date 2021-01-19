package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import com.bichler.astudio.opcua.addressspace.xml.importer.NameSpaceNotFountException;
import com.bichler.astudio.opcua.addressspace.xml.importer.NodesetImporter;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;

import opc.server.hbserver.OPCUADriverConnection;
import opc.server.hbserver.OPCUADriverServer;

public class ParametrizedImportModelHandler2 extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.extern.importmodel2";
	public static final String PARAMETER_FILE = "com.xcontrol.modeler.opc.extern.importmodel2.files";

	/**
	 * Imports from parent infomodel folder
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String open = event.getParameter(PARAMETER_FILE);
		if (open != null) {
			if (((OPCUADriverServer) ServerInstance.getInstance()).getDriverConnection() == null) {
				OPCUADriverConnection driverConnection = new OPCUADriverConnection(
						ServerInstance.getInstance().getServerInstance());
				((OPCUADriverServer) ServerInstance.getInstance()).setDriverConnection(driverConnection);
			}
			NodesetImporter importer = new NodesetImporter();
			try {
				importer.importNodeSet((OPCUADriverServer) ServerInstance.getInstance(), open);
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						ModelBrowserView part = DesignerUtils.refreshBrowserAll();
						part.setDirty(true);
					}
				});
			} catch (NameSpaceNotFountException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
				// ex.printStackTrace();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				// e.printStackTrace();
			}
		}
		return null;
	}
}
