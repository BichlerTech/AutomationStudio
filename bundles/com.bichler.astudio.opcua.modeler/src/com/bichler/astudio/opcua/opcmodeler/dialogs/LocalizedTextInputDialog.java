package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.LocalizedText;

public class LocalizedTextInputDialog extends Dialog {
	private CCombo cmb_locale;
	private Text txt_text;
	private LocalizedText localizedText;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public LocalizedTextInputDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		Label lblInputOfLocalized = new Label(container, SWT.NONE);
		lblInputOfLocalized.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblInputOfLocalized.setText("Input of Localized Text");
		this.cmb_locale = new CCombo(container, SWT.BORDER);
		this.txt_text = new Text(container, SWT.BORDER);
		txt_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fillControls();
		this.txt_text.setFocus();
		return container;
	}

	private void fillControls() {
		Locale[] locales = Locale.getAvailableLocales();
		Locale defaultLocale = Locale.getDefault();
		int index = 0;
		for (int i = 0; i < locales.length; i++) {
			if (locales[i].equals(defaultLocale)) {
				index = i;
			}
			this.cmb_locale.add(locales[i].toString());
			this.cmb_locale.setData(locales[i].toString(), locales[i]);
		}
		this.cmb_locale.select(index);
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
		this.localizedText = new LocalizedText(this.txt_text.getText(),
				(Locale) this.cmb_locale.getData(this.cmb_locale.getText()));
		super.okPressed();
	}

	public LocalizedText getLocalizedText() {
		return this.localizedText;
	}
}
