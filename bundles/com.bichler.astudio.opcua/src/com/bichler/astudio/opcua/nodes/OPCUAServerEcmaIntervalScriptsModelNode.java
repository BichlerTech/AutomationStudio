package com.bichler.astudio.opcua.nodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class OPCUAServerEcmaIntervalScriptsModelNode extends StudioModelNode {
	private String scriptName = "";
	private String serverName = "";

	public OPCUAServerEcmaIntervalScriptsModelNode() {
		super();
		// children = new ArrayList<VisuModelNode>();
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_SCRIPTS);
	}

	@Override
	public String getLabelText() {
		return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				this.getClass().getSimpleName() + ".LabelText");
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		if (nodes == null) {
			nodes = new ArrayList<StudioModelNode>();
			boolean intervalSect = false;
			OPCUAServerEcmaScriptModelNode node = null;
			if (filesystem != null && filesystem.isconnected()) {
				// IPreferenceStore store =
				// ComponentsActivator.getDefault().getPreferenceStore();
				// IPreferenceStore opcuastore =
				// OPCUAActivator.getDefault().getPreferenceStore();
				String path = filesystem.getRootPath() + filesystem.getTargetFileSeparator() + "ecmascripts"
						+ filesystem.getTargetFileSeparator() + "start.conf";
				int interval = 0;
				if (filesystem.isFile(path)) {
					BufferedReader reader = null;
					try {
						InputStream stream = filesystem.readFile(path);
						InputStreamReader streamreader = new InputStreamReader(stream);
						reader = new BufferedReader(streamreader);
						String line = "";
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("#interval")) {
								intervalSect = true;
								continue;
							}
							if (line.startsWith("##")) {
								try {
									interval = Integer.parseInt(line.replace("##", "").trim());
								} catch (NumberFormatException ex) {
									interval = 0;
								}
								continue;
							}
							if (line.startsWith("#")) {
								continue;
							}
							if (line.trim().isEmpty()) {
								continue;
							}
							if (intervalSect) {
								node = new OPCUAServerEcmaScriptModelNode();
								node.setScriptName(line.replace(".js", ""));
								node.setServerName(this.getServerName());
								node.setFilesystem(filesystem);
								node.setInterval(interval);
								node.setParent(this);
								nodes.add(node);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return nodes.toArray();
	}

	@Override
	public void refresh() {
	}
}
