package com.bichler.astudio.opcua.opcmodeler.wizards.opc.namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.ResourceManager;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignedit.TableLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.newApplication.NamespaceItem;

public class OPCNamespaceWizardPage extends WizardPage {
	private List<NamespaceItem> namespaces;
	private TableViewer tableViewer;
	private Button btn_add;
	private Button btn_edit;
	private Button btn_delete;
	private Button btn_Up;
	private Button btn_Down;

	/**
	 * Create the wizard.
	 * 
	 * @param nsServer
	 */
	public OPCNamespaceWizardPage(String[] nsServer) {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.description"));
		this.namespaces = new ArrayList<>();
		for (String ns : nsServer) {
			this.namespaces.add(new NamespaceItem(ns));
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
		container.setLayout(new GridLayout(2, false));
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		Table tb_nameSpaces = tableViewer.getTable();
		tb_nameSpaces.setHeaderVisible(true);
		tb_nameSpaces.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmn_index = tableViewerColumn.getColumn();
		tblclmn_index.setWidth(40);
		tblclmn_index.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.index"));
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmn_uri = tableViewerColumn_1.getColumn();
		tblclmn_uri.setWidth(250);
		tblclmn_uri.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.uri"));
		tableViewer.setLabelProvider(new NamespacesLabelProvider());
		tableViewer.setContentProvider(new NamespacesContentProvider());
		Composite composite = new Composite(container, SWT.NONE);
		RowLayout rl_composite = new RowLayout(SWT.VERTICAL);
		rl_composite.fill = true;
		rl_composite.center = true;
		composite.setLayout(rl_composite);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 2));
		btn_add = new Button(composite, SWT.NONE);
		btn_add.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.btn_add.toolTipText"));
		btn_add.setText("");
		btn_add.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/add_32.png"));
		btn_edit = new Button(composite, SWT.NONE);
		btn_edit.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.btn_edit.toolTipText"));
		btn_edit.setText("");
		btn_edit.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/edit_32.png"));
		btn_delete = new Button(composite, SWT.NONE);
		btn_delete.setToolTipText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NamespaceDialog.btn_delete.toolTipText"));
		btn_delete.setText("");
		btn_delete.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/delete_32.png"));
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "OPCNamespaceWizardPage.label.text")); //$NON-NLS-1$
		this.btn_Up = new Button(composite, SWT.NONE);
		btn_Up.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.tooltip.btnup"));
		btn_Up.setText("");
		btn_Up.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/arrow_up.png"));
		btn_Down = new Button(composite, SWT.NONE);
		btn_Down.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.tooltip.btndown"));
		btn_Down.setText("");
		btn_Down.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/arrow_down.png"));
		setHandler();
		setInput();
		this.tableViewer.getTable().notifyListeners(SWT.Selection, new Event());
	}

	@Override
	public boolean isPageComplete() {
		int length = this.namespaces.size();
		// we have no other namespaces then the default opc ua namespace
//		if (length <= 1) {
//			return false;
//		}
		return true;
	}

	private void addNamespace() {
		InputDialog input = new InputDialog(getShell(),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.btn_add.toolTipText"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"),
				"", new IInputValidator() {
					@Override
					public String isValid(String newText) {
						if (newText == null) {
							return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
									"wizard.namespace.empty");
						}
						if (newText.isEmpty()) {
							return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
									"wizard.namespace.empty");
						}
						List<NamespaceItem> ns = namespaces;
						for (NamespaceItem item : ns) {
							String nsString = item.getNamespace();
							if (nsString.equals(newText)) {
								return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
										"wizard.namespace.exist");
							}
						}
						// no error
						return null;
					}
				});
		int open = input.open();
		if (open == Dialog.OK) {

			String namespace = input.getValue();
			if (namespace != null) {
				for (NamespaceItem n : namespaces) {
					if (n.getNamespace().equals(namespace)) {
						break;
					}
				}
			}
			if (namespace != null) {
				namespaces.add(new NamespaceItem(namespace));
				setInput();
			}
		}
		getWizard().getContainer().updateButtons();
	}

	private void editNamespace() {
		/** map the selected index to the namespaceindex */
		TableItem item = tableViewer.getTable().getSelection()[0];
		int originIndex = -1;
		String originUri = "";
		NamespaceItem data = null;
		try {
			data = (NamespaceItem) item.getData();
			originUri = data.getNamespace();
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			originIndex = nsTable.getIndex(originUri);

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		if (originIndex >= 0 && originIndex <= 0) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.zero"));
			return;
		}
		// else if (originIndex > 2) {
		IStructuredSelection selection = (IStructuredSelection) this.tableViewer.getSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		}
		final NamespaceItem bo = (NamespaceItem) selection.getFirstElement();
		InputDialog input = new InputDialog(getShell(),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.btn_edit.toolTipText"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"),
				bo.getNamespace(), new IInputValidator() {
					@Override
					public String isValid(String newText) {
						if (newText == null) {
							return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
									"wizard.namespace.empty");
						}
						if (newText.isEmpty()) {
							return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
									"wizard.namespace.empty");
						}
						List<NamespaceItem> ns = namespaces;
						for (NamespaceItem item : ns) {
							String nsString = item.getNamespace();
							if (!(bo.getNamespace().equals(newText)) && nsString.equals(newText)) {
								return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
										"wizard.namespace.exist");
							}
						}
						// no error
						return null;
					}
				});
		int open = input.open();
		if (open == Dialog.OK) {
			String namespace = input.getValue();
			bo.setModifiedNamespace(namespace);
			update(bo);
		}
		getWizard().getContainer().updateButtons();
	}

	private void removeNamespace() {
		/** map the selected index to the namespaceindex */
		TableItem item = tableViewer.getTable().getSelection()[0];
		int originIndex = -1;
		String originUri = "";
		NamespaceItem data = null;
		try {
			data = (NamespaceItem) item.getData();
			originUri = data.getNamespace();
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			originIndex = nsTable.getIndex(originUri);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		if (originIndex == 0) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.zero"));
			return;
		}
		this.namespaces.remove(item.getData());
		NamespaceItem[] ns = namespaces.toArray(new NamespaceItem[0]);
		tableViewer.setInput(ns);
		getWizard().getContainer().updateButtons();
	}

	private void shiftNamespace(boolean shiftUp) {
		TableItem tableItem = tableViewer.getTable().getSelection()[0];
		int index = -1;
		try {
			index = Integer.parseInt(tableItem.getText(0));
		} catch (NumberFormatException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
		}
		if (index < 0) {
			return;
		}
		int nextIndex = -1;
		if (shiftUp) {
			nextIndex = index + 1;
		} else {
			nextIndex = index - 1;
		}
		NamespaceItem change = this.namespaces.get(index);
		this.namespaces.set(index, this.namespaces.get(nextIndex));
		this.namespaces.set(nextIndex, change);
		setInput();
		tableViewer.getTable().setSelection(nextIndex);
		tableViewer.getTable().notifyListeners(SWT.Selection, new Event());
	}

	private void setEnableModifiyButtons(String selectedNs, boolean enable) {
		this.btn_add.setEnabled(true);
		// disable
		if (!enable) {
			this.btn_edit.setEnabled(false);
			this.btn_delete.setEnabled(false);
			this.btn_Up.setEnabled(false);
			this.btn_Down.setEnabled(false);
			return;
		}
		// check if allow enable
		for (int i = 0; i < namespaces.size(); i++) {
			NamespaceItem ns = namespaces.get(i);
			// find index of selected namespace
			if (ns == null || !ns.getNamespace().equals(selectedNs)) {
				continue;
			}
			// cannot modify server defined namespaces [0-2]
			if (i <= 0) {
				this.btn_edit.setEnabled(false);
				this.btn_delete.setEnabled(false);
				this.btn_Up.setEnabled(false);
				this.btn_Down.setEnabled(false);
				return;
			}
			// cannot shift index [3] to position [2] or [0] if position [3] is
			// last
			else if (i == 3 && i == (namespaces.size() - 1)) {
				this.btn_Down.setEnabled(false);
				this.btn_Up.setEnabled(false);
				break;
			}
			// cannot shift index [3] to position [2]
			else if (i == 1) {
				this.btn_Down.setEnabled(true);
				this.btn_Up.setEnabled(false);
				break;
			}
			// cannot shift index [last] to position [0]
			else if (i == (namespaces.size() - 1)) {
				this.btn_Down.setEnabled(false);
				this.btn_Up.setEnabled(true);
				break;
			}
			// shifting allowed
			else {
				this.btn_Down.setEnabled(true);
				this.btn_Up.setEnabled(true);
			}
		}
		// enable when selection is valid to modify
		this.btn_edit.setEnabled(true);
		this.btn_delete.setEnabled(true);
	}

	private void setInput() {
		NamespaceItem[] ns = namespaces.toArray(new NamespaceItem[0]);

		tableViewer.setInput(ns);
	}

	private void update(NamespaceItem bo) {
		this.tableViewer.update(bo, null);
	}

	private void setHandler() {
		btn_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNamespace();
			}
		});
		btn_edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editNamespace();
			}
		});
		this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection == null || selection.isEmpty()) {
					setEnableModifiyButtons(null, false);
				} else {
					String ns = ((NamespaceItem) selection.getFirstElement()).getNamespace();
					setEnableModifiyButtons(ns, true);
				}
			}
		});
		this.tableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editNamespace();
			}
		});
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeNamespace();
			}
		});
		btn_Down.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shiftNamespace(true);
			}
		});
		btn_Up.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shiftNamespace(false);
			}
		});
	}

	public NamespaceItem[] getNamespaces() {
		// List<String> ns = new ArrayList<>();
		// for (NSTableItemBO bo : this.namespaces) {
		// ns.add(bo.getNamespace());
		// }
		return this.namespaces.toArray(new NamespaceItem[0]);
	}

	class NamespacesLabelProvider extends TableLabelProvider {
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
			// else {
			// String[] elements = (String[]) element;
			//
			// if (elements[columnIndex] == null) {
			// return "";
			// }
			// return ((String[]) element)[columnIndex].toString();
			// }
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
