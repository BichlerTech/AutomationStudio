package com.bichler.astudio.editor.siemens.wizard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.SiemensDPEditor;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.dialog.DialogResults;
import com.bichler.astudio.editor.siemens.dialog.SiemensModelImportDialog;
import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.model.SiemensDataValueFactory;
import com.bichler.astudio.editor.siemens.model.SiemensNamespaceItem;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensDBWizardPage;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensImportTypeWizardPage;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensImportWizardPage2;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensNamespacePage;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensUDTWizardPage;
import com.bichler.astudio.editor.siemens.xml.SIEMENS_MAPPING_TYPE;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.server.core.OPCInternalServer;

public class SiemensImportWizard2 extends Wizard {
	private IFileSystem filesystem;
	private SiemensImportWizardPage2 importPage;
	private SiemensDBResourceManager structManager;
	private SiemensImportTypeWizardPage importTypePage;
	private SiemensDBWizardPage dbPage;
	private SiemensUDTWizardPage udtPage;
	private SiemensNamespacePage namespacePage;

	public SiemensImportWizard2(SiemensDBResourceManager structManager) {
		setWindowTitle(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.title"));
		this.structManager = structManager;
	}

	@Override
	public void addPages() {
		this.importTypePage = new SiemensImportTypeWizardPage(this.filesystem, this.structManager);
		this.udtPage = new SiemensUDTWizardPage(this.filesystem, this.structManager);
		this.dbPage = new SiemensDBWizardPage(this.filesystem, this.structManager);
		this.importPage = new SiemensImportWizardPage2(this.filesystem, this.structManager);
		this.namespacePage = new SiemensNamespacePage();
		this.udtPage.setImportTypePage(this.importTypePage);
		this.dbPage.setImportPage(this.importPage);
		this.dbPage.setImportTypePage(this.importTypePage);
		this.dbPage.setNamespacePage(this.namespacePage);
		this.importTypePage.setImportPage(this.importPage);
		addPage(this.importTypePage);
		addPage(this.udtPage);
		addPage(this.dbPage);
		addPage(this.importPage);
		addPage(this.namespacePage);
	}

	@Override
	public boolean performFinish() {
		TableViewer tv = null;
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (activeEditor instanceof SiemensDPEditor) {
			tv = ((SiemensDPEditor) activeEditor).getTableViewer();
		} else {
			System.out.println("wrong active data point editor!");
			return false;
		}
		AbstractSiemensNode input = this.importPage.getInput();
		Map<String, AbstractSiemensNode> mappingObjects = new HashMap<>();
		List<AbstractSiemensNode> activated = new ArrayList<>();
		AbstractSiemensNode root = input.fillActiveAll(activated, mappingObjects);
		String symbAddrName = root.getName();
		// String symbolAddressName = "";
		String symbolAddress = "";
		if (symbAddrName != null) {
			// symbolAddressName = SiemensAreaCode.DB.name();
			symbolAddress = symbAddrName.replaceFirst(SiemensAreaCode.DB.name(), "").trim();
		}
		activated.remove(root);
		/**
		 * NAMESPACE
		 */
		SiemensNamespaceItem namespaceItem = this.namespacePage.getNamespaceItem();
		if (namespaceItem != null && !namespaceItem.isServerEntry()) {
			// Add new namespace to the server`s namespacetable
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			nsTable.add(namespaceItem.getNamespace());
			// set dirty for the OPC UA information model
			ModelBrowserView mbv = (ModelBrowserView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView(ModelBrowserView.ID);
			mbv.setDirty(true);
		}
		// create siemens entry model (list)
		Map<String, SiemensEntryModelNode> mapping = new HashMap<>();
		List<SiemensEntryModelNode> model = new ArrayList<>();
		SiemensEntryModelNode last = null;
		for (AbstractSiemensNode item : activated) {
			SiemensEntryModelNode semn = new SiemensEntryModelNode();
			// semn.setId(++maxNodeId);
			float i = item.getAddressIndex();
			// float i2 = item.getIndex();
			// semn.setNEW_INDEX(item.getIndex());
			semn.setIndex(i);
			semn.setActive(true);
			// semn.setAddress(item.getAddress());
			semn.setAddressType(SiemensAreaCode.DB);
			semn.setAddress(symbolAddress);
			String dtype = item.getDataType();
			semn.setDataType(dtype);
			// if (item instanceof SiemensArrayNode) {
			// int dim = ((SiemensArrayNode) item).getDimension();
			// semn.setParsedDataType(SiemensModelParser.DATATYPE_ARRAY + "["
			// + dim + "]");
			// }
			semn.setDescription(item.getDescription());
			semn.setSymbolName(item.getSymbolName());
			semn.setLabelImage(item.getLabelImage());
			model.add(semn);
			semn.setNeighbor(last);
			last = semn;
			mapping.put(item.getSymbolName(), semn);
		}
		List<SiemensEntryModelNode> oldModel = (List<SiemensEntryModelNode>) tv.getInput();
		// replace old model
		boolean yes = true;
		if (oldModel != null && !oldModel.isEmpty()) {
			SiemensModelImportDialog dialog = new SiemensModelImportDialog(getShell());
			int result = dialog.open();
			if (Dialog.OK == result) {
				DialogResults dr = dialog.getResult();
				switch (dr) {
				case OK:
					yes = true;
					break;
				case Append:
					yes = false;
					break;
				default:
					break;
				}
			}
			// exit
			else {
				return true;
			}
		}
		// generate opc ua model if needed
		boolean isGenModel = this.importPage.isGenModel();
		if (isGenModel) {
			ExportType type = this.importTypePage.getType();
			boolean genSymbolnameId = this.namespacePage.isGenerateSymbolnameId();
			String objectname = this.namespacePage.getStartNodeName();
			String namespace = this.namespacePage.getNamespaceItem().getNamespace();
			NodeId parentId = this.namespacePage.getStartNodeId();
			String db = input.getAddress();
			SiemensDataTransformator sdt = new SiemensDataTransformator(genSymbolnameId, objectname, namespace,
					parentId, mapping, mappingObjects);
			AddNodesItem[] nodes2add = sdt.generate2(model, input);
			// switch (type) {
			// case Tia:
			try {
				Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<ExpandedNodeId, AddNodesItem>();
				for (AddNodesItem item : nodes2add) {
					mappedNodes.put(item.getRequestedNewNodeId(), item);
				}
				// add nodes to server
				ServerInstance.getInstance().getServerInstance().getMaster().addNodes(nodes2add, mappedNodes, false,
						null, false);
				for (Entry<String, SiemensEntryModelNode> m : sdt.mapping.entrySet()) {
					NodeId nodeId = sdt.mapping2.get(m.getKey());
					SiemensEntryModelNode node = m.getValue();
					// set rootid
					node.setRootId(
							ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(sdt.rootId));
					// set nodeid
					node.setDisplayname(m.getKey());
					// set opc ua model id
					node.setNodeId(nodeId);
					// get browsepath
					Deque<BrowsePathElement> browsepathelems = OPCUABrowseUtils.getFullBrowsePath(nodeId,
							ServerInstance.getInstance().getServerInstance(), Identifiers.RootFolder);
					String browsepath = "";
					// start browsepath from roots folder
					for (BrowsePathElement element : browsepathelems) {
						if (element.getId().equals(Identifiers.RootFolder)) {
							continue;
						}
						browsepath += "/" + element.getBrowsename().getName();
					}
					// set browsepath
					node.setBrowsepath(browsepath);
					node.setAddress(db);
					if (node instanceof SiemensEntryModelNode) {
						String datatype = node.getDataType();
						boolean isVariablePrimitiveArray = SiemensDataTypePattern.matchVariablePrimitiveArray(datatype);
						// change mapping of array
						if (isVariablePrimitiveArray) {
							// check datatype for array
							SIEMENS_MAPPING_TYPE datatypemapping = node.getMapping();
							node.setMapping(SIEMENS_MAPPING_TYPE.ARRAY_ARRAY);
							String datatypeTransformed = SiemensDataTypePattern.getVariableDatatypeFromArray(datatype);
							// change datatype
							node.setDataType(datatypeTransformed);
							// node.setDataType(dataType);
						}
					}
				}
				// remove unnecessary model elements
				for (Entry<String, AbstractSiemensNode> node : sdt.elements2skip4datapointmapping.entrySet()) {
					AbstractSiemensNode v = node.getValue();
					// if (!v.isActive()) {
					SiemensEntryModelNode value = sdt.mapping.get(node.getKey());
					model.remove(value);
					// }
				}
				// change value mapping from array and datatype
				// refresh opc ua model viewer
				ModelBrowserView mbv = (ModelBrowserView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView(ModelBrowserView.ID);
				mbv.setDirty(true);
				mbv.refresh(parentId);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			// rootmodel
			// break;
			// default:
			// break;
			// }
		}
		if (!yes) {
			oldModel.addAll(model);
		} else {
			tv.setInput(model);
		}
		tv.refresh(true);
		return true;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage nextPage = super.getNextPage(page);
		if (nextPage instanceof SiemensNamespacePage) {
			boolean isGenModel = this.importPage.isGenModel();
			if (!isGenModel) {
				return page;
			}
		}
		return nextPage;
	}

	@Override
	public boolean canFinish() {
		boolean finish = super.canFinish();
		IWizardPage page = getContainer().getCurrentPage();
		if (page instanceof SiemensImportWizardPage2) {
			boolean isGenModel = this.importPage.isGenModel();
			if (isGenModel) {
				return false;
			} else {
				return true;
			}
		}
		return finish;
	}

	class SiemensDataTransformator {
		private boolean useSymbolId;
		private String objectname;
		private String namespace;
		private NodeId parentId;
		private ExpandedNodeId rootId;
		// mapping of target siemens entrys
		private Map<String, SiemensEntryModelNode> mapping;
		// mapping for siemens object entrys to create children
		private Map<String, AbstractSiemensNode> mappingObjects;
		// mapping of opc ua nodes to siemens entrys
		private Map<String, NodeId> mapping2 = new HashMap<>();
		// elements to remove after mapping
		private Map<String, AbstractSiemensNode> elements2skip4datapointmapping = new HashMap<>();

		public SiemensDataTransformator(boolean genSymbolnameId, String objectname, String namespace, NodeId parentId,
				Map<String, SiemensEntryModelNode> mapping, Map<String, AbstractSiemensNode> mappingObjects) {
			this.useSymbolId = genSymbolnameId;
			this.objectname = objectname;
			this.namespace = namespace;
			this.parentId = parentId;
			this.mapping = mapping;
			this.mappingObjects = mappingObjects;
		}

		public AddNodesItem[] generate2(List<SiemensEntryModelNode> model, AbstractSiemensNode input) {
			List<AddNodesItem> nodes2add = new ArrayList<>();

			this.rootId = initializeDBHeader(nodes2add, input);
			// create opc ua nodes
			AbstractSiemensNode[] children = input.getChildren();
			for (AbstractSiemensNode child : children) {
				doGen(nodes2add, child, rootId);
			}
			return nodes2add.toArray(new AddNodesItem[0]);
		}

		private void doGen(List<AddNodesItem> nodes2add, AbstractSiemensNode node, ExpandedNodeId parentId) {
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			NodeId nodeId = nextNodeId(node);
			SiemensEntryModelNode value1 = this.mapping.get(node.getSymbolName());
			AbstractSiemensNode value2 = this.mappingObjects.get(node.getSymbolName());
			// skip items which are not activated
			if (value1 == null && value2 == null) {
				AbstractSiemensNode[] children = node.getChildren();
				for (AbstractSiemensNode child : children) {
					doGen(nodes2add, child, parentId);
				}
			} else {
				String datatype = node.getDataType();
				boolean primitiveVariable = SiemensDataTypePattern.matchVariablePrimitive(datatype);// datatype.matches(patternVariable);
				boolean primitiveVariableArray = SiemensDataTypePattern.matchVariablePrimitiveArray(datatype);// .matches(patternArrayVariable);
				// match array types (e.g.: BYTE[10])
				boolean primitiveVariableArray2 = SiemensDataTypePattern.matchVariablePrimitiveArray2(datatype);
				if (primitiveVariable) {
					// datatype
					SiemensDataValueFactory type = SiemensDataValueFactory.createDatatype(datatype);
					// create opc node
					genVariablePrimitive(nodes2add, node,
							new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(), nsTable),
							parentId, Identifiers.HasComponent, type,
							new ExpandedNodeId(nsTable.getUri(Identifiers.BaseDataVariableType.getNamespaceIndex()),
									Identifiers.BaseDataVariableType.getValue(), nsTable));
					// genVariablePrimitive(nodes2add, node,
					// nsTable.toExpandedNodeId(nodeId), parentId,
					// Identifiers.HasComponent, type,
					// nsTable.toExpandedNodeId(Identifiers.BaseDataVariableType));
					// work on children
					AbstractSiemensNode[] children = node.getChildren();
					for (AbstractSiemensNode child : children) {
						doGen(nodes2add, child, new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()),
								nodeId.getValue(), nsTable));
						// doGen(nodes2add, child, nsTable.toExpandedNodeId(nodeId),
						// nodeIds);
					}
				} else if (primitiveVariableArray) {
					String T = datatype.split(" ")[3];
					SiemensDataValueFactory type = SiemensDataValueFactory.createDatatype(T);
					// create opc node
					AbstractSiemensNode[] children = node.getChildren();
					// genVariableArrayPrimitive(nodes2add, node,
					// nsTable.toExpandedNodeId(nodeId), parentId, children,
					// Identifiers.HasComponent, type,
					// nsTable.toExpandedNodeId(Identifiers.BaseDataVariableType));
					genVariableArrayPrimitive(nodes2add, node,
							new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(), nsTable),
							parentId, children, Identifiers.HasComponent, type,
							new ExpandedNodeId(nsTable.getUri(Identifiers.BaseDataVariableType.getNamespaceIndex()),
									Identifiers.BaseDataVariableType.getValue(), nsTable));
				} else if (primitiveVariableArray2) {
					int index = datatype.indexOf("[");
					String T = datatype.substring(0, index);
					// String T = datatype.split(" ")[3];
					SiemensDataValueFactory type = SiemensDataValueFactory.createDatatype(T);
					// create opc node
					AbstractSiemensNode[] children = node.getChildren();
					genVariableArrayPrimitive(nodes2add, node,
							new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(), nsTable),
							parentId, children, Identifiers.HasComponent, type,
							new ExpandedNodeId(nsTable.getUri(Identifiers.BaseDataVariableType.getNamespaceIndex()),
									Identifiers.BaseDataVariableType.getValue(), nsTable));
					// genVariableArrayPrimitive(nodes2add, node,
					// nsTable.toExpandedNodeId(nodeId), parentId, children,
					// Identifiers.HasComponent, type,
					// nsTable.toExpandedNodeId(Identifiers.BaseDataVariableType));
				} else {
					genObject(nodes2add, node,
							new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(), nsTable),
							parentId, Identifiers.Organizes,
							new ExpandedNodeId(nsTable.getUri(Identifiers.BaseObjectType.getNamespaceIndex()),
									Identifiers.BaseObjectType.getValue(), nsTable));
					// genObject(nodes2add, node, nsTable.toExpandedNodeId(nodeId),
					// parentId, Identifiers.Organizes,
					// nsTable.toExpandedNodeId(Identifiers.BaseObjectType));
					// work on children
					AbstractSiemensNode[] children = node.getChildren();
					for (AbstractSiemensNode child : children) {
						doGen(nodes2add, child, new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()),
								nodeId.getValue(), nsTable));
						// doGen(nodes2add, child, nsTable.toExpandedNodeId(nodeId),
						// nodeIds);
					}
				}
			}
		}

		private ExpandedNodeId initializeDBHeader(List<AddNodesItem> nodes2add, AbstractSiemensNode node) {

			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

			node.setSymbolName(this.objectname);
			NodeId rootId = nextNodeId(node);
			genObject(nodes2add, node,
					new ExpandedNodeId(nsTable.getUri(rootId.getNamespaceIndex()), rootId.getValue(), nsTable),
					new ExpandedNodeId(nsTable.getUri(this.parentId.getNamespaceIndex()), this.parentId.getValue(),
							nsTable),
					Identifiers.Organizes,
					new ExpandedNodeId(nsTable.getUri(Identifiers.BaseObjectType.getNamespaceIndex()),
							Identifiers.BaseObjectType.getValue(), nsTable));
			// genObject(nodes2add, node, nsTable.toExpandedNodeId(rootId),
			// nsTable.toExpandedNodeId(this.parentId),
			// Identifiers.Organizes,
			// nsTable.toExpandedNodeId(Identifiers.BaseObjectType));
			return new ExpandedNodeId(nsTable.getUri(rootId.getNamespaceIndex()), rootId.getValue(), nsTable);
			// return nsTable.toExpandedNodeId(rootId);
		}

		private void genObject(List<AddNodesItem> nodes2add, AbstractSiemensNode node, ExpandedNodeId newId,
				ExpandedNodeId parentId, NodeId refTypeId, ExpandedNodeId typeDef) {
			try {
				AddNodesItem node2add = new AddNodesItem();
				node2add.setBrowseName(new QualifiedName(node.getSymbolName()));
				node2add.setNodeClass(NodeClass.Object);
				node2add.setParentNodeId(parentId);
				node2add.setReferenceTypeId(refTypeId);
				node2add.setRequestedNewNodeId(newId);
				node2add.setTypeDefinition(typeDef);
				ObjectAttributes oa = new ObjectAttributes();
				oa.setDescription(LocalizedText.EMPTY);
				oa.setDisplayName(new LocalizedText(node.getSymbolName()));
				oa.setEventNotifier(UnsignedByte.ZERO);
				oa.setUserWriteMask(UnsignedInteger.ZERO);
				oa.setWriteMask(UnsignedInteger.ZERO);
				node2add.setNodeAttributes(ExtensionObject.binaryEncode(oa, EncoderContext.getDefaultInstance()));
				nodes2add.add(node2add);
				// this.mapping2.put(node.getSymbolName(),
				// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(newId));
				this.elements2skip4datapointmapping.put(node.getSymbolName(), node);
			} catch (EncodingException e) {
				e.printStackTrace();
			}
		}

		private void genVariablePrimitive(List<AddNodesItem> nodes2add, AbstractSiemensNode node, ExpandedNodeId newId,
				ExpandedNodeId parentId, NodeId refTypeId, SiemensDataValueFactory datatype, ExpandedNodeId typeDef) {
			// node attributes
			try {
				AddNodesItem node2add = new AddNodesItem();
				node2add.setBrowseName(new QualifiedName(node.getSymbolName()));
				node2add.setNodeClass(NodeClass.Variable);
				node2add.setParentNodeId(parentId);
				node2add.setReferenceTypeId(refTypeId);
				node2add.setRequestedNewNodeId(newId);
				node2add.setTypeDefinition(typeDef);
				VariableAttributes va = new VariableAttributes();
				va.setDescription(LocalizedText.EMPTY);
				va.setDisplayName(new LocalizedText(node.getSymbolName()));
				va.setUserWriteMask(UnsignedInteger.ZERO);
				va.setWriteMask(UnsignedInteger.ZERO);
				va.setAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
				va.setArrayDimensions(new UnsignedInteger[0]);
				va.setDataType(datatype.getDatatypeId());
				va.setHistorizing(false);
				va.setMinimumSamplingInterval(1000.0);
				va.setUserAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
				va.setUserWriteMask(UnsignedInteger.ZERO);
				va.setValueRank(ValueRanks.Scalar.getValue());
				// data value
				DataValue dv = new DataValue(Variant.NULL);
				dv = SiemensDataValueFactory.createDataValue(datatype.getClassname(), datatype.getDefaultValue());
				va.setValue(dv.getValue());
				node2add.setNodeAttributes(ExtensionObject.binaryEncode(va, EncoderContext.getDefaultInstance()));
				nodes2add.add(node2add);
				this.mapping2.put(node.getSymbolName(),
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(newId));
			} catch (EncodingException e) {
				e.printStackTrace();
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}

		private void genVariableArrayPrimitive(List<AddNodesItem> nodes2add, AbstractSiemensNode node,
				ExpandedNodeId newId, ExpandedNodeId parentId, AbstractSiemensNode[] children, NodeId refTypeId,
				SiemensDataValueFactory datatype, ExpandedNodeId typeDef) {
			// node
			try {
				AddNodesItem node2add = new AddNodesItem();
				node2add.setBrowseName(new QualifiedName(node.getSymbolName()));
				node2add.setNodeClass(NodeClass.Variable);
				node2add.setParentNodeId(parentId);
				node2add.setReferenceTypeId(refTypeId);
				node2add.setRequestedNewNodeId(newId);
				node2add.setTypeDefinition(typeDef);
				VariableAttributes va = new VariableAttributes();
				va.setDescription(LocalizedText.EMPTY);
				va.setDisplayName(new LocalizedText(node.getSymbolName()));
				va.setUserWriteMask(UnsignedInteger.ZERO);
				va.setWriteMask(UnsignedInteger.ZERO);
				va.setAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
				va.setDataType(datatype.getDatatypeId());
				va.setHistorizing(false);
				va.setMinimumSamplingInterval(1000.0);
				va.setUserAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
				va.setUserWriteMask(UnsignedInteger.ZERO);

				this.mapping2.put(node.getSymbolName(),
						ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(newId));

				DataValue array = new DataValue(Variant.NULL);
				try {
					Class<?> cfn = Class.forName(datatype.getClassname());
					Object array2use = Array.newInstance(cfn, children.length);
					int length = Array.getLength(array2use);
					for (int i = 0; i < length; i++) {
						AbstractSiemensNode child = children[i];
						DataValue dv = SiemensDataValueFactory.createDataValue(datatype.getClassname(),
								datatype.getDefaultValue());
						Object value = dv.getValue().getValue();
						((Object[]) array2use)[i] = value;
						this.elements2skip4datapointmapping.put(child.getSymbolName(), child);
					}
					array = new DataValue(new Variant(array2use));
					va.setValue(array.getValue());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				// array dimensions
				int dim = array.getValue().getDimension();
				UnsignedInteger[] arraydimensions = new UnsignedInteger[dim];
				for (int i = 0; i < dim; i++) {
					arraydimensions[i] = UnsignedInteger.ZERO;
				}
				va.setArrayDimensions(new UnsignedInteger[0]);
				// value rank
				int[] adim = array.getValue().getArrayDimensions();
				ValueRanks valueRank = ValueRanks.getValueRanks(adim.length);
				va.setValueRank(valueRank.getValue());
				node2add.setNodeAttributes(ExtensionObject.binaryEncode(va, EncoderContext.getDefaultInstance()));
				nodes2add.add(node2add);
			} catch (EncodingException e) {
				e.printStackTrace();
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}

		private NodeId nextNodeId(AbstractSiemensNode node) {
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			NodeId newId = null;
			// check namespace index
			int index = nsTable.getIndex(this.namespace);
			if (index < 0) {
				throw new IllegalArgumentException("Index cannot be used!");
			}
			// nodeid from its symbolname
			if (this.useSymbolId) {
				String symbolname = node.getSymbolName();
				newId = new NodeId(index, symbolname);
			}
			// nodeid from a number
			else {
				OPCInternalServer opcServer = ServerInstance.getInstance().getServerInstance();
				newId = opcServer.getAddressSpaceManager().getNodeFactory().getNextNodeId(index, UnsignedInteger.ZERO,
						IdType.Numeric, NodeIdMode.APPEND);
			}

			return newId;
		}
	}

	static class SiemensDataTypePattern {
		private static final String pattern1 = "bool|byte|int|dint|word|dword|real|string|date_and_time|";
		private static final String pattern2 = "Bool|Byte|Int|DInt|Word|DWord|Real|String|Date_and_Time|";
		private static final String pattern3 = "BOOL|BYTE|INT|DINT|WORD|DWORD|REAL|STRING|DATE_AND_TIME";
		private static final String patternVariable = pattern1 + pattern2 + pattern3;
		private static final String patternArrayVariable = "Array \\[([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})\\]\\ of\\ ("
				+ patternVariable + ")";
		// ([0-9][0-9]{0,})\\.\\.([1-9][0-9]{0,})
		private static final String pav2 = "(" + patternVariable + ")\\[(\\d+)\\]";

		public static boolean matchVariablePrimitive(String datatype) {
			return datatype.matches(patternVariable);
		}

		public static String getVariableDatatypeFromArray(String datatype) {
			int length = SiemensDataTypePattern.getVariablePrimitiveArrayLength(datatype);
			String primitiveType = datatype;
			// get array
			String[] T = primitiveType.split(" ");
			primitiveType = T[T.length - 1];
			return primitiveType + "[" + length + "]";
		}

		public static boolean matchVariablePrimitiveArray(String datatype) {
			return datatype.matches(patternArrayVariable);
		}

		public static boolean matchVariablePrimitiveArray2(String datatype) {
			return datatype.matches(pav2);
		}

		private static int getVariablePrimitiveArrayLength(String datatype) {
			String elecnt = datatype.toUpperCase().replace("ARRAY [", "");
			elecnt = elecnt.substring(0, elecnt.indexOf("]"));
			String[] elements = elecnt.replace(" ", "").split("\\.\\.");
			// int length = Integer.parseInt(elements[1]) -
			// Integer.parseInt(elements[0]); // "${BASH_REMATCH[2]}"
			int begin = Integer.parseInt(elements[0]);
			int end = Integer.parseInt(elements[1]);
			return end - begin + 1;
		}
	}
}
