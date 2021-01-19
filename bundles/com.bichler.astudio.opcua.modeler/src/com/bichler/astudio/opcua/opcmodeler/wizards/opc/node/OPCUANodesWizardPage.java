package com.bichler.astudio.opcua.opcmodeler.wizards.opc.node;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUANodesWizardPage extends WizardPage {
	private TableViewer tableViewer;
	private List<Node[]> nodes;

	/**
	 * Create the wizard.
	 */
	public OPCUANodesWizardPage() {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.opcnodes"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.node.infected"));
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
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNodeid = tableViewerColumn.getColumn();
		tblclmnNodeid.setWidth(100);
		tblclmnNodeid.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_nodeId.text"));
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDisplayname = tableViewerColumn_1.getColumn();
		tblclmnDisplayname.setWidth(100);
		tblclmnDisplayname.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_displayName.text"));
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNodeclass = tableViewerColumn_2.getColumn();
		tblclmnNodeclass.setWidth(100);
		tblclmnNodeclass.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.nodeclass"));
		this.tableViewer.setLabelProvider(new NodeTableLabelProvider());
		this.tableViewer.setContentProvider(new NodeContentProvider());
		this.tableViewer.setInput(this.nodes);
	}

	public void setNodes(List<Node[]> nodes) {
		this.nodes = nodes;
	}

	enum NodeTableNames {
		NodeId, Displayname, NodeClass, Unknown;
		public static int getIndex(NodeTableNames value) {
			int index = 0;
			for (NodeTableNames item : values()) {
				if (value == item) {
					return index;
				}
				index++;
			}
			return -1;
		}

		public static NodeTableNames getTableName(int index) {
			for (int i = 0; i < values().length; i++) {
				if (i == index) {
					return values()[i];
				}
			}
			return NodeTableNames.Unknown;
		}
	}

	private class NodeTableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			NodeTableNames index = NodeTableNames.getTableName(columnIndex);
			Node node = (Node) element;
			switch (index) {
			case NodeId:
				return node.getNodeId().toString();
			case Displayname:
				return node.getDisplayName().toString();
			case NodeClass:
				return node.getNodeClass().name();
			default:
				return "Unknown";
			}
		}
	}

	private static class NodeContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				List<Node> nodes = new ArrayList<>();
				for (Object node : ((List) inputElement)) {
					for (Node n : ((Node[]) node)) {
						nodes.add(n);
					}
				}
				return nodes.toArray(new Node[0]);
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
}
