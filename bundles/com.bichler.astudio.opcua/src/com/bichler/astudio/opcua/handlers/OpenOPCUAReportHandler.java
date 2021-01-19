package com.bichler.astudio.opcua.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
//import com.bichler.astudio.birt.editor.BirtReportEditor;
//import com.bichler.astudio.birt.editor.RCPMultiPageReportEditor;
//import com.bichler.astudio.birt.editor.input.BirtReportEditorInput;
import com.bichler.astudio.opcua.nodes.OPCUAReportModelNode;

public class OpenOPCUAReportHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.commands.openopcuareport";

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
		OPCUAReportModelNode selectedNode = (OPCUAReportModelNode) selection.getFirstElement();
		// if(event.getTrigger() instanceof CometOPCUAEcmaScriptOpenEvent) {
		// try
		// {
		String filepath = new Path(selectedNode.getFilesystem().getRootPath()).append("report.rptdesign").toOSString();
		// if(node.get)
		File file = new File(filepath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
