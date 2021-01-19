package com.bichler.astudio.licensemanagement.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.licensemanagement.editor.LicenseManagerEditorPart;
import com.bichler.astudio.licensemanagement.editor.inputs.LicenseManagerEditorInput;

public class OpenLicenseManagerHandler extends AbstractHandler {

	public static final String ID= "com.bichler.astudio.commands.openlicensemanager";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		
//		TreeSelection selection = (TreeSelection) HandlerUtil
//				.getActiveWorkbenchWindow(event).getSelectionService()
//				.getSelection(NavigationView.ID);
		
//		LicenseManagerModelNode selectedNode = (LicenseManagerModelNode) selection.getFirstElement();
		try {
			LicenseManagerEditorInput licman = new LicenseManagerEditorInput();
		//	licman.setManager(selectedNode.getManager());
			page.openEditor(licman, LicenseManagerEditorPart.ID, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
