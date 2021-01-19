package com.bichler.iec.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.ui.editor.XtextEditor;

public class IECEditor extends XtextEditor implements IEditorPart {
	
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
	}

	public IECEditor() {
		super();
		
	}	
}
