package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;

import com.bichler.astudio.components.ui.file.filter.XMLFileFilter;
import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;

public class ParametrizedImportModelHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.extern.importmodel";
	public static final String PARAMETER_FILE = "com.xcontrol.modeler.opc.extern.importmodel.files";

	/**
	 * Imports from parent infomodel folder
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String open = event.getParameter(PARAMETER_FILE);
		// open the file
		if (open != null) {
			try {
				// OPCModelDesignerUtil.getMessageConsole().println(
				// "load model file: " + open);
				File infoModelFolder = new Path(open).toFile();
				List<URL> urls = new ArrayList<>();
				XMLFileFilter filter = new XMLFileFilter();
				if (infoModelFolder != null) {
					if (infoModelFolder.isDirectory()) {
						for (File model : infoModelFolder.listFiles(filter)) {
							if (!model.isHidden()) {
								urls.add(model.toURI().toURL());
							}
						}
					} else if (infoModelFolder.isFile()) {
						urls.add(infoModelFolder.toURI().toURL());
					}
				}
				if (!urls.isEmpty()) {
					// import model
					try {
						ServerInstance.importModelFile(null, urls.toArray(new URL[0]));
						// refesh
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								ModelBrowserView part = DesignerUtils.refreshBrowserAll();
								part.setDirty(true);
							}
						});
					} catch (ASStudioLicenseException e) {
						// prevents to open new model file
						e.printStackTrace();
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
