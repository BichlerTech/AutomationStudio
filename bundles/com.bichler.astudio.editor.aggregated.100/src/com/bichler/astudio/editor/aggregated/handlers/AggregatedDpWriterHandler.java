package com.bichler.astudio.editor.aggregated.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.editor.aggregated.dp.AggregatedDPItem;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpModelNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class AggregatedDpWriterHandler extends AbstractOPCUADPWriterHandler {

	public static final String ID = "com.bichler.astudio.editor.aggregated.1.0.0.export";

	@Override
	protected void write(IFileSystem fs, String path, List<Object> datapoints) {
		StringBuffer buffer = new StringBuffer();
		String[] ns = null;

		NamespaceTable uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		buffer.append("#ns;id	browsepath	targetdisplayname	#ns;id	targetbrowsepath	active	read	write\n");
		for (Object item : datapoints) {
			AggregatedDPItem dp = ((AggregatedDpModelNode) item).getDPItem();

			// generate nodeid value
			if (dp.getServerNodeId() == null) {
				continue;
			}
			String[] idelements = dp.getServerNodeId().toString().split(";");
			String id = "";
			if (idelements != null) {
				if (idelements.length == 1) {
					id = idelements[0];
				} else if (idelements.length == 2) {

					id = idelements[1];
				}
			}

			String targetid = "";
			if (dp.getTargetNodeId() == null) {
				targetid = dp.getLoadedTargetNodeId();
			} else {
				String[] targetidelements = dp.getTargetNodeId().toString().split(";");

				if (targetidelements != null) {
					if (targetidelements.length == 1) {
						targetid = targetidelements[0];
					} else if (targetidelements.length == 2) {

						targetid = targetidelements[1];
					}
				}
			}

			buffer.append(uris.getUri(dp.getServerNodeId().getNamespaceIndex()) + ";" + id + "\t"
					+ dp.getServerBrowsePath() + "\t");

			if (ns != null && ns.length > 0 && dp.getTargetNodeId() != null) {
				String namespace = ns[dp.getTargetNodeId().getNamespaceIndex()];
				targetid = namespace + ";" + targetid;
			}

			if (targetid == null) {
				targetid = "";
			}

			buffer.append(dp.getTargetDisplayName() + "\t" + targetid + "\t" + dp.getTargetBrowsePath() + "\t"
					+ dp.isActive() + "\t" + dp.isRead() + "\t" + dp.isWrite() + "\n");
		}

		try (OutputStream out = fs.writeFile(path)) {
			// first we have to clear file
			out.write(new byte[0]);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		try (OutputStream out = fs.writeFile(path)) {
			out.write(buffer.toString().getBytes());
			out.flush();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
