package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.addressspace.model.gen.OPCClassModelFactory;
import com.bichler.astudio.opcua.addressspace.wizard.java.JavaClassGeneratorWizard;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class OPCUACompileAddressSpaceHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.addressspace.compile";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		// DirectoryDialog dialog = new DirectoryDialog(window.getShell());
		// String destination = dialog.open();
		// if (destination == null) {
		// return null;
		// }

		JavaClassGeneratorWizard wizard = new JavaClassGeneratorWizard();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);

		int open = dialog.open();
		if (WizardDialog.OK == open) {
			String constantsName = wizard.getConstantsName();
			String destination = wizard.getDestination();
			String packagename = wizard.getPackageName();

			OPCClassModelFactory factory = new OPCClassModelFactory(ServerInstance.getInstance());

			factory.setConstantsName(constantsName);
			factory.setPackageName(packagename);

			factory.writeClasses(destination, packagename);
		}
		return null;
	}
}
