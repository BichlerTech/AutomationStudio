package com.bichler.astudio.editor.siemens.wizard.removeDriverConfig;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.SiemensSharedImages;
import com.bichler.astudio.editor.siemens.model.SiemensNamespaceItem;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.newApplication.NamespaceItem;

public class DriverStatusNamespacePage extends WizardPage {
	private TableViewer tableViewer;
	private List<SiemensNamespaceItem> namespaces = new ArrayList<>();
	private Text txt_namespace;
	private Button btn_add;
	private Button btn_UseSymbolnameAs;
	private SiemensNamespaceItem usedNamespaceItem = null;
	private boolean generateSymbolnameId = false;

	/**
	 * Create the wizard.
	 */
	public DriverStatusNamespacePage() {
		super("wizardPage");
		setTitle(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.wizard.namespace.title"));
		setDescription(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.wizard.namespace.description"));
		String[] namespaces = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		for (String namespace : namespaces) {
			this.namespaces.add(new SiemensNamespaceItem(namespace));
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
		this.btn_UseSymbolnameAs = new Button(container, SWT.CHECK);
		btn_UseSymbolnameAs.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btn_UseSymbolnameAs.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"siemens.namespace.usesymbolname"));
		Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		Label lblNamespace = new Label(container, SWT.NONE);
		lblNamespace
				.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.namespace"));
		this.txt_namespace = new Text(container, SWT.BORDER);
		this.txt_namespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.btn_add = new Button(container, SWT.NONE);
		this.btn_add.setImage(SiemensSharedImages.getImage(SiemensSharedImages.ICON_ADD));
		this.btn_add.setToolTipText(
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.namespace.new"));
		this.tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		Table tree = tableViewer.getTable();
		tree.setHeaderVisible(true);
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_tree.verticalIndent = 5;
		tree.setLayoutData(gd_tree);
		TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		TableColumn tblclmn_index = tableViewerColumn.getColumn();
		tblclmn_index.setWidth(40);
		tblclmn_index.setText(
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.namespace.index"));
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmn_uri = tableViewerColumn_1.getColumn();
		tblclmn_uri.setWidth(250);
		tblclmn_uri.setText(
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.namespace.uri"));
		this.tableViewer.setLabelProvider(new NamespacesLabelProvider());
		this.tableViewer.setContentProvider(new NamespacesContentProvider());
		this.tableViewer.setInput(this.namespaces.toArray(new NamespaceItem[0]));
		setHandler();
		fill();
	}

	private void fill() {
		Deque<BrowsePathElement> browsePath = OPCUABrowseUtils.getFullBrowsePath(Identifiers.ObjectsFolder,
				ServerInstance.getInstance().getServerInstance(), Identifiers.RootFolder);
		Iterator<BrowsePathElement> iterator = browsePath.iterator();
		String path = "";
		while (iterator.hasNext()) {
			BrowsePathElement element = iterator.next();
			path += element.getBrowsename().getName();
			if (iterator.hasNext()) {
				path += "/";
			}
		}
	}

	@Override
	public boolean isPageComplete() {
		if (this.txt_namespace.getText().isEmpty()) {
			return false;
		}
		return true;
	}

	private void setHandler() {
		this.btn_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog input = new InputDialog(getShell(),
						CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
								"siemens.dialog.namespace.title"),
						CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
								"siemens.dialog.namespace.message"),
						"", new IInputValidator() {
							@Override
							public String isValid(String newText) {
								if (newText == null) {
									return CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
											"siemens.dialog.empty");
								}
								if (newText.isEmpty()) {
									return CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
											"siemens.dialog.empty");
								}
								List<SiemensNamespaceItem> ns = namespaces;
								for (NamespaceItem item : ns) {
									String nsString = item.getNamespace();
									if (nsString.equals(newText)) {
										return CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
												"siemens.dialog.exist");
									}
								}
								// no error
								return null;
							}
						});
				int open = input.open();
				if (Dialog.OK == open) {
					List<SiemensNamespaceItem> n2 = new ArrayList<>();
					for (SiemensNamespaceItem sni : namespaces) {
						if (sni.isServerEntry()) {
							n2.add(sni);
						}
					}
					usedNamespaceItem = new SiemensNamespaceItem(input.getValue(), false);
					n2.add(usedNamespaceItem);
					namespaces.clear();
					namespaces.addAll(n2);
					tableViewer.setInput(namespaces.toArray(new NamespaceItem[0]));
					tableViewer.setSelection(new StructuredSelection(usedNamespaceItem));
					txt_namespace.setText(input.getValue());
				}
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		this.tableViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem selection = ((Table) e.getSource()).getSelection()[0];
				Object data = selection.getData();
				if (data instanceof SiemensNamespaceItem) {
					usedNamespaceItem = (SiemensNamespaceItem) data;
					String ns = usedNamespaceItem.getNamespace();
					txt_namespace.setText(ns);
				}
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		this.btn_UseSymbolnameAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateSymbolnameId = ((Button) e.getSource()).getSelection();
			}
		});
		this.txt_namespace.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				e.doit = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});
	}

	public SiemensNamespaceItem getNamespaceItem() {
		return this.usedNamespaceItem;
	}

	public boolean isGenerateSymbolnameId() {
		return this.generateSymbolnameId;
	}

	class NamespacesLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof NamespaceItem) {
				String ns = ((NamespaceItem) element).getNamespace();
				switch (columnIndex) {
				case 0:
					return "" + namespaces.indexOf(element);
				case 1:
					return ns;
				}
			}
			return null;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	class NamespacesContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Object[]) {
				return (Object[]) inputElement;
			}
			return new Object[0];
		}
	}
}
