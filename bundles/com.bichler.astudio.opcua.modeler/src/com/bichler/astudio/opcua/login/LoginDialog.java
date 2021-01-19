package com.bichler.astudio.opcua.login;

import java.util.Locale;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.constants.ImageConstants;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;

public class LoginDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text;
	private Text text_1;
	private ComboViewer comboViewer;
	private CheckBoxButton chbtn_autoLogin;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public LoginDialog(Shell parentShell) {
		super(parentShell);
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
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LoginDialog.frm_mainForm.text"));
		formToolkit.decorateFormHeading(frm_mainForm);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginTop = 10;
		gridLayout.marginLeft = 10;
		frm_mainForm.getBody().setLayout(gridLayout);
		Label lbl_userName = new Label(frm_mainForm.getBody(), SWT.NONE);
		GridData gd_lbl_userName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_userName.widthHint = 110;
		lbl_userName.setLayoutData(gd_lbl_userName);
		lbl_userName.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LoginDialog.lbl_userName.text"));
		formToolkit.adapt(lbl_userName, true, true);
		text = new Text(frm_mainForm.getBody(), SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 250;
		text.setLayoutData(gd_text);
		formToolkit.adapt(text, true, true);
		Label lbl_password = new Label(frm_mainForm.getBody(), SWT.NONE);
		GridData gd_lbl_password = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_password.widthHint = 110;
		lbl_password.setLayoutData(gd_lbl_password);
		formToolkit.adapt(lbl_password, true, true);
		lbl_password.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LoginDialog.lbl_password.text")); //$NON-NLS-1$
		text_1 = new Text(frm_mainForm.getBody(), SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(text_1, true, true);
		Label lbl_language = new Label(frm_mainForm.getBody(), SWT.NONE);
		GridData gd_lbl_language = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_language.widthHint = 110;
		lbl_language.setLayoutData(gd_lbl_language);
		lbl_language.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LoginDialog.lbl_language.text")); //$NON-NLS-1$
		formToolkit.adapt(lbl_language, true, true);
		comboViewer = new ComboViewer(frm_mainForm.getBody(), SWT.READ_ONLY);
		Combo combo_1 = comboViewer.getCombo();
		combo_1.setTouchEnabled(true);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboViewer.setLabelProvider(new LoginLanguageLabelProvider());
		comboViewer.setContentProvider(new LoginLanguageContentProvider());
		formToolkit.paintBordersFor(combo_1);
		new Label(frm_mainForm.getBody(), SWT.NONE);
		chbtn_autoLogin = new CheckBoxButton(frm_mainForm.getBody(), SWT.NONE);
		chbtn_autoLogin.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "LoginDialog.chbtn_autoLogin.text")); //$NON-NLS-1$
		formToolkit.adapt(chbtn_autoLogin);
		formToolkit.paintBordersFor(chbtn_autoLogin);
		this.setLanguages();
		this.setHandlers();
		return container;
	}

	private void setLanguages() {
		LanguageItem[] langs = new LanguageItem[Locale.getAvailableLocales().length];
		int index = 0;
		int selected = 0;
		for (Locale loc : Locale.getAvailableLocales()) {
			langs[index] = new LanguageItem();
			langs[index].setLanguage(loc.getDisplayName());
			langs[index].setLocale(loc);
			langs[index].setImage(
					DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.VARIABLETYPE_IMG));
			if (Locale.getDefault().equals(loc)) {
				selected = index;
			}
			index++;
		}
		comboViewer.setInput(langs);
		comboViewer.getCombo().select(selected);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		createButton(parent, IDialogConstants.OPEN_ID,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.new.user"), true);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	private void setHandlers() {
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (comboViewer.getSelection() != null && !comboViewer.getSelection().isEmpty()) {
					if (((StructuredSelection) comboViewer.getSelection()).getFirstElement() != null) {
						// Locale.setDefault(
						// ((LanguageItem)((StructuredSelection)comboViewer.getSelection()).getFirstElement()).getLocale()
						// );
						// OPCDesignerString.setBundle(Locale.getDefault());
					}
				}
			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(409, 250);
	}

	private void loadDescriptions() {
		ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
		ISecurePreferences connections = preferences.node("SAVED");
	}
}
