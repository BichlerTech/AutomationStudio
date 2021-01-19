package com.bichler.astudio.editor.siemens.model;

import java.io.BufferedReader;
import java.io.IOException;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;

public class SiemensNodeFactory2 {
	public SiemensNodeFactory2() {
	}

	public AbstractSiemensNode parseCSV(String csvFile, BufferedReader reader, boolean isDBFile,
			SiemensDBResourceManager structureManager) throws IOException {
		SiemensModelParser2 parser = new SiemensModelParser2();
		parser.readLine(structureManager, csvFile, reader);
		fillUDTIfNeeded(structureManager, parser);
		return parser.generate(isDBFile);
	}

	public AbstractSiemensNode parseCSVUDT(String csvFile, BufferedReader reader, boolean isDBFile,
			SiemensDBResourceManager structureManager) throws IOException {
		SiemensModelParser2 parser = new SiemensModelParser2();
		parser.readLineUDT(structureManager, csvFile, reader);
		fillUDTIfNeeded(structureManager, parser);
		return parser.generate(isDBFile);
	}

	private void fillUDTIfNeeded(SiemensDBResourceManager structureManager, SiemensModelParser2 parser) {
		// AbstractSiemensNode root = (AbstractSiemensNode)
		// this.treeViewer.getInput();
		if (parser == null) {
			return;
		}
		parser.fillUDT(structureManager);
		// fillUDT(root);
	}
}
