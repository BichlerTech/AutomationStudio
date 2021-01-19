package com.bichler.astudio.editor.aggregated.clientbrowser.link;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class LinkedWithEditorPartListener implements IPartListener2 {

	private ILinkedWithEditorView view;

	public LinkedWithEditorPartListener(ILinkedWithEditorView view) {
		this.view = view;
	}

	@Override
	public void partActivated(IWorkbenchPartReference ref) {
		if (ref.getPart(true) instanceof IEditorPart) {
			view.editorActivated(view.getViewSite().getPage().getActiveEditor());
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			view.editorActivated(view.getViewSite().getPage().getActiveEditor());
		}
	}

	@Override
	public void partVisible(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			IEditorPart editor = view.getViewSite().getPage().getActiveEditor();
			if (editor != null) {
				view.editorActivated(editor);
			}
		}
	}

	@Override
	public void partOpened(IWorkbenchPartReference ref) {
		if (ref.getPart(true) == view) {
			view.editorActivated(view.getViewSite().getPage().getActiveEditor());
		}
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

}
