package com.bichler.astudio.opcua.datadictionary.base.model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.xml.sax.SAXException;

import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;

public abstract class AbstractStructuredDataTypeManager {

	private static final String NAME_TYPEDICTIONARY = "TypeDictionary";

	private AbstractStructuredDatatypeNodeGenerator nodeGenerator;

	public AbstractStructuredDataTypeManager() {

	}

	public void setNodeGenerator(AbstractStructuredDatatypeNodeGenerator generator) {
		this.nodeGenerator = generator;
	}

	public boolean initOpcEnumerationStructure(DataTypeNode node) {
		try {
			initOpcUaDictionaryStructure(node);
		} catch (ServiceResultException e) {
			return false;
		}

		return true;
	}

	public boolean initOpcBinaryStructure(DataTypeNode node) {
		String name = node.getBrowseName().getName();
		NodeId inputId = node.getNodeId();
		ExpandedNodeId defaultBinaryEncoding = this.nodeGenerator.findDefaultBinaryEncoding(node);

		try {
			if (ExpandedNodeId.isNull(defaultBinaryEncoding)) {
				defaultBinaryEncoding = this.nodeGenerator.createNodeDefaultBinary(inputId);
			}

			ExpandedNodeId binarySchemaId = initOpcUaDictionaryStructure(node);
			// find variable datatype description
			ExpandedNodeId dataTypeDescription = this.nodeGenerator.findDatatypeDescription(binarySchemaId, name);
			if (ExpandedNodeId.isNull(dataTypeDescription)) {
				// VARIABLE - DatatypeDescriptionType
				dataTypeDescription = this.nodeGenerator.createNodeDataTypeDescription(inputId.getNamespaceIndex(),
						name, binarySchemaId);
			}
			// default binary hasDescription
			this.nodeGenerator.createReferencesHasDescription(defaultBinaryEncoding, dataTypeDescription);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Cannot create BinarySchema for " + node.getBrowseName().getName());
			return false;
		}
		return true;
	}

	public ByteString writeOpcEnumDictionary(DataTypeNode node, DefinitionBean definition) {
		Node binarySchema = getNode(Identifiers.OPCBinarySchema_TypeSystem); 
																			
		ExpandedNodeId[] targets = binarySchema.findTargets(Identifiers.HasComponent, false);
		ExpandedNodeId binarySchemaId = ExpandedNodeId.NULL;

		for (ExpandedNodeId target : targets) {
			Node targetNode = getNode(target); // OpcUtils.getNode(this.opcServer, target);
			if (targetNode == null) {
				continue;
			}
			if (targetNode.getNodeId().getNamespaceIndex() == node.getNodeId().getNamespaceIndex()) {
				binarySchemaId = target;
				break;
			}
		}

		if (ExpandedNodeId.isNull(binarySchemaId)) {
			throw new IllegalStateException("No structure for binary datatype encoding!");
		}

		VariableNode opcUaBinarySchema = (VariableNode) getNode(binarySchemaId); // OpcUtils.getNode(this.opcServer,
																					// binarySchemaId);
		ByteString value = (ByteString) opcUaBinarySchema.getValue().getValue();

		try {
			AbstractDataDictionaryHelper ddHelper = createDataDictionaryHelper(); // new
																					// AbstractDataDictionaryHelper(this.opcServer);
			ByteString dataDictionary = ddHelper.createNodeFromDefinition(node, value, definition);
			opcUaBinarySchema.setValue(new Variant(dataDictionary));
			return dataDictionary;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return ByteString.EMPTY;
	}

	public ByteString writeOpcDataDictionary(DataTypeNode node, DefinitionBean definition) {
		ExpandedNodeId binarySchemaId = findBinarySchemaId(node);
		if (ExpandedNodeId.isNull(binarySchemaId)) {
			throw new IllegalStateException("No structure for binary datatype encoding!");
		}

		VariableNode opcUaBinarySchema = (VariableNode) getNode(binarySchemaId);
		ByteString value = (ByteString) opcUaBinarySchema.getValue().getValue();
		try {
			AbstractDataDictionaryHelper ddHelper = createDataDictionaryHelper();
			ByteString dataDictionary = ddHelper.createNodeFromDefinition(node, value, definition);
			opcUaBinarySchema.setValue(new Variant(dataDictionary));
			return dataDictionary;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return ByteString.EMPTY;
	}

	

	public VariableNode findNodeEnumerationStrings(NodeId enumerationId) {
		Node node = getNode(enumerationId); // OpcUtils.getNode(this.opcServer, enumerationId);
		// read enumeration values
		for (ReferenceNode reference : node.getReferences()) {
			// check isComponent reference
			boolean isProperty = isTypeOf(reference.getReferenceTypeId(), Identifiers.HasProperty); // OpcUtils.isTypeOf(this.opcServer,
			if (reference.getIsInverse() || !isProperty) {
				continue;
			}
			// check target variable value
			Node target = getNode(reference.getTargetId()); // OpcUtils.getNode(this.opcServer,
															// reference.getTargetId());
			String name = target.getBrowseName().getName();
			if (!(target instanceof VariableNode) && !"EnumStrings".equalsIgnoreCase(name)) {
				continue;
			}
			return (VariableNode) target;
		}

		return null;
	}

	private String askForName(String typeDictionaryName) {
		String name = askForTypeDictionaryName(typeDictionaryName);
		return name;
	}

	private ExpandedNodeId initOpcUaDictionaryStructure(DataTypeNode node) throws ServiceResultException {
		NodeId inputId = node.getNodeId();

		try {
			ExpandedNodeId binarySchemaId = this.nodeGenerator.findBinarySchemaId(inputId.getNamespaceIndex());
			if (ExpandedNodeId.isNull(binarySchemaId)) {
				String binarySchemaName = askForName(NAME_TYPEDICTIONARY);
				if (binarySchemaName == null) {
					throw new ServiceResultException(
							"Cannot create BinarySchema for " + node.getBrowseName().getName());
				}
				binarySchemaId = this.nodeGenerator.createNodeBinarySchema(inputId.getNamespaceIndex(),
						binarySchemaName);
			}
			return binarySchemaId;
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Cannot create BinarySchema for " + node.getBrowseName().getName());
			throw e;
		}
	}

	protected abstract String askForTypeDictionaryName(String defaultName);

	protected abstract ExpandedNodeId findBinarySchemaId(DataTypeNode node);
	
	protected abstract Node getNode(NodeId nodeId);

	protected abstract Node getNode(ExpandedNodeId nodeId);

	protected abstract boolean isTypeOf(NodeId type, NodeId typeOf);

	protected abstract AbstractDataDictionaryHelper createDataDictionaryHelper();

	protected abstract AbstractStructuredDatatypeNodeGenerator createStructuredDatatypeNodeGenerator();

}
