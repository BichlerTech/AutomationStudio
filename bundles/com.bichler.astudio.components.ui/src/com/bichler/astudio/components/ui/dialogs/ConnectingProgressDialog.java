package com.bichler.astudio.components.ui.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.components.ui.ComponentsUIActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import org.eclipse.swt.widgets.Label;

public class ConnectingProgressDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private ProgressBar progressBar;

	private volatile boolean isReady = false;
	private String hostname;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ConnectingProgressDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ConnectingProgressDialog(Shell parent, int style, String hostname) {
		super(parent, style);
		this.hostname = hostname;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), SWT.MODELESS);

		shell.setEnabled(false);
		shell.setSize(450, 143);
		shell.setText(getText());
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.marginTop = 35;
		gl_shell.marginRight = 35;
		gl_shell.marginLeft = 35;
		gl_shell.marginBottom = 35;
		gl_shell.marginHeight = 0;
		shell.setLayout(gl_shell);

		Point size = shell.getSize();
		Rectangle screen = Display.getCurrent().getMonitors()[0].getBounds();
		shell.setBounds((screen.width - size.x) / 2, (screen.height - size.y) / 2, size.x, size.y);

		Label lblConnectingToHost = new Label(shell, SWT.NONE);
		lblConnectingToHost.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		lblConnectingToHost.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,
				"ConnectionProgressDialog.Connecting") + /* "connecting to host: " */ this.hostname);

		progressBar = new ProgressBar(shell, SWT.SMOOTH);
		GridData gd_progressBar = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_progressBar.heightHint = 50;
		progressBar.setLayoutData(gd_progressBar);

		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {

				while (!isReady) {
					if (progressBar.isDisposed())
						return;

					// Increment the progress bar
					progressBar.setSelection(progressBar.getSelection() + 1);

					if (progressBar.getSelection() == 100) {
						progressBar.setSelection(0);
					}

				}
				shell.close();
			}
		});
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
}
