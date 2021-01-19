package com.bichler.astudio.opcua.opcmodeler.commands.handler.clipboard;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;

public class CopyHandler extends AbstractClipboardHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = getSelectionFromView();
		if (selection == null) {
			return null;
		}
		if (selection.isEmpty()) {
			return null;
		}
		copy(selection, true);
		return null;
	}
}
