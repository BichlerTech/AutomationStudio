package com.bichler.astudio.opcua.nodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class OPCUAServerEcmaSingleScriptsModelNode extends StudioModelNode {
	private String scriptName = "";
	private String serverName = "";
	private int active = 1;

	public OPCUAServerEcmaSingleScriptsModelNode() {
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
			OPCUAServerEcmaScriptModelNode node = null;
			if (filesystem != null && filesystem.isconnected()) {
				// IPreferenceStore store =
				// ComponentsActivator.getDefault().getPreferenceStore();
				// IPreferenceStore opcuastore =
				// OPCUAActivator.getDefault().getPreferenceStore();
				String path = filesystem.getRootPath() + filesystem.getTargetFileSeparator() + "ecmascripts"
						+ filesystem.getTargetFileSeparator() + "start.conf";
				if (filesystem.isFile(path)) {
					BufferedReader reader = null;
					try {
						InputStream stream = filesystem.readFile(path);
						InputStreamReader streamreader = new InputStreamReader(stream);
						reader = new BufferedReader(streamreader);
						String line = "";
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("#single")) {
								continue;
							}
							if (line.startsWith("#interval")) {
								break;
							}
							if (line.startsWith("#")) {
								continue;
							}
							if (line.trim().isEmpty()) {
								continue;
							}
							node = new OPCUAServerEcmaScriptModelNode();
							String[] items = line.split(":");
							if (items != null && items.length > 1) {
								try {
									setActive(Integer.parseInt(items[0]));
								} catch (NumberFormatException ex) {
									Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
								}
								line = items[1];
							}
							node.setScriptName(line.replace(".js", ""));
							node.setServerName(this.getServerName());
							node.setFilesystem(filesystem);
							node.setParent(this);
							nodes.add(node);
						}
						reader.close();
						streamreader.close();
						stream.close();
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
		// TODO Auto-generated method stub
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
}
