package com.bichler.astudio.opcua.handlers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.licensemanagement.util.LicenseUtil;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.IFileSystemNavigator;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.wizard.NewOPCUAServerEcmaScriptWizard;

public class AddOPCUAServerEcmaScriptHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.commands.addopcuaserverecmascript";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		/**
		 * first test license
		 */
		try {
			LicManActivator.getDefault().getLicenseManager().getLicense().validateAddEcmaScript(1, true);
			IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
			TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
			NewOPCUAServerEcmaScriptWizard wizard = new NewOPCUAServerEcmaScriptWizard();
			if ((selection instanceof IStructuredSelection) || (selection == null))
				wizard.init(page.getWorkbenchWindow().getWorkbench(), (IStructuredSelection) selection);
			IFileSystemNavigator view = (IFileSystemNavigator) page.findView(NavigationView.ID);
			if (view == null) {
				view = (IFileSystemNavigator) page.findView(OPCNavigationView.ID);
			}
			// Instantiates the wizard container with the wizard and opens it
			WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				String scriptname = wizard.getScriptName();
				int scripttype = wizard.getScriptType();
				int interval = wizard.getInterval();
				Object element = selection.getFirstElement();
				Object updatenode = (StudioModelNode) element;

				IFileSystem filesystem = ((StudioModelNode) element).getFilesystem();
				/** open scripts file */
				// IPreferenceStore store =
				OPCUAActivator.getDefault().getPreferenceStore();
				String config = new Path(filesystem.getRootPath()).append("ecmascripts").append("start.conf")
						.toOSString();
				List<String> singlelines = new ArrayList<String>();
				List<String> intervlines = new ArrayList<String>();
				List<String> actlist = singlelines;
				if (filesystem.isFile(config)) {
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new FileReader(config));
						String line = "";
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("#interval")) {
								actlist = intervlines;
							}
							if (line.trim().isEmpty()) {
								continue;
							} else {
								actlist.add(line);
							}
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				/** if the ingle array is empty so we add description string */
				if (singlelines.isEmpty()) {
					singlelines.add("#single");
				}
				/** if the interval array is empty so we add description string */
				if (intervlines.isEmpty()) {
					intervlines.add("#interval");
				}
				if (scripttype == 0) {
					singlelines.add("1:" + scriptname);
				} else {
					intervlines.add("##" + interval);
					intervlines.add("1:" + scriptname);
				}
				OutputStreamWriter writer = null;
				try {
					if (!filesystem.isFile(config)) {
						filesystem.addFile(config);
					}
					writer = new OutputStreamWriter(filesystem.writeFile(config));
					for (String s : singlelines) {
						writer.write(s + "\n");
					}
					for (String i : intervlines) {
						writer.write(i + "\n");
					}
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				view.refresh((StudioModelNode) updatenode);
				// view.refresh((StudioModelNode) updatenode);
			}
		} catch (ASStudioLicenseException e) {
			LicenseUtil.openLicenseErrorStartup("License Manager", "No valid license for Scripting plugin found!",
					"The Scripting Plugin is an extension to feature your opc ua server with additional javascripts. ");
		}
		return null;
	}
}
