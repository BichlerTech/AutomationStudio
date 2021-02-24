package opc.sdk.server.service.session;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.transport.ServerSecureChannel;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.application.SecurityCertificateAdministration;

public class OPCSessionConfigurator {
	/** Maximum allowed Sessions */
	private UnsignedInteger maxSessionCount = null;
	/** Maximum timout for a Session */
	private double maxSessionTimeout = -1;
	/** Minimum timout for a Session */
	private double minSessionTimeout = -1;
	/** Maximum age for a Request */
	private UnsignedInteger maxRequestAge = null;
	/** Maximum ContinuationPoints stored by a browse */
	private int maxBrowseContinuationPoints = -1;
	/** Maximum ContinuationPoints stored by History Access */
	private int maxHistoryContinuationPoints = -1;
	/** Minimum NonceLength used by the Security */
	// private int minNonceLength = -1;
	/** maximum requested message size */
	private UnsignedInteger maxRequestedMessageSize = null;
	private long creationTimeCertificate = -1, creationTimePrivKey = -1;
	private SecurityCertificateAdministration securityCert;
	// private Map<String, SecurityCertificateAdministration> securityCerts = new
	// HashMap<>();
	private Runnable runnable = null;
	private int certificateValidity;

	public OPCSessionConfigurator() {
		initDefault();
	}

	public int getMinNonceLength(ServerSecureChannel channel) {
		// return channel.getSecurityPolicy().getSecureChannelNonceLength();
		return 32;
	}

	public void initWatchCertificate(final Application application, final String pathCert, final String pathKey) {
		this.runnable = new Runnable() {
			@Override
			public void run() {
				File certificate = new File(pathCert);
				File key = new File(pathKey);
				// cannot find files
				if (!certificate.exists() || !key.exists()) {
					return;
				}
				long lmCert = certificate.lastModified();
				long lmKey = key.lastModified();
				// set init time
				if (creationTimeCertificate < 0 || creationTimePrivKey < 0) {
					creationTimeCertificate = lmCert;
					creationTimePrivKey = lmKey;
					return;
				}
				// same time
				if (creationTimeCertificate == lmCert && creationTimePrivKey == lmKey) {
					// do nothing
					return;
				}
				// Load certificate from file
				try {
					Cert cert = Cert.load(certificate);
					PrivKey privKey = PrivKey.load(key);
					KeyPair result = new KeyPair(cert, privKey);
					securityCert = new SecurityCertificateAdministration(cert, privKey);
					// securityCerts.put(null, securityCert);
					creationTimeCertificate = certificate.lastModified();
					creationTimePrivKey = key.lastModified();
					KeyPair[] currentCertificates = application.getApplicationInstanceCertificates();
					for (KeyPair keyPair : currentCertificates) {
						application.removeApplicationInstanceCertificate(keyPair);
					}
					application.addApplicationInstanceCertificate(result);
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		};
	}

	private void initDefault() {
		this.maxSessionCount = new UnsignedInteger(1000);
		this.maxSessionTimeout = 120000;
		this.minSessionTimeout = 5000;
		this.maxRequestAge = new UnsignedInteger(60000);
		this.maxBrowseContinuationPoints = 1000;
		this.maxHistoryContinuationPoints = 1000;
		this.maxRequestedMessageSize = UnsignedInteger.ZERO;
		this.certificateValidity = 3650;
	}

	/**
	 * Sets configuration
	 * 
	 * @param configuration
	 */
	public void setConfiguration(ApplicationConfiguration configuration) {
		try {
			this.certificateValidity = Integer.parseInt(configuration.getCertificateValidity());
		} catch (NumberFormatException nfe) {
		}
		this.maxSessionCount = configuration.getServerConfiguration().getMaxSessionCount();
		this.maxSessionTimeout = configuration.getServerConfiguration().getMaxSessionTimeout();
		this.minSessionTimeout = configuration.getServerConfiguration().getMinSessionTimeout();
		this.maxRequestAge = configuration.getServerConfiguration().getMaxRequestAge();
		this.maxBrowseContinuationPoints = configuration.getServerConfiguration().getMaxBrowseContinuationPoints();
		this.maxHistoryContinuationPoints = configuration.getServerConfiguration().getMaxHistoryContinuationPoints();
		// this.minNonceLength =
		// configuration.getSecurityConfiguration().getMinNonceLength();
		this.maxRequestedMessageSize = configuration.getMaxRequestMessageSize();
	}

	public UnsignedInteger getMaxSessionCount() {
		return maxSessionCount;
	}

	public UnsignedInteger getMaxRequestedMessageSize() {
		return maxRequestedMessageSize;
	}

	public void setMaxRequestedMessageSize(UnsignedInteger maxRequestedMessageSize) {
		this.maxRequestedMessageSize = maxRequestedMessageSize;
	}

	public int getCertificateValidity() {
		return this.certificateValidity;
	}

	public void setCertificateValidity(int certificateValidity) {
		this.certificateValidity = certificateValidity;
	}

	public double getMaxSessionTimeout() {
		return maxSessionTimeout;
	}

	public double getMinSessionTimeout() {
		return minSessionTimeout;
	}

	public UnsignedInteger getMaxRequestAge() {
		return maxRequestAge;
	}

	public int getMaxBrowseContinuationPoints() {
		return maxBrowseContinuationPoints;
	}

	public int getMaxHistoryContinuationPoints() {
		return maxHistoryContinuationPoints;
	}

	public void setMaxSessionCount(UnsignedInteger maxSessionCount) {
		this.maxSessionCount = maxSessionCount;
	}

	public void setMaxSessionTimeout(double maxSessionTimeout) {
		this.maxSessionTimeout = maxSessionTimeout;
	}

	public void setMinSessionTimeout(double minSessionTimeout) {
		this.minSessionTimeout = minSessionTimeout;
	}

	public void setMaxRequestAge(UnsignedInteger maxRequestAge) {
		this.maxRequestAge = maxRequestAge;
	}

	public void setMaxBrowseContinuationPoints(int maxBrowseContinuationPoints) {
		this.maxBrowseContinuationPoints = maxBrowseContinuationPoints;
	}

	public void setMaxHistoryContinuationPoints(int maxHistoryContinuationPoints) {
		this.maxHistoryContinuationPoints = maxHistoryContinuationPoints;
	}

	// public void setMinNonceLength(int minNonceLength) {
	// this.minNonceLength = minNonceLength;
	// }
	public void setSecurityCertificate(SecurityCertificateAdministration securityCert) {
		this.securityCert = securityCert;
	}
	//
	// public void addSecurityCertificate(String host,
	// SecurityCertificateAdministration securityCert)
	// {
	// this.securityCerts.put(host, securityCert);
	// }

	public SecurityCertificateAdministration getSecurityCert() {
		return securityCert;
	}

	// public SecurityCertificateAdministration getSecurityCert(String address)
	// {
	// SecurityCertificateAdministration admin = securityCerts.get(address);
	// if(admin == null && !securityCerts.isEmpty())
	// return securityCerts.get(null);
	// return admin;
	// }
	/**
	 * Watches certificate to be the current!
	 * 
	 * @param threadPool
	 */
	public void startWatchCertificate(ScheduledExecutorService threadPool) {
		threadPool.scheduleAtFixedRate(this.runnable, 0, 60000, TimeUnit.MILLISECONDS);
	}

}
