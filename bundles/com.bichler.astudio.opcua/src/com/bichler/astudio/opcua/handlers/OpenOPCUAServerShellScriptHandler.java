package com.bichler.astudio.opcua.handlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.components.file.CometLocaleFile;
import com.bichler.astudio.opcua.editor.OPCUAEcmaScriptEditor;
import com.bichler.astudio.opcua.editor.input.OPCUAEcmaScriptEditorInput;

/**
 * 
 * @author hannes bichler
 * 
 */
public class OpenOPCUAServerShellScriptHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		if (page != null) {
			// Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event)
			// .getShell();

			if (event.getTrigger() instanceof OpenOPCUAEcmaScriptEvent) {

				File file = new File(((OpenOPCUAEcmaScriptEvent) event.getTrigger()).getFilePath());
				int interval = ((OpenOPCUAEcmaScriptEvent) event.getTrigger()).getInterval();

				try {

					CometLocaleFile locale = new CometLocaleFile(file);
					OPCUAEcmaScriptEditorInput input = new OPCUAEcmaScriptEditorInput(locale);
					input.setInterval(interval);

					// IEditorPart editor =
					page.openEditor(input, OPCUAEcmaScriptEditor.ID);

					// ((CompilationUnitEditor)editor).setInput(input);

				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

}
