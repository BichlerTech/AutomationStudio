package com.bichler.astudio.opcua.opcmodeler.language;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;

import opc.sdk.core.language.LanguageItem;
import opc.sdk.core.language.LanguagePack;

public class LanguageCSVWriter {
	enum CSVHeader {
		NodeId, Inversename, Description, Displayname;
	}

	public static final String SPLIT_CSV = ";";

	public LanguageCSVWriter() {
	}

	public void write(File file, LanguageItem[] items) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			if (items == null) {
				return;
			}
			Set<String> ALL = new LinkedHashSet<String>();
			findAllServerLocales(items, ALL);
			writeHeader_1(writer, ALL);
			writeColumnNames_1(writer, ALL);
			for (LanguageItem item : items) {
				parseItem(writer, ALL, item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void writeColumnNames_1(BufferedWriter writer, Set<String> languages) throws IOException {
		if (languages.isEmpty()) {
			return;
		}
		writer.write(CSVHeader.NodeId.name());
		writer.write(SPLIT_CSV);
		for (int i = 0; i < languages.size(); i++) {
			writer.write(CSVHeader.Inversename.name());
			writer.write(SPLIT_CSV);
			writer.write(CSVHeader.Description.name());
			writer.write(SPLIT_CSV);
			writer.write(CSVHeader.Displayname.name());
			if (languages.size() - 1 >= i) {
				writer.write(SPLIT_CSV);
			}
		}
		writer.newLine();
	}

	private void writeHeader_1(BufferedWriter writer, Set<String> languages) throws IOException {
		if (languages.isEmpty()) {
			return;
		}
		// append languages
		for (String lang : languages) {
			writer.write(SPLIT_CSV);
			writer.write(lang);
			writer.write(SPLIT_CSV);
			writer.write(SPLIT_CSV);
		}
		writer.newLine();
	}

	/**
	 * Collect all available languages for this server
	 * 
	 * @param items
	 * @param all_lang
	 */
	private void findAllServerLocales(LanguageItem[] items, Set<String> all_lang) {
		if (items == null) {
			return;
		}
		for (LanguageItem item : items) {
			Collection<String> langs = item.getLanguages();
			all_lang.addAll(langs);
		}
	}

	private void parseItem(BufferedWriter writer, Set<String> languages, LanguageItem item) throws IOException {
		NodeId nodeId = item.getNodeId();
		if (NodeId.isNull(nodeId)) {
			return;
		}
		writer.write("\"" + nodeId.toString() + "\"");
		writer.write(SPLIT_CSV);
		for (String lang : languages) {
			LanguagePack pack = item.getLanguagePack(lang);
			if (pack != null) {
				LocalizedText inversename = pack.getInversename();
				if (inversename != null && inversename.getText() != null) {
					writer.write(inversename.getText());
				}
				writer.write(SPLIT_CSV);
				LocalizedText description = pack.getDescription();
				if (description != null && description.getText() != null) {
					writer.write(description.getText());
				}
				writer.write(SPLIT_CSV);
				LocalizedText displayname = pack.getDisplayname();
				if (displayname != null && displayname.getText() != null) {
					writer.write(displayname.getText());
				}
				writer.write(SPLIT_CSV);
			}
			// skip
			else {
				writer.write(SPLIT_CSV);
				writer.write(SPLIT_CSV);
				writer.write(SPLIT_CSV);
			}
		}
		writer.newLine();
	}
}
