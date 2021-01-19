package com.bichler.astudio.editor.siemens.model;

import java.io.BufferedReader;
import java.io.IOException;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;

public class SiemensNodeFactory {
	public SiemensNodeFactory() {
	}

	public AbstractSiemensNode parseCSV(BufferedReader reader, boolean isDBFile,
			SiemensDBResourceManager structureManager) {
		try {
			SiemensModelParser parser = new SiemensModelParser(structureManager);
			String line = "";
			while ((line = reader.readLine()) != null) {
				parser.readLine(line);
			}
			return parser.generate(isDBFile);
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
}
