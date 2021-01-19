package com.bichler.astudio.view.drivermodel.handler.util;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.enums.DriverConfigProperties;
import com.bichler.astudio.view.drivermodel.browser.DriverModelBrowserView;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelDragConverter;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelListener;

public class DriverBrowserUtil {
	private DriverBrowserUtil() {
	}

	public static void openEmptyDriverModelView() {
		DriverModelBrowserView view = openDriverModelView();
		if (view == null) {
			return;
		}
		view.updateViewer();
	}

	/**
	 * Open driver view.
	 * 
	 * @param contentProvider
	 * @param labelProvider
	 * @param filesystem
	 * @param driverpath
	 * @param listeners
	 */
	public static void openDriverModelView(IContentProvider contentProvider, IBaseLabelProvider labelProvider,
			IFileSystem filesystem, String driverpath, Map<DriverConfigProperties, IDriverModelListener> listeners,
			IDriverModelDragConverter converter, IDriverStructResourceManager structureManager) {
		DriverModelBrowserView view = openDriverModelView();
		if (view == null) {
			return;
		}
		view.updateViewer(contentProvider, labelProvider, driverpath, filesystem, listeners, converter,
				structureManager);
	}

	private static DriverModelBrowserView openDriverModelView() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		DriverModelBrowserView view = null;
		try {
			// if view is visible
			view = (DriverModelBrowserView) page.findView(DriverModelBrowserView.ID);
			// otherwise open view
			if (view == null) {
				view = (DriverModelBrowserView) page.showView(DriverModelBrowserView.ID);
			}
		} catch (PartInitException e) {
			Logger.getLogger(DriverBrowserUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return view;
	}

	/**
	 * Set input to driver model viewer
	 * 
	 * @param input
	 */
	public static void updateDriverModelView(Object input) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		DriverModelBrowserView view = (DriverModelBrowserView) page.findView(DriverModelBrowserView.ID);
		if (view != null) {
			view.setViewerInput(input);
		}
	}
}
