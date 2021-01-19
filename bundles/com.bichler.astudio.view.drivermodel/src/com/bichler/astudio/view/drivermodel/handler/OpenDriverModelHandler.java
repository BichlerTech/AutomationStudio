package com.bichler.astudio.view.drivermodel.handler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.OPCDriverUtil;
import com.bichler.astudio.opcua.driver.enums.DriverConfigProperties;
import com.bichler.astudio.opcua.driver.enums.DriverModelExtensions;
import com.bichler.astudio.view.drivermodel.browser.DriverModelBrowserView;

public class OpenDriverModelHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.view.drivermodel.openDrivermodelView";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}

		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}

		DriverModelBrowserView view = (DriverModelBrowserView) page
				.findView(DriverModelBrowserView.ID);
		if (view == null) {
			return null;
		}

		String driverPath = view.getDriverPath();
		IFileSystem filesystem = view.getFilesystem();
		Map<String, String> attributes = OPCDriverUtil.readDriverCom(
				filesystem, driverPath);

		String type = attributes.get(DriverConfigProperties.drivertype.name());
		String drvModel = attributes.get(DriverConfigProperties.pathdrivermodel
				.name());

		String[] exts = DriverModelExtensions.getDriverModelExtensions(type);

		FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event));
		dialog.setFilterExtensions(exts);
		dialog.setFilterPath((drvModel == null) ? "" : drvModel);

		String path = dialog.open();
		if (path != null) {
			Map<String, String> attributes2change = new HashMap<>();
			attributes2change.put(
					DriverConfigProperties.pathdrivermodel.name(), path);
			OPCDriverUtil.writeDriverCom(filesystem, driverPath,
					attributes2change);

			view.refresh(DriverConfigProperties.pathdrivermodel, path);
		}

		return null;
	}

}
