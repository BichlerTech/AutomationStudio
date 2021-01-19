package com.bichler.astudio.opcua.opcmodeler.editor.node.language;

import opc.sdk.core.language.LanguagePack;

import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DialogDescriptionLanguage extends AbstractLanguageDialog {
	public DialogDescriptionLanguage(Shell parentShell, NodeId nodeId, String name) {
		super(parentShell, nodeId, CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_description.text"), name);
	}

	@Override
	protected String getLanguageTextFromPack(LanguagePack langPack) {
		LocalizedText description = langPack.getDescription();
		return description == null ? "" : description.getText();
	}

	@Override
	protected void setLanguageTextToPack(LanguagePack langPack, String text) {
		if (text == null) {
			return;
		}
		langPack.setDescription(text);
	}
}
