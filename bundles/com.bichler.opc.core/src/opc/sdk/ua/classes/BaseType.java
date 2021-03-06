package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.encoding.DecodingException;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class BaseType extends BaseNode {
	boolean isAbstract = false;
	private NodeId superTypeId = null;
	private BaseNode parent;

	public BaseType(NodeClass nodeClass, BaseNode parent) {
		super(nodeClass);
		this.isAbstract = false;
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.None);
		this.parent = parent;
	}

	@Override
	protected void export(ISystemContext context, Node node) {
		super.export(context, node);
		NamespaceTable nsTable = context.getNamespaceUris();
		if (!NodeId.isNull(this.superTypeId)) {
			List<ReferenceNode> refsList = new ArrayList<ReferenceNode>(Arrays.asList(node.getReferences()));
			refsList.add(new ReferenceNode(Identifiers.HasSubtype, true, new ExpandedNodeId(
					nsTable.getUri(this.superTypeId.getNamespaceIndex()), this.superTypeId.getValue(), nsTable)));
			// refsList.add(new ReferenceNode(Identifiers.HasSubtype, true,
			// context.getNamespaceUris().toExpandedNodeId(this.superTypeId)));
			node.setReferences(refsList.toArray(new ReferenceNode[0]));
		}
		switch (getNodeClass()) {
		case ObjectType:
			((ObjectTypeNode) node).setIsAbstract(this.isAbstract);
			break;
		case VariableType:
			((VariableTypeNode) node).setIsAbstract(this.isAbstract);
			break;
		case DataType:
			((DataTypeNode) node).setIsAbstract(this.isAbstract);
			break;
		case ReferenceType:
			((ReferenceTypeNode) node).setIsAbstract(this.isAbstract);
			break;
		}
	}

	@Override
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		super.update(context, decoder, attributesToLoad);
		if (attributesToLoad.contains(AttributesToSave.SuperTypeId)) {
			setSuperTypeId(decoder.getNodeId(null));
		}
		if (attributesToLoad.contains(AttributesToSave.IsAbstract)) {
			setIsAbstract(decoder.getBoolean(null));
		}
	}

	@Override
	protected void initialize(IOPCContext context) {
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.None);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
	}

	@Override
	protected void initialize(ISystemContext context, BaseNode source) {
		if (source instanceof BaseType) {
			this.superTypeId = ((BaseType) source).getSuperTypeId();
			this.isAbstract = ((BaseType) source).getIsAbstract();
		}
		super.initialize(context, source);
	}

	/**
	 * Get the is abstract type.
	 * 
	 * @return IsAbstract
	 */
	public boolean getIsAbstract() {
		return this.isAbstract;
	}

	/**
	 * Set the is abstract type.
	 * 
	 * @param value IsAbstract
	 */
	public void setIsAbstract(boolean value) {
		if (this.isAbstract != value) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.isAbstract = value;
	}

	/**
	 * Get the Identifier of the supertype node.
	 * 
	 * @return SuperTypeId
	 */
	public NodeId getSuperTypeId() {
		return this.superTypeId;
	}

	/**
	 * Set the Identifier of the supertype node.
	 * 
	 * @param value SuperTypeId
	 */
	public void setSuperTypeId(NodeId value) {
		if (this.superTypeId != value) {
			setChangeMask(NodeStateChangeMasks.REFERENCES);
		}
		this.superTypeId = value;
	}
}
