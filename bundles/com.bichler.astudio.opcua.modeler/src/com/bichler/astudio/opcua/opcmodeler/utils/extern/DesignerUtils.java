package com.bichler.astudio.opcua.opcmodeler.utils.extern;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.addressspace.model.tool.OPCProgressMonitor;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.CreateFactory;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorPart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTreeDef;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.opcua.opcmodeler.preferences.DesignerPreferences;
import com.bichler.astudio.opcua.opcmodeler.preferences.ShowDialogPreferencePage;
import com.bichler.astudio.opcua.opcmodeler.utils.OperationType;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.NoCancleWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.OPCUACopyModelWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.PasteStartNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.attribute.ModelAttributeWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.change.ModelChangeWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.remove.ModelRemoveWizard;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.persistence.xml.SaxNodeWriter;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.OPCInternalServer;

public class DesignerUtils {
	private static final int COUNT = 1;

	private static void addDataTypeChildren(Map<NodeId, String> dataTypes, NodeId startId) {
		Node[] children = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
				.findChildren(startId);
		if (children.length > 0) {
			addDataTypeChildrenToMap(dataTypes, children);
			for (Node child : children) {
				addDataTypeChildren(dataTypes, child.getNodeId());
			}
		} else {
			return;
		}
	}

	private static void addDataTypeChildrenToMap(Map<NodeId, String> types, Node[] children) {
		for (Node child : children) {
			if (NodeClass.DataType.equals(child.getNodeClass())) {
				types.put(child.getNodeId(), child.getBrowseName().getName());
			}
		}
	}


	private static void addObjectTypeChildren(Map<String, NodeId> objectTypes, NodeId startId, TypeTable typeTable) {
		Node[] children = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
				.findChildren(startId);
		if (children.length > 0) {
			addObjectTypeChildrenToMap(objectTypes, children);
			for (Node child : children) {
				addObjectTypeChildren(objectTypes, child.getNodeId(), typeTable);
			}
		} else {
			return;
		}
	}

	private static void addObjectTypeChildrenToMap(Map<String, NodeId> objectTypes, Node[] children) {
		for (Node child : children) {
			if (NodeClass.ObjectType.equals(child.getNodeClass())) {
				objectTypes.put(child.getBrowseName().getName(), child.getNodeId());
			}
		}
	}



	private static void addVariableTypeChildren(Map<String, NodeId> objectTypes, NodeId startId, TypeTable typeTable) {
		Node[] children = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
				.findChildren(startId);
		if (children.length > 0) {
			addVariableTypeChildrenToMap(objectTypes, children);
			for (Node child : children) {
				addVariableTypeChildren(objectTypes, child.getNodeId(), typeTable);
			}
		} else {
			return;
		}
	}

	private static void addVariableTypeChildrenToMap(Map<String, NodeId> objectTypes, Node[] children) {
		for (Node child : children) {
			if (NodeClass.VariableType.equals(child.getNodeClass())) {
				objectTypes.put(child.getBrowseName().getName(), child.getNodeId());
			}
		}
	}
	
	private static BrowserModelNode find(NodeId targetId, BrowserModelNode[] children) {
		BrowserModelNode found = null;
		for (BrowserModelNode child : children) {
			NodeId nodeId = child.getNode().getNodeId();
			if (nodeId.equals(targetId)) {
				found = child;
				break;
			}
			return find(targetId, child.getChildren());
		}
		return found;
	}
	
	private static Node findParent(Node node) {
		try {
			BrowseResult[] results = ServerInstance.browse(node.getNodeId(), Identifiers.HierarchicalReferences,
					NodeClass.ALL, BrowseResultMask.ALL, BrowseDirection.Inverse, true);
			if (results != null && results.length > 0 && results[0].getReferences() != null
					&& results[0].getReferences().length > 0) {
				ExpandedNodeId parentId = results[0].getReferences()[0].getNodeId();
				return ServerInstance.getNode(parentId);
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Returns all DataTypes from the AddressSpace.
	 * 
	 * @param AddressSpace AddressSpace to fetch all DataTypes
	 * @return Map Keyed by the DataTypeId and the value is its BrowseName.
	 */
	public static Map<NodeId, String> fetchAllDatatypes() {
		// Map<NodeId, String> dataTypes = new HashMap<NodeId, String>();
		NodeId startId = Identifiers.BaseDataType;
		return fetchAllDatatypes(startId);
		/**
		 * String startString = "BaseDataType"; dataTypes.put(startId, startString);
		 * 
		 * addDataTypeChildren(dataTypes, startId, addressSpace, typeTable);
		 * 
		 * return dataTypes;
		 */
	}
	
	public static Map<NodeId, String> fetchAllDatatypesSortByValue(){
		Map<NodeId, String> dt = fetchAllDatatypes();
		List<Entry<NodeId, String>> sorted = new ArrayList<>();
		
		sorted.addAll(dt.entrySet());
		sorted.sort(new Comparator<Entry<NodeId, String>>() {

			@Override
			public int compare(Entry<NodeId, String> o1, Entry<NodeId, String> o2) {
				String value1 = o1.getValue();
				String value2 = o2.getValue();
				return value1.compareTo(value2);
			}
		});
		
		Map<NodeId, String> sortedMap = new LinkedHashMap<>();
		
		for(Entry<NodeId, String> obj : sorted) {
			sortedMap.put(obj.getKey(), obj.getValue());
		}
		
		return sortedMap;
	}

	/**
	 * Returns all DataTypes from the AddressSpace.
	 * 
	 * @param AddressSpace AddressSpace to fetch all DataTypes
	 * @return Map Keyed by the DataTypeId and the value is its BrowseName.
	 */
	public static Map<NodeId, String> fetchAllDatatypes(NodeId startId) {
		Map<NodeId, String> dataTypes = new HashMap<NodeId, String>();
		// TypeTable typeTable =
		// ServerInstance.getInstance().getServerInstance()
		// .getTypeTree();
		// InternalServer server = ServerInstance.getInstance()
		// .getServerInstance();

		Node node = ServerInstance.getNode(startId);
		if (node != null) {
			dataTypes.put(startId, node.getBrowseName().getName());
		}
		addDataTypeChildren(dataTypes, startId);
		return dataTypes;
	}
	
	public static Map<String, NodeId> fetchAllObjectTypes() {
		Map<String, NodeId> objectTypes = new HashMap<String, NodeId>();
		TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
		NodeId startId = Identifiers.ObjectTypesFolder;
		addObjectTypeChildren(objectTypes, startId, typeTable);
		return objectTypes;
	}

	public static Map<String, NodeId> fetchAllVariableTypes() {
		Map<String, NodeId> variableTypes = new HashMap<String, NodeId>();
		TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
		NodeId startId = Identifiers.VariableTypesFolder;
		addVariableTypeChildren(variableTypes, startId, typeTable);
		return variableTypes;
	}

	

	/**
	 * Resolves an image with the given path and the image name to the plugin
	 * 
	 * @param path
	 * @param imgName
	 * @return
	 */
	public static Image resolveImage(IPath path, String imgName) {
		path = path.append(imgName);
		URL imgURL = FileLocator.find(Activator.getDefault().getBundle(), path, null);
		URL fileURL = null;
		try {
			fileURL = FileLocator.toFileURL(imgURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(fileURL);
		Image image = descriptor.createImage();
		return image;
	}

	/**
	 * checks the childreferences between the parent and the child, if child and
	 * parent is the same id, it also returns true.
	 * 
	 * @param child
	 * @param parent
	 * @return
	 */
	public static boolean isChildOf(NodeId child, NodeId parent) {
		if (child.equals(parent)) {
			return true;
		}
		// InternalServer internalServer = ServerInstance.getInstance()
		// .getServerInstance();
		// AddressSpace addressSpace = internalServer.getAddressSpace();
		// MasterNodeManager manager = internalServer.getMasterNodeManager();
		// TypeTable typeTree = internalServer.getTypeTree();
		BrowseResult[] result = null;
		NodeId target = null;
		try {
			/** iterate to root folder or to parent */
			do {
				result = ServerInstance.browse(child, Identifiers.HierarchicalReferences, NodeClass.ALL,
						BrowseResultMask.ALL, BrowseDirection.Inverse, true);
				if (result != null && result.length > 0 && result[0].getReferences() != null
						&& result[0].getReferences().length > 0) {
					target = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(result[0].getReferences()[0].getNodeId());
					if (parent.equals(target)) {
						return true;
					} else if (Identifiers.RootFolder.equals(target)) {
						return false;
					}
					child = target;
				}
				// no more parent
				else {
					return true;
				}
			} while (true);
		} catch (ServiceResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param parent
	 * @param derived
	 * @return
	 */
	public static List<NodeId> getInheritance(NodeId parent, NodeId derived) {
		List<NodeId> hierarchie = new ArrayList<NodeId>();
		// InternalServer internalServer = ServerInstance.getInstance()
		// .getServerInstance();
		// AddressSpace addressSpace = internalServer.getAddressSpace();
		// MasterNodeManager manager = internalServer.getMasterNodeManager();
		// TypeTable typeTree = internalServer.getTypeTree();
		BrowseResult[] result = null;
		NodeId target = null;
		hierarchie.add(derived);
		try {
			/** iterate to root folder or to parent */
			do {
				result = ServerInstance.browse(derived, Identifiers.HasSubtype, NodeClass.ALL, BrowseResultMask.ALL,
						BrowseDirection.Inverse, true);
				// fetch parent hierachies
				if (result.length > 0 && result[0].getReferences().length > 0) {
					target = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(result[0].getReferences()[0].getNodeId());
					hierarchie.add(target);
					if (parent.equals(target)) {
						break;
					} else if (Identifiers.RootFolder.equals(target)) {
						break;
					}
					derived = target;
				} else if (result.length > 0 && result[0].getReferences().length == 0
						&& result[0].getStatusCode().isGood()) {
					break;
				}
			} while (true);
		} catch (ServiceResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hierarchie;
	}

	public static BrowserModelNode findBrowserModelNode(IWorkbenchWindow workbenchWindow, ExpandedNodeId targetId)
			throws ServiceResultException {
		ModelBrowserView view = (ModelBrowserView) workbenchWindow.getActivePage().findView(ModelBrowserView.ID);
		if (view == null) {
			return null;
		}
		TreeViewer treeViewer = view.getTreeViewer();
		BrowserModelNode root = (BrowserModelNode) treeViewer.getInput();
		return find(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(targetId),
				root.getChildren());
	}

	public static void askModelChangeAttribute(BrowserModelNode node, UnsignedInteger attributeId) {
		ModelTypDef ptd = findParentTypDef(node);
		if (ptd == null) {
			return;
		}
		ptd.buildChangedAttributeModelTree(attributeId);
		openWizardModelAttribute(ptd, attributeId, node);
	}

	/**
	 * If Object, Variable or Method is created
	 * 
	 * @param parent
	 * @param result
	 */
	public static void askModelChange(BrowserModelNode parent, ModelChangeInfo result) {
		ModelTypDef ptd = findParentTypDef(parent);
		if (ptd == null) {
			return;
		}
		// typemodel has changed
		ptd.buildChangedModelTree(result);
		ptd.buildTypeTree();
		openWizardModelChangeCreate(ptd, result);
	}

	public static void askModelChangeRemove(BrowserModelNode node, DeleteNodesItem[] removedNodes) {
		ModelTypDef ptd = findParentTypDef(node);
		if (ptd == null) {
			return;
		}
		// map NodeId to ExpandedNodeId
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		List<ExpandedNodeId> nodes2remove = new ArrayList<>();
		for (DeleteNodesItem remove : removedNodes) {
			String nsUri = nsTable.getUri(remove.getNodeId().getNamespaceIndex());
			// ExpandedNodeId expNewId =
			nodes2remove.add(new ExpandedNodeId(remove.getNodeId()));
			// nodes2remove.add(nsTable.toExpandedNodeId(remove.getNodeId()));
		}
		// typemodel has changed
		ptd.buildRemovedModelTree(nodes2remove);
		ptd.buildTypeTree();
		openWizardModelChangeRemove(ptd, nodes2remove);
	}

	public static void doModelChangeRemove(Node[] nodes, Map<NodeId, DeleteNodesItem> removedNodes)
			throws ServiceResultException {
		// sorting by nodeclasses
		Node[] sorted = sortNodesForModelChangeRemove(nodes);
		for (Node node2remove : sorted) {
			ModelTypDef ptd = findParentTypDef(node2remove);
			if (ptd == null) {
				return;
			}
			List<DeleteNodesItem> nodesToDelete = new ArrayList<>();
			List<NodeId> preventLoop = new ArrayList<>();
			findAllNodesToDelete(preventLoop, nodesToDelete,
					ServerInstance.getInstance().getServerInstance().getTypeTable(), node2remove.getNodeId());
			for (DeleteNodesItem remove : nodesToDelete) {
				if (!removedNodes.containsKey(remove.getNodeId())) {
					removedNodes.put(remove.getNodeId(), new DeleteNodesItem(remove.getNodeId(), true));
				}
			}
			// map NodeId to ExpandedNodeId
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			List<ExpandedNodeId> nodes2remove = new ArrayList<>();
			for (DeleteNodesItem remove : nodesToDelete) {
				nodes2remove.add(new ExpandedNodeId(remove.getNodeId()));
				// nodes2remove.add(nsTable.toExpandedNodeId(remove.getNodeId()));
			}
			for (DeleteNodesItem remove : removedNodes.values()) {
				nodes2remove.add(new ExpandedNodeId(remove.getNodeId()));
				// nodes2remove.add(nsTable.toExpandedNodeId(remove.getNodeId()));
			}
			// typemodel has changed
			ptd.buildRemovedModelTree(nodes2remove);
			ptd.buildTypeTree();
			doModelChangeRemove(ptd, nodes2remove);
			// openWizardModelChangeRemove(ptd, nodes2remove);
		}
	}

	public static void findAllNodesToDelete(List<NodeId> preventLoop, List<DeleteNodesItem> nodesToDelete,
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

	private static Node[] sortNodesForModelChangeRemove(Node[] nodes) {
		List<Node> sorted = new ArrayList<>();
		sortNodes(NodeClass.DataType, nodes, sorted);
		sortNodes(NodeClass.ObjectType, nodes, sorted);
		sortNodes(NodeClass.VariableType, nodes, sorted);
		sortNodes(NodeClass.Object, nodes, sorted);
		sortNodes(NodeClass.Method, nodes, sorted);
		sortNodes(NodeClass.Variable, nodes, sorted);
		sortNodes(NodeClass.View, nodes, sorted);
		sortNodes(NodeClass.ReferenceType, nodes, sorted);
		return sorted.toArray(new Node[0]);
	}

	private static void sortNodes(NodeClass nodeClass, Node[] nodes, List<Node> sorted) {
		for (Node node : nodes) {
			if (node.getNodeClass() == nodeClass) {
				sorted.add(node);
			}
		}
	}

	private static void openWizardModelAttribute(ModelTypDef ptd, UnsignedInteger attributeId, BrowserModelNode node) {
		// DataValue value = new DataValue();
		// node.getNode().readAttributeValue(attributeId, value);
		ModelAttributeWizard wizard = new ModelAttributeWizard();
		wizard.setNeedsProgressMonitor(true);
		wizard.setTypeDef(ptd);
		wizard.setAttributeId(attributeId);
		wizard.setValue2update(node.getNode().getNodeId());
		WizardDialog dialog = new NoCancleWizard(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				wizard);
		dialog.open();
	}

	private static void openWizardModelChangeCreate(final ModelTypDef ptd, final ModelChangeInfo info) {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ProgressMonitorDialog progressDilog = new ProgressMonitorDialog(shell);
		try {
			progressDilog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Betroffene Objekte werden gesucht...", IProgressMonitor.UNKNOWN);
					final ModelTypDef[] input = getMCCInput(ptd);
					monitor.done();
					if (input.length > 0) {
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								ModelChangeWizard wizard = new ModelChangeWizard();
								wizard.setInput(input);
								wizard.setTypeDef(ptd);
								// wizard.setMCInfo(info);
								wizard.setNeedsProgressMonitor(true);
								WizardDialog dialog = new NoCancleWizard(shell, wizard);
								dialog.open();
								// ModelRemoveWizard wizard = new
								// ModelRemoveWizard();
								// wizard.setNeedsProgressMonitor(true);
								// wizard.setTypeDef(ptd);
								// wizard.setInput(input);
								// wizard.setNodesToRemove(nodes2remove);
								// WizardDialog dialog = new
								// NoCancleWizard(shell, wizard);
								// dialog.open();
							}
						});
					}
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ModelChangeWizard wizard = new ModelChangeWizard();
		// wizard.setTypeDef(ptd);
		// wizard.setMCInfo(info);
		// wizard.setNeedsProgressMonitor(true);
		// WizardDialog dialog = new NoCancleWizard(shell, wizard);
		// dialog.open();
	}

	private static void doModelChangeRemove(ModelTypDef ptd, List<ExpandedNodeId> nodes2remove)
			throws ServiceResultException {
		final ModelTypDef[] input = getMCRInput(ptd);
		// do remove from remove wizard
		modelChangeRemove(ptd, input, nodes2remove);
	}

	private static void modelChangeRemove(ModelTypDef ptd, ModelTypDef[] input, List<ExpandedNodeId> nodes2remove)
			throws ServiceResultException {
		List<ExpandedNodeId> objectNodes2remove = new ArrayList<>();
		for (ModelTypDef updateDef : ((ModelTypDef[]) input)) {
			// parent <=> typedef
			updateDef.buildModelTree();
			ptd.doCompareModelRemove(objectNodes2remove, nodes2remove, updateDef);
			// refresh.add(updateDef);
		}
		// remove model changes itself
		remove(objectNodes2remove);
	}

	private static void remove(List<ExpandedNodeId> nodes2remove) throws ServiceResultException {
		List<DeleteNodesItem> delete = new ArrayList<>();
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		for (ExpandedNodeId node2remove : nodes2remove) {
			Node node = ServerInstance.getNode(node2remove);
			if (node == null) {
				continue;
			}
			try {
				List<DeleteNodesItem> nodesToDelete = new ArrayList<>();
				List<NodeId> preventLoop = new ArrayList<>();
				DesignerUtils.findAllNodesToDelete(preventLoop, nodesToDelete,
						ServerInstance.getInstance().getServerInstance().getTypeTable(), nsTable.toNodeId(node2remove));
				delete.addAll(nodesToDelete);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		CreateFactory.remove(delete.toArray(new DeleteNodesItem[0]));
	}

	private static void openWizardModelChangeRemove(final ModelTypDef ptd, final List<ExpandedNodeId> nodes2remove) {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
		try {
			progressDialog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Betroffene Objekte werden gesucht...", IProgressMonitor.UNKNOWN);
					// try {
					final ModelTypDef[] input = getMCRInput(ptd);
					// } finally {
					monitor.done();
					// }
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// no element has changed. do not open an wizard.
							if (input == null) {
								return;
							}
							if (input.length <= 0) {
								return;
							}
							ModelRemoveWizard wizard = new ModelRemoveWizard();
							wizard.setNeedsProgressMonitor(true);
							wizard.setTypeDef(ptd);
							wizard.setInput(input);
							wizard.setNodesToRemove(nodes2remove);
							WizardDialog dialog = new NoCancleWizard(shell, wizard);
							dialog.open();
						}
					});
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static ModelTypDef[] getMCCInput(ModelTypDef ptd) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ModelTreeDef typeTree = ptd.getTypeTree();
		// there is a tree
		List<ExpandedNodeId> typeList = new ArrayList<>();
		typeList.add(
				new ExpandedNodeId(ptd.typeId));
		// typeList.add(nsTable.toExpandedNodeId(ptd.typeId));
		rekTyp(typeList, typeTree);
		List<ExpandedNodeId> preventStackOverflow = new ArrayList<>();
		List<ModelTypDef> effected = new ArrayList<>();
		try {
			/**
			 * find all effected types under objects folder
			 */
			doInputMCC(nsTable, effected,
					new ExpandedNodeId(Identifiers.ObjectsFolder),
					NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method), typeList,
					preventStackOverflow, true);
			// doInputMCC(nsTable, effected,
			// nsTable.toExpandedNodeId(Identifiers.ObjectsFolder),
			// NodeClass.getMask(NodeClass.Object, NodeClass.Variable,
			// NodeClass.Method), typeList,
			// preventStackOverflow, true);
			//
			switch (ptd.typeClass) {
			// object types folder
			case ObjectType:
				doInputMCC(nsTable, effected,
						new ExpandedNodeId(Identifiers.ObjectTypesFolder),
						NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method, NodeClass.ObjectType),
						typeList, preventStackOverflow, false);
				// doInputMCC(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.ObjectTypesFolder),
				// NodeClass.getMask(NodeClass.Object, NodeClass.Variable,
				// NodeClass.Method, NodeClass.ObjectType),
				// typeList, preventStackOverflow, false);
				break;
			// variable types folder AND object types folder
			case VariableType:
				doInputMCC(nsTable, effected,
						new ExpandedNodeId(Identifiers.ObjectTypesFolder),
						NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method, NodeClass.ObjectType),
						typeList, preventStackOverflow, false);
				// doInputMCC(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.ObjectTypesFolder),
				// NodeClass.getMask(NodeClass.Object, NodeClass.Variable,
				// NodeClass.Method, NodeClass.ObjectType),
				// typeList, preventStackOverflow, false);
				doInputMCC(nsTable, effected,
						new ExpandedNodeId(Identifiers.VariableTypesFolder),
						NodeClass.getMask(NodeClass.Variable, NodeClass.VariableType), typeList, preventStackOverflow,
						false);
				// doInputMCC(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.VariableTypesFolder),
				// NodeClass.getMask(NodeClass.Variable, NodeClass.VariableType),
				// typeList, preventStackOverflow,
				// false);
				break;
			// reftypefolder
			case ReferenceType:
				doInputMCC(nsTable, effected,
						new ExpandedNodeId(Identifiers.ReferenceTypesFolder),
						NodeClass.getMask(NodeClass.ReferenceType, NodeClass.Variable), typeList, preventStackOverflow,
						false);
				// doInputMCC(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.ReferenceTypesFolder),
				// NodeClass.getMask(NodeClass.ReferenceType, NodeClass.Variable),
				// typeList, preventStackOverflow,
				// false);
				break;
			// datatype folder
			case DataType:
				doInputMCC(nsTable, effected,
						new ExpandedNodeId(Identifiers.DataTypesFolder),
						NodeClass.getMask(NodeClass.DataType, NodeClass.Variable), typeList, preventStackOverflow,
						false);
				// doInputMCC(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.DataTypesFolder),
				// NodeClass.getMask(NodeClass.DataType, NodeClass.Variable), typeList,
				// preventStackOverflow,
				// false);
				break;
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return effected.toArray(new ModelTypDef[0]);
	}

	private static ModelTypDef[] getMCRInput(ModelTypDef ptd) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		ModelTreeDef typeTree = ptd.getTypeTree();
		// there is a tree
		List<ExpandedNodeId> typeList = new ArrayList<>();
		typeList.add(
				new ExpandedNodeId(ptd.typeId));
		// typeList.add(nsTable.toExpandedNodeId(ptd.typeId));
		rekTyp(typeList, typeTree);
		List<ExpandedNodeId> preventLoop = new ArrayList<>();
		List<ModelTypDef> effected = new ArrayList<>();
		try {
			/**
			 * find all effected types under objects folder
			 */
			doInputMCR(nsTable, effected,
					new ExpandedNodeId(Identifiers.ObjectsFolder),
					NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
			// doInputMCR(nsTable, effected,
			// nsTable.toExpandedNodeId(Identifiers.ObjectsFolder),
			// NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
			switch (ptd.typeClass) {
			// object types folder
			case ObjectType:
				doInputMCR(nsTable, effected,
						new ExpandedNodeId(Identifiers.ObjectTypesFolder),
						NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				// doInputMCR(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.ObjectTypesFolder),
				// NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				break;
			// variable types folder AND object types folder
			case VariableType:
				doInputMCR(nsTable, effected,
						new ExpandedNodeId(Identifiers.ObjectTypesFolder),
						NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				// doInputMCR(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.ObjectTypesFolder),
				// NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				doInputMCR(nsTable, effected,
						new ExpandedNodeId(Identifiers.VariableTypesFolder),
						NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				// doInputMCR(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.VariableTypesFolder),
				// NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				break;
			// reftypefolder
			case ReferenceType:
				doInputMCR(nsTable, effected,
						new ExpandedNodeId(Identifiers.ReferenceTypesFolder),
						NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				// doInputMCR(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.ReferenceTypesFolder),
				// NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				break;
			// datatype folder
			case DataType:
				doInputMCR(nsTable, effected,
						new ExpandedNodeId(Identifiers.DataTypesFolder),
						NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				// doInputMCR(nsTable, effected,
				// nsTable.toExpandedNodeId(Identifiers.DataTypesFolder),
				// NodeClass.getMask(NodeClass.ALL), typeList, preventLoop, true);
				break;
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return effected.toArray(new ModelTypDef[0]);
	}

	private static void rekTyp(List<ExpandedNodeId> typeList, ModelTreeDef typeTree) {
		if (typeTree != null) {
			for (ModelTreeDef typeModel : typeTree.getChildren()) {
				typeList.add(typeModel.getNodeId());
				rekTyp(typeList, typeModel);
			}
		}
	}

	/**
	 * 
	 * @param nsTable
	 * @param effected
	 * @param browse
	 * @param nodeClassMask
	 * @param typesToFind
	 * @param preventLoop
	 * @param mapToType     Map elements under the objects folder to its given types
	 * @throws ServiceResultException
	 */
	private static void doInputMCC(NamespaceTable nsTable, List<ModelTypDef> effected, ExpandedNodeId browse,
			UnsignedInteger nodeClassMask, List<ExpandedNodeId> typesToFind, List<ExpandedNodeId> preventLoop,
			boolean mapToType) throws ServiceResultException {
		BrowseResult[] result;
		try {
			result = ServerInstance.browse(nsTable.toNodeId(browse), Identifiers.HierarchicalReferences,
					NodeClass.getSet(nodeClassMask), BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (result != null && result.length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					ExpandedNodeId refTyp = reference.getTypeDefinition();
					boolean exist = preventLoop.contains(reference.getNodeId());
					// dont follow existing path
					if (exist) {
						continue;
					}
					// add found element with given typ
					if (refTyp != null && typesToFind.contains(refTyp)) {
						ModelTypDef m = new ModelTypDef();
						m.name = reference.getDisplayName();
						m.typeClass = reference.getNodeClass();
						m.nodeId = reference.getNodeId();
						m.reference = reference;
						m.mapToType = mapToType;
						effected.add(m);
					}
					// prevents cycle
					preventLoop.add(reference.getNodeId());
					// rek
					doInputMCC(nsTable, effected, reference.getNodeId(), nodeClassMask, typesToFind, preventLoop,
							mapToType);
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	private static void doInputMCR(NamespaceTable nsTable, List<ModelTypDef> effected, ExpandedNodeId browse,
			UnsignedInteger nodeClassMask, List<ExpandedNodeId> typesToFind, List<ExpandedNodeId> preventLoop,
			boolean mapToType) throws ServiceResultException {
		// browse children
		BrowseResult[] result = null;
		try {
			result = ServerInstance.browse(nsTable.toNodeId(browse), Identifiers.HierarchicalReferences,
					NodeClass.getSet(nodeClassMask), BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (result != null && result.length > 0) {
				ReferenceDescription[] references = result[0].getReferences();
				for (ReferenceDescription reference : references) {
					ExpandedNodeId refTyp = reference.getTypeDefinition();
					boolean exist = preventLoop.contains(reference.getNodeId());
					// dont follow existing path
					if (exist) {
						continue;
					}
					// add found element with given typ
					if (refTyp != null && typesToFind.contains(refTyp)) {
						ModelTypDef m = new ModelTypDef();
						m.name = reference.getDisplayName();
						m.typeClass = reference.getNodeClass();
						m.nodeId = reference.getNodeId();
						m.reference = reference;
						m.mapToType = mapToType;
						effected.add(m);
					}
					// prevents cycle
					preventLoop.add(reference.getNodeId());
					doInputMCR(nsTable, effected, reference.getNodeId(), nodeClassMask, typesToFind, preventLoop,
							mapToType);
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	public static ModelTypDef findParentTypDef(BrowserModelNode node) {
		NodeClass c = node.getNode().getNodeClass();
		switch (c) {
		case ObjectType:
		case VariableType:
			ModelTypDef ptd = new ModelTypDef();
			ptd.typeId = node.getNode().getNodeId();
			ptd.typeClass = c;
			ptd.name = node.getNode().getDisplayName();
			return ptd;
		default:
			if (node.getParent() != null) {
				return findParentTypDef(node.getParent());
			}
			break;
		}
		return null;
	}

	public static ModelTypDef findParentTypDef(Node node) {
		NodeClass c = node.getNodeClass();
		switch (c) {
		case ObjectType:
		case VariableType:
			ModelTypDef ptd = new ModelTypDef();
			ptd.typeId = node.getNodeId();
			ptd.typeClass = c;
			ptd.name = node.getDisplayName();
			return ptd;
		default:
			Node parent = findParent(node);
			if (parent != null) {
				return findParentTypDef(parent);
			}
			break;
		}
		return null;
	}


	public static ModelBrowserView refreshBrowserAll() {
		for (IViewReference viewRef : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getViewReferences()) {
			if (viewRef.getId().equals(ModelBrowserView.ID)) {
				ModelBrowserView part = (ModelBrowserView) viewRef.getPart(true);
				TreeViewer viewer = part.getTreeViewer();
				BrowserModelNode root = (BrowserModelNode) viewer.getInput();
				refreshAll(viewer, root.getChildren());
				return part;
			}
		}
		return null;
	}

	private static void refreshAll(TreeViewer viewer, BrowserModelNode[] children) {
		for (BrowserModelNode child : children) {
			viewer.refresh(child);
			refreshAll(viewer, child.getChildren());
		}
	}

	public static void refreshBrowserNode(BrowserModelNode node, boolean isDirty) {
		if (node == null) {
			return;
		}
		for (IViewReference viewRef : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getViewReferences()) {
			if (viewRef.getId().equals(ModelBrowserView.ID)) {
				ModelBrowserView part = (ModelBrowserView) viewRef.getPart(true);
				part.refresh(node);
				part.setDirty(isDirty);
				break;
			}
		}
		refreshEditors();
	}

	public static void refreshEditors() {
		IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		if (editorRefs == null) {
			return;
		}
		for (IEditorReference editorRef : editorRefs) {
			IEditorPart editor = editorRef.getEditor(false);
			if (editor instanceof NodeEditorPart) {
				((NodeEditorPart) editor).refreshReferenceTable();
			}
		}
	}

	public static void updateBrowserNode(BrowserModelNode node) {
		for (IViewReference page : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getViewReferences()) {
			if (page.getId().equals(ModelBrowserView.ID)) {
				ModelBrowserView viewRef = ((ModelBrowserView) page.getPart(true));
				viewRef.update(node);
				break;
			}
		}
	}

	/**
	 * Refresh parents
	 * 
	 * @param array
	 */
	public static void refresh(Collection<ExpandedNodeId> array) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		for (IViewReference viewRef : window.getActivePage().getViewReferences()) {
			if (viewRef.getId().equals(ModelBrowserView.ID)) {
				ModelBrowserView part = (ModelBrowserView) viewRef.getPart(true);
				BrowserModelNode root = (BrowserModelNode) part.getTreeViewer().getInput();
				NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
				startRefresh(nsTable, part, root, array);
				break;
			}
		}
	}

	private static void startRefresh(NamespaceTable nsTable, ModelBrowserView part, BrowserModelNode node,
			Collection<ExpandedNodeId> array) {
		NodeId nodeId = node.getNode().getNodeId();
		ExpandedNodeId id = new ExpandedNodeId(nodeId);
		// ExpandedNodeId id = nsTable.toExpandedNodeId(nodeId);
		if (array.contains(id)) {
			part.refresh(node);
		} else {
			for (BrowserModelNode child : node.getChildren()) {
				startRefresh(nsTable, part, child, array);
			}
		}
	}

	public static PasteStartNode askNamespaceIndexIfNeeded(Shell shell, OperationType operation, Integer namespaceIndex,
			Node parent, String content) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		boolean isShowDialog = preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_SHOWDIALOG_COPYPASTE);
		if (isShowDialog) {
			OPCUACopyModelWizard wizard = new OPCUACopyModelWizard(parent);
			wizard.setContents(content);
			WizardDialog dialog = new WizardDialog(shell, wizard);
			if (WizardDialog.OK == dialog.open()) {
				return wizard.getStartNode();
			}
		}
		return null;
	}

	public static NodeIdMode getPreferenceContinueCreateNodeIds() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		int value = preferenceStore.getInt(ShowDialogPreferencePage.PREFERENCE_NODEID_FILL_GAPES);
		return NodeIdMode.getValue(value);
	}

	public static String getPreferenceLastDestinationFile() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		return preferenceStore.getString(DesignerPreferences.PREFERENCE_LAST_DESTINATION_FILE);
	}

	public static void setPreferenceLastDestinationFile(String destinationFile) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setValue(DesignerPreferences.PREFERENCE_LAST_DESTINATION_FILE, destinationFile);
	}

	public static boolean doSaveOPCInformationModel(IProgressMonitor monitor, String param) {
		String backupModelPath = new Path(param).append("BUmodel.xml").toOSString();
		String modelPath = new Path(param).append("model.xml").toOSString();
		// find current server instance
		OPCInternalServer sInstance = ServerInstance.getInstance().getServerInstance();
		// get all nodes to export
		Node[] nodes = sInstance.getAddressSpaceManager().getAllNodes();
		// begin task
		int work = nodes.length;
		if (monitor != null) {
			monitor.beginTask("Speichern ...", work);
			monitor.subTask("Abspeichern des Informationsmodells ...");
		}
		// initialize writer
		SaxNodeWriter writer = new SaxNodeWriter(sInstance.getNamespaceUris(), sInstance.getServerUris(),
				sInstance.getTypeTable());
		// internal progress monitor to cancle operations
		OPCProgressMonitor progress = new OPCProgressMonitor(monitor);
		writer.setProgressMonitor(progress, COUNT);

		// persist extension infos
		Studio_ResourceManager.persistNodeExtensions();
		// also save node infos
		Studio_ResourceManager.persistNodeInfos();
		// save datatype definitions
		Studio_ResourceManager.persistDatatypeDefinitions();
		// save OPC UA node custom attributes
		Studio_ResourceManager.persistNodeCustomAttributes();
		
		// fetch full server namespacetable
		NamespaceTable allNs = sInstance.getNamespaceUris();
		// write
		FileOutputStream fis = null;
		File modelFile = null;
		File backupFile = null;
		boolean isSuccessFull = true;
		try {
			modelFile = new File(modelPath);
			backupFile = new File(backupModelPath);
			boolean fileExist = modelFile.exists();
			// save backup
			if (fileExist) {
				// backupFile.createNewFile();
				modelFile.renameTo(backupFile);
			}
			fileExist = modelFile.exists();
			// create new file
			if (!fileExist) {
				modelFile.createNewFile();
			}
			fis = new FileOutputStream(modelFile);
			isSuccessFull = writer.writeNodes(fis, nodes, allNs);
		} catch (IOException e) {
			// delete existing modelfile because it is buggy
			isSuccessFull = false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// cancled by user... restore backup
		boolean isBu = backupFile != null ? backupFile.exists() : false;
		if (!isSuccessFull) {
			if (isBu) {
				boolean isModel = modelFile != null ? modelFile.exists() : false;
				if (isModel) {
					modelFile.delete();
					modelFile = new File(modelPath);
				}
				backupFile.renameTo(modelFile);
			}
		}
		// write opc types model
		else {
			ServerInstance.exportTypesModel(modelPath, allNs.toArray());
		}
		// delete backup file
		if (isBu) {
			backupFile.delete();
		}
		return isSuccessFull;
	}
}
