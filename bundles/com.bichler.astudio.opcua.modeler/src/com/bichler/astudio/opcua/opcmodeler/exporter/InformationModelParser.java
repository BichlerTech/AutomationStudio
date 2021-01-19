package com.bichler.astudio.opcua.opcmodeler.exporter;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.bichler.astudio.opcua.opcmodeler.utils.extern.OPCModelDesignerUtil;

/**
 * OPC InformationModel Parser that is used to export an Servers AddressSpace
 * Model. It is exported as an OPC Foundation XML Format Document.
 * 
 * @author Thomas Zöchbauer
 * 
 */
public class InformationModelParser {
	InformationModelParser() {
	}

	/**
	 * Returns a new XML-InformationModelParser.
	 * 
	 * @return InformationModelParser
	 */
	public static InformationModelParser newInstance() {
		return new InformationModelParser();
	}

	/**
	 * Creates a new XML-Document, for the exported InformationModel.
	 * 
	 * @return Document
	 */
	public Document createDocument() {
		// Document
		Document document = null;
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbfac.newDocumentBuilder();
			document = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return document;
	}

	public void writeDocument(Document document, String filePath) {
		// prepare for writing
		Source source = new DOMSource(document);
		// output file
		File file = new File(filePath);
		Result result = new StreamResult(file);
		// Result consresult = new StreamResult(
		// OPCModelDesignerUtil.getMessageConsole());
		// write the dom document to file
		Transformer xformer = null;
		try {
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			xformer.transform(source, result);
			// xformer.transform(source, consresult);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
