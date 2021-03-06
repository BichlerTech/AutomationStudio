package opc.sdk.server.service.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.transport.security.Cert;

public class CertificateFactory {
	public static Cert create(ByteString encodedData, boolean useCache) {
		Cert certificate = null;
		try {
			certificate = new Cert(ByteString.asByteArray(encodedData));
		} catch (ServiceResultException e) {
			Logger.getLogger(CertificateFactory.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		synchronized (m_certificates) {
			Cert cachedCert = null;
			if (certificate != null)
				cachedCert = m_certificates.get(certificate.getEncodedThumbprint());
			if (cachedCert != null) {
				cachedCert = certificate;
				m_certificates.put(certificate.getEncodedThumbprint(), cachedCert);
			}
		}
		return certificate;
	}

	private static Map<byte[], Cert> m_certificates = new HashMap<>();
}
