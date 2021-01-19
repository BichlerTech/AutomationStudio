package opc.sdk.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Content;
import org.jdom.DataConversionException;
import org.jdom.Element;

/**
 * class contains all trusted peer certificates
 * 
 * @author thomas zoechbauer
 * 
 * @version 0.0.9
 *
 *          changed hannes bichler 12.09.2013
 * 
 */
public class TrustedPeerCertificates {
	public class PeerCertificate {
		private boolean enabled = false;
		private String certName = "";

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getCertName() {
			return certName;
		}

		public void setCertName(String certName) {
			this.certName = certName;
		}
	}

	private String storePath = "";
	private List<PeerCertificate> certificates = new ArrayList<>();
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * Creates an empty trustedpeercertificate object.
	 *
	 */
	public TrustedPeerCertificates() {
	}

	@SuppressWarnings("unchecked")
	TrustedPeerCertificates(Content content) {
		for (Content c : ((List<Content>) ((Element) content).getContent())) {
			if (c instanceof Element) {
				if (((Element) c).getName().equals(ApplicationConfigurationTags.StorePath.name())) {
					if (!((Element) c).getContent().isEmpty()) {
						this.storePath = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.Certificate.name())) {
					if (!((Element) c).getContent().isEmpty()) {
						PeerCertificate cert = new PeerCertificate();
						try {
							cert.setEnabled(((Element) c).getAttribute("enabled").getBooleanValue());
						} catch (DataConversionException e) {
							logger.log(Level.SEVERE, e.getMessage());
						}
						cert.setCertName(((Content) ((Element) c).getContent().get(0)).getValue());
						this.certificates.add(cert);
					}
				}
			}
		}
	}

	public List<PeerCertificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<PeerCertificate> certificates) {
		this.certificates = certificates;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}
}
