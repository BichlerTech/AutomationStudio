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

public class OPCUAServerPostShellScriptsModelNode extends OPCUAServerShellScriptsModelNode {
	public OPCUAServerPostShellScriptsModelNode() {
		super();
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
			if (filesystem != null && filesystem.isconnected()) {
				String path = filesystem.getRootPath() + filesystem.getTargetFileSeparator() + "shellscripts"
						+ filesystem.getTargetFileSeparator() + "start.conf";
				if (filesystem.isFile(path)) {
					BufferedReader reader = null;
					try {
						InputStream stream = filesystem.readFile(path);
						InputStreamReader streamreader = new InputStreamReader(stream);
						reader = new BufferedReader(streamreader);
						String line = "";
						boolean postSection = false;
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("#presection")) {
								continue;
							}
							if (line.startsWith("#postsection")) {
								postSection = true;
								continue;
							}
							if (line.startsWith("#")) {
								continue;
							}
							if (line.trim().isEmpty()) {
								continue;
							}
							if (postSection) {
								OPCUAServerPostShellScriptModelNode node = new OPCUAServerPostShellScriptModelNode();
								node.setServerName(getServerName());
								node.setScriptName(line.replace(".es", ""));
								node.setFilesystem(this.filesystem);
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
}
