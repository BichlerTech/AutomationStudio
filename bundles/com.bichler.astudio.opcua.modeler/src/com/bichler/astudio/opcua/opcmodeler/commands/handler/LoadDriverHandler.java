package com.bichler.astudio.opcua.opcmodeler.commands.handler;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.bichler.astudio.opcua.drivers.views.DriversView;
import com.bichler.astudio.opcua.opcmodeler.Activator;

public class LoadDriverHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		BundleContext context = Activator.getDefault().getBundle().getBundleContext();
		String[] filterExt = new String[] { "*.jar" };
		String[] filterNames = new String[] { "Osgi Bundles" };
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				SWT.OPEN);
		fileDialog.setFilterNames(filterNames);
		fileDialog.setFilterExtensions(filterExt);
		fileDialog.setFilterPath(File.listRoots()[0].getPath());
		String open = fileDialog.open();
		// open the file
		if (open != null) {
			try {
				Bundle bundle = context.installBundle("file:" + open);
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window != null) {
					MenuManager manager = ((WorkbenchWindow) window).getMenuManager();
					/** create a new driver item to start menu */
					MenuManager startitem = (MenuManager) ((MenuManager) manager.find("drivers")).find("startdriver");
					CommandContributionItemParameter startparam = new CommandContributionItemParameter(window, null,
							"commands.designer.startdriver", SWT.PUSH);
					startparam.label = bundle.getSymbolicName() + " " + bundle.getVersion();
					startparam.icon = AbstractUIPlugin.imageDescriptorFromPlugin("OPC_Designer_0_2",
							"icons/default_icons/run_24.png");
					StartCommandContributionItem newstartitem = new StartCommandContributionItem(startparam);
					newstartitem.setBundle(bundle);
					startitem.add(newstartitem);
					/** create a new driver item to stop menu */
					MenuManager stopitem = (MenuManager) ((MenuManager) manager.find("drivers")).find("stopdriver");
					CommandContributionItemParameter stopparam = new CommandContributionItemParameter(window, null,
							"commands.designer.stopdriver", SWT.PUSH);
					stopparam.label = bundle.getSymbolicName() + " " + bundle.getVersion();
					stopparam.icon = AbstractUIPlugin.imageDescriptorFromPlugin("OPC_Designer_0_2",
							"icons/default_icons/stop_24.png");
					StopCommandContributionItem newstopitem = new StopCommandContributionItem(stopparam);
					newstopitem.setBundle(bundle);
					stopitem.add(newstopitem);
					DriversView view = (DriversView) window.getActivePage().findView(DriversView.ID);
					view.addDriver(bundle);
				}
			} catch (BundleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
