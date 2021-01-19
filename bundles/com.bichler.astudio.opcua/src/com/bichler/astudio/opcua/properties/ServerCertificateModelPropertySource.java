package com.bichler.astudio.opcua.properties;

import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.security.OPCUAKeyPairModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ServerCertificateModelPropertySource extends AbstractOPCPropertySource {
	private static final String PROPERTY_DESCRIPTION = "p_desc";
	private static final String PROPERTY_ALGORITHM = "p_algm";
	private static final String PROPERTY_ISSUER = "p_issuer";

	public ServerCertificateModelPropertySource(StudioModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_ISSUER,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.issuer")),
				new PropertyDescriptor(PROPERTY_DESCRIPTION,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description")),
				new PropertyDescriptor(PROPERTY_ALGORITHM,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.algorithm")) };
	}

	@Override
	public Object getPropertyValue(Object id) {
		KeyPair keyPair = getAdapter().getKeyPair();
		if (keyPair == null) {
			return "";
		}
		Cert cert = keyPair.getCertificate();
		if (cert == null) {
			return "";
		}
		X509Certificate certificate = cert.getCertificate();
		if (PROPERTY_ALGORITHM.equals(id)) {
			return certificate.getSigAlgName();
		}
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return "";
		}
		if (PROPERTY_ISSUER.equals(id)) {
			X500Principal issuer = certificate.getIssuerX500Principal();
			return issuer.getName();
		}
		return null;
	}

	@Override
	public OPCUAKeyPairModelNode getAdapter() {
		return (OPCUAKeyPairModelNode) super.getAdapter();
	}
}
