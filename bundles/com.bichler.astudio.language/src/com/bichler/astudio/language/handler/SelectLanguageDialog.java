package com.bichler.astudio.language.handler;

import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.bichler.astudio.language.LanguageActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class SelectLanguageDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Button btnGerman;

	private Button btnEnglish;

	private static final int WIDTH_LANGUAGEBUTTON = 170;

	private Locale applicationLocale = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SelectLanguageDialog(Shell parentShell) {
		super(parentShell);
	}

	public Locale getApplicationLocale() {
		return this.applicationLocale;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(getColor(SWT.COLOR_WHITE));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(getColor(SWT.COLOR_WHITE));
		container.setLayout(new GridLayout(1, false));

		Label lblTopCenter = new Label(container, SWT.NONE);
		lblTopCenter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		formToolkit.adapt(lblTopCenter, true, true);

		this.btnGerman = formToolkit.createButton(container,
				CustomString.getString(LanguageActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.dialog.language.german"),
				SWT.NONE);
		GridData gd_btnGerman = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_btnGerman.widthHint = WIDTH_LANGUAGEBUTTON;
		btnGerman.setLayoutData(gd_btnGerman);

		this.btnEnglish = formToolkit.createButton(container,
				CustomString.getString(LanguageActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.dialog.language.english"),
				SWT.NONE);
		GridData gd_btnEnglish = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_btnEnglish.widthHint = WIDTH_LANGUAGEBUTTON;
		btnEnglish.setLayoutData(gd_btnEnglish);

		Label lblBottomCenter = new Label(container, SWT.NONE);
		lblBottomCenter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		formToolkit.adapt(lblBottomCenter, true, true);

		setHandler();

		return container;
	}

	private void setHandler() {
		this.btnGerman.addSelectionListener(new LanguageSelectionAdapter(Locale.GERMAN));
		this.btnEnglish.addSelectionListener(new LanguageSelectionAdapter(Locale.ENGLISH));
		// this.btnHubabuba.addSelectionListener(new
		// LanguageSelectionAdapter(Locale.JAPANESE));
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(getColor(SWT.COLOR_WHITE));
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	class LanguageSelectionAdapter extends SelectionAdapter {

		private Locale locale;

		public LanguageSelectionAdapter(Locale locale) {
			this.locale = locale;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			applicationLocale = this.locale;
			okPressed();
		}

	}

	public static Color getColor(int systemColorID) {
		Display display = Display.getCurrent();
		return display.getSystemColor(systemColorID);
	}

}
