package com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.change;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.ServerTypeModel;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;

public class ModelChangeElementPage extends WizardPage {
	private Image imgRefType;
	private Image imgObj;
	private Image imgVar;
	private Image imgMethod;
	private Image imgObjType;
	private Image imgVarType;
	private Image imgDatatype;
	private Image imgView;
	private TableViewer tableViewer;
	private ModelTypDef[] input;
	private Image imgInvalidMapping;
	private Image imgValidMapping;

	/**
	 * Create the wizard.
	 */
	public ModelChangeElementPage() {
		super("ModelChangeElementPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.modelchange.title"));
		setDescription(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.modelchange.description"));
		this.imgRefType = Activator.getImageDescriptor("icons/browser_icons/referencetype_16.png").createImage();
		this.imgObj = Activator.getImageDescriptor("icons/browser_icons/object_16.png").createImage();
		this.imgObjType = Activator.getImageDescriptor("icons/browser_icons/objecttype_16.png").createImage();
		this.imgVar = Activator.getImageDescriptor("icons/browser_icons/variable_16.png").createImage();
		this.imgVarType = Activator.getImageDescriptor("icons/browser_icons/variabletype_16.png").createImage();
		this.imgMethod = Activator.getImageDescriptor("icons/browser_icons/method_16.png").createImage();
		this.imgDatatype = Activator.getImageDescriptor("icons/browser_icons/datatype_16.png").createImage();
		this.imgView = Activator.getImageDescriptor("icons/browser_icons/view_16.png").createImage();
		this.imgInvalidMapping = Activator.getImageDescriptor("icons/default_icons/delete_16.png").createImage();
		this.imgValidMapping = Activator.getImageDescriptor("icons/default_icons/run_16.png").createImage();
	}

	enum MCTableItems {
		State, Klasse, Name, NodeId;
		public static MCTableItems valueOf(int i) {
			switch (i) {
			case 0:
				return State;
			case 1:
				return Klasse;
			case 2:
				return Name;
			case 3:
				return NodeId;
			default:
				return null;
			}
		}
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		this.tableViewer.setContentProvider(new MCEContentProvider());
		// this.tableViewer.setLabelProvider(new MCELabelProvider());
//    Table table = tableViewer.getTable();
		TableViewerColumn tableViewerColumnInfo = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNameInfo = tableViewerColumnInfo.getColumn();
		tblclmnNameInfo.setMoveable(true);
		tblclmnNameInfo.setResizable(true);
		tblclmnNameInfo.setWidth(100);
		tblclmnNameInfo.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.state"));
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setMoveable(true);
		tblclmnName.setResizable(true);
		tblclmnName.setWidth(100);
		tblclmnName.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.class"));
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn_1.getColumn();
		tblclmnNewColumn.setMoveable(true);
		tblclmnNewColumn.setResizable(true);
		tblclmnNewColumn.setWidth(150);
		tblclmnNewColumn.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.name"));
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn_1.setMoveable(true);
		tblclmnNewColumn_1.setResizable(true);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_nodeId.text"));
		tableViewerColumnInfo.setLabelProvider(new ColumnLabelProvider(0));
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(1));
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(2));
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(3));
		setHandler();
		setInput();
		// this.tableViewer.setAllChecked(true);
	}

	private void setHandler() {
	}

	private void setInput() {
		this.tableViewer.setInput(this.input);
	}
	// private Object getInput() {
	// NamespaceTable nsTable = ServerInstance.getInstance()
	// .getServerInstance().getNamespaceUris();
	//
	// ModelTreeDef typeTree = this.typeDef.getTypeTree();
	// // there is a tree
	// List<ExpandedNodeId> typeList = new ArrayList<>();
	// typeList.add(nsTable.toExpandedNodeId(this.typeDef.typeId));
	//
	// rekTyp(typeList, typeTree);
	//
	// List<ExpandedNodeId> preventStackOverflow = new ArrayList<>();
	// List<ModelTypDef> effected = new ArrayList<>();
	//
	// try {
	// /**
	// * find all effected types under objects folder
	// */
	// doInput(nsTable, effected,
	// nsTable.toExpandedNodeId(Identifiers.ObjectsFolder),
	// NodeClass.getMask(NodeClass.Object, NodeClass.Variable,
	// NodeClass.Method), typeList, preventStackOverflow,
	// true);
	//
	// switch (this.typeDef.typeClass) {
	// // object types folder
	// case ObjectType:
	// doInput(nsTable, effected, nsTable
	// .toExpandedNodeId(Identifiers.ObjectTypesFolder),
	// NodeClass.getMask(NodeClass.Object, NodeClass.Variable,
	// NodeClass.Method, NodeClass.ObjectType),
	// typeList, preventStackOverflow, false);
	// break;
	// // variable types folder AND object types folder
	// case VariableType:
	// doInput(nsTable, effected, nsTable
	// .toExpandedNodeId(Identifiers.ObjectTypesFolder),
	// NodeClass.getMask(NodeClass.Object, NodeClass.Variable,
	// NodeClass.Method, NodeClass.ObjectType),
	// typeList, preventStackOverflow, false);
	// doInput(nsTable,
	// effected,
	// nsTable.toExpandedNodeId(Identifiers.VariableTypesFolder),
	// NodeClass.getMask(NodeClass.Variable,
	// NodeClass.VariableType), typeList,
	// preventStackOverflow, false);
	// break;
	// // reftypefolder
	// case ReferenceType:
	// doInput(nsTable,
	// effected,
	// nsTable.toExpandedNodeId(Identifiers.ReferenceTypesFolder),
	// NodeClass.getMask(NodeClass.ReferenceType,
	// NodeClass.Variable), typeList,
	// preventStackOverflow, false);
	// break;
	// // datatype folder
	// case DataType:
	// doInput(nsTable, effected,
	// nsTable.toExpandedNodeId(Identifiers.DataTypesFolder),
	// NodeClass.getMask(NodeClass.DataType,
	// NodeClass.Variable), typeList,
	// preventStackOverflow, false);
	// break;
	// }
	//
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	//
	// return effected.toArray(new ModelTypDef[0]);
	// }

	// private void rekTyp(List<ExpandedNodeId> typeList, ModelTreeDef typeTree)
	// {
	// if (typeTree != null) {
	// for (ModelTreeDef typeModel : typeTree.getChildren()) {
	// typeList.add(typeModel.getNodeId());
	// rekTyp(typeList, typeModel);
	// }
	// }
	// }
	/**
	 * 
	 * @param nsTable
	 * @param effected
	 * @param browse
	 * @param nodeClassMask
	 * @param typesToFind
	 * @param preventStackOverflow
	 * @param mapToType            Map elements under the objects folder to its
	 *                             given types
	 * @throws ServiceResultException
	 */
	// private void doInput(NamespaceTable nsTable, List<ModelTypDef> effected,
	// ExpandedNodeId browse, UnsignedInteger nodeClassMask,
	// List<ExpandedNodeId> typesToFind,
	// List<ExpandedNodeId> preventStackOverflow, boolean mapToType)
	// throws ServiceResultException {
	//
	// BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
	// BrowseDescription description = new BrowseDescription();
	// description.setBrowseDirection(BrowseDirection.Forward);
	// description.setIncludeSubtypes(true);
	// description.setNodeClassMask(nodeClassMask);
	// description.setNodeId(nsTable.toNodeId(browse));
	// description.setReferenceTypeId(Identifiers.HierarchicalReferences);
	// description.setResultMask(BrowseResultMask
	// .getMask(BrowseResultMask.All));
	//
	// nodesToBrowse[0] = description;
	//
	// BrowseResult[] result;
	// try {
	// result = ServerInstance.browse(nsTable.toNodeId(browse),
	// Identifiers.HierarchicalReferences,
	// NodeClass.getSet(nodeClassMask), BrowseResultMask.ALL,
	// BrowseDirection.Forward, true);
	//
	// if (result != null && result.length > 0) {
	// ReferenceDescription[] references = result[0].getReferences();
	//
	// for (ReferenceDescription reference : references) {
	// ExpandedNodeId refTyp = reference.getTypeDefinition();
	// boolean exist = preventStackOverflow.contains(reference
	// .getNodeId());
	// // dont follow existing path
	// if (exist) {
	// continue;
	// }
	// // add found element with given typ
	// if (refTyp != null && typesToFind.contains(refTyp)) {
	// ModelTypDef m = new ModelTypDef();
	// m.name = reference.getDisplayName();
	// m.typeClass = reference.getNodeClass();
	// m.nodeId = reference.getNodeId();
	// m.reference = reference;
	// m.mapToType = mapToType;
	// effected.add(m);
	// }
	// // prevents cycle
	// preventStackOverflow.add(reference.getNodeId());
	// doInput(nsTable, effected, reference.getNodeId(),
	// nodeClassMask, typesToFind, preventStackOverflow,
	// mapToType);
	// }
	// }
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	// }
	class MCEContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			return super.getElements(inputElement);
		}
	}

	class ColumnLabelProvider extends CellLabelProvider {
		private int index = -1;

		protected ColumnLabelProvider(int index) {
			this.index = index;
		}

		@Override
		public void update(ViewerCell cell) {
			ModelTypDef element = (ModelTypDef) cell.getElement();
			switch (MCTableItems.valueOf(this.index)) {
			case State:
				ServerTypeModel typeModel = ServerInstance.getTypeModel();
				ExpandedNodeId type2obj = typeModel.getTypeIdFromObject(element.nodeId);
				if (type2obj != null) {
					cell.setImage(imgValidMapping);
				} else {
					cell.setImage(imgInvalidMapping);
				}
				break;
			case Klasse:
				NodeClass c = ((ModelTypDef) element).typeClass;
				switch (c) {
				case ReferenceType:
					cell.setImage(imgRefType);
					break;
				case Object:
					cell.setImage(imgObj);
					break;
				case Variable:
					cell.setImage(imgVar);
					break;
				case Method:
					cell.setImage(imgMethod);
					break;
				case ObjectType:
					cell.setImage(imgObjType);
					break;
				case VariableType:
					cell.setImage(imgVarType);
					break;
				case DataType:
					cell.setImage(imgDatatype);
					break;
				case View:
					cell.setImage(imgView);
					break;
				default:
					break;
				}
				break;
			case Name:
				cell.setText(((ModelTypDef) element).name.toString());
				break;
			case NodeId:
				cell.setText(((ModelTypDef) element).nodeId.toString());
				break;
			}
		}
	}

	/**
	 * Not Used
	 * 
	 * @author Thomas Zöchbauer
	 * 
	 */
	class MCELabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			switch (MCTableItems.valueOf(columnIndex)) {
			case State:
				break;
			case Klasse:
				NodeClass c = ((ModelTypDef) element).typeClass;
				if (c != null) {
					System.out.println();
				}
				return null;
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch (MCTableItems.valueOf(columnIndex)) {
			case Name:
				return ((ModelTypDef) element).name.toString();
			case NodeId:
				return ((ModelTypDef) element).nodeId.toString();
			case Klasse:
				break;
			case State:
				break;
			default:
				break;
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
		}
	}

	public void setInput(ModelTypDef[] input) {
		this.input = input;
	}

	public void setTypeDef(ModelTypDef typeDef) {
	}

	public ModelTypDef[] getChanges() {
		ModelTypDef[] input = (ModelTypDef[]) this.tableViewer.getInput();
		return input;
		// List<ModelTypDef> sorted = new ArrayList<>();
		//
		// for (ModelTypDef i : input) {
		// ExpandedNodeId parentId = i.nodeId;
		//
		// Node node = ServerInstance.getInstance().getServerInstance()
		// .getMasterNodeManager().getAddressSpaceManager()
		// .getNode(parentId);
		//
		// Node parent = getParent(node);
		//
		// switch (parent.getNodeClass()) {
		// case DataType:
		// case ObjectType:
		// case ReferenceType:
		// case VariableType:
		// sorted.add(0, i);
		// break;
		// case Object:
		// case Variable:
		// sorted.add(i);
		// break;
		// }
		// }
		//
		// return sorted.toArray(new ModelTypDef[0]);
	}

	// private Node getParent(Node parent) {
	// if (parent.getParent() != null) {
	// return getParent(parent.getParent());
	// }
	// return parent;
	// }
	@Override
	public void dispose() {
		if (imgRefType != null) {
			imgRefType.dispose();
		}
		if (imgObj != null) {
			imgObj.dispose();
		}
		if (imgObjType != null) {
			imgObjType.dispose();
		}
		if (imgVar != null) {
			imgVar.dispose();
		}
		if (imgVarType != null) {
			imgVarType.dispose();
		}
		if (imgMethod != null) {
			imgMethod.dispose();
		}
		if (imgDatatype != null) {
			imgDatatype.dispose();
		}
		if (imgView != null) {
			imgView.dispose();
		}
		if (imgValidMapping != null) {
			imgValidMapping.dispose();
		}
		if (imgInvalidMapping != null) {
			imgInvalidMapping.dispose();
		}
		super.dispose();
	}
}
