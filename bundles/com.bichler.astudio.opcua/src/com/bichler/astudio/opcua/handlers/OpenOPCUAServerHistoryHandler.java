package com.bichler.astudio.opcua.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.editor.OPCUAServerHistoryEditor;
import com.bichler.astudio.opcua.editor.input.OPCUAHistoryPathEditorInput;
import com.bichler.astudio.opcua.nodes.OPCUAServerHistoryModelNode;

public class OpenOPCUAServerHistoryHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.commands.openopcuaserverhistory";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page == null) {
			return null;
		}
		TreeSelection selection = (TreeSelection) page.getSelection(OPCNavigationView.ID);
		if (selection == null) {
			return null;
		}
		OPCUAServerHistoryModelNode selectedNode = (OPCUAServerHistoryModelNode) selection.getFirstElement();
		// if(event.getTrigger() instanceof CometOPCUAEcmaScriptOpenEvent) {
		try {
			String filepath = new Path(selectedNode.getFilesystem().getRootPath()).append("history.txt").toOSString();
			// if(node.get)
			File file = new File(filepath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// CometLocaleFile locale = new CometLocaleFile(file);
			IPath location = new Path(filepath);
			final OPCUAHistoryPathEditorInput input = new OPCUAHistoryPathEditorInput(location);
			input.setProjectName(selectedNode.getServerName());
			// final IFileStore externalFile =
			// EFS.getLocalFileSystem().fromLocalFile(locale.getFile());
			// FileStoreEditorInput input = new FileStoreEditorInput(externalFile);
			// OPCUAServerHistoryEditorInput input = new OPCUAServerHistoryEditorInput();
			// input.setNode(selectedNode);
			// input.init();
			page.openEditor(input, OPCUAServerHistoryEditor.ID);
			// ((CompilationUnitEditor)editor).setInput(input);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
		return null;
	}
}
