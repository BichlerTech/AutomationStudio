package com.bichler.astudio.editor.calculation.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.opc.driver.calculation.CalculationDP;
import com.bichler.opc.driver.calculation.CalculationExpression;
import com.bichler.opc.driver.calculation.CalculationNode;
import com.bichler.opc.driver.calculation.CalculationObject;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.editor.calculation.model.CalculationDPEditorImporter;
import com.bichler.astudio.editor.calculation.model.CalculationModelNode;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUAUpdateNodeIdHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class OPCUACalculationUpdateNodeIdHandler extends AbstractOPCUAUpdateNodeIdHandler {

	public static final String ID = "command.update.opcua.nodeid.driver.calculation";

	@Override
	public void onUpdateDatapoints(ASUpdateable updateable, String path) {
		OPCUAUpdateNodeIdEvent trigger = (OPCUAUpdateNodeIdEvent) updateable.getTrigger();

		InputStream input = null;
		try {
			String datapointsPath = new Path(path).append("datapoints.com").toOSString();

			if (!updateable.getFilesystem().isFile(datapointsPath)) {
				return;
			}
			input = updateable.getFilesystem().readFile(datapointsPath);

			CalculationDPEditorImporter importer = new CalculationDPEditorImporter();
			List<CalculationDP> dps = importer.loadDPs(input,
					ServerInstance.getInstance().getServerInstance().getNamespaceUris(), null, -1);

			List<IDriverNode> driverNodes = new ArrayList<>();

			NodeId newId = trigger.getNewId();
			NodeId oldId = trigger.getOldId();

			for (CalculationDP dp : dps) {
				CalculationModelNode item = new CalculationModelNode();
				item.setDP(dp);
				driverNodes.add(item);

				if (oldId.equals(item.getDp().getTarget().getTargetId())) {
					item.getDp().getTarget().setTargetId(newId);
				}

				List<CalculationObject> expression = item.getDp().getCalculationExpressions();
				for (CalculationObject obj : expression) {
					if (obj instanceof CalculationNode) {
						String content = ((CalculationNode) obj).getContent();
						NodeId nodeId = NodeId.parseNodeId(content);
						if (oldId.equals(nodeId)) {
							((CalculationNode) obj).setContent(newId.toString());
						}
					}
				}
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			buffer.append("<CalculationConfiguration>\n");
			buffer.append("  <calculations>\n");

			for (IDriverNode node : driverNodes) {
				buffer.append(saveChildren((CalculationModelNode) node));
			}

			buffer.append("  </calculations>\n");
			buffer.append("</CalculationConfiguration>\n");

			OutputStream output = null;
			try {
				if (updateable.getFilesystem().isFile(datapointsPath)) {
					output = updateable.getFilesystem().writeFile(datapointsPath);
					output.write(buffer.toString().getBytes());
					output.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String saveChildren(CalculationModelNode dpItem) {
		StringBuffer buffer = new StringBuffer();
		NamespaceTable uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		String id = "";
		String[] idelements = null;

		if (!NodeId.isNull(dpItem.getDp().getTarget().getTargetId())) {
			idelements = dpItem.getDp().getTarget().getTargetId().toString().split(";");
			if (idelements != null) {
				if (idelements.length == 1) {
					id = idelements[0];
				} else if (idelements.length == 2) {
					id = idelements[1];
				}
			}
		}

		buffer.append("    <calc>\n");
		buffer.append("      <target ns=\"");
		String nsUri = "";
		if (!NodeId.isNull(dpItem.getDp().getTarget().getTargetId())) {
			nsUri = uris.getUri(dpItem.getDp().getTarget().getTargetId().getNamespaceIndex());
		}
		buffer.append(nsUri);

		buffer.append("\" id=\"" + id + "\" name=\"" + dpItem.getName() + "\" index=\"" + dpItem.getDp().getArrayindex()
				+ "\"/>\n");
		buffer.append("      <active value=\"" + dpItem.getDp().isActive() + "\" />\n");
		buffer.append("      <calcevent value=\"" + dpItem.getDp().getEvent().name() + "\" timeout=\""
				+ dpItem.getDp().getTimeout() + "\"/>\n");

		for (CalculationObject obj : dpItem.getDp().getCalculationExpressions()) {
			if (obj instanceof CalculationExpression) {
				buffer.append("      <operation value=\""
						+ obj.getContent().replace("<", "$lower$").replace(">", "$greater$") + "\" />\n");
			} else if (obj instanceof CalculationNode) {
				// String[] txtDisplay = obj.getContent().split(" ");
				// NodeId nodeid = NodeId
				// .decode(txtDisplay[txtDisplay.length - 1]);

				id = "";
				nsUri = "";

				try {
					NodeId nodeid = NodeId.parseNodeId(((CalculationNode) obj).getContent());
					idelements = nodeid.toString().split(";");
					// id value
					if (idelements != null) {
						if (idelements.length == 1) {
							id = idelements[0];
						} else if (idelements.length == 2) {
							id = idelements[1];
						}
						// id namespace
						nsUri = uris.getUri(nodeid.getNamespaceIndex());
					}
				} catch (IllegalArgumentException e) {
					// nodeid null
				}
				buffer.append("      <node ns=\"" + nsUri + "\" id=\"" + id + "\" name=\"" + obj.getContent()
						+ "\" index=\"" + ((CalculationNode) obj).getArrayIndex() + "\"/>\n");
			}
		}

		buffer.append("    </calc>\n");
		return buffer.toString();
	}

}
