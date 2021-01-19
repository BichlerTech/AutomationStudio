package com.bichler.astudio.opcua.nodes.security;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class OPCUACertificateModelNode extends AbstractOPCUACertificateModelNode {

	private X509Certificate certificate = null;
	private String certificatePath;

	@Override
	public X509Certificate getCertificate() {
		return certificate;
	}

	public String getCertPath() {
		return this.certificatePath;
	}

	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

	public void setCertPath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	@Override
	public Image getLabelImage() {
		return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
				CommonImagesActivator.CERTIFICATE);
		// return StudioImageActivator
		// .getImage(StudioImages.ICON_OPCUACERTIFICATE);
	}

	@Override
	public String getLabelText() {
		return this.certificate.getSubjectX500Principal().getName();
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();

		if (nodes == null) {
			nodes = new ArrayList<>();
		}

		return nodes.toArray();
	}

	@Override
	public void refresh() {

	}
}
