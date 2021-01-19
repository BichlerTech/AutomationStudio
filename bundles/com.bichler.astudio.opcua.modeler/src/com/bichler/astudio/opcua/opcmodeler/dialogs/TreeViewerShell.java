package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;

public class TreeViewerShell extends Shell {
	private static final int FADE_TIMER = 10;
	private static final int FADE_IN_STEP = 5;

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public TreeViewerShell(Display display) {
		super(display, SWT.RESIZE | SWT.PRIMARY_MODAL);
		setTouchEnabled(true);
		setMinimumSize(new Point(10, 10));
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(10, 10);
		this.fadeIn(getShell());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void fadeIn(final Shell _shell) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				try {
					if (_shell == null || _shell.isDisposed()) {
						return;
					}
					// int cur = _shell.getAlpha();
					// cur += FADE_IN_STEP;
					if (_shell.getBounds().height > 400) {
						// _shell.setAlpha(FINAL_ALPHA);
						// startTimer(_shell);
						return;
					}
					_shell.setSize(_shell.getBounds().height + 10, _shell.getBounds().width + 10);
					// _shell.setAlpha(cur);
					Display.getDefault().timerExec(FADE_TIMER, this);
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		};
		Display.getDefault().timerExec(FADE_TIMER, run);
	}
}
