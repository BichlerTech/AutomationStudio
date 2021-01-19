package com.bichler.astudio.opcua.opcmodeler.wizards.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.wizards.AbstractCreateWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DataTypeEnumPage;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DataTypeEnumTypePage;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DetailDataTypePage;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DataTypeEnumTypePage.EnumType;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.types.TypeTable;

public class CreateDataTypeWizard extends AbstractCreateWizard {
	private Node parent;
	private DetailDataTypePage detailPage;
	private boolean isEnum;
	private DataTypeEnumTypePage enumTypePage;
	private DataTypeEnumPage enumValuePage;
	// dialog result
	private DialogResult result;
	private EnumType enumType;
	private Object[] enumValues;

	public CreateDataTypeWizard(Node selectedParent) {
		super(selectedParent);
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.extenddatatype"));
		this.parent = selectedParent;
		NodeId nodeId = this.parent.getNodeId();
		// check is enum
		TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
		this.isEnum = typeTable.isEnumeration(nodeId);
	}

	@Override
	public void addPages() {
		super.addPages();
		this.detailPage = new DetailDataTypePage();
		addPage(this.detailPage);
		if (this.isEnum) {
			this.enumTypePage = new DataTypeEnumTypePage();
			// TODO: UNCOMMENT
//			 addPage(this.enumTypePage);
			this.enumValuePage = new DataTypeEnumPage();
			addPage(this.enumValuePage);
			this.enumValuePage.setEnumTypePage(this.enumTypePage);
			this.enumTypePage.setEnumValuePage(this.enumValuePage);
		}
	}

	@Override
	public boolean performFinish() {
		QualifiedName browsename = getGeneralPage().getBrowseName();
		LocalizedText description = getGeneralPage().getDescriptionText();
		LocalizedText displayname = getGeneralPage().getDisplayname();
		NodeId nodeid = getGeneralPage().getNodeId();
		UnsignedInteger userwritemask = getGeneralPage().getUserWriteMask();
		UnsignedInteger writemask = getGeneralPage().getWriteMask();
		boolean isAbstract = this.detailPage.isAbstract();
		ReferenceNode data = getGeneralPage().getReferenceNode();
		Node node = new Node(nodeid, NodeClass.DataType, browsename, displayname, description, writemask, userwritemask,
				null);
		NodeAttributes attributes = new DataTypeAttributes(null, displayname, description, writemask, userwritemask,
				isAbstract);
		ExpandedNodeId parentNodeId = data.getTargetId();
		NodeId referenceTypeId = data.getReferenceTypeId();
		// TODO: Additional OPC UA nodes
		Map<Node, Boolean> additionalNodes = new HashMap<>();
		List<ReferenceNode> additionalReferences = new ArrayList<ReferenceNode>();
		if (this.isEnum) {
			this.enumType = enumTypePage.getEnumType();
			this.enumValues = enumValuePage.getEnumerationValues();
			switch (enumType) {
			case EnumValueType:
				break;
			case StringValue:
				Variant value = new Variant(this.enumValues);
				NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
				NodeId newId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeFactory()
						.getNextNodeId(nodeid.getNamespaceIndex(), nodeid.getValue(), nodeid.getIdType(), ccNodeId);
				UAVariableNode svNode = new UAVariableNode(newId, NodeClass.Variable, new QualifiedName("EnumStrings"),
						new LocalizedText("EnumStrings"), new LocalizedText(""), UnsignedInteger.ZERO,
						UnsignedInteger.ZERO, new ReferenceNode[0], value, Identifiers.LocalizedText,
						ValueRanks.OneDimension.getValue(), new UnsignedInteger[] { UnsignedInteger.ZERO },
						AccessLevel.getMask(AccessLevel.READONLY), AccessLevel.getMask(AccessLevel.READONLY), 1000.0,
						false);
				additionalNodes.put(svNode, true);
				break;
			default:
				break;
			}
		}
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

	public boolean isEnum() {
		return this.isEnum;
	}

	public EnumType getEnumType() {
		return this.enumType;
	}

	public Object[] getEnumValues() {
		return this.enumValues;
	}

	public DialogResult getDialogResult() {
		return this.result;
	}

	@Override
	protected String getTitle() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorDataTypePart.frm_mainForm.text");
	}

	@Override
	protected String getDescription() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.sctn_general.text");
	}
}
