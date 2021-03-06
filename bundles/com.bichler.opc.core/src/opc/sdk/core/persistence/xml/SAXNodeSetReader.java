package opc.sdk.core.persistence.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.ReferenceNode;

import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.context.StringTable;
import opc.sdk.core.types.TypeTable;

public class SAXNodeSetReader {
	private ICancleOperation progressMonitor;
	/**
	 * Flag to read a Node Tag.
	 */
	private boolean isNode = false;
	/**
	 * Flag to read a NodeSet Tag.
	 */
	private boolean isNodeSet = false;
	private boolean isUAObject = false;
	/** The ServerNamespace Table to import */
	private NamespaceTable serverNamespaceTable = null;
	/** A Typetable from the imported Node Types */
	private TypeTable serverTypeTable = null;
	/** A ServerUri Table to import */
	private StringTable serverUriTable = null;
	/** Map to store all Nodes to create with an AddNodes Service */
	private Map<ExpandedNodeId, AddNodesItem> allNodeItems = null;
	/** List to store all References to create with an AddReference Service */
	private List<AddReferencesItem> allReferenceItems = null;
	/** List to store all Namespaces */
	private NamespaceTable namespacesFromFile = null;
	/** Map to store all References from an imported Node */
	private Map<ExpandedNodeId, List<ReferenceNode>> referencesPerNode = null;
	/**
	 * REC
	 */
	private List<List<Integer>> cachedDimensions = new ArrayList<>();

	/**
	 * SAX Parser to import OPC Nodes. This parser uses the server namespace table
	 * and the server uri table to append maybe new read namespace uris or server
	 * uris. The server typetable is used, to fetch existing hierachical references.
	 * 
	 * @param NamespaceTable Server�s NamespaceTable to add the imported
	 *                       Namespaces.
	 * @param ServerTable    ServerUriTable to add the imported ServerUris.
	 * @param TypeTable      Server�s TypeTable to add the imported Types.
	 */
	public SAXNodeSetReader(NamespaceTable namespaceTable, StringTable serverTable, TypeTable typeTable) {
		this.serverNamespaceTable = namespaceTable;
		this.serverUriTable = serverTable;
		this.serverTypeTable = typeTable;
		this.allNodeItems = new HashMap<>();
		this.allReferenceItems = new ArrayList<>();
		this.namespacesFromFile = new NamespaceTable();
		this.referencesPerNode = new HashMap<>();
		// init simple values
		this.cachedDimensions = new ArrayList<>();
	}

	/**
	 * Creates the OPC Nodes from the XML file. Returns an Entry of AddNodesItem and
	 * AddReferencesItem, which contains all Nodes and References to add. Returns
	 * Null if an error occures.
	 * 
	 * @param xmlFilePath
	 * @return
	 */
	public OPCEntry<List<AddNodesItem>, List<AddReferencesItem>> createNodes(String xmlFilePath) {
		XMLEventReader reader = null;
		File fileToParse = null;
		FileInputStream fis = null;
		try {
			fileToParse = new File(xmlFilePath);
			// open the document
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			fis = new FileInputStream(fileToParse);
			reader = inputFactory.createXMLEventReader(fis, "UTF-8");
			while (reader.hasNext()) {
				if (checkMonitorChancled()) {
					return null;
				}
				// reads element
				XMLEvent evt = null;
				try {
					evt = reader.nextEvent();
					if (evt == null)
						throw new NullPointerException("Next event is null!");
				} catch (XMLStreamException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				if (evt == null)
					throw new NullPointerException("Next event is null!");
				// start element
				if (evt.isStartElement()) {
					StartElement element = evt.asStartElement();
					// Element is a Node Element
					openFlagTag(element);
				}
				// character for data
				else if (evt.isCharacters()) {
					setTagData(evt);
				}
				// end element
				else if (evt.isEndElement()) {
					EndElement element = evt.asEndElement();
					closeFlagTag(element);
				}
				evt = null;
			}
		}
		// Appea
		catch (NullPointerException npe) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
			return null;
		} catch (FileNotFoundException | XMLStreamException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		// dispose the parser
		finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (XMLStreamException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			this.dispose();
		}
		return null;
	}

	/**
	 * Close the flags because a Tag is closed in the XML file.
	 * 
	 * @param Element Element that is closed.
	 */
	void closeFlagTag(EndElement element) {
		if ("UANodeSet".equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNodeSet = false;
		} else if ("UAObject".equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isUAObject = false;
		}
	}

	/**
	 * Sets a tag data to the given node attribute, or create collections to cache
	 * information of a node.
	 * 
	 * @param Evt SAX Event to get the data of the Tag.
	 */
	void setTagData(XMLEvent evt) {
		if (isUAObject) {
			Characters chars = evt.asCharacters();
		}
	}

	public void setProgressMonitor(ICancleOperation monitor) {
		this.progressMonitor = monitor;
	}

	/**
	 * Set the Flags, that the tag is opened, which is currently read with the
	 * parser.
	 * 
	 * @param Element An opened Tag from the File.
	 */
	void openFlagTag(StartElement element) {
		if (this.isNode) {
			// fetchTagOfNodeAttributes(element);
		} else if ("UANodeSet".equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNodeSet = true;
		} else if ("UAObject".equalsIgnoreCase(element.getName().getLocalPart())) {
			String symbolicName = element.getAttributeByName(new QName("SymbolicName")).getValue();
			String browseName = element.getAttributeByName(new QName("BrowseName")).getValue();
			String nodeId = element.getAttributeByName(new QName("NodeId")).getValue();
			System.out.println(symbolicName);
			System.out.println(browseName);
			System.out.println(nodeId);
			System.out.println();
			this.isUAObject = true;
		}
	}

	/**
	 * Disposes the parsers instance variables which are filled with values.
	 */
	private void dispose() {
		// this.allNodeItems = null;
		// this.allReferenceItems = null;
		// this.namespacesFromFile = null;
		// this.referencesPerNode = null;
	}

	private boolean checkMonitorChancled() {
		if (this.progressMonitor != null && this.progressMonitor.isCanceled()) {
			return true;
		}
		return false;
	}
}
