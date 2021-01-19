package com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.remove;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.ServerTypeModel;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;

public class ModelRemoveElementPage extends WizardPage {
	private Image imgRefType;
	private Image imgObj;
	private Image imgVar;
	private Image imgMethod;
	private Image imgObjType;
	private Image imgVarType;
	private Image imgDatatype;
	private Image imgView;
	private TableViewer tableViewer;
	private ModelTypDef typeDef;
	private ModelTypDef[] input;
	private Image imgInvalidMapping;
	private Image imgValidMapping;

	/**
	 * Create the wizard.
	 */
	public ModelRemoveElementPage() {
		super("ModelRemoveElementPage");
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

	enum RemoveTableItems {
		State, Klasse, Name, NodeId;
		public static RemoveTableItems valueOf(int i) {
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
		container.setLayout(new GridLayout(3, false));
		this.tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		this.tableViewer.setContentProvider(new MREContentProvider());
		this.tableViewer.setLabelProvider(new MRELabelProvider());
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
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
		// try {
		// getContainer().run(true, true, new IRunnableWithProgress() {
		//
		// @Override
		// public void run(IProgressMonitor monitor)
		// throws InvocationTargetException, InterruptedException {
		//
		// monitor.beginTask("Task1", IProgressMonitor.UNKNOWN);
		// try {
		setInput();
		// } finally {
		// monitor.done();
		// }
		// }
		// });
		// } catch (InvocationTargetException e) {
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	private void setHandler() {
	}

	private void setInput() {
		// this.tableViewer.setInput(getInput());
		this.tableViewer.setInput(this.input);
	}

	class ColumnLabelProvider extends CellLabelProvider {
		private int index = -1;

		protected ColumnLabelProvider(int index) {
			this.index = index;
		}

		@Override
		public void update(ViewerCell cell) {
			ModelTypDef element = (ModelTypDef) cell.getElement();
			switch (RemoveTableItems.valueOf(this.index)) {
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

	class MREContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			return super.getElements(inputElement);
		}
	}

	class MRELabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof ModelTypDef) {
				return ((ModelTypDef) element).nodeId.toString() + "  \t " + ((ModelTypDef) element).name.toString()
						+ "  \t " + ((ModelTypDef) element).typeClass.name();
			}
			return super.getText(element);
		}
	}

	public void setTypeDef(ModelTypDef typeDef) {
		this.typeDef = typeDef;
	}

	public Object getChanges() {
		return this.tableViewer.getInput();
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
		if (imgInvalidMapping != null) {
			imgInvalidMapping.dispose();
		}
		if (imgValidMapping != null) {
			imgValidMapping.dispose();
		}
		super.dispose();
	}

	public void setInput(ModelTypDef[] input) {
		this.input = input;
	}
}
