package com.bichler.astudio.opcua.editor;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.components.ui.ComponentsSharedImages;
import com.bichler.astudio.opcua.IOPCPerspectiveEditor;

public class OPCUAEcmaScriptEditor extends CompilationUnitEditor implements IOPCPerspectiveEditor {
	public static final String ID = "com.bichler.astudio.editors.opcuaecmascripteditor"; //$NON-NLS-1$

	public OPCUAEcmaScriptEditor() {
		super();
	}

	@Override
	public Image getTitleImage() {
		// TODO Auto-generated method stub
		return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_SCRIPTS);
	}
}
