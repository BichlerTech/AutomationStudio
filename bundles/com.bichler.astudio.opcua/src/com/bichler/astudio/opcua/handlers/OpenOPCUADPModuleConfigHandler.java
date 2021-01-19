package com.bichler.astudio.opcua.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.editor.input.OPCUAModuleDPEditorInput;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModuleDPsModelNode;

public class OpenOPCUADPModuleConfigHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		TreeSelection selection = (TreeSelection) page.getSelection(OPCNavigationView.ID);
		if (selection == null) {
			return null;
		}
		if (selection.isEmpty()) {
			return null;
		}
		Object selectedNode = selection.getFirstElement();
		if (selectedNode instanceof OPCUAServerModuleDPsModelNode) {
			OPCUAServerModuleDPsModelNode dps = (OPCUAServerModuleDPsModelNode) selectedNode;
			final OPCUAModuleDPEditorInput input = new OPCUAModuleDPEditorInput();
			IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
			Path serverPath = new Path(dps.getFilesystem().getRootPath());
			// drivers/xxxdriver
			IPath driverFolderPath = serverPath.append(opcuastore.getString(OPCUAConstants.ASOPCUAModulesFolder))
					.append(dps.getModuleName());
			String driverConfigPath = null;
			input.setNode(dps);
			input.setServerName(dps.getServerName());
			input.setDriverName(dps.getModuleName());
			input.setFilesystem(dps.getFilesystem());
			input.setDriverConfigFile(driverFolderPath.append("driver.com").toOSString());
			input.setDPConfigFile(driverFolderPath.append("datapoints.com").toOSString());
			input.setDriverPath(driverFolderPath.toOSString());
			input.setServerRuntimePath(driverConfigPath);
			// load dp editor id from driver config
			String editorId = "com.bichler.astudio.editor." + dps.getModuleType() + "." + dps.getModuleVersion() + "."
					+ dps.getEditorName();
			try {
				page.openEditor(input, editorId);
			} catch (PartInitException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return null;
	}
}
