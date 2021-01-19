package com.bichler.astudio.opcua;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;
import com.bichler.astudio.opcua.handlers.opcua.resource.DisposeOPCUAEditorsHandler;

@SuppressWarnings("unused")
public abstract class AbstractOPCDPDriverViewLinkEditorPart extends MultiPageEditorPart
		implements IOPCDataPointEditPart {

	/**
	 * ISaveablePart that's clean, no need to save.
	 *
	 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=372799,
	 * https://github.com/ControlSystemStudio/cs-studio/issues/1619
	 *
	 * Workaround: Force adaptation of property page to clean_saveable.
	 */
	private final static ISaveablePart clean_saveable = new ISaveablePart() {
		@Override
		public boolean isSaveOnCloseNeeded() {
			return false;
		}

		@Override
		public boolean isSaveAsAllowed() {
			return false;
		}

		@Override
		public boolean isDirty() {
			return false;
		}

		@Override
		public void doSaveAs() {
			// NOP
		}

		@Override
		public void doSave(IProgressMonitor monitor) {
			// NOP
		}

	};

	public AbstractOPCDPDriverViewLinkEditorPart() {
		super();
	}

	@Override
	public void setFocus() {
		onFocusRemoteView();
	}

	@Override
	public void dispose() {
		onDisposeRemoteView();
		super.dispose();
	}

	@Override
	public void onDisposeRemoteView() {
		IHandlerService handlerService = getSite().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(DisposeOPCUAEditorsHandler.ID, null);
		} catch (ExecutionException | NotDefinedException | NotHandledException | NotEnabledException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == ISaveablePart.class) {
			return (T) clean_saveable;
		}

		return super.getAdapter(adapter);
	}

}
