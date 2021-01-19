package com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.attribute;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTreeDef;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ModelAttributeElementPage extends WizardPage {
	private Image imgRefType;
	private Image imgObj;
	private Image imgVar;
	private Image imgMethod;
	private Image imgObjType;
	private Image imgVarType;
	private Image imgDatatype;
	private Image imgView;
	private ModelTypDef typeDef;
	private TableViewer tableViewer;

	/**
	 * Create the wizard.
	 */
	public ModelAttributeElementPage() {
		super("modelchangeattribute");
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
	}

	enum MCTableItems {
		Klasse, Name, NodeId;
		public static MCTableItems valueOf(int i) {
			switch (i) {
			case 0:
				return Klasse;
			case 1:
				return Name;
			case 2:
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
		Table table = tableViewer.getTable();
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setMoveable(true);
		tblclmnName.setResizable(true);
		tblclmnName.setWidth(100);
		tblclmnName.setText(MCTableItems.Klasse.name());
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn_1.getColumn();
		tblclmnNewColumn.setMoveable(true);
		tblclmnNewColumn.setResizable(true);
		tblclmnNewColumn.setWidth(150);
		tblclmnNewColumn.setText(MCTableItems.Name.name());
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn_1.setMoveable(true);
		tblclmnNewColumn_1.setResizable(true);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText(MCTableItems.NodeId.name());
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(0));
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(1));
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(2));
		setHandler();
		setInput();
	}

	private void setInput() {
		ModelTreeDef modelTree = this.typeDef.getModelTree();
		ModelTreeDef[] changedNodes = modelTree.getChildren();
		this.tableViewer.setInput(changedNodes);
	}

	private void setHandler() {
	}

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
			ModelTreeDef element = (ModelTreeDef) cell.getElement();
			switch (MCTableItems.valueOf(this.index)) {
			case Klasse:
				NodeClass c = ((ModelTreeDef) element).getNodeInfo().getNodeClass();
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
				cell.setText(((ModelTreeDef) element).getNodeInfo().getDisplayName().toString());
				break;
			case NodeId:
				cell.setText(((ModelTreeDef) element).getNodeId().toString());
				break;
			}
		}
	}

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
		super.dispose();
	}

	public void setTypeDef(ModelTypDef typeDef) {
		this.typeDef = typeDef;
	}

	public Object getInput() {
		return this.tableViewer.getInput();
	}
}
