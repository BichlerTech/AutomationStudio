package com.bichler.astudio.opcua.handlers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.editor.OPCUAServerVersionEditor;
import com.bichler.astudio.opcua.editor.input.OPCUAVersionPathEditorInput;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerVersionModelNode;

public class OpenOPCUAServerVersionHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.commands.openopcuaserverversion";

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
		OPCUAServerVersionModelNode selectedNode = (OPCUAServerVersionModelNode) selection.getFirstElement();
		try {
			String filepath = new Path(selectedNode.getFilesystem().getRootPath()).append("versions")
					.append(selectedNode.getVersionName()).toOSString();
			File file = new File(filepath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
			IPath location = new Path(filepath);
			final OPCUAVersionPathEditorInput input = new OPCUAVersionPathEditorInput(location);
			input.setProjectName(selectedNode.getServerName());
			input.setVersionName(selectedNode.getVersionName());
			page.openEditor(input, OPCUAServerVersionEditor.ID);
		} catch (PartInitException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return null;
	}
}
