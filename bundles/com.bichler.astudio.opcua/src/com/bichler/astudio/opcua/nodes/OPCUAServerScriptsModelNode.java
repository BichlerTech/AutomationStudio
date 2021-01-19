package com.bichler.astudio.opcua.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerScriptsModelNode extends OPCUAServerModelNode {
	public OPCUAServerScriptsModelNode() {
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
			OPCUAServerShellScriptsModelNode shell = new OPCUAServerShellScriptsModelNode();
			shell.setServerName(this.serverName);
			shell.setFilesystem(this.filesystem);
			nodes.add(shell);
			OPCUAServerEcmaScriptsModelNode ecma = new OPCUAServerEcmaScriptsModelNode();
			ecma.setServerName(this.serverName);
			ecma.setFilesystem(filesystem);
			nodes.add(ecma);
		}
		return nodes.toArray();
	}

	@Override
	public void nodeDBLClicked() {
		// do nothing here
		// IWorkbenchWindow window =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		// OPCUAUtil.openProjectPerspective(window, this);
	}
}
