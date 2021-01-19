package com.bichler.astudio.editor.siemens.xml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.runtime.Path;

public class SiemensCustomExporter {
	private String pn_initvalue;
	private String pn_vdtype;
	private String pn_clname;
	private String struct_index;

	// # Datenbaustein-Namen : Liste der Namen der .csv Dateien (ohne Endung)
	// #
	// ####################################################################################################
	// ####################################################################################################
	// ####################################################################################################
	public void plc_nodes(String path, String dbname, OutputStream dps, OutputStream output) throws IOException {
		// pn_t1=$(date +%s)
		// #echo -n >/hbin/www/tmp/debug.log
		// echo -n "START" >/hbin/www/tmp/progress
		// ####################################################################################################
		// ###### DATA_NODES.XML + DATAPOINTS.COM
		// ### csv-Row-Counter
		int pn_RC = 0;
		// ### Datenbaustein-Name
		String pn_DB = dbname;
		// ### Datenbaustein-Number
		String pn_DN = "0";
		// ### Datenbaustein-Counter
		int pn_DC = 0;
		// ### ns-Counter (ns=[012] already exist)
		int pn_NS = 2;
		// ### Datenbaustein-Node-Counter (i=)
		int pn_NC = 0;
		// ### valuedatetype
		// String pn_vdtype = "";
		// ### classname
		// ### number of array elements
		int pn_arrdim = 0;
		// ### index of last array element (arrdim-1)
		int pn_elelst = 0;
		// ### array elements counter
		int pn_elecnt = 0;
		// ### number of lines in .csv
		int pn_LC = 0;
		// ### character for progress
		String pn_PR = "";
		// ####################################################################################################
		// ###### Pattern
		// ### pn_pat0: Erste Zeile - Nummber
		String pn_pat0 = "([1-9][0-9]{0,})([\\;]{1,})";
		// ### pn_pat1: Primitive Typen
		String pn_pat1_1 = "bool|byte|int|dint|word|dword|real|string|";
		String pn_pat1_2 = "Bool|Byte|Int|DInt|Word|DWord|Real|String|";
		String pn_pat1_3 = "BOOL|BYTE|INT|DINT|WORD|DWORD|REAL|STRING";
		String pn_pat1 = pn_pat1_1 + pn_pat1_2 + pn_pat1_3;
		// ### pn_pat2: STRUCT
		String pn_pat2 = "([\"]{0,}[a-zA-Z0-9][a-zA-Z0-9_-]{0,}[\"]{0,})";
		// ### pn_pat3: nur ARRAY von Primitive Typen (pn_pat1)
		String pn_pat3 = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ (" + pn_pat1 + ")";
		// #pn_pat3="[\"]{0,}Array\ \[0\.\.([1-9][0-9]{0,})\]\ of\
		// (${pn_pat1}|${pn_pat2})"
		// ### pn_pat4: [zahl] in name
		String pn_pat4 = "([([0-9]|[1-9][0-9]{1,})])";
		// ####################################################################################################
		// ### pn_[sa]io=1: STRUCT oder ARRAY is open: FALSE 1
		int pn_sio = 1;
		// int pn_aio = 1;
		// ####################################################################################################
		// pn_DB = "DB0";
		BufferedReader reader = null;
		try {
			xml_header(output, pn_DB); // "${@}"
			com_header(dps);
			// new Path(path).append(dbname+".csv").toFile()
			reader = new BufferedReader(new FileReader(new Path(path).append(pn_DB + ".csv").toFile()));
			String line = "";
			// ####################################################################################################
			while ((line = reader.readLine()) != null) {
				// ####################################################################################################
				// ### csv-Row-Counter
				pn_RC++;
				// ####################################################################################################
				//
				// test "${A}" = "x" && A="true" || A="false"
				// ####################################################################################################
				// ### 1: Nummer der DB
				if (pn_RC == 1) {
					// if [[ "${line}" =~ ^${pn_pat0}$ ]]; then
					// pn_DB="${1}" ; shift
					// pn_DN=$(echo "${line}" | cut -d ';' -f 1)
					String pn_type = line.split(";")[0].trim();
					pn_DC++;
					pn_NS++;
					pn_NC = 1;
					if (pn_type.toUpperCase().compareTo("DB") == 0) {
						pn_DN = line.split(";")[1].trim();
						// we create an object
						xml_objectnode(output, pn_DB, pn_NS, pn_NC); // "${pn_DB}"
						// "${pn_NS}"
						// "${pn_NC}"
					} else {
						// we create an udt
						xml_objecttypenode(output, pn_DB, pn_NS, pn_NC);
					}
					// debug 1__
				}
				// ####################################################################################################
				// ### 2: TYPE (SCALAR , element of ARRAY , NO struct)
				else {
					// ### Felder: Active , Name , Type , Index , Value
					String A = line.split(";")[0].trim(); // $(echo "${line}" |-
					// cut
					// -d ';' -f 1)
					String N = line.split(";")[1].trim(); // $(echo "${line}" |
					// cut
					// -d ';' -f 2)
					String T = line.split(";")[2].trim(); // T=$(echo "${line}"
					// |
					// cut -d ';' -f 3)
					String I = line.split(";")[3].trim(); // I=$(echo "${line}"
					// |
					// cut -d ';' -f 4)
					pn_initvalue = line.split(";")[4].trim(); // V=$(echo
					// "${line}"
					// |
					// cut -d ';' -f
					// 5)
					if (A.toLowerCase().compareTo("x") == 0)
						A = "true";
					else
						A = "false";
					if (T.matches(pn_pat3)) { // && pn_aio == 0) {
												// elif [[ "${N}" =~ ${pn_pat4} && "${pn_aio}" -eq 0 ]];
												// then
						String elecnt = T.toUpperCase().replace("ARRAY [", "");
						elecnt = elecnt.substring(0, elecnt.indexOf("]"));
						String[] elements = elecnt.replace(" ", "").split("\\.\\.");
						pn_elecnt = Integer.parseInt(elements[1]) - Integer.parseInt(elements[0]); // "${BASH_REMATCH[2]}"
						T = T.split(" ")[3];
						// if (pn_elecnt < pn_elelst) {
						// // if test "${pn_elecnt}" -lt "${pn_elelst}"; then
						// xml_node_value_array_element(pn_vdtype, V);
						// // debug 221
						// } else if (pn_elecnt == pn_elelst) {
						// else if test "${pn_elecnt}" -eq "${pn_elelst}";
						// then
						set_XML_vdtype_clname(T);
						pn_NC++;
						xml_node_header(output, N, pn_NS, pn_NC);
						xml_node_value_array_header(output, pn_elecnt, pn_clname);
						// write all initial values
						for (int i = 0; i < pn_elecnt; i++) {
							xml_node_value_array_element(output, pn_vdtype, pn_initvalue);
						}
						xml_node_value_array_footer(output);
						xml_node_footer(output);
						com_datapoint(dps, pn_DB, pn_NC, N, A, pn_DN, "ARRAY_ARRAY", I, T, "[" + pn_elecnt + "]");
						// # setze ARRAY is open: FALSE 1
						// pn_aio = 1;
						pn_arrdim = 0;
						pn_elelst = 0;
						pn_elecnt = 0;
						// debug 222
						// }
					} else if (N.contains("[") && N.contains("]")) {
						// we found an array item
						continue;
					} else if (T.matches(pn_pat1)) {
						// [[ "${T}" =~ ^${pn_pat1}$ ]]; then
						// ### valuedatatype + classname
						set_XML_vdtype_clname(T);
						// ### SCALAR
						// if (!N.matches(pn_pat4) && pn_aio == 1) {
						// if [[ ! "${N}" =~ ${pn_pat4} && "${pn_aio}" -eq 1 ]];
						// then
						pn_NC++;
						xml_node_header(output, N, pn_NS, pn_NC); // "${N:-noname}"
						// "${pn_NS}"
						// "${pn_NC}"
						xml_node_value_scalar(output, pn_vdtype, pn_initvalue); // "${pn_vdtype}"
						// "${V:-novalue}"
						xml_node_footer(output);
						com_datapoint(output, pn_DB, pn_NC, N, A, pn_DN, "SCALAR", I, T, ""); // "${pn_DB:-noDB}"
						// "${pn_NC:-noNC}"
						// "${N:-noname}"
						// "${A}"
						// "${pn_DN:-noDN}"
						// "SCALAR"
						// "${I:-0}"
						// "${T}"
						// ''
						// ### element of ARRAY
					} else if (T.matches(pn_pat2)) {
						// [[ "${T}" =~ ^${pn_pat2}$ ]]; then
						// # pn_sio=0: STRUCT is open: TRUE 0
						if (pn_sio == 0) { // test "${pn_sio}" -eq 0; then
							struct_index = I; // fi
						}
						// # pn_sio=1: STRUCT is open: FALSE 1
						if (pn_sio == 1) {
							// test "${pn_sio}" -eq 1; then struct_index="${I}";
							// fi
							struct_index = I;
						}
					} else { // we found an user defined element
						pn_NC++;
						xml_node_header(output, N, pn_NS, pn_NC);
						xml_node_footer(output);
					}
					// else
					// ### no SCALAR , no element of ARRAY , STRUCT ???
					// ####################################################################################################
					// ### 3: ARRAY: "Array [n..m] of ""TYPE|STRUCT"""
					// else if (!T.matches(pn_pat3)) {// [[ "${T}" =~
					// ^${pn_pat3}$
					// ]];
					// // then
					// // ### valuedatatype + classname
					//
					// T = T.split(" ")[3]; //"${BASH_REMATCH[2]}";
					// set_XML_vdtype_clname(T); // "${T}"
					// // # starte neuen ARRAY, wenn pn_aio=1: ARRAY is open:
					// FALSE
					// // 1
					// if (pn_aio == 1) {
					// // if test "${pn_aio}" -eq 1; then
					// pn_NC++;
					// // pn_elelst="${BASH_REMATCH[1]}";
					//
					// // pn_arrdim=$((${BASH_REMATCH[1]}+1))
					// // # setze ARRAY is open: TRUE 0
					// pn_aio = 0;
					// xml_node_header(N, pn_NS, pn_NC); // "${N:-noname}"
					// // "${pn_NS}"
					// // "${pn_NC}"
					// xml_node_value_array_header(pn_arrdim, pn_clname); //
					// "${pn_arrdim}"
					// // "${pn_clname}"
					// com_datapoint(pn_DB, pn_NC, N, A, pn_DN, "ARRAY_ARRAY",
					// I, T,
					// pn_arrdim + ""); // "${pn_DB:-noDB}"
					// // "${pn_NC:-noNC}"
					// // "${N:-noname}"
					// // "${A}"
					// // "${pn_DN:-noDN}"
					// // "ARRAY_ARRAY"
					// // "${I:-0}"
					// // "${T}"
					// // "[${pn_arrdim}]"
					// }
					// }
					// ####################################################################################################
					// ### 4: STRUCT
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
		// ### 6: END
		xml_footer(output);
		com_footer(dps);
		// openssl sha1 "${pn_fcom}" | awk '{print $2}' | tr -d '\n' >
		// "${pn_fcom_sha1}"
		// debug 6___
		// ####################################################################################################
		// echo -n "END" >/hbin/www/tmp/progress
		// echo "${pn_LC} rows in $(($(date +%s)-${pn_t1})) sec"
		// return 0
		// ####################################################################################################
	}

	private void xml_objecttypenode(OutputStream output, String dbname, int ns_cnt, int i_cnt) throws IOException {
		// dbname="${1}"
		// ns_cnt="${2}"
		// i_cnt="${3}"
		// #### node mit counter 1
		output.write("<Node i:type=\"ObjectTypeNode\"> \n".getBytes());
		output.write("<NodeId> \n".getBytes());
		// #### node mit counter i=1 (pro DB)
		// #### counter: ns=${pn_NS};i=${pn_NC}
		output.write(("<Identifier>ns=" + ns_cnt + ";i=" + i_cnt + "</Identifier> \n").getBytes());
		output.write("</NodeId> \n".getBytes());
		output.write(("<NodeClass>ObjectType_8</NodeClass> \n").getBytes());
		output.write("<BrowseName> \n".getBytes());
		output.write("<NamespaceIndex>0</NamespaceIndex> \n".getBytes());
		// #### db name
		output.write(("<Name>" + dbname + "</Name> \n").getBytes());
		output.write("</BrowseName> \n".getBytes());
		output.write("<DisplayName> \n".getBytes());
		output.write("<Locale>en</Locale> \n".getBytes());
		// #### db name
		output.write(("<Text>" + dbname + "</Text> \n").getBytes());
		output.write("</DisplayName> \n".getBytes());
		output.write("<Description> \n".getBytes());
		output.write("<Locale>en</Locale> \n".getBytes());
		// #### db name
		output.write(("<Text>" + dbname + "</Text> \n").getBytes());
		output.write("</Description> \n".getBytes());
		output.write("<WriteMask>0</WriteMask> \n".getBytes());
		output.write("<UserWriteMask>0</UserWriteMask> \n".getBytes());
		output.write("<References> \n".getBytes());
		output.write("<ReferenceNode> \n".getBytes());
		output.write("<ReferenceTypeId> \n".getBytes());
		output.write("<Identifier>i=45</Identifier> \n".getBytes());
		output.write("</ReferenceTypeId> \n".getBytes());
		output.write("<IsInverse>true</IsInverse> \n".getBytes());
		output.write("<TargetId> \n".getBytes());
		// #### i=85 bleibt, nur bei <Node> ersetzen
		output.write("<Identifier>i=58</Identifier> \n".getBytes());
		output.write("</TargetId> \n".getBytes());
		output.write("</ReferenceNode> \n".getBytes());
		output.write("</References> \n".getBytes());
		output.write("<IsAbstract>false</IsAbstract> \n".getBytes());
		output.write("</Node> \n".getBytes());
		// #----------------------------------------------------------------------------------------------#
	}

	private void xml_objectnode(OutputStream output, String dbname, int ns_cnt, int i_cnt) throws IOException {
		// dbname="${1}"
		// ns_cnt="${2}"
		// i_cnt="${3}"
		// #### node mit counter 1
		output.write("<Node i:type=\"ObjectNode\"> \n".getBytes());
		output.write("<NodeId> \n".getBytes());
		// #### node mit counter i=1 (pro DB)
		// #### counter: ns=${pn_NS};i=${pn_NC}
		output.write(("<Identifier>ns=" + ns_cnt + ";i=" + i_cnt + "</Identifier> \n").getBytes());
		output.write("</NodeId> \n".getBytes());
		output.write(("<NodeClass>Object_1</NodeClass> \n").getBytes());
		output.write("<BrowseName> \n".getBytes());
		output.write("<NamespaceIndex>0</NamespaceIndex> \n".getBytes());
		// #### db name
		output.write(("<Name>" + dbname + "</Name> \n").getBytes());
		output.write("</BrowseName> \n".getBytes());
		output.write("<DisplayName> \n".getBytes());
		output.write("<Locale>en</Locale> \n".getBytes());
		// #### db name
		output.write(("<Text>" + dbname + "</Text> \n").getBytes());
		output.write("</DisplayName> \n".getBytes());
		output.write("<Description> \n".getBytes());
		output.write("<Locale>en</Locale> \n".getBytes());
		// #### db name
		output.write(("<Text>" + dbname + "</Text> \n").getBytes());
		output.write("</Description> \n".getBytes());
		output.write("<WriteMask>0</WriteMask> \n".getBytes());
		output.write("<UserWriteMask>0</UserWriteMask> \n".getBytes());
		output.write("<References> \n".getBytes());
		output.write("<ReferenceNode> \n".getBytes());
		output.write("<ReferenceTypeId> \n".getBytes());
		output.write("<Identifier>i=35</Identifier> \n".getBytes());
		output.write("</ReferenceTypeId> \n".getBytes());
		output.write("<IsInverse>true</IsInverse> \n".getBytes());
		output.write("<TargetId> \n".getBytes());
		// #### i=85 bleibt, nur bei <Node> ersetzen
		output.write("<Identifier>i=85</Identifier> \n".getBytes());
		output.write("</TargetId> \n".getBytes());
		output.write("</ReferenceNode> \n".getBytes());
		output.write("<ReferenceNode> \n".getBytes());
		output.write("<ReferenceTypeId> \n".getBytes());
		output.write("<Identifier>i=40</Identifier> \n".getBytes());
		output.write("</ReferenceTypeId> \n".getBytes());
		output.write("<IsInverse>false</IsInverse> \n".getBytes());
		output.write("<TargetId> \n".getBytes());
		output.write("<Identifier>i=58</Identifier> \n".getBytes());
		output.write("</TargetId> \n".getBytes());
		output.write("</ReferenceNode> \n".getBytes());
		output.write("</References> \n".getBytes());
		output.write("<EventNotifier>1</EventNotifier> \n".getBytes());
		output.write("</Node> \n".getBytes());
		// #----------------------------------------------------------------------------------------------#
	}

	private void xml_header(OutputStream output, String dbname) throws IOException {
		output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n".getBytes());
		output.write(
				"<NodeSet xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\"> \n"
						.getBytes());
		output.write("<NamespaceUris>".getBytes());
		output.write("<String>http://opcfoundation.org/UA/</String> \n".getBytes());
		output.write("<String>urn:localhost:UASDK:AS-Server</String> \n".getBytes());
		output.write("<String>http://opcfoundation.org/UA/Diagnostics</String> \n".getBytes());
		output.write(("<String>http://hbs-siemens/" + dbname + "</String> \n").getBytes());
		// for dbname in "${@}"; do
		// output.write("<String>http://hbs-siemens/'${dbname}'</String>".getBytes());
		// done
		output.write("</NamespaceUris> \n".getBytes());
		output.write("<ServerUris/> \n".getBytes());
		output.write("<Nodes> \n".getBytes());
	}

	// ####################################################################################################
	// ####################################################################################################
	// ####################################################################################################
	private void set_XML_vdtype_clname(String type) {
		// # type: Siemens-Type
		// # vdtype: OPC-Type
		// # clname: Java-Type
		// type="${1}"
		switch (type.toUpperCase()) {
		case "BOOL":
			pn_initvalue = pn_initvalue.isEmpty() ? "false" : pn_initvalue;
			pn_vdtype = "Boolean";
			pn_clname = "java.lang.Boolean";
			break;
		case "BYTE":
			pn_initvalue = pn_initvalue.isEmpty() ? "0" : pn_initvalue;
			pn_vdtype = "Byte";
			pn_clname = "java.lang.Byte";
			break;
		case "INT":
			pn_initvalue = pn_initvalue.isEmpty() ? "0" : pn_initvalue;
			pn_vdtype = "Int16";
			pn_clname = "java.lang.Short";
			break;
		case "DINT":
			pn_initvalue = pn_initvalue.isEmpty() ? "0" : pn_initvalue;
			pn_vdtype = "Int32";
			pn_clname = "java.lang.Integer";
			break;
		case "WORD":
			pn_initvalue = pn_initvalue.isEmpty() ? "0" : pn_initvalue;
			pn_vdtype = "UInt16";
			pn_clname = "org.opcfoundation.ua.builtintypes.UnsignedShort";
			break;
		case "DWORD":
			pn_initvalue = pn_initvalue.isEmpty() ? "0" : pn_initvalue;
			pn_vdtype = "UInt32";
			pn_clname = "org.opcfoundation.ua.builtintypes.UnsignedInteger";
			break;
		case "REAL":
			pn_initvalue = pn_initvalue.isEmpty() ? "0.0" : pn_initvalue;
			pn_vdtype = "Float";
			pn_clname = "java.lang.Float";
			break;
		case "STRING":
			pn_initvalue = pn_initvalue.isEmpty() ? " " : pn_initvalue;
			pn_vdtype = "String";
			pn_clname = "java.lang.String";
			break;
		default:
			break;
		}
	}

	// ####################################################################################################
	private void com_footer(OutputStream dps) throws IOException {
		dps.write("</DPs> \n".getBytes());
		dps.write("</DPConfiguration> \n".getBytes());
	}

	// ####################################################################################################
	private void xml_footer(OutputStream output) throws IOException {
		output.write("</Nodes> \n".getBytes());
		output.write("</NodeSet> \n".getBytes());
	}

	// ####################################################################################################
	// ####################################################################################################
	// ####################################################################################################
	private void com_header(OutputStream dps) throws IOException {
		// echo -n > ${pn_fcom}
		dps.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n".getBytes());
		dps.write("<DPConfiguration> \n".getBytes());
		dps.write("<DPs> \n".getBytes());
	}

	private void xml_node_header(OutputStream output, String name, int ns_cnt, int i_cnt) throws IOException {
		// name="${1}"
		// ns_cnt="${2}"
		// i_cnt="${3}"
		output.write("<Node i:type=\"VariableNode\"> \n".getBytes());
		output.write("<NodeId> \n".getBytes());
		// #### node mit counter i=2,3,4,... (pro DB)
		// #### counter: ns=${pn_NS};i=${pn_NC}
		output.write(("<Identifier>ns=" + ns_cnt + ";i=" + i_cnt + "</Identifier> \n").getBytes());
		output.write("</NodeId> \n".getBytes());
		output.write("<NodeClass>Variable_2</NodeClass> \n".getBytes());
		output.write("<BrowseName> \n".getBytes());
		output.write("<NamespaceIndex>0</NamespaceIndex> \n".getBytes());
		// #### db name
		output.write(("<Name>" + name + "</Name> \n").getBytes());
		output.write("</BrowseName> \n".getBytes());
		output.write("<DisplayName> \n".getBytes());
		output.write("<Locale>en</Locale> \n".getBytes());
		// #### db name
		output.write(("<Text>" + name + "</Text> \n").getBytes());
		output.write("</DisplayName> \n".getBytes());
		output.write("<Description> \n".getBytes());
		output.write("<Locale>en</Locale> \n".getBytes());
		// #### db name
		output.write(("<Text>" + name + "</Text> \n").getBytes());
		output.write("</Description> \n".getBytes());
		output.write("<WriteMask>0</WriteMask> \n".getBytes());
		output.write("<UserWriteMask>0</UserWriteMask> \n".getBytes());
		output.write("<References> \n".getBytes());
		output.write("<ReferenceNode> \n".getBytes());
		output.write("<ReferenceTypeId> \n".getBytes());
		output.write("<Identifier>i=47</Identifier> \n".getBytes());
		output.write("</ReferenceTypeId> \n".getBytes());
		output.write("<IsInverse>true</IsInverse> \n".getBytes());
		output.write("<TargetId> \n".getBytes());
		output.write(("<Identifier>ns=" + ns_cnt + ";i=1</Identifier> \n").getBytes());
		// # output.write("<Identifier>i=85</Identifier>".getBytes());
		output.write("</TargetId> \n".getBytes());
		output.write("</ReferenceNode> \n".getBytes());
		output.write("<ReferenceNode> \n".getBytes());
		output.write("<ReferenceTypeId> \n".getBytes());
		output.write("<Identifier>i=40</Identifier> \n".getBytes());
		output.write("</ReferenceTypeId> \n".getBytes());
		output.write("<IsInverse>false</IsInverse> \n".getBytes());
		output.write("<TargetId> \n".getBytes());
		output.write("<Identifier>i=63</Identifier> \n".getBytes());
		output.write("</TargetId> \n".getBytes());
		output.write("</ReferenceNode> \n".getBytes());
		output.write("<ReferenceNode> \n".getBytes());
		output.write("<ReferenceTypeId> \n".getBytes());
		output.write("<Identifier>i=37</Identifier> \n".getBytes());
		output.write("</ReferenceTypeId> \n".getBytes());
		output.write("<IsInverse>false</IsInverse> \n".getBytes());
		output.write("<TargetId> \n".getBytes());
		output.write("<Identifier>i=78</Identifier> \n".getBytes());
		output.write("</TargetId> \n".getBytes());
		output.write("</ReferenceNode> \n".getBytes());
		output.write("</References> \n".getBytes());
		output.write("<AccessLevel>3</AccessLevel> \n".getBytes());
	}

	// ####################################################################################################
	private void xml_node_footer(OutputStream output) throws IOException {
		output.write("</Node> \n".getBytes());
	}

	// ####################################################################################################
	private void xml_node_value_array_footer(OutputStream output) throws IOException {
		int valuerank = 1;
		output.write("</Elements> \n".getBytes());
		output.write("</Matrix> \n".getBytes());
		output.write("</Value> \n".getBytes());
		output.write("</Value> \n".getBytes());
		// #----------------------------------------------------------------------------------------------#
		// #### valuerank: ARRAY 1
		output.write(("<ValueRank>" + valuerank + "</ValueRank> \n").getBytes());
	}

	// ####################################################################################################
	private void com_datapoint(OutputStream dps, String dbname, int i_cnt, String name, String active, String dbnumb,
			String mapping, String index, String datatype, String arraydim) throws IOException {
		// dbname="${1}"
		// i_cnt="${2}"
		// name="${3}"
		// active="${4}"
		// dbnumb="${5}"
		// mapping="${6}"
		// index="${7}"
		// datatype=$(echo "${8}" | tr '[a-z]' '[A-Z]')
		datatype = datatype.toUpperCase().trim();
		// arraydim="${9}"
		dps.write("<dp> \n".getBytes());
		// # dbname: $pn_DB , i_cnt: $pn_NC (von VariableNode)
		dps.write(("<nodeid value=\"ns=http://hbs-siemens/" + dbname + ";i=" + i_cnt + "\" /> \n").getBytes());
		// # name: $N
		dps.write(("<symbolname value=\"" + name + "\" /> \n").getBytes());
		// # active: $A
		dps.write(("<isactive value=\"" + active + "\" /> \n").getBytes());
		dps.write("<cycletime value=\"1000\" /> \n".getBytes());
		dps.write("<addresstype value=\"DB\" /> \n".getBytes());
		// # dbnumb: $pn_DN
		dps.write(("<address value=\"" + dbnumb + "\" /> \n").getBytes());
		// # mapping: SCALAR oder ARRAY_ARRAY (oder SCALAR_ARRAY)
		dps.write(("<mapping value=\"" + mapping + "\" /> \n").getBytes());
		// # index: $I
		dps.write(("<index value=\"" + index + "\" /> \n").getBytes());
		// # datatype: ${pn_vdtype} (SCALAR Siemens-Data-Type)
		// # datatype: ${pn_vdtype}[$arrdim] (ARRAY Siemens-Data-Type)
		dps.write(("<datatype value=\"" + datatype + arraydim + "\" /> \n").getBytes());
		dps.write("<description value=\"insert comment\" /> \n".getBytes());
		dps.write("</dp> \n".getBytes());
	}

	// ####################################################################################################
	private void xml_node_value_scalar(OutputStream output, String type, String value) throws IOException {
		// type="${1}"
		// value="${2}"
		int valuerank = -1;
		// #### SCALAR
		output.write("<ArrayDimension/> \n".getBytes());
		// #### SCALAR + ARRAY
		output.write("<DataType> \n".getBytes());
		output.write("<Identifier>i=24</Identifier> \n".getBytes());
		output.write("</DataType> \n".getBytes());
		output.write("<Historizing>true</Historizing> \n".getBytes());
		output.write("<MinimumSamplingInterval>1000.0</MinimumSamplingInterval> \n".getBytes());
		output.write("<UserAccessLevel>3</UserAccessLevel> \n".getBytes());
		// #----------------------------------------------------------------------------------------------#
		// #### type + value
		output.write("<Value> \n".getBytes());
		output.write("<Value> \n".getBytes());
		output.write(("<" + type + ">" + value + "</" + type + "> \n").getBytes());
		output.write("</Value> \n".getBytes());
		output.write("</Value> \n".getBytes());
		// #----------------------------------------------------------------------------------------------#
		// #### valuerank: SCALAR -1
		output.write(("<ValueRank>" + valuerank + "</ValueRank> \n").getBytes());
	}

	private void xml_node_value_array_header(OutputStream output, int arrdim, String clname) throws IOException {
		// arrdim=2;
		// String clname="${2}";
		// #### Arrays
		output.write(("<ArrayDimension>" + arrdim + "</ArrayDimension> \n").getBytes());
		// #### SCALAR + ARRAY
		output.write("<DataType> \n".getBytes());
		output.write("<Identifier>i=24</Identifier> \n".getBytes());
		output.write("</DataType> \n".getBytes());
		output.write("<Historizing>true</Historizing> \n".getBytes());
		output.write("<MinimumSamplingInterval>1000.0</MinimumSamplingInterval> \n".getBytes());
		output.write("<UserAccessLevel>3</UserAccessLevel> \n".getBytes());
		// #----------------------------------------------------------------------------------------------#
		output.write("<Value> \n".getBytes());
		output.write("<Value> \n".getBytes());
		output.write("<Matrix> \n".getBytes());
		output.write(("<Dimensions><Int32>" + arrdim + "</Int32></Dimensions> \n").getBytes());
		// #### clname
		output.write(("<Elements className=\"" + clname + "\"> \n").getBytes());
		// #### Elements
	}

	// ####################################################################################################
	private void xml_node_value_array_element(OutputStream output, String type, String value) throws IOException {
		// type="${1}"
		// value="${2}"
		output.write(("<" + type + ">" + value + "</" + type + "> \n").getBytes());
	}
}
