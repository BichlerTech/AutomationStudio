package com.bichler.astudio.device.opcua.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.licensemanagement.util.LicenseUtil;

public class DefaultTestcenterHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.device.testcenter";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	  LicenseUtil.openLicenseErrorStartup("License Manager", "No valid license for Testcenter plugin found!", "The Testcenter Plugin is an extension to test some OPC UA Server direct on the development machine. " );
		return null;
	}

}
