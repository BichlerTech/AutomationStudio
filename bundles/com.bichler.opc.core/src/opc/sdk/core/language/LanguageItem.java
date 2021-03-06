package opc.sdk.core.language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.opcfoundation.ua.builtintypes.NodeId;

public class LanguageItem {
	private NodeId nodeId = null;
	private Map<String, LanguagePack> languagePacks = new LinkedHashMap<>();

	/**
	 * Stores all language packages for a given opc ua node.
	 * 
	 * @param NodeId NodeId reference to an OPC UA node
	 */
	public LanguageItem(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Add OPC UA names for a language. (Replace old)
	 * 
	 * @param Language Language package to localize opc ua texts
	 */
	public void addLanguagePack(LanguagePack language) {
		// language.setParentContainer(this);
		this.languagePacks.put(language.getLocale().getLanguage(), language);
	}

	public LanguagePack[] getLanguagePacks() {
		return this.languagePacks.values().toArray(new LanguagePack[0]);
	}

	/**
	 * Returns a languagepack for a locale
	 * 
	 * @param locale
	 * @return
	 */
	public LanguagePack getLanguagePack(Locale locale) {
		return this.languagePacks.get(locale.getLanguage());
	}

	public LanguagePack getLanguagePack(String language) {
		return this.languagePacks.get(language);
	}

	public void removeLanguages() {
		this.languagePacks.clear();
	}

	/**
	 * Removes a language pack for an OPC UA node
	 * 
	 * @param Locale Locale to remove
	 */
	public void removeLanguage(Locale locale) {
		this.languagePacks.remove(locale.getLanguage());
	}

	public void removeLanguage(String language) {
		this.languagePacks.remove(language);
	}

	/**
	 * Removes a language pack for an OPC UA node
	 * 
	 * @param Language Language pack with the locale to remove
	 */
	public void removeLanguagePack(LanguagePack language) {
		removeLanguage(language.getLocale());
	}

	/**
	 * Gets the nodeId, which references to an OPC UA node
	 * 
	 * @return NodeId
	 */
	public NodeId getNodeId() {
		return nodeId;
	}

	public Collection<String> getLanguages() {
		List<String> languages = new ArrayList<>();
		for (Entry<String, LanguagePack> e : this.languagePacks.entrySet()) {
			languages.add(e.getKey());
		}
		return languages;
	}
	// /**
	// * Sets the nodeId reference. (Should not be used)
	// * @param nodeId
	// */
	// protected void setNodeId(NodeId nodeId) {
	// this.nodeId = nodeId;
	// }
}
