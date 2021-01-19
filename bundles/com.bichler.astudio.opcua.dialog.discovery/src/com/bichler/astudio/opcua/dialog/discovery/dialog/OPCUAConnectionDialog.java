package com.bichler.astudio.opcua.dialog.discovery.dialog;

import org.eclipse.swt.widgets.Shell;

public class OPCUAConnectionDialog extends AbstractOPCDiscoveryDialog{

	public OPCUAConnectionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected boolean isValidDiscovery() {
		return false;
	}

}
