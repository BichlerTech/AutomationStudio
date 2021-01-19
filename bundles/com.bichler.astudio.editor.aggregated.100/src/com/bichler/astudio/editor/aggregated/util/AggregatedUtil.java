package com.bichler.astudio.editor.aggregated.util;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;


public class AggregatedUtil {

	public static String openModel(){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if(window == null){
			return null;
		}

		FileDialog dialog = new FileDialog(window.getShell());
		dialog.setFilterExtensions(DesignerConstants.EXTENSION_INFORMATIONMODEL);
		
		String path = dialog.open();
		if (path != null) {
//			OPCUAUtil.openModel(window, path);
		}

		return path;
	}
}
