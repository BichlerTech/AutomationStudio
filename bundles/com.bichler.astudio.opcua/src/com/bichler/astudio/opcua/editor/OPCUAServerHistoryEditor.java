package com.bichler.astudio.opcua.editor;

import org.eclipse.ui.editors.text.TextEditor;

import com.bichler.astudio.opcua.editor.completion.OPCUAHistorySourceViewerConfiguration;
import com.bichler.astudio.opcua.editor.providers.OPCUAHistoryColorManager;
import com.bichler.astudio.opcua.editor.providers.OPCUAHistoryDocumentProvider;

public class OPCUAServerHistoryEditor extends TextEditor {
	public static final String ID = "com.bichler.astudio.editors.opcuaserverhistoryeditor";
	private OPCUAHistoryColorManager colorManager;
	// private IEditorInput input;
	// private EditorContentOutlinePage outlinePage;

	public OPCUAServerHistoryEditor() {
		colorManager = new OPCUAHistoryColorManager();
		setSourceViewerConfiguration(new OPCUAHistorySourceViewerConfiguration(colorManager));
		setDocumentProvider(new OPCUAHistoryDocumentProvider());
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
}
