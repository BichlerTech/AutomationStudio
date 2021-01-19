package com.bichler.astudio.opcua.handlers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.IFileSystemNavigator;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerVersionsModelNode;

public class AddOPCUAServerVersionHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// test if version dir exists
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page == null) {
			return null;
		}
		TreeSelection selection = (TreeSelection) page.getSelection(OPCNavigationView.ID);
		if (selection == null) {
			return null;
		}
		OPCUAServerVersionsModelNode selectedNode = (OPCUAServerVersionsModelNode) selection.getFirstElement();
		String filepath = new Path(selectedNode.getFilesystem().getRootPath()).append("versions").toOSString();
		if (!selectedNode.getFilesystem().isDir(filepath)) {
			try {
				selectedNode.getFilesystem().addDir(filepath);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		try {
			// find all existing files
			String[] versions = selectedNode.getFilesystem().listFiles(filepath);
			int versionid = 0;
			if (versions != null) {
				for (String version : versions) {
					try {
						versionid = Integer.parseInt(version.replace("Version", ""));
					} catch (NumberFormatException ex) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
					}
				}
			}
			// increment versionid
			versionid++;
			// now create new version file
			filepath = new Path(selectedNode.getFilesystem().getRootPath()).append("versions")
					.append("Version" + versionid).toOSString();
			selectedNode.getFilesystem().addFile(filepath);
			IFileSystemNavigator view = (IFileSystemNavigator) page.findView(OPCNavigationView.ID);
			view.refresh((StudioModelNode) selectedNode);
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
		}
		return null;
	}
}
