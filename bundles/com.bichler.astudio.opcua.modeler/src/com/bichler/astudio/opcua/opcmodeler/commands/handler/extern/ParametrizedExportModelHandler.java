package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorPart;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ParametrizedExportModelHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.extern.exportmodel";
	public static final String PARAMETER_FILE = "com.xcontrol.modeler.opc.extern.exportmodel.files";

	class ResultValue {
		boolean isSuccessFull = true;
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		boolean confirm = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.save"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.save.confirm.informationmodel"));
		if (!confirm) {
			return null;
		}
		IEditorPart[] dirtyEditors = HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage()
				.getDirtyEditors();
		if (dirtyEditors.length > 0) {
			boolean found = false;
			for (IEditorPart part : dirtyEditors) {
				if (part instanceof NodeEditorPart) {
					found = true;
					break;
				}
			}
			// Save node edit parts before saving the model
			if (found) {
				for (IEditorPart part : dirtyEditors) {
					if (part instanceof NodeEditorPart) {
						part.doSave(null);
					}
				}
			}
		}
		// start progress monitor
		final ResultValue rv = new ResultValue();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				// fetch parameters from command
				String param = event.getParameter(PARAMETER_FILE);
				boolean isSuccessFull = DesignerUtils.doSaveOPCInformationModel(monitor, param);
				rv.isSuccessFull = isSuccessFull;
				monitor.done();
			}
		};
		try {
			dialog.run(true, true, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return !rv.isSuccessFull;
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
