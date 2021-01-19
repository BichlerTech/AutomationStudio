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

public class OpenPaPHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			
			PickAndPlaceEditorInput input = new PickAndPlaceEditorInput();
			IEditorPart editor = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().openEditor(input,
					PickAndPlaceEditor.ID);

			

//			ModelLoader importer = new ModelLoader((EventFXEditor) editor);
//			importer.load(path);
//
//			HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(IEC61131SymbolView.ID);
//
//			IViewPart viewer = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findView("com.bichler.iec.ui.IECViewer");
//			
//			System.out.println(viewer);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
