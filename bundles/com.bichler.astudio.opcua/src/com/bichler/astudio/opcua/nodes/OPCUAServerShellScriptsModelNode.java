package com.bichler.astudio.opcua.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerShellScriptsModelNode extends OPCUAServerScriptsModelNode {
	public OPCUAServerShellScriptsModelNode() {
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
			OPCUAServerPreShellScriptsModelNode prenode = new OPCUAServerPreShellScriptsModelNode();
			prenode.setServerName(this.serverName);
			prenode.setFilesystem(this.filesystem);
			nodes.add(prenode);
			OPCUAServerPostShellScriptsModelNode postnode = new OPCUAServerPostShellScriptsModelNode();
			postnode.setServerName(this.serverName);
			postnode.setFilesystem(this.filesystem);
			nodes.add(postnode);
		}
		return nodes.toArray();
	}
}
