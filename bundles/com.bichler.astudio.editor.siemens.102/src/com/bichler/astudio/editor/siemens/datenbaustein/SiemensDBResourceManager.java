package com.bichler.astudio.editor.siemens.datenbaustein;

import java.util.HashMap;
import java.util.Map;

import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.view.drivermodel.handler.util.IDriverStructResourceManager;

public class SiemensDBResourceManager implements IDriverStructResourceManager {
	private Map<String, AbstractSiemensNode> siemens_db_structures = new HashMap<>();
	private Map<String, Float> siemens_db_addressindex = new HashMap<>();
	{
		siemens_db_addressindex.put("BOOL", 0.1f);
		siemens_db_addressindex.put("BYTE", 1f);
		siemens_db_addressindex.put("UDINT", 4f);
		siemens_db_addressindex.put("WORD", 2f);
		siemens_db_addressindex.put("DWORD", 4f);
		siemens_db_addressindex.put("CHAR", 1f);
		siemens_db_addressindex.put("INT", 2f);
		siemens_db_addressindex.put("DINT", 4f);
		siemens_db_addressindex.put("REAL", 4f);
		siemens_db_addressindex.put("S5TIME", 2f);
		siemens_db_addressindex.put("TIME", 4f);
		siemens_db_addressindex.put("TIMEOFDAY", 4f);
		siemens_db_addressindex.put("DATE", 2f);
	}
	// private static SiemensDBResourceManager instance = null;

	public SiemensDBResourceManager() {
	}

	// public static SiemensDBResourceManager getInstance() {
	// if (instance == null) {
	// instance = new SiemensDBResourceManager();
	// }
	// return instance;
	// }
	public void addStructures(Map<String, AbstractSiemensNode> structures) {
		siemens_db_structures.putAll(structures);
	}

	public AbstractSiemensNode getStructure(String key) {
		return siemens_db_structures.get(key);
	}

	public void addDatatypeIndex(String datatype, Float indexsize) {
		this.siemens_db_addressindex.put(datatype, indexsize);
	}

	public Float getIndexOfDatatype(String datatype) {
		// if (this.siemens_db_addressindex.containsKey(datatype)) {
		return this.siemens_db_addressindex.get(datatype);
		// }
		// return null;
	}
}
