package com.bichler.astudio.p2.handler;

import org.eclipse.equinox.p2.ui.LoadMetadataRepositoryJob;

public class InstallNewSoftwareHandler extends PreloadingRepositoryHandler {

	@Override
	protected void doExecute(LoadMetadataRepositoryJob job) {
		getProvisioningUI().openInstallWizard(null, null, job);
	}

}
