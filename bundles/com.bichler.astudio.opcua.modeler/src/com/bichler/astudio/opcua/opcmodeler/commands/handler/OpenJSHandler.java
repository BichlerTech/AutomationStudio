package com.bichler.astudio.opcua.opcmodeler.commands.handler;

import java.io.File;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OpenJSHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
			String[] filterExt = new String[] { "*.js" };
			String[] filterNames = new String[] { "JS Files" };
			FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
			fileDialog.setFilterNames(filterNames);
			fileDialog.setFilterExtensions(filterExt);
			fileDialog.setFilterPath(File.listRoots()[0].getPath());
			String path = fileDialog.open();
			// open the file
			if (path != null) {
				Logger.getLogger(getClass().getName()).info(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.open.jsconfig") + path);
				// EditorUtility.openInEditor(inputElement);
				IPath filepath = new Path(path);
				IFileStore fileStore = FileBuffers.getFileStoreAtLocation(filepath);
				// JSEditorInput input = new JSEditorInput(fileStore);
				// IDE.openEditorOnFileStore(page, fileStore);
				/**
				 * try { IDE.openEditorOnFileStore(page, fileStore); } catch (PartInitException
				 * e) { TODO Auto-generated catch block e.printStackTrace(); }
				 */
				// FileStoreEditorInput input = new
				// FileStoreEditorInput(fileStore);
				// IClassFile file =
				/**
				 * InternalClassFileEditorInput input = new
				 * InternalClassFileEditorInput(classFile); try { page .openEditor(input,
				 * JSEditor.ID); } catch (PartInitException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); }
				 */
			}
		}
		return null;
	}
}
