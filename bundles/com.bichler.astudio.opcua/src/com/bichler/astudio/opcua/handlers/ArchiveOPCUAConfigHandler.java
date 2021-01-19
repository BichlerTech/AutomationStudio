package com.bichler.astudio.opcua.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.zip.Com_Box_Archiver;

public class ArchiveOPCUAConfigHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event)
//				.getActivePage();

		FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { ".zip" });

		String saveFilePath = dialog.open();
		if (saveFilePath != null) {
			TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
					.getSelection(NavigationView.ID);

			if (selection.getFirstElement() != null) {
				Com_Box_Archiver archiver = new Com_Box_Archiver();
				IFileSystem fs = ((OPCUAServerModelNode) selection.getFirstElement()).getFilesystem();

//				IPreferenceStore opcuastore = OPCUAActivator.getDefault()
//						.getPreferenceStore();

				String serverPath = new Path(fs.getRootPath()).toOSString();

				archiver.setFilesystem(fs);

				try {
					archiver.zipDir(serverPath, saveFilePath);
					// "/Users/applemc207da/Desktop/"
					// + ((OPCUAServerModelNode) selection
					// .getFirstElement()).getServerName()
					// + ".zip");
				} catch (IOException e2) {
					System.err.println(e2);
				}
			}
		}
		return null;
	}

}
