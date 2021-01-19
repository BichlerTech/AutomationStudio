package com.bichler.astudio.opcua.opcmodeler.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.opcfoundation.ua.builtintypes.NodeId;

import opc.sdk.core.language.LanguageItem;
import opc.sdk.core.language.LanguagePack;

public class LanguageCSVReader {
	public static final String SPLIT_CSV = ";";

	public LanguageCSVReader() {
	}

	public LanguageItem[] read(File file) {
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));
			int lineNumber = 0;
			String buffer = null;
			List<Locale> locales = new ArrayList<>();
			List<LanguageItem> languageItems = new ArrayList<>();
			while ((buffer = reader.readLine()) != null) {
				if (lineNumber > 1) {
					readLocalePackage(buffer, locales, languageItems);
				} else if (lineNumber == 0) {
					readLocales(buffer, locales);
				} else if (lineNumber == 1) {
					// skip header for user readability
				}
				lineNumber++;
			}
			return languageItems.toArray(new LanguageItem[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private void readLocalePackage(String buffer, List<Locale> locales, List<LanguageItem> languageItems) {
		String[] splitted = buffer.split(SPLIT_CSV);
		if (splitted != null && splitted.length > 0) {
			int beginParseRow = 1;
			String nId = splitted[0];
			nId = nId.replaceAll("\"", "");
			if (nId.startsWith("ns=")) {
				String nId2 = splitted[1];
				nId2 = nId2.replaceAll("\"", "");
				nId += SPLIT_CSV + nId2;
				beginParseRow = 2;
			}
			NodeId nodeId = NodeId.parseNodeId(nId);
			LanguageItem item = new LanguageItem(nodeId);
			LanguagePack currentPack = null;
			int currentLocale = 0;
			int row = 0;
			for (int i = beginParseRow; i < splitted.length; i++) {
				String text = splitted[i];
				int section = row % 3;
				switch (section) {
				// inversename
				case 0:
					if (locales.size() > currentLocale) {
						Locale locale = locales.get(currentLocale);
						currentPack = new LanguagePack(locale);
						currentLocale++;
					}
					currentPack.setInversename(text);
					break;
				// description
				case 1:
					currentPack.setDescription(text);
					break;
				// displayname
				case 2:
					currentPack.setDisplayname(text);
					item.addLanguagePack(currentPack);
					break;
				}
				// if(!item.containsLanguagePack(currentPack)){
				//
				// }
				row++;
			}
			languageItems.add(item);
		}
	}

	private void readLocales(String line, List<Locale> locales) {
		String[] split = line.split(SPLIT_CSV);
		if (split != null) {
			for (String localeId : split) {
				if (localeId != null && !localeId.isEmpty()) {
					Locale locale = new Locale(localeId);
					locales.add(locale);
				}
			}
		}
	}
}
