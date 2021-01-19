package com.bichler.astudio.opcua.properties.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.properties.editor.ImageEditor;
import com.bichler.astudio.opcua.properties.editor.ImageEditorInput;

public class DeleteImageHandler extends AbstractHandler {

	public static final String ID = "";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

//		String picsPath = Studio_ResourceManager.getInfoModellerDokuPath();

		ImageEditorInput input = new ImageEditorInput(Studio_ResourceManager.getServerName());
		IEditorPart ide = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findEditor(input);
//				update editor
		if (ide instanceof ImageEditor) {
			ImageEditor editor = (ImageEditor) ide;
			editor.deleteFile();
		}

		return null;
	}
}
