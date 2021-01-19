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
import com.bichler.astudio.opcua.editor.input.OPCUAAdvancedDriverEditorInput;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAAdvancedServerDriversModelNode;

public class OpenOPCUAAdvancedServerConfigurationHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.commands.openopcuaadvanceddriverconfig";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		TreeSelection selection = (TreeSelection) page.getSelection(OPCNavigationView.ID);
		if (selection == null) {
			return null;
		}
		if (selection.isEmpty()) {
			return null;
		}
		Object selectedNode = selection.getFirstElement();
		if (selectedNode instanceof OPCUAAdvancedServerDriversModelNode) {
			OPCUAAdvancedServerDriversModelNode dps = (OPCUAAdvancedServerDriversModelNode) selectedNode;
			String editorId = "";
			try {
				OPCUAAdvancedDriverEditorInput input = new OPCUAAdvancedDriverEditorInput();
				IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
				String serverpath = dps.getFilesystem().getRootPath();
				IPath config = new Path(serverpath).append(store.getString(OPCUAConstants.ASOPCUADriversFolder))
						.append(dps.getDriverName());
				input.setNode(dps);
				input.setDriverType(dps.getDriverType());
				input.setServerName(dps.getServerName());
				input.setDriverName(dps.getDriverName());
				input.setFileSystem(dps.getFilesystem());
				input.setDriverConfigPath(config.append("driver.com").toOSString());
				input.setDPConfigFile(config.append("datapoints.com").toOSString());
				input.setDriverPath(config.toOSString());
				input.setServerRuntimePath(serverpath);
				// load driver editor id from driver config
				editorId = "com.bichler.astudio.editor." + dps.getDriverType() + "." + dps.getDriverVersion() + "."
						+ dps.getEditorName();
				page.openEditor(input, editorId);
			} catch (PartInitException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return null;
	}
}
