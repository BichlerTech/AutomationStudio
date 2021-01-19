package opc.sdk.server;

import org.opcfoundation.ua.builtintypes.NodeId;

import opc.sdk.core.language.LanguageItem;

public interface ILanguageServer {
	public LanguageItem[] getLanguageInformation(NodeId[] nodeIds);

	public String[] getServerLocales();

	public void importLanguage(LanguageItem[] languages);
}
