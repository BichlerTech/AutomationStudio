package com.bichler.astudio.device.opcua.dialog;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class UploadProgressDialog extends ProgressMonitorDialog {

	private DoCancle cancleOperation;

	public UploadProgressDialog(Shell parent) {
		super(parent);
	}

	public Button getCancleButton() {
		return this.cancel;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// TODO Auto-generated method stub
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void cancelPressed() {

		if (this.cancleOperation != null) {
			this.cancleOperation.cancle();
		}

		super.cancelPressed();
	}

	public DoCancle getCancle() {
		return this.cancleOperation;
	}

	public void setCancle(DoCancle cancle) {
		this.cancleOperation = cancle;
	}

	
}
