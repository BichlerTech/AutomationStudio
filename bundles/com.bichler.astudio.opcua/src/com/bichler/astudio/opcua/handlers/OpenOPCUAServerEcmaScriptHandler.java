package com.bichler.astudio.opcua.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.components.file.CometLocaleFile;
import com.bichler.astudio.opcua.editor.OPCUAEcmaScriptEditor;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaScriptModelNode;

/**
 * 
 * @author hannes bichler
 * 
 */
public class OpenOPCUAServerEcmaScriptHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			// Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event)
			// .getShell();
			TreeSelection selection = (TreeSelection) page.getSelection(OPCNavigationView.ID);
			if (selection == null) {
				return null;
			}
			if (selection.isEmpty()) {
				return null;
			}
			Object selectedNode = selection.getFirstElement();
			if (selectedNode instanceof OPCUAServerEcmaScriptModelNode) {
				OPCUAServerEcmaScriptModelNode node = (OPCUAServerEcmaScriptModelNode) selectedNode;

				String filepath = new Path(node.getFilesystem().getRootPath()).append("ecmascripts")
						.append(node.getScriptName() + ".js").toOSString();
//        if(node.get)
				File file = new File(filepath);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				@SuppressWarnings("unused")
				int interval = node.getInterval();
				try {
					CometLocaleFile locale = new CometLocaleFile(file);
//          OPCUAEcmaScriptEditorInput input = new OPCUAEcmaScriptEditorInput(locale);
//          input.setInterval(interval);

//          final File tmp = locale.getFile()
					final IFileStore externalFile = EFS.getLocalFileSystem().fromLocalFile(locale.getFile());
					FileStoreEditorInput input = new FileStoreEditorInput(externalFile);
//          page.openEditor(input, "org.eclipse.jdt.ui.CompilationUnitEditor");
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
