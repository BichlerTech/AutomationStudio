package com.bichler.astudio.opcua.nodes.security;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;

public abstract class AbstractOPCUAServerCertificateStoreModelNode extends StudioModelNode {

	// private CertificateType type;

	public AbstractOPCUAServerCertificateStoreModelNode() {
		// this.type = type;
	}

	private String serverName = "";

	private String certStorePath = "";

	private String name = "";

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getCertStorePath() {
		return certStorePath;
	}

	public void setCertTypePath(String certKeyName) {
		this.certStorePath = certKeyName;
	}

	@Override
	public void nodeDBLClicked() {

	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_OPCUACERTIFICATE);
	}

	@Override
	public String getLabelText() {
		return this.name;
	}

	@Override
	public void refresh() {

	}

	public void setName(String name) {
		this.name = name;
	}
}
