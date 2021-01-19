package com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.change;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelChangeInfo;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ModelChangeWizard extends Wizard {
	private ModelChangeElementPage pageOne;
	private ModelChangeElementIdPage pageTwo;
	private ModelTypDef typeDef;
	// private ModelChangeInfo info;
	private ModelTypDef[] input;

	// private ModelChangeInfo modelChange;
	public ModelChangeWizard() {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.synchronize"));
	}

	@Override
	public void addPages() {
		this.pageOne = new ModelChangeElementPage();
		this.pageOne.setTypeDef(this.typeDef);
		this.pageOne.setInput(this.input);
		addPage(this.pageOne);
		this.pageTwo = new ModelChangeElementIdPage();
		addPage(this.pageTwo);
	}

	@Override
	public boolean performFinish() {
		final ModelTypDef[] changes = this.pageOne.getChanges();
		final boolean useNextIdFromParent = this.pageTwo.getUseNextIdFromParent();
		if (changes == null) {
			return true;
		}
		ProgressMonitorDialog progressDilog = new ProgressMonitorDialog(getShell());
		try {
			progressDilog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.modelchange.update")
									+ "...",
							IProgressMonitor.UNKNOWN);
					try {
						NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
						final Set<ExpandedNodeId> parentIds = new HashSet<>();
						// find change in model
						Map<ExpandedNodeId, AddNodesResult[]> modelChangeNodes = new HashMap<>();
						Map<ExpandedNodeId, ExpandedNodeId> createdNodesMapping = new HashMap<>();
						for (ModelTypDef updateDef : ((ModelTypDef[]) changes)) {
							updateDef.buildModelTree();
							typeDef.doCompareModelCreate(parentIds, updateDef, modelChangeNodes, createdNodesMapping,
									useNextIdFromParent);
							monitor.worked(1);
						}
						// mapping
						Map<ExpandedNodeId, ExpandedNodeId> newMapping = new HashMap<>();
						for (ModelTypDef updateDef : ((ModelTypDef[]) changes)) {
							ExpandedNodeId id = updateDef.nodeId;
							AddNodesResult[] results = modelChangeNodes.get(id);
							if (results == null) {
								continue;
							}
							NodeId typeDefId = typeDef.typeId;
							ExpandedNodeId expTdId = new ExpandedNodeId(nsTable.getUri(typeDefId.getNamespaceIndex()),
									typeDefId.getValue(), nsTable);
							// ExpandedNodeId expTdId =
							// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							// .toExpandedNodeId(typeDefId);
							ExpandedNodeId parentOfNodeToMap = getTypeToMap(expTdId, id);
							if (parentOfNodeToMap == null) {
								Logger.getLogger(getClass().getName()).log(Level.INFO,
										"No modelparent for " + expTdId.toString());
								continue;
							}
							List<ExpandedNodeId> nodeIdsToMap = new ArrayList<>();
							for (AddNodesResult item : results) {
								nodeIdsToMap.add(
										new ExpandedNodeId(nsTable.getUri(item.getAddedNodeId().getNamespaceIndex()),
												item.getAddedNodeId().getValue(), nsTable));
								// nodeIdsToMap.add(ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								// .toExpandedNodeId(item.getAddedNodeId()));
							}
							discoverCreateChangesMapping(createdNodesMapping, parentOfNodeToMap, id, nodeIdsToMap,
									newMapping);
						}
						for (Entry<ExpandedNodeId, ExpandedNodeId> entry : newMapping.entrySet()) {
							ExpandedNodeId k = entry.getKey();
							ExpandedNodeId v = entry.getValue();
							ServerInstance.getTypeModel().addModelMapping(k, v);
						}
						monitor.done();
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								DesignerUtils.refresh(parentIds);
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
		return true;
	}

	private void discoverCreateChangesMapping(Map<ExpandedNodeId, ExpandedNodeId> actionMapping,
			ExpandedNodeId parentIdOfNodeToMap, ExpandedNodeId parentNodeToChange, List<ExpandedNodeId> nodeIdsToMap,
			Map<ExpandedNodeId, ExpandedNodeId> newMapping) {
		try {
			ReferenceDescription[] children = browse(parentNodeToChange);
			for (ReferenceDescription child : children) {
				if (nodeIdsToMap.contains(child.getNodeId())) {
					ExpandedNodeId found = child.getNodeId();
					ExpandedNodeId discoverdMapping = discoverCreateMapping(actionMapping, found, parentIdOfNodeToMap);
					if (discoverdMapping != null) {
						newMapping.put(found, discoverdMapping);
						discoverCreateChangesMapping(actionMapping, discoverdMapping, found, nodeIdsToMap, newMapping);
					}
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

	private ExpandedNodeId discoverCreateMapping(Map<ExpandedNodeId, ExpandedNodeId> actionMapping,
			ExpandedNodeId found, ExpandedNodeId parentIdOfNodeToMap) throws ServiceResultException {
		ExpandedNodeId a2m = actionMapping.get(found);
		// TODO: There is no mapping for the type (maybe subtype is not
		// synchronized with model)
		if (a2m == null) {
			// TODO: LOG
			return null;
		}
		ReferenceDescription[] mappingNodes = browse(parentIdOfNodeToMap);
		for (ReferenceDescription mappingChildren : mappingNodes) {
			ExpandedNodeId mChildId = mappingChildren.getNodeId();
			ExpandedNodeId mId = ServerInstance.getTypeModel().getTypeIdFromObject(mChildId);
			if (mId == null) {
				mId = actionMapping.get(mChildId);
			}
			if (a2m.equals(mId)) {
				return mChildId;
			}
		}
		ExpandedNodeId mappingId = null;
		// nothing found look in parent
		ReferenceDescription[] parentTypes = browseSupertypes(parentIdOfNodeToMap);
		if (parentTypes != null) {
			for (ReferenceDescription parentId : parentTypes) {
				mappingId = discoverCreateMapping(actionMapping, found, parentId.getNodeId());
				if (mappingId != null) {
					break;
				}
			}
		}
		return mappingId;
	}

	private ExpandedNodeId getTypeToMap(ExpandedNodeId typeDefId, ExpandedNodeId nodeIdToChange) {
		List<ExpandedNodeId> tdId = ServerInstance.getTypeModel().getObjectsFromType(typeDefId);
		// is okay
		ExpandedNodeId typeToMap = null;
		if (tdId != null && tdId.contains(nodeIdToChange)) {
			// do mapping
			typeToMap = typeDefId;
		} else {
			if (tdId == null) {
				ReferenceDescription[] subtypes = browseSubtypes(typeDefId);
				if (subtypes != null) {
					for (ReferenceDescription subtype : subtypes) {
						typeToMap = getTypeToMap(subtype.getNodeId(), nodeIdToChange);
						if (typeToMap != null) {
							break;
						}
					}
				}
			} else {
				for (ExpandedNodeId relatedNodesToType : tdId) {
					typeToMap = getTypeToMap(relatedNodesToType, nodeIdToChange);
					if (typeToMap != null) {
						break;
					}
				}
			}
		}
		return typeToMap;
	}

	protected ExpandedNodeId findNewMapping(List<ExpandedNodeId> objects, ExpandedNodeId expNodeId,
			List<ExpandedNodeId> relatedNodes) {
		ExpandedNodeId obj2 = null;
		for (ExpandedNodeId obj : objects) {
			if (!expNodeId.equals(obj) && relatedNodes.contains(obj)) {
				obj2 = obj;
				break;
			}
		}
		if (obj2 != null) {
			return obj2;
		}
		for (ExpandedNodeId obj : objects) {
			ExpandedNodeId t = ServerInstance.getTypeModel().getTypeIdFromObject(obj);
			if (t == null) {
				continue;
			}
			List<ExpandedNodeId> objects2 = ServerInstance.getTypeModel().getObjectsFromType(t);
			if (objects2.contains(t)) {
				return t;
			}
			// obj2 = findNewMapping(objs, expNodeId, relatedNodes);
			// if (obj2 != null) {
			// break;
			// }
		}
		return null;
	}

	public void setInput(ModelTypDef[] input) {
		this.input = input;
	}

	public void setTypeDef(ModelTypDef ptd) {
		this.typeDef = ptd;
	}

	// forward and hierachical
	private ReferenceDescription[] browse(ExpandedNodeId parentId) throws ServiceResultException {
		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		BrowseDescription nodeToBrowse = new BrowseDescription();
		nodesToBrowse[0] = nodeToBrowse;
		nodeToBrowse.setBrowseDirection(BrowseDirection.Forward);
		nodeToBrowse.setIncludeSubtypes(true);
		nodeToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method));
		nodeToBrowse.setNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(parentId));
		nodeToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
		nodeToBrowse.setResultMask(BrowseResultMask.ALL);
		BrowseResult[] result = ServerInstance.browse(
				ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(parentId),
				Identifiers.HierarchicalReferences,
				NodeClass.getSet(NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method)),
				BrowseResultMask.ALL, BrowseDirection.Forward, true);
		if (result != null && result.length > 0) {
			if (result.length > 0) {
				return result[0].getReferences();
			}
		}
		return null;
	}

	private ReferenceDescription[] browseSupertypes(ExpandedNodeId typeDefId) {
		try {
			BrowseResult[] result = ServerInstance.browse(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(typeDefId),
					Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.ObjectType, NodeClass.VariableType,
							NodeClass.ReferenceType, NodeClass.DataType)),
					BrowseResultMask.ALL, BrowseDirection.Inverse, true);
			if (result != null && result.length > 0) {
				if (result.length > 0) {
					return result[0].getReferences();
				}
			}
		} catch (ServiceResultException sre) {
			sre.printStackTrace();
		}
		return null;
	}

	private ReferenceDescription[] browseSubtypes(ExpandedNodeId typeDefId) {
		try {
			BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
			BrowseDescription nodeToBrowse = new BrowseDescription();
			nodesToBrowse[0] = nodeToBrowse;
			nodeToBrowse.setBrowseDirection(BrowseDirection.Forward);
			nodeToBrowse.setIncludeSubtypes(true);
			nodeToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.ObjectType, NodeClass.VariableType,
					NodeClass.ReferenceType, NodeClass.DataType));
			nodeToBrowse
					.setNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(typeDefId));
			nodeToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
			nodeToBrowse.setResultMask(BrowseResultMask.ALL);
			BrowseResult[] result = ServerInstance.browse(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(typeDefId),
					Identifiers.HierarchicalReferences,
					NodeClass.getSet(NodeClass.getMask(NodeClass.ObjectType, NodeClass.VariableType,
							NodeClass.ReferenceType, NodeClass.DataType)),
					BrowseResultMask.ALL, BrowseDirection.Forward, true);
			if (result != null && result.length > 0) {
				if (result.length > 0) {
					return result[0].getReferences();
				}
			}
		} catch (ServiceResultException sre) {
			sre.printStackTrace();
		}
		return null;
	}
}
