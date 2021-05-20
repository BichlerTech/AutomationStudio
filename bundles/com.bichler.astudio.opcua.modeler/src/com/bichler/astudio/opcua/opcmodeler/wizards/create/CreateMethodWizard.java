package com.bichler.astudio.opcua.opcmodeler.wizards.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.wizards.AbstractCreateWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DetailMethodPage;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.MethodArgumentPage;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.VariableNode;

public class CreateMethodWizard extends AbstractCreateWizard {
	private MethodArgumentPage createMethodArgumentPage;
	private DetailMethodPage detailPage;
	private DialogResult result;

	public CreateMethodWizard(Node selectedParent) {
		super(selectedParent);
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.extendmethod"));
	}

	@Override
	public void addPages() {
		super.addPages();
		this.detailPage = new DetailMethodPage();
		addPage(this.detailPage);
		this.createMethodArgumentPage = new MethodArgumentPage();
		addPage(this.createMethodArgumentPage);
	}

	@Override
	public boolean performFinish() {
		QualifiedName browsename = getGeneralPage().getBrowseName();
		LocalizedText description = getGeneralPage().getDescriptionText();
		LocalizedText displayname = getGeneralPage().getDisplayname();
		NodeId nodeid = getGeneralPage().getNodeId();
		UnsignedInteger userwritemask = getGeneralPage().getUserWriteMask();
		UnsignedInteger writemask = getGeneralPage().getWriteMask();
		boolean executeable = this.detailPage.getExecuteable();
		boolean userexecuteable = this.detailPage.getUserExecuteable();
		ReferenceNode data = getGeneralPage().getReferenceNode();
		Argument[] inputArgs = this.createMethodArgumentPage.getInputArguments();
		Argument[] outputArgs = this.createMethodArgumentPage.getOutputArguments();
		Node node = new Node(nodeid, NodeClass.Method, browsename, displayname, description, writemask, userwritemask,
				null);
		NodeAttributes attributes = new MethodAttributes(null, displayname, description, writemask, userwritemask,
				executeable, userexecuteable);
		ExpandedNodeId parentNodeId = data.getTargetId();
		NodeId referenceTypeId = data.getReferenceTypeId();
		// TODO: ADDITIONAL REFERENCES
		Map<Node, Boolean> additionalNodes = new HashMap<>();
		List<ReferenceNode> additionalReferences = new ArrayList<>();

		addNodes(inputArgs, outputArgs, additionalNodes, additionalReferences, nodeid);

		DialogResult dialogResult = new DialogResult();
		dialogResult.setNodeResult(node);
		dialogResult.setNodeAttributes(attributes);
		dialogResult.setParentNodeId(parentNodeId);
		dialogResult.setReferenceTypeId(referenceTypeId);
		dialogResult.setAdditionalNodesMap(additionalNodes);
		dialogResult
				.setAdditionalReferences(additionalReferences.toArray(new ReferenceNode[additionalReferences.size()]));
		dialogResult.setType(node.getNodeId());
		this.result = dialogResult;
		return true;
	}

	public DialogResult getDialogResult() {
		return this.result;
	}

	@Override
	protected String getTitle() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.frm_mainForm.text");
	}

	@Override
	protected String getDescription() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.sctn_general.text");
	}

	private void addNodes(Argument[] inputArgs, Argument[] outputArgs, Map<Node, Boolean> additionalNodes,
			List<ReferenceNode> additionalReferences, NodeId parentId) {
		
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		if (inputArgs.length > 0) {
			Node node = createArgumentNode("InputArguments", inputArgs, parentId);
			additionalNodes.put(node, true);
			ReferenceNode ref = new ReferenceNode();
			ref.setIsInverse(false);
			ref.setReferenceTypeId(Identifiers.HasProperty);
			ref.setTargetId(new ExpandedNodeId(node.getNodeId()));
			additionalReferences.add(ref);
		}
		if (outputArgs.length > 0) {
			Node node = createArgumentNode("OutputArguments", outputArgs, parentId);
			additionalNodes.put(node, true);
			ReferenceNode ref = new ReferenceNode();
			ref.setIsInverse(false);
			ref.setReferenceTypeId(Identifiers.HasProperty);
			ref.setTargetId(new ExpandedNodeId(node.getNodeId()));
			additionalReferences.add(ref);
		}
	}

	private Node createArgumentNode(String nodeName, Argument[] arguments, NodeId parentId) {
		List<ExtensionObject> extObj = new ArrayList<>();
		for (Argument arg : arguments) {
			try {
				extObj.add(ExtensionObject.binaryEncode(arg, EncoderContext.getDefaultInstance()));
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		
		Variant value = new Variant(extObj.toArray(new ExtensionObject[0]));
		NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
		NodeId newId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
				.getNextNodeId(parentId.getNamespaceIndex(), parentId.getValue(), parentId.getIdType(),
						ccNodeId);

		return new VariableNode(newId, NodeClass.Variable, new QualifiedName(nodeName),
				new LocalizedText(nodeName), new LocalizedText(""), UnsignedInteger.ZERO, UnsignedInteger.ZERO,
				new ReferenceNode[0], value, Identifiers.Argument, ValueRanks.OneDimension.getValue(),
				new UnsignedInteger[] { UnsignedInteger.ONE }, AccessLevel.getMask(AccessLevel.READONLY),
				AccessLevel.getMask(AccessLevel.READONLY), 0.0, false);
	}
}
