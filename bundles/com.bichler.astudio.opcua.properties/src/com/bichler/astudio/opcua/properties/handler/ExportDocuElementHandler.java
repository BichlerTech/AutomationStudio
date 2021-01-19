package com.bichler.astudio.opcua.properties.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.ExportDocuElementsHandler;
import com.bichler.astudio.opcua.properties.view.ASDocuView;

public class ExportDocuElementHandler extends AbstractHandler {

	private FileOutputStream out = null;
	private XWPFDocument document = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {

			ASDocuView view = (ASDocuView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
					.findView(ASDocuView.ID);
			// Node selectedNode = ((BrowserModelNode)
			// selection.getFirstElement()).getNode();

			// Blank Document
			document = new XWPFDocument();
			

			FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.docx", "*.*" });
			String fileName = dialog.open();
			if (fileName == null) {
				return null;
			}

			// Write the Document in file system
			File file = new File(fileName);

			if (!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);
			
			ExportDocuElementsHandler.createAutomationHeader(document,out);
			ExportDocuElementsHandler.createBichlerFooter(document,out);

			// processVariableTypeNode(selectedNode);
			document.write(out);

			Logger.getLogger(getClass().getName()).log(Level.INFO, "docu export finished");
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
