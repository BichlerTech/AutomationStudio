package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.edit;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.types.TypeTable;
import opc.sdk.core.node.Node;
import opc.sdk.server.service.util.AddressSpaceUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.CreateFactory;
import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorInput;
import com.bichler.astudio.opcua.opcmodeler.preferences.ShowDialogPreferencePage;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class RemoveHandler extends AbstractHandler {
	public static final String ID = "commands.designer.remove";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeSelection treeSelectionElement = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event)
				.getSelectionService().getSelection(ModelBrowserView.ID);
		// BrowserModelNode browserNode = (BrowserModelNode)
		// treeSelectionElement
		// .getFirstElement();
		Node selection = ((BrowserModelNode) treeSelectionElement.getFirstElement()).getNode();
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		// all nodeids with 0 namespaceindex lower than 11250 are
		// serverdefinednodes others can be removed!
		if (!AddressSpaceUtil.isServerBaseNode(selection)
				|| !(selection.getNodeId().getValue() instanceof UnsignedInteger)
				|| (selection.getNodeId().getNamespaceIndex() == 0
						&& ((UnsignedInteger) selection.getNodeId().getValue()).intValue() >= 11250)
				|| preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_OPCUA_EDIT_INTERNAL)) {
			// boolean isDeleteAllowed = checkDeleteNodeAllowed(selection);
			// no prevention to remove modelparent children
			boolean isDeleteAllowed = true;
			if (isDeleteAllowed) {
				// check for delete dialog
				boolean confirmDelete = askRemoveConfirm(HandlerUtil.getActiveShell(event));
				if (confirmDelete) {
					List<DeleteNodesItem> nodesToDelete = new ArrayList<DeleteNodesItem>();
					// AddressSpace addrSpace = ServerInstance.getInstance()
					// .getServerInstance().getAddressSpace();
					// MasterNodeManager nodeManager = ServerInstance
					// .getInstance().getServerInstance()
					// .getMasterNodeManager();
					TypeTable typeTree = ServerInstance.getInstance().getServerInstance().getTypeTable();
					List<NodeId> preventLoop = new ArrayList<>();
					findAllNodesToDelete(preventLoop, nodesToDelete, typeTree, selection.getNodeId());
					/** ModelChange */
					DesignerUtils.askModelChangeRemove(((BrowserModelNode) treeSelectionElement.getFirstElement()),
							nodesToDelete.toArray(new DeleteNodesItem[0]));
					try {
						CreateFactory.remove(nodesToDelete.toArray(new DeleteNodesItem[0]));
					} catch (ServiceResultException e1) {
						e1.printStackTrace();
					}
					// close all open editors, which nodes have been deleted
					ModelBrowserView part = DesignerUtils.refreshBrowserAll();
					// close editor if one is open
					for (IWorkbenchPage page : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()) {
						for (DeleteNodesItem item : nodesToDelete) {
							try {
								for (IEditorReference editor : page.getEditorReferences()) {
									// editor for a node
									if (DesignerConstants.NODEEDITORIDS.contains(editor.getId())) {
										NodeId inputNode = ((NodeEditorInput) editor.getEditorInput()).getNode()
												.getNode().getNodeId();
										if (inputNode.equals(item.getNodeId())) {
											HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage()
													.closeEditor((IEditorPart) editor.getPart(true), true);
										}
									}
								}
							} catch (PartInitException e) {
								e.printStackTrace();
							}
						}
					}
					part.setDirty(true);
					// something has changed in the information model, mark as
					// changed
					MCPreferenceStoreUtil.setHasInformationModelChanged(HandlerUtil.getActiveShell(event), selection);
				}
			} else {
				MessageDialog.openWarning(HandlerUtil.getActiveShell(event),
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.error.modelingrule"),
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"opc.error.modelingrule.description"));
			}
		}
		// cannot edit defined opc ua nodes (namespace index = 0)
		else {
			MessageDialog.openError(HandlerUtil.getActiveShell(event),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.node.defined.title"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"message.error.node.defined.message"));
		}
		return null;
	}

	/*
	 * @Override public void setEnabled(Object evaluationContext) { Object selection
	 * = ((EvaluationContext) evaluationContext) .getVariable("selection"); if
	 * (selection instanceof TreeSelection) { Node var = (Node) ((TreeSelection)
	 * ((EvaluationContext) evaluationContext)
	 * .getVariable("selection")).getFirstElement(); if (var != null) { // enable
	 * 
	 * boolean isDeleteAllowed = checkDeleteNodeAllowed(var);
	 * 
	 * if ((!AddressSpaceUtil.isServerBaseNode(var) || !(var.getNodeId().getValue()
	 * instanceof UnsignedInteger) || (var .getNodeId().getNamespaceIndex() == 0 &&
	 * ((UnsignedInteger) var .getNodeId().getValue()).intValue() >= 11250)) &&
	 * isDeleteAllowed) { setBaseEnabled(true); } else { setBaseEnabled(false); } }
	 * } }
	 */
	private boolean askRemoveConfirm(Shell shell) {
		boolean confirmed = true;
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		boolean isShowDialog = preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_SHOWDIALOG_DELETE);
		if (isShowDialog) {
			confirmed = MessageDialog.openConfirm(shell,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.delete"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.delete.description"));
		}
		return confirmed;
	}

	/**
	 * private boolean checkDeleteNodeAllowed(Node selection) {
	 * 
	 * MasterNodeManager manager = ServerInstance.getInstance()
	 * .getServerInstance().getMasterNodeManager();
	 * 
	 * BrowseDescription[] nodesToBrowse = { new BrowseDescription(
	 * selection.getNodeId(), BrowseDirection.Inverse,
	 * Identifiers.HierarchicalReferences, true,
	 * NodeClass.getMask(Arrays.asList(NodeClass.values())),
	 * BrowseResultMask.getMask(Arrays.asList(BrowseResultMask .values()))) };
	 * 
	 * try { BrowseResult[] browseDescription = ServerInstance.getInstance()
	 * .getServerInstance().getProfileManager() .browse(nodesToBrowse, new
	 * UnsignedInteger(10000), null);
	 * 
	 * ExpandedNodeId[] modelParents = selection.findTargets(
	 * Identifiers.HasModelParent, false);
	 * 
	 * if (modelParents == null) { return true; } else if (modelParents != null &&
	 * modelParents.length <= 0) { return true; }
	 * 
	 * for (ReferenceDescription refDesc : browseDescription[0] .getReferences()) {
	 * for (ExpandedNodeId id : modelParents) { if (refDesc.getNodeId().equals(id))
	 * { // get parent Node modelParentNode = manager.getAddressSpaceManager()
	 * .getNode(id);
	 * 
	 * // if parent is an type, it is allowed to remove its // children if
	 * (modelParentNode.getNodeClass().equals( NodeClass.ObjectType) ||
	 * modelParentNode.getNodeClass().equals( NodeClass.VariableType)) {
	 * 
	 * return true; }
	 * 
	 * return checkDeleteNodeAllowed(modelParentNode); } } }
	 * 
	 * } catch (ServiceResultException e) { e.printStackTrace(); } return false; }
	 */
	// private boolean checkDeleteNodeAllowed(Node selection) {
	//
	// // BrowseDescription[] nodesToBrowse = { new BrowseDescription(
	// // selection.getNodeId(), BrowseDirection.Inverse,
	// // Identifiers.HierarchicalReferences, true,
	// // NodeClass.getMask(Arrays.asList(NodeClass.values())),
	// // BrowseResultMask.getMask(Arrays.asList(BrowseResultMask
	// // .values()))) };
	//
	// try {
	//
	// BrowseResult[] browseDescription = ServerInstance.browse(
	// selection.getNodeId(), Identifiers.HierarchicalReferences,
	// NodeClass.ALL, BrowseResultMask.ALL,
	// BrowseDirection.Inverse, true);
	// // ServerInstance.getInstance()
	// // .getServerInstance().getProfileManager()
	// // .browse(nodesToBrowse, UnsignedInteger.ZERO, null);
	//
	// // ExpandedNodeId[] modelParents = selection.findTargets(
	// // Identifiers.HasModelParent, false);
	//
	// for (ReferenceDescription refDesc : browseDescription[0]
	// .getReferences()) {
	// // for (ExpandedNodeId id : modelParents) {
	// // if (refDesc.getNodeId().equals(id)) {
	// // get parent
	// Node modelParentNode = ServerInstance.getNode(refDesc
	// .getNodeId());
	//
	// // if parent is an type, it is allowed to remove its
	// // children
	// if (modelParentNode.getNodeClass().equals(NodeClass.ObjectType)
	// || modelParentNode.getNodeClass().equals(
	// NodeClass.VariableType)) {
	// continue;
	// }
	// return false;
	// }
	// // }
	// // }
	//
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	// return true;
	// }
	private void findAllNodesToDelete(List<NodeId> preventLoop, List<DeleteNodesItem> nodesToDelete,
			/* AddressSpace addrSpace, MasterNodeManager nodeManager, */
			TypeTable typeTree, NodeId nodeIdToDelete) {
		// deleteNodeitem
		DeleteNodesItem nodeToDelete = new DeleteNodesItem();
		nodeToDelete.setDeleteTargetReferences(true);
		nodeToDelete.setNodeId(nodeIdToDelete);
		nodesToDelete.add(nodeToDelete);
		preventLoop.add(nodeIdToDelete);
		// browse children
		BrowseDescription bd = new BrowseDescription();
		bd.setBrowseDirection(BrowseDirection.Forward);
		bd.setIncludeSubtypes(true);
		bd.setNodeClassMask(NodeClass.ALL);
		bd.setNodeId(nodeIdToDelete);
		bd.setReferenceTypeId(Identifiers.HierarchicalReferences);
		bd.setResultMask(BrowseResultMask.ALL);
		// BrowseDescription[] nodesToBrowse = { bd };
		try {
			BrowseResult[] results = ServerInstance.browse(nodeIdToDelete, Identifiers.HierarchicalReferences,
					NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (results.length > 0) {
				for (ReferenceDescription refDesc : results[0].getReferences()) {
					NodeId refId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(refDesc.getNodeId());
					if (!preventLoop.contains(refId)) {
						findAllNodesToDelete(preventLoop, nodesToDelete, typeTree, refId);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}
}
