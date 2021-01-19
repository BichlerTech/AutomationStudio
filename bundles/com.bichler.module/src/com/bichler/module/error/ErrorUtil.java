package com.bichler.module.error;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class ErrorUtil {

	public static void showError(String msg) {
		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", msg);
	}
	
	public static void showInfo(String msg, String[] additional) {
		StringBuilder builder = new StringBuilder();
		builder.append(msg);
		builder.append("\n\n");
		for(int i = 0; i < additional.length; i++) {
			String line = additional[i];
			builder.append(line);
			// skip last line
			if(i < additional.length-1) {
				builder.append("\n");
			}
		}
		
		showInfo(builder.toString());
	}
	
	public static void showInfo(String msg) {
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Info", msg);
	}
}
