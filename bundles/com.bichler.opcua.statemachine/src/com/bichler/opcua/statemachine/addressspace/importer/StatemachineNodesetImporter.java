package com.bichler.opcua.statemachine.addressspace.importer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.impl.ClassifierImpl;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.EnumValueType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.opcua.statemachine.BaseStatemachineActivator;
import com.bichler.opcua.statemachine.addressspace.CustomSAXBuilder;
import com.bichler.opcua.statemachine.addressspace.NameSpaceNotFountException;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.ExtractOpcUaStructureContext;
import com.bichler.opcua.statemachine.transform.IStateMachineExtractor;
import com.bichler.opcua.statemachine.transform.StateMachineMappingContext;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.persistence.xml.SAXNodeReader;

/**
 * @author andreas.schartlmueller@sequality.at
 */
public class StatemachineNodesetImporter {
	public static final String INPUT_ARGUMENTS = "InputArguments";
	public static final String OUTPUT_ARGUMENTS = "OutputArguments";
	private static final String MINIMUM_SAMPLING_INTERVAL = "MinimumSamplingInterval";
	private static final String IS_ABSTRACT = "IsAbstract";
	private static final String ACCESS_LEVEL = "AccessLevel";
	private static final String UA_VARIABLE_TYPE = "UAVariableType";
	private static final String ENUM_VALUES = "EnumValues";
	private static final String ENUM_STRINGS = "EnumStrings";
	/* XML Identifiers */
	private static final String USER_ACCESS_LEVEL = "UserAccessLevel";
	private static final String ARRAY_DIMENSIONS = "ArrayDimensions";
	private static final String VALUE_RANK = "ValueRank";
	private static final String HISTORIZING = "Historizing";
	private static final String DATA_TYPE = "DataType";
	private static final String UA_DATA_TYPE = "UADataType";
	private static final String UA_METHOD = "UAMethod";
	private static final String HAS_MODELLING_RULE = "HasModellingRule";
	private static final String MODELS = "Models";
	private static final String SYMMETRIC = "Symmetric";
	private static final String DESCRIPTION = "Description";
	private static final String UA_REFERENCE_TYPE = "UAReferenceType";
	private static final String UA_OBJECT_TYPE = "UAObjectType";
	private static final String IS_FORWARD = "IsForward";
	private static final String NAMESPACE_URIS = "NamespaceUris";
	private static final String ALIASES = "Aliases";
	private static final String ALIAS = "Alias";
	private static final String UA_OBJECT = "UAObject";
	private static final String UA_VARIABLE = "UAVariable";
	private static final String REFERENCE_TYPE = "ReferenceType";
	private static final String PARENT_NODE_ID = "ParentNodeId";
	private static final String NODE_ID = "NodeId";
	private static final String BROWSE_NAME = "BrowseName";
	private static final String DISPLAY_NAME = "DisplayName";
	private static final String REFERENCES = "References";
	private static final String HAS_TYPE_DEFINITION = "HasTypeDefinition";
	private static final String HAS_DESCRIPTION = "HasDescription";
	private static final String HAS_ENCODING = "HasEncoding";
	private static final String HAS_SUBTYPE = "HasSubtype";
	private static final String ORGANIZES = "Organizes";
	private static final String HAS_PROPERTY = "HasProperty";
	private static final String HAS_COMPONENT = "HasComponent";

	private static List<NodeId> HIERACHICAL_REFERENCE_IDS = new ArrayList<>();
	static {
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.HierarchicalReferences);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.DataSetToWriter);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.HasComponent);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.HasHistoricalConfiguration);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.HasProperty);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.HasSubtype);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.HasEventSource);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.HasNotifier);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.Organizes);
		HIERACHICAL_REFERENCE_IDS.add(Identifiers.AlarmGroupMember);

	}

	private int[] namespaceMap = null;
	private List<NodeSetBean1> nodesModellingRule = null;

	private List<AddReferencesItem> additionalReferences = null;

	private List<String> spezificReferenceType = null;
	private Map<String, NodeId> spezificDataTypeMapping = null;
	private Map<NodeId, AddNodesItem> addNodeItems = null;
	private Map<NodeId, AddNodesItem> allNodeItems;

	private NamespaceTable namespaceTable = new NamespaceTable();
	private Map<String, List<Classifier>> nodesClasses = null;
	private Map<String, Map<String, Classifier>> typeIdClassMapping = null;
	// private Map<String, Map<Class, String>> nodesClassIdMapping = null;
	private Map<ExpandedNodeId, Node> nodesItemList = null;
	private Map<ExpandedNodeId, Node> structuredDatatypeNodesItemList = null;

	private Map<String, Map<NodeId, Classifier>> umlDataTypeResources;
	private Map<String, UMLResource> umlResources;
	private Map<ExpandedNodeId, Node> additionalNodes;

	private Map<NodeId, DefinitionBean> datatypeDefinitionList;
	
	private Map<ExpandedNodeId, List<ExpandedNodeId>> hierachicalList;

	public StatemachineNodesetImporter() {
		this.nodesClasses = new HashMap<>();
		this.typeIdClassMapping = new HashMap<>();
		// this.nodesClassIdMapping = new HashMap<>();
		this.nodesItemList = new HashMap<>();
		this.structuredDatatypeNodesItemList = new HashMap<>();
		this.umlDataTypeResources = new HashMap<>();
		this.umlResources = new HashMap<>();

		this.hierachicalList = new HashMap<>();
		
		this.datatypeDefinitionList = new HashMap<>();
	}

	public void addNode(List<Node> nodes) {
		for (Node node : nodes) {
			addNode(node);
		}
	}
	
	public void addNode(Node node) {
		ExpandedNodeId expandedNodeId = getNamespaceTable().toExpandedNodeId(node.getNodeId());

		if (getStructuredDatatypeNodesItemList().containsKey(expandedNodeId)) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Cannot add OpcUa Node with the same nodeid " + expandedNodeId);
			return ;
		}

		getStructuredDatatypeNodesItemList().put(expandedNodeId, node);
	}

	/**
	 * Add a reference to a node.
	 * 
	 * @param SourceId     NodeId of the node to add the reference
	 * @param newReference New reference to add
	 */
	public void addReference(NodeId sourceId, ReferenceNode newReference) {
		Node source = getNodesItemById(sourceId);
		if(source == null) {
			source = getStructuredDatatypeNodesItemList().get(getNamespaceTable().toExpandedNodeId(sourceId));
		}
		List<ReferenceNode> references = new ArrayList<>();
		for (ReferenceNode reference : source.getReferences()) {
			references.add(reference);
		}
		references.add(newReference);

		source.setReferences(references.toArray(new ReferenceNode[0]));
	}

	public NodeId createNodeIdByXmlValue(String value) {
		if (value != null) {
			NodeId tmp = NodeId.parseNodeId(value);
			return NodeIdUtil.createNodeId(getMappedNamespaceByNodeId(tmp.getNamespaceIndex()), tmp.getValue());
		}
		return null;
	}

	public void clearAdditionalNodes() {
		if (this.additionalNodes != null) {
			this.additionalNodes.clear();
		}
		this.additionalNodes = null;
	}

	/**
	 * Extract an OPC UA type structure
	 * 
	 * @param transformer  Used to generate OPC UA NodeIds
	 * @param statemachine Parent UML statemachine element
	 * @param structure    Extracted OPC UA structure for the element
	 * @param type         Statemachine Type
	 * @param element      UML element to generate a structure for
	 * @return
	 */
	public List<Node> extractStructure(StateMachineMappingContext mappingContext,
			AbstractStateMachineToOpcTransformer transformer, StateMachine statemachine,
			Map<Node, List<Node>> structure, Node type, NamedElement element) {

		List<Node> extracted = new ArrayList<Node>();
		Map<ExpandedNodeId, ExpandedNodeId> idMapping = new HashMap<>();

		ExtractOpcUaStructureContext context = new ExtractOpcUaStructureContext();
		for (Entry<Node, List<Node>> nodes : structure.entrySet()) {
			Node node = extractOPCUAType(mappingContext, context, transformer, statemachine, nodes, type, element,
					structure, idMapping);
			if (node == null) {
				continue;
			}
			// add generated node
			extracted.add(node);
		}

		return extracted;
	}

	/**
	 * Finds the UML class of the statemachine type
	 * 
	 * @param resourceIds      UML diagram class ids
	 * @param statemachineType UML class type of the statemachine
	 * @return Class of the statemachineType or NULL
	 */
	public Classifier findTypeFromResource(ClassifierImpl statemachineType) {
		URI uri = statemachineType.eProxyURI();
		// class is already inside a generalized resource set
		if (uri == null) {
			return (Classifier) statemachineType;
		}
		String lastSegment = uri.lastSegment();
		String resourceToFind = lastSegment.substring(0,
				lastSegment.indexOf("." + BaseStatemachineActivator.EXTENSION_UML));
		Map<String, Classifier> idMapping = getTypeClassIdMapping().get(resourceToFind);
		if (idMapping == null) {
			return null;
		}
		String fragmentId = uri.fragment();
		Classifier type = idMapping.get(fragmentId);

		return type;
	}

	/**
	 * Finds the UML class of the statemachine type
	 * 
	 * @param resourceIds      UML diagram class ids
	 * @param statemachineType UML class type of the statemachine
	 * @return Class of the statemachineType or NULL
	 */
	public String findTypenameFromResource(ClassifierImpl statemachineType) {
		Classifier type = findTypeFromResource(statemachineType);

		if (type == null) {
			return null;
		}

		return type.getName();
	}

	/**
	 * Check type structure from basetype to type
	 * 
	 * @param type     Type of the structure
	 * @param baseType Base type from type
	 * @throws ServiceResultException
	 * @return property nodes
	 */
	public Map<Node, List<Node>> fetchOpcNodeTypeStructure(NodeId type, NodeId baseType) throws ServiceResultException {
		Map<Node, List<Node>> properties = new LinkedHashMap<Node, List<Node>>();
		Node current = getNodesItemById(type);

		fetchOpcNodeTypeStructure(current, properties, type, baseType, true);
		return properties;
	}

	public NamespaceTable getNamespaceTable() {
		return this.namespaceTable;
	}

	public Map<ExpandedNodeId, Node> getNodesItemList() {
		if (nodesItemList == null) {
			nodesItemList = new HashMap<>();
		}
		return nodesItemList;
	}
	
	public Map<NodeId, DefinitionBean> getDatatypeDefinition() {
		if (datatypeDefinitionList == null) {
			datatypeDefinitionList = new HashMap<>();
		}
		return datatypeDefinitionList;
	}
	
	public Map<ExpandedNodeId, Node> getStructuredDatatypeNodesItemList() {
		if (structuredDatatypeNodesItemList == null) {
			structuredDatatypeNodesItemList = new HashMap<>();
		}
		return structuredDatatypeNodesItemList;
	}

//	public Map<String, Class> findIdFromOpcUaType(String name) {
//		for (Entry<String, Map<Class, String>> typeset : this.nodesClassIdMapping.entrySet()) {
//			for (Entry<Class, String> classSet : typeset.getValue().entrySet()) {
//				String typename = classSet.getKey().getName();
//				if (name != null && name.equals(typename)) {
//					System.out.println();
//				}
//			}
//		}
//
//		return this.nodesIdClassMapping.get(name);
//	}

	public Node getNodeByBrowsename(String browseName) {
		for (Node node : this.nodesItemList.values()) {
			if (node.getBrowseName() != null && browseName.equalsIgnoreCase(node.getBrowseName().getName())) {
				return node;
			}
		}
		return null;
	}

	public Node getNodesItemById(ExpandedNodeId nodeId) {
		Node found = this.nodesItemList.get(nodeId);
		if (found == null) {
			if (this.additionalNodes != null) {
				found = this.additionalNodes.get(nodeId);
			}
		}
		return found;
	}

	public Node getNodesItemById(NodeId nodeId) {
		return getNodesItemById(namespaceTable.toExpandedNodeId(nodeId));
	}

	public String getResource(String modelname, NodeId nodeId, ResourceType type) throws StatemachineException {
		Map<NodeId, Classifier> resource = this.umlDataTypeResources.get(modelname);
		if (resource == null) {
			throw new StatemachineException("There are no UML resources for model " + modelname);
		}
		Classifier umlModel = resource.get(nodeId);
		return getResourceId(modelname, umlModel, type);
	}

	public void importClassModel(File[] files) {
		for (File file : files) {
//			Logger.getLogger(getClass().getName()).log(Level.INFO, "Read UML class model - " + file.getName());
			ResourceSet rs = new ResourceSetImpl();
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					new UMLResourceFactoryImpl());
			rs.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE.eClass());

			URI uml_model_path = URI.createFileURI(file.getAbsolutePath());
			UMLResource res = (UMLResource) rs.getResource(uml_model_path, true);
			Model m = (Model) res.getContents().get(0);

			String name = m.getName();
			// initialize id list of a class model
			Map<String, Classifier> ids = this.typeIdClassMapping.get(name);

			if (ids == null) {
				ids = new HashMap<>();
				this.typeIdClassMapping.put(name, ids);
			}

			// equivalent to OpcUa namespace URI
			// - Root (Package)
			// -- Package of Classes (Package)
			// --- Classes (Class)

			for (PackageableElement element : m.getPackagedElements()) {
				// skip non package elements
				if (!(element instanceof Package)) {
					continue;
				}
				// namespace Uri
				String uri = ((Package) element).getURI();
				List<Classifier> allClasses = this.nodesClasses.get(uri);
				if (allClasses == null) {
					allClasses = new ArrayList<>();
					this.nodesClasses.put(uri, allClasses);
				}
				for (PackageableElement datatypeElement : ((Package) element).getPackagedElements()) {
					// Classifier
					if (datatypeElement instanceof DataType) {
						allClasses.add((DataType) datatypeElement);
						ids.put(((DataType) datatypeElement).getName(), (DataType) datatypeElement);
						String id = res.getID(datatypeElement);
						ids.put(id, (DataType) datatypeElement);
					} else if(datatypeElement instanceof Class) {
						allClasses.add((Class) datatypeElement);
						ids.put(((Class) datatypeElement).getName(), (Class) datatypeElement);
						String id = res.getID(datatypeElement);
						ids.put(id, (Class) datatypeElement);
					}
				}
			}
		}
	}

	public Map<String, Map<NodeId, String>> mapNodeIdToUML(File... files) {
		Map<ExpandedNodeId, Node> allNodes = getNodesItemList();

		// [NodeId, UMLID]
		Map<String, Map<NodeId, String>> mapping = new HashMap<>();
		// open uml resource model
		for (File file : files) {
			ResourceSet rs = new ResourceSetImpl();
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					new UMLResourceFactoryImpl());
			rs.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE.eClass());

			URI uml_model_path = URI.createFileURI(file.getAbsolutePath());
			UMLResource res = (UMLResource) rs.getResource(uml_model_path, true);
			Model m = (Model) res.getContents().get(0);

			// iterate uml classes
			for (PackageableElement umlPackage : m.getPackagedElements()) {
				// classes are arranged within packages
				if (!(umlPackage instanceof Package)) {
					continue;
				}

				for (PackageableElement umlClass : ((Package) umlPackage).getPackagedElements()) {
					Map<NodeId, String> map = null;
					String resourceName = file.getName().substring(0, file.getName().lastIndexOf("."));
					if ((map = mapping.get(resourceName)) == null) {
						map = new HashMap<>();
						mapping.put(resourceName, map);
					}
					String umlId = res.getID(umlClass);

					String nodename = umlClass.getName();
					for (Node node : allNodes.values()) {
						String name = node.getBrowseName().getName();
						if (nodename.equals(name)) {
							map.put(node.getNodeId(), umlId);
							break;
						}
					}
				}
			}
		}

		return mapping;
	}

	public void loadUMLResource(File... files) {
		Map<ExpandedNodeId, Node> allNodes = getNodesItemList();

		// [NodeId, UMLID]
		Map<String, Map<NodeId, Classifier>> mapping = new HashMap<>();
		Map<String, UMLResource> resourceMapping = new HashMap<>();

		// open uml resource model
		for (File file : files) {
			ResourceSet rs = new ResourceSetImpl();
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					new UMLResourceFactoryImpl());
			rs.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE.eClass());

			URI uml_model_path = URI.createFileURI(file.getAbsolutePath());
			UMLResource res = (UMLResource) rs.getResource(uml_model_path, true);
			Model m = (Model) res.getContents().get(0);

			String resourceName = file.getName().substring(0, file.getName().lastIndexOf("."));
			resourceMapping.put(resourceName, res);

			// iterate uml classes
			for (PackageableElement umlPackage : m.getPackagedElements()) {
				// classes are arranged within packages
				if (!(umlPackage instanceof Package)) {
					continue;
				}

				for (PackageableElement umlClass : ((Package) umlPackage).getPackagedElements()) {
					Map<NodeId, Classifier> map = null;

					if ((map = mapping.get(resourceName)) == null) {
						map = new HashMap<>();
						mapping.put(resourceName, map);
					}
//					String umlId = res.getID(umlClass);

					String nodename = umlClass.getName();
					for (Node node : allNodes.values()) {
						String name = node.getBrowseName().getName();
						if (nodename.equals(name)) {
							map.put(node.getNodeId(), (Classifier) umlClass);
							break;
						}
					}
				}
			}
		}

		this.umlResources.putAll(resourceMapping);
		this.umlDataTypeResources.putAll(mapping);
	}

	public String getResourceId(String modelname, Classifier resourceDataType, ResourceType type) {
		UMLResource resource = this.umlResources.get(modelname);
		switch (type) {
		case UMLClass:
			return resource.getID(resourceDataType);
		case Generalization:
			return resource.getID(resourceDataType.getGeneralizations().get(0));
		}
		return "";
	}

	public Map<String, Map<String, Classifier>> getTypeClassIdMapping() {
		return this.typeIdClassMapping;
	}

	public void importNodeSet(File[] models) throws StatemachineException {
		// init existing namespace table
		try {
			for (File model : models) {
//				Logger.getLogger(getClass().getName()).log(Level.INFO, "Read OPC UA nodeset2 - " + model.getName());
				readXmlDocument(model);
			}
			finishReadXmlDocument();
		} catch (ServiceResultException e) {
			throw new StatemachineException(e);
		}
	}

	public void setAdditionalNodes(List<Node> nodes) {
		this.additionalNodes = new HashMap<>();
		for (Node node : nodes) {
			this.additionalNodes.put(this.namespaceTable.toExpandedNodeId(node.getNodeId()), node);
		}
	}

	public boolean isInverseHierachical(ReferenceNode reference) {
		NodeId referenceTypeId = reference.getReferenceTypeId();

		if (!reference.getIsInverse()) {
			return false;
		}

		if (!isHierachicalReference(referenceTypeId)) {
			return false;
		}
		return true;
	}

	public Node[] findChildren(NodeId nodeId, Map<NodeId, Node> additionalNodes) {
		List<Node> children = new ArrayList<>();

		ExpandedNodeId parentId = namespaceTable.toExpandedNodeId(nodeId);
		List<ExpandedNodeId> childrenList = getHierachicalList().get(parentId);
		if (childrenList != null) {
			for (ExpandedNodeId childId : childrenList) {
				Node child = getNodesItemById(childId);
				children.add(child);
			}
		}

		// check for additional parent
//		ExpandedNodeId hierachicalParentId = ExpandedNodeId.NULL;
//		for(Entry<NodeId, Node> entry : additionalNodes.entrySet()) {
//			for(ReferenceNode reference : entry.getValue().getReferences()){
//				boolean isReference = isInverseHierachical(reference);
//				if(isReference) { 
//					hierachicalParentId = reference.getTargetId();
//					break;
//				}
//			}
//		}

		return children.toArray(new Node[0]);
	}

	public boolean isHierachicalReference(NodeId referenceTypeId) {
		return HIERACHICAL_REFERENCE_IDS.contains(referenceTypeId);
	}

	protected NodeId getSpezificDataType(String dataType) {
		NodeId retVal = null;
		if (spezificDataTypeMapping != null) {
			retVal = spezificDataTypeMapping.get(dataType);
		}
		return retVal;
	}

	private void addUaNode(NodeSetBean1 bean, NodeAttributes attributes) throws ServiceResultException {
		AddNodesItem item = createNodesItem(bean, attributes);
		getAddNodeItems().put(this.namespaceTable.toNodeId(item.getRequestedNewNodeId()), item);

		// send2Opc();
	}

	private void addSpezificDataTypeMapping(String dataType, NodeId nid) {
		if (spezificDataTypeMapping == null) {
			spezificDataTypeMapping = new HashMap<>();
		}
		spezificDataTypeMapping.put(dataType, nid);
	}

	private void addSpezificReferenceType(String referenceType) {
		if (spezificReferenceType == null) {
			spezificReferenceType = new ArrayList<>();
		}
		spezificReferenceType.add(referenceType);
	}

	// <Models>
	// <Model ModelUri="http://www.euromap.org/euromap83/"
	// PublicationDate="2017-08-29T09:13:02Z" Version="RC 1.00">
	// <RequiredModel ModelUri="http://opcfoundation.org/UA/"
	// PublicationDate="2013-12-02T00:00:00Z" Version="1.03"/>
	// <RequiredModel ModelUri="http://opcfoundation.org/UA/DI/"
	// PublicationDate="2013-12-02T00:00:00Z" Version="1.01"/>
	// </Model>
	// </Models>
	@SuppressWarnings("unchecked")
	private void checkModels(Element ele, NamespaceTable newModelTable) throws NameSpaceNotFountException {
//		Logger.getLogger(getClass().getName()).log(Level.INFO, "check required namespaces from server");
		for (Element model : (List<Element>) ele.getChildren()) {
			// <Model ModelUri="http://www.euromap.org/euromap83/"
			// PublicationDate="2017-08-29T09:13:02Z" Version="RC 1.00">
			for (Element requiredModel : (List<Element>) model.getChildren()) {
				// <RequiredModel ModelUri="http://opcfoundation.org/UA/"
				// PublicationDate="2013-12-02T00:00:00Z" Version="1.03"/>
				String modelUri = requiredModel.getAttributeValue("ModelUri");
//				Logger.getLogger(getClass().getName()).log(Level.INFO, "check namespaces {0} from server " + modelUri);
				if (namespaceTable.getIndex(modelUri) == -1) {
					if (newModelTable.getIndex(modelUri) == -1) {
						// Logger.getLogger(getClass().getName()).log(Level.SEVERE, "namespace " +
						// modelUri + " not in list");
						throw new NameSpaceNotFountException("namespace " + modelUri + " not in list");
					}
				}
			}
		}
	}

	/**
	 * @param referenceType
	 * @return
	 */
	private boolean containsSpezificReferenceType(String referenceType) {
		boolean retVal = false;
		if (spezificReferenceType != null) {
			retVal = spezificReferenceType.contains(referenceType);
		}
		return retVal;
	}

	private List<AddReferencesItem> getAdditionalReferences() {
		if (additionalReferences == null) {
			additionalReferences = new ArrayList<>();
		}
		return additionalReferences;
	}

	private List<NodeSetBean1> getNodesModellingRule() {
		if (nodesModellingRule == null) {
			nodesModellingRule = new ArrayList<>();
		}
		return nodesModellingRule;
	}

	private ObjectAttributes createObjectAttributes(String displayName, String description) {
		ObjectAttributes attributes = new ObjectAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setEventNotifier(UnsignedByte.getFromBits((byte) 0));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private ObjectTypeAttributes createObjectTypeAttributes(String displayName, String description) {
		ObjectTypeAttributes attributes = new ObjectTypeAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setIsAbstract(false);
		return attributes;
	}

	@SuppressWarnings("unchecked")
	private VariableAttributes createVariableAttributes(String displayName, String varDescription, Element element)
			throws ServiceResultException {

		SAXNodeReader saxReader = new SAXNodeReader();
		Object saxReaderRetVal = null;
		VariableAttributes attributesVar = new VariableAttributes();
		attributesVar.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributesVar.setDescription(new LocalizedText(varDescription, Locale.ENGLISH));
		String dataType = element.getAttributeValue(DATA_TYPE);
		if (dataType != null) {
			try {
				attributesVar.setDataType((NodeId) Identifiers.class.getDeclaredField(dataType).get(null));
			} catch (NoSuchFieldException nsfe) {
				// UADataTypes, falls DataType standardmäßig nicht vorhanden ist, prüfen
				// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
				// andernfalls Fehler/Abbruch
				NodeId nid = getSpezificDataType(dataType);
				if (nid != null) {
					attributesVar.setDataType(createNodeIdByXmlValue(nid.toString()));
				} else if (containsInternalDataType(dataType)) {
					attributesVar.setDataType(NodeId.parseNodeId(dataType));
				} else {
					throw new ServiceResultException(dataType + " not found");
				}
			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, dataType + "  not found", ex);
				throw new ServiceResultException(dataType + " not found ");
			}
		} else {
			attributesVar.setDataType(Identifiers.BaseDataType);
		}
		String historizing = element.getAttributeValue(HISTORIZING);
		if (historizing != null) {
			attributesVar.setHistorizing(Boolean.valueOf(historizing));
		}
		String valueRank = element.getAttributeValue(VALUE_RANK);
		if (valueRank != null) {
			attributesVar.setValueRank(Integer.parseInt(valueRank));
		} else {
			// set valuerank = -1 (scalar) as default
			attributesVar.setValueRank(-1);
		}
		String arrayDimensions = element.getAttributeValue(ARRAY_DIMENSIONS);
		if (arrayDimensions != null) {
			if (attributesVar.getValueRank() > 0) {
				String[] items = arrayDimensions.split(",");
				List<UnsignedInteger> list = new ArrayList<UnsignedInteger>();
				for (String item : items) {
					list.add(UnsignedInteger.parseUnsignedInteger(item));
				}
				attributesVar.setArrayDimensions(list.toArray(new UnsignedInteger[list.size()]));
			} else {
				attributesVar.setArrayDimensions(new UnsignedInteger[0]);
			}
		}
		String userAccessLevel = element.getAttributeValue(USER_ACCESS_LEVEL);
		if (userAccessLevel != null) {
			attributesVar.setUserAccessLevel(UnsignedByte.parseUnsignedByte(userAccessLevel));
		} else {
			attributesVar.setUserAccessLevel(UnsignedByte.parseUnsignedByte("1"));
		}
		String accessLevel = element.getAttributeValue(ACCESS_LEVEL);
		if (accessLevel != null) {
			attributesVar.setAccessLevel(UnsignedByte.parseUnsignedByte(accessLevel));
		} else {
			attributesVar.setAccessLevel(UnsignedByte.parseUnsignedByte("1"));
		}
		String minimumSamplingInterval = element.getAttributeValue(MINIMUM_SAMPLING_INTERVAL);
		if (minimumSamplingInterval != null) {
			try {
				attributesVar.setMinimumSamplingInterval(Double.parseDouble(minimumSamplingInterval));
			} catch (Exception e) {
				throw e;
			}
		}
		attributesVar.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributesVar.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));

		Element value = element.getChild("Value", element.getNamespace());
		if (value != null) {

			if (valueRank == null /*
									 * && ((NodeId) Identifiers.class.getDeclaredField(dataType).get(null)) != null
									 */
			) {
				// String,....
				if (value.getChildren() != null && value.getChildren().size() > 0) {
					Variant v = saxReader.constructValueAttribute(((Element) value.getChildren().get(0)).getName(),
							((Element) value.getChildren().get(0)).getValue());
					if (v != null) {
						attributesVar.setValue(v);
					} else {
						throw new ServiceResultException("unknown datatype");
					}
				}
			} else if (valueRank.equals("1") && (element.getAttribute(BROWSE_NAME).getValue().endsWith(OUTPUT_ARGUMENTS)
					|| element.getAttribute(BROWSE_NAME).getValue().endsWith(INPUT_ARGUMENTS))) {
				if (((List<Element>) value.getChildren()) != null) {
					Element list = ((List<Element>) value.getChildren()).get(0);
					List<ExtensionObject> eoList = new ArrayList<>();
					for (Element eo : (List<Element>) list.getChildren()) {
						// <ExtensionObject>
						Element body = eo.getChild("Body", eo.getNamespace());
						Element arg = body.getChild("Argument", body.getNamespace());
						Element dt = arg.getChild("DataType", arg.getNamespace());
						Element identifier = dt.getChild("Identifier", dt.getNamespace());
						Argument argument = new Argument(arg.getChildText("Name", arg.getNamespace()),
								NodeId.parseNodeId(identifier.getText()),
								Integer.parseInt(arg.getChildText("ValueRank", arg.getNamespace())),
								new UnsignedInteger[] {},
								new LocalizedText(arg.getChildText("Description", arg.getNamespace())));
						eoList.add(ExtensionObject.binaryEncode(argument, EncoderContext.getDefaultInstance()));
					}
					attributesVar.setValue(new Variant(eoList.toArray(new ExtensionObject[eoList.size()])));
					if (arrayDimensions == null) {
						attributesVar.setArrayDimensions(new UnsignedInteger[] { new UnsignedInteger(eoList.size()) });
					}
				}
			}
			// HB 2017-11-15 add import enum values support
			else if (valueRank.equals("1") && element.getAttribute(BROWSE_NAME).getValue().endsWith(ENUM_VALUES)) {
				if (((List<Element>) value.getChildren()) != null) {
					Element list = ((List<Element>) value.getChildren()).get(0);
					List<ExtensionObject> eoList = new ArrayList<>();
					for (Element eo : (List<Element>) list.getChildren()) {
						// <ExtensionObject>

						Element body = eo.getChild("Body", eo.getNamespace());

						Element enumval = body.getChild("EnumValueType", body.getNamespace());
						Element val = enumval.getChild("Value", enumval.getNamespace());
						long index = Long.parseLong(val.getText());

						Element identifier = enumval.getChild("DisplayName", enumval.getNamespace());
						try {
							Element locale = identifier.getChild("Locale", identifier.getNamespace());
							Element text = identifier.getChild("Text", identifier.getNamespace());
							Element description = enumval.getChild("Description", enumval.getNamespace());

							LocalizedText desc = LocalizedText.EMPTY;
							if (description != null) {
								Element locDescription = description.getChild("Locale", identifier.getNamespace());
								Element locText = description.getChild("Text", identifier.getNamespace());

								String dlocale = "";
								String dtext = "";
								if (locDescription != null) {
									dlocale = locDescription.getText();
								}
								if (locText != null) {
									dtext = locText.getText();
								}
								desc = new LocalizedText(dlocale, dtext);
							}

							String llocale = "";
							String ltext = "";
							if (locale != null)
								llocale = locale.getText();
							if (text != null)
								ltext = text.getText();
							LocalizedText dName = new LocalizedText(ltext, llocale);

							EnumValueType enumv = new EnumValueType(index, dName, desc);
							eoList.add(ExtensionObject.binaryEncode(enumv, EncoderContext.getDefaultInstance()));
						} catch (NullPointerException npe) {

						}
					}
					attributesVar.setValue(new Variant(eoList.toArray(new ExtensionObject[eoList.size()])));
					if (arrayDimensions == null) {
						attributesVar.setArrayDimensions(new UnsignedInteger[] { new UnsignedInteger(eoList.size()) });
					}
				}
			} else if (valueRank.equals("1") && (saxReaderRetVal = saxReader.constructValueObject(dataType,
					((Element) value.getChildren().get(0)).getValue())) != null) {
				Object array = null;
				List<Element> l = (List<Element>) ((Element) value.getChildren().get(0)).getChildren();
				Element e = null;
				for (int i = 0; i < l.size(); i++) {
					e = l.get(i);
					if (array == null) {
						array = Array.newInstance(saxReaderRetVal.getClass(), l.size());
					}
					if (dataType.equals("LocalizedText")) {
						String val = e.getChild("Text", e.getNamespace()).getValue();
						String loc = null;
						if (e.getChild("Locale", e.getNamespace()) != null) {
							loc = e.getChild("Locale", e.getNamespace()).getValue();
						}
						if (loc != null) {
							loc = loc.trim();
						}
						Array.set(array, i, new LocalizedText(val, loc));
					} else {
						// String,....
						if (value.getChildren() != null && value.getChildren().size() > 0) {
							Object v = saxReaderRetVal;
							Array.set(array, i, v);
						}
					}
				}
				attributesVar.setValue(new Variant(array));
				if (arrayDimensions == null) {
					attributesVar.setArrayDimensions(new UnsignedInteger[] { new UnsignedInteger(l.size()) });
				}
			} else {
//				Logger.getLogger(getClass().getName()).log(Level.INFO,
//						"unhandled variable value: " + element.getAttribute(NODE_ID).getValue() + " Browsename: "
//								+ element.getAttribute(BROWSE_NAME).getValue());
			}
		}

		return attributesVar;
	}

	/**
	 * HB 2017/10/13 verify if datatype already exists in opc ua internal dataspace.
	 * 
	 * @param dataType
	 * @return
	 */
	private boolean containsInternalDataType(String dataType) {
//		if (opcServer.getServerInstance().getAddressSpaceManager().getNodeById(NodeId.parseNodeId(dataType)) != null) {
		return true;
//		}
//		return false;
	}

	private ReferenceTypeAttributes createReferenceTypeAttributes(NodeSetBean1 bean) {
		ReferenceTypeAttributes rta = new ReferenceTypeAttributes();
		rta.setDisplayName(new LocalizedText(bean.getDisplayName(), Locale.ENGLISH));
		rta.setDescription(new LocalizedText(bean.getDescription(), Locale.ENGLISH));
		rta.setSymmetric(bean.isSymmetric());
		rta.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		rta.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return rta;
	}

	private AddNodesItem createNodesItem(NodeSetBean1 bean, NodeAttributes attributes) throws EncodingException {
		AddNodesItem item = new AddNodesItem();
		item.setNodeClass(bean.getNodeClass());

		// BrowseName="1:BoxId"
		int nsBrowseName = 0;
		if (bean.getBrowseName() != null) {
			try {
				nsBrowseName = Integer.parseInt(bean.getBrowseName().substring(0, bean.getBrowseName().indexOf(":")));
				nsBrowseName = getMappedNamespaceByNodeId(nsBrowseName);
			} catch (Exception ex) {
				nsBrowseName = 0;
			}
			item.setBrowseName(new QualifiedName(nsBrowseName,
					bean.getBrowseName().substring(bean.getBrowseName().indexOf(":") + 1)));
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "browseName is null\n" + bean.getNodeId().toString());
		// }
		if (bean.getNodeId() != null) {
			item.setRequestedNewNodeId(
					new ExpandedNodeId(this.namespaceTable.getUri(bean.getNodeId().getNamespaceIndex()),
							bean.getNodeId().getValue(), this.namespaceTable));
		}
		// else {
		// throw new EncodingException("node id is null!!");
		// }
		if (bean.getReferenceType() != null) {
			item.setReferenceTypeId(bean.getReferenceType());
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "referenceTypeId is null\n" + bean.getNodeId().toString());
		// }
		if (bean.getTypeDefinition() != null) {
			item.setTypeDefinition(
					new ExpandedNodeId(this.namespaceTable.getUri(bean.getTypeDefinition().getNamespaceIndex()),
							bean.getTypeDefinition().getValue(), this.namespaceTable));
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "typeDefinition is null\n" + bean.getNodeId().toString());
		// }
		if (bean.getParentNodeId() != null) {
			item.setParentNodeId(
					new ExpandedNodeId(this.namespaceTable.getUri(bean.getParentNodeId().getNamespaceIndex()),
							bean.getParentNodeId().getValue(), this.namespaceTable));
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "parentNodeId is null\n" + bean.getNodeId().toString());
		// }
		item.setNodeAttributes(ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance()));
		if (bean.getHasModellingRule() != null) {
			getNodesModellingRule().add(bean);
		}

		return item;
	}

	@SuppressWarnings("unchecked")
	private void readXmlDocument(File file) throws ServiceResultException {
		CustomSAXBuilder saxBuilder = new CustomSAXBuilder();
		try {
			Document document = saxBuilder.build(file);
			Element rootElement = document.getRootElement();
			List<Element> childrenList = rootElement.getChildren();
			// first check required namespaces
			NamespaceTable nsTable = null;
			boolean finish = false;
			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {
				case NAMESPACE_URIS:
					nsTable = findModelNamespaces(ele);
					break;
				case MODELS:
					checkModels(ele, nsTable);
					finish = true;
					break;
				}
				if (finish) {
					break;
				}
			}

			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {
				case NAMESPACE_URIS:
					createNamespaceMapping(ele);
					break;
				case ALIASES:
					createAliasDatatypes(ele);
					break;
				case UA_OBJECT:
					addUaObject(ele);
					break;
				case UA_OBJECT_TYPE:
					addUaObjectType(ele);
					break;
				case UA_DATA_TYPE:
					addUaDataType(ele);
					break;
				case UA_REFERENCE_TYPE:
					addUaReferenceType(ele);
					break;
				}
			}
			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {
				case UA_VARIABLE_TYPE:
					addUaVariableType(ele);
					break;
				}
			}
			childrenList = rootElement.getChildren();
			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {

				case UA_VARIABLE:
					addUaVariable(ele);
					break;
				case UA_METHOD:
					addUaMethod(ele);
					break;
				}
			}

			handleNodes();
			handleHasModellingRules();
			handleAdditionalReferences();
		} catch (NameSpaceNotFountException e) {
			e.printStackTrace();
			throw new ServiceResultException(e);
		} catch (JDOMException e) {
			e.printStackTrace();
			throw new ServiceResultException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceResultException(e);
		}
	}

	private void handleNodes() {
		Map<NodeId, AddNodesItem> items = getAddNodeItems();

		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		Node node = null;

		for (Entry<NodeId, AddNodesItem> item : items.entrySet()) {
			try {
				node = nodeFactory.createNode(this.namespaceTable, item.getValue().getBrowseName(),
						item.getValue().getNodeAttributes(), item.getValue().getNodeClass(),
						item.getValue().getParentNodeId(), item.getValue().getReferenceTypeId(),
						item.getValue().getRequestedNewNodeId(), item.getValue().getTypeDefinition());

				getNodesItemList().put(item.getValue().getRequestedNewNodeId(), node);
				getAllNodeItemsList().put(node.getNodeId(), item.getValue());
			} catch (EncodingException e) {
				e.printStackTrace();
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}

		for (Entry<NodeId, AddNodesItem> item : items.entrySet()) {
			AddNodesItem addItem = item.getValue();

			ExpandedNodeId nodeId = addItem.getRequestedNewNodeId();
			ExpandedNodeId parent = addItem.getParentNodeId();

			List<ExpandedNodeId> children = getHierachicalList().get(parent);
			if (children == null) {
				children = new ArrayList<>();
				getHierachicalList().put(parent, children);
			}
			children.add(nodeId);
		}

		this.addNodeItems = null;
	}

	private Map<ExpandedNodeId, List<ExpandedNodeId>> getHierachicalList() {
		return this.hierachicalList;
	}

	private Map<NodeId, AddNodesItem> getAllNodeItemsList() {
		if (this.allNodeItems == null) {
			this.allNodeItems = new HashMap<>();
		}

		return this.allNodeItems;
	}

	private Map<NodeId, AddNodesItem> getAddNodeItems() {
		if (this.addNodeItems == null) {
			this.addNodeItems = new HashMap<>();
		}
		return this.addNodeItems;
	}

	private void handleHasModellingRules() {
//		Logger.getLogger(getClass().getName()).log(Level.INFO, "handle hasModellingRules");
		if (nodesModellingRule != null && getNodesModellingRule().size() > 0) {
			ArrayList<AddReferencesItem> list = new ArrayList<>();
			for (NodeSetBean1 b : getNodesModellingRule()) {
				AddReferencesItem descr = new AddReferencesItem();
				descr.setIsForward(true);
				descr.setReferenceTypeId(Identifiers.HasModellingRule);
				descr.setSourceNodeId(b.getNodeId());
				descr.setTargetNodeClass(NodeClass.Variable);
				descr.setTargetNodeId(
						new ExpandedNodeId(this.namespaceTable.getUri(b.getHasModellingRule().getNamespaceIndex()),
								b.getHasModellingRule().getValue(), this.namespaceTable));
				list.add(descr);
			}
//			Logger.getLogger(getClass().getName()).log(Level.INFO, "add modelling rules to server");
			getAdditionalReferences().addAll(list);
		}
	}

	private void handleAdditionalReferences() throws ServiceResultException {
//		Logger.getLogger(getClass().getName()).log(Level.INFO, "handle additional References");
		if (this.additionalReferences != null) {
			// opcServer.getDriverConnection().addReferences(this.additionalReferences.toArray(new
			// AddReferencesItem[0]));
			for (AddReferencesItem reference : this.additionalReferences) {
				Boolean isForward = reference.getIsForward();
				NodeId sourceId = reference.getSourceNodeId();
				NodeId referenceType = reference.getReferenceTypeId();
				ExpandedNodeId targetId = reference.getTargetNodeId();

				Node node1 = getNodesItemById(sourceId);

				ReferenceNode reference2add = new ReferenceNode();
				reference2add.setIsInverse(isForward);
				reference2add.setTargetId(targetId);
				reference2add.setReferenceTypeId(referenceType);

//				Node node2 = getNodesItemById(targetId);
				boolean contains = false;
				List<ReferenceNode> ref1 = new ArrayList<ReferenceNode>();
				for (ReferenceNode node : node1.getReferences()) {
					ref1.add(node);

					if (reference2add.equals(node)) {
						contains = true;
					}
				}

				if (!contains) {
					ref1.add(reference2add);
					node1.setReferences(ref1.toArray(new ReferenceNode[0]));
				}
//				List<ReferenceNode> ref2 = new ArrayList<ReferenceNode>();
//				for (ReferenceNode node : node2.getReferences()) {
//					ref2.add(node);
//				}

			}
		}

		this.additionalReferences = null;
	}

//	private void initExisitingNamespaces() {
//		namespaceTable = new NamespaceTable();
//	}

	private NamespaceTable findModelNamespaces(Element element) {
		List<Element> namespaceChilds = element.getChildren();
		int[] namespaces = new int[namespaceChilds.size()];
		NamespaceTable nsTable = NamespaceTable.createFromArray(new String[0]);

		int idx = -1;
		for (int i = 0; i < namespaceChilds.size(); i++) {
			Element e = namespaceChilds.get(i);
			if ((idx = nsTable.getIndex(e.getValue())) > -1) {
				// namespace already in table
				namespaces[i] = idx;
			} else {
				namespaces[i] = nsTable.size();
				nsTable.add(e.getValue());
			}
		}

		return nsTable;
	}

	@SuppressWarnings("unchecked")
	private void createNamespaceMapping(Element element) {
		List<Element> namespaceChilds = element.getChildren();
		namespaceMap = new int[namespaceChilds.size()];
		int idx = -1;
		for (int i = 0; i < namespaceChilds.size(); i++) {
			Element e = namespaceChilds.get(i);
			if ((idx = namespaceTable.getIndex(e.getValue())) > -1) {
				// namespace already in table
				namespaceMap[i] = idx;
			} else {
				namespaceMap[i] = namespaceTable.size();
				namespaceTable.add(e.getValue());
			}
		}

		// DataValue dv = new DataValue(new Variant(namespaceTable.toArray()));
		// add Namespace to Server
//		this.opcServer.getDriverConnection().writeFromDriver(Identifiers.Server_NamespaceArray, Attributes.Value, null,
//				dv, 0l);
	}

	private int getMappedNamespace(int xmlNs) {
		return namespaceMap[xmlNs - 1];
	}

	@SuppressWarnings("unchecked")
	private NodeSetBean1 parseElement(NodeClass nodeClass, Element element) throws ServiceResultException {
		NodeSetBean1 bean = new NodeSetBean1(nodeClass, this);
		/* Parent */
		bean.setParentNodeId(createNodeIdByXmlValue(element.getAttributeValue(PARENT_NODE_ID)));
		/* Node */
		bean.setNodeId(createNodeIdByXmlValue(element.getAttributeValue(NODE_ID)));
		/* BrowseName */
		bean.setBrowseName(element.getAttributeValue(BROWSE_NAME));
		/* Symmetric */
		String val = element.getAttributeValue(SYMMETRIC);
		if (val != null) {
			bean.setSymmetric(Boolean.valueOf(val));
		}

		List<Element> list = element.getChildren();
		for (Element e : list) {
			switch (e.getName()) {
			case DISPLAY_NAME:
				bean.setDisplayName(e.getValue());
				break;
			case DESCRIPTION:
				bean.setDescription(e.getValue());
				break;
			case REFERENCES:
				List<Element> references = e.getChildren();
				for (Element ref : references) {
					/*
					 * if (ref.getAttributeValue(IS_FORWARD) != null &&
					 * !Boolean.valueOf(ref.getAttributeValue(IS_FORWARD))) { // ==>use entry für
					 * reference type String referenceTyp = ref.getAttributeValue(REFERENCE_TYPE);
					 * // HAS_COMPONENT, HAS_ORDERED_COMPONENT,... try {
					 * bean.setReferenceType((NodeId)
					 * Identifiers.class.getDeclaredField(referenceTyp).get(null)); // use parent
					 * node from refrerence
					 * bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue())); } catch
					 * (NoSuchFieldException nsfe) { // UAReferences, falls ReferenceType
					 * standardmäßig nicht vorhanden ist, prüfen // ob in eigener Liste ein Eintrag
					 * vorhanden ist und ggf neue NodeId erzeugen, // andernfalls Fehler/Abbruch if
					 * (containsSpezificReferenceType(referenceTyp)) {
					 * bean.setReferenceType(NodeId.parseNodeId(referenceTyp)); // use parent node
					 * from refrerence bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
					 * } else { throw new Exception(referenceTyp + " not found "); } } catch
					 * (Exception ex) { Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					 * referenceTyp + "  not found", ex); throw ex; } }
					 */
					switch (ref.getAttributeValue(REFERENCE_TYPE)) {
					case HAS_TYPE_DEFINITION:
						bean.setTypeDefinition(createNodeIdByXmlValue(ref.getValue()));
						break;
					case HAS_DESCRIPTION:
						this.getAdditionalReferences()
								.add(bean.createTypeDescription(createNodeIdByXmlValue(ref.getValue()),
										ref.getAttributeValue(IS_FORWARD), this.namespaceTable));
						break;
					case HAS_ENCODING:
						if (ref.getAttributeValue(IS_FORWARD) != null
								&& !Boolean.valueOf(ref.getAttributeValue(IS_FORWARD))) {
							String referenceTyp = ref.getAttributeValue(REFERENCE_TYPE);
							try {
								bean.setReferenceType(
										(NodeId) Identifiers.class.getDeclaredField(referenceTyp).get(null));
								// use parent node from refrerence
								bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
							} catch (NoSuchFieldException nsfe) {
								// UAReferences, falls ReferenceType standardmäßig nicht vorhanden ist, prüfen
								// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
								// andernfalls Fehler/Abbruch
								if (containsSpezificReferenceType(referenceTyp)) {
									bean.setReferenceType(createNodeIdByXmlValue(referenceTyp));
									// use parent node from refrerence
									bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
								} else {
									throw new ServiceResultException(referenceTyp + " not found ");
								}
							} catch (Exception ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceTyp + "  not found",
										ex);
								throw new ServiceResultException(referenceTyp + " not found ");
							}
						} else {
							this.getAdditionalReferences()
									.add(bean.createTypeEncoding(createNodeIdByXmlValue(ref.getValue()),
											ref.getAttributeValue(IS_FORWARD), this.namespaceTable));
						}
						break;
					case HAS_MODELLING_RULE:
						bean.setHasModellingRule(createNodeIdByXmlValue(ref.getValue()));
						break;
					case HAS_SUBTYPE:
					case ORGANIZES:
					case HAS_PROPERTY:
					case HAS_COMPONENT:
						if (ref.getAttributeValue(IS_FORWARD) != null
								&& !Boolean.valueOf(ref.getAttributeValue(IS_FORWARD))) {
							// ==>use entry für reference type
							String referenceTyp = ref.getAttributeValue(REFERENCE_TYPE);
							// HAS_COMPONENT, HAS_ORDERED_COMPONENT,...
							try {
								bean.setReferenceType(
										(NodeId) Identifiers.class.getDeclaredField(referenceTyp).get(null));
								// use parent node from refrerence
								bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
							} catch (NoSuchFieldException nsfe) {
								// UAReferences, falls ReferenceType standardmäßig nicht vorhanden ist, prüfen
								// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
								// andernfalls Fehler/Abbruch
								if (containsSpezificReferenceType(referenceTyp)) {
									bean.setReferenceType(createNodeIdByXmlValue(referenceTyp));
									// use parent node from refrerence
									bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
								} else {
									throw new ServiceResultException(referenceTyp + " not found ");
								}
							} catch (Exception ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceTyp + "  not found",
										ex);
								throw new ServiceResultException(referenceTyp + " not found ");
							}
						}
						break;
					default:
						String isforward = "true";
						if (ref.getAttributeValue(IS_FORWARD) != null) {
							isforward = ref.getAttributeValue(IS_FORWARD);
						}
						if (bean.getReferenceType() == null && !Boolean.parseBoolean(isforward)
								&& bean.getParentNodeId() != null) {
							try {
								bean.setReferenceType(createNodeIdByXmlValue(ref.getAttributeValue(REFERENCE_TYPE)));
							} catch (IllegalArgumentException ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
							}
						} else {
							getAdditionalReferences().add(bean.createAdditionalReference(
									createNodeIdByXmlValue(ref.getValue()), ref.getAttributeValue(REFERENCE_TYPE),
									isforward, this.namespaceTable, this));
						}
						break;
					}
					// switch (ref.getAttributeValue(REFERENCE_TYPE)) {
					// case HAS_TYPE_DEFINITION: {
					// String val = ref.getValue();
					// int nsTypeDef = getMappedNamespaceByNodeId(val);
					// String nodeIdTypDef = parseXmlNodeId(val);
					// typeDefinition = new NodeId(nsTypeDef, nodeIdTypDef);
					// break;
					// }
					// }
				}
				break;
			}
		}
		return bean;
	}

	private void createAliasDatatypes(Element element) {
		for (Object el : element.getChildren()) {
			NodeId nid = NodeId.parseNodeId(((Element) el).getValue());
			addSpezificDataTypeMapping(((Element) el).getAttributeValue(ALIAS), nid);
			addSpezificDataTypeMapping(((Element) el).getValue(), nid);
			// addSpezificDataType();
		}
	}

	private VariableTypeAttributes createVariableTypeAttributes(String displayName, String description, Element element)
			throws ServiceResultException {
		VariableTypeAttributes attributes = new VariableTypeAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		String dataType = element.getAttributeValue(DATA_TYPE);
		if (dataType != null) {
			try {
				attributes.setDataType((NodeId) Identifiers.class.getDeclaredField(dataType).get(null));
			} catch (NoSuchFieldException nsfe) {
				// UADataTypes, falls DataType standardmäßig nicht vorhanden ist, prüfen
				// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
				// andernfalls Fehler/Abbruch
				NodeId nid = getSpezificDataType(dataType);
				if (nid != null) {
					attributes.setDataType(nid);
				} else {
					throw new ServiceResultException(dataType + " not found");
				}
			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, dataType + "  not found", ex);
				throw new ServiceResultException(dataType + " not found ");
			}
		} else {
			attributes.setDataType(Identifiers.BaseDataType);
		}
		String valueRank = element.getAttributeValue(VALUE_RANK);
		if (valueRank != null) {
			attributes.setValueRank(Integer.parseInt(valueRank));
		}
		String arrayDimensions = element.getAttributeValue(ARRAY_DIMENSIONS);
		if (arrayDimensions != null) {
			if (attributes.getValueRank() > 0) {
				String[] items = arrayDimensions.split(",");
				List<UnsignedInteger> list = new ArrayList<UnsignedInteger>();
				for (String item : items) {
					list.add(UnsignedInteger.parseUnsignedInteger(item));
				}
				attributes.setArrayDimensions(list.toArray(new UnsignedInteger[list.size()]));
			} else {
				attributes.setArrayDimensions(new UnsignedInteger[0]);
			}
		}
		String isAbstract = element.getAttributeValue(IS_ABSTRACT);
		if (isAbstract != null) {
			attributes.setIsAbstract(Boolean.valueOf(isAbstract));
		}
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private DataTypeAttributes createDataTypeAttributes(String displayName, String description) {
		DataTypeAttributes attributes = new DataTypeAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private MethodAttributes createMethodAttributes(String displayName, String description) {
		MethodAttributes attributes = new MethodAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private void addUaDataType(Element element) throws ServiceResultException {
		NodeSetBean1 bean = parseElement(NodeClass.DataType, element);
		DataTypeAttributes attributes = createDataTypeAttributes(bean.getDisplayName(), bean.getDescription());
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
		/*
		 * HBS add specific datatypes from alias
		 */
		NodeId nid = NodeId.parseNodeId(element.getAttributeValue(NODE_ID));
		addSpezificDataTypeMapping(bean.getDisplayName(), nid);
		addSpezificDataTypeMapping(element.getAttributeValue(NODE_ID), nid);
	}

	private void addUaMethod(Element element) throws ServiceResultException {
		NodeSetBean1 bean = parseElement(NodeClass.Method, element);
		MethodAttributes attributes = createMethodAttributes(bean.getDisplayName(), bean.getDescription());
		addUaNode(bean, attributes);
		// AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaObject(Element element) throws ServiceResultException {
		NodeSetBean1 bean = parseElement(NodeClass.Object, element);
		ObjectAttributes attributes = createObjectAttributes(bean.getDisplayName(), bean.getDescription());
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, oa);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaObjectType(Element element) throws ServiceResultException {
		NodeSetBean1 bean = parseElement(NodeClass.ObjectType, element);
		ObjectTypeAttributes attributes = createObjectTypeAttributes(bean.getDisplayName(), bean.getDescription());
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaReferenceType(Element element) throws ServiceResultException {
		NodeSetBean1 bean = parseElement(NodeClass.ReferenceType, element);
		ReferenceTypeAttributes attributes = createReferenceTypeAttributes(bean);
		addUaNode(bean, attributes);

//		AddNodesItem item = createNodesItem(bean, rta);
//		getNodesItemList().add(item);
//		send2Opc();
		addSpezificReferenceType(element.getAttributeValue(NODE_ID));
	}

	private void addUaVariable(Element element) throws ServiceResultException {
		NodeSetBean1 bean = parseElement(NodeClass.Variable, element);
		// referenceTypeId = Identifiers.HasProperty;
		VariableAttributes attributes = createVariableAttributes(bean.getDisplayName(), bean.getDescription(), element);
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaVariableType(Element element) throws ServiceResultException {
		NodeSetBean1 bean = parseElement(NodeClass.VariableType, element);
		VariableTypeAttributes attributes = createVariableTypeAttributes(bean.getDisplayName(), bean.getDescription(),
				element);
		addUaNode(bean, attributes);
	}

	private int getMappedNamespaceByNodeId(int namespaceIndex) {
		try {
			if (namespaceIndex != 0) {
				return getMappedNamespace(namespaceIndex);
			}
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			throw ex;
		}
		return namespaceIndex;
	}

	private void finishReadXmlDocument() {
		for (Entry<NodeId, AddNodesItem> item : getAllNodeItemsList().entrySet()) {
			AddNodesItem value = item.getValue();
			ExpandedNodeId parent = value.getParentNodeId();
			NodeId referenceType = value.getReferenceTypeId();

			Node parentNode = getNodesItemList().get(parent);
			Node current = getNodesItemList().get(this.namespaceTable.toExpandedNodeId(item.getKey()));

			if (parentNode != null) {
				ReferenceNode[] parentReferences = parentNode.getReferences();
				ReferenceNode[] references = current.getReferences();

				ReferenceNode ref = new ReferenceNode();
				ref.setIsInverse(false);
				ref.setReferenceTypeId(referenceType);
				ref.setTargetId(this.namespaceTable.toExpandedNodeId(item.getKey()));

				ReferenceNode inverseRef = new ReferenceNode();
				inverseRef.setIsInverse(true);
				inverseRef.setReferenceTypeId(referenceType);
				inverseRef.setTargetId(parent);

				List<ReferenceNode> parentRefList = referencesToList(parentReferences);
				List<ReferenceNode> refList = referencesToList(references);

				checkReferenceToAdd(parentRefList, ref);
				checkReferenceToAdd(refList, inverseRef);

				parentNode.setReferences(parentRefList.toArray(new ReferenceNode[0]));
				current.setReferences(refList.toArray(new ReferenceNode[0]));
			}
		}
	}

	private void checkReferenceToAdd(List<ReferenceNode> references, ReferenceNode referenceToAdd) {
		boolean contains = false;
		for (ReferenceNode reference : references) {
			if (reference.equals(referenceToAdd)) {
				contains = true;
				break;
			}
		}

		if (!contains) {
			references.add(referenceToAdd);
		}
	}

	private void fetchOpcNodeTypeStructure(Node current, Map<Node, List<Node>> properties, NodeId type, NodeId baseType,
			boolean isType) throws ServiceResultException {

		List<Node> children = new ArrayList<>();
		properties.put(current, children);

		ExpandedNodeId[] hasProperties = current.findTargets(Identifiers.HasProperty, false);
		for (ExpandedNodeId hasProperty : hasProperties) {
			Node property = getNodesItemById(hasProperty);
			if (property != null) {
				// TODO: Check remove clone
				Node cloned = property.clone();
				children.add(cloned);
				fetchOpcNodeTypeStructure(cloned, properties, type, baseType, false);

			}
		}

		if (baseType.equals(type) || !isType) {
			return;
		}

		ExpandedNodeId parentType = current.findTarget(Identifiers.HasSubtype, true);
		fetchOpcNodeTypeStructure(getNodesItemById(parentType), properties, getNamespaceTable().toNodeId(parentType),
				baseType, true);
	}

	/**
	 * Extracts an OPC UA Type.
	 * 
	 * @param transformer
	 * @param statemachine
	 * @param entry
	 * @param type
	 * @param element
	 * @param structure
	 * @param idMapping
	 * @return
	 */
	private Node extractOPCUAType(StateMachineMappingContext mappingContext, ExtractOpcUaStructureContext context,
			AbstractStateMachineToOpcTransformer transformer, StateMachine statemachine, Entry<Node, List<Node>> entry,
			Node type, NamedElement element, Map<Node, List<Node>> structure,
			Map<ExpandedNodeId, ExpandedNodeId> idMapping) {

		Node target = null;
		try {
			Node node = entry.getKey();

			DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
			QualifiedName browseName = null;
			NodeAttributes attributes = null;
			ExtensionObject extAttr = null;
			NodeClass nodeClass = null;
			ExpandedNodeId parentId = null;
			NodeId referenceType = null;
			ExpandedNodeId typeDefinition = null;
			ExpandedNodeId requestedNewId = null;

			switch (node.getNodeClass()) {
			case ObjectType:
				// skip super type node generation
				if (context.isTypeDefined()) {
					idMapping.put(getNamespaceTable().toExpandedNodeId(node.getNodeId()), context.getNodeIdOfType());
					return null;
				}

				// new node id
				requestedNewId = getNamespaceTable().toExpandedNodeId(transformer.getNewNodeId(getNamespaceTable(),
						statemachine, /* type.getNodeId().getNamespaceIndex(), */ element, null));
				context.setTypeDefined(true);
				context.setIdOfType(requestedNewId);

				idMapping.put(getNamespaceTable().toExpandedNodeId(node.getNodeId()), requestedNewId);

				// attributes
				attributes = new ObjectAttributes();
				((ObjectAttributes) attributes).setDescription(new LocalizedText(""));
				((ObjectAttributes) attributes)
						.setDisplayName(new LocalizedText(IStateMachineExtractor.encapsulateName(element)));
				((ObjectAttributes) attributes).setEventNotifier(UnsignedByte.ZERO);
				((ObjectAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
				((ObjectAttributes) attributes).setWriteMask(node.getWriteMask());
				// properties
				browseName = new QualifiedName(1, IStateMachineExtractor.encapsulateName(element));
				extAttr = ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance());
				nodeClass = NodeClass.Object;
				parentId = getNamespaceTable().toExpandedNodeId(type.getNodeId());
				referenceType = Identifiers.HasComponent;
				typeDefinition = getNamespaceTable().toExpandedNodeId(node.getNodeId());
				target = nodeFactory.createNode(getNamespaceTable(), browseName, extAttr, nodeClass, parentId,
						referenceType, requestedNewId, typeDefinition);
				// copy non hierachical references to type
				copyReferencesForStatemachineType(node, target);
				mappingContext.mapNamedElement(requestedNewId, element);
				break;
			case Object:
				// new node id
				requestedNewId = getNamespaceTable().toExpandedNodeId(transformer.getNewNodeId(getNamespaceTable(),
						statemachine, /* type.getNodeId().getNamespaceIndex(), */ element, null));
				idMapping.put(getNamespaceTable().toExpandedNodeId(node.getNodeId()), requestedNewId);
				// attributes
				attributes = new ObjectAttributes();
				((ObjectAttributes) attributes).setDescription(new LocalizedText(""));
				((ObjectAttributes) attributes).setDisplayName(new LocalizedText(element.getName(), Locale.ENGLISH));
				((ObjectAttributes) attributes).setEventNotifier(UnsignedByte.ZERO);
				((ObjectAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
				((ObjectAttributes) attributes).setWriteMask(node.getWriteMask());

				browseName = new QualifiedName(1, element.getName());
				extAttr = ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance());
				nodeClass = NodeClass.Object;
				parentId = getNamespaceTable().toExpandedNodeId(type.getNodeId());
				referenceType = Identifiers.HasComponent;
				typeDefinition = getNamespaceTable().toExpandedNodeId(node.getNodeId());

				target = nodeFactory.createNode(getNamespaceTable(), browseName, extAttr, nodeClass, parentId,
						referenceType, requestedNewId, typeDefinition);
				// copy non hierachical references to type
				copyReferencesForStatemachineType(node, target);
				break;
			case Variable:
				// new node id
				requestedNewId = getNamespaceTable()
						.toExpandedNodeId(transformer.getNewNodeId(getNamespaceTable(), statemachine,
								/* type.getNodeId().getNamespaceIndex(), */ element, node.getBrowseName().getName()));
				idMapping.put(getNamespaceTable().toExpandedNodeId(node.getNodeId()), requestedNewId);

				// attributes
				attributes = new VariableAttributes();
				((VariableAttributes) attributes).setAccessLevel(((VariableNode) node).getAccessLevel());
				((VariableAttributes) attributes).setArrayDimensions(((VariableNode) node).getArrayDimensions());
				((VariableAttributes) attributes).setDataType(((VariableNode) node).getDataType());
				((VariableAttributes) attributes).setDescription(new LocalizedText(""));
				((VariableAttributes) attributes).setDisplayName(node.getDisplayName());
				((VariableAttributes) attributes).setHistorizing(((VariableNode) node).getHistorizing());
				((VariableAttributes) attributes)
						.setMinimumSamplingInterval(((VariableNode) node).getMinimumSamplingInterval());
				((VariableAttributes) attributes).setUserAccessLevel(((VariableNode) node).getUserAccessLevel());
				((VariableAttributes) attributes).setUserWriteMask(((VariableNode) node).getUserWriteMask());
				((VariableAttributes) attributes).setValue(((VariableNode) node).getValue());
				((VariableAttributes) attributes).setWriteMask(((VariableNode) node).getWriteMask());

				browseName = ((VariableNode) node).getBrowseName();
				extAttr = ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance());
				nodeClass = NodeClass.Variable;

				referenceType = Identifiers.HasProperty;

				typeDefinition = node.findTarget(Identifiers.HasTypeDefinition, false);
				// parent id
				parentId = isChildOf(structure, node, idMapping);

				target = nodeFactory.createNode(getNamespaceTable(), browseName, extAttr, nodeClass, parentId,
						referenceType, requestedNewId, typeDefinition);
				// copy non hierachical references to type
				copyReferencesForStatemachineType(node, target);
				break;
			case VariableType:
			case DataType:
			case Method:
			case ReferenceType:
			case View:
			default:
				break;
			}
			//

		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return target;
	}

	/**
	 * Copy references from a source structure to target type structure.
	 * 
	 * @param source OPC UA source node references to copy
	 * @param target OPC UA target node to insert references
	 */
	private void copyReferencesForStatemachineType(Node source, Node target) {
		// no references to copy
		ReferenceNode[] sourceReferences = source.getReferences();
		if (sourceReferences == null) {
			return;
		}

		// base copy target references
		List<ReferenceNode> referencesToAdd = new ArrayList<>();
		ReferenceNode[] targetReferences = target.getReferences();
		if (targetReferences != null) {
			for (ReferenceNode targetRefs : targetReferences) {
				referencesToAdd.add(targetRefs);
			}
		}

		// copy additional references
		for (ReferenceNode reference : sourceReferences) {
			boolean isInverse = reference.getIsInverse();
			NodeId referenceId = reference.getReferenceTypeId();
			// modelling rules
			if (Identifiers.HasModellingRule.equals(referenceId)) {
				// create reference node
				ReferenceNode referenceToAdd = new ReferenceNode();
				referenceToAdd.setIsInverse(isInverse);
				referenceToAdd.setReferenceTypeId(referenceId);
				referenceToAdd.setTargetId(reference.getTargetId());
				// add reference node
				referencesToAdd.add(referenceToAdd);
			}
		}

		target.setReferences(referencesToAdd.toArray(new ReferenceNode[0]));
	}

	private ExpandedNodeId isChildOf(Map<Node, List<Node>> structure, Node node,
			Map<ExpandedNodeId, ExpandedNodeId> idMapping) {
		ExpandedNodeId nodeId = ExpandedNodeId.NULL;
		for (Entry<Node, List<Node>> entry : structure.entrySet()) {
			for (Node child : entry.getValue()) {
				if (child.getNodeId().equals(node.getNodeId())) {
					nodeId = getNamespaceTable().toExpandedNodeId(entry.getKey().getNodeId());
					break;
				}
			}
			if (!ExpandedNodeId.isNull(nodeId)) {
				break;
			}
		}

		ExpandedNodeId result = idMapping.get(nodeId);
//		if (ExpandedNodeId.isNull(result)) {
//			System.out.println("");
//		}
		return result;
	}

	private List<ReferenceNode> referencesToList(ReferenceNode[] references) {
		List<ReferenceNode> refList = new ArrayList<ReferenceNode>();
		for (ReferenceNode ref : references) {
			refList.add(ref);
		}

		return refList;
	}

}
