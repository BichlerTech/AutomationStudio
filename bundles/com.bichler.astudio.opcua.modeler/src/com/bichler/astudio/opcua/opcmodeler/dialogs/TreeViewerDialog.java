package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.ResourceManager;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class TreeViewerDialog extends Dialog {
	private static final int EXPAND_TIMER = 1;
	private static final int EXPAND_STEP = 20;
	private Composite treeViewerParent = null;
	private TreeViewer treeViewer = null;
	private String labelText = "";
	private int positionX = 0;
	private int positionY = 0;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public TreeViewerDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.SYSTEM_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		Label lblNewLabel = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 125;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText(this.labelText);
		this.treeViewer.getControl().setParent(container);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "TreeViewerDialog.lbl_minimize.text"));
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getShell().close();
			}
		});
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/close_fullsize.png"));
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		getShell().setBounds(this.positionX, this.positionY, 10, 10);
		this.fadeIn();
		return container;
	}

	public void setPosition(int x, int y) {
		this.positionX = x;
		this.positionY = y;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(0, 0);
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public void setLabelText(String text) {
		this.labelText = text;
	}

	public void setTreeViewer(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
		this.treeViewerParent = treeViewer.getTree().getParent();
	}

	private void fadeIn() {
		Runnable run = new Runnable() {
			final Shell shell = getShell();

			@Override
			public void run() {
				try {
					if (shell.getBounds().height > 600) {
						return;
					}
					shell.setBounds(shell.getBounds().x - EXPAND_STEP / 2, shell.getBounds().y - EXPAND_STEP / 2,
							shell.getBounds().width + EXPAND_STEP, shell.getBounds().height + EXPAND_STEP);
					Display.getDefault().timerExec(EXPAND_TIMER, this);
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		};
		Display.getDefault().timerExec(EXPAND_TIMER, run);
	}

	private void fadeOut() {
		Runnable run = new Runnable() {
			final Shell shell = getShell();

			@Override
			public void run() {
				try {
					if (shell.getBounds().height < 25) {
						getShell().close();
						return;
					}
					/** calculate width and height */
					int width = shell.getBounds().width;
					width = width - EXPAND_STEP;
					if (width < 0) {
						width = 0;
					}
					int height = shell.getBounds().height;
					height = height - EXPAND_STEP;
					if (height < 0) {
						height = 0;
					}
					shell.setBounds(shell.getBounds().x + (EXPAND_STEP / 2), shell.getBounds().y + (EXPAND_STEP / 2),
							width, height);
					Display.getDefault().timerExec(EXPAND_TIMER, this);
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		};
		Display.getDefault().timerExec(EXPAND_TIMER, run);
	}

	@Override
	public boolean close() {
		if (getShell().getBounds().height > 25) {
			fadeOut();
			return false;
		} else {
			treeViewer.getControl().setParent(treeViewerParent);
			return super.close();
		}
	}
}
