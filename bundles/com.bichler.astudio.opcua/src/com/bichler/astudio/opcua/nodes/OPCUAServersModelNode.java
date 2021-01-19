package com.bichler.astudio.opcua.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.connections.nodes.HostConnectionModelNode;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.hbsoft.visu.Comet_ResourceManager;

public class OPCUAServersModelNode extends HostConnectionModelNode {
	protected Comet_ResourceManager manager = null;

	public OPCUAServersModelNode() {
		super();
	}

	@Override
	public Image getLabelImage() {
		return null;
	}

	@Override
	public String getLabelText() {
		return filesystem.getConnectionName();
	}

	@Override
	public Object[] getChildren() {
		/** load all servers */
		List<StudioModelNode> nodes = this.getChildrenList();
		// check if we have previously browsed children
		if (nodes == null) {
			nodes = new ArrayList<>();
			OPCUAServerModelNode node = null;
			if (filesystem instanceof SimpleFileSystem) {
				Comet_ResourceManager manager = null;
				node = new OPCUAServerModelNode();
				node.setServerName(filesystem.getConnectionName());
				node.setFilesystem(filesystem);
				// create comet resource manager
				manager = Comet_ResourceManager.getManager();
				// set the resourcemanager to node
				node.setManager(manager);
				nodes.add(node);
			}
			this.setChildren(nodes);
		}
		return nodes.toArray();
	}

	public Comet_ResourceManager getManager() {
		return manager;
	}

	public void setManager(Comet_ResourceManager manager) {
		this.manager = manager;
	}
}
