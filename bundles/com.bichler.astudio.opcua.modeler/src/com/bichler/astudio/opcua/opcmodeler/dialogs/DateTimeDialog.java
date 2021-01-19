package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DateTimeDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Calendar calendarValue;
	private DateTime dtCalendar;
	private DateTime dtTime;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public DateTimeDialog(Shell parentShell) {
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
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Form frmDateTime = formToolkit.createForm(container);
		frmDateTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(frmDateTime);
		frmDateTime.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.calendar"));
		frmDateTime.getBody().setLayout(new GridLayout(1, false));
		this.dtCalendar = new DateTime(frmDateTime.getBody(), SWT.BORDER | SWT.DROP_DOWN | SWT.CALENDAR);
		dtCalendar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.dtTime = new DateTime(frmDateTime.getBody(), SWT.BORDER | SWT.DROP_DOWN | SWT.TIME | SWT.LONG);
		dtTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		this.dtCalendar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setCalendar();
			}
		});
		this.dtTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setCalendar();
			}
		});
		setCalendar();
		return container;
	}

	protected void setCalendar() {
		Calendar greg = GregorianCalendar.getInstance(SimpleTimeZone.getDefault());
		greg.set(dtCalendar.getYear(), dtCalendar.getMonth(), dtCalendar.getDay(), dtTime.getHours(),
				dtTime.getMinutes(), dtTime.getSeconds());
		calendarValue = greg;
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

	public Calendar getDateTime() {
		return this.calendarValue;
	}
}
