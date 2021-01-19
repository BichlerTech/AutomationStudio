package com.bichler.astudio.picandplace.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.picandplace.editor.PickAndPlaceEditorInput;
import com.bichler.astudio.picandplace.editor.PickAndPlaceEditor;

public class ExportPaPHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			
			PickAndPlaceEditorInput input = new PickAndPlaceEditorInput();
			IEditorPart editor = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findEditor(input);
			
			if(editor instanceof PickAndPlaceEditor) {
				((PickAndPlaceEditor)editor).export();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
