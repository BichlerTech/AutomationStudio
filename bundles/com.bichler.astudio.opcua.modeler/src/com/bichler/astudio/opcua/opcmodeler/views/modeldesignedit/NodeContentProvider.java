package com.bichler.astudio.opcua.opcmodeler.views.modeldesignedit;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.enums.NodeAttributeName;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.Variant;

public class NodeContentProvider implements ITreeContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getChildren(Object parentElement) {
		List<OPCEntry<String, Object>> attributes = new ArrayList<OPCEntry<String, Object>>();
		// node attributes
		if (parentElement instanceof Node) {
			attributes.add(new OPCEntry<String, Object>(NodeAttributeName.BrowseName.name(),
					((Node) parentElement).getBrowseName()));
			attributes.add(new OPCEntry<String, Object>(NodeAttributeName.Descriptions.name(),
					((Node) parentElement).getDescription()));
			attributes.add(new OPCEntry<String, Object>(NodeAttributeName.DisplayName.name(),
					((Node) parentElement).getDisplayName()));
			attributes.add(
					new OPCEntry<String, Object>(NodeAttributeName.NodeId.name(), ((Node) parentElement).getNodeId()));
			attributes.add(new OPCEntry<String, Object>(NodeAttributeName.UserWriteMask.name(),
					((Node) parentElement).getUserWriteMask()));
			attributes.add(new OPCEntry<String, Object>(NodeAttributeName.WriteMask.name(),
					((Node) parentElement).getWriteMask()));
			// object node attributes
			if (parentElement instanceof ObjectNode) {
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.EventNotifier.name(),
						((ObjectNode) parentElement).getEventNotifier()));
			}
			// object type node attributes
			else if (parentElement instanceof ObjectTypeNode) {
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.IsAbstract.name(),
						((ObjectTypeNode) parentElement).getIsAbstract()));
			}
			// variable node attributes
			else if (parentElement instanceof VariableNode) {
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.AccessLevel.name(),
						((VariableNode) parentElement).getAccessLevel()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.ArrayDimensions.name(),
						((VariableNode) parentElement).getArrayDimensions()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.DataType.name(),
						((VariableNode) parentElement).getDataType()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.Historizing.name(),
						((VariableNode) parentElement).getHistorizing()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.MinimumSamplingInterval.name(),
						((VariableNode) parentElement).getMinimumSamplingInterval()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.UserAccessLevel.name(),
						((VariableNode) parentElement).getUserAccessLevel()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.Value.name(),
						((VariableNode) parentElement).getValue()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.ValueRank.name(),
						ValueRanks.getValueRanks(((VariableNode) parentElement).getValueRank())));
			}
			// variable type node
			else if (parentElement instanceof VariableTypeNode) {
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.ArrayDimensions.name(),
						((VariableTypeNode) parentElement).getArrayDimensions()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.DataType.name(),
						((VariableTypeNode) parentElement).getDataType()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.IsAbstract.name(),
						((VariableTypeNode) parentElement).getIsAbstract()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.Value.name(),
						((VariableTypeNode) parentElement).getValue()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.ValueRank.name(),
						ValueRanks.getValueRanks(((VariableNode) parentElement).getValueRank())));
			}
			// reference type node
			else if (parentElement instanceof ReferenceTypeNode) {
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.InverseName.name(),
						((ReferenceTypeNode) parentElement).getInverseName()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.Symmetric.name(),
						((ReferenceTypeNode) parentElement).getSymmetric()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.IsAbstract.name(),
						((ReferenceTypeNode) parentElement).getIsAbstract()));
			}
			// data type node
			else if (parentElement instanceof DataTypeNode) {
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.IsAbstract.name(),
						((DataTypeNode) parentElement).getIsAbstract()));
			}
			// method node
			else if (parentElement instanceof MethodNode) {
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.Executable.name(),
						((MethodNode) parentElement).getExecutable()));
				attributes.add(new OPCEntry<String, Object>(NodeAttributeName.UserExecutable.name(),
						((MethodNode) parentElement).getUserExecutable()));
			}
			return attributes.toArray();
		}
		// entry
		else if (parentElement instanceof OPCEntry) {
			// qualified name attributes
			if (((OPCEntry<String, Object>) parentElement).getValue() instanceof QualifiedName) {
				attributes.add(new OPCEntry<String, Object>("NamespaceIndex",
						((QualifiedName) ((OPCEntry<String, Object>) parentElement).getValue()).getNamespaceIndex(),
						(OPCEntry<String, Object>) parentElement));
				attributes.add(new OPCEntry<String, Object>("Name",
						((QualifiedName) ((OPCEntry<String, Object>) parentElement).getValue()).getName(),
						(OPCEntry<String, Object>) parentElement));
				return attributes.toArray();
			}
			// localized text attributes
			else if (((OPCEntry<String, Object>) parentElement).getValue() instanceof LocalizedText) {
				LocalizedText t = (LocalizedText) ((OPCEntry<String, Object>) parentElement).getValue();
				// testcases to see what the correct value of the locale should
				// be displayed
				// Locale l = t.getLocale();
				String lid = t.getLocaleId();
				attributes.add(new OPCEntry<String, Object>("Locale", lid, (OPCEntry<String, Object>) parentElement));
				attributes.add(new OPCEntry<String, Object>("Text",
						((LocalizedText) ((OPCEntry<String, Object>) parentElement).getValue()).getText(),
						(OPCEntry<String, Object>) parentElement));
				return attributes.toArray();
			}
			// node id attributes
			else if (((OPCEntry<String, Object>) parentElement).getValue() instanceof NodeId) {
				attributes.add(new OPCEntry<String, Object>("NamespaceIndex",
						((NodeId) ((OPCEntry<String, Object>) parentElement).getValue()).getNamespaceIndex(),
						(OPCEntry<String, Object>) parentElement));
				attributes.add(new OPCEntry<String, Object>("ID",
						((NodeId) ((OPCEntry<String, Object>) parentElement).getValue()).getValue(),
						(OPCEntry<String, Object>) parentElement));
				return attributes.toArray();
			} else if (((OPCEntry<String, Object>) parentElement).getValue() instanceof Variant) {
				Object[] array = (Object[]) ((Variant) ((OPCEntry<String, Object>) parentElement).getValue())
						.getValue();
				for (int ii = 0; ii < array.length; ii++) {
					attributes.add(
							new OPCEntry<String, Object>("" + ii, array[ii], (OPCEntry<String, Object>) parentElement));
				}
				return attributes.toArray();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getParent(Object element) {
		OPCEntry<String, Object> parent = ((OPCEntry<String, Object>) element).getParent();
		if (parent != null)
			return parent;
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof OPCEntry) {
			if (((OPCEntry<String, Object>) element).getValue() instanceof QualifiedName) {
				return true;
			} else if (((OPCEntry<String, Object>) element).getValue() instanceof LocalizedText) {
				return true;
			} else if (((OPCEntry<String, Object>) element).getValue() instanceof NodeId) {
				return true;
			} else if (((OPCEntry<String, Object>) element).getValue() instanceof Variant) {
				Variant v = (Variant) ((OPCEntry<String, Object>) element).getValue();
				if (!v.isEmpty()) {
					int dim = v.getArrayDimensions().length;
					if (dim > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
