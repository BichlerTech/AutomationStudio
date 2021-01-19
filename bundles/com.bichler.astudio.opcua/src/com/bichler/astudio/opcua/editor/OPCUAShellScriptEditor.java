package com.bichler.astudio.opcua.editor;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.graphics.Image;
//import org.eclipse.wst.jsdt.internal.ui.javaeditor.CompilationUnitEditor;

import com.bichler.astudio.components.ui.ComponentsSharedImages;
import com.bichler.astudio.opcua.IOPCPerspectiveEditor;
import com.bichler.astudio.opcua.editor.input.OPCUAShellScriptEditorInput;

public class OPCUAShellScriptEditor extends CompilationUnitEditor implements IOPCPerspectiveEditor {

	@Override
	public String getTitle() {
		return "License Manager";
	}

	public static final String ID = "com.bichler.astudio.editors.cometshellscripteditor"; //$NON-NLS-1$

	public OPCUAShellScriptEditor() {
		super();
	}

	@Override
	public Image getTitleImage() {
		// TODO Auto-generated method stub
		return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_SCRIPTS);
	}

	@Override
	public OPCUAShellScriptEditorInput getEditorInput() {
		// TODO Auto-generated method stub
		return (OPCUAShellScriptEditorInput) super.getEditorInput();
	}
}
