package com.bichler.astudio.opcua.opcmodeler.editor.node.language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import opc.sdk.core.language.LanguageItem;
import opc.sdk.core.language.LanguagePack;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;

public abstract class AbstractLanguageDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtName;
	private TableViewer tableViewer;
	private String defaultName;
	private NodeId nodeId;
	private LanguageItem item;
	private String name;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public AbstractLanguageDialog(Shell parentShell, NodeId nodeId, String defaultName, String name) {
		super(parentShell);
		this.defaultName = defaultName;
		this.nodeId = nodeId;
		this.name = name;
		LanguageItem[] item = ServerInstance.getInstance().getServerInstance()
				.getLanguageInformation(new NodeId[] { nodeId });
		if (item != null && item.length > 0) {
			this.item = item[0];
		}
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form form = formToolkit.createForm(container);
		formToolkit.paintBordersFor(form);
		form.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LoginDialog.lbl_language.text"));
		form.getBody().setLayout(new GridLayout(2, false));
		Label lblName = new Label(form.getBody(), SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblName, true, true);
		txtName = new Text(form.getBody(), SWT.BORDER | SWT.READ_ONLY);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtName, true, true);
		Label lblSeperator = new Label(form.getBody(), SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeperator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		formToolkit.adapt(lblSeperator, true, true);
		Composite composite = formToolkit.createComposite(form.getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));
		Button btnAdd = new Button(composite, SWT.NONE);
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add.widthHint = 50;
		btnAdd.setLayoutData(gd_btn_add);
		btnAdd.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/add_16.png"));
		formToolkit.adapt(btnAdd, true, true);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<LanguagePack> packs = (List<LanguagePack>) tableViewer.getInput();
				if (!packs.isEmpty()) {
					LanguagePack last = packs.get(packs.size() - 1);
					boolean isLastEmpty = last.getLocale() == null && (last.getDescription() == null
							|| last.getDisplayname() == null || last.getInversename() == null);
					if (!isLastEmpty) {
						// add new pack
						LanguagePack pack = new LanguagePack();
						packs.add(pack);
						tableViewer.refresh();
					}
				} else {
					LanguagePack pack = new LanguagePack();
					packs.add(pack);
					tableViewer.refresh();
				}
			}
		});
		Button btnRemove = new Button(composite, SWT.NONE);
		GridData gdbtnRemove = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdbtnRemove.widthHint = 50;
		btnRemove.setLayoutData(gdbtnRemove);
		btnRemove.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/delete_16.png"));
		formToolkit.adapt(btnRemove, true, true);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				LanguagePack pack = (LanguagePack) selection.getFirstElement();
				List<LanguagePack> packs = (List<LanguagePack>) tableViewer.getInput();
				// add pack
				packs.remove(pack);
				tableViewer.refresh();
			}
		});
		this.tableViewer = new TableViewer(form.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		this.tableViewer.setContentProvider(new LangTableContentProvider());
		final Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(table);
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				List<LanguagePack> input = (List<LanguagePack>) tableViewer.getInput();
				List<Locale> languages = new ArrayList<>();
				Locale[] available = Locale.getAvailableLocales();
				// add available locales
				for (Locale locale : available) {
					languages.add(locale);
				}
				// remove existing locales
				for (LanguagePack pack : input) {
					Locale locale = pack.getLocale();
					if (locale == null) {
						continue;
					}
					languages.remove(locale);
				}
				ComboBoxViewerCellEditor cbvce = new ComboBoxViewerCellEditor(table, SWT.READ_ONLY);
				cbvce.setContentProvider(new ArrayContentProvider());
				cbvce.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(Object element) {
						return element == null ? "..." : ((Locale) element).getDisplayName();
					}
				});
				cbvce.setInput(languages);
				return cbvce;
			}

			@Override
			protected Object getValue(Object element) {
				return ((LanguagePack) element).getLocale();
			}

			@Override
			protected void setValue(Object element, Object value) {
				// disable null value input
				if (value == null) {
					return;
				}
				((LanguagePack) element).setLocale((Locale) value);
				tableViewer.update(element, null);
			}
		});
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				LanguagePack langPack = (LanguagePack) element;
				return langPack.getLocale() == null ? "..." : langPack.getLocale().getLanguage();
			}
		});
		TableColumn tblclmnLokale = tableViewerColumn.getColumn();
		tblclmnLokale.setWidth(100);
		tblclmnLokale.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.local"));
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}

			@Override
			protected Object getValue(Object element) {
				return getLanguageTextFromPack((LanguagePack) element);
			}

			@Override
			protected void setValue(Object element, Object value) {
				setLanguageTextToPack((LanguagePack) element, (String) value);
				tableViewer.update(element, null);
			}
		});
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				LanguagePack langPack = (LanguagePack) element;
				String text = getLanguageTextFromPack(langPack);
				return text == null ? "..." : text;
			}
		});
		TableColumn tblclmnName = tableViewerColumn_1.getColumn();
		tblclmnName.setWidth(250);
		tblclmnName.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.name"));
		setDefaultLableName(lblName);
		fillTable();
		return container;
	}

	protected abstract String getLanguageTextFromPack(LanguagePack langPack);

	protected abstract void setLanguageTextToPack(LanguagePack element, String value);

	private void fillTable() {
		Collection<String> langs = this.item.getLanguages();
		List<LanguagePack> packs = new ArrayList<>();
		for (String lang : langs) {
			LanguagePack pack = this.item.getLanguagePack(lang);
			packs.add(pack);
		}
		this.tableViewer.setInput(packs);
	}

	private void setDefaultLableName(Label label) {
		label.setText(this.defaultName);
		this.txtName.setText(this.name);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
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
		List<LanguagePack> items = (List<LanguagePack>) this.tableViewer.getInput();
		this.item.removeLanguages();
		for (LanguagePack loopItem : items) {
			if (loopItem.getLocale() == null) {
				continue;
			}
			this.item.addLanguagePack(loopItem);
		}
		// save()
		super.okPressed();
	}

	class LangTableContentProvider extends ArrayContentProvider {
	}
}
