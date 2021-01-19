package com.bichler.astudio.opcua.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.zip.Com_Box_Archiver;

public class DeArchiveOPCUAConfigHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
				.getSelection(NavigationView.ID);

		if (selection.getFirstElement() != null) {

			Com_Box_Archiver archiver = new Com_Box_Archiver();
			IFileSystem fs = ((OPCUAServerModelNode) selection.getFirstElement()).getFilesystem();

			IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();

			String serverPath = fs.getRootPath() + fs.getTargetFileSeparator()
					+ opcuastore.getString(OPCUAConstants.ASOPCUAServersPath);

			try {
				archiver.unZipArchive(
						"/Users/applemc207da/Desktop/"
								+ ((OPCUAServerModelNode) selection.getFirstElement()).getServerName() + ".zip",
						serverPath);
			} catch (IOException e2) {
				System.err.println(e2);
			}

		}
		return null;
	}

}
