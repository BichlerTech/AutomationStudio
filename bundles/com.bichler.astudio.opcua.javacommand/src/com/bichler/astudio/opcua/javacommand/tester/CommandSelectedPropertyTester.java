package com.bichler.astudio.opcua.javacommand.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.javacommand.view.JavaCommandView;

public class CommandSelectedPropertyTester extends PropertyTester {

	public CommandSelectedPropertyTester() {

	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();

		if (!(part instanceof JavaCommandView)) {
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
