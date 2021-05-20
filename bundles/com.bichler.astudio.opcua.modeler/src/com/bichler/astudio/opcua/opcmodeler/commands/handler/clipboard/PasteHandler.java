package com.bichler.astudio.opcua.opcmodeler.commands.handler.clipboard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.core.ViewAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.CreateFactory;
import com.bichler.astudio.opcua.opcmodeler.dialogs.CreateObjectModellingDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.CreateVariableModellingDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.utils.OperationType;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.PasteStartNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.AbstractNodeFactory;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.node.ViewNode;
import opc.sdk.core.types.TypeTable;

public class PasteHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Integer nsindex = -1;
		if (selection == null) {
			return null;
		}
		if (selection.isEmpty()) {
			return null;
		}
		if (selection.getFirstElement() instanceof BrowserModelNode) {
			nsindex = ((BrowserModelNode) selection.getFirstElement()).getNode().getNodeId().getNamespaceIndex();
		}
		Clipboard cb = new Clipboard(Display.getDefault());
		TextTransfer transfer = TextTransfer.getInstance();
		final Object contents = cb.getContents(transfer);
		/**
		 * -------------------- Use information from preferences (copy/paste) to
		 * determine the namespaceindex to paste new nodes
		 */
		final PasteStartNode startNode = DesignerUtils.askNamespaceIndexIfNeeded(HandlerUtil.getActiveShell(event),
				OperationType.Paste, nsindex, ((BrowserModelNode) selection.getFirstElement()).getNode(),
				(String) contents);
		if (startNode == null) {
			return null;
		}
		final Shell shell = HandlerUtil.getActiveShell(event);
		ProgressMonitorDialog progressDilog = new ProgressMonitorDialog(shell);
		try {
			progressDilog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.monitor.paste")+ "...",
							IProgressMonitor.UNKNOWN);
					try {
						/** content is string */
						if (contents instanceof String) {
							IStructuredSelection sel = (IStructuredSelection) selection;
							BrowserModelNode parent = (BrowserModelNode) sel.getFirstElement();
							final Node parentNode = parent.getNode();
							/**
							 * get all parent nodes with its node class to copy
							 */
							Map<NodeId, NodeClass> nodeMap = new HashMap<>();
							parseClipboardInformation((String) contents, nodeMap);
							List<ModelChange> infos = new ArrayList<>();
							List<Boolean> allow = new ArrayList<>();
							List<Integer[]> results = new ArrayList<>();
							/** validation */
							for (Entry<NodeId, NodeClass> entry : nodeMap.entrySet()) {
								/**
								 * Target parent node
								 */
								NodeId parentNodeId = parentNode.getNodeId();
								NodeClass parentNodeClass = parentNode.getNodeClass();
								/** Nodeclass of node to copy */
								final NodeClass nodeClass = entry.getValue();
								/** source nodeId to copy */
								final NodeId nodeId = entry.getKey();
								boolean isAllowed = validatePasteAllowed(parentNodeId, parentNodeClass, nodeClass);
								final ModelChangeInfo info = new ModelChangeInfo(false);
								ModelChange change = new ModelChange(nodeId, nodeClass, info);
								infos.add(change);
								allow.add(isAllowed);
								if (!isAllowed) {
									MessageDialog.openError(HandlerUtil.getActiveShell(event),
											CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
													"window.error"),
											CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
													"opc.error.copypaste"));
									continue;
								}
								boolean useCopyIds = startNode.isCopyIds();
								final Integer[] changed = doPaste(shell, info, startNode, parentNode, nodeId, nodeClass,
										useCopyIds);
								results.add(changed);
								info.setAdditionalRefernces(new ArrayList<ReferenceNode>());
							}
							// update modelchange if needed
							for (int i = 0; i < infos.size(); i++) {
								final ModelChange info = infos.get(i);
								final boolean isAllowed = allow.get(i);
								final Integer[] result = results.get(i);
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										// mark as not allowed
										if (!isAllowed) {
											MessageDialog.openError(HandlerUtil.getActiveShell(event),
													CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
															"window.error"),
													CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
															"opc.error.copypaste"));
											return;
										}
										/**
										 * Only ask modelchange when inserting objects variables methods:
										 */
										switch (info.getNodeClass()) {
										case Object:
										case Variable:
										case Method:
											DesignerUtils.askModelChange((BrowserModelNode) selection.getFirstElement(),
													info.getInfo());
											break;
										}
										MCPreferenceStoreUtil.setHasInformationModelChanged(
												HandlerUtil.getActiveShell(event), result);
									}
								});
							}
							// refresh viewer
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									// refresh
									DesignerUtils.refreshBrowserNode((BrowserModelNode) selection.getFirstElement(),
											true);
								}
							});
						} else {
							throw new ServiceResultException(StatusCodes.Bad_NodeNotInView);
						}
					} catch (ServiceResultException e) {
						MessageDialog.openError(HandlerUtil.getActiveShell(event),
								CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "window.error"),
								CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
										"�pc.error.copypaste.remove"));
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
		return null;
	}

	private Integer[] doPaste(Shell shell, ModelChangeInfo info, PasteStartNode startNode, Node parentNode,
			NodeId nodeId2copy, NodeClass nodeClass2copy, boolean useCopyIds) {
		Map<ExpandedNodeId, ExpandedNodeId> typeMapping = new HashMap<>();
		Map<ExpandedNodeId, AddNodesItem> pasteMapping = new HashMap<>();
		/**
		 * Collect the information to paste.<br>
		 * Key is origin id Value is addnodesitem
		 */
		Map<NodeId, AddNodesItem> nodes2add = new HashMap<>();
		/** add reference item and its origin target id */
		List<AddReferencesItem> references2add = new ArrayList<>();
		/** map <originIDS,pasteIDS> */
		Map<NodeId, NodeId> idMapping = new HashMap<>();
//		List<NodeId> rekIds = new ArrayList<>();
		Boolean hasRuleItself = null;
		if (parentNode != null) {
			LinkedList<QualifiedName> path = new LinkedList<>();
			switch (nodeClass2copy) {
			case Object:
			case Method:
				CreateObjectModellingDialog dialog1 = new CreateObjectModellingDialog(shell);
				hasRuleItself = dialog1.hasRuleItself(parentNode, path);
				break;
			case Variable:
				CreateVariableModellingDialog dialog2 = new CreateVariableModellingDialog(shell);
				hasRuleItself = dialog2.hasRuleItself(parentNode, path);
				break;
			default:
				break;
			}
		}
		/** INFO SET HAS RULE ITSELF */
		info.setHasRuleItself(hasRuleItself);
		info.setNodeClass(nodeClass2copy);
		Map<NodeId, NodeId> nodes = new HashMap<>();
		rekPaste(info, startNode.getNodeId().getNamespaceIndex(), startNode, idMapping, typeMapping, pasteMapping,
				nodes2add, references2add, /* rekIds, */ parentNode.getNodeId(), nodeId2copy, nodes, useCopyIds, true);
		info.setAdditionalPasteNodes(pasteMapping);
		info.setAdditionalPasteReferences(references2add);
		/** finish to build addreferences structures */
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		List<AddReferencesItem> refernces2skip = new ArrayList<>();
		for (AddReferencesItem item : references2add) {
			try {
				NodeId originTarget = nsTable.toNodeId(item.getTargetNodeId());
				NodeId pasteTarget = idMapping.get(originTarget);
				/** recalculate new reference */
				if (pasteTarget != null) {
					AddNodesItem addNodeItem = nodes2add.get(pasteTarget);
					NodeClass targetClass = null;
					if (addNodeItem != null) {
						targetClass = addNodeItem.getNodeClass();
					}
					if (targetClass == null) {
						Node serverNode = ServerInstance.getNode(item.getTargetNodeId());
						// Node serverNode = ServerInstance.getInstance()
						// .getServerInstance().getMasterNodeManager()
						// .getAddressSpaceManager()
						// .getNode(item.getTargetNodeId());
						targetClass = serverNode.getNodeClass();
					}
					item.setTargetNodeClass(targetClass);
					item.setTargetNodeId(new ExpandedNodeId(pasteTarget));
					// item.setTargetNodeId(nsTable.toExpandedNodeId(pasteTarget));
				} else {
					NodeId refTypeId = item.getReferenceTypeId();
					boolean isSkippedHierachical = checkIsReferenceType(nsTable, refTypeId,
							Identifiers.HierarchicalReferences);
					// skip hierachical items
					if (isSkippedHierachical) {
						refernces2skip.add(item);
					}
				}
			} catch (ServiceResultException e1) {
				e1.printStackTrace();
			}
		}
		references2add.removeAll(refernces2skip);
		if (hasRuleItself != null && hasRuleItself) {
			addHasModelParentRefernceIfNeeded(references2add, info.getNodeId(), info.getNodeClass(),
					parentNode.getNodeId(), parentNode.getNodeClass());
		}
		/** add all nodes */
		try {
			ServerInstance.addNode(nodes2add.values().toArray(new AddNodesItem[0]), false);
			if (!references2add.isEmpty()) {
				ServerInstance.addReferences(references2add.toArray(new AddReferencesItem[0]));
			}
			CreateFactory.doTypeMapping(typeMapping);
			Set<Integer> changedIndizes = new HashSet<>();
			for (Entry<NodeId, NodeId> id : idMapping.entrySet()) {
				changedIndizes.add(id.getValue().getNamespaceIndex());
			}
			return changedIndizes.toArray(new Integer[0]);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return new Integer[0];
	}

	private boolean checkIsReferenceType(NamespaceTable nsTable, NodeId refTypeId, NodeId lookupId) {
		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		BrowseDescription nodeToBrowse = new BrowseDescription();
		nodeToBrowse.setBrowseDirection(BrowseDirection.Inverse);
		nodeToBrowse.setIncludeSubtypes(true);
		nodeToBrowse.setNodeClassMask(NodeClass.ReferenceType);
		nodeToBrowse.setNodeId(refTypeId);
		nodeToBrowse.setReferenceTypeId(lookupId);
		nodeToBrowse.setResultMask(BrowseResultMask.ALL);
		nodesToBrowse[0] = nodeToBrowse;
		try {
			BrowseResult[] result = ServerInstance.browse(refTypeId, lookupId,
					NodeClass.getSet(NodeClass.ReferenceType.getValue()), BrowseResultMask.ALL, BrowseDirection.Inverse,
					true);
			// BrowseResult[] result = ServerInstance.getInstance()
			// .getServerInstance().getProfileManager()
			// .browse(nodesToBrowse, UnsignedInteger.ZERO, null);
			if (result != null && result.length > 0 & result[0].getReferences() != null) {
				for (ReferenceDescription refDesc : result[0].getReferences()) {
					NodeId refId = nsTable.toNodeId(refDesc.getNodeId());
					if (Identifiers.HierarchicalReferences.equals(refId)) {
						return true;
					}
					return checkIsReferenceType(nsTable, refId, lookupId);
				}
			} else {
				return false;
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void addHasModelParentRefernceIfNeeded(List<AddReferencesItem> referencesToAdd, NodeId newId,
			NodeClass newClass, NodeId parentId, NodeClass parentClass) {

		NamespaceTable table = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		String nsUri = table.getUri(newId.getNamespaceIndex());

		ExpandedNodeId expNewId = new ExpandedNodeId(newId);

		ExpandedNodeId expParentId = new ExpandedNodeId(UnsignedInteger.ZERO, parentId);
	}

	private void rekPaste(ModelChangeInfo info, Integer index, PasteStartNode startNode, Map<NodeId, NodeId> idMapping,
			Map<ExpandedNodeId, ExpandedNodeId> typeMapping, Map<ExpandedNodeId, AddNodesItem> pasteMapping,
			Map<NodeId, AddNodesItem> nodes2add, List<AddReferencesItem> references2add, /* List<NodeId> rekNewIds, */
			NodeId parentId, NodeId nodeId2copy, Map<NodeId, NodeId> nodes, boolean isCopyIds, boolean startRek) {
		// do not contain already
		if (!nodes2add.containsKey(nodeId2copy)) {
			/** nodefactory for NEW NODEID */
			AbstractNodeFactory nFactory = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeFactory();
			Integer index2use = null;
			if (index == null) {
				index2use = nodeId2copy.getNamespaceIndex();
			} else {
				index2use = index;
			}
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			NodeId newId = NodeId.NULL;
			if (!startNode.isInit()) {
				newId = startNode.getNodeId();
			} else {
				if (isCopyIds) {
					newId = nFactory.getNextNodeId(index2use, nodeId2copy.getValue(), nodeId2copy.getIdType(),
							/* rekNewIds, */ NodeIdMode.CONTINUE);
				} else {
					switch(nodeId2copy.getIdType()){
					case String:
						newId = nFactory.getNextNodeId(index2use, nodeId2copy.getValue(), nodeId2copy.getIdType(),
								/*
								 * rekNewIds,
								 */ccNodeId);
						break;
					default:
						newId = nFactory.getNextNodeId(index2use, parentId.getValue(), nodeId2copy.getIdType(),
								/*
								 * rekNewIds,
								 */ccNodeId);
						break;
					}
					
					
					
				}
			}
			// rekNewIds.add(newId);
			/**
			 * Wrapp nodeids
			 */
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			ExpandedNodeId expNewId = new ExpandedNodeId(newId);
			// ExpandedNodeId expNewId = nsTable.toExpandedNodeId(newId);
			ExpandedNodeId expParentId = new ExpandedNodeId(parentId);
			// ExpandedNodeId expParentId = nsTable.toExpandedNodeId(parentId);
			/***************************/
			/** fetched node to copy */
			Node node2copy = ServerInstance.getNode(nodeId2copy);
			ExtensionObject nodeAttributes = null;
			NodeAttributes nattr = null;
			try {
				nattr = getNodeAttributes(node2copy);
				nodeAttributes = ExtensionObject.binaryEncode(nattr, EncoderContext.getDefaultInstance());
			} catch (EncodingException e) {
				e.printStackTrace();
			}
			ExpandedNodeId typeDef = null;
			NodeId referenceId = null;
			TypeTable tree = ServerInstance.getInstance().getServerInstance().getTypeTable();
			// hierachical
			ReferenceDescription[] references2match = new ReferenceDescription[0];
			try {
				BrowseResult[] result = ServerInstance.browse(nodeId2copy, Identifiers.HierarchicalReferences,
						NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Inverse, true);
				if (result != null && result.length > 0 && result[0].getReferences() != null) {
					references2match = result[0].getReferences();
				}
			} catch (ServiceResultException e1) {
				e1.printStackTrace();
			}
			for (ReferenceNode refNode : node2copy.getReferences()) {
				Boolean isInverse = refNode.getIsInverse();
				NodeId refId = refNode.getReferenceTypeId();
				ExpandedNodeId targetId = refNode.getTargetId();
				boolean isHierachical = tree.isTypeOf(refNode.getReferenceTypeId(), Identifiers.HierarchicalReferences);
				boolean isTypeDefinition = tree.isTypeOf(refNode.getReferenceTypeId(), Identifiers.HasTypeDefinition);
				if (isHierachical && isInverse && referenceId == null) {
					// es gibt keinen orignalen parent
					// <- als parent einf�gen
					ExpandedNodeId expPID = new ExpandedNodeId(parentId);
					// ExpandedNodeId expPID = nsTable.toExpandedNodeId(parentId);
					AddNodesItem originParent = pasteMapping.get(expPID);
					if (originParent == null) {
						referenceId = refId;
					} else {
						NodeId targetOriginParentNode = nodes.get(parentId);
						ExpandedNodeId expTargetOriginParentNode = nsTable.toExpandedNodeId(targetOriginParentNode);
						if (references2match.length > 0) {
							for (ReferenceDescription refDesc : references2match) {
								if (refDesc.getNodeId().equals(expTargetOriginParentNode)) {
									referenceId = refDesc.getReferenceTypeId();
									break;
								}
							}
						}
					}
					try {
						idMapping.put(nsTable.toNodeId(targetId), parentId);
					} catch (ServiceResultException e) {
						e.printStackTrace();
					}
					continue;
				} else if (isTypeDefinition && typeDef == null) {
					typeDef = targetId;
					continue;
				}
				Node targetNode = ServerInstance.getNode(targetId);
				// additional reference item
				AddReferencesItem item = new AddReferencesItem();
				item.setIsForward(!isInverse);
				item.setReferenceTypeId(refId);
				item.setSourceNodeId(newId);
				item.setTargetNodeClass(targetNode.getNodeClass());
				item.setTargetNodeId(targetId);
				references2add.add(item);
			}
			// add nodes
			AddNodesItem item = new AddNodesItem();
			item.setBrowseName(node2copy.getBrowseName());
			item.setNodeAttributes(nodeAttributes);
			item.setNodeClass(node2copy.getNodeClass());
			item.setParentNodeId(expParentId);
			item.setRequestedNewNodeId(expNewId);
			item.setTypeDefinition(typeDef);
			item.setReferenceTypeId(referenceId);
			if (!startNode.isInit()) {
				item.setBrowseName(startNode.getBrowsename());
				nattr.setDescription(startNode.getDescription());
				nattr.setDisplayName(startNode.getDisplayname());
				try {
					item.setNodeAttributes(ExtensionObject.binaryEncode(nattr, EncoderContext.getDefaultInstance()));
				} catch (EncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startNode.setInit();
			}
			/** NodeId mapping */
			nodes2add.put(nodeId2copy, item);
			pasteMapping.put(expNewId, item);
			idMapping.put(nodeId2copy, newId);
			// type of root object to copy is mapping
			if (startRek) {
				typeMapping.put(expNewId, typeDef);
			}
			// find mapping of origin node
			else {
				ExpandedNodeId expTypeId = new ExpandedNodeId(node2copy.getNodeId());
				// ExpandedNodeId expTypeId =
				// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				// .toExpandedNodeId(node2copy.getNodeId());
				ExpandedNodeId typeId = ServerInstance.getTypeModel().getTypeIdFromObject(expTypeId);
				typeMapping.put(expNewId, typeId);
			}
			// add node to prevent rekursion
			nodes.put(newId, nodeId2copy);
			/** children */
			try {
				BrowseDescription browseDescription = new BrowseDescription();
				browseDescription.setBrowseDirection(BrowseDirection.Forward);
				browseDescription.setIncludeSubtypes(true);
				browseDescription.setNodeClassMask(NodeClass.ALL);
				browseDescription.setNodeId(nodeId2copy);
				browseDescription.setReferenceTypeId(Identifiers.HierarchicalReferences);
				browseDescription.setResultMask(BrowseResultMask.ALL);
				BrowseResult[] children = ServerInstance.browse(nodeId2copy, Identifiers.HierarchicalReferences,
						NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Forward, true);
				if (children.length > 0 && children[0].getStatusCode() != null && children[0].getStatusCode().isGood()
						&& children[0].getReferences() != null && children[0].getReferences().length > 0) {
					for (int i = 0; i < children[0].getReferences().length; i++) {
						ReferenceDescription reference = children[0].getReferences()[i];
						ExpandedNodeId child = reference.getNodeId();
						rekPaste(info, index, startNode, idMapping, typeMapping, pasteMapping, nodes2add,
								references2add, /* rekNewIds, */ newId, nsTable.toNodeId(child), nodes, isCopyIds,
								false);
					}
				}
				info.setNodeId(newId);
				info.setNodeAttributes(nattr);
				info.setBrowseName(node2copy.getBrowseName());
				info.setNodeClass(node2copy.getNodeClass());
				info.setNodeAttributes(nattr);
				info.setParentNodeId(expParentId);
				info.setType(nsTable.toNodeId(typeDef));
				info.setReferenceTypeId(referenceId);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
	}

	private NodeAttributes getNodeAttributes(Node node2copy) {
		NodeAttributes nodeAttributes = null;
		switch (node2copy.getNodeClass()) {
		case Object:
			nodeAttributes = new ObjectAttributes();
			((ObjectAttributes) nodeAttributes).setEventNotifier(((ObjectNode) node2copy).getEventNotifier());
			break;
		case ObjectType:
			nodeAttributes = new ObjectTypeAttributes();
			((ObjectTypeAttributes) nodeAttributes).setIsAbstract(((ObjectTypeNode) node2copy).getIsAbstract());
			break;
		case Variable:
			nodeAttributes = new VariableAttributes();
			((VariableAttributes) nodeAttributes).setAccessLevel(((VariableNode) node2copy).getAccessLevel());
			((VariableAttributes) nodeAttributes).setArrayDimensions(((VariableNode) node2copy).getArrayDimensions());
			((VariableAttributes) nodeAttributes).setDataType(((VariableNode) node2copy).getDataType());
			((VariableAttributes) nodeAttributes).setHistorizing(((VariableNode) node2copy).getHistorizing());
			((VariableAttributes) nodeAttributes)
					.setMinimumSamplingInterval(((VariableNode) node2copy).getMinimumSamplingInterval());
			((VariableAttributes) nodeAttributes).setUserAccessLevel(((VariableNode) node2copy).getUserAccessLevel());
			((VariableAttributes) nodeAttributes).setValue(((VariableNode) node2copy).getValue());
			((VariableAttributes) nodeAttributes).setValueRank(((VariableNode) node2copy).getValueRank());
			break;
		case VariableType:
			nodeAttributes = new VariableTypeAttributes();
			((VariableTypeAttributes) nodeAttributes)
					.setArrayDimensions(((VariableTypeNode) node2copy).getArrayDimensions());
			((VariableTypeAttributes) nodeAttributes).setIsAbstract(((VariableTypeNode) node2copy).getIsAbstract());
			((VariableTypeAttributes) nodeAttributes).setDataType(((VariableTypeNode) node2copy).getDataType());
			((VariableTypeAttributes) nodeAttributes).setValue(((VariableTypeNode) node2copy).getValue());
			((VariableTypeAttributes) nodeAttributes).setValueRank(((VariableTypeNode) node2copy).getValueRank());
			break;
		case DataType:
			nodeAttributes = new DataTypeAttributes();
			((DataTypeAttributes) nodeAttributes).setIsAbstract(((DataTypeNode) node2copy).getIsAbstract());
			break;
		case Method:
			nodeAttributes = new MethodAttributes();
			((MethodAttributes) nodeAttributes).setExecutable(((MethodNode) node2copy).getExecutable());
			((MethodAttributes) nodeAttributes).setUserExecutable(((MethodNode) node2copy).getUserExecutable());
			break;
		case ReferenceType:
			nodeAttributes = new ReferenceTypeAttributes();
			((ReferenceTypeAttributes) nodeAttributes).setInverseName(((ReferenceTypeNode) node2copy).getInverseName());
			((ReferenceTypeAttributes) nodeAttributes).setIsAbstract(((ReferenceTypeNode) node2copy).getIsAbstract());
			((ReferenceTypeAttributes) nodeAttributes).setSymmetric(((ReferenceTypeNode) node2copy).getSymmetric());
			break;
		case View:
			nodeAttributes = new ViewAttributes();
			((ViewAttributes) nodeAttributes).setContainsNoLoops(((ViewNode) node2copy).getContainsNoLoops());
			((ViewAttributes) nodeAttributes).setEventNotifier(((ViewNode) node2copy).getEventNotifier());
			break;
		case Unspecified:
			// TODO:should never appear
			break;
		}
		nodeAttributes.setDescription(node2copy.getDescription());
		nodeAttributes.setDisplayName(node2copy.getDisplayName());
		nodeAttributes.setUserWriteMask(node2copy.getUserWriteMask());
		nodeAttributes.setWriteMask(node2copy.getWriteMask());
		return nodeAttributes;
	}

	/**
	 * Parses the text clipboard information
	 * 
	 * @param content
	 * @return
	 */
	private void parseClipboardInformation(String content, Map<NodeId, NodeClass> map) {
		String[] nodeTextInformation = ((String) content).split("\n");
		// TODO: BUT IT IS ONLY SINGLE SELECTION
		/** fetch copy info from clipboard */
		for (int i = 0; i < nodeTextInformation.length; i++) {
			String info = nodeTextInformation[i];
			String[] splitted = info.split("\t");
			/** does not match [nodeid \t nodeclass] \t iscopy \n */
			if (splitted.length < 3) {
				continue;
			}
			NodeId nodeId = NodeId.parseNodeId(splitted[0]);
			NodeClass nodeClass = NodeClass.valueOf(new Integer(splitted[1]));
			map.put(nodeId, nodeClass);
			// depricated is copy
			Boolean.parseBoolean(splitted[2]);
		}
	}

	/**
	 * Checks if paste is allowed with its parent selection
	 * 
	 * @param parentNodeId
	 * 
	 * @param parentNodeClass
	 * @param nodeClass
	 * @return
	 */
	private boolean validatePasteAllowed(NodeId parentNodeId, NodeClass parentNodeClass, NodeClass nodeClass) {
		boolean isAllowed = false;
		switch (parentNodeClass) {
		case Object:
			/**
			 * exclude some objects, which are not allowed to paste something into
			 */
			if (!(Identifiers.TypesFolder.equals(parentNodeId) || Identifiers.DataTypesFolder.equals(parentNodeId)
					|| Identifiers.EventTypesFolder.equals(parentNodeId)
					|| Identifiers.ObjectTypesFolder.equals(parentNodeId)
					|| Identifiers.ReferenceTypesFolder.equals(parentNodeId)
					|| Identifiers.VariableTypesFolder.equals(parentNodeId))) {
				switch (nodeClass) {
				case Object:
				case Variable:
				case Method:
					isAllowed = true;
					break;
				case DataType:
					break;
				case ObjectType:
					break;
				case ReferenceType:
					break;
				case Unspecified:
					break;
				case VariableType:
					break;
				case View:
					break;
				default:
					break;
				}
			}
			break;
		case Variable:
			switch (nodeClass) {
			case Variable:
				isAllowed = true;
				break;
			case DataType:
				break;
			case Method:
				break;
			case Object:
				break;
			case ObjectType:
				break;
			case ReferenceType:
				break;
			case Unspecified:
				break;
			case VariableType:
				break;
			case View:
				break;
			default:
				break;
			}
			break;
		case Method:
			switch (nodeClass) {
			case Variable:
				isAllowed = true;
				break;
			case DataType:
				break;
			case Method:
				break;
			case Object:
				break;
			case ObjectType:
				break;
			case ReferenceType:
				break;
			case Unspecified:
				break;
			case VariableType:
				break;
			case View:
				break;
			default:
				break;
			}
			break;
		case DataType:
			switch (nodeClass) {
			case DataType:
				isAllowed = true;
				break;
			case Method:
				break;
			case Object:
				break;
			case ObjectType:
				break;
			case ReferenceType:
				break;
			case Unspecified:
				break;
			case Variable:
				break;
			case VariableType:
				break;
			case View:
				break;
			default:
				break;
			}
			break;
		case View:
			switch (nodeClass) {
			case Object:
			case View:
				isAllowed = true;
				break;
			case DataType:
				break;
			case Method:
				break;
			case ObjectType:
				break;
			case ReferenceType:
				break;
			case Unspecified:
				break;
			case Variable:
				break;
			case VariableType:
				break;
			default:
				break;
			}
			break;
		case ObjectType:
			switch (nodeClass) {
			case ObjectType:
			case Object:
			case Variable:
				isAllowed = true;
				break;
			case DataType:
				break;
			case Method:
				isAllowed = true;
				break;
			case ReferenceType:
				break;
			case Unspecified:
				break;
			case VariableType:
				break;
			case View:
				break;
			default:
				break;
			}
			break;
		case VariableType:
			switch (nodeClass) {
			case Variable:
			case VariableType:
				isAllowed = true;
				break;
			case DataType:
				break;
			case Method:
				break;
			case Object:
				break;
			case ObjectType:
				break;
			case ReferenceType:
				break;
			case Unspecified:
				break;
			case View:
				break;
			default:
				break;
			}
			break;
		case ReferenceType:
			switch (nodeClass) {
			case ReferenceType:
				isAllowed = true;
				break;
			case DataType:
				break;
			case Method:
				break;
			case Object:
				break;
			case ObjectType:
				break;
			case Unspecified:
				break;
			case Variable:
				break;
			case VariableType:
				break;
			case View:
				break;
			default:
				break;
			}
			break;
		case Unspecified:
			break;
		default:
			break;
		}
		return isAllowed;
	}

	class ModelChange {
		private ModelChangeInfo info;
		private NodeId nodeId;
		private NodeClass nodeClass;

		public ModelChange(NodeId nodeId, NodeClass nodeClass, ModelChangeInfo info) {
			this.info = info;
			this.nodeId = nodeId;
			this.nodeClass = nodeClass;
		}

		public ModelChangeInfo getInfo() {
			return info;
		}

		public NodeId getNodeId() {
			return nodeId;
		}

		public NodeClass getNodeClass() {
			return nodeClass;
		}
	}
}
