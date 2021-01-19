package com.bichler.astudio.device.core.dialog;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class UABoxStatusDialog extends Dialog {
	protected Object result;
	protected Shell shlUaboxControl;
	private Button btnStart;
	private Button btnStop;
	private IFileSystem filesystem;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public UABoxStatusDialog(Shell parent, IFileSystem filesystem, int style) {
		super(parent, style);
		setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.dialog.uaboxstatus.title"));
		this.filesystem = filesystem;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlUaboxControl.open();
		shlUaboxControl.layout();
		Display display = getParent().getDisplay();
		while (!shlUaboxControl.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlUaboxControl = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL);
		shlUaboxControl.setSize(450, 300);
		shlUaboxControl.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.dialog.uaboxstatus.title"));
		shlUaboxControl.setLayout(new GridLayout(2, false));
		Label lblNewLabel = new Label(shlUaboxControl, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_lblNewLabel.heightHint = 175;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.dialog.uaboxstatus.running"));
		btnStart = new Button(shlUaboxControl, SWT.NONE);
		btnStart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnStart.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.dialog.uaboxstatus.start"));
		btnStop = new Button(shlUaboxControl, SWT.NONE);
		btnStop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnStop.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.dialog.uaboxstatus.stop"));
		this.fillEditor();
	}

	private void fillEditor() {
		this.btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// try to start opc ua server
				try {
					boolean connect = filesystem.connect();
					if (!connect) {
						return;
					}
					boolean resultStartServer = filesystem.execCommand("sh /hbin/ciwd.sh start");
					if (resultStartServer) {
						btnStart.setEnabled(false);
						btnStop.setEnabled(true);
					}
				} catch (Exception ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
				} finally {
					filesystem.disconnect();
				}
			}
		});
		this.btnStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// try to stop opc ua server
				try {
					boolean connect = filesystem.connect();
					if(!connect) {
						return;
					}
					boolean resultStopServer = filesystem.execCommand("sh /hbin/ciwd.sh stop");
					if (resultStopServer) {
						btnStart.setEnabled(true);
						btnStop.setEnabled(false);
					}
				}

				catch (Exception ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
				} finally {
					filesystem.disconnect();
				}
			}
		});
	}
}
