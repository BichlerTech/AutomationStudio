package com.bichler.astudio.opcua.opcmodeler.commands.handler.refresh;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorInput;

public class RefreshEditors extends AbstractHandler {
	public static final String ID = "commands.designer.editor.refresh";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// close and open the editors
		// TODO: SLOW!
		for (IEditorReference page : HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.getEditorReferences()) {
			if (page.getId().equals(DesignerConstants.NODEEDITOR_ID)) {
				IWorkbenchPage workbenchPage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
				NodeEditorInput input = null;
				try {
					input = (NodeEditorInput) page.getEditorInput();
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
				workbenchPage.closeEditor(page.getEditor(true), true);
				try {
					workbenchPage.openEditor(input, page.getId());
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
