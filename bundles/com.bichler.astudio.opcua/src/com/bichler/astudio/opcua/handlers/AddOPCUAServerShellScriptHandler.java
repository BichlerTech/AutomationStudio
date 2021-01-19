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

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.IFileSystemNavigator;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.wizard.NewOPCUAServerEcmaScriptWizard;
import com.bichler.astudio.opcua.wizard.NewOPCUAServerShellScriptWizard;

public class AddOPCUAServerShellScriptHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.commands.addopcuaservershellscript";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		NewOPCUAServerShellScriptWizard wizard = new NewOPCUAServerShellScriptWizard();
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
			String scriptname = wizard.getScriptname();
			int scripttype = wizard.getScripttype();
			Object element = selection.getFirstElement();
			StudioModelNode updatenode = (StudioModelNode) element;
			IFileSystem filesystem = ((StudioModelNode) element).getFilesystem();
			/** open scripts file */
			String config = new Path(filesystem.getRootPath()).append("shellscripts").append("start.conf").toOSString();
			List<String> prelines = new ArrayList<>();
			List<String> postlines = new ArrayList<>();
			List<String> actlist = prelines;
			if (filesystem.isFile(config)) {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(config));
					String line = "";
					while ((line = reader.readLine()) != null) {
						if (line.startsWith("#postsection")) {
							actlist = postlines;
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
			if (prelines.isEmpty()) {
				prelines.add("#presection");
			}
			/** if the interval array is empty so we add description string */
			if (postlines.isEmpty()) {
				postlines.add("#postsection");
			}
			if (scripttype == 0) {
				prelines.add(scriptname + ".es");
			} else {
				// postlines.add("##" + interval);
				postlines.add(scriptname + ".es");
			}
			OutputStreamWriter writer = null;
			try {
				if (!filesystem.isFile(config)) {
					filesystem.addFile(config);
				}
				writer = new OutputStreamWriter(filesystem.writeFile(config));
				for (String s : prelines) {
					writer.write(s + "\n");
				}
				for (String i : postlines) {
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
		return null;
	}
}
