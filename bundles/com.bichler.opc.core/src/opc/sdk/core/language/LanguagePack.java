package opc.sdk.core.language;

import java.util.Locale;

import org.opcfoundation.ua.builtintypes.LocalizedText;

public class LanguagePack {
	/** language locale */
	private Locale locale = null;
	/** text for displayname attribute in opc ua node */
	private LocalizedText displayname = null;
	/** text for description attribute in opc ua node */
	private LocalizedText description = null;
	/**
	 * text for inversename attribute in opc ua referencenode (used to complete
	 * basic opc ua localized texts)
	 */
	private LocalizedText inversename = null;
	// private LanguageItem packContainer;

	/**
	 * Packs all basic opc ua node localizedtext for one language. Null initializer
	 */
	public LanguagePack() {
	}

	/**
	 * Packs all basic opc ua node localizedtext for one language.
	 * 
	 * @param Locale Language Locale to use
	 */
	public LanguagePack(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Gets the displayname for a given language.
	 * 
	 * @return Displayname
	 */
	public LocalizedText getDisplayname() {
		return this.displayname;
	}

	/**
	 * Sets the displayname for the given language
	 * 
	 * @param Displayname
	 */
	public void setDisplayname(String displayname) {
		this.displayname = new LocalizedText(displayname, this.locale);
	}

	/**
	 * Gets the description for a given language.
	 * 
	 * @return Description
	 */
	public LocalizedText getDescription() {
		return this.description;
	}

	/**
	 * Sets the description for the given language
	 * 
	 * @param Description
	 */
	public void setDescription(String description) {
		this.description = new LocalizedText(description, this.locale);
	}

	/**
	 * Gets the inversename for a given language.
	 * 
	 * @return Inversename
	 */
	public LocalizedText getInversename() {
		return this.inversename;
	}

	/**
	 * Sets the inversename for the given language
	 * 
	 * @param Inversename
	 */
	public void setInversename(String inversename) {
		this.inversename = new LocalizedText(inversename, this.locale);
	}

	/**
	 * Gets the Locale from this language package.
	 * 
	 * @return Language Locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	// public LanguageItem getParentContainer() {
	// return this.packContainer;
	// }
	//
	// public void setParentContainer(LanguageItem language) {
	// this.packContainer = language;
	// }
}
