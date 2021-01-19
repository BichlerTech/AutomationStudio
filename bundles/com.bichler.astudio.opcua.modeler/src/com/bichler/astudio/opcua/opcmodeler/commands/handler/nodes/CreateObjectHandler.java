package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.dialogs.CreateObjectDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.CreateObjectModellingDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAVariableTypeNode;

public class CreateObjectHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		// get the selection (parent node)
		final TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event)
				.getSelectionService().getSelection(ModelBrowserView.ID);
		final Node selectedNode = ((BrowserModelNode) selection.getFirstElement()).getNode();

		int firstNamespaceIndex = CreateFactory.checkForNewNamespaceTable();
		if (firstNamespaceIndex == WizardDialog.CANCEL) {
			return null;
		}
		/** Get the type of a Node */
		// NodeId type = AddressSpaceUtil.expandedNodeIdToNodeId(selectedNode
		// .findTarget(Identifiers.HasTypeDefinition, false));
		/*
		 * if ((NodeClass.Object.equals(selectedNode.getNodeClass()) ||
		 * NodeClass.ObjectType .equals(selectedNode.getNodeClass())) &&
		 * (!Identifiers.FolderType.equals(type) || Identifiers.ObjectsFolder
		 * .equals(selectedNode.getNodeId()))) {
		 */
		CreateObjectDialog dialog = new CreateObjectDialog(shell, selectedNode);
		// CreateObjectModellingDialog dialog = new
		// CreateObjectModellingDialog(shell);
		int open = dialog.open();
		if (open == Dialog.OK) {
			// get the result from the dialog
			DialogResult result = dialog.getResult();
			CreateObjectModellingDialog modellingDialog = new CreateObjectModellingDialog(shell);
			modellingDialog.setParentNodeId(result.getType());
			/**
			 * Changed by Thomas Zöchbauer
			 * 
			 * Open Mandatory/Optional Dialog only when required (references exist)
			 * 
			 */
			boolean hasRules = modellingDialog.hasTreeViewerChildren(result.getType(),
					NodeClass.getMask(NodeClass.Variable, NodeClass.Object, NodeClass.Method));
			LinkedList<QualifiedName> path = new LinkedList<>();
			boolean hasRuleItself = modellingDialog.hasRuleItself(selectedNode, path);
			modellingDialog.setRuleItself(hasRuleItself);
			modellingDialog.setHasRules(hasRules);
			if ((((hasRules || hasRuleItself) && modellingDialog.open() == Dialog.OK) || !hasRules)
					&& modellingDialog.getReturnCode() == Dialog.OK) {
				Map<Node, Boolean> additionalNodes = modellingDialog.getModelRuleChildren();
				NamingRuleType selfRule = modellingDialog.getModelRuleSelf();
				result.setAdditionalNodesMap(additionalNodes);
				result.setHasRuleItself(hasRuleItself);
				result.setNamingRuleTyp(selfRule);
				final ModelChangeInfo info = new ModelChangeInfo(true);
				info.setNodeId(result.getNodeResult().getNodeId());
				info.setType(result.getType());
				info.setBrowseName(result.getNodeResult().getBrowseName());
				info.setNodeClass(result.getNodeResult().getNodeClass());
				info.setNodeAttributes(result.getNodeAttributes());
				info.setAdditionalNodesMap(additionalNodes);
				info.setHasRuleItself(hasRuleItself);
				info.setNamingRuleTyp(selfRule);
				info.setParentNodeId(result.getParentNodeId());
				info.setReferenceTypeId(result.getReferenceTypeId());
				info.setAdditionalRefernces(result.getAdditionalReferences());
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
				try {
					progressDialog.run(true, false, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException {
							monitor.beginTask(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
									"opc.message.extendobject") + "...", IProgressMonitor.UNKNOWN);
							try {
								// creates the AddNodesItem and insert
								// it to the address space
								create((BrowserModelNode) selection.getFirstElement(), info, selectedNode);
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										// refresh
										DesignerUtils.refreshBrowserNode((BrowserModelNode) selection.getFirstElement(),
												true);
										// information model
										// change
										MCPreferenceStoreUtil.setHasInformationModelChanged(shell,
												info.getNodeId().getNamespaceIndex());
										// refresh View
										DesignerUtils.askModelChange((BrowserModelNode) selection.getFirstElement(),
												info);
									}
								});
							} finally {
								monitor.done();
							}
						}
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		/*
		 * } else { Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
		 * "Parent is not an Object or an ObjectType!", null);
		 * ErrorDialog.openError(shell, "Error",
		 * "Not able to create a new Object Node here!", status); }
		 */
		return null;
	}

	private void create(BrowserModelNode treeSelection, ModelChangeInfo result, Node selection) {
		ExpandedNodeId parentNodeId = result.getParentNodeId();
		NodeId referenceTypeId = result.getReferenceTypeId();
		NodeClass parentNodeClass = selection.getNodeClass();
		if (selection instanceof UAObjectTypeNode || selection instanceof UAVariableTypeNode)
			CreateFactory.create(NodeClass.Object, parentNodeId, parentNodeClass, referenceTypeId, result, false, true);
		else
			CreateFactory.create(NodeClass.Object, parentNodeId, parentNodeClass, referenceTypeId, result, false,
					false);
		// List<AddNodesItem> nodesToAdd = new ArrayList<AddNodesItem>();
		// List<AddReferencesItem> referencesToAdd = new
		// ArrayList<AddReferencesItem>();
		//
		// createAddNodesItemStatement(result, parentNodeId, referenceTypeId,
		// nodesToAdd, referencesToAdd, parentNodeClass);
		//
		// if (!nodesToAdd.isEmpty()) {
		// try {
		// AddNodesItem[] nodesToAddArray = nodesToAdd
		// .toArray(new AddNodesItem[nodesToAdd.size()]);
		//
		// ServerInstance.getInstance().getServerInstance()
		// .getProfileManager().createNodes(nodesToAddArray);
		//
		// } catch (ServiceResultException e) {
		// e.printStackTrace();
		// return;
		// }
		// }
		// if (!referencesToAdd.isEmpty()) {
		// try {
		// AddReferencesItem[] referencesToAddArray = referencesToAdd
		// .toArray(new AddReferencesItem[referencesToAdd.size()]);
		//
		// ServerInstance.getInstance().getServerInstance()
		// .getProfileManager()
		// .addReferences(referencesToAddArray);
		// } catch (ServiceResultException e) {
		// e.printStackTrace();
		// return;
		// }
		// }
	}
	/**
	 * Create the object node from its given type
	 * 
	 * @param result
	 * @param additionalNodes
	 * @param parentNodeId
	 * @param referenceTypeId
	 * @param referencesToAdd
	 * @param parentNodeClass
	 * @param namingRule
	 * @param nodesToAdd2
	 * @return
	 */
	// private void createAddNodesItemStatement(DialogResult result,
	// ExpandedNodeId parentNodeId, NodeId referenceTypeId,
	// List<AddNodesItem> nodesToAdd,
	// List<AddReferencesItem> referencesToAdd, NodeClass parentNodeClass) {
	//
	// ExtensionObject attributes = null;
	// try {
	// attributes = ExtensionObject.binaryEncode(result
	// .getNodeAttributes());
	// } catch (EncodingException e) {
	// e.printStackTrace();
	// }
	//
	// MasterNodeManager nodeManager = ServerInstance.getInstance()
	// .getServerInstance().getMasterNodeManager();
	// TypeTable typeTree = ServerInstance.getInstance().getServerInstance()
	// .getTypeTree();
	//
	// AddressSpace addrSpace = nodeManager.getAddressSpaceManager()
	// .getAddressSpace();
	// NodeId id = addrSpace.getNodeFactory().getNextNodeId(
	// result.getNodeResult().getNodeId().getNamespaceIndex(),
	// result.getNodeResult().getNodeId().getValue(), new
	// ArrayList<NodeId>());
	//
	// ExpandedNodeId newNodeId = ServerInstance.getInstance()
	// .getServerInstance().getNamespaceUris().toExpandedNodeId(id);
	//
	// AddNodesItem nodeToAdd = new AddNodesItem();
	// nodeToAdd.setBrowseName(result.getNodeResult().getBrowseName());
	// nodeToAdd.setNodeAttributes(attributes);
	// nodeToAdd.setNodeClass(result.getNodeResult().getNodeClass());
	// nodeToAdd.setParentNodeId(parentNodeId);
	// nodeToAdd.setReferenceTypeId(referenceTypeId);
	// nodeToAdd.setRequestedNewNodeId(newNodeId);
	// nodeToAdd.setTypeDefinition(ServerInstance.getInstance()
	// .getServerInstance().getNamespaceUris()
	// .toExpandedNodeId(result.getType()));
	//
	// nodesToAdd.add(nodeToAdd);
	//
	// if (result.getNamingRule() != null) {
	// NodeId ruleId = null;
	//
	// switch (result.getNamingRule()) {
	// case Constraint:
	// ruleId = Identifiers.ModellingRule_ExposesItsArray;
	// break;
	// case Mandatory:
	// ruleId = Identifiers.ModellingRule_Mandatory;
	// break;
	// case Optional:
	// ruleId = Identifiers.ModellingRule_Optional;
	// break;
	// }
	//
	// AddReferencesItem modelingRule = new AddReferencesItem();
	// modelingRule.setIsForward(true);
	// modelingRule.setReferenceTypeId(Identifiers.HasModellingRule);
	// modelingRule.setSourceNodeId(NodeIdUtil.createNodeId(
	// newNodeId.getNamespaceIndex(), newNodeId.getValue()));
	// modelingRule.setTargetNodeClass(NodeClass.Object);
	// modelingRule.setTargetNodeId(new ExpandedNodeId(ruleId));
	// referencesToAdd.add(modelingRule);
	// }
	//
	// if (result.hasRuleItself()) {
	// // forward ref (child -> parent)
	// AddReferencesItem modelingItemForward = new AddReferencesItem();
	// modelingItemForward.setIsForward(true);
	// modelingItemForward.setReferenceTypeId(Identifiers.HasModelParent);
	// try {
	// modelingItemForward.setSourceNodeId(ServerInstance
	// .getInstance().getServerInstance().getNamespaceUris()
	// .toNodeId(newNodeId));
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	// modelingItemForward.setTargetNodeClass(parentNodeClass);
	// modelingItemForward.setTargetNodeId(result.getParentNodeId());
	//
	// // inverse ref (parent -> new child)
	//
	// AddReferencesItem modelingItemInverse = new AddReferencesItem();
	// modelingItemInverse.setIsForward(false);
	// modelingItemInverse.setReferenceTypeId(Identifiers.HasModelParent);
	// try {
	// modelingItemInverse.setSourceNodeId(ServerInstance
	// .getInstance().getServerInstance().getNamespaceUris()
	// .toNodeId(parentNodeId));
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	// modelingItemInverse.setTargetNodeClass(result.getNodeResult()
	// .getNodeClass());
	// modelingItemInverse.setTargetNodeId(newNodeId);
	//
	// referencesToAdd.add(modelingItemInverse);
	// referencesToAdd.add(modelingItemForward);
	// }
	//
	// for (ReferenceNode additionalReferenceNode : result
	// .getAdditionalReferences()) {
	// AddReferencesItem addReference = new AddReferencesItem();
	// addReference.setIsForward(!additionalReferenceNode.getIsInverse());
	// addReference.setReferenceTypeId(additionalReferenceNode
	// .getReferenceTypeId());
	// addReference.setTargetNodeId(additionalReferenceNode.getTargetId());
	//
	// NodeClass targetNodeClass = nodeManager.getAddressSpaceManager()
	// .getNode(additionalReferenceNode.getTargetId())
	// .getNodeClass();
	//
	// addReference.setSourceNodeId(result.getNodeResult().getNodeId());
	//
	// addReference.setTargetNodeClass(targetNodeClass);
	// // addReference.setTargetServerUri(TargetServerUri);
	// referencesToAdd.add(addReference);
	// }
	//
	// createAddNodes(result, nodesToAdd, referencesToAdd, addrSpace,
	// nodeManager, typeTree, result.getType(), newNodeId, null);
	// }
	//
	// private void createAddNodes(DialogResult result,
	// List<AddNodesItem> addNodesItemList,
	// List<AddReferencesItem> referencesToAdd, AddressSpace addrSpace,
	// MasterNodeManager nodeManager, TypeTable typeTree, NodeId typeId,
	// ExpandedNodeId targetNodeId, ExpandedNodeId parentNodeId) {
	//
	// BrowseResult[] hierachicalbrowseResult = null;
	// try {
	// hierachicalbrowseResult = ServerInstance
	// .getInstance()
	// .getServerInstance()
	// .getProfileManager()
	// .browse(new BrowseDescription[] { new BrowseDescription(
	// typeId, BrowseDirection.Forward,
	// Identifiers.HierarchicalReferences, true,
	// NodeClass.getMask(NodeClass.Object,
	// NodeClass.Variable, NodeClass.Method),
	// BrowseResultMask.getMask(BrowseResultMask.ALL)) },
	// new UnsignedInteger(10000), null);
	// } catch (ServiceResultException e1) {
	// e1.printStackTrace();
	// }
	//
	// BrowseResult[] nonhierachicalbrowseResult = null;
	// try {
	// nonhierachicalbrowseResult = ServerInstance
	// .getInstance()
	// .getServerInstance()
	// .getProfileManager()
	// .browse(new BrowseDescription[] { new BrowseDescription(
	// typeId, BrowseDirection.Both,
	// Identifiers.NonHierarchicalReferences, true,
	// NodeClass.getMask(NodeClass.ALL),
	// BrowseResultMask.getMask(BrowseResultMask.ALL)) },
	// new UnsignedInteger(10000), null);
	// } catch (ServiceResultException e1) {
	// e1.printStackTrace();
	// }
	// // only when there are browse results!
	// if (nonhierachicalbrowseResult[0].getReferences() != null) {
	// for (ReferenceDescription refDescription :
	// nonhierachicalbrowseResult[0]
	// .getReferences()) {
	//
	// NodeId sourceId = null;
	// try {
	// sourceId = ServerInstance.getInstance().getServerInstance()
	// .getNamespaceUris().toNodeId(targetNodeId);
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	//
	// AddReferencesItem addReferenceItem = new AddReferencesItem();
	//
	// addReferenceItem.setIsForward(refDescription.getIsForward());
	// addReferenceItem.setReferenceTypeId(refDescription
	// .getReferenceTypeId());
	// addReferenceItem.setSourceNodeId(sourceId);
	// addReferenceItem.setTargetNodeClass(refDescription
	// .getNodeClass());
	// addReferenceItem.setTargetServerUri(refDescription.getNodeId()
	// .getNamespaceUri());
	// addReferenceItem.setTargetNodeId(refDescription.getNodeId());
	//
	// if (hierachicalbrowseResult[0] == null
	// || hierachicalbrowseResult[0].getReferences() == null
	// || hierachicalbrowseResult[0].getReferences().length <= 0) {
	//
	// // change targetId
	//
	// boolean isHasModelParent = ServerInstance
	// .getInstance()
	// .getServerInstance()
	// .getTypeTree()
	// .isTypeOf(refDescription.getReferenceTypeId(),
	// Identifiers.HasModelParent);
	//
	// if (isHasModelParent && parentNodeId != null) {
	// addReferenceItem.setTargetNodeId(parentNodeId);
	// }
	//
	// } else {
	// boolean isHasModelParent = ServerInstance
	// .getInstance()
	// .getServerInstance()
	// .getTypeTree()
	// .isTypeOf(refDescription.getReferenceTypeId(),
	// Identifiers.HasModelParent);
	//
	// if (isHasModelParent && parentNodeId != null
	// && refDescription.getIsForward()) {
	// addReferenceItem.setTargetNodeId(parentNodeId);
	// }
	// }
	//
	// boolean exists = existsIdInNodeList(
	// result.getAdditionalNodes(), refDescription.getNodeId());
	// if (exists) {
	// referencesToAdd.add(addReferenceItem);
	// }
	// }
	// }
	//
	// for (ReferenceDescription refDescription : hierachicalbrowseResult[0]
	// .getReferences()) {
	//
	// Node n = nodeManager.getAddressSpaceManager().getNode(
	// refDescription.getNodeId());
	// NodeAttributes attributes = AddressSpaceUtil.fetchNodeAttribute(n);
	//
	// ExtensionObject encodedAttributes = null;
	// try {
	// encodedAttributes = ExtensionObject.binaryEncode(attributes);
	// } catch (EncodingException e) {
	// e.printStackTrace();
	// }
	//
	// ExpandedNodeId newNodeId = null;
	//
	// if (targetNodeId.getValue() instanceof UnsignedInteger) {
	// newNodeId = new ExpandedNodeId(addrSpace.getNodeFactory()
	// .getNextNodeId(targetNodeId.getNamespaceIndex(),
	// targetNodeId.getValue(), new ArrayList<NodeId>()));
	// } else {
	// newNodeId = new ExpandedNodeId(addrSpace.getNodeFactory()
	// .getNextNodeId(targetNodeId.getNamespaceIndex(), null, new
	// ArrayList<NodeId>()));
	// }
	//
	// boolean exists = existsIdInNodeList(result.getAdditionalNodes(),
	// refDescription.getNodeId());
	//
	// // modelrule is good
	// if (exists) {
	// referencesToAdd(refDescription, referencesToAdd, newNodeId);
	//
	// AddNodesItem nodesToAdd = new AddNodesItem();
	// nodesToAdd.setBrowseName(refDescription.getBrowseName());
	// nodesToAdd.setNodeAttributes(encodedAttributes);
	// nodesToAdd.setNodeClass(refDescription.getNodeClass());
	// nodesToAdd.setParentNodeId(targetNodeId);
	// nodesToAdd.setReferenceTypeId(refDescription
	// .getReferenceTypeId());
	// nodesToAdd.setRequestedNewNodeId(newNodeId);
	// nodesToAdd
	// .setTypeDefinition(refDescription.getTypeDefinition());
	//
	// addNodesItemList.add(nodesToAdd);
	//
	// if (n.getNodeClass().equals(NodeClass.Object)
	// || n.getNodeClass().equals(NodeClass.Variable)
	// || n.getNodeClass().equals(NodeClass.Method)) {
	//
	// createAddNodes(result, addNodesItemList, referencesToAdd,
	// addrSpace, nodeManager, typeTree, n.getNodeId(),
	// newNodeId, targetNodeId);
	// }
	// }
	// }
	//
	// NodeId supertype = ServerInstance.getInstance().getServerInstance()
	// .getTypeTree().findSuperType(typeId);
	// // when the current node is an object node and no other child objects
	// // node are there, then go a step back
	// // when the end of the object type is reached
	// if (Identifiers.BaseObjectType.equals(supertype)) {
	// return;
	// }
	// // when a supertype is available, create its children
	// else if (!NodeId.isNull(supertype)) {
	// createAddNodes(result, addNodesItemList, referencesToAdd,
	// addrSpace, nodeManager, typeTree, supertype, targetNodeId,
	// null);
	// }
	// }
	//
	// private boolean existsIdInNodeList(Map<Node, Boolean>
	// additionalNodes,
	// ExpandedNodeId nodeId) {
	//
	// // no modeling rule == MANDATORY
	// if (additionalNodes == null) {
	// return true;
	// }
	//
	// for (Node node : additionalNodes.keySet()) {
	// if (nodeId.equals(node.getNodeId())) {
	// return additionalNodes.get(node);
	// }
	// }
	//
	// return true;
	// }
	//
	// /**
	// * References to add
	// *
	// * @param additionalNodes
	// *
	// * @param parentNodeId
	// *
	// * @param RefDescription
	// * ReferenceDescription of the origin Node.
	// * @param ReferencesToAdd
	// * References to Add on the new Node
	// * @param NewNodeId
	// * NewNodeId of the new Node
	// */
	// private void referencesToAdd(ReferenceDescription refDescription,
	// List<AddReferencesItem> referencesToAdd, ExpandedNodeId newNodeId) {
	//
	// for (AddReferencesItem item : referencesToAdd) {
	// if (item.getTargetNodeId().equals(refDescription.getNodeId())) {
	// item.setTargetNodeId(newNodeId);
	// break;
	// }
	// }
	// }
}
