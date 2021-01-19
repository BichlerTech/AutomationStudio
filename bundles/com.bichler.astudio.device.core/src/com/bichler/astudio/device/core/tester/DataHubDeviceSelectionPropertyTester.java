package com.bichler.astudio.device.core.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.preference.DevicePreferenceConstants;
import com.bichler.astudio.device.core.view.OPCUADeviceView;

public class DataHubDeviceSelectionPropertyTester extends PropertyTester {

	public DataHubDeviceSelectionPropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();

		if (!(part instanceof OPCUADeviceView)) {
			return false;
		}

		IStructuredSelection selection = (IStructuredSelection) part.getSite().getSelectionProvider().getSelection();
		if (selection == null) {
			return false;
		}

		boolean empty = selection.isEmpty();
		if (empty) {
			return false;
		}

		Preferences fe = (Preferences) selection.getFirstElement();
		String type = fe.get(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, "");
		switch (type) {
		case "0":
			return true;
		default:
			return false;
		}
	}

}
