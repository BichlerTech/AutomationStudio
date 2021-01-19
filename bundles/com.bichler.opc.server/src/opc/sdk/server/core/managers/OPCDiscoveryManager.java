package opc.sdk.server.core.managers;

import java.net.URI;
import java.net.URISyntaxException;
// import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.RegisterServerRequest;
import org.opcfoundation.ua.core.RegisterServerResponse;
import org.opcfoundation.ua.core.RegisteredServer;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.transport.ServerSecureChannel;
import org.opcfoundation.ua.transport.ServiceChannel;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
// import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.EndpointUtil;

import opc.sdk.server.core.OPCInternalServer;

public class OPCDiscoveryManager implements IOPCManager {
	private Map<String, RegisteredServer> discoveryApplications = new HashMap<>();
	private OPCInternalServer server;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
	private Logger logger = Logger.getLogger(OPCDiscoveryManager.class.getName());

	public OPCDiscoveryManager(OPCInternalServer server) {
		this.server = server;
		// this.discoveryApplications.put(server.getApplication().getApplicationDescription().getApplicationUri(),
		// server.getApplication().getApplicationDescription());
	}

	protected ApplicationDescription[] findServers(String endpointUrl, String[] serverUris, String[] localeIds)
			throws ServiceResultException {
		this.lock.readLock().lock();
		try {
			System.out.println();
			// result
			List<ApplicationDescription> results = new ArrayList<>();
			// add registered servers
			for (RegisteredServer server : this.discoveryApplications.values()) {
				if (!server.getIsOnline()) {
					continue;
				}
				String[] urls = server.getDiscoveryUrls();
				if (urls == null || urls.length == 0) {
					continue;
				}
				for (String url : urls) {
					// try {
					// URI uri1 = new URI(url);
					// URI uri2 = new URI(endpointUrl);
					if (endpointUrl != null && url.startsWith(endpointUrl)) {
						ApplicationDescription registeredDescription = getRegisteredServerToApplicationDescription(
								server);
						// filtering serverurls by the client
						if (serverUris != null && serverUris.length > 0) {
							boolean exists = false;
							for (int i = 0; i < serverUris.length; i++) {
								if (serverUris[i] != null
										&& serverUris[i].startsWith(registeredDescription.getApplicationUri())) {
									exists = true;
									break;
								}
							}
							if (!exists) {
								continue;
							}
						}
						results.add(registeredDescription);
					}
					// } catch (URISyntaxException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				}
			}
			// application description
			ApplicationDescription serverDescription = this.server.getApplication().getApplicationDescription();
			// available endpoint urls to connect
			String[] serverUrls = serverDescription.getDiscoveryUrls();
			// filter available urls
			String[] filterUrls = filterByEndpointUrl(endpointUrl, serverUrls);
			// empty endpoint urls
			if (filterUrls.length == 0) {
				// if (serverUris != null && serverUris.length > 0) {
				// boolean exists = false;
				// for (int i = 0; i < serverUris.length; i++) {
				// if (serverUris[i] != null &&
				// serverUris[i].startsWith(serverDescription.getApplicationUri()))
				// {
				// exists = true;
				// break;
				// }
				// }
				// // TODO: Default discovery url
				// if (exists) {
				// results.add(serverDescription);
				// }
				// }
				// results.add(new ApplicationDescription());
				if (results.isEmpty()) {
					results.add(serverDescription);
				}
				return results.toArray(new ApplicationDescription[0]);
			}
			// build result list
			EndpointDescription[] endpoints = this.server.getEndpointDescriptions();
			Set<String> processedUrls = new HashSet<>();
			for (EndpointDescription ep : endpoints) {
				ApplicationDescription epServer = ep.getServer();
				// skip processed servers
				if (processedUrls.contains(epServer.getApplicationUri())) {
					continue;
				}
				// filtering serverurls by the client
				if (serverUris != null && serverUris.length > 0) {
					boolean exists = false;
					for (int i = 0; i < serverUris.length; i++) {
						if (serverUris[i] != null
								&& EndpointUtil.urlEqualsHostIgnoreCase(serverUris[i], epServer.getApplicationUri())) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						continue;
					}
				}
				// locale applicationname
				LocalizedText applicationname = epServer.getApplicationName();
				ApplicationDescription application = translateApplicationDescription(endpointUrl, epServer, filterUrls,
						applicationname);
				results.add(application);
				processedUrls.add(application.getApplicationUri());
			}
			return results.toArray(new ApplicationDescription[0]);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	protected void registerServer(ServerSecureChannel channel, RegisteredServer serverToRegister)
			throws ServiceResultException {
		if (SecurityPolicy.NONE.equals(channel.getSecurityPolicy())) {
			throw new ServiceResultException(StatusCodes.Bad_SecurityPolicyRejected);
		}
		this.lock.writeLock().lock();
		try {
			String serverUri = serverToRegister.getServerUri();
			if (serverUri == null || serverUri.isEmpty()) {
				throw new ServiceResultException(StatusCodes.Bad_ServerUriInvalid);
			}
			LocalizedText[] serverNames = serverToRegister.getServerNames();
			if (serverNames == null || serverNames.length == 0) {
				throw new ServiceResultException(StatusCodes.Bad_ServerNameMissing);
			}
			String[] discoveryUrls = serverToRegister.getDiscoveryUrls();
			if (discoveryUrls == null || discoveryUrls.length == 0) {
				throw new ServiceResultException(StatusCodes.Bad_DiscoveryUrlMissing);
			}
			ApplicationType serverType = serverToRegister.getServerType();
			if (serverType == null || serverType == ApplicationType.Client) {
				throw new ServiceResultException(StatusCodes.Bad_InvalidArgument);
			}
			// try {
			// String remoteUri =
			// CertificateUtils.getApplicationUriOfCertificate(channel.getRemoteCertificate());
			// String localUri = CertificateUtils
			// .getApplicationUriOfCertificate(channel.getLocalCertificate().getCertificate());
			// } catch (CertificateParsingException e) {
			// e.printStackTrace();
			// }
			this.discoveryApplications.put(serverToRegister.getServerUri(), serverToRegister);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	private ApplicationDescription getRegisteredServerToApplicationDescription(RegisteredServer server) {
		LocalizedText defaultServerName = LocalizedText.EMPTY;
		LocalizedText[] serverNames = server.getServerNames();
		if (serverNames != null && serverNames.length > 0) {
			defaultServerName = serverNames[0];
		}
		ApplicationDescription applicationDescription = new ApplicationDescription(server.getServerUri(),
				server.getProductUri(), defaultServerName, server.getServerType(), server.getGatewayServerUri(), null,
				server.getDiscoveryUrls());
		return applicationDescription;
	}

	private String[] filterByEndpointUrl(String endpointUrl, String[] serverUrls) throws ServiceResultException {
		// return empty endpoints
		if (endpointUrl == null) {
			return new String[0];
		}
		List<String> accessible = new ArrayList<>();
		for (String url : serverUrls) {
			try {
				URI uri1 = new URI(endpointUrl);
				URI uri2 = new URI(url);
				if (uri1.equals(uri2)) {
					accessible.add(url);
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return accessible.toArray(new String[0]);
	}

	private ApplicationDescription translateApplicationDescription(String clientUrl, ApplicationDescription description,
			String[] baseAddresses, LocalizedText applicationname) {
		// get discovery urls
		// String[] discoveryUrls = new String[0];
		// TODO: MATCH UP WITH DISCOVERY URLS
		ApplicationDescription copy = new ApplicationDescription();
		copy.setApplicationName(description.getApplicationName());
		copy.setApplicationUri(description.getApplicationUri());
		copy.setApplicationType(description.getApplicationType());
		copy.setProductUri(description.getProductUri());
		copy.setGatewayServerUri(description.getGatewayServerUri());
		copy.setDiscoveryUrls(baseAddresses);
		if (applicationname != null) {
			copy.setApplicationName(applicationname);
		}
		return copy;
	}

	/**
	 * change return value trom void to boolean, to signal success or not
	 * 
	 * @param online
	 * @return
	 */
	boolean registerWithLocalDiscoveryServer(boolean online) {
		boolean success = true;
		ServiceChannel channel = null;
		try {
			Client registerClient = Client
					.createClientApplication(getServer().getApplication().getApplicationInstanceCertificate());
			String url = "opc.tcp://127.0.0.1:4840";
			EndpointDescription[] endpoints = registerClient.discoverEndpoints(url);
			EndpointDescription[] e128 = EndpointUtil.selectBySecurityPolicy(endpoints, SecurityPolicy.BASIC128RSA15);
			EndpointDescription used = EndpointUtil.select(e128);
			channel = registerClient.createServiceChannel(used);
			RegisteredServer registeredServer = new RegisteredServer();
			registeredServer.setDiscoveryUrls(getServer().getSecurityConfigurator().getServerAddresses());
			registeredServer.setGatewayServerUri(
					getServer().getApplication().getApplicationDescription().getGatewayServerUri());
			registeredServer.setIsOnline(online);
			registeredServer.setProductUri(getServer().getApplication().getApplicationDescription().getProductUri());
			registeredServer.setSemaphoreFilePath("");
			registeredServer.setServerNames(new LocalizedText[] {
					getServer().getApplication().getApplicationDescription().getApplicationName() });
			registeredServer
					.setServerType(getServer().getApplication().getApplicationDescription().getApplicationType());
			registeredServer.setServerUri(getServer().getApplication().getApplicationDescription().getApplicationUri());
			RegisterServerRequest req = new RegisterServerRequest(null, registeredServer);
			req.setRequestHeader(new RequestHeader());
			req.getRequestHeader().setTimeoutHint(UnsignedInteger.valueOf(registerClient.getTimeout()));
			RegisterServerResponse res = channel.RegisterServer(req);
			if (res == null) {
				// TODO log message response not OK
			}
		} catch (ServiceResultException e) {
			// TODO verify exception
			success = false;
			this.logger.severe(e.getMessage());
		} finally {
			if (channel != null) {
				channel.close();
				channel.dispose();
			}
		}
		return success;
	}

	@Override
	public boolean start() {
		/**
		 * should we register to discovery server
		 */
		if (server.getApplicationConfiguration().getServerConfiguration().getRegisterLocalDiscovery()) {
			registerWithLocalDiscoveryServer(true);
		}
		this.hasInitialized = true;
		return this.hasInitialized;
	}

	@Override
	public boolean stop() {
		registerWithLocalDiscoveryServer(false);
		this.hasInitialized = false;
		return this.hasInitialized;
	}

	@Override
	public OPCInternalServer getServer() {
		return this.server;
	}

	boolean hasInitialized = false;

	@Override
	public boolean isInitialized() {
		return this.hasInitialized;
	}
}
