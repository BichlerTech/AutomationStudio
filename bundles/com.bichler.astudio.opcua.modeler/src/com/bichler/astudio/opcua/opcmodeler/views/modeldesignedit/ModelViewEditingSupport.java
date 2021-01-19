package com.bichler.astudio.opcua.opcmodeler.views.modeldesignedit;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.UUID;

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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Control;
import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.utils.BijectionMap;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.dialogs.NodeValueDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.ValueDialog;

public class ModelViewEditingSupport extends EditingSupport {
	private TreeViewer treeViewer = null;
	private TextCellEditor textCellEditor = null;
	private ComboBoxCellEditor comboBoxCellEditor = null;

	public ModelViewEditingSupport(TreeViewer viewer, int column) {
		super(viewer);
		this.treeViewer = viewer;
		this.textCellEditor = new TextCellEditor(viewer.getTree());
	}

	/**
	 * Returns the cell editor for the element
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected CellEditor getCellEditor(Object element) {
		// boolean
		if (((OPCEntry<String, Object>) element).getValue() instanceof Boolean) {
			String[] booleanItems = { "true", "false" };
			this.comboBoxCellEditor = new ComboBoxCellEditor(this.treeViewer.getTree(), booleanItems);
			return this.comboBoxCellEditor;
		}
		// namespace index
		else if ("NamespaceIndex".equals(((OPCEntry<String, Object>) element).getKey())) {
			if (NodeAttributeName.NodeId.name().equals(((OPCEntry<String, Object>) element).getParent().getKey())) {
				this.comboBoxCellEditor = new ComboBoxCellEditor(this.treeViewer.getTree(),
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray());
				return this.comboBoxCellEditor;
			}
		}
		// locale
		else if ("Locale".equals(((OPCEntry<String, Object>) element).getKey())) {
			Locale[] availableLocales = Locale.getAvailableLocales();
			String[] localeStrings = new String[availableLocales.length];
			for (int i = 0; i < availableLocales.length; i++) {
				localeStrings[i] = availableLocales[i].toString();
			}
			this.comboBoxCellEditor = new ComboBoxCellEditor(this.treeViewer.getTree(), localeStrings);
			return this.comboBoxCellEditor;
		}
		// value rank
		else if (NodeAttributeName.ValueRank.name().equals(((OPCEntry<String, Object>) element).getKey())) {
			String[] valueRanks = new String[ValueRanks.values().length];
			for (int i = 0; i < ValueRanks.values().length; i++) {
				valueRanks[i] = ValueRanks.values()[i].toString();
			}
			this.comboBoxCellEditor = new ComboBoxCellEditor(this.treeViewer.getTree(), valueRanks);
			return this.comboBoxCellEditor;
		}
		// value
		else if (NodeAttributeName.Value.name().equals(((OPCEntry<String, Object>) element).getKey())) {
			if (((Variant) ((OPCEntry<String, Object>) element).getValue()).isArray()) {
				return new DialogCellEditor(this.treeViewer.getTree()) {
					@Override
					protected Object openDialogBox(Control cellEditorWindow) {
						ValueDialog dialog = new ValueDialog(treeViewer.getTree().getShell(), (Variant) getValue());
						int open = dialog.open();
						if (open == Dialog.OK) {
							return (Variant) dialog.getValue();
						}
						return null;
					}
				};
			}
		}
		return this.textCellEditor;
	}

	/**
	 * Checks if the given element is editable
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected boolean canEdit(Object element) {
		if (((OPCEntry<String, Object>) element).getValue() instanceof QualifiedName) {
			return false;
		}
		if (((OPCEntry<String, Object>) element).getValue() instanceof LocalizedText) {
			return false;
		}
		if (((OPCEntry<String, Object>) element).getValue() instanceof NodeId) {
			return false;
		}
		boolean hasParent = ((OPCEntry<String, Object>) element).getParent() != null ? true : false;
		// element has a parent
		if (hasParent) {
			// element is a value
			if (NodeAttributeName.Value.name().equals(((OPCEntry<String, Object>) element).getParent().getKey())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the value for the element
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object getValue(Object element) {
		Object value = ((OPCEntry<String, Object>) element).getValue();
		// change the namespaceindex of the combobox cell editor
		if ("NamespaceIndex".equals(((OPCEntry<String, Object>) element).getKey())) {
			if (NodeAttributeName.NodeId.name().equals(((OPCEntry<String, Object>) element).getParent().getKey())) {
				int itemInCombo = 0;
				for (String item : this.comboBoxCellEditor.getItems()) {
					if (item.equals(value)) {
						break;
					}
					itemInCombo++;
				}
				return itemInCombo;
			}
		} else if ("Locale".equals(((OPCEntry<String, Object>) element).getKey())) {
			int itemInCombo = 0;
			for (String item : this.comboBoxCellEditor.getItems()) {
				if (item.equals(value)) {
					break;
				}
				itemInCombo++;
			}
			return itemInCombo;
		} else if (NodeAttributeName.ArrayDimensions.name().equals(((OPCEntry<String, Object>) element).getKey())) {
			return "" + ((UnsignedInteger[]) ((OPCEntry<String, Object>) element).getValue()).length;
		} else if (NodeAttributeName.ValueRank.name().equals(((OPCEntry<String, Object>) element).getKey())) {
			int itemInCombo = 0;
			for (String item : this.comboBoxCellEditor.getItems()) {
				if (item.equals(((ValueRanks) value).name())) {
					break;
				}
				itemInCombo++;
			}
			return itemInCombo;
		}
		// value is an boolean, open the combobox cell editor
		else if (value instanceof Boolean) {
			int itemInCombo = 0;
			for (String item : this.comboBoxCellEditor.getItems()) {
				if (item.equals(value)) {
					break;
				}
				itemInCombo++;
			}
			return itemInCombo;
		}
		// variant value . toString
		if (value instanceof Variant) {
			if (((Variant) value).isEmpty()) {
				return "";
			}
			if (((Variant) value).isArray()) {
				return value;
			}
			return ((Variant) value).getValue().toString();
		}
		// value is null
		if (value == null) {
			return "";
		}
		// text cell editor
		return ((OPCEntry<String, Object>) element).getValue().toString();
	}

	/**
	 * Sets the changed value to the element
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object element, Object value) {
		if ("NamespaceIndex".equals(((OPCEntry<String, Object>) element).getKey())) {
			// namespaceIndex of nodeId
			if (NodeAttributeName.NodeId.name().equals(((OPCEntry<String, Object>) element).getParent().getKey())) {
				if (((Integer) value) != -1) {
					String valueOfComboboxCellEditor = this.comboBoxCellEditor.getItems()[((Integer) value)];
					NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
					int namespaceIndexValue = nsTable.add(-1, valueOfComboboxCellEditor);
					((OPCEntry<String, Object>) element).setValue(namespaceIndexValue);
				}
			}
			// namespaceIndex of browsename
			else if (NodeAttributeName.BrowseName.name()
					.equals(((OPCEntry<String, Object>) element).getParent().getKey())) {
				((OPCEntry<String, Object>) element).setValue(new Integer(((String) value)));
			}
		}
		// set a locale
		else if ("Locale".equals(((OPCEntry<String, Object>) element).getKey())) {
			if (((Integer) value) != -1) {
				Locale[] locales = Locale.getAvailableLocales();
				Locale locale = locales[(Integer) value];
				((OPCEntry<String, Object>) element).setValue(locale.toString());
			}
		}
		// set an array dimension
		else if (NodeAttributeName.ArrayDimensions.name().equals(((OPCEntry<String, Object>) element).getKey())) {
			try {
				int dim = new Integer((String) value);
				UnsignedInteger[] arrayDimension = new UnsignedInteger[dim];
				((OPCEntry<String, Object>) element).setValue(arrayDimension);
			} catch (IllegalArgumentException iae) {
			}
		}
		// set a value
		else if (NodeAttributeName.Value.name().equals(((OPCEntry<String, Object>) element).getKey())) {
			Node input = (Node) this.treeViewer.getInput();
			NodeId dataType = null;
			if (input instanceof VariableNode) {
				dataType = ((VariableNode) input).getDataType();
			} else if (input instanceof VariableTypeNode) {
				dataType = ((VariableTypeNode) input).getDataType();
			}
			// value is an array
			if (((Variant) value).isArray()) {
				// String array
				if (((Variant) value).getCompositeClass() == String.class) {
					((OPCEntry<String, Object>) element).setValue(value);
				}
			}
			// value is an extension object (complex item)
			else if (((Variant) value).getCompositeClass() == ExtensionObject.class) {
			}
			// value is a builtin type (simple value)
			else {
				BijectionMap<NodeId, Class<?>> map = BuiltinsMap.ID_CLASS_MAP;
				Class<?> right = map.getRight(dataType);
				Object constructedValue = null;
				if (right != null) {
					try {
						constructedValue = right.getConstructor(String.class).newInstance(value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					((OPCEntry<String, Object>) element).setValue(new Variant(constructedValue));
				} else {
					((OPCEntry<String, Object>) element).setValue(Variant.NULL);
				}
			}
		}
		// set a boolean value
		else if (((OPCEntry<String, Object>) element).getValue() instanceof Boolean) {
			if (((Integer) value) != -1) {
				String valueOfComboboxCellEditor = this.comboBoxCellEditor.getItems()[((Integer) value)];
				((OPCEntry<String, Object>) element).setValue(new Boolean(valueOfComboboxCellEditor));
			}
		} else if (NodeAttributeName.ValueRank.name().equals(((OPCEntry<String, Object>) element).getKey())) {
			if (((Integer) value) != -1) {
				String valueOfComboboxCellEditor = this.comboBoxCellEditor.getItems()[((Integer) value)];
				ValueRanks valueRank = ValueRanks.valueOf(valueOfComboboxCellEditor);
				((OPCEntry<String, Object>) element).setValue(valueRank);
			}
		}
		// set a string value
		else {
			String valueOfTextCellEditor = this.textCellEditor.getValue().toString();
			((OPCEntry<String, Object>) element).setValue(valueOfTextCellEditor);
		}
		boolean refresh = refreshNode((OPCEntry<String, Object>) element);
		if (refresh) {
			getViewer().update(element, null);
		}
	}

	/**
	 * Refresh the node attributes, the this.treeviewer and the browse view (because
	 * of displayname....)
	 * 
	 * @param changedAttribute
	 */
	private boolean refreshNode(OPCEntry<String, Object> changedAttribute) {
		Node input = (Node) treeViewer.getInput();
		// values which has a parent (Browsename, NodeId)
		if ("NamespaceIndex".equals(changedAttribute.getKey())) {
			OPCEntry<String, Object> parent = changedAttribute.getParent();
			// browsename namespaceindex
			if (NodeAttributeName.BrowseName.name().equals(parent.getKey())) {
				String name = input.getBrowseName().getName();
				QualifiedName qName = new QualifiedName((Integer) changedAttribute.getValue(), name);
				input.setBrowseName(qName);
				parent.setValue(qName);
				getViewer().update(parent, null);
			}
			// nodeid namespaceindex
			else if (NodeAttributeName.NodeId.name().equals(parent.getKey())) {
				Object nodeIdValue = input.getNodeId().getValue();
				NodeId nodeId = null;
				if (nodeIdValue instanceof byte[]) {
					nodeId = new NodeId((Integer) changedAttribute.getValue(), (byte[]) nodeIdValue);
				} else if (nodeIdValue instanceof Integer) {
					nodeId = new NodeId((Integer) changedAttribute.getValue(), (Integer) nodeIdValue);
				} else if (nodeIdValue instanceof String) {
					nodeId = new NodeId((Integer) changedAttribute.getValue(), (String) nodeIdValue);
				} else if (nodeIdValue instanceof UnsignedInteger) {
					nodeId = new NodeId((Integer) changedAttribute.getValue(), (UnsignedInteger) nodeIdValue);
				} else if (nodeIdValue instanceof UUID) {
					nodeId = new NodeId((Integer) changedAttribute.getValue(), (UUID) nodeIdValue);
				}
				boolean hasChanged = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.changeNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris(), input,
								nodeId);
				if (hasChanged) {
					input.setNodeId(nodeId);
					parent.setValue(nodeId);
					getViewer().update(parent, null);
				} else {
					return false;
				}
			}
		}
		// browse name text
		else if ("Name".equals(changedAttribute.getKey())) {
			OPCEntry<String, Object> parent = changedAttribute.getParent();
			if (NodeAttributeName.BrowseName.name().equals(parent.getKey())) {
				QualifiedName value = input.getBrowseName();
				int index = value.getNamespaceIndex();
				value = new QualifiedName(index, (String) changedAttribute.getValue());
				input.setBrowseName(value);
				parent.setValue(value);
				getViewer().update(parent, null);
			}
		}
		// id value of the NodeId
		else if ("ID".equals(changedAttribute.getKey())) {
			OPCEntry<String, Object> parent = changedAttribute.getParent();
			if (NodeAttributeName.NodeId.name().equals(parent.getKey())) {
				int namespaceIndex = input.getNodeId().getNamespaceIndex();
				NodeId nodeIdValue = (NodeId) parent.getValue();
				IdType idType = nodeIdValue.getIdType();
				NodeId newId = null;
				Object value = changedAttribute.getValue();
				switch (idType) {
				case Guid:
					// TODO: CHange string to guid
					newId = new NodeId(namespaceIndex, value.toString());
					break;
				case Numeric:
					if (nodeIdValue.getValue() instanceof Integer) {
						newId = new NodeId(namespaceIndex, Integer.parseInt((String) value));
						break;
					} else if (nodeIdValue.getValue() instanceof UnsignedInteger) {
						newId = new NodeId(namespaceIndex, new UnsignedInteger((String) value));
						break;
					} else if (nodeIdValue.getValue() instanceof byte[]) {
						newId = new NodeId(namespaceIndex, (byte[]) value);
						break;
					}
				case String:
					newId = new NodeId(namespaceIndex, (String) value);
					break;
				case Opaque:
					break;
				}
				boolean hasChanged = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.changeNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris(), input,
								newId);
				if (hasChanged) {
					input.setNodeId(newId);
					parent.setValue(newId);
					getViewer().update(parent, null);
				} else {
					return false;
				}
			}
		}
		// text value from localized text
		else if ("Text".equals(changedAttribute.getKey())) {
			OPCEntry<String, Object> parent = changedAttribute.getParent();
			LocalizedText value = (LocalizedText) parent.getValue();
			String l = value.getLocaleId();
			String text = (String) changedAttribute.getValue();
			value = new LocalizedText(text, l);
			if (NodeAttributeName.DisplayName.name().equals(parent.getKey())) {
				input.setDisplayName(value);
			} else if (NodeAttributeName.Descriptions.name().equals(parent.getKey())) {
				input.setDescription(value);
			}
			parent.setValue(value);
			getViewer().update(parent, null);
		}
		// text value from localized text
		else if ("Locale".equals(changedAttribute.getKey())) {
			OPCEntry<String, Object> parent = changedAttribute.getParent();
			LocalizedText value = (LocalizedText) parent.getValue();
			String l = (String) changedAttribute.getValue();
			Locale locale = null;
			for (Locale loc : Locale.getAvailableLocales()) {
				if (loc.toString().equals(l)) {
					locale = loc;
					break;
				}
			}
			String text = value.getText();
			LocalizedText newValue = new LocalizedText(text, locale);
			if (NodeAttributeName.DisplayName.name().equals(parent.getKey())) {
				input.setDisplayName(newValue);
			} else if (NodeAttributeName.Descriptions.name().equals(parent.getKey())) {
				input.setDescription(newValue);
			}
			parent.setValue(newValue);
			getViewer().update(parent, null);
		} else if (NodeAttributeName.AccessLevel.name().equals(changedAttribute.getKey())) {
			if (input instanceof VariableNode) {
				try {
					UnsignedByte accessLvl = new UnsignedByte((String) changedAttribute.getValue());
					((VariableNode) input).setAccessLevel(accessLvl);
				} catch (IllegalArgumentException iae) {
					UnsignedByte accessLvl = ((VariableNode) input).getAccessLevel();
					changedAttribute.setValue(accessLvl);
				}
			}
		} else if (NodeAttributeName.ArrayDimensions.name().equals(changedAttribute.getKey())) {
			if (input instanceof VariableNode) {
				try {
					Integer arraydimension = new Integer(((UnsignedInteger[]) changedAttribute.getValue()).length);
					UnsignedInteger[] arrayDimValue = new UnsignedInteger[arraydimension];
					((VariableNode) input).setArrayDimensions(arrayDimValue);
				} catch (IllegalArgumentException iae) {
					int value = ((VariableNode) input).getArrayDimensions().length;
					changedAttribute.setValue(value);
				}
			} else if (input instanceof VariableTypeNode) {
				try {
					Integer arraydimension = new Integer(((UnsignedInteger[]) changedAttribute.getValue()).length);
					UnsignedInteger[] arrayDimValue = new UnsignedInteger[arraydimension];
					((VariableTypeNode) input).setArrayDimensions(arrayDimValue);
				} catch (IllegalArgumentException iae) {
					int value = ((VariableTypeNode) input).getArrayDimensions().length;
					changedAttribute.setValue(value);
				}
			}
		} else if (NodeAttributeName.DataType.name().equals(changedAttribute.getKey())) {
			// TODO:
		} else if (NodeAttributeName.EventNotifier.name().equals(changedAttribute.getKey())) {
			if (input instanceof ObjectNode) {
				UnsignedByte ub = null;
				try {
					ub = new UnsignedByte((String) changedAttribute.getValue());
					((ObjectNode) input).setEventNotifier(ub);
				} catch (NumberFormatException nfe) {
					ub = ((ObjectNode) input).getEventNotifier();
					changedAttribute.setValue(ub);
				}
			}
		} else if (NodeAttributeName.Executable.name().equals(changedAttribute.getKey())) {
			((MethodNode) input).setExecutable((Boolean) changedAttribute.getValue());
		} else if (NodeAttributeName.Historizing.name().equals(changedAttribute.getKey())) {
			((VariableNode) input).setHistorizing((Boolean) changedAttribute.getValue());
		} else if (NodeAttributeName.InverseName.name().equals(changedAttribute.getKey())) {
			// TODO:
		} else if (NodeAttributeName.IsAbstract.name().equals(changedAttribute.getKey())) {
			if (input instanceof ObjectTypeNode) {
				((ObjectTypeNode) input).setIsAbstract((Boolean) changedAttribute.getValue());
			} else if (input instanceof VariableTypeNode) {
				((VariableTypeNode) input).setIsAbstract((Boolean) changedAttribute.getValue());
			} else if (input instanceof ReferenceTypeNode) {
				((ReferenceTypeNode) input).setIsAbstract((Boolean) changedAttribute.getValue());
			} else if (input instanceof DataTypeNode) {
				((DataTypeNode) input).setIsAbstract((Boolean) changedAttribute.getValue());
			}
		} else if (NodeAttributeName.MinimumSamplingInterval.name().equals(changedAttribute.getKey())) {
			Double minSamplingInterval = null;
			try {
				minSamplingInterval = new Double((String) changedAttribute.getValue());
				((VariableNode) input).setMinimumSamplingInterval(minSamplingInterval);
				changedAttribute.setValue(minSamplingInterval);
			} catch (NumberFormatException nfe) {
				minSamplingInterval = ((VariableNode) input).getMinimumSamplingInterval();
				changedAttribute.setValue(minSamplingInterval);
			}
		} else if (NodeAttributeName.References.name().equals(changedAttribute.getKey())) {
			// TODO:
		} else if (NodeAttributeName.Symmetric.name().equals(changedAttribute.getKey())) {
			((ReferenceTypeNode) input).setSymmetric((Boolean) changedAttribute.getValue());
		} else if (NodeAttributeName.UserAccessLevel.name().equals(changedAttribute.getKey())) {
			if (input instanceof VariableNode) {
				try {
					UnsignedByte accessLvl = new UnsignedByte((String) changedAttribute.getValue());
					((VariableNode) input).setUserAccessLevel(accessLvl);
				} catch (IllegalArgumentException iae) {
					UnsignedByte accessLvl = ((VariableNode) input).getUserAccessLevel();
					changedAttribute.setValue(accessLvl);
				}
			}
		} else if (NodeAttributeName.UserExecutable.name().equals(changedAttribute.getKey())) {
			((MethodNode) input).setUserExecutable((Boolean) changedAttribute.getValue());
		} else if (NodeAttributeName.UserWriteMask.name().equals(changedAttribute.getKey())) {
			try {
				UnsignedInteger userWriteMask = new UnsignedInteger((String) changedAttribute.getValue());
				input.setUserWriteMask(userWriteMask);
			} catch (IllegalArgumentException e) {
				UnsignedInteger userWriteMask = input.getUserWriteMask();
				changedAttribute.setValue(userWriteMask);
			}
		} else if (NodeAttributeName.Value.name().equals(changedAttribute.getKey())) {
			// value is ready to set in variable
			if (changedAttribute.getValue() instanceof Variant) {
				WriteValue writeValue = new WriteValue();
				writeValue.setAttributeId(Attributes.Value);
				writeValue.setNodeId(input.getNodeId());
				writeValue.setValue(new DataValue((Variant) changedAttribute.getValue()));
				// write value on server
				try {
					ServerInstance.getInstance().getServerInstance().getMaster().write(new WriteValue[] { writeValue },
							true, null, null);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
		} else if (NodeAttributeName.ValueRank.name().equals(changedAttribute.getKey())) {
			int valRank = ((ValueRanks) changedAttribute.getValue()).getValue();
			if (input instanceof VariableNode) {
				((VariableNode) input).setValueRank(valRank);
			} else if (input instanceof VariableTypeNode) {
				((VariableTypeNode) input).setValueRank(valRank);
			}
		} else if (NodeAttributeName.WriteMask.name().equals(changedAttribute.getKey())) {
			try {
				UnsignedInteger writeMask = new UnsignedInteger((String) changedAttribute.getValue());
				input.setUserWriteMask(writeMask);
			} catch (IllegalArgumentException e) {
				UnsignedInteger writeMask = input.getUserWriteMask();
				changedAttribute.setValue(writeMask);
			}
		}
		return true;
	}
}
