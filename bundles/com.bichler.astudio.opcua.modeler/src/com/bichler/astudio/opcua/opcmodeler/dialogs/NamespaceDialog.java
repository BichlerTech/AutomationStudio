package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.node.Node;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.WriteValue;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.namespaceprovider.NamespacesContentProvider;
import com.bichler.astudio.opcua.opcmodeler.dialogs.namespaceprovider.NamespacesLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.internationalization.CustomString;

/**
 * @deprecated use
 *             com.hbsoft.designer.wizards.opc.namespace.OPCNamespaceWizardPage
 * 
 * @author Kofi-Eagle
 *
 */
public class NamespaceDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Button btn_add;
	private Button btn_edit;
	private Button btn_delete;
	private Table tb_nameSpaces;
	private TableViewer tv_nameSpaces;
	private NamespaceTable namespaces = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public NamespaceDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.RESIZE | SWT.TITLE);
		this.namespaces = NamespaceTable
				.createFromArray(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form frm_mainForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frm_mainForm);
		frm_mainForm.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.frm_mainForm.caption"));
		formToolkit.decorateFormHeading(frm_mainForm);
		frm_mainForm.getBody().setLayout(new GridLayout(2, false));
		tv_nameSpaces = new TableViewer(frm_mainForm.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tb_nameSpaces = tv_nameSpaces.getTable();
		tb_nameSpaces.setHeaderVisible(true);
		tb_nameSpaces.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		formToolkit.paintBordersFor(tb_nameSpaces);
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tv_nameSpaces, SWT.NONE);
		TableColumn tblclmn_index = tableViewerColumn.getColumn();
		tblclmn_index.setWidth(40);
		tblclmn_index.setText("index");
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_nameSpaces, SWT.NONE);
		TableColumn tblclmn_uri = tableViewerColumn_1.getColumn();
		tblclmn_uri.setWidth(250);
		tblclmn_uri.setText("URI");
		tv_nameSpaces.setLabelProvider(new NamespacesLabelProvider());
		tv_nameSpaces.setContentProvider(new NamespacesContentProvider());
		tv_nameSpaces.setInput(namespaces.toArray());
		Composite composite = new Composite(frm_mainForm.getBody(), SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 2));
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		Label lbl_nameSpaces = new Label(composite, SWT.NONE);
		formToolkit.adapt(lbl_nameSpaces, true, true);
		lbl_nameSpaces.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"));
		btn_add = new Button(composite, SWT.NONE);
		btn_add.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.btn_add.toolTipText")); //$NON-NLS-1$
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add.widthHint = 60;
		gd_btn_add.heightHint = 40;
		btn_add.setLayoutData(gd_btn_add);
		btn_add.setText("");
		btn_add.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/add_32.png"));
		formToolkit.adapt(btn_add, true, true);
		btn_edit = new Button(composite, SWT.NONE);
		btn_edit.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.btn_edit.toolTipText"));
		GridData gd_btn_edit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_edit.widthHint = 60;
		gd_btn_edit.heightHint = 40;
		btn_edit.setLayoutData(gd_btn_edit);
		btn_edit.setText("");
		btn_edit.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/edit_32.png"));
		formToolkit.adapt(btn_edit, true, true);
		btn_delete = new Button(composite, SWT.NONE);
		btn_delete.setToolTipText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NamespaceDialog.btn_delete.toolTipText"));
		GridData gd_btn_remove = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_remove.widthHint = 60;
		gd_btn_remove.heightHint = 40;
		btn_delete.setLayoutData(gd_btn_remove);
		btn_delete.setText("");
		btn_delete.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/delete_32.png"));
		formToolkit.adapt(btn_delete, true, true);
		this.refreshNamespaceTextField();
		this.setHandlers();
		return container;
	}

	private void setHandlers() {
		btn_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog input = new InputDialog(getShell(), "Add Namespace", "Namespace:", null, null);
				int open = input.open();
				if (open == Dialog.OK) {
					String namespace = input.getValue();
					int index = namespaces.getIndex(namespace);
					if (index == -1) {
						namespaces.add(-1, namespace);
						refreshNamespaceTextField();
					}
				}
			}
		});
		btn_edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editNamespace();
			}
		});
		tb_nameSpaces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editNamespace();
			}
		});
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/** first check if we have selected a namespace to delete */
				if (tb_nameSpaces.getSelection() != null && tb_nameSpaces.getSelectionIndex() >= 0) {
					/** map the selected index to the namespaceindex */
					TableItem item = tb_nameSpaces.getSelection()[0];
					int index = -1;
					try {
						index = Integer.parseInt(item.getText(0));
					} catch (NumberFormatException ex) {
					}
					if (index == 0) {
						// DesignerFormToolkit k = new DesignerFormToolkit();
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Could not remove namespace index 0, it is the internal namespace");
						return;
					} else if (index > 0) {
						/**
						 * now we have to check if we have an node with that namespaceindex
						 */
						Node[] nodes = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
								.getAllNodes(index);
						if (nodes.length > 0) {
							/**
							 * namespace could not be deleted because we found a node with that index
							 */
							MessageDialog.openError(getShell(), "Error removing Namespace!",
									"Could not remove namespace index because there is a node found  for that name space");
							return;
						}
						namespaces.remove(index);
						refreshNamespaceTextField();
					}
				}
			}
		});
	}

	private void editNamespace() {
		if (tb_nameSpaces.getSelection() != null && tb_nameSpaces.getSelection().length > 0) {
			InputDialog input = new InputDialog(getShell(), "Edit Namespace", "Namespace:",
					((String[]) tb_nameSpaces.getSelection()[0].getData())[1], null);
			int open = input.open();
			if (open == Dialog.OK) {
				String namespace = input.getValue();
				int index = Integer.parseInt(((String[]) tb_nameSpaces.getSelection()[0].getData())[0]);
				namespaces.remove(index);
				namespaces.add(index, namespace);
				refreshNamespaceTextField();
			}
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void okPressed() {
		String[] namespaceArrays = this.namespaces.toArray();
		String[] uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		if (!Arrays.equals(namespaceArrays, uris)) {
			try {
				WriteValue value = new WriteValue();
				value.setAttributeId(Attributes.Value);
				value.setNodeId(Identifiers.Server_NamespaceArray);
				value.setValue(new DataValue(new Variant(namespaceArrays)));
				ServerInstance.getInstance().getServerInstance().getMaster().write(new WriteValue[] { value }, true,
						null, null);
				// remove all namespace uris
				for (String uri : uris) {
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().remove(uri);
				}
				// add all namespaces to array
				for (String uri : namespaceArrays) {
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().add(-1, uri);
				}
				ModelBrowserView mbv = (ModelBrowserView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView(ModelBrowserView.ID);
				mbv.setDirty(true);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		super.okPressed();
	}

	private void refreshNamespaceTextField() {
		tv_nameSpaces.setInput(namespaces.toArray());
		// tv_nameSpaces.getTable().update();
		/**
		 * String []namespaces = ServerInstance.getInstance().getServerInstance()
		 * .getNamespaceUris().toArray(); for (String namespace : namespaces) {
		 * if(namespace != null) { this.tv_nameSpaces.add(namespace); } }
		 */
	}
}
