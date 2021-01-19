package com.bichler.astudio.opcua.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerEcmaScriptsModelNode extends OPCUAServerScriptsModelNode {
	public OPCUAServerEcmaScriptsModelNode() {
		super();
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
			String serverName = this.getServerName();
			OPCUAServerEcmaSingleScriptsModelNode single = new OPCUAServerEcmaSingleScriptsModelNode();
			single.setServerName(serverName);
			single.setFilesystem(filesystem);
			OPCUAServerEcmaIntervalScriptsModelNode interval = new OPCUAServerEcmaIntervalScriptsModelNode();
			interval.setServerName(serverName);
			interval.setFilesystem(filesystem);
			nodes.add(single);
			nodes.add(interval);
		}
		return nodes.toArray();
	}
}
