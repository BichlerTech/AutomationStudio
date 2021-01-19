package com.bichler.astudio.editor.siemens.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Path;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;

public class SiemensModelParser2 {
	private static final String SEPERATOR_SYMBOLNAME_BEGIN = "[";
	private static final String SEPERATOR_SYMBOLNAME_END = "]";
	private static final String SEPERATOR_SYMBOLNAME = ".";
	public static final String DATATYPE_ARRAY = "ARRAY";
	public static final String DATATYPE_STRUCT = "STRUCT";
	public static final String DATATYPE_END_STRUCT = "END_STRUCT";
	private SiemensStructNode root = null;
	private int lineCount = 0;
	private String rootName = "";

	protected SiemensModelParser2() {
	}

	public void fillUDT(SiemensDBResourceManager structureManager) {
		if (this.root == null) {
			return;
		}
		fillUDT(structureManager, this.root);
	}

	public AbstractSiemensNode generate(boolean isDBFile) {
		this.root.setName(this.rootName);
		this.root.setRoot(true);
		if (isDBFile) {
			generateSymbolNames("", this.root.children);
		}
		return this.root;
	}

	public void readLineUDT(SiemensDBResourceManager structureManager, String csvFile, BufferedReader reader)
			throws IOException {
		if (this.root == null) {
			// this.current =
			this.root = new SiemensStructNode(structureManager);
		}
		String line = "";
		// ### elements counter
		int pn_elecnt = 0;
		// ###### Pattern
		String pn_pat1_1 = "bool|byte|int|dint|word|dword|real|string|";
		String pn_pat1_2 = "Bool|Byte|Int|DInt|Word|DWord|Real|String|";
		String pn_pat1_3 = "BOOL|BYTE|INT|DINT|WORD|DWORD|REAL|STRING";
		String pn_pat1 = pn_pat1_1 + pn_pat1_2 + pn_pat1_3;
		String pn_pat2 = "([\"]{0,}[a-zA-Z0-9][a-zA-Z0-9_-]{0,}[\"]{0,})";
		String pn_pat3 = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ (" + pn_pat1 + ")";
		String pn_pat3_3 = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ (" + pn_pat2 + ")";
		int pn_sio = 1;
		while ((line = reader.readLine()) != null) {
			lineCount++;
			line = line.replaceAll("\"", "");
			if (this.lineCount == 1) {
				String name = new Path(csvFile).lastSegment();
				name = name.replace("." + new Path(csvFile).getFileExtension(), "");
				if (!name.toUpperCase().startsWith("DB")) {
					this.rootName = name;
				}
			}
			// ####################################################################################################
			// ### 2: TYPE (SCALAR , element of ARRAY , NO struct)
			else {
				AbstractSiemensNode node = null;
				// ### Felder: Active , Name , Type , Index , Value
				String A = line.split(";")[0].trim(); // $(echo "${line}" | cut
				// -d ';' -f 1)
				String N = line.split(";")[1].trim(); // $(echo "${line}" | cut
				// -d ';' -f 2)
				String T = line.split(";")[2].trim(); // T=$(echo "${line}" |
				// cut -d ';' -f 3)
				// String I = line.split(";")[3].trim(); // I=$(echo "${line}" |
				// cut -d ';' -f 4)
				Helper1 helper1 = new Helper1();
				helper1.initValue = line.split(";")[4].trim(); // V=$(echo
				// "${line}"
				// |
				// cut -d ';' -f
				// 5)
				A = "true";
				// insert space to array value
				if (T != null && T.startsWith("Array")) {
					char space = T.charAt(5);
					if (space != ' ') {
						T = T.replace("Array", "Array ");
					}
				}
				if (T.matches(pn_pat3)) { // && pn_aio == 0) {
					node = new SiemensArrayNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					String elecnt = T.toUpperCase().replace("ARRAY [", "");
					elecnt = elecnt.substring(0, elecnt.indexOf("]"));
					String[] elements = elecnt.replace(" ", "").split("\\.\\.");
					pn_elecnt = Integer.parseInt(elements[1]) - Integer.parseInt(elements[0]); // "${BASH_REMATCH[2]}"
					int begin = Integer.parseInt(elements[0]);
					int end = Integer.parseInt(elements[1]);
					pn_elecnt = end - begin + 1;
					T = T.split(" ")[3];
					// write all initial values
					for (int i = 0; i < pn_elecnt; i++) {
						AbstractSiemensNode entryNode = new SiemensEntryNode(structureManager);
						entryNode.setName("" + (begin + i));
						entryNode.setDataType(T);
						SiemensDataValueFactory type = SiemensDataValueFactory.createDatatype(T);
						node.setInitValue(type.getDefaultValue());
						node.addChild(entryNode);
					}
					pn_elecnt = 0;
					this.root.addChild(node);
				} else if (T.matches(pn_pat3_3)) {
					node = new SiemensArrayNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					String elecnt = T.toUpperCase().replace("ARRAY [", "");
					elecnt = elecnt.substring(0, elecnt.indexOf("]"));
					String[] elements = elecnt.replace(" ", "").split("\\.\\.");
					int begin = Integer.parseInt(elements[0]);
					int end = Integer.parseInt(elements[1]);
					pn_elecnt = end - begin + 1; // "${BASH_REMATCH[2]}"
					T = T.split(" ")[3];
					// write all initial values
					for (int i = 0; i < pn_elecnt; i++) {
						AbstractSiemensNode entryNode = new SiemensStructNode(structureManager);
						entryNode.setName("" + (begin + i));
						entryNode.setDataType(T);
						SiemensDataValueFactory type = SiemensDataValueFactory.createDatatype(T);
						node.setInitValue(type.getDefaultValue());
						node.addChild(entryNode);
					}
					pn_elecnt = 0;
					this.root.addChild(node);
				} else if (N.contains("[") && N.contains("]")) {
					// we found an array item
					continue;
				} else if (T.matches(pn_pat1)) {
					node = new SiemensEntryNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					node.setInitValue(helper1.initValue);
					this.root.addChild(node);
				} else if (T.matches(pn_pat2)) {
					node = new SiemensStructNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					this.root.addChild(node);
				} else {
					node = new SiemensStructNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					this.root.addChild(node);
				}
				node.setActive((A.equals("true") ? true : false));
			}
		}
	}

	public void readLine(SiemensDBResourceManager structureManager, String csvFile, BufferedReader reader)
			throws IOException {
		if (this.root == null) {
			// this.current =
			this.root = new SiemensStructNode(structureManager);
		}
		String line = "";
		int pn_elecnt = 0;
		// ####################################################################################################
		// ###### Pattern
		String pn_pat1_1 = "bool|byte|int|dint|word|dword|real|string|";
		String pn_pat1_2 = "Bool|Byte|Int|DInt|Word|DWord|Real|String|";
		String pn_pat1_3 = "BOOL|BYTE|INT|DINT|WORD|DWORD|REAL|STRING";
		String pn_pat1 = pn_pat1_1 + pn_pat1_2 + pn_pat1_3;
		String pn_pat2 = "([\"]{0,}[a-zA-Z0-9][a-zA-Z0-9_-]{0,}[\"]{0,})";
		String pn_pat3 = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ (" + pn_pat1 + ")";
		String pn_pat3_3 = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ (" + pn_pat2 + ")";
		int pn_sio = 1;
		// String pn_initvalue = "";
		while ((line = reader.readLine()) != null) {
			lineCount++;
			line = line.replaceAll("\"", "");
			if (this.lineCount == 1) {
				String pn_type = line.split(";")[0].trim();
				this.root.setAddress(pn_type);
				String name = new Path(csvFile).lastSegment();
				name = name.replace("." + new Path(csvFile).getFileExtension(), "");
			}
			// ####################################################################################################
			// ### 2: TYPE (SCALAR , element of ARRAY , NO struct)
			else {
				AbstractSiemensNode node = null;
				// ### Felder: Active , Name , Type , Index , Value
				String A = line.split(";")[0].trim(); // $(echo "${line}" | cut
				// -d ';' -f 1)
				String N = line.split(";")[1].trim(); // $(echo "${line}" | cut
				// -d ';' -f 2)
				String T = line.split(";")[2].trim(); // T=$(echo "${line}" |
				// cut -d ';' -f 3)
				String I = line.split(";")[3].trim(); // I=$(echo "${line}" |
				// cut -d ';' -f 4)
				Helper1 helper1 = new Helper1();
				helper1.initValue = line.split(";")[4].trim();
				A = "true";
				if (T != null && T.startsWith("Array")) {
					char space = T.charAt(5);
					if (space != ' ') {
						T = T.replace("Array", "Array ");
					}
				}
				if (T.matches(pn_pat3)) {
					node = new SiemensArrayNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					node.setIndex(Float.parseFloat(I));
					// elif [[ "${N}" =~ ${pn_pat4} && "${pn_aio}" -eq 0 ]];
					// then
					String elecnt = T.toUpperCase().replace("ARRAY [", "");
					elecnt = elecnt.substring(0, elecnt.indexOf("]"));
					String[] elements = elecnt.replace(" ", "").split("\\.\\.");
					pn_elecnt = Integer.parseInt(elements[1]) - Integer.parseInt(elements[0]); // "${BASH_REMATCH[2]}"
					int begin = Integer.parseInt(elements[0]);
					int end = Integer.parseInt(elements[1]);
					pn_elecnt = end - begin + 1;
					T = T.split(" ")[3];
					for (int i = 0; i < pn_elecnt; i++) {
						AbstractSiemensNode entryNode = new SiemensEntryNode(structureManager);
						entryNode.setName("" + (begin + i));
						entryNode.setDataType(T);
						SiemensDataValueFactory type = SiemensDataValueFactory.createDatatype(T);
						node.setInitValue(type.getDefaultValue());
						node.addChild(entryNode);
					}
					pn_elecnt = 0;
					this.root.addChild(node);
				} else if (T.matches(pn_pat3_3)) {
					node = new SiemensArrayNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					node.setIndex(Float.parseFloat(I));
					String elecnt = T.toUpperCase().replace("ARRAY [", "");
					elecnt = elecnt.substring(0, elecnt.indexOf("]"));
					String[] elements = elecnt.replace(" ", "").split("\\.\\.");
					int begin = Integer.parseInt(elements[0]);
					int end = Integer.parseInt(elements[1]);
					pn_elecnt = end - begin + 1; // "${BASH_REMATCH[2]}"
					T = T.split(" ")[3];
					for (int i = 0; i < pn_elecnt; i++) {
						AbstractSiemensNode entryNode = new SiemensStructNode(structureManager);
						entryNode.setName("" + (begin + i));
						entryNode.setDataType(T);
						SiemensDataValueFactory type = SiemensDataValueFactory.createDatatype(T);
						node.setInitValue(type.getDefaultValue());
						node.addChild(entryNode);
					}
					pn_elecnt = 0;
					this.root.addChild(node);
				} else if (N.contains("[") && N.contains("]")) {
					// we found an array item
					continue;
				} else if (T.matches(pn_pat1)) {
					node = new SiemensEntryNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					node.setIndex(Float.parseFloat(I));
					node.setInitValue(helper1.initValue);
					// pn_NC++;
					this.root.addChild(node);
				} else if (T.matches(pn_pat2)) {
					node = new SiemensStructNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					node.setIndex(Float.parseFloat(I));
					this.root.addChild(node);
				} else { // we found an user defined element
					node = new SiemensStructNode(structureManager);
					node.setName(N);
					node.setDataType(T);
					node.setIndex(Float.parseFloat(I));
					this.root.addChild(node);
				}
				node.setActive((A.equals("true") ? true : false));
			}
		}
	}

	private boolean checkForArrayType(String T) {
		String pn_pat1_1 = "bool|byte|int|dint|word|dword|real|string|";
		String pn_pat1_2 = "Bool|Byte|Int|DInt|Word|DWord|Real|String|";
		String pn_pat1_3 = "BOOL|BYTE|INT|DINT|WORD|DWORD|REAL|STRING";
		String pn_pat1 = pn_pat1_1 + pn_pat1_2 + pn_pat1_3;
		// ### pn_pat2: STRUCT
		String pn_pat2 = "([\"]{0,}[a-zA-Z0-9][a-zA-Z0-9_-]{0,}[\"]{0,})";
		String pn_pat3 = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ (" + pn_pat1 + ")";
		String pn_pat3_3 = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ (" + pn_pat2 + ")";
		if (T.matches(pn_pat3)) {
			return true;
		} else if (T.matches(pn_pat3_3)) {
			return true;
		}
		return false;
	}

	private void fillUDT(SiemensDBResourceManager structureManager, AbstractSiemensNode node) {
		// iterate child nodes
		for (AbstractSiemensNode child : node.children) {
			// datatype
			String datatype = child.getDataType();
			// look if datatype is a structure
			AbstractSiemensNode type = structureManager.getStructure(datatype.toUpperCase());
			// base datatype (int, real, word,....)
			if (type == null) {
				// look if datatype is an array
				boolean isArrayType = checkForArrayType(datatype);
				// get a base datatype index
				if (child.isDefaultIndex()) {
					Float index = structureManager.getIndexOfDatatype(datatype.toUpperCase());
					// found an index
					if (index != null) {
						child.setIndex(index);
					} else if (index == null && isArrayType) {
						// continue array types
					} else {
						throw new IllegalArgumentException("No base type defined! " + datatype);
					}
				}
				// fill udt structure
				fillUDT(structureManager, child);
				continue;
			}
			// structure types
			if (child.children.isEmpty()) {
				// clone defined structure
				AbstractSiemensNode cloned = type.cloneNode(false, true);
				cloned.setDefaultIndex(true);
				// add structure to model
				String lastType = null;
				Float structLength = null;
				Float lastIndex = 0f;
				for (int i = 0; i < cloned.children.size(); i++) {
					AbstractSiemensNode clone = cloned.children.get(i);
					// insert cloned structure
					clone.setStructure(true);
					clone.setActive(true);
					child.addChild(clone);
					// find datatype index
					Float index = structureManager.getIndexOfDatatype(clone.getDataType().toUpperCase());
					// look if array type
					boolean isArrayType = checkForArrayType(clone.getDataType());
					// index exists
					if (index != null) {
						// end floating indizes
						String t = clone.getDataType().toUpperCase();
						// use next byte for BYTE datatype if an boolean is
						// first
						if (lastType != null && lastType.equalsIgnoreCase("BOOL") && t.equalsIgnoreCase("BYTE")) {
							lastIndex = (float) Math.ceil(lastIndex);
						} else if (lastType != null && lastType.equalsIgnoreCase("BYTE")
								&& t.equalsIgnoreCase("BOOL")) {
							lastIndex = (float) Math.ceil(lastIndex);
						} else if (lastType != null && lastType.equalsIgnoreCase("BOOL")
								&& !t.equalsIgnoreCase(lastType)) {
							// increase for boolean if byte is full
							if (lastIndex % 2 > 0 && lastIndex % 2 < 1) {
								lastIndex += 1;
							}
							lastIndex = (float) Math.ceil(lastIndex);
						} else if (t.equalsIgnoreCase("BOOL") && (lastType != null && lastType.equalsIgnoreCase(t))) {
							float byteLength = lastIndex % 1;
							// increase for boolean if byte is full
							if (byteLength > 0.7) {
								// next value
								lastIndex = (float) Math.ceil(lastIndex);
							}
						}
						// set address index
						clone.setIndex(lastIndex);
						if (structLength == null) {
							structLength = 0f;
						}
						// floating indizes
						if (t.equalsIgnoreCase("BOOL") && (lastType != null && lastType.equalsIgnoreCase(t))) {
							lastIndex = lastIndex + index;
						}
						// increase floating indizes
						else {
							lastIndex = (float) Math.ceil(lastIndex) + index;
						}
						// set last type
						lastType = clone.getDataType().toUpperCase();
					}
					// is an array type
					else if (isArrayType) {
						if (lastIndex % 2 > 0 && lastIndex % 2 < 1) {
							lastIndex += 1;
						}
						lastIndex = (float) Math.ceil(lastIndex);
						clone.setIndex(lastIndex);
						float ii = 0f;
						String lit = null;
						for (AbstractSiemensNode ic : clone.children) {
							String it = ic.getDataType().toUpperCase();
							if (lit != null && lit.equalsIgnoreCase("BOOL") && !it.equalsIgnoreCase(lit)) {
								ii = (float) Math.ceil(ii);
							}
							if (it.equalsIgnoreCase("BOOL") && (lit != null && lit.equalsIgnoreCase(it))) {
								float byteLength = ii % 1;
								if (byteLength > 0.7) {
									// next value
									ii = (float) Math.ceil(ii);
								} else {
									// increase
									ii += ic.getIndex();
								}
							} else {
								// get struct length
								if (ic instanceof SiemensStructNode) {
									ii = (float) Math.ceil(ii) + ((SiemensStructNode) ic).getStructLength();
								}
								// get type length
								else {
									ii = (float) Math.ceil(ii) + ic.getIndex();
								}
							}
							lit = ic.getDataType().toUpperCase();
						}
						// increase last index
						lastIndex += ii;
						// wrap last index
						if (lastIndex % 2 > 0 && lastIndex % 2 < 1) {
							lastIndex += 1;
						}
						lastIndex = (float) Math.ceil(lastIndex);
					}
					// missing structure
					else {
						// fill udt structure
						fillUDT(structureManager, clone);
						// increase index
						lastIndex += ((SiemensStructNode) clone).getStructLength();
						continue;
					}
					// fill udt structure
					fillUDT(structureManager, clone);
				}
				// define structure length
				if (structLength != null) {
					structLength = lastIndex - structLength;
					((SiemensStructNode) child).setStructLength(structLength);
				} else if (child instanceof SiemensStructNode) {
					structLength = lastIndex;
					((SiemensStructNode) child).setStructLength(structLength);
				}
			}
		}
	}

	/**
	 * Generate all unique symbol node names
	 * 
	 * @param baseSet
	 */
	private void generateSymbolNames(String symbolname, List<AbstractSiemensNode> baseSet) {
		for (AbstractSiemensNode node : baseSet) {
			String extended = "";
			boolean isArray = false;
			if (node.getParent() instanceof SiemensArrayNode) {
				extended += symbolname + SEPERATOR_SYMBOLNAME_BEGIN;
				isArray = true;
			} else {
				if (!symbolname.isEmpty()) {
					extended += symbolname + SEPERATOR_SYMBOLNAME;
				}
			}
			String symbolString = node.getName();
			String extended2 = extended + symbolString;
			if (isArray) {
				extended2 += SEPERATOR_SYMBOLNAME_END;
			}
			node.setSymbolName(extended2);
			generateSymbolNames(extended2, node.children);
		}
	}

	class Helper1 {
		String initValue = "";
		String type = "";
		String clname = "";

		public Helper1() {
		}
	}
}
