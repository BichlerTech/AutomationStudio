package com.bichler.astudio.opcua.opcmodeler.dialogs.utils;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.opc.ExportNamespaceModelHandler;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorInput;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class MCPreferenceStoreUtil {
	public static final String HASINFORMATIONMODELCHANGED = "INFOMODELCHANGE";
	public static final String FLAG_DIRTY = "ISMODELDIRTY";
	public static final String FLAG_NAMESPACE = "ISNAMESPACE";
	public static final String NODE_DIRTY = "ISNODEDIRTY";

	/**
	 * Asks, if the model has been changed, to export it again
	 * 
	 * @param shell
	 */
	public static void preWindowSaveInformationModel(Shell shell) {
		IEclipsePreferences info = InstanceScope.INSTANCE.getNode(HASINFORMATIONMODELCHANGED);
		Preferences isDirtyNode = info.node(NODE_DIRTY);
		boolean isDirty = isDirtyNode.getBoolean(FLAG_DIRTY, false);
		String[] uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		if (isDirty) {
			List<String> ns2save = new ArrayList<>();
			for (int i = 0; i < uris.length; i++) {
				String value = isDirtyNode.get(uris[i], "");
				if (!value.isEmpty()) {
					ns2save.add(value);
				}
			}
			boolean confirmSave = MessageDialog.openConfirm(shell,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.save"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.save.confirm"));
			if (confirmSave) {
				// todo:
				// Export
				// mark not dirty
				ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getService(ICommandService.class);
				Command command = commandService.getCommand(ExportNamespaceModelHandler.ID);
				IParameter iParam = null;
				try {
					iParam = command.getParameter(ExportNamespaceModelHandler.PARAMETER_NAMESPACE);
				} catch (NotDefinedException e1) {
					e1.printStackTrace();
				}
				Parameterization[] parameterizations = new Parameterization[1];
				parameterizations[0] = new Parameterization(iParam,
						ExportNamespaceModelHandler.toStringIndexArray(ns2save.toArray(new String[0])));
				ParameterizedCommand pcmd = new ParameterizedCommand(command, parameterizations);
				final Event trigger = new Event();
				ExecutionEvent executionEvent = ((IHandlerService) PlatformUI.getWorkbench()
						.getService(IHandlerService.class)).createExecutionEvent(pcmd, trigger);
				Object result = null;
				try {
					result = command.executeWithChecks(executionEvent);
				} catch (NotDefinedException e) {
					e.printStackTrace();
				} catch (NotEnabledException e) {
					e.printStackTrace();
				} catch (NotHandledException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			try {
				if (info.nodeExists(NODE_DIRTY)) {
					isDirtyNode.removeNode();
				}
				info.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setHasInformationModelChanged(Shell shell, Integer... indizes) {
		IEclipsePreferences info = InstanceScope.INSTANCE.getNode(HASINFORMATIONMODELCHANGED);
		Preferences isDirtyNode = info.node(NODE_DIRTY);
		for (Integer index : indizes) {
			String nsUri = ServerInstance.getInstance().getServerInstance().getNamespaceUris().getUri(index);
			boolean isDirty = isDirtyNode.getBoolean(FLAG_DIRTY, false);
			// mark as dirty
			if (!isDirty) {
				isDirtyNode.putBoolean(FLAG_DIRTY, true);
			}
			// mark namespace
			isDirtyNode.put(nsUri, nsUri);
		}
		try {
			isDirtyNode.flush();
			info.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param shell
	 * @param selection
	 */
	public static void setHasInformationModelChanged(Shell shell, Node node) {
		NodeId nodeId = node.getNodeId();
		setHasInformationModelChanged(shell, nodeId.getNamespaceIndex());
	}

	public static void setHasInformationModelChanged(Shell shell, NodeEditorInput editorInput) {
		BrowserModelNode browserNode = editorInput.getNode();
		Node node = browserNode.getNode();
		setHasInformationModelChanged(shell, node);
	}

	/**
	 * Remove namespaces from list because they are currently exported, maybe set
	 * isdirty flag to false
	 * 
	 * @param shell
	 * @param allowedNamespaces
	 */
	public static void setExportedInformationModelNamespaces(Shell shell, List<Integer> allowedNamespaces) {
		IEclipsePreferences info = InstanceScope.INSTANCE.getNode(HASINFORMATIONMODELCHANGED);
		Preferences isDirtyNode = info.node(NODE_DIRTY);
		String[] uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		for (Integer nsIndex : allowedNamespaces) {
			String uri = uris[nsIndex];
			isDirtyNode.remove(uri);
		}
		List<String> remainingNamespaces = new ArrayList<>();
		for (String uri : uris) {
			String remaining = isDirtyNode.get(uri, "");
			if (!remaining.isEmpty()) {
				remainingNamespaces.add(remaining);
			}
		}
		if (remainingNamespaces.isEmpty()) {
			try {
				if (info.nodeExists(NODE_DIRTY)) {
					isDirtyNode.removeNode();
				}
				info.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}
}
