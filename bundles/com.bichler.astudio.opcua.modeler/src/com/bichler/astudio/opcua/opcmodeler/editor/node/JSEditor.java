package com.bichler.astudio.opcua.opcmodeler.editor.node;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.IDocumentProvider;
//import org.eclipse.wst.jsdt.internal.ui.javaeditor.CompilationUnitEditor;

public class JSEditor extends CompilationUnitEditor {
	@Override
	protected void performSave(boolean overwrite, IProgressMonitor progressMonitor) {
		JSEditorInput input = (JSEditorInput) getEditorInput();
		String script = getViewer().getTextWidget().getText();
		IDocumentProvider provider = getDocumentProvider();
		// System.out.println(script);
		// System.out.println(this.isDirty());
	}

	public static final String ID = "com.hbsoft.designer.editor.node.CometJavaScript"; //$NON-NLS-1$

	public JSEditor() {
		// org.eclipse.wst.jsdt.internal.ui.javaeditor.
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}
}
