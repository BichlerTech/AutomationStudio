package com.bichler.astudio.opcua.properties.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.properties.editor.ImageEditor;
import com.bichler.astudio.opcua.properties.editor.ImageEditorInput;

public class AddImageHandler extends AbstractHandler {

	public static final String ID = "";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.jpg", "*.png" });
		// dialog.setFilterPath("c:\\temp");
		String result = dialog.open();

		String picsPath = Studio_ResourceManager.getInfoModellerDokuPath();
		if (result != null && !result.isEmpty()) {
			File f = new File(result);
			if (f.exists()) {
				String path = new Path(picsPath).append(f.getName()).toOSString();

				try (OutputStream os = new FileOutputStream(path); InputStream is = new FileInputStream(result);) {
					f = new File(path);
					if (!f.exists())
						f.createNewFile();
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}
				} catch (IOException ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
				}

				ImageEditorInput input = new ImageEditorInput(Studio_ResourceManager.getServerName());
				IEditorPart ide = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findEditor(input);
//				update editor
				if (ide instanceof ImageEditor) {
					ImageEditor editor = (ImageEditor) ide;
					editor.addGalleryItem(path);
				}
			}
		}
		return null;
	}
}
