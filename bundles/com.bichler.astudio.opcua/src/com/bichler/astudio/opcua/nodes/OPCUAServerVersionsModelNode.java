package com.bichler.astudio.opcua.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerVersionsModelNode extends OPCUAServerModelNode {
	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub
		// super.nodeDBLClicked();
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		if (nodes == null) {
			nodes = new ArrayList<>();
			String filepath = new Path(getFilesystem().getRootPath()).append("versions").toOSString();
			String[] versions;
			try {
				versions = getFilesystem().listFiles(filepath);
				if (versions != null) {
					for (String version : versions) {
						OPCUAServerVersionModelNode versionnode = new OPCUAServerVersionModelNode();
						versionnode.setServerName(serverName);
						versionnode.setFilesystem(filesystem);
						versionnode.setVersionName(version);
						versionnode.setParent(this);
						nodes.add(versionnode);
					}
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		return nodes.toArray();
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_OPCUAVERSION);
	}

	@Override
	public String getLabelText() {
		return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				this.getClass().getSimpleName() + ".LabelText");
	}
}
