package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.opcua.OpenServerParameter;

import opc.sdk.server.core.UAServerApplicationInstance;

public class StartOPCServerExternHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.opcua.modeler.startServer";
	// public static final String PARAMETER_SERVER_NAME =
	// "com.bichler.astudio.opcua.modeler.startServer.servername";
	// public static final String PARAMETER_SERVER_CONFIG_PATH =
	// "com.bichler.astudio.opcua.modeler.startServer.config";
	// public static final String PARAMETER_SERVER_CERTIFICATES =
	// "com.bichler.astudio.opcua.modeler.startServer.certificate";
	// public static final String PARAMETER_SERVER_MODELS =
	// "com.bichler.astudio.opcua.modeler.startServer.model";
	// public static final String PARAMETER_SERVER_LOCALIZATION =
	// "com.bichler.astudio.opcua.modeler.startServer.localization";
	// public static final String PARAMETER_DESIGNER_REFRESHVIEW =
	// "com.bichler.astudio.opcua.modeler.startServer.view";

	// public static final String PARAMETER_SERVER_INFOMODELLS = "";
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OpenServerParameter trigger = (OpenServerParameter) evalCxt.getVariable(OpenServerParameter.PARAMETER_ID);
		// (OpenServerParameter)
		String servername = trigger.getServername();// event.getParameter(PARAMETER_SERVER_NAME);
		String config = trigger.getConfiguration(); // event.getParameter(PARAMETER_SERVER_CONFIG_PATH);
		String certs = trigger.getCertificates();// event.getParameter(PARAMETER_SERVER_CERTIFICATES);
		String infoModel = trigger.getModel();// event.getParameter(PARAMETER_SERVER_MODELS);
		String localization = trigger.getLocalization(); // event.getParameter(PARAMETER_SERVER_LOCALIZATION);
		// if (servername == null) {
		// try {
		// IParameter pServername =
		// event.getCommand().getParameter(PARAMETER_SERVER_NAME);
		// System.out.println();
		// } catch (NotDefinedException e) {
		// e.printStackTrace();
		// }
		//
		// }
		URL[] urls = null;
		if (infoModel != null) {
			String[] pUrls = infoModel.split(";");
			urls = new URL[pUrls.length];
			for (int i = 0; i < pUrls.length; i++) {
				try {
					urls[i] = new Path(pUrls[i]).toFile().toURI().toURL();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		UAServerApplicationInstance server = (UAServerApplicationInstance) Studio_ResourceManager
				.getOPCUAServerInstance(servername);
		// return existing instance
		try {
			if (server == null) {
				UAServerApplicationInstance instance = ServerInstance.startServer(servername, config, certs,
						localization, urls);
				if (instance != null) {
					Studio_ResourceManager.addOPCUAServer(servername, instance);
					refresh();
				}
			} else {
				ServerInstance.startServer(server);
				refresh();
			}
		} catch (ASStudioLicenseException e) {
			throw new ExecutionException(e.getMessage(), e);
		}
		// if(window == null){
		// // TODO: BUG WHEN OPENING EDITORS
		// return server;
		// }
		return server;
	}

	private void refresh() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				// IWorkbenchWindow window = HandlerUtil
				// .getActiveWorkbenchWindow(event);
				IWorkbenchPage page = window.getActivePage();
				final ModelBrowserView mbv = (ModelBrowserView) page
						.findView("com.hbsoft.designer.views.modeldesignbrowser.modelbrowserview");
				if (mbv != null) {
					mbv.startView();
				}
			}
		});
	}
}
