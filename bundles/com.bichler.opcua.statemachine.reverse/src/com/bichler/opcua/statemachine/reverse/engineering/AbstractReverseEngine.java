package com.bichler.opcua.statemachine.reverse.engineering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.reverse.IdGenerator;
import com.bichler.opcua.statemachine.reverse.model.IEdgeNotation;
import com.bichler.opcua.statemachine.reverse.model.ReverseEngineFactory;
import com.bichler.opcua.statemachine.reverse.model.ReverseTransformationContext;

import opc.sdk.core.node.Node;

public abstract class AbstractReverseEngine implements IOPCServerEngine {
	private IdGenerator idUMLGenerator;
	private IdGenerator idNotationGenerator;
	private ReverseEngineFactory revModelFactory;

	private Map<NodeId, AbstractMap.SimpleEntry<String, String>> staticIds;

	public AbstractReverseEngine() {
		this.idUMLGenerator = new IdGenerator(17);
		this.idNotationGenerator = new IdGenerator(16);
		this.revModelFactory = new ReverseEngineFactory();

		this.staticIds = new HashMap<>();
	}

	@Override
	public void addUMLStaticId(NodeId nodeId, String umlId, String externalUMLName) {
		AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>(umlId, externalUMLName);
		this.staticIds.put(nodeId, entry);
	}

	public void createBaseModel(IReverseTransformationContext context) throws StatemachineException {
		File[] baseTypeFiles = getUMLBaseTypeFiles();
		for (File copy : baseTypeFiles) {
			File file = new File(context.getDirectoryPath() + File.separator + copy.getName());
			if (file.exists()) {
				file.delete();
			}

			try {
				Files.copy(copy.toPath(), file.toPath());
			} catch (IOException e) {
				StatemachineException se = new StatemachineException(
						"Cannot create an UML model for basic OPC UA types");
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, se.getMessage());
				throw se;
			}
		}
	}

	@Override
	public IReverseTransformationContext createTransformationContext(String directoryPath) {
		return new ReverseTransformationContext(directoryPath);
	}

	@Override
	public void transformTypesModel(IReverseTransformationContext context, String projectName, String[] namespaceUris)
			throws StatemachineException {
		try {
			// OPC UA ObjectTypes
			Map<NodeId, IUMLTranslation> objectTypes = new HashMap<>();
			Node startObjectTypesNode = getNodeById(Identifiers.BaseObjectType);
			objectTypes.putAll(createTranslateModel(projectName, startObjectTypesNode, null, namespaceUris,
					Identifiers.BaseEventType));
			// OPC UA EventTypes
			Map<NodeId, IUMLTranslation> eventTypes = new HashMap<>();
			IUMLTranslation parent = objectTypes.get(Identifiers.BaseObjectType);
			Node startEventTypeNode = getNodeById(Identifiers.BaseEventType);
			eventTypes.putAll(createTranslateModel(projectName, startEventTypeNode, parent, namespaceUris));
			// OPC UA DataTypes
			Map<NodeId, IUMLTranslation> dataTypes = new HashMap<>();
			Node startDataTypeNode = getNodeById(Identifiers.BaseDataType);
			dataTypes.putAll(createTranslateModel(projectName, startDataTypeNode, null, namespaceUris));

			List<AbstractMap.SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>> translations = new ArrayList<>();

			translations.add(new AbstractMap.SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>(
					Identifiers.BaseObjectType, objectTypes));
			translations.add(new AbstractMap.SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>(
					Identifiers.BaseEventType, eventTypes));
			translations.add(new AbstractMap.SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>(Identifiers.BaseDataType,
					dataTypes));

			boolean isEmpty = checkEmptyModel(translations);
			if (isEmpty) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, "Nothing to export, empty model");
				return;
			}
			// If files exist, ask to replace dialog
			// TODO: MessageDialog.openConfirm
			File fileDI = new File(context.getDirectoryPath() + File.separator + projectName + ".di");
			if (fileDI.exists()) {
				fileDI.delete();
			}

			fileDI.createNewFile();

			File fileUML = new File(context.getDirectoryPath() + File.separator + projectName + ".uml");
			if (fileUML.exists()) {
				fileUML.delete();
			}
			fileUML.createNewFile();

			File fileNotation = new File(context.getDirectoryPath() + File.separator + projectName + ".notation");
			if (fileNotation.exists()) {
				fileNotation.delete();
			}
			fileNotation.createNewFile();

			engineer(context, fileDI, fileUML, fileNotation, projectName, namespaceUris, translations);
		} catch (IOException e) {
			StatemachineException se = new StatemachineException(e);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, se.getMessage());
			throw se;
		}
	}

	Map<NodeId, IUMLTranslation> createTranslateModel(String modelId, Node startNode, IUMLTranslation parent,
			String[] namespaceUris, NodeId... exclude) {

		Map<NodeId, IUMLTranslation> map = new HashMap<>();
		rekEngineer(modelId, map, startNode, parent, namespaceUris, exclude);
		return map;
	}

	void engineer(IReverseTransformationContext context, File fileDI, File fileUML, File fileNotation,
			String projectName, String[] namespaceUris,
			List<SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>> translations) throws StatemachineException {

		// print *.di file
		printFileDI(fileDI);
		// print *.uml file
		printFileUML(context, fileUML, projectName, namespaceUris, translations);
		// load uml resources from generated file
		loadUMLResources(fileUML);
		// print *.notation file
		printFileNotation(context, fileNotation, projectName, namespaceUris, translations);
	}

	void rekEngineer(String modelname, Map<NodeId, IUMLTranslation> map, Node node, IUMLTranslation parent,
			String[] namespaceUris, NodeId... exclude) {

		// check if node is excluded
		boolean isExcluded = false;
		for (NodeId excludedNode : exclude) {
			if (excludedNode.equals(node.getNodeId())) {
				isExcluded = true;
				break;
			}
		}

		// stop recursion
		if (isExcluded) {
			return;
		}
		// check if namespace is included
		String umlId = (this.staticIds.get(node.getNodeId()) != null) ? this.staticIds.get(node.getNodeId()).getKey()
				: this.idUMLGenerator.buildId(5);
		String externalResourceId = (this.staticIds.get(node.getNodeId()) != null)
				? this.staticIds.get(node.getNodeId()).getValue()
				: null;

		IUMLTranslation umlModel = null;
		try {
			if (externalResourceId != null) {
				umlModel = this.revModelFactory.createNodeTranslation(externalResourceId, node, umlId, parent);
			} else {
				umlModel = this.revModelFactory.createNodeTranslation(modelname, node, umlId, parent);
			}

			int nsIndex = node.getNodeId().getNamespaceIndex();
			String namespaceUri = namespaceUris[nsIndex];
			// filter node from namespace Uri
			if (namespaceUri != null) {
				map.put(umlModel.getNodeId(), umlModel);
			}
			ExpandedNodeId[] target = findReferences(node, Identifiers.HasSubtype);
			for (ExpandedNodeId expNodeId : target) {
				rekEngineer(modelname, map, getNodeById(expNodeId), umlModel, namespaceUris, exclude);
			}
		} catch (StatemachineException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	private boolean checkEmptyModel(List<SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>> translations) {
		for (int i = 0; i < translations.size(); i++) {
			if (translations.get(i).getValue().size() > 0) {
				return false;
			}
		}

		return true;
	}

	private ExpandedNodeId[] findReferences(Node node, NodeId refId) {
		return node.findTargets(refId, false);
	}

	private boolean hasTranslations(Map<NodeId, IUMLTranslation> translations, int indexToExport) {

		if (translations.isEmpty()) {
			return true;
		}

		for (Entry<NodeId, IUMLTranslation> umlModel : translations.entrySet()) {
			NodeId nodeId = umlModel.getKey();
			if (nodeId.getNamespaceIndex() != indexToExport) {
				continue;
			}
			return false;
		}

		return true;
	}

	private String loadFromResource(IUMLTranslation umlTranslation) {
		return getResource(umlTranslation.getModelname(), umlTranslation.getNodeId());
	}

	private String[] makePackages(String namespaceUri) {
		List<String> packages = new ArrayList<String>();

		String packageName = namespaceUri.replaceFirst("http://", "");
		packages.add(packageName + "_objecttypes");
		packages.add(packageName + "_eventtypes");
		packages.add(packageName + "_datatypes");

		return packages.toArray(new String[0]);
	}

	private void printFileNotation(IReverseTransformationContext context, File file, String projectName,
			String[] namespaceUris, List<AbstractMap.SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>> translations)
			throws StatemachineException {

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			printFileNotationHeader(out);

			for (String namespaceUri : namespaceUris) {
				if (namespaceUri == null) {
					continue;
				}
				int indexToExport = getNamespaceTable().getIndex(namespaceUri);
				String[] segments = makePackages(namespaceUri);

				for (int i = 0; i < segments.length; i++) {
					String segment = segments[i];

					NodeId baseId = translations.get(i).getKey();
					Map<NodeId, IUMLTranslation> map = translations.get(i).getValue();

					boolean isEmpty = hasTranslations(map, indexToExport);
					if (isEmpty) {
						continue;
					}

					// start diagram
					printFileNotationDiagram(out, segment);
					// internal edges to draw
					List<IEdgeNotation> edges = new ArrayList<>();
					// referenced edges to external target models
					Map<NodeId, IEdgeNotation> externalEdges = new HashMap<>();
					// draw model for namespace
					for (Entry<NodeId, IUMLTranslation> umlModel : map.entrySet()) {
						NodeId nodeId = umlModel.getKey();
						if (nodeId.getNamespaceIndex() != indexToExport) {
							continue;
						}

						IEdgeNotation edge = umlModel.getValue().notate(out, this.idNotationGenerator);
						if (edge != null) {
							edges.add(edge);

							boolean isLocalTarget = edge.isLocalTarget();
							// create referenced class to a target model
							if (!isLocalTarget) {
								// do not add dupplicate external classes
								NodeId externalNodeId = edge.getTarget().getNodeId();

								if (!externalEdges.containsKey(externalNodeId)) {
									externalEdges.put(externalNodeId, edge);
									// draw referenced class
									edge.getTarget().notate(out, this.idNotationGenerator);
								}
							}
						}
					}
					// draw model to base
					Map<IEdgeNotation, String> refEdges = new HashMap<>();
					Map<NodeId, IUMLTranslation> refClasses = new HashMap<>();

					for (Entry<NodeId, IEdgeNotation> entry : externalEdges.entrySet()) {
						IEdgeNotation edge = entry.getValue();

						while (true) {
							IUMLTranslation target = edge.getTarget();
							NodeId targetId = target.getNodeId();

							if (!externalEdges.containsKey(targetId) && !refClasses.containsKey(targetId)) {
								// draw external
								refClasses.put(target.getNodeId(), target);
								edge = target.notate(out, this.idNotationGenerator);
							} else {
								edge = target.createEdge(this.idNotationGenerator);
							}
							if (edge == null) {
								break;
							}
							// prevent double refernces to base model if same edge already exist
							boolean foundSame = false;
							for (Entry<IEdgeNotation, String> obj : refEdges.entrySet()) {
								IEdgeNotation refEdge = obj.getKey();
								if (refEdge.getSource().getId().equals(edge.getSource().getId())
										&& refEdge.getTarget().getId().equals(edge.getTarget().getId())) {
									foundSame = true;
									break;
								}
							}
							// already existing reference to base type
							if (foundSame) {
								break;
							}

							String id = loadFromResource(edge.getSource());
							refEdges.put(edge, id);
							if (baseId.equals(edge.getSource().getNodeId())) {
								break;
							}

						}
					}

					String owner = context.mapPackage(segment);
					printFileNotationStyle(out, owner, projectName);
					// draw edges
					for (IEdgeNotation edge : edges) {
						edge.notate(out, this.idNotationGenerator, projectName);
					}

					for (Entry<IEdgeNotation, String> entry : refEdges.entrySet()) {
						entry.getKey().notate(out, this.idNotationGenerator, entry.getKey().getSource().getModelname(),
								entry.getValue());
					}
					// end diagram
					printFileNotationDiagramEnd(out);
				}

			}
			printFileNotationFooter(out);
			out.flush();
		} catch (IOException e) {
			StatemachineException se = new StatemachineException(e);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, se.getMessage());
			throw se;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Cannot close writer to file " + file.getName());
				}
			}
		}
	}

	private void printFileUML(IReverseTransformationContext context, File file, String projectName,
			String[] namespaceUris, List<AbstractMap.SimpleEntry<NodeId, Map<NodeId, IUMLTranslation>>> translations)
			throws StatemachineException {
		// generate uml model id
		String UMLmodelId = this.idUMLGenerator.buildId(5);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			// write *.uml <model....> header
			printFileUMLHeader(out, UMLmodelId, projectName);
			// write importedpackages of *.uml file
			printFileUMLImportedPackage(out);
			// generate packages for OPC UA NamespaceUris
			for (String namespaceUri : namespaceUris) {
				if (namespaceUri == null) {
					continue;
				}
				int indexToExport = getNamespaceTable().getIndex(namespaceUri);
				// devide a package in data-, event- and objecttypes
				String[] segments = makePackages(namespaceUri);
				for (int i = 0; i < segments.length; i++) {
					String segment = segments[i];
					Map<NodeId, IUMLTranslation> map = translations.get(i).getValue();

					boolean isEmpty = hasTranslations(map, indexToExport);
					if (isEmpty) {
						continue;
					}

					printFileUMLStartPackage(context, out, segment, namespaceUri);

					for (Entry<NodeId, IUMLTranslation> umlModel : map.entrySet()) {
						NodeId nodeId = umlModel.getKey();
						if (nodeId.getNamespaceIndex() != indexToExport) {
							continue;
						}
						IUMLTranslation translation = umlModel.getValue();
						umlModel.getValue().setPackageName(segment);
						translation.translate(out, this.idUMLGenerator);
					}

					printFileUMLEndPackage(out);
				}
			}
			// write *.uml footer </model>
			printFileUMLFooter(out);
			out.flush();
		} catch (IOException e) {
			StatemachineException se = new StatemachineException(e);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, se.getMessage());
			throw se;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Cannot close writer to file " + file.getName());
				}
			}
		}
	}

	private void printFileDI(File file) throws StatemachineException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.newLine();
			out.write(
					"<architecture:ArchitectureDescription xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:architecture=\"http://www.eclipse.org/papyrus/infra/core/architecture\" contextId=\"org.eclipse.papyrus.infra.services.edit.TypeContext\"/>");
			out.newLine();

			out.flush();
		} catch (IOException e) {
			StatemachineException se = new StatemachineException(e);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, se.getMessage());
			throw se;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Cannot close writer to file " + file.getName());
				}
			}
		}
	}

	private void printFileNotationDiagramEnd(BufferedWriter out) throws IOException {
		out.write("  </notation:Diagram>");
		out.newLine();
	}

	private void printFileNotationDiagram(BufferedWriter out, String diagramName) throws IOException {
		out.write(
				"  <notation:Diagram xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:notation=\"http://www.eclipse.org/gmf/runtime/1.0.2/notation\" xmlns:style=\"http://www.eclipse.org/papyrus/infra/gmfdiag/style\" xmlns:uml=\"http://www.eclipse.org/uml2/5.0.0/UML\" xmi:id=\""
						+ this.idNotationGenerator.buildId(6) + "\" type=\"PapyrusUMLClassDiagram\" name=\""
						+ diagramName + "\" measurementUnit=\"Pixel\">");
		out.newLine();
	}

	private void printFileNotationStyle(BufferedWriter out, String owner, String modelName) throws IOException {
		out.write("  <styles xmi:type=\"notation:StringValueStyle\" xmi:id=\"" + this.idNotationGenerator.buildId(6)
				+ "\" name=\"diagram_compatibility_version\" stringValue=\"1.4.0\"/>");
		out.newLine();
		out.write("  <styles xmi:type=\"notation:DiagramStyle\" xmi:id=\"" + this.idNotationGenerator.buildId(6)
				+ "\"/>");
		out.newLine();
		out.write("  <styles xmi:type=\"style:PapyrusDiagramStyle\" xmi:id=\"" + this.idNotationGenerator.buildId(6)
				+ "\" diagramKindId=\"org.eclipse.papyrus.uml.diagram.class\">");
		out.newLine();
		out.write("    <owner xmi:type=\"uml:Model\" href=\"" + modelName + ".uml#" + owner + "\"/>");
		out.newLine();
		out.write("  </styles>");
		out.newLine();
		out.write("  <element xmi:type=\"uml:Model\" href=\"" + modelName + ".uml#" + owner + "\"/>");
		out.newLine();
	}

	/**
	 * Close package for UML model <packagedElement
	 * 
	 * @param out Output stream
	 * @throws IOException
	 */
	private void printFileUMLEndPackage(BufferedWriter out) throws IOException {
		// <packagedElement ...
		out.write("  </packagedElement>");
		out.newLine();
	}

	/**
	 * Open package for model <packagedElement ...
	 * 
	 * @param context
	 * 
	 * @param out     Output stream
	 * @throws IOException
	 */
	private void printFileUMLStartPackage(IReverseTransformationContext context, BufferedWriter out, String packagename,
			String namespaceUri) throws IOException {
		String packageId = this.idUMLGenerator.buildId(5);
		context.addUMLPackageMapping(packagename, packageId);
		// <packagedElement ...
		out.write("  <packagedElement xmi:type=\"uml:Package\" xmi:id=\"" + packageId + "\" name=\"" + packagename
				+ "\" URI=\"" + namespaceUri + "\">");
		out.newLine();
	}

	private void printFileNotationFooter(BufferedWriter out) throws IOException {
		out.write("</xmi:XMI>");
	}

	private void printFileUMLFooter(BufferedWriter out) throws IOException {
		out.write("</uml:Model>");
	}

	private void printFileNotationHeader(BufferedWriter out) throws IOException {
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.newLine();
		out.write(
				"<xmi:XMI xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" xmlns:notation=\"http://www.eclipse.org/gmf/runtime/1.0.2/notation\" xmlns:style=\"http://www.eclipse.org/papyrus/infra/gmfdiag/style\" xmlns:uml=\"http://www.eclipse.org/uml2/5.0.0/UML\">");
		out.newLine();
	}

	private void printFileUMLHeader(BufferedWriter out, String UMLmodelId, String name) throws IOException {
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.newLine();
		out.write(
				"<uml:Model xmi:version=\"20131001\" xmlns:xmi=\"http://www.omg.org/spec/XMI/20131001\" xmlns:uml=\"http://www.eclipse.org/uml2/5.0.0/UML\" xmi:id=\""
						+ UMLmodelId + "\" name=\"" + name + "\">");
		out.newLine();
	}

	private void printFileUMLImportedPackage(BufferedWriter out) throws IOException {
		// open <packageImport ....
		out.write(ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE
				+ "<packageImport xmi:type=\"uml:PackageImport\" xmi:id=\"" + this.idUMLGenerator.buildId(5) + "\">");
		out.newLine();
		// <importedPackage
		out.write(ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE
				+ ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE
				+ "<importedPackage xmi:type=\"uml:Model\" href=\"pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#_0\"/>");
		out.newLine();
		// close </packageImport
		out.write(ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE + "</packageImport>");
		out.newLine();
	}

	public abstract void loadUMLResources(File... files);

	public abstract File[] getUMLBaseTypeFiles();

}
