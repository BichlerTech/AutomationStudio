package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;


public abstract class BaseXMLGen {

	public static String XMLNS_TYPES = "xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\"";
	public static String XMLNS_XSD = "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"";
	public static String XMLNS_XSI = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
	public static String XMLNS_NODESET = "xmlns=\"http://opcfoundation.org/UA/2011/03/UANodeSet.xsd\"";
	
	public static String UANODESET = "UANodeSet";
	public static String NAMESPACEURIS = "NamespaceUris";
	public static String URI = "Uri";
	public static String MODELS = "Models";
	public static String MODEL = "Model";
	public static String REQUIRED_MODEL = "RequiredModel";
	public static String LAST_MODIFIED = "LastModified";
	public static String MODEL_URI = "ModelUri";
	public static String PUBLICATION_NDATE = "PublicationDate";
	public static String VERSION = "Version";
	public static String ALIASES = "Aliases";
	public static String ALIAS = "Alias";
	
	public BaseXMLGen() {

	}

	public abstract void writeXML(BufferedWriter out, StatemachineNodesetImporter importer, NamespaceTable serverTable,
			NamespaceTable exportTable) throws IOException;

}
