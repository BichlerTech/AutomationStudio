package opc.client.application;

/**
 * Static client configuration instance for the certificate store
 * 
 * @author Thomas Zoechbauer, Hannes Bichler
 * @since 23.05.2012, HB-Softsolution e.U., 23.05.2017
 */
public class ClientConfiguration {
	/**
	 * relative path (starting from the project folder) of the certificate store
	 * folder
	 */
	private static String certificatePath = "cert";
	/**
	 * relative path (starting from the project folder) of the client certificate
	 */
	public static final String CERTIFICATE_NAME = "clientcertificate.der";
	/** relative path (starting from the project folder) of the private key */
	public static final String PRIVATE_KEY_NAME = "clientkey.pfx";
	/** Client Configuration Folder */
	public static final String CONFIGURATION_FILE_PATH = "appconfig";
	/** Certificate-store folder */
	public static final String CERTIFICATE_STORE_PATH = "PKI/keystore.properties";
	/** Client Configuration File */
	public static final String CONFIGURATION_FILE = "clientconfig.xml";

	/**
	 * private constructor to hide the default public constructor
	 */
	private ClientConfiguration() {
	}

	public static String getCertificatePath() {
		return certificatePath;
	}

	public static void setCertificatePath(String certificatePath) {
		ClientConfiguration.certificatePath = certificatePath;
	}
}
