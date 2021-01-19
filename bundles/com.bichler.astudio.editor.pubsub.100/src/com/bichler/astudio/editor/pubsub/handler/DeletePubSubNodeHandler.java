package com.bichler.astudio.editor.pubsub.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.editor.pubsub.PubSubActivator;
import com.bichler.astudio.editor.pubsub.PubSubDPEditor;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DeletePubSubNodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PubSubDPEditor pubsub = null;
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		IEditorPart editor = page.getActiveEditor();

		boolean confirm = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
				CustomString.getString(PubSubActivator.getDefault().RESOURCE_BUNDLE, "confirm.delete.pubsubelement.title"),
				CustomString.getString(PubSubActivator.getDefault().RESOURCE_BUNDLE, "confirm.delete.pubsubelement.description"));
		if (!confirm) {
			return null;
		}

		if (editor instanceof PubSubDPEditor) {
			pubsub = (PubSubDPEditor) editor;
			pubsub.removePubSubNode();
		}

		return null;
	}

}
