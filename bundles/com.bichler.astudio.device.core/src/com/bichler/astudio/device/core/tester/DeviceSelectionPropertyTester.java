package com.bichler.astudio.device.core.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.device.core.view.OPCUADeviceView;


public class DeviceSelectionPropertyTester extends PropertyTester {

	public DeviceSelectionPropertyTester() {
		
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();

		if (!(part instanceof OPCUADeviceView)) {
			return false;
		}

		ISelection selection = part.getSite().getSelectionProvider()
				.getSelection();
		if (selection == null) {
			return false;
		}
		return !selection.isEmpty();
	}

}
