package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.edit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.encoding.IEncodeable;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserMethodModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableModelNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.node.OPCUAEditMethodArgumentWizard;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.server.service.node.UAServerVariableNode;

public class EditMethodArgumentsHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.editMethodArgs";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		if (selection == null) {
			return null;
		}
		final BrowserMethodModelNode node = (BrowserMethodModelNode) selection.getFirstElement();
		Argument[] inputArgs = getArgumentsFromNode(node, "InputArguments");
		Argument[] outputArgs = getArgumentsFromNode(node, "OutputArguments");
		OPCUAEditMethodArgumentWizard wizard = new OPCUAEditMethodArgumentWizard();
		wizard.setInputArgs(inputArgs);
		wizard.setOutputArgs(outputArgs);
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		if (WizardDialog.OK == dialog.open()) {
			// remove all nodes, create new one
			BrowserModelNode inputArgNode = changeArgumentChild(node, "InputArguments", wizard.getInputArgs());
			BrowserModelNode outputArgNode = changeArgumentChild(node, "OutputArguments", wizard.getOutputArgs());

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
			try {
				progressDialog.run(true, false, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"opc.message.extendmethod") + "...", IProgressMonitor.UNKNOWN);
						// create((BrowserModelNode) selection.getFirstElement(), info);
						try {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									// refresh
									DesignerUtils.refreshBrowserNode(node, true);
									if (inputArgNode != null) {
										DesignerUtils.refreshBrowserNode(inputArgNode, true);
									}
									if (outputArgNode != null) {
										DesignerUtils.refreshBrowserNode(outputArgNode, true);
									}
								}
							});
						} finally {
							monitor.done();
						}
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}

	private Node createArgumentNode(String nodeName, Argument[] arguments, NodeId argsNID, int nsIndex) {
		List<ExtensionObject> extObj = new ArrayList<>();
		for (Argument arg : arguments) {
			try {
				extObj.add(ExtensionObject.binaryEncode(arg, EncoderContext.getDefaultInstance()));
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		Variant value = new Variant(extObj.toArray(new ExtensionObject[extObj.size()]));
		// Variant value = new Variant(arguments);
		if (argsNID == null) {
			argsNID = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
					.getNextNodeId(nsIndex, null, IdType.Numeric, NodeIdMode.FILL);
		}
		VariableNode node = new UAServerVariableNode(argsNID, NodeClass.Variable, new QualifiedName(nodeName),
				new LocalizedText(nodeName), new LocalizedText(""), UnsignedInteger.ZERO, UnsignedInteger.ZERO,
				new ReferenceNode[0], value, Identifiers.Argument, ValueRanks.OneDimension.getValue(),
				new UnsignedInteger[] { UnsignedInteger.ONE }, AccessLevel.getMask(AccessLevel.READONLY),
				AccessLevel.getMask(AccessLevel.READONLY), 0.0, false);
		return node;
	}

	private BrowserModelNode changeArgumentChild(BrowserMethodModelNode node, String path, Argument[] arguments) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		BrowserModelNode[] children = node.getChildren();
		NodeId argsNID = null;
		int nsIndex = node.getNode().getNodeId().getNamespaceIndex();

		// remove argument node
		BrowserModelNode argNode = null;
		for (BrowserModelNode child : children) {
			argNode = child;
			Node cNode = child.getNode();
			QualifiedName browsename = cNode.getBrowseName();

			if (path.equalsIgnoreCase(browsename.getName())) {
				argsNID = child.getNode().getNodeId();
				// remove nodes from addrss space too
				DeleteNodesItem del = new DeleteNodesItem(argsNID, true);
				try {
					ServerInstance.deleteNodes(new DeleteNodesItem[] { del });
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
				break;
			}
		}

		if (arguments.length > 0) {
			Node argnode = createArgumentNode(path, arguments, argsNID, nsIndex);
			if (argsNID == null) {
				argsNID = argnode.getNodeId();
			}

//			List<ReferenceNode> refnodes = new ArrayList<>();
//			for (ReferenceNode fn : node.getNode().getReferences()) {
//				refnodes.add(fn);
//			}
//			refnodes.add(new ReferenceNode(Identifiers.HasProperty, false, new ExpandedNodeId(
//					nsTable.getUri(argnode.getNodeId().getNamespaceIndex()), argnode.getNodeId().getValue(), nsTable)));

			VariableAttributes va = new VariableAttributes();
			va.setAccessLevel(((VariableNode) argnode).getAccessLevel());
			va.setArrayDimensions(((VariableNode) argnode).getArrayDimensions());
			va.setDataType(((VariableNode) argnode).getDataType());
			va.setDescription(((VariableNode) argnode).getDescription());
			va.setDisplayName(((VariableNode) argnode).getDisplayName());
			va.setHistorizing(((VariableNode) argnode).getHistorizing());
			va.setMinimumSamplingInterval(((VariableNode) argnode).getMinimumSamplingInterval());
			va.setUserAccessLevel(((VariableNode) argnode).getUserAccessLevel());
			va.setUserWriteMask(((VariableNode) argnode).getUserWriteMask());
			va.setValue(new Variant((ExtensionObject[]) ((UAServerVariableNode) argnode).getValue().getValue()));
			va.setValueRank(((UAServerVariableNode) argnode).getValueRank());
			va.setWriteMask(((VariableNode) argnode).getWriteMask());

			try {
				AddNodesItem item = new AddNodesItem(
						new ExpandedNodeId(nsTable.getUri(node.getNode().getNodeId().getNamespaceIndex()),
								node.getNode().getNodeId().getValue(), nsTable),
						Identifiers.HasProperty,
						new ExpandedNodeId(nsTable.getUri(argsNID.getNamespaceIndex()), argsNID.getValue(), nsTable),
						new QualifiedName(path), NodeClass.Variable,
						ExtensionObject.binaryEncode(va, EncoderContext.getDefaultInstance()),
						new ExpandedNodeId(nsTable.getUri(Identifiers.PropertyType.getNamespaceIndex()),
								Identifiers.PropertyType.getValue(), nsTable));

				ServerInstance.addNode(new AddNodesItem[] { item }, false);
				
				((VariableNode) argNode.getNode()).setValue(new Variant(arguments));
				
				return argNode;
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}

		return null;
	}

	private Argument[] getArgumentsFromNode(BrowserMethodModelNode node, String path) {
		List<Argument> arguments = new ArrayList<>();
		BrowserModelNode[] children = node.getChildren();
		for (BrowserModelNode child : children) {
			Node cNode = child.getNode();
			QualifiedName browsename = cNode.getBrowseName();
			if (path.equalsIgnoreCase(browsename.getName())) {
				if (cNode instanceof VariableNode) {
					Variant variant = ((VariableNode) cNode).getValue();
					// empty
					if (variant.isEmpty()) {
						break;
					}
					// cast check value
					Object extValue = variant.getValue();
					if (!(extValue instanceof Argument[])) {
						break;
					}
					// add encoded argument values
					for (Argument extObj : (Argument[]) extValue) {
						// try {
						// decode argument values
						IEncodeable arg2add = extObj;
						// extObj.decode(EncoderContext.getDefaultInstance());
						// if (!(arg2add instanceof Argument)) {
						// continue;
						// }
						arguments.add((Argument) arg2add);
						// } catch (DecodingException e) {
						// e.printStackTrace();
						// }
					}
				}
			}
		}
		return arguments.toArray(new Argument[0]);
	}
}
