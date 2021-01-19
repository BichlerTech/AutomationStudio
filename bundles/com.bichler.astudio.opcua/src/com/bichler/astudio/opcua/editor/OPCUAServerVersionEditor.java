package com.bichler.astudio.opcua.editor;

import org.eclipse.ui.editors.text.TextEditor;

import com.bichler.astudio.opcua.editor.completion.OPCUAVersionSourceViewerConfiguration;
import com.bichler.astudio.opcua.editor.providers.OPCUAVersionColorManager;
import com.bichler.astudio.opcua.editor.providers.OPCUAVersionDocumentProvider;

public class OPCUAServerVersionEditor extends TextEditor {
	public static final String ID = "com.bichler.astudio.editors.opcuaserverversioneditor";
	private OPCUAVersionColorManager colorManager;

	public OPCUAServerVersionEditor() {
		colorManager = new OPCUAVersionColorManager();
		setSourceViewerConfiguration(new OPCUAVersionSourceViewerConfiguration(colorManager));
		setDocumentProvider(new OPCUAVersionDocumentProvider());
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
}
