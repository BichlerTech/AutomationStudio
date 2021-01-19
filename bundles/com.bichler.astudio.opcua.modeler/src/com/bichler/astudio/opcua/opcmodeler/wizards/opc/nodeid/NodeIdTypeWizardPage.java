package com.bichler.astudio.opcua.opcmodeler.wizards.opc.nodeid;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.IQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;

public class NodeIdTypeWizardPage extends AbstractNodeIdWizardPage {
	private Node node;

	/**
	 * Create the wizard.
	 * 
	 * @param node
	 * 
	 * @param initId
	 */
	public NodeIdTypeWizardPage(Node node, NodeId initId) {
		super("NodeIdTypeWizardPage", initId);
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.node.type.title"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.node.type.description"));
		// this.initId = initId;
		this.node = node;
	}

	@Override
	protected void validateNodeIdValueText() {
		// isKeyInput = false;
		if (selectedType != null) {
			NodeId nodeId = (NodeId) txtNodeId.getControl().getData(selectedType.name());
			if (nodeId != null && nodeId.getIdType() == selectedType
					&& nodeId.getNamespaceIndex() == comboNodeId.getSelectionIndex()) {
				setValue(nodeId);
			} else {
				// use init nodeid if init namespace is selected
				if (selectedType == getInitId().getIdType() && comboNodeId.getSelectionIndex() == getInitId().getNamespaceIndex()) {
					nodeId = getInitId();
					setValue(nodeId);
				} else if(!NodeId.isNull(nodeId)){
					switch (nodeId.getIdType()) {
					case String:
						int index = comboNodeId.getSelectionIndex();
						String value = (String) getInitId().getValue();
						NodeId validation = new NodeId(index, (String) value);
						Node validationNode = ServerInstance.getNode(validation);
						if (validationNode == null) {
							setValue(nodeId);
							break;
						}
						Logger.getLogger(getClass().getName()).log(Level.INFO,
								"NodeId " + validation.toString() + " already exists, a new NodeId is generated");
					default:
						// get new
						txtNodeId.setContents("");
						getQuickFixProvider().doQuickFix(txtNodeId);
						break;
					}
				} else {
					// do not set new
					//txtNodeId.setContents("");
				getQuickFixProvider().doQuickFix(txtNodeId);
				}
				// new NodeIdFieldQuickFixProvider<NodeId>()
				// .doQuickFix(txt_nodeId);
			}
			txtNodeId.validate();
		}
		// isKeyInput = true;
	}

	@Override
	protected CometCombo createComboNodeId(Composite parent) {
		DesignerFormToolkit tk = new DesignerFormToolkit();
		return tk.createComboNodeId(group, node.getNodeId().getNamespaceIndex());
	}

	@Override
	public NodeIdTypeFieldValidator<String> getNodeIdTypeFieldValidator() {
		return new NodeNodeIdTypeFieldValidator<String>();
	}

	class NodeIdFieldQuickFixProvider<T> implements IQuickFixProvider<String> {
		public NodeIdFieldQuickFixProvider() {
		}

		@Override
		public boolean doQuickFix(ValidatingField<String> value) {
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			int index = nsTable.getIndex(comboNodeId.getText());
			NodeId nxtNodeId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeFactory().showNextNodeId(index, selectedType, ccNodeId);
			setValue(nxtNodeId);
			return true;
		}

		@Override
		public String getQuickFixMenuText() {
			return "Insert a valid NodeId!";
		}

		@Override
		public boolean hasQuickFix(String value) {
			/*
			 * if (value != null) { return false; }
			 */
			return true;
		}
	}

	class NodeNodeIdTypeFieldValidator<T> extends NodeIdTypeFieldValidator<String> {
		public NodeNodeIdTypeFieldValidator() {
			super();
		}

		@Override
		public String getErrorMessage() {
			return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeId.invalid.error");
		}

		@Override
		public String getWarningMessage() {
			return null;
		}

		@Override
		public boolean isValid(String value) {
			// check if the input string is empty or null
			if (selectedType == null) {
				return false;
			}
			NodeId nodeId = null;
			if (!isKeyInput) {
				nodeId = (NodeId) txtNodeId.getControl().getData(selectedType.name());
				if (nodeId == null) {
					return false;
				}
				// TODO: CHECK IF THE NODEID IS OPC DEFINITION VALID (UNIQUE IN
				// ADDRESSSPACE)
				NamespaceTable namespaces = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
				// AddressSpace addressSpace = ServerInstance.getInstance()
				// .getServerInstance().getAddressSpace();
				int index = namespaces.getIndex(comboNodeId.getText());
				if (index == -1) {
					return false;
				}
			} else {
				switch (selectedType) {
				case Numeric:
					Integer v = null;
					try {
						v = Integer.parseInt(value);
						nodeId = new NodeId(comboNodeId.getSelectionIndex(), v);
						break;
					} catch (NumberFormatException e) {
						return false;
					}
				case String:
					nodeId = new NodeId(comboNodeId.getSelectionIndex(), value);
					break;
				}
			}
			// // same nodeId than origin is allowed
			if (node.getNodeId().equals(nodeId)) {
				txtNodeId.getControl().setData(nodeId.getIdType().name(), nodeId);
				return true;
			}
			if (nodeId == null) {
				return false;
			}
			txtNodeId.getControl().setData(nodeId.getIdType().name(), nodeId);
			Node result = ServerInstance.getNode(nodeId);
			if (result != null) {
				return false;
			}
			return true;
		}

		@Override
		public boolean warningExist(String nodeId) {
			return false;
		}
	}

	@Override
	public IQuickFixProvider<String> getQuickFixProvider() {
		return new NodeIdFieldQuickFixProvider<>();
	}
}
