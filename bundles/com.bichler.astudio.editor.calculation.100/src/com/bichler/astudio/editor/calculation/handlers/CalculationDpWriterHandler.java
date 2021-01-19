package com.bichler.astudio.editor.calculation.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.hbsoft.driver.calculation.CalculationExpression;
import com.hbsoft.driver.calculation.CalculationNode;
import com.hbsoft.driver.calculation.CalculationObject;
import com.bichler.astudio.editor.calculation.model.CalculationModelNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class CalculationDpWriterHandler extends AbstractOPCUADPWriterHandler {

	public static final String ID = "com.bichler.astudio.drv.export.calculation";

	@Override
	protected void write(IFileSystem fs, String path, List<Object> datapoints) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buffer.append("<CalculationConfiguration>\n");
		buffer.append("  <calculations>\n");
		for (Object comp : datapoints) {
			String buf = toStringCalculationItem((CalculationModelNode) comp);
			buffer.append(buf);
			// CalculationDP dp = ((CalculationItem) comp).getDp();
			// buffer.append(dp.toString());
		}
		buffer.append("  </calculations>\n");
		buffer.append("</CalculationConfiguration>\n");

		try (OutputStream out = fs.writeFile(path)) {
			out.write(buffer.toString().getBytes());
			out.flush();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}

	}

	private String toStringCalculationItem(CalculationModelNode dpItem) {
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
		buffer.append("      <calcevent value=\"" + dpItem.getDp().getEvent() + "\" timeout=\""
				+ dpItem.getDp().getTimeout() / 1000000 + "\"/>\n");

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
