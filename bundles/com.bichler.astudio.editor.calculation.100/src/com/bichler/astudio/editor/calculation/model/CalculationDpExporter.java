package com.bichler.astudio.editor.calculation.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.hbsoft.driver.calculation.CalculationDP;
import com.hbsoft.driver.calculation.CalculationExpression;
import com.hbsoft.driver.calculation.CalculationNode;
import com.hbsoft.driver.calculation.CalculationObject;
import com.hbsoft.driver.calculation.targets.CalculationTarget;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.AbstractOPCDriverExporter;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

import opc.sdk.core.node.mapper.NodeIdMapper;

public class CalculationDpExporter extends AbstractOPCDriverExporter {

	public CalculationDpExporter(IFileSystem filesystem, String path) {
		super(filesystem, path);
	}

	@Override
	public StringBuffer build(List<IDriverNode> items, NamespaceTable nsTable) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buffer.append("<CalculationConfiguration>\n");
		buffer.append("  <calculations>\n");

		for (IDriverNode node : items) {
			buffer.append(saveChildren(node, nsTable));
		}

		buffer.append("  </calculations>\n");
		buffer.append("</CalculationConfiguration>\n");

		try (OutputStream out = filesystem.writeFile(path)) {
			write(out, buffer);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return buffer;
	}

	@Override
	protected void updateChild(IDriverNode item2update, OPCUAUpdateNodeIdEvent trigger) {
		CalculationModelNode item = (CalculationModelNode) item2update;

		CalculationDP dp = item.getDp();
		CalculationTarget target = dp.getTarget();

		NodeId newId = trigger.getNewId();
		NodeId oldId = trigger.getOldId();

		// List<CalculationObject> calculationExpression = dp
		// .getCalculationExpressions();
		// StringBuffer script = dp.getScript();
		if (target != null) {
			// StringBuffer targetString = target.getTarget();
			NodeId targetId = target.getTargetId();

//			NodeId newTargetId = NodeIdMapper.mapNamespaceIndex(targetId, mapping);
			// no nodeid
//			if (NodeId.isNull(newTargetId)) {
//				newTargetId = NodeId.NULL;
//			}

			if (oldId.equals(targetId)) {
				target.setTargetId(newId);
			}

		}
	}

	@Override
	protected void updateChild(IDriverNode item2update, Map<Integer, Integer> mapping) {
		CalculationModelNode item = (CalculationModelNode) item2update;

		CalculationDP dp = item.getDp();
		CalculationTarget target = dp.getTarget();
		// List<CalculationObject> calculationExpression = dp
		// .getCalculationExpressions();
		// StringBuffer script = dp.getScript();
		if (target != null) {
			// StringBuffer targetString = target.getTarget();
			NodeId targetId = target.getTargetId();
			NodeId newTargetId = NodeIdMapper.mapNamespaceIndex(targetId, mapping);
			// no nodeid
			if (NodeId.isNull(newTargetId)) {
				newTargetId = NodeId.NULL;
			}

			target.setTargetId(newTargetId);
		}
	}

	@Override
	protected StringBuffer saveChildren(IDriverNode item, NamespaceTable nsTable) {

		StringBuffer buffer = new StringBuffer();
		CalculationModelNode dpItem = (CalculationModelNode) item;

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
			nsUri = nsTable.getUri(dpItem.getDp().getTarget().getTargetId().getNamespaceIndex());
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
						nsUri = nsTable.getUri(nodeid.getNamespaceIndex());
					}
				} catch (IllegalArgumentException e) {
					// nodeid null
				}
				buffer.append("      <node ns=\"" + nsUri + "\" id=\"" + id + "\" name=\"" + obj.getContent()
						+ "\" index=\"" + ((CalculationNode) obj).getArrayIndex() + "\"/>\n");
			}
		}

		buffer.append("    </calc>\n");

		return buffer;
	}
}
