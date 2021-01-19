package opc.sdk.server.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Server;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ServerState;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.transport.Endpoint;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicyUri;
import org.opcfoundation.ua.utils.CertificateUtils;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.application.HistoryConfiguration;
import opc.sdk.core.application.SecurityCertificateAdministration;
import opc.sdk.core.application.ServerSecurityPolicy;
import opc.sdk.core.context.StringTable;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.managers.DiagnosticManager;
import opc.sdk.server.core.managers.OPCAddressSpaceManager;
import opc.sdk.server.core.managers.OPCDiscoveryManager;
import opc.sdk.server.core.managers.OPCHistoryManager;
import opc.sdk.server.core.managers.OPCServiceManager;
import opc.sdk.server.core.managers.OPCSessionManager;
import opc.sdk.server.core.managers.OPCSubscriptionManager;
import opc.sdk.server.core.managers.OPCUserAuthentificationManager;
import opc.sdk.server.language.LanguageManager;
import opc.sdk.server.security.CertificatePath;
import opc.sdk.server.security.OPCSecurityConfigurator;
import opc.sdk.server.service.history.HistoryManager;
import opc.sdk.server.service.history.impl.CSVHistoryManager;
import opc.sdk.server.service.history.impl.SimpleSqlHistoryManager;
import opc.sdk.server.service.session.OPCSessionConfigurator;
import opc.sdk.server.service.subscribe.OPCSubscriptionConfiguratior;

public abstract class OPCServer extends Server {
	/** diagnostics manager */
	private DiagnosticManager diagnosticsManager;
	/** security configurator for server instance */
	private OPCSecurityConfigurator securityManager = null;
	/** session configurator for server instance */
	private OPCSessionConfigurator sessionConfigurator = null;
	/** subscription configurator for server instance */
	private OPCSubscriptionConfiguratior subscriptionConfigurator = null;
	/** server uris */
	private StringTable serverUris;
	/** namespace uris */
	private NamespaceTable namespaceUris;
	/** opc ua informatiom type model */
	private TypeTable typeTable;
	/** server state */
	private int state = ServerState.NoConfiguration.getValue();
	/** server default locale */
	private Locale defaultLocale = null;
	private boolean isCertificateStore = false;
	private ApplicationConfiguration configuration;
	private Logger logger = Logger.getLogger(getClass().getName());

	public ApplicationConfiguration getApplicationConfiguration() {
		return this.configuration;
	}

	public OPCServer(Application application) {
		super(application);
		this.defaultLocale = Locale.getDefault();
		/** opc ua server namespace uris */
		this.namespaceUris = new NamespaceTable();
		/** diagnostics manager */
		this.diagnosticsManager = new DiagnosticManager();
		/** server configuration */
		this.securityManager = new OPCSecurityConfigurator(application);
		this.sessionConfigurator = new OPCSessionConfigurator();
		this.subscriptionConfigurator = new OPCSubscriptionConfiguratior();
		this.serverUris = new StringTable();
		// typetable of opc ua informationmodel
		this.typeTable = new TypeTable(this.namespaceUris);
	}

	/**
	 * Instantiate the opc ua certificates
	 * 
	 * @param certificateStorePath
	 */
	public void loadOrCreateServerCertificateStore(String certificateStorePath, boolean useHostName, String commonName,
			String organisation, String... defaultIP) throws IOException {
		File certFile = null;
		File privFile = null;
		String configPath = this.securityManager.getCertStorePath();
		if (configPath.endsWith("certs/")) {
			// skip
		} else if (configPath.endsWith("certs")) {
			configPath += "/";
		} else if (!configPath.endsWith("certs/")) {
			configPath += "certs/";
		}
		String pathCert = "", pathKey = "";
		if (certificateStorePath == null) {
			Files.createDirectories(
					Paths.get(certificateStorePath + "/" + configPath + CertificatePath.publiccert.getPath()));
			Files.createDirectories(
					Paths.get(certificateStorePath + "/" + configPath + CertificatePath.privatekey.getPath()));
			pathCert = configPath + CertificatePath.publiccert.getPath() + this.securityManager.getCertName()
					+ "_cert.crt";
			pathKey = configPath + CertificatePath.privatekey.getPath() + this.securityManager.getCertName()
					+ "_key.pfx";
			certFile = new File(pathCert);
			privFile = new File(pathKey);
		} else {
			Files.createDirectories(
					Paths.get(certificateStorePath + "/" + configPath + CertificatePath.publiccert.getPath()));
			Files.createDirectories(
					Paths.get(certificateStorePath + "/" + configPath + CertificatePath.privatekey.getPath()));
			pathCert = certificateStorePath + "/" + configPath + CertificatePath.publiccert.getPath() + "/"
					+ this.securityManager.getCertName() + "_cert.crt";
			pathKey = certificateStorePath + "/" + configPath + CertificatePath.privatekey.getPath() + "/"
					+ this.securityManager.getCertName() + "_key.pfx";
			certFile = new File(pathCert);
			privFile = new File(pathKey);
		}
		if (!certFile.exists()) {
			try {
				certFile.createNewFile();
			} catch (IOException e) {
				certFile = certFile.getAbsoluteFile();
				try {
					certFile.createNewFile();
				} catch (IOException e1) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		if (!privFile.exists()) {
			try {
				privFile.createNewFile();
			} catch (IOException e) {
				privFile = privFile.getAbsoluteFile();
				try {
					privFile.createNewFile();
				} catch (IOException e1) {
					logger.log(Level.SEVERE, e.getMessage(), e1);
				}
			}
		}
		// try to load certificate information (public certificate & private
		// key)
		KeyPair result = null;
		try {
			// Load certificate from file
			Cert cert = Cert.load(certFile);
			// This may be used to load private key from keystore.
			PrivKey privKey = PrivKey.load(privFile);
			result = new KeyPair(cert, privKey);
			SecurityCertificateAdministration securityCertAdmin = new SecurityCertificateAdministration(cert, privKey);
			this.sessionConfigurator.setSecurityCertificate(securityCertAdmin);
			this.sessionConfigurator.initWatchCertificate(getApplication(), pathCert, pathKey);
		} catch (Exception e1) {
			// could not find the certificate info...create new self-signeg
			// certificate
			try {
				String hostname[] = new String[1];
				if (defaultIP != null && defaultIP.length > 0)
					hostname = defaultIP;
				if (useHostName)
					hostname[0] = InetAddress.getLocalHost().getHostName();
				result = CertificateUtils.createApplicationInstanceCertificate(commonName, organisation,
						getApplication().getApplicationUri(), this.sessionConfigurator.getCertificateValidity(),
						hostname);
				result.save(certFile, privFile);
				SecurityCertificateAdministration securityCertAdmin = new SecurityCertificateAdministration(
						result.getCertificate(), result.getPrivateKey());
				this.sessionConfigurator.setSecurityCertificate(securityCertAdmin);
				this.sessionConfigurator.initWatchCertificate(getApplication(), pathCert, pathKey);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				String hostname = null;
				try {
					hostname = execReadToString("hostname");
					result = CertificateUtils.createApplicationInstanceCertificate(commonName, organisation,
							getApplication().getApplicationUri(), this.sessionConfigurator.getCertificateValidity(),
							hostname);
					result.save(certFile, privFile);
					SecurityCertificateAdministration securityCertAdmin = new SecurityCertificateAdministration(
							result.getCertificate(), result.getPrivateKey());
					this.sessionConfigurator.setSecurityCertificate(securityCertAdmin);
					this.sessionConfigurator.initWatchCertificate(getApplication(), pathCert, pathKey);
				} catch (Exception e2) {
					logger.log(Level.SEVERE, e2.getMessage(), e2);
				}
			}
		}
		if (result != null) {
			this.application.addApplicationInstanceCertificate(result);
		}
	}

	// TODO close stream as expected
	private static String execReadToString(String execCommand) throws IOException {
		Process proc = Runtime.getRuntime().exec(execCommand);
		try (InputStream stream = proc.getInputStream()) {
			try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
				return s.hasNext() ? s.next() : "";
			}
		}
	}

	/**
	 * Used for internal services only.
	 * 
	 * @return
	 */
	public int getState() {
		return this.state;
	}

	public NamespaceTable getNamespaceUris() {
		return this.namespaceUris;
	}

	/**
	 * OPC UA session configuration
	 * 
	 * @return session configuration
	 */
	public OPCSessionConfigurator getSessionConfigurator() {
		return this.sessionConfigurator;
	}

	/**
	 * OPC UA subscription configuration
	 * 
	 * @return subscription configuration
	 */
	public OPCSubscriptionConfiguratior getSubscriptionConfigurator() {
		return this.subscriptionConfigurator;
	}

	public TypeTable getTypeTable() {
		return this.typeTable;
	}

	public StringTable getServerUris() {
		return this.serverUris;
	}

	public abstract OPCUserAuthentificationManager getUserAuthentifiationManager();

	public abstract OPCSessionManager getSessionManager();

	public abstract OPCSubscriptionManager getSubscriptionManager();

	public abstract OPCAddressSpaceManager getAddressSpaceManager();

	public abstract OPCHistoryManager getHistoryManager();

	public abstract OPCServiceManager getServiceManager();

	public abstract OPCDiscoveryManager getDiscoveryManager();

	public abstract LanguageManager getLanguageManager();

	/**
	 * OPC UA application configuration.
	 * 
	 * @param configuration
	 */
	public void setServerConfiguration(ApplicationConfiguration configuration) {
		// server application
		ApplicationDescription serverAppDesc = getApplication().getApplicationDescription();
		// application uri
		String applicationUri = configuration.getApplicationUri();
		int nsAppUri = this.namespaceUris.getIndex(applicationUri);
		// already found
		if (nsAppUri == -1) {
			// remove index
			// this.namespaceUris.remove(1);
			// we do not need the default application uri
			// this.namespaceUris.add(applicationUri);
		}
		// namespace index 1 is application uri
		// this.namespaceUris.add(1, applicationUri);
		// namespace index 2 is diagnostics
		// this.diagnosticsManager.addDiagnosticsNamespace(this.namespaceUris);
		// endpoints for sever
		List<String> endpointUrls = configuration.getServerConfiguration().getBaseAddresses();
		String[] discoveryUrls = resolveEndpointUrls(endpointUrls);
		// fill application description
		serverAppDesc.setApplicationName(new LocalizedText(configuration.getApplicationName(), this.defaultLocale));
		serverAppDesc.setApplicationType(configuration.getApplicationType());
		serverAppDesc.setApplicationUri(applicationUri);
		serverAppDesc.setDiscoveryProfileUri("");
		serverAppDesc.setDiscoveryUrls(discoveryUrls);
		serverAppDesc.setGatewayServerUri("");
		serverAppDesc.setProductUri(configuration.getProductUri());
		List<ServerSecurityPolicy> securityPolicies = new ArrayList<>();
		// ensure at least one security policy exists
		if (configuration.getServerConfiguration().getSecurityPolicies().isEmpty()) {
			securityPolicies.add(new ServerSecurityPolicy());
		} else {
			securityPolicies.addAll(configuration.getServerConfiguration().getSecurityPolicies());
		}
		this.securityManager.setSecurityPolicies(securityPolicies.toArray(new ServerSecurityPolicy[0]));
		this.securityManager.setCertificateName(configuration.getApplicationCertificateKeyName());
		this.securityManager.setInternCertStorePath(configuration.getApplicationCertificateStorePath());
		this.isCertificateStore = configuration.getServerConfiguration().isUseServerCertificateStore();
		addUserTokenToServer(configuration);
		// session configuration
		this.sessionConfigurator.setConfiguration(configuration);
		// subscription configuraton
		this.subscriptionConfigurator.setConfiguration(configuration);
		HistoryConfiguration historyConfig = configuration.getHistoryConfiguration();
		boolean activeDriver = historyConfig.isActive();
		if (activeDriver) {
			HistoryManager hManager = null;
			if (historyConfig.getDrvName().toUpperCase().compareTo("CSV") == 0) {
				hManager = new CSVHistoryManager(historyConfig.getDatabase());
			} else {
				hManager = new SimpleSqlHistoryManager(historyConfig.getDrvName(), historyConfig.getDatabaseName(),
						historyConfig.getDatabase(), historyConfig.getUser(), historyConfig.getPw(),
						historyConfig.isPlainValue());
			}
			hManager.setInternalServer(this);
			this.getHistoryManager().setHistory(hManager);
		}
		this.configuration = configuration;
	}

	public boolean isCertificateStore() {
		return isCertificateStore;
	}

	/**
	 * OPC UA transport and security configuration
	 * 
	 * @return security configuration
	 */
	public OPCSecurityConfigurator getSecurityConfigurator() {
		return this.securityManager;
	}

	protected abstract ScheduledExecutorService getThreadPool();

	/**
	 * Starts server application. (1) bind Endpoints
	 */
	protected void startServer() {
		this.sessionConfigurator.startWatchCertificate(getThreadPool());
		// bind endpoints
		bindEndpoints();
		this.state = ServerState.Running.getValue();
	}

	/**
	 * Starts server application. (1) bind Endpoints, does not set serverstate to
	 * running
	 */
	protected void startServerNoRunning() {
		this.sessionConfigurator.startWatchCertificate(getThreadPool());
		// bind endpoints
		bindEndpoints();
	}

	/**
	 * Stops server application. (1) unbind endpoints
	 */
	protected void stopServer() {
		close();
		this.state = ServerState.Shutdown.getValue();
	}

	private void addUserTokenToServer(ApplicationConfiguration configuration) {
		// usertokens init remove all
		UserTokenPolicy[] serverTokens = getUserTokenPolicies();
		for (UserTokenPolicy p : serverTokens) {
			removeUserTokenPolicy(p);
		}
		// ensure at least one user token policy exists
		if (configuration.getServerConfiguration().getUserTokenPolicies().isEmpty()) {
			UserTokenPolicy userTokenPolicy = new UserTokenPolicy();
			userTokenPolicy.setTokenType(UserTokenType.Anonymous);
			userTokenPolicy.setPolicyId(userTokenPolicy.getTokenType().toString());
			addUserTokenPolicy(userTokenPolicy);
		} else {
			List<UserTokenPolicy> userToken2add = new ArrayList<UserTokenPolicy>();
			// all user token policies
			List<UserTokenPolicy> userTokens = configuration.getServerConfiguration().getUserTokenPolicies();
			// all security policies
			List<ServerSecurityPolicy> policies = configuration.getSecurityPolicy();
			for (ServerSecurityPolicy securityPolicy : policies) {
				SecurityMode securityMode = securityPolicy.getSecurityMode();
				switch (securityMode.getSecurityPolicy().getPolicyUri()) {
				case SecurityPolicyUri.URI_BINARY_NONE:
					for (UserTokenPolicy policy : userTokens) {
						UserTokenType type = policy.getTokenType();
						if (type == UserTokenType.Anonymous && !userToken2add.contains(UserTokenPolicy.ANONYMOUS)) {
							userToken2add.add(UserTokenPolicy.ANONYMOUS);
						} else if (type == UserTokenType.UserName
								&& !userToken2add.contains(UserTokenPolicy.SECURE_USERNAME_PASSWORD)) {
							userToken2add.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD);
						}
					}
					break;
				case SecurityPolicyUri.URI_BINARY_BASIC128RSA15:
					for (UserTokenPolicy policy : userTokens) {
						UserTokenType type = policy.getTokenType();
						if (type == UserTokenType.Anonymous && !userToken2add.contains(UserTokenPolicy.ANONYMOUS)) {
							userToken2add.add(UserTokenPolicy.ANONYMOUS);
						} else if (type == UserTokenType.UserName
								&& !userToken2add.contains(UserTokenPolicy.SECURE_USERNAME_PASSWORD)) {
							userToken2add.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD);
						} else if (type == UserTokenType.Certificate
								&& !userToken2add.contains(UserTokenPolicy.SECURE_CERTIFICATE)) {
							userToken2add.add(UserTokenPolicy.SECURE_CERTIFICATE);
						}
					}
					break;
				case SecurityPolicyUri.URI_BINARY_BASIC256:
					for (UserTokenPolicy policy : userTokens) {
						UserTokenType type = policy.getTokenType();
						if (type == UserTokenType.Anonymous && !userToken2add.contains(UserTokenPolicy.ANONYMOUS)) {
							userToken2add.add(UserTokenPolicy.ANONYMOUS);
						} else if (type == UserTokenType.UserName
								&& !userToken2add.contains(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256);
						} else if (type == UserTokenType.Certificate
								&& !userToken2add.contains(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256);
						}
					}
					break;
				case SecurityPolicyUri.URI_BINARY_BASIC256SHA256:
					for (UserTokenPolicy policy : userTokens) {
						UserTokenType type = policy.getTokenType();
						if (type == UserTokenType.Anonymous && !userToken2add.contains(UserTokenPolicy.ANONYMOUS)) {
							userToken2add.add(UserTokenPolicy.ANONYMOUS);
						} else if (type == UserTokenType.UserName
								&& !userToken2add.contains(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256);
						} else if (type == UserTokenType.Certificate
								&& !userToken2add.contains(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256);
						}
					}
					break;
				case SecurityPolicyUri.URI_BINARY_AES128_SHA256_RSAOAEP:
					for (UserTokenPolicy policy : userTokens) {
						UserTokenType type = policy.getTokenType();
						if (type == UserTokenType.Anonymous && !userToken2add.contains(UserTokenPolicy.ANONYMOUS)) {
							userToken2add.add(UserTokenPolicy.ANONYMOUS);
						} else if (type == UserTokenType.UserName
								&& !userToken2add.contains(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256);
						} else if (type == UserTokenType.Certificate
								&& !userToken2add.contains(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256);
						}
					}
					break;
				case SecurityPolicyUri.URI_BINARY_AES256_SHA256_RSAPSS:
					for (UserTokenPolicy policy : userTokens) {
						UserTokenType type = policy.getTokenType();
						if (type == UserTokenType.Anonymous && !userToken2add.contains(UserTokenPolicy.ANONYMOUS)) {
							userToken2add.add(UserTokenPolicy.ANONYMOUS);
						} else if (type == UserTokenType.UserName
								&& !userToken2add.contains(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256);
						} else if (type == UserTokenType.Certificate
								&& !userToken2add.contains(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256)) {
							userToken2add.add(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256);
						}
					}
					break;
				}
			}
			for (UserTokenPolicy userToken : userToken2add) {
				addUserTokenPolicy(userToken);
			}
		}
	}

	private void bindEndpoints() {
		List<URI> urisToBind = new ArrayList<URI>();
		String[] addresses = this.securityManager.getServerAddresses();
		for (String addr : addresses) {
			// address to uri
			URI endpointUri = null;
			try {
				endpointUri = new URI(addr);
				// add
				urisToBind.add(endpointUri);
			} catch (URISyntaxException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			// get hostname
			String host = "";
			if (endpointUri != null)
				host = endpointUri.getHost();
			// localhost means to bind all available addresses
			if ("LOCALHOST".equalsIgnoreCase(host)) {
				// change <localhost> to <hostname>
				String hostname = null;
				try {
					hostname = InetAddress.getLocalHost().getHostName();
				} catch (UnknownHostException e1) {
					logger.log(Level.SEVERE, e1.getMessage(), e1);
				}
				// replace the hostname
				String replacedAddress = "";
				if (endpointUri != null)
					replacedAddress = endpointUri.toASCIIString().replaceFirst(host, hostname);
				try {
					endpointUri = new URI(replacedAddress);
					// add
					urisToBind.add(endpointUri);
				} catch (URISyntaxException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				// bind network interface addresses
				Enumeration<NetworkInterface> nets = null;
				try {
					// fetch available network interfaces
					nets = NetworkInterface.getNetworkInterfaces();
					for (NetworkInterface netint : Collections.list(nets)) {
						// get the enum of addresses from the network interface
						Enumeration<InetAddress> netInterface = NetworkInterface.getByName(netint.getName())
								.getInetAddresses();
						while (netInterface.hasMoreElements()) {
							InetAddress nextelement = netInterface.nextElement();
							// is ip4 address
							if (nextelement instanceof Inet4Address) {
								// create uri
								String networkAddress = endpointUri.toASCIIString().replaceFirst(endpointUri.getHost(),
										nextelement.getHostAddress());
								try {
									endpointUri = new URI(networkAddress);
									// add
									urisToBind.add(endpointUri);
								} catch (URISyntaxException e) {
									logger.log(Level.SEVERE, e.getMessage(), e);
								}
							}
						}
					}
				} catch (SocketException e1) {
					logger.log(Level.SEVERE, e1.getMessage(), e1);
				}
			}
		}
		// bind endpoints
		for (URI uri : urisToBind) {
			bindEndpoints(uri);
		}
	}

	/**
	 * Binds an endpoint with a given Uri to the server-network-interfaces.
	 * 
	 * @param Uri Uri to bind.
	 */
	private void bindEndpoints(URI endpointUri) {
		ServerSecurityPolicy[] securityPolicies = this.securityManager.getSecurityPolicies();
		// wrap security policie to opc ua
		SecurityMode[] securityModes = new SecurityMode[securityPolicies.length];
		for (int i = 0; i < securityPolicies.length; i++) {
			securityModes[i] = securityPolicies[i].getSecurityMode();
		}
		// create endpoint
		Endpoint endpoint = new Endpoint(endpointUri, securityModes);
		// bind endpoint
		try {
			bind(endpointUri.toString(), endpoint);
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * Resolves endpointurls and set the base server addresses
	 * 
	 * @param endpointUrls
	 * @return mapped discovery urls
	 */
	private String[] resolveEndpointUrls(List<String> endpointUrls) {
		// base addresses
		List<String> baseAddresses = new ArrayList<>();
		List<String> discoveryUris = new ArrayList<>();
		for (String baseAdress : endpointUrls) {
			URI builder = null;
			try {
				builder = new URI(baseAdress);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			baseAddresses.add(baseAdress);
			String compare = "";
			if (builder != null)
				compare = builder.getHost();
			boolean switchHostname = "localhost".equalsIgnoreCase(compare);
			if (switchHostname) {
				String computerName = null;
				try {
					computerName = InetAddress.getLocalHost().getHostName();
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
				baseAdress = baseAdress.replaceFirst(compare, computerName);
				try {
					builder = new URI(baseAdress);
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
			try {
				if (builder != null) {
					boolean switchPath = !(builder.getScheme().startsWith("opc.tcp"));
					if (switchPath) {
						baseAdress = baseAdress.concat("/discovery");
						try {
							builder = new URI(baseAdress);
						} catch (Exception e) {
							logger.log(Level.SEVERE, e.getMessage());
						}
					}
				}
			} catch (Exception npe) {
				logger.log(Level.SEVERE, npe.getMessage());
			}
			if (builder != null)
				discoveryUris.add(builder.toString());
		}
		// sets base addresses for endpoints to bind
		this.securityManager.setServerAddress(baseAddresses.toArray(new String[0]));
		if (discoveryUris.isEmpty())
			return new String[0];
		return discoveryUris.toArray(new String[0]);
	}

	abstract Map<Integer, Integer> doChangeNamespaceTable(NamespaceTable newMappingTable,
			NamespaceTable originMappingTable);

	void setNamespaceTable(NamespaceTable namespaceTable) {
		this.namespaceUris = namespaceTable;
	}
}
