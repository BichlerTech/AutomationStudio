package com.bichler.astudio.opcua.opcmodeler.commands.handler.opc;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.export.ExportWizard;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ExportNamespaceModelHandler extends AbstractHandler {
	public static final String ID = "commands.designer.export";
	public static final String PARAMETER_NAMESPACE = "commands.designer.export.parameter.namespace";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart[] dirtyEditors = HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage()
				.getDirtyEditors();
		if (dirtyEditors.length > 0) {
			boolean confirm = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.dialog.save.title"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"opc.message.dialog.save.description"));
			// MessageBox messageBox = new
			// MessageBox(HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell(),
			// SWT.ICON_WARNING | SWT.YES | SWT.NO);
			//
			// if (dirtyEditors.length == 1) {
			// messageBox.setMessage("There is an unsaved model element!\n Do
			// you want to save it?");
			// } else {
			// messageBox.setMessage("There are some unsaved model elements!\n
			// Do you want to save them?");
			// }
			//
			// messageBox.setText("Save Model Elements");
			//
			// int response = messageBox.open();
			//
			// if (response == SWT.YES) {
			if (confirm) {
				for (IEditorPart part : dirtyEditors) {
					part.doSave(null);
				}
			}
		}
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		String param = event.getParameter(PARAMETER_NAMESPACE);
		String[] indizes2export = toArrayIndexString(param);
		ExportWizard wizard = new ExportWizard();
		wizard.setSelectedIndizes(indizes2export);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.setPageSize(600, 420);
		int result = dialog.open();
		if (result == WizardDialog.OK) {
			MCPreferenceStoreUtil.setExportedInformationModelNamespaces(shell, wizard.getAllowedNamespaces());
		}
		return null;
	}

	public static String[] toArrayIndexString(String namespaceIndizes) {
		if (namespaceIndizes != null && !namespaceIndizes.isEmpty()) {
			return namespaceIndizes.split("\t");
		}
		return new String[0];
	}

	public static String toStringIndexArray(String[] namespaceIndizes) {
		if (namespaceIndizes != null && namespaceIndizes.length > 0) {
			StringBuilder to = new StringBuilder();
			for (int i = 0; i < namespaceIndizes.length - 1; i++) {
				to.append(namespaceIndizes[i]);
				to.append("\t");
			}
			to.append(namespaceIndizes[namespaceIndizes.length - 1]);
			return to.toString();
		}
		return null;
	}
}
