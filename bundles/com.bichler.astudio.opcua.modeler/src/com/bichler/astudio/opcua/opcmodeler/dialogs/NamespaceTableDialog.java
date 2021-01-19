package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.namespaceprovider.NamespacesIndexContentProvider;
import com.bichler.astudio.opcua.opcmodeler.dialogs.namespaceprovider.NamespacesLabelProvider;

public class NamespaceTableDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table tb_nameSpaces;
	private TableViewer tv_nameSpaces;
	private int namespaceIndex = -1;
	private NamespaceTable namespaces = null;
	private boolean showZeroNS;
	private String[] selectedNamespace;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public NamespaceTableDialog(Shell parentShell, boolean showZeroNS, Integer namespaceIndex) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.RESIZE | SWT.TITLE);
		namespaces = NamespaceTable
				.createFromArray(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray());
		this.showZeroNS = showZeroNS;
		this.namespaceIndex = namespaceIndex;
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
		frm_mainForm.getBody().setLayout(new GridLayout(1, false));
		tv_nameSpaces = new TableViewer(frm_mainForm.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tb_nameSpaces = tv_nameSpaces.getTable();
		tb_nameSpaces.setHeaderVisible(true);
		tb_nameSpaces.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		formToolkit.paintBordersFor(tb_nameSpaces);
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tv_nameSpaces, SWT.NONE);
		TableColumn tblclmn_index = tableViewerColumn.getColumn();
		tblclmn_index.setWidth(40);
		tblclmn_index.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.index"));
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_nameSpaces, SWT.NONE);
		TableColumn tblclmn_uri = tableViewerColumn_1.getColumn();
		tblclmn_uri.setWidth(250);
		tblclmn_uri.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.uri"));
		tv_nameSpaces.setLabelProvider(new NamespacesLabelProvider());
		tv_nameSpaces.setContentProvider(new NamespacesIndexContentProvider());
		String[] array = getNamespaceAsArray();
		tv_nameSpaces.setInput(array);
		this.refreshNamespaceTextField();
		this.setHandlers();
		return container;
	}

	private String[] getNamespaceAsArray() {
		String[] fullNs = this.namespaces.toArray();
		if (showZeroNS) {
			return fullNs;
		}
		List<String> ns = new ArrayList<>();
		for (int i = 1; i < fullNs.length; i++) {
			ns.add(fullNs[i]);
		}
		return ns.toArray(new String[0]);
	}

	private void setHandlers() {
		tv_nameSpaces.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectedNamespace = (String[]) selection.getFirstElement();
			}
		});
		if (namespaceIndex >= 1 && namespaceIndex < getNamespaceAsArray().length + 1) {
			tv_nameSpaces.getTable().select(namespaceIndex - 1);
		}
		IStructuredSelection selection = (IStructuredSelection) tv_nameSpaces.getSelection();
		this.selectedNamespace = (String[]) selection.getFirstElement();
	}

	public String[] getSelectedNamespace() {
		return this.selectedNamespace;
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
		super.okPressed();
	}

	private void refreshNamespaceTextField() {
		tv_nameSpaces.setInput(getNamespaceAsArray());
	}
}
