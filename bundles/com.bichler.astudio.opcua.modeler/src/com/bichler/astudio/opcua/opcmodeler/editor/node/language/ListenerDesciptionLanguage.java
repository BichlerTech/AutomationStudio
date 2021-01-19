package com.bichler.astudio.opcua.opcmodeler.editor.node.language;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorPart;

public class ListenerDesciptionLanguage extends AbstractLanguageListener {
	public ListenerDesciptionLanguage(Text nodeId, Text description, NodeEditorPart editor) {
		super(nodeId, description, editor);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		DialogDescriptionLanguage dialog = new DialogDescriptionLanguage(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), getNodeId(), getName());
		int open = dialog.open();
		if (open == Dialog.OK) {
			getEditor().setDirty(true);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}
}
