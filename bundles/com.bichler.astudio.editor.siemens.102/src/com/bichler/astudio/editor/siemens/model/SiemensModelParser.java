package com.bichler.astudio.editor.siemens.model;

import java.util.HashMap;
import java.util.Map;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;

public class SiemensModelParser {
	private static final String SEPERATOR_CSV = ";";
	public static final String DATATYPE_ARRAY = "ARRAY";
	public static final String DATATYPE_STRUCT = "STRUCT";
	public static final String DATATYPE_END_STRUCT = "END_STRUCT";
	private SiemensStructNode root = null;
	private int lineCount = 0;
	private String rootName = "";
	// private String address = "";
	private AbstractSiemensNode current = null;
	private Map<SiemensStructNode, SiemensArrayNode> arrays = new HashMap<>();
	// private float calculatedIndex = 0.0f;
	private SiemensDBResourceManager structureManager;

	protected SiemensModelParser(SiemensDBResourceManager structureManager) {
		this.structureManager = structureManager;
	}

	public void readLine(String line) {
		lineCount++;
		if (this.lineCount == 1) {
			parseFirstLine(line);
		} else if (this.lineCount == 2) {
			parseSecondLine(line);
		} else {
			parse(line);
		}
	}

	private void parse(String line) {
		if (line.startsWith("+")) {
			parsePlus(line, true);
		} else if (line.startsWith("=")) {
			parseEqual(line);
		} else if (line.startsWith("*")) {
			parseStar(line);
		}
		/**
		 * "First" line (+) is missing
		 */
		else {
			parsePlus(line, false);
		}
	}

	private void parseStar(String line) {
		String[] entries = line.split(SEPERATOR_CSV);
		entries[0] = entries[0].replace("*", "");
		String dataType = entries[1].trim();
		int dimension = ((SiemensArrayNode) this.current).getDimension();
		/**
		 * Build array
		 */
		AbstractSiemensNode item = null;
		if (dataType.indexOf(DATATYPE_STRUCT) != -1) {
			item = new SiemensStructNode(this.structureManager);
			((SiemensStructNode) item).setArrayEntry(true);
			item.setDataType(dataType);
			item.setIndex(Float.parseFloat(entries[0]));
			this.arrays.put((SiemensStructNode) item, (SiemensArrayNode) this.current);
			// dont add... it is be done when closing the stuct
			// this.current.addChild(item);
			int i = this.current.getChildIndex(item);
			item.setName("[" + i + "]");
			((SiemensArrayNode) this.current).setArrayType(DATATYPE_STRUCT);
			this.current = item;
		} else {
			((SiemensArrayNode) this.current).setArrayType(dataType);
			for (int i = 0; i < dimension; i++) {
				item = new SiemensEntryNode(this.structureManager);
				item.setDataType(dataType);
				item.setIndex(Float.parseFloat(entries[0]));
				item.setName("[" + i + "]");
				// DATATYPE datatype = DATATYPE.valueOf(entries[2].trim());
				// item.setDataType(datatype.name());
				this.current.addChild(item);
			}
			// End Build simple array
			if (this.current instanceof SiemensArrayNode) {
				this.current = this.current.getParent();
			}
		}
	}

	private void parseEqual(String line) {
		String[] entries = line.split(SEPERATOR_CSV);
		entries[0] = entries[0].replace("=", "");
		// close last struct
		if (entries[1].indexOf(DATATYPE_END_STRUCT) != -1) {
			// defines the length of the struct
			try {
				float structLength = Float.parseFloat(entries[0]);
				((SiemensStructNode) this.current).setStructLength(structLength);
			} catch (NumberFormatException nfe) {
			}
			closeLastStruct(this.current);
		}
	}

	private void parsePlus(String line, boolean isReal) {
		/**
		 * First line to init model
		 */
		if (!isReal) {
			this.root = new SiemensStructNode(this.structureManager);
			this.current = this.root;
			return;
		}
		String[] entries = line.split(SEPERATOR_CSV);
		entries[0] = entries[0].replace("+", "");
		String dataType = entries[2].trim();
		AbstractSiemensNode item = null;
		if (dataType.indexOf(DATATYPE_ARRAY) != -1) {
			item = new SiemensArrayNode(structureManager);
			((SiemensArrayNode) item).setDimension(entries[2]);
			item.setDataType(DATATYPE_ARRAY + "[" + ((SiemensArrayNode) item).getDimension() + "]");
			this.current.addChild(item);
			this.current = item;
		} else if (dataType.indexOf(DATATYPE_STRUCT) != -1) {
			item = new SiemensStructNode(this.structureManager);
			item.setDataType(dataType);
			this.current.addChild(item);
			this.current = item;
		} else {
			item = new SiemensEntryNode(this.structureManager);
			item.setDataType(dataType);
			// if(dt.indexOf("[") != -1){
			//
			// }
			// DATATYPE datatype = DATATYPE.valueOf(dt);
			// item.setDataType(datatype.name());
			this.current.addChild(item);
		}
		item.setIndex(Float.parseFloat(entries[0]));
		item.setName(entries[1]);
		// append other stuff as comment
		if (entries.length >= 4) {
			StringBuffer comment = new StringBuffer();
			for (int i = 3; i < entries.length; i++) {
				comment.append(escape(entries[i]) + " ");
			}
			item.setDescription(comment.toString());
		}
	}

	private void parseFirstLine(String line) {
		String[] entries = line.split(SEPERATOR_CSV);
		if (entries.length >= 2) {
			// id of structure reference
			String id = entries[1].replace("\"", "");
			this.rootName = id;
		}
	}

	private void parseSecondLine(String line) {
	}

	private void closeLastStruct(AbstractSiemensNode current) {
		SiemensArrayNode arrayParent = this.arrays.get(current);
		// End Build simple array
		if (arrayParent != null) {
			int dimension = arrayParent.getDimension();
			SiemensStructNode node = (SiemensStructNode) this.current;
			for (int i = 0; i < dimension; i++) {
				AbstractSiemensNode item = node.cloneNode(false, true);
				item.setName("[" + i + "]");
				arrayParent.addChild(item);
			}
			closeLastStruct(arrayParent);
			this.arrays.remove(current);
		} else if (current.getParent() != null) {
			AbstractSiemensNode parent = current.getParent();
			if (parent instanceof SiemensStructNode) {
				this.current = parent;
				// if (parent.getParent() != null) {
				// this.current = parent.getParent();
				// } else {
				// this.current = parent;
				// }
			} else {
				closeLastStruct(parent);
			}
		}
	}

	public AbstractSiemensNode generate(boolean isDBFile) {
		this.root.setName(this.rootName);
		this.root.setRoot(true);
		if (isDBFile) {
			generateSymbolNames(this.root.getChildren(), "");
		}
		return this.root;
	}

	private static final String SEPERATOR_SYMBOLNAME = ".";

	/**
	 * Generate all unique symbol node names
	 * 
	 * @param baseSet
	 */
	private void generateSymbolNames(AbstractSiemensNode[] baseSet, String symbolname) {
		if (!symbolname.toString().isEmpty()) {
			symbolname += SEPERATOR_SYMBOLNAME;
		}
		for (AbstractSiemensNode node : baseSet) {
			String extended = "" + symbolname;
			// append array symbolname
			String symbolString = node.getName();
			String extended2 = extended + symbolString;
			// set symbolname
			node.setSymbolName(extended2);
			generateSymbolNames(node.getChildren(), extended2);
		}
	}

	private String escape(String input) {
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			if ((int) input.charAt(i) > 32 && (int) input.charAt(i) < 126) {
				output.append(input.charAt(i));
			}
		}
		return output.toString().replace("Š", "ae").replace("€", "Ae").replace("š", "oe").replace("…", "Oe")
				.replace("Ÿ", "ue").replace("†", "Ue").replace("§", "ss").replace("\"", "");
	}
}
