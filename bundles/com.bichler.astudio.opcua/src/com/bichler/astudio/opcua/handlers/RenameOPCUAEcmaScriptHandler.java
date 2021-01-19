package com.bichler.astudio.opcua.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaScriptModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServersModelNode;
import com.bichler.astudio.opcua.wizard.RenameOPCUAServerEcmaScriptWizard;

public class RenameOPCUAEcmaScriptHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
				.getSelection(NavigationView.ID);

		RenameOPCUAServerEcmaScriptWizard wizard = new RenameOPCUAServerEcmaScriptWizard();
		if ((selection instanceof IStructuredSelection) || (selection == null))
			wizard.init(page.getWorkbenchWindow().getWorkbench(), (IStructuredSelection) selection);

		Object element = selection.getFirstElement();
		String scriptName = "";
		String serverName = "";

		if (element instanceof OPCUAServerEcmaScriptModelNode) {
			scriptName = ((OPCUAServerEcmaScriptModelNode) element).getScriptName();

			serverName = ((OPCUAServerEcmaScriptModelNode) element).getServerName();
		}
		wizard.setScriptName(scriptName);

		NavigationView view = (NavigationView) page.findView(NavigationView.ID);

		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
		dialog.create();

		if (dialog.open() == Dialog.OK) {
			IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
			String newScriptName = wizard.getScriptName();
			IFileSystem filesystem = ((OPCUAServersModelNode) element).getFilesystem();

			if (element instanceof OPCUAServerEcmaScriptModelNode) {
				((OPCUAServerEcmaScriptModelNode) element).setScriptName(newScriptName);
			}

			/** open scripts file */
			String config = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
					+ store.getString(OPCUAConstants.ASOPCUAServersPath) + filesystem.getTargetFileSeparator()
					+ serverName + filesystem.getTargetFileSeparator() + "ecmascripts"
					+ filesystem.getTargetFileSeparator() + "start.conf";

			if (filesystem.isFile(config)) {
				BufferedReader reader;
				try {

					InputStream stream = filesystem.readFile(config);

					reader = new BufferedReader(new InputStreamReader(stream));
					String line = "";
					List<String> lines = new ArrayList<String>();
					while ((line = reader.readLine()) != null) {

						if (line.compareTo(scriptName + ".es") == 0) {
							lines.add(newScriptName + ".es");
						} else {
							lines.add(line);
						}
					}

					reader.close();

					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(filesystem.writeFile(config)));

					for (String i : lines) {
						writer.write(i + "\n");
					}

					writer.flush();
					writer.close();

					// now rename file
					// File (or directory) with old name
					String file = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
							+ store.getString(OPCUAConstants.ASOPCUAServersPath) + filesystem.getTargetFileSeparator()
							+ serverName + filesystem.getTargetFileSeparator() + "ecmascripts"
							+ filesystem.getTargetFileSeparator() + scriptName + ".es";

					// File (or directory) with new name
					String file2 = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
							+ store.getString(OPCUAConstants.ASOPCUAServersPath) + filesystem.getTargetFileSeparator()
							+ serverName + filesystem.getTargetFileSeparator() + "ecmascripts"
							+ filesystem.getTargetFileSeparator() + newScriptName + ".es";

					// Rename file (or directory)

					boolean success = filesystem.renameFile(file, file2);
					if (!success) {
						// File was not successfully renamed
					}
				} catch (FileNotFoundException e) {

				} catch (IOException e) {

				}
			}
			view.refresh((StudioModelNode) element);
		}
		return null;
	}

}
