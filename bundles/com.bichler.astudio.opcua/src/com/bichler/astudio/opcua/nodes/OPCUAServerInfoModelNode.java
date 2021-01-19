package com.bichler.astudio.opcua.nodes;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;

public class OPCUAServerInfoModelNode extends OPCUAServerInfoModelsNode {

	private String infModelName = "";

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getInfModelName() {
		return infModelName;
	}

	public void setInfModelName(String infModelName) {
		this.infModelName = infModelName;
	}

	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_OPCUAINFORMATIONMODEL);
	}

	@Override
	public String getLabelText() {
		return this.infModelName;
	}

	@Override
	public Object[] getChildren() {
		return new Object[0];
	}

	@Override
	public boolean useNavigationComperator() {
		return false;
	}
}
