package com.bichler.astudio.opcua.properties.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.properties.editor.ImageEditor;
import com.bichler.astudio.opcua.properties.editor.ImageEditorInput;

public class OpenImageEditorHandler extends AbstractHandler {

	public static final String ID = "";
	private FileOutputStream out = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

			if (window == null) {
				return null;
			}

			IWorkbenchPage page = window.getActivePage();
			if (page == null) {
				return null;
			}
			
			ImageEditorInput input = new ImageEditorInput(Studio_ResourceManager.getServerName());
			try {
				page.openEditor(input, ImageEditor.ID);
			} catch (PartInitException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
			
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}

		return null;
	}
}
