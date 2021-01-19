package com.bichler.astudio.opcua.opcmodeler.editor.node.language;

import opc.sdk.core.language.LanguagePack;

import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DialogInversenameLanguage extends AbstractLanguageDialog {
	public DialogInversenameLanguage(Shell parentShell, NodeId nodeId, String name) {
		super(parentShell, nodeId,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.reference.name"), name);
	}

	@Override
	protected String getLanguageTextFromPack(LanguagePack langPack) {
		LocalizedText inversename = langPack.getInversename();
		return inversename == null ? "" : inversename.getText();
	}

	@Override
	protected void setLanguageTextToPack(LanguagePack langPack, String text) {
		if (text == null) {
			return;
		}
		langPack.setInversename(text);
	}
}
