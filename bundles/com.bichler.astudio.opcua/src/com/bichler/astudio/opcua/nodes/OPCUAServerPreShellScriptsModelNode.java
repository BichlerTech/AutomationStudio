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

public class OPCUAServerPreShellScriptsModelNode extends OPCUAServerShellScriptsModelNode {
	public OPCUAServerPreShellScriptsModelNode() {
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
			nodes = new ArrayList<>();
			if (filesystem != null && filesystem.isconnected()) {
				String path = filesystem.getRootPath() + filesystem.getTargetFileSeparator()
						+ "shellscripts/start.conf";
				if (filesystem.isFile(path)) {
					try (InputStream stream = filesystem.readFile(path);
							InputStreamReader streamreader = new InputStreamReader(stream);
							BufferedReader reader = new BufferedReader(streamreader);) {
						String line = "";
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("#presection")) {
								continue;
							}
							if (line.startsWith("#postsection")) {
								break;
							}
							if (line.startsWith("#")) {
								continue;
							}
							if (line.trim().isEmpty()) {
								continue;
							}
							OPCUAServerPreShellScriptModelNode node = new OPCUAServerPreShellScriptModelNode();
							node.setScriptName(line.replace(".es", ""));
							node.setServerName(this.serverName);
							node.setFilesystem(this.filesystem);
							nodes.add(node);
						}
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}
		return nodes.toArray();
	}
}
