package com.bichler.astudio.opcua.opcmodeler.commands.handler.opc;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ImportXMLModelFileHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		String[] filterExt = new String[] { "*.xml" };
		String[] filterNames = new String[] { "XML Files" };
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterNames(filterNames);
		fileDialog.setFilterExtensions(filterExt);
		fileDialog.setFilterPath(File.listRoots()[0].getPath());
		String open = fileDialog.open();
		// open the file
		if (open != null) {
			try {
				Logger.getLogger(getClass().getName()).info(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.load.modelfile")
								+ " " + open);
				ServerInstance.importModelFile(null, new Path(open).toFile().toURI().toURL());
				DesignerUtils.refreshBrowserAll();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ASStudioLicenseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
