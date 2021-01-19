package com.bichler.astudio.opcua.opcmodeler.singletons.type;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.informationmodel.xml.NodesCategoryTags;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.w3c.dom.Document;

import com.bichler.astudio.opcua.opcmodeler.exporter.InformationModelParser;
import com.bichler.astudio.opcua.opcmodeler.exporter.OPCXMLWriter;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class ServerModelTypeMapper {
	private static final String TYPES = "_types";
	private Map<UAServerApplicationInstance, ServerTypeModel> types = new HashMap<UAServerApplicationInstance, ServerTypeModel>();

	public void exportModelFile(String filepath, InformationModelParser parser, String[] serverUris,
			String[] urisToExport) {
		if (ServerInstance.getTypeModel() != null) {
			Document typeDoc = parser.createDocument();
			OPCXMLWriter writer = new OPCXMLWriter();
			writer.writeModelDocument(typeDoc, urisToExport, serverUris);
			// do we have the opc ua
			writer.writeTypes(typeDoc, ServerInstance.getTypeModel(), urisToExport);
			Path p = new Path(filepath);
			String ls = p.lastSegment();
			String[] lsA = ls.split(".xml");
			String ls2 = lsA[0] + TYPES + ".xml";
			IPath np = p.removeLastSegments(1);
			parser.writeDocument(typeDoc, np.append(ls2).toOSString());
		}
	}

	public void exportModelFile(String filePath, InformationModelParser parser, OPCXMLWriter writer,
			String[] serverUris, String[] namespace) {
		// write type model
		Document typeDocument = parser.createDocument();
		// fill type model
		writer.writeModelDocument(typeDocument, namespace, serverUris);
		writer.writeTypes(typeDocument, ServerInstance.getTypeModel(), namespace);
		// write type model
		Path p = new Path(filePath);
		String ls = p.lastSegment();
		String[] lsA = ls.split(".xml");
		String ls2 = lsA[0] + TYPES + ".xml";
		IPath np = p.removeLastSegments(1);
		parser.writeDocument(typeDocument, np.append(ls2).toOSString());
	}

	public ServerTypeModel getServerModel(UAServerApplicationInstance server) {
		return this.types.get(server);
	}

	public void importModelFile(UAServerApplicationInstance instance, URL... modelFiles) {
		ServerTypeModel typeModel = types.get(instance);
		if (typeModel == null) {
			typeModel = new ServerTypeModel();
			types.put(instance, typeModel);
		}
		SAXBuilder builder = new SAXBuilder();
		Map<ExpandedNodeId, ExpandedNodeId> typeMapping = new HashMap<>();
		for (URL url : modelFiles) {
			try {
				File f = new File(url.toURI());
				Path fPath = new Path(f.getPath());
				String ls = fPath.lastSegment();
				String[] ls2 = ls.split(".xml");
				String ls3 = ls2[0] + TYPES + ".xml";
				IPath fPath2 = fPath.removeLastSegments(1);
				File modelTypes = fPath2.append(ls3).toFile();
				if (!modelTypes.exists()) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING,
							"There is no model types file for informationmodel: " + f.getAbsolutePath());
					continue;
				}
				org.jdom.Document document = builder.build(modelTypes);
				Element root = document.getRootElement();
				List<String> namespaces = new ArrayList<>();
				List<String> serverUris = new ArrayList<>();
				NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
				Element nsUris = getChildIgnoreCase(root, NodesCategoryTags.NamespaceUris.name());
				List<?> nsChildren = nsUris.getChildren();
				for (Object child : nsChildren) {
					namespaces.add(((Element) child).getText());
				}
				Element sUris = getChildIgnoreCase(root, NodesCategoryTags.ServerUris.name());
				List<?> sUriChildren = sUris.getChildren();
				for (Object child : sUriChildren) {
					serverUris.add(((Element) child).getText());
				}
				Element nodes = getChildIgnoreCase(root, NodesCategoryTags.Nodes.name());
				List<?> nodeMapping = nodes.getChildren();
				for (Object node : nodeMapping) {
					// attributes
					try {
						String sourceId = ((Element) node).getAttributeValue("sourceId", Namespace.NO_NAMESPACE);
						String targetId = ((Element) node).getAttributeValue("targetId", Namespace.NO_NAMESPACE);
						NodeId decodedSource = NodeId.parseNodeId(sourceId);
						NodeId decodedTarget = NodeId.parseNodeId(targetId);
						// change nodeid
						String sourceNs = namespaces.get(decodedSource.getNamespaceIndex());
						String targetNs = namespaces.get(decodedTarget.getNamespaceIndex());
						int sourceIndex = nsTable.getIndex(sourceNs);
						int targetIndex = nsTable.getIndex(targetNs);
						NodeId sId = NodeIdUtil.createNodeId(sourceIndex, decodedSource.getValue());
						NodeId tId = NodeIdUtil.createNodeId(targetIndex, decodedTarget.getValue());
						typeMapping.put(
								new ExpandedNodeId(nsTable.getUri(sId.getNamespaceIndex()), sId.getValue(), nsTable),
								new ExpandedNodeId(nsTable.getUri(tId.getNamespaceIndex()), tId.getValue(), nsTable));
					} catch (IndexOutOfBoundsException e1) {
						// skip model entry because do not exist
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
					}
				}
			} catch (JDOMException | URISyntaxException | IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
		for (Entry<ExpandedNodeId, ExpandedNodeId> entry : typeMapping.entrySet()) {
			ServerInstance.getTypeModel().addModelMapping(entry.getKey(), entry.getValue());
		}
	}

	public org.jdom.Element getChildIgnoreCase(org.jdom.Element element, String tagName) {
		List<Element> allChildren = (List<Element>) element.getChildren();
		Iterator<?> iter = allChildren.iterator();
		while (iter.hasNext()) {
			org.jdom.Element nextElement = (org.jdom.Element) iter.next();
			if (nextElement.getName().compareToIgnoreCase(tagName) == 0) {
				return nextElement;
			}
		}
		return null;
	}
}
